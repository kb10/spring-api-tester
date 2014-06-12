package com.cinefms.apitester.model.info;

public class ApiResult {

	private ApiObject returnClass;
	private String exception;
	private String status;
	private int statusCode;
	private boolean collection;

	public ApiObject getReturnClass() {
		return returnClass;
	}

	public void setReturnClass(ApiObject returnClass) {
		this.returnClass = returnClass;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public boolean isCollection() {
		return collection;
	}

	public void setCollection(boolean collection) {
		this.collection = collection;
	}

}
