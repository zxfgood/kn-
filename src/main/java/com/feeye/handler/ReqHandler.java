package com.feeye.handler;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feeye.entity.AccountInfo;
import com.feeye.entity.OrderInfo;
import com.feeye.entity.PaxInfo;
import com.feeye.init.SysData;
import com.feeye.util.HttpClientUtil;
import com.feeye.util.InitUtil;
import com.feeye.util.MD5Util;
import com.feeye.util.PropUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/17 14:04
 */
public class ReqHandler {
	private static final Logger logger = Logger.getLogger(ReqHandler.class);

	public static final String GETVERSION = "getVersion";
	public static final String verifyAccount = "verifyAccount";
	public static final String isUserGetApp = "isUserGetApp";
	public static final String queryOrderList = "queryOrderList";
	public static final String queryAccountList = "queryAccountInfos";
	public static final String deleteOrdersInfo = "deleteOrdersInfo";
	public static final String deleteAccountsInfo = "deleteAccountInfo";
	public static final String addOrderInfo = "addOrderInfo";
	public static final String updateOrderInfo = "modifyOrdersInfo";
	public static final String addAccountInfo = "addAccountInfo";
	public static final String updateAccountInfo = "modifyAccountInfo";
	public static final String getProxyUser = "getProxyUser";
	public static final String modifyInfo = "modifyInfo";

	public static String getVersionNo() {
		Map<String, Object> paraMap = Maps.newHashMap();
		paraMap.put("tag", "getVersion");
		String resp = HttpClientUtil.getRespText(paraMap, GETVERSION);
		if (resp!=null) {
			try {
				JSONObject json = JSON.parseObject(resp);
				if ("1001".equals(json.getString("code"))) {
					if (SysData.versionNo.equals(json.getString("versionNo"))) {
						return "true";
					} else {
						return "请下载最新版本";
					}
				} else {
					return json.getString("errMsg");
				}
			} catch (Throwable e) {
				logger.error("版本校验异常", e);
			}
		}
		return "版本校验异常";
	}

	public static String verifyAccount(String feeyeusr, String feeyepwd) {
		Map<String, Object> paraMap = Maps.newHashMap();
		paraMap.put("feeyeusr", feeyeusr);
		paraMap.put("feeyepwd", feeyepwd);
		paraMap.put("versionNo", SysData.versionNo);
		String resp = HttpClientUtil.getRespText(paraMap, verifyAccount);
		if (resp!=null) {
			try {
				JSONObject json = JSON.parseObject(resp);
				if ("1001".equals(json.getString("code"))) {
					if (StringUtil.isEmpty(SysData.loginCookie)) {
						return "登陆未获取到cookie";
					}
					return "true";
				} else {
					return json.getString("errMsg");
				}
			} catch (Throwable e) {
				logger.error("登陆异常", e);
			}
		}
		return "登陆异常";
	}

	public static String isUserGetApp(String feeyeusr) {
		Map<String, Object> paraMap = Maps.newHashMap();
		paraMap.put("feeyeusr", feeyeusr);
		paraMap.put("versionNo", SysData.versionNo);
		String resp = HttpClientUtil.getRespText(paraMap, isUserGetApp);
		if (resp!=null) {
			try {
				JSONObject json = JSON.parseObject(resp);
				if ("1001".equals(json.getString("code"))) {
					return "true";
				} else {
					return json.getString("errMsg");
				}
			} catch (Throwable e) {
				logger.error("权限校验异常", e);
			}
		}
		return "权限校验异常";
	}

	public static String modifyOrderInfo(OrderInfo orderInfo) {
		Map<String, Object> paraMap = Maps.newHashMap();
		paraMap.put("feeyeusr", SysData.feeyeusr);
		paraMap.put("versionNo", SysData.versionNo);
		paraMap.put("id", orderInfo.getId()+"");
		paraMap.put("ticketsInfo", getTicketInfos(orderInfo.getPaxInfos()));
		String resp = HttpClientUtil.getRespText(paraMap, modifyInfo);
		if (resp!=null) {
			try {
				JSONObject json = JSON.parseObject(resp);
				if ("1001".equals(json.getString("code"))) {
					return "true";
				} else {
					return json.getString("errMsg");
				}
			} catch (Throwable e) {
				logger.error("账号添加异常", e);
			}
		}
		return "回填票号异常";
	}

	private static JSONArray getTicketInfos(List<PaxInfo> paxInfos) {
		JSONArray jsonArray = new JSONArray();
		for (PaxInfo paxInfo : paxInfos) {
			JSONObject json = new JSONObject();
			jsonArray.add(json);
			json.put("passengerName", paxInfo.getPaxName());
			json.put("cardId", paxInfo.getCardNo());
			json.put("ticketNo", paxInfo.getTicketNo());
		}
		return jsonArray;
	}
	public static String logOffAccount() {
		Map<String, Object> paraMap = Maps.newHashMap();
		paraMap.put("feeyeusr", SysData.feeyeusr);
		paraMap.put("versionNo", SysData.versionNo);
		String resp = HttpClientUtil.getRespText(paraMap, "logOffAccount");
		if (resp!=null) {
			try {
				JSONObject json = JSON.parseObject(resp);
				if ("1001".equals(json.getString("code"))) {
					return "true";
				} else {
					return json.getString("errMsg");
				}
			} catch (Throwable e) {
				logger.error("账号修改异常", e);
			}
		}
		return "账号修改异常";
	}

//	public static void main(String[] args) {
//		SysData.importurl = "http://113.106.91.189:8066/importorder/ExactOrderServlet.do";
//		SysData.feeyeusr = "qitian";
//		String result = importOrder("qid190226233320784001", "qunaer");
//		System.out.println(result);
//	}
	public static String importOrder(String orderNos, String platform) {
		for (String orderNo : orderNos.split(";")) {
			List<OrderInfo> orderInfos = SqliteHander.queryOrderInfo(null, null, null, null, orderNo, null, null);
			if (orderInfos!=null&&!orderInfos.isEmpty()) {
				return "系统中存在订单:"+orderNo;
			}
		}
		String resp = HttpClientUtil.methodGet(orderNos, platform);
		logger.info("录单--orderNos--"+orderNos+"返回--"+resp);
		if (StringUtil.isEmpty(resp)) {
			return "录入失败";
		}
		JSONObject jsonObj = null;
		try {
			jsonObj = JSON.parseObject(resp);
		} catch (Throwable e) {
			logger.error("error", e);
			return "录入异常";
		}
		if (!"success".equals(jsonObj.getString("result"))) {
			return "录入失败";
		}
		JSONArray orderInfos = jsonObj.getJSONArray("orderInfos");
		if (orderInfos==null||orderInfos.isEmpty()) {
			return "录入失败";
		}
		List<OrderInfo> infos = parseOrderInfos(orderInfos);
		for (OrderInfo info : infos) {
			SqliteHander.addObjInfo(info);
		}
		Set<String> orderNoSet = Sets.newHashSet();
		for (String orderNo : orderNos.split(";")) {
			orderNoSet.add(orderNo);
		}
		Iterator<String> iterator = orderNoSet.iterator();
		while (iterator.hasNext()) {
			String orderNo = iterator.next();
			for (OrderInfo info : infos) {
				if (orderNo.equals(info.getOrderNo())) {
					iterator.remove();
					break;
				}
			}
		}
		if (orderNoSet.isEmpty()) {
			return "true";
		} else {
			StringBuffer sbf = new StringBuffer("订单");
			for (String orderNo : orderNoSet) {
				sbf.append(","+orderNo);
			}
			return sbf.substring(1) + "录入失败";
		}
	}

	private static List<OrderInfo> parseOrderInfos(JSONArray orderInfos) {
		List<OrderInfo> infos = Lists.newArrayList();
		for (int i = 0; i < orderInfos.size(); i++) {
			JSONObject json = orderInfos.getJSONObject(i);
			JSONArray flightInfos = json.getJSONArray("flightInfos");
			if (flightInfos==null||flightInfos.isEmpty()||flightInfos.size()>1) {
				continue;
			}
			OrderInfo orderInfo = new OrderInfo();
			orderInfo.setImportDate(SysData.sdf_datetime.format(new Date()));
			orderInfo.setUsername(SysData.feeyeusr);
			orderInfo.setOrderNo(json.getString("orderNo"));
			orderInfo.setOrderStatus(json.getString("orderStatus"));
			orderInfo.setPlatform(json.getString("platformType"));
			JSONObject flight = flightInfos.getJSONObject(0);
			orderInfo.setArr(flight.getString("arrival"));
			orderInfo.setDep(flight.getString("departure"));
			try {
				String date = flight.getString("departureDate");
				String time = flight.getString("departureDateTime");
				time = time.substring(0, 5)+":00";
				orderInfo.setDepTime(date+" "+time);
			} catch (Exception e) {
				e.printStackTrace();
			}
			orderInfo.setFlightNo(flight.getString("flightNo"));
			JSONArray passengers = json.getJSONArray("passengers");
			List<PaxInfo> paxInfos = Lists.newArrayList();
			orderInfo.setPaxInfos(paxInfos);
			for (int j = 0; j < passengers.size(); j++) {
				JSONObject jsonObject = passengers.getJSONObject(j);
				PaxInfo paxInfo = new PaxInfo();
				paxInfo.setPaxName(jsonObject.getString("passengerName"));
				paxInfo.setPaxType(jsonObject.getString("passengerType"));
				paxInfo.setSellPrice(jsonObject.getString("price"));
				paxInfo.setSex(jsonObject.getString("sex"));
				if (StringUtil.isEmpty(paxInfo.getSex())) {
					paxInfo.setSex("男");
				}
				paxInfo.setTicketNo(jsonObject.getString("ticketNo"));
				try {
					paxInfo.setBirth(jsonObject.getString("birthday").substring(0, 10));
				} catch (Exception e) {
					e.printStackTrace();
				}
				paxInfo.setCardNo(jsonObject.getString("passengerInfo"));
				paxInfo.setCardType(jsonObject.getString("passengercardType"));
				paxInfos.add(paxInfo);
			}
			String orderStatus = orderInfo.getOrderStatus();
			if (StringUtil.isEmpty(orderStatus)||(!"等待出票".equals(orderStatus)&&!"过滤订单".equals(orderStatus))) {
				continue;
			}
			infos.add(orderInfo);
		}
		return infos;
	}

	//	public static List<OrderInfo> getOrderInfos(String pageIndex, String startDate, String endDate, String policyCode, String orderNo, String orderStatus, String airCompany) {
//		Map<String, Object> paraMap = Maps.newHashMap();
//		paraMap.put("feeyeusr", SysData.feeyeusr);
//		paraMap.put("versionNo", SysData.versionNo);
//		paraMap.put("pageSize", "50");
//		paraMap.put("page", pageIndex);
//		paraMap.put("deptDateStart", startDate);
//		paraMap.put("deptDateEnd", endDate);
//		paraMap.put("policyCode", policyCode);
//		paraMap.put("orderNo", orderNo);
//		paraMap.put("orderStatus", orderStatus);
//		paraMap.put("airCompany", airCompany);
//		String resp = HttpClientUtil.getRespText(paraMap, queryOrderList);
//		return parseOrderInfos(resp);
//	}
//
//	public static List<OrderInfo> parseOrderInfos(String resp) {
//		if (StringUtil.isEmpty(resp)) {
//			return null;
//		}
//		List<OrderInfo> orderInfos = null;
//		try {
//			orderInfos = Lists.newArrayList();
//			JSONObject json = JSON.parseObject(resp);
//			if ("1001".equals(json.getString("code"))) {
//				String ordersInfo = json.getString("ordersInfo");
//				String totalCount = json.getString("totalCount");
//				if (StringUtil.isNotEmpty(ordersInfo)) {
//					JSONArray orders = JSON.parseArray(ordersInfo);
//					for (int i = 0; i < orders.size(); i++) {
//
//						JSONObject obj = orders.getJSONObject(i);
//						OrderInfo orderInfo = new OrderInfo();
//						if (totalCount!=null) {
//							orderInfo.setTotalCount(totalCount);
//						}
//						orderInfo.setId(Long.parseLong(obj.getString("id")));
//						orderInfo.setOrderNo(obj.getString("orderNo"));
//						orderInfo.setCookie(obj.getString("cookie"));
//						orderInfo.setLocation(obj.getString("location"));
//						try {
//							orderInfo.setCreatTime(obj.getString("creatTime")!=null?Long.parseLong(obj.getString("creatTime")):null);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//						orderInfo.setOrderStatus(obj.getString("orderStatus"));
//						orderInfo.setImportDate(obj.getString("importDate"));
//						orderInfo.setPlatform(obj.getString("platform"));
//						orderInfo.setAccount(obj.getString("account"));
//						orderInfo.setGrabPrice(obj.getString("grabPrice"));
//						orderInfo.setLocation(obj.getString("alipayUrl"));
//						JSONObject flightObj = JSON.parseArray(obj.getString("flightsInfo")).getJSONObject(0);
//						orderInfo.setFlightNo(flightObj.getString("flightNo"));
//						String depDate = flightObj.getString("depDate");
//						String depDateTime = flightObj.getString("depDateTime");
//						if (StringUtil.isNotEmpty(depDateTime)) {
//							depDate = depDate+" "+depDateTime;
//						}
//						orderInfo.setDepTime(depDate);
//						orderInfo.setDep(flightObj.getString("dep"));
//						orderInfo.setArr(flightObj.getString("arr"));
//
//						JSONArray pasInfos = JSON.parseArray(obj.getString("passengersInfo"));
//						List<PaxInfo> paxInfos = Lists.newArrayList();
//						for (int j = 0; j < pasInfos.size(); j++) {
//							PaxInfo paxInfo = new PaxInfo();
//							JSONObject jsonObj = pasInfos.getJSONObject(j);
//							paxInfo.setPaxName(jsonObj.getString("passengerName"));
//							paxInfo.setTicketNo(jsonObj.getString("ticketNo"));
//							paxInfo.setBirth(jsonObj.getString("birth"));
//							paxInfo.setCardNo(jsonObj.getString("cardId"));
//							paxInfo.setCardType(jsonObj.getString("cardType"));
//							paxInfo.setPaxType(jsonObj.getString("passengerType"));
//							paxInfo.setSellPrice(jsonObj.getString("sellprice"));
//							paxInfo.setSex(jsonObj.getString("gender"));
//							orderInfo.setOutPrice(jsonObj.getString("outPrice"));
//							paxInfos.add(paxInfo);
//						}
//						orderInfo.setPaxInfos(paxInfos);
//						orderInfos.add(orderInfo);
//					}
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return orderInfos;
//	}
//
//	public static String deleteOrderInfo(JSONArray ids, String type) {
//		Map<String, Object> paraMap = Maps.newHashMap();
//		paraMap.put("feeyeusr", SysData.feeyeusr);
//		paraMap.put("versionNo", SysData.versionNo);
//		String tag = null;
//		if ("account".equals(type)) {
//			tag = deleteAccountsInfo;
//			paraMap.put("ids", ids);
//		} else {
//			tag = deleteOrdersInfo;
//			paraMap.put("orderIds", ids);
//		}
//		String resp = HttpClientUtil.getRespText(paraMap, tag);
//		if (resp!=null) {
//			try {
//				JSONObject json = JSON.parseObject(resp);
//				if ("1001".equals(json.getString("code"))) {
//					return "true";
//				} else {
//					return json.getString("errMsg");
//				}
//			} catch (Throwable e) {
//				logger.error("删除异常", e);
//			}
//		}
//		return "删除异常";
//	}
//	public static String addOrderInfo(OrderInfo orderInfo) {
//		Map<String, Object> paraMap = orderToMap(orderInfo);
//		paraMap.put("feeyeusr", SysData.feeyeusr);
//		paraMap.put("versionNo", SysData.versionNo);
//		String resp = HttpClientUtil.getRespText(paraMap, addOrderInfo);
//		if (resp!=null) {
//			try {
//				JSONObject json = JSON.parseObject(resp);
//				if ("1001".equals(json.getString("code"))) {
//					return "true";
//				} else {
//					return json.getString("errMsg");
//				}
//			} catch (Throwable e) {
//				logger.error("订单补录异常", e);
//			}
//		}
//		return "订单补录异常";
//	}
//	private static Map<String, Object> orderToMap(OrderInfo orderInfo) {
//		Map<String, Object> paraMap = Maps.newHashMap();
//		paraMap.put("id", orderInfo.getId()!=null?orderInfo.getId()+"":null);
//		paraMap.put("orderNo", orderInfo.getOrderNo());
//		paraMap.put("platform", orderInfo.getPlatform());
//		paraMap.put("orderStatus", orderInfo.getOrderStatus());
//		paraMap.put("account", orderInfo.getAccount());
//		paraMap.put("grabPrice", orderInfo.getGrabPrice());
////		paraMap.put("outPrice", orderInfo.getOutPrice());
//		paraMap.put("importDate", orderInfo.getImportDate());
////		paraMap.put("isHande", orderInfo.getHande()!=null?orderInfo.getHande()+"":null);
////		paraMap.put("location", orderInfo.getLocation());
////		paraMap.put("cookie", orderInfo.getCookie());
////		paraMap.put("creatTime", orderInfo.getCreatTime()+"");
//
//		JSONArray flightArray = new JSONArray();
//		JSONObject flightObj = new JSONObject();
//		flightObj.put("flightNo", orderInfo.getFlightNo());
//		flightObj.put("depDate", orderInfo.getDepTime().substring(0, 10));
//		flightObj.put("depDateTime", orderInfo.getDepTime().substring(11));
//		flightObj.put("dep", orderInfo.getDep());
//		flightObj.put("arr", orderInfo.getArr());
//		flightArray.add(flightObj);
//		paraMap.put("flightsInfo", flightArray);
//
//		JSONArray paxArray = new JSONArray();
//		for (PaxInfo paxInfo : orderInfo.getPaxInfos()) {
//			JSONObject jsonObj = new JSONObject();
//			jsonObj.put("cardType", paxInfo.getCardType());
//			jsonObj.put("passengerName", paxInfo.getPaxName());
//			jsonObj.put("passengerType", paxInfo.getPaxType());
//			jsonObj.put("cardId", paxInfo.getCardNo());
//			jsonObj.put("sellprice", paxInfo.getSellPrice());
//			jsonObj.put("birth", paxInfo.getBirth());
//			jsonObj.put("ticketNo", paxInfo.getTicketNo());
//			jsonObj.put("gender", paxInfo.getSex());
//			jsonObj.put("outPrice", orderInfo.getOutPrice());
//			paxArray.add(jsonObj);
//		}
//		paraMap.put("passengersInfo", paxArray);
//		return paraMap;
//	}
//	public static String updateOrderInfo(List<OrderInfo> orderInfos) {
////		JSONArray jsonArray = new JSONArray();
////		for (OrderInfo orderInfo : orderInfos) {
////			jsonArray.add(orderToMap(orderInfo));
////		}
//		Map<String, Object> paraMap = orderToMap(orderInfos.get(0));
////		Map<String, Object> paraMap = Maps.newHashMap();
//		paraMap.put("feeyeusr", SysData.feeyeusr);
//		paraMap.put("versionNo", SysData.versionNo);
//		String resp = HttpClientUtil.getRespText(paraMap, updateOrderInfo);
//		if (resp!=null) {
//			try {
//				JSONObject json = JSON.parseObject(resp);
//				if ("1001".equals(json.getString("code"))) {
//					return "true";
//				} else {
//					return json.getString("errMsg");
//				}
//			} catch (Throwable e) {
//				logger.error("订单更改异常", e);
//			}
//		}
//		return "订单更改异常";
//	}
//
//	public static List<AccountInfo> getAccountInfos() {
//		Map<String, Object> paraMap = Maps.newHashMap();
//		paraMap.put("feeyeusr", SysData.feeyeusr);
//		paraMap.put("versionNo", SysData.versionNo);
//		paraMap.put("airCompany", "KN");
//		String resp = HttpClientUtil.getRespText(paraMap, queryAccountList);
//		List<AccountInfo> accountInfos = parseAccountInfos(resp);
//		if (accountInfos!=null) {
//			synchronized (SysData.accountMap) {
//				for (AccountInfo accountInfo : accountInfos) {
//					AccountInfo info = InitUtil.getAccountInfoById(accountInfo.getId());
//					if (info!=null) {
//						accountInfo.setLoginTime(info.getLoginTime());
//						accountInfo.setLoginState(info.getLoginState());
//					}
//				}
//				SysData.accountMap.clear();
//				if (accountInfos!=null&&!accountInfos.isEmpty()) {
//					for (AccountInfo accountInfo : accountInfos) {
//						Map<Long, AccountInfo> infoMap = SysData.accountMap.get(accountInfo.getAirCompany());
//						if (infoMap==null) {
//							infoMap = Maps.newConcurrentMap();
//						}
//						infoMap.put(accountInfo.getId(), accountInfo);
//						SysData.accountMap.put(accountInfo.getAirCompany(), infoMap);
//					}
//				}
//			}
//		}
//		return accountInfos;
//	}
//
//	private static List<AccountInfo> parseAccountInfos(String resp) {
//		List<AccountInfo> accountInfos = null;
//		if (StringUtil.isEmpty(resp)) {
//			return accountInfos;
//		}
//		JSONObject json = JSON.parseObject(resp);
//		if ("1001".equals(json.getString("code"))) {
//			accountInfos = Lists.newArrayList();
//			String accountsInfo = json.getString("accountInfos");
//			if (StringUtil.isNotEmpty(accountsInfo)) {
//				JSONArray orders = JSON.parseArray(accountsInfo);
//				for (int i = 0; i < orders.size(); i++) {
//					JSONObject obj = orders.getJSONObject(i);
//					AccountInfo accountInfo = new AccountInfo();
//					accountInfo.setId(Long.parseLong(obj.getString("id")));
//					accountInfo.setAirCompany(obj.getString("airCompany"));
//					accountInfo.setAccount(obj.getString("account"));
//					accountInfo.setPassword(obj.getString("password"));
//					accountInfo.setContact(obj.getString("contact"));
//					accountInfo.setTelPhone(obj.getString("telphone"));
////					accountInfo.setLoginState(obj.getString("loginState"));
//					accountInfos.add(accountInfo);
//				}
//			}
//		}
//		return accountInfos;
//	}
//
//	public static String addAccountInfo(JSONObject parajson) {
//		Map<String, Object> paraMap = Maps.newHashMap();
//		paraMap.put("feeyeusr", SysData.feeyeusr);
//		paraMap.put("versionNo", SysData.versionNo);
//		paraMap.put("accountInfo", parajson);
//		String resp = HttpClientUtil.getRespText(paraMap, addAccountInfo);
//		if (resp!=null) {
//			try {
//				JSONObject json = JSON.parseObject(resp);
//				if ("1001".equals(json.getString("code"))) {
//					return "true";
//				} else {
//					return json.getString("errMsg");
//				}
//			} catch (Throwable e) {
//				logger.error("账号添加异常", e);
//			}
//		}
//		return "账号添加异常";
//	}
//
	public static void main(String[] args) {
//		SysData.desKey = "44b08086cfd54c77aa39d405f010aa8e";
//		SysData.md5Key = "feeye!@#";
//		SysData.proxyUser = "feeyeapp";
//		SysData.proxyPwd = "feeye789";
//		SysData.abuyunUser = "HL7F5JF125K85K8D";
//		SysData.abuyunPwd = "FC393F432489B2E5";
//		SysData.versionNo = PropUtil.getPropertiesValue("config", "version");
//		SysData.url = PropUtil.getPropertiesValue("config", "grabServiceUrl");
//		SysData.importurl = PropUtil.getPropertiesValue("config", "grabImportOrderUrl");
////		exeRealPath = new File(new File("").getAbsolutePath()).getParent();
//		SysData.exeRealPath = new File("").getAbsolutePath();
//		SysData.feeyeusr = "policytest";
//		SysData.versionNo = "1.0.0";
//		String proxyInfo = getProxyInfo();
//		System.out.println(proxyInfo);
		String getProxyUserpolicytest = MD5Util.getMD5("getProxyUserpolicytest", "GBK");
		System.out.println(getProxyUserpolicytest);
	}
	public static String getProxyInfo() {
		Map<String, Object> paraMap = Maps.newHashMap();
		paraMap.put("feeyeusr", SysData.feeyeusr);
		paraMap.put("proxyUser", MD5Util.getMD5("getProxyUserpolicytest","UTF-8"));
		paraMap.put("proxyPass", MD5Util.getMD5("getProxyUserfeeye0100","UTF-8"));
		paraMap.put("versionNo", SysData.versionNo);
		paraMap.put("proxyType", 1);
		String resp = HttpClientUtil.getRespText(paraMap, getProxyUser);
		if (!StringUtil.isEmpty(resp)) {
			try {
				JSONObject json = JSON.parseObject(resp);
				if ("1001".equals(json.getString("code"))) {
					return json.getString("accountInfo");
				}
			} catch (Throwable e) {
				logger.error("账号添加异常", e);
			}
		}
		return null;
	}

	public static List<String> getProxyIps() {
		List<String> proxyIp = getProxyIp();
		StringBuffer sbf = new StringBuffer();
		for (String ipInfo : proxyIp) {
			String ip = ipInfo.split(",")[0].split(":")[0];
			String port = ipInfo.split(",")[0].split(":")[1];
			sbf.append(","+ip+"-"+port);
		}
		return new ArrayList<>(Arrays.asList(sbf.substring(1).split(",")));
	}

	public static List<String> getProxyIp(){
		logger.info("重新请求获取代理IP!");
		List<String> proxyList = new ArrayList<String>();
		//return proxyList;

		String url = "http://feeyeapp.v4.dailiyun.com/query.txt?key=NP031FF6E7&word=&rand=false&detail=true&count=10";

		CloseableHttpClient client = HttpClients.createDefault();

		String result = "";

		HttpGet get = null;
		CloseableHttpResponse response = null;
		try{
			get = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(4000).setConnectTimeout(4000).build();//设置请求和传输超时时间
			get.setConfig(requestConfig);
			response = client.execute(get);

			result =  EntityUtils.toString(response.getEntity(), "UTF-8");

			if(result!=null&&!"".equals(result.trim())){
				String ips [] = result.split("\r\n");
				for (int i = 0; i < ips.length; i++) {
					Pattern p = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\:\\d{1,8}");
					if (p.matcher(ips[i]).find()) {
						proxyList.add(ips[i]);
					}

				}
			}
		}catch (Exception e) {
			// TODO: handle exception
			logger.error("error", e);
		}finally{
			try{
				if(response !=null){
					response.close();
				}
				if(get !=null){
					get.releaseConnection();
				}
				if(client !=null){
					client.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
				logger.error("error",e);
			}
		}
		return proxyList;

	}


//	public static String updateAccountInfo(AccountInfo accountInfo) {
//		Map<String, Object> paraMap = Maps.newHashMap();
//		JSONObject jsonObject = new JSONObject();
//		jsonObject.put("id", accountInfo.getId()+"");
//		jsonObject.put("account", accountInfo.getAccount());
//		jsonObject.put("password", accountInfo.getPassword());
//		jsonObject.put("contact", accountInfo.getContact());
//		jsonObject.put("telphone", accountInfo.getTelPhone());
//		jsonObject.put("airCompany", accountInfo.getAirCompany());
//		paraMap.put("accountInfo", jsonObject);
//		paraMap.put("feeyeusr", SysData.feeyeusr);
//		paraMap.put("versionNo", SysData.versionNo);
//		String resp = HttpClientUtil.getRespText(paraMap, updateAccountInfo);
//		if (resp!=null) {
//			try {
//				JSONObject json = JSON.parseObject(resp);
//				if ("1001".equals(json.getString("code"))) {
//					return "true";
//				} else {
//					return json.getString("errMsg");
//				}
//			} catch (Throwable e) {
//				logger.error("账号修改异常", e);
//			}
//		}
//		return "账号修改异常";
//	}
//

}
