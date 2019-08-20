/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.nub.backingbean;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.KhComparison.entities.PsyDocument;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.psy.nub.entities.PsyNubProposal;
import org.inek.dataportal.psy.nub.entities.PsyNubProposalDocument;
import org.inek.dataportal.psy.nub.facade.PsyNubFacade;
import org.inek.dataportal.psy.nub.helper.NewPsyNubProposalHelper;
import org.inek.dataportal.psy.nub.helper.PsyNubProposalHelper;
import org.inek.dataportal.psy.nub.helper.PsyNubProposalValueChecker;
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
    private PsyNubProposalHelper _psyNubProposalHelper;
    @Inject
    private AccessManager _accessManager;

    private PsyNubProposal _psyNubProposal;
    private List<Integer> _allowedIks;
    private Boolean _readOnly;

    public List<Integer> getAllowedIks() {
        return _allowedIks;
    }

    public Boolean getReadOnly() {
        return _readOnly;
    }

    public PsyNubProposal getPsyNubProposal() {
        return _psyNubProposal;
    }

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            return;
        } else if ("new".equals(id)) {
            _psyNubProposal = createNewNubProposal();
        } else {
            if (userHasAccess(Integer.parseInt(id))) {
                _psyNubProposal = _psyNubFacade.findNubById(Integer.parseInt(id));
            } else {
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

    private boolean userHasAccess(int nubId) {
        // todo: accessmanager
        return true;
    }

    public void setReadOnly() {
        // todo: accessmanager und configkey
        _readOnly = false;
    }

    public Boolean isChangeAllowed() {
        // todo: accessmanager und configkey
        return true;
    }

    public Boolean isSendAllowed() {
        // todo: accessmanager und configkey
        return true;
    }

    private PsyNubProposal createNewNubProposal() {
        return NewPsyNubProposalHelper.createNewPsyNubProposal(_sessionController.getAccount());
    }

    public void ikChanged() {
        _psyNubProposal.setIkName(_sessionController.getApplicationTools().retrieveHospitalName(_psyNubProposal.getIk()));
    }

    public Boolean save() {
        preparePsyNubProposalForSave();
        _psyNubProposal = _psyNubFacade.save(_psyNubProposal);
        DialogController.showSaveDialog();
        //todo save nub
        return true;
    }

    public String send() {
        //todo save nub
        return "";
    }

    private void preparePsyNubProposalForSave() {
        _psyNubProposal.setLastModifiedAt(new Date());
        _psyNubProposal.setLastChangedByAccountId(_sessionController.getAccountId());
    }

    private void formatProxyIks() {
        // TODO IKs formatieren
    }

    public void handleDocumentUpload(FileUploadEvent file) {
        PsyNubProposalDocument doc = new PsyNubProposalDocument();
        doc.setName(file.getFile().getFileName());
        doc.setContent(file.getFile().getContents());
        _psyNubProposal.addDocument(doc);
    }

    public void removeDocument(PsyNubProposalDocument doc) {
        _psyNubProposal.removeDocument(doc);
    }

    public StreamedContent downloadDocument(PsyNubProposalDocument doc) {
        ByteArrayInputStream stream = new ByteArrayInputStream(doc.getContent());
        return new DefaultStreamedContent(stream, "applikation/" + doc.getContentTyp(), doc.getName());
    }

    public void checkProxyIKs(FacesContext context, UIComponent component, Object value) {
        String msg = _psyNubProposalHelper.checkProxyIKs(value.toString());
        if (!msg.isEmpty()) {
            FacesMessage fmsg = new FacesMessage(msg);
            throw new ValidatorException(fmsg);

        }
    }

    public void checkPostalCode(FacesContext context, UIComponent component, Object value) {
        if (!PsyNubProposalValueChecker.isValidPostalCode(value.toString())) {
            String msg = Utils.getMessage("errPostalCode");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void checkProcedureCodes(FacesContext context, UIComponent component, Object value) {
        String invalidCodes = _psyNubProposalHelper.checkProcedureCodes(value.toString(), _psyNubProposal.getTargetYear());
        if (invalidCodes.length() > 0) {
            FacesMessage msg = new FacesMessage(invalidCodes);
            throw new ValidatorException(msg);
        }
    }

    public void checkStringForValidDate(FacesContext facesContext, UIComponent uiComponent, Object o) {
        if (!PsyNubProposalValueChecker.isValidStringForDateValue(o.toString())) {
            String msg = "Das Datum hat ein ungültiges Format (MM/YY)";
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void checkPepps(FacesContext facesContext, UIComponent uiComponent, Object o) {
        String invalidPepps = PsyNubProposalValueChecker.checkPeppString(o.toString());
        if (invalidPepps.length() > 0) {
            FacesMessage msg = new FacesMessage(invalidPepps);
            throw new ValidatorException(msg);
        }
    }
}
