package com.cinefms.apitester.model.info;

public class ApiObject implements Comparable<ApiObject> {
	
	private String className;
	private String description;
	private boolean primitive;
	private boolean collection;

	public ApiObject() {
	}
	
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
		try {
			return getClassName().compareTo(arg.getClassName());
		} catch (Exception e) {
			System.err.println("unable to compare: "+getClassName()+" / "+arg.getClassName());
			return 0;
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isPrimitive() {
		return primitive;
	}

	public void setPrimitive(boolean primitive) {
		this.primitive = primitive;
	}

	public boolean isCollection() {
		return collection;
	}

	public void setCollection(boolean collection) {
		this.collection = collection;
	}
	
}
