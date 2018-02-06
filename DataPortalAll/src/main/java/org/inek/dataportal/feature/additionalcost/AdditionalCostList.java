package org.inek.dataportal.feature.additionalcost;

import java.util.List;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.additionalcost.AdditionalCost;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.facades.AdditionalCostFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.utils.DocumentationUtil;

@Named
@RequestScoped
public class AdditionalCostList {

    private static final Logger LOGGER = Logger.getLogger("AdditionalCostListLogger");

    @Inject private AdditionalCostFacade _addFacade;
    @Inject private SessionController _sessionController;

    public List<AdditionalCost> getOpenAdditionalCosts() {
        return _addFacade.getAdditionalCosts(_sessionController.getAccountId(), DataSet.AllOpen);
    }

    public List<AdditionalCost> getProvidedAdditionalCosts() {
        return _addFacade.getAdditionalCosts(_sessionController.getAccountId(), DataSet.AllSealed);
    }

    public String editAdditionalCost() {
        // if the user hit the browser's back-button, a request might be still active. 
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("AdditionalCost");
        return Pages.AdditionalCostEdit.URL();
    }

    public String getConfirmMessage(int adId) {
        AdditionalCost add = _addFacade.findAdditionalCost(adId);
        String msg = "Meldung für " + add.getIk()+ "\n"
                + (add.getStatusId() <= 9 ? Utils.getMessage("msgConfirmDelete") : Utils.getMessage("msgConfirmRetire"));
        msg = msg.replace("\r\n", "\n").replace("\n", "\\r\\n").replace("'", "\\'").replace("\"", "\\'");
        return "return confirm ('" + msg + "');";
    }
    public String deleteAdditionalCost(int adId) {
        AdditionalCost add = _addFacade.findAdditionalCost(adId);
        if (add == null) {
            return "";
        }
        if (_sessionController.isMyAccount(add.getAccountId())) {
            if (add.getStatusId() < WorkflowStatus.Provided.getId()) {
                _addFacade.deleteAdditionalCost(add);
            } else {
                add.setStatusId(WorkflowStatus.Retired.getId());
                _addFacade.saveAdditionalCost(add);
            }
        }
        return "";
    }
    
    public String printNotice(int adId) {
        Utils.getFlash().put("headLine", Utils.getMessage("nameADDITIONAL_COST"));
        Utils.getFlash().put("targetPage", Pages.InsuranceSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_addFacade.findAdditionalCost(adId)));
        return Pages.PrintView.URL();
    }

}
