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
 * A Zipcode. Depending on the validation mode, either
 * {@link ZipCode#FIVE_DIGIT} or {@link ZipCode#NINE_DIGIT} codes can be
 * allowed. The default validation mode is {@link ZipCode#BOTH}.
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
 *  &#064;ZipCode
 *  private String value;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;ZipCode
 *  public String getValue() { 
 *    ... 
 *  };
 *  
 *  ...//or
 *  
 *  //When validating method parameters
 *  public static void foo(&#64;ZipCode String bar) {
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
public @interface ZipCode
{
	/**
	 * Indicates an invalid Zipcode.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Problem extends
			ValidationProblem<Object, ZipCode, Object, CharSequence>
	{
		Problem(final Object instance, final ZipCode annotation,
				final Object target, final CharSequence value)
		{
			super(instance, annotation, target, value);
		}
	}

	/**
	 * A Zipcode. Depending on the validation mode, either
	 * {@link ZipCode#FIVE_DIGIT} or {@link ZipCode#NINE_DIGIT} codes can be
	 * allowed. The default validation mode is {@link ZipCode#BOTH}.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, ZipCode, Object, CharSequence>
	{
        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance,
				final ZipCode annotation, final Object target,
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
			
			final int mode = annotation.validationMode();
			final int length = value.length();

			if (length == 5 && (mode == FIVE_DIGIT || mode == BOTH))
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
			else if (length == 10 && (mode == NINE_DIGIT || mode == BOTH))
			{
				for (int i = 0; i < length; ++i)
				{
					final char c = value.charAt(i);

					if (i == 5)
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
	 * Indicate that either a five digit number or a nine digit number with
	 * dashes is acceptable.
	 */
	public static final int BOTH = 2;

	/**
	 * A five digit zipcode like this: 12345.
	 */
	public static final int FIVE_DIGIT = 0;

	/**
	 * A nine digit zipcode with a dash like this: 12345-6789.
	 */
	public static final int NINE_DIGIT = 1;

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
	 * Either {@link ZipCode#FIVE_DIGIT} or {@link ZipCode#NINE_DIGIT} or
	 * {@link ZipCode#BOTH}. The default validation mode is {@link ZipCode#BOTH}
	 * .
	 */
	int validationMode() default BOTH;

	/**
	 * The {@link ZipCode.Validator Validator} for this annotation.
	 * 
	 * @return The {@link ZipCode.Validator} class
	 */
	Class<?> validator() default Validator.class;
}