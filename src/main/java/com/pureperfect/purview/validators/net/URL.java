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
package com.pureperfect.purview.validators.net;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.net.MalformedURLException;

/**
 * Mark a field as one that is a Uniform Resource Locator.
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
 *  &#064;URL
 *  private String value;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;URL
 *  public String getValue() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;URL String bar) {
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
public @interface URL
{
	/**
	 * Validator for Uniform Resource Locator.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<InvalidURLProblem, Object, URL, Object, CharSequence>
	{
		public InvalidURLProblem validate(final Object instance,
				final URL annotation, final Object target,
				final CharSequence url)
		{
			if (url == null || url.length() < 1)
			{
				if (annotation.required())
				{
					return new InvalidURLProblem(instance, annotation, target,
							url);
				}
				
				return null;
			}

			try
			{
				new java.net.URL(url.toString());

				return null;
			}
			catch (final MalformedURLException e)
			{
				return new InvalidURLProblem(instance,
						annotation,
						target, url);
			}
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
	 * The {@link URL.Validator Validator} for this annotation.
	 * 
	 * @return {@link URL.Validator}
	 */
	Class<?> validator() default Validator.class;
}