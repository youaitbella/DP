/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.peppproposal.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.helper.structures.ProposalInfo;
import org.inek.dataportal.common.tree.ProposalInfoTreeNode;
import org.inek.dataportal.psy.peppproposal.facades.PeppProposalFacade;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;
import org.inek.dataportal.common.tree.entityTree.AccountTreeNode;

/**
 *
 * @author aitbellayo
 */
@Dependent
public class AccountTreeNodeObserver implements TreeNodeObserver{
    @Inject private PeppProposalFacade _peppProposalFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainAccountNodeChildren((AccountTreeNode) treeNode); //To change body of generated methods, choose Tools | Templates.
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
            children.add(ProposalInfoTreeNode.create(treeNode, info, null));
        }
        return children;
    }
    
    private List<ProposalInfo> obtainProposalInfosForRead(int partnerId, int year) {
        DataSet dataSet;
        if (partnerId == _sessionController.getAccountId()) {
            dataSet = DataSet.AllSealed;
        } else {
            boolean canReadSealed = _accessManager.canRead(Feature.PEPP_PROPOSAL, partnerId);
            dataSet = canReadSealed ? DataSet.AllSealed : DataSet.None;
        }
        return _peppProposalFacade.getPeppProposalInfos(partnerId, year, dataSet);
    }

    private List<ProposalInfo> obtainProposalInfosForEdit(int partnerId) {
        DataSet dataSet;
        if (partnerId == _sessionController.getAccountId()) {
            dataSet = DataSet.AllOpen;
        } else {
            boolean canRead = _accessManager.canRead(Feature.PEPP_PROPOSAL, partnerId);
            dataSet = canRead ? DataSet.AllOpen : DataSet.None;
        }
        return _peppProposalFacade.getPeppProposalInfos(partnerId, -1, dataSet);
    }
    
    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        Stream<ProposalInfoTreeNode> stream = treeNode.getChildren().stream().map(n -> (ProposalInfoTreeNode) n);
        Stream<ProposalInfoTreeNode> sorted;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "id":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n2.getProposalInfo().getId(), n1.
                            getProposalInfo().getId()));
                } else {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n1.getProposalInfo().getId(), n2.
                            getProposalInfo().getId()));
                }
                break;
            case "name":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> n2.getProposalInfo().getName().compareTo(n1.getProposalInfo().
                            getName()));
                } else {
                    sorted = stream.sorted((n1, n2) -> n1.getProposalInfo().getName().compareTo(n2.getProposalInfo().
                            getName()));
                }
                break;
            case "status":
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }
    
}
