/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.documents;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.Helper;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class EditDocument extends AbstractEditController {

    private static final Logger LOGGER = Logger.getLogger("EditDocument");

    @Inject private AccountDocumentFacade _accDocFacade;
    @Inject private AccountFacade _accFacade;
    @Inject private ConfigFacade _configFacade;
    @Inject private SessionController _sessionController;

    public String downloadDocument(int docId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        AccountDocument doc = _accDocFacade.find(docId);
        if (doc == null){
            LOGGER.log(Level.WARNING, "Document not found: {0}", docId);
            return "";
        }
        if (_sessionController.getAccountId() != doc.getAccountId() && !_sessionController.isInekUser(Feature.DOCUMENTS)) {
            return "";
        }
        try {
            byte[] buffer = doc.getContent();
            externalContext.setResponseHeader("Content-Type", Helper.getContentType(doc.getName()));
            externalContext.setResponseHeader("Content-Length", "" + buffer.length);
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + doc.getName() + "\"");
            ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            new StreamHelper().copyStream(is, externalContext.getResponseOutputStream());

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return Pages.Error.URL();
        }
        facesContext.responseComplete();
        if (_sessionController.getAccountId() == doc.getAccountId()) {
            doc.setRead(true);
        }
        _accDocFacade.merge(doc);
        return "";
    }

    public String deleteDocument(int docId) {
        AccountDocument doc = _accDocFacade.find(docId);
        if (doc != null && _sessionController.getAccountId() == doc.getAccountId()) {
            _accDocFacade.remove(doc);
        }
        return "";
    }

    public String getConfirmMessage(String name, String dateString) {
        String msg = name + " vom " + dateString + "\n"
                + Utils.getMessage("msgConfirmDelete");
        msg = msg.replace("\r\n", "\n").replace("\n", "\\r\\n").replace("'", "\\'").replace("\"", "\\'");
        return "return confirm ('" + msg + "');";
    }

    @Override
    protected void addTopics() {

    }
}
