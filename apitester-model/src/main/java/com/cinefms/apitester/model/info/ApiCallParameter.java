package com.cinefms.apitester.model.info;

public class ApiCallParameter {
	
	private String parameterName;
	private ApiObject parameterType;

	private String description;
	private String defaultValue;
	private String format;
	
	private String since;
	private String deprecatedSince;
	
	private boolean mandatory;
	private boolean deprecated;
	private boolean collection;
	
	public ApiCallParameter() {
	}
	
	public String getParameterName() {
		return parameterName;
	}
	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}
	public ApiObject getParameterType() {
		return parameterType;
	}
	public void setParameterType(ApiObject parameterType) {
		this.parameterType = parameterType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getSince() {
		return since;
	}
	public void setSince(String since) {
		this.since = since;
	}
	public String getDeprecatedSince() {
		return deprecatedSince;
	}
	public void setDeprecatedSince(String deprecatedSince) {
		this.deprecatedSince = deprecatedSince;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public boolean isDeprecated() {
		return deprecated;
	}
	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public boolean isCollection() {
		return collection;
	}

	public void setCollection(boolean collection) {
		this.collection = collection;
	}
	

}
