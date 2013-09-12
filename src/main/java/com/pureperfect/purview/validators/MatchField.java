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
import java.lang.reflect.Field;

import com.pureperfect.purview.ValidationException;
import com.pureperfect.purview.ValidationProblem;

/**
 * Match the value of another field.
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
 *  
 *  private String value;
 *  
 *  &#064;MatchField(&quot;value&quot;)
 *  private String field2;
 * 
 *  ...//or
 *  
 *  //When validating method return values
 *  &#064;MatchField(&quot;value&quot;)
 *  public String myMethod() { 
 *    ... 
 *  };
 * }
 * </pre>
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.METHOD, ElementType.FIELD })
public @interface MatchField
{
	/**
	 * Indicates that the values did not match.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Problem extends
			ValidationProblem<Object, MatchField, Object, Object>
	{
		private final Object expectedValue;

		Problem(final Object instance, final MatchField annotation,
				final Object target, final Object value,
				final Object expectedValue)
		{
			super(instance, annotation, target, value);
			this.expectedValue = expectedValue;
		}

		/**
		 * The value that was expected.
		 * 
		 * @return The value that was expected.
		 */
		public Object getExpectedValue()
		{
			return this.expectedValue;
		}
	}

	/**
	 * Match the value of another field.
	 * 
	 * @author J. Chris Folsom
	 * @version 1.1
	 * @since 1.0
	 */
	public class Validator
			implements
			com.pureperfect.purview.Validator<Problem, Object, MatchField, Object, Object>
	{
        /**
         * {@inheritDoc}
         */
		public Problem validate(final Object instance,
				final MatchField annotation, final Object target,
				final Object actualValue)
		{
			Object expectedValue;

			try
			{
				final String fieldName = annotation.value();

				final Field field = instance.getClass().getField(fieldName);

				expectedValue = field.get(instance);
			}
			catch (final Exception e)
			{
				throw new ValidationException(e);
			}

			Problem problem = null;

			if (actualValue == null)
			{
				if (expectedValue != null)
				{
					problem = new Problem(instance, annotation, target,
							expectedValue, actualValue);
				}
			}
			else if (expectedValue == null)
			{
				problem = new Problem(instance, annotation, target,
						expectedValue, actualValue);
			}
			else if (!actualValue.equals(expectedValue))
			{
				problem = new Problem(instance, annotation, target,
						expectedValue, actualValue);
			}

			return problem;
		}
	}

	/**
	 * Optional message key to use to define an i18n message.
	 * 
	 * @return Optional message key to use to define an i18n message.
	 */
	String messageKey() default "";

	/**
	 * The {@link MatchField.Validator Validator} for this annotation.
	 * 
	 * @return the {@link MatchField.Validator} class
	 */
	Class<?> validator() default Validator.class;

	/**
	 * The name of the other field to match within the same class.
	 * 
	 * @return The name of the other method to match within the same class.
	 */
	String value() default "";
}