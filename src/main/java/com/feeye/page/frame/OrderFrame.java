package com.feeye.page.frame;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

import com.feeye.entity.OrderInfo;
import com.feeye.handler.ReqHandler;
import com.feeye.handler.SqliteHander;
import com.feeye.init.SysData;
import com.feeye.page.panel.GrabListPanel;
import com.feeye.page.panel.OrderListPanel;
import com.feeye.page.panel.SetttingPanel;
import com.feeye.util.HttpClientUtil;
import com.feeye.util.MD5Util;
import com.feeye.util.MsgUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Maps;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 * @description: This is a class!
 * @author: domcj
 * @date: 2019/01/15 14:49
 */
public class OrderFrame {

	private static final Logger logger = Logger.getLogger(OrderFrame.class);

	private static JButton bt_order;
	private static JLabel lb_order;
	public static JFrame jf_order;
	private static JTabbedPane jtp_grab;
	public static ScheduledExecutorService freshService = Executors.newScheduledThreadPool(4);

	public OrderFrame() {
		jf_order = new JFrame("飞耶订单-官网订票");
		jf_order.setBounds(200, 100, 1100, 700);
		jf_order.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		jf_order.setResizable(false);
		jf_order.setFont(new Font("宋体", Font.BOLD, 10));
		lb_order = new JLabel();
		ImageIcon icon = new ImageIcon(this.getClass().getResource("/static/picture/login_logo1.jpg"));
		jf_order.setIconImage(icon.getImage());
		jtp_grab = new JTabbedPane();
		OrderListPanel orderListPanel = new OrderListPanel();
		GrabListPanel grabListPanel = new GrabListPanel();
		jtp_grab.addTab("订单列表", orderListPanel);
		jtp_grab.addTab("抢票列表", grabListPanel);
		jtp_grab.addTab("设置", new SetttingPanel());
		OrderListPanel.loadListData(orderListPanel);
		this.dynfreshGrab(grabListPanel);  //定时刷新抢票列表
		this.dyncheckLogin();
		this.dynfreshLog(grabListPanel);
		this.synchronGrabData();
		jtp_grab.setBounds( 0,0,1100, 700);
		jtp_grab.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane source = (JTabbedPane) e.getSource();
				String title = source.getTitleAt(source.getSelectedIndex());
				if ("订单列表".equals(title)) {
					OrderListPanel selectedComponent = (OrderListPanel) source.getSelectedComponent();
					OrderListPanel.loadListData(selectedComponent);
				} else if ("抢票列表".equals(title)){
					GrabListPanel selectedComponent = (GrabListPanel) source.getSelectedComponent();
					GrabListPanel.loadGrabData(selectedComponent, "1");
				}
			}
		});
		lb_order.add(jtp_grab);

		jf_order.add(lb_order);
		jf_order.setVisible(true);
		jf_order.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (MsgUtil.selectRemind("确认关闭")!=0) {
					return;
				}
				new Thread(new Runnable() {
					@Override
					public void run() {
						ReqHandler.logOffAccount();
					}
				});
				super.windowClosing(e);
				try {
					Thread.sleep(2*1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				System.exit(0);
			}
		});
	}

	private void synchronGrabData() {
		List<OrderInfo> orderInfos = SqliteHander.queryOrderInfo("", "", "", "", "", "抢票中", "");
		if (orderInfos==null) {
			orderInfos = SqliteHander.queryOrderInfo("", "", "", "", "", "抢票中", "");
		}
		if (orderInfos!=null&&!orderInfos.isEmpty()) {
			for (OrderInfo orderInfo : orderInfos) {
				if (StringUtil.isNotEmpty(orderInfo.getGrabPrice())&&StringUtil.isNotEmpty(orderInfo.getAccount())) {
					orderInfo.setGrabOver(false);
					orderInfo.setGrabTime("");
					orderInfo.setAppPrice("");
					orderInfo.setGrabStatus("价格刷取");
					orderInfo.setOutPrice("");
					SysData.grabOrderMap.put(orderInfo.getId()+"", orderInfo);
				}
			}
		}
	}

	private void dyncheckLogin() {
		freshService.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				Map<String, Object> paraMap = Maps.newHashMap();
				paraMap.put("feeyeusr", SysData.feeyeusr);
				paraMap.put("proxyUser", MD5Util.getMD5("getProxyUserpolicytest","UTF-8"));
				paraMap.put("proxyPass", MD5Util.getMD5("getProxyUserfeeye0100","UTF-8"));
				paraMap.put("versionNo", SysData.versionNo);
				paraMap.put("proxyType", 1);
//				String respText = HttpClientUtil.getRespText(paraMap, "getProxyUser");
//				if (respText!=null&&respText.contains("请先登录")) {
//					MsgUtil.errorRemind("登录失效");
//					SysData.orderFrame.jf_order.dispose();
//					System.exit(0);
//				}
			}
		}, 5, 1, TimeUnit.SECONDS);
	}

	private void dynfreshLog(GrabListPanel grabListPanel) {
		freshService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				List<String> logs = (List<String>) SysData.logs.clone();
				grabListPanel.initlog(logs, "1");
			}
		}, 10, 1, TimeUnit.SECONDS);
	}

	public void dynfreshGrab(GrabListPanel instance) {
		freshService.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				while (!SysData.cancleGrabOver) {
					try {
						Thread.sleep(1*1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				GrabListPanel.loadGrabData(instance, "0");
			}
		}, 5, 2, TimeUnit.SECONDS);
	}
	public static void main(String[] args1) {
		try {
			InputStream stream = OrderFrame.class.getResourceAsStream("/voice/system.wav");
			AudioPlayer.player.start(new AudioStream(stream));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
