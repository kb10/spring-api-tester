package com.cinefms.apitester.springmvc.crawlers.handler_mapping;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

public abstract class AbstractController {
	
	@RequestMapping(value="/xxxx/abstract/{id}",method=RequestMethod.GET)
	public int getAbstract(@PathVariable String id, @RequestParam List<String> names) {
		return 0;
	}

}
