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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import com.pureperfect.purview.ValidationException;
import com.pureperfect.purview.ValidationProblem;

/**
 * Some common validation routines for the validation annotations in this
 * package.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
class ValidationRoutines
{
	public static Set<String> TLDS = ValidationRoutines.loadValidTlds();

	/**
	 * Test to see if the character is valid as the account portion of the email
	 * address.
	 * 
	 * @param c
	 *            The character to test
	 * @return Whether or not the character is valid
	 */
	public static boolean isValidEmailAccountChar(final char c)
	{
		return Character.isLetter(c) || Character.isDigit(c) || c == '.'
				|| c == '-' || c == '_';
	}

	/**
	 * Test to see if a character is a valid character in a host name
	 * 
	 * @param c
	 *            the character to test
	 * @return true if it is.
	 */
	public static boolean isValidHostNameChar(final char c)
	{
		return Character.isLetter(c) || Character.isDigit(c) || c == '_'
				|| c == '-';
	}

	/**
	 * Validate an octet.
	 * 
	 * @param octet
	 *            the octet to validate
	 * @return whether or not it is valid.
	 */
	public static boolean isValidOctet(final String octet)
	{
		try
		{
			final int val = Integer.parseInt(octet);

			return (val > -1 && val < 256);
		}
		catch (final Exception e)
		{
			return false;
		}
	}

	private static Set<String> loadValidTlds()
	{
		try
		{
			final ClassLoader cl = Thread.currentThread()
					.getContextClassLoader();

			final InputStream in = cl
					.getResourceAsStream("com/pureperfect/purview/validators/net/validtlds.lst");

            if(in == null)
            {
                throw new ValidationException("Could not find validtlds.lst!!!");
            }

			final Set<String> tlds = new HashSet<String>();

			final BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));

			for (String s = reader.readLine(); s != null; s = reader.readLine())
			{
				tlds.add(s.trim());
			}

			return tlds;
		}
        catch (ValidationException e)
        {
            throw new ValidationException();
        }
		catch (final Throwable t)
		{
			throw new ValidationException("Could not load validtlds.lst", t);
		}
	}

	/**
	 * Validate a domain
	 * 
	 * @param instance
	 *            the object that is validated
	 * @param annotation
	 *            the Domain annotation
	 * @param target
	 *            the target the annotation was attached to
	 * @param value
	 *            the value of the target
	 * @param reverse
	 *            whether or not to perform reverse lookup
	 * @param validateTLDs
	 *            whether or not to validate TLDs
	 * 
	 * @return the problem if one occurred or null
	 */
	@SuppressWarnings("rawtypes")
	public static ValidationProblem validateDomain(final Object instance,
			final Annotation annotation, final Object target,
			final CharSequence value, final boolean reverse,
			final boolean validateTLDs)
	{
		final String s = value.toString();

		final int lastDot = s.lastIndexOf('.');

		if (lastDot < 0 || lastDot == value.length() - 1)
		{
			return new InvalidDomainNameProblem(instance, annotation, target,
					value);
		}

		/*
		 * Validate top level domains.
		 */
		final String tld = s.substring(lastDot + 1);

		if (validateTLDs)
		{
			if (TLDS.contains(tld.toUpperCase()))
			{
				return ValidationRoutines.validateHost(instance, annotation,
						target, value, reverse);
			}

			// Not a valid tld
			return new InvalidDomainNameProblem(instance, annotation, target,
					value);
		}

		// Do not validate top level domains
		return ValidationRoutines.validateHost(instance, annotation, target,
				value, reverse);
	}

	/**
	 * Validate a host.
	 * 
	 * @param instance
	 *            the object instance
	 * @param annotation
	 *            the annotation
	 * @param target
	 *            the method
	 * @param name
	 *            the host name
	 * @param reverse
	 *            whether or not to perform a reverse lookup
	 * @return null if there is no problem or the problem if there was one.
	 */
	public static InvalidHostNameProblem validateHost(final Object instance,
			final Annotation annotation, final Object target,
			final CharSequence name, final boolean reverse)
	{
		if (reverse)
		{
			try
			{
				InetAddress.getByName(name.toString());
				return null;
			}
			catch (final UnknownHostException e)
			{
				return new InvalidHostNameProblem(instance, annotation, target,
						name);
			}
		}

		final char[] chars = name.toString().toCharArray();

		if (chars[0] == '.' || chars[chars.length - 1] == '.')
		{
			return new InvalidHostNameProblem(instance, annotation, target,
					name);
		}

		boolean previousWasDot = false;

		for (final char c : chars)
		{
			if (c == '.')
			{
				// Two . back to back are invalid
				if (previousWasDot)
				{
					return new InvalidHostNameProblem(instance, annotation,
							target, name);
				}

				previousWasDot = true;
			}
			else if (isValidHostNameChar(c))
			{
				previousWasDot = false;
			}
			else
			// Not a valid host name char
			{
				return new InvalidHostNameProblem(instance, annotation, target,
						name);
			}
		}

		return null;
	}

	private ValidationRoutines()
	{
		// hide me
	}
}