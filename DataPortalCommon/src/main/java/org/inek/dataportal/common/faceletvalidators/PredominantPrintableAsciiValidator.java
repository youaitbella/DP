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

import java.util.HashSet;
import java.util.Set;

/**
 * Checks whether a required files contains at least one non whitespace
 * character
 *
 * @author muellermi
 */
@FacesValidator(value = "PredominantPrintableAsciiValidator")
public class PredominantPrintableAsciiValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }
        if (!isValidName("" + value)) {
            String msg = Utils.getMessage("msgAsciiNeeded");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public boolean isValidName(String name) {
        String test = name.replaceAll("(\\r|\\n|\\u0085|\\u2028|\\u2029)", ""); // remove line breaks to avoid matching conflicts
        Set<Integer> validSpecialCharacters = new HashSet<>();
        validSpecialCharacters.add(196); // Ä
        validSpecialCharacters.add(228); // ä

        validSpecialCharacters.add(214); // Ö
        validSpecialCharacters.add(246); // ö

        validSpecialCharacters.add(220); // Ü
        validSpecialCharacters.add(252); // ü

        validSpecialCharacters.add(223); // ?

        long asciiCount = test.chars().filter(c -> c >= 32 && c < 127 || validSpecialCharacters.contains(c)).count();
        return asciiCount >= test.length() * .9;
    }
}
