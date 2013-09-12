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
 * A whole number that is greater than zero.
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
 *  &#064;Id
 *  private Integer value;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;Id
 *  public Integer getSeconds() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;Id Integer bar) {
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
public @interface Id
{
	/**
	 * Indicates that the value was not a valid identifier.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Problem extends ValidationProblem<Object, Id, Object, Number>
	{
		Problem(final Object instance, final Id annotation,
				final Object target, final Number value)
		{
			super(instance, annotation, target, value);
		}
	}

	/**
	 * A whole number that is greater than zero.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, Id, Object, Number>
	{
        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance, final Id annotation,
				final Object target, final Number id)
		{
			if (id == null)
			{
				if (annotation.required())
				{
					return new Problem(instance, annotation, target, id);
				}
			}
			else if (id.longValue() < 0)
			{
				return new Problem(instance, annotation, target, id);
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
	 * The {@link Id.Validator Validator} for this annotation.
	 * 
	 * @return The {@link Id.Validator} class
	 */
	Class<?> validator() default Validator.class;
}