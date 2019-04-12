package com.feeye.init;

import com.feeye.handler.LoginHandler;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/15 09:54
 */
public class InitProject{
	public static void main(String[] args) {
		SysData.initData();
		LoginHandler.initFrame();
//		InitUtil.playMusic();
	}
}
