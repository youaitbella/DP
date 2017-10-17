/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.ikadmin.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.feature.ikadmin.entity.AccessRight;
import org.inek.dataportal.feature.ikadmin.entity.User;
import org.inek.dataportal.feature.ikadmin.enums.Right;
import org.inek.dataportal.feature.ikadmin.facade.IkAdminFacade;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class IkAdminTasks extends AbstractEditController implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("IkAdminTasks");
    private static final String TOPIC_USER = "topicUserManagement";

    @Inject private SessionController _sessionController;
    @Inject private IkAdminFacade _ikAdminFacade;
    @Inject private ApplicationTools _appTools;

    private int _ik;

    public int getIk() {
        return _ik;
    }

    @Override
    protected void addTopics() {
        addTopic(TOPIC_USER, Pages.IkAdminUser.URL());
    }

    @Override
    protected void topicChanged() {
//        if (_sessionController.getAccount().isAutoSave() && !isReadOnly()) {
//            save(false);
//        }
    }

    @Override
    protected String getOutcome() {
        return "";
    }

    @PostConstruct
    private void init() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String ikParam = "" + params.get("ik");
        try {
            int ik = Integer.parseInt(ikParam);
            if (_sessionController.getAccount().getAdminIks().stream().anyMatch(a -> a.getIk() == ik)) {
                _ik = ik;
                return;
            }
        } catch (NumberFormatException ex) {
            // ignore here and handle after catch
        }
        Utils.navigate(Pages.NotAllowed.RedirectURL());
    }

    // <editor-fold defaultstate="collapsed" desc="User Management">
    private List<AccessRight> _accessRights;

    public List<AccessRight> getAccessRights() {
        if (_accessRights == null) {
            _accessRights = _ikAdminFacade.findAccessRights(_ik);
        }
        return _accessRights;
    }

    public List<Right> getRights() {
        List<Right> rights = new ArrayList<>();
        for (Right right : Right.values()) {
            rights.add(right);
        }
        return rights;
    }

    public String saveAccessRights() {
        try {
            for (AccessRight accessRight : _accessRights) {
                _ikAdminFacade.saveAccessRight(accessRight);
            }
            _sessionController.alertClient("Berechtigungen gespeichert");
            return null;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            return Pages.Error.URL();
        }
    }

    private int _userId;
    public int getUserId() {
        return _userId;
    }

    public void setUserId(int userId) {
        _userId = userId;
    }
    
    public Set<User> getUsers() {
        Set<User> users = new HashSet<>();
        for (AccessRight accessRight : _accessRights) {
            users.add(accessRight.getUser());
        }
        String[] mailDomains = _sessionController
                .getAccount()
                .getAdminIks()
                .stream()
                .filter(a -> a.getIk() == _ik)
                .map(a -> a.getMailDomain())
                .findAny()
                .orElse("")
                .split(";");
        for (String mailDomain : mailDomains) {
            List<User> mailUsers = _ikAdminFacade.findUsersByMailDomain((mailDomain.startsWith("@") ? "" : "@") + mailDomain);
            for (User mailUser : mailUsers) {
                users.add(mailUser);
            }
        }
        return users;
    }
    // </editor-fold>

}
