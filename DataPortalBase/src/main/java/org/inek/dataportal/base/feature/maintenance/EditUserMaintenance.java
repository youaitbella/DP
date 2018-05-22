package org.inek.dataportal.base.feature.maintenance;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
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
        tabUMConfig;
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
    private boolean _isMofified = false;
    private String _oldPassword;
    private String _newPassword;
    private String _repeatPassword;
    private String _script = "";
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
        _account = _sessionController.getAccount();
        _user = _account.getUser();
        _oldPassword = "";
        _newPassword = "";
        _repeatPassword = "";
        _isMofified = false;
    }

    @Override
    protected void addTopics() {
        addTopic(UserMaintenaceTabs.tabUMMaster.name(), Pages.UserMaintenanceMasterData.URL());
        addTopic(UserMaintenaceTabs.tabUMFeatures.name(), Pages.UserMaintenanceFeatures.URL());
        addTopic(UserMaintenaceTabs.tabUMOther.name(), Pages.UserMaintenanceOther.URL());
        addTopic(UserMaintenaceTabs.tabUMConfig.name(), Pages.UserMaintenanceConfig.URL());
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
        if (!_isMofified) {
            setActiveTopic(newTopic);
        }
    }

    public boolean isMofified() {
        return _isMofified;
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

    // <editor-fold defaultstate="collapsed" desc="Part Additional IKs">
    public List<AccountIk> getAdditionalIKs() {
        return _account.getAdditionalIKs();
    }

    public void addNewIK() {
        _account.addIk(0);
    }

    public void removeIK(AccountIk accountIk) {
        _account.getAdditionalIKs().remove(accountIk);
        //TODO Remove IK-Admin Right
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Part Features">
    public List<FeatureEditorDAO> getFeatures() {
        ensureFeatures();
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

    public String up(String featureName) {
        int pos = findFeature(featureName);
        if (pos > 0) {
            FeatureEditorDAO entry = _features.remove(pos);
            _features.add(pos - 1, entry);
        }
        return null;
    }

    public String down(String featureName) {
        int pos = findFeature(featureName);
        if (pos >= 0 && pos < _features.size() - 1) {
            FeatureEditorDAO entry = _features.remove(pos);
            _features.add(pos + 1, entry);
        }
        return null;
    }

    private int findFeature(String featureName) {
        for (int i = 0; i < _features.size(); i++) {
            FeatureEditorDAO entry = _features.get(i);
            AccountFeature accFeature = entry.getAccFeature();
            if (accFeature.getFeature() == Feature.valueOf(featureName)) {
                return i;
            }
        }
        return -1;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Part password">
    public void checkPassword(FacesContext context, UIComponent component, Object value) {
        Utils.checkPassword(context, component, value);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Part other">
    /**
     * requests deleting of the account this function displays a confirmation dialog confirming with "ok" performs a
     * call to delete
     *
     * @return
     */
    public String requestDelete() {
        String msg = Utils.getMessage("msgDeleteAccount").replace("\r", "").replace("\n", "\\r\\n");
        String areYouShure = Utils.getMessage("msgConfirm").replace("\r", "").replace("\n", "\\r\\n");
        _script += "if (confirm ('" + msg + "')) {if (confirm('" + areYouShure + "')) {document.getElementById('form:deleteAccount').click();}}\r\n";
        return "";
    }

    /**
     * This function usually can only be called if the request to delete is confirmed. As a precaution, it performs some
     * checks which have been done in requestSeal.
     *
     * @return
     */
    public String delete() {
        _sessionController.deleteAccount();
        return Pages.Login.URL();
    }

    public boolean deleteAllowed() {
        return _sessionController.getAccount().getAdminIks().isEmpty();
    }

    // </editor-fold>
    public String save() {
        removeDuplicateIks(_account);
        checkIKAdminRights(_account);
        _sessionController.saveAccount();
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
        _features = null; // force reload on / after save
        setActiveTopic(UserMaintenaceTabs.tabUMFeatures.name());
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
            _script = "alert ('" + Utils.getMessage("msgPasswordChanged") + "');";
        }
        return success ? "" : Pages.Error.URL();
    }

    private String updateMail() {
        if (Utils.isNullOrEmpty(getEmail())) {
            return Pages.UserMaintenanceOther.URL();
        }
        boolean success = _accountChangeMailFacade.changeMail(getAccount().getId(), getEmail());
        if (success) {
            _script = "alert ('" + Utils.getMessage("msgMailChanged") + "');";
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
        _script = msg.isEmpty() ? "" : "alert ('" + msg + "');";
        return msg.isEmpty();
    }

    private boolean mergeFeaturesIfModified() {
        if (!areFeaturesMofified()) {
            return false;
        }
        List<AccountFeature> accFeatures = _sessionController.getAccount().getFeatures();
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

    public String getScript() {
        String script = _script;
        _script = "";
        return script;
    }

    public boolean removeIkAllowed(int ik) {
        if (ik == 0) {
            return true;
        }
        return _accountFacade.deleteIkAllowed(ik, _sessionController.getAccount());
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

    @PreDestroy
    public void leaveBean() {
        try {
            _sessionController.refreshAccount(_account.getId());
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
