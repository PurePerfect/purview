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

import com.pureperfect.purview.ValidationException;
import com.pureperfect.purview.ValidationProblem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Default to a specified value if the value under validation is null or is an
 * empty string. If the annotation is attached to a field, that field will be
 * populated with the default value. If the annotation is attached to a method
 * that starts with "get", the corresponding setter will be called.
 * <p/>
 * <p>
 * E.G:
 * </p>
 * <p/>
 * <pre>
 *
 * public class MyClass
 * {
 *  //When populating fields
 *  &#064;Default("foo")
 *  private String name;
 *
 *  ...//or
 *
 *  //Will attempt to call setName(String)
 *  &#064;Default("foo")
 *  public String getName() {
 *    ...
 *  };
 * }
 * </pre>
 *
 * @author J. Chris Folsom
 * @version 1.3
 * @since 1.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
        {ElementType.METHOD, ElementType.FIELD})
public @interface Default
{
    /**
     * Default to a specified value if the value under validation is null or is
     * an empty string. If the annotation is attached to a field, that field
     * will be populated with the default value. If the annotation is attached
     * to a method that starts with "get", the corresponding setter will be
     * called.
     *
     * @author J. Chris Folsom
     * @version 1.3
     * @since 1.3
     */
    public class Validator
            implements
            com.pureperfect.purview.Validator<ValidationProblem<Object, Default, Object, CharSequence>, Object, Default, Object, CharSequence>
    {
        /**
         * {@inheritDoc}
         */
        public ValidationProblem<Object, Default, Object, CharSequence> validate(
                final Object instance, final Default annotation,
                final Object target, final CharSequence value)
        {
            if(value == null || value.length() < 1)
            {
                try
                {
                    if(target instanceof Method)
                    {
                        Method m = (Method) target;

                        String methodName = m.getName();

                        /*
                               * Check to see if the method is a getter. If it is, try
                               * to call a corresponding setter.
                               */
                        if(methodName.startsWith("get"))
                        {
                            StringBuilder temp = new StringBuilder(32);

                            temp.append("set");
                            temp.append(methodName.substring(3));

                            //TODO determine type rather than assuming string
                            Method setter = instance.getClass().getMethod(
                                    temp.toString(), new Class[]
                                    {String.class});

                            setter.invoke(instance, annotation.value());
                        }
                    }
                    /*
                          * If it is a field we can just set the value directly.
                          */
                    else if(target instanceof Field)
                    {
                        Field f = (Field) target;

                        f.set(instance, annotation.value());
                    }
                } catch (Exception e)
                {
                    throw new ValidationException(e);
                }
            }

            return null;
        }
    }

    /**
     * The {@link Default.Validator Validator} for this annotation.
     *
     * @return the {@link Default.Validator} class
     */
    public Class<?> validator() default Validator.class;

    /**
     * The value to use as a default if no value is provided.
     *
     * @return the value to use as a default if no value was provided.
     */
    String value();
}