package org.inek.dataportal.drg.additionalcost;

import org.inek.dataportal.drg.additionalcost.entity.AdditionalCost;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.helper.FeatureMessageHandler;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.drg.additionalcost.facade.AdditionalCostFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScopedContextHolder;
import org.inek.dataportal.common.utils.DocumentationUtil;

@Named
@RequestScoped
public class AdditionalCostList {

    private AdditionalCostFacade _addFacade;
    private AccessManager _accessManager;

    public AdditionalCostList() {
    }

    @Inject
    public AdditionalCostList(AdditionalCostFacade addFacade,
            AccessManager accessManager) {
        _addFacade = addFacade;
        _accessManager = accessManager;
    }

    public List<AdditionalCost> getOpenAdditionalCosts() {
        return retrieveAddidtionalCosts(DataSet.AllOpen);
    }

    public List<AdditionalCost> getProvidedAdditionalCosts() {
        return retrieveAddidtionalCosts(DataSet.AllSealed);
    }

    private List<AdditionalCost> retrieveAddidtionalCosts(DataSet dataset) {
        Set<Integer> allowedManagedIks = _accessManager.retrieveAllowedManagedIks(Feature.ADDITIONAL_COST);
        Set<Integer> deniedManagedIks = _accessManager.retrieveDeniedManagedIks(Feature.ADDITIONAL_COST);
        int accountId = _accessManager.getSessionAccount().getId();
        return _addFacade.getAdditionalCosts(accountId, allowedManagedIks, deniedManagedIks, dataset);
    }

    public String editAdditionalCost() {
        // if the user hit the browser's back-button, a request might be still active. 
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("AdditionalCost");
        return Pages.AdditionalCostEdit.URL();
    }

    public String getConfirmMessage(int adId) {
        AdditionalCost add = _addFacade.findAdditionalCost(adId);
        String msg = "Meldung für " + add.getIk() + "\n"
                + (add.getStatusId() <= 9 ? Utils.getMessage("msgConfirmDelete") : Utils.getMessage("msgConfirmRetire"));
        msg = msg.replace("\r\n", "\n").replace("\n", "\\r\\n").replace("'", "\\'").replace("\"", "\\'");
        return "return confirm ('" + msg + "');";
    }

    public String deleteAdditionalCost(int adId) {
        AdditionalCost additionalCost = _addFacade.findAdditionalCost(adId);
        if (additionalCost == null) {
            return "";
        }
        Boolean deleteEnabled = _accessManager.isDeleteEnabled(
                Feature.ADDITIONAL_COST,
                additionalCost.getAccountId(),
                additionalCost.getIk()
        );
        if (deleteEnabled) {
            if (additionalCost.getStatusId() < WorkflowStatus.Provided.getId()) {
                _addFacade.deleteAdditionalCost(additionalCost);
            } else {
                additionalCost.setStatusId(WorkflowStatus.Retired.getId());
                _addFacade.saveAdditionalCost(additionalCost);
            }
        } else {
            DialogController.showAccessDeniedDialog();
        }
        return "";
    }

    public String printNotice(int adId) {
        Utils.getFlash().put("headLine", FeatureMessageHandler.getMessage("nameADDITIONAL_COST"));
        Utils.getFlash().put("targetPage", Pages.AdditionalCostSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_addFacade.findAdditionalCost(adId)));
        return Pages.PrintView.URL();
    }

    public boolean getCreateAllowed() {
        return _accessManager.isCreateAllowed(Feature.ADDITIONAL_COST);
    }

}
