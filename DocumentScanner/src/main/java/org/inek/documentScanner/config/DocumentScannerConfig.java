package org.inek.documentScanner.config;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class DocumentScannerConfig {

    private Boolean _scanEnabled = false;

    public Boolean isUpdateEnabled() {
        return _updateEnabled;
    }

    public void setUpdateEnabled(Boolean updateEnabled) {
        this._updateEnabled = updateEnabled;
    }

    private Boolean _updateEnabled = false;
    private Boolean _removeOldDocumentsEnabled = false;
    private Boolean _removeOldWaitingDocumentsEnabled = false;

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

    public Boolean isRemoveOldWaitingDocumentsEnabled() {
        return _removeOldWaitingDocumentsEnabled;
    }

    public void setRemoveOldWaitingDocumentsEnabled(Boolean removeOldWaitingDocumentsEnabled) {
        this._removeOldWaitingDocumentsEnabled = removeOldWaitingDocumentsEnabled;
    }
}
