package com.cinefms.apitester.springmvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cinefms.apitester.model.info.ApiCall;

public class SpringAnnotationCrawlerTest {

	@Test
	public void testSimpleClassExtractionExpectEmptyListSuccess() {
		SpringAnnotationCrawler sac = new SpringAnnotationCrawler();
		List<Object> controllers = new ArrayList<Object>();
		controllers.add(new TestController1());
		List<ApiCall> calls = sac.scanControllers(controllers);
		assertNotNull(calls);
		assertEquals(0, calls.size());
	}

	@Test
	public void testSimpleClassExtractionExpectSingleCallsSuccess() {
		SpringAnnotationCrawler sac = new SpringAnnotationCrawler();
		List<Object> controllers = new ArrayList<Object>();
		controllers.add(new TestController2());
		List<ApiCall> calls = sac.scanControllers(controllers);
		assertNotNull(calls);
		assertEquals(1, calls.size());
		assertEquals("/blah", calls.get(0).getFullPath());
		assertEquals("", calls.get(0).getBasePath());
		assertEquals("GET", calls.get(0).getMethod());
	}

	
	
}
