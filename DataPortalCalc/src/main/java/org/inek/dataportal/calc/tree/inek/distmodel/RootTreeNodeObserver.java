package org.inek.dataportal.calc.tree.inek.distmodel;

import java.util.ArrayList;
import java.util.Collection;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;

/**
 *
 * @author muellermi
 */
public class RootTreeNodeObserver implements TreeNodeObserver{

    private final Instance<YearTreeNodeObserver> _yearTreeNodeObserverProvider;

    @Inject
    public RootTreeNodeObserver(Instance<YearTreeNodeObserver> yearTreeNodeObserverProvider) {
        _yearTreeNodeObserverProvider = yearTreeNodeObserverProvider;
    }

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        Collection<TreeNode> oldChildren = treeNode.getChildren();

        Collection<TreeNode> children = new ArrayList<>();
        for (int year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL); year >= 2016; year--) {
            children.add(getNode(oldChildren, year, treeNode));
        }
        return children;
    }

    private TreeNode getNode(Collection<TreeNode> oldChildren, int year, TreeNode treeNode) {
        TreeNode node = oldChildren
                .stream()
                .filter(n -> ((YearTreeNode)n).getYear() == year)
                .findFirst()
                .orElseGet(() -> YearTreeNode.create(treeNode, year, _yearTreeNodeObserverProvider.get()));
        return node;
    }
}
