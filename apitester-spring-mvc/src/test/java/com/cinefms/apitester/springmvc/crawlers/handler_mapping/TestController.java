package com.cinefms.apitester.springmvc.crawlers.handler_mapping;


import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cinefms.apitester.annotations.ApiDescription;

@RestController
@RequestMapping(value={"/aaa/{aaaId}"})
public class TestController extends AbstractController implements IController {
	
	public TestController() {
		System.err.println("instantiated: "+getClass());
	}
	

	@ApiDescription(file="controller_12_description_a.txt")
	public Map<String,Object> getBasic1(String id) {
		return null;
	}

	@RequestMapping(value={"/b/{id}"},method=RequestMethod.GET)
	@ResponseBody
	@ApiDescription(file="/controller_12_description_b.txt")
	public Map<String,Object> getBasic2() {
		return null;
	}

	@RequestMapping(value={"/c/{id}"},method=RequestMethod.GET)
	@ResponseBody
	@ApiDescription("returns the A")
	public Map<String,Object> getBasic3() {
		return null;
	}

	@RequestMapping(value={"/d/{id}"},method=RequestMethod.GET)
	@ResponseBody
	@ApiDescription(deprecatedSince="1.22",since="0.9")
	public Map<String,Object> getBasic4() {
		return null;
	}
	
	@RequestMapping(value={"/d/{id}"},method=RequestMethod.PUT)
	@ResponseBody
	@Deprecated
	public Map<String,Object> getBasic5() {
		return null;
	}
	
	
}
