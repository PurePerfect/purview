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

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import junit.framework.TestCase;

import com.pureperfect.purview.i18n.JEXLExpressionEngine;
import com.pureperfect.purview.validators.NotNull;

/**
 * Unit test for {@link ValidationProblem}.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class ValidationProblemTest extends TestCase
{
	/**
	 * Test get localized message.
	 */
	public void testGetLocalizedMessage()
	{
		final ResourceBundle bundle1 = ResourceBundle
				.getBundle("com.pureperfect.purview.bundle1");
		final ResourceBundle bundle2 = ResourceBundle
				.getBundle("com.pureperfect.purview.bundle2");
		final ResourceBundle bundle3 = ResourceBundle
				.getBundle("com.pureperfect.purview.bundle3");

		final NotNull.Problem problem = new NotNull.Problem("iamaproblem",
				null, null, null);

		try
		{
			problem.getLocalizedMessage(bundle1);
			fail();
		}
		catch (final MissingResourceException e)
		{
			// resource should be missing
		}

		assertEquals(
				"this is a test message",
				problem.getLocalizedMessage(bundle2,
						JEXLExpressionEngine.defaultInstance()));
		assertEquals(
				"this is a test message with expression interpolation: iamaproblem",
				problem.getLocalizedMessage(bundle3,
						JEXLExpressionEngine.defaultInstance()));
	}
}
