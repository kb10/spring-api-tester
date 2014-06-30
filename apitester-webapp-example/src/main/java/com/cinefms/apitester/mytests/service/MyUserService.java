package com.cinefms.apitester.mytests.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cinefms.apitester.mytests.model.TestUser;

@Component
public class MyUserService implements IMyUserService {

	@Override
	public TestUser createUser(TestUser user) {
		return new TestUser();
	}

	@Override
	public boolean deleteUser(int id) {
		return false;
	}

	@Override
	public List<TestUser> listUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> listUsersOptions() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TestUser updateUser(int id, TestUser user) {
		// TODO Auto-generated method stub
		return null;
	}

}
