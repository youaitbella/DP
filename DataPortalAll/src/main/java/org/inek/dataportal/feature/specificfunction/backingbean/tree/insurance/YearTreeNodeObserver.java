/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.specificfunction.backingbean.tree.insurance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import org.inek.dataportal.common.AccessManager;
import static org.inek.dataportal.common.AccessManager.canReadSealed;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.feature.account.entities.Account;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.feature.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;

/**
 *
 * @author aitbellayo
 */
@Dependent
public class YearTreeNodeObserver implements TreeNodeObserver{
    
    @Inject private AccessManager _accessManager;
    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private SessionController _sessionController;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        return obtainYearNodeChildren((YearTreeNode) treeNode); //To change body of generated methods, choose Tools | Templates.
    }

    private Collection<TreeNode> obtainYearNodeChildren(YearTreeNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.SPECIFIC_FUNCTION, canReadSealed());
        List<Account> accounts = _specificFunctionFacade.loadAgreementAccountsForYear(accountIds, treeNode.getId(),
                WorkflowStatus.Provided, WorkflowStatus.Retired);
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
            if (account == currentUser) {
                // auto expand user's own data
                childNode.expand();
            }
        }
        return children;
    }
    
}
