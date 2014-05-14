package org.inek.dataportal.feature.modelintention;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.modelintention.ModelIntention;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.modelintention.ModelIntentionFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.EntityInfo;
import org.inek.dataportal.utils.DocumentationUtil;

@Named
@RequestScoped
public class ModelIntentionList {

    @Inject ModelIntentionFacade _modelIntentionFacade;
    @Inject SessionController _sessionController;
    
    public List<EntityInfo> getModelIntentions() {
        return _modelIntentionFacade.getModelIntentionInfos(_sessionController.getAccountId(), DataSet.All, false);
    }

    public String newModelIntention() {
        return Pages.ModelIntentionTypeAndNumPat.URL();
    }

    public String editModelIntention(int modelId) {
        Utils.getFlash().put("modelId", modelId);
        return Pages.ModelIntentionTypeAndNumPat.URL();
    }

    public String deleteModelIntention(int modelId) {
        ModelIntention intention = _modelIntentionFacade.find(modelId);
        if (intention == null) { 
            return "";
        }
        if (_sessionController.isMyAccount(intention.getAccountId())) {
            if (intention.getStatus().getValue() <= WorkflowStatus.Provided.getValue()) {
                _modelIntentionFacade.remove(intention);
            } else {
                intention.setStatus(WorkflowStatus.Retired.getValue());
                _modelIntentionFacade.saveModelIntention(intention);
            }
        }
        return "";
    }

    public String printModelIntention(int modelId) {
        Utils.getFlash().put("headLine", Utils.getMessage("nameMODEL_INTENTION"));
        Utils.getFlash().put("targetPage", Pages.ModelIntentionSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_modelIntentionFacade.find(modelId)));
        return Pages.PrintView.URL();
    }
}
