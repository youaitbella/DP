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
import javax.inject.Inject;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author muellermi
 */
@FacesValidator(value="IkValidator")
public class IkValidator implements Validator{
    @Inject private CustomerFacade _customerFacade;
    
    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null || value.toString().isEmpty()){return;} 
        if (!_customerFacade.isValidIK("" + value)){
            String msg = Utils.getMessage("errIK");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    
}
