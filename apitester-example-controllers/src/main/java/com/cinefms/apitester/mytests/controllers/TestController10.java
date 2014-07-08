package com.cinefms.apitester.mytests.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value={"/aaa/{aaaId}"})
public class TestController10 {

	@RequestMapping(value={"/x/{id}"},method=RequestMethod.GET)
	@ResponseBody
	public void getBasic() {
	}
	
}
