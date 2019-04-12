package com.feeye.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

import com.feeye.util.StringUtil;

/**
* 发送短信基础类
* @author administration
*
*/
public class SmsBase {
	public String x_id = "feeyeapp";
	public String x_pwd = "feeyeapp";
	private static final Logger logger = Logger.getLogger(SmsBase.class);
	public String SendSms(String mobile, String content)
			throws UnsupportedEncodingException {
		Integer x_ac = 10;//发送信息
		HttpURLConnection httpconn = null;
		String result = "-20";
		String memo = content.length() < 70 ? content.trim() : content.trim()
				.substring(0, 70);
		StringBuilder sb = new StringBuilder();
		sb.append("http://service.winic.org/sys_port/gateway/?");
		sb.append("id=").append(x_id);
		sb.append("&pwd=").append(x_pwd);
		sb.append("&to=").append(mobile);
		sb.append("&content=").append(URLEncoder.encode(content, "gb2312")); //注意乱码的话换成gb2312编码
		try {
			URL url = new URL(sb.toString());
			httpconn = (HttpURLConnection) url.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					httpconn.getInputStream()));
			result = rd.readLine();
			rd.close();
		} catch (MalformedURLException e) {
			logger.error("error",e);
		} catch (IOException e) {
			logger.error("error",e);
		} finally {
			if (httpconn != null) {
				httpconn.disconnect();
				httpconn = null;
			}

		}
		return result;
	}
}
