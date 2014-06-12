package com.cinefms.apitester.springmvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cinefms.apitester.core.ApitesterService;

@Controller
@RequestMapping(value={"/api"})
public class ApiTesterController {

	@Autowired
	private ApitesterService apitesterService;

	public ApitesterService getApitesterService() {
		return apitesterService;
	}

	public void setApitesterService(ApitesterService apitesterService) {
		this.apitesterService = apitesterService;
	}
	
	
}
