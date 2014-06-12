package com.cinefms.apitester.springmvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Test;

import com.cinefms.apitester.model.info.ApiCall;

public class SpringAnnotationCrawlerTest {

	@Test
	public void testPathCanonicalization() {
		SpringAnnotationCrawler sac = new SpringAnnotationCrawler();
		Map<String,String> beforeAndAfter = new HashMap<String, String>();
		beforeAndAfter.put("//a/{a}/xcd", "/a/{a}/xcd");
		beforeAndAfter.put("/b/a//{a}/xcd", "/b/a/{a}/xcd");
		for(Map.Entry<String, String> e : beforeAndAfter.entrySet()) {
			assertEquals(e.getValue(), sac.getPath(e.getKey()));
		}
	}

	@Test
	public void testBasePathCanonicalization() {
		SpringAnnotationCrawler sac = new SpringAnnotationCrawler();
		Map<String,String> beforeAndAfter = new HashMap<String, String>();
		beforeAndAfter.put("//a/{a}/xcd", "/a");
		beforeAndAfter.put("/b/a//{a}/xcd", "/b/a");
		beforeAndAfter.put("/b/a//", "/b/a/");
		beforeAndAfter.put("/b/a//x", "/b/a/x");
		for(Map.Entry<String, String> e : beforeAndAfter.entrySet()) {
			assertEquals(e.getValue(), sac.getBasePath(e.getKey()));
		}
	}

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
		assertEquals("/blah", calls.get(0).getBasePath());
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
	
	@Test
	public void testPathParametersExpectTwoSuccess() {
		SpringAnnotationCrawler sac = new SpringAnnotationCrawler();
		List<Object> controllers = new ArrayList<Object>();
		controllers.add(new TestController4());
		List<ApiCall> calls = sac.scanControllers(controllers);
		assertNotNull(calls);
		assertEquals(1, calls.size());
		assertEquals("/x", calls.get(0).getBasePath());
		assertEquals("/x/{id}/sub/{value}/xxx/{a}", calls.get(0).getFullPath());
		assertEquals(3, calls.get(0).getPathParameters().size());
		// first param
		assertEquals(String.class.getCanonicalName(), calls.get(0).getPathParameters().get(0).getParameterType().getClassName());

		// second param
		assertEquals(String.class.getCanonicalName(), calls.get(0).getPathParameters().get(1).getParameterType().getClassName());
		assertEquals(true, calls.get(0).getPathParameters().get(1).isCollection());
		assertEquals(null, calls.get(0).getPathParameters().get(1).getDefaultValue());
		assertEquals("value", calls.get(0).getPathParameters().get(1).getParameterName());

		// thrid param
		assertEquals("int", calls.get(0).getPathParameters().get(2).getParameterType().getClassName());
		assertEquals(false, calls.get(0).getPathParameters().get(2).isCollection());
		assertEquals(null, calls.get(0).getPathParameters().get(2).getDefaultValue());
		assertEquals("a", calls.get(0).getPathParameters().get(2).getParameterName());
	}
	
	@Test
	public void testClassLevelAnnotationExpectTwoSuccess() {
		SpringAnnotationCrawler sac = new SpringAnnotationCrawler();
		List<Object> controllers = new ArrayList<Object>();
		controllers.add(new TestController5());
		controllers.add(new TestController6());
		List<ApiCall> calls = sac.scanControllers(controllers);
		assertNotNull(calls);
		assertEquals(2, calls.size());
		assertEquals("/aaa/x/{id}", calls.get(0).getFullPath());
		assertEquals("/aaa/x", calls.get(0).getBasePath());
		assertEquals("/aaa/{aaaId}/x/{id}", calls.get(1).getFullPath());
		assertEquals("/aaa", calls.get(1).getBasePath());
	}
	
	
	
}
