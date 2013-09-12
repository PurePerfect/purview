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

/**
 * Thrown when there is a problem validating an object.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class ValidationException extends RuntimeException
{
	private static final long serialVersionUID = 0x000100;

	/**
	 * Create a new instance.
	 */
	public ValidationException()
	{
		super();
	}

	/**
	 * Create a new instance.
	 * 
	 * @param message
	 *            the message
	 */
	public ValidationException(final String message)
	{
		super(message);
	}

	/**
	 * Create a new instance.
	 * 
	 * @param message
	 *            the message
	 * @param cause
	 *            the underlying cause
	 */
	public ValidationException(final String message, final Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Create a new instance.
	 * 
	 * @param cause
	 *            the underlying cause
	 */
	public ValidationException(final Throwable cause)
	{
		super(cause);
	}
}