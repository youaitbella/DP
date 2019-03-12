package org.inek.documentScanner.backingBean;

import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.mail.Mailer;
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
    @Inject
    private Mailer _mailer;
    @Inject
    private ConfigFacade _config;

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

    public Boolean getUpdateStatus() {
        return _documentScannerConfig.isUpdateEnabled();
    }

    public void switchUpdateOldDocuments() {
        _documentScannerConfig.setUpdateEnabled(!_documentScannerConfig.isUpdateEnabled());
    }

    public void sendTestErrorMail() {
        _mailer.sendMail("portaladmin@inek-drg.de", "Testmail Dokumentenscanner", "Testemail");
    }

    public String getBaseDir() {
        return _config.readConfig(ConfigKey.FolderRoot) + "/" + _config.readConfig(ConfigKey.FolderDocumentScanBase);

    }
}
