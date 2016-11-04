package org.inek.dataportal.feature.modelintention;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.cooperation.CooperationRight;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.modelintention.ModelIntention;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.UserSet;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.cooperation.CooperationFacade;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.modelintention.ModelIntentionFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.EntityInfo;
import org.inek.dataportal.utils.DocumentationUtil;

@Named
@RequestScoped
public class ModelIntentionList {

    @Inject ModelIntentionFacade _modelIntentionFacade;
    @Inject SessionController _sessionController;
    @Inject AccountFacade _accountFacade;
    @Inject CooperationFacade _cooperationFacade;
    @Inject CooperationRightFacade _cooperationRightFacade;
    Map<Account, List<EntityInfo>> _modelIntentionInfos;
    private List<Account> _partners;
    List<EntityInfo> _partnerEntityInfos;

    public List<EntityInfo> getModelIntentions() {
        return _modelIntentionFacade.getModelIntentionInfos(_sessionController.getAccountId(), DataSet.All, UserSet.DenotedUsers);
    }

    public List<EntityInfo> getModelIntentions4Account(int accountId) {
        List<EntityInfo> infos = new ArrayList<>();
        for (EntityInfo info : _partnerEntityInfos) {
            if (info.getAccountId() == accountId) {
                infos.add(info);
            }
        }
        return infos;
    }

    public List<Account> getPartners() {
        ensureInfos();
        return _partners;
    }

    private void ensureInfos() {
        if (_partners != null) {
            return;
        }
        List<CooperationRight> achievedRights = _cooperationRightFacade.getAchievedCooperationRights(_sessionController.getAccountId(), Feature.MODEL_INTENTION);
        Set<Integer> ids = new HashSet<>();
        for (CooperationRight right : achievedRights) {
            if (right.getCooperativeRight() != CooperativeRight.None) {
                ids.add(right.getOwnerId());
            }
        }
        ids.remove(_sessionController.getAccountId());
        if (_sessionController.isInekUser(Feature.MODEL_INTENTION)) {
            _partnerEntityInfos = _modelIntentionFacade.getModelIntentionInfos(_sessionController.getAccountId(), DataSet.All, UserSet.OtherUsers);
        } else {
            _partnerEntityInfos = _modelIntentionFacade.getModelIntentionInfos(ids, DataSet.All, UserSet.DenotedUsers);
            // remove entries, if not sealed and only sealed are allowesd visible
            // TODO: When switched to Java 8 replace this ugly code by streams with filter
            for (Iterator<EntityInfo> itr = _partnerEntityInfos.iterator(); itr.hasNext();) {
                EntityInfo entry = itr.next();
                if (entry.getStatus().getValue() < WorkflowStatus.Provided.getValue()) {
                    for (CooperationRight right : achievedRights) {
                        if (right.getOwnerId() == entry.getAccountId() && right.getCooperativeRight() == CooperativeRight.ReadSealed) {
                            itr.remove();
                            break;
                        }
                    }
                }
            }
        }
        ids.clear();
        for (EntityInfo info : _partnerEntityInfos) {
            ids.add(info.getAccountId());
        }
        _partners = _accountFacade.getAccountsForIds(ids);
    }

    public String newModelIntention() {
        return Pages.ModelIntentionTypeAndNumPat.URL();
    }

    public String editModelIntention(int modelId) {
        return Pages.ModelIntentionTypeAndNumPat.URL();
    }

    public String deleteModelIntention(int modelId) {
        ModelIntention intention = _modelIntentionFacade.find(modelId);
        if (intention == null) {
            return "";
        }
        if (_sessionController.isMyAccount(intention.getAccountId())) {
            if (intention.getStatus().getValue() <= WorkflowStatus.Provided.getValue()) {
                _modelIntentionFacade.remove(intention);
            } else {
                intention.setStatus(WorkflowStatus.Retired.getValue());
                _modelIntentionFacade.saveModelIntention(intention);
            }
        }
        return "";
    }

    public String printModelIntention(int modelId) {
        Utils.getFlash().put("headLine", Utils.getMessage("nameMODEL_INTENTION"));
        Utils.getFlash().put("targetPage", Pages.ModelIntentionSummary.URL());
        Utils.getFlash().put("printContent", DocumentationUtil.getDocumentation(_modelIntentionFacade.find(modelId)));
        return Pages.PrintView.URL();
    }

}
