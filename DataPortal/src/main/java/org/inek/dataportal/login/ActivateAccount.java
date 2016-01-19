/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.login;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountActivation;
import org.inek.dataportal.entities.account.AccountPwd;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountActivationFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.account.AccountPwdFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.Crypt;

/**
 *
 * @author muellermi
 */
@Named
@SessionScoped
public class ActivateAccount implements Serializable {
    
    private String _email;
    private String _guid;
    private String _pw;
    private String _pwRepeat;
    private AccountActivation _accActiv;
    private Account _account;
    private AccountPwd _accPwd;
    @Inject private AccountActivationFacade _aaFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accFacade;
    @Inject private AccountPwdFacade _pwdFacade;
    
    public ActivateAccount() {
        HttpServletRequest r = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        _guid = r.getParameter("guid");
    }
    
    public void comparePasswords(FacesContext fc, UIComponent ui, Object value) {
//        String rPw = value.toString();
//        if(!rPw.equals(_pw)) {
//            throw new ValidationException(Utils.getMessage("msg"));
//        }0
        Utils.checkPassword(fc, ui, value);
    }
    
    public String activateAndLogin() {
        _accActiv = _aaFacade.findAccountByGUID(_guid);
        if(_accActiv == null) {
            _sessionController.setScript("alert ('"+Utils.getMessage("errGuidAccId")+"');");
            return ""; 
            /* Account-ID wird nirgends übergeben. 
            *  Die Fehlermeldung dient nur zum "Verwirren" von Try-and-Error versuchen.
            */
        }
        _account = _accFacade.find(_accActiv.getAccountId());
        if(_account == null) {
            _sessionController.setScript("alert ('"+Utils.getMessage("errAccountNotExists")+"');");
            return "";
            /* Der Account existiert nicht, der in der AccountActivation-Tabelle mit der angegebenen GUID verknüpft ist. */
        }
        _email = _account.getEmail().replace("@nub#", "");
        _account.setEmail(_email);
        _account.setDeactivated(false);
        _accFacade.merge(_account);
        _accPwd = _pwdFacade.find(_account.getId());
        _accPwd.setPasswordHash(Crypt.getPasswordHash(_pw, _account.getId().intValue()));
        _pwdFacade.merge(_accPwd);
        _aaFacade.remove(_accActiv);
        if(_sessionController.loginAndSetTopics(_email, _pw)) {
            return _sessionController.countInstalledFeatures() <= 1 ? Pages.UserMaintenanceFeatures.URL() : Pages.MainApp.URL();
        }
        _sessionController.setScript("alert ('"+Utils.getMessage("errProcessing")+"');");
        return "";
    }

    public String getGuid() {
        return _guid;
    }

    public void setGuid(String guid) {
        this._guid = guid;
    }

    public String getPw() {
        return _pw;
    }

    public void setPw(String pw) {
        this._pw = pw;
    }

    public String getPwRepeat() {
        return _pwRepeat;
    }

    public void setPwRepeat(String pwRepeat) {
        this._pwRepeat = pwRepeat;
    }
}
