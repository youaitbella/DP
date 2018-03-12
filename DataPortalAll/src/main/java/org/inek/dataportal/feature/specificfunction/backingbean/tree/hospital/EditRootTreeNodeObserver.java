package org.inek.dataportal.feature.specificfunction.backingbean.tree.hospital;

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
import org.inek.dataportal.common.data.icmt.entities.Customer;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.feature.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.entityTree.CustomerTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;

@Dependent
public class EditRootTreeNodeObserver implements TreeNodeObserver {

    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;
    @Inject private CustomerFacade _customerFacade;
    @Inject private Instance<AccountTreeNodeObserver> _accountTreeNodeObserverProvider;
    @Inject private Instance<CustomerTreeNodeObserver> _customerTreeNodeObserverProvider;

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        List<? extends TreeNode> oldChildren = new ArrayList<>(treeNode.getChildren());
        
        Collection<TreeNode> children = obtainCustomerNodes(oldChildren, treeNode);
        children.addAll(obtainAccountNodes(oldChildren, treeNode));
        return children;
    }

    private Collection<TreeNode> obtainCustomerNodes(
            List<? extends TreeNode> oldChildren, 
            TreeNode treeNode) {
        Collection<TreeNode> children = new ArrayList<>();
        for (int ik : _accessManager.retrieveAllowedManagedIks(Feature.SPECIFIC_FUNCTION)) {
            Optional<? extends TreeNode> existing = oldChildren
                    .stream()
                    .filter(n -> n instanceof CustomerTreeNode && n.getId() == ik)
                    .findFirst();
            CustomerTreeNode childNode = existing.isPresent()
                    ? (CustomerTreeNode) existing.get()
                    : createCustomerNode(treeNode, ik);
            children.add((TreeNode) childNode);
            childNode.expand();  // auto-expand all edit nodes by default
        }
        return children;
    }

    private CustomerTreeNode createCustomerNode(TreeNode parent, int ik) {
        Customer customer = _customerFacade.getCustomerByIK(ik);
        return CustomerTreeNode.create(parent, customer, _customerTreeNodeObserverProvider.get());
    }

    private Collection<TreeNode> obtainAccountNodes(
            List<? extends TreeNode> oldChildren, 
            TreeNode treeNode) {
        Collection<TreeNode> children = new ArrayList<>();
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.SPECIFIC_FUNCTION, canReadCompleted());
        Set<Integer> managedIks = _accessManager.retrieveAllManagedIks(Feature.SPECIFIC_FUNCTION);
        List<Account> accounts = _specificFunctionFacade.loadRequestAccounts(
                accountIds,
                WorkflowStatus.New,
                WorkflowStatus.ApprovalRequested,
                managedIks);
        Account currentUser = _sessionController.getAccount();
        if (accounts.contains(currentUser)) {
            // ensure current user is first, if in list
            accounts.remove(currentUser);
            accounts.add(0, currentUser);
        }
        for (Account account : accounts) {
            Integer id = account.getId();
            Optional<? extends TreeNode> existing = oldChildren
                    .stream()
                    .filter(n -> n instanceof AccountTreeNode && n.getId() == id)
                    .findFirst();
            AccountTreeNode childNode = existing.isPresent()
                    ? (AccountTreeNode) existing.get()
                    : AccountTreeNode.create(treeNode, account, _accountTreeNodeObserverProvider.get());
            children.add((TreeNode) childNode);
            childNode.expand();  // auto-expand all edit nodes by default
        }
        return children;
    }

    
}
