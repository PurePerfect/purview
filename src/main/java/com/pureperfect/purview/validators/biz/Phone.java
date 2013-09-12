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
 * A phone number. Depending on the validation mode, either
 * {@link Phone#NO_DASHES} or {@link Phone#DASHES} numbers can be allowed. The
 * default validation mode is {@link Phone#DASHES}.
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
 *  &#064;Phone
 *  private String value;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;Phone
 *  public String getValue() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;Phone String bar) {
 *    ...
 *  }
 * }
 * </pre>
 * 
 * @author J. Chris Folsom
 * @version 1.
 * @since 1.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
public @interface Phone
{
	/**
	 * Indicates an invalid Phone number.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.3
	 * @since 1.3
	 */
	public class Problem extends
			ValidationProblem<Object, Phone, Object, CharSequence>
	{
		Problem(final Object instance, final Phone annotation,
				final Object target, final CharSequence value)
		{
			super(instance, annotation, target, value);
		}
	}

	/**
	 * A phone number. Depending on the validation mode, either
	 * {@link Phone#NO_DASHES} or {@link Phone#DASHES} numbers can be allowed.
	 * The default validation mode is {@link Phone#DASHES}.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.3
	 * @since 1.3
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, Phone, Object, CharSequence>
	{
        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance, final Phone annotation,
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

			final int mode = annotation.validationMode();
			final int length = value.length();

			if (length == 10 && (mode == NO_DASHES || mode == BOTH))
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
			else if (length == 12 && (mode == DASHES || mode == BOTH))
			{
				for (int i = 0; i < length; ++i)
				{
					final char c = value.charAt(i);

					if (i == 3 || i == 7)
					{
						if (c != '-')
						{
							return new Problem(instance, annotation, target,
									value);
						}
					}
					else if (!Character.isDigit(c))
					{
						return new Problem(instance, annotation, target, value);
					}
				}

				return null;
			}

			return new Problem(instance, annotation, target, value);
		}
	}

	/**
	 * Indicate that either {@link DASHES} or {@link NO_DASHES} is acceptable.
	 */
	public static final int BOTH = 2;

	/**
	 * A phone number in the format of 1234567890.
	 */
	public static final int NO_DASHES = 0;

	/**
	 * A phone number in the format of 123-456-7890.
	 */
	public static final int DASHES = 1;

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
	 * Either {@link Phone#NO_DASHES} or {@link Phone#DASHES} or
	 * {@link Phone#BOTH}. The default validation mode is {@link Phone#DASHES}.
	 * .
	 */
	int validationMode() default DASHES;

	/**
	 * The {@link Phone.Validator Validator} for this annotation.
	 * 
	 * @return The {@link Phone.Validator} class
	 */
	Class<?> validator() default Validator.class;
}