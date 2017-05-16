package org.inek.dataportal.feature.certification;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.entities.certification.EmailLog;
import org.inek.dataportal.entities.certification.Grouper;
import org.inek.dataportal.enums.CertMailType;
import org.inek.dataportal.enums.CertStatus;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Genders;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.facades.certification.EmailLogFacade;
import org.inek.dataportal.facades.certification.GrouperFacade;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class CertGrouperResults implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("CertGrouperResults");
    private Grouper _grouper;
    private int _runs = 0;
    private String _selectedTemplate = "";
    private String _attachement = "";
    // Certificate Email
    private String _receiverEmailCertificate = "";
    private String _templateEmailCertificate = "";
    // -----------------
    private int _numErrors = 0;
    private Date _dateChecked = new Date();

    @Inject
    private AccountFacade _accFacade;

    @Inject
    private SystemFacade _sysFacade;

    @Inject
    private MailTemplateFacade _mtFacade;

    @Inject
    private EmailLogFacade _elFacade;

    @Inject
    private Mailer _mailer;

    @Inject
    private SessionController _sessionController;

    @Inject
    private GrouperFacade _grouperFacade;

    public String showResults(Grouper grouper) {
        _grouper = grouper;
        _attachement = "";
        _numErrors = getNumberOfErrors();
        _receiverEmailCertificate = "";
        _selectedTemplate = "";
        _templateEmailCertificate = "";
        _runs = 0;
        _dateChecked = new Date();
        return Pages.CertGrouperResults.RedirectURL();
    }

    public String getSystemName() {
        return _sysFacade.find(_grouper.getSystemId()).getDisplayName();
    }

    public String getCompanyName() {
        return _accFacade.find(_grouper.getAccountId()).getCompany();
    }

    public String getEmailReceivers() {
        return _accFacade.find(_grouper.getAccountId()).getEmail() + ";" + _grouper.getEmailCopy();
    }

    public String getEmailReceiver() {
        return _accFacade.find(_grouper.getAccountId()).getEmail();
    }

    public String getReceiverEmailCertificate() {
        return _receiverEmailCertificate;
    }

    public void setReceiverEmailCertificate(String receiverEmailCertificate) {
        this._receiverEmailCertificate = receiverEmailCertificate;
    }

    public int getNumErrors() {
        return _numErrors;
    }

    public void setNumErrors(int numErrors) {
        this._numErrors = numErrors;
    }

    public Date getDateChecked() {
        _dateChecked = new Date();
        return _dateChecked;
    }

    public String getFormattedDateChecked() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(getDateChecked());
    }

    public String getTemplateEmailCertificate() {
        return _templateEmailCertificate;
    }

    public void setTemplateEmailCertificate(String templateEmailCertificate) {
        _templateEmailCertificate = templateEmailCertificate == null ? "" : templateEmailCertificate;
    }

    public boolean hasNotDeliveredData() {
        return _grouper.getCertStatus() == CertStatus.New || _grouper.getCertStatus() == CertStatus.PasswordRequested;
    }

    public String getSelectedTemplate() {
        return _selectedTemplate;
    }

    public void setSelectedTemplate(String selectedTemplate) {
        this._selectedTemplate = selectedTemplate;
    }

    public List<SelectItem> getTemplates() {
        List<SelectItem> temp = new ArrayList<>();
        temp.add(new SelectItem(""));
        List<MailTemplate> mts = _mtFacade.findTemplatesByFeature(Feature.CERT);
        switch (_grouper.getCertStatus()) {
            case TestFailed1:
            case TestFailed2:
                addTemplatesToList(temp, mts, CertMailType.ErrorTest);
                break;
            case CertFailed1:
                addTemplatesToList(temp, mts, CertMailType.ErrorCert);
                break;
            case TestSucceed:
                addTemplatesToList(temp, mts, CertMailType.PassedTest);
                break;
            case CertSucceed:
                addTemplatesToList(temp, mts, CertMailType.Certified);
                break;
            default:
        }
        return temp;
    }

    private void addTemplatesToList(List<SelectItem> list, List<MailTemplate> mts, CertMailType type) {
        for (MailTemplate mt : mts) {
            if (mt.getType() == type.getId()) {
                list.add(new SelectItem(mt.getName()));
            }
        }
    }

    public int getNumberOfErrors() {
        int errors = 0;
        if (!hasNotDeliveredData()) {
            switch (_grouper.getCertStatus()) {
                case TestFailed1:
                    errors = _grouper.getTestError1();
                    break;
                case TestFailed2:
                    errors = _grouper.getTestError2();
                    break;
                case CertFailed1:
                    errors = _grouper.getCertError1();
                    break;
                default:
            }
        }
        return errors;
    }

    public int getRuns() {
        return _runs;
    }

    public String getCertState() {
        return "Zertifizierungsstatus: " + _grouper.getCertStatus().getLabel();
    }

    public boolean containsErrorInLatestUpload() {
        return getNumberOfErrors() > 0;
    }

    public boolean hasReceivedEmailToCurrentState() {
        CertMailType mailType = null;
        int numofMails = 0;
        switch (_grouper.getCertStatus()) {
            case CertFailed1:
                numofMails = 1;
                mailType = CertMailType.ErrorCert;
                break;
            case TestFailed1:
                numofMails = 1;
                mailType = CertMailType.ErrorTest;
                break;
            case TestFailed2:
                numofMails = 2;
                mailType = CertMailType.ErrorTest;
                break;
            case TestSucceed:
                numofMails = 1;
                mailType = CertMailType.PassedTest;
                break;
            case CertSucceed:
                numofMails = 1;
                mailType = CertMailType.Certified;
                break;
            default:
        }
        if (mailType == null) {
            return false;
        }
        return _elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(
                _grouper.getSystemId(), _grouper.getAccountId(), mailType.getId()
        ).size() >= numofMails;
    }

    public String getReiceiver() {
        return _accFacade.find(_grouper.getAccountId()).getEmail();
    }

    public String getCC() {
        return _grouper.getEmailCopy();
    }

    public String getBCC() {
        if (_selectedTemplate.isEmpty()) {
            return "";
        }
        return _mtFacade.findByName(_selectedTemplate).getBcc();
    }

    public String getFrom() {
        if (_selectedTemplate.isEmpty()) {
            return "";
        }
        return _mtFacade.findByName(_selectedTemplate).getFrom();
    }

    public String getAttachement() {
        if (_attachement.isEmpty()) {
            _attachement = buildAttachementString();
        }
        return _attachement;
    }

    public void setAttachement(String attachment) {
        _attachement = attachment;
    }

    public String getTest1Check() {
        if (_grouper.getTestCheck1() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getTestCheck1());
    }

    public String getTest2Check() {
        if (_grouper.getTestCheck2() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getTestCheck2());
    }

    public String getTest3Check() {
        if (_grouper.getTestCheck3() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getTestCheck3());
    }

    public String getTest1Error() {
        if (_grouper.getTestError1() == -1) {
            return "";
        }
        return _grouper.getTestError1() + "";
    }

    public String getTest2Error() {
        if (_grouper.getTestError2() == -1) {
            return "";
        }
        return _grouper.getTestError2() + "";
    }

    public String getTest3Error() {
        if (_grouper.getTestError3() == -1) {
            return "";
        }
        return _grouper.getTestError3() + "";
    }

    public String getTestUpload1() {
        if (_grouper.getTestUpload1() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getTestUpload1());
    }

    public String getTestUpload2() {
        if (_grouper.getTestUpload2() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getTestUpload2());
    }

    public String getTestUpload3() {
        if (_grouper.getTestUpload3() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getTestUpload3());
    }

    public String getCert1Check() {
        if (_grouper.getCertCheck1() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getCertCheck1());
    }

    public String getCert2Check() {
        if (_grouper.getCertCheck2() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getCertCheck2());
    }

    public String getCertUpload1() {
        if (_grouper.getCertUpload1() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getCertUpload1());
    }

    public String getCertUpload2() {
        if (_grouper.getCertUpload2() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getCertUpload2());
    }

    public String getCert1Error() {
        if (_grouper.getCertError1() == -1) {
            return "";
        }
        return _grouper.getCertError1() + "";
    }

    public String getCert2Error() {
        if (_grouper.getCertError2() == -1) {
            return "";
        }
        return _grouper.getCertError2() + "";
    }

    public String getCertMailDate() {
        if (_grouper.getCertification() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getCertification());
    }

    public String getDownloadSpec() {
        if (_grouper.getDownloadSpec() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getDownloadSpec());
    }

    public String getDownloadTest() {
        if (_grouper.getDownloadTest() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getDownloadTest());
    }

    public String getDownloadCert() {
        if (_grouper.getDownloadCert() == null) {
            return "";
        }
        return new SimpleDateFormat("dd.MM.yyyy").format(_grouper.getDownloadCert());
    }

    private String buildAttachementString() {
        String filename = "";
        if (lookForNumberOfRuns() == 1) {
            filename = "TestDaten v" + _runs + "_Diff.xls";
        } else {
            filename = "ZertDaten v" + _runs + "_Diff.xls";
        }
        String grouperYear = _sysFacade.find(_grouper.getSystemId()).getYearSystem() + "";
        String folderSystemName = _sysFacade.find(_grouper.getSystemId()).getDisplayName().replace("/", "_");
        return "\\\\vFileserver01\\company$\\EDV\\Projekte\\Zertifizierung\\Pruefung"
                + "\\System " + grouperYear + "\\" + folderSystemName + "\\Ergebnis\\" + _grouper.getAccountId()
                + "\\" + filename;
    }

    public boolean renderReceivedCertificationEmail() {
        return _elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(
                        _grouper.getSystemId(), _grouper.getAccountId(), CertMailType.Certificate.getId()).isEmpty()
                && _elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(
                        _grouper.getSystemId(), _grouper.getAccountId(), CertMailType.Certified.getId()).size() > 0
                && _grouper.getCertStatus() == CertStatus.CertSucceed;
    }

    public List<SelectItem> getInternalCertEmailReceivers() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(""));
        List<Account> accs = _accFacade.getAccounts4Feature(Feature.CERT);
        for (Account acc : accs) {
            if (acc.getEmail().endsWith("@inek-drg.de")) {
                items.add(new SelectItem(acc.getEmail()));
            }
        }
        return items;
    }

    public List<SelectItem> getEmailTemplatesCertificate() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(""));
        List<MailTemplate> templates = _mtFacade.findTemplatesByFeature(Feature.CERT);
        for (MailTemplate mt : templates) {
            if (mt.getType() == CertMailType.Certificate.getId()) {
                items.add(new SelectItem(mt.getName()));
            }
        }
        return items;
    }

    public String getEmailCertificateSubject() {
        if (_templateEmailCertificate.isEmpty()) {
            return "";
        }
        String subject = _mtFacade.findByName(_templateEmailCertificate).getSubject();
        subject = subject.replace("{company}", _accFacade.find(_grouper.getAccountId()).getCompany());
        subject = subject.replace("{system}", _sysFacade.find(_grouper.getSystemId()).getDisplayName());
        return subject;
    }

    public String getEmailCertificateBody() {
        if (_templateEmailCertificate.isEmpty() || _receiverEmailCertificate.isEmpty()) {
            return "";
        }
        String body = _mtFacade.findByName(_templateEmailCertificate).getBody();
        body = body.replace("{salutation}", buildEmailSalutation(_receiverEmailCertificate));
        body = body.replace("{company}", _accFacade.find(_grouper.getAccountId()).getCompany());
        body = body.replace("{product}", _grouper.getName());
        body = body.replace("{system}", _sysFacade.find(_grouper.getSystemId()).getDisplayName());
        body = body.replace("{certdate}", new SimpleDateFormat("dd.MM.yyyy").format(getCertDate()));
        body = body.replace("{name}", _sessionController.getAccount().getFirstName() + " " + _sessionController.getAccount().getLastName());
        return body;
    }

    private String buildEmailSalutation(String receiverEmail) {
        String receiver = receiverEmail;
        Account receiverAccount = _accFacade.findByMailOrUser(receiver);
        String title = receiverAccount.getTitle().equals("") ? "" : " " + receiverAccount.getTitle();
        boolean isFemale = true;
        if (receiverAccount.getGender() == Genders.Male.id()) {
            isFemale = false;
        }
        String salutation = "Sehr " + (isFemale ? "geehrte Frau" : "geehrter Herr") + title + " " + receiverAccount.getLastName() + ",";
        return salutation;
    }

    private Date getCertDate() {
        if (_grouper.getCertError1() == 0) {
            return _grouper.getCertCheck1();
        } else if (_grouper.getCertError2() == 0) {
            return _grouper.getCertCheck2();
        }
        return new Date();
    }

    public boolean enableSendCertificateEmailButton() {
        if (getEmailCertificateSubject().isEmpty() || getEmailCertificateBody().isEmpty()) {
            return false;
        }
        return true;
    }

    public String sendCertificateEmail() {
        EmailLog el = new EmailLog();
        if (_mailer.sendMailFrom(
                _mtFacade.findByName(_templateEmailCertificate).getFrom(),
                _receiverEmailCertificate,
                _mtFacade.findByName(_templateEmailCertificate).getBcc(),
                getEmailCertificateSubject(),
                getEmailCertificateBody())) {

            el.setType(CertMailType.Certificate.getId());
            el.setReceiverAccountId(_accFacade.findByMailOrUser(_receiverEmailCertificate).getId());
            el.setSenderAccountId(_sessionController.getAccountId());
            el.setSent(new Date());
            el.setSystemId(_grouper.getSystemId());
            el.setTemplateId(_mtFacade.findByName(_templateEmailCertificate).getId());
            _grouper.setCertStatus(CertStatus.CertificationPassed);
            _grouper.setCertification(new Date());
            _elFacade.save(el);
            try {
                Grouper savedGrouper = _grouperFacade.merge(_grouper);
                _grouper = savedGrouper;
            } catch (Exception ex) {
                if (!(ex.getCause() instanceof OptimisticLockException)) {
                    throw ex;
                }
                mergeGrouperCertDate();
            }
        }
        return "";
    }

    private void mergeGrouperCertDate() {
        _sessionController.logMessage("ConcurrentUpdate CertGrouperResults: grouper=" + _grouper.getId());
        Grouper currentGrouper = _grouperFacade.findFresh(_grouper.getId());
        currentGrouper.setCertStatus(_grouper.getCertStatus());
        currentGrouper.setCertification(_grouper.getCertification());
        _grouper = _grouperFacade.merge(currentGrouper);
    }

    public String getSubject() {
        if (_selectedTemplate.isEmpty()) {
            return "";
        }
        lookForNumberOfRuns();
        String subject = _mtFacade.findByName(_selectedTemplate).getSubject();
        subject = subject.replace("{system}", _sysFacade.find(_grouper.getSystemId()).getDisplayName());
        subject = subject.replace("{run}", _runs + "");
        return subject;
    }

    public String getBody() {
        if (_selectedTemplate.isEmpty()) {
            return "";
        }
        String errors = getNumberOfErrors() == 1 ? "1 Fall" : getNumberOfErrors() + " Fällen";
        String body = _mtFacade.findByName(_selectedTemplate).getBody();
        body = body.replace("{salutation}", buildEmailSalutation(_accFacade.find(_grouper.getAccountId()).getEmail()));
        body = body.replace("{sender}", _sessionController.getAccount().getFirstName() + " " + _sessionController.getAccount().getLastName());
        body = body.replace("{system}", _sysFacade.find(_grouper.getSystemId()).getDisplayName());
        body = body.replace("{errors}", errors);
        return body;
    }

    private int lookForNumberOfRuns() {
        int type = 1;
        if (_grouper.getTestError1() != -1) {
            _runs = 0;
            _runs++;
        }
        if (_grouper.getTestError2() != -1) {
            _runs++;
        }
        if (_grouper.getTestError3() != -1) {
            _runs++;
        }
        if (_grouper.getCertError1() != -1) {
            _runs = 0;
            type = 2;
            _runs++;
        }
        if (_grouper.getCertError2() != -1) {
            _runs++;
        }
        return type;
    }

    public boolean renderEnterErrorAndCheckDateFields() {
        switch (_grouper.getCertStatus()) {
            case CertUpload1:
            case CertUpload2:
            case TestUpload1:
            case TestUpload2:
            case TestUpload3:
                return true;
            default:
                return false;
        }
    }

    public String enterErrorAndDate() {
        switch (_grouper.getCertStatus()) {
            case TestUpload1:
                _grouper.setTestError1(_numErrors);
                _grouper.setTestCheck1(_dateChecked);
                if (_numErrors == 0) {
                    _grouper.setCertStatus(CertStatus.TestSucceed);
                } else {
                    _grouper.setCertStatus(CertStatus.TestFailed1);
                }
                break;
            case TestUpload2:
                _grouper.setTestError2(_numErrors);
                _grouper.setTestCheck2(_dateChecked);
                if (_numErrors == 0) {
                    _grouper.setCertStatus(CertStatus.TestSucceed);
                } else {
                    _grouper.setCertStatus(CertStatus.TestFailed2);
                }
                break;
            case TestUpload3:
                _grouper.setTestError3(_numErrors);
                _grouper.setTestCheck3(_dateChecked);
                if (_numErrors == 0) {
                    _grouper.setCertStatus(CertStatus.TestSucceed);
                } else {
                    _grouper.setCertStatus(CertStatus.CertificationFailed);
                }
                break;
            case CertUpload1:
                _grouper.setCertError1(_numErrors);
                _grouper.setCertCheck1(_dateChecked);
                if (_numErrors == 0) {
                    _grouper.setCertStatus(CertStatus.CertSucceed);
                } else {
                    _grouper.setCertStatus(CertStatus.CertFailed1);
                }
                break;
            case CertUpload2:
                _grouper.setCertError2(_numErrors);
                _grouper.setCertCheck2(_dateChecked);
                if (_numErrors == 0) {
                    _grouper.setCertStatus(CertStatus.CertSucceed);
                } else {
                    _grouper.setCertStatus(CertStatus.CertificationFailed);
                }
                break;
            default:
        }
        try {
            Grouper savedGrouper = _grouperFacade.merge(_grouper);
            _grouper = savedGrouper;
        } catch (Exception ex) {
            if (!(ex.getCause() instanceof OptimisticLockException)) {
                throw ex;
            }
            mergeGrouperErrorAndDate();
        }
        return "";
    }

    private void mergeGrouperErrorAndDate() {
        Grouper currentGrouper = _grouperFacade.findFresh(_grouper.getId());
        currentGrouper.setTestError1(_grouper.getTestError1());
        currentGrouper.setTestError2(_grouper.getTestError2());
        currentGrouper.setTestError3(_grouper.getTestError3());
        currentGrouper.setCertError1(_grouper.getCertError1());
        currentGrouper.setCertError2(_grouper.getCertError2());
        currentGrouper.setTestCheck1(_grouper.getTestCheck1());
        currentGrouper.setTestCheck2(_grouper.getTestCheck2());
        currentGrouper.setTestCheck3(_grouper.getTestCheck3());
        currentGrouper.setCertCheck1(_grouper.getCertCheck1());
        currentGrouper.setCertCheck2(_grouper.getCertCheck2());
        currentGrouper.setCertStatus(_grouper.getCertStatus());
        _grouper = _grouperFacade.merge(currentGrouper);
    }

    public String attachementExists() {
        File f = new File(getAttachement());
        if (f.exists()) {
            return "tick.png";
        }
        return "cross.png";
    }

    public boolean needsAttachement() {
        return getNumberOfErrors() > 0;
    }

    public String sendMail() {
        if (!needsAttachement()) {
            _attachement = "";
        }

        if (_mailer.sendMailFrom(getFrom(), getReiceiver(), getCC(), getBCC(), getSubject(), getBody(), _attachement)) {
            EmailLog el = new EmailLog();
            el.setReceiverAccountId(_accFacade.findByMailOrUser(getReiceiver()).getId());
            el.setSenderAccountId(_sessionController.getAccount().getId());
            el.setSent(new Date());
            el.setSystemId(_grouper.getSystemId());
            el.setTemplateId(_mtFacade.findByName(_selectedTemplate).getId());
            el.setType(_mtFacade.findByName(_selectedTemplate).getType());
            _elFacade.save(el);
        }
        return "";
    }

    public boolean renderCertificationFinished() {
        return _grouper.getCertStatus() == CertStatus.CertificationPassed;
    }

    public boolean renderReset1Step() {
        switch (_grouper.getCertStatus()) {
            case Unknown:
            case New:
            case CertificationFailed:
            case CertificationPassed:
                return false;
            default:
                return true;
        }
    }

    public String reset1Step() {
        switch (_grouper.getCertStatus()) {
            case PasswordRequested:
                _grouper.setCertStatus(CertStatus.New);
                _grouper.setPasswordRequest(null);
                break;
            case TestUpload1:
                _grouper.setCertStatus(CertStatus.PasswordRequested);
                _grouper.setTestUpload1(null);
                break;
            case TestFailed1:
                _grouper.setCertStatus(CertStatus.TestUpload1);
                _grouper.setTestError1(-1);
                _grouper.setTestCheck1(null);
                break;
            case TestUpload2:
                _grouper.setCertStatus(CertStatus.TestFailed1);
                _grouper.setTestUpload2(null);
                break;
            case TestFailed2:
                _grouper.setCertStatus(CertStatus.TestUpload2);
                _grouper.setTestError2(-1);
                _grouper.setTestCheck2(null);
                break;
            case TestUpload3:
                _grouper.setCertStatus(CertStatus.TestFailed2);
                _grouper.setTestUpload3(null);
                break;
            case TestSucceed:
                if (_grouper.getTestError1() == 0) {
                    _grouper.setCertStatus(CertStatus.TestUpload1);
                } else if (_grouper.getTestError2() == 0) {
                    _grouper.setCertStatus(CertStatus.TestUpload2);
                } else if (_grouper.getTestError3() == 0) {
                    _grouper.setCertStatus(CertStatus.TestUpload3);
                }
                break;
            case CertUpload1:
                _grouper.setCertStatus(CertStatus.TestSucceed);
                _grouper.setCertUpload1(null);
                break;
            case CertFailed1:
                _grouper.setCertStatus(CertStatus.CertUpload1);
                _grouper.setCertCheck1(null);
                _grouper.setCertError1(-1);
                break;
            case CertUpload2:
                _grouper.setCertStatus(CertStatus.CertFailed1);
                _grouper.setCertUpload2(null);
                break;
            case CertSucceed:
                if (_grouper.getCertError1() == 0) {
                    _grouper.setCertStatus(CertStatus.CertUpload1);
                } else if (_grouper.getCertError2() == 0) {
                    _grouper.setCertStatus(CertStatus.CertUpload2);
                }
                _grouper.setCertification(null);
                break;
            default:
        }
        _grouperFacade.merge(_grouper);
        _grouper = _grouperFacade.find(_grouper.getId()); // re-init grouper object to avoid exception
        return "";
    }

    public boolean needMail(Grouper grouper) {
        switch (grouper.getCertStatus()) {
            case TestFailed1:
            case TestFailed2:
            case TestSucceed:
            case CertFailed1:
            case CertSucceed:
            case CertificationPassed:
                return true;
            default:
                return false;
        }
    }

    public String getMailIcon(Grouper grouper) {
        if (!needMail(grouper)) {
            return "";
        }
        int sysId = grouper.getSystemId();
        int grId = grouper.getAccountId();
        String mailImage = "mail.png";
        switch (grouper.getCertStatus()) {
            case TestFailed1:
                if (_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.ErrorTest.getId()).size() == 1) {
                    return mailImage;
                }
                break;
            case TestFailed2:
                if (_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.ErrorTest.getId()).size() == 2) {
                    return mailImage;
                }
                break;
            case TestSucceed:
                if (_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.PassedTest.getId()).size() == 1) {
                    return mailImage;
                }
                break;
            case CertFailed1:
                if (_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.ErrorCert.getId()).size() == 1) {
                    return mailImage;
                }
                break;
            case CertSucceed:
                if (_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.Certified.getId()).size() == 1) {
                    return mailImage;
                }
                break;
            case CertificationPassed:
                if (_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.Certificate.getId()).size() == 1) {
                    return mailImage;
                }
                break;
            default:
        }
        return "mail_inactive.png";
    }

    public String getMailDate(Grouper grouper) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        if (!needMail(grouper)) {
            return "";
        }
        int sysId = grouper.getSystemId();
        int grId = grouper.getAccountId();
        switch (grouper.getCertStatus()) {
            case TestFailed1:
                if (_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.ErrorTest.getId()).size() == 1) {
                    return sdf.format(_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.ErrorTest.getId()).get(0).getSent());
                }
                break;
            case TestFailed2:
                if (_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.ErrorTest.getId()).size() == 2) {
                    return sdf.format(_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.ErrorTest.getId()).get(1).getSent());
                }
                break;
            case TestSucceed:
                if (_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.PassedTest.getId()).size() == 1) {
                    return sdf.format(_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.PassedTest.getId()).get(0).getSent());
                }
                break;
            case CertFailed1:
                if (_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.ErrorCert.getId()).size() == 1) {
                    return sdf.format(_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.ErrorCert.getId()).get(0).getSent());
                }
                break;
            case CertSucceed:
                if (_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.Certified.getId()).size() == 1) {
                    return sdf.format(_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.Certified.getId()).get(0).getSent());
                }
                break;
            case CertificationPassed:
                if (_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.Certificate.getId()).size() == 1) {
                    return sdf.format(_elFacade.findEmailLogsBySystemIdAndGrouperIdAndType(sysId, grId, CertMailType.Certificate.getId()).get(0).getSent());
                }
                break;
            default:
        }
        return "";
    }
}
