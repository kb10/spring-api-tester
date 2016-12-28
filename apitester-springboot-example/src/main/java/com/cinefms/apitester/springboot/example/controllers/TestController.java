package com.cinefms.apitester.springboot.example.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cinefms.apitester.springboot.example.entities.SomeEntity;

@RestController
@RequestMapping(value="/test")
public class TestController {

	@RequestMapping(value="/hello_world",method=RequestMethod.GET)
	public String helloWorld(@RequestParam String in) {
		return "hello, "+in+"!";
	}

	@RequestMapping(value="/entities/{xx}",method=RequestMethod.GET)
	public SomeEntity entity(@PathVariable String xx) {
		return new SomeEntity();
	}

 	@RequestMapping(value="",headers = "content-type=multipart/*", method=RequestMethod.POST)
 	public void uploadFileTest(@RequestParam MultipartFile file){
 		System.out.println("====START TO PARSE THE FILE========");
 	}
	
}
