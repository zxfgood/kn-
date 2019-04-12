package com.feeye.service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @ClassName MyAuthenticator
 * @Description 权限验证
 * @Author zxf
 * @Date 2019/3/27  16:32
 **/
public class MyAuthenticator extends Authenticator {
    String userName = null;
    String password = null;

    public  MyAuthenticator() {

    }
    public  MyAuthenticator(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        // return super.getPasswordAuthentication();
        return new PasswordAuthentication(userName, password);
    }
}
