package org.inek.dataportal.feature.certification;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.entities.certification.SystemAccountMapping;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.facades.certification.EmailReceiverFacade;
import org.inek.dataportal.facades.certification.EmailReceiverLabelFacade;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;

/**
 *
 * @author vohldo, muellermi
 */
@Named
@FeatureScoped
public class EditCert extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditCert");

    @Inject private SessionController _sessionController;
    @Inject private SystemFacade _systemFacade;
    @Inject private AccountFacade _accFacade;
    @Inject private MailTemplateFacade _mtFacade;
    @Inject private EmailReceiverFacade _erFacade;
    @Inject private EmailReceiverLabelFacade _erlFacade;

    private CertCertification _certification;
    public CertCertification getCertification() {
        if (_certification == null) {
            _certification = new CertCertification(_sessionController, _systemFacade);
        }
        return _certification;
    }

    private CertMail _certMail;
    public CertMail getCertMail() {
        if (_certMail == null) {
            _certMail = new CertMail(_mtFacade, _systemFacade, _accFacade, _erFacade, _erlFacade);
        }
        return _certMail;
    }

    public void setCertification(CertCertification certification) {
        _certification = certification;
    }

    @PostConstruct
    private void init() {
        _logger.log(Level.WARNING, "Init EditCert");
    }

    @PreDestroy
    private void destroy() {
        _logger.log(Level.WARNING, "Destroy EditCert");
    }

    @Override
    protected void addTopics() {
        if (_sessionController.isInekUser(Feature.CERT)) {
            addTopic(CertTabs.tabCertSystemManagement.name(), Pages.CertSystemManagement.URL());
            addTopic(CertTabs.tabCertMail.name(), Pages.CertMail.URL());
            addTopic(CertTabs.tabCertification.name(), Pages.CertCertification.URL());
        }
        addTopic(CertTabs.tabCertification.name(), Pages.CertCertification.URL());
    }

    private enum CertTabs {

        tabCertSystemManagement,
        tabCertMail,
        tabCertification;
    }

    // <editor-fold defaultstate="collapsed" desc="tab SystemManagement">
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
        _system = _systemFacade.save(_system);
        persistFiles(new File(_system.getSystemRoot(), "Spec"));
        persistFiles(new File(_system.getSystemRoot(), "Daten"));
        setSystemChanged(false);
        return Pages.CertSystemManagement.RedirectURL();
    }

    private void persistFiles(File dir) {
        if (!dir.exists()) {
            return;
        }
        for (File file : dir.listFiles((File file) -> file.isFile() && file.getName().endsWith(".upload"))) {
            File target = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 7));
            file.renameTo(target);
        }
    }

    public void systemChangeListener(AjaxBehaviorEvent event) {
        setSystemChanged(true);
    }

    //***************************************************************************************
    // account for system
    @Inject
    AccountFacade _accountFacade;

    private List<SelectItem> _certAccounts;

    public List<SelectItem> getCertAccounts() {
        if (_certAccounts == null) {
            List<Account> accounts = _accountFacade.getAccounts4Feature(Feature.CERT);
            _certAccounts = new ArrayList<>();
            _certAccounts.add(new SelectItem(-1, ""));
            for (Account account : accounts) {
                _certAccounts.add(new SelectItem(account.getAccountId(), account.getCompany()));
            }
        }
        return _certAccounts;
    }

    public void addNewMapping() {
        SystemAccountMapping mapping = new SystemAccountMapping();
        mapping.setAccountId(_sessionController.getAccountId());
        mapping.setSystemId(_system.getId());
        _system.getMappings().add(mapping);
    }

    public String deleteAccountMapping() {
        //todo
        return "";
    }

    public String deleteAccountMapping(SystemAccountMapping entry) {
        _system.getMappings().remove(entry);
        setSystemChanged(true);
        return "";
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="tab Certification">
    // </editor-fold>
}
