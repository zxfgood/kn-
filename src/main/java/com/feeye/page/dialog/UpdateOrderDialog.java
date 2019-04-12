package com.feeye.page.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
import com.feeye.page.panel.OrderListPanel;
import com.feeye.util.MsgUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;

/**
 * @description: 抢票信息弹出框!
 * @author: domcj
 * @date: 2019/01/18 11:53
 */
public class UpdateOrderDialog extends JDialog implements ActionListener{

	private static final Logger logger = Logger.getLogger(UpdateOrderDialog.class);

	private JPanel jPanel;
	private JLabel text_orderNo;
	private JLabel input_orderNo;
	private JLabel text_status;
	private JComboBox box_status;
	private JButton cancel;
	private JButton submit;
	private OrderInfo orderInfo;
	private OrderListPanel orderListPanel;

	public static ExecutorService taskService = Executors.newCachedThreadPool();

	public UpdateOrderDialog(Frame frame, OrderListPanel orderListPanel, int width, int height, String orderId) {
		super(frame,true);
		this.setResizable(true);
		this.setTitle("订单更改");
		this.setLayout(null);
		orderInfo = SysData.orderMap.get(orderId);
		this.orderListPanel = orderListPanel;
		Dimension dimension = this.getToolkit().getScreenSize();
		this.setBounds((dimension.width-width)/2, (dimension.height-height)/2, width, height);

		jPanel = new JPanel(null);
		jPanel.setBorder(BorderFactory.createEtchedBorder());
		jPanel.setBounds(0, 0, width, height);

		text_orderNo = new JLabel("订单号:");
		text_orderNo.setBounds(20, 20, 50, 20);
		jPanel.add(text_orderNo, 0);

		input_orderNo = new JLabel(orderInfo.getOrderNo());
		input_orderNo.setBounds(70, 20, 100, 20);
		jPanel.add(input_orderNo, 1);

		text_status = new JLabel("订单状态:");
		text_status.setBounds(200, 20, 60, 20);
		jPanel.add(text_status, 2);

		box_status = new JComboBox();
		box_status.addItem("等待出票");
		box_status.addItem("官网待支付");
		box_status.addItem("待回填");
		box_status.addItem("回填失败");
		box_status.addItem("回填成功");
		box_status.addItem("票号成功");
		box_status.setSelectedItem(orderInfo.getOrderStatus());
		box_status.setBounds(260, 20, 80, 20);
		jPanel.add(box_status, 3);

		submit = new JButton("提交");
		submit.setBounds(70, 60, 70, 20);
		submit.addActionListener(this);
		jPanel.add(submit, 4);

		cancel = new JButton("取消");
		cancel.setBounds(170, 60, 70, 20);
		cancel.addActionListener(this);
		jPanel.add(cancel, 5);
		for (int i = 0; i < orderInfo.getPaxInfos().size(); i++) {
			this.setSize(this.getWidth(), this.getHeight()+30);
			jPanel.setSize(jPanel.getWidth(), jPanel.getHeight()+30);
			submit.setLocation(submit.getX(), submit.getY()+30);
			cancel.setLocation(cancel.getX(), cancel.getY()+30);
			PaxInfo paxInfo = orderInfo.getPaxInfos().get(i);
			JLabel text_name = new JLabel("姓名:");
			text_name.setBounds(20, 50+30*i, 30, 20);
			jPanel.add(text_name);

			JLabel input_name = new JLabel(paxInfo.getPaxName());
			input_name.setBounds(50, 50+30*i, 50, 20);
			jPanel.add(input_name);

			JLabel text_paxType = new JLabel("证件号:");
			text_paxType.setBounds(110, 50+30*i, 50, 20);
			jPanel.add(text_paxType);

			JLabel input_paxType = new JLabel(paxInfo.getCardNo());
			input_paxType.setBounds(160, 50+30*i, 140, 20);
			jPanel.add(input_paxType);

			JLabel text_cardNo = new JLabel("票号:");
			text_cardNo.setBounds(300, 50+30*i, 30, 20);
			jPanel.add(text_cardNo);

			JTextField input_cardNo = new JTextField();
			input_cardNo.setBounds(330, 50+30*i, 120, 20);
			if (StringUtil.isNotEmpty(paxInfo.getTicketNo())) {
				input_cardNo.setText(paxInfo.getTicketNo());
			}
			jPanel.add(input_cardNo);
		}
		this.add(jPanel);
		this.setResizable(false);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if ("提交".equals(source.getText())) {
			Component[] lists = jPanel.getComponents();
			List<Component> components = Lists.newArrayList();
			for (Component component : lists) {
				if (component instanceof JTextField) {
					components.add(component);
				}
			}
			for (int i = 0; i < components.size(); i++) {
				Object value = ((JTextField) components.get(i)).getText();
				if (value==null) {
					this.orderInfo.getPaxInfos().get(i).setTicketNo(null);
				} else {
					this.orderInfo.getPaxInfos().get(i).setTicketNo(value.toString());
				}
			}
			orderInfo.setOrderStatus(box_status.getSelectedItem().toString());
			String result = SqliteHander.modifyObjInfo(orderInfo, null);
			if ("true".equals(result)) {
				MsgUtil.confirmRemind("更改成功");
				orderListPanel.queryOrder();
			} else {
				MsgUtil.errorRemind(result);
			}
		}
		this.dispose();
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
