package com.feeye.entity;

import java.lang.reflect.Field;
import java.util.Date;

import com.feeye.init.SysData;

/**
 * @description: 官网账号信息!
 * @author: domcj
 *
 * @date: 2019/01/17 11:18
 */
public class AccountInfo {
	private static String[] filedNames = {"id", "airCompany", "account", "password", "contact", "telPhone", "loginState", "loginTime"};
	private Long id;
	private String airCompany;
	private String account;
	private String password;
	private String contact;
	private String telPhone;
	private String loginState;
	private String loginTime;
	private boolean keepLogin = false;

	private StringBuffer updateSql;  //数据库操作语句

	@Override
	public String toString() {
		return "AccountInfo{" +
				"id=" + id +
				", airCompany='" + airCompany + '\'' +
				", account='" + account + '\'' +
				", password='" + password + '\'' +
				", contact='" + contact + '\'' +
				", telPhone='" + telPhone + '\'' +
				", loginState='" + loginState + '\'' +
				'}';
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAirCompany() {
		return airCompany;
	}

	public void setAirCompany(String airCompany) {
		this.airCompany = airCompany;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getLoginState() {
		return loginState;
	}

	public void setLoginState(String loginState) {
		this.loginState = loginState;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public boolean isKeepLogin() {
		return keepLogin;
	}

	public void setKeepLogin(boolean keepLogin) {
		this.keepLogin = keepLogin;
	}

	public synchronized String getUpdateSql(String[] fields, String operaType) {
		if (SysData.INSERT.equals(operaType)) {
			if (id!=null) {
				return "对象类含有主键id,请检查操作类型填写是否有误";
			}
			updateSql = new StringBuffer("INSERT INTO accountInfo values(");
			appendSqlByFiled(filedNames, SysData.INSERT);
			return updateSql.substring(0, updateSql.length()-1)+")";
		} else {
			updateSql = new StringBuffer("update accountInfo set ");
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
			updateSql.append((Boolean)paraValue?0:1+",");
		} else if (paraValue instanceof Long) {
			updateSql.append(paraValue+",");
		}
	}
}
