package org.inek.dataportal.feature.nub.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.inek.dataportal.common.AccessManager;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.nub.NubRequest;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.NubRequestFacade;
import org.inek.dataportal.feature.ikadmin.entity.AccessRight;
import org.inek.dataportal.feature.nub.NubSessionTools;
import org.inek.dataportal.helper.structures.ProposalInfo;
import org.inek.dataportal.helper.tree.ProposalInfoTreeNode;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;
import org.inek.portallib.tree.YearTreeNode;

@Dependent
public class AccountTreeNodeObserver implements TreeNodeObserver {

    @Inject private NubRequestFacade _nubRequestFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;
    @Inject private NubSessionTools _nubSessionTools;

    @Override
    public void obtainChildren(TreeNode treeNode, Collection<TreeNode> children) {
        obtainAccountNodeChildren((AccountTreeNode) treeNode, children);
    }

    private void obtainAccountNodeChildren(AccountTreeNode accountTreeNode, Collection<TreeNode> children) {
        int partnerId = accountTreeNode.getAccountId();
        List<ProposalInfo> infos;
        if (accountTreeNode.getParent() instanceof YearTreeNode) {
            int year = accountTreeNode.getParent().getId();
            infos = obtainNubInfosForRead(partnerId, year);
        } else {
            infos = obtainNubInfosForEdit(partnerId);
        }
        List<Integer> checked = new ArrayList<>();
        for (TreeNode child : accountTreeNode.copyChildren()) {
            if (child.isChecked()) {
                checked.add(child.getId());
            }
        }
        accountTreeNode.getChildren().clear();
        for (ProposalInfo info : infos) {
            ProposalInfoTreeNode node = ProposalInfoTreeNode.create(accountTreeNode, info, null);
            if (checked.contains(node.getId())) {
                node.setChecked(true);
            }
            accountTreeNode.getChildren().add(node);
        }
    }

    private List<ProposalInfo> obtainNubInfosForRead(int partnerId, int year) {
        List<ProposalInfo> infos = new ArrayList<>();
        List<AccessRight> accessRights = _accessManager.obtainAccessRights(Feature.NUB);
        if (partnerId == _sessionController.getAccountId()) {
            infos = _nubRequestFacade.
                    getNubRequestInfos(_sessionController.getAccountId(), year, DataSet.AllSealed, accessRights, getFilter());
        } else {
            Set<Integer> iks = _accessManager.getPartnerIks(Feature.NUB, partnerId);
            for (int ik : iks) {
                if (accessRights.stream().anyMatch(r -> r.getIk() == ik)) {
                    continue;
                }
                boolean canReadSealed = _accessManager.canReadSealed(Feature.NUB, partnerId, ik);
                DataSet dataSet = canReadSealed ? DataSet.AllSealed : DataSet.None;
                List<ProposalInfo> infosForIk = _nubRequestFacade.
                        getNubRequestInfos(partnerId, ik, year, dataSet, getFilter());
                infos.addAll(infosForIk);
            }
        }
        return infos;
    }

    private List<ProposalInfo> obtainNubInfosForEdit(int partnerId) {
        List<ProposalInfo> infos = new ArrayList<>();
        List<AccessRight> accessRights = _accessManager.obtainAccessRights(Feature.NUB);
        if (partnerId == _sessionController.getAccountId()) {
            infos = _nubRequestFacade.
                    getNubRequestInfos(_sessionController.getAccountId(), -1, DataSet.AllOpen, accessRights, getFilter());
        } else {
            Set<Integer> iks = _accessManager.getPartnerIks(Feature.NUB, partnerId);
            for (int ik : iks) {
                if (accessRights.stream().anyMatch(r -> r.getIk() == ik)) {
                    continue;
                }
                boolean canReadAlways = _accessManager.canReadAlways(Feature.NUB, partnerId, ik);
                boolean canReadCompleted = _accessManager.canReadCompleted(Feature.NUB, partnerId, ik);
                DataSet dataSet = canReadAlways ? DataSet.AllOpen
                        : canReadCompleted ? DataSet.ApprovalRequested : DataSet.None;
                List<ProposalInfo> infosForIk = _nubRequestFacade.
                        getNubRequestInfos(partnerId, ik, -1, dataSet, getFilter());
                infos.addAll(infosForIk);
            }
        }
        return infos;
    }

    private String getFilter() {
        String filter = _nubSessionTools.getNubFilter();
        if (!filter.isEmpty() && !filter.contains("%")) {
            filter = "%" + filter + "%";
        }
        return filter;
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode, Collection<TreeNode> children) {
        if (treeNode instanceof AccountTreeNode) {
            return sortAccountNodeChildren((AccountTreeNode) treeNode, children);
        }
        return children;
    }

    private Collection<TreeNode> sortAccountNodeChildren(AccountTreeNode treeNode, Collection<TreeNode> children) {
        Stream<ProposalInfoTreeNode> stream = children.stream().map(n -> (ProposalInfoTreeNode) n);
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
