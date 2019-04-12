package com.feeye.page.panel;//package com.feeye.page.panel;
//
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import javax.swing.Box;
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.SwingConstants;
//import javax.swing.WindowConstants;
//import javax.swing.border.EmptyBorder;
//
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.FlowLayout;
//import java.awt.GridLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//import javax.swing.Box;
//import javax.swing.ImageIcon;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JFrame;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.SwingConstants;
//import javax.swing.WindowConstants;
//import javax.swing.border.EmptyBorder;
//
//import com.changda.fingerservice.javaframe.VFlowLayout;
//
//import hong.yelinggu.date.absinterface.SelectHYDateAbstract;
//
//
///**
// * @description: This is a class!
// * @author: chenjian
// * @date: 2019/02/28 12:49
// */
//public class HongYeLingGuDate {
//
//	private final JFrame frTime = new JFrame("请选择日期时间");
//
//	private JPanel jPtimeWeek, jPtimeDay, year_form, ybJPanel, month_form, mbJPanel;
//
//	private JButton btn_year_close, btn_month_close, btn_year_left, btn_year_right, btn_yes, btn_closed;
//
//	private Box box;
//
//	private JComboBox<String> jtf_H = null, jtf_m = null, jtf_s = null;
//
//	private SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//	private List<JButton> btnList = new ArrayList<>();
//
//	private JButton yearStart, monthEnd;
//
//	private String SelectNow_day = "01";
//
//	private int yearPage = 1;// 年份的页码
//
//	private final int yearGAP = 5;// 首页的年差
//
//	private final int PAGE_MAIN = 0;
//
//	private final int PAGE_YEAR = 1;
//
//	private final int PAGE_MONTH = 2;
//
//	private Calendar instance = Calendar.getInstance();
//
//	private int now_year = 0;
//
//	private int now_month = 0;
//
//	private int now_day = 0;
//
//	private int now_hous = 0;
//
//	private int now_min = 0;
//
//	private int now_ss = 0;
//
//	private SelectHYDateAbstract HdateInterface;
//
//	String returnDateFormat = null;
//
//	/**
//	 * 实例化控件
//	 *
//	 * @param returnDateFormat
//	 *            返回的时间格式
//	 */
//	public HongYeLingGuDate(String returnFormat) {
//		// TODO Auto-generated constructor stub
//		returnDateFormat = returnFormat;
//	}
//
//	/**
//	 * 创建时间拾取器
//	 */
//	public void creatDatePicker(SelectHYDateAbstract dateInterface) {
//		// TODO Auto-generated method stub
//
//		//判断如果时间控件是显示可见的就不执行了,防止多次执行
//		if (frTime.isVisible()){
//			return;
//		}
//		HdateInterface = dateInterface;
//		Date dateTime = new java.util.Date();
//		String StringTime = sdFormat.format(dateTime);
//		instance.setTime(dateTime);
//		String[] splDate = StringTime.split(" ");
//		String dateAssemble = splDate[0];
//		String timeAssemble = splDate[1];
//		String[] splitItemDate = dateAssemble.split("-");// 日期
//		String[] splitItemTime = timeAssemble.split(":");// 时间
//		now_year = Integer.parseInt(splitItemDate[0]);
//		now_month = Integer.parseInt(splitItemDate[1]);
//		now_day = Integer.parseInt(splitItemDate[2]);
//
//		now_hous = Integer.parseInt(splitItemTime[0]);
//		now_min = Integer.parseInt(splitItemTime[1]);
//		now_ss = Integer.parseInt(splitItemTime[2]);
//
//		frTime.getContentPane().setLayout(new VFlowLayout());
//
//		// 年,月,日选择入口区
//		JPanel jPtimeTiele = new JPanel(new GridLayout(1, 3));
//		yearStart = new JButton(now_year + "年", new ImageIcon("./src/down.png"));
//		monthEnd = new JButton((now_month < 10 ? "0" + now_month : now_month) + "月", new ImageIcon("./src/down.png"));
//		yearStart.setFocusable(false);
//		yearStart.setBorderPainted(false);
//		yearStart.setBackground(new Color(0, 161, 203));
//		monthEnd.setFocusable(false);
//		monthEnd.setBorderPainted(false);
//		monthEnd.setBackground(new Color(0, 161, 203));
//		jPtimeTiele.add(yearStart);
//		JButton btn_null = new JButton("");
//		btn_null.setBorderPainted(false);
//		btn_null.setBackground(new Color(245, 245, 245));
//		btn_null.setFocusable(false);
//		jPtimeTiele.add(btn_null);
//		jPtimeTiele.add(monthEnd);
//		jPtimeTiele.setBackground(new Color(0, 161, 203));
//
//		// 周期 显示区
//		jPtimeWeek = new JPanel(new GridLayout(1, 7));
//		jPtimeWeek.setBorder(new EmptyBorder(8, 0, 8, 0));
//		jPtimeWeek.setBackground(new Color(245, 245, 245));
//		String[] weekText = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
//
//		for (int i = 0; i < weekText.length; i++) {
//			jPtimeWeek.add(new JLabel(weekText[i], SwingConstants.CENTER));
//		}
//		jPtimeDay = new JPanel(new GridLayout(6, 7));
//		jPtimeDay.setBackground(Color.WHITE);
//
//		// 日期选择入口
//		UpdataDateList(null);
//		JPanel jPtimeTime = new JPanel(new FlowLayout(FlowLayout.LEFT));
//		String[] hous = new String[24];
//		for (int i = 0; i < hous.length; i++) {
//			hous[i] = i < 10 ? "0" + i : String.valueOf(i);
//		}
//		jtf_H = new JComboBox<>(hous);
//		jtf_H.setSelectedIndex(now_hous);
//		jtf_H.setPreferredSize(new Dimension(50, 25));
//		String[] mins = new String[60];
//		String[] ss = new String[60];
//		for (int i = 0; i < mins.length; i++) {
//			mins[i] = i < 10 ? "0" + i : String.valueOf(i);
//			ss[i] = i < 10 ? "0" + i : String.valueOf(i);
//		}
//		jtf_m = new JComboBox<>(mins);
//		jtf_m.setSelectedIndex(now_min);
//		jtf_m.setPreferredSize(new Dimension(50, 25));
//		jtf_s = new JComboBox<>(ss);
//		jtf_s.setSelectedIndex(now_ss);
//		jtf_s.setPreferredSize(new Dimension(50, 25));
//		jPtimeTime.add(new JLabel("时"));
//		jPtimeTime.add(jtf_H);
//		jPtimeTime.add(new JLabel("分"));
//		jPtimeTime.add(jtf_m);
//		jPtimeTime.add(new JLabel("秒"));
//		jPtimeTime.add(jtf_s);
//		box = Box.createHorizontalBox();
//		box.add(jPtimeTime);
//		JPanel panel_r = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		btn_closed = new JButton("关闭");
//		btn_closed.setFocusable(false);
//		btn_closed.setBorderPainted(false);
//		btn_closed.setBackground(new Color(0, 161, 203));
//		btn_yes = new JButton("确认");
//		btn_yes.setBackground(new Color(0, 161, 203));
//		btn_yes.setFocusable(false);
//		btn_yes.setBorderPainted(false);
//		panel_r.add(btn_closed);
//		panel_r.add(btn_yes);
//		box.add(panel_r);
//		box.add(Box.createHorizontalGlue());
//		box.add(panel_r);
//
//		/*
//		 * 关闭时间选择器
//		 */
//		btn_closed.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				frTime.dispose();
//			}
//		});
//
//		/*
//		 * 确定选择的时间
//		 */
//		btn_yes.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				try {
//					// TODO Auto-generated method stub
//					StringBuffer sbfd = new StringBuffer();
//					sbfd.append(yearStart.getText()).append(monthEnd.getText()).append(SelectNow_day)
//							.append(jtf_H.getSelectedItem()).append(jtf_m.getSelectedItem())
//							.append(jtf_s.getSelectedItem());
//					String selectT = sbfd.toString().replace("年", "").replace("月", "");
//					if (HdateInterface != null) {
//						if (returnDateFormat != null) {
//							SimpleDateFormat nowFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//							SimpleDateFormat format = new SimpleDateFormat(returnDateFormat);
//							Date parse = nowFormat.parse(selectT);
//							selectT = format.format(parse);
//						}
//						HdateInterface.clickOnSwingToTime(selectT);
//					}
//				} catch (Exception e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//				frTime.dispose();
//			}
//		});
//
//		frTime.add(jPtimeTiele);
//		frTime.add(jPtimeWeek);
//		frTime.add(jPtimeDay);
//		frTime.add(box);
//
//		/*
//		 * 年份的浮层
//		 */
//		year_form = new JPanel(new GridLayout(5, 4));
//		year_form.setBackground(Color.WHITE);
//		final List<JButton> list_btn = new ArrayList<>();
//		for (int i = now_year - yearGAP; i < now_year + 15; i++) {
//			JButton btn_y = new JButton(i + "年");
//			btn_y.setPreferredSize(new Dimension(45, 50));
//			btn_y.setBackground(Color.WHITE);
//			btn_y.setBorderPainted(false);
//			btn_y.setFocusable(false);
//			year_form.add(btn_y);
//			list_btn.add(btn_y);
//			btn_y.addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					// TODO Auto-generated method stub
//					JButton ye = (JButton) e.getSource();
//					for (JButton jButton : list_btn) {
//						jButton.setBackground(Color.WHITE);
//					}
//					ye.setBackground(new Color(0, 161, 203));
//					selectYearClick(ye.getText().trim());
//				}
//			});
//		}
//
//		/*
//		 * 月份的浮层
//		 */
//		month_form = new JPanel(new GridLayout(3, 4));
//		month_form.setBackground(Color.WHITE);
//		final List<JButton> list_month_btn = new ArrayList<>();
//		for (int i = 1; i <= 12; i++) {
//			JButton btn_y = new JButton((i < 10 ? "0" + i : i) + "月");
//			btn_y.setPreferredSize(new Dimension(50, 80));
//			btn_y.setBackground(Color.WHITE);
//			btn_y.setBorderPainted(false);
//			btn_y.setFocusable(false);
//			month_form.add(btn_y);
//			list_month_btn.add(btn_y);
//			btn_y.addActionListener(new ActionListener() {
//
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					// TODO Auto-generated method stub
//					JButton ye = (JButton) e.getSource();
//					for (JButton jButton : list_month_btn) {
//						jButton.setBackground(Color.WHITE);
//					}
//					ye.setBackground(new Color(0, 161, 203));
//					monthEnd.setText(ye.getText().trim());
//					showhidle(PAGE_MAIN, true);
//				}
//			});
//		}
//
//		year_form.setVisible(false);
//		month_form.setVisible(false);
//		ybJPanel = new JPanel();
//		mbJPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
//		btn_year_close = new JButton("关闭");
//		btn_year_left = new JButton("上一页");
//		btn_year_right = new JButton("下一页");
//		btn_year_left.setPreferredSize(new Dimension(110, 30));
//		btn_year_right.setPreferredSize(new Dimension(110, 30));
//		btn_year_close.setPreferredSize(new Dimension(80, 30));
//
//		btn_month_close = new JButton("关闭");
//		ybJPanel.add(btn_year_left);
//		ybJPanel.add(btn_year_right);
//		JLabel jLabel_null = new JLabel("");
//		jLabel_null.setPreferredSize(new Dimension(93, 22));
//		ybJPanel.add(jLabel_null);
//		ybJPanel.add(btn_year_close);
//		mbJPanel.add(btn_month_close);
//		ybJPanel.setVisible(false);
//		mbJPanel.setVisible(false);
//		frTime.add(year_form);
//		frTime.add(month_form);
//
//		frTime.add(ybJPanel);
//		frTime.add(mbJPanel);
//
//		frTime.pack();
//		frTime.setSize(new Dimension(430, 370));
//		frTime.setLocationRelativeTo(null);
//		frTime.setResizable(false);
//		frTime.setVisible(true);
//		frTime.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);// 只关闭子窗口
//
//		/*
//		 * 选择年份
//		 */
//		yearStart.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				showhidle(PAGE_YEAR, true);
//			}
//		});
//
//		/*
//		 * 年份上一页
//		 */
//		btn_year_left.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				if (yearPage > 1) {
//					year_form.removeAll();
//					int startYest;
//					int nowGap = now_year - yearGAP;
//
//					startYest = nowGap + ((yearPage - 2) * 20);
//
//					final List<JButton> list_btn = new ArrayList<>();
//					for (int i = startYest; i < startYest + 20; i++) {
//						JButton btn_y = new JButton(i + "年");
//						btn_y.setPreferredSize(new Dimension(45, 50));
//						btn_y.setBackground(Color.WHITE);
//						btn_y.setBorderPainted(false);
//						btn_y.setFocusable(false);
//						year_form.add(btn_y);
//						list_btn.add(btn_y);
//						btn_y.addActionListener(new ActionListener() {
//
//							@Override
//							public void actionPerformed(ActionEvent e) {
//								// TODO Auto-generated method stub
//								JButton ye = (JButton) e.getSource();
//								for (JButton jButton : list_btn) {
//									jButton.setBackground(Color.WHITE);
//								}
//								ye.setBackground(new Color(0, 161, 203));
//								selectYearClick(ye.getText().trim());
//							}
//						});
//						year_form.updateUI();
//					}
//					yearPage--;
//				}
//			}
//		});
//
//		/*
//		 * 年份下一页
//		 */
//		btn_year_right.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//
//				year_form.removeAll();
//				int startYest;
//				int nowGap = now_year - yearGAP;
//
//				startYest = nowGap + (yearPage * 20);
//
//				final List<JButton> list_btn = new ArrayList<>();
//				for (int i = startYest; i < startYest + 20; i++) {
//					JButton btn_y = new JButton(i + "年");
//					btn_y.setPreferredSize(new Dimension(45, 50));
//					btn_y.setBackground(Color.WHITE);
//					btn_y.setBorderPainted(false);
//					btn_y.setFocusable(false);
//					year_form.add(btn_y);
//					list_btn.add(btn_y);
//					btn_y.addActionListener(new ActionListener() {
//
//						@Override
//						public void actionPerformed(ActionEvent e) {
//							// TODO Auto-generated method stub
//							JButton ye = (JButton) e.getSource();
//							for (JButton jButton : list_btn) {
//								jButton.setBackground(Color.WHITE);
//							}
//							ye.setBackground(new Color(0, 161, 203));
//							selectYearClick(ye.getText().trim());
//						}
//					});
//					year_form.updateUI();
//				}
//				yearPage++;
//			}
//		});
//
//		/*
//		 * 选择月份
//		 */
//		monthEnd.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				showhidle(PAGE_MONTH, true);
//			}
//		});
//
//		/**
//		 * 年份单个选择完毕
//		 */
//		btn_year_close.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				showhidle(PAGE_MAIN, true);
//			}
//		});
//
//		/**
//		 * 月份单个选择完毕
//		 */
//		btn_month_close.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				showhidle(PAGE_MAIN, true);
//			}
//		});
//	}
//
//	/*
//	 *更新时间的天数
//	 * @param data
//	 *            时间格式: 年-月
//	 */
//	private void UpdataDateList(String data) {
//		try {
//			if (data != null) {
//				Date parse = sdFormat.parse(data);
//				instance.setTime(parse);
//				btnList.clear();
//				jPtimeDay.removeAll();
//			}
//			instance.set(Calendar.DAY_OF_MONTH, 1);
//			int week_mo = instance.get(Calendar.DAY_OF_WEEK);
//			int maximum = instance.getActualMaximum(Calendar.DAY_OF_MONTH);
//			for (int i = 1; i <= 42; i++) {
//				String btnNum = "";
//				int monthDay = 0;
//				if (i >= week_mo && i < maximum + week_mo) {
//					monthDay = i - week_mo + 1;
//					btnNum = String.valueOf(monthDay < 10 ? "0" + monthDay : monthDay);
//				}
//				JButton btnDay = new JButton(btnNum);
//				btnDay.setPreferredSize(new Dimension(30, 35));
//				btnDay.setBorderPainted(false);
//				if (now_day == monthDay) {
//					btnDay.setBackground(new Color(0, 161, 203));
//					btnDay.setForeground(Color.WHITE);
//					SelectNow_day = btnNum;
//				} else {
//					btnDay.setBackground(Color.WHITE);
//				}
//				btnDay.setFocusable(false);
//				btnDay.addActionListener(new ActionListener() {
//
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						// TODO Auto-generated method stub
//						JButton source = (JButton) e.getSource();
//						SelectNow_day = source.getText().toString();
//						if (!source.getText().isEmpty()) {
//							for (JButton itembtn : btnList) {
//								itembtn.setBackground(Color.WHITE);
//								itembtn.setForeground(Color.BLACK);
//							}
//							source.setBackground(new Color(0, 161, 203));
//							source.setForeground(Color.WHITE);
//						}
//
//					}
//				});
//				btnList.add(btnDay);
//				jPtimeDay.add(btnDay);
//			}
//			if (data != null) {
//				jPtimeDay.updateUI();
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	/*
//	 * 选择的年份的事件
//	 */
//	private void selectYearClick(String year) {
//		yearStart.setText(year);
//		showhidle(PAGE_MAIN, true);
//	}
//
//	/**
//	 * 显示隐藏切换
//	 *
//	 * @param page
//	 *            页面
//	 * @param showpage
//	 *            是否显示
//	 */
//	private void showhidle(int page, boolean showpage) {
//
//		switch (page) {
//			case PAGE_MAIN:// 主页
//
//				jPtimeWeek.setVisible(showpage);
//				jPtimeDay.setVisible(showpage);
//				box.setVisible(showpage);
//
//				year_form.setVisible(!showpage);
//				ybJPanel.setVisible(!showpage);
//				String yeatText = yearStart.getText().trim().replace("年", "");
//				String monthText = monthEnd.getText().trim().replace("月", "");
//				UpdataDateList(yeatText + "-" + monthText + "-01 00:00:00");
//				break;
//			case PAGE_YEAR:
//				year_form.setVisible(showpage);
//				ybJPanel.setVisible(showpage);
//
//				jPtimeWeek.setVisible(!showpage);
//				jPtimeDay.setVisible(!showpage);
//				box.setVisible(!showpage);
//				month_form.setVisible(!showpage);
//				mbJPanel.setVisible(!showpage);
//
//				break;
//			case PAGE_MONTH:
//				jPtimeWeek.setVisible(!showpage);
//				jPtimeDay.setVisible(!showpage);
//				box.setVisible(!showpage);
//				year_form.setVisible(!showpage);
//				ybJPanel.setVisible(!showpage);
//
//				month_form.setVisible(showpage);
//				mbJPanel.setVisible(showpage);
//			default:
//				break;
//		}
//	}
//}
//
