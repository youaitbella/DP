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
import javax.annotation.PreDestroy;
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
import org.inek.dataportal.helper.structures.Triple;
import org.inek.dataportal.mail.Mailer;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped
public class EditCooperation extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditNubProposal");

    @Inject private SessionController _sessionController;
    @Inject CooperationRequestFacade _cooperationRequestFacade;
    @Inject CooperationFacade _cooperationFacade;
    @Inject AccountFacade _accountFacade;
    private Account _partnerAccount;
    private boolean _isOutstandingCooperationRequest;

    @Override
    protected void addTopics() {
        addTopic(CooperationTabs.tabCooperationPartner.name(), Pages.CooperationEditPartner.URL());
        addTopic(CooperationTabs.tabCooperationMessage.name(), Pages.CooperationEditMessage.URL());
        addTopic(CooperationTabs.tabCooperationNub.name(), Pages.CooperationEditNub.URL(), false);
        addTopic(CooperationTabs.tabCooperationOther.name(), Pages.CooperationEditOther.URL(), false);
    }

    enum CooperationTabs {

        tabCooperationPartner,
        tabCooperationMessage,
        tabCooperationNub,
        tabCooperationOther;
    }

    // <editor-fold defaultstate="collapsed" desc="fields">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
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

    private List<Triple<Feature, List<SelectItem>, CooperationRight>> _cooperationInfos;

    public List<Triple<Feature, List<SelectItem>, CooperationRight>> getCooperationInfos() {
        if (_cooperationInfos == null) {
            _cooperationInfos = new ArrayList<>();
            tryAddFeature(Feature.MODEL_INTENTION, this::getCooperativeReadRights);
            tryAddFeature(Feature.DRG_PROPOSAL, this::getCooperativeRights);
            tryAddFeature(Feature.PEPP_PROPOSAL, this::getCooperativeRights);
        }
        return _cooperationInfos;
    }

    private void tryAddFeature(Feature feature, Supplier<List<SelectItem>> cooperativeRights) {
        if (userHasSubscribedFeature(feature)) {
            EnsureCooperationRights();

            Optional<CooperationRight> optionalRight = _cooperationRights.stream().filter(r -> r.getFeature() == feature).findFirst();
            CooperationRight right;
            if (optionalRight.isPresent()) {
                right = optionalRight.get();
            } else {
                right = addAndReturnMissingCooperationRight(feature);
            }
            _cooperationInfos.add(new Triple<>(feature, cooperativeRights.get(), right));
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
        //_logger.log(Level.WARNING, "Init EditCooperation");
        Object partnerId = Utils.getFlash().get("partnerId");
        setPartnerAccount(loadAccount(partnerId));

        _isOutstandingCooperationRequest = _cooperationRequestFacade.existsAnyCooperationRequest(
                _sessionController.getAccountId(),
                getPartnerAccount().getId());
        setTopicsVisibility();
    }

    @PreDestroy
    private void destroy() {
        //_logger.log(Level.WARNING, "Destroy EditCooperation");
    }

    private Account loadAccount(Object partnerId) {
        try {
            int id = Integer.parseInt("" + partnerId);
            return _accountFacade.find(id);
        } catch (NumberFormatException ex) {
            _logger.info(ex.getMessage());
        }
        return new Account();
    }

    private CooperationController getCooperationController() {
        return (CooperationController) _sessionController.getFeatureController(Feature.COOPERATION);
    }

    private void setTopicsVisibility() {
        setTopicVisibility(CooperationTabs.tabCooperationNub.name(), Arrays.asList(Feature.NUB));
        setTopicVisibility(CooperationTabs.tabCooperationOther.name(), Arrays.asList(Feature.MODEL_INTENTION, Feature.DRG_PROPOSAL, Feature.PEPP_PROPOSAL));
    }

    private void setTopicVisibility(String topicName, List<Feature> features) {
        Topic topic = findTopic(topicName);
        if (topic == null) {
            _logger.log(Level.WARNING, "Unknown topic {0}", topicName);
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
    @Inject
    CooperationRightFacade _cooperationRightFacade;
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
        addMissingNubCooperationRights();
    }

    private void addMissingNubCooperationRights() {
        Topic topic = findTopic(CooperationTabs.tabCooperationNub.name());
        if (!topic.isVisible()) {
            return;
        }

        Set<Integer> iks = _sessionController.getAccount().getFullIkList();

        for (CooperationRight right : _cooperationRights) {
            if (right.getFeature() == Feature.NUB) {
                // remove those iks from list, which still have rights, to determine the missing
                iks.remove((Integer) right.getIk());
            }
        }
        for (int ik : iks) {
            // for the memaining Iks add new rights to create a full list
            CooperationRight right = new CooperationRight(
                    _sessionController.getAccountId(),
                    getPartnerAccount().getId(),
                    ik,
                    Feature.NUB
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
    @Inject PortalMessageFacade _messageFacade;
    @Inject private Mailer _mailer;
    List<PortalMessage> _messages;

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
        createPortalMessage(sender, receiver, _subject, _message);
        if (receiver.isMessageCopy()) {
            sendEmailCopy(sender, receiver, _subject, _message);
        }
        _createMessage = false;
        _subject = "";
        _message = "";
        _messages = null;  // will force a reload
    }

    private void sendEmailCopy(Account sender, Account receiver, String subject, String message) {
        String extMessage = "Ihr Kooperationspartner, " + sender.getDisplayName() + ", sendet Ihnen die folgende Nachricht:"
                + "\r\n\r\n"
                + "-----"
                + "\r\n\r\n"
                + message
                + "\r\n\r\n"
                + "-----"
                + "\r\n\r\n"
                + "Dies ist eine automatisch generierte Mail. Bitte beachten Sie, dass Sie die Antwortfunktion Ihres Mail-Programms nicht nutzen können.";
        _mailer.sendMailFrom("noReply@inek.org", receiver.getEmail(), "", "", subject, extMessage);
    }

    private void createPortalMessage(Account sender, Account receiver, String subject, String message) {
        PortalMessage portalMessage = new PortalMessage();
        portalMessage.setFromAccountId(sender.getId());
        portalMessage.setToAccountId(receiver.getId());
        portalMessage.setFeature(Feature.COOPERATION);
        portalMessage.setKeyId(0);
        portalMessage.setSubject(subject);
        portalMessage.setMessage(message);
        _messageFacade.persist(portalMessage);
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
