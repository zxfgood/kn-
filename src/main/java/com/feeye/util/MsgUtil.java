package com.feeye.util;

import javax.swing.JOptionPane;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/21 17:04
 */
public class MsgUtil {
	public static void errorRemind(String msg) {
		JOptionPane.showMessageDialog(null, msg, "错误提示", JOptionPane.ERROR_MESSAGE);
	}
	public static void warnRemind(String msg) {
		JOptionPane.showMessageDialog(null, msg, "消息提醒", JOptionPane.WARNING_MESSAGE);
	}
	public static void confirmRemind(String msg) {
		JOptionPane.showMessageDialog(null, msg, "消息提醒", JOptionPane.INFORMATION_MESSAGE);
	}
	public static int selectRemind(String msg) {
		return JOptionPane.showConfirmDialog(null, msg+"?", msg, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
	}
}
