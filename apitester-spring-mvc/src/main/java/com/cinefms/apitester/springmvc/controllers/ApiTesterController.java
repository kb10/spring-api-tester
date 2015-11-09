package com.cinefms.apitester.springmvc.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cinefms.apitester.annotations.ApiIgnore;
import com.cinefms.apitester.core.ApitesterService;
import com.cinefms.apitester.model.info.ApiCall;
import com.cinefms.apitester.model.info.ApiObject;

@Controller
@RequestMapping(value={"/apitester/api"})
@ApiIgnore
public class ApiTesterController {

	private static Log log = LogFactory.getLog(ApiTesterController.class);
	
	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private ApitesterService service;
	
	public ApiTesterController() {
	}
	

	@PostConstruct
	public void init() {
		log.info("################################################################ ");
		log.info("##");
		log.info("## ApiTesterController initialized: "+applicationContext);
		log.info("## ApitesterService is            : "+getService());
		log.info("##");
		log.info("################################################################ ");
	}
	
	
	@RequestMapping(value="/basepaths",method=RequestMethod.GET,produces={"application/json"})
	@ResponseBody
	public List<String> getBasePaths(
			@RequestParam(required=false) String context, 
			@RequestParam(defaultValue="true") boolean includeDeprecated
		) {
		return getService().getBasePaths(context,includeDeprecated);
	}

	@RequestMapping(value="/contexts",method=RequestMethod.GET,produces={"application/json"})
	@ResponseBody
	public List<String> getContexts() {
		return getService().getContextIds();
	}

	@RequestMapping(value="/objects",method=RequestMethod.GET,produces={"application/json"})
	@ResponseBody
	public List<ApiObject> getObjects(@RequestParam(required=false,defaultValue="") String searchTerm) {
		return getService().getObjects(searchTerm);
	}

	@RequestMapping(value="/objects/{className:.*}",method=RequestMethod.GET,produces={"application/json"})
	@ResponseBody
	public ApiObject getObject(@PathVariable String className) {
		return getService().getObject(className);
	}
	
	@RequestMapping(value="/objects/{className:.*}/details",method=RequestMethod.GET,produces={"application/json"})
	@ResponseBody
	public Object getObjectDetails(@PathVariable String className,
			@RequestParam(required=true, defaultValue="default") String requireParam,
			@RequestParam(required=false) String optionalParam
	) {
		return getService().getObjectDetails(className);
	}
	
	@RequestMapping(value="/calls",method=RequestMethod.GET,produces={"application/json"})
	@ResponseBody
	public List<ApiCall> getCalls(
			@RequestParam(required=false) String context, 
			@RequestParam(required=false) String basePath, 
			@RequestParam(required=false,defaultValue="true") boolean includeDeprecated, 
			@RequestParam(required=false) String searchTerm, 
			@RequestParam(required=false,value="method") String[] requestMethods
		) {
		return getService().getCalls(context,basePath,includeDeprecated, searchTerm, requestMethods);
	}
	
	public ApitesterService getService() {
		if(service==null) {
			ApplicationContext ctx = applicationContext;
			while(ctx!=null) {
				List<ApitesterService> s = new ArrayList<ApitesterService>(ctx.getBeansOfType(ApitesterService.class).values());
				if(s.size()>0) {
					service = s.get(0);
					log.info("## found apitester service in context: "+ctx);
					break;
				}
				ctx = ctx.getParent();
			}
		}
		return service;
	}

	
}
