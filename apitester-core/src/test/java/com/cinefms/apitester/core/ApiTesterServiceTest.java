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
		
		when(aCrawl.getApiCalls()).thenReturn(calls);

		Map<String,ApiCrawler> crawlers = new HashMap<String,ApiCrawler>();
		crawlers.put("a",aCrawl);
		
		ApplicationContext ac = mock(ApplicationContext.class);
		when(ac.getBeansOfType(any(Class.class))).thenReturn(crawlers);
		
		ApitesterService as = new ApitesterService();
		as.setApplicationContext(ac);
		List<ApiCall> callsOut;
		callsOut = as.getCalls(true, null);
		assertEquals(4, callsOut.size());
		assertEquals("/a", callsOut.get(0).getFullPath());
		assertEquals("/b", callsOut.get(1).getFullPath());
		assertEquals("/c", callsOut.get(2).getFullPath());
		assertEquals("/d", callsOut.get(3).getFullPath());
		callsOut = as.getCalls(false, null);
		assertEquals(2, callsOut.size());
		callsOut = as.getCalls(true, "a");
		assertEquals(1, callsOut.size());
		callsOut = as.getCalls(false, "a");
		assertEquals(0, callsOut.size());
		
	}
	
}
