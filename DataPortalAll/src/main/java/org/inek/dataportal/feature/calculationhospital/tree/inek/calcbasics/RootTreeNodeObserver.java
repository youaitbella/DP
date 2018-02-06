package org.inek.dataportal.feature.calculationhospital.tree.inek.calcbasics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.facades.calc.CalcFacade;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;

/**
 *
 * @author muellermi
 */
public class RootTreeNodeObserver implements TreeNodeObserver{

    private final CalcFacade _calcFacade;
    private final SessionController _sessionController;
    private final CalcBasicsTreeHandler _treeHandler;
    private final Instance<AccountTreeNodeObserver> _accountTreeNodeObserverProvider;


    @Inject
    public RootTreeNodeObserver(
            CalcFacade calcFacade,
            SessionController sessionController,
            CalcBasicsTreeHandler treeHandler,
            Instance<AccountTreeNodeObserver> accountTreeNodeObserverProvider) {
        _calcFacade = calcFacade;
        _sessionController = sessionController;
        _treeHandler = treeHandler;
        _accountTreeNodeObserverProvider = accountTreeNodeObserverProvider;
    }
    
    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        List<Account> accounts = _calcFacade.getInekAccounts(_treeHandler.getYear(), _treeHandler.getFilter());
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
            if (currentUser.equals(account) || accounts.size() <= 3) {
                childNode.expand();  // auto-expand if own node or if only few
            }
        }
        return children;
    }
    
}
