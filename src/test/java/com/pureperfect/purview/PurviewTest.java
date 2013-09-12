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
package com.pureperfect.purview;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pureperfect.purview.util.GetterMethodFilter;
import com.pureperfect.purview.util.MakeAccessibleFieldFilter;
import com.pureperfect.purview.validators.NoValidation;
import com.pureperfect.purview.validators.NotNull;

/**
 * Unit test for {@link Purview}.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class PurviewTest extends TestCase
{
	/**
	 * Stub class for testing.
	 */
	public class FieldStub
	{
		@SuppressWarnings("unused")
		@NotNull
		private String name;

		public void setName(final String name)
		{
			this.name = name;
		}
	}

	/**
	 * Stub class for testing.
	 */
	public class FieldStubStrictFail
	{
		public String name;
	}

	/**
	 * Stub class for testing.
	 */
	public class FieldStubStrictPass
	{
		@NoValidation
		public String name;
	}

	/**
	 * Stub class for testing.
	 */
	public class MethodStub
	{
		private String name;

		@NotNull
		public String getName()
		{
			return this.name;
		}

		public void setName(final String name)
		{
			this.name = name;
		}
	}

	/**
	 * Stub class for testing.
	 */
	public class MethodStubStrictFail
	{
		private String name;

		public String getName()
		{
			return this.name;
		}

		public void setName(final String name)
		{
			this.name = name;
		}
	}

	/**
	 * Stub class for testing.
	 */
	public class MethodStubStrictPass
	{
		private String name;

		@NoValidation
		public String getName()
		{
			return this.name;
		}

		public void setName(final String name)
		{
			this.name = name;
		}
	}

	/**
	 * Stub class for testing.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	public @interface MockTypeValidator
	{
		@SuppressWarnings(
		{ "unchecked", "rawtypes" })
		public class ValidatorImpl
				implements
				Validator<ValidationProblem, Object, Annotation, Object, Object>
		{
			public ValidationProblem validate(final Object instance,
					final Annotation annotation, final Object target,
					final Object value)
			{
				return new ValidationProblem(null, null, null, null)
				{
					// mock
				};
			}
		}

		Class<?> validator() default ValidatorImpl.class;
	}

	/**
	 * Stub class for testing.
	 */
	@NonValidationAnnotation
	public class NonValidatedType
	{
		@NonValidationAnnotation
		public String getName()
		{
			return "chris";
		}
	}

	/**
	 * Stub class for testing.
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(
	{ ElementType.METHOD, ElementType.TYPE })
	public @interface NonValidationAnnotation
	{
		// do nothing
	}

	/**
	 * Stub class for testing.
	 */
	public class ParamStub
	{
		public final void testMe(@NotNull final String name)
		{
			// do nothing
		}
	}

	/**
	 * Stub class for testing.
	 */
	public class ParamStubStrictFail
	{
		public final void testMe(final String name)
		{
			// do nothing
		}
	}

	/**
	 * Stub class for testing.
	 */
	public class ParamStubStrictPass
	{
		public final void testMe(@NoValidation final String name)
		{
			// do nothing
		}
	}

	/**
	 * Stub class for testing.
	 */
	@MockTypeValidator
	public class TypeStubFail
	{
		// blah blah
	}

	/**
	 * Stub class for testing.
	 */
	public class TypeStubPass
	{
		// blah blah
	}

	/**
	 * Stub class for testing.
	 */
	public class TypeStubStrictFail
	{
		// blah blah
	}

	/**
	 * Stub class for testing.
	 */
	@NoValidation
	public class TypeStubStrictPass
	{
		// blah blah
	}

	/**
	 * Test validate fields.
	 */
	public void testValidateFields()
	{
		final FieldStub mockType = new FieldStub();

		assertEquals(1, Purview.validateFields(mockType).getProblems().size());

		mockType.setName("name");

		assertEquals(0, Purview.validateFields(mockType).getProblems().size());
	}

	/**
	 * Test validate fields with a custom filter.
	 */
	public void testValidateFieldsCustomFilter()
	{
		final FieldStub mockType = new FieldStub();

		assertEquals(
				1,
				Purview.validateFields(mockType,
						MakeAccessibleFieldFilter.defaultInstance())
						.getProblems().size());

		mockType.setName("name");

		assertEquals(
				0,
				Purview.validateFields(mockType,
						MakeAccessibleFieldFilter.defaultInstance())
						.getProblems().size());
	}

	/**
	 * Test strict mode for validate fields.
	 */
	public void testValidateFieldsStrict()
	{
		final FieldStub mockType = new FieldStub();

		assertEquals(1, Purview.validateFields(mockType).getProblems().size());

		mockType.setName("name");

		assertEquals(0, Purview.validateFields(mockType).getProblems().size());

		// Test missing NoValidation annotation.
		final FieldStubStrictFail fail = new FieldStubStrictFail();

		try
		{
			Purview.validateFields(fail, true);
			fail("Should have thrown exception");
		}
		catch (final ValidationException e)
		{
			// should have thrown exception
			System.err.println(e.getMessage());
		}

		// Test NoValidation annotation
		final FieldStubStrictPass pass = new FieldStubStrictPass();

		assertEquals(0, Purview.validateFields(pass, true).getProblems().size());
	}

	/**
	 * Test strict mode for validate fields with a custom filter.
	 */
	public void testValidateFieldsStrictCustomFilter()
	{
		final FieldStub mockType = new FieldStub();

		assertEquals(1, Purview.validateFields(mockType).getProblems().size());

		mockType.setName("name");

		assertEquals(0, Purview.validateFields(mockType).getProblems().size());

		// Test missing NoValidation annotation.
		final FieldStubStrictFail fail = new FieldStubStrictFail();

		try
		{
			Purview.validateFields(fail,
					MakeAccessibleFieldFilter.defaultInstance(), true);
			fail("Should have thrown exception");
		}
		catch (final ValidationException e)
		{
			// should have thrown exception
			System.err.println(e.getMessage());
		}

		// Test NoValidation annotation
		final FieldStubStrictPass pass = new FieldStubStrictPass();

		assertEquals(
				0,
				Purview.validateFields(pass,
						MakeAccessibleFieldFilter.defaultInstance(), true)
						.getProblems().size());
	}

	/**
	 * Test validate methods.
	 */
	public void testValidateMethods()
	{
		final MethodStub mockType = new MethodStub();

		assertEquals(1, Purview.validateMethods(mockType).getProblems().size());

		mockType.setName("name");

		assertEquals(0, Purview.validateMethods(mockType).getProblems().size());
	}

	/**
	 * Test validate methods with a custom filter.
	 */
	public void testValidateMethodsCustomFilter()
	{
		final MethodStub mockType = new MethodStub();

		assertEquals(
				1,
				Purview.validateMethods(mockType,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());

		mockType.setName("name");

		assertEquals(
				0,
				Purview.validateMethods(mockType,
						GetterMethodFilter.defaultInstance()).getProblems()
						.size());
	}

	/**
	 * Test validate methods in strict mode.
	 */
	public void testValidateMethodsStrict()
	{
		// Test normal
		final MethodStub mockType = new MethodStub();

		assertEquals(1, Purview.validateMethods(mockType, true).getProblems()
				.size());

		mockType.setName("name");

		assertEquals(0, Purview.validateMethods(mockType, true).getProblems()
				.size());

		// Test strict mode missing annotation
		final MethodStubStrictFail fail = new MethodStubStrictFail();

		try
		{
			Purview.validateMethods(fail, true);
			fail("Should have thrown validation exception");
		}
		catch (final ValidationException e)
		{
			// Okay. Strict mode is on so it should have failed.
			System.err.println(e.getMessage());
		}

		final MethodStubStrictPass pass = new MethodStubStrictPass();

		assertEquals(0, Purview.validateMethods(pass, true).getProblems()
				.size());
	}

	/**
	 * Test validate methods in strict mode with a custom filter.
	 */
	public void testValidateMethodsStrictCustomFilter()
	{
		// Test normal
		final MethodStub mockType = new MethodStub();

		assertEquals(
				1,
				Purview.validateMethods(mockType,
						GetterMethodFilter.defaultInstance(), true)
						.getProblems().size());

		mockType.setName("name");

		assertEquals(
				0,
				Purview.validateMethods(mockType,
						GetterMethodFilter.defaultInstance(), true)
						.getProblems().size());

		// Test strict mode missing annotation
		final MethodStubStrictFail fail = new MethodStubStrictFail();

		try
		{
			Purview.validateMethods(fail, GetterMethodFilter.defaultInstance(),
					true);
			fail("Should have thrown validation exception");
		}
		catch (final ValidationException e)
		{
			// Okay. Strict mode is on so it should have failed.
			System.err.println(e.getMessage());
		}

		final MethodStubStrictPass pass = new MethodStubStrictPass();

		assertEquals(
				0,
				Purview.validateMethods(pass,
						GetterMethodFilter.defaultInstance(), true)
						.getProblems().size());
	}

	/**
	 * Test validate parameters.
	 */
	@SuppressWarnings("rawtypes")
	public void testValidateParameters() throws SecurityException,
			NoSuchMethodException
	{
		final ParamStub mock = new ParamStub();
		final Class[] paramTypes = new Class[]
		{ String.class };
		final Method method = mock.getClass().getMethod("testMe", paramTypes);

		assertEquals(1, Purview.validateParameters(mock, method, new Object[]
		{ null }).getProblems().size());

		assertEquals(0, Purview.validateParameters(mock, method, new Object[]
		{ "should pass" }).getProblems().size());
	}

	/**
	 * Test validate parameters in strict mode.
	 */
	@SuppressWarnings("rawtypes")
	public void testValidateParametersStrict() throws SecurityException,
			NoSuchMethodException
	{
		final ParamStubStrictPass pass = new ParamStubStrictPass();
		final Class[] paramTypes = new Class[]
		{ String.class };
		final Method passMethod = pass.getClass().getMethod("testMe",
				paramTypes);

		assertEquals(0,
				Purview.validateParameters(pass, passMethod, new Object[]
				{ null }, true).getProblems().size());

		final ParamStubStrictFail fail = new ParamStubStrictFail();
		final Method failMethod = fail.getClass().getMethod("testMe",
				paramTypes);

		try
		{
			Purview.validateParameters(fail, failMethod, new Object[]
			{ null }, true);
			fail("Should have thrown an exception");
		}
		catch (final ValidationException e)
		{
			// should throw exception
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Test validate type.
	 */
	public void testValidateType()
	{
		final Object pass = new TypeStubPass();

		assertEquals(0, Purview.validateType(pass).getProblems().size());

		final Object fail = new TypeStubFail();

		assertEquals(1, Purview.validateType(fail).getProblems().size());
	}

	/**
	 * Test validate type in strict mode.
	 */
	public void testValidateTypeStrict()
	{
		final Object pass = new TypeStubStrictPass();

		assertEquals(0, Purview.validateType(pass, true).getProblems().size());

		final Object fail = new TypeStubStrictFail();

		try
		{
			Purview.validateType(fail, true);
			fail("Should have thrown exception");
		}
		catch (final ValidationException e)
		{
			// should have thrown exception
			System.err.println(e.getMessage());
		}
	}
}