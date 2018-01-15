package org.inek.dataportal.feature.specificfunction.backingbean.tree.hospital;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.inek.dataportal.common.AccessManager;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.feature.specificfunction.entity.SpecificFunctionRequest;
import org.inek.dataportal.feature.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.helper.tree.SpecificFunctionRequestTreeNode;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;
import org.inek.portallib.tree.YearTreeNode;

/**
 *
 * @author muellermi
 */
@Dependent
public class AccountTreeNodeObserver implements TreeNodeObserver {

    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;
    @Inject private ApplicationTools _appTools;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        Account partner = ((AccountTreeNode) treeNode).getAccount();
        List<SpecificFunctionRequest> infos;
        if (treeNode.getParent() instanceof YearTreeNode) {
            int year = treeNode.getParent().getId();
            infos = obtainRequestsForRead(partner, year);
        } else {
            infos = obtainRequestsForEdit(partner);
        }
        Collection<TreeNode> children = new ArrayList<>();
        for (SpecificFunctionRequest info : infos) {
            children.add(SpecificFunctionRequestTreeNode.create(treeNode, info, null));
        }
        return children;
    }

    private List<SpecificFunctionRequest> obtainRequestsForRead(Account partner, int year) {
        List<SpecificFunctionRequest> requests = new ArrayList<>();
        Set<Integer> managedIks = _accessManager.retrieveAllManagedIks(Feature.SPECIFIC_FUNCTION);
        for (int ik : partner.getFullIkSet()) {
            if (managedIks.contains(ik)) {
                continue;
            }
            WorkflowStatus statusLow = WorkflowStatus.Provided;
            WorkflowStatus statusHigh = WorkflowStatus.Retired;
            if (partner != _sessionController.getAccount()) {
                boolean canReadSealed = _accessManager.canReadSealed(Feature.SPECIFIC_FUNCTION, partner.getId(), ik);
                if (!canReadSealed) {
                    statusLow = WorkflowStatus.Unknown;
                    statusHigh = WorkflowStatus.Unknown;
                }
            }
            List<SpecificFunctionRequest> ikRequests = _specificFunctionFacade.obtainSpecificFunctionRequests(
                    partner.getId(),
                    ik,
                    year,
                    statusLow,
                    statusHigh);
            requests.addAll(ikRequests);
        }

        return requests;
    }

    private List<SpecificFunctionRequest> obtainRequestsForEdit(Account partner) {
        List<SpecificFunctionRequest> requests = new ArrayList<>();
        Set<Integer> managedIks = _accessManager.retrieveAllManagedIks(Feature.SPECIFIC_FUNCTION);
        for (int ik : partner.getFullIkSet()) {
            if (managedIks.contains(ik)) {
                continue;
            }
            WorkflowStatus statusLow = WorkflowStatus.New;
            WorkflowStatus statusHigh = WorkflowStatus.ApprovalRequested;
            if (partner != _sessionController.getAccount()) {
                boolean canReadAlways = _accessManager.canReadAlways(Feature.SPECIFIC_FUNCTION, partner.getId(), ik);
                boolean canReadCompleted = _accessManager.
                        canReadCompleted(Feature.SPECIFIC_FUNCTION, partner.getId(), ik);
                statusLow = canReadAlways ? WorkflowStatus.New
                        : canReadCompleted ? WorkflowStatus.ApprovalRequested : WorkflowStatus.Unknown;
                statusHigh = canReadAlways
                        || canReadCompleted ? WorkflowStatus.ApprovalRequested : WorkflowStatus.Unknown;
            }
            List<SpecificFunctionRequest> ikRequests = _specificFunctionFacade.obtainSpecificFunctionRequests(
                    partner.getId(),
                    ik,
                    0,
                    statusLow,
                    statusHigh);
            requests.addAll(ikRequests);
        }

        return requests;
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        Stream<SpecificFunctionRequestTreeNode> stream = treeNode.getChildren().stream().
                map(n -> (SpecificFunctionRequestTreeNode) n);
        Stream<SpecificFunctionRequestTreeNode> sorted;
        int direction = treeNode.isDescending() ? -1 : 1;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "id":
                sorted = stream.sorted((n1, n2) -> direction * Integer.compare(n1.getSpecificFunctionRequest().getId(),
                        n2.getSpecificFunctionRequest().getId()));
                break;
            case "hospital":
                sorted = stream.sorted((n1, n2) -> direction * _appTools.retrieveHospitalInfo(n1.
                        getSpecificFunctionRequest().getIk())
                        .compareTo(_appTools.retrieveHospitalInfo(n2.getSpecificFunctionRequest().getIk())));
                break;
            case "date":
                sorted = stream.sorted((n1, n2) -> direction * n1.getSpecificFunctionRequest().getLastChanged()
                        .compareTo(n2.getSpecificFunctionRequest().getLastChanged()));
                break;
            case "status":
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }

}
