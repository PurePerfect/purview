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

/**
 * Only selects getter methods. The method is ignored if it does not start with
 * "get", is static, abstract, or the return type is void.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class GetterMethodFilter implements MethodFilter
{
	private static final GetterMethodFilter defaultInstance = new GetterMethodFilter();

	/**
	 * Get a singleton instance.
	 * 
	 * @return a singleton instance.
	 */
	public static final GetterMethodFilter defaultInstance()
	{
		return defaultInstance;
	}

    /**
     * {@inheritDoc}
     */
	public boolean include(final Method method)
	{
		if (method == null)
		{
			return false;
		}

		if (method.isSynthetic())
		{
			return false;
		}

		if (method.isBridge())
		{
			return false;
		}

		final String name = method.getName();

		return name.startsWith("get") && !ReflectionUtils.isStatic(method)
				&& !ReflectionUtils.isVoid(method);
	}
}