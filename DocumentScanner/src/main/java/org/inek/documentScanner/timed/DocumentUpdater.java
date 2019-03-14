package org.inek.documentScanner.timed;

import org.inek.dataportal.common.data.account.entities.AccountDocument;
import org.inek.dataportal.common.data.common.CommonDocument;
import org.inek.documentScanner.business.DocumentLoader;
import org.inek.documentScanner.config.DocumentScannerConfig;
import org.inek.documentScanner.facade.DocumentScannerFacade;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class DocumentUpdater {

    private static final Logger LOGGER = Logger.getLogger("DocumentUpdater");

    @Inject
    private DocumentLoader _documentLoader;

    @Inject
    private DocumentScannerConfig _documentScannerConfig;
    @Inject
    private DocumentScannerFacade _docFacade;

    @Asynchronous
    private void update() {
        try {
            Optional<AccountDocument> optionalDocument = _docFacade.findAccountDocumentWithContent();
            if (!optionalDocument.isPresent()) {
                return;
            }
            AccountDocument accountDocument = optionalDocument.get();
            CommonDocument commonDocument = createCommonDocument(accountDocument);
            accountDocument.setDocumentId(commonDocument.getId());
            accountDocument.setName("");
            accountDocument.setContent(new byte[0]);
            _docFacade.saveAccountDocument(accountDocument);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error during document update: " + ex.getMessage());
        }

    }

    private CommonDocument createCommonDocument(AccountDocument accountDocument) {
        CommonDocument doc = new CommonDocument(accountDocument.getName());
        doc.setContent(accountDocument.getContent());
        doc.setDomain(accountDocument.getDomain());
        doc.setAccountId(accountDocument.getAgentAccountId());
        _docFacade.saveCommonDocument(doc);
        LOGGER.log(Level.INFO, "CommonDocument created: {0}", doc.getName());
        return doc;
    }

}
