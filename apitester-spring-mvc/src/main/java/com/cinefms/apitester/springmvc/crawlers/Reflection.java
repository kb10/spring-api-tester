package com.cinefms.apitester.springmvc.crawlers;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.cinefms.apitester.model.info.ApiCallParameter;
import com.cinefms.apitester.model.info.ApiObject;
import com.fasterxml.classmate.MemberResolver;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.ResolvedTypeWithMembers;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.classmate.members.ResolvedMethod;

public class Reflection {

	public static ApiObject getReturnType(Class<?> clazz, Method method) {
		ApiObject out = new ApiObject();
		TypeResolver typeResolver = new TypeResolver();
		MemberResolver memberResolver = new MemberResolver(typeResolver);

		ResolvedType resolvedType = typeResolver.resolve(clazz);
		ResolvedTypeWithMembers resolvedTypeWithMembers = memberResolver.resolve(resolvedType, null, null);
		for(ResolvedMethod rm : resolvedTypeWithMembers.getMemberMethods()) {
			System.err.println(rm.toString()+" : "+rm.getRawMember().toGenericString()+" -- "+method.toGenericString());
			if(rm.getRawMember().toGenericString().compareTo(method.toGenericString())==0) {
				ResolvedType r = rm.getReturnType();
				if(r.getArrayElementType()!=null) {
					out.setCollection(true);
					out.setPrimitive(r.getArrayElementType().isPrimitive());
					out.setClassName(r.getArrayElementType().getErasedType().getCanonicalName());
				} else if(Collection.class.isAssignableFrom(r.getErasedType())) {
					out.setCollection(true);
					if(r.getTypeParameters()!=null && r.getTypeParameters().size()==1) {
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
	
	
	public static List<ApiCallParameter> getCallParameters(Class<?> clazz, Method method) {

		TypeResolver typeResolver = new TypeResolver();
		MemberResolver memberResolver = new MemberResolver(typeResolver);

		ResolvedType resolvedType = typeResolver.resolve(clazz);
		ResolvedTypeWithMembers resolvedTypeWithMembers = memberResolver.resolve(resolvedType, null, null);
		
		List<ApiCallParameter> out = new ArrayList<ApiCallParameter>(); 
		
		for(ResolvedMethod rm : resolvedTypeWithMembers.getMemberMethods()) {
			for(int i=0; i < rm.getArgumentCount();i++) {

				ApiCallParameter apc = new ApiCallParameter();
				ApiObject ao = new ApiObject();
				apc.setParameterType(ao);

				
				ResolvedType rt = rm.getArgumentType(i);
				RequestBody rb = rt.getErasedType().getAnnotation(RequestBody.class);
				RequestParam rp = rt.getErasedType().getAnnotation(RequestParam.class);
				PathVariable pv = rt.getErasedType().getAnnotation(PathVariable.class);
				
				if(rb==null && rp==null && pv==null) {
					continue;
				}
				
				if(rb!=null) {
					apc.setType(ApiCallParameter.Type.BODY);
					apc.setMandatory(rb.required());
				} else if(rp!=null) {
					apc.setType(ApiCallParameter.Type.REQUEST);
					apc.setMandatory(rb.required());
				} else if(pv!=null) {
					apc.setType(ApiCallParameter.Type.PATH);
					apc.setMandatory(true);
				}
				
				System.err.println(rm.toString()+" : "+rm.getRawMember().toGenericString()+" -- "+method.toGenericString());
				if(rm.getRawMember().toGenericString().compareTo(method.toGenericString())==0) {
					ResolvedType r = rm.getReturnType();
					if(r.getArrayElementType()!=null) {
						ao.setCollection(true);
						ao.setPrimitive(r.getArrayElementType().isPrimitive());
						ao.setClassName(r.getArrayElementType().getErasedType().getCanonicalName());
					} else if(Collection.class.isAssignableFrom(r.getErasedType())) {
						ao.setCollection(true);
						if(r.getTypeParameters()!=null && r.getTypeParameters().size()==1) {
							ao.setClassName(r.getTypeParameters().get(0).getErasedType().getCanonicalName());
							ao.setPrimitive(r.getTypeParameters().get(0).isPrimitive());
						}
					} else {
						ao.setCollection(false);
						ao.setClassName(r.getErasedType().getCanonicalName());
						ao.setPrimitive(r.getErasedType().isPrimitive());
					}
				}
			}
		}
		return out;
	}
	
	
}
