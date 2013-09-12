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
 * Should be thrown when there is an issue interpolating expressions on a
 * message.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class InterpolationException extends RuntimeException
{
	private static final long serialVersionUID = 0x000100;

	/**
	 * Create a new exception with the specified message.
	 * 
	 * @param message an error message.
	 */
	public InterpolationException(final String message)
	{
		super(message);
	}

	/**
	 * Create a new exception with the underlying cause.
	 * 
	 * @param cause the underlying cause.
	 */
	public InterpolationException(final Throwable cause)
	{
		super(cause);
	}
}