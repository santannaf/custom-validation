package com.example.customvalidation.annotation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;

import static org.springframework.util.ObjectUtils.isEmpty;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@Slf4j
public class ConditionalValidator implements ConstraintValidator<Conditional, Object> {

    private String selected;
    private String[] requires;
    private String message;
    //private String[] values;
    private boolean isNullOrEmpty;

    @Override
    public void initialize(Conditional conditional) {
        selected = conditional.selected();
        requires = conditional.requires();
        message = conditional.message();
        //values = conditional.values();
        isNullOrEmpty = conditional.isNullOrEmpty();
    }

    @Override
    public boolean isValid(Object object, ConstraintValidatorContext context) {
        boolean valid;
        try {
            Object checkedValue = BeanUtils.getProperty(object, selected);
            valid = checkedValue != null && !isEmpty(checkedValue);

            if (!isNullOrEmpty) {
                if (requires.length > 0) {
                    for (String propName : requires) {
                        Object requiredValue = BeanUtils.getProperty(object, propName);
                        valid = requiredValue != null && !isEmpty(requiredValue);
                        if (!valid) {
                            log.warn("Field '{}' selected, but value of field '{}' is not present. Field '{}' is required.",
                                    selected, propName, propName);
                            setContext(context, propName);
                        }
                    }
                }
                if (!valid) {
                    log.error("Field '{}' is invalid", selected);
                    setContext(context, selected);
                }
            } else {
                return true;
            }

            return valid;
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
    }

    private void setContext(ConstraintValidatorContext context, String field) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(field)
                .addConstraintViolation();
    }
}