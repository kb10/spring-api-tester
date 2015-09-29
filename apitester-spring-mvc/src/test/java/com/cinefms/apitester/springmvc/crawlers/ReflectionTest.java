package com.cinefms.apitester.springmvc.crawlers;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.cinefms.apitester.model.info.ApiCallParameter;
import com.cinefms.apitester.model.info.ApiObject;

public class ReflectionTest {
	
	@Test 
	public void testM1() throws NoSuchMethodException, SecurityException {
		ApiObject ao = Reflection.getReturnType(TestObjectReturnTypes.class,TestObjectReturnTypes.class.getMethod("m1", new Class[0]));
		Assert.assertNotNull(ao);
		Assert.assertEquals("java.lang.String", ao.getClassName());
		Assert.assertEquals(true, ao.isCollection());
		Assert.assertEquals(false, ao.isPrimitive());
	}
	
	@Test 
	public void testM2() throws NoSuchMethodException, SecurityException {
		ApiObject ao = Reflection.getReturnType(TestObjectReturnTypes.class,TestObjectReturnTypes.class.getMethod("m2", new Class[0]));
		Assert.assertNotNull(ao);
		Assert.assertEquals("java.lang.String", ao.getClassName());
		Assert.assertEquals(true, ao.isCollection());
		Assert.assertEquals(false, ao.isPrimitive());
	}
	
	
	@Test 
	public void testM3() throws NoSuchMethodException, SecurityException {
		ApiObject ao = Reflection.getReturnType(TestObjectReturnTypes.class,TestObjectReturnTypes.class.getMethod("m3", new Class[0]));
		Assert.assertNotNull(ao);
		Assert.assertEquals("java.lang.String", ao.getClassName());
		Assert.assertEquals(false, ao.isCollection());
		Assert.assertEquals(false, ao.isPrimitive());
	}
	
	@Test 
	public void testM4() throws NoSuchMethodException, SecurityException {
		ApiObject ao = Reflection.getReturnType(TestObjectReturnTypes.class,TestObjectReturnTypes.class.getMethod("m4", new Class[0]));
		Assert.assertNotNull(ao);
		Assert.assertEquals("java.lang.Integer", ao.getClassName());
		Assert.assertEquals(true, ao.isCollection());
		Assert.assertEquals(false, ao.isPrimitive());
	}
	
	@Test 
	public void testM5() throws NoSuchMethodException, SecurityException {
		ApiObject ao = Reflection.getReturnType(TestObjectReturnTypes.class,TestObjectReturnTypes.class.getMethod("m5", new Class[0]));
		Assert.assertNotNull(ao);
		Assert.assertEquals("java.lang.Integer", ao.getClassName());
		Assert.assertEquals(true, ao.isCollection());
		Assert.assertEquals(false, ao.isPrimitive());
	}
	
	@Test 
	public void testM6() throws NoSuchMethodException, SecurityException {
		ApiObject ao = Reflection.getReturnType(TestObjectReturnTypes.class,TestObjectReturnTypes.class.getMethod("m6", new Class[0]));
		Assert.assertNotNull(ao);
		Assert.assertEquals("java.lang.Integer", ao.getClassName());
		Assert.assertEquals(false, ao.isCollection());
		Assert.assertEquals(false, ao.isPrimitive());
	}
	
	@Test 
	public void testM7() throws NoSuchMethodException, SecurityException {
		ApiObject ao = Reflection.getReturnType(TestObjectReturnTypes.class,TestObjectReturnTypes.class.getMethod("m7", new Class[0]));
		Assert.assertNotNull(ao);
		Assert.assertEquals("int", ao.getClassName());
		Assert.assertEquals(true, ao.isCollection());
		Assert.assertEquals(true, ao.isPrimitive());
	}
	
	@Test 
	public void testM8() throws NoSuchMethodException, SecurityException {
		ApiObject ao = Reflection.getReturnType(TestObjectReturnTypes.class,TestObjectReturnTypes.class.getMethod("m8", new Class[0]));
		Assert.assertNotNull(ao);
		Assert.assertEquals("int", ao.getClassName());
		Assert.assertEquals(false, ao.isCollection());
		Assert.assertEquals(true, ao.isPrimitive());
	}
	
	@Test 
	public void testM9() throws NoSuchMethodException, SecurityException {
		ApiObject ao = Reflection.getReturnType(TestObjectReturnTypes.class,TestObjectReturnTypes.class.getMethod("m9", new Class[0]));
		Assert.assertNotNull(ao);
		Assert.assertEquals("com.cinefms.apitester.springmvc.crawlers.TestGenericsImpl", ao.getClassName());
		Assert.assertEquals(true, ao.isCollection());
		Assert.assertEquals(false, ao.isPrimitive());
	}
	
	@Test 
	public void testS1() throws NoSuchMethodException, SecurityException {
		ApiObject ao = Reflection.getReturnType(TestObjectReturnTypes.class,TestObjectReturnTypes.class.getMethod("s1", new Class[0] ));
		Assert.assertNotNull(ao);
		Assert.assertEquals("com.cinefms.apitester.springmvc.crawlers.TestGenericsImpl", ao.getClassName());
		Assert.assertEquals(true, ao.isCollection());
		Assert.assertEquals(false, ao.isPrimitive());
	}
	
	@Test 
	public void testS2() throws NoSuchMethodException, SecurityException {
		ApiObject ao = Reflection.getReturnType(TestObjectReturnTypes.class,TestObjectReturnTypes.class.getMethod("s2", new Class[0] ));
		Assert.assertNotNull(ao);
		Assert.assertEquals("com.cinefms.apitester.springmvc.crawlers.TestGenericsImpl", ao.getClassName());
		Assert.assertEquals(false, ao.isCollection());
		Assert.assertEquals(false, ao.isPrimitive());
	}
	
	@Test 
	public void testSubclassOfGenericControllerExpectCorrectReturnType() throws NoSuchMethodException, SecurityException {
		ApiObject ao = Reflection.getReturnType(GenericTestControllerImpl.class,GenericTestControllerImpl.class.getMethod("get", new Class[0] ));
		Assert.assertNotNull(ao);
		//Assert.assertEquals("java.lang.String", ao.getClassName());
		Assert.assertEquals(false, ao.isCollection());
		Assert.assertEquals(false, ao.isPrimitive());
	}
	
	@Test 
	public void testSubclassOfGenericControllerExpectCorrectParameterTypes() throws NoSuchMethodException, SecurityException {
		GenericTestControllerImpl tci = new GenericTestControllerImpl();
		Method m = tci.getClass().getMethods()[1];
		System.err.println(m.getName());
		List<ApiCallParameter> acps = Reflection.getCallParameters(GenericTestControllerImpl.class,m);
		Assert.assertNotNull(acps);
		Assert.assertEquals(2, acps.size());
		//Assert.assertEquals(acps.get(0).getParameterType().getClassName(),"java.lang.String");
		//Assert.assertEquals(acps.get(1).getParameterType().getClassName(),"java.lang.String");
	}
	
	
	/**
	 * 


	public List<TestGenericsImpl> m9() {
		return null;
	}

	public <K> K m10(Class<K> clazz) {
		return null;
	}

	 * 
	 */

}
