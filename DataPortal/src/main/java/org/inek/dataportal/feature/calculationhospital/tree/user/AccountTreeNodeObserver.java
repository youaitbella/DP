package org.inek.dataportal.feature.calculationhospital.tree.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.inek.dataportal.common.AccessManager;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.calc.CalcFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.tree.CalcHospitalTreeNode;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.TreeNode;
import org.inek.dataportal.helper.tree.TreeNodeObserver;
import org.inek.dataportal.helper.tree.YearTreeNode;

@Dependent
public class AccountTreeNodeObserver implements TreeNodeObserver {

    private final CalcFacade _calcFacade;
    private final SessionController _sessionController;
    private final AccessManager _accessManager;
    private final ApplicationTools _appTools;

    @Inject
    public AccountTreeNodeObserver(
            CalcFacade calcFacade,
            SessionController sessionController,
            AccessManager accessManager,
            ApplicationTools appTools) {
        _calcFacade = calcFacade;
        _sessionController = sessionController;
        _accessManager = accessManager;
        _appTools = appTools;
    }

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainAccountNodeChildren((AccountTreeNode) treeNode);
    }

    private Collection<TreeNode> obtainAccountNodeChildren(AccountTreeNode treeNode) {
        int partnerId = treeNode.getId();
        List<CalcHospitalInfo> infos;
        if (treeNode.getParent() instanceof YearTreeNode) {
            int year = treeNode.getParent().getId();
            infos = obtainCalculationHospitalInfosForRead(partnerId, year);
        } else {
            infos = obtainCalculationHospitalInfosForEdit(partnerId);
        }
        Collection<TreeNode> children = new ArrayList<>();
        for (CalcHospitalInfo info : infos) {
            children.add(CalcHospitalTreeNode.create(treeNode, info, null));
        }
        return children;
    }

    private List<CalcHospitalInfo> obtainCalculationHospitalInfosForRead(int partnerId, int year) {
        WorkflowStatus statusLow = WorkflowStatus.Provided;
        WorkflowStatus statusHigh = WorkflowStatus.Retired;
        if (partnerId != _sessionController.getAccountId()) {
            boolean canReadSealed = _accessManager.canReadSealed(Feature.CALCULATION_HOSPITAL, partnerId);
            if (!canReadSealed) {
                statusLow = WorkflowStatus.Unknown;
                statusHigh = WorkflowStatus.Unknown;
            }
        }
        return _calcFacade.getListCalcInfo(partnerId, year, statusLow, statusHigh);
    }

    private List<CalcHospitalInfo> obtainCalculationHospitalInfosForEdit(int partnerId) {
        WorkflowStatus statusLow;
        WorkflowStatus statusHigh;
        if (partnerId == _sessionController.getAccountId()) {
            statusLow = WorkflowStatus.New;
            statusHigh = WorkflowStatus.ApprovalRequested;
        } else {
            boolean canReadAlways = _accessManager.canReadAlways(Feature.CALCULATION_HOSPITAL, partnerId);
            boolean canReadCompleted = _accessManager.canReadCompleted(Feature.CALCULATION_HOSPITAL, partnerId);
            statusLow = canReadAlways ? WorkflowStatus.New
                    : canReadCompleted ? WorkflowStatus.ApprovalRequested : WorkflowStatus.Unknown;
            statusHigh = canReadAlways
                    || canReadCompleted ? WorkflowStatus.ApprovalRequested : WorkflowStatus.Unknown;
        }
        return _calcFacade.
                getListCalcInfo(partnerId, Utils.getTargetYear(Feature.CALCULATION_HOSPITAL), statusLow, statusHigh);
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
