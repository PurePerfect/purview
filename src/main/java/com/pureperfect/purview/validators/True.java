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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pureperfect.purview.ValidationProblem;

/**
 * Specify that the value must be <code>true.</code>
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
 *  &#064;True
 *  private Boolean field2;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;True
 *  public Boolean myMethod() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;True Boolean bar) {
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
public @interface True
{
	/**
	 * Indicates that the value was not <code>true.</code>
	 * 
	 * @author J. Chris Folsom
	 * @version 1.3
	 * @since 1.3
	 */
	public class Problem extends
			ValidationProblem<Object, True, Object, Boolean>
	{
		Problem(final Object instance, final True annotation,
				final Object target, final Boolean value)
		{
			super(instance, annotation, target, value);
		}
	}

	/**
	 * Specify that the value must be <code>true.</code>
	 * 
	 * @author J. Chris Folsom
	 * @version 1.3
	 * @since 1.3
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, True, Object, Boolean>
	{
        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance, final True annotation,
				final Object target, final Boolean value)
		{
			if (value != null && value.booleanValue())
			{
				return null;
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
	 * The {@link True.Validator Validator} for this annotation.
	 * 
	 * @return the {@link True.Validator} class
	 */
	Class<?> validator() default Validator.class;
}