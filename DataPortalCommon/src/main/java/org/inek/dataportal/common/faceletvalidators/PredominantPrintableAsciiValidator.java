/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.faceletvalidators;

import org.inek.dataportal.common.helper.Utils;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
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

    public static final int UMLAUT_UPPER_A = 196;
    public static final int UMLAUT_LOWER_A = 228;
    public static final int UMLAUT_UPPER_O = 214;
    public static final int UMLAUT_LOWER_O = 246;
    public static final int UMLAUT_UPPER_U = 220;
    public static final int UMLAUT_LOWER_U = 252;
    public static final int GERMAN_SZ = 223;

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
        validSpecialCharacters.add(UMLAUT_UPPER_A);
        validSpecialCharacters.add(UMLAUT_LOWER_A);

        validSpecialCharacters.add(UMLAUT_UPPER_O);
        validSpecialCharacters.add(UMLAUT_LOWER_O);

        validSpecialCharacters.add(UMLAUT_UPPER_U);
        validSpecialCharacters.add(UMLAUT_LOWER_U);

        validSpecialCharacters.add(GERMAN_SZ);

        long asciiCount = test.chars().filter(c -> c >= 32 && c < 127 || validSpecialCharacters.contains(c)).count();
        return asciiCount >= test.length() * .9;
    }
}
