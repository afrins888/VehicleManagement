package com.cts.vehiclemanagement.enums;

public enum HeaderMethodEnum {

	PUT("PUT"),
	DELETE("DELETE"),
	GET("GET"),
	POST("POST"),
	OPTIONS("OPTIONS")
	;
	private String codeDesc;

	HeaderMethodEnum(String desc) {
		this.codeDesc = desc;
	}

	public String getCodeDesc() {
		return codeDesc;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
}
