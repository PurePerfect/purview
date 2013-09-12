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
 * Unit test for {@link MaxLength} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class MaxLengthTest extends TestCase
{
	public class Stub
	{
		@MaxLength(5)
		public String field;

		private String value;

		@MaxLength(4)
		public String getValue()
		{
			return this.value;
		}

		public void paramTest(@MaxLength(5) final String string)
		{
			// for testing
		}

		public void setValue(final String value)
		{
			this.value = value;
		}
	}

	public class StubTestRequired
	{
		private String value;

		@MaxLength(value = 4, required = true)
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

		mock.field = "adffff";

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	public void testMaxBoundsEqual() throws Exception
	{
		final Stub mock = new Stub();

		mock.setValue("asdf");

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testMaxBoundsOver() throws Exception
	{
		final Stub mock = new Stub();

		mock.setValue("asdfa");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testMaxBoundsUnder() throws Exception
	{
		final Stub mock = new Stub();

		mock.setValue("asdf");

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
		{ "asdfff" }).getProblems().size());

		assertEquals(0, Purview.validateParameters(mock, method, new Object[]
		{ "asff" }).getProblems().size());

		assertEquals(0, Purview.validateParameters(mock, method, new Object[]
		{ null }).getProblems().size());
	}

	public void testMethod()
	{
		final Stub mock = new Stub();

		mock.setValue("asdfa");

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}
	
	public void testRequired()
	{
		final StubTestRequired mock = new StubTestRequired();

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());

		mock.setValue("");

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());

		mock.setValue("asdf");

		assertEquals(0, Purview.validateMethods(mock).getProblems().size());
	}
}
