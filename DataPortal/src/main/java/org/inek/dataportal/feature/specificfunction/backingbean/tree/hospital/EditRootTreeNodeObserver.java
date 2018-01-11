package org.inek.dataportal.feature.specificfunction.backingbean.tree.hospital;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.inek.dataportal.common.AccessManager;
import static org.inek.dataportal.common.AccessManager.canReadCompleted;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.icmt.Customer;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.feature.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.entityTree.CustomerTreeNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;

@Dependent
public class EditRootTreeNodeObserver implements TreeNodeObserver {

    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;
    @Inject private CustomerFacade _customerFacade;
    @Inject private Instance<AccountTreeNodeObserver> _accountTreeNodeObserverProvider;
    @Inject private Instance<CustomerTreeNodeObserver> _customerTreeNodeObserverProvider;

    @Override
    public void obtainChildren(TreeNode treeNode) {
        Collection<TreeNode> children = treeNode.getChildren();
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        children.clear();
        
        obtainCustomerNodes(oldChildren, treeNode, children);
        obtainAccountNodes(oldChildren, treeNode, children);
    }

    private void obtainCustomerNodes(
            List<? extends TreeNode> oldChildren, 
            TreeNode treeNode, 
            Collection<TreeNode> children) {
        for (int ik : _accessManager.retrieveAllowedManagedIks(Feature.SPECIFIC_FUNCTION)) {
            Optional<? extends TreeNode> existing = oldChildren
                    .stream()
                    .filter(n -> n instanceof CustomerTreeNode && n.getId() == ik)
                    .findFirst();
            CustomerTreeNode childNode = existing.isPresent()
                    ? (CustomerTreeNode) existing.get()
                    : createCusromerNode(treeNode, ik);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            childNode.expand();  // auto-expand all edit nodes by default
        }
    }

    private CustomerTreeNode createCusromerNode(TreeNode parent, int ik) {
        Customer customer = _customerFacade.getCustomerByIK(ik);
        return CustomerTreeNode.create(parent, customer, _customerTreeNodeObserverProvider.get());
    }

    private void obtainAccountNodes(
            List<? extends TreeNode> oldChildren, 
            TreeNode treeNode, 
            Collection<TreeNode> children) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.SPECIFIC_FUNCTION, canReadCompleted());
        Set<Integer> managedIks = _accessManager.retrieveAllManagedIks(Feature.SPECIFIC_FUNCTION);
        List<Account> accounts = _specificFunctionFacade.loadRequestAccounts(
                accountIds,
                WorkflowStatus.New,
                WorkflowStatus.ApprovalRequested);
        Account currentUser = _sessionController.getAccount();
        if (accounts.contains(currentUser)) {
            // ensure current user is first, if in list
            accounts.remove(currentUser);
            accounts.add(0, currentUser);
        }
        for (Account account : accounts) {
            Integer id = account.getId();
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == id).findFirst();
            AccountTreeNode childNode = existing.isPresent()
                    ? (AccountTreeNode) existing.get()
                    : AccountTreeNode.create(treeNode, account, _accountTreeNodeObserverProvider.get());
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            childNode.expand();  // auto-expand all edit nodes by default
        }
    }

    
}
