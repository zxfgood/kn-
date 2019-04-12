package com.feeye.handler;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.feeye.entity.AccountInfo;
import com.feeye.entity.OrderInfo;
import com.feeye.entity.PaxInfo;
import com.feeye.init.SysData;
import com.feeye.sqlite.OrderConversion;
import com.feeye.sqlite.ResultSetHandler;
import com.feeye.sqlite.SqliteHelper;
import com.feeye.util.InitUtil;
import com.feeye.util.StringUtil;
import com.google.common.collect.Maps;

/**
 * @description: This is a class!
 * @author: chenjian
 * @date: 2019/02/25 10:12
 */
public class SqliteHander {

	private static final Logger logger = Logger.getLogger(SqliteHander.class);

	public static String dbPath = "";
	public static String ORDERINFO = "orderInfo";
	public static String ACCOUNTINFO = "accountInfo";
	public static int pageSize = 50;


	public static String addObjInfo(Object obj) {
		String insertSql = null;
		SqliteHelper sqliteHelper = null;
		try {
			sqliteHelper = new SqliteHelper(dbPath);
		} catch (Exception e) {
			logger.error("添加异常", e);
			return "添加异常";
		}
		if (obj instanceof OrderInfo) {
			insertSql = ((OrderInfo) obj).getUpdateSql(null, SysData.INSERT);
		} else if (obj instanceof AccountInfo){
			insertSql = ((AccountInfo) obj).getUpdateSql(null, SysData.INSERT);
		}
		if (sqliteHelper!=null) {
			int i = sqliteHelper.executeUpdate(insertSql);
			return 1==i?"true":"添加异常";
		}
		return "添加异常";
	}
	public static String modifyObjInfo(Object obj, String[] fields) {
		String insertSql = null;
		SqliteHelper sqliteHelper = null;
		try {
			sqliteHelper = new SqliteHelper(dbPath);
		} catch (Exception e) {
			logger.error("添加获取数据库连接异常", e);
			return "更改异常";
		}
		if (obj instanceof OrderInfo) {
			insertSql = ((OrderInfo) obj).getUpdateSql(fields, SysData.UPDATE);
		} else if (obj instanceof AccountInfo){
			insertSql = ((AccountInfo) obj).getUpdateSql(fields, SysData.UPDATE);
		}
		if (sqliteHelper!=null) {
			int i = sqliteHelper.executeUpdate(insertSql);
			return 1==i?"true":"更改异常";
		}
		return "更改异常";
	}
	public static String deleteObjInfo(List<Integer> ids, String objType) {
		SqliteHelper sqliteHelper = null;
		try {
			sqliteHelper = new SqliteHelper(dbPath);
		} catch (Exception e) {
			logger.error("删除获取数据库连接异常", e);
			return "删除异常";
		}
		StringBuffer insertSql = new StringBuffer("delete from "+objType+" where id in(");
		for (int i = 0; i < ids.size(); i++) {
			if (i==ids.size()-1) {
				insertSql.append(ids.get(i)+")");
			} else {
				insertSql.append(ids.get(i)+",");
			}
		}
		if (sqliteHelper!=null) {
			int i = sqliteHelper.executeUpdate(insertSql.toString());
			return ids.size()==i?"true":"删除异常";
		}
		return "删除异常";
	}

	public static List<OrderInfo> queryOrderInfo(String pageIndex, String startDate, String endDate, String policyCode, String orderNo, String orderStatus, String airCompany) {
		SqliteHelper sqliteHelper = null;
		try {
			sqliteHelper = new SqliteHelper(dbPath);
		} catch (Exception e) {
			logger.error("删除获取数据库连接异常", e);
			return null;
		}
		StringBuffer queryOrderSql = new StringBuffer("select * from orderInfo ");
		StringBuffer querySql = new StringBuffer();
		if (StringUtil.isEmpty(startDate)) {
			startDate = "2019-01-01";
		}
		startDate += " 00:00:00";
		if (StringUtil.isEmpty(endDate)) {
			endDate = SysData.sdf_date.format(new Date());
		}
		endDate += " 23:59:59";
		querySql.append(" where datetime(importDate)>=datetime('"+startDate+"') and datetime(importDate)<=datetime('"+endDate+"') ");
		if (StringUtil.isNotEmpty(airCompany)) {
			querySql.append(" and flightNo like '"+airCompany+"%' " );
		}
		if (StringUtil.isNotEmpty(orderNo)) {
			querySql.append(" and orderNo='"+orderNo+"' ");
		}
		if (StringUtil.isNotEmpty(orderStatus)) {
			querySql.append(" and orderStatus='"+orderStatus+"' " );
		}
		queryOrderSql.append(querySql);

		queryOrderSql.append(" order by importDate desc " );
		if (StringUtil.isNotEmpty(pageIndex)) {
			int offset = (Integer.parseInt(pageIndex)-1)*pageSize;
			queryOrderSql.append(" limit "+pageSize+" offset "+offset);
		}
		List<OrderInfo> orderInfos = sqliteHelper.executeQuery(queryOrderSql.toString(), new OrderConversion());
		if (orderInfos!=null&&!orderInfos.isEmpty()) {
			StringBuffer queryCountSql = new StringBuffer("select count(*) from orderInfo ").append(querySql);
			try {
				sqliteHelper = new SqliteHelper(dbPath);
			} catch (Exception e) {
				logger.error("删除获取数据库连接异常", e);
				return null;
			}
			List<Integer> orderCount = sqliteHelper.executeQuery(queryOrderSql.toString(), new ResultSetHandler<Integer>() {
				@Override
				public Integer rowToObj(ResultSet rs) throws SQLException {
					return (Integer) rs.getObject(1);
				}
			});
			if (orderCount!=null&&!orderCount.isEmpty()) {
				orderInfos.get(0).setTotalCount(orderCount.get(0)+"");
			}
		}
		return orderInfos;
	}

	public static List<AccountInfo> queryAccountInfo() {
		SqliteHelper sqliteHelper = null;
		try {
			sqliteHelper = new SqliteHelper(dbPath);
		} catch (Exception e) {
			logger.error("删除获取数据库连接异常", e);
			return null;
		}
		StringBuffer querySql = new StringBuffer("select * from accountInfo ");

		List<AccountInfo> accountInfos = sqliteHelper.executeQuery(querySql.toString(), new ResultSetHandler<AccountInfo>() {
			@Override
			public AccountInfo rowToObj(ResultSet rs) throws SQLException {
				AccountInfo info = new AccountInfo();
				info.setId(rs.getLong(1));
				info.setAirCompany(rs.getString(2));
				info.setAccount(rs.getString(3));
				info.setPassword(rs.getString(4));
				info.setContact(rs.getString(5));
				info.setTelPhone(rs.getString(6));
				info.setLoginState(rs.getString(7));
				info.setLoginTime(rs.getString(8));
				return info;
			}
		});
		if (accountInfos!=null) {
			synchronized (SysData.accountMap) {
				for (AccountInfo accountInfo : accountInfos) {
					AccountInfo info = InitUtil.getAccountInfoById(accountInfo.getId());
					if (info!=null) {
						accountInfo.setLoginTime(info.getLoginTime());
						accountInfo.setLoginState(info.getLoginState());
						accountInfo.setKeepLogin(info.isKeepLogin());
					}
				}
				SysData.accountMap.clear();
				if (accountInfos!=null&&!accountInfos.isEmpty()) {
					for (AccountInfo accountInfo : accountInfos) {
						Map<Long, AccountInfo> infoMap = SysData.accountMap.get(accountInfo.getAirCompany());
						if (infoMap==null) {
							infoMap = Maps.newConcurrentMap();
						}
						infoMap.put(accountInfo.getId(), accountInfo);
						SysData.accountMap.put(accountInfo.getAirCompany(), infoMap);
					}
				}
			}
		}
		return accountInfos;
	}
	public static void initDatabase() {
		String path = SysData.exeRealPath+"\\database\\"+SysData.feeyeusr;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		dbPath = path+"\\grabticket.db";
		initOrderDb();
		initAccountDb();
	}

	private static void initAccountDb() {
		SqliteHelper sqliteHelper = null;
		try {
			sqliteHelper = new SqliteHelper(dbPath);
		} catch (Exception e) {
			logger.error("error", e);
		}
		if (sqliteHelper!=null) {
			sqliteHelper.executeUpdate("CREATE TABLE IF NOT EXISTS accountInfo(\n" +
				"   ID INTEGER PRIMARY KEY     NOT NULL,\n" +
				"   airCompany        VARCHAR(255),\n" +
				"   account        VARCHAR(255),\n" +
				"   password        VARCHAR(255),\n" +
				"   contact        VARCHAR(255),\n" +
				"   telPhone        VARCHAR(255),\n" +
				"   loginState        VARCHAR(255),\n" +
				"   loginTime       VARCHAR(255)\n" +
				")");
		}
	}
	private static void initOrderDb() {
		SqliteHelper sqliteHelper = null;
		try {
			sqliteHelper = new SqliteHelper(dbPath);
		} catch (Exception e) {
			logger.error("error", e);
		}
		if (sqliteHelper!=null) {
			sqliteHelper.executeUpdate("CREATE TABLE IF NOT EXISTS orderInfo(\n" +
				"   ID INTEGER PRIMARY KEY     NOT NULL,\n" +
				"   orderNo        VARCHAR(255),\n" +
				"   username        VARCHAR(255),\n" +
				"   orderStatus        VARCHAR(255),\n" +
				"   grabStatus         VARCHAR(255),\n" +
				"   flightNo        VARCHAR(255),\n" +
				"   depTime        VARCHAR(255),\n" +
				"   dep       VARCHAR(255),\n" +
				"   arr        VARCHAR(255),\n" +
				"   account        VARCHAR(255),\n" +
				"   payType        VARCHAR(255),\n" +
				"   platform        VARCHAR(255),\n" +
				"   orderNoNew        VARCHAR(255),\n" +
				"   appPrice        VARCHAR(255),\n" +
				"   grabPrice        VARCHAR(255),\n" +
				"   outPrice        VARCHAR(255),\n" +
				"   contact        VARCHAR(255),\n" +
				"   telPhone        VARCHAR(255),\n" +
				"   payCode        VARCHAR(255),\n" +
				"   importDate        VARCHAR(255),\n" +
				"   grabOver        INT(255),\n" +
				"   grabTime        VARCHAR(255),\n" +
				"   location        VARCHAR(255),\n" +
				"   cookie        VARCHAR(255),\n" +
				"   creatTime        INTEGER,\n" +
				"   paxNames        VARCHAR(255),\n" +
				"   paxTypes        VARCHAR(255),\n" +
				"   sexs        VARCHAR(255),\n" +
				"   cardTypes        VARCHAR(255),\n" +
				"   cardNos        VARCHAR(255),\n" +
				"   births        VARCHAR(255),\n" +
				"   sellPrices        VARCHAR(255),\n" +
				"   ticketNos        VARCHAR(255)\n" +
				")");
		}
	}
}
