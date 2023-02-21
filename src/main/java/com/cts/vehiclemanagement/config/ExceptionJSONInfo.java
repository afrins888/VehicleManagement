package com.cts.vehiclemanagement.config;

public class ExceptionJSONInfo {
	
	private String url;
	
	private String message;
	
	private String errorCode;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	@Override
	public String toString() {
		return "ExceptionJSONInfo [url=" + url + ", message=" + message + ", errorCode=" + errorCode + "]";
	}

}
