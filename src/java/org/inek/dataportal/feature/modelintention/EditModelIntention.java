package org.inek.dataportal.feature.modelintention;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.ModelIntention;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.ModelIntentionStatus;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.ModelIntentionFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author vohldo
 */
@Named
@ConversationScoped
public class EditModelIntention extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditModelIntention");

    @Inject private SessionController _sessionController;
    @Inject private ModelIntentionFacade _modelIntentionFacade;
    private boolean _ageYearEnabled;
    private String _conversationId;
    private ModelIntention _modelIntention;
    
    public boolean isAgeYearsEnabled() {
        return !_ageYearEnabled;
    }
    
    public SelectItem[] getGenders() {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem(Genders.Both.gender());
        items[1] = new SelectItem(Genders.Male.gender());
        items[2] = new SelectItem(Genders.Female.gender());
        return items;
    }
    
    public enum Genders {
        Both(0, "Beide"),
        Male(1, "Männlich"),
        Female(2, "Weiblich");
        
        private int _index;
        private String _gender;
        
        private Genders(int index, String gender) {
            _index = index;
            _gender = gender;
        }
        
        public int index() {
            return _index;
        }
        
        public String gender() {
            return _gender;
        }
    }

    @Override
    protected void addTopics() {
        addTopic(ModelIntentionTabs.tabModelIntTypeAndNumberOfPatients.name(), Pages.ModelIntentionTypeAndNumPat.URL());
        addTopic(ModelIntentionTabs.tabModelIntGoals.name(), Pages.ModelIntentionGoals.URL());
        addTopic(ModelIntentionTabs.tabModelIntTreatmentAreasAndCosts.name(), Pages.ModelIntentionTreatAreaAndCosts.URL());
        addTopic(ModelIntentionTabs.tabModelIntStructures.name(), Pages.ModelIntentionStructure.URL());
        addTopic(ModelIntentionTabs.tabModelIntQualityAndSupervision.name(), Pages.ModelIntentionQuality.URL());
    }

    public boolean isAgeYearEnabled() {
        return _ageYearEnabled;
    }

    public void setAgeYearEnabled(boolean ageYearEnabled) {
        this._ageYearEnabled = ageYearEnabled;
    }

    enum ModelIntentionTabs {

        tabModelIntTypeAndNumberOfPatients,
        tabModelIntGoals,
        tabModelIntTreatmentAreasAndCosts,
        tabModelIntStructures,
        tabModelIntQualityAndSupervision;
    }
    // <editor-fold defaultstate="collapsed" desc="fields">

    // </editor-fold>

    public EditModelIntention() {
        //System.out.println("EditModelIntention");
        _ageYearEnabled = false;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public ModelIntention getModelIntention() {
        return _modelIntention;
    }

    public String getUserMaintenancePage() {
        return Pages.UserMaintenance.URL();
    }

    // </editor-fold>
    @PostConstruct
    private void init() {
        _conversationId = (String) Utils.getFlash().get("conversationId");
        Object miId = Utils.getFlash().get("miId");
        if (miId == null) {
            _modelIntention = newModelIntention();
        } else {
            _modelIntention = loadModelIntention(miId);
        }
        if(_modelIntention.getAgeYearsFrom() != null || _modelIntention.getAgeYearsTo() != null)
            _ageYearEnabled = true;
        //ensureEmptyEntry(_peppProposal.getProcedures());
    }

    private ModelIntention loadModelIntention(Object ppId) {
        try {
            int id = Integer.parseInt("" + ppId);
            ModelIntention modelIntention = _modelIntentionFacade.find(id);
            return modelIntention;
        } catch (NumberFormatException ex) {
            _logger.info(ex.getMessage());
        }
        return newModelIntention();
    }

    private ModelIntention newModelIntention() {
        ModelIntention modelIntention = getModelIntentionController().createModelIntention();
        modelIntention.setAccountId(_sessionController.getAccount().getAccountId());
        return modelIntention;
    }

    private ModelIntentionController getModelIntentionController() {
        return (ModelIntentionController) _sessionController.getFeatureController(Feature.MODEL_INTENTION);
    }

    public String save() {
        if (!check4validSession()) {
            return Pages.InvalidConversation.URL();
        }
        
        _modelIntention = _modelIntentionFacade.saveModelIntention(_modelIntention);

        if (isValidId(_modelIntention.getMiId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isReadOnly() {
        return false;
    }
    public boolean isReadOnly(boolean laxCheck) {
        return false;
    }

    public boolean isRejectedModelIntention() {
        return ModelIntentionStatus.Rejected.getValue() == _modelIntention.getStatus().intValue();
    }
    
    /**
     * requests sealing of a formal request if the form is completely full
     * filled, this function displays a confirmation dialog confirming with "ok"
     * performs a call to seal
     *
     * @return
     */
    public String requestSeal() {
        if (!check4validSession()) {
            return Pages.InvalidConversation.URL();
        }
//TODO:        if (!requestIsComplete()) {
//            return getActiveTopic().getOutcome();
//        }
        String script = "if (confirm ('" + Utils.getMessage("msgConfirmSeal") + "')) {document.getElementById('form:seal').click();}";
        _sessionController.setScript(script);
        return null;
    }

    /**
     * this function seals a request usually it can only be called is the
     * request to seal is confirmed. As a precaution, it performs some checks
     * which have been done in requestSeal.
     *
     * @return
     */
    public String seal() {
        if (!check4validSession() /*TODO: || !requestIsComplete()*/) {
            return Pages.Error.URL();
        }
        if (_modelIntention.getStatus().intValue() >= 10){return Pages.Error.URL();}

        _modelIntention.setStatus(10 + _modelIntention.getStatus().intValue());
        _modelIntention = _modelIntentionFacade.saveModelIntention(_modelIntention);
        if (isValidId(_modelIntention.getMiId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameMODEL_INTENTION"));
            Utils.getFlash().put("targetPage", Pages.ModelIntentionSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_modelIntention));
            _sessionController.endConversation();
            return Pages.PrintView.URL();
        }
        return null;
    }

    public String requestApproval() {
        if (!check4validSession()) {
            return Pages.InvalidConversation.URL();
        }
//TODO:        if (!requestIsComplete()) {
//            return getActiveTopic().getOutcome();
//        }
        String script = "if (confirm ('" + Utils.getMessage("msgRequestApproval") + "')) {document.getElementById('form:confirmApprovalRequest').click();}";
        _sessionController.setScript(script);
        return null;
    }

    public String confirmApprovalRequest() {
        if (!check4validSession() /*TODO: || !requestIsComplete()*/) {
            return Pages.Error.URL();
        }
        if (_modelIntention.getStatus().intValue() >= 10){return Pages.Error.URL();}

        _modelIntention.setStatus(ModelIntentionStatus.ApprovalRequested.getValue());
        _modelIntention = _modelIntentionFacade.saveModelIntention(_modelIntention);
        return "";
    }
    
    /**
     * checks, whether the session is still valid
     *
     * @return
     */
    private boolean check4validSession() {
        return _conversationId.equals(_sessionController.getConversationId());
    }

    // <editor-fold defaultstate="collapsed" desc="CheckElements">

    // </editor-fold>
}
