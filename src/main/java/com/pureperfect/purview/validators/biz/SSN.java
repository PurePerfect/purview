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
package com.pureperfect.purview.validators.biz;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pureperfect.purview.ValidationProblem;

/**
 * A Social Security Number (SSN). Depending on the validation mode, dashes can
 * be excluded ({@link #NO_DASHES}) or allowed ({@link #DASHES}). The default
 * validation mode is {@link #BOTH}.
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
 *  &#064;SSN
 *  private String value;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;SSN
 *  public String getValue() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;SSN String bar) {
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
public @interface SSN
{
	/**
	 * Indicates an invalid Social Security Number.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Problem extends
			ValidationProblem<Object, SSN, Object, CharSequence>
	{
		Problem(final Object instance, final SSN annotation,
				final Object target, final CharSequence value)
		{
			super(instance, annotation, target, value);
		}
	}

	/**
	 * A Social Security Number (SSN). Depending on the validation mode, dashes
	 * can be excluded ({@link #NO_DASHES}) or allowed ({@link #DASHES}). The
	 * default validation mode is {@link #BOTH}.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, SSN, Object, CharSequence>
	{
        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance, final SSN annotation,
				final Object target, final CharSequence value)
		{
			if (value == null || value.length() < 1)
			{
				if (annotation.required())
				{
					return new Problem(instance, annotation, target, value);
				}

				return null;
			}

			final int length = value.length();
			final int mode = annotation.validationMode();

			if (length == 9 && (mode == BOTH || mode == NO_DASHES))
			{
				for (int i = 0; i < length; ++i)
				{
					final char c = value.charAt(i);

					if (!Character.isDigit(c))
					{
						return new Problem(instance, annotation, target, value);
					}
				}

				return null;
			}
			else if (length == 11 && (mode == BOTH || mode == DASHES))
			{
				for (int i = 0; i < length; ++i)
				{
					final char c = value.charAt(i);

					if (i == 3 || i == 6)
					{
						if (c != '-')
						{
							return new Problem(instance, annotation, target,
									value);
						}
					}
					else
					{
						if (!Character.isDigit(c))
						{
							return new Problem(instance, annotation, target,
									value);
						}
					}
				}

				return null;
			}
			else
			{
				return new Problem(instance, annotation, target, value);
			}
		}
	}

	/**
	 * Indicate that either a nine digit number or an eleven digit number with
	 * dashes is acceptable.
	 */
	public static final int BOTH = 2;

	/**
	 * Indicate that only SSNs with dashes should be allowed.
	 */
	public static final int DASHES = 0;

	/**
	 * Indicate that only a nine digit number without dashes is acceptable.
	 */
	public static final int NO_DASHES = 1;

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
	 * Either ({@link #NO_DASHES}) or ({@link #DASHES}) or {@link #BOTH}.
	 */
	int validationMode() default BOTH;

	/**
	 * The {@link SSN.Validator} for this annotation.
	 * 
	 * @return The {@link SSN.Validator} class
	 */
	Class<?> validator() default Validator.class;
}