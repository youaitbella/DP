/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.login;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class ActivateMail implements Serializable{
    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;
    private String _email;
    private String _password;
    private String _key;


    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        _password = password;
    }

    public String getKey() {
        return _key;
    }

    public void setKey(String key) {
        this._key = key;
    }

    // </editor-fold>

    public String activateAndLogin(){
        if (!_accountFacade.activateMail(_email, _password, _key)){
            return null;
        }
        if (!_sessionController.loginAndSetTopics(_email, _password)){
            return null;
        }
        return Pages.MainApp.URL();
    }


}
