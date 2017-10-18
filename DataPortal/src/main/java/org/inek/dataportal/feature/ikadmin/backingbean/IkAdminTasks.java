/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.ikadmin.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.IkReference;
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
        _featureId = 0;
    }

    private int _featureId;

    public int getFeatureId() {
        return _featureId;
    }

    public void setFeatureId(int featureId) {
        _featureId = featureId;
    }

    private Map<Integer, Account> _accounts = new HashMap<>();

    public Set<Feature> getMissingFeatures() {
        Set<Feature> features = new HashSet<>();
        if (_userId <= 0) {
            return features;
        }
        for (Feature feature : Feature.values()) {
            if (feature.getIkReference() != IkReference.Hospital) {
                continue;
            }
            if (_accessRights.stream().anyMatch(r -> r.getAccountId() == _userId && r.getFeature() == feature)) {
                continue;
            }
            features.add(feature);
        }
        return features;
    }

    private boolean ensureAccount(int userId) {
        if (_accounts.containsKey(userId)) {
            return true;
        }
        Account account = _ikAdminFacade.findAccount(userId);
        if (account == null) {
            return false;
        }
        _accounts.put(userId, account);
        return true;
    }

    private final Set<User> _users = new HashSet<>();

    public Set<User> getUsers() {
        ensureUsers();
        return _users;
    }

    private void ensureUsers() {
        if (_users.isEmpty()) {
            for (AccessRight accessRight : _accessRights) {
                _users.add(accessRight.getUser());
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
                    _users.add(mailUser);
                }
            }
        }
    }

    public void addAccessRight() {
        if (_userId == 0 || _featureId == 0) {
            return;
        }
        for (User user : getUsers()) {
            if (user.getId() == _userId) {
                Feature feature = Feature.getFeatureFromId(_featureId);
                AccessRight accessRight = new AccessRight(user, _ik, feature, Right.Deny);
                _ikAdminFacade.saveAccessRight(accessRight);
                _accessRights.add(accessRight);
                if (!ensureAccount(_userId)) {
                    LOGGER.log(Level.WARNING, "Account {0} not found", _userId);
                    continue;
                }
                Account account = _accounts.get(_userId);
                if (!account.getFullIkSet().contains(_ik)) {
                    account.addIk(_ik);
                }
                //if (account.getFeatures().stream().noneMatch(f -> f.getFeature()))
                //todo: get users from cache, add feature and or ik to account
            }
        }
    }
    // </editor-fold>

}
