package org.inek.dataportal.feature.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.admin.InekRole;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.entities.admin.RoleMapping;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.admin.InekRoleFacade;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.facades.admin.RoleMappingFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.feature.cooperation.CooperationController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped
public class AdminTask extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("AdminTask");

    public AdminTask() {
        //_logger.log(Level.INFO, "Ctor AdminTask");
    }

    @Inject private SessionController _sessionController;

    @Override
    protected void addTopics() {
        addTopic(AdminTaskTabs.tabAdminTaskSystemStatus.name(), Pages.AdminTaskSystemStatus.URL());
        addTopic(AdminTaskTabs.tabAdminTaskInekRoles.name(), Pages.AdminTaskInekRoles.URL());
        addTopic(AdminTaskTabs.tabAdminTaskRoleMapping.name(), Pages.AdminTaskRoleMapping.URL());
        addTopic(AdminTaskTabs.tabAdminTaskMailTemplate.name(), Pages.AdminTaskMailTemplate.URL());
    }

    enum AdminTaskTabs {

        tabAdminTaskSystemStatus,
        tabAdminTaskInekRoles,
        tabAdminTaskRoleMapping,
        tabAdminTaskMailTemplate;
    }

    @PostConstruct
    private void init() {
        if (!_sessionController.isInekUser(Feature.ADMIN)) {
            _sessionController.logMessage("Non-authorized access to admin task.");
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nav = fc.getApplication().getNavigationHandler();
            nav.handleNavigation(fc, null, Pages.NotAllowed.URL());
            return;
        }
    }

    @PreDestroy
    private void destroy() {
    }

    private CooperationController getCooperationController() {
        return (CooperationController) _sessionController.getFeatureController(Feature.COOPERATION);
    }

    // <editor-fold defaultstate="collapsed" desc="tab MailTemplate">
    @Inject
    MailTemplateFacade _mailTemplateFacade;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public List<SelectItem> getMailTemplates() {
        List<SelectItem> l = _mailTemplateFacade.getMailTemplateInfos();
        SelectItem emptyItem = new SelectItem(-1, Utils.getMessage("itemNewEntry"));
        emptyItem.setNoSelectionOption(true);
        l.add(emptyItem);
        return l;
    }
    
    public List<SelectItem> getMailTemplatesCert() {
        List<SelectItem> l = _mailTemplateFacade.getMailTemplateInfosByFeature(Feature.CERT);
        SelectItem emptyItem = new SelectItem(-1, Utils.getMessage("itemNewEntry"));
        emptyItem.setNoSelectionOption(true);
        l.add(emptyItem);
        return l;
    }

    private MailTemplate _mailTemplate = new MailTemplate();

    public MailTemplate getMailTemplate() {
        return _mailTemplate;
    }

    public void setMailTemplate(MailTemplate mailTemplate) {
        _mailTemplate = mailTemplate;
    }

    private int _templateId = -1;

    public int getTemplateId() {
        return _mailTemplate.getId();
    }

    public void setTemplateId(int templateId) {
        if (templateId != _mailTemplate.getId()) {
            if (templateId == -1) {
                _mailTemplate = new MailTemplate();
            } else {
                _mailTemplate = _mailTemplateFacade.find(templateId);
            }
            setTemplateChanged(false);
        }
    }

    private boolean _templateChanged = false;

    public boolean isTemplateChanged() {
        return _templateChanged;
    }

    public void setTemplateChanged(boolean isChanged) {
        _templateChanged = isChanged;
    }

    // </editor-fold>
    public String newMailTemplate() {
        _mailTemplate = new MailTemplate();
        return Pages.AdminTaskMailTemplate.RedirectURL();
    }

    public String deleteMailTemplate() {
        if (_mailTemplate.getId() > 0) {
            _mailTemplateFacade.remove(_mailTemplate);
        }
        _mailTemplate = new MailTemplate();
        setTemplateChanged(false);
        return Pages.AdminTaskMailTemplate.RedirectURL();
    }

    public String saveMailTemplate() {
        _mailTemplate = _mailTemplateFacade.save(_mailTemplate);
        setTemplateChanged(false);
        return Pages.AdminTaskMailTemplate.RedirectURL();
    }

    public void mailTemplateChangeListener(AjaxBehaviorEvent event) {
        setTemplateChanged(true);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="tab InEK roles">
    private boolean _roleChanged = false;

    public boolean isRoleChanged() {
        return _roleChanged;
    }

    public void setRoleChanged(boolean isChanged) {
        _roleChanged = isChanged;
    }

    @Inject
    InekRoleFacade _inekRoleFacade;

    private List<InekRole> _originalInekRoles;
    private List<InekRole> _inekRoles;

    public List<InekRole> getInekRoles() {
        if (_inekRoles == null) {
            _inekRoles = _inekRoleFacade.findAll();
            _originalInekRoles = new ArrayList<>();
            for (InekRole role : _inekRoles) {
                _originalInekRoles.add(role.copy());
            }
        }
        return _inekRoles;
    }

    public void setInekRoles(List<InekRole> roles) {
        _inekRoles = roles;
    }

    public void addNewInekRole() {
        _inekRoles.add(new InekRole());
        setRoleChanged(true);
    }

    public String deleteInekRole(InekRole entry) {
        _inekRoles.remove(entry);
        setRoleChanged(true);
        return "";
    }

    public String saveInekRoles() {
        for (InekRole role : _inekRoles) {
            InekRole original = findAndRemoveOriginalRole(role.getId());
            if (original == null || !role.fullyEquals(original)) {
                _inekRoleFacade.save(role);
            }
        }
        for (InekRole deletedRole : _originalInekRoles) {
            _inekRoleFacade.remove(deletedRole);
        }
        _inekRoles = null;
        setRoleChanged(false);
        return Pages.AdminTaskInekRoles.RedirectURL();
    }

    private InekRole findAndRemoveOriginalRole(int id) {
        for (Iterator<InekRole> itr = _originalInekRoles.iterator(); itr.hasNext();) {
            InekRole role = itr.next();
            if (role.getId() == id) {
                itr.remove();
                return role;
            }
        }
        return null;
    }

    public void roleChangeListener(AjaxBehaviorEvent event) {
        setRoleChanged(true);
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="tab RoleMapping">
    @Inject
    AccountFacade _accountFacade;

    private List<SelectItem> _inekAgents;

    public List<SelectItem> getInekAgents() {
        if (_inekAgents == null) {
            _inekAgents = _accountFacade.getInekAgents();
            _inekAgents.add(0, new SelectItem(-1, ""));
        }
        return _inekAgents;
    }

    public void setInekAcounts(List<SelectItem> inekAagents) {
        _inekAgents = inekAagents;
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
        _inekRole = findRole(id, _inekRoles);
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

    // </editor-fold>
    private InekRole findRole(int id, List<InekRole> roles) {
        for (InekRole role : roles) {
            if (role.getId() == id) {
                return role;
            }
        }
        return null;
    }

}
