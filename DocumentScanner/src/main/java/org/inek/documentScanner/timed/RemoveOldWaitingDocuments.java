package org.inek.documentScanner.timed;

import org.inek.documentScanner.config.DocumentScannerConfig;
import org.inek.documentScanner.facade.WaitingDocumentFacade;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vohldo
 */
@Stateless
public class RemoveOldWaitingDocuments {

    private static final Logger LOGGER = Logger.getLogger("RemoveOldWaitingDocuments");

    @Inject
    private DocumentScannerConfig _documentScannerConfig;

    @Inject
    private WaitingDocumentFacade _waitingDocumentFacade;


    @Schedule(hour = "2", minute = "15", info = "once a day")
    //@Schedule(hour = "*", minute = "*/1", info = "every 1 minute")
    private void removeOldDocuments() {
        if (_documentScannerConfig.isRemoveOldDocumentsEnabled()) {
            LOGGER.log(Level.INFO, "Start removing old waitingdocs");
            _waitingDocumentFacade.deleteOldDocuments();
        } else {
            LOGGER.log(Level.INFO, "Removing old waitingdocuments is disabled by config");
        }
    }
}
