/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Utils;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class RequestController implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("RequestController");

    @Inject private SessionController _sessionController;

    /**
     * This method should be called by every page during
     *
     * @param e
     */
    public void forceLoginIfNotLoggedIn(ComponentSystemEvent e) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String viewId = facesContext.getViewRoot().getViewId();
        if (loginByToken(facesContext)) {
            facesContext.getApplication().getNavigationHandler()
                    .handleNavigation(facesContext, null, viewId + "?faces-redirect=true");
            return;
        }
        if (_sessionController.isLoggedIn()) {
            return;
        }
        _sessionController.logMessage("Force to login: IP=" + Utils.getClientIP() + "; FromView=" + viewId);
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.Login.URL());
    }

    private boolean loginByToken(FacesContext facesContext) {
        String token = facesContext.getExternalContext().getRequestParameterMap().get("token");
        if (token == null) {
            return false;
        }
        try {
            return _sessionController.loginByToken(token);
        } catch (Exception ex) {
            return false;
        }
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
