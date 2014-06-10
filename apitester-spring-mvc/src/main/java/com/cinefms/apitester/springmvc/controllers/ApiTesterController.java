package com.cinefms.apitester.springmvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cinefms.apitester.core.ApitesterService;

@Controller
@RequestMapping(value="/apitester")
public class ApiTesterController {

	@Autowired
	private ApitesterService apitesterService;

	public ApitesterService getApitesterService() {
		return apitesterService;
	}

	public void setApitesterService(ApitesterService apitesterService) {
		this.apitesterService = apitesterService;
	}
	
	@RequestMapping(value="/demo",method=RequestMethod.GET)
	public String getDemo() {
		return "hello world!";
	}
	
	
}
