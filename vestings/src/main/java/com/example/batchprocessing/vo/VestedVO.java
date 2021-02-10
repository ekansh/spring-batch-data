package com.example.batchprocessing.vo;

import java.util.StringJoiner;

public class VestedVO {
	private String employeeId;
	private String employeeName;
	private String awardId;
	private Integer quantity;
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getAwardId() {
		return awardId;
	}
	public void setAwardId(String awardId) {
		this.awardId = awardId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	@Override
	public String toString() {
		StringJoiner stringJoiner = new StringJoiner(",").
				add(getEmployeeId())
				.add(getEmployeeName())
				.add(getAwardId())
				.add(getQuantity()+"");
		return stringJoiner.toString();
				//this.getEmployeeId()+","+this.getEmployeeName()+","+this.getAwardId()+","+this.getQuantity();
	}
}
