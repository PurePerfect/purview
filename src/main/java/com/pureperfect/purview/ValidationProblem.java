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
import java.util.ResourceBundle;

import com.pureperfect.purview.i18n.ExpressionEngine;
import com.pureperfect.purview.i18n.YALEExpressionEngine;
import com.pureperfect.purview.util.ReflectionUtils;

/**
 * Indicates that a validation problem occurred.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public abstract class ValidationProblem<I extends Object, A extends Annotation, T extends Object, V extends Object>
{
	/**
	 * {@link YALEExpressionEngine}.
	 */
	public static final ExpressionEngine DEFAULT_EXPRESSION_ENGINE = YALEExpressionEngine
			.defaultInstance();

	private final A annotation;

	private final I instance;

	private final T target;

	private final V value;

	/**
	 * Create a new validation problem.
	 * 
	 * @param instance
	 *            the object that failed validation
	 * @param annotation
	 *            the annotation that caused the problem
	 * @param target
	 *            the target that the annotation was attached to
	 * @param value
	 *            the value that caused the problem
	 */
	public ValidationProblem(final I instance, final A annotation,
			final T target, final V value)
	{
		super();
		this.instance = instance;
		this.annotation = annotation;
		this.target = target;
		this.value = value;
	}

	/**
	 * Get the validation annotation responsible for the problem.
	 * 
	 * @return the validation annotation.
	 */
	public A getAnnotation()
	{
		return this.annotation;
	}

	/**
	 * Get the object instance that caused the problem.
	 * 
	 * @return the object instance that caused the problem.
	 */
	public I getInstance()
	{
		return this.instance;
	}

	/**
	 * Load the appropriate message from the resource bundle using the default
	 * expression engine.
	 * 
	 * @param bundle
	 *            the resource bundle to load the message from.
	 * @return the message with any expressions evaluated.
	 */
	public String getLocalizedMessage(final ResourceBundle bundle)
	{
		return this.getLocalizedMessage(bundle, DEFAULT_EXPRESSION_ENGINE);
	}

	/**
	 * Load the appropriate message from the resource bundle using the specified
	 * expression engine.
	 * 
	 * @param bundle
	 *            the resource bundle to load the message from.
	 * @param expEng
	 *            the expression engine to use when evaluating expressions.
	 * @return the message with any expressions evaluated.
	 */
	public String getLocalizedMessage(final ResourceBundle bundle,
			final ExpressionEngine expEng)
	{
		String messageKey = null;

		try
		{
			/*
			 * See if message key attribute is defined.
			 */
			messageKey = (String) ReflectionUtils.getAnnotationValue(
					this.getAnnotation(), "messageKey");
		}
		catch (final Exception e)
		{
			/*
			 * messageKey attribute not defined just use class name instead
			 */
			messageKey = this.getClass().getName();
		}

		/*
		 * If the message key is not defined, use the class name as the default message key.
		 */
		if ("".equals(messageKey))
		{
			messageKey = this.getClass().getName();
		}

		final String message = bundle.getString(messageKey);

		return expEng.eval(message, this);
	}

	/**
	 * Get the target that the annotation was attached to.
	 * 
	 * @return the target that the annotation was attached to.
	 */
	public T getTarget()
	{
		return this.target;
	}

	/**
	 * Get the value that did not pass validation.
	 * 
	 * @return the value that did not pass validation.
	 */
	public V getValue()
	{
		return this.value;
	}
}