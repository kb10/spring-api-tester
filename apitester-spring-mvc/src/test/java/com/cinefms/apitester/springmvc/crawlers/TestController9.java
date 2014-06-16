package com.cinefms.apitester.springmvc.crawlers;


import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value={"/aaa/{aaaId}"})
public class TestController9 {

	@RequestMapping(value={"/x/{id}"},method=RequestMethod.GET)
	@ResponseBody
	public List<String> getBasic() {
		return null;
	}
	
}
