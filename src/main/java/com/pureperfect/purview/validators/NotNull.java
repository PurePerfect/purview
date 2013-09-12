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

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pureperfect.purview.ValidationProblem;

/**
 * Mark a field as not allowing null values.
 * 
 * <p>
 * E.G:
 * </p>
 * 
 * <pre>
 * 
 * public class MyClass
 * {
 *  //When validating fields
 *  &#064;NotNull
 *  private String value;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;NotNull
 *  public String getValue() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;NotNull String bar) {
 *    ...
 *  }
 * }
 * </pre>
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface NotNull
{
	/**
	 * Indicates that the field was null.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Problem extends
			ValidationProblem<Object, Annotation, Object, Object>
	{
		public Problem(final Object instance, final Annotation annotation,
				final Object target, final Object value)
		{
			super(instance, annotation, target, value);
		}
	}

	/**
	 * Validator to check and see if the field is null. If it is an instance of
	 * {@link com.pureperfect.purview.ValidationProblem} will be returned.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, NotNull, Object, Object>
	{
        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance,
				final NotNull annotation, final Object target,
				final Object value)
		{
			if (value == null)
			{
				return new Problem(instance, annotation, target, value);
			}

			return null;
		}
	}

	/**
	 * Optional message key to use to define an i18n message.
	 * 
	 * @return Optional message key to use to define an i18n message.
	 */
	String messageKey() default "";

	/**
	 * The {@link NotNull.Validator Validator} for this annotation.
	 * 
	 * @return the {@link NotNull.Validator} class
	 */
	Class<?> validator() default Validator.class;
}