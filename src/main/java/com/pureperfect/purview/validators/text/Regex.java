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
import java.util.regex.Pattern;

import com.pureperfect.purview.ValidationProblem;

/**
 * Make sure the value to which this annotation is attached matches the given expression.
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
 *  &#064;Regex(&quot;[a-z]*&quot;)
 *  private String name;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;Regex(&quot;[a-z]*&quot;)
 *  public String getName() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#064;Regex(&quot;[a-z]*&quot;) String bar) {
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
public @interface Regex
{
	/**
	 * Indicates that the value did not patch the provided pattern.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.3
	 * @since 1.3
	 */
	public class Problem extends
			ValidationProblem<Object, Regex, Object, CharSequence>
	{
		Problem(final Object instance, final Regex annotation,
				final Object target, final CharSequence value)
		{
			super(instance, annotation, target, value);
		}
	}

	/**
	 * Make sure the value to which this annotation is attached matches the given expression.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.3
	 * @since 1.3
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, Regex, Object, CharSequence>
	{
        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance,
				final Regex annotation, final Object target,
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

			if(Pattern.matches(annotation.value(), value))
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
	 * Whether or not the field is required. If it is not required, null values
	 * and empty strings will be allowed.
	 * 
	 * @return Whether or not the field is required.
	 */
	boolean required() default false;

	/**
	 * The {@link Regex.Validator Validator} for this annotation.
	 * 
	 * @return the {@link Regex.Validator} class
	 */
	public Class<?> validator() default Validator.class;

	/**
	 * The regular expression to match.
	 * 
	 * @return the regular expression to match.
	 */
	String value();
}