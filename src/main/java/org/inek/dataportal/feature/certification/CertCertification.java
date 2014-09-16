package org.inek.dataportal.feature.certification;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.certification.Grouper;
import org.inek.dataportal.entities.certification.GrouperAction;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.enums.CertStatus;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.certification.GrouperActionFacade;
import org.inek.dataportal.facades.certification.GrouperFacade;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.helper.StreamHelper;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class CertCertification {

    private static final Logger _logger = Logger.getLogger("CertCertification");

    @Inject private SessionController _sessionController;
    @Inject private SystemFacade _systemFacade;
    @Inject private GrouperFacade _grouperFacade;

    @PostConstruct
    private void init() {
        _logger.log(Level.WARNING, "Init CertCertification");
    }

    @PreDestroy
    private void destroy() {
        _logger.log(Level.WARNING, "Destroy CertCertification");
    }

    public List<SelectItem> getSystems4Account() {

        List<SelectItem> list = new ArrayList<>();
        for (RemunerationSystem system : _sessionController.getAccount().getRemuneratiosSystems()) {
            if (system.isApproved()) {
                list.add(new SelectItem(system.getId(), system.getDisplayName()));
            }
        }
        SelectItem emptyItem = new SelectItem(-1, Utils.getMessage("itemChoose"));
        emptyItem.setNoSelectionOption(true);
        list.add(emptyItem);
        return list;
    }

    private Grouper _grouper = new Grouper();

    public Grouper getGrouper() {
        return _grouper;
    }

    public void setGrouper(Grouper grouper) {
        _grouper = grouper;
    }

    public int getSystemId() {
        return _grouper.getSystemId();
    }

    public void setSystemId(int systemId) {
        if (systemId != _grouper.getId()) {
            if (systemId <= 0) {
                _grouper = new Grouper();
            } else {
                _grouper = _grouperFacade.findByAccountAndSystemId(_sessionController.getAccountId(), systemId);
                _file = null;
                cleanupUploadFiles();
            }
            setGrouperChanged(false);
        }
    }

    private boolean _grouperChanged = false;

    public boolean isGrouperChanged() {
        return _grouperChanged;
    }

    public void setGrouperChanged(boolean isChanged) {
        _grouperChanged = isChanged;
    }

    public void grouperChangeListener(AjaxBehaviorEvent event) {
        setGrouperChanged(true);
    }

    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public String downloadSpec() {
        if (_grouper.getDownloadSpec() == null) {
            _grouper.setDownloadSpec(new Date());
            save();
        }
        logAction("Download Spec");

        return download("Spec", "Grouper-Spezifikation", "exe");
    }

    public String downloadTest() {
        if (_grouper.getDownloadTest() == null) {
            _grouper.setDownloadTest(new Date());
            save();
        }
        logAction("Download Test");
        return download("Daten", "Testdaten", "zip");
    }

    public String downloadCert() {
        if (_grouper.getDownloadCert() == null) {
            _grouper.setDownloadCert(new Date());
            save();
        }
        logAction("Download Zert");
        return download("Daten", "Zertdaten", "zip");
    }

    private String download(String folder, String fileNameBase, String extension) {
        if (_grouper.getSystemId() <= 0) {
            return "";
        }
        EditCert editCert = FeatureScopedContextHolder.Instance.getBean(EditCert.class);
        File file = editCert.getCertFile(_grouper.getSystemId(), folder, fileNameBase, extension, false);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        try {
            try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(file), StreamHelper.BufLen)) {
                externalContext.setResponseHeader("Content-Type", "text/plain");
                externalContext.setResponseHeader("Content-Length", "");
                externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + file.getName() + "\"");
                new StreamHelper().copyStream(is, externalContext.getResponseOutputStream());
            }
        } catch (IOException ex) {
            _logger.log(Level.SEVERE, null, ex);
            return Pages.Error.URL();
        }
        facesContext.responseComplete();
        return "";
    }

    public String getExpectedFileName() {
        switch (_grouper.getCertStatus()) {
            case PasswordRequested:
            case TestUpload1:
                return "TestDaten v1";
            case TestFailed1:
            case TestUpload2:
                return "TestDaten v2";
            case TestFailed2:
            case TestUpload3:
                return "TestDaten v3";
            case TestSucceed:
            case CertUpload1:
                return "ZertDaten v1";
            case CertFailed1:
            case CertUpload2:
                return "ZertDaten v2";
            default:
                return "";
        }
    }

    public String getResultFileName() {
        Optional<File> uploadFolder = getUploadFolder();
        if (!uploadFolder.isPresent()) {
            return "";
        }
        EditCert editCert = FeatureScopedContextHolder.Instance.getBean(EditCert.class);
        String fileNamePattern = getExpectedFileName() + ".zip(\\.upload)?";
        File file = editCert.getLastFile(uploadFolder.get(), fileNamePattern);
        String prefix = "";
        if (_file != null && !_file.getSubmittedFileName().isEmpty()) {
            prefix = _file.getSubmittedFileName() + " geladen als ";
        }
        return prefix + file.getName().replace(".upload", " [ungespeichert]");
    }

    public void uploadTestResult() {
        EditCert editCert = FeatureScopedContextHolder.Instance.getBean(EditCert.class);
        Optional<File> uploadFolder = getUploadFolder();
        if (!uploadFolder.isPresent()) {
            return;
        }
        String prefix = getExpectedFileName();
        String outFile = prefix + ".zip.upload";
        editCert.uploadFile(_file, new File(uploadFolder.get(), outFile));
        logAction("Upload " + _file.getSubmittedFileName() + " -> " + outFile);
    }

    @Inject GrouperActionFacade _actionFacade;
    private void logAction(String message) {
        GrouperAction action = new GrouperAction();
        action.setAccountId(_grouper.getAccountId());
        action.setSystemId(_grouper.getSystemId());
        action.setAction(message);
        _actionFacade.merge(action);
    }

    private Optional<File> getUploadFolder() {
        EditCert editCert = FeatureScopedContextHolder.Instance.getBean(EditCert.class);
        RemunerationSystem system = _systemFacade.find(_grouper.getSystemId());
        if (system == null) {
            _logger.log(Level.WARNING, "upload, missing system with id {0}", _grouper.getSystemId());
            return Optional.empty();
        }
        Optional<File> uploadFolderBase = editCert.getUploadFolder(system, "Ergebnis");
        if (!uploadFolderBase.isPresent()) {
            return Optional.empty();
        }
        File uploadFolder = new File(uploadFolderBase.get(), "" + _sessionController.getAccountId());
        createMarkerFile(uploadFolder);
        return Optional.of(uploadFolder);
    }

    private void createMarkerFile(File uploadFolder) {
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }
        File marker = new File(uploadFolder, _sessionController.getAccount().getCompany());
        if (!marker.exists()) {
            try {
                marker.createNewFile();
            } catch (IOException ex) {
                _logger.log(Level.WARNING, "Could not create marker file {0}", marker.getAbsolutePath());
            }
        }
    }

    @Inject Mailer _mailer;
    public void saveOther(ActionEvent event) {
        String id = event.getComponent().getClientId();
        if (id.equals("form:btnConfirmFile")) {
            setPersistUploadFile();
            _mailer.sendMail("edv.zert@inek-drg.de", "Upload Ergebnis", _sessionController.getAccount().getCompany());
        }
        save();
    }

    private void setPersistUploadFile() {
        Optional<File> uploadFolder = getUploadFolder();
        if (!uploadFolder.isPresent()) {
            return;
        }
        EditCert editCert = FeatureScopedContextHolder.Instance.getBean(EditCert.class);
        String fileName = getExpectedFileName() + ".zip.upload";
        File file = editCert.getLastFile(uploadFolder.get(), fileName);
        if (!file.getName().equals(fileName)) {
            return;
        }
        if (fileName.startsWith("TestDaten v1")) {
            _grouper.setTestUpload1(new Date());
            _grouper.setCertStatus(CertStatus.TestUpload1);
        }
        if (fileName.startsWith("TestDaten v2")) {
            _grouper.setTestUpload2(new Date());
            _grouper.setCertStatus(CertStatus.TestUpload2);
        }
        if (fileName.startsWith("TestDaten v3")) {
            _grouper.setTestUpload3(new Date());
            _grouper.setCertStatus(CertStatus.TestUpload3);
        }
        if (fileName.startsWith("ZertDaten v1")) {
            _grouper.setCertUpload1(new Date());
            _grouper.setCertStatus(CertStatus.CertUpload1);
        }
        if (fileName.startsWith("ZertDaten v2")) {
            _grouper.setCertUpload2(new Date());
            _grouper.setCertStatus(CertStatus.CertUpload2);
        }
        File target = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 7));
        if (target.exists()) {
            target.delete();
        }
        _file = null;
        file.renameTo(target);
    }

    public void save() {
        _grouper = _grouperFacade.merge(_grouper);
    }

    public String cancelSystem() {
        cleanupUploadFiles();
        setSystemId(_grouper.getSystemId());
        return Pages.CertCertification.RedirectURL();
    }

    private void cleanupUploadFiles() {
        deleteFiles(getUploadFolder().get(), ".*\\.upload");
    }

    public void deleteFiles(File dir, final String fileNamePattern) {
        if (!dir.exists()) {
            return;
        }
        for (File file : dir.listFiles((File file) -> file.isFile() && file.getName().matches(fileNamePattern))) {
            file.delete();
        }
    }

}
