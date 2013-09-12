package com.pureperfect.purview;

import com.pureperfect.purview.util.FieldFilter;
import com.pureperfect.purview.util.MethodFilter;
import com.pureperfect.purview.util.ReflectionUtils;
import com.pureperfect.purview.validators.NoValidation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;

/**
 * Default {@link ValidationEngine} implementation.
 *
 * @author J. Chris Folsom
 * @version 1.3
 * @since 1.3
 */
public class ValidationEngineImpl implements ValidationEngine
{
    private ValidatorFactory validatorFactory;

    /*
     * TESTME also
     */

    /**
     * Create a new validation engine using the specified factory.
     */
    public ValidationEngineImpl(ValidatorFactory validatorFactory)
    {
        this.validatorFactory = validatorFactory;
    }

    /**
     * {@inheritDoc}
     */
    public ValidationResults validateFields(final Object instance)
    {
        return validateFields(instance, ValidationEngine.DEFAULT_FIELD_FILTER);
    }

    /**
     * {@inheritDoc}
     */
    public ValidationResults validateFields(final Object instance,
                                            final boolean useStrictMode)
    {
        return validateFields(instance, ValidationEngine.DEFAULT_FIELD_FILTER,
        useStrictMode);
    }

    /**
     * {@inheritDoc}
     */
    public ValidationResults validateFields(final Object instance,
                                            final FieldFilter filter)
    {
        return validateFields(instance, filter, false);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(
    {"unchecked", "rawtypes"})
    public ValidationResults validateFields(final Object instance,
                                            final FieldFilter filter, final boolean strict)
    {
        final Collection<Field> fields = ReflectionUtils.getDeclaredFields(
        instance, filter);

        final ValidationResults results = new ValidationResults();

        boolean wasValidated = false;

        for (final Field field : fields)
        {
            /*
             * Explicitly skip outer class reference if it exists.
             */
            if("this$0".equals(field.getName()))
            {
                continue;
            }

            final Annotation[] annotations = field.getDeclaredAnnotations();

            if(annotations != null)
            {
                for (final Annotation annotation : annotations)
                {
                    final Validator validator = this.validatorFactory
                    .createValidator(annotation);

                    if(validator != null)
                    {
                        try
                        {
                            final Object value = field.get(instance);

                            final ValidationProblem problem = validator
                            .validate(instance, annotation, field,
                            value);

                            if(problem != null)
                            {
                                results.addProblem(problem);
                            }

                            wasValidated = true;
                        } catch (final ValidationException e)
                        {
                            throw e;
                        } catch (final Throwable t)
                        {
                            throw new ValidationException(t);
                        }
                    }
                }
            }

            /*
             * Strict mode check.
             */
            if(wasValidated)
            {
                results.setValidated(true);
            } else if(strict)
            {
                final Object novalidation = field
                .getAnnotation(NoValidation.class);

                if(novalidation == null)
                {
                    throw new ValidationException(
                    "STRICT MODE IS ON, but field \""
                    + field
                    + "\" did not have a validation annotation.");
                }
            }
        }

        return results;
    }

    /**
     * Validate the methods on the given instance.
     *
     * @param instance
     * the object that validation is being performed on
     * @param method
     * the method that validation is being performed on
     * @param useStrictMode
     * whether or not to use strict mode
     * @param results
     * used to accumulate values into a single result object
     */
    @SuppressWarnings(
    {"unchecked", "rawtypes"})
    private void validateMethod(final Object instance, final Method method,
                                final boolean useStrictMode, final ValidationResults results)
    {
        /*
         * Validate all method annotations.
         */
        final Annotation[] annotations = method.getDeclaredAnnotations();

        boolean wasValidated = false;

        if(annotations != null)
        {
            /*
             * Run through all of the method's annotations and find validation
             * annotations.
             */
            for (final Annotation annotation : annotations)
            {
                final Validator validator = this.validatorFactory
                .createValidator(annotation);

                try
                {
                    /*
                     * If the factory did not return null, then the annotation
                     * was a validation annotation.
                     */
                    if(validator != null)
                    {
                        final Object value = ReflectionUtils.getValueOfMethod(
                        method, instance);

                        final ValidationProblem problem = validator.validate(
                        instance, annotation, method, value);

                        if(problem != null)
                        {
                            results.addProblem(problem);
                        }

                        wasValidated = true;
                    }
                } catch (final ValidationException t)
                {
                    throw t;
                } catch (final Throwable t)
                {
                    throw new ValidationException(t);
                }
            }
        }

        if(wasValidated)
        {
            results.setValidated(true);
        } else if(useStrictMode)
        {
            final Object novalidation = method
            .getAnnotation(NoValidation.class);

            if(novalidation == null)
            {
                throw new ValidationException(
                "STRICT MODE IS ON, but method \"" + method
                + "\" did not have a validation annotation.");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public ValidationResults validateMethods(final Object instance)
    {
        return validateMethods(instance, ValidationEngine.DEFAULT_METHOD_FILTER);
    }

    /**
     * {@inheritDoc}
     */
    public ValidationResults validateMethods(final Object instance,
                                             final boolean useStrictMode)
    {
        return validateMethods(instance,
        ValidationEngine.DEFAULT_METHOD_FILTER, useStrictMode);
    }

    /**
     * {@inheritDoc}
     */
    public ValidationResults validateMethods(final Object instance,
                                             final MethodFilter filter)
    {
        return validateMethods(instance, filter, false);
    }

    /**
     * {@inheritDoc}
     */
    public ValidationResults validateMethods(final Object instance,
                                             final MethodFilter filter, final boolean useStrictMode)
    {
        try
        {
            final ValidationResults results = new ValidationResults();

            final Collection<Method> getters = ReflectionUtils
            .getDeclaredMethods(instance, filter);

            for (final Method getter : getters)
            {
                validateMethod(instance, getter, useStrictMode, results);
            }

            return results;
        } catch (final ValidationException e)
        {
            throw e;
        } catch (final Throwable e)
        {
            throw new ValidationException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public ValidationResults validateParameters(final Object instance,
                                                final Method method, final Object[] parameters)
    {
        return validateParameters(instance, method, parameters, false);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(
    {"unchecked", "rawtypes"})
    public ValidationResults validateParameters(final Object instance,
                                                final Method method, final Object[] params,
                                                final boolean useStrictMode)
    {
        final ValidationResults results = new ValidationResults();

        final Annotation[][] parameters = method.getParameterAnnotations();

        if(parameters != null)
        {
            /*
             * Iterate over the list of parameters.
             */
            for (int i = 0; i < parameters.length; ++i)
            {
                final Annotation[] annotations = parameters[ i ];

                boolean hasNoValidationAnnotation = false;
                boolean wasValidated = false;

                if(annotations != null)
                {
                    /*
                     * Iterate over the set of annotations for each parameter.
                     */
                    for (int j = 0; j < annotations.length; ++j)
                    {
                        final Annotation annotation = annotations[ j ];

                        /*
                         * Break early if we have NoValidation annotation.
                         */
                        if(NoValidation.class.equals(annotation
                        .annotationType()))
                        {
                            hasNoValidationAnnotation = true;
                            break;
                        }

                        final Validator validator = this.validatorFactory
                        .createValidator(annotation);

                        /*
                               * Perform validation if the annotation is a validation
                               * annotation.
                               */
                        if(validator != null)
                        {
                            final Object value = params[ j ];

                            final ValidationProblem problem = validator
                            .validate(instance, annotation, method,
                            value);

                            if(problem != null)
                            {
                                results.addProblem(problem);
                            }

                            wasValidated = true;
                        }
                    }

                    /*
                          * Perform strict mode checking.
                          */
                    if(wasValidated)
                    {
                        results.setValidated(true);
                    } else if(useStrictMode && !hasNoValidationAnnotation)
                    {
                        throw new ValidationException(
                        "STRICT MODE IS ON, but parameter "
                        + i
                        + " on method \""
                        + method
                        + "\" did not have a validation annotation.");
                    }
                }
            }
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    public ValidationResults validateType(final Object instance)
    {
        return validateType(instance, false);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings(
    {"unchecked", "rawtypes"})
    public ValidationResults validateType(final Object instance,
                                          final boolean useStrictMode)
    {
        final ValidationResults results = new ValidationResults();

        if(instance != null)
        {
            final Class<?> clazz = instance.getClass();

            final Annotation[] annotations = clazz.getDeclaredAnnotations();

            boolean wasValidated = false;

            if(annotations != null)
            {
                for (final Annotation annotation : annotations)
                {
                    final Validator v = this.validatorFactory
                    .createValidator(annotation);

                    if(v != null)
                    {
                        final ValidationProblem problem = v.validate(instance,
                        annotation, instance.getClass(), instance);

                        if(problem != null)
                        {
                            results.addProblem(problem);
                        }

                        wasValidated = true;
                    }
                }
            }

            /*
             * Check for no validation if strict mode is enabled.
             */
            if(wasValidated)
            {
                results.setValidated(true);
            } else if(useStrictMode)
            {
                final Object novalidation = clazz
                .getAnnotation(NoValidation.class);

                if(novalidation == null)
                {
                    throw new ValidationException(
                    "STRICT MODE IS ON, but class \""
                    + clazz.getName()
                    + "\" did not have a validation annotation.");
                }
            }
        }

        return results;
    }
}