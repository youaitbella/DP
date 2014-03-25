package org.inek.dataportal.feature.modelintention;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.ModelIntention;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.ModelIntentionStatus;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.ModelIntentionFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.helper.structures.Triple;

@Named
@RequestScoped
public class ModelIntentionList {

    @Inject ModelIntentionFacade _modelIntentionFacade;
    @Inject SessionController _sessionController;
    
    public List<Triple> getModelIntentions() {
        return _modelIntentionFacade.getModelIntentionInfos(_sessionController.getAccountId(), DataSet.OPEN);
    }

    public List<Triple> getSealedModelIntentions() {
        return _modelIntentionFacade.getModelIntentionInfos(_sessionController.getAccountId(), DataSet.SEALED);
    }

    public String newModelIntention() {
        return Pages.ModelIntentionTypeAndNumPat.URL();
    }

    public String editModelIntention(int modelId) {
        Utils.getFlash().put("modelId", modelId);
        return Pages.ModelIntentionTypeAndNumPat.URL();
    }

    public String requestDeleteModelIntention(int modelId) {
        Utils.getFlash().put("modelId", modelId);
        ModelIntention intention = _modelIntentionFacade.find(modelId);
        if (_sessionController.isMyAccount(intention.getAccountId())) {
            String msg = intention.getStatus().intValue() <= 9 ? Utils.getMessage("msgConfirmDelete") : Utils.getMessage("msgConfirmRetire");
            String script = "if (confirm ('MI" + intention.getMiId().toString().replaceAll("(\\r|\\n)", "") + "\\r\\n" + msg + "')) {document.getElementById('deleteModelIntention').click();}";
            _sessionController.setScript(script);
        }
        return "";
    }

    public String deleteModelIntention(int modelId) {
        ModelIntention intention = _modelIntentionFacade.find(modelId);
        if (intention == null) { 
            return "";
        }
        if (_sessionController.isMyAccount(intention.getAccountId())) {
            if (intention.getStatus().intValue() <= 9) {
                _modelIntentionFacade.remove(intention);
            } else {
                intention.setStatus(ModelIntentionStatus.Retired.getValue());
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
