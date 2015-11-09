package com.cinefms.apitester.springmvc.crawlers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.cinefms.apitester.model.ApiCrawler;
import com.cinefms.apitester.model.info.ApiCall;

public class SpringRequestMappingCrawler implements ApiCrawler {

	@Autowired
	private RequestMappingHandlerMapping handlerMapping; 
	
	public SpringRequestMappingCrawler() {
	}
	
	public SpringRequestMappingCrawler(ApplicationContext ac) {
		handlerMapping = ac.getBean(RequestMappingHandlerMapping.class);
	}
	
	
	@Override
	public List<ApiCall> getApiCalls() {
		Map<RequestMappingInfo,HandlerMethod> m = handlerMapping.getHandlerMethods();
		System.err.println("number of mapped methods: "+m.size());
		for(Map.Entry<RequestMappingInfo, HandlerMethod> e : m.entrySet()) {
			System.err.println(e.getValue().getMethod());
			System.err.println(e.getKey().getPatternsCondition());
			for(MethodParameter mp : e.getValue().getMethodParameters()) {
				System.err.println(" ----> "+mp.getParameterName()+" / "+mp.getParameterType()+" / "+mp.getTypeIndexForCurrentLevel());
				System.err.println(" ----> "+mp.getGenericParameterType());
				System.err.println(" ----> "+mp.toString());
			}
		}
		return null;
	}

	
	
	
}
