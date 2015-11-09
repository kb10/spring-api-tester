package com.cinefms.apitester.springmvc.crawlers.handler_mapping;

import java.util.Map;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public interface IController {

	@RequestMapping(value={"/a/{id}"},method=RequestMethod.GET)
	@ResponseBody
	public abstract Map<String,Object> getBasic1(@PathVariable String id);

}
