package org.inek.dataportal.insurance.specificfunction.backingbean.tree.insurance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.insurance.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.common.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;

/**
 *
 * @author aitbellayo
 */
@Dependent
public class ViewRootTreeNodeObserver implements TreeNodeObserver{
    
    @Inject private AccessManager _accessManager;
    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private SessionController _sessionController;
    @Inject private Instance<AccountTreeNodeObserver> _accountTreeNodeObserver;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainViewNodeChildren(treeNode); 
    }

    private Collection<TreeNode> obtainViewNodeChildren(TreeNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.SPECIFIC_FUNCTION);
        List<Account> accounts = _specificFunctionFacade.loadAgreementAccounts(accountIds, 
                WorkflowStatus.Provided, WorkflowStatus.Retired);
        Account currentUser = _sessionController.getAccount();
        if (accounts.contains(currentUser)) {
            // ensure current user is first, if in list
            accounts.remove(currentUser);
            accounts.add(0, currentUser);
        }
        List<TreeNode> oldChildren = new ArrayList<>(treeNode.getChildren());
        Collection<TreeNode> children = new ArrayList<>();
        for (Account account : accounts) {
            int id = account.getId();
            TreeNode childNode = oldChildren
                    .stream()
                    .filter(n -> n.getId() == id)
                    .findFirst()
                    .orElseGet(() -> createAccountNode(treeNode, account));
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (account == currentUser) {
                // auto expand user's own data
                childNode.expand();
            }
        }
        return children;
    }
    
    private TreeNode createAccountNode(TreeNode parent, Account account) {
        return AccountTreeNode.create(parent, account, _accountTreeNodeObserver.get());
    }
    
}

