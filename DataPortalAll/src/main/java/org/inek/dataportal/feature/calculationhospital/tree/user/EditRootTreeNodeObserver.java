package org.inek.dataportal.feature.calculationhospital.tree.user;

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
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.calc.CalcFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;

@Dependent
public class EditRootTreeNodeObserver implements TreeNodeObserver{

    private final CalcFacade _calcFacade;
    private final SessionController _sessionController;
    private final AccessManager _accessManager;
    private final AccountFacade _accountFacade;
    private final Instance<AccountTreeNodeObserver> _accountTreeNodeObserverProvider;

    @Inject
    public EditRootTreeNodeObserver(
            CalcFacade calcFacade,
            SessionController sessionController,
            AccessManager accessManager,
            AccountFacade accountFacade,
            Instance<AccountTreeNodeObserver> accountTreeNodeObserverProvider) {
        _calcFacade = calcFacade;
        _sessionController = sessionController;
        _accessManager = accessManager;
        _accountFacade = accountFacade;
        _accountTreeNodeObserverProvider = accountTreeNodeObserverProvider;
    }


    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.CALCULATION_HOSPITAL, canReadCompleted());
        accountIds = _calcFacade.checkAccountsForYear(accountIds, Utils.getTargetYear(Feature.CALCULATION_HOSPITAL),
                WorkflowStatus.New, WorkflowStatus.ApprovalRequested);
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
            childNode.expand();  // auto-expand all edit nodes by default
        }
        return children;
    }
    
}
