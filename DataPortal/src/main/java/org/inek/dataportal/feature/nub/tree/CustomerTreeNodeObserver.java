package org.inek.dataportal.feature.nub.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.inject.Inject;
import org.inek.dataportal.common.AccessManager;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.nub.NubRequest;
import org.inek.dataportal.facades.NubRequestFacade;
import org.inek.dataportal.feature.nub.NubSessionTools;
import org.inek.dataportal.helper.tree.ProposalInfoTreeNode;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.entityTree.CustomerTreeNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;

public class CustomerTreeNodeObserver implements TreeNodeObserver {

    @Inject private NubRequestFacade _nubRequestFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;
    @Inject private NubSessionTools _nubSessionTools;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainCustomerNodeChildren((CustomerTreeNode) treeNode);
    }

    private Collection<TreeNode> obtainCustomerNodeChildren(CustomerTreeNode treeNode) {
        Collection<TreeNode> children = new ArrayList<>();
        return children;
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
        if (treeNode instanceof AccountTreeNode) {
            return sortAccountNodeChildren((AccountTreeNode) treeNode);
        }
        return treeNode.getChildren();
    }

    private Collection<TreeNode> sortAccountNodeChildren(AccountTreeNode treeNode) {
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
