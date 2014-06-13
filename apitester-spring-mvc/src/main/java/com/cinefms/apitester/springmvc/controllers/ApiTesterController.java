package com.cinefms.apitester.springmvc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.apitester.core.ApitesterService;
import com.cinefms.apitester.model.info.ApiCall;

@Controller
@RequestMapping(value={""})
public class ApiTesterController {

	@Autowired
	private ApitesterService apitesterService;

	public ApitesterService getApitesterService() {
		return apitesterService;
	}

	public void setApitesterService(ApitesterService apitesterService) {
		this.apitesterService = apitesterService;
	}

	@RequestMapping(value="/basepaths",method=RequestMethod.GET)
	@ResponseBody
	public List<String> getBasePaths(
			@RequestParam(defaultValue="true") boolean includeDeprecated) {
		return apitesterService.getBasePaths(includeDeprecated);
	}

	@RequestMapping(value="/calls",method=RequestMethod.GET)
	@ResponseBody
	public List<ApiCall> getCalls(
			@RequestParam(required=false,defaultValue="true") boolean includeDeprecated, 
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false,value="method") String[] requestMethods) {
		return apitesterService.getCalls(includeDeprecated, searchTerm, requestMethods);
	}
	
	
	
	
}
