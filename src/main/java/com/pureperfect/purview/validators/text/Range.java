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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pureperfect.purview.ValidationProblem;

/**
 * Specify a range for a string. TODO convert to 1..4 notation.
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
 *  &#064;Range(min=8, max=32)
 *  private String name;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;Range(min=8, max=32)
 *  public String getName() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;Range(min=8, max=32) String bar) {
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
public @interface Range
{
	/**
	 * Problem returned when the string is not within the specified range.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Problem extends
			ValidationProblem<Object, Range, Object, CharSequence>
	{
		Problem(final Object instance, final Range annotation,
				final Object target, final CharSequence value)
		{
			super(instance, annotation, target, value);
		}

		/**
		 * The max length specified by the annotation.
		 * 
		 * @return The max length specified by the annotation.
		 */
		public int getMaxLength()
		{
			return this.getAnnotation().max();
		}

		/**
		 * The min length specified by the annotation.
		 * 
		 * @return The min length specified by the annotation.
		 */
		public int getMinLength()
		{
			return this.getAnnotation().min();
		}
	}

	/**
	 * Specify a range for a string.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, Range, Object, CharSequence>
	{
        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance, final Range annotation,
				final Object target, final CharSequence value)
		{
			if (value == null)
			{
				if (annotation.required())
				{
					return new Problem(instance, annotation, target, value);
				}
			}
			else
			{
				final int minLength = annotation.min();
				final int maxLength = annotation.max();

				int length = value.length();
				
				if (length > maxLength || length < minLength)
				{
					return new Problem(instance, annotation, target, value);
				}

			}

			return null;
		}
	}

	/**
	 * The max value right-bound inclusive.
	 * 
	 * @return The max value right-bound inclusive.
	 */
	int max();

	/**
	 * Optional message key to use to define an i18n message.
	 * 
	 * @return Optional message key to use to define an i18n message.
	 */
	String messageKey() default "";

	/**
	 * The min value left-bound inclusive.
	 * 
	 * @return The min value left-bound inclusive.
	 */
	int min();

	/**
	 * Whether or not the field is required. If it is not required, null values
	 * and empty strings will be allowed.
	 * 
	 * @return Whether or not the field is required.
	 */
	boolean required() default false;

	/**
	 * The {@link Range.Validator Validator} for this annotation.
	 * 
	 * @return the {@link Range.Validator} class
	 */
	Class<?> validator() default Validator.class;
}