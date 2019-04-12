package com.feeye.entity;

public class CabinInfo {
	private Long id;
	private String cabinCode;//仓位代码
	private String lastSeat;//剩余座位
	private String baseCabin;//基础仓位代码
	private String price;//价格
	private String basePrice;//基础仓位价格
	private String origPrice;//原始公布运价
	private String refundMessage;//退票规则
	private String changeMessage;//改签规则
	private String discount;//折扣
	private String cardPrice;
	
	private String tax;
	//国航请求ID
	private String requestId;
	//价格类型
	private String priceType;
	
	private String luggagecode;
	private String adultprice;//成人票价
	private String infantprice;//婴儿票价
	private String childprice;//儿童票价
	private String adultfarebasis;//成人基础舱位
	private String infantfarebasis;//婴儿
	private String childfarebasis;//儿童
	private String farereference;
	private String childfarereference;
	private String infantfarereference;
	/**山东航空需要用到的参数↓*/
	private String dlyId;
	private String pakId;
	private String departuredatetime;
	private String arrivaldatetime;
	private String arrivaldatetimethirty;
	private String hexstr;
	private String signa;
	private String flightinfo;
	private String group;
	private String groupCode;
	/**山东航空需要用到的参数↑*/
	/**
	 * 年龄限制范围
	 */
	private String ageArea;
	/**
	 * 备注字段
	 */
	private String comment;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCabinCode() {
		return cabinCode;
	}

	public void setCabinCode(String cabinCode) {
		this.cabinCode = cabinCode;
	}

	public String getLastSeat() {
		return lastSeat;
	}

	public void setLastSeat(String lastSeat) {
		this.lastSeat = lastSeat;
	}

	public String getBaseCabin() {
		return baseCabin;
	}

	public void setBaseCabin(String baseCabin) {
		this.baseCabin = baseCabin;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}

	public String getOrigPrice() {
		return origPrice;
	}

	public void setOrigPrice(String origPrice) {
		this.origPrice = origPrice;
	}

	public String getRefundMessage() {
		return refundMessage;
	}

	public void setRefundMessage(String refundMessage) {
		this.refundMessage = refundMessage;
	}

	public String getChangeMessage() {
		return changeMessage;
	}

	public void setChangeMessage(String changeMessage) {
		this.changeMessage = changeMessage;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getCardPrice() {
		return cardPrice;
	}

	public void setCardPrice(String cardPrice) {
		this.cardPrice = cardPrice;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getLuggagecode() {
		return luggagecode;
	}

	public void setLuggagecode(String luggagecode) {
		this.luggagecode = luggagecode;
	}

	public String getAdultprice() {
		return adultprice;
	}

	public void setAdultprice(String adultprice) {
		this.adultprice = adultprice;
	}

	public String getInfantprice() {
		return infantprice;
	}

	public void setInfantprice(String infantprice) {
		this.infantprice = infantprice;
	}

	public String getChildprice() {
		return childprice;
	}

	public void setChildprice(String childprice) {
		this.childprice = childprice;
	}

	public String getAdultfarebasis() {
		return adultfarebasis;
	}

	public void setAdultfarebasis(String adultfarebasis) {
		this.adultfarebasis = adultfarebasis;
	}

	public String getInfantfarebasis() {
		return infantfarebasis;
	}

	public void setInfantfarebasis(String infantfarebasis) {
		this.infantfarebasis = infantfarebasis;
	}

	public String getChildfarebasis() {
		return childfarebasis;
	}

	public void setChildfarebasis(String childfarebasis) {
		this.childfarebasis = childfarebasis;
	}

	public String getFarereference() {
		return farereference;
	}

	public void setFarereference(String farereference) {
		this.farereference = farereference;
	}

	public String getChildfarereference() {
		return childfarereference;
	}

	public void setChildfarereference(String childfarereference) {
		this.childfarereference = childfarereference;
	}

	public String getInfantfarereference() {
		return infantfarereference;
	}

	public void setInfantfarereference(String infantfarereference) {
		this.infantfarereference = infantfarereference;
	}

	public String getDlyId() {
		return dlyId;
	}

	public void setDlyId(String dlyId) {
		this.dlyId = dlyId;
	}

	public String getPakId() {
		return pakId;
	}

	public void setPakId(String pakId) {
		this.pakId = pakId;
	}

	public String getDeparturedatetime() {
		return departuredatetime;
	}

	public void setDeparturedatetime(String departuredatetime) {
		this.departuredatetime = departuredatetime;
	}

	public String getArrivaldatetime() {
		return arrivaldatetime;
	}

	public void setArrivaldatetime(String arrivaldatetime) {
		this.arrivaldatetime = arrivaldatetime;
	}

	public String getArrivaldatetimethirty() {
		return arrivaldatetimethirty;
	}

	public void setArrivaldatetimethirty(String arrivaldatetimethirty) {
		this.arrivaldatetimethirty = arrivaldatetimethirty;
	}

	public String getHexstr() {
		return hexstr;
	}

	public void setHexstr(String hexstr) {
		this.hexstr = hexstr;
	}

	public String getSigna() {
		return signa;
	}

	public void setSigna(String signa) {
		this.signa = signa;
	}

	public String getFlightinfo() {
		return flightinfo;
	}

	public void setFlightinfo(String flightinfo) {
		this.flightinfo = flightinfo;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getGroupCode() {
		return groupCode;
	}

	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}

	public String getAgeArea() {
		return ageArea;
	}

	public void setAgeArea(String ageArea) {
		this.ageArea = ageArea;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}
