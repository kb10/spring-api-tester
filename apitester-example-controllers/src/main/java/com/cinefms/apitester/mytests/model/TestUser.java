package com.cinefms.apitester.mytests.model;

import com.cinefms.apitester.annotations.ApiDescription;

@ApiDescription(file="TestUser.md")
public class TestUser {
	private String name;
	private String email;

	public TestUser() {
	}
	
	public TestUser(String name, String email) {
		this.name = name;
		this.email = email;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
