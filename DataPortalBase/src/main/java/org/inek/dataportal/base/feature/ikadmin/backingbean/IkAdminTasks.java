/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.base.feature.ikadmin.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.IkReference;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.data.ikadmin.entity.User;
import org.inek.dataportal.common.enums.Right;
import org.inek.dataportal.common.data.ikadmin.facade.IkAdminFacade;
import org.inek.dataportal.common.helper.Utils;
import org.primefaces.context.RequestContext;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class IkAdminTasks extends AbstractEditController implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("IkAdminTasks");
    private static final String TOPIC_USER = "topicUserManagement";

    @Inject
    private SessionController _sessionController;
    @Inject
    private IkAdminFacade _ikAdminFacade;
    @Inject
    private AccountFacade _accountFacade;

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
            if (saveAccessRightsAllowed(_accessRights)) {
                for (AccessRight accessRight : _accessRights) {
                    _ikAdminFacade.saveAccessRight(accessRight);
                }
                _sessionController.alertClient("Berechtigungen gespeichert");
                return null;
            } else {
                addMessage("");
                return null;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            return Pages.Error.URL();
        }
    }

    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
        _featureId = 0;
    }

    private int _featureId;

    public int getFeatureId() {
        return _featureId;
    }

    public void setFeatureId(int featureId) {
        _featureId = featureId;
    }

    private final Map<Integer, Account> _accounts = new HashMap<>();

    public Set<Feature> getMissingFeatures() {
        Set<Feature> features = new HashSet<>();
        if (_accountId <= 0) {
            return features;
        }
        for (Feature feature : Feature.values()) {
            if (feature.getIkReference() != IkReference.Hospital) {
                continue;
            }
            if (_accessRights.stream().anyMatch(r -> r.getAccountId() == _accountId && r.getFeature() == feature)) {
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
        Account account = _accountFacade.findAccount(userId);
        if (account == null) {
            return false;
        }
        _accounts.put(userId, account);
        return true;
    }

    public Collection<Account> getAccounts() {
        ensureAccounts();
        return _accounts
                .values()
                .stream()
                .sorted((a1, a2) -> a1.getLastName().compareTo(a2.getLastName()))
                .collect(Collectors.toList());
    }

    private void ensureAccounts() {
        if (_accounts.isEmpty()) {
            List<Account> accounts = _accountFacade.getAccounts4Ik(_ik);
            for (Account account : accounts) {
                _accounts.put(account.getId(), account);
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
                String domain = mailDomain.trim();
                List<Account> mailAccounts = _accountFacade.findAccountsByMailDomain((domain.startsWith("@") ? "" : "@") + domain);
                for (Account account : mailAccounts) {
                    _accounts.put(account.getId(), account);
                }
            }
        }
    }

    public void addAccessRight() {
        if (_accountId == 0 || _featureId == 0) {
            return;
        }
        ensureAccount(_accountId);
        Account account = _accounts.get(_accountId);
        Feature feature = Feature.getFeatureFromId(_featureId);
        User user = createUserFromAccount(account);
        AccessRight accessRight = new AccessRight(user, _ik, feature, Right.Deny);
        _ikAdminFacade.saveAccessRight(accessRight);
        _accessRights.add(accessRight);
        if (!account.getFullIkSet().contains(_ik)) {
            account.addIk(_ik);
        }
        if (account.getFeatures().stream().noneMatch(f -> f.getFeature() == feature)) {
            account.addFeature(feature, true);
        }
        _accountFacade.updateAccount(account);
        _featureId = 0;
    }
    // </editor-fold>

    private User createUserFromAccount(Account account) {
        User user = new User();
        user.setId(account.getId());
        user.setFirstName(account.getFirstName());
        user.setLastName(account.getLastName());
        user.setEmail(account.getEmail());
        user.setCompany(account.getCompany());
        return user;
    }

    public boolean saveAccessRightsAllowed(List<AccessRight> accessRights) {
        for (AccessRight ar : accessRights) {
            if (!accessRights.stream()
                    .anyMatch(c -> c.getIk() == ar.getIk()
                    && c.getFeature() == ar.getFeature()
                    && c.getRight() != Right.Deny)) {
                return false;
            }
        }
        return true;
    }

    private void addMessage(String message) {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('dialog').show();");
    }
}
