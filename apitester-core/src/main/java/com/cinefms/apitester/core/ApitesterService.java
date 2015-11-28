package com.cinefms.apitester.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.javaruntype.type.TypeParameter;
import org.javaruntype.type.Types;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.cinefms.apitester.model.info.ApiCall;
import com.cinefms.apitester.model.info.ApiObject;

@Service
public class ApitesterService implements ApplicationContextAware {

	private static Log log = LogFactory.getLog(ApitesterService.class);

	private List<ApiCall> calls = new ArrayList<ApiCall>();
	private ApplicationContext applicationContext;

	@PostConstruct
	public void init() {
		log.info("################################################################ ");
		log.info("##");
		log.info("## ApitesterService initialized: " + applicationContext);
		log.info("##");
		log.info("################################################################ ");
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public synchronized void registerCalls(List<ApiCall> apiCalls) {

		log.info(" REGISTER CALLS: " + apiCalls.size() + " calls are being registered in context: "
				+ applicationContext);

		calls.addAll(apiCalls);
		Collections.sort(calls, new Comparator<ApiCall>() {

			@Override
			public int compare(ApiCall o1, ApiCall o2) {
				return o1.getFullPath().compareTo(o2.getFullPath());
			}

		});
	}

	private List<ApiCall> getCallsInternal() {
		return calls;
	}

	public List<String> getBasePaths(String context, boolean includeDeprecated) {
		List<String> out = new ArrayList<String>();
		for (ApiCall ac : getCalls(context, null, includeDeprecated, null, null)) {
			if (!out.contains(ac.getBasePath())) {
				out.add(ac.getBasePath());
			}
		}
		return out;
	}

	public List<String> getContextIds() {
		List<String> out = new ArrayList<String>();
		for (ApiCall ac : getCallsInternal()) {
			if (!out.contains(ac.getNameSpace())) {
				out.add(ac.getNameSpace());
			}
		}
		return out;
	}

	public List<ApiObject> getObjects(String searchTerm) {
		Set<ApiObject> out = new TreeSet<ApiObject>();
		for (ApiCall ac : getCallsInternal()) {
			for (ApiObject ao : ac.getApiObjects()) {
				if (ao.getClassName() == null) {
					log.warn("classname of an api object is NULL (" + ac.getMethod() + ":" + ac.getFullPath() + " / "
							+ ao.getDescription());
				} else {
					if (ao.getClassName().toUpperCase().contains(searchTerm.toUpperCase())) {
						out.add(ao);
					}
				}
			}
		}
		return new ArrayList<ApiObject>(out);
	}

	public ApiObject getObject(String className) {
		for (ApiObject ao : getObjects(className)) {
			if (ao.getClassName().compareTo(className) == 0) {
				return ao;
			}
		}
		return null;
	}

	private List<ApiObjectFieldInfo> getFieldInfos(Class<?> clazz) {
		List<ApiObjectFieldInfo> out = new ArrayList<ApiObjectFieldInfo>();
		for (Method m : clazz.getMethods()) {
			try {
				String fieldname = null;
				if ((m.getName().startsWith("get")) && m.getParameterTypes().length == 0) {
					try {
						if (clazz.getMethod("set" + m.getName().substring(3), m.getReturnType()) != null) {
							fieldname = m.getName().substring(3);
							fieldname = fieldname.substring(0, 1).toLowerCase() + fieldname.substring(1);
						}
					} catch (NoSuchMethodException e) {
						log.debug("assymetrics: " + m.getName() + " does not have a setter");
					}
				}
				if ((m.getName().startsWith("is")) && m.getParameterTypes().length == 0) {
					if (clazz.getMethod("set" + m.getName().substring(2), m.getReturnType()) != null) {
						fieldname = m.getName().substring(2);
						fieldname = fieldname.substring(0, 1).toLowerCase() + fieldname.substring(1);
					}
				}

				if (fieldname != null) {
					@SuppressWarnings("unchecked")
					ApiObjectFieldInfo aofi = new ApiObjectFieldInfo();
					org.javaruntype.type.Type<String> strType = (org.javaruntype.type.Type<String>) Types.forJavaLangReflectType(m.getGenericReturnType());
					if (Collection.class.isAssignableFrom(strType.getRawClass())) {
						aofi.setName(fieldname);
						aofi.setCollection(true);
						for (TypeParameter<?> tp : strType.getTypeParameters()) {
							aofi.setTypeClass(tp.getType().getRawClass());
							aofi.setTypeName(tp.getType().getSimpleName() + "[]");
						}
						out.add(aofi);
					} else {
						aofi.setName(fieldname);
						aofi.setCollection(false);
						aofi.setTypeClass(strType.getRawClass());
						aofi.setTypeName(strType.getSimpleName());
						out.add(aofi);

					}
				}
			} catch (Exception e) {
				log.warn("exception while trying to get object structure: " + e.getMessage() + "/" + e.getClass(), e);
			}
		}
		return out;
	}

	private Object createMap(Class<?> clazz, List<Class<?>> done) {
		if (done.contains(clazz)) {
			return "[loop detected]";
		}
		done = new ArrayList<Class<?>>(done);
		done.add(clazz);
		if (clazz.isEnum()) {
			try {
				List<String> ss = new ArrayList<>();
				for (Object s : clazz.getEnumConstants()) {
					ss.add(s.toString());
				}
				return ss;
			} catch (Exception e) {
				return null;
			}
		} else if (clazz.getCanonicalName().compareTo("long") == 0) {
			return 1l;
		} else if (clazz.getCanonicalName().compareTo("boolean") == 0) {
			return Boolean.FALSE;
		} else if (clazz.getCanonicalName().compareTo("double") == 0) {
			return new Double(0.5);
		} else if (clazz.getCanonicalName().compareTo("float") == 0) {
			return new Float(0.5);
		} else if (clazz.getCanonicalName().compareTo("int") == 0) {
			return 1;
		} else if (String.class.isAssignableFrom(clazz)) {
			return "a string";
		} else if (Boolean.class.isAssignableFrom(clazz)) {
			return new Boolean(true);
		} else if (Date.class.isAssignableFrom(clazz)) {
			return new Date();
		} else if (Number.class.isAssignableFrom(clazz)) {
			return 1l;
		} else if (Boolean.class.isAssignableFrom(clazz)) {
			return true;
		} else if (Locale.class.isAssignableFrom(clazz)) {
			return Locale.GERMANY;
		} else {
			Map<String, Object> m = new LinkedHashMap<String, Object>();
			List<ApiObjectFieldInfo> aofis = getFieldInfos(clazz);
			for (ApiObjectFieldInfo aofi : aofis) {
				Object o;
				o = createMap(aofi.getTypeClass(), done);
				if (aofi.isCollection()) {
					List<Object> x = new ArrayList<Object>();
					x.add(o);
					o = x;
				}
				m.put(aofi.getName(), o);
			}
			return m;
		}
	}

	public Object getObjectDetails(String className) {
		ApiObject ao = getObject(className);
		if (ao != null) {
			try {
				Object o = createMap(Class.forName(ao.getClassName()), new ArrayList<Class<?>>());
				return o;
			} catch (Exception e) {
				log.error("error creating JSON object: " + e.getMessage());
			}
		}
		return "{none}";
	}

	public List<ApiCall> getCalls(String context, String basePath, boolean includeDeprecated, String searchTerm,
			String[] requestMethods) {
		List<ApiCall> out = new ArrayList<ApiCall>();
		List<String> rms = null;
		if (requestMethods != null) {
			rms = new ArrayList<String>();
			for (String rm : requestMethods) {
				rms.add(rm);
			}
		}
		for (ApiCall ac : getCallsInternal()) {
			if (context != null && context.compareTo(ac.getNameSpace()) != 0) {
				continue;
			}
			if (basePath != null && ac.getBasePath().compareTo(basePath) != 0) {
				continue;
			}
			if (!includeDeprecated && ac.isDeprecated()) {
				continue;
			}
			if (searchTerm != null && !ac.getFullPath().toLowerCase().contains(searchTerm.toLowerCase())) {
				continue;
			}
			if (rms != null && !rms.contains(ac.getMethod())) {
				continue;
			}
			out.add(ac);
		}
		return out;
	}

	private class ApiObjectFieldInfo {

		private String name;
		private String typeName;
		private Class<?> typeClass;
		private boolean collection;
		private boolean enumeration;
		private List<String> values;

		public ApiObjectFieldInfo() {
			super();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public Class<?> getTypeClass() {
			return typeClass;
		}

		public void setTypeClass(Class<?> typeClass) {
			this.typeClass = typeClass;
		}

		public boolean isCollection() {
			return collection;
		}

		public void setCollection(boolean collection) {
			this.collection = collection;
		}

		public boolean isEnumeration() {
			return enumeration;
		}

		public void setEnumeration(boolean enumeration) {
			this.enumeration = enumeration;
		}

		public List<String> getValues() {
			return values;
		}

		public void setValues(List<String> values) {
			this.values = values;
		}

	}

}
