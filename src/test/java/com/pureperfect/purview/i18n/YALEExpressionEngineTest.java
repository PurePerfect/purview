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

import junit.framework.TestCase;

import com.pureperfect.purview.validators.net.Email;

/**
 * Test for {@link YALEExpressionEngine}.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class YALEExpressionEngineTest extends TestCase
{
	private ExpressionEngine engine = YALEExpressionEngine.defaultInstance();

	/**
	 * Interpolation on a broken exception should throw
	 * {@link InterpolationException}.
	 */
	public void testEvalBrokenBraces()
	{
		final Email.Problem problem = new Email.Problem("instanceValue", null,
				null, "bademail");

		try
		{
			this.engine
					.eval("oops: ${problem.value the object was: ${problem.instance}",
							problem);
			fail();
		}
		catch (final InterpolationException e)
		{
			// should have thrown parse exception
		}

		try
		{
			this.engine.eval("oops: ${problem.value", problem);
			fail();
		}
		catch (final InterpolationException e)
		{
			// should have thrown parse exception
			assertTrue(e.getMessage().contains("${problem.value"));
		}
	}

	/**
	 * Make sure the validation problem can be evaluated.
	 */
	public void testEvalProblemOkay()
	{
		final Email.Problem problem = new Email.Problem("instanceValue", null,
				null, "bademail");

		String result

		= this.engine
				.eval("The bad email address was: ${problem.getValue}the object was: ${problem.getInstance}",
						problem);

		assertEquals(
				"The bad email address was: bademailthe object was: instanceValue",
				result);
	}

	/**
	 * Make sure references to members in the validation problem can be
	 * evaluated.
	 */
	public void testEvalProblemMemberOkay()
	{
		final Email.Problem problem = new Email.Problem("instanceValue", null,
				null, "bademail");

		String result = this.engine.eval(
				"The bad email address was: ${problem.getInstance.length}",
				problem);

		assertEquals("The bad email address was: 13", result);
	}

	/**
	 * Make sure the '$' character is still included in expression.
	 */
	public void testEvalDollarSignIsIncludedInExpression()
	{
		final Email.Problem problem = new Email.Problem("instanceValue", null,
				null, "bademail");

		String result = this.engine.eval("oops: $problem.value", problem);

		assertEquals("oops: $problem.value", result);
	}

	/**
	 * Make sure the '{' character is still included in expression.
	 */
	public void testEvalLeftBraceIsIncludedInExpression()
	{
		final Email.Problem problem = new Email.Problem("instanceValue", null,
				null, "bademail");

		String result = this.engine.eval("oops: {problem.value", problem);

		assertEquals("oops: {problem.value", result);
	}

	/**
	 * Make sure the '}' character is still included in expression.
	 */
	public void testEvalRightBraceIsIncludedInExpression()
	{
		final Email.Problem problem = new Email.Problem("instanceValue", null,
				null, "bademail");

		String result = this.engine.eval("oops: }problem.value", problem);

		assertEquals("oops: }problem.value", result);
	}
}