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
import java.util.HashMap;
import java.util.Map;

/**
 * A validator factory that caches validators.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class CachingValidatorFactory extends ValidatorFactoryImpl
{
	@SuppressWarnings("rawtypes")
	private final Map<String, Validator> validatorCache = new HashMap<String, Validator>();

    /**
     * {@inheritDoc}
     */
	@Override
	@SuppressWarnings("rawtypes")
	public Validator createValidator(final Annotation annotation)
	{
		final String key = annotation.getClass().getName();

		Validator v = this.validatorCache.get(key);

		if (v == null)
		{
			/*
			 * No previously cached version so delegate to the default factory
			 * to create the validator.
			 */
			v = super.createValidator(annotation);

			/*
			 * Cache the validator if a validator was created.
			 */
			if (v != null)
			{
				this.validatorCache.put(key, v);
			}
		}

		return v;
	}
}