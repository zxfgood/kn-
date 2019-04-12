package com.feeye.handler;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.feeye.util.SendMailUtil;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import com.feeye.entity.AccountInfo;
import com.feeye.entity.OrderInfo;
import com.feeye.init.SysData;
import com.feeye.service.KNAppOutticketService;
import com.feeye.util.InitUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/21 09:19
 */
public class OutticketHandler {

	private static final Logger logger = Logger.getLogger(OutticketHandler.class);

	private static ScheduledExecutorService timerService = Executors.newSingleThreadScheduledExecutor();
//	private static ThreadPoolExecutor taskService = (ThreadPoolExecutor) Executors.newFixedThreadPool(SysData.threadNum);
	// 创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程
	private static ExecutorService outticketService = Executors.newCachedThreadPool();
	// ThreadPoolExecutor内部线程池的形式对外提供管理任务执行，线程调度，线程池管理等等服务，一个航班对应一个线程池
	public static Map<String, ThreadPoolExecutor> taskServiceMap = Maps.newHashMap();
	// long为订单id，刷取的则为true，订单号对应的刷取状态
	public static Map<Long, Boolean> grabState = Maps.newHashMap();

	private  volatile  String createFlag = "sucess";
	// 使用scheduleAtFixedRate()方法实现周期性执行
	public void handleOrder() {
		timerService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				if (!SysData.grabPriceStart) {
					return;
				}
				// 获取抢票订单的信息
				Collection<OrderInfo> values = SysData.grabOrderMap.values();
				List<OrderInfo> orderInfos = Lists.newArrayList();
				if (!values.isEmpty()) {
					for (OrderInfo value : values) {
						OrderInfo clone = value.clone();
						if (clone!=null) {
							orderInfos.add(clone);
						}
					}
				}
				Iterator<OrderInfo> iterator = orderInfos.iterator();
				while (iterator.hasNext()) {
					OrderInfo next = iterator.next();
					// 当抢到价格不再刷票
					if (next.getGrabOver()!=null&&next.getGrabOver()) {
						iterator.remove();
					}
				}
                      // 一个订单号对应多个航班信息
				Map<String, List<String>> flightGrabMap = Maps.newHashMap();
				for (OrderInfo orderInfo : orderInfos) {
					// 航班号 + 出发地 + 到达地 + 出发时间
					String flightKey = orderInfo.getFlightNo()+"_"+orderInfo.getDep()+"_"+orderInfo.getArr()+"_"+orderInfo.getDepTime();
					List<String> flights = flightGrabMap.get(flightKey);
					if (flights==null) {
						flights = Lists.newArrayList();
					}
					// 抢票设置价格 + 订单号
					flights.add(orderInfo.getId()+"-"+orderInfo.getGrabPrice()+"-"+orderInfo.getOrderNo());
					// 一个航班对应的多个订单，key代表航班信息，flight是订单信息
					flightGrabMap.put(flightKey, flights);
				}
				Iterator<String> ite = taskServiceMap.keySet().iterator();
				while (ite.hasNext()) {
					if (flightGrabMap.get(ite.next())==null) {
						ThreadPoolExecutor executor = taskServiceMap.get(ite.next());
						if (executor!=null) {
							executor.shutdownNow();
							ite.remove();
						}
					}
				}
                      // 航班号 + 出发地 + 到达地 + 出发时间
				for (String flightInfo : flightGrabMap.keySet()) {
					ThreadPoolExecutor executor = taskServiceMap.get(flightInfo);
					if (executor==null) {
						// newFixedThreadPool() 创建一个可重用固定线程数的线程池
						executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(SysData.threadNum);
						taskServiceMap.put(flightInfo, executor);
					}
					if (SysData.threadNum!=executor.getCorePoolSize()) {
						// 在没有任务到来之前就创建corePoolSize个线程或者一个线程
						executor.setCorePoolSize(SysData.threadNum);
						executor.setMaximumPoolSize(SysData.threadNum);
					}
					int needExeNum = SysData.threadNum-executor.getActiveCount();
					if (needExeNum<1) {
						return;
					}
					GrabTask grabTask = new GrabTask(flightInfo, flightGrabMap.get(flightInfo));
					// 启动线程开始刷票
					for (int i = 0; i < needExeNum; i++) {
						try {
							Future<String> submit = executor.submit(grabTask);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
		}, 5, 2, TimeUnit.SECONDS);
	}
	/**
	 * @Description:    刷票任务
	 * @Author:         zxf
	 * @Date:     2019/3/29 17:23
	 * @Param
	 * @return
	 */
	public class GrabTask implements Callable<String> {
		private String flightInfo;
		private List<String> priceInfos;

		public GrabTask(String flightInfo, List<String> priceInfos) {
			this.flightInfo = flightInfo;
			this.priceInfos = priceInfos;
		}
		@Override
		public String call() {
			try {
				String[] split = flightInfo.split("_");
				List<String> flightPriceInfo = new KNAppOutticketService().getFlightPriceInfo(split[1], split[2], split[3].substring(0, 10), split[0]);
				String priceInfo = flightPriceInfo.get(0);
				String appPrice = null;
				String depTime = null;
				if (priceInfo.contains("true")) {
					JSONObject json = new JSONObject(priceInfo);
					appPrice = json.getString("strprice").split("-")[0];
					depTime = json.getString("depTime");
				}
				for (String info : priceInfos) {
					try {
						String orderId = info.split("-")[0];
						String grabPrice = info.split("-")[1];
						String orderNo = info.split("-")[2];
						OrderInfo orderInfo = SysData.grabOrderMap.get(orderId);
						// 用户登录
						/*AccountInfo accountInfo = null;
						String airCompany = orderInfo.getFlightNo().substring(0, 2).toUpperCase();
						Collection<AccountInfo> accountInfos = SysData.accountMap.get(airCompany).values();
						if (accountInfos==null||accountInfos.isEmpty()) {
							logger.info(orderInfo.getOrderNo()+"--无对应官网账号--"+airCompany);
							// return "";
							continue;
						}
						for (AccountInfo info2 : accountInfos) {
							if (orderInfo.getAccount().equals(info2.getAccount())) {
								accountInfo = info2;
								continue;
							}
						}
						if (accountInfo==null) {
							logger.info(accountInfo.getAccount()+"--无对应官网账号--"+airCompany);
							continue ;
						}
						String cookie = new KNAppOutticketService().login(accountInfo);
						if (cookie == null) {
							logger.info( "--登录失败");
							continue;
						} else {
							logger.info("登录成功");
						}*/
						///////////////////////////////////////////////
						if (appPrice!=null) {
							logger.info(orderNo + "--抢票价格:"+grabPrice+"--app:"+appPrice);
							// 订单id对应的订单
							// OrderInfo orderInfo = SysData.grabOrderMap.get(orderId);
							if (orderInfo!=null) {
								if (depTime!=null&&checkDepTime(depTime, orderInfo.getDepTime())) {
									orderInfo.setDepTime(depTime);
									String[] flieds = {"depTime"};
									orderInfo.setDepTime(depTime);
									SqliteHander.modifyObjInfo(orderInfo, flieds);
								}
								InitUtil.orderRemind(orderInfo.getId(), "价格刷取", "抢票价格:"+grabPrice+"--app:"+appPrice, false, "OutticketHandler");
								orderInfo.setAppPrice(appPrice);
								orderInfo.setGrabTime(SysData.sdf_datetime.format(new Date()));
								if (Float.parseFloat(appPrice)<=Float.parseFloat(grabPrice)) {
									synchronized (grabState) {
										Boolean state = grabState.get(orderInfo.getId());
										if (state!=null&&state) {
											return "已刷取成功";
										}
										grabState.put(orderInfo.getId(), true);
									}
									orderInfo.setGrabOver(true);
									orderInfo.setOutPrice(appPrice);
									orderInfo.setGrabStatus("刷取成功,正在创单");
									// String content = "刷取成功,正在创单";
									// InitUtil.sendSMS(SysData.phonenum, content);
									String back = flightPriceInfo.get(1);
									// SendMailUtil.sendMail("love", "love");
									// InitUtil.sendSMS(SysData.phonenum, "test");
									Map<String,String> verifyPostParam = Maps.newHashMap();
									try {
										KNAppOutticketService.parseFlightInfo(back, orderInfo, verifyPostParam);
									} catch (Exception e) {
										e.printStackTrace();
									}
									if (verifyPostParam.size()>0) {
										SysData.verifyParamMap.put(orderInfo.getId(), verifyPostParam);
									}
									// 将航班信息放进航班号中
									orderInfo.setBirths(flightInfo);
									// 创单
									outticketService.execute(new OutticketTask(orderInfo));
									InitUtil.orderRemind(orderInfo.getId(), "抢票价格:"+grabPrice+"--app:"+appPrice+",价格刷取成功", "价格刷取成功", true, "OutticketHandler");
									String content = "订单号--"+orderInfo.getOrderNo()+"--抢票价格:"+grabPrice+"--app:"+appPrice+",价格刷取成功";
									InitUtil.sendSMS(SysData.phonenum, content);
									String emailSubject = "订单号--"+orderInfo.getOrderNo() + "抢票结果"+ System.currentTimeMillis();
									SendMailUtil.sendMail(emailSubject, content);
								}
							} else {
								logger.info(orderNo + "--已取消抢票--抢票价格:"+grabPrice+"--app:"+appPrice);
							}
						} else {
							logger.info(orderNo+"--刷取价格失败");
//							InitUtil.operationLog(orderNo, "刷取价格失败", "OutticketHandler");
						}
					} catch (Exception e) {
						logger.error("抓取异常", e);
					}
				}
			} catch (Throwable e) {
				logger.error("抓取之前异常", e);
			} finally {
				try {
					Thread.sleep(SysData.delaySec*1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return "啊哈，跑完啦";
		}
	}

	private boolean checkDepTime(String depTime, String depTime1) {
		if (StringUtil.isEmpty(depTime)||StringUtil.isEmpty(depTime1)) {
			return false;
		}
		if (depTime1.length()>12) {
			return false;
		}
		if (!depTime.contains(depTime1)) {
			return false;
		}
		return true;
	}

	/**
	 * @Description:    出票的线程
	 * @Author:         zxf
	 * @Date:     2019/3/29 17:30
	 * @Param
	 * @return
	 */
	public class OutticketTask implements Runnable{

		private OrderInfo orderInfo;

		public OutticketTask(OrderInfo orderInfo) {
			this.orderInfo = orderInfo;
		}

		@Override
		public void run() {
			AccountInfo accountInfo = null;
			List<String> paxIds = null;
			String airCompany = orderInfo.getFlightNo().substring(0, 2).toUpperCase();
			Collection<AccountInfo> accountInfos = SysData.accountMap.get(airCompany).values();
			if (accountInfos==null||accountInfos.isEmpty()) {
				logger.info(orderInfo.getOrderNo()+"--无对应官网账号--"+airCompany);
				return;
			}
			for (AccountInfo info : accountInfos) {
				if (orderInfo.getAccount().equals(info.getAccount())) {
					accountInfo = info;
					break;
				}
			}
			if (accountInfo==null) {
				logger.info(accountInfo.getAccount()+"--无对应官网账号--"+airCompany);
				return;
			}
			paxIds = SysData.paxIdMap.get(orderInfo.getId()+"-"+accountInfo.getId());
			if ("KN".equals(airCompany)) {
				try {
					new KNAppOutticketService().startCreatOrder(orderInfo, accountInfo, paxIds, 1);
				} catch (Throwable e) {
					logger.error("出票异常", e);
				}
			}
		}
	}
}
