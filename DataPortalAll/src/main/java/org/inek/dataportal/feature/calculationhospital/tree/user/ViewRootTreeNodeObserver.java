package org.inek.dataportal.feature.calculationhospital.tree.user;

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
import org.inek.dataportal.facades.calc.CalcFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;

@Dependent
public class ViewRootTreeNodeObserver implements TreeNodeObserver{
    private final CalcFacade _calcFacade;
    private final AccessManager _accessManager;
    private final Instance<YearTreeNodeObserver> _yearTreeNodeObserverProvider;

    @Inject
    public ViewRootTreeNodeObserver(
            CalcFacade calcFacade,
            AccessManager accessManager,
            Instance<YearTreeNodeObserver> yearTreeNodeObserverProvider) {
        _calcFacade = calcFacade;
        _accessManager = accessManager;
        _yearTreeNodeObserverProvider = yearTreeNodeObserverProvider;
    }


    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.CALCULATION_HOSPITAL, canReadSealed());
        List<Integer> years = _calcFacade.getCalcYears(accountIds);
        int targetYear = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
        List<? extends TreeNode> oldChildren = new ArrayList<>(treeNode.getChildren());
        Collection<TreeNode> children = new ArrayList<>();
        for (Integer year : years) {
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == year).findFirst();
            YearTreeNode childNode = existing.isPresent()
                    ? (YearTreeNode) existing.get()
                    : YearTreeNode.create(treeNode, year, _yearTreeNodeObserverProvider.get());
            children.add((TreeNode) childNode);
            if (year == targetYear) {
                childNode.expand();
            }
        }
        return children;
    }
    
}
