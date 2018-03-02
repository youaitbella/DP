package org.inek.dataportal.feature.calculationhospital.tree.inek.sop;

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
        for (int year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL); year >= 2016; year--) {
            children.add(YearTreeNode.create(treeNode, year, _yearTreeNodeObserverProvider.get()));
        }
        return children;
    }

}
