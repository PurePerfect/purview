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

/**
 * Unit test for {@link Minutes} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class MinutesValidatorTest extends TestCase
{
	/**
	 * Stub class for testing.
	 */
	public class Stub
	{
		@Minutes
		public Integer field;

		@Minutes(required = true)
		public Integer required = new Integer(5);

		private Number value;

		@Minutes
		public Number getValue()
		{
			return this.value;
		}

		public void parameterTest(@Minutes final Integer mins)
		{
			// for testing
		}

		public void setValue(final Number value)
		{
			this.value = value;
		}
	}

	/**
	 * Stub class for testing.
	 */
	public class FloatStub
	{
		@Minutes
		public float value;
	}
	
	/**
	 * Stub class for testing.
	 */
	public class DoubleStub
	{
		@Minutes
		public double value;
	}
	
	/**
	 * Test max for field.
	 */
	public void testFieldMaxExceeded()
	{
		final Stub mock = new Stub();

		mock.field = new Integer(60);

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/**
	 * Test min for field.
	 */
	public void testFieldMinExceeded()
	{
		final Stub mock = new Stub();

		mock.field = new Integer(-1);

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/**
	 * Test range of valid values for field.
	 */
	public void testFieldValidValues()
	{
		final Stub mock = new Stub();

		for (int i = 0; i < 60; ++i)
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
	 * Test parameter mins.
	 */
	public void testParameterMinExceeded() throws SecurityException,
			NoSuchMethodException
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getMethod("parameterTest",
				new Class[]
				{ Integer.class });

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ new Integer(-1) }).getProblems().size());
	}

	/**
	 * Test parameter max.
	 */
	public void testParameterMaxExceeded() throws SecurityException,
			NoSuchMethodException
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getMethod("parameterTest",
				new Class[]
				{ Integer.class });

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ new Integer(60) }).getProblems().size());
	}

	/**
	 * Test range of valid values for parameters.
	 */
	public void testParameterValidValues() throws SecurityException,
			NoSuchMethodException
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getMethod("parameterTest",
				new Class[]
				{ Integer.class });

		for (int i = 0; i < 60; ++i)
		{
			assertEquals(0,
					Purview.validateParameters(mock, method, new Object[]
					{ new Integer(i) }).getProblems().size());
		}
	}

	/**
	 * Test method lower bounds.
	 */
	public void testMethodMinExceeded()
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
	 * Test method upper bounds.
	 */
	public void testMethodMaxExceeded()
	{
		final Stub mock = new Stub();

		mock.setValue(new Integer(60));

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	/**
	 * Test range of valid values for method.
	 */
	public void testMethodValidValues()
	{
		final Stub mock = new Stub();

		for (int i = 0; i < 60; ++i)
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
	 * Test when required value is set.
	 */
	public void testRequiredSet()
	{
		final Stub mock = new Stub();

		mock.required = new Integer(6);

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}
	
	/**
	 * Test when required value not set.
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

		stub.value = (float) 59.9999;

		assertEquals(0, Purview.validateMethods(stub).getProblems().size());
	}

	/**
	 * Test double values.
	 */
	public void testDouble()
	{
		DoubleStub stub = new DoubleStub();

		stub.value = 59.9999999999999999999;

		assertEquals(0, Purview.validateMethods(stub).getProblems().size());
	}
}
