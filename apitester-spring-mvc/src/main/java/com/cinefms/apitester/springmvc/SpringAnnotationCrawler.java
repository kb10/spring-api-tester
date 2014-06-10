package com.cinefms.apitester.springmvc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cinefms.apitester.core.AbstractApiCrawler;
import com.cinefms.apitester.model.info.ApiCall;

@Component
public class SpringAnnotationCrawler extends AbstractApiCrawler implements ApplicationContextAware, ApplicationListener<ContextRefreshedEvent> {

	private ApplicationContext applicationContext;
	
	private List<ApiCall> apiCalls = null;
	
	public List<ApiCall> getApiCalls() {
		if(apiCalls==null) {
			triggerScan();
		}
		return apiCalls;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		triggerScan();
	}

	public void triggerScan() {
		List<Object> controllers = new ArrayList<Object>(applicationContext.getBeansWithAnnotation(Controller.class).values());
		apiCalls = scanControllers(controllers);
	}
	
	public List<ApiCall> scanControllers(List<Object> controllers) {
		List<ApiCall> out = new ArrayList<ApiCall>();
		
		for(Object controller : controllers) {

			String handlerClass = controller.getClass().getName();  
		
			Method[] methods = controller.getClass().getMethods();
			
			for(Method m : methods) {
				try {
					String handlerMethod = m.getName();
					RequestMapping rmm = m.getAnnotation(RequestMapping.class); 
					if(rmm!=null) {
						
						List<String> mappings = new ArrayList<String>();
						for(String value : rmm.value()) {
							mappings.add(value);
						}
						
						List<RequestMethod> requestMethods = new ArrayList<RequestMethod>();
						for(RequestMethod rm : rmm.method()) {
							requestMethods.add(rm);
						}
						
					}
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
		return out;
	}

}
