/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.faceletvalidators;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@FacesValidator(value="NameValidator")
public class NameValidator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null){return;}
        if (!isValidName("" + value)) {
            String msg = Utils.getMessage("msgInvalidCharacters");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }
    
    public static boolean isValidName(String name){
        return name.matches("\\p{L}+((-|_|\\.| |\\. )?(\\d|\\p{L})+)*");
    }
}
