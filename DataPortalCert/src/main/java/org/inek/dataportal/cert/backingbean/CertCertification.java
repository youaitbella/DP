package org.inek.dataportal.cert.backingbean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.util.IOUtils;
import org.inek.dataportal.cert.enums.CertStatus;
import org.inek.dataportal.cert.comparer.CertComparer;
import org.inek.dataportal.cert.Helper.CertFileHelper;
import org.inek.dataportal.cert.entities.Grouper;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.utils.Helper;
import org.inek.dataportal.cert.entities.GrouperAction;
import org.inek.dataportal.cert.entities.RemunerationSystem;
import org.inek.dataportal.cert.facade.GrouperActionFacade;
import org.inek.dataportal.cert.facade.GrouperFacade;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.RemunSystem;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
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
    private GrouperFacade _grouperFacade;
    @Inject
    private ConfigFacade _configFacade;
    @Inject
    private DialogController _dialogController;

    private int _compareResult;
    private UploadedFile _file;
    private RemunerationSystem _system;
    private Grouper _grouper;
    private int _grouperId;

    public int getGrouperId() {
        return _grouperId;
    }

    public void setGrouperId(int grouperId) {
        this._grouperId = grouperId;
        _grouper = _grouperFacade.findFresh(grouperId);
    }

    public Grouper getGrouper() {
        return _grouper;
    }

    public void setGrouper(Grouper grouper) {
        _grouper = grouper;
    }

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

    public List<Grouper> getGrouper4Account() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date today = cal.getTime();

        List<Grouper> groupers = _grouperFacade.findByAccountIdAndDate(_sessionController.getAccountId(), today);

        return groupers;
    }

    public void grouperChanged(ValueChangeEvent event) {
        //_file = null;
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

    public StreamedContent downloadTest() {
        if (_grouper.getDownloadTest() == null) {
            _grouper.setDownloadTest(new Date());
            save();
        }
        logAction("Download Test");
        return downloadFile("Daten", "Testdaten", "zip");
    }

    public StreamedContent downloadCert() {
        if (_grouper.getDownloadCert() == null) {
            _grouper.setDownloadCert(new Date());
            save();
        }
        logAction("Download Zert");
        return downloadFile("Daten", "Zertdaten", "zip");
    }

    public StreamedContent downloadSpec() {
        if (_grouper.getDownloadSpec() == null) {
            _grouper.setDownloadSpec(new Date());
            save();
        }
        logAction("Download Spec");
        return downloadFile("Spec", "Grouper-Spezifikation", "exe");
    }

    private StreamedContent downloadFile(String folder, String name, String extension) {
        File file = CertFileHelper.getCertFile(_grouper.getSystem(), folder, name, extension);
        try {
            return new DefaultStreamedContent(new FileInputStream(file),
                    Helper.getContentType(file.getName()), file.getName());
        } catch (Exception ex) {
            return null;
        }
    }

    public String getExpectedFileName(Grouper grouper) {
        switch (grouper.getCertStatus()) {
            case PasswordRequested:
            case TestUpload1:
                return "TestDaten v1";
            case TestFailed1:
            case TestUpload2:
                return "TestDaten v2";
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
                + _grouper.getCertStatus().getLabel() + "\r\n\r\n"
                + buildResultString(_compareResult);
        return _mailer.sendMailFrom("edv.zert@inek-drg.de", "edv.zert@inek-drg.de", "", "", "Upload Ergebnis " + system.getDisplayName(), msg);
    }

    private String buildResultString(int result) {
        String msg = "Ergebnis des Vergleiches: ";

        switch (result) {
            case -2:
                msg += "Automatischer Vergleich ist deaktiviert\r\n";
                break;
            case -1:
                msg += "Fehler beim Vergleichen\r\n";
                break;
            case 0:
                msg += "Keine Fehler gefunden\r\n";
                break;
            default:
                msg += result + " Fehler gefunden\r\n";
                break;
        }

        return msg;
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

    public void saveOther() {
        save();
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
            if (_file != null) {
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
            }
            sendEmailToInek();
            _grouper = _grouperFacade.merge(_grouper);
            _dialogController.showSaveDialog();
            _file = null;
        } catch (Exception ex) {
            _dialogController.showErrorDialog("Fehler beim speichern",
                    "Fehler beim Speichern. Bitte versuchen Sie es erneut oder wenden Sie sich an das InEK");
            LOGGER.log(Level.SEVERE, "Error during compare", ex);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        _file = event.getFile();
    }

    public Boolean isTestUploadEnabled() {
        switch (_grouper.getCertStatus()) {
            case PasswordRequested:
            case TestFailed1:
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

    public Boolean inCheckPhase() {
        switch (_grouper.getCertStatus()) {
            case TestUpload1:
            case TestUpload2:
            case CertUpload1:
            case CertUpload2:
                return true;
            default:
                return false;
        }
    }
}
