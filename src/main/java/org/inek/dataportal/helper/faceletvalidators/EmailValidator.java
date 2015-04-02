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
import org.inek.dataportal.facades.TrashMailFacade;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@FacesValidator(value = "EmailValidator")
public class EmailValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) {
            return;
        }
        if (!isValidEmail("" + value)) {
            String msg = Utils.getMessage("msgNoEmail");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public boolean isValidEmail(String addres) {
        boolean isValid = addres.matches("(\\w[a-zA-Z_0-9+-.]*\\w|\\w+)@(\\w(\\w|-|\\.)*\\w|\\w+)\\.[a-zA-Z]+");
        if (!isValid) {
            return false;
        }
        //TrashMailFacade _facade = new TrashMailFacade();
        String domain = addres.substring(addres.indexOf("@") + 1);
        //return _facade.exists(domain);
        return true;
    }
}
