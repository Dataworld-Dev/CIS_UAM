package com.dw.ngms.cis.uam.enums;

public enum Status {

	Y("Active"),
	N("Inactive");

	String status;
	
	private Status(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
