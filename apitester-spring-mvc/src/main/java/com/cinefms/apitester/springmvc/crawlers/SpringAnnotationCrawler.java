package com.cinefms.apitester.springmvc.crawlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

import com.cinefms.apitester.annotations.ApiDescription;
import com.cinefms.apitester.model.ApiCrawler;
import com.cinefms.apitester.model.info.ApiCall;
import com.cinefms.apitester.model.info.ApiCallParameter;
import com.cinefms.apitester.model.info.ApiObject;

@Component
public class SpringAnnotationCrawler implements ApiCrawler, ApplicationContextAware {

	private static Log log = LogFactory.getLog(SpringAnnotationCrawler.class);
	
	private ApplicationContext applicationContext;
	
	private List<ApiCall> apiCalls = null;
	
	public List<ApiCall> getApiCalls() {
		if(apiCalls==null) {
			apiCalls = new ArrayList<ApiCall>();
			ApplicationContext ctx = applicationContext;
			do {
				apiCalls.addAll(scanControllers(ctx));
				ctx = ctx.getParent();
			} while(ctx!=null);
			log.info(" ############################################################### ");
			log.info(" ##  ");
			log.info(" ##  FOUND "+apiCalls.size()+" API CALLS");
			log.info(" ##  ");
			log.info(" ############################################################### ");
		}
		return apiCalls;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public List<ApiCall> scanControllers(ApplicationContext ctx) {
		String namespace = "[default]";
		if(ctx.getId()!=null) {
			namespace = ctx.getId();
		}
		return scanControllers(namespace, new ArrayList<Object>(ctx.getBeansWithAnnotation(Controller.class).values()));
	}
	
	public List<ApiCall> scanControllers(String namespace, List<Object> controllers) {
		System.err.println("application: ----  "+namespace+" / "+controllers.size()+" controllers ");

		List<ApiCall> out = new ArrayList<ApiCall>();
		
		for(Object controller : controllers) {

			String handlerClass = controller.getClass().getName();  

			log.info(" ##  FOUND "+controllers.size()+" CONTROLLERS ... "+handlerClass);

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
								a.setNameSpace(namespace);
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
		return getApiCalls(m, false);
	}
	
	public List<ApiCallParameter> getPathParameters(Method m) {
		return getApiCalls(m, true);
	}
	
	private List<ApiCallParameter> getApiCalls(Method m, boolean path) {
		List<ApiCallParameter> out = new ArrayList<ApiCallParameter>();
		Annotation[][] anns = m.getParameterAnnotations(); 
		Type[] params = m.getGenericParameterTypes();
		String[] paramNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(m);
		for(int i=0;i<params.length;i++) {
			
			PathVariable p = null;
			RequestParam r = null;
			Deprecated d = null;
			ApiDescription ad = null;
			
			for(Annotation a : anns[i]) {
				if (a.annotationType() == PathVariable.class) {
					p = (PathVariable)a;
				}
				if (a.annotationType() == RequestParam.class) {
					r = (RequestParam)a;
				}
				if (a.annotationType() == Deprecated.class) {
					d = (Deprecated)a;
				}
				if (a.annotationType() == ApiDescription.class) {
					ad = (ApiDescription)a;
				}
			}
			if(p!=null) {
				System.err.println(paramNames[i]+" / path variable");
			}
			if(r!=null) {
				System.err.println(paramNames[i]+" / request param");
			}
			
			if((p!=null && path) || (r!=null && !path)) {
				ApiCallParameter acp = new ApiCallParameter();
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
				if(path && p!=null && p.value()!=null && p.value().length()>0) {
					field = p.value();
					acp.setMandatory(true);
				} else if(!path && r!=null) {
					if(r.value()!=null && r.value().length()>0) {
						field = r.value();
					}
					acp.setMandatory(r.required());
					if(r.defaultValue()!=null && r.defaultValue().compareTo(ValueConstants.DEFAULT_NONE)!=0) {
						acp.setDefaultValue(r.defaultValue());
					}
				}
				if(d!=null) {
					acp.setDeprecated(true);
				}
				if(ad!=null) {
					acp.setDescription(ad.value());
					if(ad.format().length()>0) {
						acp.setFormat(ad.format());
					}
					if(ad.since().length()>0) {
						acp.setSince(ad.since());
					}
					acp.setDeprecatedSince(ad.deprecatedSince());
					if(ad.deprecatedSince()!=null && ad.deprecatedSince().length()>0) {
						acp.setDeprecated(true);
					}
				}
				
				acp.setParameterName(field);
				out.add(acp);
			}
			
		}
		return out;		
	}
	
	
	
}
