/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.admin;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.Agency;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountDocument;
import org.inek.dataportal.enums.DocumentTarget;
import org.inek.dataportal.facades.AgencyFacade;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.utils.StreamUtils;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "AdminTask")
public class AdminUploadDoc {

    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;

    // <editor-fold defaultstate="collapsed" desc="Property DocumentTarget">
    private DocumentTarget _documentTarget = DocumentTarget.Account;

    public DocumentTarget getDocumentTarget() {
        return _documentTarget;
    }

    public void setDocumentTarget(DocumentTarget documentTarget) {
        _documentTarget = documentTarget;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Account">
    private Account _account;

    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
        _account = account;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Agency">
    private Agency _agency;

    public Agency getAgency() {
        return _agency;
    }

    public void setAgency(Agency agency) {
        _agency = agency;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Agencies">
    @Inject AgencyFacade _agencyFacade;

    public List<Agency> getAgencies() {
        return _agencyFacade.findAll();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Property File">
    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }
    // </editor-fold>

    public String getEmail() {
        return _account == null ? "" : _account.getEmail();
    }

    public void setEmail(String email) {
        _account = _accountFacade.findByMailOrUser(email);
    }

    public void setAccountId(int accountId) {
        _account = _accountFacade.find(accountId);
    }

    public int getAccountId() {
        return _account == null ? 0 : _account.getId();
    }

    public void checkEmail(FacesContext context, UIComponent component, Object value) {
        String email = (String) value;
        Account account = _accountFacade.findByMailOrUser(email);
        if (account == null) {
            String msg = Utils.getMessage("errUnknownEmail");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void checkAccountId(FacesContext context, UIComponent component, Object value) {
        Account account = _accountFacade.find((int) value);
        if (account == null) {
            String msg = Utils.getMessage("errUnknownAccount");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void uploadDoc() {
    }

    public String saveDocument() {
        try {
            if (_file != null) {
                byte[] document = StreamUtils.stream2blob(_file.getInputStream());
                String name = _file.getName();
                if (_documentTarget == DocumentTarget.Account){
                storeDocument(name, document, _account.getId());
                }else{
                    //todo: determine all account ids and store
                }
                
            }
        } catch (IOException | NoSuchElementException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Upload failed!"));
        }
        return "";
    }

    @Inject AccountDocumentFacade _docFacade;
    private void storeDocument(String name, byte[] document, int accountId){
        AccountDocument accountDocument = new AccountDocument();
        accountDocument.setAccountId(accountId);
        accountDocument.setContent(document);
        accountDocument.setName(name);
        _docFacade.persist(accountDocument);
    }
}
