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
package com.pureperfect.purview.validators.numeric;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pureperfect.purview.Purview;
import com.pureperfect.purview.util.GetterMethodFilter;
import com.pureperfect.purview.util.MakeAccessibleFieldFilter;

/**
 * Unit test for {@link GreaterThan} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class GreaterThanTest extends TestCase
{
	public class Stub
	{
		@GreaterThan(25)
		public Long field = new Long(26);

		@GreaterThan(value = 5, required = true)
		public Float required = new Float(16);

		private Number value;

		@GreaterThan(25)
		public Number getValue()
		{
			return this.value;
		}

		public void paramTest(@GreaterThan(25) final Long id)
		{
			// for testing
		}

		public void setValue(final Number value)
		{
			this.value = value;
		}
	}

	public void testField() throws Exception
	{
		final Stub mock = new Stub();

		mock.field = new Long(10);

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.field = new Long(123);

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	public void testParameters() throws Exception
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getMethod("paramTest",
				new Class[]
				{ Long.class });

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ new Long(10) }).getProblems().size());

		assertEquals(0, Purview.validateParameters(mock, method, new Object[]
		{ new Long(123) }).getProblems().size());
	}

	public void testRequired() throws Exception
	{
		final Stub mock = new Stub();

		mock.required = new Float(5.00001);

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.required = new Float(4);

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.required = null;

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	public void testMethod() throws Exception
	{
		final Stub obj = new Stub();

		obj.setValue(new Long(26));

		assertEquals(
				0,
				Purview.validateMethods(obj,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		obj.setValue(new Double(25.000000001));

		assertEquals(
				0,
				Purview.validateMethods(obj,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		obj.setValue(new Double(25));

		assertEquals(
				1,
				Purview.validateMethods(obj,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}
}