package org.inek.documentScanner.config;

import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class DocumentScannerConfig {

    public Boolean _scanEnabled = true;
    public Boolean _removeOldDocumentsEnabled = true;

    public Boolean isScanEnabled() {
        return _scanEnabled;
    }

    public void setScanEnabled(Boolean scanEnabled) {
        this._scanEnabled = scanEnabled;
    }

    public Boolean isRemoveOldDocumentsEnabled() {
        return _removeOldDocumentsEnabled;
    }

    public void setRemoveOldDocumentsEnabled(Boolean removeOldDocumentsEnabled) {
        this._removeOldDocumentsEnabled = removeOldDocumentsEnabled;
    }
}
