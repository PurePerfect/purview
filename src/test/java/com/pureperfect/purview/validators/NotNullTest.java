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
import java.util.ResourceBundle;

import junit.framework.TestCase;

import com.pureperfect.purview.Purview;
import com.pureperfect.purview.ValidationProblem;
import com.pureperfect.purview.ValidationResults;
import com.pureperfect.purview.util.GetterMethodFilter;
import com.pureperfect.purview.util.MakeAccessibleFieldFilter;

/**
 * Unit test for {@link NotNull} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class NotNullTest extends TestCase
{
	/**
	 * Stub class for testing.
	 */
	public class Stub
	{
		private String name;

		@NotNull
		public Integer value;

		@NotNull(messageKey = "notNullOverride")
		public String getName()
		{
			return this.name;
		}

		public void setName(final String name)
		{
			this.name = name;
		}

		public void validateParams(@NotNull final String one)
		{
			// do nothing
		}
	}

	/**
	 * Test message key override.
	 */
	@SuppressWarnings("rawtypes")
	public void testMessageKey()
	{
		final Stub form = new Stub();

		final ValidationResults results = Purview.validateMethods(form,
				GetterMethodFilter.defaultInstance());

		final ValidationProblem problem = results.getProblems().iterator()
				.next();

		final ResourceBundle bundle = ResourceBundle
				.getBundle("com.pureperfect.purview.validators.bundle0");

		assertEquals("this is my override", problem.getLocalizedMessage(bundle));
	}
	
	/**
	 * Test null.
	 */
	public void testFieldIsNull()
	{
		final Stub form = new Stub();

		form.value = null;

		ValidationResults results = Purview.validateFields(form, new MakeAccessibleFieldFilter());

		assertEquals(1, results.getProblems().size());
	}
	
	/**
	 * Test not null.
	 */
	public void testFieldIsNotNull()
	{
		final Stub form = new Stub();

		form.value = new Integer(23);

		ValidationResults results = Purview.validateFields(form, new MakeAccessibleFieldFilter());

		assertEquals(0, results.getProblems().size());
	}

	/**
	 * Test null
	 */
	public void testMethodIsNull()
	{
		final Stub form = new Stub();

		ValidationResults results = Purview.validateMethods(form,
				GetterMethodFilter.defaultInstance());

		assertEquals(1, results.getProblems().size());
	}
	
	/**
	 * Test not null.
	 */
	public void testMethodIsNotNull()
	{
		final Stub form = new Stub();

		form.setName("asdf");
		
		ValidationResults results = Purview.validateMethods(form,
				GetterMethodFilter.defaultInstance());

		assertEquals(0, results.getProblems().size());
	}
	
	/**
	 * Test null.
	 */
	public void testParameterIsNull() throws Exception
	{
		final Stub form = new Stub();

		final Method toValidate = form.getClass().getDeclaredMethod(
				"validateParams", new Class[]
				{ String.class });

		ValidationResults results = Purview.validateParameters(form, toValidate, new Object[]
		{ null });

		assertEquals(1, results.getProblems().size());
	}

	/**
	 * Test not null.
	 */
	public void testParameterIsNotNull() throws Exception
	{
		final Stub form = new Stub();

		final Method toValidate = form.getClass().getDeclaredMethod(
				"validateParams", new Class[]
				{ String.class });

		ValidationResults results = Purview.validateParameters(form,
				toValidate, new Object[]
				{ "cool" });

		assertEquals(0, results.getProblems().size());
	}
}