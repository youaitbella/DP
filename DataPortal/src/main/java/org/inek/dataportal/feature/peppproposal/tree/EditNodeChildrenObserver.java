/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.peppproposal.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.inek.dataportal.common.AccessManager;
import static org.inek.dataportal.common.AccessManager.canReadCompleted;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.PeppProposalFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.portallib.tree.RootNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;

/**
 *
 * @author aitbellayo
 */
@Dependent
public class EditNodeChildrenObserver implements TreeNodeObserver{
    
    @Inject private PeppProposalFacade _peppProposalFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;
    @Inject private AccountFacade _accountFacade;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainEditNodeChildren((RootNode) treeNode);
    }
    
    private Collection<TreeNode> obtainEditNodeChildren(RootNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.PEPP_PROPOSAL, canReadCompleted());
        accountIds = _peppProposalFacade.
                checkAccountsForProposalOfYear(accountIds, -1, WorkflowStatus.New, WorkflowStatus.ApprovalRequested);
        List<Account> accounts = _accountFacade.getAccountsForIds(accountIds);
        Account currentUser = _sessionController.getAccount();
        if (accounts.contains(currentUser)) {
            // ensure current user is first, if in list
            accounts.remove(currentUser);
            accounts.add(0, currentUser);
        }
        List<? extends TreeNode> oldChildren = new ArrayList<>(treeNode.getChildren());
        Collection<TreeNode> children = new ArrayList<>();
        for (Account account : accounts) {
            Integer id = account.getId();
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == id).findFirst();
            AccountTreeNode childNode = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.
                    create(treeNode, account, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            childNode.expand();  // auto-expand all edit nodes by default
        }
        return children;
    }
    
}
