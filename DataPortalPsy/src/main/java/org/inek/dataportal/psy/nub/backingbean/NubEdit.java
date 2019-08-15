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
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.util.Date;

/**
 * @author lautenti
 */
@Named
@FeatureScoped
public class NubEdit {

    @Inject
    private PsyNubProposal _psyNubProposal;
    @Inject
    private PsyNubFacade _psyNubFacade;
    @Inject
    private SessionController _sessionController;
    @Inject
    private AccessManager _accessManager;

    private Boolean _readOnly;

    public Boolean getReadOnly() {
        return _readOnly;
    }

    public void setReadOnly(Boolean readOnly) {
        this._readOnly = readOnly;
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
        setReadOnly();
    }

    private boolean userHasAccess(int nubId) {
        // todo: accessmanager
        return true;
    }

    public void setReadOnly() {
        // todo: accessmanager und configkey
        setReadOnly(false);
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

    public Boolean save() {
        preparePsyNubProposalForSave();
        _psyNubFacade.save(_psyNubProposal);
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

    public void handleDocumentUpload(FileUploadEvent file) {
        PsyNubProposalDocument doc = new PsyNubProposalDocument();
        doc.setName(file.getFile().getFileName());
        doc.setContent(file.getFile().getContents());
        _psyNubProposal.addDocument(doc);
    }

    public void removeDocument(PsyNubProposalDocument doc) {
        _psyNubProposal.removeDocument(doc);
    }

    public StreamedContent downloadDocument(PsyDocument doc) {
        ByteArrayInputStream stream = new ByteArrayInputStream(doc.getContent());
        return new DefaultStreamedContent(stream, "applikation/" + doc.getContentTyp(), doc.getName());
    }
}
