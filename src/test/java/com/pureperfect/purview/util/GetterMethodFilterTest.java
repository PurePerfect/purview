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
package com.pureperfect.purview.util;

import java.lang.reflect.Method;
import java.util.Collection;

import junit.framework.TestCase;

import com.pureperfect.purview.validators.NoValidation;

/***
 * Unit test for {@link GetterMethodFilter}.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class GetterMethodFilterTest extends TestCase
{
	/**
	 * Stub class for testing.
	 */
	public class Stub
	{
		/**
		 * Matches filter
		 */
		public String getName()
		{
			return "foo";
		}

		/**
		 * Matches filter
		 */
		@NoValidation
		public String getPassword()
		{
			return "bar";
		}

		/**
		 * should not be grabbed by filter since return type is null.
		 */
		public void getVoid()
		{
			//do nothing
		}
		
		/**
		 * should not be grabbed by filter since it's not a getter
		 */
		public Boolean blah()
		{
			return Boolean.FALSE;
		}
	}

	/**
	 * Make sure only getter methods are included.
	 */
	public void testInclude()
	{
		final Stub form = new Stub();

		Collection<Method> methods = ReflectionUtils.getDeclaredMethods(form,
				GetterMethodFilter.defaultInstance());
		
		assertEquals(
				2,
				methods.size());
		
		for(Method m : methods)
		{
			assertTrue("getPassword".equals(m.getName()) || "getName".equals(m.getName()));
		}
	}
}
