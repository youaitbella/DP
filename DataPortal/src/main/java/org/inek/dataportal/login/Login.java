/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.login;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIInput;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.Quality;
import org.inek.dataportal.utils.SecurePassword;

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

    public String login() {
        if (_sessionController.isInMaintenanceMode()) {
            _sessionController.alertClient("Aufgrund von Wartungsarbeiten ist derzeit kein Zugang möglich");
            return "";
        }
        if (!_sessionController.loginAndSetTopics(_emailOrUser, _password)) {
            _loginMessage = "Name bzw. Email und / oder Kennwort sind ungültig";
            return "";
        }
        _loginMessage = "";
        return _sessionController.countInstalledFeatures() <= 1 ? Pages.UserMaintenanceFeatures.URL() : Pages.MainApp.URL();
    }

}
