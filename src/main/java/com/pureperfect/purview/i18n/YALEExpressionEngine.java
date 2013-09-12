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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

import com.pureperfect.purview.util.ReflectionUtils;

/**
 * YALE stands for Yet Another Language for Expressions. YALE is an attempt at
 * speeding up interpolation of expressions by making the expression language as
 * simple as possible. While YALE is not as dumb as <a
 * href="http://en.wikipedia.org/wiki/Paul_Krugman">some Yale graduates</a>, it
 * is not very smart either. It only supports execution of methods that take
 * zero parameters. As far as I can tell however, it remains the world's fastest
 * expression engine. Like the JEXL expression engine, expressions should be
 * escaped using ${} and the problem should be referenced in the message
 * template using the identifier <code>problem</code>.
 * 
 * <p>
 * For Example:
 * </p>
 * 
 * <p>
 * If the problem has a method called <code>getEmail()</code> that returns a
 * value of "<code>abademailaddress</code> ", then evaluating the String
 * <code>"${problem.getEmail} is not a valid email
 * address"</code> would result in the phrase
 * <code>"abademailaddress is not a valid email address."</code>
 * </p>
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class YALEExpressionEngine implements ExpressionEngine
{
	private static YALEExpressionEngine defaultInstance = new YALEExpressionEngine();

	private static final Class<?>[] NO_ARGS = new Class<?>[]
	{};

	private static final int STATE_IN_EXPRESSION = 0;

	private static final int STATE_OUT_OF_EXPRESSION = 1;

	/**
	 * Singleton instance.
	 * 
	 * @return Singleton instance.
	 */
	public static YALEExpressionEngine defaultInstance()
	{
		return defaultInstance;
	}

	/*
	 * Evaluate the given expression on the given object and return the results
	 * of the expression.
	 */
	private static Object evaluate(final Object problem, final String expression)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException
	{
		final StringTokenizer tok = new StringTokenizer(expression, ".");

		Object result = problem;

		if (tok.hasMoreTokens())
		{
			final String problemToken = tok.nextToken();

			if ("problem".equals(problemToken))
			{
				while (tok.hasMoreTokens())
				{
					final String nextMethodName = tok.nextToken();

					final Class<?> clazz = result.getClass();
					final Method method = clazz.getMethod(nextMethodName,
							NO_ARGS);

					if (method != null)
					{
						result = ReflectionUtils.getValueOfMethod(method,
								result);
					}
					else
					{
						break;
					}
				}
			}
		}

		return result;
	}

	private YALEExpressionEngine()
	{
		// singleton
	}

    /**
     * {@inheritDoc}
     */
	public String eval(final String messageTemplate, final Object problem)
	{
		try
		{
			final StringBuilder results = new StringBuilder(
					messageTemplate.length());

			int state = STATE_OUT_OF_EXPRESSION;
			int expStartIndex = 0;

			/*
			 * So you may have guessed that writing compilers is not my strong
			 * suit. Okay so this isn't exactly the fastest or most attractive
			 * code in the world, but it solved the problem in a hurry and
			 * didn't require much of a state machine. If you think my code
			 * sucks, at least I got it working in 30 minutes. If you think you
			 * can do better please send me a patch.
			 */
			for (int i = 0, length = messageTemplate.length(); i < length; ++i)
			{
				final char current = messageTemplate.charAt(i);
				char nextChar = 0x0000;

				if (i + 1 < length)
				{
					nextChar = messageTemplate.charAt(i + 1);
				}

				switch (state)
				{
					case STATE_IN_EXPRESSION:
						if (nextChar == '}')
						{
							final String found = messageTemplate.substring(
									expStartIndex, ++i);

							final Object result = evaluate(problem, found);

							results.append(result);
							state = STATE_OUT_OF_EXPRESSION;
						}

						break;
					default:
						// Start expression
						if (current == '$' && nextChar == '{')
						{
							// skip $ and {
							++i;
							expStartIndex = ++i;
							state = STATE_IN_EXPRESSION;
						}
						else
						{
							results.append(current);
						}

						break;
				}
			}

			/*
			 * append the entire escape just incase they left it open, so
			 * ${problem.value gets printed in the results if there is no
			 * closing brace;
			 */
			if (state == STATE_IN_EXPRESSION)
			{
				final String openExpression = messageTemplate.substring(
						expStartIndex - 2, messageTemplate.length());

				throw new InterpolationException(openExpression);
			}

			return results.toString();
		}
		catch (final Throwable t)
		{
			throw new InterpolationException(t);
		}
	}
}