package com.feeye.page.panel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import com.alibaba.fastjson.JSONArray;
import com.eltima.components.ui.DatePicker;
import com.feeye.entity.OrderInfo;
import com.feeye.entity.PaxInfo;
import com.feeye.handler.ReqHandler;
import com.feeye.handler.SqliteHander;
import com.feeye.init.SysData;
import com.feeye.page.dialog.ImportOrderDialog;
import com.feeye.page.dialog.OrderDialog;
import com.feeye.page.grabFrame.CheckBoxEditor;
import com.feeye.page.grabFrame.CheckHeaderCellRenderer;
import com.feeye.page.grabFrame.OrderTBEditor;
import com.feeye.page.grabFrame.PaxInfoEditor;
import com.feeye.page.grabFrame.TableModelProxy;
import com.feeye.util.InitUtil;
import com.feeye.util.MsgUtil;
import com.feeye.util.PageUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;
import com.sun.org.apache.xml.internal.security.Init;

/**
 * @description: 订票列表页面!
 * @author: domcj
 * @date: 2019/01/16 14:03
 */
public class OrderListPanel extends JPanel {
	private static JPanel jp_data;
	private static JPanel jp_query;
	private static JPanel jp_thread;
	private static JPanel jp_account;

	public OrderListPanel() {
		this.setLayout(null);
		initquery();
		this.add(jp_query, 0);
		initdata(null, "0");
		this.add(jp_data,1);
	}
	public void initdata(List<OrderInfo> orderInfos, String type) {
		jp_data = new JPanel(null);
		jp_data.setBounds(0,40,1090, 700);

		jp_data.add(this.getTable(orderInfos), 0);
		jp_data.add(this.getPageInfo(orderInfos), 1);
		if (!"0".equals(type)) {
			this.remove(1);
			this.add(jp_data);
			this.revalidate();
		}
		if (orderInfos!=null) {
			synchronized (SysData.orderMap) {
				SysData.orderMap.clear();
				for (OrderInfo orderInfo : orderInfos) {
					SysData.orderMap.put(orderInfo.getId()+"", orderInfo);
				}
			}
		}
		SysData.updateTime = new Date().getTime();
	}

	private JPanel getPageInfo(List<OrderInfo> orderInfos) {
		int pageNum = 1;
		if (orderInfos!=null&&!orderInfos.isEmpty()) {
			String totalCount = orderInfos.get(0).getTotalCount();
			if (StringUtil.isNotEmpty(totalCount)) {
				pageNum = (int) Math.ceil(Integer.parseInt(totalCount)/50)+1;
			}
		}
		final int num = pageNum;
		JPanel jpanel_page = new JPanel(null);
		jpanel_page.setBounds(5,568,1080, 30);
		jpanel_page.setBorder(BorderFactory.createEtchedBorder());

		JTextField jTextField_pageIndex = new JTextField();
		jTextField_pageIndex.setText(" "+SysData.pageIndex);
		jTextField_pageIndex.setEnabled(false);
		jTextField_pageIndex.setBounds(500, 5, 20, 20);
		jpanel_page.add(jTextField_pageIndex, 0);

		JLabel jLabel_previous = new JLabel("上一页");
		jLabel_previous.setBounds(445, 5, 60, 20);
		jpanel_page.add(jLabel_previous, 1);
		jLabel_previous.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (1==SysData.pageIndex) {
					MsgUtil.errorRemind("当前已是首页");
					return;
				}
				SysData.pageIndex=SysData.pageIndex-1;
				jTextField_pageIndex.setText(SysData.pageIndex+"");
				queryOrder();
			}
		});

		JLabel jLabel_delete = new JLabel("删除");
		jLabel_delete.setBounds(10, 5, 30, 20);
		jpanel_page.add(jLabel_delete, 2);
		jLabel_delete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JPanel jPanel = (JPanel) jp_data.getComponent(0);
				JScrollPane scroll = (JScrollPane) jPanel.getComponentAt(0, 3);
				JViewport viewport = (JViewport) scroll.getComponent(0);
				JTable table = (JTable) viewport.getComponent(0);
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

				List<OrderInfo> grabs = Lists.newArrayList();
				for (Integer selectedRow : selectedRows) {
					OrderInfo orderInfo = SysData.grabOrderMap.get(selectedRow + "");
					if (orderInfo!=null) {
						grabs.add(orderInfo);
					}
				}
				StringBuffer sbf = null;
				if (!grabs.isEmpty()) {
					sbf = new StringBuffer("存在抢票订单:");
					for (OrderInfo grab : grabs) {
						sbf.append(grab.getOrderNo()+", ");
					}
					sbf.append("确认删除");
				}
				if (MsgUtil.selectRemind(sbf==null?"确认删除":sbf.toString())!=0) {
					return;
				}
				if (!grabs.isEmpty()) {
					for (OrderInfo grab : grabs) {
						SysData.grabOrderMap.remove(grab.getId()+"");
					}
				}
				String result = SqliteHander.deleteObjInfo(selectedRows, SqliteHander.ORDERINFO);
				if ("true".equals(result)) {
					MsgUtil.confirmRemind("删除成功");
					queryOrder();
				} else {
					MsgUtil.errorRemind("错误提示");
				}
			}
		});

		JLabel jLabel_add = new JLabel("添加");
		jLabel_add.setBounds(50, 5, 30, 20);
		jpanel_page.add(jLabel_add, 3);
		jLabel_add.addMouseListener(new OrderMouseAdapter(this, "add"));


		JLabel label_import = new JLabel("订单补录");
		label_import.setBounds(90, 5, 70, 20);
		jpanel_page.add(label_import,4);
		label_import.addMouseListener(new OrderMouseAdapter(this, "import"));

		JLabel jLabel_first = new JLabel("首页");
		jLabel_first.setBounds(400, 5, 40, 20);
		jpanel_page.add(jLabel_first, 5);
		jLabel_first.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (1==SysData.pageIndex) {
					MsgUtil.errorRemind("当前已是首页");
					return;
				}
				SysData.pageIndex = 1;
				jTextField_pageIndex.setText("1");
				queryOrder();
			}
		});

		JLabel jLabel_next = new JLabel("下一页");
		jLabel_next.setBounds(535, 5, 60, 20);
		jpanel_page.add(jLabel_next, 6);
		jLabel_next.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (num<=SysData.pageIndex) {
					MsgUtil.errorRemind("当前已是尾页");
					return;
				}
				SysData.pageIndex=SysData.pageIndex+1;
				jTextField_pageIndex.setText(SysData.pageIndex+"");
				queryOrder();
			}
		});

		JLabel jLabel_pageNums = new JLabel("共"+pageNum+"页");
		jLabel_pageNums.setBounds(600, 5, 60, 20);
		jpanel_page.add(jLabel_pageNums, 7);
		return jpanel_page;
	}

	private JPanel getTable(List<OrderInfo> orderInfos) {
		JPanel jpanel_table = new JPanel(new BorderLayout());
		jpanel_table.setBounds(5,0,1080, 566);
		String[] colNames = {"订单Id","全选","订单号","状态","航班","出发","到达","日期","价格","创建日期","乘机信息(乘客||证件号||票号)", "操作"};
		Object[][] rowDatas = getRowDatas(orderInfos, colNames.length);
		JTable jTable = new JTable();
		TableModelProxy tableModel = new TableModelProxy(colNames, rowDatas);
//		JTable jTable = new JTable(new DefaultTableModel(rowDatas, colNames){
//			@Override
//			public boolean isCellEditable(int row, int column) {
//				if (11==column||2==column) {
//					return true;
//				}
//				return false;
//			}
//		});
		jTable.setModel(tableModel);
		jTable.getTableHeader().setDefaultRenderer(new CheckHeaderCellRenderer(jTable));
		CheckBoxEditor checkBoxEditor = new CheckBoxEditor();
		jTable.getColumnModel().getColumn(1).setCellRenderer(checkBoxEditor);
		jTable.getColumnModel().getColumn(1).setCellRenderer(checkBoxEditor);
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
		jTable.setDefaultRenderer(Object.class, PageUtil.getTableRender());
		TableColumnModel model = jTable.getColumnModel();
		model.getColumn(0).setMaxWidth(0);
		model.getColumn(0).setMinWidth(0);
		model.getColumn(0).setPreferredWidth(0);
		model.getColumn(1).setPreferredWidth(50);
		model.getColumn(2).setPreferredWidth(110);
		model.getColumn(3).setPreferredWidth(60);
		model.getColumn(4).setPreferredWidth(60);
		model.getColumn(5).setPreferredWidth(50);
		model.getColumn(6).setPreferredWidth(50);
		model.getColumn(7).setPreferredWidth(80);
		model.getColumn(8).setPreferredWidth(60);
		model.getColumn(9).setPreferredWidth(130);
		model.getColumn(10).setCellRenderer(new PaxInfoEditor());
		model.getColumn(10).setPreferredWidth(290);
		model.getColumn(11).setPreferredWidth(137);
		model.getColumn(11).setCellRenderer(new OrderTBEditor());
		model.getColumn(11).setCellEditor(new OrderTBEditor());
		jTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		jpanel_table.add(new JScrollPane(jTable));
		return jpanel_table;
	}

	private Object[][] getRowDatas(List<OrderInfo> orderInfos, int colNums) {
//		{"订单Id","序号","订单号","状态","航班","出发","到达","日期","价格","乘机信息","创建日期", "操作"}
		if (orderInfos==null) {
			return new Object[0][colNums];
		}
		Object[][] rowDatas = new Object[orderInfos.size()][colNums];
		for (int i = 0; i < orderInfos.size(); i++) {
			List<PaxInfo> paxInfos = orderInfos.get(i).getPaxInfos();
			String nameAndTicketno = "";
			String sellprice = "";
			for (PaxInfo paxInfo : paxInfos) {
				nameAndTicketno += "&"+paxInfo.getPaxName();
				nameAndTicketno+=" || "+paxInfo.getCardNo();
				nameAndTicketno+=" || "+paxInfo.getTicketNo();
				if ("成人".equals(paxInfo.getPaxType())) {
					sellprice = paxInfo.getSellPrice();
				}
			}
			rowDatas[i][0] = orderInfos.get(i).getId();
//			rowDatas[i][1] = "";
			rowDatas[i][2] = orderInfos.get(i).getOrderNo();
			rowDatas[i][3] = orderInfos.get(i).getOrderStatus();
			rowDatas[i][4] = orderInfos.get(i).getFlightNo();
			rowDatas[i][5] = orderInfos.get(i).getDep();
			rowDatas[i][6] = orderInfos.get(i).getArr();
			try {
				rowDatas[i][7] = orderInfos.get(i).getDepTime().substring(0, 10);
			} catch (Exception e) {
				e.printStackTrace();
			}
			rowDatas[i][8] = sellprice;
			rowDatas[i][9] = orderInfos.get(i).getImportDate();
			rowDatas[i][10] = nameAndTicketno.substring(1);
			rowDatas[i][11] = "";
		}
		return rowDatas;
	}

	private void initquery() {
		jp_query = new JPanel(null);
		jp_query.setBounds(5,5,1080, 30);
		jp_query.setBorder(BorderFactory.createEtchedBorder());


		JLabel label_aircompany = new JLabel("航司");
		label_aircompany.setBounds(10, 5, 30, 20);
		jp_query.add(label_aircompany, 0);
		JComboBox box_query = new JComboBox();
		box_query.addItem("KN");
		box_query.addItem("MF");
		box_query.addItem("MU");
		box_query.addItem("SC");
		box_query.addItem("8L");
		box_query.setBounds(40, 5, 50, 20);
		jp_query.add(box_query, 1);
		box_query.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				SysData.airCompany=box_query.getSelectedItem().toString();
			}
		});
		JLabel start_query = new JLabel("开始时间:");
		start_query.setBounds(110, 5, 55, 20);
		jp_query.add(start_query,2);

		Chooser chooser = Chooser.getInstance("yyyy-MM-dd");
		JTextField startDate = new JTextField();
		startDate.setBounds(165, 5, 80, 20);
		chooser.register(startDate);
		jp_query.add(startDate,3);

		JLabel jtp_2 = new JLabel("结束时间:");
		jtp_2.setBounds(265, 5, 55, 20);
		jp_query.add(jtp_2, 4);

		Chooser chooser2 = Chooser.getInstance();
		JTextField endDate = new JTextField();
		endDate.setBounds(320, 5, 80, 20);
		chooser2.register(endDate);
		jp_query.add(endDate, 5);

		JLabel jtp_3 = new JLabel("政策代码:");
		jtp_3.setBounds(420, 5, 55, 20);
		jp_query.add(jtp_3, 6);
		JTextField jtf_3 = new JTextField();
		jtf_3.setBounds(480, 5, 120, 20);
		jp_query.add(jtf_3, 7);

		JLabel jtp_4 = new JLabel("订单号:");
		jtp_4.setBounds(620, 5, 55, 20);
		jp_query.add(jtp_4, 8);
		JTextField jtf_4 = new JTextField();
		jtf_4.setBounds(670, 5, 120, 20);
		jp_query.add(jtf_4, 9);

		JLabel status = new JLabel("订单状态:");
		status.setBounds(800, 5, 55, 20);
		jp_query.add(status, 10);
		JComboBox status_query = new JComboBox();
		status_query.addItem("全部");
		status_query.addItem("等待出票");
		status_query.addItem("抢票中");
//		status_query.addItem("创单成功");
		status_query.addItem("创单失败");
		status_query.addItem("官网待支付");
		status_query.addItem("回填失败");
		status_query.addItem("出票完成");
		status_query.setBounds(855, 5, 80, 20);
		jp_query.add(status_query, 11);

		JButton jb_query = new JButton("查询");
		jb_query.setBackground(Color.LIGHT_GRAY);
		jb_query.setBounds(1000, 5, 70, 20);
		jp_query.add(jb_query, 12);
		jb_query.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				queryOrder();
			}
		});
	}

	public static void loadListData(OrderListPanel instance) {
		List<OrderInfo> orderInfos = SqliteHander.queryOrderInfo("1", "", "", "", "", "", "KN");
		SysData.pageIndex=1;
		instance.initdata(orderInfos, "1");
	}

	public void queryOrder() {
		JComboBox jtf = (JComboBox) jp_query.getComponent(1);
		String airCompany = jtf.getSelectedItem().toString();

		JTextField jtf2 = (JTextField) jp_query.getComponent(3);
		String startTime = "";
		if (StringUtil.isNotEmpty(jtf2.getText())) {
			if (!InitUtil.checkDate(jtf2.getText(), "yyyy-MM-dd")) {
				MsgUtil.errorRemind("开始日期格式不对");
				return;
			}
			startTime = jtf2.getText();
		}

		JTextField jtf3 = (JTextField) jp_query.getComponent(5);
		String endTime = "";
		if (StringUtil.isNotEmpty(jtf3.getText())) {
			if (!InitUtil.checkDate(jtf3.getText(), "yyyy-MM-dd")) {
				MsgUtil.errorRemind("结束日期格式不对");
				return;
			}
			endTime = jtf3.getText();
		}

		JTextField jtf4 = (JTextField) jp_query.getComponent(7);
		String policyCode = jtf4.getText();

		JTextField jtf5 = (JTextField) jp_query.getComponent(9);
		String orderNo = jtf5.getText();

		JComboBox jtf6 = (JComboBox) jp_query.getComponent(11);
		String orderStatus = jtf6.getSelectedItem().toString();
		if ("全部".equals(orderStatus)) {
			orderStatus = "";
		}
		List<OrderInfo> orderInfos = SqliteHander.queryOrderInfo(SysData.pageIndex+"", startTime, endTime, policyCode, orderNo, orderStatus, airCompany);
		initdata(orderInfos, "1");
	}
	public class OrderMouseAdapter extends MouseAdapter{
		private OrderListPanel orderListPanel;
		private String type;
		public OrderMouseAdapter(OrderListPanel orderListPanel, String type) {
			this.orderListPanel = orderListPanel;
			this.type = type;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			if ("add".equals(type)) {
				new OrderDialog(JOptionPane.getRootFrame(), orderListPanel, 500, 250, null);
			} else if ("import".equals(type)) {
				new ImportOrderDialog(JOptionPane.getRootFrame(), orderListPanel, 440, 220, null);
			}
		}
	}
}
