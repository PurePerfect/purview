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
package com.pureperfect.purview.validators.text;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pureperfect.purview.Purview;

/**
 * Unit test for {@link UpperCase} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.3
 * @since 1.3
 */
public class UpperCaseTest extends TestCase
{
	//TODO this package
	
	public class Stub
	{
		@UpperCase
		String value;

		@UpperCase
		public String getValue()
		{
			return this.value;
		}

		public void foo(@UpperCase String bar)
		{
			// testing
		}
	}

	public class RequiredStub
	{
		@UpperCase(required=true)
		String value;
	}
	
	public void testField() throws Exception
	{
		Stub stub = new Stub();

		assertEquals(0, Purview.validateFields(stub).getProblems().size());

		stub.value = "Asdf";

		assertEquals(1, Purview.validateFields(stub).getProblems().size());

		stub.value = "ASDF";

		assertEquals(0, Purview.validateFields(stub).getProblems().size());
	}

	public void testMethod() throws Exception
	{
		Stub stub = new Stub();

		assertEquals(0, Purview.validateMethods(stub).getProblems().size());

		stub.value = "Asdf";

		assertEquals(1, Purview.validateMethods(stub).getProblems().size());

		stub.value = "ASDF";

		assertEquals(0, Purview.validateMethods(stub).getProblems().size());
	}

	public void testParameters() throws Exception
	{
		Stub stub = new Stub();
		
		Method m = stub.getClass().getMethod("foo", new Class[] {String.class});
		
		assertEquals(0, Purview.validateParameters(stub, m, new Object[]{null}).getProblems().size());
		
		assertEquals(1, Purview.validateParameters(stub, m, new Object[]{"asdf"}).getProblems().size());
		
		assertEquals(0, Purview.validateParameters(stub, m, new Object[]{"ASDF"}).getProblems().size());
		
	}

	public void testRequired()
	{
		RequiredStub stub = new RequiredStub();

		assertEquals(1, Purview.validateFields(stub).getProblems().size());

		stub.value = "Asdf";

		assertEquals(1, Purview.validateFields(stub).getProblems().size());

		stub.value = "ASDD";

		assertEquals(0, Purview.validateFields(stub).getProblems().size());
	}
}
