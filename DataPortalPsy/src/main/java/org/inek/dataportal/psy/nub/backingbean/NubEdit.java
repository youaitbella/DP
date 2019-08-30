/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.nub.backingbean;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.psy.nub.entities.PsyNubRequest;
import org.inek.dataportal.psy.nub.entities.PsyNubRequestDocument;
import org.inek.dataportal.psy.nub.facade.PsyNubFacade;
import org.inek.dataportal.psy.nub.helper.*;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lautenti
 */
@Named
@FeatureScoped
public class NubEdit {

    @Inject
    private PsyNubFacade _psyNubFacade;
    @Inject
    private SessionController _sessionController;
    @Inject
    private PsyNubRequestHelper _psyNubRequestHelper;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private ConfigFacade _config;

    private PsyNubRequest _psyNubRequest;
    private List<Integer> _allowedIks;
    private Boolean _readOnly;

    private String _errorMessages = "";

    public String getErrorMessages() {
        return _errorMessages;
    }

    public List<Integer> getAllowedIks() {
        return _allowedIks;
    }

    public Boolean getReadOnly() {
        return _readOnly;
    }

    public PsyNubRequest getPsyNubRequest() {
        return _psyNubRequest;
    }

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            return;
        } else if ("new".equals(id)) {
            _psyNubRequest = createNewNubRequest();
        } else {
            _psyNubRequest = _psyNubFacade.findNubById(Integer.parseInt(id));
            if (!userHasAccess(_psyNubRequest)) {
                DialogController.showAccessDeniedDialog();
                Utils.navigate(Pages.NubPsySummary.RedirectURL());
            }
        }
        setAllowedIksList();
        setReadOnly();
    }

    private void setAllowedIksList() {
        _allowedIks = new ArrayList<>();
        for (Integer ik : _sessionController.getAccount().getFullIkSet()) {
            if (_accessManager.ikIsManaged(ik, Feature.NUB_PSY)) {
                if (_accessManager.userHasReadAccess(Feature.NUB_PSY, ik)) {
                    _allowedIks.add(ik);
                }
            } else {
                _allowedIks.add(ik);
            }
        }
    }

    private boolean userHasAccess(PsyNubRequest request) {
        if (request.getIk() > 0 && _accessManager.ikIsManaged(request.getIk(), Feature.NUB_PSY)) {
            if (_accessManager.userHasReadAccess(Feature.NUB_PSY, request.getIk())) {
                return true;
            } else {
                return false;
            }
        } else {
            return request.getCreatedByAccountId() == _sessionController.getAccountId();
        }
    }

    public void setReadOnly() {
        if (_psyNubRequest.getStatus().getId() >= WorkflowStatus.Accepted.getId()) {
            _readOnly = true;
            return;
        }
        if (_psyNubRequest.getIk() > 0 && _accessManager.ikIsManaged(_psyNubRequest.getIk(), Feature.NUB_PSY)) {
            if (_accessManager.userHasReadAccess(Feature.NUB_PSY, _psyNubRequest.getIk())) {
                _readOnly = false;
            } else {
                _readOnly = true;
            }
        } else {
            _readOnly = !(_psyNubRequest.getCreatedByAccountId() == _sessionController.getAccountId());
        }
    }

    public Boolean isSaveAllowed() {
        return _config.readConfigBool(ConfigKey.IsPsyNubCreateEnabled) && !_readOnly;
    }

    public Boolean isCreateTemplateAllowed() {
        return !_psyNubRequest.getName().isEmpty();
    }

    public Boolean isSendAllowed() {
        return _config.readConfigBool(ConfigKey.IsPsyNubSendEnabled) && !_readOnly;
    }

    private PsyNubRequest createNewNubRequest() {
        return NewPsyNubRequestHelper.createNewPsyNubRequest(_sessionController.getAccount());
    }

    public void ikChanged() {
        _psyNubRequest.setIkName(_sessionController.getApplicationTools().retrieveHospitalName(_psyNubRequest.getIk()));
    }

    public Boolean save() {
        preparePsyNubRequestForSave();
        try {
            _psyNubRequest = _psyNubFacade.save(_psyNubRequest);
            if (_psyNubRequest.getStatus().equals(WorkflowStatus.New)) {
                DialogController.showSaveDialog();
            }
            return true;
        } catch (OptimisticLockException ex) {
            // TODO Merge Data
            //msg = mergeAndReportChanges();
        } catch (Exception ex) {
            throw ex;
        }
        return false;
    }

    public void send() {
        if (isReadyForSend()) {
            preparePsyNubRequestForSend();
            if (save()) {
                DialogController.showSendDialog();
                _psyNubRequestHelper.sendPsyNubConformationMail(_psyNubRequest, _sessionController.getAccount());
            }
        } else {
            DialogController.openDialogByName("errorMessageDialog");
        }
    }

    private boolean isReadyForSend() {
        List<String> errorMessages = PsyNubRequestChecker.checkPsyRequestForSend(_psyNubRequest);
        _errorMessages = String.join("\n", errorMessages);
        return errorMessages.isEmpty();
    }

    private void preparePsyNubRequestForSend() {
        _psyNubRequest.setStatus(WorkflowStatus.Accepted);
        _psyNubRequest.setSealedAt(new Date());
    }

    private void preparePsyNubRequestForSave() {
        _psyNubRequest.setLastModifiedAt(new Date());
        _psyNubRequest.setLastChangedByAccountId(_sessionController.getAccountId());
        formatProxyIks();
    }

    private void formatProxyIks() {
        _psyNubRequest.getProposalData().setProxyIks(_psyNubRequestHelper.formatProxyIks(_psyNubRequest.getProposalData().getProxyIks()));
    }

    public void handleDocumentUpload(FileUploadEvent file) {
        PsyNubRequestDocument doc = new PsyNubRequestDocument();
        doc.setName(file.getFile().getFileName());
        doc.setContent(file.getFile().getContents());
        _psyNubRequest.addDocument(doc);
    }

    public void removeDocument(PsyNubRequestDocument doc) {
        _psyNubRequest.removeDocument(doc);
    }

    public StreamedContent downloadDocument(PsyNubRequestDocument doc) {
        ByteArrayInputStream stream = new ByteArrayInputStream(doc.getContent());
        return new DefaultStreamedContent(stream, "applikation/" + doc.getContentTyp(), doc.getName());
    }

    public void checkProxyIKs(FacesContext context, UIComponent component, Object value) {
        String msg = _psyNubRequestHelper.checkProxyIKs(value.toString());
        if (!msg.isEmpty()) {
            FacesMessage fmsg = new FacesMessage(msg);
            throw new ValidatorException(fmsg);

        }
    }

    public void checkPostalCode(FacesContext context, UIComponent component, Object value) {
        if (!PsyNubRequestValueChecker.isValidPostalCode(value.toString())) {
            String msg = Utils.getMessage("errPostalCode");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void checkProcedureCodes(FacesContext context, UIComponent component, Object value) {
        String invalidCodes = _psyNubRequestHelper.checkProcedureCodes(value.toString(), _psyNubRequest.getTargetYear());
        if (invalidCodes.length() > 0) {
            FacesMessage msg = new FacesMessage(invalidCodes);
            throw new ValidatorException(msg);
        }
    }

    public void checkStringForValidDate(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (!PsyNubRequestValueChecker.isValidStringForDateValue(o.toString())) {
            String msg = "Das Datum hat ein ungültiges Format (MM/YY)";
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void checkPepps(FacesContext facesContext, UIComponent uiComponent, Object o) {
        String invalidPepps = PsyNubRequestValueChecker.checkPeppString(o.toString());
        if (invalidPepps.length() > 0) {
            FacesMessage msg = new FacesMessage(invalidPepps);
            throw new ValidatorException(msg);
        }
    }

    public void createTemplateFromNubAndDownload() {
        if (!_psyNubRequest.getName().isEmpty()) {
            String content = PsyNubRequestTemplateHelper.createTemplateContentFromPsyNubRequest(_psyNubRequest, _sessionController.getAccount());
            String name = PsyNubRequestTemplateHelper.createFileName(_psyNubRequest);
            Utils.downloadText(content, name, "UTF-8");
        } else {
            DialogController.showInfoDialog("Vorlage erstellen nicht möglich", "Bitte geben Sie der NUB einen Namen, " +
                    "damit Sie eine Vorlage erstellen können.");
        }
    }

    public void reloadAccountInformation() {
        NewPsyNubRequestHelper.fillAccountToPsyNub(_psyNubRequest, _sessionController.getAccount());
    }
}
