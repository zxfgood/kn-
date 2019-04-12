package com.feeye.handler;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import com.feeye.page.frame.LoginFrame;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/15 09:44
 */
public class LoginHandler {

	private static final Logger logger = Logger.getLogger(LoginHandler.class);

	public static void initFrame() {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				try {
					new LoginFrame();
				} catch (Throwable e) {
					logger.error("error", e);
				}
			}
		});
	}
	public static String checkLoginInfo(String feeyeusr, String feeyepwd) {
		String resp = ReqHandler.getVersionNo();
		if (!"true".equals(resp)) {
			return resp;
		}
		resp = ReqHandler.isUserGetApp(feeyeusr);
		if (!"true".equals(resp)) {
			return resp;
		}
		resp = ReqHandler.verifyAccount(feeyeusr, feeyepwd);
		if (!"true".equals(resp)) {
			return resp;
		}
		return null;
	}
}
