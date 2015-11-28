package com.cinefms.apitester.springmvc.crawlers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ValueConstants;

import com.cinefms.apitester.annotations.ApiDescription;
import com.cinefms.apitester.model.info.ApiCallParameter;
import com.cinefms.apitester.model.info.ApiObject;
import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedMethod;

public class Reflection {
	
	private static Log log = LogFactory.getLog(Reflection.class);
	
	public static ApiObject getReturnType(Class<?> clazz, Method method) {
		ApiObject out = new ApiObject();
		TypeResolver typeResolver = new TypeResolver();
		MemberResolver memberResolver = new MemberResolver(typeResolver);

		ResolvedType resolvedType = typeResolver.resolve(clazz);
		ResolvedTypeWithMembers resolvedTypeWithMembers = memberResolver.resolve(resolvedType, null, null);
		for (ResolvedMethod rm : resolvedTypeWithMembers.getMemberMethods()) {
			if (rm.getRawMember().toGenericString().compareTo(method.toGenericString()) == 0) {

				ResolvedType r = rm.getReturnType();

				if (r == null) {
					out.setClassName("void");
				} else if (r.getArrayElementType() != null) {
					out.setCollection(true);
					out.setPrimitive(r.getArrayElementType().isPrimitive());
					out.setClassName(r.getArrayElementType().getErasedType().getCanonicalName());
				} else if (Collection.class.isAssignableFrom(r.getErasedType())) {
					out.setCollection(true);
					if (r.getTypeParameters() != null && r.getTypeParameters().size() == 1) {
						out.setClassName(r.getTypeParameters().get(0).getErasedType().getCanonicalName());
						out.setPrimitive(r.getTypeParameters().get(0).isPrimitive());
					}
				} else {
					out.setCollection(false);
					out.setClassName(r.getErasedType().getCanonicalName());
					out.setPrimitive(r.getErasedType().isPrimitive());
				}
			}
		}
		return out;
	}

	private static List<ResolvedMethod> getAllMethods(Class<?> clazz) {
		List<ResolvedMethod> out = new ArrayList<ResolvedMethod>();
		if(clazz!=null && clazz != Object.class) {
			TypeResolver typeResolver = new TypeResolver();
			MemberResolver memberResolver = new MemberResolver(typeResolver);
			ResolvedType resolvedType = typeResolver.resolve(clazz);
			ResolvedTypeWithMembers resolvedTypeWithMembers = memberResolver.resolve(resolvedType, null, null);
			
			
			
			for (ResolvedMethod rm : resolvedTypeWithMembers.getMemberMethods()) {
				out.add(rm);
			}
			//out.addAll(getAllMethods(clazz.getSuperclass()));
		}
		return out;
	}
	
	public static boolean match(Class clazz, Method m, ResolvedMethod rm) {
		if(rm.getArgumentCount()!=m.getParameterCount()) {
			log.info("argument count doesn't match .... ");
			return false;
		}
		if(rm.getName().compareTo(m.getName())!=0) {
			log.info("name doesn't match .... ");
			return false;
		}
		for(int i=0;i<rm.getArgumentCount();i++) {
			Type tt = m.getGenericParameterTypes()[i];
			if(tt instanceof TypeVariable) {
			} else if(tt instanceof ParameterizedType) {
			} else if(!((Class)m.getGenericParameterTypes()[i]).isAssignableFrom(rm.getArgumentType(i).getErasedType())) {
				log.info("arguments don't match: "+rm.getArgumentType(i).getErasedType()+" is not assignable from "+m.getGenericParameterTypes()[i]);
				return false;
			}
		}
		return true;
	}
	
	
	public static List<ApiCallParameter> getCallParameters(Class<?> clazz, Method method) {

		List<ApiCallParameter> out = new ArrayList<ApiCallParameter>();

		TypeResolver typeResolver = new TypeResolver();
		MemberResolver memberResolver = new MemberResolver(typeResolver);


		List<ResolvedMethod> methods = getAllMethods(clazz);
		
		for (ResolvedMethod rm : methods) {

			if(!match(clazz,method,rm)) {
				log.warn("checking: "+clazz.getName()+"."+method.getName()+" against "+rm.getDeclaringType()+" / "+rm.getRawMember().getName()+" --- no match!");
				continue;
			}
			log.warn("checking: "+clazz.getName()+"."+method.getName()+" against "+rm.getDeclaringType()+" / "+rm.getRawMember().getName()+" --- MATCH!");

	        String[] paramNames = new LocalVariableTableParameterNameDiscoverer().getParameterNames(method);
	        

			for (int i = 0; i < rm.getArgumentCount(); i++) {

				ApiCallParameter apc = new ApiCallParameter();
				ApiObject ao = new ApiObject();
				apc.setParameterType(ao);

				String field = "[unknown]";
				
				if (paramNames != null) {
					field = paramNames[i];
				}
				
				RequestBody rb = null;
				RequestParam rp = null;
				PathVariable pv = null;
				Deprecated d = null;
				ApiDescription ad = null;
				for(Annotation a : method.getParameterAnnotations()[i]) {
					if(a instanceof Deprecated) {
						apc.setDeprecated(true);
					}
					if(a instanceof ApiDescription) {
						ad = (ApiDescription)a;
						if(ad.deprecatedSince().length()>0) {
							apc.setDeprecated(true);
							apc.setDeprecatedSince(ad.deprecatedSince());
						}
						if(ad.value().length()>0) {
							apc.setDescription(ad.value());
						}
						if(ad.format().length()>0) {
							apc.setFormat(ad.format());
						}
						if(ad.since().length()>0) {
							apc.setSince(ad.since());
						}
					}
					if(a instanceof PathVariable) {
						pv = (PathVariable)a;
						apc.setType(ApiCallParameter.Type.PATH);
						apc.setMandatory(true);
						if(pv.value().length()>0) {
							field = pv.value();
						}
					}
					if(a instanceof RequestParam) {
						rp = (RequestParam)a;
						apc.setType(ApiCallParameter.Type.REQUEST);
						apc.setMandatory(rp.required());
						apc.setParameterName(field);
						String dv = ((RequestParam)a).defaultValue();
						if(dv!=null && !dv.equals(ValueConstants.DEFAULT_NONE)) {
							apc.setDefaultValue(dv);
						}
						if(rp.value().length()>0) {
							field = rp.value();
						}
					}
					if(a instanceof RequestBody) {
						rb = (RequestBody)a;
						apc.setType(ApiCallParameter.Type.BODY);
						apc.setMandatory(rb.required());
					}
				}
				if (rb == null && rp == null && pv == null) {
					continue;
				}

				apc.setParameterName(field);
				
				ResolvedType rt = rm.getArgumentType(i);

				if (rt.getArrayElementType() != null) {
					ao.setCollection(true);
					ao.setPrimitive(rt.getArrayElementType().isPrimitive());
					ao.setClassName(rt.getArrayElementType().getErasedType().getCanonicalName());
				} else if (Collection.class.isAssignableFrom(rt.getErasedType())) {
					ao.setCollection(true);
					if (rt.getTypeParameters() != null && rt.getTypeParameters().size() == 1) {
						ao.setClassName(rt.getTypeParameters().get(0).getErasedType().getCanonicalName());
						ao.setPrimitive(rt.getTypeParameters().get(0).isPrimitive());
					}
				} else {
					ao.setCollection(false);
					ao.setClassName(rt.getErasedType().getCanonicalName());
					ao.setPrimitive(rt.getErasedType().isPrimitive());
				}
				
				
				out.add(apc);
				
			}
			return out;
		}
		log.warn(" =========================================================================== ");
		log.warn(" == ");
		log.warn(" == no matching method found for: "+clazz.getName()+"."+method.getName());
		log.warn(" == ");
		log.warn(" =========================================================================== ");
		return out;
	}

}
