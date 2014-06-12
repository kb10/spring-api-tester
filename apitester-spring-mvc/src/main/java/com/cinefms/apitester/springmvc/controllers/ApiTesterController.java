package com.cinefms.apitester.springmvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.cinefms.apitester.core.ApitesterService;

@Controller
@RequestMapping(value={"/a","/b/"})
public class ApiTesterController {

	@Autowired
	private ApitesterService apitesterService;

	public ApitesterService getApitesterService() {
		return apitesterService;
	}

	public void setApitesterService(ApitesterService apitesterService) {
		this.apitesterService = apitesterService;
	}
	
	@RequestMapping(value={"/x","/y"})
	@ResponseBody
	@ResponseStatus(value=HttpStatus.OK)
	public void aaa() {
		
	}
	
}
