package com.feeye.page.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import org.omg.CORBA.INTERNAL;

import com.alibaba.fastjson.JSONArray;
import com.feeye.entity.AccountInfo;
import com.feeye.entity.OrderInfo;
import com.feeye.entity.PaxInfo;
import com.feeye.handler.ReqHandler;
import com.feeye.handler.SqliteHander;
import com.feeye.init.SysData;
import com.feeye.page.dialog.AccountDialog;
import com.feeye.page.dialog.OrderDialog;
import com.feeye.page.grabFrame.AccountTBEditor;
import com.feeye.page.grabFrame.CheckBoxEditor;
import com.feeye.page.grabFrame.GrabTBEditor;
import com.feeye.page.grabFrame.OrderTBEditor;
import com.feeye.util.MsgUtil;
import com.feeye.util.PageUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;

/**
 * @description: 订票列表页面!
 * @author: domcj
 * @date: 2019/01/16 14:03
 */
public class GrabListPanel extends JPanel{
	private static JPanel panel_data;
	private static JPanel panel_thread;
	private static JPanel panel_combine;
	private static JTabbedPane tabbedPane;
	private static JPanel panel_account;
	private static JPanel panel_log;

	private  static int i = 0;
	public GrabListPanel() {
		this.setLayout(null);
		initdata(null, "0");
		this.add(panel_data,0);
		initThread();
		this.add(panel_thread, 1);
		initCombine(null, "0", null, "0");
		this.add(panel_combine, 2);
	}

	private void initCombine(List<String> logs, String logtype, List<AccountInfo> accountInfos, String type) {
		panel_combine = new JPanel(null);
		panel_combine.setBounds(5,390,1090, 250);
		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(0,0,1090, 250);
		this.initlog(logs, logtype);
		this.initaccount(accountInfos, type);
		tabbedPane.addTab("操作日志", panel_log);
		tabbedPane.addTab("官网账号", panel_account);
		tabbedPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane source = (JTabbedPane) e.getSource();
				String title = source.getTitleAt(source.getSelectedIndex());
				if ("官网账号".equals(title)) {
					JRootPane rootPane = source.getRootPane();
					//GrabListPanel instance = (GrabListPanel) rootPane.findComponentAt(2, 25);
//					GrabListPanel instance = new GrabListPanel();
//					instance.queryAccount();

					List<AccountInfo> accountInfos = SqliteHander.queryAccountInfo();
					initaccount(accountInfos, "1");
				}
			}
		});
		panel_combine.add(tabbedPane);
	}
	public void initlog(List<String> logs, String type) {
		if ("0".equals(type)) {
			this.panel_log = new JPanel(null);
			panel_log.setBounds(0,0,1090, 250);
			JTextArea jTextArea = new JTextArea();
			jTextArea.setBounds(0,0,1090, 250);
			jTextArea.setText("");
			panel_log.add(jTextArea);
		} else {
			StringBuffer sbf = new StringBuffer();
			if (logs!=null&&!logs.isEmpty()) {
				for (String log : logs) {
					sbf.append("\n"+log);
				}
			}
			JTextArea jTextArea = (JTextArea) panel_log.getComponent(0);
			jTextArea.setText(sbf.length()==0?"":sbf.substring(1));
			panel_log.revalidate();
		}
	}

	private void initaccount(List<AccountInfo> accountInfos, String type) {
		if ("0".equals(type)) {
			panel_account = new JPanel(null);
			panel_account.setBounds(0,0,1090, 250);
			panel_account.add(getAccountTable(accountInfos), 0);
			panel_account.add(getOperatPanel(), 1);
		} else {

			panel_account.remove(0);
			panel_account.add(getAccountTable(accountInfos),0);
			// panel_account.updateUI();
			panel_account.revalidate();
		}
	}


	public void initdata(List<OrderInfo> orderInfos, String type) {
		panel_data = new JPanel(null);
		panel_data.setBounds(5,5,1090, 350);
		panel_data.add(this.getTable(orderInfos));
		if (!"0".equals(type)) {
			this.remove(0);
			this.add(panel_data, 0);
			this.revalidate();
		}
	}

	private JPanel getTable(List<OrderInfo> orderInfos) {
		JPanel jpanel_table = new JPanel(new BorderLayout());
		jpanel_table.setBounds(5,0,1080, 340);
		Object[] colNames = {"订单Id","序号","订单号","抢票状态","航班","出发","到达","日期","出票价格","当前最低价格","抢票时间","设定价格","乘机人","官网账号", "操作"};
		Object[][] rowDatas = getRowDatas(orderInfos, colNames.length);
		JTable jTable = new JTable(new DefaultTableModel(rowDatas, colNames){
			@Override
			public boolean isCellEditable(int row, int column) {
				if (2==column||12==column||13==column||14==column) {
					return true;
				}
				return false;
			}
		});
		jTable.setDefaultRenderer(Object.class, PageUtil.getTableRender());
		TableColumnModel model = jTable.getColumnModel();
		model.getColumn(0).setMinWidth(0);
		model.getColumn(0).setMaxWidth(0);
		model.getColumn(0).setPreferredWidth(0);
		model.getColumn(1).setPreferredWidth(30);
		model.getColumn(2).setPreferredWidth(60);
		model.getColumn(3).setPreferredWidth(90);
		model.getColumn(4).setPreferredWidth(50);
		model.getColumn(5).setPreferredWidth(30);
		model.getColumn(6).setPreferredWidth(30);
		model.getColumn(7).setPreferredWidth(120);
		model.getColumn(8).setPreferredWidth(60);
		model.getColumn(9).setPreferredWidth(80);
		model.getColumn(10).setPreferredWidth(120);
		model.getColumn(11).setPreferredWidth(50);
		model.getColumn(13).setPreferredWidth(170);
		model.getColumn(13).setPreferredWidth(100);
		model.getColumn(14).setPreferredWidth(66);
		model.getColumn(14).setCellRenderer(new GrabTBEditor());
		model.getColumn(14).setCellEditor(new GrabTBEditor());
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		jTable.setPreferredScrollableViewportSize(new Dimension(770, 250));
		jpanel_table.add(new JScrollPane(jTable));
		return jpanel_table;
	}

	private Object[][] getRowDatas(List<OrderInfo> orderInfos, int colNums) {
//		{"订单Id","订单号","抢票状态","航班","出发","到达","日期","价格","当前价格","抢票时间","抢票价格","乘机人", "操作"}
		if (orderInfos==null) {
			return new Object[0][colNums];
		}
		Object[][] rowDatas = new Object[orderInfos.size()][colNums];
		for (int i = 0; i < orderInfos.size(); i++) {
			List<PaxInfo> paxInfos = orderInfos.get(i).getPaxInfos();
			String paxnames = "";
			String sellprice = "";
			for (PaxInfo paxInfo : paxInfos) {
				paxnames += ","+paxInfo.getPaxName();
				if ("成人".equals(paxInfo.getPaxType())) {
					sellprice = paxInfo.getSellPrice();
				}
			}
			paxnames=paxnames.substring(1);
			rowDatas[i][0] = orderInfos.get(i).getId();
			rowDatas[i][1] = i+1;
			rowDatas[i][2] = orderInfos.get(i).getOrderNo();
			String grabStatus = orderInfos.get(i).getGrabStatus();
			if ("价格刷取".equals(grabStatus)) {
				grabStatus = SysData.grabPriceStart?"正在刷价":"刷价未开启";
			}
			rowDatas[i][3] = grabStatus;
			rowDatas[i][4] = orderInfos.get(i).getFlightNo();
			rowDatas[i][5] = orderInfos.get(i).getDep();
			rowDatas[i][6] = orderInfos.get(i).getArr();
			rowDatas[i][7] = orderInfos.get(i).getDepTime();
			rowDatas[i][8] = orderInfos.get(i).getOutPrice();
			rowDatas[i][9] = orderInfos.get(i).getAppPrice();
			rowDatas[i][10] = orderInfos.get(i).getGrabTime();
			rowDatas[i][11] = orderInfos.get(i).getGrabPrice();
			rowDatas[i][12] = paxnames;
			rowDatas[i][13] = orderInfos.get(i).getAccount();
		}
		return rowDatas;
	}
	private void initThread() {
		panel_thread = new JPanel(null);
		panel_thread.setBounds(10,355,1080, 30);
		panel_thread.setBorder(BorderFactory.createEtchedBorder());

		JLabel jtp = new JLabel("线程数");
		jtp.setBounds(10, 5, 50, 20);
		panel_thread.add(jtp);
		JTextField jtf = new JTextField(185);
		jtf.setText(SysData.threadNum+"");
		jtf.setBounds(55, 5, 50, 20);
		panel_thread.add(jtf);

		JLabel jtp_3 = new JLabel("延时(秒)");
		jtp_3.setBounds(130, 5, 60, 20);
		panel_thread.add(jtp_3);
		JTextField jtf_3 = new JTextField(SysData.delaySec);
		jtf_3.setText(SysData.delaySec+"");
		jtf_3.setBounds(190, 5, 50, 20);
		panel_thread.add(jtf_3);

		JButton jb_query = new JButton("更改");
		jb_query.setBackground(Color.LIGHT_GRAY);
		jb_query.setBounds(260, 5, 70, 20);
		panel_thread.add(jb_query);
		jb_query.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String threadNum = jtf.getText();
				String delaySec = jtf_3.getText();
				String errMsg = null;
				if (!checkNum(threadNum)) {
					errMsg = "请输入线程数";
				} else if (!checkNum(delaySec)) {
					errMsg = "请输入延时";
				}
				if (errMsg!=null) {
					MsgUtil.errorRemind(errMsg);
					return;
				}
				SysData.threadNum = Integer.parseInt(threadNum);
				SysData.delaySec = Integer.parseInt(delaySec);
				String content = "threadNum="+threadNum+"\n"+"delaySec="+delaySec;
				boolean result = SysData.writeTxt(SysData.pathUrl, content);
				if (result) {
					MsgUtil.confirmRemind("更新成功");
				} else {
					MsgUtil.errorRemind("更新失败");
				}
			}
		});

		JLabel jtp_4 = new JLabel("(线程越多，查询越快 延时越低，查询越快 电脑卡顿请调慢点)");
		jtp_4.setBounds(350, 5, 400, 20);
		jtp_4.setFont(new Font("宋体", Font.BOLD, 12));
		panel_thread.add(jtp_4);

		JButton button_startGrab = new JButton("价格刷取开始");
		button_startGrab.setBackground(Color.LIGHT_GRAY);
		button_startGrab.setBounds(900, 5, 120, 20);
		panel_thread.add(button_startGrab);
		button_startGrab.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (SysData.grabPriceStart) {
					MsgUtil.errorRemind("价格刷取已经开始");
					return;
				}
				SysData.grabPriceStart = true;
				MsgUtil.confirmRemind("开启成功");
			}
		});
	}

	private void initAccount(List<AccountInfo> accountInfos, String type) {
		panel_account = new JPanel(null);
		panel_account.setBounds(5,390,1090, 250);
		panel_account.add(getAccountTable(accountInfos), 0);
		panel_account.add(getOperatPanel(), 1);
		if (!"0".equals(type)) {
			this.remove(2);
			this.add(panel_account, 2);
			this.revalidate();
		}
	}

	public JPanel getAccountTable(List<AccountInfo> accountInfos) {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBounds(0,0,1085, 190);
		Object[] colNames = {"账号Id1","序号","航司","账号","密码","联系人","联系电话","登陆状态","操作"};
		Object[][] rowDatas = this.getAccountRowDatas(accountInfos, colNames.length);
		DefaultTableModel tableModel = new DefaultTableModel(rowDatas, colNames);
		tableModel.fireTableDataChanged();
		JTable jTable = new JTable(tableModel);
		SwingUtilities.updateComponentTreeUI(jTable);
		CheckBoxEditor checkBoxEditor = new CheckBoxEditor();
		for (int i = 0 ; i < jTable.getRowCount(); i++) {
			jTable.getColumnModel().getColumn(1).setCellRenderer(checkBoxEditor);
		}
		jTable.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount() == 1){
					int columnIndex = jTable.columnAtPoint(e.getPoint()); //获取点击的列
					int rowIndex = jTable.rowAtPoint(e.getPoint()); //获取点击的行

					if(columnIndex == 1) {//第0列时，执行代码
						if(jTable.getValueAt(rowIndex,columnIndex) == null){ //如果未初始化，则设置为false
							jTable.setValueAt(false, rowIndex, columnIndex);
						}
						if(((Boolean)jTable.getValueAt(rowIndex,columnIndex)).booleanValue()){ //原来选中
							jTable.setValueAt(false, rowIndex, 1); //点击后，取消选中
						}
						else {//原来未选中
							jTable.setValueAt(true, rowIndex, 1);
						}
					}

				}
			}
		});
		TableColumnModel model = jTable.getColumnModel();
		jTable.setDefaultRenderer(Object.class, PageUtil.getTableRender());
		model.getColumn(8).setCellRenderer(new AccountTBEditor());
		model.getColumn(8).setCellEditor(new AccountTBEditor());
		model.getColumn(0).setMinWidth(0);
		model.getColumn(0).setMaxWidth(0);
		model.getColumn(0).setPreferredWidth(0);
		model.getColumn(1).setPreferredWidth(20);
		model.getColumn(2).setPreferredWidth(100);
		model.getColumn(3).setPreferredWidth(150);
		model.getColumn(4).setPreferredWidth(150);
		model.getColumn(5).setPreferredWidth(100);
		model.getColumn(6).setPreferredWidth(150);
		model.getColumn(7).setPreferredWidth(250);
		model.getColumn(8).setPreferredWidth(160);
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
		jTable.setPreferredScrollableViewportSize(new Dimension(770, 250));
		panel.add(new JScrollPane(jTable));
		return panel;
	}

	private Object[][] getAccountRowDatas(List<AccountInfo> accountInfos, int length) {
//		{"账号Id","序号","航司","账号","密码","联系人","联系电话","登陆状态","操作"}
		if (accountInfos==null||accountInfos.isEmpty()) {
			return new Object[0][0];
		}
		Object[][] rowDatas = new Object[accountInfos.size()][length];
		for (int i = 0; i < accountInfos.size(); i++) {
			rowDatas[i][0] = accountInfos.get(i).getId();
//			rowDatas[i][1] = i+1;
			rowDatas[i][2] = accountInfos.get(i).getAirCompany();
			rowDatas[i][3] = accountInfos.get(i).getAccount();
//			rowDatas[i][4] = accountInfos.get(i).getPassword();
			rowDatas[i][4] = "********";
			rowDatas[i][5] = accountInfos.get(i).getContact();
			rowDatas[i][6] = accountInfos.get(i).getTelPhone();
			String loginState = accountInfos.get(i).getLoginState();
			if (StringUtil.isNotEmpty(loginState)) {
				rowDatas[i][7] = "登录成功";
			} else {
				rowDatas[i][7] = "未登录";
			}
			rowDatas[i][8] = "";
		}
		return rowDatas;
	}

	public JPanel getOperatPanel() {
		JPanel panel = new JPanel(null);
		panel.setBounds(0,191,1085, 30);
		panel.setBorder(BorderFactory.createEtchedBorder());

		JLabel jLabel_delete = new JLabel("删除");
		jLabel_delete.setBounds(10, 5, 30, 20);
		panel.add(jLabel_delete);
		jLabel_delete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// JScrollPane scroll = (JScrollPane) jPanel.getComponentAt(0, 3);
				// panel_account.gett
				JScrollPane scroll = new JScrollPane(/*jPanel.getComponentAt(0, 3)*/ panel_account);
				// JViewport viewport = (JViewport) scroll.getComponent(0);a
				// viewport.getr
				JTable table = (JTable) scroll.getComponent(0);
				//JTable table = new JTable(scroll.);
				List<Integer> selectedRows = new ArrayList();
				for (int i = 0; i < table.getRowCount(); i++) {
					Object valueAt = table.getValueAt(i, 1);
					if (valueAt!=null&&Boolean.valueOf(valueAt.toString())) {
						selectedRows.add(Integer.parseInt(table.getValueAt(i, 0).toString()));
					}
				}
				if (selectedRows.size()==0) {
					MsgUtil.errorRemind("请选择删除项");
					return;
				}
				String result = SqliteHander.deleteObjInfo(selectedRows, SqliteHander.ACCOUNTINFO);
				if ("true".equals(result)) {
					MsgUtil.confirmRemind("删除成功");
					queryAccount();
				} else {
					MsgUtil.errorRemind(result);
				}
			}
		});
		JLabel jLabel_add = new JLabel("添加");
		jLabel_add.setBounds(50, 5, 55, 20);
		panel.add(jLabel_add);
		jLabel_add.addMouseListener(new GrabMouseAdapter(this));

		return panel;
	}

	public void queryAccount() {
		List<AccountInfo> accountInfos = SqliteHander.queryAccountInfo();
		initaccount(accountInfos, "1");
	}

	public static void loadListData(GrabListPanel instance) {
		List<AccountInfo> accountInfos = SqliteHander.queryAccountInfo();
		instance.initaccount(accountInfos, "1");
	}

	public class GrabMouseAdapter extends MouseAdapter{
		private GrabListPanel grabListPanel;
		public GrabMouseAdapter(GrabListPanel grabListPanel) {
			this.grabListPanel = grabListPanel;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			new AccountDialog(JOptionPane.getRootFrame(), grabListPanel, 400, 200, null);
		}
	}
	public static void loadGrabData(GrabListPanel instance, String type) {
		Collection<OrderInfo> values = SysData.grabOrderMap.values();
		List<OrderInfo> orderInfos = Lists.newArrayList();
		for (OrderInfo value : values) {
			orderInfos.add(value.clone());
		}
//		List<OrderInfo> orderInfos = new ArrayList<>(SysData.grabOrderMap.values());
		instance.initdata(orderInfos, "1");
		if ("1".equals(type)) {
			List<AccountInfo> accountInfos = SqliteHander.queryAccountInfo();
			instance.initaccount(accountInfos, type);
		}
	}
	private boolean checkNum(String number) {
		try {
			Integer.parseInt(number);
			return true;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return false;
	}

}
