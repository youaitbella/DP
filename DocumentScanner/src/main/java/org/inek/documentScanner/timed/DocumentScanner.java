package org.inek.documentScanner.timed;

import org.inek.documentScanner.config.DocumentScannerConfig;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author vohldo
 */
@Stateless
public class DocumentScanner {

    private static final Logger LOGGER = Logger.getLogger("DocumentScanner");

    @Inject
    private DocumentLoader _documentLoader;

    @Inject
    private DocumentScannerConfig _documentScannerConfig;

    @Schedule(hour = "*", minute = "*/1", info = "every 1 minute")
    private void scanDocumentRoot() {
        if (_documentScannerConfig.isScanEnabled()) {
            LOGGER.log(Level.INFO, "Start scanning docs");
            _documentLoader.scanDocumentRoot();
        }
        else {
            LOGGER.log(Level.INFO, "Scanning is disabled by config");
        }
    }
}
