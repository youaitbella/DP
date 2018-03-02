package org.inek.dataportal.feature.calculationhospital.tree.inek.sop;

import java.util.ArrayList;
import java.util.Collection;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;

/**
 *
 * @author muellermi
 */
public class RootTreeNodeObserver implements TreeNodeObserver {

    private final Instance<YearTreeNodeObserver> _yearTreeNodeObserverProvider;

    @Inject
    public RootTreeNodeObserver(Instance<YearTreeNodeObserver> yearTreeNodeObserverProvider) {
        _yearTreeNodeObserverProvider = yearTreeNodeObserverProvider;
    }

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        // todo: obtain years from database
        Collection<TreeNode> children = new ArrayList<>();
        children.add(YearTreeNode.create(treeNode, 2017, _yearTreeNodeObserverProvider.get()));
        children.add(YearTreeNode.create(treeNode, 2016, _yearTreeNodeObserverProvider.get()));
        return children;
    }

}
