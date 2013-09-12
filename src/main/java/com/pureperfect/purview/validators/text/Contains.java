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
 * Make sure the value to which this annotation is attached contains the
 * specified string.
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
 *  &#064;Contains(&quot;chris&quot;)
 *  private String name;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;Contains(&quot;chris&quot;)
 *  public String getName() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;Contains(&quot;bar&quot;) String bar) {
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
public @interface Contains
{
	/**
	 * Indicates that the specified value was not contained in the object being
	 * validated.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.3
	 * @since 1.3
	 */
	public class Problem extends
			ValidationProblem<Object, Contains, Object, CharSequence>
	{
		Problem(final Object instance, final Contains annotation,
				final Object target, final CharSequence value)
		{
			super(instance, annotation, target, value);
		}

		/**
		 * The string that the value was supposed to contain.
		 * 
		 * @return  The string that the value was supposed to contain.
		 */
		public String getContains()
		{
			final Contains anno = this.getAnnotation();

			return anno.value();
		}
	}

	/**
	 * Make sure the value to which this annotation is attached contains the
	 * specified string.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.3
	 * @since 1.3
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, Contains, Object, CharSequence>
	{
        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance,
				final Contains annotation, final Object target,
				final CharSequence value)
		{
			if (value == null || value.length() < 1)
			{
				if (annotation.required())
				{
					return new Problem(instance, annotation, target, value);
				}

				return null;
			}

			final String string = value.toString();

			if (!string.contains(annotation.value()))
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
	 * The {@link Contains.Validator Validator} for this annotation.
	 * 
	 * @return the {@link Contains.Validator} class
	 */
	public Class<?> validator() default Validator.class;

	/**
	 * The value that the string must contain in order to be valid.
	 * 
	 * @return the value that the string must contain in order to be valid.
	 */
	String value();
}