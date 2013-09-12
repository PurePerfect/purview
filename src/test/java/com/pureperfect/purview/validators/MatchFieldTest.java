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
package com.pureperfect.purview.validators;

import junit.framework.TestCase;

import com.pureperfect.purview.Purview;
import com.pureperfect.purview.util.GetterMethodFilter;
import com.pureperfect.purview.util.MakeAccessibleFieldFilter;

/**
 * Unit test for {@link MatchField} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class MatchFieldTest extends TestCase
{
	/**
	 * Stub class for testing.
	 * 
	 */
	public class Stub
	{
		@MatchField("expected")
		public String actual;

		public String expected;
		
		public String method;
		
		@MatchField("expected")
		public String getMethod()
		{
			return this.method;
		}
	}

	/**
	 * Test actual value is null for field.
	 */
	public void testFieldActualNull()
	{
		final Stub mock = new Stub();

		mock.expected = "foo";
		
		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/**
	 * Test expected value is null for field.
	 */
	public void testFieldExpectedNull()
	{
		final Stub mock = new Stub();

		mock.actual = "actual";

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/**
	 * Test matching values for field.
	 */
	public void testFieldMatch()
	{
		final Stub mock = new Stub();

		mock.expected = "expected";
		mock.actual = "expected";

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/**
	 * Test mismatched values for field.
	 */
	public void testFieldMismatch()
	{
		final Stub mock = new Stub();

		mock.actual = "nope";
		mock.expected = "expected";

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/**
	 * Test method actual value is null.
	 */
	public void testMethodActualNull()
	{
		final Stub mock = new Stub();

		mock.expected = "expected";
		
		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	/**
	 * Test method expected value is null.
	 */
	public void testMethodExpectedNull()
	{
		final Stub mock = new Stub();

		mock.method = "expected";
		
		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	/**
	 * Test matching values for method.
	 */
	public void testMethodMatch()
	{
		final Stub mock = new Stub();

		mock.expected = "woot";
		mock.method = "woot";
		
		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	/**
	 * Test mismatched values for method.
	 */
	public void testMethodMismatched()
	{
		final Stub mock = new Stub();

		mock.expected = "woot";
		mock.method = "awwww";
		
		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}
}
