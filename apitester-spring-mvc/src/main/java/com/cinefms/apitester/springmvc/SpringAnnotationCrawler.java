package com.cinefms.apitester.springmvc;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.javaruntype.type.TypeParameter;
import org.javaruntype.type.Types;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.cinefms.apitester.model.ApiCrawler;
import com.cinefms.apitester.model.info.ApiCall;
import com.cinefms.apitester.model.info.ApiCallParameter;
import com.cinefms.apitester.model.info.ApiObject;

@Component
public class SpringAnnotationCrawler implements ApiCrawler, ApplicationContextAware {

	
	
	private ApplicationContext applicationContext;
	
	private List<ApiCall> apiCalls = null;
	
	public List<ApiCall> getApiCalls() {
		if(apiCalls==null) {
			List<Object> controllers = new ArrayList<Object>(applicationContext.getBeansWithAnnotation(Controller.class).values());
			apiCalls = scanControllers(controllers);
		}
		return apiCalls;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public List<ApiCall> scanControllers(List<Object> controllers) {

		List<ApiCall> out = new ArrayList<ApiCall>();
		
		for(Object controller : controllers) {

			String handlerClass = controller.getClass().getName();  

			Method[] methods = controller.getClass().getMethods();
			
			String[] classLevelPaths = new String[] { "" };

			RequestMapping rmType = controller.getClass().getAnnotation(RequestMapping.class); 
			if(rmType!=null) {
				classLevelPaths = rmType.value();
			}
			
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

						List<String> allPaths = new ArrayList<String>();
						for(String basePath : classLevelPaths) {
							for(String path : rmm.value()) {
								allPaths.add((basePath+path).replaceAll("//+", "/"));
							}
						}
						
						for(String path : allPaths) {
							for(RequestMethod method : requestMethods) {
								ApiCall a = new ApiCall();
								String fullPath = path;
								a.setFullPath(fullPath);
								String basePath = getBasePath(path);
								a.setBasePath(basePath);
								a.setHandlerClass(handlerClass);
								a.setHandlerMethod(handlerMethod);
								a.setMethod(method.toString());
								a.setRequestParameters(getRequestParameters(m));
								a.setPathParameters(getPathParameters(m));
								out.add(a);
							}
						}
					}
					
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
		
		return out;
	}
	
	public String getPath(String path) {
		String out = path.replaceAll("/+", "/");
		return out;
	}

	public String getBasePath(String path) {
		return getPath(path.replaceAll("/*\\{.*", ""));
	}

	public List<ApiCallParameter> getRequestParameters(Method m) {
		List<ApiCallParameter> out = new ArrayList<ApiCallParameter>();
		
		return out;
	}
	
	public List<ApiCallParameter> getPathParameters(Method m) {
		List<ApiCallParameter> out = new ArrayList<ApiCallParameter>();
		Annotation[][] anns = m.getParameterAnnotations(); 
		Type[] params = m.getGenericParameterTypes();
		String[] paramNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(m);
		for(int i=0;i<params.length;i++) {
			
			PathVariable p = null;
			
			for(Annotation a : anns[i]) {
				if (a.annotationType() == PathVariable.class) {
					p = (PathVariable)a;
				}
			}
			
			if(p!=null) {
				ApiCallParameter acp = new ApiCallParameter();
				acp.setMandatory(true);
				acp.setCollection(false);
				
				acp.setParameterType(new ApiObject());
				@SuppressWarnings("unchecked")
				org.javaruntype.type.Type<String> strType = (org.javaruntype.type.Type<String>) Types.forJavaLangReflectType(params[i]);
				String paramClass = strType.getRawClass().getCanonicalName();
				acp.getParameterType().setClassName(paramClass);
				if(Collection.class.isAssignableFrom(strType.getRawClass())) {
					acp.setCollection(true);
					acp.getParameterType().setClassName(paramClass);
					for(TypeParameter<?> tp : strType.getTypeParameters()) {
						paramClass = tp.getType().getName();
						acp.getParameterType().setClassName(paramClass);
					}
				}

				
				String field = "[unknown]";
				if(paramNames !=null && paramNames.length==params.length) {
					field = paramNames[i];
				}
				if(p.value()!=null && p.value().length()>0) {
					field = p.value();
				}
				acp.setParameterName(field);
				
				out.add(acp);
			}
			
		}
		return out;
	}
	

}
