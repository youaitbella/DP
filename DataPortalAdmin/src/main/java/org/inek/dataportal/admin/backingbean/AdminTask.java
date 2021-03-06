package org.inek.dataportal.admin.backingbean;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.adm.InekRole;
import org.inek.dataportal.common.data.adm.RoleMapping;
import org.inek.dataportal.admin.facade.RoleMappingFacade;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.data.adm.facade.InekRoleFacade;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "AdminTask")
public class AdminTask extends AbstractEditController {

    private static final Logger LOGGER = Logger.getLogger("AdminTask");

    public AdminTask() {
        //LOGGER.log(Level.INFO, "Ctor AdminTask");
    }

    @Inject
    private SessionController _sessionController;

    @Override
    protected void addTopics() {

        addTopic(AdminTaskTabs.tabAdminTaskInekRoles.name(), Pages.AdminTaskInekRoles.URL());
        addTopic(AdminTaskTabs.tabAdminTaskRoleMapping.name(), Pages.AdminTaskRoleMapping.URL());

    }

    enum AdminTaskTabs {

        tabAdminTaskInekRoles,
        tabAdminTaskRoleMapping,

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

    // <editor-fold defaultstate="collapsed" desc="tab InEK roles">
    private boolean _roleChanged = false;

    public boolean isRoleChanged() {
        return _roleChanged;
    }

    public void setRoleChanged(boolean isChanged) {
        _roleChanged = isChanged;
    }

    @Inject
    private InekRoleFacade _inekRoleFacade;

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
        DialogController.showInfoMessage("Die Daten wurden gespeichert");
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
    private AccountFacade _accountFacade;

    private List<SelectItem> _inekAgents;

    public List<SelectItem> getInekAgents() {
        if (_inekAgents == null) {
            _inekAgents = _accountFacade.getInekAgents();
            _inekAgents.add(0, new SelectItem(-1, ""));
        }
        return _inekAgents;
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

    @Inject
    private RoleMappingFacade _mappingFacade;

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
            _mappingFacade.save(mapping);
        }
        for (RoleMapping mapping : former) {
            _mappingFacade.remove(mapping);
        }
        setMappingChanged(false);
        _inekRoles = null;
        DialogController.showInfoMessage("Die Daten wurden gespeichert");
        return Pages.AdminTaskRoleMapping.RedirectURL();
    }

    public void mappingChangeListener(AjaxBehaviorEvent event) {
        setMappingChanged(true);
    }
    // </editor-fold>

    public List<SelectItem> getFeatures() {
        List<SelectItem> l = new ArrayList<>();
        SelectItem emptyItem = new SelectItem(null, "");
        emptyItem.setNoSelectionOption(true);
        l.add(emptyItem);
        for (Feature f : Feature.values()) {
            l.add(new SelectItem(f, f.getDescription()));
        }
        return l;
    }

}
