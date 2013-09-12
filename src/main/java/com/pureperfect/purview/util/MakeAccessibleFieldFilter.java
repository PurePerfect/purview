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

import java.lang.reflect.Field;

/**
 * Make the field accessible by calling field.setAccessible(true).
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class MakeAccessibleFieldFilter implements FieldFilter
{
	private static MakeAccessibleFieldFilter defaultInstance = new MakeAccessibleFieldFilter();

	/**
	 * Get a singleton instance of this class.
	 * 
	 * @return A singleton instance of this class.
	 */
	public static MakeAccessibleFieldFilter defaultInstance()
	{
		return defaultInstance;
	}

	/*
	 * (non-Javadoc)
	 * @see com.pureperfect.purview.util.FieldFilter#include(java.lang.reflect.Field)
	 */
	public boolean include(final Field field)
	{
		field.setAccessible(true);
		return field.isAccessible();
	}
}
