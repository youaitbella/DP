package org.inek.dataportal.feature.certification;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.entities.certification.Grouper;
import org.inek.dataportal.entities.certification.GrouperAction;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.enums.CertStatus;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.facades.certification.GrouperActionFacade;
import org.inek.dataportal.facades.certification.GrouperFacade;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.common.utils.Helper;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class CertCertification implements Serializable{

    private static final Logger LOGGER = Logger.getLogger("CertCertification");

    @Inject private SessionController _sessionController;
    @Inject private SystemFacade _systemFacade;
    @Inject private GrouperFacade _grouperFacade;

    public List<SelectItem> getSystems4Account() {

        List<SelectItem> list = new ArrayList<>();
        List<RemunerationSystem> remunerationSystems = _systemFacade.getRemunerationSystems(_sessionController.getAccountId());
        for (RemunerationSystem system : remunerationSystems) {
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
        // session timeout extended to 1 hour (to provide enough time for an upload)
        FacesContext.getCurrentInstance().getExternalContext().setSessionMaxInactiveInterval(3600); 
        return _grouper.getSystemId();
    }

    public void setSystemId(int systemId) {
        if (systemId != _grouper.getId()) {
            if (systemId <= 0) {
                _grouper = new Grouper();
            } else {
                _grouper = _grouperFacade.findByAccountAndSystemId(_sessionController.getAccountId(), systemId);
                _file = "";
                //cleanupUploadFiles();
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

    private String _file = "";

    public String getFile() {
        return _file;
    }

    public void setFile(String file) {
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
            try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(file), StreamHelper.BUFFER_LENGHT)) {
                externalContext.setResponseHeader("Content-Type", Helper.getContentType(file.getName()));
                externalContext.setResponseHeader("Content-Length", "");
                externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + file.getName() + "\"");
                new StreamHelper().copyStream(is, externalContext.getResponseOutputStream());
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
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
        if (_file != null && !_file.isEmpty()) {
            prefix = _file + " geladen als ";
        }
        return prefix + file.getName().replace(".upload", " [ungespeichert]");
    }

    public String getUploadFileName(EditCert editCert) {
        Optional<File> uploadFolder = getUploadFolder(editCert);
        if (!uploadFolder.isPresent()) {
            return "";
        }
        String prefix = getExpectedFileName();
        String outFile = prefix + ".zip.upload";
        return new File(uploadFolder.get(), outFile).getAbsolutePath();
    }

    @Inject private GrouperActionFacade _actionFacade;

    public void logAction(String message) {
        GrouperAction action = new GrouperAction();
        action.setAccountId(_grouper.getAccountId());
        action.setSystemId(_grouper.getSystemId());
        action.setAction(message);
        _actionFacade.merge(action);
    }

    private Optional<File> getUploadFolder() {
        EditCert editCert = FeatureScopedContextHolder.Instance.getBean(EditCert.class);
        return getUploadFolder(editCert);
    }

    private Optional<File> getUploadFolder(EditCert editCert) {
        RemunerationSystem system = _systemFacade.findFresh(_grouper.getSystemId());
        if (system == null) {
            LOGGER.log(Level.WARNING, "upload, missing system with id {0}", _grouper.getSystemId());
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
                LOGGER.log(Level.WARNING, "Could not create marker file {0}", marker.getAbsolutePath());
            }
        }
    }

    @Inject private Mailer _mailer;

    public String saveFile() {
        setPersistUploadFile();
        RemunerationSystem system = _systemFacade.findFresh(_grouper.getSystemId());
        Account account = _sessionController.getAccount();
        String phone = account.getPhone();
        if (phone.trim().isEmpty()){
            phone = account.getCustomerPhone();
        }
        String msg = "Account: \t\t" + account.getId() + "\r\n"
                + "Firma: \t\t\t" + account.getCompany() + "\r\n"
                + "Ansprechpartner: \t" + account.getFirstName() + " " + account.getLastName() + "\r\n"
                + "Telefon: \t\t" + phone + "\r\n\r\n"
                + system.getDisplayName() + "\r\n"
                + _grouper.getCertStatus().getLabel() + "\r\n";
        _mailer.sendMailFrom("edv.zert@inek-drg.de", "edv.zert@inek-drg.de", "", "", "Upload Ergebnis " + system.getDisplayName(), msg);
        return saveOther();
    }

    public String saveOther() {
        save();
        setGrouperChanged(false);
        return "";
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
        _file = "";
        file.renameTo(target);
    }

    public void save() {
        try {
            Grouper savedGrouper = _grouperFacade.merge(_grouper);
            _grouper = savedGrouper;
        } catch (Exception ex) {
            if (!(ex.getCause() instanceof OptimisticLockException)) {
                throw ex;
            }
            mergeGrouper();
        }
    }

    private void mergeGrouper() {
        _sessionController.logMessage("ConcurrentUpdate CertCertification: grouper=" + _grouper.getId());
        Grouper currentGrouper = _grouperFacade.findFresh(_grouper.getId());
        currentGrouper.setName(_grouper.getName());
        currentGrouper.setEmailCopy(_grouper.getEmailCopy());
        currentGrouper.setDownloadTest(_grouper.getDownloadTest());
        currentGrouper.setDownloadSpec(_grouper.getDownloadSpec());
        currentGrouper.setDownloadCert(_grouper.getDownloadCert());
        currentGrouper.setCertUpload1(_grouper.getCertUpload1());
        currentGrouper.setCertUpload2(_grouper.getCertUpload2());
        currentGrouper.setTestUpload1(_grouper.getTestUpload1());
        currentGrouper.setTestUpload2(_grouper.getTestUpload2());
        currentGrouper.setTestUpload3(_grouper.getTestUpload3());
        _grouper = _grouperFacade.merge(currentGrouper);
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

    public String refresh() {
        return "";
    }

}
