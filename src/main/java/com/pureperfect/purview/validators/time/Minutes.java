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
package com.pureperfect.purview.validators.time;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pureperfect.purview.ValidationProblem;

/**
 * A value for minutes should be a <code>java.lang.Number</code> between 0 and
 * 60. These values are left bound inclusive and right bound exclusive (i.e. 0
 * >= valid < 60).
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
 *  &#064;Minutes
 *  private int min;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;Minutes
 *  public Integer getMinutes() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;Minutes Integer bar) {
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
public @interface Minutes
{
	/**
	 * Indicates that an invalid number of minutes was specified.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Problem extends
			ValidationProblem<Object, Minutes, Object, Number>
	{
		Problem(final Object instance, final Minutes annotation,
				final Object target, final Number value)
		{
			super(instance, annotation, target, value);
		}
	}

	/**
	 * A value for minutes should be a <code>java.lang.Number</code> between 0
	 * and 60. These values are left bound inclusive and right bound exclusive
	 * (i.e. 0 >= valid < 60).
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, Minutes, Object, Number>
	{
		private static final int MAX_MINS = 60;

		private static final int MIN_MINS = 0;

        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance,
				final Minutes annotation, final Object target,
				final Number value)
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
				final double val = value.intValue();

				if (val < MIN_MINS || val >= MAX_MINS)
				{
					return new Problem(instance, annotation, target, value);
				}
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
	 * will be allowed.
	 * 
	 * @return Whether or not the field is required.
	 */
	boolean required() default false;

	/**
	 * The {@link Minutes.Validator Validator} for this annotation.
	 * 
	 * @return the {@link Minutes.Validator} class
	 */
	Class<?> validator() default Validator.class;
}