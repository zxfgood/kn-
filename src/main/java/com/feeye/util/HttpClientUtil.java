package com.feeye.util;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.feeye.handler.ReqHandler;
import com.feeye.init.SysData;
import com.feeye.page.frame.LoginFrame;


/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/15 13:48
 */
public class HttpClientUtil {

	private static final Logger logger = Logger.getLogger(HttpClientUtil.class);

	public static final String CHARSET = "UTF-8";

	public static final int timeout = 30 * 1000;// 连接超时时间
	public static String sendHttpRequest(String url, Map<String, String> paramMap) throws Exception {
		String result = null;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost post = new HttpPost(SysData.url);
		RequestConfig config = RequestConfig.custom().setConnectTimeout(timeout).setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).build();
		post.setConfig(config);
		List<BasicNameValuePair> nameValuePairs = generatNameValuePair(paramMap);
		post.setEntity(new UrlEncodedFormEntity(nameValuePairs,"utf-8"));
		post.setHeader("Cookie", "isUserGetApp".equals(paramMap.get("tag")) ? "JSESSIONID=feeye12345":SysData.loginCookie);
		try {
			HttpResponse response = httpclient.execute(post);
			result = EntityUtils.toString(response.getEntity(), "utf-8");
			if (ReqHandler.verifyAccount.equals(paramMap.get("tag"))) {
				Header[] allHeaders = response.getAllHeaders();
				String cookie = null;
				for (Header header : allHeaders) {
					if ("Set-Cookie".equals(header.getName())) {
						cookie = header.getValue();
					}
				}
				if (cookie!=null) {
					String[] split = cookie.split(";");
					cookie=null;
					for (String s : split) {
						if (s.contains("JSESSIONID")) {
							cookie = s;
							break;
						}
					}
					if (cookie!=null) {
						SysData.loginCookie = cookie;
					}
				}
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			httpclient.close();
		}
		return result;
	}
	private static List<BasicNameValuePair> generatNameValuePair(Map<String, String> paramMap) {
		List<BasicNameValuePair> nameValuePair = new ArrayList<BasicNameValuePair>();
		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
		}
		return nameValuePair;
	}
	private static Map<String, String> buildRequestPara(Map<String, Object> paraMap, String tag) {
		Map<String, String> reqMap = new LinkedHashMap<>();
		try {
			String md5Key = SysData.md5Key;
			String desKey = SysData.desKey;
			String timestamp = System.currentTimeMillis()+"";
			System.out.println(JSON.toJSONString(paraMap));
//
			String desData = DesUtil.encrypt(URLEncoder.encode(JSON.toJSONString(paraMap), "UTF-8"), desKey);
			if ("getVersion".equals(tag)) {
				desData="";
			}
			String signData = tag+desData+timestamp+md5Key;
			String sign = MD5Util.getMD5(signData, "UTF-8");
			reqMap.put("tag",  tag);
			reqMap.put("desData", desData);
			reqMap.put("timestamp", timestamp);
			reqMap.put("sign", sign);
		} catch (Throwable e) {
			logger.error("error", e);
		}
		return reqMap;
	}

	public static String getRespText(Map<String, Object> paraMap, String tag) {
		String respText = null;
		try {
			Map<String, String> reqParaMap = buildRequestPara(paraMap, tag);
			respText = HttpClientUtil.sendHttpRequest(SysData.url, reqParaMap);
//			logger.info("返回的未解密数据:"+respText);
			respText = URLDecoder.decode(DesUtil.decrypt(respText, SysData.desKey));
//			logger.info("返回的解密数据:"+respText);
		} catch (Exception e) {
			logger.error("error", e);
		}
		return respText;
	}
	public static String methodGet(String orderNos, String platType) {
		String url = SysData.importurl+"?orderNo=" + orderNos + "&username=" + SysData.feeyeusr + "&platType=" + platType + "&orderStartTime=&orderEndTime=&isManual=true&grabClient=true";
		CloseableHttpClient client = null;
		CloseableHttpResponse response = null;
		String result = null;
		try {
			BasicCookieStore cookieStore = new BasicCookieStore();
			Integer timeout = Integer.parseInt("60000");
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout)
					.build();// 设置请求和传输超时时间
			client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
			HttpGet get = null;
			try {
				get = new HttpGet(url);
				get.setConfig(requestConfig);
				response = client.execute(get);
				result = EntityUtils.toString(response.getEntity(), "utf-8");
			} catch (Exception e) {
				logger.error("getValidValue()", e);
			} finally {
				try {
					if (response != null) {
						response.close();
					}
					if (get != null) {
						get.releaseConnection();
					}
					if (client != null) {
						client.close();
					}
				} catch (Exception e) {
					logger.error("error", e);
				}
			}
		} catch (Exception e) {
			logger.error("error", e);
		}
		return result;
	}
}
