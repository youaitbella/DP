package org.inek.documentScanner.backingBean;

import org.inek.documentScanner.config.DocumentScannerConfig;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;

@ManagedBean(name = "documentScannerBean")
@SessionScoped
public class DocumentScannerBean implements Serializable {

    @Inject
    private DocumentScannerConfig _documentScannerConfig;

    public Boolean getDocumentScanStatus() {
        return _documentScannerConfig.isScanEnabled();
    }

    public Boolean getRemovingOldDocumentsStatus() {
        return _documentScannerConfig.isRemoveOldDocumentsEnabled();
    }

    public Boolean getRemovingOldWaitingDocumentsStatus() {
        return _documentScannerConfig.isRemoveOldWaitingDocumentsEnabled();
    }

    public void switchDocumentScanner() {
        _documentScannerConfig.setScanEnabled(!_documentScannerConfig.isScanEnabled());
    }

    public void switchRemovingOldDocuments() {
        _documentScannerConfig.setRemoveOldDocumentsEnabled(!_documentScannerConfig.isRemoveOldDocumentsEnabled());
    }

    public void switchRemovingOldWaitingDocuments() {
        _documentScannerConfig.setRemoveOldWaitingDocumentsEnabled(!_documentScannerConfig.isRemoveOldWaitingDocumentsEnabled());
    }
}
