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
package com.pureperfect.purview.validators.numeric;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pureperfect.purview.Purview;

/**
 * Unit test for {@link AllowOnly} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.3
 * @since 1.3
 */
public class AllowOnlyTest extends TestCase
{
	//TODO this package
	
	public class Stub
	{
		@AllowOnly({9, 3})
		private Double value;

		@AllowOnly({9, 3})
		public Double getValue()
		{
			return this.value;
		}

		public void setValue(Double value)
		{
			this.value = value;
		}

		public void foo(@AllowOnly({9, 3}) Double bar)
		{
			// test method
		}
	}
	
	public class RequiredStub
	{
		@AllowOnly(value={9, 3, 11.1}, required=true)
		public Double value;
	}
	
	@SuppressWarnings("boxing")
	public void testField() throws Exception
	{
		Stub mock = new Stub();

		assertEquals(0, Purview.validateFields(mock).getProblems().size());

		mock.setValue(3.14289);
		
		assertEquals(1, Purview.validateFields(mock).getProblems().size());
		
		mock.setValue((double) 3);
		
		assertEquals(0, Purview.validateFields(mock).getProblems().size());
	}
	
	@SuppressWarnings("boxing")
	public void testMethod() throws Exception
	{
		Stub mock = new Stub();

		assertEquals(0, Purview.validateMethods(mock).getProblems().size());
		
		mock.setValue(3.14289);
		
		assertEquals(1, Purview.validateMethods(mock).getProblems().size());
		
		mock.setValue((double) 3);
		
		assertEquals(0, Purview.validateMethods(mock).getProblems().size());
	}

	@SuppressWarnings("boxing")
	public void testParameters() throws Exception
	{
		Stub mock = new Stub();

		Method m = mock.getClass().getMethod("foo", new Class[]{Double.class});
		
		assertEquals(0, Purview.validateParameters(mock, m, new Object[] {null}).getProblems().size());
		
		assertEquals(1, Purview.validateParameters(mock, m, new Object[] {69.1}).getProblems().size());
		
		assertEquals(0, Purview.validateParameters(mock, m, new Object[] {(double)9}).getProblems().size());
	}

	@SuppressWarnings("boxing")
	public void testRequired()
	{
		RequiredStub stub = new RequiredStub();
		
		assertEquals(1, Purview.validateFields(stub).getProblems().size());
		
		stub.value = 0.1;
		
		assertEquals(1, Purview.validateFields(stub).getProblems().size());
		
		stub.value = (double) 9;
		
		assertEquals(0, Purview.validateFields(stub).getProblems().size());
	}
}
