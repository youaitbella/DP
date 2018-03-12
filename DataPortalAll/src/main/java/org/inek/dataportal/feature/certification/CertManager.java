package org.inek.dataportal.feature.certification;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PreDestroy;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.OptimisticLockException;
import javax.servlet.http.Part;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.entities.certification.Grouper;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.enums.CertStatus;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.certification.GrouperFacade;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.scope.FeatureScopedContextHolder;
import org.inek.dataportal.common.utils.DateUtils;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class CertManager implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("CertManager");

    @Inject private ConfigFacade _config;
    @Inject
    private SystemFacade _systemFacade;
    @Inject
    private GrouperFacade _grouperFacade;
    @Inject
    private SessionController _sessionController;

    @PreDestroy
    private void preDestroy() {
        cleanupUploadFiles();
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public List<SelectItem> getSystems() {
        List<SelectItem> list = _systemFacade.getRemunerationSystemInfosActive(isActive());
        SelectItem emptyItem = new SelectItem(-1, Utils.getMessage("itemNewEntry"));
        emptyItem.setNoSelectionOption(true);
        list.add(emptyItem);
        return list;
    }

    public List<SelectItem> getSystemsWithoutNew() {
        List<SelectItem> list = _systemFacade.getRemunerationSystemInfos();
        SelectItem emptyItem = new SelectItem(-1, "");
        emptyItem.setNoSelectionOption(true);
        list.add(0, emptyItem);
        return list;
    }

    private RemunerationSystem _system = new RemunerationSystem();

    public RemunerationSystem getSystem() {
        return _system;
    }

    public void setSystem(RemunerationSystem system) {
        _system = system;
    }

    public int getSystemId() {
        return _system.getId();
    }

    public void setSystemId(int systemId) {
        setSystemId(systemId, false);
    }

    private void setSystemId(int systemId, boolean force) {
        if (force || systemId != _system.getId()) {
            if (_system.getId() > 0) {
                cleanupUploadFiles();
            }
            if (systemId == -1) {
                _system = new RemunerationSystem();
            } else {
                _systemFacade.clearCache(Grouper.class);
                _system = _systemFacade.findFresh(systemId);
                Collections.sort(_system.getGrouperList(),
                        (o1, o2) -> o1.getAccount().getCompany().compareToIgnoreCase(o2.getAccount().getCompany()));

            }
            setSystemChanged(false);
        }
    }

    private boolean _active = true;

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean active) {
        this._active = active;
    }

    private boolean _systemChanged = false;

    public boolean isSystemChanged() {
        return _systemChanged;
    }

    public void setSystemChanged(boolean isChanged) {
        _systemChanged = isChanged;
    }

    // </editor-fold>
    public String newSystem() {
        _system = new RemunerationSystem();
        return Pages.CertSystemManagement.RedirectURL();
    }

    public String deleteSystem() {
        if (_system.getId() > 0) {
            _systemFacade.remove(_system);
        }
        _system = new RemunerationSystem();
        setSystemChanged(false);
        return Pages.CertSystemManagement.RedirectURL();
    }

    @SuppressWarnings("ModifiedControlVariable")
    public List<Grouper> findFreshGrouper() {
        List<Grouper> freshList = getSystem().getGrouperList();
        for (Grouper g : freshList) {
            g = _grouperFacade.findFresh(g.getId());
        }
        getSystem().setGrouperList(freshList);
        return getSystem().getGrouperList();
    }

    public String saveSystem() {
        List<Grouper> savedGroupers = new ArrayList<>();
        for (Grouper grouper : _system.getGrouperList()) {
            if (grouper.getId() == -1) {
                copyEmail(grouper);
            }
            try {
                savedGroupers.add(_grouperFacade.merge(grouper));
            } catch (Exception ex) {
                if ((ex.getCause() instanceof OptimisticLockException)) {
                    _sessionController.alertClient("Die Daten wurden bereits von einem anderen Benutzer geändert. Speichern nicht möglich.");
                    return "";
                }
                savedGroupers.add(mergeGrouper(grouper));
            }
        }
        _system.setGrouperList(savedGroupers);
        try {
            _systemFacade.save(_system);
        } catch (Exception ex) {
            if ((ex.getCause() instanceof OptimisticLockException)) {
                _sessionController.alertClient("Die Daten wurden bereits von einem anderen Benutzer geändert. Speichern nicht möglich.");
                return "";
            }
        }
        
        _system = _systemFacade.findFresh(_system.getId());
        persistFiles(new File(getSystemRoot(_system), "Spec"));
        persistFiles(new File(getSystemRoot(_system), "Daten"));
        setSystemChanged(false);
        return "";
    }

    private Grouper mergeGrouper(Grouper grouper) {
        Grouper currentGrouper = _grouperFacade.findFresh(grouper.getId());
        currentGrouper.setPasswordRequest(grouper.getPasswordRequest());
        currentGrouper.setCertStatus(grouper.getCertStatus());
        return _grouperFacade.merge(currentGrouper);
    }

    public String resetSystem() {
        for (Grouper grouper : _system.getGrouperList()) {
            grouper.setPasswordRequest(null);
            grouper.setDownloadSpec(null);
            grouper.setDownloadTest(null);
            grouper.setDownloadCert(null);
            grouper.setTestUpload1(null);
            grouper.setTestUpload2(null);
            grouper.setTestUpload3(null);
            grouper.setTestCheck1(null);
            grouper.setTestCheck2(null);
            grouper.setTestCheck3(null);
            grouper.setTestError1(-1);
            grouper.setTestError2(-1);
            grouper.setTestError3(-1);
            grouper.setCertCheck1(null);
            grouper.setCertCheck2(null);
            grouper.setCertError1(-1);
            grouper.setCertError2(-1);
            grouper.setCertUpload1(null);
            grouper.setCertUpload2(null);
            grouper.setCertification(null);
            grouper.setCertStatus(CertStatus.New);
        }
        setSystemChanged(true);
        return "";
    }

    public String cancelSystem() {
        setSystemId(_system.getId(), true);
        return "";
    }

    private void cleanupUploadFiles() {
        deleteFiles(new File(getSystemRoot(_system), "Spec"), ".*\\.upload");
        deleteFiles(new File(getSystemRoot(_system), "Daten"), ".*\\.upload");
    }

    public void deleteFiles(File dir, final String fileNamePattern) {
        if (!dir.exists()) {
            return;
        }
        for (File file : dir.listFiles((File file) -> file.isFile() && file.getName().matches(fileNamePattern))) {
            file.delete();
        }
    }

    private void persistFiles(File dir) {
        if (!dir.exists()) {
            return;
        }
        for (File file : dir.listFiles((File file) -> file.isFile() && file.getName().endsWith(".upload"))) {
            File target = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 7));
            if (target.exists()) {
                target.delete();
            }
            file.renameTo(target);
        }
    }

    public void systemChangeListener(AjaxBehaviorEvent event) {
        setSystemChanged(true);
    }

    public void activeSystemChangeListener(AjaxBehaviorEvent event) {
        setActive(true);
    }

    @Inject
    private AccountFacade _accountFacade;

    private List<SelectItem> _certAccounts;

    /**
     * Get a list of manufactorers who are not assigned yet
     *
     * @return
     */
    public List<SelectItem> getCertAccounts() {
        ensureCertAccounts();
        Set<Integer> grouperAccountIds = new HashSet<>();
        _system.getGrouperList().stream().filter(g -> g.getAccountId() > 0).mapToInt(g -> g.getAccountId()).forEach(i -> grouperAccountIds.add(i));
        return _certAccounts.stream().filter(i -> !grouperAccountIds.contains((int) i.getValue())).collect(Collectors.toList());
    }
    
    public List<Account> getAllGrouperAccounts() {
        return _accountFacade.getAccounts4Feature(Feature.CERT);
    }

    private void ensureCertAccounts() {
        if (_certAccounts == null) {
            List<Account> accounts = _accountFacade.getAccounts4Feature(Feature.CERT);
            _certAccounts = new ArrayList<>();
            _certAccounts.add(new SelectItem(-1, ""));
            for (Account account : accounts) {
                _certAccounts.add(new SelectItem(
                        account.getId(),
                        account.getCompany() + " - " + account.getFirstName() + " " + account.getLastName()));
            }
        }
    }

    public String getAccountDisplayName(int accountId) {
        ensureCertAccounts();
        Optional<SelectItem> item = _certAccounts.stream().filter(i -> (int) i.getValue() == accountId).findAny();
        if (item.isPresent()) {
            return item.get().getLabel();
        }
        return "???";
    }

    public void addNewGrouper() {
        Grouper grouper = new Grouper();
        grouper.setSystemId(_system.getId());
        setSystemChanged(true);
        _system.getGrouperList().add(grouper);
    }

    public String deleteGrouper(Grouper grouper) {
        _system.getGrouperList().remove(grouper);
        setSystemChanged(true);
        return "";
    }

    public void passwordRequest(Grouper grouper) {
        grouper.setPasswordRequest(Calendar.getInstance().getTime());
        grouper.setCertStatus(CertStatus.PasswordRequested);
        setSystemChanged(true);
    }

    public void addedHp(Grouper grouper) {
        grouper.setWebsiteRelease(Calendar.getInstance().getTime());
        setSystemChanged(true);
    }

    public boolean isCertVendorOnWebsite(Grouper grouper) {
        return grouper.getWebsiteRelease() != null;
    }

    public String getCertHpInactiveClass(Grouper grouper) {
        if (isCertVendorOnWebsite(grouper)) {
            return "certWebsiteButtonInactive";
        }
        return "";
    }

    public String getCertWebsiteDate(Grouper grouper) {
        if (isCertVendorOnWebsite(grouper)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            return sdf.format(grouper.getWebsiteRelease());
        }
        return "";
    }

    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public void uploadSpec(int systemId) {
        uploadCertFile("Spec", "Grouper-Spezifikation", "exe");
    }

    public void uploadTestData(int systemId) {
        uploadCertFile("Daten", "Testdaten", "zip");
    }

    public void uploadCertData(int systemId) {
        uploadCertFile("Daten", "Zertdaten", "zip");
    }

    public void uploadCertFile(String folder, String fileNameBase, String extension) {
        int systemId = _system.getId();
        EditCert editCert = FeatureScopedContextHolder.Instance.getBean(EditCert.class);
        RemunerationSystem system = editCert.getSystem(systemId);
        if (system == null) {
            return;
        }

        Optional<File> uploadFolder = editCert.getUploadFolder(system, folder);
        if (!uploadFolder.isPresent()) {
            return;
        }
        String fileNamePattern = fileNameBase + "_" + system.getFileName() + "_.*\\.upload";
        deleteFiles(uploadFolder.get(), fileNamePattern);
        String outFile = fileNameBase + "_" + system.getFileName() + "_(" + DateUtils.todayAnsi() + ")." + extension + ".upload";
        editCert.uploadFile(_file, new File(uploadFolder.get(), outFile));
        setSystemChanged(true);
    }

    private void copyEmail(Grouper newGrouper) {
        Grouper source = newGrouper;
        for (Grouper grouper : _grouperFacade.findByAccountId(newGrouper.getAccountId())) {
            if (grouper.getId() > source.getId()) {
                source = grouper;
            }
        }
        newGrouper.setEmailCopy(source.getEmailCopy());
    }

    public boolean disableApprovedCheckbox() {
        if (!_system.isCheckList() || !_system.isSpecManual()) {
            return true;
        }
        return false;
    }

    public boolean disableCheckAndSpecCheckbox() {
        if (_system.isApproved()) {
            return true;
        }
        return false;
    }
    
    public void ExportCertGrouper() {
        _sessionController.createSingleDocument("CertGrouperAkt.xlsx", _system.getId());
    }
    
   // <editor-fold defaultstate="collapsed" desc="SystemRoot">
    public File getSystemRoot(RemunerationSystem system) {
        File root = new File(_config.readConfig(ConfigKey.CertiFolderRoot), "System " + system.getYearSystem());
        File systemRoot = new File(root, system.getFileName());
        return systemRoot;
    }
    // </editor-fold>
    
}
