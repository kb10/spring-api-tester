package com.cinefms.apitester.model.info;

import java.util.ArrayList;
import java.util.List;

public class ApiCall {
	
	private String basePath;
	private String fullPath;
	private String description;
	private String method;

	private String handlerClass;
	private String handlerMethod;
	
	private String acceptMimeType;
	private String returnsMimeType;
	
	private String since;
	private String deprecatedSince;

	private boolean deprecated;
	
	private List<ApiCallParameter> pathParameters = new ArrayList<ApiCallParameter>();
	private List<ApiCallParameter> requestParameters = new ArrayList<ApiCallParameter>();
	
	private ApiResult returnType;
	
	private List<ApiResult> errorReturnTypes = new ArrayList<ApiResult>();
	
	public ApiCall() {
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getHandlerClass() {
		return handlerClass;
	}

	public void setHandlerClass(String handlerClass) {
		this.handlerClass = handlerClass;
	}

	public String getHandlerMethod() {
		return handlerMethod;
	}

	public void setHandlerMethod(String handlerMethod) {
		this.handlerMethod = handlerMethod;
	}

	public String getAcceptMimeType() {
		return acceptMimeType;
	}

	public void setAcceptMimeType(String acceptMimeType) {
		this.acceptMimeType = acceptMimeType;
	}

	public String getReturnsMimeType() {
		return returnsMimeType;
	}

	public void setReturnsMimeType(String returnsMimeType) {
		this.returnsMimeType = returnsMimeType;
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

	public boolean isDeprecated() {
		return deprecated;
	}

	public void setDeprecated(boolean deprecated) {
		this.deprecated = deprecated;
	}

	public List<ApiCallParameter> getPathParameters() {
		return pathParameters;
	}

	public void setPathParameters(List<ApiCallParameter> pathParameters) {
		this.pathParameters = pathParameters;
	}

	public List<ApiCallParameter> getRequestParameters() {
		return requestParameters;
	}

	public void setRequestParameters(List<ApiCallParameter> requestParameters) {
		this.requestParameters = requestParameters;
	}

	public ApiResult getReturnType() {
		return returnType;
	}

	public void setReturnType(ApiResult returnType) {
		this.returnType = returnType;
	}

	public List<ApiResult> getErrorReturnTypes() {
		return errorReturnTypes;
	}

	public void setErrorReturnTypes(List<ApiResult> errorReturnTypes) {
		this.errorReturnTypes = errorReturnTypes;
	}

}
