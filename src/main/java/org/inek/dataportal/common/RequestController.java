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
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class RequestController implements Serializable {

    private static final Logger _logger = Logger.getLogger("RequestController");

    @Inject private SessionController _sessionController;

    /**
     * This method should be called by every page during
     *
     * @param e
     */
    public void forceLoginIfNotLoggedIn(ComponentSystemEvent e) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String viewId = facesContext.getViewRoot().getViewId();
        if (viewId.equals(Pages.SessionTimeoutRedirector.URL())) {
            // The template displays the logged in user. This view will be created _before_ tryLogout is called.
            // To ensure the user is logged-out when displaying error or timeout,
            // we first call these redirectors, log out and redirect to the target pages
            tryLogout(viewId);
            facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.SessionTimeout.URL());
            return;
        }
        if (viewId.equals(Pages.ErrorRedirector.URL())) {
            tryLogout(viewId);
            facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.Error.URL());
            return;
        }
        if (viewId.equals(Pages.DataErrorRedirector.URL())) {
            tryLogout(viewId);
            facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.DataError.URL());
            return;
        }
        if (viewId.equals(Pages.NotAllowed.URL())) {
            tryLogout(viewId);
            String url = (String) facesContext.getExternalContext().getRequestMap().get(RequestDispatcher.ERROR_REQUEST_URI);
            if (!url.endsWith("/favicon.ico") && !url.endsWith("/wpad.dat") && !Utils.getClientIP().startsWith("192.168.0.")) {
                // log, if none of the well known accesses
                _sessionController.logMessage("Invald access: URL:" + url + "; IP=" + Utils.getClientIP());
            }
            Utils.sleep(500);  // force client to wait a bit
            return;
        }
        if (viewId.equals(Pages.Error.URL())
                || viewId.equals(Pages.InvalidConversation.URL())
                || viewId.equals(Pages.SessionTimeout.URL())
                || viewId.equals(Pages.DoubleWindow.URL())
                || viewId.equals(Pages.NotAllowed.URL())) {
            tryLogout(viewId);
            return;
        }
        if (viewId.startsWith("/login/") || _sessionController.isLoggedIn()) {
            // either pages are allowed without being logged in or the user is logged in
            return;
        }
        _sessionController.logMessage("Force to login: IP=" + Utils.getClientIP() + "; FromView=" + viewId);
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.Login.URL());
    }

    private void tryLogout(String message) {
        if (_sessionController != null) {
            _sessionController.logout(message);
        }
    }

    public void forceLoginIfNotInternal(ComponentSystemEvent e) {
        if (_sessionController.isInternalClient()) {
            return;
        }
        _logger.log(Level.WARNING, "Attempt to call admin page from ip: {0}", Utils.getClientIP());
        FacesContext facesContext = FacesContext.getCurrentInstance();
        tryLogout("Attempt to call admin page from ip: " + Utils.getClientIP());
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.Error.URL());
    }

}
