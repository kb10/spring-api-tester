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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javaruntype.type.TypeParameter;
import org.javaruntype.type.Types;
import org.javaruntype.typedef.TypeDef;
import org.javaruntype.typedef.TypeDefVariable;
import org.javaruntype.typedef.TypeDefs;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
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

public class SpringAnnotationCrawler implements ApiCrawler,
		ApplicationContextAware, ServletContextAware {

	private static Log log = LogFactory.getLog(SpringAnnotationCrawler.class);

	private ApplicationContext applicationContext;
	private ServletContext servletContext;

	private ApitesterService service;

	private String prefix = "";

	private Map<String, String> defaultReqParams = new HashMap<String, String>();

	private List<ApiCall> apiCalls = null;

	@PostConstruct
	public List<ApiCall> getApiCalls() {
		log.info("################################################################ ");
		log.info("##");
		log.info("## SpringAnnotationCrawler initialized: "
				+ applicationContext);
		log.info("## ApitesterService is                : " + getService());
		log.info("##");
		log.info("################################################################ ");
		if (apiCalls == null) {
			apiCalls = new ArrayList<ApiCall>();
			apiCalls.addAll(scanControllers(applicationContext));
			log.info(" ############################################################### ");
			log.info(" ##  ");
			log.info(" ##  FOUND " + apiCalls.size()
					+ " API CALLS in context: " + applicationContext);
			log.info(" ##  ");
			for (ApiCall ac : apiCalls) {
				log.info(" ##  " + ac.getBasePath() + " --- "
						+ ac.getFullPath());
			}
			log.info(" ##  ");
			log.info(" ############################################################### ");
		}
		Collections.sort(apiCalls, new Comparator<ApiCall>() {

			@Override
			public int compare(ApiCall o1, ApiCall o2) {
				return o1.getFullPath().compareTo(o2.getFullPath());
			}

		});
		getService().registerCalls(apiCalls);
		log.info(" ##  ");
		log.info(" ##  GOT: "
				+ getService().getCalls(null, null, true, null, null));
		log.info(" ##  ");
		log.info(" ############################################################### ");
		return apiCalls;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		this.applicationContext = applicationContext;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public List<ApiCall> scanControllers(ApplicationContext ctx) {
		String namespace = "[default]";
		if (ctx.getId() != null) {
			namespace = ctx.getId();
		}
		return scanControllers(namespace, new ArrayList<Object>(ctx.getBeansWithAnnotation(Controller.class).values()));
	}

	public List<ApiCall> scanControllers(String namespace, List<Object> controllers) {

		List<ApiCall> out = new ArrayList<ApiCall>();

		for (Object controller : controllers) {

			// com.cinefms.apitester.springmvc.crawlers.TestController2$$EnhancerByCGLIB$$84793797

			Class clazz = controller.getClass();
			
			String handlerClass = controller.getClass().getName();
			
			if(handlerClass.indexOf("$$")>-1) {
				handlerClass = handlerClass.substring(0,handlerClass.indexOf("$$"));
				try {
					clazz = Class.forName(handlerClass);
				} catch (ClassNotFoundException e) {
				}
			}
			
			

			log.info(" ##  FOUND " + controllers.size() + " CONTROLLERS ... " + handlerClass);

			Method[] methods = clazz.getMethods();

			String[] classLevelPaths = new String[] { "" };

			RequestMapping rmType = (RequestMapping) clazz.getAnnotation(RequestMapping.class);
			if (rmType != null) {
				classLevelPaths = rmType.value();
			}

			for (Method m : methods) {

				try {

					String description = null;
					String deprecatedSince = null;
					String since = null;
					boolean deprecated = false;
					if (m.getAnnotation(Deprecated.class) != null) {
						deprecated = true;
					}
					ApiDescription ad = m.getAnnotation(ApiDescription.class);
					if (ad != null) {
						if (ad.deprecatedSince().length() > 0) {
							deprecatedSince = ad.deprecatedSince();
							deprecated = true;
						}
						if (ad.since().length() > 0) {
							since = ad.since();
						}
						if (ad.value().length() > 0) {
							description = ad.value();
						}
						if (ad.file().length() > 0) {
							description = loadResource(controller.getClass(),
									ad.file());
						}
					}

					String handlerMethod = m.getName();
					RequestMapping rmm = m.getAnnotation(RequestMapping.class);

					if (rmm != null) {

						List<String> mappings = new ArrayList<String>();
						for (String value : rmm.value()) {
							mappings.add(value);
						}

						List<RequestMethod> requestMethods = new ArrayList<RequestMethod>();
						for (RequestMethod rm : rmm.method()) {
							requestMethods.add(rm);
						}

						List<String> allPaths = new ArrayList<String>();
						for (String basePath : classLevelPaths) {
							for (String path : rmm.value()) {
								allPaths.add((basePath + path).replaceAll(
										"//+", "/"));
							}
						}

						for (String path : allPaths) {
							String p = "";
							if (servletContext != null) {
								p = servletContext.getContextPath() + "/";
							}
							if (getPrefix() != null) {
								p = p + "/" + getPrefix();
							}
							p = p + "/" + path;
							String fullPath = p.replaceAll("/+", "/");
							String basePath = getBasePath(fullPath);
							for (RequestMethod method : requestMethods) {
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
								a.setDefaultRequestParameters(getDefaultReqParams());
								a.setReturnType(
										new ApiResult(
												Reflection.getReturnType(controller.getClass(), m)
										)
								);
								for(ApiCallParameter acp : Reflection.getCallParameters(controller.getClass(), m)) {
									a.addParameter(acp);
								}
								
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


			InputStream is = thatClass.getResourceAsStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			int a = 0;
			while ((a = is.read(buff)) > -1) {
				baos.write(buff, 0, a);
			}
			return new String(baos.toByteArray(), "utf-8");
		} catch (Exception e) {
			log.error("error loading resource: " + thatClass + " / " + file, e);
			return "error loading resource: " + thatClass + " / " + file;
		}
	}

	public String getPath(String path) {
		String out = path.replaceAll("/+", "/");
		return out;
	}

	public String getBasePath(String path) {
		return getPath(path.replaceAll("/*\\{.*", ""));
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public ApitesterService getService() {
		if (service == null) {
			ApplicationContext ctx = applicationContext;
			while (ctx != null) {
				List<ApitesterService> s = new ArrayList<ApitesterService>(ctx
						.getBeansOfType(ApitesterService.class).values());
				if (s.size() > 0) {
					service = s.get(0);
					log.info("## found apitester service in context: " + ctx);
					break;
				}
				ctx = ctx.getParent();
			}
		}
		return service;
	}

	public void setService(ApitesterService service) {
		this.service = service;
	}

	public Map<String, String> getDefaultReqParams() {
		return defaultReqParams;
	}

	public void setDefaultReqParams(Map<String, String> defaultReqParams) {
		this.defaultReqParams = defaultReqParams;
	}

	private class ApiObjectInfo {

		private ApiObject apiObject;
		private boolean collection;

		public ApiObject getApiObject() {
			return apiObject;
		}

		public void setApiObject(ApiObject apiObject) {
			this.apiObject = apiObject;
		}

		public boolean isCollection() {
			return collection;
		}

		public void setCollection(boolean collection) {
			this.collection = collection;
		}

	}

}
