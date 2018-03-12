package org.inek.dataportal.feature.specificfunction.backingbean.tree.hospital;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.feature.specificfunction.entity.SpecificFunctionRequest;
import org.inek.dataportal.feature.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.helper.tree.SpecificFunctionRequestTreeNode;
import org.inek.dataportal.helper.tree.entityTree.CustomerTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;

public class CustomerTreeNodeObserver implements TreeNodeObserver {

    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private ApplicationTools _appTools;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        int ik = ((CustomerTreeNode) treeNode).getIk();
        List<SpecificFunctionRequest> infos;
        if (treeNode.getParent() instanceof YearTreeNode) {
            int year = treeNode.getParent().getId();
            infos = obtainRequestsForRead(ik, year);
        } else {
            infos = obtainRequestsForEdit(ik);
        }
        Collection<TreeNode> children = new ArrayList<>();
        for (SpecificFunctionRequest info : infos) {
            children.add(SpecificFunctionRequestTreeNode.create(treeNode, info, null));
        }
        return children;
    }

    private List<SpecificFunctionRequest> obtainRequestsForRead(int ik, int year) {
        WorkflowStatus statusLow = WorkflowStatus.Provided;
        WorkflowStatus statusHigh = WorkflowStatus.Retired;
        return _specificFunctionFacade.obtainSpecificFunctionRequests(
                ik,
                year,
                statusLow,
                statusHigh);
    }

    private List<SpecificFunctionRequest> obtainRequestsForEdit(int ik ) {
        WorkflowStatus statusLow = WorkflowStatus.New;
        WorkflowStatus statusHigh = WorkflowStatus.ApprovalRequested;
        return _specificFunctionFacade.obtainSpecificFunctionRequests(
                ik,
                0,
                statusLow,
                statusHigh);
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
