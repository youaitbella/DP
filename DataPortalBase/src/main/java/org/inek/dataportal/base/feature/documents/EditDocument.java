package org.inek.dataportal.base.feature.documents;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.base.facades.account.DocumentFacade;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.common.data.account.iface.Document;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.Helper;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@RequestScoped
public class EditDocument extends AbstractEditController {

    private static final Logger LOGGER = Logger.getLogger("EditDocument");

    @Inject
    private DocumentFacade _documentFacade;
    @Inject
    private ConfigFacade _configFacade;
    @Inject
    private SessionController _sessionController;

    public String downloadDocument(int accountDocumentId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        AccountDocument doc = _documentFacade.findAccountDocument(accountDocumentId);
        if (doc == null) {
            LOGGER.log(Level.WARNING, "Document not found: {0}", accountDocumentId);
            return "";
        }
        if (_sessionController.getAccountId() != doc.getAccountId() && !_sessionController.isInekUser(Feature.DOCUMENTS)) {
            return "";
        }

        Document document;
        if (doc.getDocumentId() == 0) {
            document = doc;
        } else {
            document = _documentFacade.findCommonDocument(doc.getDocumentId());
        }

        try {
            byte[] buffer = document.getContent();
            externalContext.setResponseHeader("Content-Type", Helper.getContentType(document.getName()));
            externalContext.setResponseHeader("Content-Length", "" + buffer.length);
            externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + document.getName() + "\"");
            ByteArrayInputStream is = new ByteArrayInputStream(buffer);
            new StreamHelper().copyStream(is, externalContext.getResponseOutputStream());

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return Pages.Error.URL();
        }
        facesContext.responseComplete();
        if (_configFacade.readConfigBool(ConfigKey.DocumentSetRead)) {
            if (_sessionController.getAccountId() == doc.getAccountId()) {
                doc.setRead(true);
            }
            _documentFacade.saveAccountDocument(doc);
        }

        return "";
    }

    public String deleteDocument(int docId) {
        AccountDocument doc = _documentFacade.findAccountDocument(docId);

        if (doc != null && _sessionController.getAccountId() == doc.getAccountId()) {
            _documentFacade.remove(doc);
        }
        return "";
    }

    public String
    getConfirmMessage(String name, String dateString) {
        String msg
                = name
                + " vom " + dateString
                + "\n"
                + Utils
                .getMessage("msgConfirmDelete")
                .replace("\r\n", "\n")
                .replace("\n", "\\r\\n")
                .replace("'", "\\'")
                .replace("\"", "\\'");

        return "return confirm ('" + msg
                + "');";

    }

    @Override
    protected void addTopics() {
    }
}
