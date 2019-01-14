/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.insurance.specificfunction.backingbean.tree.insurance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.AccessManager;
import static org.inek.dataportal.common.overall.AccessManager.canReadCompleted;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.insurance.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;

/**
 *
 * @author aitbellayo
 */
@Dependent
public class EditRootTreeNodeObserver implements TreeNodeObserver{
    @Inject private AccessManager _accessManager;
    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private SessionController _sessionController;
    @Inject private Instance<AccountTreeNodeObserver> _accountTreeNodeObserver;
    
    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainEditNodeChildren( treeNode);
    }
    
    private Collection<TreeNode> obtainEditNodeChildren(TreeNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.SPECIFIC_FUNCTION, canReadCompleted());
        List<Account> accounts = _specificFunctionFacade.loadAgreementAccounts(accountIds,
                WorkflowStatus.New, WorkflowStatus.ApprovalRequested);
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
                    create(treeNode, account, _accountTreeNodeObserver.get());
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            childNode.expand();  // auto-expand all edit nodes by default
        }
        return children;
    }
}
