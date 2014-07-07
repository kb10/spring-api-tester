package com.cinefms.apitester.mytests.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.apitester.annotations.ApiDescription;
import com.cinefms.apitester.mytests.model.TestUser;
import com.cinefms.apitester.mytests.service.IMyUserService;

@Controller
@RequestMapping(value={"/mytests"})
public class MyUserController {

	@Autowired
	private IMyUserService myUserService;

	public MyUserController() {
	}
	
	@RequestMapping(value="/users",method=RequestMethod.POST,produces={"application/json"})
	@ResponseBody
	public TestUser createUser(@RequestBody TestUser newUser) {
		return myUserService.createUser(newUser);
	}

	@RequestMapping(value="/users/{id}",method=RequestMethod.DELETE,produces={"application/json"})
	@ResponseBody
	@ApiDescription(deprecatedSince="Friday June 27, 2014")
	public boolean deleteUser(@PathVariable int id) {
		return myUserService.deleteUser(id);
	}

	@RequestMapping(value="/users",method=RequestMethod.GET,produces={"application/json"})
	@ResponseBody
	public List<TestUser> listUsers() {
		return myUserService.listUsers();
	}

	@RequestMapping(value="/users",method=RequestMethod.OPTIONS,produces={"application/json"})
	@ResponseBody
	public List<String> listUsersOptions() {
		return myUserService.listUsersOptions();
	}

	@RequestMapping(value="/users/{id}",method=RequestMethod.PUT,produces={"application/json"})
	@ResponseBody
	public TestUser updateUser(@PathVariable int id, @RequestBody TestUser newUser) {
		return myUserService.updateUser(id, newUser);
	}
	
	
}
