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

/**
 * Interface for implementing validators using annotations.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
@SuppressWarnings("rawtypes")
public interface Validator<P extends ValidationProblem, I extends Object, A extends Annotation, T extends Object, V extends Object>
{
	/**
	 * Perform validation.
	 * 
	 * @param instance
	 *            the object instance that owns the value
	 * @param annotation
	 *            the validation annotation
	 * @param target
	 *            the thing that the annotation was attached to.
	 * @param value
	 *            the value to validate
	 * @return a {@link ValidationProblem ValidationProblem} if the value is
	 *         invalid or null if the value is valid.
	 */
	public P validate(I instance, A annotation, T target, V value);
}