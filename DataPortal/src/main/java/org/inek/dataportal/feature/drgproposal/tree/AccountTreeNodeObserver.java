/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.drgproposal.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.inek.dataportal.common.AccessManager;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.DrgProposalFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;
import org.inek.dataportal.helper.structures.ProposalInfo;
import org.inek.dataportal.helper.tree.ProposalInfoTreeNode;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;
import org.inek.portallib.tree.YearTreeNode;

/**
 *
 * @author aitbellayo
 */
@Dependent
public class AccountTreeNodeObserver implements TreeNodeObserver{
    
    @Inject private DrgProposalFacade _drgProposalFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainAccountNodeChildren((AccountTreeNode) treeNode);
    }
    private Collection<TreeNode> obtainAccountNodeChildren(AccountTreeNode treeNode) {
        int partnerId = treeNode.getId();
        List<ProposalInfo> infos;
        if (treeNode.getParent() instanceof YearTreeNode) {
            int year = treeNode.getParent().getId();
            infos = obtainProposalInfosForRead(partnerId, year);
        } else {
            infos = obtainProposalInfosForEdit(partnerId);
        }
        Collection<TreeNode> children = new ArrayList<>();
        for (ProposalInfo info : infos) {
            children.add(ProposalInfoTreeNode.create(treeNode, info, this));
        }
        return children;
    }
    private List<ProposalInfo> obtainProposalInfosForRead(int partnerId, int year) {
        DataSet dataSet;
        if (partnerId == _sessionController.getAccountId()) {
            dataSet = DataSet.AllSealed;
        } else {
            boolean canReadSealed = _accessManager.canReadSealed(Feature.DRG_PROPOSAL, partnerId);
            dataSet = canReadSealed ? DataSet.AllSealed : DataSet.None;
        }
        return _drgProposalFacade.getDrgProposalInfos(partnerId, year, dataSet);
    }

    private List<ProposalInfo> obtainProposalInfosForEdit(int partnerId) {
        DataSet dataSet;
        if (partnerId == _sessionController.getAccountId()) {
            dataSet = DataSet.AllOpen;
        } else {
            boolean canReadAlways = _accessManager.canReadAlways(Feature.DRG_PROPOSAL, partnerId);
            boolean canReadCompleted = _accessManager.canReadCompleted(Feature.DRG_PROPOSAL, partnerId);
            dataSet = canReadAlways ? DataSet.AllOpen
                    : canReadCompleted ? DataSet.ApprovalRequested : DataSet.None;
        }
        return _drgProposalFacade.getDrgProposalInfos(partnerId, -1, dataSet);
    }
}
