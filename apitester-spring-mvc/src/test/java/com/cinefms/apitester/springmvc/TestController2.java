package com.cinefms.apitester.springmvc;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


public class TestController2 {

	@RequestMapping(value="/blah",method=RequestMethod.GET)
	public void getBasic(String x) {
		
	}
	
}
