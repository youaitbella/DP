/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.peppproposal.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.psy.peppproposal.facades.PeppProposalFacade;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;
import org.inek.dataportal.common.tree.entityTree.AccountTreeNode;

/**
 *
 * @author aitbellayo
 */
@Dependent
public class YearTreeNodeObserver implements TreeNodeObserver{
    @Inject private PeppProposalFacade _peppProposalFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;
    @Inject private AccountFacade _accountFacade;
    @Inject private Instance<AccountTreeNodeObserver> _accountTreeNodeObserverProvider;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainYearNodeChildren((YearTreeNode) treeNode);
    }
    
    private Collection<TreeNode> obtainYearNodeChildren(YearTreeNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.PEPP_PROPOSAL);
        accountIds = _peppProposalFacade.
                checkAccountsForProposalOfYear(accountIds, treeNode.getId(), WorkflowStatus.Provided, WorkflowStatus.Retired);
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
            AccountTreeNode childNode = existing.isPresent() 
                    ? (AccountTreeNode) existing.get() 
                    : AccountTreeNode.create(treeNode, account, _accountTreeNodeObserverProvider.get());
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (account == currentUser) {
                // auto expand user's own data
                childNode.expand();
            }
        }
        return children;
    }
}
