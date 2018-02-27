/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.peppproposal.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.inek.dataportal.common.AccessManager;
import static org.inek.dataportal.common.AccessManager.canReadSealed;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.facades.PeppProposalFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.tree.RootNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;

/**
 *
 * @author aitbellayo
 */
@Dependent
public class ViewRootTreeNodeObserver implements TreeNodeObserver{

    @Inject private PeppProposalFacade _peppProposalFacade;
    @Inject private AccessManager _accessManager;
    @Inject private Instance<YearTreeNodeObserver> _yearTreeNodeObserverProvider;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainViewNodeChildren((RootNode) treeNode);
    }
    private Collection<TreeNode> obtainViewNodeChildren(RootNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.PEPP_PROPOSAL, canReadSealed());
        List<Integer> years = _peppProposalFacade.getProposalYears(accountIds);
        int targetYear = Utils.getTargetYear(Feature.PEPP_PROPOSAL);
        List<? extends TreeNode> oldChildren = new ArrayList<>(treeNode.getChildren());
        Collection<TreeNode> children = new ArrayList<>();
        for (Integer year : years) {
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == year).findFirst();
            YearTreeNode childNode = existing.isPresent() ? (YearTreeNode) existing.get() : YearTreeNode.
                    create(treeNode, year, _yearTreeNodeObserverProvider.get());
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (year == targetYear) {
                childNode.expand();
            }
        }
        return children;
    }
}
