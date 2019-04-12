package com.feeye.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FlightInfo {
	
	private long id;
	private String fuelTax;
	private String AirportTax;
	private String flightNo;
	private List<CabinInfo> cabins = new ArrayList<CabinInfo>();

	private String deparutre;
	private String arrival;
	private String deparutreDate;
	private String deparutreTime;
	private String arriveDate;//用来存放到达日期
	private String arriveTime;
	private String webType;
	//工作时间
	private String workTime;
	//航班周期
	private String flightCycle;
	
	private String ip;
	private Date updateTime;
	private int flightHash;
	private String planeType;
	private String shareairflightNoIncluding;
	/**
	 * 共享航班
	 */
	private String shareFlightNo;
	//请求口令
	private String token;
	//请求参数
	private String parames;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFuelTax() {
		return fuelTax;
	}

	public void setFuelTax(String fuelTax) {
		this.fuelTax = fuelTax;
	}

	public String getAirportTax() {
		return AirportTax;
	}

	public void setAirportTax(String airportTax) {
		AirportTax = airportTax;
	}

	public String getFlightNo() {
		return flightNo;
	}

	public void setFlightNo(String flightNo) {
		this.flightNo = flightNo;
	}

	public String getDeparutre() {
		return deparutre;
	}

	public void setDeparutre(String deparutre) {
		this.deparutre = deparutre;
	}

	public String getArrival() {
		return arrival;
	}

	public void setArrival(String arrival) {
		this.arrival = arrival;
	}

	public String getDeparutreDate() {
		return deparutreDate;
	}

	public void setDeparutreDate(String deparutreDate) {
		this.deparutreDate = deparutreDate;
	}

	public String getDeparutreTime() {
		return deparutreTime;
	}

	public void setDeparutreTime(String deparutreTime) {
		this.deparutreTime = deparutreTime;
	}

	public String getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(String arriveDate) {
		this.arriveDate = arriveDate;
	}

	public String getArriveTime() {
		return arriveTime;
	}

	public void setArriveTime(String arriveTime) {
		this.arriveTime = arriveTime;
	}

	public String getWebType() {
		return webType;
	}

	public void setWebType(String webType) {
		this.webType = webType;
	}

	public String getWorkTime() {
		return workTime;
	}

	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}

	public String getFlightCycle() {
		return flightCycle;
	}

	public void setFlightCycle(String flightCycle) {
		this.flightCycle = flightCycle;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public int getFlightHash() {
		return flightHash;
	}

	public void setFlightHash(int flightHash) {
		this.flightHash = flightHash;
	}

	public String getPlaneType() {
		return planeType;
	}

	public void setPlaneType(String planeType) {
		this.planeType = planeType;
	}

	public String getShareairflightNoIncluding() {
		return shareairflightNoIncluding;
	}

	public void setShareairflightNoIncluding(String shareairflightNoIncluding) {
		this.shareairflightNoIncluding = shareairflightNoIncluding;
	}

	public String getShareFlightNo() {
		return shareFlightNo;
	}

	public void setShareFlightNo(String shareFlightNo) {
		this.shareFlightNo = shareFlightNo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getParames() {
		return parames;
	}

	public void setParames(String parames) {
		this.parames = parames;
	}

	public List<CabinInfo> getCabins() {
		return cabins;
	}

	public void setCabins(List<CabinInfo> cabins) {
		this.cabins = cabins;
	}
}
