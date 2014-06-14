package com.cinefms.apitester.springmvc.crawlers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping(value={"/a","/b"},method=RequestMethod.GET)
public class TestController3 {

	@RequestMapping(value={"/x","/y","/z"},method=RequestMethod.GET)
	public void getBasic(String x) {
		
	}
	
}
