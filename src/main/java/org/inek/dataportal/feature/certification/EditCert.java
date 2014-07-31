package org.inek.dataportal.feature.certification;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.admin.InekRole;
import org.inek.dataportal.entities.admin.RoleMapping;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.admin.RoleMappingFacade;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author vohldo, muellermi
 */
@Named
@ConversationScoped
public class EditCert extends AbstractEditController {

    @Inject private SessionController _sessionController;
    @Inject private Conversation _conversation;
    @Inject SystemFacade _systemFacade;

    @PostConstruct
    private void init() {
        _sessionController.beginConversation(_conversation);
    }

    @PreDestroy
    private void destroy() {
    }

    @Override
    protected void addTopics() {
        if (_sessionController.isInekUser(Feature.CERT)) {
            addTopic(CertTabs.tabCertSystemManagement.name(), Pages.CertSystemManagement.URL());
        }
        addTopic(CertTabs.tabCertification.name(), Pages.CertCertification.URL());
    }

    private enum CertTabs {

        tabCertSystemManagement,
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
        setSystemChanged(false);
        return Pages.CertSystemManagement.RedirectURL();
    }

    public void systemChangeListener(AjaxBehaviorEvent event) {
        setSystemChanged(true);
    }

//***************************************************************************************
    @Inject
    AccountFacade _accountFacade;
    private List<InekRole> _inekRoles;

    private List<SelectItem> _certAccounts;

    public List<SelectItem> getCertAccounts() {
        if (_certAccounts == null) {
            List<Account> accounts = _accountFacade.getAcounts4Feature(Feature.CERT);
            _certAccounts.add(new SelectItem(-1, ""));
            for (Account account : accounts) {
                _certAccounts.add(new SelectItem(account.getAccountId(), account.getCompany()));
            }
        }
        return _certAccounts;
    }

    public void setInekAcounts(List<SelectItem> inekAagents) {
        _certAccounts = inekAagents;
    }

    private InekRole _inekRole;

    public InekRole getInekRole() {
        return _inekRole;
    }

    public void setInekRole(InekRole inekRole) {
        _inekRole = inekRole;
    }

    public int getInekRoleId() {
        return _inekRole == null ? -1 : _inekRole.getId();
    }

    public void setInekRoleId(int id) {
        //_inekRole = findRole(id, _inekRoles);
        _mappings = copyList(_inekRole.getMappings());
    }

    private List<RoleMapping> copyList(List<RoleMapping> mappings) {
        // Collections.copy alwas threw an index out of bound, even if sized before :(
        List<RoleMapping> copy = new ArrayList<>();
        for (RoleMapping roleMapping : mappings) {
            RoleMapping clone = new RoleMapping();
            clone.setAccountId(roleMapping.getAccountId());
            clone.setInekRoleId(roleMapping.getInekRoleId());
            copy.add(clone);
        }
        return copy;
    }

    private List<RoleMapping> _mappings;

    public List<RoleMapping> getMappings() {
        return _mappings;
    }

    public void setMappings(List<RoleMapping> mappings) {
        _mappings = mappings;
    }

    private boolean _mappingChanged = false;

    public boolean isMappingChanged() {
        return _mappingChanged;
    }

    public void setMappingChanged(boolean isChanged) {
        _mappingChanged = isChanged;
    }

    public void addNewMapping() {
        RoleMapping mapping = new RoleMapping();
        mapping.setInekRoleId(_inekRole.getId());
        _mappings.add(mapping);
    }

    public String deleteRoleMapping(RoleMapping entry) {
        _mappings.remove(entry);
        setMappingChanged(true);
        return "";
    }

    @Inject RoleMappingFacade _mappingFacade;
    public String saveRoleMapping() {
        List<RoleMapping> former = copyList(_inekRole.getMappings());
        for (Iterator<RoleMapping> itr = _mappings.iterator(); itr.hasNext();) {
            RoleMapping mapping = itr.next();
            if (mapping.getAccountId() == -1 || mapping.getInekRoleId() == -1) {
                itr.remove();
            }
            if (former.contains(mapping)) {
                former.remove(mapping);
                itr.remove();
            }
        }
        for (RoleMapping mapping : _mappings) {
            _mappingFacade.persist(mapping);
        }
        for (RoleMapping mapping : former) {
            _mappingFacade.remove(mapping);
        }
        setMappingChanged(false);
        _inekRoles = null;
        return Pages.AdminTaskRoleMapping.RedirectURL();
    }

    public void mappingChangeListener(AjaxBehaviorEvent event) {
        setMappingChanged(true);
    }

//***************************************************************************************
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="tab Certification">
    public List<SelectItem> getSystems4Account() {

        List<SelectItem> list = new ArrayList<>();
        for (RemunerationSystem system : _sessionController.getAccount().getRemuneratiosSystems()) {
            if (system.isApproved()) {
                list.add(new SelectItem(system.getId(), system.getDisplayName()));
            }
        }
        return list;
    }

    // </editor-fold>
}
