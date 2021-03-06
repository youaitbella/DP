/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.insurance.specificfunction.backingbean;

import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.insurance.specificfunction.entity.SpecificFunctionAgreement;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.insurance.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class SpecificFunctionAgreementList {

    // <editor-fold defaultstate="collapsed" desc="fields">
    private static final Logger LOGGER = Logger.getLogger("SpecificFunctionAgreementList");

    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private ApplicationTools _appTools;
    // </editor-fold>

    public boolean isNewAllowed() {
        return _appTools.isEnabled(ConfigKey.IsSpecificFunctionAgreementCreateEnabled);
    }

    public String newRequest() {
        return Pages.InsuranceSpecificFunctionEditAgreement.URL();
    }

    public String print(SpecificFunctionAgreement request) {
        Utils.getFlash().put("headLine", Utils.getMessage("nameSPECIFIC_FUNCTION"));
        Utils.getFlash().put("targetPage", Pages.SpecificFunctionSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(request));
        return Pages.PrintView.URL();
    }

    public void delete(SpecificFunctionAgreement agreement) {
        if (agreement == null) {
            // might be deleted by somebody else
            return;
        }
        if (agreement.getStatus().getId() >= WorkflowStatus.Provided.getId()) {
            agreement.setStatus(WorkflowStatus.Retired);
            _specificFunctionFacade.saveSpecificFunctionAgreement(agreement);
        } else {
            _specificFunctionFacade.deleteSpecificFunctionAgreement(agreement);
        }
    }

    public String edit() {
        return Pages.InsuranceSpecificFunctionEditAgreement.URL();
    }

}
