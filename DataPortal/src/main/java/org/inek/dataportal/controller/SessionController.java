package org.inek.dataportal.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.SearchController;
import org.inek.dataportal.entities.CustomerType;
import org.inek.dataportal.entities.icmt.Customer;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountFeature;
import org.inek.dataportal.entities.admin.InekRole;
import org.inek.dataportal.entities.admin.Log;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.FeatureState;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.CustomerTypeFacade;
import org.inek.dataportal.facades.DrgFacade;
import org.inek.dataportal.facades.PeppFacade;
import org.inek.dataportal.facades.account.AccountDocumentFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.admin.LogFacade;
import org.inek.dataportal.facades.common.DiagnosisFacade;
import org.inek.dataportal.facades.common.ProcedureFacade;
import org.inek.dataportal.helper.NotLoggedInException;
import org.inek.dataportal.helper.Topic;
import org.inek.dataportal.helper.Topics;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;
import org.inek.dataportal.mail.Mailer;
import org.inek.dataportal.system.SessionCounter;

/**
 *
 * @author muellermi
 */
@Named
@SessionScoped
public class SessionController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger("SessionController");
    @Inject private AccountFacade _accountFacade;
    @Inject private ProcedureFacade _procedureFacade;
    @Inject private DiagnosisFacade _diagnosisFacade;
    @Inject private PeppFacade _peppFacade;
    @Inject private DrgFacade _drgFacade;
    @Inject private LogFacade _logFacade;
    @Inject private AccountDocumentFacade _accDocFacade;
    @Inject private Mailer _mailer;
    @Inject private CustomerTypeFacade _typeFacade;

    public Mailer getMailer() {
        return _mailer;
    }

    private Account _account;
    private final Topics _topics = new Topics();
    private String _currentTopic = "";
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

    public void clearCurrentTopic() {
        _currentTopic = "";
        _topics.setAllInactive();
    }

    public Account getAccount() {
        checkAccount();
        return _account;
    }

    private void checkAccount() throws NotLoggedInException {
        if (_account == null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.SessionTimeout.URL());
            throw new NotLoggedInException();
        }
    }

    public boolean isHospital() {
        CustomerType type = _typeFacade.find(_account.getCustomerTypeId());
        return type.isHospital() || isInekUser(Feature.ADMIN);
    }

    public boolean isLoggedIn() {
        return _account != null;
    }

    /**
     * returns the account id if logged in otherwise it redirects to session
     * timeOut
     *
     * @return
     */
    public int getAccountId() {
        checkAccount();
        return _account.getId();
    }

    public SearchController getSearchController() {
        if (_searchController == null) {
            _searchController = new SearchController(this, _procedureFacade, _diagnosisFacade, _peppFacade, _drgFacade);
        }
        return _searchController;
    }

    public String getMainPage() {
        return Pages.MainApp.URL();
    }

    public String getRemainingTime() {
        int maxInterval = FacesContext.getCurrentInstance().getExternalContext().getSessionMaxInactiveInterval();
        int minutes = maxInterval / 60;
        int seconds = maxInterval % 60;
        //substract some time to ensure the client will log-out before the session expires
        if (minutes > 20) {
            minutes -= 1;
        } else {
            minutes -= 1;
            seconds = 55;
        }
        return "" + minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }
    // </editor-fold>

    public String navigate(String url) {
        logMessage("Navigate: URL=" + url);
        setCurrentTopicByUrl(url);
        endAllConversations();
        FeatureScopedContextHolder.Instance.destroyAllBeans();
        return url + "?faces-redirect=true";
    }

    public void setCurrentTopicByUrl(String url) {
        Topic topic = _topics.findTopicByOutcome(url);
        if (topic.getKey() == null) {
            clearCurrentTopic();
        } else {
            setCurrentTopic(topic.getKey());
        }
    }

    public String beginConversation(Conversation conversation) {
        if (conversation.isTransient()) {
            int minutes = 30;
            conversation.setTimeout(minutes * 60000);
            conversation.begin(UUID.randomUUID().toString());
            LOGGER.log(Level.INFO, "Conversation started: {0}", conversation.getId());
            return conversation.getId();
        } else {
            LOGGER.log(Level.INFO, "Conversation still running: {0}", conversation.getId());
            return conversation.getId();
        }
    }

    public void endConversation(Conversation conversation) {
        if (!conversation.isTransient()) {
            LOGGER.log(Level.INFO, "Conversation stopping: {0}", conversation.getId());
            conversation.end();
        }
    }

    public void endAllConversations() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
        @SuppressWarnings("unchecked")
        Map<String, Conversation> conversations = (Map<String, Conversation>) map.get("org.jboss.weld.context.ConversationContext.conversations");
        if (conversations == null) {
            return;
        }
        for (Conversation conversation : conversations.values()) {
            endConversation(conversation);
        }
    }

    public String logout() {
        performLogout("Logout");
        invalidateSession();
        return Pages.Login.URL();// + "?faces-redirect=true";
    }

    public void logout(String message) {
        performLogout(message);
        invalidateSession();
    }

    private void performLogout(String message) {
        if (_account != null) {
            endAllConversations();
            FeatureScopedContextHolder.Instance.destroyAllBeans();
            logMessage(message);
            _topics.clear();
            _features.clear();
            _parts.clear();
            _account = null;
        }
    }

    private void invalidateSession() {
        String sessionId = retrieveSessionId();
        if (sessionId.length() > 0) {
            try {
                //LOGGER.log(Level.INFO, "invalidateSession: old session {0}", sessionId);
                FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
                FacesContext.getCurrentInstance().getExternalContext().getSession(true);
                sessionId = retrieveSessionId();
                //LOGGER.log(Level.INFO, "invalidateSession: new session {0}", sessionId);
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Exception during invalidatesesion");
            }
        }
    }

    public void logMessage(String msg) {
        String sessionId = retrieveSessionId();
        int accountId = -1;
        if (_account != null) {
            accountId = _account.getId();
        }
        Log log = new Log(accountId, sessionId, msg);
        _logFacade.save(log);
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
     * Login for usage within the portal. Setup environment after login.
     *
     * @param mailOrUser
     * @param password
     * @return
     */
    public boolean loginAndSetTopics(String mailOrUser, String password) {
        String sessionId = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).changeSessionId();
        //invalidateSession();
        login(mailOrUser, password);
        setTopics();
        setParts();
        return _account != null;
    }

    /**
     * General login function. Will be used from UploadServlet (DatenDienst)
     * too. Thus, perform login and initFeatures only.
     *
     * @param mailOrUser
     * @param password
     * @return
     */
    private boolean login(String mailOrUser, String password) {
        String loginInfo = Utils.getClientIP() + "; UserAgent=" + Utils.getUserAgent();
        if (!login(mailOrUser, password, loginInfo)) {
            return false;
        }

        int sessionTimeout = (_account.getEmail().toLowerCase().endsWith("@inek-drg.de") && isInternalClient()) ? 36000 : 1800; // session timeout extended to 10 hour for internal user
        FacesContext.getCurrentInstance().getExternalContext().setSessionMaxInactiveInterval(sessionTimeout);
        return true;
    }

    public boolean login(String mailOrUser, String password, String loginInfo) {
        _account = _accountFacade.getAccount(mailOrUser, password);
        if (_account == null) {
            logMessage("Login failed: " + loginInfo);
            return false;
        }
        logMessage("Login successful: " + loginInfo);
        initFeatures();
        return true;
    }

    public boolean isElderInternetExplorer() {
        String userAgent = Utils.getUserAgent();
        if (userAgent == null) {
            return false;
        }
        String search = "compatible; MSIE";
        int pos = userAgent.indexOf(search);
        if (pos < 0) {
            return false;
        }
        int posAfter = userAgent.indexOf(";", pos + search.length());
        String versionString = userAgent.substring(pos + search.length(), posAfter).trim();
        try {
            Float version = Float.parseFloat(versionString);
            return version < 9.0;
        } catch (NumberFormatException ex) {
            return true;
        }
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
            if (_appTools.isFeatureEnabled(accFeature.getFeature()) && (accFeature.getFeatureState() == FeatureState.SIMPLE || accFeature.getFeatureState() == FeatureState.APPROVED)) {
                features.put(accFeature.getSequence(), accFeature.getFeature());
            }
        }
        if (isInekUser(Feature.ADMIN)) {
            _features.add(FeatureFactory.createController(Feature.ADMIN, _account, this));
        }
        if (!hasMaintenance) {
            _features.add(FeatureFactory.createController(Feature.USER_MAINTENANCE, _account, this));
        }
        if (!hasDocument && (userHasDocuments() || isInekUser(Feature.DOCUMENTS))) {
            _features.add(FeatureFactory.createController(Feature.DOCUMENTS, _account, this));
            persistDocumentFeature();
        }
        for (Feature f : features.values()) {
            _features.add(FeatureFactory.createController(f, _account, this));
        }
    }

    private boolean userHasDocuments() {
        return _accDocFacade.count(_account.getId()) > 0;
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
        if (_account == null) {
            return "???";
        }
        return _account.getFirstName() + " " + _account.getLastName();
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
        if (requestedFeature != Feature.DOCUMENTS && !isInternalClient()) {
            return false;
        }  // documents partially allowed from outside!
        if (_account == null || _account.getInekRoles() == null) {
            return false;
        }
        for (InekRole role : _account.getInekRoles()) {
            if ((role.isWriteEnabled() || !needsWriteAccess)
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
    @Inject private CustomerFacade _customerFacade;

    public String getIkName(Integer ik) {
        if (ik == null || ik == 0) {
            return "";
        }
        Customer customer = _customerFacade.getCustomerByIK(ik);
        String name = customer.getName() == null ? Utils.getMessage("msgUnknownIK") : customer.getName();
        return name;
    }

    public boolean isMyAccount(int accountId) {
        return isMyAccount(accountId, true);
    }

    public boolean isMyAccount(int accountId, boolean log) {
        if (_account.getId() == accountId) {
            return true;
        }
        if (log) {
            LOGGER.log(Level.WARNING, "Account {0} tried to access object from account {1}", new Object[]{_account.getId(), accountId});
        }
        return false;
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

    /**
     * This methods is used in an onclick event of a facelets page to add a
     * configurable confirmation behavior.
     *
     * @param key
     * @return
     */
    public String getConfirmMessage(String key) {
        String message = Utils.getMessageOrEmpty(key);
        if (message.isEmpty()) {
            message = key;
        }
        return "return confirm ('" + message.replace("\r\n", "\n").replace("\n", "\\r\\n") + "');";
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
        return null;
    }

    private boolean _testPerformed = false;

    public boolean isTestPerformed() {
        return _testPerformed;
    }

    public void setTestPerformed(boolean testPerformed) {
        _testPerformed = testPerformed;
    }

    private boolean _clickable = false;

    public boolean isClickable() {
        return _clickable;
    }

    public void setClickable(boolean clickable) {
        _clickable = clickable;
    }

    public String testClick() {
        _clickable = true;
        setTestPerformed(true);
        return Pages.Login.URL();
    }

    private String _windowName;

    public String getWindowName() {
        return _windowName;
    }

    public void setWindowName(String windowName) {
        if (_windowName == null) {
            // first access
            _windowName = "#DataPortal#" + UUID.randomUUID();
        } else if (!windowName.equals(_windowName)) {
            // new tab or window
            //performLogout("DoubleWindow");
            Utils.navigate(Pages.DoubleWindow.RedirectURL());
        }
    }

    public String navigateLogin() {
        _windowName = null;
        return Pages.Login.RedirectURL();
    }

    public String getManual() {
        if (_account == null) {
            return "InEK-Datenportal.pdf";
        }
        if (_account.getEmail().toLowerCase().endsWith("@inek-drg.de")) {
            return "InEK-DatenportalIntern.pdf";
        }
        if (_account.getFeatures().stream().anyMatch(f -> f.getFeature() == Feature.CERT)) {
            return "InEK-DatenportalZerti.pdf";
        }
        return "InEK-Datenportal.pdf";
    }
    @Inject ApplicationTools _appTools;

    public ApplicationTools getApplicationTools() {
        return _appTools;
    }

    public boolean isInMaintenanceMode() {
        // todo: read config or something else appropiate to determine, whether system is in maintenance mode
        return false;
    }

    private final Set<String> _acceptedTerms = new HashSet<>(4);

    public void acceptTermsOfUse(String name) {
        _acceptedTerms.add(name);
        logMessage("Nutzungsbedingungen akzeptiert: " + name);
    }

    public boolean isTermsOfUseAccepted(String name) {
        return _acceptedTerms.contains(name);
    }

    //@SuppressWarnings("unchecked")
    public <T> T findBean(String beanName, Class<T> clazz) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        return clazz.cast(facesContext.getApplication().evaluateExpressionGet(facesContext, "#{" + beanName + "}", Object.class));
        //return (T) facesContext.getApplication().evaluateExpressionGet(facesContext, "#{" + beanName + "}", Object.class);
    }

    public void hideData(boolean enabled) {
        if (!enabled) {
            String msg = "Sie haben gerade einen Bereich, der möglicherweise Daten enthält, ausgeblendet. "
                    + "Sofern dieser Daten enthält, bleiben diese vorerst erhalten, so dass diese zur Verfügung stehen, wenn Sie den Bereich wieder aktivieren. "
                    + "Sobald Sie die Daten an das InEK senden, werden diese bereinigt.";
            //Utils.showMessageInBrowser(msg);
            setScript("alert('" + msg + "');");
        }
    }

}
