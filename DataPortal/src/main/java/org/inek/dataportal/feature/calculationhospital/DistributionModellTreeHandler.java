package org.inek.dataportal.feature.calculationhospital;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.facades.calc.DistributionModelFacade;
import org.inek.dataportal.helper.tree.AccountTreeNode;
import org.inek.dataportal.helper.tree.CalcHospitalTreeNode;
import org.inek.portallib.tree.RootNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;

/**
 *
 * @author muellermi
 */
@Named @SessionScoped
public class DistributionModellTreeHandler implements Serializable, TreeNodeObserver {
    
    private static final Logger _logger = Logger.getLogger("DistributionModellTreeHandler");
    private static final long serialVersionUID = 1L;
    
    @Inject private DistributionModelFacade _distributionModelFacade;
    @Inject private SessionController _sessionController;
    @Inject private ApplicationTools _appTools;
    
    private final RootNode _rootNode = RootNode.create(0, this);
    private AccountTreeNode _accountNode;
    
    public RootNode getRootNode() {
        if (!_rootNode.isExpanded()) {
            _rootNode.expand();
        }
        return _rootNode;
    }
    
    public void refreshNodes() {
        _rootNode.refresh();
    }
    
    @Override
    public void obtainChildren(TreeNode treeNode, Collection<TreeNode> children) {
        if (treeNode instanceof RootNode) {
            obtainRootNodeChildren((RootNode) treeNode, children);
        }
        if (treeNode instanceof AccountTreeNode) {
            obtainAccountNodeChildren((AccountTreeNode) treeNode, children);
        }
    }
    
    private void obtainRootNodeChildren(RootNode node, Collection<TreeNode> children) {
        List<Account> accounts = _distributionModelFacade.getInekAccounts();
        Account currentUser = _sessionController.getAccount();
        if (accounts.contains(currentUser)) {
            // ensure current user is first, if in list
            accounts.remove(currentUser);
            accounts.add(0, currentUser);
        }
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        children.clear();
        for (Account account : accounts) {
            Integer id = account.getId();
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == id).findFirst();
            AccountTreeNode childNode = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.create(node, account, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (currentUser.equals(account)) {
                childNode.expand();  // auto-expand own node
            }
        }
    }
    
    private void obtainAccountNodeChildren(AccountTreeNode accountTreeNode, Collection<TreeNode> children) {
        List<CalcHospitalInfo> infos = _distributionModelFacade.getDistributionModelsForAccount(accountTreeNode.getAccount());
        accountTreeNode.getChildren().clear();
        for (CalcHospitalInfo info : infos) {
            accountTreeNode.getChildren().add(CalcHospitalTreeNode.create(accountTreeNode, info, this));
        }
    }
    
    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode, Collection<TreeNode> children) {
        if (treeNode instanceof AccountTreeNode) {
            return sortAccountNodeChildren((AccountTreeNode) treeNode, children);
        }
        return children;
    }
    
    public Collection<TreeNode> sortAccountNodeChildren(AccountTreeNode treeNode, Collection<TreeNode> children) {
        Stream<CalcHospitalTreeNode> stream = children.stream().map(n -> (CalcHospitalTreeNode) n);
        Stream<CalcHospitalTreeNode> sorted;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "ik":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n2.getCalcHospitalInfo().getIk(), n1.getCalcHospitalInfo().getIk()));
                } else {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n1.getCalcHospitalInfo().getIk(), n2.getCalcHospitalInfo().getIk()));
                }
                break;
            case "hospital":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> _appTools.retrieveHospitalInfo(n2.getCalcHospitalInfo().getIk()).compareTo(_appTools.retrieveHospitalInfo(n1.getCalcHospitalInfo().getIk())));
                } else {
                    sorted = stream.sorted((n1, n2) -> _appTools.retrieveHospitalInfo(n1.getCalcHospitalInfo().getIk()).compareTo(_appTools.retrieveHospitalInfo(n2.getCalcHospitalInfo().getIk())));
                }
                break;
            case "name":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> n2.getCalcHospitalInfo().getName().compareTo(n1.getCalcHospitalInfo().getName()));
                } else {
                    sorted = stream.sorted((n1, n2) -> n1.getCalcHospitalInfo().getName().compareTo(n2.getCalcHospitalInfo().getName()));
                }
                break;
            case "status":
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }
}