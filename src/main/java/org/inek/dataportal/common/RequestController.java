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
     * @param e 
     */
    public void forceLoginIfNotLoggedIn(ComponentSystemEvent e) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        String viewId = facesContext.getViewRoot().getViewId();
        if (viewId.equals(Pages.SessionTimeoutRedirector.URL())) {
            // The template displays the logged in user. This view will be created _before_ tryLogout is called.
            // To ensure the user is logged-out when displaying error or timeout, 
            // we first call these redirectors, log out and redirect to the target pages
            tryLogout();
            facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.SessionTimeout.URL());
            return;
        }
        if(viewId.equals(Pages.InvalidConversation.URL())) {
            tryLogout();
            facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.InvalidConversation.URL());
            return;
        }
        if (viewId.equals(Pages.ErrorRedirector.URL())) {
            tryLogout();
            facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.Error.URL());
            return;
        }
        if (viewId.equals(Pages.Error.URL()) || viewId.equals(Pages.SessionTimeout.URL())) {
            tryLogout();
            return;
        }
        if (viewId.startsWith("/login/") || _sessionController.isLoggedIn()) {
            // either pages are allowed without being logged in or the user is logged in
            return;
        }
        _sessionController.logMessage("Force to login. From " + viewId);
        facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.Login.URL());
    }

    private void tryLogout() {
        if (_sessionController != null) {
            _sessionController.logout();
        }
    }
    
        public void forceLoginIfNotInternal(ComponentSystemEvent e) {
            if (_sessionController.isInternalClient()){return;}
            _logger.log(Level.WARNING, "Attempt to call admin page from ip: {0}", Utils.getClientIP());
            FacesContext facesContext = FacesContext.getCurrentInstance();
            tryLogout();
            facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, Pages.Error.URL());
        }
}
