package com.pureperfect.purview.i18n;

import com.pureperfect.purview.validators.net.Email;

import junit.framework.TestCase;

/**
 * Unit test for instances of {@link JEXLExpressionEngine}.
 * 
 * @author J. Chris Folsom
 * @since 1.3
 * @version 1.3
 */
public class JEXLExpressionEngineTest extends TestCase
{
	private ExpressionEngine engine = JEXLExpressionEngine.defaultInstance();

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
				.eval("The bad email address was: ${problem.value}the object was: ${problem.instance}",
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
				"The bad email address was: ${problem.instance.length()}",
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
