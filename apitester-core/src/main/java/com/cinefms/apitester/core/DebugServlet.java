package com.cinefms.apitester.core;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public class DebugServlet extends HttpServlet {

	@Override
	public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
		res.setContentType("test/plain");
		StringBuffer sb = new StringBuffer();
		HttpServletRequest hreq = (HttpServletRequest)req;
		Enumeration<String> hNames = hreq.getHeaderNames(); 
		while(hNames.hasMoreElements()) {
			String s = hNames.nextElement();
			Enumeration<String> hValues = hreq.getHeaders(s);
			while(hValues.hasMoreElements()) {
				String v = hValues.nextElement();
				sb.append(s);
				sb.append(": ");
				sb.append(v);
				sb.append("\r\n");
			}
		}
		super.service(req, res);
	}

}
