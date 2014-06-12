package com.cinefms.apitester.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import com.cinefms.apitester.model.ApiCrawler;
import com.cinefms.apitester.model.info.ApiCall;

@Component
public class ApitesterService implements ApplicationContextAware {
	
	private List<ApiCall> calls;
	private ApplicationContext applicationContext;

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
	private List<ApiCall> getCallsInternal() {
		if(calls==null) {
			calls = new ArrayList<ApiCall>();
			for(ApiCrawler ac : applicationContext.getBeansOfType(ApiCrawler.class).values()) {
				calls.addAll(ac.getApiCalls());
			}
			Collections.sort(calls,new Comparator<ApiCall>() {

				@Override
				public int compare(ApiCall o1, ApiCall o2) {
					return o1.getFullPath().compareTo(o2.getFullPath());
				}
				
			});
		}
		return calls;
	}

	public List<ApiCall> getCalls(boolean includeDeprecated, String searchTerm) {
		List<ApiCall> out = new ArrayList<ApiCall>();
		for(ApiCall ac : getCallsInternal()) {
			if(!includeDeprecated && ac.isDeprecated()) {
				continue;
			}
			if(searchTerm!=null && !ac.getFullPath().toLowerCase().contains(searchTerm.toLowerCase())) {
				continue;
			}
			out.add(ac);
		}
		return out;
	}
	
	

}
