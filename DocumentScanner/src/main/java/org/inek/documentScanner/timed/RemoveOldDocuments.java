package org.inek.documentScanner.timed;

import org.inek.documentScanner.config.DocumentScannerConfig;
import org.inek.documentScanner.facade.AccountDocumentFacade;

import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vohldo
 */
@Stateless
public class RemoveOldDocuments {

    private static final Logger LOGGER = Logger.getLogger("RemoveOldDocuments");

    @Inject
    private DocumentScannerConfig _documentScannerConfig;

    @Inject
    private AccountDocumentFacade _accountDocumentFacade;

    //@Schedule(hour = "2", minute = "15", info = "once a day")
    @Schedule(hour = "*", minute = "*/1", info = "every 1 minute")
    private void removeOldDocuments() {
        if (_documentScannerConfig.isRemoveOldDocumentsEnabled()) {
            LOGGER.log(Level.INFO, "Start removing old docs");
            deleteOldDocuments();
        }
        else {
            LOGGER.log(Level.INFO, "Removing old documents is disabled by config");
        }
    }

    @Asynchronous
    private void deleteOldDocuments() {
        try {
            int removedDocs = _accountDocumentFacade.removeOldDocuments();
            LOGGER.log(Level.INFO, "Removed docs: " + removedDocs);
        }
        catch(Exception ex) {
            LOGGER.log(Level.SEVERE, "Error during removing old docs: " + ex.getMessage(), ex);
        }
    }
}
