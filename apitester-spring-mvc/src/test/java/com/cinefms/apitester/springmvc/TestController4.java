package com.cinefms.apitester.springmvc;


import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public class TestController4 {

	@RequestMapping(value={"/x/{id}/sub/{value}/xxx/{a}"},method=RequestMethod.GET)
	@ResponseBody
	public void getBasic(String x, @PathVariable String id, @PathVariable(value="value") List<String> hund, @PathVariable int a) {
		
	}
	
}
