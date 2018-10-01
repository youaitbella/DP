/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.common.login;

import java.io.Serializable;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.SessionTools;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class RequestPassword implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject private AccountFacade _accountFacade;
    @Inject private SessionTools _sessionTools;
    private String _email;
    private String _password;
    private String _repeatPassword;

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

    public String getRepeatPassword() {
        return _repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this._repeatPassword = repeatPassword;
    }

    // </editor-fold>

    public void checkEmail(FacesContext context, UIComponent component, Object value) {
        String input = "" + value;
        if (!_sessionTools.isValidNonTrashEmail(input)) {
            String msg = Utils.getMessage("msgNoEmail");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void checkPassword(FacesContext context, UIComponent component, Object value) {
        Utils.checkPassword(context, component, value);
    }
    
    public String requestPassword() {
        if (!_accountFacade.requestPassword(_email, _password)) {
            //Utils.showMessageInBrowser(Utils.getMessage("errProcessing"));
            //return "";
        }
        return Pages.LoginFinishRequestPwd.URL();
    }

    public String[] getFinishRegisterText() {
        String[] strings = Utils.getMessage("msgFinishRequestPwd").split("\\n");
        return strings;
    }
    
}
