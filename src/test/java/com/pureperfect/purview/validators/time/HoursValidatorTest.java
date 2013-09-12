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
package com.pureperfect.purview.validators.time;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pureperfect.purview.Purview;
import com.pureperfect.purview.util.GetterMethodFilter;
import com.pureperfect.purview.util.MakeAccessibleFieldFilter;

/***
 * Unit test for {@link Hours} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class HoursValidatorTest extends TestCase
{
	/**
	 * Stub for testing.
	 */
	public class Stub
	{
		@Hours
		public Integer field;

		@Hours(required = true)
		public Integer required = new Integer(3);

		private Number value;

		@Hours
		public Number getValue()
		{
			return this.value;
		}

		public void setValue(final Number value)
		{
			this.value = value;
		}

		public void testParams(@Hours final Integer hours)
		{
			// for testing
		}
	}

	/**
	 * Stub for testing.
	 */
	public class FloatStub
	{
		@Hours
		public float value;
	}

	/**
	 * Stub for testing.
	 */
	public class DoubleStub
	{
		@Hours
		public double value;
	}

	/**
	 * Test upper bounds.
	 */
	public void testFieldRangeMaxExceeded()
	{
		final Stub mock = new Stub();

		mock.field = new Integer(24);

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/**
	 * Test lower bounds.
	 */
	public void testFieldRangeMinExceeded()
	{
		final Stub mock = new Stub();

		mock.field = new Integer(-1);

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/**
	 * Test the full range of valid values.
	 */
	public void testFieldValidValues() throws Exception
	{
		final Stub mock = new Stub();

		for (int i = 0; i < 24; ++i)
		{
			mock.field = new Integer(i);

			assertEquals(
					0,
					Purview.validateFields(mock,
							new MakeAccessibleFieldFilter()).getProblems()
							.size());
		}
	}

	/**
	 * Test minimum bounds for method.
	 */
	public void testMethodMinExceeded() throws Exception
	{
		final Stub mock = new Stub();

		mock.setValue(new Integer(-1));

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	/**
	 * Test maximum bounds for method.
	 */
	public void testMethodMaxExceeded() throws Exception
	{
		final Stub mock = new Stub();

		mock.setValue(new Integer(24));

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	/**
	 * Test the range of valid values for a method.
	 */
	public void testMethodValidValues()
	{
		final Stub mock = new Stub();

		for (int i = 0; i < 24; ++i)
		{
			mock.setValue(new Integer(i));

			assertEquals(
					0,
					Purview.validateMethods(mock,
							GetterMethodFilter.defaultInstance()).getProblems()
							.size());
		}
	}

	/**
	 * Test minimum bounds for parameter.
	 */
	public void testParameterMinExceeded() throws Exception
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getMethod("testParams",
				new Class[]
				{ Integer.class });

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ new Integer(-1) }).getProblems().size());
	}

	/**
	 * Test maximum bounds for parameter.
	 */
	public void testParameterMaxExceeded() throws Exception
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getMethod("testParams",
				new Class[]
				{ Integer.class });

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ new Integer(24) }).getProblems().size());
	}

	/**
	 * Test valid range for paramter.
	 */
	public void testParameterValidValues() throws Exception
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getMethod("testParams",
				new Class[]
				{ Integer.class });

		for (int i = 0; i < 24; ++i)
		{
			assertEquals(0,
					Purview.validateParameters(mock, method, new Object[]
					{ new Integer(i) }).getProblems().size());
		}
	}

	/**
	 * Test required flag when populated.
	 */
	public void testRequiredSet()
	{
		final Stub mock = new Stub();

		mock.required = new Integer(23);

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/**
	 * Test required not set.
	 */
	public void testRequiredNotSet()
	{
		final Stub mock = new Stub();

		mock.required = null;

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/**
	 * Test float values.
	 */
	public void testFloat()
	{
		FloatStub stub = new FloatStub();

		stub.value = (float) 23.9999;

		assertEquals(0, Purview.validateMethods(stub).getProblems().size());
	}

	/**
	 * Test double values.
	 */
	public void testDouble()
	{
		DoubleStub stub = new DoubleStub();

		stub.value = 23.9999999999999999999;

		assertEquals(0, Purview.validateMethods(stub).getProblems().size());
	}
}
