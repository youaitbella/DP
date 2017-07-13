/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.backingbean;

import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.feature.specificfunction.entity.SpecificFunctionRequest;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class PsychStafftList {

    // <editor-fold defaultstate="collapsed" desc="fields">
    private static final Logger LOGGER = Logger.getLogger(PsychStafftList.class.getName());

// todo    @Inject private Facade Facade;
    @Inject private ApplicationTools _appTools;
    // </editor-fold>

    public boolean isNewAllowed() {
        return _appTools.isEnabled(ConfigKey.IsPsychStaffCreateEnabled);
    }

    public String print(SpecificFunctionRequest request) {
        Utils.getFlash().put("headLine", Utils.getMessage("namePSYCH_STAFF"));
        Utils.getFlash().put("targetPage", Pages.PsychStaffSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(request));
        return Pages.PrintView.URL();
    }

    public void delete(Object data) {// todo: replace object and code
        if (data == null) {
            // might be deleted by somebody else
            return;
        }
//        if (data.getStatus().getId() >= WorkflowStatus.Provided.getId()) {
//            data.setStatus(WorkflowStatus.Retired);
//            Facade.saveS(data);
//        } else {
//            Facade.delete(date);
//        }
    }

    public String edit() {
        return Pages.PsychStaffEdit.URL();
    }

}
