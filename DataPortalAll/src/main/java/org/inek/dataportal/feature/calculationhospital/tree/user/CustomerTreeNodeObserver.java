package org.inek.dataportal.feature.calculationhospital.tree.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.facades.calc.CalcFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.helper.tree.CalcHospitalTreeNode;
import org.inek.dataportal.common.tree.entityTree.CustomerTreeNode;

/**
 *
 * @author muellermi
 */
@Dependent
public class CustomerTreeNodeObserver implements TreeNodeObserver {

    private final CalcFacade _calcFacade;
    private final ApplicationTools _appTools;

    @Inject
    public CustomerTreeNodeObserver(
            CalcFacade calcFacade,
            ApplicationTools appTools) {
        _calcFacade = calcFacade;
        _appTools = appTools;
    }

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        int ik = ((CustomerTreeNode) treeNode).getIk();
        List<CalcHospitalInfo> infos;
        if (treeNode.getParent() instanceof YearTreeNode) {
            int year = treeNode.getParent().getId();
            infos = obtainInfos(year, WorkflowStatus.Provided, WorkflowStatus.Retired, ik);
        } else {
            int year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
            infos = obtainInfos(year, WorkflowStatus.New, WorkflowStatus.ApprovalRequested, ik);
        }

        Collection<TreeNode> children = new ArrayList<>();
        for (CalcHospitalInfo info : infos) {
            children.add(CalcHospitalTreeNode.create(treeNode, info, null));
        }
// if we need to keep state (e.g. when we aktive the checkbox feature), we need to recycle existing nodes like this:        
//        Collection<TreeNode> children = new ArrayList<>();
//        Collection<TreeNode> oldChildren = treeNode.getChildren();
//        for (CalcHospitalInfo info : infos) {
//            TreeNode node = oldChildren
//                    .stream()
//                    .filter(c -> c.getId() == info.getId())
//                    .findFirst()
//                    .orElseGet(() -> CalcHospitalTreeNode.create(treeNode, info, null));
//            children.add(node);
//        }
        return children;
    }

    private List<CalcHospitalInfo> obtainInfos(int year, WorkflowStatus statusLow, WorkflowStatus statusHigh, int ik) {
        List<CalcHospitalInfo> infos = new ArrayList<>();
        List<CalcHospitalInfo> infosForIk = _calcFacade.
                getListCalcInfo(0, year, statusLow, statusHigh, ik);
        infos.addAll(infosForIk);
        return infos;
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        Stream<CalcHospitalTreeNode> stream = treeNode.getChildren().stream().map(n -> (CalcHospitalTreeNode) n);
        Stream<CalcHospitalTreeNode> sorted;
        int direction = treeNode.isDescending() ? -1 : 1;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "ik":
                sorted = stream.sorted((n1, n2) -> direction * Integer.compare(n1.getCalcHospitalInfo().getIk(),
                        n2.getCalcHospitalInfo().getIk()));
                break;
            case "hospital":
                sorted = stream.sorted((n1, n2) -> direction * _appTools.retrieveHospitalInfo(n1.getCalcHospitalInfo().
                        getIk())
                        .compareTo(_appTools.retrieveHospitalInfo(n2.getCalcHospitalInfo().getIk())));
                break;
            case "name":
                sorted = stream.sorted((n1, n2) -> direction * n1.getCalcHospitalInfo().getName()
                        .compareTo(n2.getCalcHospitalInfo().getName()));
                break;
            case "date":
                sorted = stream.sorted((n1, n2) -> direction * n1.getCalcHospitalInfo().getLastChanged()
                        .compareTo(n2.getCalcHospitalInfo().getLastChanged()));
                break;
            case "status":
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }

}
