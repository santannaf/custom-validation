package com.example.customvalidation.annotation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import static org.springframework.util.ObjectUtils.isEmpty;

@Slf4j
public class ConditionalValidator implements ConstraintValidator<Conditional, Object> {

    private String selected;
    private String[] required;
    private String message;
    private String[] values;
    private boolean isNullOrEmpty;

    @Override
    public void initialize(Conditional conditional) {
        selected = conditional.selected();
        required = conditional.required();
        message = conditional.message();
        values = conditional.values();
        isNullOrEmpty = conditional.isNullOrEmpty();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        boolean valid = true;
        try {
            Object checkedValue = BeanUtils.getProperty(object, selected);

            if (!isNullOrEmpty) {
                for (String propName : required) {
                    Object requiredValue = BeanUtils.getProperty(object, propName);
                    valid = requiredValue != null && !isEmpty(requiredValue);
                    System.out.println("value: " + "" + requiredValue);

                    if (!valid) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(message)
                                .addPropertyNode(propName)
                                .addConstraintViolation();
                    }
                }
                return valid;
            }


            if (checkedValue == null || checkedValue == "") {
                checkedValue = "";
            }

            if (Arrays.asList(values).contains(checkedValue)) {
                for (String propName : required) {
                    Object requiredValue = BeanUtils.getProperty(object, propName);
                    valid = requiredValue != null && !isEmpty(requiredValue);
                    System.out.println("value: " + "" + requiredValue);
                    if (!valid) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate(message)
                                .addPropertyNode(propName)
                                .addConstraintViolation();
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error("Accessor method is not available for class : {}, exception : {}", object.getClass().getName(), e);
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e) {
            log.error("Field or method is not present on class : {}, exception : {}", object.getClass().getName(), e);
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            log.error("An exception occurred while accessing class : {}, exception : {}", object.getClass().getName(), e);
            e.printStackTrace();
            return false;
        }
        return valid;
    }
}