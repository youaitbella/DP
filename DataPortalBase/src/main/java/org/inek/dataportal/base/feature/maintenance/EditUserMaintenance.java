package org.inek.dataportal.base.feature.maintenance;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.FeatureState;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountFeature;
import org.inek.dataportal.common.data.account.facade.AccountChangeMailFacade;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.account.facade.AccountPwdFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.common.data.ikadmin.entity.AccountResponsibility;
import org.inek.dataportal.common.data.ikadmin.entity.IkAdmin;
import org.inek.dataportal.common.data.ikadmin.facade.IkAdminFacade;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.faceletvalidators.NameValidator;
import org.inek.dataportal.common.helper.AccessRightHelper;
import org.inek.dataportal.common.helper.MailTemplateHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.overall.SessionTools;
import org.inek.dataportal.common.scope.FeatureScoped;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;
import java.util.logging.Logger;

/**
 * @author muellermi
 */
@Named
@FeatureScoped(name = "UserMaintenance")
public class EditUserMaintenance extends AbstractEditController {

    private static final Logger LOGGER = Logger.getLogger("EditUserMaintenance");
    // todo: reduce injection by combining facades. Next replace field injection by constructor injection
    @Inject
    private ApplicationTools _appTools;
    @Inject
    private SessionTools _sessionTools;
    @Inject
    private SessionController _sessionController;
    @Inject
    private AccountFacade _accountFacade;
    @Inject
    private AccountPwdFacade _accountPwdFacade;
    @Inject
    private AccountChangeMailFacade _accountChangeMailFacade;
    @Inject
    private IkAdminFacade _ikAdminFacade;
    @Inject
    private CustomerFacade _customerFacade;
    private String _user;
    private String _email;
    private Account _account;
    private String _oldPassword;
    private String _newPassword;
    private String _repeatPassword;
    private List<Integer> _iksNotAllowedForDelete = new ArrayList<>();

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Account getAccount() {
        return _account;
    }
    // </editor-fold>

    public String getUser() {
        return _user;
    }

    public void setUser(String user) {
        this._user = user;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        this._email = email;
    }

    public String getOldPassword() {
        return _oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this._oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return _newPassword;
    }

    public void setNewPassword(String newPassword) {
        this._newPassword = newPassword;
    }

    public String getRepeatPassword() {
        return _repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this._repeatPassword = repeatPassword;
    }

    // </editor-fold>
    @PostConstruct
    private void init() {
        initOrResetData();
    }

    private void initOrResetData() {
        _account = _accountFacade.findAccount(_sessionController.getAccount().getId());
        _user = _account.getUser();
        _oldPassword = "";
        _newPassword = "";
        _repeatPassword = "";
        setUsedIks();
        addTopicResponsibility();
    }

    @Override
    protected void addTopics() {
        addTopic(UserMaintenaceTabs.tabUMMaster.name(), Pages.UserMaintenanceMasterData.URL());
        addTopic(UserMaintenaceTabs.tabUMFeatures.name(), Pages.UserMaintenanceFeatures.URL());
        addTopic(UserMaintenaceTabs.tabUMOther.name(), Pages.UserMaintenanceOther.URL());
    }

    /**
     * This action will change the tab. It may prevent changing if the data has changed.
     *
     * @param newTopic
     */
    @Override
    public void changeTab(String newTopic) {
        if (getActiveTopic().getKey().equals(newTopic)) {
            return;
        }
        setActiveTopic(newTopic);
    }

    private void addTopicResponsibility(){
        if(_account.getResponsibleForIks().size() > 0){
            addTopic(UserMaintenaceTabs.tabUMResponsibility.name(), Pages.UserMaintenanceResponsibility.URL());
        }
    }
    
    private void setUsedIks() {
        for (int ik : _account.getFullIkSet()) {
            if (!_accountFacade.deleteIkAllowed(ik, _account)) {
                _iksNotAllowedForDelete.add(ik);
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Part UserMaintenance">
    public void checkUser(FacesContext context, UIComponent component, Object value) {
        String input = "" + value;
        if (!NameValidator.isValidName(input)) {
            String msg = Utils.getMessage("msgInvalidCharacters");
            throw new ValidatorException(new FacesMessage(msg));
        }
        if (!input.equals(getAccount().getUser()) && _accountFacade.existsMailOrUser(input)) {
            String msg = Utils.getMessage("msgUserExists");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void checkEmail(FacesContext context, UIComponent component, Object value) {
        String input = ("" + value).trim();
        if (input.isEmpty()) {
            // no check for empty mail address
            return;
        }
        if (!_sessionTools.isValidNonTrashEmail(input)) {
            String msg = Utils.getMessage("msgNoEmail");
            throw new ValidatorException(new FacesMessage(msg));
        }
        if (!input.equals(getAccount().getEmail()) && _accountFacade.existsMailOrUser(input)) {
            String msg = Utils.getMessage("msgUserExists");
            throw new ValidatorException(new FacesMessage(msg));
        }
    }

    public void addNewIK() {
        _account.addIk(0);
    }
    // </editor-fold>

    public void removeIK(int ik) {
        _account.removeIk(ik);
    }

    private AccountFeature createAccountFeature(Feature feature) {
        AccountFeature accFeature = new AccountFeature();
        accFeature.setFeature(feature);
        accFeature.setFeatureState(FeatureState.NEW);
        accFeature.setSequence(0);
        return accFeature;
    }

    public void reorderFeatures() {
        for (int i = 0; i < _account.getFeatures().size(); i++) {
            _account.getFeatures().get(i).setSequence(i);
        }
    }

    public void checkPassword(FacesContext context, UIComponent component, Object value) {
        Utils.checkPassword(context, component, value);
    }

    public String delete() {
        _sessionController.deleteAccount();
        return Pages.Login.URL();
    }

    public boolean deleteAllowed() {
        return _account.getAdminIks().isEmpty();
    }

    public String save() {
        if (_sessionTools.isHospital(_account.getCustomerTypeId()) && _account.getFullIkSet().isEmpty()) {
            DialogController.showWarningDialog("Bitte mindestens eine IK eingeben", "Fehler beim Speichern");
        } else {
            checkIKAdminRights(_account);
            _account.setUser(_user);
            _account = _accountFacade.updateAccount(_account);
            _sessionController.refreshAccount(_account.getId());
            DialogController.showSaveDialog();
        }
        return "";
    }

    public List<Feature> getAvailableFeatures() {
        List<Feature> newFeatures = new ArrayList<>();
        for (Feature feature : Feature.values()) {
            if (_account.getFeatures().stream().anyMatch(c -> c.getFeature() == feature)) {
                continue;
            }
            if (feature.isSelectable() && _appTools.isFeatureEnabled(feature)) {
                newFeatures.add(feature);
            }
        }
        return newFeatures;
    }

    public void removeAccountFeature(AccountFeature feature) {
        _account.removeAccountFeature(feature);
    }

    public void addFeature(Feature feature) {
        AccountFeature af = createAccountFeature(feature);
        _account.getFeatures().add(af);
        DialogController.showInfoDialog("Eingabe bestätigen",
                "Bitte bestätigen Sie Ihre Eingabe über den Speicherbutton.");
    }

    public String saveFeatures() {
        try {
            _account = _accountFacade.updateAccount(_account);
            _sessionController.refreshAccount(_account.getId());
            DialogController.showSaveDialog();
        } catch (Exception ex) {
            DialogController.showInfoMessage("Fehler beim Speichern: " + ex.getMessage());
        }
        return "";
    }

    public String savePassword() {
        return updatePassword();
    }

    public String saveEmail() {
        return updateMail();
    }

    public boolean getFeatureNub() {
        List<AccountFeature> list = _account.getFeatures();
        for (AccountFeature ft : list) {
            if (ft.getFeature() == Feature.NUB) {
                return true;
            }
        }
        return false;
    }

    public boolean getFeatureCoop() {
        List<AccountFeature> list = _account.getFeatures();
        for (AccountFeature ft : list) {
            if (ft.getFeature() == Feature.COOPERATION) {
                return true;
            }
        }
        return false;
    }

    private String updatePassword() {
        if (!validPwdInput()) {
            return Pages.UserMaintenanceOther.URL();
        }
        boolean success = _accountPwdFacade.changePassword(getAccount().getId(), _oldPassword, _newPassword);
        if (success) {
            DialogController.showInfoMessage(Utils.getMessage("msgPasswordChanged"));
        }
        return success ? "" : Pages.Error.URL();
    }

    private String updateMail() {
        if (Utils.isNullOrEmpty(getEmail())) {
            return Pages.UserMaintenanceOther.URL();
        }
        boolean success = _accountChangeMailFacade.changeMail(getAccount().getId(), getEmail());
        if (success) {
            DialogController.showInfoMessage(Utils.getMessage("msgMailChanged"));
        }
        return success ? "" : Pages.Error.URL();
    }

    private boolean validPwdInput() {
        String msg = "";
        if (_oldPassword.isEmpty()) {
            msg = "Das aktuelle Kennwort fehlt.";
        }
        if (_newPassword.isEmpty() && _repeatPassword.isEmpty()) {
            msg += (msg.isEmpty() ? "" : "\\r\\n") + "Ein neues Kennwort ist erforderlich.";
        } else if (!_newPassword.equals(_repeatPassword)) {
            msg += (msg.isEmpty() ? "" : "\\r\\n") + "Passwort und Wiederholung stimmen nicht überein.";
        } else if (!_accountPwdFacade.isCorrectPassword(_account.getId(), _oldPassword)) {
            msg += (msg.isEmpty() ? "" : "\\r\\n") + "Das alte Passwort stimmt nicht überein.";
        }
        if (!msg.isEmpty()) {
            DialogController.showWarningDialog(msg, "Falsche Eingabe");
        }
        return msg.isEmpty();
    }

    private void checkIKAdminRights(Account account) {
        for (int ik : account.getFullIkSet()) {
            Set<Integer> ikAdminIdsForMailing = new HashSet<>();
            List<IkAdmin> ikAdminsForIk = _ikAdminFacade.findIkAdminsForIk(ik);

            AccessRightHelper.ensureRightsForAccountFeature(account, ikAdminsForIk, ikAdminIdsForMailing, ik);
            AccessRightHelper.ensureFeatureAndRightsForIkAdminOnly(account, ikAdminsForIk, ikAdminIdsForMailing, ik);

            List<Account> ikAdminsAccountsForMailing = _accountFacade.getAccountsForIds(ikAdminIdsForMailing);
            if (ikAdminIdsForMailing.size() > 0) {
                notifyIkAdmins(ikAdminsAccountsForMailing, ik, account);
            }
        }
    }

    private void notifyIkAdmins(List<Account> ikAdmins, int ik, Account userAccount) {

        String user = userAccount.getFirstName() + " " + userAccount.getLastName() + " (" + userAccount.getCompany()
                + ", " + userAccount.getTown() + ")";

        for (Account admin : ikAdmins) {
            Mailer mailer = _sessionController.getMailer();
            MailTemplate template = mailer.getMailTemplate("IK-Admin: new user");

            MailTemplateHelper.setPlaceholderInTemplate(template, "{formalSalutation}", mailer.getFormalSalutation(admin));
            MailTemplateHelper.setPlaceholderInTemplate(template, "{user}", user);
            MailTemplateHelper.setPlaceholderInTemplate(template, "{ik}", String.valueOf(ik));

            Optional<IkAdmin> ikAdmin = admin.getAdminIks().stream().filter(c -> c.getIk() == ik).findFirst();
            ikAdmin.ifPresent(ikAdmin1 -> MailTemplateHelper.setPlaceholderInTemplate(template, "{features}", ikAdmin1.getConcateFeatures()));

            mailer.sendMailTemplate(template, admin.getEmail());
        }
    }

    public boolean removeIkAllowed(int ik) {
        return !_iksNotAllowedForDelete.contains(ik);
    }

    public String getCustomerName(int ik) {
        return _customerFacade.getCustomerByIK(ik).getName();
    }

    public String hasIkAdminText(int ik) {
        return (_ikAdminFacade.hasIkAdmin(ik)) ? getCustomerName(ik) + " (Funktionsbeauftragter vorhanden)" : getCustomerName(ik);
    }

    public Boolean isValidIk(int ik) {
        return _customerFacade.isValidIK("" + ik);
    }

    public void isIKValid(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        int ik = (Integer) value;
//        if (_account.getFullIkSet().contains(ik)){
//            throw new ValidatorException(new FacesMessage("IK bereits vorhanden"));
//        }
        if (!isValidIk(ik)) {
            throw new ValidatorException(new FacesMessage("Ungültige IK"));
        }
    }

    // todo: Do not copy and merge parts. Simply get a fresh copy from database to edit.
    // After save, replace the account object in sessionConctoller.
    // Refactor this class according to the common edit handling
    // <editor-fold defaultstate="collapsed" desc="fields">
    enum UserMaintenaceTabs {
        tabUMMaster,
        tabUMFeatures,
        tabUMOther,
        tabUMResponsibility,
    }
}
