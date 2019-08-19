package org.inek.dataportal.psy.nub.helper;

import org.inek.dataportal.common.data.access.ProcedureFacade;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

public class PsyNubProposalValueChecker implements Serializable {

    public static boolean isValidPostalCode(String value) {
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

    public static boolean isValidStringForDateValue(String value) {
        if (value.isEmpty()) {
            return true;
        }
        String regex = "0[1-9]\\/[0-9][0-9]|1[1-2]\\/[0-9][0-9]";
        return value.matches(regex);
    }
}
