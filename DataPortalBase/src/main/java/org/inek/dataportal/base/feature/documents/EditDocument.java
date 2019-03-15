package org.inek.dataportal.base.feature.documents;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.base.facades.account.DocumentFacade;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.common.data.account.iface.Document;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.helper.Utils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
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
        AccountDocument doc = _documentFacade.findAccountDocument(accountDocumentId);
        if (doc == null) {
            LOGGER.log(Level.WARNING, "Document not found: {0}", accountDocumentId);
            return "";
        }
        if (_sessionController.getAccountId() != doc.getAccountId() && !_sessionController.isInekUser(Feature.DOCUMENTS)) {
            return "";
        }

        Document document = _documentFacade.findCommonDocument(doc.getDocumentId());

        String target = Utils.downloadDocument(document);
        if (_configFacade.readConfigBool(ConfigKey.DocumentSetRead)) {
            if (_sessionController.getAccountId() == doc.getAccountId()) {
                doc.setRead(true);
            }
            _documentFacade.saveAccountDocument(doc);
        }

        return target;
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
                = (name
                + " vom " + dateString
                + "\n"
                + Utils
                .getMessage("msgConfirmDelete"))
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
