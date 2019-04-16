package com.feeye.page.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.feeye.entity.AccountInfo;
import com.feeye.handler.LoginHandler;
import com.feeye.handler.OutticketHandler;
import com.feeye.handler.ReqHandler;
import com.feeye.handler.SqliteHander;
import com.feeye.init.SysData;
import com.feeye.service.KNAppOutticketService;
import com.feeye.util.MsgUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;


/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/14 17:13
 */
public class LoginFrame {

	private static final Logger logger = Logger.getLogger(LoginFrame.class);
	private static JButton bt_login;
	private static JLabel lb_login;
	private static JLabel lb_user;
	private static JLabel lb_pwd;
	public static JFrame jf_login;
	private static JTextField jtf_user;
	private static JPasswordField jtf_pwd;

	public LoginFrame() {
 		Font font = new Font("宋体", Font.BOLD, 15);
		jf_login = new JFrame("飞耶订单-官网订票");
		jf_login.setSize(500, 350);
		jf_login.setResizable(false);
		ImageIcon icon_login = new ImageIcon(this.getClass().getResource("/static/picture/login_ad.jpg"));
		icon_login.setImage(icon_login.getImage().getScaledInstance(icon_login.getIconWidth(), icon_login.getIconHeight(), Image.SCALE_FAST));
		lb_login = new JLabel();
		lb_login.setIcon(icon_login);
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/static/picture/login_logo1.jpg"));
		jf_login.setIconImage(icon.getImage());

		lb_user = new JLabel("用户名:");
		lb_user.setBounds(260,90,60, 30);
		lb_user.setFont(font);
		lb_pwd = new JLabel("密  码:");
		lb_pwd.setBounds(260,130,60, 30);
		lb_pwd.setFont(font);
		bt_login = new JButton("登陆");
		bt_login.setBounds(350, 180,80, 30);
		bt_login.setFont(font);
		bt_login.setBackground(Color.GRAY);
		jtf_user = new JTextField();
		jtf_user.setBounds(330,90,140, 30);
		jtf_user.setFont(font);
		jtf_pwd = new JPasswordField();
		jtf_pwd.setBounds(330,130,140, 30);
		jtf_pwd.setFont(font);

		lb_login.add(lb_user);
		lb_login.add(lb_pwd);
		lb_login.add(bt_login);
		lb_login.add(jtf_user);
		lb_login.add(jtf_pwd);

		jf_login.add(lb_login);
		jf_login.setVisible(true);
		jf_login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		jf_login.setLocation(400, 250);

		bt_login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String feeyeusr = /*jtf_user.getText()*/ "policytest";
				String feeyepwd = /*String.valueOf(jtf_pwd.getPassword())*/ "feeye0100";
				String errMsg = null;
				if (StringUtil.isEmpty(feeyeusr)) {
					errMsg = "请输入用户名";
				} else if (StringUtil.isEmpty(feeyepwd)) {
					errMsg = "请输入密码";
				}
				if (errMsg==null) {
//					String result = LoginHandler.checkLoginInfo(feeyeusr, feeyepwd);
					String result = null;
					if (result!=null) {
						errMsg = result;
					}
				}
				if (errMsg!=null) {
					MsgUtil.errorRemind(errMsg);
					return;
				}
				SysData.feeyeusr = feeyeusr;
				String proxyInfo = ReqHandler.getProxyInfo();
				if (proxyInfo==null) {
					proxyInfo = ReqHandler.getProxyInfo();
				}
				if (proxyInfo!=null) {
					try {
						SysData.abuyunUser = proxyInfo.split("_")[0];
						SysData.abuyunPwd = proxyInfo.split("_")[1];
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				SqliteHander.initDatabase();
				this.initThreaNum(feeyeusr);
				this.initMsgInfo(feeyeusr);
				this.initAccountInfo();
				KNAppOutticketService.updateCookie();
				new OutticketHandler().handleOrder();
				//清除数据库抢票标记
				OrderFrame orderFrame = new OrderFrame();
				SysData.orderFrame = orderFrame;
				jf_login.dispose();
			}
			private void initThreaNum(String feeyeusr) {
				SysData.pathUrl = SysData.exeRealPath+"\\database\\"+feeyeusr+"-thread.txt";
				String config = SysData.readText(SysData.pathUrl);
				if (StringUtil.isNotEmpty(config)&&config.contains("threadNum")&&config.contains("delaySec")) {
					try {
						config = config.replace("\n", "").replace("\r", "");
						String regix = "\\w{9}=(\\d{0,})\\w{8}=(\\d{0,})";
						Pattern p = Pattern.compile(regix);
						Matcher m = p.matcher(config);
						while (m.find()) {
							SysData.threadNum = Integer.valueOf(m.group(1));
							SysData.delaySec = Integer.valueOf(m.group(2));
						}
					} catch (Exception e) {
						logger.error("error", e);
					}
				}
			}
			private void initMsgInfo(String feeyeusr) {
				SysData.msgUrl = SysData.exeRealPath+"\\database\\"+feeyeusr+"-msg.txt";
				String config = SysData.readText(SysData.msgUrl);
				if (StringUtil.isNotEmpty(config)&&config.contains("msgAccount")&&config.contains("msgPwd")) {
					try {
						config = config.replace("\n", "").replace("\r", "");
						String regix = "msgAccount=(\\w{0,})msgPwd=(\\w{0,})phonenum=(\\w{0,})email=(\\w{0,}@\\w{0,}\\.\\w{0,})";
						Pattern p = Pattern.compile(regix);
						Matcher m = p.matcher(config);
						while (m.find()) {
							SysData.msgAccount = m.group(1);
							SysData.msgPwd = m.group(2);
							SysData.phonenum = m.group(3);
							SysData.email = m.group(4);
						}
					} catch (Exception e) {
						logger.error("error", e);
					}
				}
			}
			private void initAccountInfo() {
				List<AccountInfo> accountInfos = SqliteHander.queryAccountInfo();
				if (accountInfos!=null&&!accountInfos.isEmpty()) {
					for (AccountInfo accountInfo : accountInfos) {
						Map<Long, AccountInfo> infoMap = SysData.accountMap.get(accountInfo.getAirCompany());
						if (infoMap==null) {
							infoMap = Maps.newConcurrentMap();
						}
						infoMap.put(accountInfo.getId(), accountInfo);
						SysData.accountMap.put(accountInfo.getAirCompany(), infoMap);
					}
				}
			}
		});
	}

	public static void main(String[] args) {
		new LoginFrame();
	}
}
