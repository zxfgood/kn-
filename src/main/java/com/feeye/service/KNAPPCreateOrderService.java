package com.feeye.service;//package com.feeye.service;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.nio.charset.Charset;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import javax.net.ssl.SSLContext;
//import javax.script.Invocable;
//import javax.script.ScriptEngine;
//import javax.script.ScriptEngineManager;
//
//import org.apache.commons.io.IOUtils;
//import org.apache.commons.lang.StringUtils;
//import org.apache.http.Header;
//import org.apache.http.HttpHost;
//import org.apache.http.NameValuePair;
//import org.apache.http.auth.AUTH;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.MalformedChallengeException;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.SSLContextBuilder;
//import org.apache.http.conn.ssl.TrustStrategy;
//import org.apache.http.cookie.Cookie;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.BasicAuthCache;
//import org.apache.http.impl.client.BasicCookieStore;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicHeader;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.apache.log4j.Logger;
//import org.dom4j.DocumentHelper;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//
//import com.feeye.service.official.OfficialMain;
//import com.feeye.util.DateUtil;
//import com.feeye.util.PropertiesUtils;
//import com.feeye.util.StringUtil;
//import com.feeye.util.YunSu;
//
//
//public class KNAPPCreateOrderService {
//	private static final int timeout = 40000;
//	private static final Logger logger = Logger.getLogger(KNAPPCreateOrderService.class);
//	private static Map<String,String> accountMap = new HashMap<String,String>();
//
//	public static void main(String[] args)throws Exception {//15595168130
//		String orderJson =
//				"{\"id\":\"27775145\",\"account\":\"13532989542_feeye123\",\"airline\":\"KN\",\"orderNo\":\"1523502286921\",\"username\":\"policytest\",\"childrenMobile\":\"18617070230\",\"payType\":\"zfb\",\"code\":\"\",\"orderTime\":\"2018-04-12 15:53:39\",\"codePassword\":\"\",\"price\":\"268.0\",\"departure\":\"NAY\",\"arrival\":\"BAV\",\"departureDate\":\"2018-05-24\",\"flightNo\":\"KN2905\",\"cabin\":\"R\",\"mobile\":\"13532989542\",\"creditNo\":\"18713083283\",\"expireMonth\":\"null\",\"expireYear\":\"null\",\"cvv\":\"15817476200\",\"ownername\":\"null\",\"idCardType\":\"null\",\"idCardNo\":\"null\",\"payerMobile\":\"12346579\",\"account_no\":\"null\",\"deduct_third_code\":\"null\",\"linkMan\":\"罗轩\",\"isOutticket\":\"true\",\"ytype\":\"????\",\"passengers\":[{\"passengerName\":\"郑文荻\",\"idcard\":\"140402198809022821\",\"passengerType\":\"成人\",\"passengercardType\":\"身份证\",\"birthday\":\"1988-09-02\",\"passengerSex\":\"null\"},{\"passengerName\":\"樊冰蝶\",\"idcard\":\"654225199206300847\",\"passengerType\":\"成人\",\"passengercardType\":\"身份证\",\"birthday\":\"1992-06-30\",\"passengerSex\":\"null\"},{\"passengerName\":\"WANG/XIAXIA\",\"idcard\":\"G51944503\",\"passengerType\":\"成人\",\"passengercardType\":\"护照\",\"birthday\":\"1979-07-20\",\"passengerSex\":\"null\"}],\"ifUsedCoupon\":false,\"drawerType\":\"GW\",\"qiangpiao\":\"\",\"otheraccount\":\"b_b\"}";
//		KNAPPCreateOrderService kn = new KNAPPCreateOrderService();
//		kn.StartCreateOrder(orderJson, 0, 0);
//	}
//
//	public String StartCreateOrder(String orderJson, int retryCount, int requestType) {
//		if(StringUtils.isEmpty(orderJson)){
//			return "ERROR:数据不完整";
//		}
//		long startTime = new Date().getTime();
//		logger.info("获取到的数据KN:"+orderJson);
//		String cancelUrl = PropertiesUtils.getPropertiesValue("config", "cancelUrl");
//		SSLConnectionSocketFactory sslsf = null;
//		BasicCookieStore cookieStore = new BasicCookieStore();// 一个cookies
//		String cookie = "";
//		try {
//			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
//				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//					return true;
//				}
//			}).build();
//			sslsf = new SSLConnectionSocketFactory(sslContext,
//					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//			// 初始化SSL连接
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		HttpHost proxy = new HttpHost("http-dyn.abuyun.com", 9020, "http");
//		org.apache.http.impl.auth.BasicScheme proxyAuth = new org.apache.http.impl.auth.BasicScheme();
//		BasicAuthCache authCache = new BasicAuthCache();
//		authCache.put(proxy, proxyAuth);
//		CredentialsProvider credsProvider = new BasicCredentialsProvider();
//		String proxyUser = "HL7F5JF125K85K8D";
//		String proxyPass = "FC393F432489B2E5";
//		credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(proxyUser, proxyPass));
//		try {
//			proxyAuth.processChallenge(new BasicHeader(AUTH.PROXY_AUTH, "BASIC realm=default"));
//		} catch (MalformedChallengeException e1) {
//		}
//
//		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout)
//				.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setProxy(proxy)
//				.setExpectContinueEnabled(false).setStaleConnectionCheckEnabled(true).build();
//		HttpClientBuilder builder = null;
//		builder = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCookieStore(cookieStore)
//				.setDefaultRequestConfig(defaultRequestConfig).setDefaultCredentialsProvider(credsProvider);
//		CloseableHttpClient httpclient = builder.build();
//		String childrenUser = "";
//		String order_id = "";
//		Map<String, String> verifyPostParam = new HashMap<String, String>();
//		String billNo = "";
//		try {
//			JSONObject json = new JSONObject(orderJson);
//			String account = json.getString("account");
//			String userAccount[] = account.split("_");
//			childrenUser = json.getString("username");
//			order_id = json.getString("id");
//			String linkMan = json.getString("linkMan");
//			String mobile = json.getString("mobile");
//			String payType = "";
//			try {
//				payType = json.getString("payType");
//			} catch (Exception e) {}
//			String payTypeCn = "易宝";
//			if(StringUtils.isNotEmpty(payType)&&"zfb".equals(payType)){
//				payTypeCn = "支付宝";
//			}else if(StringUtils.isNotEmpty(payType)&&"xyk".equals(payType)){
//				payTypeCn = "易宝信用卡";
//			}else if(StringUtils.isNotEmpty(payType)&&"ybhy".equals(payType)){
//				payTypeCn = "易宝会员";
//			}
//			JSONArray passengers = json.getJSONArray("passengers");
//			try {
//				billNo = json.getString("billNo");
//			} catch (Exception e) {}
//			String newOrderNo = "";
//			try {
//				newOrderNo = json.getString("newOrderNo");
//			} catch (Exception e) {}
//			//开始查询航班
//			String back = "";
//			int index=0;
//			try {
//				if(requestType == 1){
//					//手动获取票号的，只做回填
//					int count = 40;
//					back = "";
//					while (count > 0) {
//						sendOrderStatus(childrenUser, order_id, "获取票号信息");
//						try {
//							cookie = accountMap.get(userAccount[0]+userAccount[1]);
//							if(StringUtils.isEmpty(cookie)){
//								//登录后查询
//								for(int loginIndex = 0 ;loginIndex< 5;loginIndex++){
//									cookie = getCookie(httpclient,"",json,defaultRequestConfig);
//									if(!cookie.contains("登陆失败")&&!cookie.contains("请填写正确的云速账号")){
//										accountMap.put(userAccount[0]+userAccount[1], cookie);
//										break;
//									}
//									if(cookie.contains("请填写正确的云速账号")){
//										sendCreateOrderInfo("error", "请填写正确的云速账号", "", childrenUser, newOrderNo, order_id, "", "", null, "", "",
//												"false", "true",billNo,"",requestType);
//										return "SUCCESS#@@#请填写正确的云速账号";
//									}
//									Thread.sleep(500);
//								}
//							}
//							back = getDetail(httpclient, defaultRequestConfig, cookie, newOrderNo, orderJson);
//							if("重新登录".equals(back)){
//								accountMap.remove(userAccount[0]+userAccount[1]);
//								back = "";
//							}
//						} catch (Exception e) {
//							logger.error("error",e);
//							logger.info(order_id+"获取票号信息异常");
//							Thread.sleep(20*1000);
//							--count;
//							continue;
//						}
//						//返回的结果格式为 乘机人1##证件号码##票号##BANKNO#_#乘机人2##证件号码##票号##BANKNO#_#@_@流水号
//						if(StringUtils.isNotEmpty(back)){
//							back = back.replaceAll("BANKNO", "");
//							String ticketnos[] = back.split("@_@");
//							if (ticketnos.length == 2) {
//								sendOrderStatus(childrenUser, order_id, "回填票号");
//								// 判断获取的票号数是否与乘客数量相符合,不相符合重新
//								String[] ticketCount = ticketnos[0].split("#_#");
//								if (ticketCount.length == passengers.length() && !ticketnos[0].contains("null")) {
//									sendCreateOrderInfo("success", "", "" , childrenUser, newOrderNo, order_id, "",
//											"true", ticketnos[0], "易宝", "", "false", "true",billNo,"",requestType);
//									return "SUCCESS";
//								}
//							}
//						}
//						try {
//							Thread.sleep(20*1000);
//						} catch (InterruptedException e) {
//							logger.error("error", e);
//						}
//						--count;
//					}
//					if (count <= 0) {
//						sendCreateOrderInfo("error", "未找到票号信息", "", childrenUser, newOrderNo, order_id, "", "", null, "", "",
//								"false", "true",billNo,"",requestType);
//						return "SUCCESS#@@#未找到票号信息";
//					}
//				}
//			} catch (Exception e) {
//				logger.error("error",e);
//				sendCreateOrderInfo("error", "获取票号异常", "", childrenUser, newOrderNo, order_id, "", "", null, "", "",
//						"false", "true",billNo,"",requestType);
//				return "error:获取票号异常";
//			}
//			Map<String,String> resultMap = null;
//			for(;index<3;index++){
//				sendOrderStatus(childrenUser, order_id, "开始查询航班");
//				resultMap = flightSearch(defaultRequestConfig,orderJson,httpclient,cookieStore);
//				if(resultMap == null || resultMap.size()==0){
//					if(retryCount<9){
//						sendOrderStatus(childrenUser, order_id, "查询不到航班--重试创单");
//						return StartCreateOrder(orderJson, ++retryCount, requestType);
//					}else {
//						sendCreateOrderInfo("error", "查询不到航班", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//						return "error:查询不到航班";
//					}
//
//				}
//				cookie = resultMap.get("cookie");
//				back = resultMap.get("result");
//				logger.info(order_id+",查询航班返回结果:"+back);
//				if(StringUtils.isNotEmpty(back)&&!back.contains("503 Service")){
//					break;
//				}
//			}
//			if(index==3){
//				if(retryCount<9){
//					sendOrderStatus(childrenUser, order_id, "查询航班失败");
//					logger.info(order_id+"--重试创单");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}else {
//					sendCreateOrderInfo("error", "查询航班失败", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					return "error:查询航班失败";
//				}
//
//			}
//			//开始解析航班数据
//			sendOrderStatus(childrenUser, order_id, "解析航班数据");
//			try {
//				parseFlightInfo(back,orderJson,verifyPostParam);
//			} catch (Exception e) {
//				if(retryCount<9){
//					sendOrderStatus(childrenUser, order_id, "返回结果异常，正在重试创单");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}else {
//					logger.error("error",e);
//					sendCreateOrderInfo("error", "解析航班数据异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					return "error:解析航班数据异常";
//				}
//			}
//			if ("cancel".equals(cancel(cancelUrl, order_id, childrenUser))) {
//				sendOrderStatus(childrenUser, order_id, "已取消出票");
//				sendCreateOrderInfo("error", "已取消出票", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				logger.info(order_id+"已取消出票");
//				return "取消出票";
//			}
//			if(verifyPostParam==null||verifyPostParam.size()==0){
//				if(retryCount<9){
//					sendOrderStatus(childrenUser, order_id, "无符合条件航班，正在重试创单");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}else {
//					sendOrderStatus(childrenUser, order_id, "无符合条件航班");
//					sendCreateOrderInfo("error", "无符合条件航班", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					logger.info(order_id+"无符合条件航班");
//					return "error:无符合条件航班";
//				}
//			}
//			//登录
//			sendOrderStatus(childrenUser, order_id, "开始登陆");
//			try {
//				for(int i=0;i<5;i++){
//					String mapCookie = accountMap.get(userAccount[0]+userAccount[1]);
//					if(StringUtils.isNotEmpty(mapCookie)&&mapCookie.contains("tokenId")){
//						cookie = mapCookie;
//						break;
//					}
//					cookie = getCookie(httpclient,cookie,json,defaultRequestConfig);
//					if(StringUtils.isNotEmpty(cookie)&&cookie.contains("用户或密码不正确")){
//						break;
//					}
//					if(StringUtils.isNotEmpty(cookie)&&cookie.contains("密码错误")){
//						break;
//					}
//					if(StringUtils.isNotEmpty(cookie)&&cookie.contains("密码连续错误")){
//						break;
//					}
//					if(StringUtils.isNotEmpty(cookie)&&!cookie.contains("登录失败")){
//						break;
//					}
//				}
//			} catch (Exception e) {
//				if(retryCount<9){
//					sendOrderStatus(childrenUser, order_id, "登录异常，重新登录");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}else {
//					logger.error("error",e);
//					sendCreateOrderInfo("error", "登录异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					return "error:登录异常";
//				}
//
//			}
//			if(StringUtils.isEmpty(cookie)){
//				sendCreateOrderInfo("error", "登录失败", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "登录失败";
//			}
//			if(cookie.contains("用户或密码不正确")){
//				sendCreateOrderInfo("error", "用户或密码不正确", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "用户或密码不正确";
//			}
//			if(cookie.contains("密码错误")){
//				sendCreateOrderInfo("error", "请配置正确的验证码账号", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "请配置正确的验证码账号";
//			}
//			if(cookie.contains("密码连续错误")){
//				sendCreateOrderInfo("error", "验证码账号或密码连续错误，请稍后再试", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "验证码账号或密码连续错误，请稍后再试";
//			}
//			if(cookie.contains("请填写正确的云速账号")){
//				sendCreateOrderInfo("error", "请填写正确的云速账号", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "请填写正确的云速账号";
//			}
//			if ("cancel".equals(cancel(cancelUrl, order_id, childrenUser))) {
//				sendOrderStatus(childrenUser, order_id, "已取消出票");
//				sendCreateOrderInfo("error", "已取消出票", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "取消出票";
//			}
//			if(verifyPostParam.get("shoppingKey")==null||StringUtils.isEmpty(verifyPostParam.get("shoppingKey"))){
//				sendCreateOrderInfo("error", "未找到对应价格", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "未找到对应价格";
//			}
//			//选择航班
//			accountMap.put(userAccount[0]+userAccount[1], cookie);
//			sendOrderStatus(childrenUser, order_id, "选择航班");
//			try {
//				for(int i=0;i<3;i++){
//					resultMap = selectFlight(verifyPostParam,orderJson,cookie,httpclient,defaultRequestConfig);
//					if(resultMap==null||resultMap.size()==0){
//						continue;
//					}
//					String error = resultMap.get("error");
//					if(resultMap!=null&&StringUtils.isEmpty(error)){
//						break;
//					}
//					if("请先登录".equals(error)){
//						accountMap.remove(userAccount[0]+userAccount[1]);
//						if(retryCount<9){
//							sendOrderStatus(childrenUser, order_id, "登录失效，重新登录");
//							return StartCreateOrder(orderJson, retryCount, requestType);
//						}
//					}
//				}
//			} catch (Exception e) {
//				if(retryCount<9){
//					sendOrderStatus(childrenUser, order_id, "返回结果异常，正在重试创单");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}else {
//					sendCreateOrderInfo("error", "选择航班异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					logger.error("error",e);
//					return "error:选择航班异常";
//				}
//			}
//			if(resultMap==null||resultMap.size()==0){
//				if(retryCount<9){
//					sendOrderStatus(childrenUser, order_id, "选择航班异常，重新创单");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}else {
//					sendCreateOrderInfo("error", "选择航班异常，重新创单", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					return "error:选择航班异常";
//				}
//			}
//			if(resultMap.get("error")!=null){
//				if(retryCount<9){
//					sendOrderStatus(childrenUser, order_id, "返回503结果异常，正在重试创单");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}else {
//					sendCreateOrderInfo("error", "选择航班异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					return "error:选择航班异常";
//				}
//			}
//			if ("cancel".equals(cancel(cancelUrl, order_id, childrenUser))) {
//				sendOrderStatus(childrenUser, order_id, "已取消出票");
//				sendCreateOrderInfo("error", "已取消出票", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "取消出票";
//			}
//			String random = (int) ((Math.random() + 1) * 1000000) + "";
//			String memberId = "";
//			try {
//				memberId = getMemberId(httpclient, cookie, orderJson, defaultRequestConfig,order_id);
//				if("用户或密码不正确".equals(memberId)){
//					accountMap.remove(userAccount[0]+userAccount[1]);
//					if(retryCount<9){
//						sendOrderStatus(childrenUser, order_id, "登录失效，重新登录");
//						return StartCreateOrder(orderJson, retryCount, requestType);
//					}
//				}
//			} catch (Exception e) {
//				if(retryCount<9){
//					sendOrderStatus(childrenUser, order_id, "获取账号id异常，正在重试创单");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}else {
//					sendCreateOrderInfo("error", "获取账号id异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					logger.error("error",e);
//					return "error:获取账号id异常";
//				}
//			}
//			if(StringUtils.isEmpty(memberId)){
//				sendCreateOrderInfo("error", "获取账号id异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:获取账号id异常";
//			}
//			//新增乘客
//			sendOrderStatus(childrenUser, order_id, "添加乘客");
//			List<String> passengerId = new ArrayList<String>();
//			try {
//				passengerId = getSamePassengerList(httpclient, cookie, random, orderJson, defaultRequestConfig, memberId);
//				if(passengerId!=null&&passengerId.size()!=0&&"please login first".equals(passengerId.get(0))){
//					accountMap.remove(userAccount[0] + userAccount[1]);
//					if (retryCount < 9) {
//						sendOrderStatus(childrenUser, order_id, "登录失效，重新登录");
//						return StartCreateOrder(orderJson, retryCount, requestType);
//					}
//				}
//				if(passengerId.size()!=passengers.length()){
//					back = addPassengers(orderJson,httpclient,cookie,defaultRequestConfig,passengerId);
//				}
//			} catch (Exception e) {
//				if(retryCount<9){
//					sendOrderStatus(childrenUser, order_id, "返回结果异常，正在重试创单");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}else {
//					sendCreateOrderInfo("error", "添加乘客异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					logger.error("error",e);
//					return "error:添加乘客异常";
//				}
//			}
//			if ("cancel".equals(cancel(cancelUrl, order_id, childrenUser))) {
//				sendOrderStatus(childrenUser, order_id, "已取消出票");
//				sendCreateOrderInfo("error", "已取消出票", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "取消出票";
//			}
//			if(StringUtils.isNotEmpty(back)&&(back.contains("404错误")||back.contains("出错啦")||back.contains("您要查看的页面不存在或已删除"))){
//				if(retryCount<9){
//					sendOrderStatus(childrenUser, order_id, "返回结果异常，正在重试创单");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}else {
//					sendCreateOrderInfo("error", "创单失败", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					return "ERROR:创单失败";
//				}
//			}
//			if(back.contains("error:添加乘客")){
//				sendCreateOrderInfo("error", back.split("\\:")[1], "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "ERROR:"+back.split("\\:")[1];
//			}
//			if(passengerId.size()!=passengers.length()){
//				if(retryCount<9){
//					sendOrderStatus(childrenUser, order_id, "提交乘客数量有误，重新创单");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}else {
//					sendCreateOrderInfo("error", "提交乘客数量有误", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					return "error:提交乘客数量有误";
//				}
//			}
//			defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout)
//					.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
//					.setExpectContinueEnabled(false).setStaleConnectionCheckEnabled(true).build();
//			//获取提交订单的加密参数
//			sendOrderStatus(childrenUser, order_id, "获取增值服务");
//			try {
//				for(int i=0;i<3;i++){
//					resultMap = ancilSearch(cookie,defaultRequestConfig,httpclient,resultMap,orderJson);
//					if(resultMap!=null&&resultMap.get("error")==null){
//						break;
//					}
//				}
//			} catch (Exception e) {
//				if(retryCount<9){
//					sendOrderStatus(childrenUser, order_id, "返回结果异常，正在重试创单");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}else {
//					sendCreateOrderInfo("error", "获取增值服务异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					logger.error("error",e);
//					return "error:获取增值服务异常";
//				}
//			}
//			if(resultMap==null||resultMap.size()==0){
//				sendCreateOrderInfo("error", "获取增值服务失败", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:获取增值服务失败";
//			}
//			if ("cancel".equals(cancel(cancelUrl, order_id, childrenUser))) {
//				sendOrderStatus(childrenUser, order_id, "已取消出票");
//				sendCreateOrderInfo("error", "已取消出票", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "取消出票";
//			}
//			//获取联系人Id
//			sendOrderStatus(childrenUser, order_id, "获取联系人Id");
//			defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout)
//					.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setProxy(proxy)
//					.setExpectContinueEnabled(false).setStaleConnectionCheckEnabled(true).build();
//			try {
//				back = getContactId(cookie,defaultRequestConfig,httpclient,orderJson,memberId);
//			} catch (Exception e) {
//				if(retryCount<9){
//					sendOrderStatus(childrenUser, order_id, "返回结果异常，正在重试创单");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}else {
//					sendCreateOrderInfo("error", "获取联系人Id异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					logger.error("error",e);
//					return "error:获取联系人Id异常";
//				}
//			}
//			if(StringUtils.isEmpty(back)){
//				sendCreateOrderInfo("error", "获取联系人Id失败", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:获取联系人Id失败";
//			}
//			if(back.contains("获取不到联系人id")){
//				Map<String,String> map = null;
//				try {
//					map = getDefaultContacter(cookie, defaultRequestConfig, httpclient, memberId,order_id);
//				} catch (Exception e) {
//					if(retryCount<9){
//						sendOrderStatus(childrenUser, order_id, "获取默认联系人Id异常");
//						return StartCreateOrder(orderJson, ++retryCount, requestType);
//					}else {
//						sendCreateOrderInfo("error", "获取默认联系人Id异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//						return "error:获取联系人Id异常";
//					}
//				}
//				if(map==null||map.size()==0){
//					sendCreateOrderInfo("error", "获取默认联系人Id失败", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					return "error:获取联系人Id失败";
//				}else {
//					String error = map.get("error");
//					if("please login first".equals(error)){
//						accountMap.remove(userAccount[0]+userAccount[1]);
//						if(retryCount<9){
//							sendOrderStatus(childrenUser, order_id, "登录失效，重新登录");
//							return StartCreateOrder(orderJson, retryCount, requestType);
//						}
//					}
//					linkMan = map.get("name");
//					mobile = map.get("mobile");
//					back = map.get("id");
//				}
//			}else if(back.contains("ERROR:")){
//				String errorMsg = back.split("\\:")[1];
//				sendCreateOrderInfo("error", errorMsg, "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:"+errorMsg;
//			}
//			if(StringUtils.isEmpty(back)){
//				sendCreateOrderInfo("error", "获取默认联系人Id失败", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:获取默认联系人Id失败";
//			}
//			//提交订单
//			defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout)
//					.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
//					.setExpectContinueEnabled(false).setStaleConnectionCheckEnabled(true).build();
//			sendOrderStatus(childrenUser, order_id, "提交订单信息");
//			try {
//				back = orderConfirm(cookie, defaultRequestConfig, httpclient, resultMap, orderJson, back, passengerId,
//						linkMan, mobile);
//				if (StringUtils.isEmpty(back)) {
//					if (StringUtils.isEmpty(back)) {
//						sendCreateOrderInfo("error", "提交订单信息失败", "", childrenUser, "", order_id, "", "", null, "", "",
//								"false", "true", billNo, "", requestType);
//						return "error:提交订单信息失败";
//					}
//				} else if (back.contains("会话已超时")) {
//					accountMap.remove(userAccount[0]+userAccount[1]);
//					if (retryCount < 9) {
//						sendOrderStatus(childrenUser, order_id, "返回结果异常，正在重试创单");
//						return StartCreateOrder(orderJson, ++retryCount, requestType);
//					}
//				}
//			} catch (Exception e) {
//				sendCreateOrderInfo("error", "提交订单信息异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				logger.error("error",e);
//				return "error:提交订单信息异常";
//			}
//			if(StringUtils.isEmpty(back)){
//				sendCreateOrderInfo("error", "提交订单信息失败", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:提交订单信息失败";
//			}
//			sendOrderStatus(childrenUser, order_id, "选择支付方式");
//			resultMap.clear();
//			try {
//				resultMap = selectPayMethod(back,payType);
//			} catch (Exception e) {
//				if (retryCount < 9) {
//					sendOrderStatus(childrenUser, order_id, "返回结果异常，正在重试创单");
//					return StartCreateOrder(orderJson, ++retryCount, requestType);
//				}
//				sendCreateOrderInfo("error", "选择支付方式异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				logger.error("error",e);
//				return "error:选择支付方式异常";
//			}
//			if(resultMap==null||resultMap.size()==0){
//				sendCreateOrderInfo("error", "选择支付方式失败", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:选择支付方式失败";
//			}
//			String paymentType = resultMap.get("paymentType");
//			String paymentCode = resultMap.get("paymentCode");
//			if ("cancel".equals(cancel(cancelUrl, order_id, childrenUser))) {
//				sendOrderStatus(childrenUser, order_id, "已取消出票");
//				sendCreateOrderInfo("error", "已取消出票", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "取消出票";
//			}
//			//获取订单信息
//			sendOrderStatus(childrenUser, order_id, "开始创建订单");
//			int i = 0;
//			for(;i<3;i++){
//				try {
//					back = createOrder(httpclient,defaultRequestConfig,cookie);
//				} catch (Exception e) {
//					sendOrderStatus(childrenUser, order_id, "请求创建订单异常,重试创单");
//					logger.error("error",e);
//				}
//				if(StringUtils.isEmpty(back)){
//					sendOrderStatus(childrenUser, order_id, "请求创建订单未返回结果,重试创单");
//				}
//				if(StringUtils.isNotEmpty(back)&&!back.contains("503 Service")){
//					break;
//				}
//				sendOrderStatus(childrenUser, order_id, "请求结果异常,重试创单");
//			}
//			if(i==3){
//				sendCreateOrderInfo("error", "创单失败", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:创单失败";
//			}
//			JSONObject createOrderObj = new JSONObject(back);
//			String orderStatus = "";
//			try {
//				orderStatus = createOrderObj.getString("orderStatus");
//			} catch (Exception e) {
//				logger.error("createOrderResultError"+e);/**/
//				logger.info(order_id+"createOrderResultError:"+back);
//				String errorMsg = getErrorMsg(createOrderObj);
//				sendCreateOrderInfo("error", errorMsg, "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:"+errorMsg;
//			}
//			resultMap.put("orderStatus", orderStatus);
//
//			if ("cancel".equals(cancel(cancelUrl, order_id, childrenUser))) {
//				sendOrderStatus(childrenUser, order_id, "已取消出票");
//				sendCreateOrderInfo("error", "已取消出票", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "取消出票";
//			}
//
//			String orderNo = "";
//			String amount = "";
//			if(StringUtils.isNotEmpty(orderStatus)&&"BOOKED".equalsIgnoreCase(orderStatus)){
//				orderNo = createOrderObj.getString("orderNo");
//				try {
//					JSONObject orderPrice = createOrderObj.getJSONObject("orderPrice");
//					amount = orderPrice.getString("amount");
//				} catch (Exception e) {}
//				logger.info(order_id+"--orderNo:"+orderNo +"--amount:"+amount);
//			}
//			if(DateUtil.IsRunningTimeOut(startTime,7*60*1000)){
//				sendOrderStatus(childrenUser, order_id, "创单超时，停止创单");
//				sendCreateOrderInfo("error", "创单超时，停止创单", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "创单超时，停止创单";
//			}
//			/*
//			 * String paymentType = "";
//			String paymentCode = "";
//			 */
//			//获取平台订单号
//			sendOrderStatus(childrenUser, order_id, "生成订单");
//			try {
//				back = payment(defaultRequestConfig,httpclient,cookie,paymentType,paymentCode,orderNo);
//			} catch (Exception e) {
//				logger.error("createOrderResultError"+e);
//				sendCreateOrderInfo("error", "请求生成订单异常，请到官网确认是否已经生成订单", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:请求生成订单异常，请到官网确认是否已经生成订单";
//			}
//			if(StringUtils.isEmpty(back)){
//				sendCreateOrderInfo("error", "请求生成订单未返回结果，请到官网确认是否已经生成订单", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:请求生成订单未返回结果，请到官网确认是否已经生成订单";
//			}
//			JSONObject paymentObj = new JSONObject(back);
//			String transactionNumber = ""; //平台订单号
//			String url = "";
//			boolean flag = false;
//			try {
//				url = paymentObj.getString("url");
//				// 生成订单后第一个请求
//				String[] str = url.split("&");
//				for (String s : str) {
//					if (s.contains("transactionNumber")) {
//						transactionNumber = s.split("=")[1];
//						logger.info("平台订单号:" + transactionNumber);
//						sendCreateOrderInfo("success", "", amount, childrenUser, transactionNumber, order_id, "", "", null, payTypeCn,"","false","true",billNo,"",requestType);
//						flag = true;
//					}
//				}
//			} catch (Exception e) {
//				logger.info(order_id + "生单后返回Error:"+back);
//				String errorMsg = getErrorMsg(createOrderObj);
//				sendCreateOrderInfo("error", errorMsg, "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:"+errorMsg;
//			}
//			logger.info(order_id+"第一个请求的url:"+url);
//			if(url == null || StringUtil.isEmpty(url)){
//				sendOrderStatus(childrenUser, order_id, "未获取到支付请求");
//				sendCreateOrderInfo("error", "未获取到支付请求", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:未获取到支付请求";
//			}
//			//已经获取到订单号，开始进行支付
//			sendOrderStatus(childrenUser, order_id, "进行支付");
//			if(flag){
//				resultMap.clear();
//				if(StringUtil.isNotEmpty(payType)&&"zfb".equals(payType)){
//					OfficialZfbPayService payService = new OfficialZfbPayService();
//					resultMap = payService.aliPayM(url,orderJson,cookie);
//					if(resultMap==null||resultMap.size()==0){
//						sendCreateOrderInfo("error", "支付失败", "", childrenUser, transactionNumber, order_id, "", "", null, "","","false","true",billNo,"",requestType);
//						return "error:支付失败";
//					}else {
//						String location = resultMap.get("locationValue");
//						sendOrderStatus(childrenUser, order_id, "跳转支付中");
//						sendAliPayUrl(childrenUser,order_id,location,cookie);
//						return "success";
//					}
//				}else{
//					OfficialXykPayService payService = new OfficialXykPayService();
//					resultMap = payService.yeePayM(url,orderJson,cookie);
//				}
//			}else {
//				sendCreateOrderInfo("error", "未获取到平台订单号", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//			}
//			if(resultMap==null||resultMap.size()==0){
//				sendCreateOrderInfo("error", "支付异常，请到官网检查是否已经支付", "", childrenUser, transactionNumber, order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:支付异常，请到官网检查是否已经支付";
//			}
//			String errorMsg = "";
//			try {
//				errorMsg = resultMap.get("error");
//			} catch (Exception e) {}
//			if(StringUtils.isNotEmpty(errorMsg)){
//				sendCreateOrderInfo("error", errorMsg, "", childrenUser, transactionNumber, order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:"+errorMsg;
//			}
//			String bankNo = ""; //银行订单号
//			try {
//				bankNo = resultMap.get("bankNo");
//			} catch (Exception e) {}
//			if(StringUtils.isEmpty(bankNo)){
//				sendCreateOrderInfo("error", "支付未知错误，请到官网确认是否已支付", "", childrenUser, transactionNumber, order_id, "", "", null, "","","false","true",billNo,"",requestType);
//				return "error:支付未知错误，请到官网确认是否已支付";
//			}
//			String money = resultMap.get("money");
//			int count = 40;
//			back = "";
//			while (bankNo != null && count > 0) {
//				String paytype = "易宝";
//				if(StringUtils.isNotEmpty(payType)&&"xyk".equals(payType)){
//					paytype = "易宝信用卡";
//				}else if(StringUtils.isNotEmpty(payType)&&"ybhy".equals(payType)){
//					paytype = "易宝会员";
//				}
//				sendCreateOrderInfo("success", "", money, childrenUser, orderNo, order_id, "true", "", null, paytype,
//						"", "false", "true",billNo,"",requestType);
//				sendOrderStatus(childrenUser, order_id, "获取票号信息");
//				try {
//					cookie = accountMap.get(userAccount[0]+userAccount[1]);
//					if(StringUtils.isEmpty(cookie)){
//						//登录后查询
//						for(int loginIndex = 0 ;loginIndex< 5;loginIndex++){
//							cookie = getCookie(httpclient,"",json,defaultRequestConfig);
//							if(!cookie.contains("登陆失败")&&!cookie.contains("请填写正确的云速账号")){
//								accountMap.put(userAccount[0]+userAccount[1], cookie);
//								break;
//							}
//							if(cookie.contains("请填写正确的云速账号")){
//								sendCreateOrderInfo("error", "请填写正确的云速账号", "", childrenUser, newOrderNo, order_id, "", "", null, "", "",
//										"false", "true",billNo,"",requestType);
//								return "SUCCESS#@@#请填写正确的云速账号";
//							}
//							Thread.sleep(500);
//						}
//					}
//					back = getDetail(httpclient, defaultRequestConfig, cookie, transactionNumber, orderJson);
//					if("重新登录".equals(back)){
//						accountMap.remove(userAccount[0]+userAccount[1]);
//						back = "";
//					}
//				} catch (Exception e) {
//					logger.error("error",e);
////					sendOrderStatus(childrenUser, order_id, "获取票号信息异常");
////					sendCreateOrderInfo("error", "获取票号信息异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//					logger.info(order_id+"获取票号信息异常");
//					Thread.sleep(20*1000);
//					--count;
//					continue;
//				}
//				//返回的结果格式为 乘机人1##证件号码##票号##BANKNO#_#乘机人2##证件号码##票号##BANKNO#_#@_@流水号
//				if(StringUtils.isNotEmpty(back)){
//					back = back.replaceAll("BANKNO", bankNo);
//					String ticketnos[] = back.split("@_@");
//					if (ticketnos.length == 2) {
//						sendOrderStatus(childrenUser, order_id, "回填票号");
//						// 判断获取的票号数是否与乘客数量相符合,不相符合重新
//						String[] ticketCount = ticketnos[0].split("#_#");
//						if (ticketCount.length == passengers.length() && !ticketnos[0].contains("null")) {
//							sendCreateOrderInfo("success", "", money , childrenUser, orderNo, order_id, "",
//									"true", ticketnos[0], "易宝", ticketnos[1], "false", "true",billNo,verifyPostParam.get("cabinCode"),0);
//							return "SUCCESS";
//						}
//					}
//				}
//				try {
//					Thread.sleep(20*1000);
//				} catch (InterruptedException e) {
//					logger.error("error", e);
//				}
//				--count;
//			}
//			if (count <= 0) {
//				sendCreateOrderInfo("error", "未找到票号信息", "", childrenUser, orderNo, order_id, "", "", null, "", "",
//						"false", "true",billNo,"",requestType);
//				return "SUCCESS#@@#未找到票号信息";
//			}
//		}catch (Exception e) {
//			logger.error(e);
//			sendCreateOrderInfo("error", "创单异常", "", childrenUser, "", order_id, "", "", null, "","","false","true",billNo,"",requestType);
//			return "ERROR:创单异常";
//		}finally{
//			try {
//				httpclient.close();
//			} catch (IOException e) {
//				logger.error(e);
//			}
//		}
//		return "SUCCESS";
//	}
//
//	/**
//	 * 发送支付宝支付链接
//	 * @param childrenUser
//	 * @param order_id
//	 * @param location
//	 */
//	private String sendAliPayUrl(String childrenUser, String order_id, String location, String cookie) {
//		try {
//			String orderUrl = PropertiesUtils.getPropertiesValue("config", "aliPayUrl");
//			logger.info(order_id + "支付链接" + location);
//			StringBuffer buffer = new StringBuffer();
//			buffer.append("<feeye-official>");
//			buffer.append("<official>"+"KNAPP"+"</official> ");
//			buffer.append("<url>"+orderUrl+"</url> ");
//			buffer.append("<type>0</type> ");
//			buffer.append("<method>post</method>");
//			buffer.append("<max>20</max> ");
//			buffer.append("<encod>utf-8</encod> ");
//			buffer.append("<params>");
//			buffer.append("<param name='childrenUser'>"+childrenUser+"</param>");
//			buffer.append("<param name='orderId'>"+order_id+"</param>");
//			buffer.append("<param name='location'>"+location+"</param>");
//			buffer.append("<param name='cookie'>"+cookie.replaceAll("\"", "'")+"</param>");
//			buffer.append("</params>");
//			buffer.append("</feeye-official>");
//
//			String content = OfficialMain.setRequestParams(buffer.toString());
//			if(content != null){
//				String rs1[] = content.split("#@_@#");
//				if(rs1.length == 2){
//					content = rs1[1];
//					return content;
//				}
//				if (rs1.length == 3) {
//					logger.info(rs1[2]);
//					return rs1[2];
//				}
//			}
//
//		} catch (Exception e) {
//			logger.error("error",e);
//		}
//		return null;
//
//
//	}
//
//	private String getDetail(CloseableHttpClient httpclient, RequestConfig defaultRequestConfig,String cookie, String orderNo, String orderJson) throws Exception {
//		defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout)
//				.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
//				.setExpectContinueEnabled(false).setStaleConnectionCheckEnabled(true).build();
//		StringBuilder resultBuilder = new StringBuilder();
//		JSONObject json = new JSONObject(orderJson);
//		JSONArray pa = json.getJSONArray("passengers");
//		//获取票号
//		HttpPost post = new HttpPost("/h5/pip/ticketOrder/orderDetail.json");
//		HttpHost target = new HttpHost("m.flycua.com", 443, "https");
//		post.setConfig(defaultRequestConfig);
//		String jsonObject = "{\"orderNo\":\""+orderNo+"\"}";
//		logger.info("请求参数："+jsonObject+",Cookie:"+cookie);
//		StringEntity entity = new StringEntity(jsonObject.toString(),Charset.forName("UTF-8"));
//		post.setEntity(entity);
//		post.setConfig(defaultRequestConfig);
//		post.setHeader("Content-Type","application/json;charset=UTF-8");
//		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//		post.setHeader("Host","m.flycua.com");
//		post.setHeader("Cookie",cookie);
//		String back = "";
//		try {
//			CloseableHttpResponse response = httpclient.execute(target,post);
//			back = EntityUtils.toString(response.getEntity(),"utf-8");
//		} catch (Exception e) {
//			post.abort();
//		}
//		logger.info("获取订单详情返回结果:"+back);
//		if(back.contains("中联航单点登录")){
//			return "重新登录";
//		}
//		JSONObject backObj = new JSONObject(back);
//		JSONObject pipOrderDetail = backObj.getJSONObject("pipOrderDetail");
//		JSONObject components = pipOrderDetail.getJSONObject("components");
//		JSONObject air = components.getJSONObject("AIR");
//		JSONArray goItinerarysIterator = air.getJSONArray("goItinerarys");
//		Map<String,String> passengerMap = new HashMap<String,String>();
//		for(int i=0;i<goItinerarysIterator.length();i++){
//			JSONObject goItinerarysIteratorObj = goItinerarysIterator.getJSONObject(i);
//			JSONArray passengers = goItinerarysIteratorObj.getJSONArray("passengers");
//			for(int j=0;j<passengers.length();j++){
//				JSONObject passenger = passengers.getJSONObject(j);
//				String name = passenger.getString("name"); //乘机人
//				JSONObject details = passenger.getJSONArray("details").getJSONObject(0);
//				String ticketNo = details.getString("ticketNo"); //票号
//				if(StringUtils.isNotEmpty(name)&&StringUtils.isNotEmpty(ticketNo)){
//					passengerMap.put(name, ticketNo);
//				}
//			}
//		}
//		JSONObject payInfos = pipOrderDetail.getJSONArray("payInfos").getJSONObject(0);
//		String transactionNo = payInfos.getString("transactionNo"); //交易流水号
//		for(int i=0;i<pa.length();i++){
//			JSONObject paObj = pa.getJSONObject(i);
//			String passengerName = paObj.getString("passengerName");
//			String idcard = paObj.getString("idcard");
//			String ticketNo = passengerMap.get(passengerName);
//			if(StringUtils.isNotEmpty(ticketNo)){
//				String passengerinfo = passengerName + "##" + idcard.replace("/", "") + "##" + ticketNo + "##" + "BANKNO";
//				resultBuilder.append(passengerinfo+"#_#");
//			}else {
//				return null;
//			}
//		}
//		if(StringUtils.isNotEmpty(transactionNo)){
//			resultBuilder.append("@_@").append(transactionNo);
//		}else {
//			return null;
//		}
//		return resultBuilder.toString();
//	}
//
//	private Map<String, String> selectPayMethod(String back,String payType) throws Exception {
//		Map<String,String> resultMap = new HashMap<String,String>();
//		JSONObject orderConfirmObj = new JSONObject(back);
//		JSONObject paymentMethodsObj = null;
//		String paymentType = "";
//		String paymentCode = "";
//		JSONArray paymentMethodsArr = orderConfirmObj.getJSONArray("paymentMethods");
//		for (int i = 0; i < paymentMethodsArr.length(); i++) {
//			paymentMethodsObj = paymentMethodsArr.getJSONObject(i);
//			String providerName = paymentMethodsObj.getString("providerName");
//			if(StringUtils.isNotEmpty(payType)&&"zfb".equals(payType)){
//				if(!"支付宝WAP".equals(providerName)){
//					continue;
//				}
//			}else {
//				if (!"易宝支付".equals(providerName)) {
//					continue;
//				}
//			}
//			paymentType = paymentMethodsObj.getString("paymentType");
//			paymentCode = paymentMethodsObj.getString("paymentCode");
//		}
//		resultMap.put("paymentType", paymentType);
//		resultMap.put("paymentCode", paymentCode);
//		return resultMap;
//	}
//
//	private String payment(RequestConfig defaultRequestConfig, CloseableHttpClient httpclient, String cookie,
//			String paymentType, String paymentCode, String orderNo) throws Exception {
//		HttpPost post = new HttpPost("/h5/pay/payment.json");
//		String jsonObject = "{\"paymentType\":\""+paymentType+"\",\"paymentCode\":\""+paymentCode+"\",\"orderNum\":\""+orderNo+"\"}";
//		post.setConfig(defaultRequestConfig);
//		HttpHost target = new HttpHost("m.flycua.com", 443, "https");
//		StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
//		post.setEntity(entity);
//		post.setHeader("Referer","https://m.flycua.com/h5/");
//		post.setHeader("Content-Type","application/json;charset=UTF-8");
//		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//		post.setHeader("Host","m.flycua.com");
//		post.setHeader("Cookie",cookie);
//		CloseableHttpResponse response = httpclient.execute(target,post);
//		String back = EntityUtils.toString(response.getEntity(),"utf-8");
//		//outPrint(target, post, null, cookie, jsonObject, back);
//		return back;
//	}
//
//	private String createOrder(CloseableHttpClient httpclient, RequestConfig defaultRequestConfig,
//			String cookie) throws Exception{
//		HttpPost post = new HttpPost("/h5/pip/book/createOrder.json");
//		String jsonObject = "{\"isRedPaper\":\"no\"}";
//		post.setConfig(defaultRequestConfig);
//		HttpHost target = new HttpHost("m.flycua.com", 443, "https");
//		post.setConfig(defaultRequestConfig);
//		StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
//		post.setEntity(entity);
//		post.setHeader("Referer","https://m.flycua.com/h5/");
//		post.setHeader("Content-Type","application/json;charset=UTF-8");
//		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//		post.setHeader("Host","m.flycua.com");
//		post.setHeader("Cookie",cookie);
//		CloseableHttpResponse response = httpclient.execute(target,post);
//		String back = EntityUtils.toString(response.getEntity(),"utf-8");
//		//outPrint(target, post, null, cookie, jsonObject, back);
//		return back;
//	}
//
//	private String orderConfirm(String cookie, RequestConfig defaultRequestConfig, CloseableHttpClient httpclient,
//			Map<String, String> resultMap, String orderJson,String msg,List<String> passengerId,String linkMan,String mobile) throws Exception {
//		JSONObject json = new JSONObject(orderJson);
//		JSONArray flights = json.getJSONArray("flights");
//		String departureDate = flights.getJSONObject(0).getString("departureDate");
//		if(StringUtils.isNotEmpty(mobile)){
//			mobile = mobile.replaceAll(",", "");
//		}
//		String flightNo = flights.getJSONObject(0).getString("flightNo");
//		String arrival = flights.getJSONObject(0).getString("arrival");
//		String departure = flights.getJSONObject(0).getString("departure");
//		String ancilShoppingKey = resultMap.get("ancilShoppingKey");
//		String dstAirportName = resultMap.get("dstAirportName");
//		String orgAirportName = resultMap.get("orgAirportName");
//		HttpPost post = new HttpPost("/h5/pip/book/orderConfirm.json");
//		StringBuffer orderConfirmPostParam = new StringBuffer();
//		String flightWeek = dateToWeek(departureDate);
//		orderConfirmPostParam.append("{\"ancillaryShoppingKey\":\""+ancilShoppingKey+"\",\"contactName\":\""+linkMan+"\",\"contactMobile\":\""+mobile+"\",\"contactId\":\""+msg+"\",\"passengerIds\":[");
//		for(int i = 0; i<passengerId.size();i++){
//			orderConfirmPostParam.append("{\"id\":"+passengerId.get(i)+",\"ancillaryGroups\":[{\"ancillaries\":[],\"flightNo\":\""+flightNo+"\",\"dstAirport\":{\"airportCode\":\""+arrival+"\",\"airportName\":\""+dstAirportName+"\"},\"flightDate\":\""+departureDate+"\",\"flightWeek\":\""+flightWeek+"\",\"orgAirport\":{\"airportCode\":\""+departure+"\",\"airportName\":\""+orgAirportName+"\"},\"transpart\":\"1\",\"tripTypeString\":\"去程\"}]},");
//		}
//		orderConfirmPostParam.delete(orderConfirmPostParam.length()-1, orderConfirmPostParam.length());
//		orderConfirmPostParam.append("]}");
//		post.setConfig(defaultRequestConfig);
//		HttpHost target = new HttpHost("m.flycua.com", 443, "https");
//		StringEntity entity = new StringEntity(orderConfirmPostParam.toString(), Charset.forName("UTF-8"));
//		post.setEntity(entity);
//		post.setHeader("Referer","https://m.flycua.com/h5/");
//		post.setHeader("Content-Type","application/json;charset=UTF-8");
//		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
//		post.setHeader("Host","m.flycua.com");
//		post.setHeader("Cookie",cookie);
//		CloseableHttpResponse response = httpclient.execute(target,post);
//		String back = EntityUtils.toString(response.getEntity(),"utf-8");
//		//outPrint(target,post,null,cookie,orderConfirmPostParam.toString(),back);
//		return back;
//	}
//
//	private String getContactId(String cookie, RequestConfig defaultRequestConfig, CloseableHttpClient httpclient,String orderJson,String memberId) throws Exception {
//		JSONObject json = new JSONObject(orderJson);
//		String linkMan = json.getString("linkMan");
//		String mobile = json.getString("mobile");
//		if(StringUtils.isNotEmpty(mobile)){
//			mobile = mobile.replace(",", "");
//		}
//		String order_id = json.getString("id");
//		HttpPost post = new HttpPost("/h5/pip/book/verifyContact.json");
//		String jsonObject = "{\"contactName\":\""+linkMan+"\",\"contactMobile\":\""+mobile+"\"}";
//		post.setConfig(defaultRequestConfig);
//		HttpHost target = new HttpHost("m.flycua.com", 443, "https");
//		post.setConfig(defaultRequestConfig);
//		StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
//		post.setEntity(entity);
//		post.setHeader("Referer","https://m.flycua.com/h5/");
//		post.setHeader("Content-Type","application/json;charset=UTF-8");
//		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//		post.setHeader("Host","m.flycua.com");
//		post.setHeader("Cookie",cookie);
//		CloseableHttpResponse response = httpclient.execute(target,post);
//		String back = EntityUtils.toString(response.getEntity(),"utf-8");
//		//outPrint(target,post,null,cookie,jsonObject,back);
//		String msg = "";
//		try {
//			JSONObject verifyContactObj = new JSONObject(back);
//			try {
//				msg = verifyContactObj.getString("msg");
//			} catch (Exception e) {
//				logger.error("verifyContactResultError"+e);
//				logger.info(order_id + "verifyContactResultError:"+back);
//				String errorMsg = getErrorMsg(verifyContactObj);
//				return "ERROR:"+errorMsg;
//			}
//		} catch (Exception e) {
//			logger.error("error",e);
//		}
//		if(msg == null || "null".equalsIgnoreCase(msg) || StringUtils.isEmpty(msg)){
//			logger.info(order_id+"获取不到联系人id:"+back);
//			return "ERROR:获取不到联系人id,请在官网账号列表中更换成该官网账号默认的联系人和联系电话";
//		}
//		return msg;
//	}
//
//	private Map<String,String> getDefaultContacter(String cookie, RequestConfig defaultRequestConfig,
//			CloseableHttpClient httpclient, String memberId, String order_id) throws Exception{
//		Map<String,String> resultMap = new HashMap<String,String>();
//		HttpHost target = new HttpHost("higo.flycua.com", 443, "https");
//		HttpGet get = new HttpGet("/hh/html/contacts.html?rand="+(int) ((Math.random() + 1) * 1000000) + "&from=null");
//		get.setConfig(defaultRequestConfig);
//		get.setHeader("Referer","https://higo.flycua.com/hh/html/personalInfo.html?rand="+(int) ((Math.random() + 1) * 1000000) + "&from=null");
//		get.setHeader("Content-Type","application/json;charset=UTF-8");
//		get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//		get.setHeader("Host","higo.flycua.com");
//		get.setHeader("Cookie",cookie);
//		CloseableHttpResponse response = httpclient.execute(target,get);
//		String back = EntityUtils.toString(response.getEntity(),"utf-8");
////		outPrint(target,null,get,cookie,"",back);
//		HttpPost post = new HttpPost("/ffp/member/contact");
//		String jsonObject = "{\"mode\":\"query\",\"memberId\":\""+memberId+"\"}";
//		jsonObject = aesInvokeFunction("encrypt", jsonObject);
//		post.setConfig(defaultRequestConfig);
//		StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
//		post.setEntity(entity);
//		post.setHeader("Referer","https://higo.flycua.com/hh/html/contacts.html?rand="+(int) ((Math.random() + 1) * 1000000) + "&from=null");
//		post.setHeader("Content-Type","application/json;charset=UTF-8");
//		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//		post.setHeader("Host","higo.flycua.com");
//		post.setHeader("Cookie",cookie);
//		response = httpclient.execute(target,post);
//		back = EntityUtils.toString(response.getEntity(),"utf-8");
//		if(StringUtils.isEmpty(back)&&back.contains("please login first")){
//			resultMap.put("error", "please login first");
//			return resultMap;
//		}
//		//outPrint(target,post,null,cookie,jsonObject,back);
//		JSONObject json = new JSONObject(back);
//		String errordesc = json.getString("errordesc");
//		back = aesInvokeFunction("decrypt", errordesc);
//		json = new JSONObject(back);
//		if(StringUtils.isEmpty(back)&&back.contains("please login first")){
//			resultMap.put("error", "please login first");
//			return resultMap;
//		}
//		JSONArray contactArr = json.getJSONArray("contact");
//		JSONObject contact = contactArr.getJSONObject(0);
//		String id = contact.getString("id");
//		String name = contact.getString("name");
//		String mobile = contact.getString("mobile");
//		resultMap.put("id", id);
//		resultMap.put("name", name);
//		resultMap.put("mobile",mobile);
//		return resultMap;
//	}
//
//	private Map<String, String> ancilSearch(String cookie, RequestConfig defaultRequestConfig,
//			CloseableHttpClient httpclient, Map<String, String> paramMap, String orderJson) throws Exception {
//		Map<String,String> resultMap = new HashMap<String,String>();
//		JSONObject json = new JSONObject(orderJson);
//		JSONArray flights = json.getJSONArray("flights");
//		String arrival = flights.getJSONObject(0).getString("arrival");
//		String departure = flights.getJSONObject(0).getString("departure");
//		String flightNo = flights.getJSONObject(0).getString("flightNo");
//		String arrayTime = paramMap.get("arrayTime");
//		String departTime = paramMap.get("departTime");
//		HttpPost post = new HttpPost("/h5/pip/book/ancilSearch.json");
//		String jsonObject = "{\"ancillaryType\":\"ALL\",\"adultNum\":\"1\",\"childNum\":\"0\",\"infantNum\":\"0\",\"odInfo\":[{\"airline\":\"KN\",\"arriveTime\":\""+arrayTime+"\",\"takeoffTime\":\""+departTime+"\",\"dstCode\":\""+arrival+"\",\"orgCode\":\""+departure+"\",\"ioBount\":\"O\",\"flightNo\":\""+flightNo+"\"}]}";
//		HttpHost target = new HttpHost("m.flycua.com", 443, "https");
//		post.setConfig(defaultRequestConfig);
//		StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
//		post.setEntity(entity);
//		post.setHeader("Referer","https://m.flycua.com/h5/");
//		post.setHeader("Origin","https://m.flycua.com");
//		post.setHeader("Content-Type","application/json;charset=UTF-8");
//		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//		post.setHeader("Host","m.flycua.com");
//		post.setHeader("Cookie",cookie);
//		CloseableHttpResponse response = httpclient.execute(target,post);
//		String back = EntityUtils.toString(response.getEntity(),"utf-8");
//		//outPrint(target,post,null,cookie,jsonObject,back);
//		if(back.contains("会话已超时，请重新预订")){
//			resultMap.put("error", "会话已超时，请重新预订");
//			return resultMap;
//		}
//		JSONObject ancilSearchObj = new JSONObject(back);
//		String ancilShoppingKey = ancilSearchObj.getString("ancilShoppingKey");
//		JSONObject goAncilTypeObj = null;
//		JSONArray goAncilTypeArr = ancilSearchObj.getJSONArray("goAncilType");
//		String orgAirportName = "";
//		String dstAirportName = "";
//		for(int i=0;i<goAncilTypeArr.length();i++){
//			goAncilTypeObj = goAncilTypeArr.getJSONObject(i);
//			JSONArray ancilODInfoArr = goAncilTypeObj.getJSONArray("ancilODInfo");
//			JSONObject ancilODInfoObj = ancilODInfoArr.getJSONObject(0);
//			JSONObject orgAirport = ancilODInfoObj.getJSONObject("orgAirport");
//			JSONObject dstAirport = ancilODInfoObj.getJSONObject("dstAirport");
//			orgAirportName = orgAirport.getString("airportName");
//			dstAirportName = dstAirport.getString("airportName");
//		}
//		logger.info("ancilShoppingKey:"+ancilShoppingKey);
//		logger.info("ancilSearch:"+back);
//		resultMap.put("ancilShoppingKey", ancilShoppingKey);
//		resultMap.put("ancilSearch", back);
//		resultMap.put("orgAirportName", orgAirportName);
//		resultMap.put("dstAirportName", dstAirportName);
//		return resultMap;
//	}
//
//	private String addPassengers(String orderJson,
//			CloseableHttpClient httpclient, String cookie, RequestConfig defaultRequestConfig, List<String> passengerId) throws Exception{
//		List<Map<String, String>> passengerInfoList = new ArrayList<Map<String,String>>();
//		JSONObject json = new JSONObject(orderJson);
//		String account = json.getString("account");
//		String userAccount[] = account.split("_");
//		String mobile = json.getString("mobile");
//		if(StringUtils.isNotEmpty(mobile)){
//			mobile = mobile.replaceAll(",", "");
//		}
//		String order_id = json.getString("id");
//		JSONArray passengers = json.getJSONArray("passengers");
//		String back = "";
//		for(int i = 0; i<passengers.length();i++){
//			Map<String, String> passengerInfoMap = new HashMap<String, String>();
//			JSONObject jObject = passengers.getJSONObject(i);
//			String name = jObject.getString("passengerName");
//			String credentials = jObject.getString("passengerType");
//			if("成人".equals(credentials)){
//				credentials = "ADT";
//			}else if("儿童".equals(credentials)){
//				credentials = "CHD";
//			}else{
//				credentials = "INF";
//			}
//			String birthday = jObject.getString("birthday");
//			if(StringUtils.isNotEmpty(birthday)&&!"null".equals(birthday)){
//				birthday = birthday.substring(0,10);
//			}
//			String sex = "男";
//			try {
//				sex = jObject.getString("passengerSex");
//			} catch (Exception e) {}
//			if("男".equals(sex)){
//				sex = "0";
//			}else {
//				sex = "1";
//			}
//			String idType = "其他";
//			String idExpires = "";
//			if("身份证".equals(idType)){
//				idType = "NI";
//			}else if("护照".equals(idType)){
//				idType = "PP";
//				idExpires = "2101-01-30";
//			}else {
//				idType = "ID";
//			}
//			String numid = jObject.getString("idcard");
//			String nameType = "CN";
//			if(StringUtils.isNotEmpty(name)&&name.contains("/")){
//				nameType = "EN";
//			}
//			passengerInfoMap.put("birthday", birthday);
//			passengerInfoMap.put("idNo", numid);
//			passengerInfoMap.put("idType", idType);
//			passengerInfoMap.put("name", name);
//			passengerInfoMap.put("nation", nameType);
//			passengerInfoMap.put("passengerType", credentials);
//			passengerInfoMap.put("sex", sex);
//			passengerInfoMap.put("idExpires",idExpires);
//			passengerInfoList.add(passengerInfoMap);
//		}
//		for(int i=0;i<passengerInfoList.size();i++){
//			Map<String, String> passengerInfo = passengerInfoList.get(i);
//			HttpPost post = new HttpPost("/h5/pip/book/mergePassenger.json");
//			String jsonObject = "{\"passengerBo\":{\"name\":\""+passengerInfo.get("name").replaceAll(" ", "")+"\",\"idNo\":\""+passengerInfo.get("idNo")+"\",\"idType\":\""+passengerInfo.get("idType")+"\",\"mobile\":\""+mobile+"\",\"birthday\":\""+passengerInfo.get("birthday")+"\",\"passengerType\":\""+passengerInfo.get("passengerType")+"\",\"sex\":\""+passengerInfo.get("sex")+"\",\"nation\":\""+passengerInfo.get("nation")+"\",\"idExpires\":\""+passengerInfo.get("idExpires")+"\"},\"isCn\":true}";
//			post.setConfig(defaultRequestConfig);
//			HttpHost target = new HttpHost("m.flycua.com", 443, "https");
//			post.setConfig(defaultRequestConfig);
//			StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
//			post.setEntity(entity);
//			post.setHeader("Referer","https://m.flycua.com/h5/");
//			post.setHeader("Origin","https://m.flycua.com");
//			post.setHeader("Content-Type","application/json;charset=UTF-8");
//			post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//			post.setHeader("Host","m.flycua.com");
//			CloseableHttpResponse response = httpclient.execute(target,post);
//			back = EntityUtils.toString(response.getEntity(),"utf-8");
////			logger.info(order_id+"添加乘客后返回:"+back);
//			//outPrint(target,post,null,cookie,jsonObject,back);
//			if(back.contains("404错误")){
//				try {
//					accountMap.remove(userAccount[0]+userAccount[1]);
//				} catch (Exception e) {}
//				logger.info(order_id+"添加乘客返回404");
//				continue;
//			}
//			JSONObject backObj = new JSONObject(back);
//			JSONObject commonRes = null;
//			try {
//				commonRes = backObj.getJSONObject("commonRes");
//			} catch (Exception e) {
//			}
//			if(commonRes == null){
//				continue;
//			}
//			String message = "";
//			try {
//				message = commonRes.getString("message");
//			} catch (Exception e) {}
//			if(StringUtils.isNotEmpty(message)&&message.contains("证件号码重复")){
//				logger.info(order_id + passengerInfo.get("name") + message);
//				continue;
//			}else if(StringUtils.isNotEmpty(message)){
//				return "error:添加乘客"+passengerInfo.get("name")+"错误:"+message;
//			}
//			JSONArray passengersArr = null;
//			try {
//				passengersArr = backObj.getJSONArray("passenger");
//			} catch (Exception e) {
//				passengersArr = backObj.getJSONArray("passengers");
//			}
//			for(int index=0;index<passengersArr.length();index++){
//				JSONObject passengersObj = passengersArr.getJSONObject(index);
//				String name = passengersObj.getString("name");
//				String idNo = "";
//				try {
//					idNo = passengersObj.getString("idNo");
//				} catch (Exception e) {
//					idNo = passengersObj.getString("idno");
//				}
//				int id = 0;
//				try {
//					id = passengersObj.getInt("id");
//				} catch (Exception e) {
//					try {
//						id = Integer.parseInt(passengersObj.getString("id"));
//					} catch (Exception e2) {
//					}
//				}
//				if(passengerInfo.get("name").replaceAll(" ", "").equals(name)&&passengerInfo.get("idNo").equals(idNo)){
//					logger.info(name + "添加成功"+ id);
//					passengerId.add(id+"");
//					break;
//				}
//			}
//		}
////		logger.info(order_id+"添加乘客后返回:"+back);
//		return back;
//	}
//
//	private Map<String,String> selectFlight(Map<String, String> verifyPostParam, String orderJson, String cookie,
//			CloseableHttpClient httpclient, RequestConfig defaultRequestConfig) throws Exception {
//		Map<String,String> resultMap = new HashMap<String,String>();
//		HttpPost post = new HttpPost("/h5/pip/book/verify.json");
//		String jsonObject = "{\"shoppingKey\":\""+verifyPostParam.get("shoppingKey")+"\",\"goPricePointUUID\":[{\"tripType\":\"I\",\"transpart\":\"1\",\"pricePointUUID\":\""+verifyPostParam.get("pricePointUUID")+"\"}]}";
//		post.setConfig(defaultRequestConfig);
//		HttpHost target = new HttpHost("m.flycua.com", 443, "https");
//		post.setConfig(defaultRequestConfig);
//		StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
//		post.setEntity(entity);
//		post.setHeader("Referer","https://m.flycua.com/h5/");
//		post.setHeader("Cookie",cookie);
//		post.setHeader("Content-Type","application/json;charset=UTF-8");
//		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//		post.setHeader("Host","m.flycua.com");
//		post.setHeader("isWechat","H5");
//		CloseableHttpResponse response = httpclient.execute(target,post);
//		String back = EntityUtils.toString(response.getEntity(),"utf-8");
//		//outPrint(target,post,null,cookie,jsonObject,back);
//		if(StringUtils.isNotEmpty(back)&&back.contains("503 Service Temporarily Unavailable")){
//			resultMap.put("error", "503异常重试");
//			return resultMap;
//		}
//		if(StringUtils.isNotEmpty(back)&&back.contains("请先登录")){
//			resultMap.put("error", "请先登录");
//			return resultMap;
//		}
//		JSONObject verifyObj = null;
//		try {
//			verifyObj = new JSONObject(back);
//			String departTime = "";
//			String arrayTime = "";
//			JSONObject goFlightInfosObj = verifyObj.getJSONObject("goFlightInfos");
//			JSONObject flightSegsObj = null;
//			JSONArray flightSegsArr = goFlightInfosObj.getJSONArray("flightSegs");
//			for(int i=0;i<flightSegsArr.length();i++){
//				flightSegsObj = flightSegsArr.getJSONObject(i);
//				departTime = flightSegsObj.getString("departTime");
//				arrayTime = flightSegsObj.getString("arrivalTime");
//				resultMap.put("departTime", departTime);
//				resultMap.put("arrayTime", arrayTime);
//			}
//		} catch (Exception e) {
//			logger.error("verifyObjError",e);
//			logger.info("verifyObjError:"+back);
//			String errorMsg = getErrorMsg(verifyObj);
//			resultMap.put("error", errorMsg);
//		}
//		return resultMap;
//	}
//
//	private void parseFlightInfo(String back, String orderJson,Map<String,String> verifyPostParam) throws Exception {
//		JSONObject json = new JSONObject(orderJson);
//		JSONArray flights = json.getJSONArray("flights");
//		String flightNo = flights.getJSONObject(0).getString("flightNo");
//		boolean matchCabin = false;
//		try {
//			matchCabin = json.getBoolean("matchCabin");
//		} catch (Exception e) {}
//		String cabin = flights.getJSONObject(0).getString("cabin");
//		String price = flights.getJSONObject(0).getString("price");
//		JSONObject jo = new JSONObject(back);
//		String shoppingKey = jo.getString("shoppingKey");
//		JSONObject goFlightInfo = jo.getJSONObject("goFlightInfo");
//		//判断所选航班和票价是否存在
//		JSONObject flightInfoObj = null;
//		JSONArray flightInfoArr = goFlightInfo.getJSONArray("flightInfo");
//		for(int i=0;i<flightInfoArr.length();i++){
//			flightInfoObj = flightInfoArr.getJSONObject(i);
//			JSONObject flightSegsObj = null;
//			JSONArray flightSegsArr = flightInfoObj.getJSONArray("flightSegs");
//			for(int j=0;j<flightSegsArr.length();j++){
//				flightSegsObj = flightSegsArr.getJSONObject(j);
//				String grabFlightNo = flightSegsObj.getString("flightNo");
//				if(!flightNo.equals(grabFlightNo)){
//					continue;
//				}
//				JSONObject brandSegObj = null;
//				JSONArray brandSegArr = flightSegsObj.getJSONArray("brandSeg");
//				for(int z=0;z<brandSegArr.length();z++){
//					brandSegObj = brandSegArr.getJSONObject(z);
//					JSONObject priceObj = null;
//					JSONArray priceArr = brandSegObj.getJSONArray("price");
//					for(int q=0;q<priceArr.length();q++){
//						priceObj = priceArr.getJSONObject(q);
//						String psgType = priceObj.getString("psgType");
//						if(!"ADT".equals(psgType)){
//							continue;
//						}
//						String grabPrice = priceObj.getString("price");
//						double grabPriceDoubleType = 0d;
//						double priceDoubleType = 0d;
//						if(StringUtils.isNotEmpty(grabPrice)&&StringUtils.isNotEmpty(price)){
//							grabPriceDoubleType = Double.parseDouble(grabPrice);
//							priceDoubleType = Double.parseDouble(price);
//						}
//						if(grabPriceDoubleType==priceDoubleType){
//							String cabinCode = brandSegObj.getString("cabinCode");	//仓位
//							if(matchCabin){
//								if(StringUtils.isNotEmpty(cabinCode)&&StringUtils.isNotEmpty(cabin)&&!cabinCode.equalsIgnoreCase(cabin)){
//									continue;
//								}
//							}
//							String pricePointUUID = brandSegObj.getString("pricePointUUID");
//							verifyPostParam.put("shoppingKey", shoppingKey);
//							verifyPostParam.put("pricePointUUID", pricePointUUID);
//							verifyPostParam.put("cabinCode", cabinCode);
//						}
//					}
//				}
//			}
//		}
//	}
//
//	private Map<String,String> flightSearch(RequestConfig defaultRequestConfig, String orderJson, CloseableHttpClient httpclient,
//			BasicCookieStore cookieStore) throws Exception {
//		Map<String,String> resultMap = new HashMap<String,String>();
//		try {
//			JSONObject json = new JSONObject(orderJson);
////			JSONArray flights = json.getJSONArray("flights");
//			String departure = "NAY";
//			String arrival = "CAN";
//			String departureDate = "2019-02-18";
//			HttpPost post = new HttpPost("/h5/pip/book/flightSearch.json");
//		    HttpHost target = new HttpHost("m.flycua.com", 443, "https");
//			post.setConfig(defaultRequestConfig);
//			String jsonObject = "{\"tripType\":\"OW\",\"orgCode\":\""+departure+"\",\"dstCode\":\""+arrival+"\",\"takeoffdate1\":\""+departureDate+"\",\"takeoffdate2\":\"\"}";
//			StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
//			post.setEntity(entity);
//			post.setHeader("Referer","https://m.flycua.com/h5/");
//			post.setHeader("Origin","https://m.flycua.com");
//			post.setHeader("Content-Type","application/json;charset=UTF-8");
//			post.setHeader("Host","m.flycua.com");
//			post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//
//			CloseableHttpResponse response = httpclient.execute(target,post);
//			List<Cookie> listCookie = cookieStore.getCookies();
//			String cookie = "";
//			for (int i = 0; i < listCookie.size(); i++) {
//				cookie += listCookie.get(i).getName() + "=" + listCookie.get(i).getValue() + ";";
//			}
//			String back = EntityUtils.toString(response.getEntity(),"utf-8");
//			//outPrint(target,post,null,cookie,jsonObject,back);
//			resultMap.put("cookie", cookie);
//			resultMap.put("result", back);
//			return resultMap;
//		} catch (Exception e) {
//			logger.error("error",e);
//		}
//
//		return null;
//	}
//
////	private void outPrint(HttpHost target, HttpPost post, HttpGet get, String cookie, String jsonObject,
////			String back) {
////		if(post!=null){
////			logger.info(target.getSchemeName()+"://"+target.getHostName()+post.getURI());
////		}
////		if(get!=null){
////			logger.info(target.getSchemeName()+"://"+target.getHostName()+get.getURI());
////		}
////		logger.info("cookie:"+cookie);
////		logger.info("param:"+jsonObject);
////		logger.info("back:"+back);
////	}
//
//	private String getErrorMsg(JSONObject verifyObj) throws Exception{
//		JSONObject object = verifyObj.getJSONObject("commonRes");
//		String errorMsg = object.getString("message");
//		return errorMsg;
//	}
//
//	public static String dateToWeek(String datetime) {
//        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
//        String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
//        Calendar cal = Calendar.getInstance(); // 获得一个日历
//        Date datet = null;
//        try {
//            datet = f.parse(datetime);
//            cal.setTime(datet);
//        } catch (ParseException e) {
//            logger.error(e);
//        }
//        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
//        if (w < 0)
//            w = 0;
//        return weekDays[w];
//    }
//	/**
//	 * 推送创单情况
//	 * String result = request.getParameter("result");  //是否创单成功
//		String message = request.getParameter("message"); //失败消息
//		String price = request.getParameter("price");  //采购总金额
//		String childrenUser = request.getParameter("childrenUser");//子帐号
//		String newOrderId = request.getParameter("newOrderId"); //创建订单成功后的官网订单号
//		String orderId = request.getParameter("orderId");  //原订单主键ID
//		String isPassuccess = request.getParameter("isPassuccess");  //是否支付成功
//		String isPassenge = request.getParameter("isPassenge");      //是否票号回填
//		String[] passengeMessage = request.getParameterValues("passengeMessage");  // 获取票号回填到系统   格式为:姓名##生份证##票号
//		String payTransactionid = request.getParameter("payTransactionid");       //获取票号回填的交易号 SC时代表联系电话
//		String payStatus = request.getParameter("payStatus");       //获取支付方式
//		String isSuccess = request.getParameter("isSuccess");       //是否完结
//		String isautoB2C = request.getParameter("isautoB2C");       //是否自动出票
//		String ifUsedCoupon = request.getParameter("ifUsedCoupon");       //是否使用红包
//	 * @param billNo
//	 */
//	public static String sendCreateOrderInfo(String result,String message,String price,String childrenUser,String newOrderId
//			,String orderId,String isPassuccess,String isPassenge,String passengeMessage,String payStatus,String payTransactionid
//			,String ifUsedCoupon,String isSuccess, String billNo,String cabin,int requestType){
//		try {
//			String orderUrl = PropertiesUtils.getPropertiesValue("config", "orderUrl");
//			logger.info(orderId + result + ":" + message);
//			StringBuffer buffer = new StringBuffer();
//			buffer.append("<feeye-official>");
//			buffer.append("<official>"+"KNAPP"+"</official> ");
//			buffer.append("<url>"+orderUrl+"</url> ");
//			buffer.append("<type>0</type> ");
//			buffer.append("<method>post</method>");
//			buffer.append("<max>20</max> ");
//			buffer.append("<encod>utf-8</encod> ");
//			buffer.append("<params>");
//			buffer.append("<param name='result'>"+result+"</param>");
//			buffer.append("<param name='message'>"+message+"</param>");
//			buffer.append("<param name='price'>"+price+"</param>");
//			buffer.append("<param name='childrenUser'>"+childrenUser+"</param>");
//			buffer.append("<param name='newOrderId'>"+newOrderId+"</param>");
//			buffer.append("<param name='orderId'>"+orderId+"</param>");
//			buffer.append("<param name='isPassuccess'>"+isPassuccess+"</param>");
//			buffer.append("<param name='isPassenge'>"+isPassenge+"</param>");
//			buffer.append("<param name='passengeMessageOther'>"+passengeMessage+"</param>");
//			buffer.append("<param name='payStatus'>"+payStatus+"</param>");
//			buffer.append("<param name='payTransactionid'>"+payTransactionid+"</param>");
//			buffer.append("<param name='ifUsedCoupon'>"+ifUsedCoupon+"</param>");
//			buffer.append("<param name='isSuccess'>"+isSuccess+"</param>");
//			buffer.append("<param name='billNo'>" + billNo + "</param>");
//			buffer.append("<param name='cabin'>" + cabin + "</param>");
//			buffer.append("<param name='dicountMoney'>" + 0 + "</param>");
//			buffer.append("<param name='requestType'>" + requestType + "</param>");
//			buffer.append("</params>");
//			buffer.append("</feeye-official>");
//
//			String content = OfficialMain.setRequestParams(buffer.toString());
//			if(content != null){
//				String rs1[] = content.split("#@_@#");
//				if(rs1.length == 2){
//					content = rs1[1];
//					return content;
//				}
//				if (rs1.length == 3) {
//					logger.info(rs1[2]);
//					return rs1[2];
//				}
//			}
//
//		} catch (Exception e) {
//			logger.error("error",e);
//		}
//		return null;
//	}
//
//	private String cancel(String url,String id,String childrenUser) {
//		CloseableHttpClient client = null;
//		CloseableHttpResponse response = null;
//		InputStream re = null;
//		HttpGet get = null;
//		HttpPost post = null;
//		String result = null;
//		try {
//			BasicCookieStore cookieStore = new BasicCookieStore();
//			Integer timeout = Integer.parseInt("60000");
//			RequestConfig requestConfig = RequestConfig.custom()
//					.setSocketTimeout(timeout).setConnectTimeout(timeout)
//					.build();
//			client = HttpClients.custom().setDefaultCookieStore(cookieStore)
//					.build();
//			List<BasicNameValuePair> nameValueParis = new ArrayList<BasicNameValuePair>();
//			nameValueParis.add(new BasicNameValuePair("orderId", id));
//			nameValueParis.add(new BasicNameValuePair("codetype", "order"));
//			nameValueParis.add(new BasicNameValuePair("childrenUser", childrenUser));
//			post = new HttpPost(url);
//			post.setEntity(new UrlEncodedFormEntity(nameValueParis,"utf-8"));
//			post.setConfig(requestConfig);
//			response = client.execute(post);
//			result = EntityUtils.toString(response.getEntity(), "utf-8");
//			JSONObject jo = new JSONObject(result);
//			result = jo.getString("msg");
//		} catch (Exception e) {
//			logger.error(e);
//		}finally {
//			try {
//				if (re != null) {
//					re.close();
//				}
//				if (response != null) {
//					response.close();
//				}
//				if (get != null) {
//					get.releaseConnection();
//				}
//				if (post != null) {
//					post.releaseConnection();
//				}
//				if (client != null) {
//					client.close();
//				}
//			} catch (Exception e) {
//				logger.error(e);
//			}
//		}
//		return result;
//	}
//	private String aesInvokeFunction(String function,String param){
//		try {
//			ScriptEngineManager manager = new ScriptEngineManager();
//			ScriptEngine engine = manager.getEngineByName("js");
//			String jsFileName = "C:\\新建文件夹\\aes.min.js";
//			FileReader reader = new FileReader(jsFileName); // 执行指定脚本
//			engine.eval(reader);
//			if (engine instanceof Invocable) {
//				Invocable invoke = (Invocable) engine;
//				param = (String) invoke.invokeFunction(function, param);
//			}
//		} catch (Exception e) {
//			param = "";
//		}
////		logger.info("执行JS获得:" + param);
//		return param;
//	}
//	private String getMemberId(CloseableHttpClient httpclient, String cookie, String orderJson,
//			RequestConfig defaultRequestConfig,String orderId) throws Exception {
//		// 获取用户id
//		HttpPost post = new HttpPost("/ffp/ssologin");
//		String jsonObject = "{\"mode\":\"login\"}";
//		jsonObject = aesInvokeFunction("encrypt", jsonObject);
//		HttpHost target = new HttpHost("higo.flycua.com", 443, "https");
//		StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
//		post.setConfig(defaultRequestConfig);
//		post.setEntity(entity);
//		post.setHeader("Referer", "https://higo.flycua.com/hh/index.html");
//		post.setHeader("Cookie", cookie);
//		post.setHeader("Content-Type", "application/json;charset=UTF-8");
//		post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//		post.setHeader("Host", "higo.flycua.com");
//		CloseableHttpResponse response = httpclient.execute(target, post);
//		String back = EntityUtils.toString(response.getEntity());
//		//outPrint(target,post,null,cookie,jsonObject,back);
//
//		JSONObject json = new JSONObject(back);
//		String errordesc = json.getString("errordesc");
//		errordesc = aesInvokeFunction("decrypt", errordesc);
//		if(StringUtils.isNotEmpty(errordesc)&&errordesc.contains("用户或密码不正确")){
//			return "用户或密码不正确";
//		}
//		JSONObject errordescJson = new JSONObject(errordesc);
//		String memberId = errordescJson.getString("memberId");
//		return memberId;
//	}
//
//	/**
//	 * 获取相同乘机人的乘客id
//	 * @param httpclient
//	 * @param cookie
//	 * @param random
//	 * @param orderJson
//	 * @param defaultRequestConfig
//	 * @return
//	 * @throws Exception
//	 */
//	private List<String> getSamePassengerList(CloseableHttpClient httpclient ,String cookie,String random, String orderJson, RequestConfig defaultRequestConfig, String memberId)throws Exception {
//		List<String> passengerIds = new ArrayList<String>();
//		JSONObject orderJsonObj = new JSONObject(orderJson);
//		JSONArray passengers = orderJsonObj.getJSONArray("passengers");
//		Map<String,String> passengersMap = new HashMap<String,String>();
//		for(int i=0;i<passengers.length();i++){
//			JSONObject passenger = passengers.getJSONObject(i);
//			String passengerName = passenger.getString("passengerName");
//			String idCard = passenger.getString("idcard");
//			passengersMap.put(passengerName.trim() + "@" + idCard.trim(), passengerName + "@" + idCard);
//		}
//
//		//获取常用乘客表
//		HttpPost post = new HttpPost("/ffp/member/profile");
//		String jsonObject = "{\"mode\":\"query\",\"memberId\":\""+memberId+"\"}";
//		jsonObject = aesInvokeFunction("encrypt", jsonObject);
//		HttpHost target = new HttpHost("higo.flycua.com", 443, "https");
//		StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
//		post.setEntity(entity);
//		post.setHeader("Referer","https://higo.flycua.com/hh/html/passenger.html?rand=0.1313124611206259&from=null");
//		post.setHeader("Cookie",cookie);
//		post.setHeader("Content-Type","application/json;charset=UTF-8");
//		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//		post.setHeader("Host","higo.flycua.com");
//		CloseableHttpResponse response = httpclient.execute(target,post);
//		String back = EntityUtils.toString(response.getEntity(), "utf-8");
////		logger.info("获取常用乘机人返回:"+back);
//		//outPrint(target,post,null,cookie,jsonObject,back);
//		JSONObject json = new JSONObject(back);
//		String errordesc = json.getString("errordesc");
//		back = aesInvokeFunction("decrypt", errordesc);
//		if(StringUtils.isNotEmpty(back)&&back.contains("please login first")){
//			passengerIds.add("please login first");
//			return passengerIds;
//		}
//		json = new JSONObject(back);
//		String errorcode = json.getString("errorcode");
//		if(!"0000".equals(errorcode)){
//			return null;
//		}
//		JSONArray passengerArr = json.getJSONArray("passenger");
//		for(int i=0;i<passengerArr.length();i++){
//			JSONObject passengerObj = passengerArr.getJSONObject(i);
//			String name = passengerObj.getString("name");
//			String idno = passengerObj.getString("idno");
//			String id = passengerObj.getString("id");
//			if(passengersMap.get(name.trim() + "@" + idno.trim())!=null){
//				logger.info("常用乘机人已存在"+id);
//				passengerIds.add(id);
//			}
//		}
//		return passengerIds;
//	}
//
//	private String getCookie(CloseableHttpClient httpclient ,String cookie,JSONObject json, RequestConfig defaultRequestConfig) throws Exception {
//		InputStream re = null;
//		String newCookie = "";
//		try {
//			String account = json.getString("account");
//			String userAccount[] = account.split("_");
//			String otheraccount = json.getString("otheraccount");
//			String otherusername = "b";
//			String otherpassword = "b";
//			try {
//				if (otheraccount.contains("_") && otheraccount.split("_").length == 2) {
//					otherusername = otheraccount.split("_")[0];
//					otherpassword = otheraccount.split("_")[1];
//				}
//				if(StringUtils.isEmpty(otherusername)||"null".equals(otherusername)){
//					otherusername = "b";
//				}
//				if(StringUtils.isEmpty(otherpassword)||"null".equals(otherpassword)){
//					otherpassword = "b";
//				}
//			} catch (Exception e) {
//			}
//			if("b".equals(otherusername)&&"b".equals(otherusername)){
//				return getCookieOld(httpclient,cookie,json,defaultRequestConfig);
//			}
//			HttpGet get = new HttpGet("/h5/verify.images");
//			get.setConfig(defaultRequestConfig);
//			HttpHost target = new HttpHost("m.flycua.com", 443, "https");
//			get.setHeader("Referer",
//					"https://m.flycua.com/h5/");
//			get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
//			get.setHeader("Host", "m.flycua.com");
//			get.setHeader("Cookie", cookie);
////			outPrint(target, null, get, cookie, "", "");
//			CloseableHttpResponse response = httpclient.execute(target, get);
//			Header[] headersArr = response.getAllHeaders();
//
//			re = response.getEntity().getContent();
//			OutputStream os = null;
//			String random = "0." + (long) ((Math.random() + 1) * 10000000000000000L);
//			String fileUri = "C://testImg//" + random + ".jpg";
//			os = new FileOutputStream(fileUri);
//			IOUtils.copy(re, os);
//			os.close();
//
//			InputStream is = new FileInputStream(new File(fileUri));
//
//			String validtext = YunSu.getValidCode(is, "3050", otherusername, otherpassword);
//			File file=new File(fileUri);
//			if(file.exists()){
//				file.delete();
//			}
//			logger.info("调用接口获取验证码返回：" + validtext);
//			org.dom4j.Document document = DocumentHelper.parseText(validtext);
//			org.dom4j.Element root = document.getRootElement();
//			String error = root.elementText("Error");
//			String result = "";
//			if (error != null && !"".equals(error)) {
//				result = error;
//				if (result.contains("点数不足")) {
//					return getCookieOld(httpclient, newCookie, json, defaultRequestConfig);
//				}else if(result.contains("密码错误")){
//					return getCookieOld(httpclient, newCookie, json, defaultRequestConfig);
//				}else if(result.contains("密码连续错误")){
//					return getCookieOld(httpclient, newCookie, json, defaultRequestConfig);
//				}
//			} else {
//				result = root.elementText("Result");
//			}
//
//			HttpPost post = new HttpPost("/h5/sso/auth.json");
//			String jsonObject = "{\"userName\":\""+userAccount[0]+"\",\"password\":\""+userAccount[1]+"\",\"verifyCode\":\""+result+"\"}";
//			StringEntity entity = new StringEntity(jsonObject.toString(), Charset.forName("UTF-8"));
//			post.setEntity(entity);
//			post.setHeader("Host", "m.flycua.com");
//			post.setHeader("Cookie", cookie);
//			post.setHeader("Content-Type","application/json;charset=UTF-8");
//			post.setHeader(
//					"Referer",
//					"https://m.flycua.com/h5/");
//			post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
//			response = httpclient.execute(target, post);
//
//			String back = EntityUtils.toString(response.getEntity(), "utf-8");
//			headersArr = response.getAllHeaders();
//
//			for(Header header: headersArr) {
//				if("Set-Cookie".equals(header.getName())) {
//					if(header.getValue().contains(";")){
//						String[] str = header.getValue().split(";");
//						for(String c:str){
//							newCookie += c+";";
//						}
//					}
//				}
//			}
//			cookie = cookie+newCookie;
//			//outPrint(target,post,null,cookie,jsonObject,back);
//			if(!cookie.contains("tokenId")){
//				return "";
//			}
//		} catch (Exception e) {
//			logger.error("error",e);
//			newCookie = "";
//		}finally{
//			if(re!=null) {
//				try {
//					re.close();
//				} catch (Exception e2) {}
//			}
//		}
//		return cookie;
//	}
//
//	private String getCookieOld(CloseableHttpClient httpclient, String cookie, JSONObject json,
//			RequestConfig defaultRequestConfig) throws Exception{
//		String account = json.getString("account");
//		String userAccount[] = account.split("_");
//
//		HttpPost post = new HttpPost("/member/member-login!popLogin.shtml");
//		post.setConfig(defaultRequestConfig);
//		HttpHost target = new HttpHost("www.flycua.com", 443, "https");
//
//		List<NameValuePair> nameValue = new ArrayList<NameValuePair>();
//		nameValue.add(new BasicNameValuePair("pageType", "login_popup_kn.jsp"));
//		nameValue.add(new BasicNameValuePair("password", userAccount[1]));
//		nameValue.add(new BasicNameValuePair("redirectUrl", "http://www.flycua.com/LoginCallback.html"));
//		nameValue.add(new BasicNameValuePair("site", "1"));
//		nameValue.add(new BasicNameValuePair("user", userAccount[0]));
//		nameValue.add(new BasicNameValuePair("validcode", "1111"));
//
//		post.setEntity(new UrlEncodedFormEntity(nameValue, "utf-8"));
//		post.setHeader("Host", "www.flycua.com");
//		post.setHeader("Cookie", cookie);
//		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//		post.setHeader(
//				"Referer",
//				"http://www.flycua.com/member/member-login!login_popup_kn.shtml?redirectUrl=http://www.flycua.com/LoginCallback.html&ltv=1&local=zh_CN");
//
//		CloseableHttpResponse response = httpclient.execute(target, post);
//
//		String back = EntityUtils.toString(response.getEntity(), "utf-8");
//		logger.info("登录返回:"+back);
//		if(StringUtils.isEmpty(back)){
//			return null;
//		}
//		if(back.contains("用户或密码不正确")){
//			return back;
//		}
//		if(back.contains("登录失败")){
//			return back;
//		}
//		Header[] headersArr = response.getAllHeaders();
//		String newCookie = "";
//		for(Header header: headersArr) {
//			if("Set-Cookie".equals(header.getName())) {
//				if(header.getValue().contains(";")){
//					String[] str = header.getValue().split(";");
//					for(String c:str){
//						if(c.contains("CUA_SSO_TOKEN")||c.contains("LOGIN_COOKIE_TIME"))
//						newCookie += c+";";
//					}
//				}
//			}
//		}
//		post = new HttpPost("/h5/sso/loginStatus.json");
//		post.setHeader("Host", "m.flycua.com");
//		post.setHeader("Cookie", newCookie);
//		post.setHeader("Referer","https://m.flycua.com/h5/home.html");
//		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//		target = new HttpHost("m.flycua.com", 443, "https");
//		response = httpclient.execute(target, post);
//
//		back = EntityUtils.toString(response.getEntity(), "utf-8");
//		logger.info("登录返回1:"+back);
//		if(back.contains("请先登录")){
//			return "登录失败";
//		}
//		headersArr = response.getAllHeaders();
//		for(Header header: headersArr) {
//			if("Set-Cookie".equals(header.getName())) {
//				if(header.getValue().contains(";")){
//					String[] str = header.getValue().split(";");
//					for(String c:str){
//						if(c.contains("JSESSIONID")||c.contains("CUA_SSO_TOKEN")||c.contains("LOGIN_COOKIE_TIME"))
//						newCookie += c+";";
//					}
//				}
//			}
//		}
//		return newCookie;
//	}
//
//	public String doParse(String content, String dept, String arrival, String dateString, String flightNo,
//			 String price) {
//		Document document = Jsoup.parse(content);
//		Elements flightList = document.getElementsByClass("flight-list-item-content");
//		String value = "";
//		if (flightList.size() != 0) {
//			for (int i = 0; i < flightList.size(); i++) {
//				Element flightItem = flightList.get(i);
//				Elements flightNos = flightItem.getElementsByClass("flight-no");
//				if (!flightNo.equals(flightNos.get(0).text())) {
//					continue;
//				}
//				Element flightListWrap = document.getElementById("cabins" + (i + 1));
//				Elements ticketPrices = flightListWrap.getElementsByClass("common-FrontColor");
//				for (int j = 0; j < ticketPrices.size(); j++) {
//					String ticketPrice = ticketPrices.get(j).text().split("￥")[1];
//					if(price.contains(".")){
//						price = price.split("\\.")[0];
//					}
//					if (price.equals(ticketPrice)) {
//						Element verify = document.getElementById("verify" + (i + 1) + "_" + (j + 1));
//						value = verify.getElementsByTag("input").first().val();
//					}
//				}
//			}
//			if (StringUtils.isEmpty(value) || value == null) {
//				value = "ERROR@@没有找到相应价格!";
//			}
//		} else {
//			Elements errorTips = document.getElementsByClass("Common-fontWeight");
//			value = "ERROR@@" + errorTips.first().text();
//		}
//		return value;
//	}
//
//
//
//	// 确认提交
//
//	/**
//	 * 推送创单情况 String childrenUser = request.getParameter("childrenUser");//子帐号
//	 * String orderId = request.getParameter("orderId"); //原订单主键ID String
//	 * payStatus = request.getParameter("payStatus"); //获取支付方式
//	 */
//	public static String sendOrderStatus(String childrenUser, String orderId, String status) {
//		try {
//			String orderUrl = PropertiesUtils.getPropertiesValue("config", "orderUrlStatus");
//			logger.info(orderId + status);
//			StringBuffer buffer = new StringBuffer();
//			buffer.append("<feeye-official>");
//			buffer.append("<official>KNAPP</official> ");
//			buffer.append("<url>" + orderUrl + "</url> ");
//			buffer.append("<type>0</type> ");
//			buffer.append("<method>post</method>");
//			buffer.append("<max>20</max> ");
//			buffer.append("<encod>utf-8</encod> ");
//			buffer.append("<params>");
//			buffer.append("<param name='childrenUser'>" + childrenUser + "</param>");
//			buffer.append("<param name='orderId'>" + orderId + "</param>");
//			buffer.append("<param name='orderStatus'>" + status + "</param>");
//
//			buffer.append("</params>");
//			buffer.append("</feeye-official>");
//
//			String content = OfficialMain.setRequestParams(buffer.toString());
//			if (content != null) {
//				String rs1[] = content.split("#@_@#");
//				if (rs1.length == 2) {
//					content = rs1[1];
//					return content;
//				}
//				if (rs1.length == 3) {
//					logger.info(rs1[2]);
//					return rs1[2];
//				}
//			}
//		} catch (Exception e) {
//			logger.error("推送\"" + status + "\"情况异常");
//		}
//		return null;
//	}
//
//}
