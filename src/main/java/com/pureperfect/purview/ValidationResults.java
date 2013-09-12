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
import java.util.Collections;
import java.util.LinkedList;

/**
 * Validation results indicate the results of a validation process.
 * 
 * @author J. Chris Folsom
 * @version 1.1
 * @since 1.0
 */
public class ValidationResults
{
	@SuppressWarnings("rawtypes")
	private final Collection<ValidationProblem> problems;

	private boolean validated;

	/**
	 * Default constructor.
	 */
	@SuppressWarnings("rawtypes")
	public ValidationResults()
	{
		this.problems = new LinkedList<ValidationProblem>();
	}

	/**
	 * Add a validation problem.
	 * 
	 * @param problem
	 *            the problem to add.
	 */
	@SuppressWarnings("rawtypes")
	public void addProblem(final ValidationProblem problem)
	{
		this.problems.add(problem);
	}

	/**
	 * Add a set of validation problems to the to results.
	 * 
	 * @param problems
	 *            the problems to add.
	 */
	@SuppressWarnings("rawtypes")
	public void addProblems(
			@SuppressWarnings("hiding") final Collection<ValidationProblem> problems)
	{
		this.problems.addAll(problems);
	}

	/**
	 * Get the validation problems.
	 * 
	 * @return the problems that occurred.
	 */
	@SuppressWarnings("rawtypes")
	public Collection<ValidationProblem> getProblems()
	{
		return Collections.unmodifiableCollection(this.problems);
	}

	/**
	 * Determine whether or not validation was run. There are some cases where
	 * results may exist, but validation was not run (like when there were no
	 * validation annotations).
	 * 
	 * @return whether or not validation occurred.
	 */
	public boolean isValidated()
	{
		return this.validated;
	}

	/**
	 * Merge the validation results with these results.
	 * 
	 * @param results
	 *            the results to merge
	 */
	public void merge(final ValidationResults results)
	{
		if (results.validated)
		{
			this.validated = true;
		}

		this.addProblems(results.getProblems());
	}

	/**
	 * Set whether or not validation occurred.
	 * 
	 * @param validated
	 *            whether or not validation occurred.
	 */
	public void setValidated(final boolean validated)
	{
		this.validated = validated;
	}
}