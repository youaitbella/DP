/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

/**
 * Check if the member variables confirm the annotions like @Min, @Max, @Length ...
 * @author muellermi
 */
public class BeanValidator {

    public static <T> String validateData(T entity) {
        return validateData(entity, 0);
    }

    public static <T> String validateData(T entity, int lineNum) {
        String alertText = "";
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(entity);
        for (ConstraintViolation<T> violation : violations) {
            if (lineNum > 0) {
                alertText += "Zeile " + lineNum + ": ";
            }
            //alertText += violation.getPropertyPath().toString();

            alertText += violation.getMessage() + ": fehlerhafter Wert [" + violation.getInvalidValue() + "]\n";
        }
        return alertText;
    }

}
