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
package com.pureperfect.purview.validators.biz;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pureperfect.purview.Purview;
import com.pureperfect.purview.util.GetterMethodFilter;
import com.pureperfect.purview.util.MakeAccessibleFieldFilter;

/**
 * Unit test for {@link SSN} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class SSNTest extends TestCase
{
	/**
	 * Stub class for testing
	 */
	public class Stub
	{
		private String both = "123456789";

		@SSN
		public String field;

		@SSN(required = true)
		public String required = "123456789";

		private String with = "123-45-6789";

		private String without = "123456789";

		@SSN
		public String getBoth()
		{
			return this.both;
		}

		@SSN(validationMode = SSN.DASHES)
		public String getWith()
		{
			return this.with;
		}

		@SSN(validationMode = SSN.NO_DASHES)
		public String getWithout()
		{
			return this.without;
		}

		public void paramTest(@SSN final String ssn)
		{
			// do nothing
		}

		public void setBoth(final String both)
		{
			this.both = both;
		}

		public void setWith(final String with)
		{
			this.with = with;
		}

		public void setWithout(final String without)
		{
			this.without = without;
		}
	}

	/**
	 * Stub class for testing.
	 */
	public void testBoth()
	{
		final Stub mock = new Stub();

		mock.setBoth("");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setBoth(null);

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setBoth("123456789");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setBoth("123-45-6789");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setBoth("12356789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setBoth("12a456789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setBoth("1234512789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testField() throws Exception
	{
		final Stub mock = new Stub();

		mock.field = "123456789";

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.field = "123-45-6789";

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.field = "12356789";

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.field = "12a456789";

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.field = "1234512789";

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/**
	 * S
	 */
	public void testParameters() throws Exception
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getMethod("paramTest",
				new Class[]
				{ String.class });

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ "1233331" }).getProblems().size());

		assertEquals(0, Purview.validateParameters(mock, method, new Object[]
		{ "123-45-6789" }).getProblems().size());
	}

	public void testRequired()
	{
		final Stub mock = new Stub();

		mock.required = "123456789";

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.required = null;

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	public void testWithDashes() throws Exception
	{
		final Stub mock = new Stub();

		mock.setWith("");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setWith(null);

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setWith("123-45-6789");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setWith("123345-6789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setWith("1-3-45-6789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setWith("123-4--6789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setWith("123-456789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testWithoutDashes()
	{
		final Stub mock = new Stub();

		mock.setWithout("");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setWithout(null);

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setWithout("123456789");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setWithout("12356789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setWithout("123-45-6789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setWithout("12a456789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setWithout("1234512789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}
}