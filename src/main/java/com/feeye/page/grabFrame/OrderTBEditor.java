package com.feeye.page.grabFrame;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import com.feeye.entity.OrderInfo;
import com.feeye.entity.PaxInfo;
import com.feeye.handler.ReqHandler;
import com.feeye.handler.SqliteHander;
import com.feeye.init.SysData;
import com.feeye.page.dialog.ConfirmGrabDialog;
import com.feeye.page.dialog.ModifyOrderDialog;
import com.feeye.page.dialog.UpdateOrderDialog;
import com.feeye.page.panel.OrderListPanel;
import com.feeye.util.MsgUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/17 17:11
 */
public class OrderTBEditor extends DefaultCellEditor implements TableCellRenderer {

	private JPanel panel;

	private JLabel bt_grab;
	private JLabel bt_pay;
	private JLabel bt_back;
	private JLabel bt_edit;

	public boolean isCellEditable(int row, int column) {
		return false;
	}

	public OrderTBEditor() {
		super(new JTextField());
		// 设置点击几次激活编辑。
		this.setClickCountToStart(1);
		initButton();
		this.panel = new JPanel(null);
		this.panel.add(bt_grab);
		this.panel.add(bt_pay);
//		this.panel.add(bt_back);
		this.panel.add(bt_edit);
	}

	private void initButton() {
		this.bt_grab = new JLabel("加入抢票");
		this.bt_grab.setBounds(80, 0, 60, 15);

		this.bt_pay = new JLabel("点击支付");
		this.bt_pay.setBounds(80, 0, 60, 15);

		this.bt_pay.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			OrderInfo orderInfo = SysData.orderMap.get(panel.getName());
			if (StringUtil.isEmpty(orderInfo.getLocation())||!"官网待支付".equals(orderInfo.getOrderStatus())) {
				MsgUtil.errorRemind("当前不能支付");
				return;
			}
//				if ("KN".equals(orderInfo.getFlightNo().substring(0, 1))) {
//					if ((orderInfo.getCreatTime().longValue()-System.currentTimeMillis())/1000/60>14) {
//						MsgUtil.errorRemind("订单支付超时");
//						orderInfo.setOrderStatus("等待出票");
//						orderInfo.setLocation(null);
//						orderInfo.setCreatTime(null);
//						orderInfo.setCookie(null);
//						List<OrderInfo> list = Lists.newArrayList();
//						list.add(orderInfo);
//						ReqHandler.updateOrderInfo(list);
//						return;
//					}
//				}
			try {
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler "+orderInfo.getLocation());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			}
		});

		this.bt_back = new JLabel("票号回填");
		this.bt_back.setBounds(60, 0, 60, 15);
		this.bt_back.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				OrderInfo orderInfo = SysData.orderMap.get(panel.getName());
				for (PaxInfo paxInfo : orderInfo.getPaxInfos()) {
					if (StringUtil.isEmpty(paxInfo.getTicketNo())) {
						MsgUtil.errorRemind("票号不能为空!");
						return;
					}
				}
				String result = ReqHandler.modifyOrderInfo(orderInfo);
				String[] fileds = {"orderStatus"};
				if ("true".equals(result)) {
					orderInfo.setOrderStatus("出票完成");
					result = SqliteHander.modifyObjInfo(orderInfo, fileds);
					if ("true".equals(result)) {
						MsgUtil.confirmRemind("回填成功");
					}
				} else {
					orderInfo.setOrderStatus("回填失败");
					SqliteHander.modifyObjInfo(orderInfo, fileds);
					MsgUtil.errorRemind(result);
				}
			}
		});

		this.bt_edit = new JLabel("订单更改");
		this.bt_edit.setBounds(15, 0, 60, 15);
		this.bt_grab.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JLabel source = (JLabel) e.getSource();
				JRootPane rootPane = source.getRootPane();
				// OrderListPanel instance = (OrderListPanel) rootPane.findComponentAt(2, 25);
				OrderListPanel instance = new OrderListPanel();
				new ConfirmGrabDialog(JOptionPane.getRootFrame(), 250, 200, panel.getName(), instance);
			}
		});
		this.bt_edit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JLabel source = (JLabel) e.getSource();
				JRootPane rootPane = source.getRootPane();
				// 无法强制转换
				// OrderListPanel componentAt = (OrderListPanel) rootPane.findComponentAt(2, 25);
				// Component component = rootPane.findComponentAt(2, 25);
				// Component
				// OrderListPanel componentAt = OrderListPanel.class.cast(component);
				OrderListPanel componentAt = new OrderListPanel();
				OrderInfo orderInfo = SysData.orderMap.get(panel.getName());
				new ModifyOrderDialog(JOptionPane.getRootFrame(), componentAt,570, 200, orderInfo);
			}
		});
	}

	/**
	 * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格）
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.panel.setName(table.getValueAt(row, 0).toString());  //存储订单ID
		OrderInfo orderInfo = SysData.orderMap.get(table.getValueAt(row, 0).toString());
		if ("等待出票".equals(orderInfo.getOrderStatus())) {
			this.panel.getComponent(0).setVisible(true);
//			this.panel.getComponent(1).setVisible(false);
		} else {
			this.panel.getComponent(0).setVisible(false);
		}
		if (StringUtil.isNotEmpty(orderInfo.getLocation())&&"官网待支付".equals(orderInfo.getOrderStatus())) {
			this.panel.getComponent(1).setVisible(true);
		} else {
			this.panel.getComponent(1).setVisible(false);
		}
		int rowHeight = table.getRowHeight(row);
		this.panel.getComponent(0).setLocation(panel.getComponent(0).getX(), rowHeight/2-8);
		this.panel.getComponent(1).setLocation(panel.getComponent(1).getX(), rowHeight/2-8);
		this.panel.getComponent(2).setLocation(panel.getComponent(2).getX(), rowHeight/2-8);
//		this.panel.getComponent(3).setLocation(panel.getComponent(3).getX(), rowHeight/2-8);
		return this.panel;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		OrderInfo orderInfo = SysData.orderMap.get(table.getValueAt(row, 0).toString());
		if (orderInfo==null) {
			JRootPane rootPane = table.getRootPane();
			// OrderListPanel instatnce = (OrderListPanel) rootPane.findComponentAt(2, 25);
			OrderListPanel instatnce = new OrderListPanel();
			instatnce.queryOrder();
		}
		if ("等待出票".equals(orderInfo.getOrderStatus())) {
			this.panel.getComponent(0).setVisible(true);
//			this.panel.getComponent(1).setVisible(false);
		} else {
			this.panel.getComponent(0).setVisible(false);
		}
		if (StringUtil.isNotEmpty(orderInfo.getLocation())&&"官网待支付".equals(orderInfo.getOrderStatus())) {
			this.panel.getComponent(1).setVisible(true);
		} else {
			this.panel.getComponent(1).setVisible(false);
		}
		int rowHeight = table.getRowHeight(row);
		this.panel.getComponent(0).setLocation(panel.getComponent(0).getX(), rowHeight/2-8);
		this.panel.getComponent(1).setLocation(panel.getComponent(1).getX(), rowHeight/2-8);
		this.panel.getComponent(2).setLocation(panel.getComponent(2).getX(), rowHeight/2-8);
//		this.panel.getComponent(3).setLocation(panel.getComponent(3).getX(), rowHeight/2-8);
		return this.panel;
	}
}
