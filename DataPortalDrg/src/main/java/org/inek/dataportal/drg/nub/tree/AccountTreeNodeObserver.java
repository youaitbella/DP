package org.inek.dataportal.drg.nub.tree;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.common.helper.structures.ProposalInfo;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.tree.ProposalInfoTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;
import org.inek.dataportal.common.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.drg.nub.NubSessionTools;
import org.inek.dataportal.drg.nub.entities.NubRequest;
import org.inek.dataportal.drg.nub.facades.NubRequestFacade;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountTreeNodeObserver implements TreeNodeObserver {

    @Inject private NubRequestFacade _nubRequestFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;
    @Inject private NubSessionTools _nubSessionTools;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainAccountNodeChildren((AccountTreeNode) treeNode);
    }

    private Collection<TreeNode> obtainAccountNodeChildren(AccountTreeNode accountTreeNode) {
        Account partner = accountTreeNode.getAccount();
        List<ProposalInfo> infos;
        if (accountTreeNode.getParent() instanceof YearTreeNode) {
            int year = accountTreeNode.getParent().getId();
            infos = obtainNubInfosForRead(partner, year);
        } else {
            infos = obtainNubInfosForEdit(partner);
        }
        List<Integer> checked = new ArrayList<>();
        for (TreeNode child : accountTreeNode.getChildren()) {
            if (child.isChecked()) {
                checked.add(child.getId());
            }
        }
        Collection<TreeNode> children = new ArrayList<>();
        for (ProposalInfo info : infos) {
            ProposalInfoTreeNode node = ProposalInfoTreeNode.create(accountTreeNode, info, null);
            if (checked.contains(node.getId())) {
                node.setChecked(true);
            }
            children.add(node);
        }
        return children;
    }

    private List<ProposalInfo> obtainNubInfosForRead(Account account, int year) {
        List<ProposalInfo> infos = new ArrayList<>();
        Set<Integer> managedIks = _accessManager.retrieveAllManagedIks(Feature.NUB);
        // todo: for own account read all nubs which are not for a managed ik, independently from the account's ik set
//        if (account.getId() == _sessionController.getAccountId()){
//        }else {
//        }
        Set<Integer> ikSet = account.getFullIkSet();
        ikSet.removeAll(managedIks);
        boolean itsMe = account == _sessionController.getAccount();
        if (itsMe) {
            ikSet.add(0);
        }
        for (int ik : ikSet) {
            if (account.getId() != _sessionController.getAccountId()
                    && !_accessManager.canRead(Feature.NUB, account.getId(), ik)) {
                continue;
            }
            List<ProposalInfo> infosForIk = _nubRequestFacade.
                    getNubRequestInfos(account.getId(), ik, year, DataSet.AllSealed, getFilter());
            infos.addAll(infosForIk);
        }
        return infos;
    }

    private List<ProposalInfo> obtainNubInfosForEdit(Account account) {
        List<ProposalInfo> infos = new ArrayList<>();
        Set<Integer> managedIks = _accessManager.retrieveAllManagedIks(Feature.NUB);
        Set<Integer> ikSet = account.getFullIkSet();
        ikSet.removeAll(managedIks);
        boolean itsMe = account == _sessionController.getAccount();
        if (itsMe) {
            ikSet.add(0);
        }
        for (int ik : ikSet) {
            if (!itsMe && !_accessManager.canRead(Feature.NUB, account.getId(), ik)) {
                continue;
            }
            List<ProposalInfo> infosForIk = _nubRequestFacade.
                    getNubRequestInfos(account.getId(), ik, -1, DataSet.AllOpen, getFilter());
            infos.addAll(infosForIk);
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
