package com.feeye.init;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;

import com.feeye.page.panel.GrabListPanel;
import org.apache.log4j.Logger;

import com.feeye.entity.AccountInfo;
import com.feeye.entity.OrderInfo;
import com.feeye.handler.LoginHandler;
import com.feeye.handler.ReqHandler;
import com.feeye.page.frame.OrderFrame;
import com.feeye.util.PropUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/15 13:58
 */
public class SysData {

	private static final Logger logger = Logger.getLogger(LoginHandler.class);
	public static String url = "";
	public static String importurl = "";
	public static String desKey = "";
	public static String md5Key = "";
	public static String feeyeusr = "";
	public static Integer threadNum = 5;
	public static Integer delaySec = 10;
	public static String msgAccount = "";
	public static String phonenum = ""; // 提醒的电话号码
	public static String email = "";  // 提醒的email
	public static String msgPwd = "";
	public static String pathUrl="C:\\新建文件夹\\"+feeyeusr+"-thread.txt";
	public static String msgUrl="C:\\新建文件夹\\"+feeyeusr+"-msg.txt";
	public static String versionNo = "";
	public static String airCompany = "KN";
	public static int pageIndex = 1;
	public static BlockingQueue<String> proxyQueue = new LinkedBlockingQueue<>();
	public static String proxyUser = "";
	public static String proxyPwd = "";
	public static String abuyunUser = "HL7F5JF125K85K8D";
	public static String abuyunPwd = "FC393F432489B2E5";
	public static String loginCookie = "";
	public static OrderFrame orderFrame = null;

	public static SimpleDateFormat sdf_datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");

	public static Map<String, List<String>> paxIdMap = Maps.newHashMap();

	public static Map<Long, Map<String, String>> verifyParamMap = new ConcurrentHashMap<>();

	public static Map<String, OrderInfo> orderMap = new ConcurrentHashMap<>();
	// 航司对应的账户信息
	public static Map<String, Map<Long, AccountInfo>> accountMap = new ConcurrentHashMap<>();
	// 订单号对应的订单信息
	public static Map<String, OrderInfo> grabOrderMap = new ConcurrentHashMap<>();
	public static boolean cancleGrabOver = true;
	public static ArrayList<String> logs = Lists.newArrayList();
	public static boolean grabPriceStart = false;
	public static boolean useAbuyunProxy = true;
	public static String exeRealPath = "";
	public static final String INSERT = "insert";
	public static final String UPDATE = "update";
	public static long updateTime = new Date().getTime();
	// 云速账户
	public static String yunSuUserName = "cdqitian";
	public static String yunsuPwd = "cd147369";
	public static GrabListPanel grabListPanel;

	public static void initData() {
//
//		String dlyun = new String(TestDllFile.MyDLL.instance.getDLProxyInfo());
//		String abuyun = new String(TestDllFile.MyDLL.instance.getABProxyInfo());
//		proxyUser = dlyun.split("_")[0];
//		proxyPwd = dlyun.split("_")[1];
//		abuyunUser = abuyun.split("_")[0];
//		abuyunPwd = abuyun.split("_")[1]
//		desKey = new String(TestDllFile.MyDLL.instance.getDES());
//		md5Key = new String(TestDllFile.MyDLL.instance.getMD5());
		desKey = "44b08086cfd54c77aa39d405f010aa8e";
		md5Key = "feeye!@#";
		proxyUser = "feeyeapp";
		proxyPwd = "feeye789";
//		abuyunUser = "HL7F5JF125K85K8D";
//		abuyunPwd = "FC393F432489B2E5";
		versionNo = PropUtil.getPropertiesValue("config", "version");
		url = PropUtil.getPropertiesValue("config", "grabServiceUrl");
		importurl = PropUtil.getPropertiesValue("config", "grabImportOrderUrl");
//		exeRealPath = new File(new File("").getAbsolutePath()).getParent();
		exeRealPath = new File("").getAbsolutePath();
	}
	public static String readText(String path) {
		StringBuffer result = new StringBuffer();
		File file = new File(path);
		if (file.isFile()&&file.exists()) {
			try {
				InputStreamReader reader = new InputStreamReader(new FileInputStream(file), "UTF-8");
				BufferedReader bf = new BufferedReader(reader);
				String line = null;
				while ((line = bf.readLine()) != null) {
					result.append(line);
				}
				reader.close();
				bf.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result.toString();
		}
		return null;
	}
	public synchronized static String getProxyInfo() {
		if (proxyQueue.isEmpty()) {
			int retryTimes = 0;
			List<String> proxyInfos = null;
			do {
				proxyInfos = ReqHandler.getProxyIps();
			} while (++retryTimes<=5&&proxyInfos==null);

			if (proxyInfos!=null&&!proxyInfos.isEmpty()) {
				Iterator<String> iterator = proxyInfos.iterator();
				while (iterator.hasNext()) {
					String proxyInfo = iterator.next();
					proxyQueue.add(proxyInfo+"-"+System.currentTimeMillis());
				}
			} else {
				return null;
			}
		}
		return proxyQueue.poll();
	}
	public static boolean writeTxt(String path, String content){
		File file = new File(path);
		FileOutputStream stream = null;
		try {
			if (!file.isFile()||!file.exists()) {
				File fileParent = file.getParentFile();
				if(!fileParent.exists()){
					fileParent.mkdirs();
				}
				file.createNewFile();
			}
			stream = new FileOutputStream(file);
			stream.write(content.getBytes("UTF-8"));
			stream.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void main(String[] args) {
		String config = null;
		try {
			config = SysData.readText("E:\\data.txt");
			if (StringUtil.isNotEmpty(config)&&config.contains("threadNum")&&config.contains("delaySec")) {
				config = config.replace("\n", "").replace("\r", "");
				String regix = "\\w{9}=(\\d{0,})\\w{8}=(\\d{0,})";
				Pattern p = Pattern.compile(regix);
				Matcher m = p.matcher(config);
				while (m.find()) {
					System.out.println(Integer.valueOf(m.group(1)));
					System.out.println(Integer.valueOf(m.group(2)));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(config);
	}
}
