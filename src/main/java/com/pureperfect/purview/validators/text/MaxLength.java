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
 * Define the max length of a string.
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
 *  &#064;MaxLength(32)
 *  private String name;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;MaxLength(32)
 *  public String getName() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;MaxLength(32) String bar) {
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
public @interface MaxLength
{
	/**
	 * Indicates that the maximum length was exceeded.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Problem extends
			ValidationProblem<Object, MaxLength, Object, CharSequence>
	{
		Problem(final Object instance, final MaxLength annotation,
				final Object target, final CharSequence value)
		{
			super(instance, annotation, target, value);
		}

		/**
		 * Get the max length that was allowed.
		 * 
		 * @return the max length that was allowed.
		 */
		public int getMaxLength()
		{
			final MaxLength anno = this.getAnnotation();

			return anno.value();
		}
	}

	/**
	 * Define the max length of a string.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, MaxLength, Object, CharSequence>
	{
        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance,
				final MaxLength annotation, final Object target,
				final CharSequence value)
		{
			final int maxLength = annotation.value();

			if (value == null || value.length() < 1)
			{
				if (annotation.required())
				{
					return new Problem(instance, annotation, target, value);
				}
			}
			else if (value.length() > maxLength)
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
	 * Whether or not the field is required. If it is not required, null values
	 * and empty strings will be allowed.
	 * 
	 * @return Whether or not the field is required.
	 */
	boolean required() default false;

	/**
	 * The {@link MaxLength.Validator Validator} for this annotation.
	 * 
	 * @return the {@link MaxLength.Validator} class
	 */
	public Class<?> validator() default Validator.class;

	/**
	 * The maximum length that should be allowed.
	 * 
	 * @return the maximum length that should be allowed.
	 */
	int value();
}