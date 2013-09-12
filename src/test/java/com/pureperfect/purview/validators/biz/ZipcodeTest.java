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
 * Unit test for {@link ZipCode} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class ZipcodeTest extends TestCase
{
	public class Stub
	{
		private String both = "12345";

		@ZipCode
		public String field = "12345";

		private String five = "12345";

		private String nine = "12345-6789";

		private String required = "12345";

		@ZipCode
		public String getBoth()
		{
			return this.both;
		}

		public String getFive()
		{
			return this.five;
		}

		@ZipCode(validationMode = ZipCode.NINE_DIGIT)
		public String getNine()
		{
			return this.nine;
		}

		@ZipCode(required = true)
		public String getRequired()
		{
			return this.required;
		}

		@ZipCode(validationMode = ZipCode.FIVE_DIGIT)
		public String getWithout()
		{
			return this.five;
		}

		public void paramTest(@ZipCode final String zip)
		{
			// do nothing
		}

		public void setBoth(final String both)
		{
			this.both = both;
		}

		public void setFive(final String five)
		{
			this.five = five;
		}

		public void setNine(final String nine)
		{
			this.nine = nine;
		}

		public void setRequired(final String required)
		{
			this.required = required;
		}
	}

	public void testBoth()
	{
		final Stub mock = new Stub();

		mock.setBoth("12345");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setBoth("12345-6789");

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
	}

	public void testField() throws Exception
	{
		final Stub mock = new Stub();

		mock.field = null;

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.field = "12345";

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.field = "12345-6789";

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
	
	public void testMethod()
	{
		final Stub mock = new Stub();

		mock.setFive("12345");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setFive("1235-6789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setFive("123456");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setFive("12a456789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setFive("1234512789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setFive("");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setFive(null);

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testFive()
	{
		final Stub mock = new Stub();

		mock.setFive("12345");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setFive("1235-6789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setFive("123456");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setFive("12a456789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setFive("1234512789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setFive("");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setFive(null);

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

	}

	public void testNineDigit() throws Exception
	{
		final Stub mock = new Stub();

		mock.setNine("12345-6789");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setNine("12345");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setNine("1-3-45-6789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setNine("123-4--6789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setNine("123-456789");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setNine("");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setNine(null);

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testParameters() throws Exception
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getMethod("paramTest",
				new Class[]
				{ String.class });

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ "1234" }).getProblems().size());

		assertEquals(0, Purview.validateParameters(mock, method, new Object[]
		{ "12345-6789" }).getProblems().size());
	}

	public void testRequired()
	{
		final Stub mock = new Stub();

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setRequired(null);

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}
}