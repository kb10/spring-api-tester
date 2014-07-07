package com.cinefms.apitester.springmvc.crawlers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javaruntype.type.TypeParameter;
import org.javaruntype.type.Types;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.ServletContextAware;

import com.cinefms.apitester.annotations.ApiDescription;
import com.cinefms.apitester.core.ApitesterService;
import com.cinefms.apitester.model.ApiCrawler;
import com.cinefms.apitester.model.info.ApiCall;
import com.cinefms.apitester.model.info.ApiCallParameter;
import com.cinefms.apitester.model.info.ApiObject;
import com.cinefms.apitester.model.info.ApiResult;

@Component
public class SpringAnnotationCrawler implements ApiCrawler, ApplicationContextAware, ServletContextAware {

	private static Log log = LogFactory.getLog(SpringAnnotationCrawler.class);
	
	private ApplicationContext applicationContext;
	private ServletContext servletContext;
	
	@Autowired
	private ApitesterService service;
	
	private String prefix = "";
	
	private List<ApiCall> apiCalls = null;
	
	
	@PostConstruct
	public List<ApiCall> getApiCalls() {
		if(apiCalls==null) {
			apiCalls = new ArrayList<ApiCall>();
			apiCalls.addAll(scanControllers(applicationContext));
			log.info(" ############################################################### ");
			log.info(" ##  ");
			log.info(" ##  FOUND "+apiCalls.size()+" API CALLS");
			log.info(" ##  ");
			for(ApiCall ac : apiCalls) {
				log.info(" ##  "+ac.getBasePath()+" --- "+ac.getFullPath());
			}
			log.info(" ##  ");
			log.info(" ############################################################### ");
		}
		Collections.sort(apiCalls,new Comparator<ApiCall>() {

			@Override
			public int compare(ApiCall o1, ApiCall o2) {
				return o1.getFullPath().compareTo(o2.getFullPath());
			}
			
		});
		getService().registerCalls(apiCalls);
		log.info(" ##  ");
		log.info(" ##  GOT: "+getService().getCalls(null, null, true, null, null));
		log.info(" ##  ");
		log.info(" ############################################################### ");
		return apiCalls;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public List<ApiCall> scanControllers(ApplicationContext ctx) {
		String namespace = "[default]";
		if(ctx.getId()!=null) {
			namespace = ctx.getId();
		}
		return scanControllers(namespace, new ArrayList<Object>(ctx.getBeansWithAnnotation(Controller.class).values()));
	}
	
	public List<ApiCall> scanControllers(String namespace, List<Object> controllers) {

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
					
					String description = null;
					String deprecatedSince = null;
					String since = null;
					boolean deprecated = false;
					if(m.getAnnotation(Deprecated.class)!=null) {
						deprecated = true;
					}
					ApiDescription ad = m.getAnnotation(ApiDescription.class);
					if(ad!=null) {
						 if(ad.deprecatedSince().length()>0) {
							 deprecatedSince = ad.deprecatedSince();
								deprecated = true;
						 }
						 if(ad.since().length()>0) {
							 since = ad.since();
						 }
						 if(ad.value().length()>0) {
							 description = ad.value();
						 }
						 if(ad.file().length()>0) {
							 description = loadResource(controller.getClass(), ad.file());
						 }
					}
					
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
							String p = "";
							if(servletContext!=null) {
								p = servletContext.getContextPath()+"/";
							}
							if(getPrefix()!=null) {
								p = p + "/"+getPrefix();
							}
							p = p+"/"+path;
							String fullPath = p.replaceAll("/+", "/");
							String basePath = getBasePath(fullPath);
							for(RequestMethod method : requestMethods) {
								ApiCall a = new ApiCall();
								a.setNameSpace(namespace);
								a.setFullPath(fullPath);
								a.setDescription(description);
								a.setBasePath(basePath);
								a.setDeprecated(deprecated);
								a.setDeprecatedSince(deprecatedSince);
								a.setSince(since);
								a.setHandlerClass(handlerClass);
								a.setHandlerMethod(handlerMethod);
								a.setMethod(method.toString());
								a.setRequestParameters(getRequestParameters(m));
								a.setRequestBodyParameters(getRequestBodyParameters(m));
								a.setPathParameters(getPathParameters(m));
								a.setReturnType(getResult(m));
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
	
	private String loadResource(Class<?> thatClass, String file) {
		try {
			URL u = thatClass.getResource(file);
			
			System.err.println("url: "+u);
			
			InputStream is = thatClass.getResourceAsStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			int a = 0;
			while((a=is.read(buff))>-1) {
				baos.write(buff,0,a);
			}
			return new String(baos.toByteArray(),"utf-8");
		} catch (Exception e) {
			log.error("error loading resource: "+thatClass+" / "+file,e);
			return "error loading resource: "+thatClass+" / "+file;
		}
	}

	public String getPath(String path) {
		String out = path.replaceAll("/+", "/");
		return out;
	}

	public String getBasePath(String path) {
		return getPath(path.replaceAll("/*\\{.*", ""));
	}

	public List<ApiCallParameter> getRequestParameters(Method m) {
		return getApiCalls(m, false,false,true);
	}
	
	public List<ApiCallParameter> getPathParameters(Method m) {
		return getApiCalls(m, true,false,false);
	}

	public List<ApiCallParameter> getRequestBodyParameters(Method m) {
		return getApiCalls(m, false,true,false);
	}
	
	private List<ApiCallParameter> getApiCalls(Method m, boolean path, boolean body, boolean request) {
		List<ApiCallParameter> out = new ArrayList<ApiCallParameter>();
		Annotation[][] anns = m.getParameterAnnotations(); 
		Type[] params = m.getGenericParameterTypes();
		String[] paramNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(m);
		for(int i=0;i<params.length;i++) {
			
			PathVariable p = null;
			RequestParam r = null;
			RequestBody rb = null;
			Deprecated d = null;
			ApiDescription ad = null;
			
			for(Annotation a : anns[i]) {
				if (a.annotationType() == PathVariable.class) {
					p = (PathVariable)a;
				}
				if (a.annotationType() == RequestParam.class) {
					r = (RequestParam)a;
				}
				if (a.annotationType() == RequestBody.class) {
					rb = (RequestBody)a;
				}
				if (a.annotationType() == Deprecated.class) {
					d = (Deprecated)a;
				}
				if (a.annotationType() == ApiDescription.class) {
					ad = (ApiDescription)a;
				}
			}
			
			if((p!=null && path) || (r!=null && request) || (rb!=null && body)) {
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
				if(path && p!=null) {
					if(p.value()!=null && p.value().length()>0) {
						field = p.value();
					}
					acp.setMandatory(true);
				} else if(request && r!=null) {
					if(r.value()!=null && r.value().length()>0) {
						field = r.value();
					}
					acp.setMandatory(r.required());
					if(r.defaultValue()!=null && r.defaultValue().compareTo(ValueConstants.DEFAULT_NONE)!=0) {
						acp.setDefaultValue(r.defaultValue());
					}
				} else if(body && rb!=null) {
					acp.setMandatory(rb.required());
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
	
	public ApiResult getResult(Method m) {
		ApiResult ar = new ApiResult();
		ApiObject ao = new ApiObject();
		ar.setReturnClass(ao);

		org.javaruntype.type.Type<String> strType = (org.javaruntype.type.Type<String>) Types.forJavaLangReflectType(m.getGenericReturnType());
		String paramClass = strType.getRawClass().getCanonicalName();
		ao.setClassName(paramClass);
		if(Collection.class.isAssignableFrom(strType.getRawClass())) {
			ar.setCollection(true);
			ao.setClassName(paramClass);
			for(TypeParameter<?> tp : strType.getTypeParameters()) {
				paramClass = tp.getType().getName();
				ao.setClassName(paramClass);
			}
		}
		return ar;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public ApitesterService getService() {
		return service;
	}

	public void setService(ApitesterService service) {
		this.service = service;
	}
	
	
	
}
