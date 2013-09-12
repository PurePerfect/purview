package com.pureperfect.purview.util;

import java.lang.reflect.Method;

/**
 * Allow any method. In other words, always return <code>true.</code>
 * 
 * @author J. Chris Folsom
 * @version 1.3
 * @since 1.3
 */
public class AnyMethodFilter implements MethodFilter
{
    /**
     * {@inheritDoc}
     */
	public boolean include(Method method)
	{
		return true;
	}
}
