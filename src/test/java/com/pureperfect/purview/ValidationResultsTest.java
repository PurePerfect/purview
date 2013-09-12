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
package com.pureperfect.purview;

import java.util.Collection;
import java.util.LinkedList;

import junit.framework.TestCase;

/***
 * Unit test for {@link ValidationResults}.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class ValidationResultsTest extends TestCase
{
	/**
	 * Stub class for testing.
	 */
	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	private class Stub extends ValidationProblem
	{
		public Stub()
		{
			super(null, null, null, null);
		}
	}

	/**
	 * Test add problem.
	 */
	public void testAddProblem()
	{
		final ValidationResults results = new ValidationResults();

		assertEquals(0, results.getProblems().size());

		results.addProblem(new Stub());

		assertEquals(1, results.getProblems().size());
	}

	/**
	 * Test add problems.
	 */
	@SuppressWarnings(
	{ "rawtypes" })
	public void testAddProblems()
	{
		final ValidationResults results = new ValidationResults();
		final Collection<ValidationProblem> probs = new LinkedList<ValidationProblem>();

		probs.add(new Stub());
		probs.add(new Stub());

		assertEquals(0, results.getProblems().size());

		results.addProblems(probs);

		assertEquals(2, results.getProblems().size());
	}

	/**
	 * Test merge.
	 */
	@SuppressWarnings("rawtypes")
	public void testMerge()
	{
		// TEST empty
		ValidationResults one = new ValidationResults();
		ValidationResults two = new ValidationResults();

		one.merge(two);

		one.merge(two);

		assertEquals(0, one.getProblems().size());
		assertFalse(one.isValidated());

		two.setValidated(true);
		one.merge(two);
		assertEquals(0, one.getProblems().size());
		assertTrue(one.isValidated());

		one = new ValidationResults();
		two = new ValidationResults();

		two.merge(one);

		assertEquals(0, two.getProblems().size());
		assertFalse(two.isValidated());

		two.setValidated(true);
		two.merge(one);
		assertEquals(0, two.getProblems().size());
		assertTrue(two.isValidated());

		// test with problems
		one = new ValidationResults();
		two = new ValidationResults();

		one.setValidated(true);
		two.setValidated(true);

		final Collection<ValidationProblem> oneP = new LinkedList<ValidationProblem>();

		oneP.add(new Stub());
		oneP.add(new Stub());

		one.addProblems(oneP);

		two.addProblem(new Stub());
		two.addProblem(new Stub());
		two.addProblem(new Stub());

		one.merge(two);
		assertTrue(one.isValidated());
		assertEquals(5, one.getProblems().size());
	}

	/**
	 * Test set validated.
	 */
	public void testSetValidated()
	{
		final ValidationResults results = new ValidationResults();

		assertFalse(results.isValidated());

		results.setValidated(true);

		assertTrue(results.isValidated());
	}
}