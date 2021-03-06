package com.cinefms.apitester.model.info;

public class ApiCallParameter {

	public static enum Type {
		BODY, PATH, REQUEST
	};
	
	
	private Type type; 
	
	private String parameterName;
	private ApiObject parameterType;

	private String description;
	private String defaultValue;
	private String format;
	
	private String since;
	private String deprecatedSince;
	
	private boolean mandatory;
	private boolean deprecated;
	
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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return getParameterName()+" ["+(isMandatory()?"*":" ")+"] "+getParameterType().getClassName();
	}
	

}
