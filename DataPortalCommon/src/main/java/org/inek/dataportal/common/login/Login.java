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

    private static final long serialVersionUID = 1L;
    private static SessionController _sessionController;

    public Login() {
    }

    @Inject
    public Login(SessionController sessionController) {
        _sessionController = sessionController;
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

    // <editor-fold defaultstate="collapsed" desc="Property ScreenSize">
    private String _screenSize;

    public String getScreenSize() {
        return _screenSize;
    }

    public void setScreenSize(String screenSize) {
        _screenSize = screenSize;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LoginMessage">
    private String _loginMessage = "";

    public String getLoginMessage() {
        return _loginMessage;
    }
    // </editor-fold>

    public String login(PortalType portalType) {
        if (!_sessionController.loginAndSetTopics(_emailOrUser, _password, portalType, _screenSize)) {
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
