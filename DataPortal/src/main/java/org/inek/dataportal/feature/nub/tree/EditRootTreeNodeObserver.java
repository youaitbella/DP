/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.nub.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.inek.dataportal.common.AccessManager;
import static org.inek.dataportal.common.AccessManager.canReadCompleted;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.NubRequestFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.feature.ikadmin.entity.AccessRight;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.entityTree.EntityTreeNode;
import org.inek.portallib.tree.RootNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;

/**
 *
 * @author muellermi
 */
public class EditRootTreeNodeObserver implements TreeNodeObserver {

    @Inject private NubRequestFacade _nubRequestFacade;
    @Inject private SessionController _sessionController;
    @Inject private Instance<AccountTreeNodeObserver> _accountTreeNodeObserverProvider;
    @Inject private AccessManager _accessManager;
    @Inject private AccountFacade _accountFacade;

    @Override
    public void obtainChildren(TreeNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.NUB, canReadCompleted());
        accountIds = _nubRequestFacade.
                checkAccountsForNubOfYear(accountIds, -1, WorkflowStatus.New, WorkflowStatus.ApprovalRequested);
        List<Account> accounts = _accountFacade.getAccountsForIds(accountIds);
        Account currentUser = _sessionController.getAccount();
        if (accounts.contains(currentUser)) {
            // ensure current user is first, if in list
            accounts.remove(currentUser);
            accounts.add(0, currentUser);
        } else {
            List<AccessRight> accessRights = _accessManager.obtainAccessRights(Feature.NUB);
            if (accessRights.stream().anyMatch(r -> r.canWrite() || r.canSeal())) {
                // the current user is not in, but might because of IK Admin
                accounts.add(0, currentUser);
            }
        }
        Collection<TreeNode> children = treeNode.getChildren();
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        children.clear();
        for (Account account : accounts) {
            Integer id = account.getId();
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == id).findFirst();
            EntityTreeNode childNode = existing.isPresent()
                    ? (EntityTreeNode) existing.get()
                    : AccountTreeNode.create(treeNode, account, _accountTreeNodeObserverProvider.get());
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            childNode.expand();  // auto-expand all edit nodes by default
        }
    }

}
