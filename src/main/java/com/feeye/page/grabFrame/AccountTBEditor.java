package com.feeye.page.grabFrame;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellRenderer;

import com.feeye.entity.AccountInfo;
import com.feeye.entity.OrderInfo;
import com.feeye.entity.PaxInfo;
import com.feeye.handler.ReqHandler;
import com.feeye.init.SysData;
import com.feeye.page.dialog.ConfirmGrabDialog;
import com.feeye.page.dialog.UpdateAccountDialog;
import com.feeye.page.dialog.UpdateOrderDialog;
import com.feeye.page.panel.GrabListPanel;
import com.feeye.page.panel.OrderListPanel;
import com.feeye.service.KNAppOutticketService;
import com.feeye.util.MsgUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/25 17:19
 */
public class AccountTBEditor extends DefaultCellEditor implements TableCellRenderer {

	private JPanel panel;

	private JLabel bt_grab;
	private JLabel bt_edit;

	public volatile boolean loginOver = true;

	public AccountTBEditor() {
		super(new JTextField());
		// 设置点击几次激活编辑。
		this.setClickCountToStart(1);
		initButton();
		this.panel = new JPanel(null);
		this.panel.add(bt_grab);
		this.panel.add(bt_edit);
	}

	private void initButton() {
		this.bt_grab = new JLabel("登录");
		this.bt_grab.setBounds(30, 0, 50, 15);

		this.bt_grab.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JLabel source = (JLabel) e.getSource();
				JRootPane rootPane = source.getRootPane();
				// GrabListPanel instance = (GrabListPanel) rootPane.findComponentAt(2, 25);
				GrabListPanel instance = SysData.grabListPanel;
				String[] split = panel.getName().split("-");
				if (!loginOver) {
					return;
				}
				loginOver = false;
				Collection<AccountInfo> accountInfos = SysData.accountMap.get(split[1]).values();
				AccountInfo accountInfo = null;
				for (AccountInfo info : accountInfos) {
					if (split[0].equals(info.getId()+"")) {
						accountInfo = info;
					}
				}
				if (accountInfo==null) {
					MsgUtil.errorRemind("未找到该账号");
				}
				if ("KN".equals(accountInfo.getAirCompany())) {
					try {
						SysData.accountMap.get("KN").get(accountInfo.getId()).setKeepLogin(true);
					} catch (Exception e1) {
					}
				}
				String result = new KNAppOutticketService().login(accountInfo);
				if ("true".equals(result)) {
					MsgUtil.confirmRemind("登录成功");
					instance.queryAccount();
				} else {
					MsgUtil.errorRemind(result);
				}
				loginOver = true;
			}
		});

		this.bt_edit = new JLabel("更改");
		this.bt_edit.setBounds(80, 0, 50, 15);

		this.bt_edit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			JLabel source = (JLabel) e.getSource();
			JRootPane rootPane = source.getRootPane();
			// GrabListPanel instance = (GrabListPanel) rootPane.findComponentAt(2, 25);
			GrabListPanel instance = SysData.grabListPanel;
			String[] split = panel.getName().split("-");
			Collection<AccountInfo> accountInfos = SysData.accountMap.get(split[1]).values();
			AccountInfo accountInfo = null;
			for (AccountInfo info : accountInfos) {
				if (split[0].equals(info.getId()+"")) {
					accountInfo = info;
				}
			}
			if (accountInfo==null) {
				MsgUtil.errorRemind("未找到该账号");
			}
			new UpdateAccountDialog(JOptionPane.getRootFrame(), instance, 400, 200, accountInfo);
			}
		});
	}

	/**
	 * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格）
	 */
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		this.panel.setName(table.getValueAt(row, 0).toString()+"-"+table.getValueAt(row, 2).toString());  //存储订单ID
		return this.panel;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		return this.panel;
	}
}
