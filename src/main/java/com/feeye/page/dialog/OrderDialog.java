package com.feeye.page.dialog;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
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
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.eltima.components.ui.DatePicker;
import com.feeye.entity.OrderInfo;
import com.feeye.entity.PaxInfo;
import com.feeye.handler.SqliteHander;
import com.feeye.init.SysData;
import com.feeye.page.panel.Chooser;
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
public class OrderDialog extends JDialog implements ActionListener{

	private static final Logger logger = Logger.getLogger(OrderDialog.class);
	private OrderListPanel parentPanel;

	public OrderDialog(Frame frame, OrderListPanel rootPanel, int width, int height, OrderInfo orderInfo) {

		super(frame,true);
		this.setResizable(true);
		parentPanel = rootPanel;
		this.setTitle("订单录入");
		this.setLayout(null);
		Dimension dimension = this.getToolkit().getScreenSize();
		this.setBounds((dimension.width-width)/2, (dimension.height-height)/2, width, height);

		JPanel jPanel = new JPanel(null);
		jPanel.setBorder(BorderFactory.createEtchedBorder());
		jPanel.setBounds(0, 0, width, height);
		this.add(jPanel, 0);
		JLabel text_orderNo = new JLabel("订单号");
		text_orderNo.setBounds(20, 20, 50, 20);
		jPanel.add(text_orderNo, 0);

		JTextField input_orderNo = new JTextField();
		input_orderNo.setBounds(70, 20, 100, 20);
		jPanel.add(input_orderNo, 1);


		JLabel text_platform = new JLabel("订单来源");
		text_platform.setBounds(200, 20, 60, 20);
		jPanel.add(text_platform, 2);

		JTextField input_platform = new JTextField();
		input_platform.setBounds(260, 20, 100, 20);
		jPanel.add(input_platform, 3);


		JLabel text_dep = new JLabel("出发地");
		text_dep.setBounds(20, 50, 50, 20);
		jPanel.add(text_dep, 4);

		JTextField input_dep = new JTextField();
		input_dep.setBounds(70, 50, 100, 20);
		jPanel.add(input_dep, 5);


		JLabel text_arr = new JLabel("到达地");
		text_arr.setBounds(200, 50, 60, 20);
		jPanel.add(text_arr, 6);

		JTextField input_arr = new JTextField();
		input_arr.setBounds(260, 50, 100, 20);
		jPanel.add(input_arr, 7);

		JLabel text_flightNo = new JLabel("航班号");
		text_flightNo.setBounds(20, 80, 50, 20);
		jPanel.add(text_flightNo, 8);

		JTextField input_flightNo = new JTextField();
		input_flightNo.setBounds(70, 80, 100, 20);
		jPanel.add(input_flightNo, 9);

		JLabel text_depTime = new JLabel("起飞时间");
		text_depTime.setBounds(200, 80, 60, 20);
		jPanel.add(text_depTime, 10);

		Chooser chooser1 = Chooser.getInstance();
		JTextField input_depTime = new JTextField();
		input_depTime.setBounds(260, 80, 80, 20);
		chooser1.register(input_depTime);
		jPanel.add(input_depTime, 11);

//		DatePicker input_depTime = InitUtil.getDatePicker(null);
////		JTextField input_depTime = new JTextField();
//		input_depTime.setBounds(260, 80, 130, 20);
//		jPanel.add(input_depTime, 11);

		JButton submit = new JButton("补入订单");
		submit.setBounds(330, height-70, 120, 25);
		submit.addActionListener(this);
		jPanel.add(submit, 12);

		JButton cancel = new JButton("取消");
		cancel.setBounds(1000, height-70, 60, 20);
		cancel.addActionListener(this);
		jPanel.add(cancel, 13);

		JButton button_addPax = new JButton("添加乘机人");
		button_addPax.setBounds(20, height-80, 130, 20);
		jPanel.add(button_addPax, 14);
		List<Component> components = Lists.newArrayList();
		components.add(this);
		components.add(jPanel);
		button_addPax.addMouseListener(new OrderMouseAdapter(components));

		JLabel text_cardType = new JLabel("证件类型:");
		text_cardType.setBounds(20, 110, 60, 20);
		jPanel.add(text_cardType, 15);

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
		input_cardType.setBounds(80, 110, 80, 20);
		jPanel.add(input_cardType, 16);

		JLabel text_name = new JLabel("姓名");
		text_name.setBounds(170, 110, 40, 20);
		jPanel.add(text_name, 17);

		JTextField input_name = new JTextField();
		input_name.setBounds(200, 110, 80, 20);
		jPanel.add(input_name, 18);

		JLabel text_paxType = new JLabel("乘客类型");
		text_paxType.setBounds(290, 110, 60, 20);
		jPanel.add(text_paxType, 19);

		JComboBox input_paxType = new JComboBox();
		input_paxType.addItem("成人");
		input_paxType.addItem("儿童");
		input_paxType.addItem("婴儿");
		input_paxType.setBounds(350, 110, 60, 20);
		jPanel.add(input_paxType, 20);

		JLabel text_delete = new JLabel("删除乘客");
		text_delete.setBounds(430, 110, 60, 20);
		text_delete.setForeground(Color.red);
		jPanel.add(text_delete, 21);

		List<Component> components2 = Lists.newArrayList();
		components2.add(this);
		components2.add(jPanel);
		text_delete.addMouseListener(new DelPax(components2));

		JLabel text_cardNo = new JLabel("证件号");
		text_cardNo.setBounds(20, 140, 50, 20);
		jPanel.add(text_cardNo, 22);

		JTextField input_cardNo = new JTextField();
		input_cardNo.setBounds(65, 140, 135, 20);
		jPanel.add(input_cardNo, 23);
		input_cardNo.addFocusListener(new FocusClass(jPanel));

		JLabel text_sellPrice = new JLabel("销售价");
		text_sellPrice.setBounds(210, 140, 60, 20);
		jPanel.add(text_sellPrice, 24);

		JTextField input_sellPrice = new JTextField();
		input_sellPrice.setBounds(260, 140, 100, 20);
		jPanel.add(input_sellPrice, 25);

		JLabel text_birth = new JLabel("生日");
		text_birth.setBounds(370, 140, 30, 20);
		jPanel.add(text_birth, 26);

		Chooser chooser = Chooser.getInstance();
		JTextField datePicker = new JTextField();
		datePicker.setBounds(400, 140, 80, 20);
		chooser.register(datePicker);
		jPanel.add(datePicker, 27);

		this.setResizable(false);
		this.setVisible(true);
	}

	public class FocusClass implements FocusListener {
		private JPanel panel;

		public FocusClass(JPanel panel) {
			this.panel = panel;
		}

		@Override
		public void focusGained(FocusEvent e) {

		}

		@Override
		public void focusLost(FocusEvent e) {
			JTextField source = (JTextField) e.getSource();
			String birth = InitUtil.getBirth(source.getText());
			if (birth==null) {
				return;
			}
			Integer index = null;
			for (int i = 0; i < panel.getComponents().length; i++) {
				if (panel.getComponents()[i].equals(source)) {
					index = i+4;
					break;
				}
			}
			if (index!=null) {
				JTextField component = (JTextField) panel.getComponent(index);
				if (StringUtil.isEmpty(component.getText())) {
					component.setText(birth);
				}
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if ("补入订单".equals(source.getText())) {
			JPanel panel = (JPanel) source.getParent();
			Component[] components = panel.getComponents();
			String orderNo = ((JTextField) components[1]).getText();
			String platform = ((JTextField) components[3]).getText();
			String dep = ((JTextField) components[5]).getText();
			String arr = ((JTextField) components[7]).getText();
			String flightNo = ((JTextField) components[9]).getText();
			String depTime = ((JTextField) components[11]).getText();

			String errMsg = null;
			if (StringUtil.isEmpty(orderNo)) {
				errMsg = "订单号不能为空";
			} else if (StringUtil.isEmpty(platform)) {
				errMsg = "订单平台不能为空";
			} else if (StringUtil.isEmpty(dep)||dep.trim().length()!=3) {
				errMsg = "请输入合格的出发地三字码";
			} else if (StringUtil.isEmpty(arr)||arr.trim().length()!=3) {
				errMsg = "请输入合格的到达地三字码";
			} else if (StringUtil.isEmpty(flightNo)) {
				errMsg = "航班号不能为空";
			} else if (StringUtil.isEmpty(depTime)) {
				errMsg = "出发时间不能为空";
			} else if (!InitUtil.checkDate(depTime, "yyyy-MM-dd")) {
				errMsg = "出发时间格式不符合(2019-01-01)标准";
			}
			if (errMsg!=null) {
				MsgUtil.errorRemind(errMsg);
				return;
			}
			List<List<String>> lists = parseData(components);
			if (lists == null) {
				return;
			}
			if (MsgUtil.selectRemind("确认添加")!=0) {
				return;
			}
			OrderInfo orderInfo = new OrderInfo();
			Map<String, String> paraMap = Maps.newHashMap();
			orderInfo.setOrderNo(orderNo);
			orderInfo.setUsername(SysData.feeyeusr);
			orderInfo.setOrderStatus("等待出票");
			orderInfo.setPlatform(platform);
			orderInfo.setFlightNo(flightNo);
			orderInfo.setDepTime(depTime);
			orderInfo.setDep(dep.trim().toUpperCase());
			orderInfo.setArr(arr.trim().toUpperCase());
			orderInfo.setImportDate(SysData.sdf_datetime.format(new Date()));
			List<PaxInfo> paxInfos = Lists.newArrayList();
			orderInfo.setPaxInfos(paxInfos);

			for (int i = 0; i < lists.get(0).size(); i++) {
				PaxInfo paxInfo = new PaxInfo();
				paxInfo.setCardType(lists.get(0).get(i));
				paxInfo.setPaxName(lists.get(1).get(i));
				paxInfo.setPaxType(lists.get(2).get(i));
				paxInfo.setCardNo(lists.get(3).get(i));
				paxInfo.setSellPrice(lists.get(4).get(i));
				paxInfo.setBirth(lists.get(5).get(i));
				paxInfo.setSex("男");
				paxInfos.add(paxInfo);
			}
			String result = SqliteHander.addObjInfo(orderInfo);
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


	public List<List<String>> parseData(Component[] components) {
		ArrayList<Component> coms = new ArrayList<>(Arrays.asList(components));
		List<Component> paxInfoList = coms.subList(15, coms.size());
		Iterator<Component> iterator = paxInfoList.iterator();
		while (iterator.hasNext()) {
			Component next = iterator.next();
			if (next instanceof JLabel) {
				iterator.remove();
			}
		}
		String errMsg = null;
		List<List<String>> lists = Lists.newArrayList();
		List<String> cardtypes = Lists.newArrayList();
		List<String> paxnames = Lists.newArrayList();
		List<String> paxtypes = Lists.newArrayList();
		List<String> cardnos = Lists.newArrayList();
		List<String> sellprices = Lists.newArrayList();
		List<String> births = Lists.newArrayList();
		lists.add(cardtypes);
		lists.add(paxnames);
		lists.add(paxtypes);
		lists.add(cardnos);
		lists.add(sellprices);
		lists.add(births);
		for (int i = 0; i < paxInfoList.size(); i++) {
			if (i%6==0) {
				JComboBox component = (JComboBox) paxInfoList.get(i);
				Object selectedItem = component.getSelectedItem().toString();
				if (selectedItem==null) {
					errMsg = "请选择证件类型";
					break;
				}
				cardtypes.add(selectedItem.toString());
			}
			if (i%6==1) {
				JTextField component = (JTextField) paxInfoList.get(i);
				String text = component.getText();
				if (StringUtil.isEmpty(text)) {
					errMsg = "姓名不能为空";
					break;
				}
				paxnames.add(text);
			}
			if (i%6==2) {
				JComboBox component = (JComboBox) paxInfoList.get(i);
				Object selectedItem = component.getSelectedItem();
				if (selectedItem==null) {
					errMsg = "请选择乘客类型";
					break;
				}
				paxtypes.add(selectedItem.toString());
			}
			if (i%6==3) {
				JTextField component = (JTextField) paxInfoList.get(i);
				String text = component.getText();
				if (StringUtil.isEmpty(text)) {
					errMsg = "证件号不能为空";
					break;
				}
				cardnos.add(text);
			}
			if (i%6==4) {
				JTextField component = (JTextField) paxInfoList.get(i);
				String text = component.getText();
				if (StringUtil.isEmpty(text)||!checkPrice(text)) {
					errMsg = "销售价输入有误";
					break;
				}
				sellprices.add(text);
			}
			if (i%6==5) {
				JTextField component = (JTextField) paxInfoList.get(i);
				String text = component.getText();
				if (text==null||!InitUtil.checkDate(text, "yyyy-MM-dd")) {
					errMsg = "生日格式不正确(2019-01-01)";
					break;
				}
				births.add(text);
			}
		}
		if (errMsg!=null) {
			MsgUtil.errorRemind(errMsg);
			return null;
		}
		return lists;
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
	public class DelPax extends MouseAdapter {
		private List<Component> components;
		public DelPax(List<Component> components) {
			this.components = components;
		}
		@Override
		public void mouseClicked(MouseEvent e) {
			JLabel source = (JLabel) e.getSource();
			OrderDialog dialog = (OrderDialog) components.get(0);

			JPanel panel = (JPanel) components.get(1);
			if (panel.getComponents().length<30) {
				MsgUtil.errorRemind("不能再删除乘客");
				return;
			}
			Component[] components = panel.getComponents();
			Integer index = null;
			for (int i = 0; i < components.length; i++) {
				if (source.equals(components[i])) {
					index = i;
					break;
				}
			}
			dialog.setSize(dialog.getWidth(), dialog.getHeight()-60);
			panel.setSize(panel.getWidth(), panel.getHeight()-60);
			JButton submit = (JButton) panel.getComponent(12);
			submit.setLocation(submit.getX(), submit.getY()-60);
			JButton cancel = (JButton) panel.getComponent(13);
			cancel.setLocation(cancel.getX(), cancel.getY()-60);
			JButton addPax = (JButton) panel.getComponent(14);
			addPax.setLocation(addPax.getX(), addPax.getY()-60);
			System.out.println("qm"+panel.getComponents().length);
			for (int i = index+6; i > index-7; i--) {
				panel.remove(i);
			}
			System.out.println("hou"+panel.getComponents().length);
			for (int i = index-6; i < panel.getComponents().length; i++) {
				Component component = panel.getComponents()[i];
				component.setLocation(component.getX(), component.getY()-60);
			}
		}
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
			input_cardType.setBounds(80, panel.getHeight()-140, 80, 20);
			panel.add(input_cardType, panel.getComponents().length);

			JLabel text_name = new JLabel("姓名");
			text_name.setBounds(170, panel.getHeight()-140, 30, 20);
			panel.add(text_name, panel.getComponents().length);

			JTextField input_name = new JTextField();
			input_name.setBounds(200, panel.getHeight()-140, 80, 20);
			panel.add(input_name, panel.getComponents().length);

			JLabel text_paxType = new JLabel("乘客类型");
			text_paxType.setBounds(290, panel.getHeight()-140, 60, 20);
			panel.add(text_paxType, panel.getComponents().length);
			JComboBox input_paxType = new JComboBox();
			input_paxType.addItem("成人");
			input_paxType.addItem("儿童");
			input_paxType.addItem("婴儿");
			input_paxType.setBounds(350, panel.getHeight()-140, 60, 20);
			panel.add(input_paxType, panel.getComponents().length);

			JLabel text_delete = new JLabel("删除乘客");
			text_delete.setBounds(430, panel.getHeight()-140, 60, 20);
			text_delete.setForeground(Color.red);
			panel.add(text_delete);
			text_delete.addMouseListener(new DelPax(components));

			JLabel text_cardNo = new JLabel("证件号");
			text_cardNo.setBounds(20, panel.getHeight()-110, 50, 20);
			panel.add(text_cardNo, panel.getComponents().length);

			JTextField input_cardNo = new JTextField();
			input_cardNo.setBounds(80, panel.getHeight()-110, 120, 20);
			panel.add(input_cardNo, panel.getComponents().length);
			input_cardNo.addFocusListener(new FocusClass(panel));

			JLabel text_sellPrice = new JLabel("销售价");
			text_sellPrice.setBounds(210, panel.getHeight()-110, 60, 20);
			panel.add(text_sellPrice, panel.getComponents().length);

			JTextField input_sellPrice = new JTextField();
			input_sellPrice.setBounds(260, panel.getHeight()-110, 100, 20);
			panel.add(input_sellPrice, panel.getComponents().length);
			dialog.add(panel);

			JLabel text_birth = new JLabel("生日");
			text_birth.setBounds(370, panel.getHeight()-110, 30, 20);
			panel.add(text_birth, panel.getComponents().length);

			Chooser chooser = Chooser.getInstance();
			JTextField datePicker = new JTextField();
			datePicker.setBounds(400, panel.getHeight()-110, 80, 20);
			chooser.register(datePicker);
			panel.add(datePicker, panel.getComponents().length);
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
