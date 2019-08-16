package org.inek.dataportal.psy.nub.helper;

import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.common.helper.Utils;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class PsyNubProposalHelper implements Serializable {

    @Inject
    private CustomerFacade _customerFacade;

    public String checkProxyIKs(String value) {
        String[] iks = value.split("\\s|,|\r|\n");
        StringBuilder invalidIKs = new StringBuilder();
        for (String ik : iks) {
            if (ik.isEmpty()) {
                continue;
            }
            if (!_customerFacade.isValidIK(ik)) {
                if (invalidIKs.length() > 0) {
                    invalidIKs.append(", ");
                }
                invalidIKs.append(ik);
            }
        }
        if (invalidIKs.length() > 0) {
            if (invalidIKs.indexOf(",") < 0) {
                invalidIKs.insert(0, "Ungültige IK: ");
            } else {
                invalidIKs.insert(0, "Ungültige IKs: ");
            }
        }
        return invalidIKs.toString();
    }

    public boolean isValidePostalCode(String value) {
        try {
            Integer tmp = Integer.parseInt(value);
            if (tmp > 99999 || tmp < 0) {
                return false;
            }
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
