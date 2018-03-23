package org.inek.dataportal.feature.calculationhospital.tree.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.feature.calculationhospital.entities.CalcHospitalInfo;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.feature.calculationhospital.facades.CalcFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.helper.tree.CalcHospitalTreeNode;
import org.inek.dataportal.common.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;

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
        Account account = treeNode.getAccount();
        List<CalcHospitalInfo> infos;
        if (treeNode.getParent() instanceof YearTreeNode) {
            int year = treeNode.getParent().getId();
            infos = obtainCalculationHospitalInfosForRead(account, year);
        } else {
            infos = obtainCalculationHospitalInfosForEdit(account);
        }
        Collection<TreeNode> children = new ArrayList<>();
        for (CalcHospitalInfo info : infos) {
            children.add(CalcHospitalTreeNode.create(treeNode, info, null));
        }
        return children;
    }

    private List<CalcHospitalInfo> obtainCalculationHospitalInfosForRead(Account account, int year) {
        if (account == _sessionController.getAccount()) {
            return obtainOwnCalculationHospitalInfos(account, year, WorkflowStatus.Provided, WorkflowStatus.Retired);
        } else {
            return obtainPartnersCalculationHospitalInfosForRead(account, year);
        }
    }

    private List<CalcHospitalInfo> obtainCalculationHospitalInfosForEdit(Account account) {
        if (account == _sessionController.getAccount()) {
            return obtainOwnCalculationHospitalInfos(account, Utils.getTargetYear(Feature.CALCULATION_HOSPITAL),
                    WorkflowStatus.New, WorkflowStatus.ApprovalRequested);
        } else {
            return obtainPartnersCalculationHospitalInfosForEdit(account);
        }
    }

    public List<CalcHospitalInfo> obtainOwnCalculationHospitalInfos(
            Account account,
            int year,
            WorkflowStatus statusLow,
            WorkflowStatus statusHigh) {
        // todo: exclude managed Iks
        return _calcFacade
                .getListCalcInfo(account.getId(),
                        year,
                        statusLow,
                        statusHigh,
                        0);
    }

    public List<CalcHospitalInfo> obtainPartnersCalculationHospitalInfosForRead(Account account, int year) {
        List<CalcHospitalInfo> infos = new ArrayList<>();
        Set<Integer> managedIks = _accessManager.retrieveAllManagedIks(Feature.NUB);
        Set<Integer> ikSet = account.getFullIkSet();
        ikSet.removeAll(managedIks);
        for (int ik : ikSet) {
            if (!_accessManager.canReadSealed(Feature.CALCULATION_HOSPITAL, account.getId(), ik)) {
                continue;
            }
            WorkflowStatus statusLow = WorkflowStatus.Provided;
            WorkflowStatus statusHigh = WorkflowStatus.Retired;
            List<CalcHospitalInfo> infosForIk = _calcFacade.
                    getListCalcInfo(account.getId(), year, statusLow, statusHigh, ik);
            infos.addAll(infosForIk);
        }
        return infos;
    }

    public List<CalcHospitalInfo> obtainPartnersCalculationHospitalInfosForEdit(Account account) {
        List<CalcHospitalInfo> infos = new ArrayList<>();
        Set<Integer> managedIks = _accessManager.retrieveAllManagedIks(Feature.NUB);
        Set<Integer> ikSet = account.getFullIkSet();
        ikSet.removeAll(managedIks);
        for (int ik : ikSet) {
            if (!_accessManager.canReadCompleted(Feature.CALCULATION_HOSPITAL, account.getId(), ik)) {
                continue;
            }
            boolean canReadAlways = _accessManager.canReadAlways(Feature.CALCULATION_HOSPITAL, account.getId(), ik);
            WorkflowStatus statusLow = canReadAlways ? WorkflowStatus.New
                    : WorkflowStatus.ApprovalRequested;
            WorkflowStatus statusHigh = WorkflowStatus.ApprovalRequested;
            List<CalcHospitalInfo> infosForIk = _calcFacade.
                    getListCalcInfo(account.getId(), Utils.getTargetYear(Feature.CALCULATION_HOSPITAL), statusLow, statusHigh, ik);
            infos.addAll(infosForIk);
        }
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
