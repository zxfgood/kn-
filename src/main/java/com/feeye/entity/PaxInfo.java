package com.feeye.entity;

/**
 * @description: 乘客信息!
 * @author: domcj
 * @date: 2019/01/25 09:17
 */
public class PaxInfo {

	private String paxName;      // 乘客姓名
	private String paxType;      // 乘客类型
	private String sex;      	  // 性别
	private String cardType;     // 证件类型
	private String cardNo;       // 证件号
	private String birth;        // 生日
	private String sellPrice;    // 销售价
	private String ticketNo;     // 票号

	public String getPaxName() {
		return paxName;
	}

	public void setPaxName(String paxName) {
		this.paxName = paxName;
	}

	public String getPaxType() {
		return paxType;
	}

	public void setPaxType(String paxType) {
		this.paxType = paxType;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getBirth() {
		return birth;
	}

	public void setBirth(String birth) {
		this.birth = birth;
	}

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getTicketNo() {
		return ticketNo;
	}

	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	@Override
	public String toString() {
		return "PaxInfo{" +
				"paxName='" + paxName + '\'' +
				", paxType='" + paxType + '\'' +
				", sex='" + sex + '\'' +
				", cardType='" + cardType + '\'' +
				", cardNo='" + cardNo + '\'' +
				", birth='" + birth + '\'' +
				", sellPrice='" + sellPrice + '\'' +
				", ticketNo='" + ticketNo + '\'' +
				'}';
	}
}
