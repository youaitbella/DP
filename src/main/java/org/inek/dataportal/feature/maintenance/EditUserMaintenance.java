/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package org.inek.dataportal.feature.maintenance;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.backingbeans.SessionTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountAdditionalIK;
import org.inek.dataportal.entities.account.AccountFeature;
import org.inek.dataportal.entities.Customer;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.FeatureState;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountChangeMailFacade;
import org.inek.dataportal.facades.AccountFacade;
import org.inek.dataportal.facades.AccountPwdFacade;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.feature.nub.NubSessionTools;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.faceletvalidators.EmailValidator;
import org.inek.dataportal.helper.faceletvalidators.IkValidator;
import org.inek.dataportal.helper.faceletvalidators.NameValidator;
import org.inek.dataportal.helper.structures.Triple;

/**
 *
 * @author muellermi
 */
@Named
@ConversationScoped
public class EditUserMaintenance extends AbstractEditController {

    // <editor-fold defaultstate="collapsed" desc="fields">
    enum UserMaintenaceTabs {

        tabUMMaster,
        tabUMAdditionalIKs,
        tabUMFeatures,
        tabUMOther,
        tabUMConfig;
    }
    private static final Logger _logger = Logger.getLogger("EditUserMaintenance");
    
    @Inject private SessionTools _sessionTools;
    @Inject private NubSessionTools _nubSessionTools;
    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;
    @Inject private AccountPwdFacade _accountPwdFacade;
    @Inject private CustomerFacade _customerFacade;
    @Inject private AccountChangeMailFacade _accountChangeMailFacade;
    @Inject private Conversation _conversation;
    private String _user;
    private String _email;
    private Account _accountWorkingCopy;
    List<FeatureEditorDAO> _features;
    private boolean _isMofified = false;
    List<Triple<Integer, Integer, String>> _additionalIKs;
    private String _oldPassword;
    private String _newPassword;
    private String _repeatPassword;
    private String _script = "";

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Account getAccount() {
        return _accountWorkingCopy;
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
    private void init(){
        //_logger.log(Level.WARNING, "Init EditUserMaintenance");
        _sessionController.beginConversation(_conversation);
        initOrResetData();
    }

    @PreDestroy
    private void destroy(){
        //_logger.log(Level.WARNING, "Destroy EditUserMaintenance");
    }
    
    private void initOrResetData() {
//        String activeTopic = (String) Utils.getFlash().get("activeTopic");
//        if (activeTopic != null) {
//            changeTab(activeTopic);
//        }
        _features = null;
        _accountWorkingCopy = _accountFacade.find(_sessionController.getAccountId());
        _user = getAccount().getUser();
        _oldPassword = "";
        _newPassword = "";
        _repeatPassword = "";
        _isMofified = false;
    }

    @Override
    protected void addTopics() {
        addTopic(UserMaintenaceTabs.tabUMMaster.name(), Pages.UserMaintenanceMasterData.URL());
        addTopic(UserMaintenaceTabs.tabUMAdditionalIKs.name(), Pages.UserMaintenanceAdditionalIKs.URL());
        addTopic(UserMaintenaceTabs.tabUMFeatures.name(), Pages.UserMaintenanceFeatures.URL());
        addTopic(UserMaintenaceTabs.tabUMOther.name(), Pages.UserMaintenanceOther.URL());
        addTopic(UserMaintenaceTabs.tabUMConfig.name(), Pages.UserMaintenanceConfig.URL());
    }

    private UserMaintenanceController getUserMaintenanceController() {
        return (UserMaintenanceController) _sessionController.getFeatureController(Feature.USER_MAINTENANCE);
    }

    /**
     * This action will change the tab. It may prevent changing if the data has
     * changed.
     *
     * @param newTopic
     */
    @Override
    public void changeTab(String newTopic) {
        if (getActiveTopic().getKey().equals(newTopic)) {
            return;
        }
        if (getActiveTopic().getKey().equals(UserMaintenaceTabs.tabUMMaster.name())
                || getActiveTopic().getKey().equals(UserMaintenaceTabs.tabUMConfig.name())) {
            _isMofified = isMasterdataChanged();
        }
        if (getActiveTopic().getKey().equals(UserMaintenaceTabs.tabUMAdditionalIKs.name())) {
            _isMofified = isIKListMofified();
        }
        if (getActiveTopic().getKey().equals(UserMaintenaceTabs.tabUMFeatures.name())) {
            _isMofified = areFeaturesMofified();
        }
        if (!_isMofified) {
            setActiveTopic(newTopic);
//            Utils.getFlash().put("activeTopic", newTopic);
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
        if (!EmailValidator.isValidEmail(input)) {
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
    public List<Triple<Integer, Integer, String>> getAdditionalIKs() {
        if (_additionalIKs == null) {
            _additionalIKs = new ArrayList<>();
            for (AccountAdditionalIK addIk : _sessionController.getAccount().getAdditionalIKs()) {
                _additionalIKs.add(new Triple(addIk.getIK(), addIk.getIK(), addIk.getName()));
            }
            addEmptyElementIfNotExists();
        }
        return _additionalIKs;
    }

    public void removeAdditionalIK(Integer ik) {
        if (_additionalIKs == null) {
            return;
        }
        for (int i = _additionalIKs.size() - 1; i >= 0; i--) {
            Triple<Integer, Integer, String> ikTriple = _additionalIKs.get(i);
            if (ikTriple.getValue2() == null && ik == null) {
                _additionalIKs.remove(i);
            } else if (ik != null && ikTriple.getValue2() != null && ikTriple.getValue2().intValue() == ik.intValue()) {
                _additionalIKs.remove(i);
            }
        }
        addEmptyElementIfNotExists();
    }

    public void ikChanged(ValueChangeEvent event) {
        if (("" + event.getOldValue()).equals("" + event.getNewValue())) {
            return;
        }
        String id = event.getComponent().getClientId();
        int posEnd = id.lastIndexOf(":");
        int posStart = id.lastIndexOf(":", posEnd - 1);
        int ind = Integer.parseInt(id.substring(posStart + 1, posEnd));
        Triple<Integer, Integer, String> ikTriple = _additionalIKs.get(ind);
        if (IkValidator.isValidIK((String) event.getNewValue())) {
            int ik = Integer.parseInt((String) event.getNewValue());
            Customer customer = _customerFacade.getCustomerByIK(ik);
            String name = customer.getName() == null ? "" : customer.getName();
            if (name.length() > 0) {
                ikTriple.setValue3(name);
            } else {
                ikTriple.setValue3(Utils.getMessage("msgUnknownIK"));
            }
            ikTriple.setValue1(ik);
            ikTriple.setValue2(ik);
        } else {
            ikTriple.setValue3(Utils.getMessage("errIK"));
        }
        addEmptyElementIfNotExists();
    }

    private void addEmptyElementIfNotExists() {
        if (_additionalIKs.isEmpty() || _additionalIKs.get(_additionalIKs.size() - 1).getValue2() != null) {
            _additionalIKs.add(new Triple(null, null, ""));
        }
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
        List<FeatureEditorDAO> features = new ArrayList<FeatureEditorDAO>();
        List<Feature> configuredFeatures = new ArrayList<Feature>();
        for (AccountFeature accFeature : _sessionController.getAccount().getFeatures()) {
            features.add(new FeatureEditorDAO(accFeature, _sessionController.getAccount()));
            configuredFeatures.add(accFeature.getFeature());
        }
        for (Feature feature : Feature.values()) {
            String description = Utils.getMessageOrEmpty("description" + feature.name());
            if (description.length() > 0 && !configuredFeatures.contains(feature)) {
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
     * requests deleting of the account this function displays a confirmation
     * dialog confirming with "ok" performs a call to delete
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
     * This function usually can only be called if the request to delete is
     * confirmed. As a precaution, it performs some checks which have been done
     * in requestSeal.
     *
     * @return
     */
    public String delete() {
        _sessionController.deleteAccount();
        return Pages.Login.URL();
    }
    // </editor-fold>

    public String save() {
        if (isMasterdataChanged()) {
            mergeMasterData();
            _sessionController.saveAccount();
            _nubSessionTools.clearCache();
        }
        return "";
    }

    public String saveIks() {
        if (mergeIKListIfModified()) {
            _sessionController.saveAccount();
            _nubSessionTools.clearCache();
        }
        Utils.getFlash().put("activeTopic", UserMaintenaceTabs.tabUMAdditionalIKs.name());
        return "";
    }

    public String saveFeatures() {
        if (mergeFeaturesIfModified()) {
            _sessionController.saveAccount();
        }
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
        List<AccountFeature> list = _accountWorkingCopy.getFeatures();
        for (AccountFeature ft : list) {
            if (ft.getFeature() == Feature.NUB) {
                return true;
            }
        }
        return false;
    }

    public boolean getFeatureCoop() {
        List<AccountFeature> list = _accountWorkingCopy.getFeatures();
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
        boolean success = _accountPwdFacade.changePassword(getAccount().getAccountId(), _oldPassword, _newPassword);
        if (success) {
            _script = "alert ('" + Utils.getMessage("msgPasswordChanged") + "');";
        }
        return success ? "" : Pages.Error.URL();
    }

    private String updateMail() {
        if (Utils.isNullOrEmpty(getEmail())) {
            return Pages.UserMaintenanceOther.URL();
        }
        boolean success = _accountChangeMailFacade.changeMail(getAccount().getAccountId(), getEmail());
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
        } else if (!_accountPwdFacade.isCorrectPassword(_accountWorkingCopy.getAccountId(), _oldPassword)) {
            msg += (msg.isEmpty() ? "" : "\\r\\n") + "Das alte Passwort stimmt nicht überein.";
        }
        _script = msg.isEmpty() ? "" : "alert ('" + msg + "');";
        return msg.isEmpty();
    }

    private boolean isMasterdataChanged() {
        Account original = _sessionController.getAccount();
        Account copy = getAccount();
        int origIK = original.getIK() == null ? -1 : original.getIK();
        int copyIK = copy.getIK() == null ? -1 : copy.getIK();
        boolean isEqual = original.getUser().equals(_user)
                && original.getGender() == copy.getGender()
                && original.getTitle().equals(copy.getTitle())
                && original.getInitials().equals(copy.getInitials())
                && original.getFirstName().equals(copy.getFirstName())
                && original.getLastName().equals(copy.getLastName())
                && original.getRoleId() == copy.getRoleId()
                && original.getPhone().equals(copy.getPhone())
                && original.getCompany().equals(copy.getCompany())
                && original.getCustomerTypeId() == copy.getCustomerTypeId()
                && origIK == copyIK
                && original.getStreet().equals(copy.getStreet())
                && original.getPostalCode().equals(copy.getPostalCode())
                && original.getTown().equals(copy.getTown())
                && original.getCustomerPhone().equals(copy.getCustomerPhone())
                && original.getCustomerFax().equals(copy.getCustomerFax())
                && original.isNubConfirmation() == copy.isNubConfirmation()
                && original.isMessageCopy() == copy.isMessageCopy();
        return !isEqual;
    }

    private void mergeMasterData() {
        Account original = _sessionController.getAccount();
        Account copy = getAccount();
        original.setUser(_user);
        original.setGender(copy.getGender());
        original.setTitle(copy.getTitle());
        original.setInitials(copy.getInitials());
        original.setFirstName(copy.getFirstName());
        original.setLastName(copy.getLastName());
        original.setRoleId(copy.getRoleId());
        original.setPhone(copy.getPhone());
        original.setCompany(copy.getCompany());
        original.setCustomerTypeId(copy.getCustomerTypeId());
        original.setIK(copy.getIK());
        original.setStreet(copy.getStreet());
        original.setPostalCode(copy.getPostalCode());
        original.setTown(copy.getTown());
        original.setCustomerPhone(copy.getCustomerPhone());
        original.setCustomerFax(copy.getCustomerFax());
        original.setNubConfirmation(copy.isNubConfirmation());
        original.setMessageCopy(copy.isMessageCopy());
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

    private boolean mergeIKListIfModified() {
        if (!isIKListMofified()) {
            return false;
        }
        Set<Integer> newSet = getNewIKSet();
        List<AccountAdditionalIK> additionalIKs = _sessionController.getAccount().getAdditionalIKs();
        for (int i = additionalIKs.size() - 1; i >= 0; i--) {
            AccountAdditionalIK addIK = additionalIKs.get(i);
            if (newSet.contains(addIK.getIK())) {
                newSet.remove(addIK.getIK());
            } else {
                additionalIKs.remove(i);
            }
        }
        for (Integer ik : newSet) {
            AccountAdditionalIK addIK = new AccountAdditionalIK();
            addIK.setIK(ik);
            additionalIKs.add(addIK);
        }
        return true;
    }

    private boolean isIKListMofified() {
        Set<Integer> newSet = getNewIKSet();
        Set<Integer> oldSet = getOldIKSet();
        if (newSet.size() != oldSet.size()) {
            return true;
        }
        for (Integer ik : newSet) {
            if (!oldSet.contains(ik)) {
                return true;
            }
        }
        return false;
    }

    private Set<Integer> getNewIKSet() {
        Set<Integer> newSet = new TreeSet<>();
        if (_additionalIKs != null) {
            for (Triple<Integer, Integer, String> ikTriple : _additionalIKs) {
                if (ikTriple.getValue2() != null) {
                    newSet.add(ikTriple.getValue2());
                }
            }
        }
        return newSet;
    }

    private Set<Integer> getOldIKSet() {
        Set<Integer> oldSet = new TreeSet<>();
        for (AccountAdditionalIK addIk : _sessionController.getAccount().getAdditionalIKs()) {
            oldSet.add(addIk.getIK());
        }
        return oldSet;
    }

    public String discardChanges() {
        initOrResetData();
        return Pages.UserMaintenanceMasterData.URL();
    }

    public String getScript() {
        String script = _script;
        _script = "";
        return script;
    }
}
