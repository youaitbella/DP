package org.inek.documentScanner.backingBean;

import org.inek.documentScanner.config.DocumentScannerConfig;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@ManagedBean(name = "documentScannerBean")
@SessionScoped
public class DocumentScannerBean implements Serializable {

    @Inject
    private DocumentScannerConfig _documentScannerConfig;

    public Boolean getDocumentScanStatus() {
        return _documentScannerConfig.isScanEnabled();
    }

    public void switchDocumentScanner() {
        _documentScannerConfig.setScanEnabled(!_documentScannerConfig.isScanEnabled());
    }
}
