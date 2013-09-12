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
package com.pureperfect.purview.validators.net;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pureperfect.purview.Purview;
import com.pureperfect.purview.util.GetterMethodFilter;
import com.pureperfect.purview.util.MakeAccessibleFieldFilter;

/**
 * Unit test for {@link Email} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class EmailTest extends TestCase
{
	public class Stub
	{
		@Email
		public String field = "me@myhost.com";

		@Email(required = true)
		public String required = "me@myhost.com";

		private String value = "me@myhost.com";

		@Email
		public String getValue()
		{
			return this.value;
		}

		public void paramTest(@Email final String address)
		{
			// for testing
		}

		public void setValue(final String value)
		{
			this.value = value;
		}
	}

	public class TLDStub
	{
		private String value;

		@Email(validateTlds = true)
		public String getValue()
		{
			return this.value;
		}

		public void setValue(final String value)
		{
			this.value = value;
		}
	}

	public void testField() throws Exception
	{
		final Stub mock = new Stub();

		mock.field = "my@";

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.field = "my@a.com";

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	public void testParameters() throws Exception
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getMethod("paramTest",
				new Class[]
				{ String.class });

		assertEquals(0, Purview.validateParameters(mock, method, new Object[]
		{ null }).getProblems().size());

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ "myhost" }).getProblems().size());

		assertEquals(0, Purview.validateParameters(mock, method, new Object[]
		{ "myaddress@myhost.com" }).getProblems().size());
	}

	public void testRequired() throws Exception
	{
		final Stub mock = new Stub();

		mock.required = "me@pureperfect.com";

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.required = null;

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.required = "";

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	public void testMethod() throws Exception
	{
		final Stub mock = new Stub();

		mock.setValue(null);

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("myname@foo.com");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("my.name@foo.com");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("my-name@foo.com");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("my_name@foo.com");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("my123name@foo.com");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("my.name@foo.com.");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("my.name@.foo.com");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("my.name@.foo.com");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("my.namefoo.com.");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("@foo.com.");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("my.name@.");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("my.name@");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testValidTlds() throws Exception
	{
		final TLDStub mock = new TLDStub();

		mock.setValue("fail@badtld.noway");

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());

		mock.setValue("rescue@pureperfect.com");

		assertEquals(0, Purview.validateMethods(mock).getProblems().size());
	}
}