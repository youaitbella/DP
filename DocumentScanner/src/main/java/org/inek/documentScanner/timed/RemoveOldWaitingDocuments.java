package org.inek.documentScanner.timed;

import org.inek.documentScanner.config.DocumentScannerConfig;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.inek.documentScanner.facade.DocumentScannerFacade;

/**
 * @author vohldo
 */
@Stateless
public class RemoveOldWaitingDocuments {

    private static final Logger LOGGER = Logger.getLogger("RemoveOldWaitingDocuments");

    @Inject
    private DocumentScannerConfig _documentScannerConfig;

    @Inject
    private DocumentScannerFacade _docFacade;


    @Schedule(hour = "2", minute = "15", info = "once a day")
    //@Schedule(hour = "*", minute = "*/1", info = "every 1 minute")
    private void removeOldDocuments() {
        if (_documentScannerConfig.isRemoveOldWaitingDocumentsEnabled()) {
            LOGGER.log(Level.INFO, "Start removing old waitingdocs");
            _docFacade.deleteOldWaitingDocuments();
        } else {
            LOGGER.log(Level.INFO, "Removing old waitingdocuments is disabled by config");
        }
    }
}
