package org.inek.dataportal.cert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.util.IOUtils;
import org.inek.dataportal.api.helper.Const;
import org.inek.dataportal.cert.comparer.CertComparer;
import org.inek.dataportal.cert.comparer.CertFileHelper;
import org.inek.dataportal.cert.entities.Grouper;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.scope.FeatureScopedContextHolder;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.utils.Helper;
import org.inek.dataportal.cert.entities.GrouperAction;
import org.inek.dataportal.cert.entities.RemunerationSystem;
import org.inek.dataportal.cert.facade.GrouperActionFacade;
import org.inek.dataportal.cert.facade.GrouperFacade;
import org.inek.dataportal.cert.facade.SystemFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.RemunSystem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class CertCertification implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("CertCertification");

    @Inject
    private SessionController _sessionController;
    @Inject
    private SystemFacade _systemFacade;
    @Inject
    private GrouperFacade _grouperFacade;
    @Inject
    private ConfigFacade _configFacade;

    private int _compareResult;
    private UploadedFile _file;
    private RemunerationSystem _system;

    public RemunerationSystem getSystem() {
        return _system;
    }

    public void setSystem(RemunerationSystem system) {
        this._system = system;
    }

    public UploadedFile getFile() {
        return _file;
    }

    public void setFile(UploadedFile file) {
        this._file = file;
    }

    public List<RemunerationSystem> getSystems4Account() {
        List<RemunerationSystem> systems = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();

        List<RemunerationSystem> remunerationSystems = _systemFacade.getRemunerationSystems(_sessionController.getAccountId());
        for (RemunerationSystem system : remunerationSystems) {
            if (system.isApproved() && _grouperFacade.grouperHasApproveForSystem(_sessionController.getAccountId(),
                    system.getId(),
                    today)) {
                systems.add(system);
            }
        }
        return systems;
    }

    private Grouper _grouper = new Grouper();

    public Grouper getGrouper() {
        return _grouper;
    }

    public void setGrouper(Grouper grouper) {
        _grouper = grouper;
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
        if (_grouper.getSystem().getId() <= 0) {
            return "";
        }
        EditCert editCert = FeatureScopedContextHolder.Instance.getBean(EditCert.class);
        File file = editCert.getCertFile(_grouper.getSystem().getId(), folder, fileNameBase, extension, false);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        try {
            try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(file), Const.BUFFER_SIZE)) {
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

    public String getExpectedFileName(Grouper grouper) {
        switch (grouper.getCertStatus()) {
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
        if (_file != null) {
            return _file.getFileName();
        }
        return "";
    }

    @Inject
    private GrouperActionFacade _actionFacade;

    public void logAction(String message) {
        GrouperAction action = new GrouperAction();
        action.setAccountId(_grouper.getAccountId());
        action.setSystemId(_grouper.getSystem().getId());
        action.setAction(message);
        _actionFacade.merge(action);
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

    @Inject
    private Mailer _mailer;

    public String saveFile() {
        File folder = getUploadPath(_grouper);

        String filename = getExpectedFileName(_grouper);
        String extension = CertFileHelper.getExtension(_file.getFileName());

        try (OutputStream outStream = new FileOutputStream(new File(folder, filename + "." + extension))) {
            IOUtils.copy(_file.getInputstream(), outStream);
            _file.getInputstream().close();
        } catch (Exception ex) {
            //Todo Fehlermeldung anzeigen
        }

        setNewStatus(filename, _grouper);
        return "";
    }

    private Boolean sendEmailToInek() {
        RemunerationSystem system = _grouper.getSystem();
        Account account = _sessionController.getAccount();
        String phone = account.getPhone();
        if (phone.trim().isEmpty()) {
            phone = account.getCustomerPhone();
        }
        String msg = "Account: \t\t" + account.getId() + "\r\n"
                + "Firma: \t\t\t" + account.getCompany() + "\r\n"
                + "Ansprechpartner: \t" + account.getFirstName() + " " + account.getLastName() + "\r\n"
                + "Telefon: \t\t" + phone + "\r\n\r\n"
                + system.getDisplayName() + "\r\n"
                + _grouper.getCertStatus().getLabel() + "\r\n"
                + "Ergenis des Vergleiches: " + _compareResult + " Fehler\r\n";
        return _mailer.sendMailFrom("edv.zert@inek-drg.de", "edv.zert@inek-drg.de", "", "", "Upload Ergebnis " + system.getDisplayName(), msg);
    }

    private File getUploadPath(Grouper grouper) {
        File folder = new File(CertFileHelper.getSystemRoot(grouper.getSystem()), "Ergebnis");
        File uploadFolder = new File(folder, "" + grouper.getAccountId());
        try {
            if (!uploadFolder.exists()) {
                uploadFolder.mkdirs();
            }
            createMarkerFile(uploadFolder);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "upload, error during creating folder {0}", folder.getAbsolutePath());
        }
        return uploadFolder;
    }

    public String saveOther() {
        save();
        return "";
    }

    private void setNewStatus(String fileName, Grouper grouper) {
        if (fileName.startsWith("TestDaten v1")) {
            grouper.setTestUpload1(new Date());
            grouper.setCertStatus(CertStatus.TestUpload1);
        }
        if (fileName.startsWith("TestDaten v2")) {
            grouper.setTestUpload2(new Date());
            grouper.setCertStatus(CertStatus.TestUpload2);
        }
        if (fileName.startsWith("TestDaten v3")) {
            grouper.setTestUpload3(new Date());
            grouper.setCertStatus(CertStatus.TestUpload3);
        }
        if (fileName.startsWith("ZertDaten v1")) {
            grouper.setCertUpload1(new Date());
            grouper.setCertStatus(CertStatus.CertUpload1);
        }
        if (fileName.startsWith("ZertDaten v2")) {
            grouper.setCertUpload2(new Date());
            grouper.setCertStatus(CertStatus.CertUpload2);
        }
    }

    public void save() {
        try {
            saveFile();
            if (_configFacade.readConfigBool(ConfigKey.CertCompareOnUpload)) {
                String referenceFile = CertFileHelper.getReferenceFile(_grouper);
                String resultFile = CertFileHelper.getResultFile(_grouper);

                CertComparer comparer = new CertComparer();

                _compareResult = comparer.compare(resultFile, referenceFile,
                        _grouper.getSystem().getRemunerationSystem() == RemunSystem.DRG,
                        CertFileHelper.isCertPhase(_grouper));
            } else {
                _compareResult = -2;
            }

            sendEmailToInek();

            _grouper = _grouperFacade.merge(_grouper);
            //Todo Benachrichtigung über erfolgreichen upload
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error during compare", ex);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        _file = event.getFile();
        //Todo Nachricht das uplaod erfolgreich
    }

    public Boolean isTestUploadEnabled() {
        switch (_grouper.getCertStatus()) {
            case PasswordRequested:
            case TestFailed1:
            case TestFailed2:
                return true;
            default:
                return false;
        }
    }

    public Boolean isCertUploadEnabled() {
        switch (_grouper.getCertStatus()) {
            case TestSucceed:
            case CertFailed1:
                if (_grouper.getName().isEmpty()) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }
}
