package com.feeye.sqlite;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.feeye.entity.OrderInfo;
import com.feeye.entity.PaxInfo;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

/**
 * @description: This is a class!
 * @author: chenjian
 * @date: 2019/02/23 09:50
 */
public class SqliteTest {

	@Test
	public void test2() throws SQLException, ClassNotFoundException {
		SqliteHelper sqliteHelper = null;
//		String path = "C:\\Users\\ASUS\\Desktop\\grabticket";
//		File file = new File(path + "\\grabticket.db");
//		if (!file.isFile()||!file.exists()) {
//			try {
//				file.createNewFile();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		try {
			sqliteHelper = new SqliteHelper("C:\\Users\\ASUS\\Desktop\\grabticket\\grabticket");
		} catch (Exception e) {
			e.printStackTrace();
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
				"   creatTime        VARCHAR(255),\n" +
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
//	@Test
//	public void testInsert() throws SQLException, ClassNotFoundException {
//		SqliteHelper sqliteHelper = null;
//		try {
//			sqliteHelper = new SqliteHelper("C:\\Users\\凉拌西红柿\\Desktop\\Idea Project\\grab\\database\\grabticket");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		OrderInfo orderInfo = new OrderInfo();
//		orderInfo.setOrderNo("xixixixixi");
//		orderInfo.setPlatform("qunaer");
//		orderInfo.setRunOver(false);
//		orderInfo.setPageNum(10);
//		orderInfo.setUsername("policytest");
//		orderInfo.setDate(1000000);
//		orderInfo.setImportDate("2018-01-02 12:12:12");
//		List<PaxInfo> paxInfos = new ArrayList<>();
//		PaxInfo paxInfo = new PaxInfo();
//		paxInfo.setPaxName("domcj");
////		paxInfo.setPaxType("成人");
//		paxInfo.setCardNo("43087846672923");
//		paxInfo.setTicketNo("430-87846672923");
//		paxInfos.add(paxInfo);
//		paxInfos.add(paxInfo);
//		orderInfo.setPaxInfos(paxInfos);
//		String insertSql = orderInfo.getUpdateSql(null, OrderInfo.INSERT);
//		if (sqliteHelper!=null) {
//			int i = sqliteHelper.executeUpdate(insertSql);
//			System.out.println(i);
//		}
//	}
//	@Test
//	public void testUpdate() throws SQLException, ClassNotFoundException {
//		SqliteHelper sqliteHelper = null;
//		try {
//			sqliteHelper = new SqliteHelper("C:\\Users\\凉拌西红柿\\Desktop\\Idea Project\\grab\\database\\grabticket");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		OrderInfo orderInfo = new OrderInfo();
//		orderInfo.setId(1L);
//		orderInfo.setOrderNo("domcj");
//		orderInfo.setPlatform("qunaer");
//		orderInfo.setRunOver(null);
//		orderInfo.setPageNum(0);
//		orderInfo.setUsername("policytest");
//		orderInfo.setDate(null);
//		List<PaxInfo> paxInfos = new ArrayList<>();
//		PaxInfo paxInfo = new PaxInfo();
//		paxInfo.setPaxName("domcj");
////		paxInfo.setPaxType("成人");
//		paxInfo.setCardNo("732323029392323");
//		paxInfo.setTicketNo("430-87846672923");
//		paxInfos.add(paxInfo);
//		paxInfos.add(paxInfo);
//		orderInfo.setPaxInfos(paxInfos);
//		String updateSql = orderInfo.getUpdateSql(null, OrderInfo.UPDATE);
//		if (sqliteHelper!=null) {
//			int i = sqliteHelper.executeUpdate(updateSql);
//			System.out.println(i);
//		}
//	}
//
//	@Test
//	public void testSelectOne() throws SQLException, ClassNotFoundException {
//		SqliteHelper sqliteHelper = null;
//		try {
//			sqliteHelper = new SqliteHelper("C:\\Users\\凉拌西红柿\\Desktop\\Idea Project\\grab\\database\\grabticket");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		//select * from sensor_data where datetime(timestamp) Between datetime('2015-01-12 00:00:00') AND datetime('2015-01-13 00:00:00')
//		String sql = "select * from orderInfo ";
//		if (sqliteHelper!=null) {
//			List<OrderInfo> orderInfs = sqliteHelper.executeQuery(sql, new ResultSetHandler<OrderInfo>() {
//				@Override
//				public OrderInfo rowToObj(ResultSet rs) throws SQLException {
//					OrderInfo orderInfo = new OrderInfo();
//					orderInfo.setId(Long.valueOf(rs.getInt(1)));
//					orderInfo.setOrderNo(rs.getString(2));
//					orderInfo.setPlatform(rs.getString(3));
//					Object runOver = rs.getObject(4);
//					if (runOver!=null) {
//						orderInfo.setRunOver(0==Integer.parseInt(runOver.toString()));
//					}
//					orderInfo.setDate(rs.getInt(5));
//					String username = rs.getString(6);
//					orderInfo.setUsername(username);
//					String paxNames = rs.getString(7);
//					String paxTypes = rs.getString(8);
//					String ticketNos = rs.getString(9);
//					String cardNos = rs.getString(10);
//					Integer pageNum = rs.getInt(11);
//					orderInfo.setImportDate(rs.getString(12));
//					orderInfo.setPageNum(pageNum);
//					List<PaxInfo> paxInfos = new ArrayList<>();
//					orderInfo.setPaxInfos(paxInfos);
//					if (paxNames!=null&&!paxNames.trim().equals("")) {
//						String[] names = paxNames.split("_");
//						String[] types = paxTypes.split("_");
//						String[] ticketnos = ticketNos.split("_");
//						String[] cardnos = cardNos.split("_");
//						for (int i = 0; i < paxNames.split("_").length; i++) {
//							PaxInfo paxInfo = new PaxInfo();
//							paxInfo.setPaxName("null".equals(names[i])?null:names[i]);
//							paxInfo.setPaxType("null".equals(types[i])?null:types[i]);
//							paxInfo.setTicketNo("null".equals(ticketnos[i])?null:ticketnos[i]);
//							paxInfo.setCardNo("null".equals(cardnos[i])?null:cardnos[i]);
//							paxInfos.add(paxInfo);
//						}
//					}
//					return orderInfo;
//				}
//			});
//			for (OrderInfo orderInfo : orderInfs) {
//				System.out.println(orderInfo.toString());
//			}
//		}
//
//	}
//	@Test
//	public void delete() throws SQLException, ClassNotFoundException {
//		SqliteHelper sqliteHelper = null;
//		try {
//			sqliteHelper = new SqliteHelper("C:\\Users\\凉拌西红柿\\Desktop\\Idea Project\\grab\\database\\grabticket");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String deleteSql = "delete from orderInfo where id in(1, 2)";
//		if (sqliteHelper != null) {
//			int i = sqliteHelper.executeUpdate(deleteSql);
//			System.out.println(i);
//		}
//	}
//	@Test
//	public void updateTable() throws SQLException, ClassNotFoundException {
//		SqliteHelper sqliteHelper = null;
//		try {
//			sqliteHelper = new SqliteHelper("C:\\Users\\凉拌西红柿\\Desktop\\Idea Project\\grab\\database\\grabticket");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		String updateSql = "alter table orderInfo add importDate VARCHAR(255) default '2018-12-12 12:00:00'";
//		if (sqliteHelper != null) {
//			int i = sqliteHelper.executeUpdate(updateSql);
//			System.out.println(i);
//		}
//	}
//	@Test
//	public void updateTable2() throws SQLException, ClassNotFoundException {
//		SqliteHelper sqliteHelper = null;
//		try {
//			sqliteHelper = new SqliteHelper("C:\\Users\\凉拌西红柿\\Desktop\\Idea Project\\grab\\database\\grabticket");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		if (sqliteHelper!=null) {
//			int i = sqliteHelper.executeUpdate("CREATE TABLE temp as select id, orderNo,platform,runOver, date, " +
//			"username, paxNames, paxTypes, ticketNos, cardNos, pageNum from orderInfo");
//		}
//		sqliteHelper = new SqliteHelper("C:\\Users\\凉拌西红柿\\Desktop\\Idea Project\\grab\\database\\grabticket");
//
//		int i = sqliteHelper.executeUpdate("drop table orderInfo");
//
//		sqliteHelper = new SqliteHelper("C:\\Users\\凉拌西红柿\\Desktop\\Idea Project\\grab\\database\\grabticket");
//		sqliteHelper.executeUpdate("alter table temp rename to orderInfo");
//	}
//
//	public static void main(String[] args) {
//		try {
//			//new File("").getAbsolutePath()+"\\myAppname.exe
//			String absolutePath = new File("").getAbsolutePath()+"\\SqliteTest.class";
//			System.out.println(absolutePath);
////			String path = "E:\\GrabApp.class";
////			CjClassLoader cjClassLoader = new CjClassLoader(path);
////			Class<?> grabApp = cjClassLoader.loadClass("com.domcj.grab.GrabApp", false);
////			Object o = grabApp.newInstance();
////			Method start = grabApp.getMethod("start");
////			start.invoke(o);
//		} catch (Throwable e) {
//			e.printStackTrace();
//		}
//	}
}
