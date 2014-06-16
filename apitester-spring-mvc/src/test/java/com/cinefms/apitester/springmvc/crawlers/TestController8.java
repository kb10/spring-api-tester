package com.cinefms.apitester.springmvc.crawlers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value={"/aaa/{aaaId}"})
public class TestController8 {

	@RequestMapping(value={"/x/{id}"},method=RequestMethod.GET)
	@ResponseBody
	public String getBasic() {
		return null;
	}
	
}
