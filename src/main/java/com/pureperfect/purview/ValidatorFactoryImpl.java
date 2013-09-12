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
package com.pureperfect.purview;

import java.lang.annotation.Annotation;

import com.pureperfect.purview.util.ReflectionUtils;

/**
 * Default {@link ValidatorFactory} implementation.
 * 
 * @author J. Chris Folsom
 * @version 1.3
 * @since 1.3
 */
public class ValidatorFactoryImpl implements ValidatorFactory
{
	@SuppressWarnings("rawtypes")
	public Validator createValidator(final Annotation annotation)
	{
		try
		{
			final Object validatorValue = ReflectionUtils.getAnnotationValue(
					annotation, "validator");

			/*
			 * Validator attribute exists on annotation, so try to instantiate
			 * it and create a validator. If there is no validator attribute, a null value
			 * will be returned as part of the interface contract. 
			 */
			if (validatorValue != null)
			{
				final Class<?> validatorClass = (Class<?>) validatorValue;

				final Object o = validatorClass.newInstance();

				if (o instanceof Validator)
				{
					return (Validator) validatorClass.newInstance();
				}
			}

			return null;
		}
		catch (final NoSuchMethodException e)
		{
			return null;
		}
		catch (final Exception e)
		{
			throw new ValidationException(e);
		}
	}
}