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
 * Unit test for {@link Seconds} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class SecondsValidatorTest extends TestCase
{
	/****
	 * Stub class for testing.
	 */
	public class Stub
	{
		@Seconds
		public Long field;

		@Seconds(required = true)
		public Long required = new Long(1);

		private Number value;

		@Seconds
		public Number getValue()
		{
			return this.value;
		}

		public void paramTest(@Seconds final Integer seconds)
		{
			// for testing
		}

		public void setValue(final Number value)
		{
			this.value = value;
		}
	}

	/*
	 * Stub class for testing.
	 */
	public class FloatStub
	{
		@Seconds
		public float value;
	}

	/*
	 * Stub class for testing.
	 */
	public class DoubleStub
	{
		@Seconds
		public double value;
	}

	/*
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

	/*
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

	/*
	 * Test all valid values for method.
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

	/*
	 * Test parameter upper bounds.
	 */
	public void testParameterMaxExceeded() throws SecurityException,
			NoSuchMethodException
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getDeclaredMethod("paramTest",
				new Class[]
				{ Integer.class });

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ new Long(60) }).getProblems().size());
	}

	/*
	 * Test parameter lower bounds.
	 */
	public void testParameterMinExceeded() throws SecurityException,
			NoSuchMethodException
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getDeclaredMethod("paramTest",
				new Class[]
				{ Integer.class });

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ new Long(-1) }).getProblems().size());
	}

	/*
	 * Test all valid values for parameter.
	 */
	public void testParameterValidValues() throws SecurityException,
			NoSuchMethodException
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getDeclaredMethod("paramTest",
				new Class[]
				{ Integer.class });

		for (int i = 0; i < 60; ++i)
		{
			assertEquals(0,
					Purview.validateParameters(mock, method, new Object[]
					{ new Long(i) }).getProblems().size());
		}
	}

	/*
	 * Test field upper bounds.
	 */
	public void testFieldMaxExceeded()
	{
		final Stub mock = new Stub();

		mock.field = new Long(60);

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/*
	 * Test field lower bounds.
	 */
	public void testFieldMinExceeded()
	{
		final Stub mock = new Stub();

		mock.field = new Long(-1);

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/*
	 * Test all valid values for field.
	 */
	public void testFieldValidValues()
	{
		final Stub mock = new Stub();

		for (int i = 0; i < 60; ++i)
		{
			mock.field = new Long(i);

			assertEquals(
					0,
					Purview.validateFields(mock,
							new MakeAccessibleFieldFilter()).getProblems()
							.size());
		}
	}

	/*
	 * Test required value present.
	 */
	public void testRequiredSet()
	{
		final Stub mock = new Stub();

		mock.required = new Long(1);

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/*
	 * Test that required field throws error when null.
	 */
	public void testRequiredNotSet()
	{
		final Stub mock = new Stub();

		mock.required = null;

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	/*
	 * Test float values.
	 */
	public void testFloat()
	{
		FloatStub stub = new FloatStub();

		stub.value = (float) 59.9999;

		assertEquals(0, Purview.validateMethods(stub).getProblems().size());
	}

	/*
	 * Test double values.
	 */
	public void testDouble()
	{
		DoubleStub stub = new DoubleStub();

		stub.value = 59.9999999999999999999;

		assertEquals(0, Purview.validateMethods(stub).getProblems().size());
	}
}
