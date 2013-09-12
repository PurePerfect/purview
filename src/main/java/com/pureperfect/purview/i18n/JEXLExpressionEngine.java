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

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.jexl.Expression;
import org.apache.commons.jexl.ExpressionFactory;
import org.apache.commons.jexl.JexlContext;
import org.apache.commons.jexl.JexlHelper;


/**
 * An expression engine implemented using JEXL. This {@link ExpressionEngine}
 * expects expressions to be escaped within <code>${}</code>. It names the
 * object that you pass in <code>problem</code>, so if you want to reference it
 * your expressions should look like this: <code>${problem.someField}</code>.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class JEXLExpressionEngine implements ExpressionEngine
{
	private static JEXLExpressionEngine defaultInstance = new JEXLExpressionEngine();

	private static final int STATE_IN_EXPRESSION = 0;

	private static final int STATE_OUT_OF_EXPRESSION = 1;

	/**
	 * Singleton.
	 * 
	 * @return Singleton.
	 */
	public static JEXLExpressionEngine defaultInstance()
	{
		return defaultInstance;
	}

	private JEXLExpressionEngine()
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
			final JexlContext ctx = JexlHelper.createContext();

			final Map<String, Object> values = new TreeMap<String, Object>();

			values.put("problem", problem);

			ctx.setVars(values);

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

							final Expression jexlExp = ExpressionFactory
									.createExpression(found);

							final Object result = jexlExp.evaluate(ctx);

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