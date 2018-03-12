package org.inek.dataportal.feature.requestsystem.backingbean;

import org.inek.dataportal.feature.requestsystem.controller.RequestSystemController;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.enums.RequestCategory;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.feature.requestsystem.entity.Request;
import org.inek.dataportal.feature.requestsystem.entity.RequestDocument;
import org.inek.dataportal.feature.requestsystem.facade.RequestFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped
public class EditRequest extends AbstractEditController {
    // <editor-fold defaultstate="collapsed" desc="fields">
    private static final Logger LOGGER = Logger.getLogger("EditRequest");
    @Inject
    private SessionController _sessionController;
    @Inject
    private RequestFacade _requestFacade;
    private Request _request;

    enum RequestTabs {

        tabReqAddress,
        tabReqProblem,
        tabReqSolutions,
        tabReqBackground,
        tabReqRelevance,
        tabReqDocuments;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Request getRequest() {
        return _request;
    }

    // </editor-fold>
    @PostConstruct
    private void init() {

        //_logger.log(Level.WARNING, "Init EditRequest");
        Object reqId = Utils.getFlash().get("reqId");
        if (reqId == null) {
            _request = newRequest();
        } else {
            _request = loadRequest(reqId);
        }
    }

    @PreDestroy
    private void destroy() {
        //_logger.log(Level.WARNING, "Destroy EditRequest");
    }

    private Request loadRequest(Object ppId) {
        try {
            int id = Integer.parseInt("" + ppId);
            Request request = _requestFacade.find(id);
            if (_sessionController.isMyAccount(request.getAccountId())) {
                return request;
            }
        } catch (NumberFormatException ex) {
            LOGGER.info(ex.getMessage());
        }
        return newRequest();
    }

    private Request newRequest() {
        Account account = _sessionController.getAccount();
        Request request = new Request();
        request.setAccountId(account.getId());
        request.setInstitute(account.getCompany());
        request.setGender(account.getGender());
        request.setTitle(account.getTitle());
        request.setFirstName(account.getFirstName());
        request.setLastName(account.getLastName());
        request.setStreet(account.getStreet());
        request.setPostalCode(account.getPostalCode());
        request.setTown(account.getTown());
        String phone = account.getPhone();
        if (Utils.isNullOrEmpty(phone)) {
            phone = account.getCustomerPhone();
        }
        request.setPhone(phone);
        request.setFax(account.getCustomerFax());
        request.setEmail(account.getEmail());
        return request;
    }

    @Override
    protected void addTopics() {
        addTopic(RequestTabs.tabReqAddress.name(), Pages.RequestEditAddress.URL());
        addTopic(RequestTabs.tabReqProblem.name(), Pages.RequestEditProblem.URL());
        addTopic(RequestTabs.tabReqSolutions.name(), Pages.RequestEditSolutions.URL());
        addTopic(RequestTabs.tabReqBackground.name(), Pages.RequestEditBackground.URL());
        addTopic(RequestTabs.tabReqRelevance.name(), Pages.RequestEditRelevance.URL());
        addTopic(RequestTabs.tabReqDocuments.name(), Pages.RequestEditDocuments.URL());
    }

    private RequestSystemController getRequestController() {
        return (RequestSystemController) _sessionController.getFeatureController(Feature.REQUEST_SYSTEM);
    }

    // <editor-fold defaultstate="collapsed" desc="Tab Background">
    private List<SelectItem> _categoryItems;

    public List<SelectItem> getRequestCategories() {
        if (_categoryItems == null) {
            _categoryItems = new ArrayList<>();
            //_categoryItems.add(new SelectItem(null, Utils.getMessage("lblChooseEntry")));
            for (RequestCategory cat : RequestCategory.values()) {
                if (cat != RequestCategory.UNKNOWN) {
                    SelectItem item = new SelectItem(cat, Utils.getMessage("RequestCategory." + cat.name()));
                    _categoryItems.add(item);
                }
            }
        }
        return _categoryItems;
    }

    public boolean isCategoryOther() {
        return getRequest().getCategory() == RequestCategory.OTHER;
    }

    /**
     * need this facade cause JSF tells "no readable property reasonPointOfIssue
     * for request" even though it exists... BUG?
     *
     * @return
     */
    public boolean isReasonPointOfIssue() {
        return getRequest().isReasonPointOfIssue() == null ? false : getRequest().isReasonPointOfIssue();
    }

    public void setReasonPointOfIssue(boolean value) {
        getRequest().setReasonPointOfIssue(value);
    }

    public boolean isReasonCurrentSuit() {
        return getRequest().isReasonCurrentSuit() == null ? false : getRequest().isReasonCurrentSuit();
    }

    public void setReasonCurrentSuit(boolean value) {
        getRequest().setReasonCurrentSuit(value);
    }

    public boolean isReasonHeavyEncodingCase() {
        return getRequest().isReasonHeavyEncodingCase() == null ? false : getRequest().isReasonHeavyEncodingCase();
    }

    public void setReasonHeavyEncodingCase(boolean value) {
        getRequest().setReasonHeavyEncodingCase(value);
    }

    public boolean isReasonHeavyEncodingIssue() {
        return getRequest().isReasonHeavyEncodingIssue() == null ? false : getRequest().isReasonHeavyEncodingIssue();
    }

    public void setReasonHeavyEncodingIssue(boolean value) {
        getRequest().setReasonHeavyEncodingIssue(value);
    }

    private Boolean _reasonOther = null;

    public boolean isReasonOther() {
        if (_reasonOther == null && getRequest() != null) {
            _reasonOther = !Utils.isNullOrEmpty(getRequest().getReasonOther());
        }
        return _reasonOther == null ? false : _reasonOther;
    }

    public void setReasonOther(boolean value) {
        _reasonOther = value;
        int i = 1;
    }

    public boolean isReasonNoActiveSuit() {
        return getRequest().isReasonNoActiveSuit() == null ? false : getRequest().isReasonNoActiveSuit();
    }

    public void setReasonNoActiveSuit(boolean value) {
        getRequest().setReasonNoActiveSuit(value);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Tab Documents">
    public boolean isAnonymousData() {
        return getRequest().isAnonymousData() == null ? false : getRequest().isAnonymousData();
    }

    public void setAnonymousData(boolean value) {
        getRequest().setAnonymousData(value);
    }

    // </editor-fold>
    public String save() {
        _request = _requestFacade.saveRequest(_request);

        if (isValidId(_request.getRequestId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSaveAndMentionSend").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    /**
     * requests sealing of a formal request if the form is completely
     * fullfilled, this function displays a confirmation dialog confirming with
     * "ok" performs a call to seal
     *
     * @return
     */
    public String requestSeal() {
        if (!requestIsComplete()) {
            return null;
        }
        _script = "if (confirm ('" + Utils.getMessage("msgConfirmSeal") + "')) {document.getElementById('form:seal').click();}";
        return null;
    }

    /**
     * this function seals a request usually it can only be called is the
     * request to seal is confirmed. As a precaution, it performs some checks
     * wich have been done in requestSeal.
     *
     * @return
     */
    public String seal() {
        if (!requestIsComplete()) {
            return null;
        }
        _request.setComplete(true);
        _request = _requestFacade.saveRequest(_request);

        if (isValidId(_request.getRequestId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameREQUEST_SYSTEM"));
            Utils.getFlash().put("targetPage", Pages.PeppProposalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_request));
            return Pages.PrintView.URL();
        }
        return null;
    }

    public String delete() {
        // todo: implement
        return Pages.MainApp.URL();
    }

    public String takeDocuments() {
        RequestSystemController reqController = (RequestSystemController) _sessionController.getFeatureController(Feature.REQUEST_SYSTEM);
        for (RequestDocument doc : reqController.getDocuments()) {
            RequestDocument existingDoc = findByName(doc.getName());
            if (existingDoc != null) {
                getRequest().getDocuments().remove(existingDoc);
            }
            getRequest().getDocuments().add(doc);
        }
        reqController.getDocuments().clear();
        return null;
    }

    public String deleteDocument(String name) {
        RequestDocument existingDoc = findByName(name);
        if (existingDoc != null) {
            getRequest().getDocuments().remove(existingDoc);
        }
        return null;
    }

    private RequestDocument findByName(String name) {
        for (RequestDocument request : getRequest().getDocuments()) {
            if (request.getName().equals(name)) {
                return request;
            }
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="CheckElements">
    private String _msg = "";
    private String _script = "";
    private String _elementId = "";

    private boolean requestIsComplete() {
        _msg = "";
        String newTopic = "";
        Request request = getRequest();
        newTopic = checkField(newTopic, request.getName(), "lblAppellation", "form:name", RequestTabs.tabReqAddress);
        newTopic = checkField(newTopic, request.getInstitute(), "lblRequestingInstitute", "form:institute", RequestTabs.tabReqAddress);
        newTopic = checkField(newTopic, request.getGender(), 1, 2, "lblSalutation", "form:cbxGender", RequestTabs.tabReqAddress);
        newTopic = checkField(newTopic, request.getFirstName(), "lblFirstName", "form:firstname", RequestTabs.tabReqAddress);
        newTopic = checkField(newTopic, request.getLastName(), "lblLastName", "form:lastname", RequestTabs.tabReqAddress);
        newTopic = checkField(newTopic, request.getStreet(), "lblStreet", "form:street", RequestTabs.tabReqAddress);
        newTopic = checkField(newTopic, request.getPostalCode(), "lblPostalCode", "form:zip", RequestTabs.tabReqAddress);
        newTopic = checkField(newTopic, request.getTown(), "lblTown", "form:town", RequestTabs.tabReqAddress);
        newTopic = checkField(newTopic, request.getPhone(), "lblPhone", "form:phone", RequestTabs.tabReqAddress);
        newTopic = checkField(newTopic, request.getEmail(), "lblMail", "form:email", RequestTabs.tabReqAddress);

        newTopic = checkField(newTopic, request.getDescription(), "lblProblemDescription", "form:problem", RequestTabs.tabReqProblem);

        newTopic = checkField(newTopic, request.getSolution(), "lblSuggestedSolution", "form:solution", RequestTabs.tabReqSolutions);
        newTopic = checkField(newTopic, request.getAlternativeSolution(), "lblAlternativeSolution", 
                "form:alternativeSolution", RequestTabs.tabReqSolutions);

        newTopic = checkField(newTopic, "" + request.getCategory(), "headCategory", "form:category", RequestTabs.tabReqBackground);
        if (request.getCategory() == RequestCategory.OTHER) {
            newTopic = checkField(newTopic, request.getCategoryOther(), "headCategory", "form:categoryOther", RequestTabs.tabReqBackground);
        }
        // todo: check checkboxes

        newTopic = checkField(newTopic, request.getRelevanceCurrent(), 1, null, "lblCurrentYear", 
                "form:relevanceCurrent", RequestTabs.tabReqRelevance);
        newTopic = checkField(newTopic, request.getRelevancePast(), 1, null, "lblPastYear", 
                "form:relevancePast", RequestTabs.tabReqRelevance);
        newTopic = checkField(newTopic, request.getRelevanceHospitals(), 1, null, "lblRelevanceHospitalCount", ""
                + "form:relevanceHospitals", RequestTabs.tabReqRelevance);
        newTopic = checkField(newTopic, request.getRelevanceReason(), "lblRelevanceReason", 
                "form:relevanceReason", RequestTabs.tabReqRelevance);

        if (request.getDocuments().size() > 0 || request.getDocumentsOffline().length() > 0) {
            newTopic = checkField(newTopic, request.isAnonymousData() ? "true" : "", "lblAnonymousData", 
                    "form:anonymousData", RequestTabs.tabReqDocuments);
        }
        if (!_msg.isEmpty()) {
            _msg = Utils.getMessage("infoMissingFields") + "\\r\\n" + _msg;
            setActiveTopic(newTopic);
            _script = "alert ('" + _msg + "');";
            if (!_elementId.isEmpty()) {
                _script += "\r\n document.getElementById('" + _elementId + "').focus();";
            }

        }
        return _msg.isEmpty();
    }

    private String checkField(String newTopic, String value, String msgKey, String elementId, RequestTabs tab) {
        if (Utils.isNullOrEmpty(value)) {
            _msg += "\\r\\n" + Utils.getMessage(msgKey);
            if (newTopic.isEmpty()) {
                newTopic = tab.name();
                _elementId = elementId;
            }
        }
        return newTopic;
    }

    private String checkField(String newTopic, Integer value, Integer minValue, Integer maxValue, String msgKey, String elementId, RequestTabs tab) {
        if (value == null
                || minValue != null && value.intValue() < minValue.intValue()
                || maxValue != null && value.intValue() > maxValue.intValue()) {
            _msg += "\\r\\n" + Utils.getMessage(msgKey);
            if (newTopic.isEmpty()) {
                newTopic = tab.name();
                _elementId = elementId;
            }
        }
        return newTopic;
    }

    public String getScript() {
        if (_script.isEmpty()) {
            return "";
        }
        String script = _script;
        _script = "";
        return script;
    }
    // </editor-fold>

}
