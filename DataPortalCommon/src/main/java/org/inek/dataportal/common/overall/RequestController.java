package org.inek.dataportal.common.overall;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.api.enums.PortalType;
import org.inek.dataportal.common.helper.EnvironmentInfo;
import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class RequestController implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("RequestController");
    private static final long serialVersionUID = 1L;
    @Inject private SessionController _sessionController;

    /**
     * This method should be called by every page during
     *
     * @param e
     */
    public void forceLoginIfNotLoggedIn(ComponentSystemEvent e) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        addCacheControlToResponse(facesContext);

        String viewId = facesContext.getViewRoot().getViewId();
        if (viewId.endsWith("/DataPrivacy.xhtml")) {
            return;
        }
        if (viewId.startsWith("/Login")) {
            handleLoginViews(facesContext);
            return;
        }
        if (loginByToken(facesContext)) {
            return;
        }
        if (_sessionController.isLoggedIn()) {
            return;
        }
        _sessionController.logMessage("Force to login: IP=" + Utils.getClientIP() + "; FromView=" + viewId);
        try {
            // this creates an illegalStateException: _sessionController.changePortal(PortalType.COMMON);
            String url = _sessionController.obtainTargetUrl(PortalType.COMMON);
            facesContext.getExternalContext().redirect(url);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private void addCacheControlToResponse(FacesContext facesContext) {
        if (facesContext.getPartialViewContext().isPartialRequest()) {
            return;
        }
        ExternalContext externalContext = facesContext.getExternalContext();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        if (!response.containsHeader("Content-Disposition")
                && !request.getRequestURI().contains(ResourceHandler.RESOURCE_IDENTIFIER)) { // Skip JSF resources (CSS/JS/Images/etc)
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            response.setDateHeader("Expires", 0); // Proxies.
        }
    }

    public void handleLoginViews(FacesContext facesContext) {
        String sessionId = facesContext.getExternalContext().getSessionId(false);
        if (sessionId == null) {
            facesContext.getExternalContext().getSessionId(true);
        }
    }

    private boolean loginByToken(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        String token = externalContext.getRequestParameterMap().get("token");
        String portal = externalContext.getRequestParameterMap().get("portal");
        String login = externalContext.getRequestParameterMap().get("login");

        if (token == null || portal == null) {
            return false;
        }
        try {
            String viewId = facesContext.getViewRoot().getViewId();
            if (!"true".equals(login)) {
                // if a user does not logout and an other user logs in using the same browser,
                // then the session of the former user might still be active.
                // Thus, instead of login by token, we invalidate the session.
                // Then we add the parameter "login=true" and perform a redirect 
                // This redirect is essential to perform the login into the new session and not to invalidate the session we just use.
                String url = EnvironmentInfo.getServerUrlWithContextpath() + viewId + "?token=" + token + "&portal=" + portal + "&login=true";
                externalContext.invalidateSession();
                FacesContext.getCurrentInstance().getExternalContext().redirect(url);
                return true;
            }
            if (_sessionController.loginByToken(token, PortalType.valueOf(portal))) {
                facesContext.getApplication().getNavigationHandler()
                        .handleNavigation(facesContext, null, viewId + "?faces-redirect=true");
                return true;
            }
            return false;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String getForceLogoutIfLoggedIn() {
        forceLogoutIfLoggedIn(null);
        return "";
    }

    public void forceLogoutIfLoggedIn(ComponentSystemEvent e) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String viewId = facesContext.getViewRoot().getViewId();
            tryLogout(viewId);
            if (!viewId.equals(Pages.NotAllowed.URL())) {
                return;
            }
            String url = (String) facesContext.getExternalContext().getRequestMap().
                    get(RequestDispatcher.ERROR_REQUEST_URI);
            if (url == null) {
                url = "";
            }
            if (Utils.getClientIP().startsWith("192.168.0.")) {
                return;
            }
            if (url.endsWith("/favicon.ico")) {
                return;
            }
            if (url.endsWith("/wpad.dat")) {
                return;
            }
            if (url.endsWith("/browserconfig.xml")) {
                return;
            }
            if (url.endsWith("/apple-touch-icon.png")) {
                return;
            }
            if (url.endsWith("/apple-touch-icon-precomposed.png")) {
                return;
            }
            // log, if none of the well known accesses
            _sessionController.logMessage("Invalid access: URL:" + url + "; IP=" + Utils.getClientIP());
        } catch (Exception ex) {
            _sessionController.logMessage("Invalid access: Error:" + ex.getMessage() + "; IP=" + Utils.getClientIP());
        }
        Utils.sleep(500);  // force client to wait a bit
    }

    private void tryLogout(String message) {
        if (_sessionController != null) {
            _sessionController.performLogout(message);
        }
    }

    public void forceLoginIfNotInternal(ComponentSystemEvent e) {
        if (_sessionController.isInternalClient()) {
            return;
        }
        LOGGER.log(Level.WARNING, "Attempt to call admin page from ip: {0}", Utils.getClientIP());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        tryLogout("Attempt to call admin page from ip: " + Utils.getClientIP());
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.Error.URL());
    }

}
