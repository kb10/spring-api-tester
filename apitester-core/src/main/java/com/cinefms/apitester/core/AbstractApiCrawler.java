package com.cinefms.apitester.core;

import java.util.List;

import com.cinefms.apitester.model.ApiCrawler;
import com.cinefms.apitester.model.info.ApiCall;

public abstract class AbstractApiCrawler implements ApiCrawler {
	
	public List<ApiCall> getApiCalls() {
		return null;
	}

}
