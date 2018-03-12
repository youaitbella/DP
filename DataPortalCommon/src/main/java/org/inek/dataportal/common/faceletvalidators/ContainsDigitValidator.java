/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.faceletvalidators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.inek.dataportal.common.helper.Utils;

/**
 * Checks whether a required files contains at least one non whitespace
 * character
 *
 * @author muellermi
 */
@FacesValidator(value = "ContainsDigitValidator")
public class ContainsDigitValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }
        if (!isValidName("" + value)) {
            String msg = Utils.getMessage("msgDigitNeeded");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public boolean isValidName(String name) {
        String test = name.replaceAll("(\\r|\\n|\\u0085|\\u2028|\\u2029)", ""); // remove line breaks to avoid matching conflicts
        return test.matches(".*[0-9]+.*");
    }
}
