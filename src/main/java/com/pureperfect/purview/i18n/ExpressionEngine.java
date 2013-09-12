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
package com.pureperfect.purview.i18n;

/**
 * Interface for interpolating expressions on strings.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public interface ExpressionEngine
{

	/**
	 * Interpolate any expressions in the given string using values from the
	 * problem object.
	 * 
	 * @param messageTemplate
	 *            the string to interpolate expressions.
	 * @param problem
	 *            the object to evaluate expressions on
	 * @return the message key with all expressions substituted with their
	 *         appropriate values.
	 * @throws InterpolationException
	 *             if an error occurs.
	 */
	public abstract String eval(String messageTemplate, Object problem)
			throws InterpolationException;
}