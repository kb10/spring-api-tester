package com.cinefms.apitester.springboot.example.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cinefms.apitester.springboot.example.entities.SomeEntity;

@RestController
@RequestMapping(value="/test")
public class TestController {
	private static Log log = LogFactory.getLog(TestController.class);

	@RequestMapping(value="/hello_world",method=RequestMethod.GET)
	public String helloWorld(@RequestParam String in) {
		return "hello, "+in+"!";
	}

	@RequestMapping(value="/entities/{xx}",method=RequestMethod.GET)
	public SomeEntity entity(@PathVariable String xx) {
		return new SomeEntity();
	}

 	@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE,value="",headers = "content-type=multipart/*", method=RequestMethod.POST)
 	public void uploadFileTest(@RequestParam MultipartFile file){
 		System.out.println("====START TO PARSE THE FILE========");
 	}
	@RequestMapping(value="/aa", method=RequestMethod.POST)
 	public String testPost(@RequestParam String a){
		if(a!=null){
			System.out.println("value of a"+a);
		}
 		return "the value of a is , "+a+"!";
 	}
//	,headers = "content-type=multipart/*"
	@RequestMapping(value="/import1", method=RequestMethod.POST,consumes = {
            "multipart/form-data", MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public void import1(@RequestPart(value = "file") MultipartFile file,@RequestPart String test ){
			log.info("Uploading file: " + file.getName());
			log.info("Uploading origin file: " + file.getOriginalFilename());
			System.out.println("======"+test+"=========");
	}	
	@RequestMapping(value="/import2", method=RequestMethod.POST,consumes = {
            "multipart/form-data", MediaType.APPLICATION_JSON_VALUE })
	@ResponseBody
	public void import2(@RequestParam(value = "file") MultipartFile file,@RequestParam String test ){
			log.info("Uploading file: " + file.getName());
			log.info("Uploading origin file: " + file.getOriginalFilename());
			System.out.println("======"+test+"=========");
	}
}
