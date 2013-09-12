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
 * Unit test for {@link Domain} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class DomainTest extends TestCase
{
	//TODO this package
	
	public class Stub
	{
		@Domain
		public String field = "myhost.com";

		@Domain(required = true)
		public String required = "myhost.com";

		private String value = "myhost.com";

		@Domain
		public String getValue()
		{
			return this.value;
		}

		public void paramTest(@Host final String address)
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
		private String value = "myhost.com";

		@Domain(validateTlds = true)
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

		mock.field = "123...123.123.123";

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.field = "123.com";

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

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ "my@host" }).getProblems().size());

		assertEquals(0, Purview.validateParameters(mock, method, new Object[]
		{ "myhost" }).getProblems().size());
	}

	public void testRequired() throws Exception
	{
		final Stub mock = new Stub();

		mock.required = "asdf.foo.com";

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.required = null;

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	public void testMethod() throws Exception
	{
		final Stub mock = new Stub();

		mock.setValue(".asdf.foo.com");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("asdf.foo.com.");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("asdf..foo.com");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("asdf.foo.com");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("as-df.foo.com");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("as_df.foo.com");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("123.foo.com");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("123.com");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testValidateTLDs()
	{
		final TLDStub mock = new TLDStub();

		mock.setValue("host.badtld");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue("host.com");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}
}