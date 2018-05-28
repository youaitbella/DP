package org.inek.dataportal.base.feature.maintenance;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.overall.SessionTools;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountIk;
import org.inek.dataportal.common.data.account.entities.AccountFeature;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.FeatureState;
import org.inek.dataportal.common.enums.IkReference;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.data.account.facade.AccountChangeMailFacade;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.account.facade.AccountPwdFacade;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.enums.Right;
import org.inek.dataportal.common.data.ikadmin.facade.IkAdminFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.faceletvalidators.NameValidator;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.mail.Mailer;
import org.primefaces.context.RequestContext;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "UserMaintenance")
public class EditUserMaintenance extends AbstractEditController {

    // todo: Do not copy and merge parts. Simply get a fresh copy from database to edit.
    // After save, replace the account object in sessionConctoller.
    // Refactor this class according to the common edit handling
    // <editor-fold defaultstate="collapsed" desc="fields">
    enum UserMaintenaceTabs {
        tabUMMaster,
        tabUMFeatures,
        tabUMOther,
    }
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
    private List<FeatureEditorDAO> _features;
    private String _oldPassword;
    private String _newPassword;
    private String _repeatPassword;
    private List<Integer> _iksNotAllowedForDelete = new ArrayList<>();
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Account getAccount() {
        return _account;
    }

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
        _features = null;
        _account = _accountFacade.findAccount(_sessionController.getAccount().getId());
        _user = _account.getUser();
        _oldPassword = "";
        _newPassword = "";
        _repeatPassword = "";
        setUsedIks();
        ensureFeatures();
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

    private void setUsedIks() {
        for (AccountIk ik : _account.getAdditionalIKs()) {
            if (!_accountFacade.deleteIkAllowed(ik.getIK(), _account)) {
                _iksNotAllowedForDelete.add(ik.getIK());
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

    public String getIkRequired() {
        return _sessionTools.isHospital(getAccount().getCustomerTypeId()) ? "true" : "false";
    }
    // </editor-fold>

    public List<AccountIk> getAdditionalIKs() {
        return _account.getAdditionalIKs();
    }

    public void addNewIK() {
        _account.addIk(0);
    }

    public void removeIK(AccountIk accountIk) {
        _account.removeIk(accountIk);
    }

    public List<FeatureEditorDAO> getFeatures() {
        return _features;
    }

    private void ensureFeatures() {
        if (_features == null) {
            _features = getFeatures4Editor();
        }
    }

    private List<FeatureEditorDAO> getFeatures4Editor() {
        List<FeatureEditorDAO> features = new ArrayList<>();
        List<Feature> configuredFeatures = new ArrayList<>();
        Account account = _sessionController.getAccount();
        if (account == null) {
            return features;
        }
        for (AccountFeature accFeature : account.getFeatures()) {
            features.add(new FeatureEditorDAO(accFeature, _sessionController.getAccount()));
            configuredFeatures.add(accFeature.getFeature());
        }
        for (Feature feature : Feature.values()) {
            if (configuredFeatures.contains(feature)) {
                continue;
            }
            if (feature.isSelectable() && _appTools.isFeatureEnabled(feature)) {
                features.add(new FeatureEditorDAO(createAccountFeature(feature), _sessionController.getAccount()));
            }
        }
        return features;
    }

    private AccountFeature createAccountFeature(Feature feature) {
        AccountFeature accFeature = new AccountFeature();
        accFeature.setFeature(feature);
        FeatureState state = FeatureState.NEW;
        accFeature.setFeatureState(state);
        accFeature.setSequence(0);
        return accFeature;
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
        removeDuplicateIks(_account);
        checkIKAdminRights(_account);
        _accountFacade.merge(_account);
        _sessionController.refreshAccount(_account.getId());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Speichern Erfolgreich"));
        return "";
    }

    public void removeDuplicateIks(Account acc) {
        Set<AccountIk> cleanList = new LinkedHashSet<>(acc.getAdditionalIKs());
        acc.getAdditionalIKs().clear();
        acc.getAdditionalIKs().addAll(cleanList);
    }

    public String saveFeatures() {
        if (mergeFeaturesIfModified()) {
            _sessionController.saveAccount();
        }
        _features = null;
        ensureFeatures();
        setActiveTopic(UserMaintenaceTabs.tabUMFeatures.name());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Speichern Erfolgreich"));
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Utils.getMessage("msgPasswordChanged")));
        }
        return success ? "" : Pages.Error.URL();
    }

    private String updateMail() {
        if (Utils.isNullOrEmpty(getEmail())) {
            return Pages.UserMaintenanceOther.URL();
        }
        boolean success = _accountChangeMailFacade.changeMail(getAccount().getId(), getEmail());
        if (success) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(Utils.getMessage("msgMailChanged")));
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
            RequestContext.getCurrentInstance().showMessageInDialog(new FacesMessage(FacesMessage.SEVERITY_WARN, "Falsche Eingabe", msg));
        }
        return msg.isEmpty();
    }

    private boolean mergeFeaturesIfModified() {
        if (!areFeaturesMofified()) {
            return false;
        }
        List<AccountFeature> accFeatures = _account.getFeatures();
        accFeatures.clear();

        int seq = 0;
        for (FeatureEditorDAO entry : _features) {
            if (entry.isValue()) {
                AccountFeature accFeature = entry.getAccFeature();
                accFeature.setSequence(seq++);
                accFeatures.add(accFeature);
            }
        }
        return true;
    }

    private boolean areFeaturesMofified() {
        if (_features == null) {
            return false;
        }
        List<FeatureEditorDAO> orgFeatures = getFeatures4Editor();
        if (_features.size() != orgFeatures.size()) {
            return true;
        }
        for (int i = 0; i < _features.size(); i++) {
            boolean equal = true;
            equal &= _features.get(i).getAccFeature().getFeature().equals(orgFeatures.get(i).getAccFeature().getFeature());
            equal &= _features.get(i).getName().equals(orgFeatures.get(i).getName());
            equal &= _features.get(i).isValue() == orgFeatures.get(i).isValue();
            equal &= _features.get(i).getAccFeature().getFeatureState().equals(orgFeatures.get(i).getAccFeature().getFeatureState());
            if (!equal) {
                return true;
            }
        }
        return false;
    }

    private void checkIKAdminRights(Account account) {
        for (AccountIk accountIK : account.getAdditionalIKs()) {
            if (_ikAdminFacade.hasIkAdmin(accountIK.getIK())) {
                boolean hasNewEntry = false;
                for (AccountFeature feature : account.getFeatures()) {
                    if (feature.getFeature().getIkReference() == IkReference.Hospital
                            && _ikAdminFacade.findAccessRightsByAccountIkAndFeature(account, accountIK.getIK(), feature.getFeature()).isEmpty()) {
                        AccessRight accessRight = new AccessRight(account.getId(), accountIK.getIK(), feature.getFeature(), Right.Deny);
                        _ikAdminFacade.saveAccessRight(accessRight);
                        hasNewEntry = true;
                    }
                }
                if (hasNewEntry) {
                    notifyIkAdmin(accountIK.getIK(), account);
                }
            }
        }
    }

    private void notifyIkAdmin(int ik, Account account) {
        String user = account.getFirstName() + " " + account.getLastName() + " (" + account.getCompany() + ", " + account.getTown() + ")";
        List<Account> admins = _ikAdminFacade.findIkAdmins(ik);

        for (Account admin : admins) {
            Mailer mailer = _sessionController.getMailer();
            MailTemplate template = mailer.getMailTemplate("IK-Admin: new user");
            String body = template.getBody()
                    .replace("{formalSalutation}", mailer.getFormalSalutation(admin))
                    .replace("{user}", "" + user)
                    .replace("{ik}", "" + ik);
            template.setBody(body);
            String subject = template.getSubject()
                    .replace("{ik}", "" + ik);
            template.setSubject(subject);
            mailer.sendMailTemplate(template, admin.getEmail());
        }
    }

    public boolean removeIkAllowed(int ik) {
        return !_iksNotAllowedForDelete.contains(ik);
    }

    public String getCustomerName(int ik) {
        return _customerFacade.getCustomerByIK(ik).getName();
    }

    public Boolean isIkValide(int ik) {
        return _customerFacade.isValidIK("" + ik);
    }

    public void isIKValid(FacesContext ctx, UIComponent component, Object value) throws ValidatorException {
        if (!isIkValide((Integer) value)) {
            throw new ValidatorException(new FacesMessage("Ungültige IK"));
        }
    }
}
