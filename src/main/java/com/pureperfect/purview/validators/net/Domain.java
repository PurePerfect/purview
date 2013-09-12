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

import com.pureperfect.purview.ValidationProblem;

/**
 * Mark a field as one that is a domain name.
 * 
 *  <p>
 * E.G:
 * </p>
 * 
 * <pre>
 * 
 * public class MyClass
 * {
 *  //When validating fields
 *  &#064;Domain
 *  private String value;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;Domain
 *  public String getValue() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;Domain String bar) {
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
public @interface Domain
{
	/**
	 * Validator for a domain name.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	@SuppressWarnings("rawtypes")
	public class Validator
			implements
			com.pureperfect.purview.Validator<ValidationProblem, Object, Domain, Object, CharSequence>
	{
        /**
         * {@inheritDoc}
         */
		public ValidationProblem validate(final Object instance,
				final Domain annotation, final Object target,
				final CharSequence value)
		{
			if (value == null || value.length() < 1)
			{
				if (annotation.required())
				{
					return new InvalidDomainNameProblem(instance, annotation,
							target, value);
				}

				return null;
			}

			return ValidationRoutines.validateDomain(instance, annotation,
					target, value, annotation.reverseLookup(),
					annotation.validateTlds());
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
	 * Whether or not to perform a reverse lookup to validate the host.
	 * 
	 * @return true if reverse lookup.
	 */
	boolean reverseLookup() default false;

	/**
	 * Compare the Top-Level Domain against <code>validtlds.lst</code> to see if
	 * it is valid. If the TLD is not in the file, validation will fail.
	 * 
	 * @return whether or not to validate Top-Level domains.
	 */
	boolean validateTlds() default false;

	/**
	 * The {@link Domain.Validator Validator} for this annotation.
	 * 
	 * @return {@link Domain.Validator}
	 */
	Class<?> validator() default Validator.class;
}