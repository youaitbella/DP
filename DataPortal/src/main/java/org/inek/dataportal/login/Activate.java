/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.login;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class Activate implements Serializable {

    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;

    protected static final Logger _logger = Logger.getLogger("Activate");

    private String _emailOrUser;
    private String _password;
    private String _key;

    /**
     * Creates a new instance of Activate
     */
    public Activate() {
        HttpServletRequest r = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        _emailOrUser = r.getParameter("user");
        _key = r.getParameter("key");
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
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
     * @return the _key
     */
    public String getKey() {
        return _key;
    }

    /**
     * @param key the _key to set
     */
    public void setKey(String key) {
        this._key = key;
    }

    // </editor-fold>
    public String activateAndLogin() {
        if (!_accountFacade.activateAccount(_emailOrUser, _password, _key)) {
            _logger.log(Level.WARNING, "Activation failed: {0}", _emailOrUser);
            return null;
        }
        if (!_sessionController.loginAndSetTopics(_emailOrUser, _password)) {
            _logger.log(Level.WARNING, "Login and set topics failed: {0}", _emailOrUser);
            return null;
        }
        _logger.log(Level.INFO, "Activation successful: {0}", _emailOrUser);
        if (_sessionController.countInstalledFeatures() <= 1) {
            _sessionController.setCurrentTopicByUrl(Pages.UserMaintenanceMasterData.URL());
            return Pages.UserMaintenanceFeatures.URL();
        }
        return Pages.MainApp.URL();
    }

}
