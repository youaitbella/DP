/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.drgproposal.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.AccessManager;
import static org.inek.dataportal.common.overall.AccessManager.canReadSealed;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.facades.DrgProposalFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;

/**
 *
 * @author aitbellayo
 */
@Dependent
public class ViewRootTreeNodeObserver implements TreeNodeObserver{
    @Inject private Instance<YearTreeNodeObserver> _yearTreeNodeObserverProvider;
    @Inject private DrgProposalFacade _drgProposalFacade;
    @Inject private AccessManager _accessManager;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.DRG_PROPOSAL, canReadSealed());
        List<Integer> years = _drgProposalFacade.getProposalYears(accountIds);
        int targetYear = Utils.getTargetYear(Feature.DRG_PROPOSAL);
        Collection<TreeNode> oldChildren = treeNode.getChildren();
        Collection<TreeNode> children = new ArrayList<>();
        for (Integer year : years) {
            TreeNode childNode = oldChildren
                    .stream()
                    .filter(n -> n.getId() == year)
                    .findFirst()
                    .orElseGet(() -> YearTreeNode.create(treeNode, year, _yearTreeNodeObserverProvider.get()));
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (year == targetYear) {
                childNode.expand();
            }
        }
        return children;
    }
}
