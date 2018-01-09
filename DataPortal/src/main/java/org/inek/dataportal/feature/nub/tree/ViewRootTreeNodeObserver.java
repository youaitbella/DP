/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.nub.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.inek.dataportal.common.AccessManager;
import static org.inek.dataportal.common.AccessManager.canReadSealed;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.NubRequestFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.portallib.tree.RootNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;
import org.inek.portallib.tree.YearTreeNode;

/**
 *
 * @author muellermi
 */
public class ViewRootTreeNodeObserver implements TreeNodeObserver {

    @Inject private NubRequestFacade _nubRequestFacade;
    @Inject private AccessManager _accessManager;
    @Inject private Instance<YearTreeNodeObserver> _yearTreeNodeObserverProvider;

    @Override
    public void obtainChildren(TreeNode treeNode, Collection<TreeNode> children) {
        obtainNubViewNodeChildren((RootNode) treeNode, children);
    }

    private void obtainNubViewNodeChildren(RootNode node, Collection<TreeNode> children) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.NUB, canReadSealed());
        List<Integer> years = _nubRequestFacade.getNubYears(accountIds);
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        int targetYear = Utils.getTargetYear(Feature.NUB);
        children.clear();
        for (Integer year : years) {
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == year).findFirst();
            YearTreeNode childNode = existing.isPresent()
                    ? (YearTreeNode) existing.get()
                    : YearTreeNode.create(node, year, _yearTreeNodeObserverProvider.get());
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (year == targetYear) {
                childNode.expand();
            }
        }
    }

}
