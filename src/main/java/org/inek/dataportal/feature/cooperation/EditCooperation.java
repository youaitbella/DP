/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.cooperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.Account;
import org.inek.dataportal.entities.AccountFeature;
import org.inek.dataportal.entities.CooperationRight;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.FeatureState;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.AccountFacade;
import org.inek.dataportal.facades.CooperationFacade;
import org.inek.dataportal.facades.CooperationRequestFacade;
import org.inek.dataportal.facades.CooperationRightFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Topic;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
@Named
@ConversationScoped
public class EditCooperation extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditNubProposal");

    @Inject private SessionController _sessionController;
    @Inject CooperationRequestFacade _cooperationRequestFacade;
    @Inject CooperationFacade _cooperationFacade;
    @Inject AccountFacade _accountFacade;
    @Inject private Conversation _conversation;
    private Account _partnerAccount;
    private boolean _isRequest;

    @Override
    protected void addTopics() {
        addTopic(CooperationTabs.tabCooperationPartner.name(), Pages.CooperationEditPartner.URL());
        addTopic(CooperationTabs.tabCooperationNub.name(), Pages.CooperationEditNub.URL(), false);
    }

    enum CooperationTabs {

        tabCooperationPartner,
        tabCooperationNub;
    }
    // <editor-fold defaultstate="collapsed" desc="fields">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Account getPartnerAccount() {
        return _partnerAccount;
    }

    public void setPartnerAccount(Account partnerAccount) {
        this._partnerAccount = partnerAccount;
    }

    public boolean isRequest() {
        return _isRequest;
    }

    public void setRequest(boolean isRequest) {
        _isRequest = isRequest;
    }

    // </editor-fold>
    @PostConstruct
    private void init() {
        //_logger.log(Level.WARNING, "Init EditCooperation");
        _sessionController.beginConversation(_conversation);
        Object partnerId = Utils.getFlash().get("partnerId");
        setPartnerAccount(loadAccount(partnerId));
        //_logger.log(Level.WARNING, "PartnerAccount {0}", getPartnerAccount());
        
        _isRequest = _cooperationRequestFacade.existsAnyCooperationRequest(
                _sessionController.getAccountId(),
                getPartnerAccount().getAccountId());
        setTopicsVisibility();
    }

    @PreDestroy
    private void destroy(){
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
        Topic topic = findTopic(CooperationTabs.tabCooperationNub.name());
        AccountFeature feature = _sessionController.findAccountFeature(Feature.NUB);
        topic.setVisible(!_isRequest && feature.getFeatureState().equals(FeatureState.SIMPLE));
    }

    // <editor-fold defaultstate="collapsed" desc="tab Partner">
    public String accept() {
        int partner1Id = _sessionController.getAccountId();
        int partner2Id = getPartnerAccount().getAccountId();
        _cooperationFacade.createCooperation(partner1Id, partner2Id);
        _cooperationRequestFacade.removeAnyCooperationRequest(partner1Id, partner2Id);
        _isRequest = false;
        setTopicsVisibility();
        return "";
    }

    public String refuse() {
        int partner1Id = _sessionController.getAccountId();
        int partner2Id = getPartnerAccount().getAccountId();
        _cooperationRequestFacade.removeAnyCooperationRequest(partner1Id, partner2Id);
        return Pages.CooperationSummary.URL();
    }

    public String finish() {
        int partner1Id = _sessionController.getAccountId();
        int partner2Id = getPartnerAccount().getAccountId();
        _cooperationFacade.finishCooperation(partner1Id, partner2Id);
        return Pages.CooperationSummary.URL();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="tab NUB">
    @Inject CooperationRightFacade _cooperationRightFacade;
    private List<CooperationRight> _cooperationRights;

    public List<CooperationRight> getCooperationRights() {
        if (_cooperationRights == null) {
            EnsureCooperationRights();
        }
        return _cooperationRights;
    }

    public void setCooperationRights(List<CooperationRight> cooperationRights) {
        this._cooperationRights = cooperationRights;
    }

    private void EnsureCooperationRights() {
        _cooperationRights = _cooperationRightFacade.getGrantedCooperationRights(_sessionController.getAccountId(), _partnerAccount.getAccountId(), Feature.NUB);
        Set<Integer> iks = _sessionController.getAccount().getFullIkList();
        for (CooperationRight right : _cooperationRights) {
            // remove those iks from list, which still have rights
            iks.remove((Integer)right.getIk());
        }
        for (int ik : iks) {
            // for the memaining Iks add new rights to create a full list
            CooperationRight right = new CooperationRight(
                    _sessionController.getAccountId(),
                    getPartnerAccount().getAccountId(),
                    ik,
                    Feature.NUB
            );
            _cooperationRights.add(right);
        }
    }

    public List<SelectItem> getCooperativeRights() {
        List<SelectItem> items = new ArrayList<>();
        for (CooperativeRight right : CooperativeRight.values()) {
            SelectItem item = new SelectItem(right.name(), Utils.getMessageOrEmpty("cor" + right.name()));
            items.add(item);
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

    // <editor-fold defaultstate="collapsed" desc="tab XXX">
    // </editor-fold>
}


