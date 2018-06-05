/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.login;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.api.enums.PortalType;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class Login implements Serializable {

    public Login() {
        //System.out.println("ctor Login");
    }

    @PostConstruct
    private void init() {
        //_sessionController.logout();
    }

    // <editor-fold defaultstate="collapsed" desc="Property EmailOrUser">
    private String _emailOrUser;

    public String getEmailOrUser() {
        return _emailOrUser;
    }

    public void setEmailOrUser(String email) {
        _emailOrUser = email;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Password">
    private String _password;

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LoginMessage">
    private String _loginMessage = "";

    public String getLoginMessage() {
        return _loginMessage;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SessionController">
    @Inject
    private SessionController _sessionController;

    public SessionController getSessionController() {
        return _sessionController;
    }

    public void setSessionController(SessionController sessionController) {
        _sessionController = sessionController;
    }
    // </editor-fold>

    public String login(PortalType portalType) {
        if (!_sessionController.loginAndSetTopics(_emailOrUser, _password, portalType)) {
            _loginMessage = "Name bzw. Email und / oder Kennwort sind ungültig";
            return "";
        }
        _loginMessage = "";
        // if no feature subscribed
        return _sessionController.hasNoFeatureSubscribed() 
                ? Pages.UserMaintenanceFeatures.RedirectURL() 
                : Pages.MainApp.RedirectURL();
    }

}
