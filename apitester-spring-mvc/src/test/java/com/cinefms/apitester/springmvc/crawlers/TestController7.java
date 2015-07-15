package com.cinefms.apitester.springmvc.crawlers;


import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.apitester.annotations.ApiDescription;

@RequestMapping(value={"/aaa/{aaaId}"})
public class TestController7 {

	@RequestMapping(value={"/x/{id}"},method=RequestMethod.GET)
	@ResponseBody
	public void getBasic(String x, 

			@ApiDescription(format="dd-mm-yyyy",deprecatedSince="3.4")
			@RequestParam String id, 
			
			@ApiDescription(since="0.9")
			@RequestParam(required=false) String id2, 
			
			@RequestParam(required=false,defaultValue="aaa") boolean id3,
			
			@ApiDescription(value="",deprecatedSince="1.4")
			@RequestParam(required=false,value="ID",defaultValue="someDefault") int id4,
			
			@Deprecated
			@RequestParam(required=true,value="id") List<String> ids
			) {
		
	}
	
}
