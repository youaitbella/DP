/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.specificfunction;

import org.inek.dataportal.feature.calculationhospital.*;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.entities.specificfunction.SpecificFunctionAgreement;
import org.inek.dataportal.entities.specificfunction.SpecificFunctionRequest;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.SpecificFunctionFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.utils.DocumentationUtil;

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
        destroyFeatureBeans();
        return Pages.SpecificFunctionEditAgreement.URL();
    }

    private void destroyFeatureBeans() {
        // if the user hit the browser's back-button, a request might be still active.
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope(EditStatementOfParticipance.class.getSimpleName());
        // todo: add other classes
    }


    public String print(SpecificFunctionRequest request) {
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
        destroyFeatureBeans();
        return Pages.SpecificFunctionEditAgreement.URL();
    }

}
