/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.common.login;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.data.account.entities.AccountRequest;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.account.facade.AccountRequestFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.faceletvalidators.NameValidator;
import org.inek.dataportal.common.overall.SessionTools;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class Register implements Serializable {

    private boolean _approved;
    private String _password;
    private String _repeatPassword;
    private String _repeatEmail;
    private AccountRequest _accountRequest;
    @Inject private SessionTools _sessionTools;
    @Inject private AccountFacade _accountFacade;
    @Inject private AccountRequestFacade _accountRequestFacade;

    public Register() {
        System.out.println("create register");
    }

    @PostConstruct
    private void init() {
        _accountRequest = new AccountRequest();
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public AccountRequest getAccountRequest() {
        return _accountRequest;
    }

    public boolean isApproved() {
        return _approved;
    }

    public void setApproved(boolean approved) {
        this._approved = approved;
    }

    public String getRepeatEmail() {
        return _repeatEmail;
    }

    public void setRepeatEmail(String repeatEmail) {
        this._repeatEmail = repeatEmail;
    }

    public String getPassword() {
        return _password;
    }

    public void setPassword(String password) {
        this._password = password;
    }

    public String getRepeatPassword() {
        return _repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this._repeatPassword = repeatPassword;
    }

    // </editor-fold>
    public void checkUser(FacesContext context, UIComponent component, Object value) {
        String input = "" + value;
        if (!NameValidator.isValidName(input)) {
            String msg = Utils.getMessage("msgInvalidCharacters");
            throw new ValidatorException(new FacesMessage(msg));
        }
        if (_accountFacade.existsMailOrUser(input) 
                || _accountRequestFacade.findByMailOrUser(input) != null 
                || input.toLowerCase().equals("supervisor")) {
            String msg = Utils.getMessage("msgUserExists");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void checkEmail(FacesContext context, UIComponent component, Object value) {
        String input = "" + value;
        if (!_sessionTools.isValidNonTrashEmail(input)) {
            String msg = Utils.getMessage("msgNoEmail");
            throw new ValidatorException(new FacesMessage(msg));
        }
        
// don't give any hint of registered users (except nick name as above)
//        if (_accountFacade.existsMailOrUser(input) || _accountRequestFacade.findByMailOrUser(input) != null) {
//            String msg = Utils.getMessage("msgUserExists");
//            throw new ValidatorException(new FacesMessage(msg));
//        }
    }

    public void checkRepeatEmail(FacesContext context, UIComponent component, Object value) {
        checkEmail(context, component, value);
        UIViewRoot root = FacesContext.getCurrentInstance().getViewRoot();
        String targetId = component.getNamingContainer().getClientId() + ":email";
        Object email = ((HtmlInputText) root.findComponent(targetId)).getValue();
        if (email != null && !email.equals("" + value)) {
            String msg = Utils.getMessage("msgEmailMismatch");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void checkPassword(FacesContext context, UIComponent component, Object value) {
        Utils.checkPassword(context, component, value);
    }

    /**
     * IK is required for hospitals
     *
     * @return
     */
    public String getIkRequired() {
        return _sessionTools.isHospital(_accountRequest.getCustomerTypeId()) ? "true" : "false";
    }

    public void checkApproval(FacesContext context, UIComponent component, Object value) {
        if (!(Boolean) value) {
            String msg = Utils.getMessage("missingAgreementMsg");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public String register() {
        if(_accountRequestFacade.accountRequestExists(_accountRequest.getEmail(), _accountRequest.getUser())) {
            return Pages.LoginFinishRegister.URL();
        }
        
        if (!_accountFacade.isReRegister(_accountRequest.getEmail())){
            _accountRequest.setPassword(_password);
            if (!_accountRequestFacade.createAccountRequest(_accountRequest)) {
                Utils.showMessageInBrowser(Utils.getMessage("errProcessing"));
                return "";
            }
        }        
        return Pages.LoginFinishRegister.URL();
    }

    public String[] getFinishRegisterText() {
        String[] strings = Utils.getMessage("msgFinishRegister").split("\\n");
        return strings;
    }

}
