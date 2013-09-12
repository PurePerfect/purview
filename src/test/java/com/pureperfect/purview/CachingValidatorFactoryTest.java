package com.pureperfect.purview;

import java.lang.annotation.Annotation;

import junit.framework.TestCase;

import com.pureperfect.purview.validators.NoValidation;
import com.pureperfect.purview.validators.NotNull;

/**
 * Unit test for {@link CachingValidatorFactory}.
 * 
 * @author J. Chris Folsom
 * @since 1.3
 * @since 1.3
 */
public class CachingValidatorFactoryTest extends TestCase
{
	/**
	 * Stub class for testing.
	 */
	public @interface NotValidationAnnotation
	{
		//stupid test class
	}
	
	/**
	 * Stub class for testing.
	 */
	@NoValidation
	public class BadStub
	{
		//stub
	}
	
	/**
	 * Stub class for testing.
	 */
	public class GoodStub
	{
		@NotNull
		public String field;
	}
	
	/**
	 * Test that validators get created appropriately.
	 */
	@SuppressWarnings("rawtypes")
	public void testValidationAnnotation() throws SecurityException, NoSuchFieldException
	{
		final Class<?> clazz = GoodStub.class;

		final Annotation[] annotations = clazz.getField("field").getDeclaredAnnotations();

		ValidatorFactory factory = new CachingValidatorFactory();
		
		Validator v = factory.createValidator(annotations[0]);
	
		assertTrue(v instanceof NotNull.Validator);
		
		//A new instance should not have been created.
		assertSame(v, factory.createValidator(annotations[0]));
	}
	
	/**
	 * Make sure annotations that are not valid validation annotations return null.
	 */
	public void testNonValidationAnnotation()
	{
		final Class<?> clazz = BadStub.class;

		final Annotation[] annotations = clazz.getDeclaredAnnotations();

		ValidatorFactory factory = new CachingValidatorFactory();
		
		assertNull(factory.createValidator(annotations[0]));
	}
}
