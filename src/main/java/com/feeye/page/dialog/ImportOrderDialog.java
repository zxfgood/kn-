package com.feeye.page.dialog;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.eltima.components.ui.DatePicker;
import com.feeye.entity.OrderInfo;
import com.feeye.entity.PaxInfo;
import com.feeye.handler.ReqHandler;
import com.feeye.handler.SqliteHander;
import com.feeye.init.SysData;
import com.feeye.page.panel.OrderListPanel;
import com.feeye.util.InitUtil;
import com.feeye.util.MsgUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/22 09:43
 */
public class ImportOrderDialog extends JDialog implements ActionListener{

	private static final Logger logger = Logger.getLogger(ImportOrderDialog.class);
	private OrderListPanel parentPanel;

	public ImportOrderDialog(Frame frame, OrderListPanel rootPanel, int width, int height, OrderInfo orderInfo) {
		super(frame,true);
		this.setResizable(true);
		parentPanel = rootPanel;
		this.setTitle("订单补录");
		this.setLayout(null);
		Dimension dimension = this.getToolkit().getScreenSize();
		this.setBounds((dimension.width-width)/2, (dimension.height-height)/2, width, height);

		JPanel jPanel = new JPanel(null);
		jPanel.setBorder(BorderFactory.createEtchedBorder());
		jPanel.setBounds(0, 0, width, height);
		this.add(jPanel);
		JLabel text_orderNo = new JLabel("平台类型:");
		text_orderNo.setBounds(10, 20, 60, 20);
		jPanel.add(text_orderNo);

		JComboBox input_orderNo = new JComboBox();
		input_orderNo.setBounds(70, 20, 80, 20);
		input_orderNo.addItem("去哪儿");
		input_orderNo.addItem("同程");
		input_orderNo.addItem("淘宝");
		input_orderNo.addItem("携程");
		input_orderNo.addItem("酷讯");
		input_orderNo.addItem("就旅行");
		jPanel.add(input_orderNo);

		JLabel text_platform = new JLabel("订单号:");
		text_platform.setBounds(10, 80, 50, 20);
		jPanel.add(text_platform);

		JTextArea input_platform = new JTextArea();
		input_platform.setBounds(60, 50, 200, 80);
		input_platform.setLineWrap(true);        //激活自动换行功能
		input_platform.setWrapStyleWord(true);
		input_platform.setFont(new Font("宋体", Font.BOLD, 14));
		jPanel.add(input_platform);

		String text = "格式(订单号;订单号;订单号),";
		JLabel text_orderNos = new JLabel(text);
		text_orderNos.setBounds(262, 80, 170, 20);
		text_orderNos.setForeground(Color.red);
		jPanel.add(text_orderNos);
		String text2 = "携程订单请填写出票单号";
		JLabel text_orderNos2 = new JLabel(text2);
		text_orderNos2.setBounds(262, 100, 150, 20);
		text_orderNos2.setForeground(Color.red);
		jPanel.add(text_orderNos2);

		JButton submit = new JButton("补录");
		submit.setBounds(320, 140, 60, 20);
		submit.addActionListener(this);
		jPanel.add(submit);

		this.setResizable(false);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		JPanel panel = (JPanel) source.getParent();
		Component[] components = panel.getComponents();
		JComboBox component = (JComboBox) components[1];
		String platform = component.getSelectedItem().toString();
		if ("去哪儿".equals(platform)) {
			platform = "qunaer";
		} else if ("同程".equals(platform)) {
			platform = "tongcheng";
		} else if ("淘宝".equals(platform)) {
			platform = "taobao";
		} else if ("携程".equals(platform)) {
			platform = "xiecheng";
		} else if ("酷讯".equals(platform)) {
			platform = "kuxun";
		} else if ("就旅行".equals(platform)) {
			platform = "jiulvxing";
		}
		JTextArea component1 = (JTextArea) components[3];
		String orderNos = component1.getText();
		if (StringUtil.isEmpty(orderNos)) {
			MsgUtil.errorRemind("请输入订单号");
			return;
		}
		String result = ReqHandler.importOrder(orderNos.replace("；", "").trim(), platform);
		if (!"true".equals(result)) {
			MsgUtil.errorRemind(result);
			return;
		} else {
			MsgUtil.confirmRemind("添加成功");
			this.dispose();
			OrderListPanel.loadListData(parentPanel);
		}
	}
}
