package org.inek.dataportal.feature.specificfunction.backingbean;

import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.feature.specificfunction.entity.SpecificFunctionRequest;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.feature.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class SpecificFunctionRequestList {

    // <editor-fold defaultstate="collapsed" desc="fields">
    private static final Logger LOGGER = Logger.getLogger(SpecificFunctionRequestList.class.getName());

    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private ApplicationTools _appTools;
    // </editor-fold>

    public boolean isNewAllowed() {
        return _appTools.isEnabled(ConfigKey.IsSpecificFunctionRequestCreateEnabled);
    }

    public String newRequest() {
        return Pages.SpecificFunctionEditRequest.URL();
    }

    public String print(SpecificFunctionRequest request) {
        Utils.getFlash().put("headLine", Utils.getMessage("nameSPECIFIC_FUNCTION"));
        Utils.getFlash().put("targetPage", Pages.SpecificFunctionSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(request));
        return Pages.PrintView.URL();
    }

    public void delete(SpecificFunctionRequest request) {
        if (request == null) {
            // might be deleted by somebody else
            return;
        }
        if (request.getStatus().getId() >= WorkflowStatus.Provided.getId()) {
            request.setStatus(WorkflowStatus.Retired);
            _specificFunctionFacade.saveSpecificFunctionRequest(request);
        } else {
            _specificFunctionFacade.deleteSpecificFunctionRequest(request);
        }
    }

    public String edit() {
        return Pages.SpecificFunctionEditRequest.URL();
    }

}
