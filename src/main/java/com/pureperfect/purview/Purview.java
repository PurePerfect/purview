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

import java.lang.reflect.Method;

import com.pureperfect.purview.util.FieldFilter;
import com.pureperfect.purview.util.MethodFilter;

/**
 * Static convenience methods for {@link ValidationEngine}. Please read the <a
 * href="../../../../index.html">user guide</a>.
 * 
 * @author J. Chris Folsom
 * @version 1.3
 * @since 1.0
 */
public final class Purview
{
	private static final ValidationEngine validationEngine = new ValidationEngineImpl(
			new CachingValidatorFactory());

	/**
	 * See {@link ValidationEngine#validateFields(Object)}.
	 * 
	 * @param instance
	 *            the instance to validated
	 * @return the validation results.
	 */
	public static ValidationResults validateFields(final Object instance)
	{
		return validationEngine.validateFields(instance);
	}

	/**
	 * See {@link ValidationEngine#validateFields(Object, boolean)}.
	 * 
	 * @param instance
	 *            the instance to validate
	 * 
	 * @param useStrictMode
	 *            whether or not to use strict mode
	 * @return the validation results
	 */
	public static ValidationResults validateFields(final Object instance,
			final boolean useStrictMode)
	{
		return validationEngine.validateFields(instance, useStrictMode);
	}

	/**
	 * See {@link ValidationEngine#validateFields(Object, FieldFilter)}.
	 * 
	 * @param instance
	 *            the instance to validate
	 * @param filter
	 *            the filter that will determine what fields to validate.
	 * @return the validation results
	 */
	public static ValidationResults validateFields(final Object instance,
			final FieldFilter filter)
	{
		return validationEngine.validateFields(instance, filter);
	}

	/**
	 * See {@link ValidationEngine#validateFields(Object, FieldFilter, boolean)}
	 * .
	 * 
	 * @param instance
	 *            the instance to validated
	 * @param filter
	 *            the filter which will determine the fields that get validated
	 * 
	 * @return the validation results.
	 */
	public static ValidationResults validateFields(final Object instance,
			final FieldFilter filter, final boolean strict)
	{
		return validationEngine.validateFields(instance, filter, strict);
	}

	/**
	 * See {@link ValidationEngine#validateMethods(Object)}.
	 * 
	 * @param instance
	 *            the object that validation is being performed on
	 * 
	 * @return the validation results.
	 */
	public static ValidationResults validateMethods(final Object instance)
	{
		return validationEngine.validateMethods(instance);
	}

	/**
	 * See {@link ValidationEngine#validateMethods(Object, boolean)}.
	 * 
	 * @param instance
	 *            the object that validation is being performed on
	 * @param useStrictMode
	 *            whether or not to use strict mode
	 * 
	 * @return the validation results.
	 */
	public static ValidationResults validateMethods(final Object instance,
			final boolean useStrictMode)
	{
		return validationEngine.validateMethods(instance, useStrictMode);
	}

	/**
	 * See {@link ValidationEngine#validateMethods(Object, MethodFilter)}.
	 * 
	 * @param instance
	 *            the object that validation is being performed on
	 * @param filter
	 *            the {@link MethodFilter} to use
	 * 
	 * @return the validation results.
	 */
	public static ValidationResults validateMethods(final Object instance,
			final MethodFilter filter)
	{
		return validationEngine.validateMethods(instance, filter);
	}

	/**
	 * See
	 * {@link ValidationEngine#validateMethods(Object, MethodFilter, boolean)}.
	 * 
	 * @param instance
	 *            the object that validation is being performed on
	 * @param filter
	 *            the {@link MethodFilter} to use
	 * @param useStrictMode
	 *            whether or not to use strict mode
	 * 
	 * @return the validation results.
	 */
	public static ValidationResults validateMethods(final Object instance,
			final MethodFilter filter, final boolean useStrictMode)
	{
		return validationEngine
				.validateMethods(instance, filter, useStrictMode);
	}

	/**
	 * See {@link ValidationEngine#validateParameters(Object, Method, Object[])}
	 * .
	 * 
	 * @param instance
	 *            the object that validation is being performed on
	 * @param method
	 *            the method that validation is being performed on
	 * @param parameters
	 *            the parameters for the method
	 * 
	 * @return the validation results.
	 */
	public static ValidationResults validateParameters(final Object instance,
			final Method method, final Object[] parameters)
	{
		return validationEngine
				.validateParameters(instance, method, parameters);
	}

	/**
	 * See
	 * {@link ValidationEngine#validateParameters(Object, Method, Object[], boolean)}
	 * .
	 * 
	 * @param instance
	 *            the object that validation is being performed on
	 * @param method
	 *            the method that validation is being performed on
	 * @param params
	 *            the parameters for the method
	 * 
	 * @param useStrictMode
	 *            whether or not to use strict mode
	 * 
	 * @return the validation results.
	 */
	public static ValidationResults validateParameters(final Object instance,
			final Method method, final Object[] params,
			final boolean useStrictMode)
	{
		return validationEngine.validateParameters(instance, method, params,
				useStrictMode);
	}

	/**
	 * See {@link ValidationEngine#validateType(Object)}.
	 * 
	 * @param instance
	 *            The object to validate.
	 * @return the validation results.
	 */
	public static ValidationResults validateType(final Object instance)
	{
		return validationEngine.validateType(instance);
	}

	/**
	 * See {@link ValidationEngine#validateType(Object, boolean)}.
	 * 
	 * @param instance
	 *            the object to validate
	 * 
	 * @param useStrictMode
	 *            whether or not to use strict mode
	 * 
	 * @return the results
	 */
	public static ValidationResults validateType(final Object instance,
			final boolean useStrictMode)
	{
		return validationEngine.validateType(instance, useStrictMode);
	}

	private Purview()
	{
		// hide constructor
	}
}