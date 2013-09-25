package org.inek.dataportal.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.admin.SessionCounter;
import org.inek.dataportal.common.SearchController;
import org.inek.dataportal.entities.Account;
import org.inek.dataportal.entities.AccountAdditionalIK;
import org.inek.dataportal.entities.AccountFeature;
import org.inek.dataportal.entities.Announcement;
import org.inek.dataportal.entities.InekRole;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.FeatureState;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.AccountFacade;
import org.inek.dataportal.facades.AnnouncementFacade;
import org.inek.dataportal.facades.DiagnosisFacade;
import org.inek.dataportal.facades.PeppFacade;
import org.inek.dataportal.facades.ProcedureFacade;
import org.inek.dataportal.helper.Topic;
import org.inek.dataportal.helper.Topics;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@Named
@SessionScoped
public class SessionController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger _logger = Logger.getLogger("SessionController");
    @Inject private Conversation _conversation;
    @Inject private AnnouncementFacade _announcementFacade;
    @Inject private AccountFacade _accountFacade;
    @Inject private ProcedureFacade _procedureFacade;
    @Inject private DiagnosisFacade _diagnosisFacade;
    @Inject private PeppFacade _peppFacade;

    private Account _account;
    private final Topics _topics = new Topics();
    private String _currentTopic;
    private final List<IFeatureController> _features;
    private SearchController _searchController;
    private final List<String> _parts = new ArrayList<>();

    public SessionController() {
        _features = new ArrayList<>();
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Conversation getCurrentConversation() {
        return _conversation;
    }

    public List<Topic> getTopics() {
        return _topics.getTopics();
    }

    public void setTopics(List<Topic> topics) {
        _topics.setTopics(topics);
    }

    public String getCurrentTopic() {
        return _currentTopic;
    }

    public void setCurrentTopic(String currentTopic) {
        _currentTopic = currentTopic;
        _topics.setActive(_currentTopic);
    }

    public Account getAccount() {
        return _account;
    }

    public boolean isLoggedIn() {
        return getAccount() != null;
    }

    public SearchController getSearchController() {
        if (_searchController == null) {
            _searchController = new SearchController(this, _procedureFacade, _diagnosisFacade, _peppFacade);
        }
        return _searchController;
    }

    public String getRemainingTime() {
        int maxInterval = FacesContext.getCurrentInstance().getExternalContext().getSessionMaxInactiveInterval();
        int minutes = maxInterval / 60;
        int seconds = maxInterval % 60;
        //substract some time to ensure the client will lot-out before the session expires
        if (minutes > 20) {
            minutes -= 1;
        } else {
            minutes -= 1;
            seconds = 55;
        }
        return "" + minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }
    // </editor-fold>

    public String navigate(String topic){
        endConversation();
        if (topic.equals(Pages.UserMaintenanceMasterData.URL())){
            beginConversation();
        }
        return topic;
    }
            
    public String beginConversation() {
        if (_conversation.isTransient()) {
            int minutes = 30;
            _conversation.setTimeout(minutes * 60000);
            _conversation.begin();
            return _conversation.getId();
        } else {
            return _conversation.getId();
        }
    }

    public void endConversation() {
        if (!_conversation.isTransient()) {
            _conversation.end();
        }
    }

    public String getConversationId() {
        return _conversation.isTransient() ? "" : _conversation.getId();
    }

    public String getForceConversationEnd() {
        endConversation();
        return "";
    }

    public String logout() {
        if (_account != null) {
            endConversation();
            _account = null;
            _topics.clear();
            _features.clear();
            _parts.clear();
        }
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return Pages.Login.URL(); // + "?faces-redirect=true";
    }

    /**
     * @param mailOrUser the _mailOrUser to set to reset the user info (e.g.
     * after logout), call this method with an empty string
     * @param password
     * @return
     */
    public boolean loginAndSetTopics(String mailOrUser, String password) {
        login(mailOrUser, password);
        setTopics();
        setParts();
        return _account != null;
    }

    public boolean login(String mailOrUser, String password) {
        _account = _accountFacade.getAccount(mailOrUser, password);
        initFeatures();
        return _account != null;
    }

    private void initFeatures() {
        _features.clear();
        if (_account == null) {
            return;
        }
        Map<Integer, Feature> features = new TreeMap<>();
        boolean hasMaintenance = false;
        for (AccountFeature accFeature : _account.getFeatures()) {
            hasMaintenance |= accFeature.getFeature() == Feature.USER_MAINTENANCE;
            if (accFeature.getFeatureState() == FeatureState.SIMPLE || accFeature.getFeatureState() == FeatureState.APPROVED) {
                features.put(accFeature.getSequence(), accFeature.getFeature());
            }
        }
        if (!hasMaintenance) {
            _features.add(FeatureFactory.createController(Feature.USER_MAINTENANCE, _account, this));
        }
        for (Feature f : features.values()) {
            _features.add(FeatureFactory.createController(f, _account, this));
        }
    }

    public void saveAccount() {
        _account = _accountFacade.updateAccount(_account);
        initFeatures();
        setTopics();
        setParts();
    }

    public void deleteAccount() {
        _accountFacade.deleteAccount(_account);
    }

    private void setTopics() {
        _topics.clear();
        if (!isLoggedIn()) {
            return;
        }
        for (IFeatureController feature : _features) {
            _topics.addTopics(feature.getTopics());
        }
    }

    public String getUser() {
        if (getAccount() == null) {
            return "???";
        }
        return getAccount().getFirstName() + " " + getAccount().getLastName();
    }

    public boolean isInekUser(Feature requestedFeature) {
        for (InekRole role : getAccount().getInekRoles()) {
            if (role.getFeature() == Feature.ADMIN || role.getFeature() == requestedFeature) {
                return true;
            }
        }
        return false;
    }

    public void setParts() {
        _parts.clear();
        for (IFeatureController feature : _features) {
            if (feature.getMainPart().length() > 0) {
                _parts.add(feature.getMainPart());
            }
        }
    }

    public List<String> getParts() {
        return _parts;
    }

    public IFeatureController getFeatureController(Feature feature) {
        for (IFeatureController featureController : _features) {
            if (featureController.getFeature() == feature) {
                return featureController;
            }
        }
        throw new IllegalArgumentException("Feature " + feature + " is not registered");
    }

    public void setFeatureActive(Feature feature) {
        for (IFeatureController featureController : _features) {
            featureController.setActive(featureController.getFeature() == feature);
        }
    }

    public IFeatureController getActiveFeatureController() {
        for (IFeatureController featureController : _features) {
            if (featureController.isActive()) {
                return featureController;
            }
        }
        throw new IllegalArgumentException("No active FeatureController available");
    }

    public int countInstalledFeatures() {
        return _features.size();
    }

    private Map<Integer, String> _ikInfo;

    public String getIkName(int ik) {
        ensureIKInfo();
        return _ikInfo.get(ik);
    }

    private void ensureIKInfo() {
        if (_ikInfo == null) {
            _ikInfo = new HashMap<>();

            Integer ik = getAccount().getIK();
            if (ik != null) {
                _ikInfo.put(ik, getAccount().getCompany());
            }
            for (AccountAdditionalIK addIk : getAccount().getAdditionalIKs()) {
                _ikInfo.put(addIk.getIK(), addIk.getName());
            }
        }
    }

    public boolean isMyAccount(int accountId) {
        if (getAccount().getAccountId() == accountId) {
            return true;
        }
        _logger.log(Level.WARNING, "Account {0} tried to access object from account {1}", new Object[]{getAccount().getAccountId(), accountId});
        return false;
    }

    public List<Announcement> getWarnings() {
        return _announcementFacade.findActiveWarnings(true);
    }

    public List<Announcement> getAnnouncements() {
        return _announcementFacade.findActiveWarnings(false);
    }

    public String getScript() {
        if (_script.isEmpty()) {
            return "";
        }
        String script = _script;
        _script = "";
        return script;
    }

    public void setScript(String script) {
        _script = script;
    }
    private String _script = "";

    public void alertClient(String message) {
        String script = "alert ('" + message.replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
        setScript(script);
    }

    public int getSessionCount() {
        return SessionCounter.getCount();
    }

    public boolean isInternalClient() {
        return Utils.getClientIP().startsWith("192.168.")
                || Utils.getClientIP().equals("127.0.0.1")
                || Utils.getClientIP().equals("0:0:0:0:0:0:0:1");
    }

    public AccountFeature findAccountFeature(Feature feature) {
        for (AccountFeature accountFeature : getAccount().getFeatures()) {
            if (accountFeature.getFeature().equals(feature)) {
                return accountFeature;
            }
        }
        return new AccountFeature();
    }

}
