/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.login;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.api.enums.PortalType;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.cooperation.facade.CooperationRequestEmailFacade;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class Activate implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;
    @Inject private CooperationRequestEmailFacade _coopRequestEmailFacade;

    protected static final Logger LOGGER = Logger.getLogger("Activate");

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
            LOGGER.log(Level.WARNING, "Activation failed: {0}", _emailOrUser);
            _sessionController.alertClient("Die eingegeben Informationen konnten nicht verifizert werden. Bitte "
                                           + "überprüfen Sie Ihre Eingaben und versuchen Sie es erneut. ");            
            return null;
        }
        Account userAcc = _accountFacade.findByMailOrUser(_emailOrUser);
        _coopRequestEmailFacade.createRealCooperationRequests(userAcc);
        
        if (!_sessionController.loginAndSetTopics(_emailOrUser, _password, PortalType.BASE)) {
            LOGGER.log(Level.WARNING, "Login and set topics failed: {0}", _emailOrUser);
            return null;
        }
        LOGGER.log(Level.INFO, "Activation successful: {0}", _emailOrUser);
        return Pages.MainApp.URL();
    }

}
