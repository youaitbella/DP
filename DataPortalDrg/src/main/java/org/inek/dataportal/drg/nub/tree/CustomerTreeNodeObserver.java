package org.inek.dataportal.drg.nub.tree;

import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.common.helper.structures.ProposalInfo;
import org.inek.dataportal.common.tree.ProposalInfoTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;
import org.inek.dataportal.common.tree.entityTree.CustomerTreeNode;
import org.inek.dataportal.drg.nub.NubSessionTools;
import org.inek.dataportal.drg.nub.entities.NubRequest;
import org.inek.dataportal.drg.nub.facades.NubRequestFacade;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CustomerTreeNodeObserver implements TreeNodeObserver {

    @Inject
    private NubRequestFacade _nubRequestFacade;
    @Inject
    private NubSessionTools _nubSessionTools;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        int ik = ((CustomerTreeNode) treeNode).getIk();
        List<ProposalInfo> infos;
        if (treeNode.getParent() instanceof YearTreeNode) {
            int year = treeNode.getParent().getId();
            infos = obtainRequestsForRead(ik, year);
        } else {
            infos = obtainRequestsForEdit(ik);
        }
        Collection<TreeNode> oldChildren = treeNode.getChildren();
        Collection<TreeNode> children = new ArrayList<>();
        for (ProposalInfo info : infos) {
            TreeNode node = oldChildren
                    .stream()
                    .filter(c -> c.getId() == info.getId())
                    .findFirst()
                    .orElseGet(() -> ProposalInfoTreeNode.create(treeNode, info, null));
            children.add(node);
        }
        return children;
    }

    private List<ProposalInfo> obtainRequestsForRead(int ik, int year) {
        return _nubRequestFacade.getNubRequestInfos(-1, ik, year, DataSet.AllSealed, getFilter());
    }

    private List<ProposalInfo> obtainRequestsForEdit(int ik) {
        return _nubRequestFacade.getNubRequestInfos(-1, ik, 0, DataSet.AllOpen, getFilter());
    }

    private String getFilter() {
        String filter = _nubSessionTools.getNubFilter();
        if (!filter.isEmpty() && !filter.contains("%")) {
            filter = "%" + filter + "%";
        }
        return filter;
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        Stream<ProposalInfoTreeNode> stream = treeNode.getChildren().stream().map(n -> (ProposalInfoTreeNode) n);
        Stream<ProposalInfoTreeNode> sorted;
        int direction = treeNode.isDescending() ? -1 : 1;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "id":
                sorted = stream.sorted((n1, n2) -> direction * Integer.compare(n1.getProposalInfo().getId(), n2.
                        getProposalInfo().getId()));
                break;
            case "ik":
                sorted = stream.sorted((n1, n2) -> direction * Integer.compare(n1.getProposalInfo().getIk(), n2.
                        getProposalInfo().getIk()));
                break;
            case "name":
                sorted = stream.sorted((n1, n2) -> direction * n1.getProposalInfo().getName().compareTo(n2.
                        getProposalInfo().getName()));
                break;
            case "status":
                sorted = stream.sorted((n1, n2) -> direction * getExternalState(n1).compareTo(getExternalState(n2)));
                break;
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }

    private String getExternalState(ProposalInfoTreeNode node) {
        int id = node.getProposalInfo().getId();
        NubRequest nubRequest = _nubRequestFacade.find(id);
        if (nubRequest == null) {
            return "";
        }
        return nubRequest.getExternalState();
    }

}
