package com.feeye.util;

import com.feeye.entity.MailSenderInfo;
import com.feeye.init.SysData;
import com.feeye.service.SimpleMailSender;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * @ClassName SendMailUtil
 * @Description 发送邮件
 * @Author zxf
 * @Date 2019/3/27  16:14
 **/
public class SendMailUtil {
    private static final Logger log = Logger.getLogger(SendMailUtil.class);

    /**
     * @param subject 主题
     * @param content 内容
     * @return void
     * @Description: 发送邮件
     * @Author: zxf
     * @Date: 2019/3/27 16:17
     * @Param mailMap
     */
    public static void sendMail(String subject, String content) {
        String[] address = SysData.email.split(",");//多个收件人
        try {
            for (String ad : address) {
                MailSenderInfo mailInfo = new MailSenderInfo();
                mailInfo.setMailServerHost(PropUtil.getPropertiesValue("mail","serverHost"));
                mailInfo.setMailServerPort(PropUtil.getPropertiesValue("mail","serverPort"));
                mailInfo.setValidate(true);
                mailInfo.setUserName(PropUtil.getPropertiesValue("mail","userName").trim());
                mailInfo.setPassword(PropUtil.getPropertiesValue("mail","password").trim());
                mailInfo.setFromAddress(PropUtil.getPropertiesValue("mail","userName").trim());
                mailInfo.setToAddress(ad);
                mailInfo.setSubject(subject);
                mailInfo.setContent(content);
                // SimpleMailSender.sendHtmlMail(mailInfo);//HTML
                SimpleMailSender.sendTextMail(mailInfo);
                Thread.sleep(5000);//每个用户延迟
            }
        } catch (Exception e) {
            log.error("sendMail:", e);
        }
    }
}
