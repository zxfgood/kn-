package com.feeye.service;//package com.feeye.service;
//
//import java.io.IOException;
//import java.io.OutputStream;
//import java.net.URI;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.security.cert.CertificateException;
//import java.security.cert.X509Certificate;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//import javax.net.ssl.SSLContext;
//import javax.persistence.criteria.CriteriaBuilder.In;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.http.Header;
//import org.apache.http.HttpHost;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.SSLContextBuilder;
//import org.apache.http.conn.ssl.TrustStrategy;
//import org.apache.http.cookie.Cookie;
//import org.apache.http.impl.client.BasicCookieStore;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.apache.log4j.Logger;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.select.Elements;
//import org.openqa.selenium.By;
//import org.openqa.selenium.Dimension;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.support.ui.ExpectedCondition;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import com.feeye.util.DigestUtil;
//import com.feeye.util.EasyPayUtil;
//import com.feeye.util.MD5Util;
//import com.feeye.util.PhantomjsDriverUtil;
//import com.gargoylesoftware.htmlunit.BrowserVersion;
//import com.gargoylesoftware.htmlunit.History;
//import com.gargoylesoftware.htmlunit.HttpMethod;
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.WebRequest;
//import com.gargoylesoftware.htmlunit.WebWindow;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//
//
//public class OfficialXykPayService {
//
//	private static final Logger logger = Logger.getLogger(OfficialXykPayService.class);
//
//	public String payAllOrder(String url, String cookie, String orderJson,String orderid) {
//		try {
//			JSONObject json = new JSONObject(orderJson);
//			JSONArray flights = json.getJSONArray("flights");
//			String flightNo = flights.getJSONObject(0).getString("flightNo");
//			String account_no = json.getString("account");
//			String deduct_third_code = "";
//			String payType = json.getString("payType")==null?"":json.getString("payType");
//			String payerMobile = json.getString("cvv")==null?"":json.getString("cvv");		//绑定手机号
//			String payMerId = json.getString("creditNo")==null?"":json.getString("creditNo");	//付款方商户编号
//			String aKey = json.getString("payerMobile")==null?"":json.getString("payerMobile");	//密钥
//
//
//			String result = "";
//			if (flightNo.contains("8L")){
//				result = payOrder8L(account_no,
//						deduct_third_code, url, cookie,payType,payerMobile,payMerId,aKey);
//			}else if(flightNo.contains("KN")){
//				result = payOrderKN(account_no,
//						deduct_third_code, url, cookie, payType, payerMobile, payMerId, aKey);
//			}else if(flightNo.contains("MF")){
//				result = payOrderMF(orderJson,payType, url, cookie,orderid);
//			}
//			try {
//				if (!StringUtils.isEmpty(result)) {
////					org.dom4j.Document document = DocumentHelper
////							.parseText(result);
////					org.dom4j.Element root = document.getRootElement();
////					String payResult = root.element("is_success").getText();
////					if ("T".equals(payResult)) {
//					return result;
////					}
////					String errorInfo = root.element("error").getText();
////					return errorInfo;
//				}
//
//			} catch (Exception e) {
//				logger.error("error",e);
//
//			}
//		} catch (Exception e) {
//			logger.error("error",e);
//		}
//
//		return null;
//	}
//
//
//	public String payOrderMU(String orderJson,String url, String cookie, String orderid,String back) {
//		String bankNo = "";
//		try {
//			Document doc = Jsoup.parse(back);
//			String requestId = doc.getElementById("requestId").val();
//			String orderamount = doc.getElementById("orderamount").val();
//			String frpId = doc.getElementById("radio_epos_0").val();
//			bankNo = yeepayCreateOrder(frpId,requestId,orderamount,cookie,orderJson,url);
//		} catch (Exception e) {
//			logger.error("error",e);
//		}
//		return bankNo;
//	}
//
//
//	public String payOrderDR(String orderJson, String cookie, Map<String,String> pay) {
//		CloseableHttpClient client = null;
//		CloseableHttpResponse response = null;
//		HttpPost post = null;
//		try {
//			JSONObject json = new JSONObject(orderJson);
//			String payUsername = json.getString("creditNo");	//会员账号
//			String payPassword = json.getString("cvv");			//会员密码
//			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
//					null, new TrustStrategy() {
//						public boolean isTrusted(X509Certificate[] chain,
//								String authType) throws CertificateException {
//							return true;
//						}
//					}).build();
//
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//					sslContext,
//					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//			BasicCookieStore cookieStore = new BasicCookieStore();
//			client = HttpClients.custom().setSSLSocketFactory(sslsf)
//					.setDefaultCookieStore(cookieStore).build();
//			Integer timeout = Integer.valueOf(Integer.parseInt("40000"));
//
//			RequestConfig.Builder builder = RequestConfig.custom();
//			builder.setSocketTimeout(timeout.intValue());
//			builder.setConnectTimeout(timeout.intValue());
//
//			RequestConfig requestConfig = builder.build();
//			post = new HttpPost("https://www.yeepay.com/app-merchant-proxy/node");
//			post.setConfig(requestConfig);
//			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
//			nameValuePairs.add(new BasicNameValuePair("hmac_safe", pay.get("hmac_safe")));
//			nameValuePairs.add(new BasicNameValuePair("p0_Cmd", pay.get("p0_Cmd")));
//			nameValuePairs.add(new BasicNameValuePair("p1_MerId", pay.get("p1_MerId")));
//			nameValuePairs.add(new BasicNameValuePair("p2_Order", pay.get("p2_Order")));
//			nameValuePairs.add(new BasicNameValuePair("p3_Amt", pay.get("p3_Amt")));
//			nameValuePairs.add(new BasicNameValuePair("p4_Cur", pay.get("p4_Cur")));
//			nameValuePairs.add(new BasicNameValuePair("p5_Pid", pay.get("p5_Pid")));
//			nameValuePairs.add(new BasicNameValuePair("p6_Pcat", pay.get("p6_Pcat")));
//			nameValuePairs.add(new BasicNameValuePair("p7_Pdesc", pay.get("p7_Pdesc")));
//			nameValuePairs.add(new BasicNameValuePair("p8_Url", pay.get("p8_Url")));
//			nameValuePairs.add(new BasicNameValuePair("p9_SAF", pay.get("p9_SAF")));
//			nameValuePairs.add(new BasicNameValuePair("pa_MP", pay.get("pa_MP")));
//			nameValuePairs.add(new BasicNameValuePair("pd_FrpId", pay.get("pd_FrpId")));
//			nameValuePairs.add(new BasicNameValuePair("pm_Period", pay.get("pm_Period")));
//			nameValuePairs.add(new BasicNameValuePair("pn_Unit", pay.get("pn_Unit")));
//			nameValuePairs.add(new BasicNameValuePair("pr_NeedResponse", pay.get("pr_NeedResponse")));
//			post.setEntity(new UrlEncodedFormEntity(nameValuePairs,"GBK"));
//			post.setHeader("Host","www.yeepay.com");
//			post.setHeader("Cookie",cookie);
//			post.setHeader("Referer","https://b2b.rlair.net/b2b/pay/payCash");
//			post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0)	 Gecko/20100101 Firefox/43.0");
//			response = client.execute(post);
//			String back = EntityUtils.toString(response.getEntity(), "utf-8");
////			logger.info("易宝第一个请求返回:"+back);
//			Header[] location = response.getHeaders("Location");
//			String locationValue = "";
//			for (int i = 0; i < location.length; i++) {
//				locationValue = location[i].getValue();
//				logger.info("Location:" + locationValue);
//			}
//			Header[] headersArr = response.getAllHeaders();
//			String newCookie = "";
//			for(Header header:headersArr) {
//				if("Set-Cookie".equals(header.getName())) {
//					newCookie += header.getValue() + ";";
//				}
//			}
//			cookie += newCookie;
//			Map<String,String> resultMap = null;
//			for(int i=0;i<3;i++){
//				try {
//					resultMap = yeePay(orderJson, cookie, locationValue);
//					if(resultMap!=null){
//						break;
//					}
//				} catch (Exception e) {
//					logger.error("error",e);
//					logger.info(pay.get("p2_Order")+"支付异常，重试支付");
//					resultMap = yeePay(orderJson, cookie, locationValue);
//				}
//			}
//			String returnUrl = "";
//			try {
//				returnUrl = resultMap.get("returnUrl");
//				if(StringUtils.isNotEmpty(returnUrl)){
//					HttpGet get = new HttpGet(returnUrl);
//					get.setConfig(requestConfig);
//					get.setHeader("Content-Type","text/html;charset=UTF-8");
//					get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//					get.setHeader("Host","m.flycua.com");
//					get.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//					get.setHeader("Accept-Encoding","gzip, deflate, br");
//					get.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//					get.setHeader("Connection","keep-alive");
//					get.setHeader("Upgrade-Insecure-Requests","1");
//					get.setHeader("Cookie",cookie);
//					response = client.execute(get);
//					back = EntityUtils.toString(response.getEntity(),"utf-8");
//					logger.error("通知航司返回："+back);
//				}
//				logger.info("bankNo:"+resultMap.get("bankNo") );
//				logger.info("money:"+resultMap.get("money") );
//			} catch (Exception e) {}
//			if(StringUtils.isNotEmpty(returnUrl)){
//				return "success";
//			}
//			if(StringUtils.isNotEmpty(resultMap.get("error"))){
//				return "error:"+resultMap.get("error");
//			}
//		}catch (Exception e) {
//			logger.error("error",e);
//		}
//		return null;
//	}
//
//	public Map<String, String> payOrderGT(String orderJson,String cookie,Map<String,String> payParam) throws Exception{
//		JSONObject json = new JSONObject(orderJson);
////		String payMerId = json.getString("creditNo");
//		String buyer_email = json.getString("cvv");
//		String order = payParam.get("order");
//		String orderNo = payParam.get("orderNo");	//商家订单号
//		String seller_customerNO = "100000000077704";	//卖方客户号
//		if(StringUtils.isEmpty(orderNo)){
//			orderNo = "";
//		}
//		String tatolpay = payParam.get("tatolpay");	//金额
//		if(StringUtils.isEmpty(tatolpay)){
//			tatolpay = "10.0";
//		}
//		String noPassKey = json.getString("deduct_third_code");
//		CloseableHttpClient client = null;
//		CloseableHttpResponse response = null;
//		HttpPost post = null;
//		OutputStream outStream = null;
//		Map<String, String> map = new HashMap<String,String>();
//		try {
//			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
//					null, new TrustStrategy() {
//						public boolean isTrusted(X509Certificate[] chain,
//								String authType) throws CertificateException {
//							return true;
//						}
//					}).build();
//
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//					sslContext,
//					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//			BasicCookieStore cookieStore = new BasicCookieStore();
//			client = HttpClients.custom().setSSLSocketFactory(sslsf)
//					.setDefaultCookieStore(cookieStore).build();
//			Integer timeout = Integer.valueOf(Integer.parseInt("30000"));
//
//			RequestConfig.Builder builder = RequestConfig.custom();
//			builder.setSocketTimeout(timeout.intValue());
//			builder.setConnectTimeout(timeout.intValue());
//
//			RequestConfig requestConfig = builder.build();
//			/*
//			 * 服务器密钥配置  signValue
//			 * 调用地址配置  ysPayUrl
//			 * 回调地址配置 callBackUrl
//			 */
//
//			String partner = "100000000082804";
//
//			if(StringUtils.isEmpty(buyer_email)){
//				buyer_email = "";
//			}
//			if(StringUtils.isEmpty(noPassKey)){
//				noPassKey = "";
//			}
//			Map<String,String> signMap = new HashMap<String,String>();
//			signMap.put("Seller_customerNO", seller_customerNO);
//			signMap.put("amount", tatolpay);
//			signMap.put("buyer_email", buyer_email);
//			signMap.put("out_trade_no", order);
//			signMap.put("partner", partner);
//			signMap.put("service", "tradeid_payment_nopass");
//			String signParam = getSign(signMap);
//			String safeKey = "90530122db26g13gg8a5da1g37eg10c3998f0a7283e094gge750c1258b211a85";
//			signParam = signParam+noPassKey+safeKey;
//			logger.info("待加密参数:"+signParam);
//			/*
//			 * 数据验名
//			 */
//			String sign = MD5Util.getMD5(signParam);
//			String url = "http://cashier.bhecard.com/TradeIdAccess_Nopass.do";
//			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
//			nameValuePairs.add(new BasicNameValuePair("service", "tradeid_payment_nopass"));
//			nameValuePairs.add(new BasicNameValuePair("partner", partner));
//			nameValuePairs.add(new BasicNameValuePair("sign", sign));
//			nameValuePairs.add(new BasicNameValuePair("sign_type", "MD5"));
//			nameValuePairs.add(new BasicNameValuePair("buyer_email", buyer_email));
//			nameValuePairs.add(new BasicNameValuePair("amount", tatolpay));
//			nameValuePairs.add(new BasicNameValuePair("out_trade_no", order));
//			nameValuePairs.add(new BasicNameValuePair("Seller_customerNO", seller_customerNO));
//			logger.info("请求参数:"+nameValuePairs.toString());
//			post = new HttpPost(url);
//			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//			post.setConfig(requestConfig);
//			response = client.execute(post);
//			String back = EntityUtils.toString(response.getEntity(), "utf-8");
//			logger.info("易生支付返回:"+back);
//			//返回的不是标准json数据，用易生提供方法解析
//			Map<String,String> resultMap = EasyPayUtil.convertToMap(back);
//			String respcode = resultMap.get("respcode");
//			if(StringUtils.isEmpty(respcode)){
//				return null;
//			}
//			if("01".equals(respcode)){
//				map.put("errorMsg", resultMap.get("respmsg"));
//				return map;
//			}
//			if("00".equals(respcode)){
//				map.put("trxId", resultMap.get("trade_id"));
//			}
//			try {
//				String return_url = resultMap.get("return_url");
//				URI uri = URI.create(return_url.replaceAll(" ", "%20"));
//				StringBuffer bu = new StringBuffer();
//				for(String param : uri.getQuery().split("&")) {
//					if(param.split("\\=").length>1) {
//						bu.append(param.split("\\=")[0]).append("=").append(URLEncoder.encode(param.split("\\=")[1],"utf-8"));
//					}
//					bu.append("&");
//				}
//				bu.delete(bu.length()-1, bu.length());
//				String locationValue = "https://"+uri.getHost()+uri.getPath()+"?"+bu.toString();
//				HttpGet get = new HttpGet(locationValue);
//				get.setConfig(requestConfig);
//				response = client.execute(get);
//				back = EntityUtils.toString(response.getEntity(), "utf-8");
//				logger.info("通知航司后返回:"+back);
//			} catch (Exception e) {
//				logger.error("error",e);
//			}
//		}catch (Exception e) {
//			logger.error("error",e);
//		}
//		return map;
//	}
//	public static void main(String[] args) throws Exception{
//		String return_url = "https://gt.hnair.com/gt/order/frontend/myorder/easyCardCallback.do?gmt_create=2018-09-14 15:01:11&seller_email=guilinhangkong1@163.com&subject=5064722&input_charset=utf-8&sign=21c2f8ed06eb7e94349a5fbbf16fc041&discount=0&body=E-Ticket Order:2018091445064722&buyer_id=100000000082804&is_success=T&notify_id=2018091401033030&notify_type=WAIT_TRIGGER&price=10.00&total_fee=10.00&trade_status=TRADE_FINISHED&sign_type=MD5&seller_id=100000000077704&is_total_fee_adjust=0&buyer_email=yndeao@126.com&notify_time=2018-09-14 15:01:11&gmt_payment=2018-09-14 15:01:11&quantity=1&gmt_logistics_modify=2018-09-14 15:01:11&version=N2&payment_type=1&out_trade_no=2018091445064722&trade_no=2018091401033030&seller_actions=SEND_GOODS";
//		URI uri = URI.create(return_url.replaceAll(" ", "%20"));
//		StringBuffer bu = new StringBuffer();
//		for(String param : uri.getQuery().split("&")) {
//			if(param.split("\\=").length>1) {
//				bu.append(param.split("\\=")[0]).append("=").append(URLEncoder.encode(param.split("\\=")[1],"utf-8"));
//			}
//			bu.append("&");
//		}
//		bu.delete(bu.length()-1, bu.length());
//		String locationValue = "https://"+uri.getHost()+uri.getPath()+"?"+bu.toString();
//		System.out.println(locationValue);
//	}
//	private String changeIdTypeKn(String idCardType){
//		String idCardTypeNo = "0";
//		if("身份证".equals(idCardTypeNo)){
//			idCardTypeNo = "0";
//		}else if("护照".equals(idCardTypeNo)){
//			idCardTypeNo = "1";
//		}else if("军官证".equals(idCardTypeNo)){
//			idCardTypeNo = "2";
//		}else if("士兵证".equals(idCardTypeNo)){
//			idCardTypeNo = "3";
//		}else if("港澳台通行证".equals(idCardTypeNo)){
//			idCardTypeNo = "4";
//		}else if("户口本".equals(idCardTypeNo)){
//			idCardTypeNo = "6";
//		}else if("其他".equals(idCardTypeNo)){
//			idCardTypeNo = "7";
//		}else if("外国人居留证".equals(idCardTypeNo)){
//			idCardTypeNo = "12";
//		}
//		return idCardTypeNo;
//	}
//	public static Map<String, String> getPayParamKn(String url, String cookie , String payType) {
//		CloseableHttpClient client = null;
//		CloseableHttpResponse response = null;
//		HttpGet get = null;
//		OutputStream outStream = null;
//		try {
//			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
//					null, new TrustStrategy() {
//						public boolean isTrusted(X509Certificate[] chain,
//								String authType) throws CertificateException {
//							return true;
//						}
//					}).build();
//
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//					sslContext,
//					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//			BasicCookieStore cookieStore = new BasicCookieStore();
//			client = HttpClients.custom().setSSLSocketFactory(sslsf)
//					.setDefaultCookieStore(cookieStore).build();
//			Integer timeout = Integer.valueOf(Integer.parseInt("12000"));
//
//			RequestConfig.Builder builder = RequestConfig.custom();
//			builder.setSocketTimeout(timeout.intValue());
//			builder.setConnectTimeout(timeout.intValue());
//
//			RequestConfig requestConfig = builder.build();
//
//			get = new HttpGet(url);
//			get.setHeader("Referer", url);
//			get.setHeader("Cookie", cookie);
//			get.setConfig(requestConfig);
//			response = client.execute(get);
//
//			String text = EntityUtils.toString(response.getEntity(), "utf-8");
//			response.close();
//			logger.info("重定向后的内容:"+text);
//
//			List<Cookie> listCookie = cookieStore.getCookies();
//			StringBuffer buf = new StringBuffer();
//
//			if (null != listCookie && listCookie.size() > 0) {
//				for (int i = 0; i < listCookie.size(); i++) {
//					buf.append(listCookie.get(i).getName() + "=" + listCookie.get(i).getValue() + ";");
//				}
//			}
//			cookie = buf.toString();
//
//			org.jsoup.nodes.Document doc = Jsoup.parse(text.toString());
//			Elements orderIds = doc.getElementsByClass("order-num");
//			String orderno = "";
//			String countDownNum = "";
//			String dynamicPointPayNum = "";
//			if(orderIds!=null){
//				orderno = orderIds.get(0).text();
//			}
//
//			Elements countDown = doc.getElementsByAttributeValue("name",
//					"countDown");
//			if (countDown.size() > 0) {
//				countDownNum = countDown.get(0).attr("value");
//			}
//
//			Elements dynamicPointPay = doc.getElementsByAttributeValue("name",
//					"dynamicPointPay");
//			if (dynamicPointPay.size() > 0) {
//				dynamicPointPayNum = dynamicPointPay.get(0).attr("value");
//			}
//
//			String pay_page_noamount = doc.getElementById("pay_page_noamount").val();
//			String payInfoPayType = doc.getElementById("payInfoPayType").val();
//			HttpPost post = new HttpPost(
//					"https://passport.flycua.com/unipay/preparepay/pay!doPay.shtml");
//
//			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
//			nameValuePairs.add(new BasicNameValuePair("accountId", ""));
//			nameValuePairs.add(new BasicNameValuePair("assignBank", ""));
//			nameValuePairs.add(new BasicNameValuePair("bankname", "YEEPAYSHARE"));
//			nameValuePairs.add(new BasicNameValuePair("broker_ecard_bin", ""));
//			nameValuePairs.add(new BasicNameValuePair("card_no", ""));
//			nameValuePairs.add(new BasicNameValuePair("card_no_qp", "信用卡/储蓄卡/中银卡"));
//			nameValuePairs.add(new BasicNameValuePair("cardname", "CHINAPAYQP"));
//			nameValuePairs.add(new BasicNameValuePair("countDown", countDownNum));
//			nameValuePairs.add(new BasicNameValuePair("cvv_code", ""));
//			nameValuePairs.add(new BasicNameValuePair("dynamicPointPay", dynamicPointPayNum));
//			nameValuePairs.add(new BasicNameValuePair("ecard_bin", ""));
//			nameValuePairs.add(new BasicNameValuePair("ecoupon_bin", ""));
//			nameValuePairs.add(new BasicNameValuePair("email", ""));
//			nameValuePairs.add(new BasicNameValuePair("id_no", ""));
//			nameValuePairs.add(new BasicNameValuePair("id_type", "0"));
//			nameValuePairs.add(new BasicNameValuePair("month", "01"));
//			nameValuePairs.add(new BasicNameValuePair("new_broker_bin", ""));
//			nameValuePairs.add(new BasicNameValuePair("owner_mobile", ""));
//			nameValuePairs.add(new BasicNameValuePair("owner_name", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.bankCode", "YEEPAYSHARE"));
//			nameValuePairs.add(new BasicNameValuePair("payBank.bankGate", "YEEPAYSHARE"));
//			nameValuePairs.add(new BasicNameValuePair("payBank.bankSubCode", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.cardBin", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.couponNo", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.cvvNo", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.effectMonth", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.effectYear", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.email", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.idNo", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.idType", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.ownerMobile", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.ownerName", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.payType", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.pointPass", ""));
//			nameValuePairs.add(new BasicNameValuePair("payBank.promoId", ""));
//			nameValuePairs.add(new BasicNameValuePair("payInfo.payType", payInfoPayType));
//			nameValuePairs.add(new BasicNameValuePair("pay_page_noamount", pay_page_noamount));
//			nameValuePairs.add(new BasicNameValuePair("pay_password", ""));
//			nameValuePairs.add(new BasicNameValuePair("quickInfo.mobileNo", ""));
//			nameValuePairs.add(new BasicNameValuePair("quickInfoJsonDTO.SMSNo", ""));
//			nameValuePairs.add(new BasicNameValuePair("quickInfoJsonDTO.bankCode", ""));
//			nameValuePairs.add(new BasicNameValuePair("quickInfoJsonDTO.bankId", ""));
//			nameValuePairs.add(new BasicNameValuePair("quickInfoJsonDTO.bankName", ""));
//			nameValuePairs.add(new BasicNameValuePair("quickInfoJsonDTO.cardIndex", ""));
//			nameValuePairs.add(new BasicNameValuePair("quickInfoJsonDTO.cardNo", ""));
//			nameValuePairs.add(new BasicNameValuePair("quickInfoJsonDTO.cardType", ""));
//			nameValuePairs.add(new BasicNameValuePair("quickInfoJsonDTO.firstIsSecond", ""));
//			nameValuePairs.add(new BasicNameValuePair("quickInfoJsonDTO.logoURL", ""));
//			nameValuePairs.add(new BasicNameValuePair("quickInfoJsonDTO.onlyId", ""));
//			nameValuePairs.add(new BasicNameValuePair("quickInfoJsonDTO.quickStatus", ""));
//			nameValuePairs.add(new BasicNameValuePair("score_bin", ""));
//			nameValuePairs.add(new BasicNameValuePair("score_coupon_bin", ""));
//			nameValuePairs.add(new BasicNameValuePair("score_kong_bin", ""));
//			nameValuePairs.add(new BasicNameValuePair("user_lastBank", ""));
//			nameValuePairs.add(new BasicNameValuePair("user_usedBank", ""));
//			nameValuePairs.add(new BasicNameValuePair("year", "2018"));
//
//			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//
//			post.setHeader("Referer", url);
//			post.setHeader("Host", "passport.flycua.com");
//			post.setHeader("Cookie", cookie);
//			response = client.execute(post);
//			text = EntityUtils.toString(response.getEntity(), "utf-8");
//			logger.info("重定向后下一个链接返回的内容:"+text);
//			listCookie = cookieStore.getCookies();
//			buf = new StringBuffer();
//
//			if (null != listCookie && listCookie.size() > 0) {
//				for (int i = 0; i < listCookie.size(); i++) {
//					buf.append(listCookie.get(i).getName() + "=" + listCookie.get(i).getValue() + ";");
//				}
//			}
//			cookie = buf.toString();
//			doc = Jsoup.parse(text.toString());
//
//			String AppTypeString = "";
//			Elements AppType = doc.getElementsByAttributeValue("name",
//					"AppType");
//			if (AppType.size() > 0) {
//				AppTypeString = AppType.get(0).attr("value");
//			}
//			String BillNoString = "";
//			Elements BillNo = doc.getElementsByAttributeValue("name",
//					"BillNo");
//			if (BillNo.size() > 0) {
//				BillNoString = BillNo.get(0).attr("value");
//			}
//			String Ext1String = "";
//			Elements Ext1 = doc.getElementsByAttributeValue("name",
//					"Ext1");
//			if (Ext1.size() > 0) {
//				Ext1String = Ext1.get(0).attr("value");
//			}
//			String Ext2String = "";
//			Elements Ext2 = doc.getElementsByAttributeValue("name",
//					"Ext2");
//			if (Ext2.size() > 0) {
//				Ext2String = Ext2.get(0).attr("value");
//			}
//			String MsgString = "";
//			Elements Msg = doc.getElementsByAttributeValue("name",
//					"Msg");
//			if (Msg.size() > 0) {
//				MsgString = Msg.get(0).attr("value");
//			}
//			String OrderAmountString = "";
//			Elements OrderAmount = doc.getElementsByAttributeValue("name",
//					"OrderAmount");
//			if (OrderAmount.size() > 0) {
//				OrderAmountString = OrderAmount.get(0).attr("value");
//			}
//			String OrderCurtypeString = "";
//			Elements OrderCurtype = doc.getElementsByAttributeValue("name",
//					"OrderCurtype");
//			if (OrderCurtype.size() > 0) {
//				OrderCurtypeString = OrderCurtype.get(0).attr("value");
//			}
//			String OrderDateString = "";
//			Elements OrderDate = doc.getElementsByAttributeValue("name",
//					"OrderDate");
//			if (OrderDate.size() > 0) {
//				OrderDateString = OrderDate.get(0).attr("value");
//			}
//			String OrderNoString = "";
//			Elements OrderNo = doc.getElementsByAttributeValue("name",
//					"OrderNo");
//			if (OrderNo.size() > 0) {
//				OrderNoString = OrderNo.get(0).attr("value");
//			}
//			String OrderTimeString = "";
//			Elements OrderTime = doc.getElementsByAttributeValue("name",
//					"OrderTime");
//			if (OrderTime.size() > 0) {
//				OrderTimeString = OrderTime.get(0).attr("value");
//			}
//			String OrderTypeString = "";
//			Elements OrderType = doc.getElementsByAttributeValue("name",
//					"OrderType");
//			if (OrderType.size() > 0) {
//				OrderTypeString = OrderType.get(0).attr("value");
//			}
//			String OrderinfoString = "";
//			Elements Orderinfo = doc.getElementsByAttributeValue("name",
//					"Orderinfo");
//			if (Orderinfo.size() > 0) {
//				OrderinfoString = Orderinfo.get(0).attr("value");
//			}
//			String OrgIdString = "";
//			Elements OrgId = doc.getElementsByAttributeValue("name",
//					"OrgId");
//			if (OrgId.size() > 0) {
//				OrgIdString = OrgId.get(0).attr("value");
//			}
//			String PaytypeString = "";
//			Elements Paytype = doc.getElementsByAttributeValue("name",
//					"Paytype");
//			if (Paytype.size() > 0) {
//				PaytypeString = Paytype.get(0).attr("value");
//			}
//			String ReturnIdString = "";
//			Elements ReturnId = doc.getElementsByAttributeValue("name",
//					"ReturnId");
//			if (ReturnId.size() > 0) {
//				ReturnIdString = ReturnId.get(0).attr("value");
//			}
//			String SIGNATUREString = "";
//			Elements SIGNATURE = doc.getElementsByAttributeValue("name",
//					"SIGNATURE");
//			if (SIGNATURE.size() > 0) {
//				SIGNATUREString = SIGNATURE.get(0).attr("value");
//			}
//			String ordernameString = "";
//			Elements ordername = doc.getElementsByAttributeValue("name",
//					"ordername");
//			if (ordername.size() > 0) {
//				ordernameString = ordername.get(0).attr("value");
//			}
//			String VersionString = "";
//			Elements Version = doc.getElementsByAttributeValue("name",
//					"Version");
//			if (Version.size() > 0) {
//				VersionString = Version.get(0).attr("value");
//			}
//			String usridString = "";
//			Elements usrid = doc.getElementsByAttributeValue("name",
//					"usrid");
//			if (usrid.size() > 0) {
//				usridString = usrid.get(0).attr("value");
//			}
//			response.close();
//			post = new HttpPost(
//					"https://easypay.travelsky.com/easypay/airlinepay.servlet");
//			nameValuePairs = new ArrayList<BasicNameValuePair>();
//			nameValuePairs.add(new BasicNameValuePair("AppType", AppTypeString));
//			nameValuePairs.add(new BasicNameValuePair("BankId", "YEEPAYSHARE"));
//			nameValuePairs.add(new BasicNameValuePair("BillNo", BillNoString));
//			nameValuePairs.add(new BasicNameValuePair("Ext1", Ext1String));
//			nameValuePairs.add(new BasicNameValuePair("Ext2", Ext2String));
//			nameValuePairs.add(new BasicNameValuePair("Insurance", ""));
//			nameValuePairs.add(new BasicNameValuePair("Lan", "CN"));
//			nameValuePairs.add(new BasicNameValuePair("Msg", MsgString));
//			nameValuePairs.add(new BasicNameValuePair("OrderAmount", OrderAmountString));
//			nameValuePairs.add(new BasicNameValuePair("OrderCurtype", OrderCurtypeString));
//			nameValuePairs.add(new BasicNameValuePair("OrderDate", OrderDateString));
//			nameValuePairs.add(new BasicNameValuePair("OrderNo", OrderNoString));
//			nameValuePairs.add(new BasicNameValuePair("OrderTime", OrderTimeString));
//			nameValuePairs.add(new BasicNameValuePair("OrderType", OrderTypeString));
//			nameValuePairs.add(new BasicNameValuePair("Orderinfo", OrderinfoString));
//			nameValuePairs.add(new BasicNameValuePair("OrgId", OrgIdString));
//			nameValuePairs.add(new BasicNameValuePair("Otherfee", ""));
//			nameValuePairs.add(new BasicNameValuePair("Paytype", PaytypeString));
//			nameValuePairs.add(new BasicNameValuePair("Product", ""));
//			nameValuePairs.add(new BasicNameValuePair("Productid", ""));
//			nameValuePairs.add(new BasicNameValuePair("ReturnId", ReturnIdString));
//			nameValuePairs.add(new BasicNameValuePair("SIGNATURE", SIGNATUREString));
//			nameValuePairs.add(new BasicNameValuePair("Tax", ""));
//			nameValuePairs.add(new BasicNameValuePair("Ticketamount", ""));
//			nameValuePairs.add(new BasicNameValuePair("Version", VersionString));
//			nameValuePairs.add(new BasicNameValuePair("gateid", ""));
//			nameValuePairs.add(new BasicNameValuePair("ordername", ordernameString));
//			nameValuePairs.add(new BasicNameValuePair("username", ""));
//			nameValuePairs.add(new BasicNameValuePair("usrid", usridString));
//
//			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//
//			post.setHeader("Referer", url);
//			post.setHeader("Host", "easypay.travelsky.com");
//			post.setHeader("Cookie", cookie);
//			response = client.execute(post);
//			text = EntityUtils.toString(response.getEntity(), "utf-8");
//			logger.info(text);
//			doc = Jsoup.parse(text.toString());
//
//			String p1_MerIdString = "";
//			Elements p1_MerId = doc.getElementsByAttributeValue("name",
//					"p1_MerId");
//			if (p1_MerId.size() > 0) {
//				p1_MerIdString = p1_MerId.get(0).attr("value");
//			}
//			String p2_OrderString = "";
//			Elements p2_Order = doc.getElementsByAttributeValue("name",
//					"p2_Order");
//			if (p2_Order.size() > 0) {
//				p2_OrderString = p2_Order.get(0).attr("value");
//			}
//			String p3_AmtString = "";
//			Elements p3_Amt = doc.getElementsByAttributeValue("name",
//					"p3_Amt");
//			if (p3_Amt.size() > 0) {
//				p3_AmtString = p3_Amt.get(0).attr("value");
//			}
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("p1_MerId", p1_MerIdString);
//			map.put("p3_Amt", p3_AmtString);	//总金额
//			map.put("orderno", orderno);
//			return map;
//		} catch (Exception e) {
//			logger.error("error",e);
//
//		} finally {
//			try {
//				if (outStream != null) {
//					outStream.close();
//				}
//				if (response != null) {
//					response.close();
//				}
//				if (get != null) {
//					get.releaseConnection();
//				}
//				if (client != null)
//					client.close();
//			} catch (Exception e) {
//				logger.error("error",e);
//			}
//		}
//
//		return null;
//	}
//	public String payOrderKN(String account_no,
//			String deduct_third_code, String orderUrl, String cookie,String payType,String payerMobile,
//			String payMerId,String aKey) {
//		try {
//			Map<String, String> map = new HashMap<String, String>();
//			map = getPayParamKn(orderUrl, cookie,payType);
//			String newS = "&p1_MerId="+map.get("p1_MerId")+"&orderno="+map.get("orderno")+"&p3_Amt="+map.get("p3_Amt");
//			String str = bind(newS,payMerId,aKey,"KN");
//			if(str.contains("@@")){
//				String[] str_split = str.split("@@");
//				String r1_PayMerId = str_split[0];
////				String r1_RequestId = str_split[1];
//				String r2_CardNo = str_split[2];
//				String r3_Cvv = str_split[3];
//				String date = str_split[4];
//				String result = pay(newS,r2_CardNo,r3_Cvv,payerMobile,r1_PayMerId,date,aKey)+"@#@"+map.get("orderno")+"@#@"+map.get("p3_Amt");
//				if(StringUtils.isEmpty(result)){
//					return "orderno:"+map.get("orderno");
//				}
//				return result;
//			}else {
//				return "orderno:"+map.get("orderno");
//			}
//		} catch (Exception e) {
//			logger.error("error",e);
//		}
//		return null;
//	}
//	public String payOrderMF(String orderJson,String payType,
//			String orderUrl, String cookie, String orderid) {
//		String str = null;
//		try {
//			str = getPayParamMF(orderUrl, cookie,payType,orderid,orderJson);
//		} catch (Exception e) {
//			logger.error("error",e);
//		}
//		return str;
//	}
//	public String payOrder8L(String account_no,
//			String deduct_third_code, String orderUrl, String cookie,String payType,String payerMobile,
//			String payMerId,String aKey) {
//		String result="";
//		try {
//			String curl = getPayParam8L(orderUrl, cookie,payType);
//			String[] cc = curl.split("#_#");
//
//			String[] ss = cc[0].split("\\?");
//
//			//重试获取支付请求信息
//
//			int j=0;
//			while(j<5&&ss.length!=2){
//				curl=getPayParam8L(orderUrl, cookie,payType);
//				cc=curl.split("#_#");
//				ss=cc[0].split("\\?");
//				if(ss.length==2){
//					break;
//				}
//				j++;
//			}
//			if(ss.length==2){
//				logger.info(ss[1]);
//			}else{
//				return "获取支付请求信息失败";
//			}
//
//
//			String originalData = ss[1];
//			String str = bind(ss[1],payMerId,aKey,"8L");
//			//添加绑卡超时重试
//			if(str.contains("绑卡超时")){
//				int i=0;
//				int retrycount=5;
//				while(i<retrycount){
//					str=bind(ss[1],payMerId,aKey,"8L");
//					if(!str.contains("绑卡超时")){
//						break;
//					}
//					i++;
//				}
//			}
//			result=str;
//			if(str.contains("@@")){
//				String[] str_split = str.split("@@");
//				String r1_PayMerId = str_split[0];
////				String r1_RequestId = str_split[1];
//				String r2_CardNo = str_split[2];
//				String r3_Cvv = str_split[3];
//				String date = str_split[4];
//				return pay(originalData,r2_CardNo,r3_Cvv,payerMobile,r1_PayMerId,date,aKey);
//			}
//		} catch (Exception e) {
//			logger.error("error",e);
//		}
//		return result;
//	}
//	private String bind(String newS,String payMerId,String aKey,String airHS) {
//		//news{&p1_MerId=p1_MerId&p2_AirRequestId=p2_AirRequestId&p3_Amt=p3_Amt}
//		String[] data = newS.split("&");
//		CloseableHttpClient client = null;
//		CloseableHttpResponse response = null;
//		HttpPost post = null;
//		String result="";
//		try {
//			BasicCookieStore cookieStore = new BasicCookieStore();
//			client = HttpClients.custom().setDefaultCookieStore(cookieStore)
//					.build();
//			Integer timeout = Integer.valueOf(Integer.parseInt("12000"));
//
//			RequestConfig.Builder builder = RequestConfig.custom();
//			builder.setSocketTimeout(timeout.intValue());
//			builder.setConnectTimeout(timeout.intValue());
//
//			RequestConfig requestConfig = builder.build();
//
//
//			String p0_Cmd = "CNetPayBindCard";
//			String p1_MerId = data[1].split("=")[1];				//请求方商户编号 = 付款方商户编号
//			String p1_PayMerId = payMerId;							//付款方商户编号
//			String p1_RequestId = System.currentTimeMillis()+""; 	//商户请求
//			String p2_AirRequestId = data[2].split("=")[1]; 		//航司订单号
//			String p3_Amt = data[3].split("=")[1];					//支付金额
//			String p4_Cur = "CNY";									//交易币种
//			String p3_BussCode = "B2C";								//业务场景
//			String p4_AirCode = airHS;								//航司二字码
//			String p6_NotifyUrl = "";								//后台通知地址
//			String p7_BussInfo = "";								//业务信息
//			String p8_CardType = ""; 								//卡种
//			String p9_BankId = "CMBCHINA";							//银行
//
//			p1_MerId = p1_MerId == null ?"":p1_MerId;
//			p1_PayMerId = p1_PayMerId == null ?"":p1_PayMerId;
//			p2_AirRequestId = p2_AirRequestId == null ?"":p2_AirRequestId;
//			p3_Amt = p3_Amt == null ?"":p3_Amt;
//
//			String avalue = p0_Cmd + p1_PayMerId + p1_PayMerId + p1_RequestId + p2_AirRequestId
//						+ p3_Amt + p4_Cur + p3_BussCode + p4_AirCode + p6_NotifyUrl + p7_BussInfo;
//
//			logger.info("绑定参数:"+avalue);
//
//			String hmac = DigestUtil.hmacSign(avalue, aKey);
//
//			List<BasicNameValuePair> nameValueParis = new ArrayList<BasicNameValuePair>();
//			nameValueParis.add(new BasicNameValuePair("p0_Cmd", p0_Cmd));
//			nameValueParis.add(new BasicNameValuePair("p1_MerId", p1_PayMerId));
//			nameValueParis.add(new BasicNameValuePair("p1_PayMerId", p1_PayMerId));
//			nameValueParis.add(new BasicNameValuePair("p1_RequestId", p1_RequestId));
//			nameValueParis.add(new BasicNameValuePair("p2_AirRequestId", p2_AirRequestId));
//			nameValueParis.add(new BasicNameValuePair("p3_Amt", p3_Amt));
//			nameValueParis.add(new BasicNameValuePair("p4_Cur", p4_Cur));
//			nameValueParis.add(new BasicNameValuePair("p3_BussCode", p3_BussCode));
//			nameValueParis.add(new BasicNameValuePair("p4_AirCode", p4_AirCode));
////			nameValueParis.add(new BasicNameValuePair("p6_NotifyUrl", p6_NotifyUrl));
////			nameValueParis.add(new BasicNameValuePair("p7_BussInfo", p7_BussInfo));
////			nameValueParis.add(new BasicNameValuePair("p8_CardType", p8_CardType));
//			nameValueParis.add(new BasicNameValuePair("p9_BankId", p9_BankId));
//			nameValueParis.add(new BasicNameValuePair("hmac", hmac));
//
//			post = new HttpPost("https://www.yeepay.com/app-merchant-proxy/command.action");
//
//			post.setEntity(new UrlEncodedFormEntity(nameValueParis,"UTF-8"));
//			post.setConfig(requestConfig);
//			response = client.execute(post);
//			String text = EntityUtils.toString(response.getEntity(),"utf-8");
//			logger.info("易宝绑卡接口返回数据：" + text);
//			String[] p = text.split("\n");
//			if(p.length < 3){
//				String errMsg = p[1].split("=")[1];
//				logger.info("易宝绑卡接口返回错误数据:"+errMsg);
//				return "绑卡失败，原因："+errMsg;
//			}
//			if(p[2].split("=")[1].equals("1")){
//				String r1_PayMerId = p[3].split("=")[1];
//				String r1_RequestId = p[4].split("=")[1];
//				String r2_CardNo = p[5].split("=")[1];
//				String r3_Cvv = p[6].split("=")[1];
//				String date = p[7].split("=")[1];
//				logger.info("绑定接口返回数据解析:"+r1_PayMerId + "@@" + r1_RequestId + "@@" + r2_CardNo + "@@" + r3_Cvv + "@@" + date);
//				return r1_PayMerId + "@@" + r1_RequestId + "@@" + r2_CardNo + "@@" + r3_Cvv + "@@" + date;
//			}else {
//				logger.info("易宝未配置C网权限");
//				return "易宝未配置C网权限";
//			}
//		} catch (Exception e) {
//			logger.error("error",e);
//			result= "卡号绑定失败 ,绑卡超时";
//		} finally {
//			try {
//				if(response!=null){
//					response.close();
//				}
//				if(client != null){
//					client.close();
//				}
//			} catch (IOException e) {
//				logger.error("error",e);
//			}
//		}
//		return result;
//	}
//	private String pay(String originalData,String account_no,String cvv,String payerMobile,
//			String r1_MerId,String date,String aKey) {
//			String[] data = originalData.split("&");
//			CloseableHttpClient client = null;
//			CloseableHttpResponse response = null;
//			HttpPost post = null;
//
//
//			String url = "https://www.yeepay.com/app-merchant-proxy/controller.action";
//			try {
//				BasicCookieStore cookieStore = new BasicCookieStore();
//				client = HttpClients.custom().setDefaultCookieStore(cookieStore)
//						.build();
//				Integer timeout = Integer.valueOf(Integer.parseInt("12000"));
//
//				RequestConfig.Builder builder = RequestConfig.custom();
//				builder.setSocketTimeout(timeout.intValue());
//				builder.setConnectTimeout(timeout.intValue());
//
//				RequestConfig requestConfig = builder.build();
//
//				post = new HttpPost(url);
//
//				String p0_Cmd = "qunarEposSale";
//				String p1_MerId = r1_MerId;								//商户编号
//				String p2_Order = data[2].split("=")[1];				//去哪儿订单号
//				String pt_ActId = account_no;							//信用卡卡号
//				String pa2_ExpireYear = date.substring(0,4);			//有效期（年）
//				String pa3_ExpireMonth = date.substring(4,6);			//有效期（月）
//				String pa4_CVV = cvv;									//CVV
//				String pf_BuyerName = "";								//消费者姓名
//				String pa_CreadType = "";								//证件类型
//				String pb_CredCode = "";							//证件号码
//				String pe_BuyerTel = payerMobile;						//消费者手机号
//
//				p1_MerId = p1_MerId == null ?"":p1_MerId;
//				p2_Order = p2_Order == null ?"":p2_Order;
//				pt_ActId = pt_ActId == null ?"":pt_ActId;
//				pa2_ExpireYear = pa2_ExpireYear == null ?"":pa2_ExpireYear;
//				pa3_ExpireMonth = pa3_ExpireMonth == null ?"":pa3_ExpireMonth;
//				pa4_CVV = pa4_CVV == null ?"":pa4_CVV;
//				pa_CreadType = pa_CreadType == null ?"":pa_CreadType;
//				pb_CredCode = pb_CredCode == null ?"":pb_CredCode;
//				pe_BuyerTel = pe_BuyerTel == null ?"":pe_BuyerTel;
//
//
//				String newOriginalData = "{\"originalData\":\""+originalData+"\"}";
//
//				String aValue = p0_Cmd + p1_MerId + p2_Order + pt_ActId + pa2_ExpireYear
//								+ pa3_ExpireMonth + pa4_CVV + pf_BuyerName + pa_CreadType
//								+ pb_CredCode + pe_BuyerTel + newOriginalData;
//
//				logger.info("支付参数:"+aValue);
//
//				String hmac = DigestUtil.hmacSign(aValue, aKey);
////				String hmac = "";
//
//				List<BasicNameValuePair> nameValueParis = new ArrayList<BasicNameValuePair>();
//				nameValueParis.add(new BasicNameValuePair("p0_Cmd", p0_Cmd));
//				nameValueParis.add(new BasicNameValuePair("p1_MerId", p1_MerId));
//				nameValueParis.add(new BasicNameValuePair("p2_Order", p2_Order));
//				nameValueParis.add(new BasicNameValuePair("pt_ActId", pt_ActId));
//				nameValueParis.add(new BasicNameValuePair("pa2_ExpireYear", pa2_ExpireYear));
//				nameValueParis.add(new BasicNameValuePair("pa3_ExpireMonth", pa3_ExpireMonth));
//				nameValueParis.add(new BasicNameValuePair("pa4_CVV", pa4_CVV));
////				nameValueParis.add(new BasicNameValuePair("pf_BuyerName", pf_BuyerName));
////				nameValueParis.add(new BasicNameValuePair("pa_CreadType", pa_CreadType));
////				nameValueParis.add(new BasicNameValuePair("pb_CredCode", pb_CredCode));
//				nameValueParis.add(new BasicNameValuePair("pe_BuyerTel", pe_BuyerTel));
//				nameValueParis.add(new BasicNameValuePair("originalData", newOriginalData));
//				nameValueParis.add(new BasicNameValuePair("hmac", hmac));
//
//				post.setEntity(new UrlEncodedFormEntity(nameValueParis,"GBK"));
//				post.setConfig(requestConfig);
//				response = client.execute(post);
//
//				String text = EntityUtils.toString(response.getEntity(),"GBK");
//				logger.info("易宝支付接口返回数据："+text);
//				if(text.contains("<!DOCTYPE html PUBLIC")){
//					return "订单失败";
//				}
//				String[] str = text.split("\n");
//				if(str!=null && str.length > 0){
//					if(str[1].split("=")[1].equals("1")){
//						return "SUCCESS";
//					}else{
//						logger.info("易宝支付接口返回错误信息:"+str[2].split("=")[1]);
//						return "支付失败，原因："+str[2].split("=")[1];
//					}
//				}
//				return "";
//			} catch (Exception e) {
//				logger.error("error",e);
//			} finally{
//				try {
//					if(response != null){
//						response.close();
//					}
//					if(client != null){
//						client.close();
//					}
//				} catch (Exception e2) {
//					logger.error("error",e2);
//				}
//			}
//			return "";
//		}
//	public static String getPayParam8L(String url, String cookie , String payType) {
//		CloseableHttpClient client = null;
//		CloseableHttpResponse response = null;
//		HttpGet get = null;
//		OutputStream outStream = null;
//		try {
//			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
//					null, new TrustStrategy() {
//						public boolean isTrusted(X509Certificate[] chain,
//								String authType) throws CertificateException {
//							return true;
//						}
//					}).build();
//
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//					sslContext,
//					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//			BasicCookieStore cookieStore = new BasicCookieStore();
//			client = HttpClients.custom().setSSLSocketFactory(sslsf)
//					.setDefaultCookieStore(cookieStore).build();
//			Integer timeout = Integer.valueOf(Integer.parseInt("12000"));
//
//			RequestConfig.Builder builder = RequestConfig.custom();
//			builder.setSocketTimeout(timeout.intValue());
//			builder.setConnectTimeout(timeout.intValue());
//
//			RequestConfig requestConfig = builder.build();
//
//			get = new HttpGet(url);
//			get.setHeader("Referer", url);
//			get.setHeader("Cookie", cookie);
//			get.setConfig(requestConfig);
//			response = client.execute(get);
//
//			String text = EntityUtils.toString(response.getEntity(), "utf-8");
//
//			org.jsoup.nodes.Document doc = Jsoup.parse(text);
//			String orderId = "";
//			String ticketPayCharge = "";
//
//			Elements orderIds = doc.getElementsByAttributeValue("name",
//					"orderId");
//			if (orderIds.size() > 0) {
//				orderId = orderIds.get(0).attr("value");
//			}
//
//			Elements ticketPayCharges = doc.getElementsByAttributeValue("name",
//					"ticketPayCharge");
//			if (ticketPayCharges.size() > 0) {
//				ticketPayCharge = ticketPayCharges.get(0).attr("value");
//			}
//			String createTime = "";
//			Elements tb_style03_as = doc.getElementsByClass("tb_style03_a");
//			if (tb_style03_as.size() > 0) {
//				Elements childrens = tb_style03_as.get(0).children().get(0)
//						.children().get(0).children();
//
//				createTime = childrens.get(3).text();
//			}
//
//			HttpPost post = new HttpPost(
//					"http://www.luckyair.net/payment/Pay.action");
//
//			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
//			nameValuePairs.add(new BasicNameValuePair("bankId", ""));
//			nameValuePairs.add(new BasicNameValuePair("extraOrderId", "11144355"));
//			nameValuePairs.add(new BasicNameValuePair("orderId", orderId));
//			nameValuePairs.add(new BasicNameValuePair("orderSource", ""));
//			nameValuePairs.add(new BasicNameValuePair("orderStatus", "WP"));
//			nameValuePairs.add(new BasicNameValuePair("pIdsForSeat", ""));
//			nameValuePairs.add(new BasicNameValuePair("payType", "YEEPAY"));
//			nameValuePairs.add(new BasicNameValuePair("payerId", "1"));
//			nameValuePairs.add(new BasicNameValuePair("paymentNo", ""));
//			nameValuePairs.add(new BasicNameValuePair("rowValue", ""));
//			nameValuePairs.add(new BasicNameValuePair("segmentID", ""));
//
//			Elements extraOrderIds = doc.getElementsByAttributeValue("name",
//					"extraOrderId");
//			if (extraOrderIds.size() > 0) {
//				for (int i = 0; i < extraOrderIds.size(); i++) {
//					nameValuePairs.add(new BasicNameValuePair("extraOrderId",
//							extraOrderIds.get(i).attr("value")));
//				}
//			}
//
//			nameValuePairs.add(new BasicNameValuePair("ticketPayCharge",
//					ticketPayCharge));
//			nameValuePairs.add(new BasicNameValuePair("productFareId", "9780"));
//
//			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//
//			post.setHeader("Referer", url);
//			post.setHeader("Host", "www.luckyair.net");
//			post.setHeader("Cookie", cookie);
//			response = client.execute(post);
//			text = EntityUtils.toString(response.getEntity(), "utf-8");
//
//			Header[] location = response.getHeaders("Location");
//
//			String locationValue = "";
//			for (int i = 0; i < location.length; i++) {
//				locationValue = location[i].getValue();
//				logger.info("Location:" + locationValue);
//			}
//
//			String str1 = locationValue + "#_#" + createTime;
//			return str1;
//		} catch (Exception e) {
//			logger.error("error",e);
//		} finally {
//			try {
//				if (outStream != null) {
//					outStream.close();
//				}
//				if (response != null) {
//					response.close();
//				}
//				if (get != null) {
//					get.releaseConnection();
//				}
//				if (client != null)
//					client.close();
//			} catch (Exception e) {
//				logger.error("error",e);
//			}
//		}
//
//		return null;
//	}
//	public static String getPayParamMF(String url, String cookie , String payType,String orderid,String orderJson) throws Exception{
//
//		CloseableHttpClient client = null;
//		CloseableHttpResponse response = null;
//		HttpGet get = null;
//		OutputStream outStream = null;
//		try {
//
//			String result = getPayByHtmlUnit(url, cookie);
//			String ss[] = result.split("#_#");
//			cookie += ss[0];
////			text = EntityUtils.toString(response.getEntity(), "utf-8");
////			logger.info(url+"易宝第三个请求返回:"+text);
//
//
//			org.jsoup.nodes.Document doc = Jsoup.parse(ss[1]);
//			String frpId = doc.getElementById("radio_epos_0").val();
//			String requestId = doc.getElementById("requestId").val();
//			String orderamount = doc.getElementById("orderamount").val();
//
//			yeepayCreateOrder(frpId,requestId,orderamount,cookie,orderJson,url);
//
//			return "";
//		} catch (Exception e) {
//			logger.error("error",e);
//		} finally {
//			try {
//				if (outStream != null) {
//					outStream.close();
//				}
//				if (response != null) {
//					response.close();
//				}
//				if (get != null) {
//					get.releaseConnection();
//				}
//				if (client != null)
//					client.close();
//			} catch (Exception e) {
//				logger.error("error",e);
//			}
//		}
//		return null;
//	}
//
//	private static String yeepayCreateOrder(String frpId, String requestId, String orderamount, String cookie,
//			String orderJson, String url) {
//		CloseableHttpClient client = null;
//		CloseableHttpResponse response = null;
//		String bankNo = "";
//		try {
//			JSONObject json = new JSONObject(orderJson);
//			String payType = json.getString("payType") == null ? "" : json.getString("payType");
//
//			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
//				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
//					return true;
//				}
//			}).build();
//
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
//					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//			BasicCookieStore cookieStore = new BasicCookieStore();
//			client = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCookieStore(cookieStore).build();
//			Integer timeout = Integer.valueOf(Integer.parseInt("70000"));
//
//			RequestConfig.Builder builder = RequestConfig.custom();
//			builder.setSocketTimeout(timeout.intValue());
//			builder.setConnectTimeout(timeout.intValue());
//
//			RequestConfig requestConfig = builder.build();
//			String text = "";
//			String newCookie = "";
//			Header[] headersArr = null;
//			// //提交订单后 跳转的页面请求
//
//			HttpPost post = new HttpPost("https://www.yeepay.com/app-merchant-proxy/createOrder.action");
//
//			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
//			nameValuePairs.add(new BasicNameValuePair("frpId", frpId));
//			nameValuePairs.add(new BasicNameValuePair("requestId", requestId));
//			nameValuePairs.add(new BasicNameValuePair("trx_order_amount", orderamount));
//			nameValuePairs.add(new BasicNameValuePair("x", "54"));
//			nameValuePairs.add(new BasicNameValuePair("y", "20"));
//			logger.info("支付cookie:"+cookie);
//			logger.info("请求参数："+nameValuePairs.toString());
//			post.setConfig(requestConfig);
//			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//			post.setHeader("Referer", url);
//			post.setHeader("Host", "www.yeepay.com");
//			post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
//			post.setHeader("Cookie", cookie);
//			post.setHeader("Connection", "keep-alive");
//			response = client.execute(post);
//			headersArr = response.getAllHeaders();
//			for (Header header : headersArr) {
//				if ("Set-Cookie".equals(header.getName())) {
//					newCookie += header.getValue() + ";";
//				}
//			}
//			cookie += newCookie;
//			text = EntityUtils.toString(response.getEntity(), "utf-8");
//			logger.info("易宝支付第一个请求返回：" + text);
//			if(!StringUtils.isEmpty(text)&&text.contains("业务接口维护中")){
//				return text;
//			}
//			//选择易宝信用卡或是易宝会员支付
//			if("xyk".equals(payType)){
//				bankNo = yeepayByCreditCard(client,requestConfig,text,orderJson,cookie,requestId,orderamount,frpId);
//			}else if("ybhy".equals(payType)){
//				yeepayByTrxData(client,requestConfig,text,orderJson,cookie);
//			}
//
//		} catch (Exception e) {
//			logger.error("error",e);
//		}
//		return bankNo;
//	}
//
//
//	private static void yeepayByTrxData(CloseableHttpClient client, RequestConfig requestConfig, String text,
//			String orderJson, String cookie) {
//		try {
//			JSONObject json = new JSONObject(orderJson);
//			String creditNo = json.getString("creditNo") == null ? "" : json.getString("creditNo");
//			String cvv = json.getString("cvv") == null ? "" : json.getString("cvv");
//			org.jsoup.nodes.Document doc = Jsoup.parse(text);
//			String paymentId = doc.getElementsByAttributeValue("name", "paymentId").val();
//			String date = doc.getElementsByAttributeValue("name", "date").val();
//			String sign = doc.getElementsByAttributeValue("name", "sign").val();
//			String paymentPage = doc.getElementsByAttributeValue("name", "paymentPage").val();
//			HttpGet get = new HttpGet("https://www.yeepay.com/app-merchant-proxy/trxData.action?paymentId="+paymentId+"&date="+date+"&sign="+sign+"&paymentPage="+paymentPage);
//			get.setConfig(requestConfig);
//			get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//			get.setHeader("Host","www.yeepay.com");
//			get.setHeader("Referer", "https://www.yeepay.com/app-merchant-proxy/createOrder.action");
//			get.setHeader("Cookie",cookie);
//			CloseableHttpResponse response = client.execute(get);
//			text = EntityUtils.toString(response.getEntity(), "utf-8");
//			logger.info("易宝会员支付第一个请求返回：" + text);
//			Header[] headersArr = response.getAllHeaders();
//			String newCookie = "";
//			for (Header header : headersArr) {
//				if ("Set-Cookie".equals(header.getName())) {
//					newCookie += header.getValue() + ";";
//				}
//			}
//			cookie += newCookie;
//
//			doc = Jsoup.parse(text);
//			date = doc.getElementsByAttributeValue("name", "date").val();
//			paymentId = doc.getElementsByAttributeValue("name", "paymentId").val();
//			sign = doc.getElementsByAttributeValue("name", "sign").val();
//			HttpPost post = new HttpPost("https://www.yeepay.com/app-merchant-proxy/eposCreditCardVerify.action");
//			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
//			nameValuePairs.add(new BasicNameValuePair("date", date));
//			nameValuePairs.add(new BasicNameValuePair("newPayMode", "on"));
//			nameValuePairs.add(new BasicNameValuePair("notSecurityPassword", cvv));
//			nameValuePairs.add(new BasicNameValuePair("password", cvv));
//			nameValuePairs.add(new BasicNameValuePair("paymentId", paymentId));
//			nameValuePairs.add(new BasicNameValuePair("securityLogin", "false"));
//			nameValuePairs.add(new BasicNameValuePair("sign", sign));
//			nameValuePairs.add(new BasicNameValuePair("userName", creditNo));
//			post.setConfig(requestConfig);
//			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//			post.setHeader("Referer", "https://www.yeepay.com/app-merchant-proxy/createOrder.action");
//			post.setHeader("Host", "www.yeepay.com");
//			post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
//			post.setHeader("Cookie", cookie);
//			post.setHeader("Connection", "keep-alive");
//			response = client.execute(post);
//			text = EntityUtils.toString(response.getEntity(), "utf-8");
//			logger.info("易宝会员支付第二个请求返回：" + text);
//		} catch (Exception e) {
//			logger.error("error",e);
//		}
//	}
//
//
//	private static String yeepayByCreditCard(CloseableHttpClient client, RequestConfig requestConfig, String text,
//			String orderJson, String cookie, String requestId, String orderamount, String frpId) {
//		String bankId = "";
//		try {
//			JSONObject json = new JSONObject(orderJson);
//			String creditNo = json.getString("creditNo") == null ? "" : json.getString("creditNo");
//			String expireMonth = json.getString("expireMonth") == null ? "" : json.getString("expireMonth");
//			String expireYear = json.getString("expireYear") == null ? "" : json.getString("expireYear");
//			String ownername = json.getString("ownername") == null ? "" : json.getString("ownername");
//			String idCardNo = json.getString("idCardNo") == null ? "" : json.getString("idCardNo");
//			String payerMobile = json.getString("payerMobile") == null ? "" : json.getString("payerMobile");
//			String cvv = json.getString("cvv") == null ? "" : json.getString("cvv");
//			org.jsoup.nodes.Document doc = Jsoup.parse(text);
//			String customerId = doc.getElementById("customerId").val();
//			HttpPost post = new HttpPost("https://www.yeepay.com/app-merchant-proxy/eposCreditCardVerify.action");
//			List<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();
//			nameValuePairs.add(new BasicNameValuePair("businessType", "EPOS"));
//			nameValuePairs.add(new BasicNameValuePair("creditNo", creditNo));
//			nameValuePairs.add(new BasicNameValuePair("customerId", customerId));
//			nameValuePairs.add(new BasicNameValuePair("requestId", requestId));
//			post.setConfig(requestConfig);
//			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//			post.setHeader("Referer", "https://www.yeepay.com/app-merchant-proxy/createOrder.action");
//			post.setHeader("Host", "www.yeepay.com");
//			post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
//			post.setHeader("Cookie", cookie);
//			post.setHeader("Connection", "keep-alive");
//			CloseableHttpResponse response = client.execute(post);
//			text = EntityUtils.toString(response.getEntity(), "utf-8");
//			logger.info("易宝信用卡支付第一个请求返回：" + text);
//			Header[] headersArr = response.getAllHeaders();
//			String newCookie = "";
//			for (Header header : headersArr) {
//				if ("Set-Cookie".equals(header.getName())) {
//					newCookie += header.getValue() + ";";
//				}
//			}
//			cookie += newCookie;
//
//			text = text.substring(1, text.length() - 1);
//			post = new HttpPost("https://www.yeepay.com/app-merchant-proxy/toEposAuthorizeConfirm.action");
//			JSONObject jo = new JSONObject(text);
//			String needParams = jo.getString("needParams");
//			nameValuePairs.clear();
//			String formatCreditNo = creditNo.substring(0, 4) + " " + creditNo.substring(4, 8) + " "
//					+ creditNo.substring(8, 12) + " " + creditNo.substring(12, 16);
//			nameValuePairs.add(new BasicNameValuePair("amount", orderamount));
//			nameValuePairs.add(new BasicNameValuePair("creditCardInfo.cvv", cvv));// 暂时写死
//			nameValuePairs.add(new BasicNameValuePair("creditNo", formatCreditNo));
//			nameValuePairs.add(new BasicNameValuePair("creditOrderType", "AUTHANDCONFIRM"));
//			nameValuePairs.add(new BasicNameValuePair("customerId", customerId));
//			nameValuePairs.add(new BasicNameValuePair("expireMonth", expireMonth));
//			nameValuePairs.add(new BasicNameValuePair("expireYear", expireYear));
//			nameValuePairs.add(new BasicNameValuePair("frpIdString", frpId));
//			nameValuePairs.add(new BasicNameValuePair("idCardNo", idCardNo));
//			nameValuePairs.add(new BasicNameValuePair("idCardType", "IDCARD"));
//			nameValuePairs.add(new BasicNameValuePair("name", ownername));
//			nameValuePairs.add(new BasicNameValuePair("needParams", needParams));
//			nameValuePairs.add(new BasicNameValuePair("needboccvv", ""));
//			nameValuePairs.add(new BasicNameValuePair("payerMobile", payerMobile));
//			nameValuePairs.add(new BasicNameValuePair("productName", ""));
//			nameValuePairs.add(new BasicNameValuePair("requestId", requestId));
//			nameValuePairs.add(new BasicNameValuePair("yeepayAgreement", "yeepayAgreement"));
//			post.setConfig(requestConfig);
//			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//			post.setHeader("Referer", "https://www.yeepay.com/app-merchant-proxy/createOrder.action");
//			post.setHeader("Host", "www.yeepay.com");
//			post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
//			post.setHeader("Cookie", cookie);
//			post.setHeader("Connection", "keep-alive");
//			response = client.execute(post);
//			text = EntityUtils.toString(response.getEntity(), "utf-8");
//			logger.info("易宝信用卡支付第二个请求返回：" + text);
//			headersArr = response.getAllHeaders();
//			for (Header header : headersArr) {
//				if ("Set-Cookie".equals(header.getName())) {
//					newCookie += header.getValue() + ";";
//				}
//			}
//			cookie += newCookie;
//
//			doc = Jsoup.parse(text);
//			String webworkToken = doc.getElementsByAttributeValue("name", "webwork.token").get(0).val();
//			String webworkTokenName = doc.getElementsByAttributeValue("name", "webwork.token.name").get(0).val();
//
//			nameValuePairs.clear();//https://www.yeepay.com/app-merchant-proxy/toEposAuthorizeConfirm.action
//			post = new HttpPost("https://www.yeepay.com/app-merchant-proxy/eposAuthorizeConfirm.action");
//			nameValuePairs.add(new BasicNameValuePair("amount", orderamount));
//			nameValuePairs.add(new BasicNameValuePair("creditCardInfo.cvv", cvv));// 暂时写死
//			nameValuePairs.add(new BasicNameValuePair("creditNo", formatCreditNo));
//			nameValuePairs.add(new BasicNameValuePair("expireMonth", expireMonth));
//			nameValuePairs.add(new BasicNameValuePair("expireYear", expireYear));
//			nameValuePairs.add(new BasicNameValuePair("frpIdString", frpId));
//			nameValuePairs.add(new BasicNameValuePair("idCardNo", idCardNo));
//			nameValuePairs.add(new BasicNameValuePair("idCardType", "IDCARD"));
//			nameValuePairs.add(new BasicNameValuePair("name", ownername));
//			nameValuePairs.add(new BasicNameValuePair("needboccvv", ""));
//			nameValuePairs.add(new BasicNameValuePair("payerMobile", payerMobile));
//			nameValuePairs.add(new BasicNameValuePair("productName", ""));
//			nameValuePairs.add(new BasicNameValuePair("requestId", requestId));
//			nameValuePairs.add(new BasicNameValuePair("webwork.token", webworkToken));
//			nameValuePairs.add(new BasicNameValuePair("webwork.token.name", webworkTokenName));
//			post.setConfig(requestConfig);
//			post.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
//			post.setHeader("Referer", "https://www.yeepay.com/app-merchant-proxy/toEposAuthorizeConfirm.action");
//			post.setHeader("Host", "www.yeepay.com");
//			post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0");
//			post.setHeader("Cookie", cookie);
//			post.setHeader("Connection", "keep-alive");
//			response = client.execute(post);
//			text = EntityUtils.toString(response.getEntity(), "utf-8");
//			logger.info("易宝支付第四个请求返回：" + text);
//			headersArr = response.getAllHeaders();
//			for (Header header : headersArr) {
//				if ("Set-Cookie".equals(header.getName())) {
//					newCookie += header.getValue() + ";";
//				}
//			}
//			cookie += newCookie;
//			try {
//				doc = Jsoup.parse(text);
//				bankId = doc.getElementsByAttributeValue("name", "r6_Order").get(0).val();
//			} catch (Exception e) {
//				bankId = null;
//				logger.info("支付异常页面:" + text);
//				logger.error("error", e);
//			}
//		} catch (Exception e) {
//			logger.error("error",e);
//		}
//		return bankId;
//	}
//
//
//	/**
//	 * 该URL必须用HTTPUNIT访问
//	 * @Title: getPayByHtmlUnit
//	 * @Description:TODO(这里用一句话描述这个方法的作用)
//	 * @param ip
//	 * @param port
//	 * @return
//	 * @author xuyx
//	 * @return String 返回类型
//	 * @throws
//	 */
//    public static String getPayByHtmlUnit(String urlStr,String ckStr) {
//
//		//可以设置不同版本的浏览器
//		WebClient webclient = new WebClient(BrowserVersion.CHROME);
//		try{
//			//开启css解析
//			webclient.getOptions().setCssEnabled(true);
//			//开启js解析
//			webclient.getOptions().setJavaScriptEnabled(true);
//			//开启cookie管理
//			webclient.getCookieManager().setCookiesEnabled(true);
//			//设置超时时间
//			webclient.getOptions().setTimeout(20000);
//			//关闭脚本错误时抛出异常
//			webclient.getOptions().setThrowExceptionOnScriptError(false);
//			URL url = new URL(urlStr);
//			String cs[] = ckStr.split(";");
//
//	        for(String c : cs) {
//	            String css[] = c.split("=");
//	            com.gargoylesoftware.htmlunit.util.Cookie ck = null;
//	            if(css.length == 1) {
//	            	ck = new com.gargoylesoftware.htmlunit.util.Cookie(css[0], "", "www.yeepay.com");
//	            }else{
//	            	ck = new com.gargoylesoftware.htmlunit.util.Cookie(css[0], css[1],"www.yeepay.com");
//	            }
//	            webclient.getCookieManager().addCookie(ck);
//	        }
//			//Get 请求
//			WebRequest webRequest =new WebRequest(url,HttpMethod.GET);
//
//
//			HtmlPage htmlpage = webclient.getPage(webRequest);
//
//			// 我把结果转成String
//			String result = htmlpage.asXml();
//			logger.info("请求"+urlStr+"返回\t\r\n"+result);
//			Set<com.gargoylesoftware.htmlunit.util.Cookie> set = webclient.getCookieManager().getCookies();
//			Iterator<com.gargoylesoftware.htmlunit.util.Cookie> iter = set.iterator();
//			StringBuffer sb = new StringBuffer();
//			while(iter.hasNext()){
//				com.gargoylesoftware.htmlunit.util.Cookie cookie = iter.next();
//				sb.append(cookie.getName()+"="+cookie.getValue()+";");
//			}
//			logger.info(sb.toString()+"#_#"+result);
//
//			return sb.toString()+"#_#"+result;
//
//		}catch (Exception e) {
//			// TODO: handle exception
//			logger.error("error",e);;
//		}finally{
//    		//清除历史记录
//    		List<WebWindow> webWindows = webclient.getWebWindows();
//    		if(webWindows!=null&&webWindows.size()>0){
//    			for(WebWindow webWindow:webWindows){
//    				History history = webWindow.getHistory();
//    				if(history!=null){
//    					history.removeCurrent();
//    				}
//    			}
//    		}
//    		webclient.getCurrentWindow().getJobManager().removeAllJobs();
//    		webclient.closeAllWindows();
//
//    	}
//		return "#_#";
//	}
//
//	public static String getSign(Map<String, String> map) {
//
//		String result = "";
//		try {
//			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(map.entrySet());
//			// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
//			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
//
//				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
//					return (o1.getKey()).toString().compareTo(o2.getKey());
//				}
//			});
//
//			// 构造签名键值对的格式
//			StringBuilder sb = new StringBuilder();
//			for (Map.Entry<String, String> item : infoIds) {
//				if (item.getKey() != null || item.getKey() != "") {
//					String key = item.getKey();
//					String val = item.getValue();
//					if (!(val == "" || val == null)) {
//						sb.append(key + "=" + val + "&");
//					}
//				}
//
//			}
//			sb.delete(sb.length()-1, sb.length());
//			result = sb.toString();
//		} catch (Exception e) {
//			return null;
//		}
//		return result;
//	}
//
//	/**
//	 * MF易宝支付，改版后方式
//	 * 用selenium模拟支付，用于MF
//	 * @param url
//	 * @return
//	 */
//	public Map<String,String> yeePayMFNew(String url,String orderJson,String cookie){
//		CloseableHttpClient httpclient = null;
//		CloseableHttpResponse response = null;
//		HttpGet get = null;
//		Map<String,String> resultMap = null;
//		try {
//			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
//					null, new TrustStrategy() {
//						public boolean isTrusted(X509Certificate[] chain,
//								String authType) throws CertificateException {
//							return true;
//						}
//					}).build();
//
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//					sslContext,
//					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//			BasicCookieStore cookieStore = new BasicCookieStore();
//			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf)
//					.setDefaultCookieStore(cookieStore).build();
//			Integer timeout = Integer.valueOf(Integer.parseInt("70000"));
//
//			RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout)
//					.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
//					.setRedirectsEnabled(false)
//					.setStaleConnectionCheckEnabled(true).build();
//
//			resultMap = yeePay(orderJson,cookie,url);
//
//			String returnUrl = "";
//			try {
//				returnUrl = resultMap.get("returnUrl");
//				logger.info("通知航司地址:"+returnUrl);
//				if(StringUtils.isNotEmpty(returnUrl)){
//					get = new HttpGet(returnUrl);
//					get.setConfig(defaultRequestConfig);
//					get.setHeader("Content-Type","text/html;charset=UTF-8");
//					get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//					get.setHeader("Host","pay.xiamenair.com");
//					get.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//					get.setHeader("Accept-Encoding","gzip, deflate, br");
//					get.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//					get.setHeader("Connection","keep-alive");
//					get.setHeader("Upgrade-Insecure-Requests","1");
//					get.setHeader("Cookie",cookie);
//					response = httpclient.execute(get);
//					String back = EntityUtils.toString(response.getEntity(),"utf-8");
//					logger.error("通知航司返回："+back);
//				}
//			} catch (Exception e) {}
//		}catch (Exception e) {
//			logger.error("error",e);
//		}finally {
//			try {
//				if(response != null){
//					response.close();
//				}
//				if(httpclient != null){
//				    httpclient.close();
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//		return resultMap;
//	}
//
//	/**
//	 * 易宝M端会员账号支付
//	 * 用selenium模拟支付，用于KNAPP
//	 * @param url
//	 * @return
//	 */
//	public Map<String,String> yeePayM(String url,String orderJson,String cookie){
//		CloseableHttpClient httpclient = null;
//		CloseableHttpResponse response = null;
//		HttpGet get = null;
//		Map<String,String> resultMap = null;
//		try {
//			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
//					null, new TrustStrategy() {
//						public boolean isTrusted(X509Certificate[] chain,
//								String authType) throws CertificateException {
//							return true;
//						}
//					}).build();
//
//			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//					sslContext,
//					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//
//			BasicCookieStore cookieStore = new BasicCookieStore();
//			httpclient = HttpClients.custom().setSSLSocketFactory(sslsf)
//					.setDefaultCookieStore(cookieStore).build();
//			Integer timeout = Integer.valueOf(Integer.parseInt("70000"));
//
//			RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout)
//					.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
//					.setRedirectsEnabled(false)
//					.setStaleConnectionCheckEnabled(true).build();
//
//			url = encodeUrl(url);
//			url= url.replaceAll(" ", "%20");
//			logger.info("第一个请求的加密url:"+url);
//			get = new HttpGet(url.substring(19));
//			defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout)
//					.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
//					.setRedirectsEnabled(false)
//					.setStaleConnectionCheckEnabled(true).build();
//			get.setConfig(defaultRequestConfig);
//			HttpHost target = new HttpHost("m.flycua.com", 443, "https");
//			get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//			get.setHeader("Host","m.flycua.com");
//			get.setHeader("Cookie",cookie);
//			get.setHeader("Content-Type","text/html;charset=UTF-8");
//			get.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//			response = httpclient.execute(target,get);
//			Header[] location = response.getHeaders("Location");
//			String locationValue = "";
//			for (int i = 0; i < location.length; i++) {
//				locationValue = location[i].getValue();
//			}
//			logger.info("第二个请求的url:"+locationValue);
//
//			get = new HttpGet(locationValue);
//			get.setConfig(defaultRequestConfig);
//			get.setHeader("Content-Type","text/html;charset=UTF-8");
//			get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//			get.setHeader("Host","pay.flycua.com");
//			get.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//			get.setHeader("Accept-Encoding","gzip, deflate, br");
//			get.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//			get.setHeader("Connection","keep-alive");
//			get.setHeader("Upgrade-Insecure-Requests","1");
//			get.setHeader("Cookie",cookie);
//			response = httpclient.execute(get);
//			location = response.getHeaders("Location");
//			locationValue = "";
//			for (int i = 0; i < location.length; i++) {
//				locationValue = location[i].getValue();
//			}
//			logger.info("第三个请求的url:"+locationValue);
//
//			get = new HttpGet(locationValue);
//			get.setConfig(defaultRequestConfig);
//			get.setHeader("Content-Type","text/html;charset=UTF-8");
//			get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//			get.setHeader("Host","cashdesk.yeepay.com");
//			get.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//			get.setHeader("Accept-Encoding","gzip, deflate, br");
//			get.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//			get.setHeader("Connection","keep-alive");
//			get.setHeader("Upgrade-Insecure-Requests","1");
//			get.setHeader("Cookie",cookie);
//			response = httpclient.execute(get);
//			location = response.getHeaders("Location");
//			locationValue = "";
//			for (int i = 0; i < location.length; i++) {
//				locationValue = location[i].getValue();
//			}
//			logger.info("第四个请求的url:"+locationValue);
//
//			get = new HttpGet(locationValue);
//			get.setConfig(defaultRequestConfig);
//			get.setHeader("Content-Type","text/html;charset=UTF-8");
//			get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//			get.setHeader("Host","cashdesk.yeepay.com");
//			get.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//			get.setHeader("Accept-Encoding","gzip, deflate, br");
//			get.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//			get.setHeader("Connection","keep-alive");
//			get.setHeader("Upgrade-Insecure-Requests","1");
//			get.setHeader("Cookie",cookie);
//			response = httpclient.execute(get);
//			location = response.getHeaders("Location");
//			locationValue = "";
//			for (int i = 0; i < location.length; i++) {
//				locationValue = location[i].getValue();
//			}
//			logger.info("第五个请求的url:"+locationValue);
//
//			get = new HttpGet(locationValue);
//			get.setConfig(defaultRequestConfig);
//			get.setHeader("Content-Type","text/html;charset=UTF-8");
//			get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//			get.setHeader("Host","cashdesk.yeepay.com");
//			get.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//			get.setHeader("Accept-Encoding","gzip, deflate, br");
//			get.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//			get.setHeader("Connection","keep-alive");
//			get.setHeader("Upgrade-Insecure-Requests","1");
//			get.setHeader("Cookie",cookie);
//			response = httpclient.execute(get);
//			location = response.getHeaders("Location");
//			locationValue = "";
//			for (int i = 0; i < location.length; i++) {
//				locationValue = location[i].getValue();
//			}
//			logger.info("第六个请求的url:"+locationValue);
//
//
//			resultMap = yeePayKN(orderJson,cookie,locationValue);
//			String returnUrl = "";
//			try {
//				returnUrl = resultMap.get("returnUrl");
//				if(StringUtils.isNotEmpty(returnUrl)){
//					get = new HttpGet(returnUrl);
//					get.setConfig(defaultRequestConfig);
//					get.setHeader("Content-Type","text/html;charset=UTF-8");
//					get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64; rv:38.0) Gecko/20100101 Firefox/38.0");
//					get.setHeader("Host","m.flycua.com");
//					get.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
//					get.setHeader("Accept-Encoding","gzip, deflate, br");
//					get.setHeader("Accept-Language","zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3");
//					get.setHeader("Connection","keep-alive");
//					get.setHeader("Upgrade-Insecure-Requests","1");
//					get.setHeader("Cookie",cookie);
//					response = httpclient.execute(get);
//					String back = EntityUtils.toString(response.getEntity(),"utf-8");
//					logger.error("通知航司返回："+back);
//				}
//			} catch (Exception e) {}
//		}catch (Exception e) {
//			logger.error("error",e);
//		}
//		return resultMap;
//	}
//	private Map<String,String> yeePay(String orderJson, String cookie, String locationValue) throws Exception {
//		final WebDriver webdriver = PhantomjsDriverUtil.getWebDriver();
//		if(webdriver==null){
//			return null;
//		}
//		Map<String,String> resultMap = new HashMap<String,String>();
//		try {
//			webdriver.manage().window().maximize();
//			webdriver.manage().timeouts().pageLoadTimeout(400, TimeUnit.SECONDS);
//			JSONObject json = new JSONObject(orderJson);
//			String account = json.getString("creditNo");
//			String password = json.getString("cvv");
//			String order_id = json.getString("id");
//			String payerMobile = json.getString("payerMobile");	//预留手机号
//			String expireMonth = "";
//			String expireYear = "";
//			String ownername = ""; //姓名
//			String idCardNo = ""; //证件号
//			String valid = "";
//			try {
//				expireMonth = json.getString("expireMonth");
//				expireYear = json.getString("expireYear");
//				ownername = json.getString("ownername");
//				idCardNo = json.getString("idCardNo");
//			} catch (Exception e) {}
//			try {
//				if(StringUtils.isNotEmpty(expireYear)){
//					expireYear = expireYear.substring(2);
//				}
//			} catch (Exception e) {}
//			valid = expireMonth + expireYear;
//
//			String payType = "";
//			try {
//				payType = json.getString("payType");
//			} catch (Exception e) {}
//			if(StringUtils.isEmpty(payType)){
//				resultMap.put("error", "没有选择支付方式");
//				return resultMap;
//			}
//			try {
//				webdriver.get(locationValue);
//			} catch (Exception e) {
//				webdriver.get(locationValue);
//			}
//			new WebDriverWait(webdriver, 15).until(ExpectedConditions.presenceOfElementLocated(By.name("userAccount")));
//			if("ybhy".equals(payType)){
//				logger.info(order_id+"选择易宝会员支付");
//				WebElement userEle = webdriver.findElement(By.name("userAccount"));
//				userEle.sendKeys(account);
//				WebElement tradePassword = webdriver.findElement(By.name("tradePassword"));
//				tradePassword.sendKeys(password);
//				WebElement passPayButton = webdriver.findElement(By.id("passPayButton"));
//				passPayButton.click();
//				(new WebDriverWait(webdriver, 15)).until(new ExpectedCondition<Boolean>() {
//
//					@Override
//					public Boolean apply(WebDriver arg0) {
//						String content = webdriver.getPageSource();
//						if(StringUtils.isNotEmpty(content)&&content.contains("支付成功")){
//							return true;
//						}
//						return false;
//					}
//				});
//				try {
//					String returnUrl = webdriver.findElement(By.id("returnButton")).getAttribute("href");
//					String money = webdriver.findElement(By.className("money")).getAttribute("title");
//					WebElement merchant = webdriver.findElement(By.className("merchant-info-wrapper")).findElements(By.className("merchant-info")).get(0);
//					String txNo = merchant.findElements(By.tagName("p")).get(1).findElements(By.tagName("span")).get(1).getAttribute("title");
//					resultMap.put("money", money);	//总支付金额
//					resultMap.put("bankNo", txNo);	//交易流水号
//					resultMap.put("returnUrl", returnUrl);  //返回通知商户url
//				} catch (Exception e) {
//					logger.error("error",e);
//				}
//			}else if("xyk".equals(payType)){
//				logger.info(order_id+"选择易宝信用卡支付");
//
//				new WebDriverWait(webdriver, 15).until(ExpectedConditions.presenceOfElementLocated(By.name("cardno")));
//				logger.info(order_id+"填写卡号"+account);
//				WebElement cardNoEle = webdriver.findElement(By.name("cardno"));
//				cardNoEle.sendKeys(account);
//
////				JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webdriver;
////				String js = "document.getElementsByClassName('tool-handler')[2].className+=' active'";
////				javascriptExecutor.executeScript(js);
////				js = "document.getElementsByClassName('tool-handler')[0].className = 'tool-handler icon'";
////				javascriptExecutor.executeScript(js);
//
//				logger.info(order_id+"手机号码"+payerMobile);
//				new WebDriverWait(webdriver, 15).until(ExpectedConditions.presenceOfElementLocated(By.name("phone")));
//				WebElement phoneEle = webdriver.findElement(By.name("phone"));
//				phoneEle.sendKeys(payerMobile);
//				try {
//					//使用储蓄卡
//					WebElement nameEle = webdriver.findElement(By.name("name"));
//					nameEle.sendKeys(ownername);
//					WebElement idnoEle = webdriver.findElement(By.name("idno"));
//					idnoEle.sendKeys(idCardNo);
//				} catch (Exception e) {}
//				try {
//					logger.info(order_id+"cvv2"+password);
//					//使用信用卡
//					WebElement cvvEle = webdriver.findElement(By.name("cvv2"));
//					cvvEle.sendKeys(password);
//					logger.info(order_id+"valid"+valid);
//					WebElement validEle = webdriver.findElement(By.name("valid"));
//					validEle.sendKeys(valid);
//				} catch (Exception e) {
//					logger.error("error",e);
//				}
//
//
//				WebElement fn_btn = webdriver.findElement(By.id("firstPayBtn"));
//				fn_btn.click();
//	//			Thread.sleep(2000);
//	//			File srcFile = ((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
//	//			try {
//	//				FileUtils.copyFile(srcFile, new File("C:\\testImg\\screenshote1.png"));
//	//			} catch (IOException e) {
//	//				logger.error("error",e);
//	//			}
//	//			logger.info("信用卡支付返回:"+webdriver.getPageSource());
////				(new WebDriverWait(webdriver, 15)).until(new ExpectedCondition<Boolean>() {
////
////					@Override
////					public Boolean apply(WebDriver arg0) {
////						String content = webdriver.getPageSource();
////						if(StringUtils.isNotEmpty(content)&&content.contains("支付成功")){
////							return true;
////						}
////						try{
////							if(!StringUtils.isEmpty(content)){
////								if(webdriver.findElement(By.id("ncPayError")) != null){
////									String error = webdriver.findElement(By.id("ncPayError")).getText();
////									return true;
////								}
////							}
////						}catch (Exception e) {
////							// TODO: handle exception
////							logger.error("error",e);
////						}
////						return false;
////					}
////				});
//				Thread.sleep(1000*1);
//				try {
//					String content = webdriver.getPageSource();
//					if(!StringUtils.isEmpty(content)){
//						try {
//							if(webdriver.findElement(By.id("ncPayError")) != null){
//								String error = webdriver.findElement(By.id("ncPayError")).getText();
//								if(!StringUtils.isEmpty(error)){
//									logger.info(order_id+"支付返回错误："+error);
//									resultMap.put("error", error);
//									return resultMap;
//								}
//							}
//						} catch (Exception e) {
//							logger.info("支付返回结果："+content);
//						}
//
//					}
//					try {
//						new WebDriverWait(webdriver, 120).until(ExpectedConditions.presenceOfElementLocated(By.id("returnButton")));
//					} catch (Exception e) {
//						logger.info("支付返回结果1："+webdriver.getPageSource());
//						resultMap.put("error", "支付未知异常，请到官网检查是否已支付");
//						return resultMap;
//					}
//
//					WebElement returnButton = null;
//					try{
//						returnButton = webdriver.findElement(By.id("returnButton"));
//					}catch (Exception e) {
//						// TODO: handle exception
//					}
//
////					int count = 0;
////					while(returnButton == null && count < 60){
////						Thread.sleep(1000*1);
////						count++;
////						try{
////							returnButton = webdriver.findElement(By.id("returnButton"));
////						}catch (Exception e) {
////							// TODO: handle exception
////						}
////					}
//					Thread.sleep(1*1000);
//					String returnUrl = webdriver.findElement(By.id("returnButton")).getAttribute("href");
//					String money = webdriver.findElement(By.className("money")).getAttribute("title");
//					WebElement merchant = webdriver.findElement(By.className("merchant-info-wrapper")).findElements(By.className("merchant-info")).get(0);
//					String txNo = merchant.findElements(By.tagName("p")).get(1).findElements(By.tagName("span")).get(1).getAttribute("title");
//					resultMap.put("money", money);	//总支付金额
//					resultMap.put("bankNo", txNo);	//交易流水号
//					resultMap.put("returnUrl", returnUrl);  //返回通知商户url
//				} catch (Exception e) {
//					logger.error("error",e);
//				}
//			}else {
//				resultMap.put("stop", "不支持该支付方式");
//				return resultMap;
//			}
//		} catch (Exception e) {
//			logger.error("error",e);
//		} finally {
//			try {
//				if(webdriver!=null){
//					webdriver.quit();
//				}
//			} catch (Exception e) {
//				logger.error("error",e);
//			}
//		}
//		try {
//			if(webdriver!=null){
//				webdriver.quit();
//			}
//		} catch (Exception e) {
//			logger.error("error",e);
//		}
//
//		return resultMap;
//	}
//
//	private Map<String,String> yeePayKN(String orderJson, String cookie, String locationValue) throws Exception {
//		final WebDriver webdriver = PhantomjsDriverUtil.getWebDriver();
//		webdriver.manage().window().setSize(new Dimension(1920, 1080));
//		webdriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
//		Map<String,String> resultMap = new HashMap<String,String>();
//		try {
//			JSONObject json = new JSONObject(orderJson);
//			String account = json.getString("creditNo");
//			String password = json.getString("cvv");
//			String order_id = json.getString("id");
//			String payerMobile = json.getString("payerMobile");	//预留手机号
//			String expireMonth = "";
//			String expireYear = "";
//			String ownername = ""; //姓名
//			String idCardNo = ""; //证件号
//			String valid = "";
//			try {
//				expireMonth = json.getString("expireMonth");
//				expireYear = json.getString("expireYear");
//				ownername = json.getString("ownername");
//				idCardNo = json.getString("idCardNo");
//			} catch (Exception e) {}
//			try {
//				if(StringUtils.isNotEmpty(expireYear)){
//					expireYear = expireYear.substring(2);
//				}
//			} catch (Exception e) {}
//			valid = expireMonth + expireYear;
//
//			String payType = "";
//			try {
//				payType = json.getString("payType");
//			} catch (Exception e) {}
//			if(StringUtils.isEmpty(payType)){
//				resultMap.put("error", "没有选择支付方式");
//				return resultMap;
//			}
//			webdriver.get(locationValue);
//			new WebDriverWait(webdriver, 15).until(ExpectedConditions.presenceOfElementLocated(By.name("userAccount")));
//			if("ybhy".equals(payType)){
//				logger.info(order_id+"选择易宝会员支付");
//				WebElement userEle = webdriver.findElement(By.name("userAccount"));
//				userEle.sendKeys(account);
//				WebElement tradePassword = webdriver.findElement(By.name("tradePassword"));
//				tradePassword.sendKeys(password);
//				WebElement passPayButton = webdriver.findElement(By.id("passPayButton"));
//				passPayButton.click();
//				(new WebDriverWait(webdriver, 15)).until(new ExpectedCondition<Boolean>() {
//
//					@Override
//					public Boolean apply(WebDriver arg0) {
//						String content = webdriver.getPageSource();
//						if(StringUtils.isNotEmpty(content)&&content.contains("支付成功")){
//							return true;
//						}
//						return false;
//					}
//				});
//				try {
//					String returnUrl = webdriver.findElement(By.id("returnButton")).getAttribute("href");
//					String money = webdriver.findElement(By.className("money")).getAttribute("title");
//					WebElement merchant = webdriver.findElement(By.className("merchant-info-wrapper")).findElements(By.className("merchant-info")).get(0);
//					String txNo = merchant.findElements(By.tagName("p")).get(1).findElements(By.tagName("span")).get(1).getAttribute("title");
//					resultMap.put("money", money);	//总支付金额
//					resultMap.put("bankNo", txNo);	//交易流水号
//					resultMap.put("returnUrl", returnUrl);  //返回通知商户url
//				} catch (Exception e) {
//					logger.error("error",e);
//				}
//			}else if("xyk".equals(payType)){
//				logger.info(order_id+"选择易宝信用卡支付");
//				JavascriptExecutor javascriptExecutor = (JavascriptExecutor) webdriver;
//				String js = "document.getElementsByClassName('tool-handler')[2].className+=' active'";
//				javascriptExecutor.executeScript(js);
//				js = "document.getElementsByClassName('tool-handler')[0].className = 'tool-handler icon'";
//				javascriptExecutor.executeScript(js);
//				new WebDriverWait(webdriver, 15).until(ExpectedConditions.presenceOfElementLocated(By.name("cardno")));
//				WebElement cardNoEle = webdriver.findElement(By.name("cardno"));
//				cardNoEle.sendKeys(account);
//				new WebDriverWait(webdriver, 15).until(ExpectedConditions.presenceOfElementLocated(By.name("phone")));
//				WebElement phoneEle = webdriver.findElement(By.name("phone"));
//				phoneEle.sendKeys(payerMobile);
//				try {
//					//使用储蓄卡
//					WebElement nameEle = webdriver.findElement(By.name("name"));
//					nameEle.sendKeys(ownername);
//					WebElement idnoEle = webdriver.findElement(By.name("idno"));
//					idnoEle.sendKeys(idCardNo);
//				} catch (Exception e) {}
//				try {
//					//使用信用卡
//					WebElement cvvEle = webdriver.findElement(By.name("cvv2"));
//					cvvEle.sendKeys(password);
//					WebElement validEle = webdriver.findElement(By.name("valid"));
//					validEle.sendKeys(valid);
//				} catch (Exception e) {}
//
//
//				WebElement fn_btn = webdriver.findElement(By.id("firstPayBtn"));
//				fn_btn.click();
//	//			Thread.sleep(2000);
//	//			File srcFile = ((TakesScreenshot)webdriver).getScreenshotAs(OutputType.FILE);
//	//			try {
//	//				FileUtils.copyFile(srcFile, new File("C:\\testImg\\screenshote1.png"));
//	//			} catch (IOException e) {
//	//				logger.error("error",e);
//	//			}
//	//			logger.info("信用卡支付返回:"+webdriver.getPageSource());
//				(new WebDriverWait(webdriver, 15)).until(new ExpectedCondition<Boolean>() {
//
//					@Override
//					public Boolean apply(WebDriver arg0) {
//						String content = webdriver.getPageSource();
//						if(StringUtils.isNotEmpty(content)&&content.contains("支付成功")){
//							return true;
//						}
//						return false;
//					}
//				});
//				try {
//					String returnUrl = webdriver.findElement(By.id("returnButton")).getAttribute("href");
//					String money = webdriver.findElement(By.className("money")).getAttribute("title");
//					WebElement merchant = webdriver.findElement(By.className("merchant-info-wrapper")).findElements(By.className("merchant-info")).get(0);
//					String txNo = merchant.findElements(By.tagName("p")).get(1).findElements(By.tagName("span")).get(1).getAttribute("title");
//					resultMap.put("money", money);	//总支付金额
//					resultMap.put("bankNo", txNo);	//交易流水号
//					resultMap.put("returnUrl", returnUrl);  //返回通知商户url
//				} catch (Exception e) {
//					logger.error("error",e);
//				}
//			}else {
//				resultMap.put("error", "不支持该支付方式");
//				return resultMap;
//			}
//		} catch (Exception e) {
//			logger.error("error",e);
//		} finally {
//			webdriver.quit();
//		}
//		return resultMap;
//	}
//
//	private String encodeUrl(String url) throws Exception{
//		String[] str = url.split("&");
//		StringBuffer sb = new StringBuffer();
//		for(String s:str){
//			String encodeUrl = splitUrl(s);
//			if(StringUtils.isNotEmpty(encodeUrl)){
//				sb.append(encodeUrl);
//			}else{
//				sb.append(s);
//			}
//			sb.append("&");
//		}
//		sb.delete(sb.length()-1,sb.length());
//		return sb.toString();
//	}
//
//	private String splitUrl(String keyValue) throws Exception{
//		if(keyValue.contains("notify_url")||
//			keyValue.contains("return_url")||
//			keyValue.contains("show_url")||
//			keyValue.contains("returnURL")||
//			keyValue.contains("showURL")||
//			keyValue.contains("callbackURL")){
//			String[] str = keyValue.split("=");
//			StringBuffer temp = new StringBuffer();
//			String value = "";
//			for(int i=0;i<str.length;i++){
//				value = str[i];
//				if(i==0){
//					temp.append(value).append("=");
//				}else if(i==1){
//					value = URLEncoder.encode(value,"utf-8");
//					temp.append(value);
//				}else{
//					value = URLEncoder.encode("="+value,"utf-8");
//					temp.append(value);
//				}
//			}
////			temp.delete(temp.length()-1,temp.length());
//		return temp.toString();
//		}
//		return "";
//	}
//}
