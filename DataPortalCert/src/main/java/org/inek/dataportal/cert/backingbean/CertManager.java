package org.inek.dataportal.cert.backingbean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.poi.util.IOUtils;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.cert.entities.RemunerationSystem;
import org.inek.dataportal.cert.entities.Grouper;
import org.inek.dataportal.cert.facade.GrouperFacade;
import org.inek.dataportal.cert.facade.SystemFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.cert.enums.CertStatus;
import org.inek.dataportal.cert.comparer.CertFileHelper;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.RemunSystem;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.utils.DateUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class CertManager implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("CertManager");

    @Inject
    private ConfigFacade _config;
    @Inject
    private SystemFacade _systemFacade;
    @Inject
    private GrouperFacade _grouperFacade;
    @Inject
    private DialogController _dialogController;

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
            }
            if (systemId == -1) {
                _system = new RemunerationSystem();
            } else {
                _systemFacade.clearCache(Grouper.class);
                _system = _systemFacade.findFresh(systemId);
                Collections.sort(_system.getGrouperList(),
                        (o1, o2) -> o1.getAccount().getCompany().compareToIgnoreCase(o2.getAccount().getCompany()));

            }
        }
    }

    private boolean _active = true;

    public boolean isActive() {
        return _active;
    }

    public void setActive(boolean active) {
        this._active = active;
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
        try {
            _system = _systemFacade.save(_system);
            _dialogController.showSaveDialog();
        } catch (Exception ex) {
            _dialogController.showErrorDialog("Fehler beim speichern", "Fehler beim Speichern. Bitte versuchen Sie es erneut");
        }
        return "";
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
        return "";
    }

    public String cancelSystem() {
        setSystemId(_system.getId(), true);
        return "";
    }

    public void activeSystemChsaangeListener(AjaxBehaviorEvent event) {
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
        grouper.setSystem(_system);
        _system.addGrouper(grouper);
    }

    public void deleteGrouper(Grouper grouper) {
        _system.removeGrouper(grouper);
    }

    public void passwordRequest(Grouper grouper) {
        grouper.setPasswordRequest(Calendar.getInstance().getTime());
        grouper.setCertStatus(CertStatus.PasswordRequested);
    }

    public void addedHp(Grouper grouper) {
        grouper.setWebsiteRelease(Calendar.getInstance().getTime());
    }

    public boolean isCertVendorOnWebsite(Grouper grouper) {
        return grouper.getWebsiteRelease() != null;
    }

    public String getCertWebsiteDate(Grouper grouper) {
        if (isCertVendorOnWebsite(grouper)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            return sdf.format(grouper.getWebsiteRelease());
        }
        return "";
    }

    public void handleSpecUpload(FileUploadEvent event) {
        uploadCertFile("Spec", "Grouper-Spezifikation", "exe", event.getFile());
    }

    public void handleTestUpload(FileUploadEvent event) {
        uploadCertFile("Daten", "Testdaten", "zip", event.getFile());
    }

    public void handleCertUpload(FileUploadEvent event) {
        uploadCertFile("Daten", "Zertdaten", "zip", event.getFile());
    }

    public void uploadCertFile(String folder, String fileNameBase, String extension, UploadedFile file) {
        String base = CertFileHelper.getSysDir(_system) + "\\" + folder;

        String outFileName = fileNameBase + "_"
                + _system.getFileName() + "_("
                + DateUtils.todayAnsi() + ")."
                + extension;

        try {
            OutputStream outStream = new FileOutputStream(new File(base, outFileName));
            IOUtils.copy(file.getInputstream(), outStream);
            file.getInputstream().close();
            outStream.close();
            _dialogController.showInfoDialog("Upload erfolgreich", "Der Upload von "
                    + file.getFileName() + " wurde erfolgreich durchgeführt");
        } catch (Exception ex) {
            _dialogController.showErrorDialog("Fehler beim Upload", "Die Datei konnte nicht "
                    + "erfolgreich hochgeladen werden: " + ex.getMessage());
        }
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
        //_reportController.createSingleDocument("CertGrouperAkt.xlsx", _system.getId());
    }

    public List getRemunerationDomains() {
        List<SelectItem> list = new ArrayList<>();
        for (RemunSystem val : RemunSystem.values()) {
            list.add(new SelectItem(val.getId(), val.getName()));
        }
        return list;
    }

    public File getSystemRoot(RemunerationSystem system) {
        File root = new File(_config.readConfig(ConfigKey.CertiFolderRoot), "System " + system.getYearSystem());
        File systemRoot = new File(root, system.getFileName());
        return systemRoot;
    }
}
