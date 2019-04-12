package com.feeye.entity;

import java.lang.reflect.Field;
import java.util.List;

import com.feeye.init.SysData;

/**
 * @description: 订单类!
 * @author: domcj
 * @date: 2019/01/17 13:36
 */
public class OrderInfo implements Cloneable{
	private static String[] filedNames = {"id", "orderNo", "username", "orderStatus", "grabStatus", "flightNo", "depTime", "dep", "arr", "account", "payType", "platform", "orderNoNew", "appPrice", "grabPrice", "outPrice", "contact", "telPhone", "payCode", "importDate", "grabOver", "grabTime", "location", "cookie", "creatTime", "paxNames", "paxTypes", "sexs", "cardTypes", "cardNos", "births", "sellPrices", "ticketNos"};
	private Long id;
	private String orderNo;       // 订单号
	private String username;       // 用户名
	private String orderStatus;   // 订单状态
	private String grabStatus;   // 抢票状态
	private String flightNo;      // 航班号
	private String depTime;		  // 出发时间  yyyy-mm-dd HH:mm:ss
	private String dep;			  // 出发地
	private String arr;			  // 到达地
	private String account;       // 官网账号
	private String payType;       // 支付方式
	private String platform;      // 订单平台
	private String orderNoNew;      // 官网订单号

	private String appPrice;      // APP价格
	private String grabPrice;     // 抢票设置价格
	private String outPrice;     // 出票价格
	private String contact;       // 联系人
	private String telPhone;		// 联系电话
	private String payCode;			// 交易流水号
	private String importDate;		// 导入日期
	private Boolean grabOver;		// 是否抢到价格
	private String grabTime;		// 抢票时间

	private String location;
	private String cookie;
	private Long creatTime;

	private String paxNames;      // 乘客姓名
	private String paxTypes;      // 乘客类型
	private String sexs;      	  // 性别
	private String cardTypes;     // 证件类型
	private String cardNos;       // 证件号
	private String births;        // 生日
	private String sellPrices;    // 销售价
	private String ticketNos;     // 票号

	private List<PaxInfo> paxInfos;  //乘客信息
	private String totalCount;
	private StringBuffer updateSql;  //数据库操作语句

	public OrderInfo clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		if (clone!=null) {
			return (OrderInfo) clone;
		}
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public String getDepTime() {
		return depTime;
	}

	public void setDepTime(String depTime) {
		this.depTime = depTime;
	}

	public String getDep() {
		return dep;
	}

	public void setDep(String dep) {
		this.dep = dep;
	}

	public String getArr() {
		return arr;
	}

	public void setArr(String arr) {
		this.arr = arr;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getOrderNoNew() {
		return orderNoNew;
	}

	public void setOrderNoNew(String orderNoNew) {
		this.orderNoNew = orderNoNew;
	}

	public String getAppPrice() {
		return appPrice;
	}

	public void setAppPrice(String appPrice) {
		this.appPrice = appPrice;
	}

	public String getGrabPrice() {
		return grabPrice;
	}

	public void setGrabPrice(String grabPrice) {
		this.grabPrice = grabPrice;
	}

	public String getOutPrice() {
		return outPrice;
	}

	public void setOutPrice(String outPrice) {
		this.outPrice = outPrice;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getTelPhone() {
		return telPhone;
	}

	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
	}

	public String getPayCode() {
		return payCode;
	}

	public void setPayCode(String payCode) {
		this.payCode = payCode;
	}

	public String getImportDate() {
		return importDate;
	}

	public void setImportDate(String importDate) {
		this.importDate = importDate;
	}

	public Boolean getGrabOver() {
		return grabOver;
	}

	public void setGrabOver(Boolean grabOver) {
		this.grabOver = grabOver;
	}

	public String getGrabTime() {
		return grabTime;
	}

	public void setGrabTime(String grabTime) {
		this.grabTime = grabTime;
	}

	public List<PaxInfo> getPaxInfos() {
		return paxInfos;
	}

	public void setPaxInfos(List<PaxInfo> paxInfos) {
		this.paxInfos = paxInfos;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getCookie() {
		return cookie;
	}

	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	public Long getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Long creatTime) {
		this.creatTime = creatTime;
	}

	public String getGrabStatus() {
		return grabStatus;
	}

	public void setGrabStatus(String grabStatus) {
		this.grabStatus = grabStatus;
	}

	public String getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}

	public String getPaxNames() {
		return paxNames;
	}

	public void setPaxNames(String paxNames) {
		this.paxNames = paxNames;
	}

	public String getPaxTypes() {
		return paxTypes;
	}

	public void setPaxTypes(String paxTypes) {
		this.paxTypes = paxTypes;
	}

	public String getSexs() {
		return sexs;
	}

	public void setSexs(String sexs) {
		this.sexs = sexs;
	}

	public String getCardTypes() {
		return cardTypes;
	}

	public void setCardTypes(String cardTypes) {
		this.cardTypes = cardTypes;
	}

	public String getCardNos() {
		return cardNos;
	}

	public void setCardNos(String cardNos) {
		this.cardNos = cardNos;
	}

	public String getBirths() {
		return births;
	}

	public void setBirths(String births) {
		this.births = births;
	}

	public String getSellPrices() {
		return sellPrices;
	}

	public void setSellPrices(String sellPrices) {
		this.sellPrices = sellPrices;
	}

	public String getTicketNos() {
		return ticketNos;
	}

	public void setTicketNos(String ticketNos) {
		this.ticketNos = ticketNos;
	}
	public synchronized String getUpdateSql(String[] fields, String operaType) {
		paxNames = "";
		paxTypes = "";
		sexs = "";
		cardTypes = "";
		cardNos = "";
		births = "";
		sellPrices = "";
		ticketNos = "";
		if (paxInfos!=null&&!paxInfos.isEmpty()) {
			for (PaxInfo paxInfo : paxInfos) {
				paxNames += "_"+paxInfo.getPaxName();
				paxTypes += "_"+paxInfo.getPaxType();
				sexs += "_"+paxInfo.getSex();
				cardTypes += "_"+paxInfo.getCardType();
				cardNos += "_"+paxInfo.getCardNo();
				births += "_"+paxInfo.getBirth();
				sellPrices += "_"+paxInfo.getSellPrice();
				ticketNos += "_"+paxInfo.getTicketNo();
			}
			paxNames = paxNames.substring(1);
			paxTypes = paxTypes.substring(1);
			sexs = sexs.substring(1);
			cardTypes = cardTypes.substring(1);
			cardNos = cardNos.substring(1);
			births = births.substring(1);
			sellPrices = sellPrices.substring(1);
			ticketNos = ticketNos.substring(1);
		}
		if (SysData.INSERT.equals(operaType)) {
			if (id!=null) {
				return "对象类含有主键id,请检查操作类型填写是否有误";
			}
			updateSql = new StringBuffer("INSERT INTO orderInfo values(");
			appendSqlByFiled(filedNames, SysData.INSERT);
			return updateSql.substring(0, updateSql.length()-1)+")";
		} else {
			updateSql = new StringBuffer("update orderInfo set ");
			appendSqlByFiled(fields==null?filedNames:fields, SysData.UPDATE);
			return updateSql.substring(0, updateSql.length()-1) + " where id="+id;
		}
	}

	private void appendSqlByFiled(String[] filedNames, String operaType) {
		Object fieldValue = null;
		Field field = null;
		for (String filedName : filedNames) {
			try {
				field = this.getClass().getDeclaredField(filedName);
				fieldValue = field.get(this);
			} catch (Exception e) {
				e.printStackTrace();
			}
			appendPara(SysData.INSERT.equals(operaType)?null:filedName, fieldValue);
		}
	}

	private void appendPara(String paraName, Object paraValue) {
		if (paraName!=null) {
			updateSql.append(paraName+"=");
		}
		if (paraValue==null) {
			updateSql.append(null+",");
		} else if (paraValue instanceof String){
			updateSql.append("'"+paraValue+"',");
		} else if (paraValue instanceof Integer) {
			updateSql.append(paraValue+",");
		} else if (paraValue instanceof Boolean) {
			updateSql.append(((Boolean)paraValue?0:1)+",");
		} else if (paraValue instanceof Long) {
			updateSql.append(paraValue+",");
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
