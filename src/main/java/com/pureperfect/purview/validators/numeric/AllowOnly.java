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
 * Make sure the value to which this annotation is attached is one of the items
 * in the specified list of allowed values.
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
 *  &#064;AllowOnly({2, 4, 6, 8})
 *  private Long name;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;AllowOnly({2, 4, 6, 8})
 *  public Integer getName() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#064;AllowOnly({2, 4, 6, 8}) Double bar) {
 *    ...
 *  }
 * }
 * </pre>
 * 
 * @author J. Chris Folsom
 * @version 1.3
 * @since 1.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface AllowOnly
{
	/**
	 * Indicates that the specified value was not in the provided list of
	 * allowed values.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.3
	 * @since 1.3
	 */
	public class Problem extends
			ValidationProblem<Object, AllowOnly, Object, Number>
	{
		Problem(final Object instance, final AllowOnly annotation,
				final Object target, final Number value)
		{
			super(instance, annotation, target, value);
		}
	}

	/**
	 * Make sure the value to which this annotation is attached is one of the
	 * items in the specified list of allowed values.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.3
	 * @since 1.3
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, AllowOnly, Object, Number>
	{
        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance,
				final AllowOnly annotation, final Object target,
				final Number value)
		{
			if (value == null)
			{
				if (annotation.required())
				{
					return new Problem(instance, annotation, target, value);
				}

				return null;
			}

			for (double i : annotation.value())
			{
				if (i == value.doubleValue())
				{
					return null;
				}
			}

			return new Problem(instance, annotation, target, value);
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
	 * The {@link AllowOnly.Validator Validator} for this annotation.
	 * 
	 * @return the {@link AllowOnly.Validator} class
	 */
	public Class<?> validator() default Validator.class;

	/**
	 * The set of allowed values.
	 * 
	 * @return the set of allowed values.
	 */
	double[] value();
}