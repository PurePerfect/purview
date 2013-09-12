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
package com.pureperfect.purview.validators.biz;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.pureperfect.purview.Purview;

/**
 * Unit test for {@link Phone} annotation.
 * 
 * @author J. Chris Folsom
 * @version 1.3
 * @since 1.3
 */
public class PhoneTest extends TestCase
{
	// TODO this package

	/**
	 * Stub class for testing.
	 */
	public class BothStub
	{
		@Phone(validationMode = Phone.BOTH)
		public String field;
		
		@Phone(validationMode = Phone.BOTH)
		public String getField()
		{
			return this.field;
		}

        public void paramTest(@Phone(validationMode = Phone.BOTH) String phone)
        {
            //For testing purposes only
        }
	}

	/**
	 * Stub class for testing.
	 */
	public class DashesStub
	{
		@Phone(validationMode = Phone.DASHES)
		public String field;
		
		@Phone(validationMode = Phone.DASHES)
		public String getField()
		{
			return this.field;
		}

        public void paramTest(@Phone(validationMode = Phone.DASHES) String phone)
        {
            //For testing purposes only
        }
	}

	/**
	 * Stub class for testing.
	 */
	public class NoDashesStub
	{
		@Phone(validationMode = Phone.NO_DASHES)
		public String field;
		
		@Phone(validationMode = Phone.NO_DASHES)
		public String getField()
		{
			return this.field;
		}

        public void paramTest(@Phone(validationMode = Phone.NO_DASHES) String phone)
        {
            //For testing purposes only
        }
	}

	/**
	 * Stub class for testing.
	 */
	public class RequiredStub
	{
		@Phone(required = true)
		public String field;
	}

	/**
	 * Stub class for testing.
	 */
	public class Stub
	{
		@Phone
		public String field;

		@Phone
		public String getField()
		{
			return this.field;
		}

		public void paramTest(@Phone final String crap)
		{
			// do nothing
		}
	}

	/**
	 * Test bad characters.
	 */
	public void testFieldBothBadChars()
	{
		final BothStub mock = new BothStub();

		mock.field = "123-456-78a";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());

		mock.field = "12345678a";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test misplaced dashes.
	 */
	public void testFieldBothMisplacedDashes()
	{
		final BothStub mock = new BothStub();

		mock.field = "123-456-78911";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());

		mock.field = "12345678911";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test valid values.
	 */
	public void testFieldBothOkay()
	{
		final BothStub mock = new BothStub();

		mock.field = "123-456-7891";

		assertEquals(0, Purview.validateFields(mock).getProblems().size());

		mock.field = "1234567890";

		assertEquals(0, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test when value is too long.
	 */
	public void testFieldBothTooLong()
	{
		final BothStub mock = new BothStub();

		mock.field = "123-456-78911";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());

		mock.field = "12345678911";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test when value is too short.
	 */
	public void testFieldBothTooShort()
	{
		final BothStub mock = new BothStub();

		mock.field = "123-456-781";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());

		mock.field = "12345671";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test bad chars.
	 */
	public void testFieldDashesBadChars()
	{
		final DashesStub mock = new DashesStub();

		mock.field = "123-45a-6791";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test dashes misplaced.
	 */
	public void testFieldDashesDashesMisplaced()
	{
		final DashesStub mock = new DashesStub();

		mock.field = "123-45-67891";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test dashes missing.
	 */
	public void testFieldDashesDashesMissing()
	{
		final DashesStub mock = new DashesStub();

		mock.field = "1234516891";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test okay
	 */
	public void testFieldDashesOkay()
	{
		final DashesStub mock = new DashesStub();

		mock.field = "123-451-6891";

		assertEquals(0, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test dashes too long.
	 */
	public void testFieldDashesTooLong()
	{
		final DashesStub mock = new DashesStub();

		mock.field = "123-451-17891";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test value too short.
	 */
	public void testFieldDashesTooShort()
	{
		final DashesStub mock = new DashesStub();

		mock.field = "123-451-178";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test bad chars.
	 */
	public void testFieldNoDashesBadChars()
	{
		final NoDashesStub mock = new NoDashesStub();

		mock.field = "1234567a1";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test okay
	 */
	public void testFieldNoDashesOkay()
	{
		final NoDashesStub mock = new NoDashesStub();

		mock.field = "1234516791";

		assertEquals(0, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test dashes too long.
	 */
	public void testFieldNoDashesTooLong()
	{
		final NoDashesStub mock = new NoDashesStub();

		mock.field = "12345167911";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test no dashes with dashes.
	 */
	public void testFieldNoDashesWithDashes()
	{
		final NoDashesStub mock = new NoDashesStub();

		mock.field = "123-451-6791";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test dashes too short.
	 */
	public void testFieldsDashesTooShort()
	{
		final DashesStub mock = new DashesStub();

		mock.field = "123-451-891";

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Test bad chars.
	 */
	public void testMethodBothBadChars()
	{
		final BothStub mock = new BothStub();

		mock.field = "123-456-78a1";

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());

		mock.field = "12345678a1";

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());
	}

	/**
	 * Test valid values.
	 */
	public void testMethodBothOkay()
	{
		final BothStub mock = new BothStub();

		mock.field = "123-456-7811";

		assertEquals(0, Purview.validateMethods(mock).getProblems().size());

		mock.field = "1234567811";

		assertEquals(0, Purview.validateMethods(mock).getProblems().size());
	}

    /**
     * Test both too long.
     */
	public void testMethodBothTooLong()
	{
		final BothStub mock = new BothStub();

		mock.field = "123-456-78111";

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());

		mock.field = "12345678991";

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());
	} 

	/**
	 * Test values too short.
	 */
	public void testMethodBothTooShort()
	{
		final BothStub mock = new BothStub();

		mock.field = "123-456-781";

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());

		mock.field = "123456781";

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());
	}

	/**
	 * Test bad characters.
	 */
	public void testMethodDashesBadChars()
	{
		final DashesStub mock = new DashesStub();

		mock.field = "123-456-78a1";

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());
	}

	/**
	 * Test misplaced dash.
	 */
	public void testMethodDashesDashesMisplaced()
	{
		final DashesStub mock = new DashesStub();

		mock.field = "123-45-67811";

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());
	}

    /**
     * Test dashes with no dashes in value.
     */
	public void testMethodDashesDashesMissing()
	{
		final DashesStub mock = new DashesStub();

		mock.field = "1234567811";

		assertEquals(1, Purview.validateMethods(mock).getProblems().size());
	}

	/**
	 * Test valid values.
	 */
	public void testMethodDashesOkay()
	{
		final DashesStub mock = new DashesStub();

		mock.field = "123-451-6781";

		assertEquals(0, Purview.validateMethods(mock).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testMethodDashesTooLong()
	{
        final DashesStub mock = new DashesStub();

        mock.field = "123-451-67811";

        assertEquals(1, Purview.validateMethods(mock).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testMethodDashesTooShort()
	{
        final DashesStub mock = new DashesStub();

        mock.field = "123-451-678";

        assertEquals(1, Purview.validateMethods(mock).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testMethodNoDashesBadChars()
	{
        final NoDashesStub mock = new NoDashesStub();

        mock.field = "123451678a";

        assertEquals(1, Purview.validateMethods(mock).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testMethodNoDashesOkay()
	{
        final NoDashesStub mock = new NoDashesStub();

        mock.field = "1234516789";

        assertEquals(0, Purview.validateMethods(mock).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testMethodNoDashesTooLong()
	{
        final NoDashesStub mock = new NoDashesStub();

        mock.field = "12345167891";

        assertEquals(1, Purview.validateMethods(mock).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testMethodNoDashesTooShort()
	{
        final NoDashesStub mock = new NoDashesStub();

        mock.field = "123451678";

        assertEquals(1, Purview.validateMethods(mock).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testMethodNoDashesWithDashes()
	{
        final NoDashesStub mock = new NoDashesStub();

        mock.field = "123-451=1678";

        assertEquals(1, Purview.validateMethods(mock).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersBothBadChars() throws NoSuchMethodException
    {
        final BothStub mock = new BothStub();

        String value = "123-456-789a";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());

        value = "123456789a";

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersBothOkay() throws NoSuchMethodException
    {
		final BothStub mock = new BothStub();

        String value = "123-456-7891";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(0, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());

        value = "1234567891";

        assertEquals(0, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
    }

    /**
     * Test case.
     */
	public void testParametersBothTooShort() throws NoSuchMethodException
    {
        final BothStub mock = new BothStub();

        String value = "123-456-789";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());

        value = "123456789";

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersDashesBadChars() throws NoSuchMethodException
    {
        final DashesStub mock = new DashesStub();

        String value = "123-8a-1234";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersDashesDashesMisplaced() throws NoSuchMethodException
    {
        final DashesStub mock = new DashesStub();

        String value = "123-811-234";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersDashesDashesMissing() throws NoSuchMethodException
    {
        final DashesStub mock = new DashesStub();

        String value = "1234568911";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersDashesOkay() throws NoSuchMethodException
    {
        final DashesStub mock = new DashesStub();

        String value = "123-456-7891";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(0, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersDashesTooShort() throws NoSuchMethodException
    {
        final DashesStub mock = new DashesStub();

        String value = "123-456-781";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersNoDashesBadChars() throws NoSuchMethodException
    {
        final NoDashesStub mock = new NoDashesStub();

        String value = "1238a1234";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersNoDashesOkay() throws NoSuchMethodException
    {
        final NoDashesStub mock = new NoDashesStub();

        String value = "1238112341";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(0, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersNoDashesTooLong() throws NoSuchMethodException
    {
        final NoDashesStub mock = new NoDashesStub();

        String value = "12381234111";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersNoDashesTooShort() throws NoSuchMethodException
    {
        final NoDashesStub mock = new NoDashesStub();

        String value = "123811234";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersNoDashesWithDashes() throws NoSuchMethodException
    {
        final NoDashesStub mock = new NoDashesStub();

        String value = "123-811-1234";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersBothTooLong() throws NoSuchMethodException
    {
        final BothStub mock = new BothStub();

        String value = "123-456-78911";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());

        value = "12345678911";

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

    /**
     * Test case.
     */
	public void testParametersDashesTooLong() throws NoSuchMethodException
    {
        final DashesStub mock = new DashesStub();

        String value = "123-456-78911";

        Method method = mock.getClass().getMethod("paramTest", new Class[]{String.class});

        assertEquals(1, Purview.validateParameters(mock, method, new String[]{value}).getProblems().size());
	}

	/**
	 * Make sure that when a required field is missing, the appropriate problem
	 * is returned.
	 */
	public void testRequiredIsEmptyString()
	{
		final RequiredStub mock = new RequiredStub();

		mock.field = null;

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Make sure that when a required field is missing, the appropriate problem
	 * is returned.
	 */
	public void testRequiredIsNull()
	{
		final RequiredStub mock = new RequiredStub();

		mock.field = null;

		assertEquals(1, Purview.validateFields(mock).getProblems().size());
	}

	/**
	 * Required field should be okay if it is not null.
	 */
	public void testRequiredOkay()
	{
		final RequiredStub mock = new RequiredStub();

		mock.field = "123-456-7891";

		assertEquals(0, Purview.validateFields(mock).getProblems().size());
	}
}