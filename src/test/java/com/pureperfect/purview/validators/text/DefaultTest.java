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

import junit.framework.TestCase;

import com.pureperfect.purview.Purview;
import com.pureperfect.purview.ValidationException;

/**
 * Unit test for {@link Default} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.3
 * @since 1.3
 */
public class DefaultTest extends TestCase
{
	/*
	 * Stub test class.
	 */
	public class MethodStub
	{
		private String value;

		@Default("foo")
		public String getValue()
		{
			return this.value;
		}

		public void setValue(String value)
		{
			this.value = value;
		}
	}

	/*
	 * Stub test class.
	 */
	public class MethodStubMissingSetter
	{
		private String value;

		@Default("foo")
		public String getValue()
		{
			return this.value;
		}
	}

	/*
	 * Stub test class.
	 */
	public class FieldStub
	{
		@Default("bar")
		public String value;
	}

	/*
	 * Stub test class.
	 */
	public class MethodValueNotString
	{
		private Integer value;

		@Default("uhoh")
		public Integer getValue()
		{
			return this.value;
		}

		public void setValue(Integer value)
		{
			this.value = value;
		}
	}

	/*
	 * Stub test class.
	 */
	public class FieldValueNotString
	{
		@Default("foo")
		public Integer value;
	}

	/*
	 * Check for expected behavior when the annotation is attached to a field
	 * and the field is not populated.
	 */
	public void testFieldValueIsPopulated() throws Exception
	{
		FieldStub stub = new FieldStub();
		
		stub.value = "test";
		
		Purview.validateFields(stub);
		
		assertEquals("test", stub.value);
	}

	/*
	 * Check for expected behavior when the annotation is attached to a field
	 * and the field is not populated.
	 */
	public void testFieldValueIsNotPopulated() throws Exception
	{
		FieldStub stub = new FieldStub();

		Purview.validateFields(stub);

		assertEquals("bar", stub.value);
	}

	/*
	 * Check for expected behavior when the annotation is attached to a getter
	 * and the field is populated.
	 */
	public void testGetterMethodValueIsPopulated() throws Exception
	{
		MethodStub stub = new MethodStub();

		stub.setValue("bar");

		Purview.validateMethods(stub);

		assertEquals("bar", stub.getValue());
	}

	/*
	 * Check for expected behavior when the annotation is attached to a getter
	 * and the field is not populated.
	 */
	public void testGetterMethodValueIsNotPopulated() throws Exception
	{
		MethodStub stub = new MethodStub();

		Purview.validateMethods(stub);

		assertEquals("foo", stub.getValue());
	}

	/*
	 * Check for expected behavior when the annotation is attached to a getter
	 * without a corresponding setter.
	 */
	public void testGetterMethodDoesNotHaveCorrespondingSetter()
			throws Exception
	{
		MethodStubMissingSetter stub = new MethodStubMissingSetter();

		try
		{
			Purview.validateMethods(stub);
			fail("Should have thrown validation exception.");
		}
		catch (ValidationException e)
		{
			assertTrue(e.getCause() instanceof NoSuchMethodException);
		}
	}

	/*
	 * Check for expected behavior when the annotation is attached to a method
	 * that is not a string.
	 */
	public void testCorrespondingMethodValueIsNotAString()
	{
		MethodValueNotString stub = new MethodValueNotString();

		try
		{
			Purview.validateMethods(stub);
			fail("Should have thrown validation exception.");
		}
		catch (ValidationException e)
		{
			assertTrue(e.getCause() instanceof NoSuchMethodException);
		}
	}

	/*
	 * Check for expected behavior when the annotation is attached to a field
	 * that is not a string.
	 */
	public void testCorrespondingFieldValueIsNotAString()
	{
		FieldValueNotString stub = new FieldValueNotString();

		try
		{
			Purview.validateFields(stub);
		}
		catch (ValidationException e)
		{
			assertTrue(e.getCause() instanceof IllegalArgumentException);
		}
	}
	
	public class MethodWithString
	{
		public String value;
		
		@Default("fooBaroo")
		public void fooBar(String s)
		{
			this.value = s;
		}
	}
}
