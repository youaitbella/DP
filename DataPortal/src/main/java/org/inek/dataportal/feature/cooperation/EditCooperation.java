/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.cooperation;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountFeature;
import org.inek.dataportal.entities.cooperation.CooperationRight;
import org.inek.dataportal.entities.cooperation.PortalMessage;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.FeatureState;
import org.inek.dataportal.enums.IkReference;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.cooperation.CooperationFacade;
import org.inek.dataportal.facades.cooperation.CooperationRequestFacade;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;
import org.inek.dataportal.facades.cooperation.PortalMessageFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Topic;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.services.MessageService;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped
public class EditCooperation extends AbstractEditController {

    private static final Logger LOGGER = Logger.getLogger("EditCooperation");

    @Inject private SessionController _sessionController;
    @Inject private CooperationRequestFacade _cooperationRequestFacade;
    @Inject private CooperationFacade _cooperationFacade;
    @Inject private AccountFacade _accountFacade;
    private Account _partnerAccount;
    private boolean _isOutstandingCooperationRequest;

    @Override
    protected void addTopics() {
        addTopic(CooperationTabs.tabCooperationPartner.name(), Pages.CooperationEditPartner.URL());
        addTopic(CooperationTabs.tabCooperationMessage.name(), Pages.CooperationEditMessage.URL());
        addTopic(CooperationTabs.tabCooperationIk.name(), Pages.CooperationEditNub.URL(), false);
        addTopic(CooperationTabs.tabCooperationOther.name(), Pages.CooperationEditOther.URL(), false);
    }

    enum CooperationTabs {

        tabCooperationPartner,
        tabCooperationMessage,
        tabCooperationIk,
        tabCooperationOther;
    }

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    private String _feature;

    public String getFeature() {
        if (_feature == null) {
            List<Feature> features = getFeaturesWithIkContext();
            if (features.size() > 0) {
                _feature = features.get(0).name();
            }
        }
        return _feature;
    }

    public void setFeature(String feature) {
        _feature = feature;
    }

    public List<Feature> getFeaturesWithIkContext() {
        return getFeatures(true);
    }

    public List<Feature> getFeaturesWithoutIkContext() {
        return getFeatures(false);
    }

    private List<Feature> getFeatures(boolean withIkContext) {
        List<Feature> features = new ArrayList<>();
        List<AccountFeature> accountfeatures = _sessionController.getAccount().getFeatures();
        for (AccountFeature accountfeature : accountfeatures) {
            Feature feature = accountfeature.getFeature();
            if (feature.isShareable() && withIkContext == (feature.getIkReference() != IkReference.None)) {
                features.add(feature);
            }
        }
        return features;
    }

    public Account getPartnerAccount() {
        return _partnerAccount;
    }

    public void setPartnerAccount(Account partnerAccount) {
        _partnerAccount = partnerAccount;
    }

    public boolean isOutstandingCooperationRequest() {
        return _isOutstandingCooperationRequest;
    }

    public void setOutstandingCooperationRequest(boolean isRequest) {
        _isOutstandingCooperationRequest = isRequest;
    }

    private List<CooperationInfo> _cooperationInfos;

    public List<CooperationInfo> getCooperationInfos() {
        if (_cooperationInfos == null) {
            _cooperationInfos = new ArrayList<>();
            for (Feature feature : getFeaturesWithoutIkContext()) {
                tryAddFeature(feature, this::getCooperativeRights);
            }
        }
        return _cooperationInfos;
    }

    private void tryAddFeature(Feature feature, Supplier<List<SelectItem>> cooperativeRights) {
        if (userHasSubscribedFeature(feature)) {
            EnsureCooperationRights();
            Optional<CooperationRight> optionalRight = _cooperationRights.stream().filter(r -> r.getFeature() == feature).findFirst();
            CooperationRight right = optionalRight.orElseGet(() -> addAndReturnMissingCooperationRight(feature));
            _cooperationInfos.add(new CooperationInfo(feature, cooperativeRights.get(), right));
        }
    }

    private CooperationRight addAndReturnMissingCooperationRight(Feature feature) {
        CooperationRight right = new CooperationRight(
                _sessionController.getAccountId(),
                getPartnerAccount().getId(),
                -1,
                feature
        );
        _cooperationRights.add(right);
        return right;
    }

    // </editor-fold>
    @PostConstruct
    private void init() {
        //LOGGER.log(Level.WARNING, "Init EditCooperation");
        Object partnerId = Utils.getFlash().get("partnerId");
        setPartnerAccount(loadAccount(partnerId));

        _isOutstandingCooperationRequest = _cooperationRequestFacade.existsAnyCooperationRequest(
                _sessionController.getAccountId(),
                getPartnerAccount().getId());
        setTopicsVisibility();
    }

    private Account loadAccount(Object partnerId) {
        try {
            int id = Integer.parseInt("" + partnerId);
            return _accountFacade.findAccount(id);
        } catch (NumberFormatException ex) {
            LOGGER.info(ex.getMessage());
        }
        return new Account();
    }

    private CooperationController getCooperationController() {
        return (CooperationController) _sessionController.getFeatureController(Feature.COOPERATION);
    }

    private void setTopicsVisibility() {
        setTopicVisibility(CooperationTabs.tabCooperationIk.name(), getFeaturesWithIkContext());
        setTopicVisibility(
                CooperationTabs.tabCooperationOther.name(),
                Arrays.asList(Feature.DRG_PROPOSAL, Feature.PEPP_PROPOSAL));
    }

    private void setTopicVisibility(String topicName, List<Feature> features) {
        Topic topic = findTopic(topicName);
        if (topic == null) {
            LOGGER.log(Level.WARNING, "Unknown topic {0}", topicName);
            return;
        }
        boolean hasSubcribed = features.stream().anyMatch(feature -> userHasSubscribedFeature(feature));

        topic.setVisible(!_isOutstandingCooperationRequest && hasSubcribed);
    }

    private boolean userHasSubscribedFeature(Feature feature) {
        AccountFeature accountFeature = _sessionController.findAccountFeature(feature);
        if (accountFeature == null) {
            // user has no such feature subscribed
            return false;
        }
        // check, whether the subscribed feature is available
        return accountFeature.getFeatureState().equals(FeatureState.SIMPLE)
                || accountFeature.getFeatureState().equals(FeatureState.APPROVED);
    }

    // <editor-fold defaultstate="collapsed" desc="tab Partner">
    public String accept() {
        int partner1Id = _sessionController.getAccountId();
        int partner2Id = getPartnerAccount().getId();
        _cooperationFacade.createCooperation(partner1Id, partner2Id);
        _cooperationRequestFacade.removeAnyCooperationRequest(partner1Id, partner2Id);
        _isOutstandingCooperationRequest = false;
        setTopicsVisibility();
        return "";
    }

    public String refuse() {
        int partner1Id = _sessionController.getAccountId();
        int partner2Id = getPartnerAccount().getId();
        _cooperationRequestFacade.removeAnyCooperationRequest(partner1Id, partner2Id);
        return Pages.CooperationSummary.URL();
    }

    public String finish() {
        int partner1Id = _sessionController.getAccountId();
        int partner2Id = getPartnerAccount().getId();
        _cooperationFacade.finishCooperation(partner1Id, partner2Id);
        return Pages.CooperationSummary.URL();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="tab NUB">
    @Inject private CooperationRightFacade _cooperationRightFacade;
    private List<CooperationRight> _cooperationRights;

    public List<CooperationRight> getCooperationRights() {
        EnsureCooperationRights();
        return _cooperationRights;
    }

    public void setCooperationRights(List<CooperationRight> cooperationRights) {
        _cooperationRights = cooperationRights;
    }

    private void EnsureCooperationRights() {
        if (_cooperationRights == null) {
            _cooperationRights = _cooperationRightFacade.getGrantedCooperationRights(_sessionController.getAccountId(), _partnerAccount.getId());
        }
        addMissingRights();
    }

    private void addMissingRights() {
        Set<Integer> iks = _sessionController.getAccount().getFullIkSet();
        List<Feature> features = getFeaturesWithIkContext();
        for (Feature feature : features) {
            addMissingRightsForFeature(iks, feature);
        }
    }

    private void addMissingRightsForFeature(Set<Integer> iks, Feature feature) {
        for (int ik : iks) {
            if (_cooperationRights.stream().anyMatch(r -> r.getFeature() == feature && r.getIk() == ik)) {
                continue;
            }
            CooperationRight right = new CooperationRight(
                    _sessionController.getAccountId(),
                    getPartnerAccount().getId(),
                    ik,
                    feature
            );
            _cooperationRights.add(right);
        }

    }

    public List<SelectItem> getCooperativeRights() {
        List<SelectItem> items = new ArrayList<>();
        for (CooperativeRight right : CooperativeRight.values()) {
            if (right.isPublic()) {
                SelectItem item = new SelectItem(right.name(), Utils.getMessageOrEmpty("cor" + right.name()));
                items.add(item);
            }
        }
        return items;
    }

    public List<SelectItem> getCooperativeReadRights() {
        List<SelectItem> items = new ArrayList<>();
        for (CooperativeRight right : CooperativeRight.values()) {
            if (right.isPublic() && !right.canWriteCompleted() && !right.canSeal()) {
                SelectItem item = new SelectItem(right.name(), Utils.getMessageOrEmpty("cor" + right.name()));
                items.add(item);
            }
        }
        return items;
    }

    public String save() {
        for (CooperationRight right : _cooperationRights) {
            _cooperationRightFacade.save(right);
        }
        return "";
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="tab Messages">
    @Inject private PortalMessageFacade _messageFacade;
    @Inject private MessageService _messageService;

    private List<PortalMessage> _messages;

    private String _message = "";

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        _message = message;
    }

    private String _subject = "";

    public String getSubject() {
        return _subject;
    }

    public void setSubject(String subject) {
        _subject = subject;
    }

    private boolean _createMessage = false;

    public boolean isCreateMessage() {
        return _createMessage;
    }

    public void setCreateMessage(boolean createMessage) {
        _createMessage = createMessage;
    }

    public void newMessage(ActionEvent e) {
        _createMessage = true;
    }

    public void cancelMessage(ActionEvent e) {
        _createMessage = false;
    }

    public void sendMessage(ActionEvent e) {
        if (_subject.isEmpty()) {
            String msg = MessageFormat.format(Utils.getMessage("txtNotAllowedEmpty"), Utils.getMessage("lblMailSubject"));
            Utils.showMessageInBrowser(msg);
            return;
        }
        if (_message.isEmpty()) {
            String msg = MessageFormat.format(Utils.getMessage("txtNotAllowedEmpty"), Utils.getMessage("lblMessage"));
            Utils.showMessageInBrowser(msg);
            return;
        }
        Account sender = _sessionController.getAccount();
        Account receiver = getPartnerAccount();
        _messageService.sendMessage(sender, receiver, _subject, _message);
        _createMessage = false;
        _subject = "";
        _message = "";
        _messages = null;  // will force a reload
    }

    public List<PortalMessage> getMessages() {
        if (_messages == null) {
            _messages = _messageFacade.getMessagesByParticipants(_sessionController.getAccountId(), getPartnerAccount().getId());
        }
        return _messages;
    }

    public String expand(int messageId) {
        setVisibility(messageId, true);
        Optional<PortalMessage> unreadMessage = getMessages()
                .stream()
                .filter(m -> m.getId() == messageId && m.getStatus() == 0 && m.getToAccountId() == _sessionController.getAccountId())
                .findFirst();
        if (unreadMessage.isPresent()) {
            PortalMessage message = unreadMessage.get();
            message.setStatus(1);
            _messageFacade.merge(message);
        }
        return "";
    }

    public String collapse(int messageId) {
        setVisibility(messageId, false);
        return "";
    }

    private void setVisibility(int messageId, boolean x) {
        getMessages().stream().filter((m) -> (m.getId() == messageId)).forEach((m) -> {
            m.setVisible(x);
        });
    }

    // </editor-fold>
}
