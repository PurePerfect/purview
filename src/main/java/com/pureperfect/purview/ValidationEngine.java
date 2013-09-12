package com.pureperfect.purview;

import java.lang.reflect.Method;

import com.pureperfect.purview.util.FieldFilter;
import com.pureperfect.purview.util.GetterMethodFilter;
import com.pureperfect.purview.util.MakeAccessibleFieldFilter;
import com.pureperfect.purview.util.MethodFilter;

/**
 * Core interface for validation operations.
 * 
 * @author J. Chris Folsom
 * @since 1.3
 * @version 1.3
 */
public interface ValidationEngine
{
	/**
	 * The default: {@link com.pureperfect.purview.util.GetterMethodFilter}.
	 */
	public static MethodFilter DEFAULT_METHOD_FILTER = GetterMethodFilter
			.defaultInstance();

	/**
	 * The default:
	 * {@link com.pureperfect.purview.util.MakeAccessibleFieldFilter}.
	 */
	public static FieldFilter DEFAULT_FIELD_FILTER = MakeAccessibleFieldFilter
			.defaultInstance();

	/**
	 * Validate the fields on the object using the default {@link FieldFilter} (
	 * {@link MakeAccessibleFieldFilter}).
	 * 
	 * @param instance
	 *            the instance to validated
	 * @return the validation results.
	 */
	public abstract ValidationResults validateFields(final Object instance);

	/**
	 * Validate the fields on the object.
	 * 
	 * @param instance
	 *            the instance to validate
	 * 
	 * @param useStrictMode
	 *            whether or not to use strict mode
	 * @return the validation results
	 */
	public abstract ValidationResults validateFields(final Object instance,
			final boolean useStrictMode);

	/**
	 * Validate the fields on the object.
	 * 
	 * @param instance
	 *            the instance to validate
	 * @param filter
	 *            the filter that will determine what fields to validate.
	 * @return the validation results
	 */
	public abstract ValidationResults validateFields(final Object instance,
			final FieldFilter filter);

	/**
	 * Validate the fields on the object.
	 * 
	 * @param instance
	 *            the instance to validated
	 * @param filter
	 *            the filter which will determine the fields that get validated
	 * 
	 * @return the validation results.
	 */
	public abstract ValidationResults validateFields(final Object instance,
			final FieldFilter filter, final boolean strict);

	/**
	 * Validate the methods on the given instance.
	 * 
	 * @param instance
	 *            the object that validation is being performed on
	 * 
	 * @return the validation results.
	 */
	public abstract ValidationResults validateMethods(final Object instance);

	/**
	 * Validate the methods on the given instance.
	 * 
	 * @param instance
	 *            the object that validation is being performed on
	 * @param useStrictMode
	 *            whether or not to use strict mode
	 * 
	 * @return the validation results.
	 */
	public abstract ValidationResults validateMethods(final Object instance,
			final boolean useStrictMode);

	/**
	 * Validate the methods on the given instance.
	 * 
	 * @param instance
	 *            the object that validation is being performed on
	 * @param filter
	 *            the {@link MethodFilter} to use
	 * 
	 * @return the validation results.
	 */
	public abstract ValidationResults validateMethods(final Object instance,
			final MethodFilter filter);

	/**
	 * Validate the methods on the given instance.
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
	public abstract ValidationResults validateMethods(final Object instance,
			final MethodFilter filter, final boolean useStrictMode);

	/**
	 * Validate the parameters using parameter annotations on the method.
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
	public abstract ValidationResults validateParameters(final Object instance,
			final Method method, final Object[] parameters);

	/**
	 * Validate the parameters using parameter annotations on the method.
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
	public abstract ValidationResults validateParameters(final Object instance,
			final Method method, final Object[] params,
			final boolean useStrictMode);

	/**
	 * Validate the object using only the class level annotations. By default,
	 * strict-mode will not be used.
	 * 
	 * @param instance
	 *            The object to validate.
	 * @return the validation results.
	 */
	public abstract ValidationResults validateType(final Object instance);

	/**
	 * Validate the object using only the class level annotations.
	 * 
	 * @param instance
	 *            the object to validate
	 * 
	 * @param useStrictMode
	 *            whether or not to use strict mode
	 * 
	 * @return the results
	 */
	public abstract ValidationResults validateType(final Object instance,
			final boolean useStrictMode);

}