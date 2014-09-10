package org.inek.dataportal.feature.certification;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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
import javax.servlet.http.Part;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.certification.Grouper;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.certification.GrouperFacade;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.utils.DateUtils;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class CertManager {

    private static final Logger _logger = Logger.getLogger("CertManager");

    @Inject private SessionController _sessionController;
    @Inject private SystemFacade _systemFacade;
    @Inject private GrouperFacade _grouperFacade;

    @PreDestroy
    private void preDestroy() {
        cleanupUploadFiles();
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public List<SelectItem> getSystems() {
        List<SelectItem> list = _systemFacade.getRemunerationSystemInfos();
        SelectItem emptyItem = new SelectItem(-1, Utils.getMessage("itemNewEntry"));
        emptyItem.setNoSelectionOption(true);
        list.add(emptyItem);
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

    public void setSystemId(int templateId) {
        if (templateId != _system.getId()) {
            if (_system.getId() > 0) {
                cleanupUploadFiles();
            }
            if (templateId == -1) {
                _system = new RemunerationSystem();
            } else {
                _system = _systemFacade.find(templateId);
            }
            setSystemChanged(false);
        }
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

    public String saveSystem() {
        // due to problems on save (grouper lost), we save the grouper separated
        // it still saved one  together with the system :(
        //List<Grouper> grouperList = _system.getGrouperList();
        _system = _systemFacade.save(_system);
//        for (Grouper grouper : grouperList) {
//            grouper.setSystemId(_system.getId());
//            _grouperFacade.merge(grouper);
//        }
        persistFiles(new File(_system.getSystemRoot(), "Spec"));
        persistFiles(new File(_system.getSystemRoot(), "Daten"));
        setSystemChanged(false);
        return Pages.CertSystemManagement.RedirectURL();
    }

    public String cancelSystem() {
        cleanupUploadFiles();
        setSystemId(_system.getId());
        return Pages.CertSystemManagement.RedirectURL();
    }

    private void cleanupUploadFiles() {
        deleteFiles(new File(_system.getSystemRoot(), "Spec"), ".*\\.upload");
        deleteFiles(new File(_system.getSystemRoot(), "Daten"), ".*\\.upload");
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

    @Inject AccountFacade _accountFacade;

    private List<SelectItem> _certAccounts;

    public List<SelectItem> getCertAccounts() {
        ensureCertAccounts();
        Set<Integer> grouperAccountIds = new HashSet<>();
        _system.getGrouperList().stream().filter(g -> g.getAccountId() > 0).mapToInt(g -> g.getAccountId()).forEach(i -> grouperAccountIds.add(i));
        return _certAccounts.stream().filter(i -> !grouperAccountIds.contains((int) i.getValue())).collect(Collectors.toList());
    }

    private void ensureCertAccounts() {
        if (_certAccounts == null) {
            List<Account> accounts = _accountFacade.getAccounts4Feature(Feature.CERT);
            _certAccounts = new ArrayList<>();
            _certAccounts.add(new SelectItem(-1, ""));
            for (Account account : accounts) {
                _certAccounts.add(new SelectItem(account.getAccountId(), account.getCompany() + " - " + account.getFirstName() + " " + account.getLastName()));
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
    }

    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public void uploadSpec(int systemId) {
        uploadCertFile(systemId, "Spec", "Grouper-Spezifikation", "exe");
    }

    public void uploadTestData(int systemId) {
        uploadCertFile(systemId, "Daten", "Testdaten", "zip");
    }

    public void uploadCertData(int systemId) {
        uploadCertFile(systemId, "Daten", "Zertdaten", "zip");
    }

    public void uploadCertFile(int systemId, String folder, String fileNameBase, String extension) {
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
        CertManager certManager = (CertManager) FeatureScopedContextHolder.Instance.getBean(CertManager.class);
        certManager.setSystemChanged(true);
    }

}
