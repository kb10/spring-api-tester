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

	@Test
	public void testSimpleClassExtractionExpectManyCallsSuccess() {
		SpringAnnotationCrawler sac = new SpringAnnotationCrawler();
		List<Object> controllers = new ArrayList<Object>();
		controllers.add(new TestController3());
		List<ApiCall> calls = sac.scanControllers(controllers);
		assertNotNull(calls);
		assertEquals(6, calls.size());
		assertEquals("/a/x", calls.get(0).getFullPath());
		assertEquals("/a/y", calls.get(1).getFullPath());
		assertEquals("/a/z", calls.get(2).getFullPath());
		assertEquals("/b/x", calls.get(3).getFullPath());
		assertEquals("/b/y", calls.get(4).getFullPath());
		assertEquals("/b/z", calls.get(5).getFullPath());
	}
	
	
	
}
