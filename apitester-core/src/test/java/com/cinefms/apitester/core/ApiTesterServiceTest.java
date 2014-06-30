package com.cinefms.apitester.core;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.cinefms.apitester.model.ApiCrawler;
import com.cinefms.apitester.model.info.ApiCall;

public class ApiTesterServiceTest {

	@Test
	public void testCollectApiCallsExpectSuccess() {
		ApiCrawler aCrawl = mock(ApiCrawler.class);
		List<ApiCall> calls = new ArrayList<ApiCall>();
		for(String s : new String[] {"/a","/c","/d","/b"}) {
			ApiCall ac = new ApiCall();
			ac.setBasePath(s);
			ac.setFullPath(s);
			calls.add(ac);
		}
		calls.get(0).setDeprecated(true);
		calls.get(3).setDeprecated(true);

		calls.get(0).setMethod("GET");
		calls.get(1).setMethod("PUT");
		calls.get(2).setMethod("POST");
		calls.get(3).setMethod("OPTIONS");
		
		ApitesterService as = new ApitesterService();
		as.registerCalls(calls);
		List<ApiCall> callsOut;
		callsOut = as.getCalls(null,null,true, null,null);
		assertEquals(4, callsOut.size());
		assertEquals("/a", callsOut.get(0).getFullPath());
		assertEquals("/b", callsOut.get(1).getFullPath());
		assertEquals("/c", callsOut.get(2).getFullPath());
		assertEquals("/d", callsOut.get(3).getFullPath());
		callsOut = as.getCalls(null,null,false, null,null);
		assertEquals(2, callsOut.size());
		callsOut = as.getCalls(null,null,true, "a",null);
		assertEquals(1, callsOut.size());
		callsOut = as.getCalls(null,null,false, "a",null);
		assertEquals(0, callsOut.size());
		callsOut = as.getCalls(null,null,true,null,new String[] {"DELETE"});
		assertEquals(0, callsOut.size());
		callsOut = as.getCalls(null,null,true,null,new String[] {"OPTIONS"});
		assertEquals(1, callsOut.size());
		
	}
	
	@Test
	public void testCollectApiCallsExpectBasePaths() {
		List<ApiCall> calls = new ArrayList<ApiCall>();
		for(String[] s : new String[][] {
				{"/a","/a/{id}"},
				{"/b","/b/{id}"},
				{"/a","/a/{id}/sub"},
			}) {
			ApiCall ac = new ApiCall();
			ac.setBasePath(s[0]);
			ac.setFullPath(s[1]);
			calls.add(ac);
		}

		ApitesterService as = new ApitesterService();
		as.registerCalls(calls);
		calls.get(0).setDeprecated(true);
		calls.get(1).setDeprecated(true);
		calls.get(2).setDeprecated(true);
		{
			List<String> basePaths = as.getBasePaths(null,true);
			assertEquals(2, basePaths.size());
		}
		calls.get(0).setDeprecated(false);
		calls.get(1).setDeprecated(false);
		calls.get(2).setDeprecated(true);
		{
			List<String> basePaths = as.getBasePaths(null,false);
			assertEquals(2, basePaths.size());
		}
		calls.get(0).setDeprecated(true);
		calls.get(1).setDeprecated(true);
		calls.get(2).setDeprecated(true);
		{
			List<String> basePaths = as.getBasePaths(null,false);
			assertEquals(0, basePaths.size());
		}
		calls.get(0).setDeprecated(true);
		calls.get(1).setDeprecated(false);
		calls.get(2).setDeprecated(true);
		{
			List<String> basePaths = as.getBasePaths(null,false);
			assertEquals(1, basePaths.size());
		}
	}
	
	@Test
	public void testCollectApiCallsExpectCorrectNameSpaces() {
		List<ApiCall> calls = new ArrayList<ApiCall>();
		for(String[] s : new String[][] {
				{"/a","/a/{id}","blah:foo"},
				{"/b","/b/{id}","blah:foo2"},
				{"/a","/a/{id}/sub","blah:foo3"},
				{"/a","/a/{id}/sub",null}
			}) {
			ApiCall ac = new ApiCall();
			ac.setBasePath(s[0]);
			ac.setFullPath(s[1]);
			ac.setNameSpace(s[2]);
			calls.add(ac);
		}
		
		ApitesterService as = new ApitesterService();
		as.registerCalls(calls);
		{
			List<String> basePaths = as.getBasePaths("blah:foo",false);
			assertEquals(1, basePaths.size());
		}
		{
			List<String> basePaths = as.getBasePaths("blah:foo3",false);
			assertEquals(1, basePaths.size());
		}
		{
			List<String> basePaths = as.getBasePaths("",false);
			assertEquals(1, basePaths.size());
		}
		{
			List<ApiCall> cs = as.getCalls("blah:foo",null,true,null,null);
			assertEquals(1, cs.size());
		}
		{
			List<ApiCall> cs = as.getCalls(null,"/a",true,null,null);
			assertEquals(3, cs.size());
		}
	}
	
}
