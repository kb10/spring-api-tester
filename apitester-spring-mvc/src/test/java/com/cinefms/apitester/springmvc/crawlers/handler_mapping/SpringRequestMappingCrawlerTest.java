package com.cinefms.apitester.springmvc.crawlers.handler_mapping;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cinefms.apitester.springmvc.crawlers.SpringRequestMappingCrawler;


public class SpringRequestMappingCrawlerTest {
	
	public ApplicationContext init(String configFile) {
		return new ClassPathXmlApplicationContext(configFile);
	}

	@Test
	public void testControllersPresent() {
		ApplicationContext ac = init("context_request_mapping_01.xml");
		SpringRequestMappingCrawler crwl = new SpringRequestMappingCrawler(ac);
		crwl.getApiCalls();
	}
	

}
