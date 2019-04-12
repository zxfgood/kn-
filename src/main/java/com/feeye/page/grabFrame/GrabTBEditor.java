package com.feeye.page.grabFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import com.feeye.entity.OrderInfo;
import com.feeye.handler.OutticketHandler;
import com.feeye.handler.ReqHandler;
import com.feeye.handler.SqliteHander;
import com.feeye.init.SysData;
import com.feeye.page.dialog.ConfirmGrabDialog;
import com.feeye.page.panel.GrabListPanel;
import com.feeye.util.MsgUtil;
import com.google.common.collect.Lists;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/17 17:11
 */
public class GrabTBEditor extends DefaultCellEditor implements TableCellRenderer {

	private JPanel panel;

	private JLabel bt_grab;

	public GrabTBEditor() {
		super(new JTextField());
		// 设置点击几次激活编辑。
		this.setClickCountToStart(1);
		initButton();
		this.panel = new JPanel(null);
		this.panel.add(bt_grab);
	}

	private void initButton() {
		this.bt_grab = new JLabel("取消抢票");
		this.bt_grab.setBounds(5, 0, 60, 15);
		this.bt_grab.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				SysData.cancleGrabOver = false;
				if (MsgUtil.selectRemind("确定取消")==0) {
					if (SysData.grabOrderMap.containsKey(panel.getName())) {
						OrderInfo orderInfo = SysData.grabOrderMap.get(panel.getName());
						OutticketHandler.grabState.remove(Long.parseLong(panel.getName()));
						String[] fileds = {"orderStatus", "grabOver", "grabTime", "grabPrice", "appPrice"};
						if ("抢票中".equals(orderInfo.getOrderStatus())) {
							orderInfo.setOrderStatus("等待出票");
							fileds = new String[]{"orderStatus", "grabOver", "grabTime", "grabPrice", "grabStatus", "appPrice"};
						}
						orderInfo.setGrabOver(true);
						orderInfo.setGrabTime("");
						orderInfo.setGrabPrice("");
						orderInfo.setGrabStatus("");
						orderInfo.setAppPrice("");
//						String[] fileds = {"orderStatus", "grabOver", "grabTime", "grabPrice", "grabStatus"};
						String result = SqliteHander.modifyObjInfo(orderInfo, fileds);
						if (!"true".equals(result)) {
							MsgUtil.errorRemind(result);
							return;
						}
						try {
							SysData.verifyParamMap.remove(orderInfo.getId());
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						SysData.grabOrderMap.remove(panel.getName());
						MsgUtil.confirmRemind("取消成功");
						JLabel component = (JLabel) e.getSource();
						// JRootPane rootPane = component.getRootPane();
						GrabListPanel rootPane = new GrabListPanel();
						GrabListPanel.loadGrabData( rootPane, "0");
					} else {
						MsgUtil.errorRemind("取消失败");
					}
				}
				SysData.cancleGrabOver = true;
			}
		});
	}

	/**
	 * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格）
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.panel.setName(table.getValueAt(row, 0).toString());  //存储订单ID
		return this.panel;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		return this.panel;
	}

}
