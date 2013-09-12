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
package com.pureperfect.purview.validators.net;

import com.pureperfect.purview.ValidationProblem;

/**
 * Indicates that a URL is not valid.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class InvalidURLProblem extends
		ValidationProblem<Object, URL, Object, CharSequence>
{
	InvalidURLProblem(final Object instance, final URL annotation,
			final Object target, final CharSequence value)
	{
		super(instance, annotation, target, value);
	}
}