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

import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pureperfect.purview.Purview;

/***
 * Unit test for {@link False} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.3
 * @since 1.3
 */
public class FalseTest extends TestCase
{
	/**
	 * Stub class for testing.
	 */
	public class Stub
	{
		@False
		Boolean value;

		@False
		public Boolean getValue()
		{
			return this.value;
		}

		public void foo(@False Boolean bar)
		{
			// testing
		}
	}

	/**
	 * Test for null values.
	 */
	public void testFieldIsNull()
	{
		Stub stub = new Stub();

		assertEquals(1, Purview.validateFields(stub).getProblems().size());
	}

	/**
	 * Test for invalid values.
	 */
	public void testFieldIsNotValid()
	{
		Stub stub = new Stub();

		stub.value = Boolean.TRUE;

		assertEquals(1, Purview.validateFields(stub).getProblems().size());
	}

	/**
	 * Test for valid values.
	 */
	public void testFieldIsValid()
	{
		Stub stub = new Stub();

		stub.value = Boolean.FALSE;

		assertEquals(0, Purview.validateFields(stub).getProblems().size());
	}

	/**
	 * Test null value.
	 */
	public void testMethodIsNull()
	{
		Stub stub = new Stub();

		assertEquals(1, Purview.validateMethods(stub).getProblems().size());
	}

	/**
	 * Test valid value.
	 */
	public void testMethodIsValid()
	{
		Stub stub = new Stub();

		stub.value = Boolean.FALSE;

		assertEquals(0, Purview.validateMethods(stub).getProblems().size());
	}

	/**
	 * Test invalid value.
	 */
	public void testMethodIsNotValid()
	{
		Stub stub = new Stub();

		stub.value = Boolean.TRUE;

		assertEquals(1, Purview.validateMethods(stub).getProblems().size());
	}

	/**
	 * Test null parameter.
	 */
	public void testParameterIsNull() throws SecurityException, NoSuchMethodException
	{
		Stub stub = new Stub();

		Method m = stub.getClass().getMethod("foo", new Class[]
		{ Boolean.class });

		assertEquals(1, Purview.validateParameters(stub, m, new Object[]
		{ null }).getProblems().size());
	}
	
	/**
	 * Test valid parameter.
	 */
	public void testParameterIsValid() throws SecurityException, NoSuchMethodException
	{
		Stub stub = new Stub();

		Method m = stub.getClass().getMethod("foo", new Class[]
		{ Boolean.class });

		assertEquals(0, Purview.validateParameters(stub, m, new Object[]
		{ Boolean.FALSE }).getProblems().size());
	}
	
	/**
	 * Test invalid parameter.
	 */
	public void testParameterIsNotValid() throws SecurityException, NoSuchMethodException
	{
		Stub stub = new Stub();

		Method m = stub.getClass().getMethod("foo", new Class[]
		{ Boolean.class });

		assertEquals(1, Purview.validateParameters(stub, m, new Object[]
		{ Boolean.TRUE }).getProblems().size());
	}
}
