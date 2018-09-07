package org.inek.dataportal.common.controller;

import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import static java.net.HttpURLConnection.HTTP_OK;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.data.common.CustomerType;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountFeature;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.FeatureState;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.api.enums.PortalType;
import org.inek.dataportal.api.helper.Const;
import org.inek.dataportal.common.helper.EnvironmentInfo;
import org.inek.dataportal.common.data.access.CustomerTypeFacade;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.adm.facade.LogFacade;
import org.inek.dataportal.common.data.cooperation.facade.CooperationRequestFacade;
import org.inek.dataportal.common.data.adm.InekRole;
import org.inek.dataportal.common.data.adm.Log;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Stage;
import org.inek.dataportal.common.helper.NotLoggedInException;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.helper.Topic;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.scope.FeatureScopedContextHolder;

/**
 *
 * @author muellermi
 */
@Named
@SessionScoped
public class SessionController implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger("SessionController");
    @Inject
    private AccountFacade _accountFacade;
    @Inject
    private LogFacade _logFacade;
    @Inject
    private Mailer _mailer;
    @Inject
    private CustomerTypeFacade _typeFacade;
    @Inject
    private CooperationRequestFacade _coopFacade;
    @Inject
    private ApplicationTools _appTools;
    @Inject
    private transient FeatureHolder _featureHolder;

    public ApplicationTools getApplicationTools() {
        return _appTools;
    }

    private PortalType _portalType = PortalType.COMMON;

    public PortalType getPortalType() {
        return _portalType;
    }

    public void setPortalType(PortalType portalType) {
        this._portalType = portalType;
    }

    public Mailer getMailer() {
        return _mailer;
    }

    private Account _account;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public List<Topic> getTopics() {
        return _featureHolder.getTopics();
    }

    public String getCurrentTopic() {
        return _featureHolder.getCurrentTopic();
    }

    public void setCurrentTopic(String currentTopic) {
        _featureHolder.setCurrentTopic(currentTopic);
    }

    public void clearCurrentTopic() {
        _featureHolder.clearCurrentTopic();
    }

    public Account getAccount() {
        checkAccount();
        return _account;
    }

    /**
     * returns the account id if logged in, otherwise it redirects to session. timeOut
     *
     * @return
     */
    public int getAccountId() {
        Account account = getAccount();
        if (account == null) {
            return 0;
        }
        return account.getId();
    }

    private void checkAccount() {
        if (_account == null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            try {
                String url = EnvironmentInfo.getServerUrlWithContextpath() + Pages.SessionTimeout.URL();
                facesContext.getExternalContext().redirect(url);
            } catch (IOException | IllegalStateException ex) {
                facesContext.getApplication().getNavigationHandler().
                        handleNavigation(facesContext, null, Pages.SessionTimeout.URL());
                throw new NotLoggedInException();
            }
        }
    }

    public void refreshAccount(int id) {
        _account = _accountFacade.findAccount(id);
    }

    public boolean isHospital() {
        // we had unexpecte null access here.
        // let's do some logging and redirect the user to an error view
        if (_typeFacade == null) {
            String msg = "Access without typeFacade";
            LOGGER.log(Level.WARNING, msg);
            logMessage(msg);
            Utils.navigate(Pages.Error.RedirectURL());
            return false;
        }
        if (_account == null) {
            String msg = "Access without account";
            LOGGER.log(Level.WARNING, msg);
            logMessage(msg);
            Utils.navigate(Pages.Error.RedirectURL());
            return false;
        }
        CustomerType type = _typeFacade.find(_account.getCustomerTypeId());
        return type.isHospital() || isInekUser(Feature.ADMIN);
    }

    public boolean isLoggedIn() {
        return _account != null;
    }

    public String getMainPage() {
        return Pages.MainApp.URL();
    }

    public String getRemainingTime() {
        int maxInterval = FacesContext.getCurrentInstance().getExternalContext().getSessionMaxInactiveInterval();
        int minutes = maxInterval / Const.SECONDS_PER_MINUTE;
        int seconds = maxInterval % Const.SECONDS_PER_MINUTE;
        //substract some seconds to ensure the client will log-out before the session expires
        seconds -= 5;
        if (seconds < 0) {
            seconds += Const.SECONDS_PER_MINUTE;
            minutes--;
        }
        return "" + minutes + ":" + (seconds < 10 ? "0" + seconds : seconds);
    }
    // </editor-fold>

    public void setCurrentTopicByUrl(String url) {
        _featureHolder.setCurrentTopicByUrl(url);
    }

    public String logout() {
        String url = obtainTargetUrl(PortalType.COMMON);
        performLogout("Logout");
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
            return null;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return Pages.Login.RedirectURL();
    }

    public void performLogout(String message) {
        if (_account != null) {
            FeatureScopedContextHolder.Instance.destroyAllBeans();
            logMessage(message);
            _featureHolder.clear();
            _account = null;
            _portalType = PortalType.BASE;
        }
        invalidateSession();
    }

    private void invalidateSession() {
        FacesContext ctxt = FacesContext.getCurrentInstance();
        if (ctxt == null) {
            return;
        }
        try {
            HttpServletRequest request = (HttpServletRequest) ctxt.getExternalContext().getRequest();
            HttpServletResponse response = (HttpServletResponse) ctxt.getExternalContext().getResponse();
            response.setContentType("text/html");
            ctxt.getExternalContext().invalidateSession();
            ctxt.getExternalContext().getSessionId(true);

            // Delete all the cookies
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Exception during invalidatesesion: {0}", ex.getMessage());
        }

    }

    public void logMessage(String msg) {
        if (msg.isEmpty()) {
            return;
        }
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
     *
     * @return
     */
    @Deprecated
    public boolean loginAndSetTopics(String mailOrUser, String password) {
        return loginAndSetTopics(mailOrUser, password, _portalType);
    }

    public String navigate(String url) {
        try {
            PortalType portalType = PortalType.valueOf(url);
            changePortal(portalType);
            return "";
        } catch (IllegalArgumentException ex) {
            logMessage("Navigate: URL=" + url);
            setCurrentTopicByUrl(url);
            FeatureScopedContextHolder.Instance.destroyAllBeans();
            return url + "?faces-redirect=true";
        }
    }

    public void changePortal(PortalType portalType) {
        String url = obtainTargetUrl(portalType);
        if (url.isEmpty()) {
            return;
        }
        url = url + "?token=" + getToken() + "&portal=" + portalType.name();
        performLogout("");
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect(url);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    public String obtainTargetUrl(PortalType portalType) {
        Stage stage = _appTools.isEnabled(ConfigKey.TestMode)
                ? EnvironmentInfo.getServerName().equals("localhost") ? Stage.DEVELOPMENT : Stage.TEST
                : Stage.PRODUCTION;
        String url = _appTools.readPortalAddress(portalType, stage);
        return url;
    }

    public String getToken() {
        // todo: retrieve service address from a common place, e.g. database
        String address = "http://vubuntu01:9999/AccountService/api/account/id/{0}".replace("{0}", "" + getAccountId());
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != HTTP_OK) {
                throw new IOException("HTTP error code : " + conn.getResponseCode());
            }
            String token = StreamHelper.toString(conn.getInputStream());
            conn.disconnect();
            return token;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            alertClient("Beim AccountService trat ein Fehler auf");
            return "";
        }
    }

    private int getId(String token) {
        String address = "http://vubuntu01:9999/AccountService/api/account/token/{0}".replace("{0}", token);
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() != HTTP_OK) {
                throw new IOException("HTTP error code : " + conn.getResponseCode());
            }
            String idString = StreamHelper.toString(conn.getInputStream());
            conn.disconnect();
            return Integer.parseInt(idString);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            alertClient("Beim AccountService trat ein Fehler auf");
            return -123;
        }
    }

    public String testNavigation() {
        return "localhost://test";
    }

    public boolean loginByToken(String token, PortalType portalType) {
        _portalType = portalType;
        String loginInfo = Utils.getClientIP() + "; UserAgent=" + Utils.getUserAgent() + "; targetServer=" + EnvironmentInfo.
                getLocalServerName();
        int id = getId(token);
        _account = _accountFacade.findAccount(id);
        if (_account == null) {
            logMessage("Login by token failed: " + loginInfo);
            _featureHolder.clear();
        } else {
            logMessage("Login by token successful: " + loginInfo);
            initFeatures();
            configureSessionTimeout();
        }

        return _account != null;
    }

    public boolean loginAndSetTopics(String mailOrUser, String password, PortalType portalType) {
        String loginInfo = Utils.getClientIP() + "; UserAgent=" + Utils.getUserAgent() + "; targetServer=" + EnvironmentInfo.
                getLocalServerName();
        if (!login(mailOrUser, password, loginInfo, portalType)) {
            return false;
        }

        configureSessionTimeout();
        return true;
    }

    private void configureSessionTimeout() {
        if (_account == null) {
            return;
        }
        int sessionTimeout = (_portalType == PortalType.CALC
                || _portalType == PortalType.CERT
                || _portalType == PortalType.BASE) ? Const.SECONDS_PER_HOUR : Const.SECONDS_PER_HOUR / 2;
        sessionTimeout = (_account.getEmail().toLowerCase().endsWith("@inek-drg.de")
                && isInternalClient()) ? 2 * Const.SECONDS_PER_HOUR : sessionTimeout; // session timeout extended to 4 hour for internal user
        FacesContext.getCurrentInstance().getExternalContext().setSessionMaxInactiveInterval(sessionTimeout);

    }

    /**
     * General login function. Will be used from UploadServlet (DatenDienst) too. Thus, perform login and initFeatures
     * only.
     *
     * @param mailOrUser
     * @param password
     * @param loginInfo  An infostring|mes
     * @param portalType to be displayed
     *
     * @return
     */
    public boolean login(String mailOrUser, String password, String loginInfo, PortalType portalType) {
        if (_featureHolder == null) {
            invalidateSession();
            _account = null;
            return false;
        }
        _portalType = portalType;
        _account = _accountFacade.getAccount(mailOrUser, password);
        if (_account == null) {
            logMessage("Login failed: " + loginInfo);
            return false;
        }
        initFeatures();
        logMessage((_account != null ? "Login successful: " : "Login failed: ") + loginInfo);
        return _account != null;
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
            return version < 9;
        } catch (NumberFormatException ex) {
            return true;
        }
    }

    private void initFeatures() {
        _featureHolder.clear();
        if (_account == null) {
            return;
        }

        addAdminIfNeeded();
        addMissingFeatures();

        List<AccountFeature> accountFatures = _account.getFeatures();
        accountFatures
                .stream()
                .filter(f -> featureIsValid(f))
                .sorted((f1, f2) -> f1.getSequence() + (belongsToCurrentPortal(f1.getFeature()) ? 0 : 100)
                - f2.getSequence() - (belongsToCurrentPortal(f2.getFeature()) ? 0 : 100))
                .forEach(accFeature -> {
                    Feature feature = accFeature.getFeature();
                    if (belongsToCurrentPortal(feature)) {
                        _featureHolder.add(feature, this);
                    } else {
                        _featureHolder.addIfMissing(feature.getPortalType());
                    }

                });

    }

    private boolean belongsToCurrentPortal(Feature feature) {
        return feature.getPortalType() == _portalType;
    }

    private void addMissingFeatures() {
        _account = addFeatureIfMissing(_account, Feature.USER_MAINTENANCE);
        _account = addFeatureIfMissing(_account, Feature.DOCUMENTS);
        if (_coopFacade.getOpenCooperationRequestCount(_account.getId()) > 0) {
            _account = addFeatureIfMissing(_account, Feature.COOPERATION);
        }
    }

    private Account addFeatureIfMissing(Account account, Feature feature) {
        boolean featureExists = account.getFeatures().stream().anyMatch(f -> f.getFeature() == feature);
        if (featureExists) {
            return account;
        }
        return addFeature(account, feature);
    }

    private Account addFeature(Account account, Feature feature) {
        AccountFeature accFeature = new AccountFeature();
        accFeature.setFeature(feature);
        FeatureState state = feature.getNeedsApproval() ? FeatureState.NEW : FeatureState.SIMPLE;
        accFeature.setFeatureState(state);
        accFeature.setSequence(account.getFeatures().size());
        account.getFeatures().add(accFeature);
        return _accountFacade.updateAccount(account);

    }

    private void addAdminIfNeeded() {
        if (isInekUser(Feature.ADMIN)) {
            if (_portalType == PortalType.ADMIN) {
                _featureHolder.add(Feature.ADMIN, this);
            } else {
                _featureHolder.addIfMissing(PortalType.ADMIN);
            }
        }
        if (_portalType == PortalType.BASE && _account.getAdminIks().size() > 0) {
            _featureHolder.add(Feature.IK_ADMIN, this);
        }
    }

    private boolean featureIsValid(AccountFeature accFeature) {
        Feature feature = accFeature.getFeature();

        return _appTools.isFeatureEnabled(feature)
                && (accFeature.getFeatureState() == FeatureState.SIMPLE
                || accFeature.getFeatureState() == FeatureState.APPROVED);
    }

    public void saveAccount() {
        _account = _accountFacade.updateAccount(_account);
        initFeatures();
    }

    public void deleteAccount() {
        _accountFacade.deleteAccount(_account);
    }

    public String getUser() {
        if (_account == null) {
            return "???";
        }
        return _account.getFirstName() + " " + _account.getLastName();
    }

    /**
     * @param requestedFeature
     *
     * @return true, if the current user is within any InEK role for the requested feature
     */
    public boolean isInekUser(Feature requestedFeature) {
        return isInekUser(requestedFeature, false);
    }

    public boolean isInekUser(String featureName) {
        try {
            Feature requestedFeature = Feature.valueOf(featureName);
            return isInekUser(requestedFeature, false);
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Unknown Feature: " + featureName);
            return false;
        }
    }

    /**
     *
     * @param requestedFeature
     * @param needsWriteAccess
     *
     * @return true, if the current user is within any InEK role for the requested feature and either has write access
     *         enabled or no write access is requested
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

    public List<String> getParts() {
        return _featureHolder.getParts();
    }

    public IFeatureController getFeatureController(Feature feature) {
        return _featureHolder.getFeatureController(feature);
    }

    public boolean hasNoFeatureSubscribed() {
        return _featureHolder.hasNoFeatureSubscribed();
    }

    public boolean isMyAccount(int accountId) {
        return isMyAccount(accountId, true);
    }

    public boolean isMyAccount(int accountId, boolean log) {
        if (_account.getId() == accountId) {
            return true;
        }
        if (log) {
            LOGGER.log(Level.WARNING, "Account {0} tried to access object from account {1}",
                    new Object[]{_account.getId(), accountId});
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

    /**
     * This methods is used in an onclick event of a facelets page to add a configurable confirmation behavior.
     *
     * @param key
     *
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
            return "intern/InEK-DatenportalIntern.pdf";
        }
        return "InEK-Datenportal.pdf";
    }

    private final Set<String> _acceptedTerms = new HashSet<>(4);

    public void acceptTermsOfUse(String name) {
        _acceptedTerms.add(name);
        logMessage("Nutzungsbedingungen akzeptiert: " + name);
    }

    public boolean isTermsOfUseAccepted(String name) {
        return _acceptedTerms.contains(name);
    }

    public void hideData(boolean enabled) {
        if (!enabled) {
            String msg = "Sie haben gerade einen Bereich, der möglicherweise Daten enthält, ausgeblendet. "
                    + "Sofern dieser Daten enthält, bleiben diese vorerst erhalten, "
                    + "so dass diese zur Verfügung stehen, wenn Sie den Bereich wieder aktivieren. "
                    + "Sobald Sie die Daten an das InEK senden, werden diese bereinigt.";
            //Utils.showMessageInBrowser(msg);
            setScript("alert('" + msg + "');");
        }
    }

    public String getCss() {
        switch (_portalType) {
            case PSY:
                return "psyportal.css";
            case DRG:
                return "drgportal.css";
            case ADMIN:
                return "adminportal.css";
            case INSURANCE:
                return "insuranceportal.css";
            case CALC:
                return "calcportal.css";
            default:
                return "commonportal.css";
        }
    }

}
