package com.feeye.sqlite;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.feeye.entity.OrderInfo;
import com.feeye.entity.PaxInfo;
import com.feeye.util.StringUtil;
import com.google.common.collect.Lists;

/**
 * @description: This is a class!
 * @author: chenjian
 * @date: 2019/02/25 13:49
 */
public class OrderConversion implements ResultSetHandler<OrderInfo> {

	@Override
	public OrderInfo rowToObj(ResultSet rs) throws SQLException {
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setId(rs.getLong(1));
		orderInfo.setOrderNo(rs.getString(2));
		orderInfo.setUsername(rs.getString(3));
		orderInfo.setOrderStatus(rs.getString(4));
		orderInfo.setGrabStatus(rs.getString(5));
		orderInfo.setFlightNo(rs.getString(6));
		orderInfo.setDepTime(rs.getString(7));
		orderInfo.setDep(rs.getString(8));
		orderInfo.setArr(rs.getString(9));
		orderInfo.setAccount(rs.getString(10));
		orderInfo.setPayType(rs.getString(11));
		orderInfo.setPlatform(rs.getString(12));
		orderInfo.setOrderNoNew(rs.getString(13));
		orderInfo.setAppPrice(rs.getString(14));
		orderInfo.setGrabPrice(rs.getString(15));
		orderInfo.setOutPrice(rs.getString(16));
		orderInfo.setContact(rs.getString(17));
		orderInfo.setTelPhone(rs.getString(18));
		orderInfo.setPayCode(rs.getString(19));
		orderInfo.setImportDate(rs.getString(20));
		Object runOver = rs.getObject(21);
		if (runOver!=null) {
			orderInfo.setGrabOver(0==Integer.parseInt(runOver.toString()));
		}
		orderInfo.setGrabTime(rs.getString(22));
		orderInfo.setLocation(rs.getString(23));
		orderInfo.setCookie(rs.getString(24));
		orderInfo.setCreatTime(rs.getLong(25));

		String paxNames = rs.getString(26);
		String paxTypes = rs.getString(27);
		String sexs = rs.getString(28);
		String cardTypes = rs.getString(29);
		String cardNos = rs.getString(30);
		String births = rs.getString(31);
		String sellPrices = rs.getString(32);
		String ticketNos = rs.getString(33);
		if (StringUtil.isNotEmpty(paxNames)) {
			List<PaxInfo> paxInfos = Lists.newArrayList();
			orderInfo.setPaxInfos(paxInfos);
			for (int i = 0; i < paxNames.split("_").length; i++) {
				PaxInfo paxInfo = new PaxInfo();
				try {
					paxInfo.setPaxName("null".equalsIgnoreCase(paxNames.split("_")[i])?null:paxNames.split("_")[i]);
				} catch (Exception e) {
				}
				try {
					paxInfo.setPaxType("null".equalsIgnoreCase(paxTypes.split("_")[i])?null:paxTypes.split("_")[i]);
				} catch (Exception e) {
				}
				try {
					paxInfo.setSex("null".equalsIgnoreCase(sexs.split("_")[i])?null:sexs.split("_")[i]);
				} catch (Exception e) {
				}
				try {
					paxInfo.setCardType("null".equalsIgnoreCase(cardTypes.split("_")[i])?null:cardTypes.split("_")[i]);
				} catch (Exception e) {
				}
				try {
					paxInfo.setCardNo("null".equalsIgnoreCase(cardNos.split("_")[i])?null:cardNos.split("_")[i]);
				} catch (Exception e) {
				}
				try {
					paxInfo.setBirth("null".equalsIgnoreCase(births.split("_")[i])?null:births.split("_")[i]);
				} catch (Exception e) {
				}
				try {
					paxInfo.setSellPrice("null".equalsIgnoreCase(sellPrices.split("_")[i])?null:sellPrices.split("_")[i]);
				} catch (Exception e) {
				}
				try {
					paxInfo.setTicketNo("null".equalsIgnoreCase(ticketNos.split("_")[i])?null:ticketNos.split("_")[i]);
				} catch (Exception e) {
				}
				paxInfos.add(paxInfo);
			}
		}
		return orderInfo;
	}
}
