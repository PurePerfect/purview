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
package com.pureperfect.purview.validators.numeric;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pureperfect.purview.ValidationProblem;

/**
 * Specify that a java.lang.Number (or a subclass) should be less than or equal
 * to a given number.
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
 *  &#064;LessThanOrEqualTo(5)
 *  private Integer value;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;LessThanOrEqualTo(5)
 *  public Integer getSeconds() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;LessThanOrEqualTo(5) Integer bar) {
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
public @interface LessThanOrEqualTo
{
	/**
	 * Indicates that the value was not less than or equal to the specified
	 * value.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Problem extends
			ValidationProblem<Object, LessThanOrEqualTo, Object, Number>
	{
		Problem(final Object instance, final LessThanOrEqualTo annotation,
				final Object target, final Number value)
		{
			super(instance, annotation, target, value);
		}
	}

	/**
	 * Specify that a java.lang.Number (or a subclass) should be less than or
	 * equal to a given number.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, LessThanOrEqualTo, Object, Number>
	{
        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance,
				final LessThanOrEqualTo annotation, final Object target,
				final Number number)
		{
			if (number == null)
			{
				if (annotation.required())
				{
					return new Problem(instance, annotation, target, number);
				}
			}
			else if (number.doubleValue() > annotation.value())
			{
				return new Problem(instance, annotation, target, number);
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
	 * @return whether or not the field is required.
	 */
	boolean required() default false;

	/**
	 * The {@link LessThanOrEqualTo.Validator Validator} for this annotation.
	 * 
	 * @return The {@link LessThanOrEqualTo.Validator} class
	 */
	Class<?> validator() default Validator.class;

	/**
	 * Specify what the value must be less than or equal to.
	 * 
	 * @return what the value must be less than or equal to.
	 */
	double value();
}