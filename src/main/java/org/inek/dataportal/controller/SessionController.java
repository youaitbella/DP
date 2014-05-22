package org.inek.dataportal.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.inek.dataportal.admin.SessionCounter;
import org.inek.dataportal.common.SearchController;
import org.inek.dataportal.entities.Announcement;
import org.inek.dataportal.entities.InekRole;
import org.inek.dataportal.entities.Log;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountAdditionalIK;
import org.inek.dataportal.entities.account.AccountDocument;
import org.inek.dataportal.entities.account.AccountFeature;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.FeatureState;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.AnnouncementFacade;
import org.inek.dataportal.facades.DiagnosisFacade;
import org.inek.dataportal.facades.LogFacade;
import org.inek.dataportal.facades.PeppFacade;
import org.inek.dataportal.facades.ProcedureFacade;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.account.AccountFacade;
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
    @Inject
    private AnnouncementFacade _announcementFacade;
    @Inject
    private AccountFacade _accountFacade;
    @Inject
    private ProcedureFacade _procedureFacade;
    @Inject
    private DiagnosisFacade _diagnosisFacade;
    @Inject
    private PeppFacade _peppFacade;
    @Inject
    private LogFacade _logFacade;
    @Inject
    private AccountDocumentFacade _accDocFacade;

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

    /**
     * returns the account id if logged in otherwise it redirects to session
     * timeOut
     *
     * @return
     */
    public int getAccountId() {
        if (_account == null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.SessionTimeout.URL());
            return Integer.MIN_VALUE;
        }
        return _account.getAccountId();
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

    public String navigate(String topic) {
        logMessage("Navigate to " + topic);
        endAllConversations();
        return topic + "?faces-redirect=true";
    }

    public String beginConversation(Conversation conversation) {
        if (conversation.isTransient()) {
            int minutes = 30;
            conversation.setTimeout(minutes * 60000);
            conversation.begin(UUID.randomUUID().toString());
            //_logger.log(Level.WARNING, "Conversation started: {0}", conversation.getId());
            return conversation.getId();
        } else {
            //_logger.log(Level.WARNING, "Conversation still running: {0}", conversation.getId());
            return conversation.getId();
        }
    }

    public void endConversation(Conversation conversation) {
        if (!conversation.isTransient()) {
            //_logger.log(Level.WARNING, "Conversation stopping: {0}", conversation.getId());
            conversation.end();
        }
    }

    public void endAllConversations() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
        Map<String, Conversation> conversations = (Map<String, Conversation>) map.get("org.jboss.weld.context.ConversationContext.conversations");
        for (Conversation conversation : conversations.values()) {
            endConversation(conversation);
        }
    }

    public String logout() {
        if (_account != null) {
            endAllConversations();
            logMessage("Logout");
            _account = null;
            _topics.clear();
            _features.clear();
            _parts.clear();
            invalidateSession();
        }
        return Pages.Login.URL() + "?faces-redirect=true";
    }

    private void invalidateSession() {
        String sessionId = retrieveSessionId();
        if (sessionId.length() > 0) {
            try {
                System.out.println("old session " + sessionId);
                //FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                sessionId = ((HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest()).changeSessionId();
                System.out.println("new session " + sessionId);
            } catch (Exception ex) {
                _logger.log(Level.WARNING, "Exception during invalidatesesion");
            }
        }
    }

    public void logMessage(String msg) {
        String sessionId = retrieveSessionId();
        int accountId = -1;
        if (_account != null) {
            accountId = _account.getAccountId();
        }
        Log log = new Log(accountId, sessionId, msg);
        _logFacade.persist(log);
    }

    private String retrieveSessionId() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (facesContext == null) {
            return "";
        }
        String sessionId = facesContext.getExternalContext().getSessionId(false);
        return sessionId == null ? "" : sessionId;
    }

    /**
     * Login for usage within the portal.
     * Setup environment after login.
     * @param mailOrUser 
     * @param password
     * @return
     */
    public boolean loginAndSetTopics(String mailOrUser, String password) {
        invalidateSession();
        login(mailOrUser, password);
        initFeatures();
        setTopics();
        setParts();
        return _account != null;
    }

    /**
     * General login function. Will be used from UploadServlet (DatenDienst) too.
     * Thus, perform login only.
     * @param mailOrUser
     * @param password
     * @return 
     */
    public boolean login(String mailOrUser, String password) {
        _account = _accountFacade.getAccount(mailOrUser, password);
        if (_account == null) {
            logMessage("Login failed");
            return false;
        }
        logMessage("Login");
        return true;
    }

    private void initFeatures() {
        _features.clear();
        if (_account == null) {
            return;
        }
        Map<Integer, Feature> features = new TreeMap<>();
        boolean hasMaintenance = false;
        boolean hasDocument = false;
        for (AccountFeature accFeature : _account.getFeatures()) {
            hasMaintenance |= accFeature.getFeature() == Feature.USER_MAINTENANCE;
            hasDocument |= accFeature.getFeature() == Feature.DOCUMENTS;
            if (accFeature.getFeatureState() == FeatureState.SIMPLE || accFeature.getFeatureState() == FeatureState.APPROVED) {
                features.put(accFeature.getSequence(), accFeature.getFeature());
            }
        }
        if (!hasMaintenance) {
            _features.add(FeatureFactory.createController(Feature.USER_MAINTENANCE, _account, this));
        }
        if (!hasDocument && userHasDocuments()) {
            _features.add(FeatureFactory.createController(Feature.DOCUMENTS, _account, this));
            persistDocumentFeature();
        }
        for (Feature f : features.values()) {
            _features.add(FeatureFactory.createController(f, _account, this));
        }
    }

    private boolean userHasDocuments() {
        List<AccountDocument> docs = _accDocFacade.findAll(_account.getAccountId());
        return docs.size() > 0;
    }

    private void persistDocumentFeature() {
        AccountFeature doc = createAccountFeature(Feature.DOCUMENTS);
        List<AccountFeature> afs = _account.getFeatures();
        afs.add(doc);
        _account.setFeatures(afs);
        _account = _accountFacade.updateAccount(_account);
    }

    private AccountFeature createAccountFeature(Feature feature) {
        AccountFeature accFeature = new AccountFeature();
        accFeature.setFeature(feature);
        FeatureState state = FeatureState.NEW;
        accFeature.setFeatureState(state);
        accFeature.setSequence(_account.getFeatures().size());
        return accFeature;
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

    /**
     * @param requestedFeature
     * @return true, if the current user is within any InEK role for the
     * requested feature
     */
    public boolean isInekUser(Feature requestedFeature) {
        return isInekUser(requestedFeature, false);
    }

    public boolean isInekUser(String featureName) {
        Feature requestedFeature = Feature.valueOf(featureName);
        return isInekUser(requestedFeature, false);
    }

    /**
     *
     * @param requestedFeature
     * @param needsWriteAccess
     * @return true, if the current user is within any InEK role for the
     * requested feature and either has write access enabled or no write access
     * is requested
     */
    public boolean isInekUser(Feature requestedFeature, boolean needsWriteAccess) {
        for (InekRole role : getAccount().getInekRoles()) {
            if ((role.isIsWriteEnabled() || !needsWriteAccess)
                    && (role.getFeature() == Feature.ADMIN || role.getFeature() == requestedFeature)) {
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
        return Utils.getClientIP().equals("127.0.0.1")
                || Utils.getClientIP().equals("0:0:0:0:0:0:0:1")
                || Utils.getClientIP().startsWith("192.168.0");
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
