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
 * Unit test for {@link LessThanOrEqualTo} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class LessThanOrEqualTest extends TestCase
{
	public class Stub
	{
		@LessThanOrEqualTo(25.9999)
		public Long field = new Long(24);

		@LessThanOrEqualTo(value = 5, required = true)
		public Float required = new Float(1);

		private Number value;

		@LessThanOrEqualTo(25.9999)
		public Number getValue()
		{
			return this.value;
		}

		public void paramTest(@LessThanOrEqualTo(25) final Long id)
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

		mock.field = new Long(100);

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.field = new Long(23);

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
		{ new Long(100) }).getProblems().size());

		assertEquals(0, Purview.validateParameters(mock, method, new Object[]
		{ new Long(23) }).getProblems().size());
	}

	public void testRequired() throws Exception
	{
		final Stub mock = new Stub();

		mock.required = new Float(3);

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.required = new Float(6);

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

		obj.setValue(new Double(25.9999));

		assertEquals(
				0,
				Purview.validateMethods(obj,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		obj.setValue(new Double(24));

		assertEquals(
				0,
				Purview.validateMethods(obj,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		obj.setValue(new Double(25.999900001));

		assertEquals(
				1,
				Purview.validateMethods(obj,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}
}