package com.cinefms.apitester.mytests.controllers;


import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.apitester.model.info.ApiObject;

@RequestMapping(value={"/aaa/{aaaId}"})
public class TestController11 {

	@RequestMapping(value={"/x/{id}"},method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> getBasic(@RequestBody(required=false) ApiObject ao, @RequestBody(required=true) ApiObject ao2) {
		return null;
	}
	
}
