/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.specificfunction;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountAdditionalIK;
import org.inek.dataportal.entities.specificfunction.CenterName;
import org.inek.dataportal.entities.specificfunction.RequestAgreedCenter;
import org.inek.dataportal.entities.specificfunction.RequestProjectedCenter;
import org.inek.dataportal.entities.specificfunction.SpecificFunction;
import org.inek.dataportal.entities.specificfunction.SpecificFunctionRequest;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.SpecificFunctionFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.MessageContainer;
import org.inek.dataportal.utils.DocumentationUtil;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class EditSpecificFunction extends AbstractEditController implements Serializable {

    // <editor-fold defaultstate="collapsed" desc="fields & enums">
    private static final Logger _logger = Logger.getLogger("EditSpecificFunctionRequest");

    @Inject private CooperationTools _cooperationTools;
    @Inject private SessionController _sessionController;
    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject ApplicationTools _appTools;

    private String _script;
    private SpecificFunctionRequest _request;

    public SpecificFunctionRequest getRequest() {
        return _request;
    }

    public void setRequest(SpecificFunctionRequest request) {
        this._request = request;
    }
    // </editor-fold>
    
    @PostConstruct
    private void init() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id = "" + params.get("id");
        if (id.equals("new")) {
            _request = newSpecificFunctionRequest();
            addCentersIfMissing();
        } else if (Utils.isInteger(id)) {
            SpecificFunctionRequest request = loadSpecificFunctionRequest(id);
            if (request.getId() == -1) {
                Utils.navigate(Pages.NotAllowed.RedirectURL());
                return;
            }
            _request = request;
            addCentersIfMissing();
        } else {
            Utils.navigate(Pages.Error.RedirectURL());
        }
    }

    private SpecificFunctionRequest loadSpecificFunctionRequest(String idObject) {
        int id = Integer.parseInt(idObject);
        SpecificFunctionRequest calcBasics = _specificFunctionFacade.findSpecificFunctionRequest(id);
        if (hasSufficientRights(calcBasics)) {
            return calcBasics;
        }
        return new SpecificFunctionRequest();
    }

    private boolean hasSufficientRights(SpecificFunctionRequest calcBasics) {
        if (_sessionController.isMyAccount(calcBasics.getAccountId(), false)) {
            return true;
        }
        return _cooperationTools.isAllowed(Feature.SPECIFIC_FUNCTION, calcBasics.getStatus(), calcBasics.getAccountId());
    }
    
    private SpecificFunctionRequest newSpecificFunctionRequest() {
        Account account = _sessionController.getAccount();
        SpecificFunctionRequest request = new SpecificFunctionRequest();
        request.setAccountId(account.getId());
        request.setGender(account.getGender());
        request.setTitle(account.getTitle());
        request.setFirstName(account.getFirstName());
        request.setLastName(account.getLastName());
        request.setPhone(account.getPhone());
        request.setMail(account.getEmail());
        request.setDataYear(Utils.getTargetYear(Feature.SPECIFIC_FUNCTION));
        return request;
    }

    public List<SelectItem> getTypeItems(){
       List<SelectItem> items = new ArrayList<>();
       items.add(new SelectItem(1, "im Krankenhausplan des Landes"));
       items.add(new SelectItem(2, "durch gleichartige Festlegung durch zuständige Landesbehörde"));
       return items;
   } 

    
    // <editor-fold defaultstate="collapsed" desc="actions">
    public boolean isOwnStatement() {
        return _sessionController.isMyAccount(_request.getAccountId(), false);
    }

    public boolean isReadOnly() {
        return _cooperationTools.isReadOnly(Feature.CALCULATION_HOSPITAL, _request.getStatus(), _request.getAccountId(), _request.getIk());
    }

    @Override
    protected void addTopics() {
        addTopic("lblFrontPage", Pages.CalcDrgBasics.URL());
    }

    public String save() {
        removeEmptyCenters();
        setModifiedInfo();
        _request = _specificFunctionFacade.saveSpecificFunctionRequest(_request);
        addCentersIfMissing();
        
        if (isValidId(_request.getId())) {
            // CR+LF or LF only will be replaced by "\r\n"
            String script = "alert ('" + Utils.getMessage("msgSave").replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
            _sessionController.setScript(script);
            return null;
        }
        return Pages.Error.URL();
    }

    private void setModifiedInfo() {
        _request.setLastChanged(Calendar.getInstance().getTime());
    }

    private boolean isValidId(Integer id) {
        return id != null && id >= 0;
    }

    public boolean isSealEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsDrgSendEnabled)) {
            return false;
        }
        return _cooperationTools.isSealedEnabled(Feature.CALCULATION_HOSPITAL, _request.getStatus(), _request.getAccountId());
    }

    public boolean isApprovalRequestEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsDrgSendEnabled)) {
            return false;
        }
        return _cooperationTools.isApprovalRequestEnabled(Feature.CALCULATION_HOSPITAL, _request.getStatus(), _request.getAccountId());
    }

    public boolean isRequestCorrectionEnabled() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsDrgSendEnabled)) {
            return false;
        }
        return _cooperationTools.isRequestCorrectionEnabled(Feature.CALCULATION_HOSPITAL, _request.getStatus(), _request.getAccountId());
    }

    public boolean isTakeEnabled() {
        return _cooperationTools.isTakeEnabled(Feature.CALCULATION_HOSPITAL, _request.getStatus(), _request.getAccountId());
    }

    /**
     * This function seals a statement od participance if possible. Sealing is
     * possible, if all mandatory fields are fulfilled. After sealing, the
     * statement od participance can not be edited anymore and is available for
     * the InEK.
     *
     * @return
     */
    public String seal() {
        if (!requestIsComplete()) {
            return getActiveTopic().getOutcome();
        }
        removeEmptyCenters();
        _request.setStatus(WorkflowStatus.Provided);
        setModifiedInfo();
        _request = _specificFunctionFacade.saveSpecificFunctionRequest(_request);

        if (isValidId(_request.getId())) {
            Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL") + " " + _request.getId());
            Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
            Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_request));
            return Pages.PrintView.URL();
        }
        return "";
    }

    private boolean requestIsComplete() {
        MessageContainer message = composeMissingFieldsMessage(_request);
        if (message.containsMessage()) {
            message.setMessage(Utils.getMessage("infoMissingFields") + "\\r\\n" + message.getMessage());
            //setActiveTopic(message.getTopic());
            String script = "alert ('" + message.getMessage() + "');";
            if (!message.getElementId().isEmpty()) {
                script += "\r\n document.getElementById('" + message.getElementId() + "').focus();";
            }
            _sessionController.setScript(script);
        }
        return !message.containsMessage();
    }

    public MessageContainer composeMissingFieldsMessage(SpecificFunctionRequest request) {
        MessageContainer message = new MessageContainer();

        String ik = request.getIk() < 0 ? "" : "" + request.getIk();
        checkField(message, ik, "lblIK", "specificFuntion:ikMulti");
        checkField(message, request.getFirstName(), "lblFirstName", "specificFuntion:firstName");
        checkField(message, request.getLastName(), "lblFirstName", "specificFuntion:lastName");
        checkField(message, request.getPhone(), "lblPhone", "specificFuntion:phone");
        checkField(message, request.getMail(), "lblMail", "specificFuntion:mail");

        for (RequestProjectedCenter center : request.getRequestProjectedCenters()) {
            checkField(message, center.getOtherCenterName(), "Bitte Art des Zentrums angeben", "");
            checkField(message, center.getOtherSpecificFunction(), "Bitte besondere Aufgaben angeben", "");
            checkField(message, center.getTypeId(), 1, 2, "Bitte Ausweisung und Festsetzung angeben", "");
            checkField(message, center.getEstimatedPatientCount(), 1, 99999999, "Bitte besondere Aufgaben angeben", "");
        }
        
        for (RequestAgreedCenter center : request.getRequestAgreedCenters()) {
            checkField(message, center.getCenter(), "Bitte Art des Zentrums angeben", "");
            checkField(message, center.getRemunerationKey(), "Bitte Entgeltschlüssel angeben", "");
            checkField(message, center.getAmount(), 1, 99999999, "Bitte Betrag angeben", "");
        }
                
        return message;
    }

    private void checkField(MessageContainer message, String value, String msgKey, String elementId) {
        if (Utils.isNullOrEmpty(value)) {
            applyMessageValues(message, msgKey, elementId);
        }
    }

    private void checkField(MessageContainer message, Integer value, Integer minValue, Integer maxValue, String msgKey, String elementId) {
        if (value == null
                || minValue != null && value < minValue
                || maxValue != null && value > maxValue) {
            applyMessageValues(message, msgKey, elementId);
        }
    }

    private void applyMessageValues(MessageContainer message, String msgKey, String elementId) {
        message.setMessage(message.getMessage() + "\\r\\n" + Utils.getMessageOrKey(msgKey));
        if (message.getTopic().isEmpty()) {
            message.setTopic("");
            message.setElementId(elementId);
        }
    }

    public String requestApproval() {
        if (!requestIsComplete()) {
            return null;
        }
        _request.setStatus(WorkflowStatus.ApprovalRequested);
        setModifiedInfo();
        _request = _specificFunctionFacade.saveSpecificFunctionRequest(_request);
        return "";
    }

    public String take() {
        if (!isTakeEnabled()) {
            return Pages.Error.URL();
        }
        _request.setAccountId(_sessionController.getAccountId());
        setModifiedInfo();
        _request = _specificFunctionFacade.saveSpecificFunctionRequest(_request);
        return "";
    }
    
    public void ikChanged() {
        // dummy listener, used by component MultiIk - do not delete
    }

    public List<SelectItem> getIks() {
        Set<Integer> iks = new HashSet<>();
        if (_request != null && _request.getIk() > 0) {
            iks.add(_request.getIk());
        }
        Account account = _sessionController.getAccount();
        if (account.getIK() > 0){
        iks.add(account.getIK());
        }
        for (AccountAdditionalIK additionalIK : account.getAdditionalIKs()) {
            iks.add(additionalIK.getIK());
        }
        List<SelectItem> items = new ArrayList<>();
        for (int ik : iks) {
            items.add(new SelectItem(ik));
        }
        return items;
    }
    
    public void addProjectedCenter(){
        RequestProjectedCenter center = new RequestProjectedCenter(_request.getId());
        _request.getRequestProjectedCenters().add(center);
    }
    
    public void deleteProjectedCenter(RequestProjectedCenter center){
        _request.getRequestProjectedCenters().remove(center);
    }
    
    public void addAgreedCenter(){
        RequestAgreedCenter center = new RequestAgreedCenter(_request.getId());
        _request.getRequestAgreedCenters().add(center);
    }
    
    public void deleteAgreedCenter(RequestAgreedCenter center){
        _request.getRequestAgreedCenters().remove(center);
    }
    // </editor-fold>

    private void removeEmptyCenters() {
        removeEmptyProjectedCenters();
        removeEmptyAgreedCenters();
    }

    private void removeEmptyProjectedCenters() {
        Iterator<RequestProjectedCenter> iter = _request.getRequestProjectedCenters().iterator();
        while (iter.hasNext()){
            RequestProjectedCenter center = iter.next();
            if (center.isEmpty()){
                iter.remove();
            }
        }
    }

    private void removeEmptyAgreedCenters() {
        Iterator<RequestAgreedCenter> iter = _request.getRequestAgreedCenters().iterator();
        while (iter.hasNext()){
            RequestAgreedCenter center = iter.next();
            if (center.isEmpty()){
                iter.remove();
            }
        }
    }

    private void addCentersIfMissing() {
        if (_request.getRequestProjectedCenters().isEmpty()){
            addProjectedCenter();
        }
        if (_request.getRequestAgreedCenters().isEmpty()){
            addAgreedCenter();
        }
    }
    
     public List<CenterName> getCenterNames(){
       return _specificFunctionFacade.getCenterNames();
   } 
    
     public List<SpecificFunction> getSpecificFunctions(){
       return _specificFunctionFacade.getSpecificFunctions();
   } 
    
}
