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
package com.pureperfect.purview.validators.text;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pureperfect.purview.Purview;
import com.pureperfect.purview.util.GetterMethodFilter;
import com.pureperfect.purview.util.MakeAccessibleFieldFilter;

/**
 * Unit test for {@link Range} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class RangeTest extends TestCase
{
	public class Stub
	{
		@Range(min = 4, max = 100)
		public String field;

		private String value;

		@Range(min = 4, max = 8)
		public String getValue()
		{
			return this.value;
		}

		public void paramTest(@Range(min = 5, max = 100) final String string)
		{
			// for testing
		}

		public void setValue(final String value)
		{
			this.value = value;
		}
	}

	public class StubRequired
	{
		@Range(min = 4, max = 100, required = true)
		public String field;

		private String value;

		@Range(min = 4, max = 8, required = true)
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

		mock.field = "aff";

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.field = "asdff";

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	public void testMaxBoundsEqual() throws Exception
	{
		final Stub mock = new Stub();

		mock.setValue("adsfffff");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testMaxBoundsOver() throws Exception
	{
		final Stub mock = new Stub();

		mock.setValue("adsffffff");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testMinBoundsEqual() throws Exception
	{
		final Stub mock = new Stub();

		mock.setValue("adsf");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testMinBoundsUnder() throws Exception
	{
		final Stub mock = new Stub();

		mock.setValue("ads");

		assertEquals(
				1,
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
		{ "a" }).getProblems().size());

		assertEquals(0, Purview.validateParameters(mock, method, new Object[]
		{ "asfff" }).getProblems().size());
	}
	
	public void testMethod()
	{
		final Stub mock = new Stub();

		mock.setValue("ads");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testRequired()
	{
		final StubRequired mock = new StubRequired();

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());

		mock.setValue("");

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());

		mock.setValue("asdfsdf");

		assertEquals(0, Purview.validateMethods(mock).getProblems().size());
	}
}