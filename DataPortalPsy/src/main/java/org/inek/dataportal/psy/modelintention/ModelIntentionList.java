package org.inek.dataportal.psy.modelintention;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.cooperation.entities.CooperationRight;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.psy.modelintention.entities.ModelIntention;
import org.inek.dataportal.common.enums.CooperativeRight;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.psy.modelintention.enums.UserSet;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.cooperation.facade.CooperationFacade;
import org.inek.dataportal.common.data.cooperation.facade.CooperationRightFacade;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.psy.modelintention.facades.ModelIntentionFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.helper.structures.EntityInfo;
import org.inek.dataportal.common.utils.DocumentationUtil;

@Named
@RequestScoped
public class ModelIntentionList {

    @Inject private ModelIntentionFacade _modelIntentionFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccountFacade _accountFacade;
    @Inject private CooperationFacade _cooperationFacade;
    @Inject private CooperationRightFacade _cooperationRightFacade;
    private Map<Account, List<EntityInfo>> _modelIntentionInfos;
    private List<Account> _partners;
    private List<EntityInfo> _partnerEntityInfos;

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
        List<CooperationRight> achievedRights = _cooperationRightFacade
                .getAchievedCooperationRights(_sessionController.getAccountId(), Feature.MODEL_INTENTION);
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
            if (intention.getStatus().getId() <= WorkflowStatus.Provided.getId()) {
                _modelIntentionFacade.remove(intention);
            } else {
                intention.setStatus(WorkflowStatus.Retired.getId());
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
