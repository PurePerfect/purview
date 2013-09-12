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
package com.pureperfect.purview.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Some various helper methods for working with the Java&trade; Reflections API.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class ReflectionUtils
{
	private static final Object[] NO_ARGS = new Object[]
	{};

	private static final Class<?>[] NO_TYPES = new Class<?>[]
	{};

	/**
	 * Get a value from an annotation.
	 * 
	 * @param annotation
	 *            the annotation to retrieve the value from.
	 * @param name
	 *            the name of the value to retrieve from the annotation.
	 * @return the value
	 * @throws InvocationTargetException
	 *             if it occurs.
	 * @throws IllegalAccessException
	 *             if it occurs.
	 * @throws IllegalArgumentException
	 *             if it occurs.
	 * @throws NoSuchMethodException
	 *             if it occurs.
	 * @throws SecurityException
	 *             if it occurs.
	 */
	public static final Object getAnnotationValue(final Annotation annotation,
			final String name) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException,
			SecurityException, NoSuchMethodException
	{
		final Method validatorDefinition = annotation.getClass()
				.getDeclaredMethod(name, NO_TYPES);

		return validatorDefinition.invoke(annotation, ReflectionUtils.NO_ARGS);
	}

	/**
	 * Get the set of <b>declared</b> fields that match the filter.
	 * 
	 * @param obj
	 *            the object to retrieve fields from
	 * @param filter
	 *            the filter to use
	 * @return the set of fields in object that also matched the filter
	 */
	public static Collection<Field> getDeclaredFields(final Object obj,
			final FieldFilter filter)
	{
		final Class<?> clazz = obj.getClass();

		final Field[] fields = clazz.getDeclaredFields();

		final Collection<Field> results = new LinkedList<Field>();

		if (fields != null)
		{
			for (final Field field : fields)
			{
				if (filter.include(field))
				{
					results.add(field);
				}
			}
		}

		return results;
	}

	/**
	 * Get all of the <b>declared</b> methods that match the filter.
	 * 
	 * @param obj
	 *            the object to search
	 * @param filter
	 *            the filter to use
	 * @return all of the declared methods that match.
	 */
	public static Collection<Method> getDeclaredMethods(final Object obj,
			final MethodFilter filter)
	{
		final Collection<Method> results = new LinkedList<Method>();

		final Class<?> clazz = obj.getClass();

		final Method[] methods = clazz.getDeclaredMethods();

		if (methods != null)
		{
			for (final Method method : methods)
			{
				if (filter.include(method))
				{
					results.add(method);
				}
			}
		}

		return results;
	}

	/**
	 * Invoke a method and return its value.
	 * 
	 * @param method
	 *            the method to invoke
	 * @param object
	 *            the object to invoke it on
	 * @return The result of the invocation.
	 * @throws IllegalArgumentException
	 *             if it occurs
	 * @throws IllegalAccessException
	 *             if it occurs
	 * @throws InvocationTargetException
	 *             if it occurs
	 */
	public static Object getValueOfMethod(final Method method,
			final Object object) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException
	{
		return method.invoke(object, new Object[]
		{});
	}

	/**
	 * Invoke the method on the object and return the value.
	 * 
	 * @param methodName
	 *            the name of the method to invoke
	 * @param object
	 *            the object to invoke it on
	 * @return the results of the invocation.
	 * @throws NoSuchMethodException
	 *             if it occurs
	 * @throws SecurityException
	 *             if it occurs
	 * @throws InvocationTargetException
	 *             if it occurs
	 * @throws IllegalAccessException
	 *             if it occurs
	 * @throws IllegalArgumentException
	 *             if it occurs
	 */
	public static Object getValueOfMethod(final String methodName,
			final Object object) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			IllegalAccessException, InvocationTargetException
	{
		final Class<?> clazz = object.getClass();

		final Method method = clazz.getMethod(methodName, NO_TYPES);

		return getValueOfMethod(method, object);
	}

	/**
	 * See if the method is static.
	 * 
	 * @param method
	 *            the method to test.
	 * @return true if the method is static.
	 */
	public static boolean isStatic(final Method method)
	{
		return Modifier.isStatic(method.getModifiers());
	}

	/**
	 * See if the return type for a method is void.
	 * 
	 * @param method
	 *            The method to test.
	 * @return true if the return type is void
	 */
	public static boolean isVoid(final Method method)
	{
		return "void".equals(method.getReturnType().toString());
	}

	private ReflectionUtils()
	{
		// private
	}
}