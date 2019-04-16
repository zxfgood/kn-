package com.feeye.util;

import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;
import javax.swing.JOptionPane;

import org.apache.http.HttpHost;
import org.apache.http.auth.AUTH;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.log4j.Logger;


import com.eltima.components.ui.DatePicker;
import com.feeye.entity.AccountInfo;
import com.feeye.entity.OrderInfo;
import com.feeye.handler.ReqHandler;
import com.feeye.init.SysData;
import com.feeye.page.frame.OrderFrame;
import com.feeye.service.SmsBase;
import com.google.common.collect.Maps;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/24 11:25
 */
public class InitUtil {

	private static final Logger logger = Logger.getLogger(InitUtil.class);

	public static Map<String, Object> getProxyInfo() {
		Map<String, Object> resultMap = Maps.newHashMap();
		SSLConnectionSocketFactory sslsf = null;
		BasicCookieStore cookieStore = new BasicCookieStore();// 一个cookies
		String cookie = "";
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			sslsf = new SSLConnectionSocketFactory(sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			// 初始化SSL连接
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpHost proxy = new HttpHost("http-dyn.abuyun.com", 9020, "http");
		org.apache.http.impl.auth.BasicScheme proxyAuth = new org.apache.http.impl.auth.BasicScheme();
		BasicAuthCache authCache = new BasicAuthCache();
		authCache.put(proxy, proxyAuth);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		if (StringUtil.isEmpty(SysData.abuyunUser)) {
			String proxyInfo = ReqHandler.getProxyInfo();
			if (proxyInfo!=null) {
				try {
					SysData.abuyunUser = proxyInfo.split("_")[0];
					SysData.abuyunPwd = proxyInfo.split("_")[1];
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		String proxyUser = SysData.abuyunUser;
		String proxyPass = SysData.abuyunPwd;
		credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(proxyUser, proxyPass));
		try {
			proxyAuth.processChallenge(new BasicHeader(AUTH.PROXY_AUTH, "BASIC realm=default"));
		} catch (MalformedChallengeException e1) {
		}

		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(400000)
				.setConnectTimeout(400000).setConnectionRequestTimeout(400000)
				.setProxy(proxy)
				.setExpectContinueEnabled(false).setStaleConnectionCheckEnabled(true).build();
		HttpClientBuilder builder = null;
		builder = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCookieStore(cookieStore)
				.setDefaultRequestConfig(defaultRequestConfig).setDefaultCredentialsProvider(credsProvider);
		resultMap.put("result", "true");
		resultMap.put("builder", builder);
		resultMap.put("authCache", authCache);
		resultMap.put("credsProvider", credsProvider);
		resultMap.put("defaultRequestConfig", defaultRequestConfig);
		resultMap.put("cookieStore", cookieStore);
		return resultMap;
	}
	public static Map<String, Object> getProxyPara() {
		if (SysData.useAbuyunProxy) {
			return getProxyInfo();
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		String proxyInfo = SysData.getProxyInfo();
		int retryTimes = 0;
		while (++retryTimes<=15&&(proxyInfo==null||!checkProxyTime(proxyInfo))) {
			proxyInfo = SysData.getProxyInfo();
		}
		if (proxyInfo==null) {
			resultMap.put("result", "false");
			logger.info("未获取到代理ip");
			return resultMap;
		}
		String proxyIp = proxyInfo.split("-")[0];
		int proxyPort = Integer.parseInt(proxyInfo.split("-")[1]);
		SSLConnectionSocketFactory sslsf = null;
		BasicCookieStore cookieStore = new BasicCookieStore();// 一个cookies
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			sslsf = new SSLConnectionSocketFactory(sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			// 初始化SSL连接
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpHost proxy = new HttpHost(proxyIp, proxyPort, "http");
		org.apache.http.impl.auth.BasicScheme proxyAuth = new org.apache.http.impl.auth.BasicScheme();
		BasicAuthCache authCache = new BasicAuthCache();
		authCache.put(proxy, proxyAuth);
		CredentialsProvider credsProvider = new BasicCredentialsProvider();

		credsProvider.setCredentials(AuthScope.ANY,new UsernamePasswordCredentials(SysData.proxyUser, SysData.proxyPwd));
		try {
			proxyAuth.processChallenge(new BasicHeader(AUTH.PROXY_AUTH, "BASIC realm=default"));
		} catch (MalformedChallengeException e1) {
		}

		int timeout = 40000;

		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout)
				.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
				.setRedirectsEnabled(false)
				.setStaleConnectionCheckEnabled(true).build();
		HttpClientBuilder builder = null;
		builder = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCookieStore(cookieStore)
				.setDefaultRequestConfig(defaultRequestConfig);
		resultMap.put("result", "true");
		resultMap.put("builder", builder);
		resultMap.put("authCache", authCache);
		resultMap.put("credsProvider", credsProvider);
		resultMap.put("defaultRequestConfig", defaultRequestConfig);
		resultMap.put("cookieStore", cookieStore);
		return resultMap;
	}
	public static Map<String, Object> getInitPara() {
		if (1==1) {
			return getProxyPara();
		}
		Map<String, Object> resultMap = Maps.newHashMap();
		SSLConnectionSocketFactory sslsf = null;
		BasicCookieStore cookieStore = new BasicCookieStore();// 一个cookies
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
			sslsf = new SSLConnectionSocketFactory(sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			// 初始化SSL连接
		} catch (Exception e) {
			e.printStackTrace();
		}
		int timeout = 40000;

		RequestConfig defaultRequestConfig = RequestConfig.custom().setSocketTimeout(timeout)
				.setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
				.setRedirectsEnabled(false)
				.setStaleConnectionCheckEnabled(true).build();
		HttpClientBuilder builder = null;
		builder = HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultCookieStore(cookieStore).setDefaultRequestConfig(defaultRequestConfig);
		resultMap.put("result", "true");
		resultMap.put("builder", builder);
		resultMap.put("defaultRequestConfig", defaultRequestConfig);
		resultMap.put("cookieStore", cookieStore);
		return resultMap;
	}
	public static boolean checkProxyTime(String proxyInfo) {
		String surviveTime = proxyInfo.split("-")[2];
		return System.currentTimeMillis()-Long.parseLong(surviveTime)<8*60*1000;
	}

	public static DatePicker getDatePicker(String type) {
		String format = "yyyy-MM-dd HH:mm:ss";
		boolean showTime = true;
		if ("date".equalsIgnoreCase(type)) {
			format = "yyyy-MM-dd";
			showTime = false;
		}
		DatePicker datePicker = new DatePicker(null, format, new Font("Times New Roman", Font.BOLD, 12), new Dimension(60, 20));
		datePicker.setLocale(Locale.CHINA);
		datePicker.setTimePanleVisible(showTime);
		return datePicker;
	}
	public static AccountInfo getAccountInfoById(Long accountId) {
		for (String airCompany : SysData.accountMap.keySet()) {
			Map<Long, AccountInfo> map = SysData.accountMap.get(airCompany);
			if (map!=null&&map.get(accountId)!=null) {
				return map.get(accountId);
			}
		}
		return null;
	}
	public static void operationLog(String orderNo, String content, String className) {
		String log = SysData.sdf_datetime.format(new Date())+"--"+className+".class--编号("+orderNo+")--"+content;
		synchLog(log);
	}
	public static void orderRemind(Long orderId, String grabState, String content, boolean msgInform, String className) {
		OrderInfo orderInfo = SysData.grabOrderMap.get(orderId + "");
		if (orderInfo==null) {
			return;
		}
		if (grabState!=null) {
			try {
				SysData.grabOrderMap.get(orderId + "").setGrabStatus(grabState);
				String grabStatus = SysData.grabOrderMap.get(orderId + "").getGrabStatus();
				System.out.println(grabState);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		String log = SysData.sdf_datetime.format(new Date())+"--"+className+".class--订单号("+orderInfo.getOrderNo()+")--"+content;
		synchLog(log);
		if (msgInform) {
//			playMusic();
		}
	}
	public synchronized static void synchLog(String log) {
		if (SysData.logs.size()>=12) {
			SysData.logs.remove(0);
		}
		SysData.logs.add(log);
	}

	public static void playMusic() {
		String path = new File("").getAbsolutePath();
		FileInputStream resourceAsStream = null;
		try {
			resourceAsStream = new FileInputStream(path+"\\msg.wav");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		AudioStream as= null;
		try {
			as = new AudioStream(resourceAsStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		AudioPlayer.player.start(as);
	}

	public static void main(String[] args) {
		SysData.msgAccount="feeyejp";
		SysData.msgPwd="feeye0724";
		String ss = sendSMS("17673049327", "fy3424抢票成功");
		System.out.println(ss);
	}
	public static String sendSMS(String phoneNum,String content){
		content="【深圳飞耶软件】"+content;
		if (StringUtil.isEmpty(SysData.msgAccount)||StringUtil.isEmpty(SysData.msgPwd)) {
			return "未配置短信账号和密码";
		}
		SmsBase sms=new SmsBase();
		sms.x_id=SysData.msgAccount;
		sms.x_pwd=SysData.msgPwd;
		String string = "";
		if(StringUtil.isEmpty(phoneNum)){
			return "无配置手机号";
		}
		String[] phoneNums = null;
		if(phoneNum.contains(",")){
			phoneNums = phoneNum.split(",");
			for(String telNum:phoneNums){
				try {
					string = sms.SendSms(telNum,content);
					logger.info("通知"+telNum+","+content);
				} catch (UnsupportedEncodingException e) {
					logger.error("error",e);
				}
			}
		}else{
			try {
				string = sms.SendSms(phoneNum,content);
				logger.info("通知"+phoneNum+","+content);
			} catch (UnsupportedEncodingException e) {
				logger.error("error",e);
			}
		}
		return string;
	}
	public static boolean checkDate(String date, String format) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);
			Date formatdate = dateFormat.parse(date);
			String format1 = dateFormat.format(formatdate);
			return date.equals(format1);
		} catch (ParseException e) {
			return false;
		}
	}
	public static final int IDENTITYCODE_OLD = 15; // 老身份证15位
	public static final int IDENTITYCODE_NEW = 18; // 新身份证18位
	/**
	 * 判断身份证号码是否正确。
	 *
	 * @param code
	 *            身份证号码。
	 * @return 如果身份证号码正确，则返回true，否则返回false。
	 */
	public static String getBirth(String code) {

		if (StringUtil.isEmpty(code)) {
			return null;
		}

		String birthDay = "";
		code = code.trim().toUpperCase();

		// 长度只有15和18两种情况
		if ((code.length() != 15)
				&& (code.length() != 18)) {
			return null;
		}

		// 身份证号码必须为数字(18位的新身份证最后一位可以是x)
		Pattern pt = Pattern.compile("(^\\d{15}$)|(\\d{17}(?:\\d|x|X)$)");
		Matcher mt = pt.matcher(code);
		if (!mt.find()) {
			return null;
		}

		// 验证生日
		if (code.length() == 15) {
			birthDay = "19" + code.substring(6, 12);
		} else {
			birthDay = code.substring(6, 14);
		}

		if (!InitUtil.checkDate(birthDay, "yyyyMMdd")) {
			return null;
		}
		birthDay = birthDay.substring(0, 4)+"-"+birthDay.substring(4, 6)+"-"+birthDay.substring(6);
		return birthDay;
	}
}
