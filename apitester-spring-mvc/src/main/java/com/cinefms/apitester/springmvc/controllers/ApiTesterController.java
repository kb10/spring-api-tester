package com.cinefms.apitester.springmvc.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.apitester.core.ApitesterService;

@Controller
public class ApiTesterController {

	@Autowired
	private ApitesterService apitesterService;

	public ApitesterService getApitesterService() {
		return apitesterService;
	}

	public void setApitesterService(ApitesterService apitesterService) {
		this.apitesterService = apitesterService;
	}
	
	@RequestMapping(value="/demo",method=RequestMethod.GET,produces={"application/json"})
	@ResponseBody
	public Map<String,String> getDemo() {
		Map<String,String> out = new HashMap<String, String>();
		out.put("message", "hello world!");
		return out;
	}
	
	
	
}
