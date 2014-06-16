package com.cinefms.apitester.model.info;

public class ApiObject implements Comparable<ApiObject> {
	
	private String className;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof ApiObject)) {
			return false;
		}
		return ((ApiObject)obj).getClassName().equals(getClassName());
	}

	@Override
	public int compareTo(ApiObject arg) {
		return getClassName().compareTo(arg.getClassName());
	}
	
}
