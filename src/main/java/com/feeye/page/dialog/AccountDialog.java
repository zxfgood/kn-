package com.feeye.page.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.feeye.entity.AccountInfo;
import com.feeye.entity.OrderInfo;
import com.feeye.handler.ReqHandler;
import com.feeye.handler.SqliteHander;
import com.feeye.page.panel.GrabListPanel;
import com.feeye.page.panel.OrderListPanel;
import com.feeye.util.MsgUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/23 11:00
 */
public class AccountDialog extends JDialog implements ActionListener {

	private static final Logger logger = Logger.getLogger(OrderDialog.class);
	private GrabListPanel parentPanel;

	public AccountDialog(Frame frame, GrabListPanel rootPanel, int width, int height, AccountInfo accountInfo) {

		super(frame,true);
		this.setResizable(true);
		parentPanel = rootPanel;
		this.setTitle("账号录入");
		if (accountInfo!=null) {
			this.setTitle("账号修改");
		}
		this.setLayout(null);
		Dimension dimension = this.getToolkit().getScreenSize();
		this.setBounds((dimension.width-width)/2, (dimension.height-height)/2, width, height);

		JPanel jPanel = new JPanel(null);
		jPanel.setBorder(BorderFactory.createEtchedBorder());
		jPanel.setBounds(0, 0, width, height);
		this.add(jPanel, 0);
		JLabel text_orderNo = new JLabel("账号");
		text_orderNo.setBounds(20, 20, 50, 20);
		jPanel.add(text_orderNo, 0);

		JTextField input_orderNo = new JTextField();
		input_orderNo.setBounds(70, 20, 100, 20);
		jPanel.add(input_orderNo, 1);

		JLabel text_platform = new JLabel("密码");
		text_platform.setBounds(200, 20, 60, 20);
		jPanel.add(text_platform, 2);

		JTextField input_platform = new JTextField();
		input_platform.setBounds(260, 20, 100, 20);
		jPanel.add(input_platform, 3);

		JLabel text_dep = new JLabel("联系人");
		text_dep.setBounds(20, 50, 50, 20);
		jPanel.add(text_dep, 4);

		JTextField input_dep = new JTextField();
		input_dep.setBounds(70, 50, 100, 20);
		jPanel.add(input_dep, 5);

		JLabel text_arr = new JLabel("联系电话");
		text_arr.setBounds(200, 50, 60, 20);
		jPanel.add(text_arr, 6);

		JTextField input_tel = new JTextField();
		input_tel.setBounds(260, 50, 100, 20);
		jPanel.add(input_tel, 7);

		JLabel input_arr = new JLabel("航司");
		input_arr.setBounds(20, 80, 50, 20);
		jPanel.add(input_arr, 8);

		JComboBox input_cardType = new JComboBox();
		input_cardType.addItem("KN");
		input_cardType.addItem("MF");
		input_cardType.addItem("MU");
		input_cardType.addItem("SC");
		input_cardType.addItem("8L");
		input_cardType.setBounds(70, 80, 60, 20);
		jPanel.add(input_cardType, 9);

		JButton submit = new JButton("提交");
		submit.setBounds(100, 120, 60, 20);
		submit.addActionListener(this);
		jPanel.add(submit, 10);

		JButton cancel = new JButton("取消");
		cancel.setBounds(220, 120, 60, 20);
		cancel.addActionListener(this);
		jPanel.add(cancel, 11);


		this.setResizable(false);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if ("取消".equals(source.getText())) {
			this.dispose();
		} else if ("提交".equals(source.getText())) {
			JPanel panel = (JPanel) source.getParent();
			Component[] components = panel.getComponents();
			String account = ((JTextField) components[1]).getText();
			String password = ((JTextField) components[3]).getText();
			String contact = ((JTextField) components[5]).getText();
			String telphone = ((JTextField) components[7]).getText();
			String airCompany = ((JComboBox) components[9]).getSelectedItem().toString();
			String errMsg = null;
			if (StringUtil.isEmpty(account)) {
				errMsg = "账号不能为空";
			} else if (StringUtil.isEmpty(password)) {
				errMsg = "密码不能为空";
			} else if (StringUtil.isEmpty(contact)) {
				errMsg = "联系人不能为空";
			} else if (!checkPhone(telphone)) {
				errMsg = "电话号码不符";
			}
			if (errMsg!=null) {
				MsgUtil.errorRemind(errMsg);
				return;
			}
			if (MsgUtil.selectRemind("确认添加")!=0) {
				return;
			}
			AccountInfo info = new AccountInfo();
			info.setAccount(account);
			info.setPassword(password);
			info.setContact(contact);
			info.setTelPhone(telphone);
			info.setAirCompany(airCompany);
			String result = SqliteHander.addObjInfo(info);
			if (!"true".equals(result)) {
				MsgUtil.errorRemind(result);
				return;
			} else {
				MsgUtil.confirmRemind("添加成功");
				this.dispose();
				GrabListPanel.loadListData(parentPanel);
			}
		}
	}

	private boolean checkPhone(String telphone) {
		if (StringUtil.isEmpty(telphone)||telphone.trim().length()!=11) {
			return false;
		}
		Pattern p = Pattern.compile("[0-9]*");
		return p.matcher(telphone).matches();
	}

	public class OrderMouseAdapter extends MouseAdapter {
		private List<Component> components;
		public OrderMouseAdapter(List<Component> components) {
			this.components = components;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			OrderDialog dialog = (OrderDialog) components.get(0);
			if (dialog.getHeight()>600) {
				MsgUtil.errorRemind("不能再添加乘客");
				return;
			}
			dialog.setSize(dialog.getWidth(), dialog.getHeight()+60);
			JPanel panel = (JPanel) components.get(1);
			panel.setSize(panel.getWidth(), panel.getHeight()+60);
			JButton submit = (JButton) panel.getComponent(12);
			submit.setLocation(submit.getX(), submit.getY()+60);
			JButton cancel = (JButton) panel.getComponent(13);
			cancel.setLocation(cancel.getX(), cancel.getY()+60);
			JButton addPax = (JButton) panel.getComponent(14);
			addPax.setLocation(addPax.getX(), addPax.getY()+60);

			JLabel text_cardType = new JLabel("证件类型");
			text_cardType.setBounds(20, panel.getHeight()-140, 60, 20);
			panel.add(text_cardType, panel.getComponents().length);

			JComboBox input_cardType = new JComboBox();
			input_cardType.addItem("身份证");
			input_cardType.addItem("护照");
			input_cardType.addItem("学生证");
			input_cardType.addItem("军人证");
			input_cardType.addItem("回乡证");
			input_cardType.addItem("台胞证");
			input_cardType.addItem("港澳通行证");
			input_cardType.addItem("国际海员证");
			input_cardType.addItem("外国人永久居住证");
			input_cardType.addItem("其他");
			input_cardType.setBounds(80, panel.getHeight()-140, 100, 20);
			panel.add(input_cardType, panel.getComponents().length);

			JLabel text_name = new JLabel("姓名");
			text_name.setBounds(190, panel.getHeight()-140, 40, 20);
			panel.add(text_name, panel.getComponents().length);

			JTextField input_name = new JTextField();
			input_name.setBounds(230, panel.getHeight()-140, 80, 20);
			panel.add(input_name, panel.getComponents().length);

			JLabel text_paxType = new JLabel("乘客类型");
			text_paxType.setBounds(320, panel.getHeight()-140, 60, 20);
			panel.add(text_paxType, panel.getComponents().length);
			JComboBox input_paxType = new JComboBox();
			input_paxType.addItem("成人");
			input_paxType.addItem("儿童");
			input_paxType.addItem("婴儿");
			input_paxType.setBounds(380, panel.getHeight()-140, 80, 20);
			panel.add(input_paxType, panel.getComponents().length);

			JLabel text_cardNo = new JLabel("证件号");
			text_cardNo.setBounds(20, panel.getHeight()-110, 50, 20);
			panel.add(text_cardNo, panel.getComponents().length);

			JTextField input_cardNo = new JTextField();
			input_cardNo.setBounds(80, panel.getHeight()-110, 120, 20);
			panel.add(input_cardNo, panel.getComponents().length);

			JLabel text_sellPrice = new JLabel("销售价");
			text_sellPrice.setBounds(220, panel.getHeight()-110, 60, 20);
			panel.add(text_sellPrice, panel.getComponents().length);

			JTextField input_sellPrice = new JTextField();
			input_sellPrice.setBounds(270, panel.getHeight()-110, 100, 20);
			panel.add(input_sellPrice, panel.getComponents().length);
			dialog.add(panel);
		}
	}
	private boolean checkDate(String date) {
		try {
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
			return true;
		} catch (ParseException e) {
			logger.error("error", e	);
			return false;
		}
	}
}
