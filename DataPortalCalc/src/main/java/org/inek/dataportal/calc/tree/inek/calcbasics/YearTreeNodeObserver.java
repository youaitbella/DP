package org.inek.dataportal.calc.tree.inek.calcbasics;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.calc.facades.CalcFacade;
import org.inek.dataportal.common.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.tree.YearTreeNode;

/**
 *
 * @author muellermi
 */
public class YearTreeNodeObserver implements TreeNodeObserver{

    private final CalcFacade _calcFacade;
    private final SessionController _sessionController;
    private final CalcBasicsTreeHandler _treeHandler;
    private final Instance<AccountTreeNodeObserver> _accountTreeNodeObserverProvider;


    @Inject
    public YearTreeNodeObserver(
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
        int year = ((YearTreeNode) treeNode).getYear();
        List<Account> accounts = _calcFacade.getInekAccounts(year, _treeHandler.getFilter());
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
