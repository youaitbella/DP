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
import static org.inek.dataportal.common.AccessManager.canReadSealed;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.icmt.entities.Customer;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.facades.NubRequestFacade;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.cooperation.facade.CooperationRightFacade;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.entityTree.CustomerTreeNode;
import org.inek.dataportal.helper.tree.entityTree.EntityTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;

/**
 *
 * @author muellermi
 */
public class YearTreeNodeObserver implements TreeNodeObserver {

    @Inject private NubRequestFacade _nubRequestFacade;
    @Inject private SessionController _sessionController;
    @Inject private Instance<AccountTreeNodeObserver> _accountTreeNodeObserverProvider;
    @Inject private Instance<CustomerTreeNodeObserver> _customerTreeNodeObserverProvider;
    @Inject private AccessManager _accessManager;
    @Inject private AccountFacade _accountFacade;
    @Inject private CustomerFacade _customerFacade;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        List<TreeNode> oldChildren = new ArrayList<>(treeNode.getChildren());

        Collection<TreeNode> children = obtainCustomerNodes(oldChildren, treeNode);
        children.addAll(obtainAccountNodes(oldChildren, treeNode));
        return children;
    }

    private Collection<TreeNode> obtainCustomerNodes(
            List<TreeNode> oldChildren,
            TreeNode treeNode) {
        Collection<TreeNode> children = new ArrayList<>();
        for (int ik : _accessManager.retrieveAllowedManagedIks(Feature.NUB)) {
            TreeNode childNode = oldChildren
                    .stream()
                    .filter(n -> n instanceof CustomerTreeNode && n.getId() == ik)
                    .findFirst()
                    .orElseGet(() -> createCustomerNode(treeNode, ik));
            children.add((TreeNode) childNode);
        }
        return children;
    }

    private CustomerTreeNode createCustomerNode(TreeNode parent, int ik) {
        Customer customer = _customerFacade.getCustomerByIK(ik);
        return CustomerTreeNode.create(parent, customer, _customerTreeNodeObserverProvider.get());
    }

    private Collection<TreeNode> obtainAccountNodes(
            List<TreeNode> oldChildren,
            TreeNode treeNode) {
        Collection<TreeNode> children = new ArrayList<>();
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.NUB, canReadSealed());
        Set<Integer> managedIks = _accessManager.retrieveAllManagedIks(Feature.NUB);
        List<Account> accounts = _nubRequestFacade.
                checkAccountsForNubOfYear(accountIds, treeNode.getId(), WorkflowStatus.Provided, WorkflowStatus.Retired, managedIks);
        Account currentUser = _sessionController.getAccount();
        if (accounts.contains(currentUser)) {
            // ensure current user is first, if in list
            accounts.remove(currentUser);
            accounts.add(0, currentUser);
        }
        for (Account account : accounts) {
            Integer id = account.getId();
            TreeNode childNode = oldChildren
                    .stream()
                    .filter(n -> n instanceof AccountTreeNode && n.getId() == id)
                    .findFirst()
                    .orElseGet(() -> AccountTreeNode.create(treeNode, account, _accountTreeNodeObserverProvider.get()));
            children.add((TreeNode) childNode);
            if (account == currentUser) {
                childNode.expand();
            }
        }
        return children;
    }

}
