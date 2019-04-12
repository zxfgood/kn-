package com.feeye.page.dialog;

import com.feeye.entity.OrderInfo;
import com.feeye.entity.PaxInfo;
import com.feeye.handler.ReqHandler;
import com.feeye.handler.SqliteHander;
import com.feeye.init.SysData;
import com.feeye.page.panel.Chooser;
import com.feeye.page.panel.OrderListPanel;
import com.feeye.util.InitUtil;
import com.feeye.util.MsgUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class ModifyOrderDialog extends JDialog implements ActionListener {
    private static final Logger logger = Logger.getLogger(ModifyOrderDialog.class);
    private OrderListPanel parentPanel;
    private OrderInfo orderInfo;
    public static ExecutorService backTicketService = Executors.newFixedThreadPool(5);

    public ModifyOrderDialog(Frame frame, OrderListPanel rootPanel, int width, int height, OrderInfo orderInfo) {
        super(frame, true);
        setResizable(true);
        this.parentPanel = rootPanel;
        this.orderInfo = orderInfo;
        setTitle("订单更改");
        setLayout(null);
        Dimension dimension = getToolkit().getScreenSize();
        setBounds((dimension.width - width) / 2, (dimension.height - height) / 2, width, height);

        JPanel jPanel = new JPanel(null);
        jPanel.setBorder(BorderFactory.createEtchedBorder());
        jPanel.setBounds(0, 0, width, height);
        add(jPanel, 0);
        JLabel text_orderNo = new JLabel("订单号:");
        text_orderNo.setBounds(20, 20, 50, 20);
        jPanel.add(text_orderNo, 0);

        JTextField input_orderNo = new JTextField(orderInfo.getOrderNo());
        input_orderNo.setBounds(70, 20, 150, 20);
        jPanel.add(input_orderNo, 1);


        JLabel text_platform = new JLabel("订单来源:");
        text_platform.setBounds(300, 20, 60, 20);
        jPanel.add(text_platform, 2);

        JTextField input_platform = new JTextField(orderInfo.getPlatform());
        input_platform.setBounds(360, 20, 100, 20);
        jPanel.add(input_platform, 3);


        JLabel text_dep = new JLabel("出发地:");
        text_dep.setBounds(20, 50, 50, 20);
        jPanel.add(text_dep, 4);

        JTextField input_dep = new JTextField(orderInfo.getDep());
        input_dep.setBounds(70, 50, 60, 20);
        jPanel.add(input_dep, 5);


        JLabel text_arr = new JLabel("到达地:");
        text_arr.setBounds(180, 50, 60, 20);
        jPanel.add(text_arr, 6);

        JTextField input_arr = new JTextField(orderInfo.getArr());
        input_arr.setBounds(230, 50, 60, 20);
        jPanel.add(input_arr, 7);

        JLabel text_staus = new JLabel("订单状态:");
        text_staus.setBounds(330, 50, 60, 20);
        jPanel.add(text_staus, 8);

        JComboBox input_staus = new JComboBox();
        input_staus.addItem("等待出票");
        input_staus.addItem("抢票中");
        input_staus.addItem("创单失败");
        input_staus.addItem("官网待支付");
        input_staus.addItem("回填失败");
        input_staus.addItem("出票完成");
        input_staus.setBounds(390, 50, 80, 20);
        if (StringUtil.isNotEmpty(orderInfo.getOrderStatus())) {
            input_staus.setSelectedItem(orderInfo.getOrderStatus());
        }
        jPanel.add(input_staus, 9);

        JLabel text_flightNo = new JLabel("航班号:");
        text_flightNo.setBounds(20, 80, 50, 20);
        jPanel.add(text_flightNo, 10);

        JTextField input_flightNo = new JTextField(orderInfo.getFlightNo());
        input_flightNo.setBounds(70, 80, 100, 20);
        jPanel.add(input_flightNo, 11);

        JLabel text_depTime = new JLabel("起飞时间:");
        text_depTime.setBounds(300, 80, 60, 20);
        jPanel.add(text_depTime, 12);

        Chooser chooser1 = Chooser.getInstance();
        JTextField input_depTime = new JTextField();
        input_depTime.setBounds(360, 80, 80, 20);
        try {
            input_depTime.setText(orderInfo.getDepTime().substring(0, 10));
        } catch (Exception e) {
            e.printStackTrace();
        }
        chooser1.register(input_depTime);
        jPanel.add(input_depTime, 13);


        JButton submit = new JButton("保存订单");
        submit.setBounds(380, height - 70, 120, 25);
        submit.addActionListener(this);
        jPanel.add(submit, 14);
        for (PaxInfo paxInfo : orderInfo.getPaxInfos()) {
            setSize(getWidth(), getHeight() + 60);
            jPanel.setSize(jPanel.getWidth(), jPanel.getHeight() + 60);
            submit.setLocation(submit.getX(), submit.getY() + 60);

            JLabel text_cardType = new JLabel("证件类型:");
            text_cardType.setBounds(20, jPanel.getHeight() - 140, 60, 20);
            jPanel.add(text_cardType);

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
            if (StringUtil.isNotEmpty(paxInfo.getCardType())) {
                input_cardType.setSelectedItem(paxInfo.getCardType());
            }
            input_cardType.setBounds(80, jPanel.getHeight() - 140, 80, 20);
            jPanel.add(input_cardType);

            JLabel text_name = new JLabel("姓名:");
            text_name.setBounds(170, jPanel.getHeight() - 140, 40, 20);
            jPanel.add(text_name);

            JTextField input_name = new JTextField(paxInfo.getPaxName());
            input_name.setBounds(200, jPanel.getHeight() - 140, 80, 20);
            jPanel.add(input_name);

            JLabel text_paxType = new JLabel("乘客类型:");
            text_paxType.setBounds(290, jPanel.getHeight() - 140, 60, 20);
            jPanel.add(text_paxType);

            JComboBox input_paxType = new JComboBox();
            input_paxType.addItem("成人");
            input_paxType.addItem("儿童");
            input_paxType.addItem("婴儿");
            input_paxType.setBounds(350, jPanel.getHeight() - 140, 60, 20);
            if (StringUtil.isNotEmpty(paxInfo.getPaxType())) {
                input_paxType.setSelectedItem(paxInfo.getPaxType());
            }
            jPanel.add(input_paxType);

            JLabel text_birth = new JLabel("生日:");
            text_birth.setBounds(430, jPanel.getHeight() - 140, 30, 20);
            jPanel.add(text_birth);

            Chooser chooser = Chooser.getInstance();
            JTextField datePicker = new JTextField();
            datePicker.setBounds(460, jPanel.getHeight() - 140, 80, 20);
            datePicker.setText(paxInfo.getBirth());
            chooser.register(datePicker);
            jPanel.add(datePicker);

            JLabel text_cardNo = new JLabel("证件号:");
            text_cardNo.setBounds(20, jPanel.getHeight() - 110, 50, 20);
            jPanel.add(text_cardNo);

            JTextField input_cardNo = new JTextField(paxInfo.getCardNo());
            input_cardNo.setBounds(65, jPanel.getHeight() - 110, 135, 20);
            jPanel.add(input_cardNo);
            input_cardNo.addFocusListener(new FocusClass(jPanel));

            JLabel text_sellPrice = new JLabel("销售价:");
            text_sellPrice.setBounds(210, jPanel.getHeight() - 110, 60, 20);
            jPanel.add(text_sellPrice);

            JTextField input_sellPrice = new JTextField(paxInfo.getSellPrice());
            input_sellPrice.setBounds(260, jPanel.getHeight() - 110, 100, 20);
            jPanel.add(input_sellPrice);

            JLabel text_teicketNo = new JLabel("票号:");
            text_teicketNo.setBounds(380, jPanel.getHeight() - 110, 30, 20);
            jPanel.add(text_teicketNo);

            JTextField input_teicketNo = new JTextField(paxInfo.getTicketNo());
            input_teicketNo.setBounds(410, jPanel.getHeight() - 110, 135, 20);
            jPanel.add(input_teicketNo);
            input_teicketNo.addFocusListener(new FocusClass(jPanel));
        }
        setResizable(false);
        setVisible(true);
    }

    public class FocusClass
            implements FocusListener {
        private JPanel panel;

        public FocusClass(JPanel panel) {
            this.panel = panel;
        }

        public void focusGained(FocusEvent e) {
        }

        public void focusLost(FocusEvent e) {
            JTextField source = (JTextField) e.getSource();
            String birth = InitUtil.getBirth(source.getText());
            if (birth == null) {
                return;
            }
            Integer index = null;
            for (int i = 0; i < this.panel.getComponents().length; i++) {
                if (this.panel.getComponents()[i].equals(source)) {
                    index = Integer.valueOf(i - 2);
                    break;
                }
            }
            if (index != null) {
                JTextField component = (JTextField) this.panel.getComponent(index.intValue());
                if (StringUtil.isEmpty(component.getText())) {
                    component.setText(birth);
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton) e.getSource();
        if ("保存订单".equals(source.getText())) {
            JPanel panel = (JPanel) source.getParent();
            Component[] components = panel.getComponents();
            String orderNo = ((JTextField) components[1]).getText();
            String platform = ((JTextField) components[3]).getText();
            String dep = ((JTextField) components[5]).getText();
            String arr = ((JTextField) components[7]).getText();
            String orderStatus = ((JComboBox) components[9]).getSelectedItem().toString();

            String flightNo = ((JTextField) components[11]).getText();
            String depTime = ((JTextField) components[13]).getText();

            String errMsg = null;
            if (StringUtil.isEmpty(orderNo)) {
                errMsg = "订单号不能为空";
            } else if (StringUtil.isEmpty(platform)) {
                errMsg = "订单平台不能为空";
            } else if ((StringUtil.isEmpty(dep)) || (dep.trim().length() != 3)) {
                errMsg = "请输入合格的出发地三字码";
            } else if ((StringUtil.isEmpty(arr)) || (arr.trim().length() != 3)) {
                errMsg = "请输入合格的到达地三字码";
            } else if (StringUtil.isEmpty(flightNo)) {
                errMsg = "航班号不能为空";
            } else if (StringUtil.isEmpty(depTime)) {
                errMsg = "出发时间不能为空";
            } else if (!InitUtil.checkDate(depTime, "yyyy-MM-dd")) {
                errMsg = "出发时间格式不符合(2019-01-01)标准";
            }
            if (errMsg != null) {
                MsgUtil.errorRemind(errMsg);
                return;
            }
            List<List<String>> lists = parseData(components);
            if (lists == null) {
                return;
            }
            this.orderInfo.setOrderNo(orderNo);
            this.orderInfo.setOrderStatus(orderStatus);
            this.orderInfo.setPlatform(platform);
            this.orderInfo.setFlightNo(flightNo);
            String time = "";
            if (StringUtil.isNotEmpty(this.orderInfo.getDepTime())) {
                try {
                    time = " " + this.orderInfo.getDepTime().substring(11);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            this.orderInfo.setDepTime(depTime + time);
            this.orderInfo.setDep(dep.trim().toUpperCase());
            this.orderInfo.setArr(arr.trim().toUpperCase());
            List<PaxInfo> paxInfos = Lists.newArrayList();
            this.orderInfo.setPaxInfos(paxInfos);
            for (int i = 0; i < ((List) lists.get(0)).size(); i++) {
                PaxInfo paxInfo = new PaxInfo();
                paxInfo.setCardType((String) ((List) lists.get(0)).get(i));
                paxInfo.setPaxName((String) ((List) lists.get(1)).get(i));
                paxInfo.setPaxType((String) ((List) lists.get(2)).get(i));
                paxInfo.setCardNo((String) ((List) lists.get(3)).get(i));
                paxInfo.setSellPrice((String) ((List) lists.get(4)).get(i));
                paxInfo.setBirth((String) ((List) lists.get(5)).get(i));


                paxInfo.setSex("男");
                if (lists.get(0).get(i).equals("身份证")) {
                    String card = lists.get(3).get(i);
                    // paxInfo.setBirth();
                    String year = card.substring(6, 10);// 截取年
                    String month = card.substring(10, 12);// 截取月份
                    String day = card.substring(12, 14);// 截取天
                    String birth = year + "-" + month + "-" + day;
                    paxInfo.setBirth(birth);
                    char sex = card.charAt(16);
                    if ((Integer.valueOf(sex) % 2) == 0 ) {
                        paxInfo.setSex("女");
                    }
                }
                paxInfo.setTicketNo((String) ((List) lists.get(6)).get(i));
                paxInfos.add(paxInfo);
            }
            if (!"官网待支付".equals(orderStatus)) {
                this.orderInfo.setLocation(null);
            }
            OrderInfo info = (OrderInfo) SysData.grabOrderMap.get(this.orderInfo.getId() + "");
            if ((checkBackTicket(this.orderInfo)) && (info != null) &&
                    (MsgUtil.selectRemind("该订单处于抢票中,是否取消") == 0)) {
                SysData.grabOrderMap.remove(info.getId() + "");
                if ("抢票中".equals(this.orderInfo.getOrderStatus())) {
                    this.orderInfo.setOrderStatus("等待出票");
                }
                this.orderInfo.setGrabOver(Boolean.valueOf(true));
                this.orderInfo.setGrabTime("");
                this.orderInfo.setGrabPrice("");
                this.orderInfo.setGrabStatus("");
            }
            if (MsgUtil.selectRemind("确定更改") != 0) {
                return;
            }
            String result = SqliteHander.modifyObjInfo(this.orderInfo, null);
            if (!"true".equals(result)) {
                MsgUtil.errorRemind(result);
                return;
            }
            MsgUtil.confirmRemind("更改成功");
            dispose();
            OrderListPanel.loadListData(this.parentPanel);
            if ((StringUtil.isNotEmpty(this.orderInfo.getOrderStatus())) && (!"出票完成".equals(this.orderInfo.getOrderStatus())) && (checkBackTicket(this.orderInfo))) {
                backTicketService.submit(new Runnable() {
                    public void run() {
                        ModifyOrderDialog.this.orderInfo.setOrderStatus("回填失败");
                        String[] fileds = {"orderStatus"};
                        String result = ReqHandler.modifyOrderInfo(ModifyOrderDialog.this.orderInfo);
                        if ("true".equals(result)) {
                            ModifyOrderDialog.this.orderInfo.setOrderStatus("出票完成");
                        }
                        SqliteHander.modifyObjInfo(ModifyOrderDialog.this.orderInfo, fileds);
                    }
                });
            }
        }
    }

    private boolean checkBackTicket(OrderInfo orderInfo) {
        List<PaxInfo> paxInfos = orderInfo.getPaxInfos();
        if ((paxInfos == null) || (paxInfos.isEmpty())) {
            return false;
        }
        for (PaxInfo paxInfo : orderInfo.getPaxInfos()) {
            if ((StringUtil.isEmpty(paxInfo.getPaxName())) || (StringUtil.isEmpty(paxInfo.getCardNo())) || (StringUtil.isEmpty(paxInfo.getTicketNo()))) {
                return false;
            }
        }
        return true;
    }

    public List<List<String>> parseData(Component[] components) {
        ArrayList<Component> coms = new ArrayList(Arrays.asList(components));
        List<Component> paxInfoList = coms.subList(15, coms.size());
        Iterator<Component> iterator = paxInfoList.iterator();
        while (iterator.hasNext()) {
            Component next = (Component) iterator.next();
            if ((next instanceof JLabel)) {
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
        List<String> ticketNos = Lists.newArrayList();
        List<String> births = Lists.newArrayList();
        lists.add(cardtypes);
        lists.add(paxnames);
        lists.add(paxtypes);
        lists.add(cardnos);
        lists.add(sellprices);
        lists.add(births);
        lists.add(ticketNos);
        for (int i = 0; i < paxInfoList.size(); i++) {
            if (i % 7 == 0) {
                JComboBox component = (JComboBox) paxInfoList.get(i);
                Object selectedItem = component.getSelectedItem().toString();
                if (selectedItem == null) {
                    errMsg = "请选择证件类型";
                    break;
                }
                cardtypes.add(selectedItem.toString());
            }
            if (i % 7 == 1) {
                JTextField component = (JTextField) paxInfoList.get(i);
                String text = component.getText();
                if (StringUtil.isEmpty(text)) {
                    errMsg = "姓名不能为空";
                    break;
                }
                paxnames.add(text);
            }
            if (i % 7 == 2) {
                JComboBox component = (JComboBox) paxInfoList.get(i);
                Object selectedItem = component.getSelectedItem();
                if (selectedItem == null) {
                    errMsg = "请选择乘客类型";
                    break;
                }
                paxtypes.add(selectedItem.toString());
            }
            if (i % 7 == 3) {
                JTextField component = (JTextField) paxInfoList.get(i);
                String text = component.getText();
                if ((text == null) || (!InitUtil.checkDate(text, "yyyy-MM-dd"))) {
                    errMsg = "生日格式不正确(2019-01-01)";
                    break;
                }
                births.add(text);
            }
            if (i % 7 == 4) {
                JTextField component = (JTextField) paxInfoList.get(i);
                String text = component.getText();
                if (StringUtil.isEmpty(text)) {
                    errMsg = "证件号不能为空";
                    break;
                }
                cardnos.add(text);
            }
            if (i % 7 == 5) {
                JTextField component = (JTextField) paxInfoList.get(i);
                String text = component.getText();
                if ((StringUtil.isEmpty(text)) || (!checkPrice(text))) {
                    errMsg = "销售价输入有误";
                    break;
                }
                sellprices.add(text);
            }
            if (i % 7 == 6) {
                JTextField component = (JTextField) paxInfoList.get(i);
                String text = component.getText();
                ticketNos.add(text);
            }
        }
        if (errMsg != null) {
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

    private boolean checkDate(String date) {
        try {
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
            return true;
        } catch (ParseException e) {
            logger.error("error", e);
        }
        return false;
    }
}
