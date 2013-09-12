/*
 * Copyright [2006] PurePerfect.com
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * 
 * You may obtain a copy of the License at 
 * 		http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, 
 * software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. 
 * 
 * See the License for the specific language governing permissions
 * and limitations under the License. 
 */
package com.pureperfect.purview.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pureperfect.purview.validators.NoValidation;
import com.pureperfect.purview.validators.numeric.Id;
import com.pureperfect.purview.validators.text.MaxLength;
import com.pureperfect.purview.validators.text.MinLength;

/***
 * Unit test for {@link ReflectionUtils}.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class ReflectionUtilsTest extends TestCase
{
	/**
	 * Stub class for testing.
	 */
	public static class Stub
	{
		public static Object nonvoidMethod()
		{
			return null;
		}

		public static void staticMethod()
		{
			// Do not delete This method is used for testing
		}

		public String accessible;

		@SuppressWarnings("unused")
		private String notAccessible;

		@NoValidation
		@MinLength(3)
		@MaxLength(3)
		@Id
		public String getName()
		{
			return "foo";
		}

		@NoValidation
		public String getPassword()
		{
			return "bar";
		}

		public void getVoid()
		{
			// yadda yadda
		}

		public void nonStaticMethod()
		{
			// Do not delete This method is used for testing
		}
	}

	/**
	 * Test get declared fields.
	 */
	public void testGetDeclaredFields()
	{
		final Stub form = new Stub();

		assertEquals(
				2,
				ReflectionUtils.getDeclaredFields(form,
						new MakeAccessibleFieldFilter()).size());
	}

	/**
	 * Test get declared methods.
	 */
	public void testGetDeclaredMethods()
	{
		final Stub form = new Stub();

		assertEquals(
				2,
				ReflectionUtils.getDeclaredMethods(form,
						GetterMethodFilter.defaultInstance()).size());
	}

	/**
	 * Test get value using method.
	 */
	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	public void testGetValueUsingMethod() throws Exception
	{
		final Stub form = new Stub();

		final Class theClass = form.getClass();

		final String name = (String) ReflectionUtils.getValueOfMethod(
				theClass.getDeclaredMethod("getName", new Class[]
				{}), form);

		assertEquals("foo", name);

		final String pass = (String) ReflectionUtils.getValueOfMethod(
				theClass.getDeclaredMethod("getPassword", new Class[]
				{}), form);

		assertEquals("bar", pass);
	}

	/**
	 * Test get value using name.
	 */
	public void testGetValueUsingName() throws SecurityException,
			IllegalArgumentException, NoSuchMethodException,
			IllegalAccessException, InvocationTargetException
	{
		final Stub form = new Stub();

		final String name = (String) ReflectionUtils.getValueOfMethod(
				"getName", form);

		assertEquals("foo", name);

		final String pass = (String) ReflectionUtils.getValueOfMethod(
				"getPassword", form);

		assertEquals("bar", pass);
	}

	/**
	 * Test is static.
	 */
	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	public void testIsStatic() throws Exception
	{
		final Class clazz = Stub.class;

		final Method staticMethod = clazz.getDeclaredMethod("staticMethod",
				new Class[]
				{});

		assertTrue(ReflectionUtils.isStatic(staticMethod));

		final Method nonStaticMethod = clazz.getDeclaredMethod(
				"nonStaticMethod", new Class[]
				{});

		assertFalse(ReflectionUtils.isStatic(nonStaticMethod));
	}

	/**
	 * Test get annotation value.
	 */
	public void testGetAnnotationValue()
	{
		// TODO TESTME
	}

	/**
	 * Test is void.
	 */
	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	public void testIsVoid() throws Exception
	{
		final Class clazz = Stub.class;

		final Method voidMethod = clazz.getDeclaredMethod("staticMethod",
				new Class[]
				{});

		assertTrue(ReflectionUtils.isVoid(voidMethod));

		final Method nonvoidMethod = clazz.getDeclaredMethod("nonvoidMethod",
				new Class[]
				{});

		assertFalse(ReflectionUtils.isVoid(nonvoidMethod));
	}
}