/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.login;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.entities.AccountRequest;
import org.inek.dataportal.backingbeans.SessionTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.AccountFacade;
import org.inek.dataportal.facades.AccountRequestFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.faceletvalidators.EmailValidator;
import org.inek.dataportal.helper.faceletvalidators.NameValidator;

/**
 *
 * @author muellermi
 */
@Named
@ConversationScoped
public class Register implements Serializable {

    private boolean _approved;
    private String _password;
    private String _repeatPassword;
    private String _repeatEmail;
    private AccountRequest _account;
    @Inject private SessionTools _sessionTools;
    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;
    @Inject private AccountRequestFacade _accountRequestFacade;

    public Register() {
        System.out.println("create register");
    }

    @PostConstruct
    private void init() {
        _account = new AccountRequest();
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public AccountRequest getAccountRequest() {
        return _account;
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
        if (_accountFacade.existsMailOrUser(input) || _accountRequestFacade.findByMailOrUser(input) != null) {
            String msg = Utils.getMessage("msgUserExists");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void checkEmail(FacesContext context, UIComponent component, Object value) {
        String input = "" + value;
        if (!EmailValidator.isValidEmail(input)) {
            String msg = Utils.getMessage("msgNoEmail");
            throw new ValidatorException(new FacesMessage(msg));
        }
        if (_accountFacade.existsMailOrUser(input) || _accountRequestFacade.findByMailOrUser(input) != null) {
            String msg = Utils.getMessage("msgUserExists");
            throw new ValidatorException(new FacesMessage(msg));
        }
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
        return _sessionTools.isHospital(_account.getCustomerTypeId()) ? "true" : "false";
    }

    public void checkApproval(FacesContext context, UIComponent component, Object value) {
        if (!(Boolean) value) {
            String msg = Utils.getMessage("missingAgreementMsg");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public String register() {
        _account.setPassword(_password);
        if (!_accountRequestFacade.createAccountRequest(_account)) {
            Utils.showMessageInBrowser(Utils.getMessage("errProcessing"));
            return "";
        }
        _sessionController.endConversation();
        return Pages.LoginFinishRegister.URL();
    }

    public String[] getFinishRegisterText() {
        String[] strings = Utils.getMessage("msgFinishRegister").split("\\n");
        return strings;
    }
}
