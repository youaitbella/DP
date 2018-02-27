package org.inek.dataportal.login;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.PortalType;
import org.inek.dataportal.facades.account.AccountFacade;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class ActivatePassword implements Serializable{
    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;
    private String _emailOrUser;
    private String _password;
    private String _key;

    /** Creates a new instance of Activate */
    public ActivatePassword() {
        HttpServletRequest r = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        _emailOrUser = r.getParameter("mail");
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

    public String activateAndLogin(){
        if (!_accountFacade.activatePassword(_emailOrUser, _password, _key)){
            _sessionController.alertClient("Die eingegeben Informationen konnten nicht verifizert werden. Bitte "
                                           + "überprüfen Sie Ihre Eingaben und versuchen Sie es erneut. ");
            return null;
        }
        if (!_sessionController.loginAndSetTopics(_emailOrUser, _password,PortalType.DRG)){
            return null;
        }
        return _sessionController.countInstalledFeatures() <= 1 ? Pages.UserMaintenanceFeatures.URL() :  Pages.MainApp.URL();
    }


}
