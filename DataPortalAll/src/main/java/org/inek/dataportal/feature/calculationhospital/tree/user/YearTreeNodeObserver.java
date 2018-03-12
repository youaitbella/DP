package org.inek.dataportal.feature.calculationhospital.tree.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.inek.dataportal.common.overall.AccessManager;
import static org.inek.dataportal.common.overall.AccessManager.canReadSealed;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.facades.calc.CalcFacade;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;
import org.inek.dataportal.common.data.icmt.entities.Customer;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.helper.tree.entityTree.CustomerTreeNode;

@Dependent
public class YearTreeNodeObserver implements TreeNodeObserver {

    private final CalcFacade _calcFacade;
    private final SessionController _sessionController;
    private final AccessManager _accessManager;
    private final AccountFacade _accountFacade;
    private final CustomerFacade _customerFacade;
    private final Instance<AccountTreeNodeObserver> _accountTreeNodeObserverProvider;
    private final Instance<CustomerTreeNodeObserver> _customerTreeNodeObserverProvider;

    @Inject
    public YearTreeNodeObserver(
            CalcFacade calcFacade,
            SessionController sessionController,
            AccessManager accessManager,
            AccountFacade accountFacade,
            CustomerFacade customerFacade,
            Instance<AccountTreeNodeObserver> accountTreeNodeObserverProvider,
            Instance<CustomerTreeNodeObserver> customerTreeNodeObserverProvider) {
        _calcFacade = calcFacade;
        _sessionController = sessionController;
        _accessManager = accessManager;
        _accountFacade = accountFacade;
        _customerFacade = customerFacade;
        _accountTreeNodeObserverProvider = accountTreeNodeObserverProvider;
        _customerTreeNodeObserverProvider = customerTreeNodeObserverProvider;
    }

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
        for (int ik : _accessManager.retrieveAllowedManagedIks(Feature.CALCULATION_HOSPITAL)) {
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

    public Collection<TreeNode> obtainAccountNodes(List<TreeNode> oldChildren, TreeNode treeNode) {
        Collection<TreeNode> children = new ArrayList<>();
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.CALCULATION_HOSPITAL, canReadSealed());
        Set<Integer> managedIks = _accessManager.retrieveAllManagedIks(Feature.CALCULATION_HOSPITAL);
        int year = ((YearTreeNode) treeNode).getYear();
        accountIds = _calcFacade.checkAccountsForYear(accountIds, year,
                WorkflowStatus.Provided, WorkflowStatus.Retired, managedIks);
        List<Account> accounts = _accountFacade.getAccountsForIds(accountIds);
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
                    .filter(n -> n.getId() == id)
                    .findFirst()
                    .orElseGet(() -> AccountTreeNode.create(treeNode, account, _accountTreeNodeObserverProvider.get()));
            children.add((TreeNode) childNode);
            if (account == currentUser) {
                // auto expand user's own data
                childNode.expand();
            }
        }
        return children;
    }

}
