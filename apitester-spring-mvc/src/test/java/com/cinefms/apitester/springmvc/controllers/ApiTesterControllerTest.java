package com.cinefms.apitester.springmvc.controllers;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;

import com.cinefms.apitester.core.ApitesterService;


public class ApiTesterControllerTest {
	
	@Test
	public void testGetterSetter() {
		ApiTesterController atc = new ApiTesterController();
		assertNull(atc.getApitesterService());
		atc.setApitesterService(new ApitesterService());
		assertNotNull(atc.getApitesterService());
	}

}
