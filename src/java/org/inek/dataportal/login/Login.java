/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.login;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Pages;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class Login implements Serializable {

    @Inject
    private SessionController _sessionController;
    private String _emailOrUser;
    private String _password;
    private String _loginMessage = "";

    /**
     * Creates a new instance of Login
     */
    public Login() {
        //System.out.println("ctor Login");
    }

    @PostConstruct
    private void init() {
        //_sessionController.logout();
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public String getLoginMessage() {
        return _loginMessage;
    }

    public String getEmailOrUser() {
        return _emailOrUser;
    }

    public void setEmailOrUser(String email) {
        _emailOrUser = email;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

    /**
     * @return the sessionController
     */
    public SessionController getSessionController() {
        return _sessionController;
    }

    /**
     * @param sessionController the sessionController to set
     */
    public void setSessionController(SessionController sessionController) {
        this._sessionController = sessionController;
    }

    // </editor-fold>
    public String login() {
        if (!_sessionController.loginAndSetTopics(_emailOrUser, _password)) {
            _loginMessage = "Name bzw. Email und / oder Kennwort sind ungültig";
            return null;
        }
        _loginMessage = "";
        return _sessionController.countInstalledFeatures() <= 1 ? Pages.UserMaintenanceFeatures.URL() : Pages.MainApp.URL();
    }
}
