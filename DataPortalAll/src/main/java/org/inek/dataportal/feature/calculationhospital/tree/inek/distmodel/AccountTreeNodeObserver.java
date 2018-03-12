package org.inek.dataportal.feature.calculationhospital.tree.inek.distmodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.facades.calc.DistributionModelFacade;
import org.inek.dataportal.helper.tree.CalcHospitalTreeNode;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;

/**
 *
 * @author muellermi
 */
public class AccountTreeNodeObserver implements TreeNodeObserver {

    private final DistributionModelFacade _distributionModelFacade;
    private final ApplicationTools _appTools;
    private final DistributionModellTreeHandler _treeHandler;

    @Inject
    public AccountTreeNodeObserver(
            DistributionModelFacade distributionModelFacade,
            ApplicationTools appTools,
            DistributionModellTreeHandler treeHandler) {
        _distributionModelFacade = distributionModelFacade;
        _appTools = appTools;
        _treeHandler = treeHandler;
    }

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainAccountNodeChildren((AccountTreeNode) treeNode);
    }

    private Collection<TreeNode> obtainAccountNodeChildren(AccountTreeNode treeNode) {
        int year = ((YearTreeNode) treeNode.getParent()).getYear();
        List<CalcHospitalInfo> infos = _distributionModelFacade.
                getDistributionModelsByEmail(treeNode.getEmail(), year, _treeHandler.getFilter());
        Collection<TreeNode> children = new ArrayList<>();
        for (CalcHospitalInfo info : infos) {
            children.add(CalcHospitalTreeNode.create(treeNode, info, null));
        }
        return children;
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        Stream<CalcHospitalTreeNode> stream = treeNode.getChildren().stream().map(n -> (CalcHospitalTreeNode) n);
        Stream<CalcHospitalTreeNode> sorted;
        int direction = treeNode.isDescending() ? -1 : 1;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "ik":
                sorted = stream.sorted((n1, n2) -> direction * Integer.compare(n1.getCalcHospitalInfo().getIk(), n2.
                        getCalcHospitalInfo().getIk()));
                break;
            case "hospital":
                sorted = stream.sorted((n1, n2) -> direction * _appTools.retrieveHospitalInfo(n1.getCalcHospitalInfo().
                        getIk())
                        .compareTo(_appTools.retrieveHospitalInfo(n2.getCalcHospitalInfo().getIk())));
                break;
            case "name":
                sorted = stream.sorted((n1, n2) -> direction * n1.getCalcHospitalInfo().getName().compareTo(n2.
                        getCalcHospitalInfo().getName()));
                break;
            case "status":
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }

}
