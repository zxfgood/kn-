package com.feeye.page.dialog;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.feeye.entity.AccountInfo;
import com.feeye.entity.OrderInfo;
import com.feeye.handler.ReqHandler;
import com.feeye.handler.SqliteHander;
import com.feeye.init.SysData;
import com.feeye.page.panel.GrabListPanel;
import com.feeye.page.panel.OrderListPanel;
import com.feeye.util.MsgUtil;
import com.feeye.util.StringUtil;
import com.feeye.service.KNAppOutticketService;
import com.google.common.collect.Lists;

/**
 * @description: 抢票信息弹出框!
 * @author: domcj
 * @date: 2019/01/18 11:53
 */
public class ConfirmGrabDialog extends JDialog implements ActionListener{

	private static final Logger logger = Logger.getLogger(ConfirmGrabDialog.class);

	private JLabel text_price;
	private JTextField input_price;
	private JLabel text_tips;
	private JLabel text_account;
	private JComboBox box_account;
	private JButton cancel;
	private JButton submit;
	private String orderId;
	private OrderListPanel instance;

	public static ExecutorService taskService = Executors.newCachedThreadPool();

	public ConfirmGrabDialog(Frame frame, int width, int height, String orderId, OrderListPanel instance) {
		super(frame,true);
		this.setResizable(true);
		this.setTitle("抢票设置");
		this.setLayout(null);
		this.orderId = orderId;
		this.instance = instance;
		Dimension dimension = this.getToolkit().getScreenSize();
		this.setBounds((dimension.width-width)/2, (dimension.height-height)/2, width, height);

		JPanel jPanel = new JPanel(null);
		jPanel.setBorder(BorderFactory.createEtchedBorder());
		jPanel.setBounds(0, 0, width, height);

		text_price = new JLabel("接受价格");
		text_price.setBounds(20, 20, 60, 20);
		jPanel.add(text_price, 0);

		input_price = new JTextField();
		input_price.setBounds(80, 20, 50, 20);
		jPanel.add(input_price, 1);

		text_tips = new JLabel("以下进行抢单");
		text_tips.setBounds(130, 20, 100, 20);
		jPanel.add(text_tips, 2);

		text_account = new JLabel("官网账号");
		text_account.setBounds(20, 70, 60, 20);
		jPanel.add(text_account, 3);
		Collection<AccountInfo> accountInfos = new ArrayList<>();
		Map<Long, AccountInfo> map = SysData.accountMap.get(SysData.airCompany);
		if (map!=null) {
			accountInfos = map.values();
		}
		box_account = new JComboBox();
		if (!accountInfos.isEmpty()) {
			for (AccountInfo accountInfo : accountInfos) {
				box_account.addItem(accountInfo.getAccount());
			}
		}
		box_account.setBounds(80, 70, 120, 20);
		jPanel.add(box_account, 4);

		submit = new JButton("提交");
		submit.setBounds(30, 120, 70, 20);
		submit.addActionListener(this);
		jPanel.add(submit, 4);

		cancel = new JButton("取消");
		cancel.setBounds(120, 120, 70, 20);
		cancel.addActionListener(this);
		jPanel.add(cancel, 5);

		this.add(jPanel);
		this.setResizable(false);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if ("提交".equals(source.getText())) {
			String grabPrice = input_price.getText();
			if (!checkPrice(grabPrice)) {
				MsgUtil.errorRemind("价格输入有误");
				return;
			}
			Object selectedItem = box_account.getSelectedItem();
			if (selectedItem==null) {
				MsgUtil.errorRemind("请配置出票账号");
				return;
			}
			String input_account = selectedItem.toString();
			if (StringUtil.isEmpty(input_account)) {
				MsgUtil.errorRemind("请选择出票账号");
				return;
			}
			AccountInfo accountInfo = null;
			for (AccountInfo account : SysData.accountMap.get(SysData.airCompany).values()) {
				if (input_account.equals(account.getAccount())) {
					accountInfo = account;
					break;
				}
			}
			if (accountInfo==null) {
				MsgUtil.errorRemind("出票账号选择有误");
				return;
			}
			if (SysData.grabOrderMap.containsKey(orderId)) {
				MsgUtil.errorRemind("已加入抢票");
				return;
			}
			OrderInfo orderInfo = SysData.orderMap.get(orderId);
			orderInfo.setAccount(accountInfo.getAccount());
			orderInfo.setTelPhone(accountInfo.getTelPhone());
			orderInfo.setGrabPrice(grabPrice);
			orderInfo.setGrabOver(false);
			orderInfo.setOrderStatus("抢票中");
			orderInfo.setGrabStatus("价格刷取");
			orderInfo.setOutPrice("");
			String result = SqliteHander.modifyObjInfo(orderInfo, null);
			if (!"true".equals(result)) {
				MsgUtil.errorRemind(result);
				return;
			}
			SysData.grabOrderMap.put(orderId, orderInfo);   //加入抢票列表
			if ("KN".equals(accountInfo.getAirCompany())) {
				try {
					SysData.accountMap.get("KN").get(accountInfo.getId()).setKeepLogin(true);
				} catch (Exception e1) {
				}
			}
			MsgUtil.confirmRemind("加入抢票成功");
			instance.queryOrder();
			taskService.execute(new AddPaxTask(orderInfo, accountInfo));
		}
		this.dispose();
	}

	public class AddPaxTask implements Runnable {
		private OrderInfo orderInfo;
		private AccountInfo accountInfo;

		public AddPaxTask(OrderInfo orderInfo, AccountInfo accountInfo) {
			this.orderInfo = orderInfo;
			this.accountInfo = accountInfo;
		}

		@Override
		public void run() {
			try {
				if ("KN".equals(accountInfo.getAirCompany())) {
					new KNAppOutticketService().startAddPax(orderInfo, accountInfo);
				}
			} catch (Throwable e) {
				logger.error("添加乘机人异常", e);
			}
		}
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
