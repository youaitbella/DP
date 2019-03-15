package org.inek.documentScanner.timed;

import org.inek.documentScanner.config.DocumentScannerConfig;
import org.inek.documentScanner.facade.DocumentScannerFacade;

import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class RemoveOldDocuments {

    private static final Logger LOGGER = Logger.getLogger("RemoveOldDocuments");

    @Inject
    private DocumentScannerConfig _documentScannerConfig;

    @Inject
    private DocumentScannerFacade _docFacade;

    @Schedule(hour = "2", minute = "15", info = "once a day")
    //@Schedule(hour = "*", minute = "*/1", info = "every 1 minute")
    private void removeOldDocuments() {
        if (_documentScannerConfig.isRemoveOldDocumentsEnabled()) {
            LOGGER.log(Level.INFO, "Start removing old docs");
            deleteOldDocuments();
        } else {
            LOGGER.log(Level.INFO, "Removing old documents is disabled by config");
        }
    }

    @Asynchronous
    private void deleteOldDocuments() {
        try {
            int removedDocs = _docFacade.removeOldAccountDocuments();
            LOGGER.log(Level.INFO, "Removed AccountDocuments: " + removedDocs);
            removedDocs = _docFacade.removeOldCommonDocuments();
            LOGGER.log(Level.INFO, "Removed CommonDocuments: " + removedDocs);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error during removing old docs: " + ex.getMessage(), ex);
        }
    }
}
