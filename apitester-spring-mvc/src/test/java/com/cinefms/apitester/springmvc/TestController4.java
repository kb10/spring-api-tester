package com.cinefms.apitester.springmvc;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

public class TestController4 {

	@RequestMapping(value={"/x/{id}/sub/{value}"},method=RequestMethod.GET)
	public void getBasic(String x, @PathVariable String id, @PathVariable int value) {
		
	}
	
}
