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
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private PsyNubRequest _psyNubRequestBaseline;
    private Boolean _readOnly;

    private String _errorMessageTitle = "";
    private String _errorMessages = "";

    private static String ERROR_MESSAGE_TITLE_ERRORS_FOUND = "Bitte geben Sie die folgenden Angaben an.";
    private static String ERROR_MESSAGE_TITLE_MERGE_CONFLICT = "Dieser Datensatz wurde zwischenzeitlich von einem anderen Benutzer geändert. " +
            "Die Daten wurden neu geladen. Nachfolgend finden Sie eine Liste der Felder, welche von dem anderen Benutzer geändert wurde. Die " +
            "Eingabe des anderen Benutzers wurden in das Formular geladen. Bitte prüfen Sie die Eingabe und nehmen Sie ggf. Korrekturen vor. " +
            "Felder die von dem anderen Benutzer und Ihnen  geändert wurden, sind mit einem '###' gekennzeichnet";

    public String getErrorMessageTitle() {
        return _errorMessageTitle;
    }

    public String getErrorMessages() {
        return _errorMessages;
    }

    public List<SelectItem> getAllowedIks() {
        Set<Integer> iks = _accessManager.obtainIksForCreation(Feature.NUB_PSY);
        if (iks.size() == 0) {
            return new ArrayList<>();
        }
        List<SelectItem> items = iks.stream().map(i -> new SelectItem(i, "" + i)).collect(Collectors.toList());
        items.add(new SelectItem(0, ""));
        return items;
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
            _psyNubRequestBaseline = _psyNubFacade.findNubById(Integer.parseInt(id));
            if (!userHasAccess(_psyNubRequest)) {
                DialogController.showAccessDeniedDialog();
                Utils.navigate(Pages.NubPsySummary.RedirectURL());
            }
        }
        setReadOnly();
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
        if (_psyNubRequest.getIk() <= 0 && _psyNubRequest.getStatus() == WorkflowStatus.New) {
            _readOnly = false;
            return;
        }

        _readOnly = _accessManager.isReadOnly(Feature.NUB_PSY,
                _psyNubRequest.getStatus(),
                _psyNubRequest.getCreatedByAccountId(),
                _psyNubRequest.getIk());
    }

    public Boolean isSaveAllowed() {
        return _config.readConfigBool(ConfigKey.IsPsyNubCreateEnabled) && !_readOnly;
    }

    public Boolean isCreateTemplateAllowed() {
        return !_psyNubRequest.getName().isEmpty();
    }

    public Boolean isSendAllowed() {
        return _config.readConfigBool(ConfigKey.IsPsyNubSendEnabled) &&
                _accessManager.isSealedEnabled(Feature.NUB_PSY,
                        _psyNubRequest.getStatus(),
                        _psyNubRequest.getCreatedByAccountId(),
                        _psyNubRequest.getIk());
    }

    public Boolean isCreateNewNubFromNubAllowed() {
        return _config.readConfigBool(ConfigKey.IsPsyNubCreateEnabled) &&
                _accessManager.isCreateAllowed(Feature.NUB_PSY);
    }

    private PsyNubRequest createNewNubRequest() {
        PsyNubRequest psyNubRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(_sessionController.getAccount());
        Set<Integer> iks = _accessManager.obtainIksForCreation(Feature.NUB_PSY);
        if (iks.size() == 1) {
            psyNubRequest.setIk(iks.stream().findFirst().get());
        }
        return psyNubRequest;
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
        } catch (EJBException ex) {
            boolean hasNoMergeErrors = handleOptimisticLockException();
            if (hasNoMergeErrors) {
                _psyNubRequest.setVersion(_psyNubFacade.findNubById(_psyNubRequest.getId()).getVersion());
                save();
            } else {
                _psyNubRequest.setStatus(WorkflowStatus.New);
                _psyNubRequest.setSealedAt(Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC)));
                _psyNubRequest.setVersion(_psyNubFacade.findNubById(_psyNubRequest.getId()).getVersion());
                _errorMessageTitle = ERROR_MESSAGE_TITLE_MERGE_CONFLICT;
                DialogController.openDialogByName("errorMessageDialog");
            }

        } catch (Exception ex) {
            throw ex;
        }
        return false;
    }

    private Boolean handleOptimisticLockException() {
        PsyNubRequest partnerNub = _psyNubFacade.findNubById(_psyNubRequest.getId());
        PsyNubRequestMergeHelper mergeHelper = new PsyNubRequestMergeHelper(_psyNubRequestBaseline, _psyNubRequest, partnerNub);
        List<String> conflicts = mergeHelper.compareProposals();

        if (conflicts.isEmpty()) {
            return true;
        } else {
            _errorMessages = String.join("\n", conflicts);
            return false;
        }

    }

    public void send() {
        if (isReadyForSend()) {
            preparePsyNubRequestForSend();
            if (save()) {
                DialogController.showSendDialog();
                _psyNubRequestHelper.sendPsyNubConformationMail(_psyNubRequest, _sessionController.getAccount());
            }
        } else {
            _errorMessageTitle = ERROR_MESSAGE_TITLE_ERRORS_FOUND;
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

    public void createNewNubFromNub() {
        PsyNubRequest newRequest = NewPsyNubRequestHelper.createNewPsyNubRequestFromPsyNubRequest(_psyNubRequest, _sessionController.getAccount());
        _psyNubFacade.save(newRequest);
        DialogController.showSuccessDialog("Neue NUB erfolgreich angelegt", "Die NUB wurde erfolgreich kopiert. " +
                "Sie finden diese in der Übersichtsliste");
    }
}
