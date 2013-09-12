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
 * A valid value for hours is a <code>java.lang.Number</code> between 0 and 24.
 * These values are left bound inclusive and right bound exclusive (i.e. 0 >=
 * valid < 24).
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
 *  &#064;Hours
 *  private int hours;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;Hours
 *  public Integer getHours() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;Hours Integer bar) {
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
public @interface Hours
{
	/**
	 * Indicates that an invalid number of hours was specified.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Problem extends
			ValidationProblem<Object, Hours, Object, Number>
	{
		Problem(final Object instance, final Hours annotation,
				final Object target, final Number value)
		{
			super(instance, annotation, target, value);
		}
	}

	/**
	 * A valid value for hours is a <code>java.lang.Number</code> between 0 and
	 * 24. These values are left bound inclusive and right bound exclusive (i.e.
	 * 0 >= valid < 24).
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, Hours, Object, Number>
	{
		private static final int MAX_HRS = 24;

		private static final int MIN_HRS = 0;

        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance, final Hours annotation,
				final Object target, final Number value)
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

				if (val < MIN_HRS || val >= MAX_HRS)
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
	 * The {@link Hours.Validator Validator} for this annotation.
	 * 
	 * @return the {@link Hours.Validator} class
	 */
	Class<?> validator() default Validator.class;
}