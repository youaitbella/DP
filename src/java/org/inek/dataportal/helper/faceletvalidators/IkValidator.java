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
@FacesValidator(value="IkValidator")
public class IkValidator implements Validator{
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null){return;}
        if (!isValidIK("" + value)){
            String msg = Utils.getMessage("errIK");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public static boolean isValidIK(String ikString) {
        Integer ik;
        try {
            ik = new Integer(ikString);
        } catch (NumberFormatException e) {
            return false;
        }
        if (ik < 100000000 || ik > 999999999){
            return false;
        }
        if (ikString.startsWith("2222222") || ikString.startsWith("70")){
            // special numbers for testing: his manufactorers / training calc
            return true;
        }
        int checkSum = 0;
        int coreIk = (ik / 10) % 1000000;
        while (coreIk > 0){
            checkSum += coreIk % 10;
            coreIk = coreIk / 10;
            int digit = 2 * (coreIk % 10);
            checkSum += digit - (digit < 10 ? 0 : 9);
            coreIk = coreIk / 10;
        }
        checkSum = checkSum % 10;
        return (ik % 10) == checkSum;
    }
    
}
