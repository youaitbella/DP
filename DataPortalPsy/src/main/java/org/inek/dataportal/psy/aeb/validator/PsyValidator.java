/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.aeb.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Named;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScoped;

/**
 *
 * @author lautenti
 */
@Named
@FeatureScoped
public class PsyValidator {

    public void isValidPepp(FacesContext context, UIComponent component, Object value) {
        String input = "" + value;
        if (input.length() != 5) {
            String msg = Utils.getMessage("Ungültige Pepp");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void isValidEt(FacesContext context, UIComponent component, Object value) {
        String input = "" + value;
        if (input.length() != 4) {
            String msg = Utils.getMessage("Ungültige ET");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

}
