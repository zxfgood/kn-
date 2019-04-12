package com.feeye.page.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.feeye.entity.AccountInfo;
import com.feeye.entity.OrderInfo;
import com.feeye.entity.PaxInfo;
import com.feeye.handler.ReqHandler;
import com.feeye.handler.SqliteHander;
import com.feeye.init.SysData;
import com.feeye.page.panel.GrabListPanel;
import com.feeye.page.panel.OrderListPanel;
import com.feeye.util.MsgUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;

/**
 * @description: 抢票信息弹出框!
 * @author: domcj
 * @date: 2019/01/18 11:53
 */
public class UpdateAccountDialog extends JDialog implements ActionListener{

	private static final Logger logger = Logger.getLogger(UpdateAccountDialog.class);

	private JPanel jPanel;
	private JLabel text_orderNo;
	private JComboBox input_orderNo;
	private JLabel text_account;
	private JTextField input_account;
	private JLabel text_pwd;
	private JTextField input_pwd;
	private JLabel text_link;
	private JTextField input_link;
	private JLabel text_tel;
	private JTextField input_tel;
	private JLabel text_airCompany;
	private JTextField input_airCompany;
	private JButton cancel;
	private JButton submit;
	private AccountInfo accountInfo;
	private GrabListPanel grabListPanel;

	public UpdateAccountDialog(Frame frame, GrabListPanel grabListPanel, int width, int height, AccountInfo accountInfo) {
		super(frame,true);
		this.setResizable(true);
		this.setTitle("账号更改");
		this.setLayout(null);
		this.accountInfo = accountInfo;
		this.grabListPanel = grabListPanel;
		Dimension dimension = this.getToolkit().getScreenSize();
		this.setBounds((dimension.width-width)/2, (dimension.height-height)/2, width, height);

		jPanel = new JPanel(null);
		jPanel.setBorder(BorderFactory.createEtchedBorder());
		jPanel.setBounds(0, 0, width, height);

		text_orderNo = new JLabel("航司:");
		text_orderNo.setBounds(20, 20, 40, 20);
		jPanel.add(text_orderNo);

		input_orderNo = new JComboBox();
		input_orderNo.addItem("KN");
		input_orderNo.addItem("MF");
		input_orderNo.addItem("MU");
		input_orderNo.addItem("SC");
		input_orderNo.addItem("8L");
		input_orderNo.setBounds(70, 20, 80, 20);
		input_orderNo.setSelectedItem(accountInfo.getAirCompany());
		jPanel.add(input_orderNo);

		text_account = new JLabel("账号:");
		text_account.setBounds(200, 20, 40, 20);
		jPanel.add(text_account);

		input_account = new JTextField(accountInfo.getAccount());
		input_account.setBounds(240, 20, 100, 20);
		jPanel.add(input_account);

		text_pwd = new JLabel("密码:");
		text_pwd.setBounds(20, 50, 40, 20);
		jPanel.add(text_pwd);

		input_pwd = new JTextField(accountInfo.getPassword());
		input_pwd.setBounds(60, 50, 100, 20);
		jPanel.add(input_pwd);

		text_link = new JLabel("联系人:");
		text_link.setBounds(200, 50, 60, 20);
		jPanel.add(text_link, 2);

		input_link = new JTextField(accountInfo.getContact());
		input_link.setBounds(260, 50, 100, 20);
		jPanel.add(input_link);

		text_tel = new JLabel("联系电话:");
		text_tel.setBounds(20, 80, 60, 20);
		jPanel.add(text_tel, 2);

		input_tel = new JTextField(accountInfo.getTelPhone());
		input_tel.setBounds(80, 80, 100, 20);
		jPanel.add(input_tel);

		submit = new JButton("提交");
		submit.setBounds(70, 110, 70, 20);
		submit.addActionListener(this);
		jPanel.add(submit);

		cancel = new JButton("取消");
		cancel.setBounds(170, 110, 70, 20);
		cancel.addActionListener(this);
		jPanel.add(cancel);

		this.add(jPanel);
		this.setResizable(false);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if ("提交".equals(source.getText())) {
			String airCompany = input_orderNo.getSelectedItem().toString();
			String account = input_account.getText();
			String password = input_pwd.getText();
			String link = input_link.getText();
			String tel = input_tel.getText();
			String errMsg = null;
			if (StringUtil.isEmpty(account)) {
				errMsg = "请输入账号";
			} else if (StringUtil.isEmpty(password)) {
				errMsg = "请输入密码";
			} else if (StringUtil.isEmpty(link)) {
				errMsg = "请输入联系人";
			} else if (StringUtil.isEmpty(tel)&&checkPhone(tel)) {
				errMsg = "请输入联系号码";
			}
			if (errMsg!=null) {
				MsgUtil.errorRemind(errMsg);
			}
			if (!airCompany.equals(accountInfo.getAirCompany())||!account.equals(accountInfo.getAccount())||!password.equals(accountInfo.getPassword())) {
				try {
					SysData.accountMap.get(airCompany).get(accountInfo.getId()).setLoginState(null);
					SysData.accountMap.get(airCompany).get(accountInfo.getId()).setLoginTime(null);
					SysData.accountMap.get(airCompany).get(accountInfo.getId()).setKeepLogin(false);
				} catch (Exception e1) {
				}
			}
			accountInfo.setPassword(password);
			accountInfo.setContact(link);
			accountInfo.setTelPhone(tel);
			accountInfo.setAirCompany(airCompany);
			accountInfo.setAccount(account);

			String result = SqliteHander.modifyObjInfo(accountInfo, null);
			if ("true".equals(result)) {
				MsgUtil.confirmRemind("修改成功");
				GrabListPanel.loadListData(grabListPanel);
			} else {
				MsgUtil.errorRemind(result);
			}
		}
		this.dispose();
	}
	private boolean checkPhone(String telphone) {
		if (StringUtil.isEmpty(telphone)||telphone.trim().length()!=11) {
			return false;
		}
		Pattern p = Pattern.compile("[0-9]*");
		return p.matcher(telphone).matches();
	}
	private boolean checkPrice(String price) {
		try {
			 Float.parseFloat(price);
			 return true;
		} catch (Exception e) {
			e.getStackTrace();
		}
		return false;
	}
}
