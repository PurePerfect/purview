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
 * Unit test for {@link Id} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class IdentifierTest extends TestCase
{
	public class Stub
	{
		@Id
		public Integer field;

		@Id(required = true)
		public Integer required = new Integer(23);

		private Integer value;

		@Id
		public Integer getValue()
		{
			return this.value;
		}

		public void paramTest(@Id final Integer id)
		{
			// for testing
		}

		public void setValue(final Integer value)
		{
			this.value = value;
		}
	}

	public class LongStub
	{
		@Id
		public Long field = new Long(1);

		@Id(required = true)
		public Long required = new Long(5);

		private Long value;

		@Id
		public Long getValue()
		{
			return this.value;
		}

		public void paramTest(@Id final Long id)
		{
			// for testing
		}

		public void setValue(final Long value)
		{
			this.value = value;
		}
	}
	
	public void testIntField() throws Exception
	{
		final Stub mock = new Stub();

		mock.field = new Integer(-1);

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.field = new Integer(23);

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	public void testIntNegative() throws Exception
	{
		final Stub mock = new Stub();

		mock.setValue(new Integer(-23));

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testIntParameters() throws Exception
	{
		final Stub mock = new Stub();

		final Method method = mock.getClass().getMethod("paramTest",
				new Class[]
				{ Integer.class });

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ new Integer(-1) }).getProblems().size());

		assertEquals(0, Purview.validateParameters(mock, method, new Object[]
		{ new Integer(23) }).getProblems().size());
	}

	public void testIntRequired() throws Exception
	{
		final Stub mock = new Stub();

		mock.required = new Integer(1);

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.required = null;

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	public void testMethod() throws Exception
	{
		final Stub mock = new Stub();

		mock.setValue(new Integer(23));

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue(null);

		assertEquals(
				0,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mock.setValue(new Integer(-1));

		assertEquals(
				1,
				Purview.validateMethods(mock,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testLongField() throws Exception
	{
		final LongStub mock = new LongStub();

		mock.field = new Long(-1);

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.field = new Long(23);

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	public void testLongNegative() throws Exception
	{
		final LongStub obj = new LongStub();

		obj.setValue(new Long(-23));

		assertEquals(
				1,
				Purview.validateMethods(obj,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	public void testLongParameters() throws Exception
	{
		final LongStub mock = new LongStub();

		final Method method = mock.getClass().getMethod("paramTest",
				new Class[]
				{ Long.class });

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ new Long(-1) }).getProblems().size());

		assertEquals(0, Purview.validateParameters(mock, method, new Object[]
		{ new Long(23) }).getProblems().size());
	}

	public void testLongRequired() throws Exception
	{
		final LongStub mock = new LongStub();

		mock.required = new Long(1);

		assertEquals(0,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());

		mock.required = null;

		assertEquals(1,
				Purview.validateFields(mock, new MakeAccessibleFieldFilter())
						.getProblems().size());
	}

	public void testLongValid() throws Exception
	{
		final LongStub obj = new LongStub();

		obj.setValue(new Long(23));

		assertEquals(
				0,
				Purview.validateMethods(obj,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}
}