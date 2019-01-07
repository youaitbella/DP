package org.inek.dataportal.drg.specificfunction.backingbean.tree.hospital;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.drg.specificfunction.entity.SpecificFunctionRequest;
import org.inek.dataportal.drg.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.drg.specificfunction.backingbean.tree.SpecificFunctionRequestTreeNode;
import org.inek.dataportal.common.tree.entityTree.CustomerTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;

public class CustomerTreeNodeObserver implements TreeNodeObserver {

    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private ApplicationTools _appTools;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        int ik = ((CustomerTreeNode) treeNode).getIk();
        List<SpecificFunctionRequest> infos;
        if (treeNode.getParent().getId() == 1) {
            infos = obtainRequestsForEdit(ik);
        } else {
            infos = obtainRequestsForRead(ik);
        }
        Collection<TreeNode> children = new ArrayList<>();
        for (SpecificFunctionRequest info : infos) {
            children.add(SpecificFunctionRequestTreeNode.create(treeNode, info, null));
        }
        return children;
    }

    private List<SpecificFunctionRequest> obtainRequestsForRead(int ik) {
        WorkflowStatus statusLow = WorkflowStatus.Provided;
        WorkflowStatus statusHigh = WorkflowStatus.Retired;
        return _specificFunctionFacade.obtainSpecificFunctionRequests(
                ik,
                statusLow,
                statusHigh);
    }

    private List<SpecificFunctionRequest> obtainRequestsForEdit(int ik ) {
        WorkflowStatus statusLow = WorkflowStatus.New;
        WorkflowStatus statusHigh = WorkflowStatus.ApprovalRequested;
        return _specificFunctionFacade.obtainSpecificFunctionRequests(
                ik,
                statusLow,
                statusHigh);
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        return Sorter.obtainSortedChildren(treeNode, _appTools);
    }

}
