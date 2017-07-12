package org.inek.dataportal.feature.valuationratio;

import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.valuationratio.ValuationRatio;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.ValuationRatioFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.utils.DocumentationUtil;

@Named
@RequestScoped
public class ValuationRatioList {

    private static final Logger LOGGER = Logger.getLogger("ValuationRatioListLogger");

    @Inject private ValuationRatioFacade _valuationRatioFacade;
    @Inject private SessionController _sessionController;

    public List<ValuationRatio> getOpenRelations() {
        return _valuationRatioFacade.getValuationRatios(_sessionController.getAccountId(), DataSet.AllOpen);
    }

    public List<ValuationRatio> getProvidedRelations() {
        return _valuationRatioFacade.getValuationRatios(_sessionController.getAccountId(), DataSet.AllSealed);
    }

    public String editValuationRatio() {
        // if the user hit the browser's back-button, a request might be still active. 
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("ValuationRatio");
        return Pages.ValuationRatioEdit.URL();
    }

    public String getConfirmMessage(int valuationRatioId) {
        ValuationRatio vr = _valuationRatioFacade.findFreshValuationRatio(valuationRatioId);
        String msg = "Meldung für " + vr.getIk() + "\n"
                + (vr.getStatus() <= 9 ? Utils.getMessage("msgConfirmDelete") : Utils.getMessage("msgConfirmRetire"));
        msg = msg.replace("\r\n", "\n").replace("\n", "\\r\\n").replace("'", "\\'").replace("\"", "\\'");
        return "return confirm ('" + msg + "');";
    }
    
    public String deleteValuationRatio(int vrId) {
        ValuationRatio valuationratio = _valuationRatioFacade.findFreshValuationRatio(vrId);
        if (valuationratio == null) {
            return "";
        }
        if (_sessionController.isMyAccount(valuationratio.getAccountId())) {
            if (valuationratio.getStatus() < WorkflowStatus.Provided.getId()) {
                _valuationRatioFacade.delete(valuationratio);
            } else {
                valuationratio.setStatus(WorkflowStatus.Retired.getId());
                _valuationRatioFacade.saveValuationRatio(valuationratio);
            }
        }
        return "";
    }
    
    public String printValuationRatio(int valuationRatioId) {
        Utils.getFlash().put("headLine", Utils.getMessage("nameValuationRatio"));
        Utils.getFlash().put("targetPage", Pages.ValuationRatioSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_valuationRatioFacade.findValuationRatio(valuationRatioId)));
        return Pages.PrintView.URL();
    }

}
