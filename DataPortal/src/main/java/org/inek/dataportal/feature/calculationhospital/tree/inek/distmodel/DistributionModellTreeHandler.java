package org.inek.dataportal.feature.calculationhospital.tree.inek.distmodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.calc.DistributionModelFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
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

    private static final Logger LOGGER = Logger.getLogger("DistributionModellTreeHandler");
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

    private String _filter = "";

    public String getFilter() {
        return _filter;
    }

    public void setFilter(String filter) {
        _filter = filter == null ? "" : filter;
        refreshNodes();
    }

    private int _year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        _year = year;
        refreshNodes();
    }

    @Override
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        if (treeNode instanceof RootNode) {
            return obtainRootNodeChildren((RootNode) treeNode);
        }
        if (treeNode instanceof AccountTreeNode) {
            return obtainAccountNodeChildren((AccountTreeNode) treeNode);
        }
        return new ArrayList<TreeNode>();
    }

    private Collection<TreeNode> obtainRootNodeChildren(RootNode treeNode) {
        List<Account> accounts = _distributionModelFacade.getInekAccounts(getYear(), getFilter());
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
            if (currentUser.equals(account) || accounts.size() <= 3) {
                childNode.expand();  // auto-expand own node
            }
        }
        return children;
    }

    private Collection<TreeNode> obtainAccountNodeChildren(AccountTreeNode treeNode) {
        List<CalcHospitalInfo> infos = _distributionModelFacade.
                getDistributionModelsByEmail(treeNode.getEmail(), getYear(), getFilter());
        Collection<TreeNode> children = new ArrayList<>();
        for (CalcHospitalInfo info : infos) {
            children.add(CalcHospitalTreeNode.create(treeNode, info, this));
        }
        return children;
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        if (treeNode instanceof AccountTreeNode) {
            return sortAccountNodeChildren((AccountTreeNode) treeNode);
        }
        return treeNode.getChildren();
    }

    public Collection<TreeNode> sortAccountNodeChildren(AccountTreeNode treeNode) {
        Stream<CalcHospitalTreeNode> stream = treeNode.getChildren().stream().map(n -> (CalcHospitalTreeNode) n);
        Stream<CalcHospitalTreeNode> sorted;
        int direction = treeNode.isDescending() ? -1 : 1;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "ik":
                sorted = stream.sorted((n1, n2) -> direction * Integer.compare(n1.getCalcHospitalInfo().getIk(), n2.
                        getCalcHospitalInfo().getIk()));
                break;
            case "hospital":
                sorted = stream.sorted((n1, n2) -> direction * _appTools.retrieveHospitalInfo(n1.getCalcHospitalInfo().
                        getIk())
                        .compareTo(_appTools.retrieveHospitalInfo(n2.getCalcHospitalInfo().getIk())));
                break;
            case "name":
                sorted = stream.sorted((n1, n2) -> direction * n1.getCalcHospitalInfo().getName().compareTo(n2.
                        getCalcHospitalInfo().getName()));
                break;
            case "status":
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }
}
