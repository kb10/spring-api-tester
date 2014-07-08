package com.cinefms.apitester.mytests.service;

import java.util.List;

import com.cinefms.apitester.mytests.model.TestUser;

public interface IMyUserService {

	public TestUser createUser(TestUser user);

	public boolean deleteUser(int id);

	public List<TestUser> listUsers();

	public List<String> listUsersOptions();

	public TestUser updateUser(int id, TestUser user);

}