package com.feeye.page.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.alibaba.fastjson.JSONArray;
import com.feeye.entity.AccountInfo;
import com.feeye.entity.OrderInfo;
import com.feeye.entity.PaxInfo;
import com.feeye.handler.ReqHandler;
import com.feeye.init.SysData;
import com.feeye.page.dialog.AccountDialog;
import com.feeye.page.grabFrame.AccountTBEditor;
import com.feeye.page.grabFrame.CheckBoxEditor;
import com.feeye.page.grabFrame.GrabTBEditor;
import com.feeye.util.MsgUtil;
import com.feeye.util.PageUtil;
import com.feeye.util.StringUtil;

/**
 * @description: 订票列表页面!
 * @author: domcj
 * @date: 2019/01/16 14:03
 */
public class SetttingPanel extends JPanel{
	private static JPanel panel_thread;

	public SetttingPanel() {
		this.setLayout(null);
		initThread();
		this.add(panel_thread);
	}

	private void initThread() {
		panel_thread = new JPanel(null);
		panel_thread.setBounds(5,10,1085, 30);
		panel_thread.setBorder(BorderFactory.createEtchedBorder());

		JLabel jtp = new JLabel("短信通知账号");
		jtp.setBounds(10, 5, 80, 20);
		panel_thread.add(jtp);
		JTextField jtf = new JTextField();
		jtf.setText(SysData.msgAccount);
		jtf.setBounds(90, 5, 100, 20);
		panel_thread.add(jtf);

		JLabel jtp_3 = new JLabel("短信通知密码");
		jtp_3.setBounds(200, 5, 80, 20);
		panel_thread.add(jtp_3);
		JPasswordField jtf_3 = new JPasswordField();
		jtf_3.setText(SysData.msgPwd);
		jtf_3.setBounds(280, 5, 100, 20);
		panel_thread.add(jtf_3);
		// 短信通知手机号码
		JLabel jtn = new JLabel("短信通知手机号");
		jtn.setBounds(390, 5, 100, 20);
		panel_thread.add(jtn);
		JTextField jtnf = new JTextField();
		jtnf.setText(SysData.phonenum);
		jtnf.setBounds(490, 5, 100, 20);
		panel_thread.add(jtnf);

		// 通知邮箱号
		JLabel jte = new JLabel("邮箱");
		jte.setBounds(600, 5, 80, 20);
		panel_thread.add(jte);
		JTextField jtef = new JTextField();
		jtef.setText(SysData.email.trim());
		jtef.setBounds(640, 5, 140, 20);
		panel_thread.add(jtef);

		JButton jb_query = new JButton("更改");
		jb_query.setBackground(Color.LIGHT_GRAY);
		jb_query.setBounds(790, 5, 120, 20);
		panel_thread.add(jb_query);
		jb_query.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String threadNum = jtf.getText();
				String delaySec = String.valueOf(jtf_3.getPassword());
				String phonenum = jtnf.getText();
				String eamil = jtef.getText();
				String errMsg = null;
				if (StringUtil.isEmpty(threadNum)) {
					errMsg = "请输入短信账号";
				} else if (StringUtil.isEmpty(delaySec)) {
					errMsg = "请输入短信密码";
				} else if (StringUtil.isEmpty(phonenum.trim())) {
					errMsg = "请输入电话号码";
				} else if (!StringUtil.isMobileNum(phonenum)) {
					errMsg = "电话号码格式不正确";
				} else if (StringUtil.isEmpty(eamil)) {
					errMsg = "请输入邮箱账号";
				} else if (!StringUtil.isEmail(eamil)) {
					errMsg = "邮箱账号格式错误";
				}

				if (errMsg!=null) {
					MsgUtil.errorRemind(errMsg);
					return;
				}
				SysData.msgAccount = threadNum;
				SysData.msgPwd = delaySec;
				SysData.phonenum = phonenum;
				SysData.email = eamil;
				String content = "msgAccount="+threadNum+"\n"+"msgPwd="+delaySec+"\n"+
							"phonenum="+phonenum+"\n"+"email="+eamil ;
				boolean result = SysData.writeTxt(SysData.msgUrl, content);
				if (result) {
					MsgUtil.confirmRemind("更新成功");
				} else {
					MsgUtil.errorRemind("更新失败");
				}
			}
		});

		/*JLabel jtp_4 = new JLabel("吉信通抢票短信通知(http://winic.org)");
		jtp_4.setBounds(920, 5, 400, 20);
		jtp_4.setFont(new Font("宋体", Font.BOLD, 12));
		panel_thread.add(jtp_4);*/
	}
}
