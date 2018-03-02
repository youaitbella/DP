package org.inek.dataportal.feature.calculationhospital.tree.inek.sop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.facades.calc.CalcSopFacade;
import org.inek.dataportal.helper.tree.CalcHospitalTreeNode;

/**
 *
 * @author muellermi
 */
public class YearTreeNodeObserver implements TreeNodeObserver{

    private final CalcSopFacade _calcFacade;
    private final ApplicationTools _appTools;
    private final SopTreeHandler _treeHandler;


    @Inject
    public YearTreeNodeObserver(
            CalcSopFacade calcFacade,
            ApplicationTools appTools,
            SopTreeHandler treeHandler) {
        _calcFacade = calcFacade;
        _appTools = appTools;
        _treeHandler = treeHandler;
    }
    
    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        int year = ((YearTreeNode) treeNode).getYear();
        List<CalcHospitalInfo> infos = _calcFacade.retrieveSopForInek(year, _treeHandler.getFilter());
        Collection<TreeNode> children = new ArrayList<>();
        for (CalcHospitalInfo info : infos) {
            children.add(CalcHospitalTreeNode.create(treeNode, info, null));
        }
        return children;
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        int direction = treeNode.isDescending() ? -1 : 1;
        Stream<CalcHospitalTreeNode> stream = treeNode.getChildren().stream().map(n -> (CalcHospitalTreeNode) n);
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "ik":
                stream = stream.sorted((n1, n2) -> direction * Integer.compare(n1.getCalcHospitalInfo().getIk(), n2.
                        getCalcHospitalInfo().getIk()));
                break;
            case "hospital":
                stream = stream
                        .sorted((n1, n2) -> direction * _appTools.retrieveHospitalInfo(n1.getCalcHospitalInfo().getIk())
                        .compareTo(_appTools.retrieveHospitalInfo(n2.getCalcHospitalInfo().getIk())));
                break;
            case "name":
                stream = stream.sorted((n1, n2) -> direction * n1.getCalcHospitalInfo().getName().compareTo(n2.
                        getCalcHospitalInfo().getName()));
                break;
            default:
        }
        return stream.collect(Collectors.toList());
    }
}