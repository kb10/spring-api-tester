package com.cinefms.apitester.mytests.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cinefms.apitester.mytests.model.TestUser;

@Component
public class MyUserService implements IMyUserService {

	@Override
	public TestUser createUser(TestUser user) {
		return new TestUser("NewUser", "newuser@111.com");
	}

	@Override
	public boolean deleteUser(int id) {
		return false;
	}

	@Override
	public List<TestUser> listUsers() {
		TestUser userA = new TestUser("Tom", "tom@crm-factory.com.cn");
		TestUser userB = new TestUser("Big", "big@mcon.net");
		List<TestUser> list = new ArrayList<TestUser>();
		list.add(userA);
		list.add(userB);
		return list;
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
