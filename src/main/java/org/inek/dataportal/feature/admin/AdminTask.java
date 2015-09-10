package org.inek.dataportal.feature.admin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.NavigationHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.admin.InekRole;
import org.inek.dataportal.entities.admin.MailTemplate;
import org.inek.dataportal.entities.admin.RoleMapping;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.admin.InekRoleFacade;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.facades.admin.RoleMappingFacade;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;
import org.inek.dataportal.feature.AbstractEditController;
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
        addTopic(AdminTaskTabs.tabAdminTaskIkSupervisor.name(), Pages.AdminTaskIkSupervisor.URL());

    }

    enum AdminTaskTabs {

        tabAdminTaskSystemStatus,
        tabAdminTaskInekRoles,
        tabAdminTaskRoleMapping,
        tabAdminTaskMailTemplate,
        tabAdminTaskIkSupervisor;
    }

    @PostConstruct
    private void init() {
        if (!_sessionController.isInekUser(Feature.ADMIN)) {
            _sessionController.logMessage("Non-authorized access to admin task.");
            FacesContext fc = FacesContext.getCurrentInstance();
            NavigationHandler nav = fc.getApplication().getNavigationHandler();
            nav.handleNavigation(fc, null, Pages.NotAllowed.URL());
        }
    }

    // <editor-fold defaultstate="collapsed" desc="tab Status">
    public void clearCache(ActionEvent e) {
        _accountFacade.clearCache();
    }
    // </editor-fold>

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
            _inekRoleFacade.clearCache(InekRole.class);
            _inekRoles = _inekRoleFacade.findAll();
            _originalInekRoles = new ArrayList<>();
            for (InekRole role : _inekRoles) {
                _originalInekRoles.add(role.copy());
            }
            int roleId = _inekRole == null ? _inekRoles.get(0).getId() : _inekRole.getId();
            setInekRoleId(roleId);
        }
        return _inekRoles;
    }

    public void setInekRoles(List<InekRole> roles) {
        _inekRoles = roles;
    }

    public void addNewInekRole() {
        getInekRoles().add(new InekRole());
        setRoleChanged(true);
    }

    public String deleteInekRole(InekRole entry) {
        getInekRoles().remove(entry);
        setRoleChanged(true);
        return "";
    }

    public String saveInekRoles() {
        for (InekRole role : getInekRoles()) {
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
        _inekRole = findRole(id, getInekRoles());
        _mappings = copyList(_inekRole.getMappings());
    }

    private InekRole findRole(int id, List<InekRole> roles) {
        for (InekRole role : roles) {
            if (role.getId() == id) {
                return role;
            }
        }
        return null;
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
    // <editor-fold defaultstate="collapsed" desc="tab IkSupervisor">
    @Inject private CooperationRightFacade _cooperationRightFacade;

    public List<IkSupervisorInfo> getIkSupervisorInfos() {
        return _cooperationRightFacade.getIkSupervisorInfos();
    }

    private Feature _feature = Feature.NUB;
    private int _ik;
    private Account _account;
    private CooperativeRight _cooperativeRight = CooperativeRight.ReadWriteCompletedSealSupervisor;
    
    public Feature[] getFeatures() {
        return Feature.values();
    }

    public CooperativeRight[] getCooperativeRights() {
        return CooperativeRight.values();
    }

    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
    }

    public int getAccountId() {
        return _account == null ? 0 : _account.getId();
    }

    public void setAccountId(int accountId) {
        _account = _accountFacade.find(accountId);
    }

    public String getEmail() {
        return _account == null ? "" : _account.getEmail();
    }

    public void setEmail(String email) {
        _account = _accountFacade.findByMailOrUser(email);
    }

    public Account getAccount() {
        return _account;
    }

    public void setAccount(Account account) {
        _account = account;
    }

    public CooperativeRight getCooperativeRight() {
        return _cooperativeRight;
    }

    public void setCooperativeRight(CooperativeRight cooperativeRight) {
        _cooperativeRight = cooperativeRight;
    }

    public void checkEmail(FacesContext context, UIComponent component, Object value) {
        String email = (String) value;
        Account account = _accountFacade.findByMailOrUser(email);
        if (account == null) {
            String msg = Utils.getMessage("errUnknownEmail");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void checkAccountId(FacesContext context, UIComponent component, Object value) {
        Account account = _accountFacade.find((int) value);
        if (account == null) {
            String msg = Utils.getMessage("errUnknownAccount");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public String deleteIkSupervisor(IkSupervisorInfo info) {
        _sessionController.logMessage("Delete IK supervisor: account=" + info.getAccount().getId() + ", feature=" + info.getFeature() + ", ik=" + info.getIk() + ", right=" + info.getRight().name());
        _cooperationRightFacade.deleteCooperationRight(-1, info.getAccount().getId(), info.getFeature(), info.getIk());
        return "";
    }

    public String saveIkSupervisor(){
        _sessionController.logMessage("Create IK supervisor: account=" + _account.getId() + ", feature=" + _feature + ", ik=" + _ik + ", right=" + _cooperativeRight.name() );
        _cooperationRightFacade.createIkSupervisor (_feature, _ik, _account.getId(), _cooperativeRight);
        return "";
    }
    // </editor-fold>
}
