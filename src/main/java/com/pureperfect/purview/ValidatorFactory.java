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
 * Manages the creation of validators for annotations.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public interface ValidatorFactory
{
	/**
	 * Create a new validator for the annotation.
	 * 
	 * @param annotation
	 *            the annotation to create a validator for.
	 * 
	 * @return the new validator or null if the annotation does not specify a
	 *         validator class using the validator() attribute.
	 */
	@SuppressWarnings("rawtypes")
	public abstract Validator createValidator(Annotation annotation);
}