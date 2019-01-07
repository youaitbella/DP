package org.inek.dataportal.drg.specificfunction.backingbean.tree.hospital;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.drg.specificfunction.entity.SpecificFunctionRequest;
import org.inek.dataportal.drg.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.common.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.drg.specificfunction.backingbean.tree.SpecificFunctionRequestTreeNode;

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
        if (treeNode.getParent() == null // within part, there is no parent
                || treeNode.getParent().getId() == 1) {
            infos = obtainRequestsForEdit(partner);
        } else {
            infos = obtainRequestsForRead(partner);
        }
        Collection<TreeNode> children = new ArrayList<>();
        for (SpecificFunctionRequest info : infos) {
            children.add(SpecificFunctionRequestTreeNode.create(treeNode, info, null));
        }
        return children;
    }

    private List<SpecificFunctionRequest> obtainRequestsForRead(Account account) {
        List<SpecificFunctionRequest> requests = new ArrayList<>();
        Set<Integer> ikSet = account.getFullIkSet();
        ikSet.removeAll(_accessManager.retrieveAllManagedIks(Feature.SPECIFIC_FUNCTION));
        for (int ik : ikSet) {
            if (account != _sessionController.getAccount()
                    && !_accessManager.canReadSealed(Feature.SPECIFIC_FUNCTION, account.getId(), ik)) {
                continue;
            }
            WorkflowStatus statusLow = WorkflowStatus.Provided;
            WorkflowStatus statusHigh = WorkflowStatus.Retired;
            List<SpecificFunctionRequest> ikRequests = _specificFunctionFacade.obtainSpecificFunctionRequests(
                    account.getId(),
                    ik,
                    statusLow,
                    statusHigh);
            requests.addAll(ikRequests);
        }

        return requests;
    }

    private List<SpecificFunctionRequest> obtainRequestsForEdit(Account account) {
        List<SpecificFunctionRequest> requests = new ArrayList<>();
        Set<Integer> ikSet = account.getFullIkSet();
        ikSet.removeAll(_accessManager.retrieveAllManagedIks(Feature.SPECIFIC_FUNCTION));
        for (int ik : ikSet) {
            boolean itsMe = account == _sessionController.getAccount();
            if (!itsMe && !_accessManager.canReadCompleted(Feature.SPECIFIC_FUNCTION, account.getId(), ik)) {
                continue;
            }
            boolean canReadAlways = itsMe || _accessManager.
                    canReadAlways(Feature.SPECIFIC_FUNCTION, account.getId(), ik);
            WorkflowStatus statusLow = canReadAlways ? WorkflowStatus.New : WorkflowStatus.ApprovalRequested;
            WorkflowStatus statusHigh = WorkflowStatus.ApprovalRequested;
            List<SpecificFunctionRequest> ikRequests = _specificFunctionFacade.obtainSpecificFunctionRequests(
                    account.getId(),
                    ik,
                    statusLow,
                    statusHigh);
            requests.addAll(ikRequests);
        }

        return requests;
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        return Sorter.obtainSortedChildren(treeNode, _appTools);
    }

}
