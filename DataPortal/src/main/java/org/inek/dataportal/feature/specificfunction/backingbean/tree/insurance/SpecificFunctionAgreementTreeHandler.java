package org.inek.dataportal.feature.specificfunction.backingbean.tree.insurance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.common.AccessManager;
import static org.inek.dataportal.common.AccessManager.canReadCompleted;
import static org.inek.dataportal.common.AccessManager.canReadSealed;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.feature.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.feature.specificfunction.entity.SpecificFunctionAgreement;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.SpecificFunctionAgreementTreeNode;
import org.inek.dataportal.helper.tree.SpecificFunctionRequestTreeNode;
import org.inek.portallib.tree.RootNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;
import org.inek.portallib.tree.YearTreeNode;
import org.inek.dataportal.utils.KeyValueLevel;

/**
 *
 * @author muellermi
 */
@Named @SessionScoped
public class SpecificFunctionAgreementTreeHandler implements Serializable, TreeNodeObserver {

    private static final Logger LOGGER = Logger.getLogger("SpecificFunctionAgreementTreeHandler");
    private static final long serialVersionUID = 1L;

    @Inject private SpecificFunctionFacade _specificFunctionFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;
    @Inject private ApplicationTools _appTools;

    private final RootNode _rootNode = RootNode.create(0, this);
    private AccountTreeNode _accountNode;

    private RootNode getRootNode(int id) {
        Optional<TreeNode> optionalRoot = _rootNode.getChildren().stream().filter(n -> n.getId() == id).findAny();
        if (optionalRoot.isPresent()) {
            return (RootNode) optionalRoot.get();
        }
        RootNode node = RootNode.create(id, this);
        node.expand();
        _rootNode.setExpanded(true);
        _rootNode.getChildren().add(node);
        return node;
    }

    public RootNode getEditNode() {
        return getRootNode(1);
    }

    public RootNode getViewNode() {
        return getRootNode(2);
    }

    public void refreshNodes() {
        _rootNode.refresh();
    }

    public AccountTreeNode getAccountNode() {
        if (_accountNode == null) {
            _accountNode = AccountTreeNode.create(null, _sessionController.getAccount(), this);
            _accountNode.expand();
        }
        return _accountNode;
    }

    @Override
    public void obtainChildren(TreeNode treeNode, Collection<TreeNode> children) {
        if (treeNode instanceof RootNode && treeNode.getId() == 1) {
            obtainEditNodeChildren((RootNode) treeNode, children);
        }
        if (treeNode instanceof RootNode && treeNode.getId() == 2) {
            obtainViewNodeChildren((RootNode) treeNode, children);
        }
        if (treeNode instanceof YearTreeNode) {
            obtainYearNodeChildren((YearTreeNode) treeNode, children);
        }
        if (treeNode instanceof AccountTreeNode) {
            obtainAccountNodeChildren((AccountTreeNode) treeNode, children);
        }
    }

    private void obtainEditNodeChildren(RootNode node, Collection<TreeNode> children) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.SPECIFIC_FUNCTION, canReadCompleted());
        List<Account> accounts = _specificFunctionFacade.loadAgreementAccountsForYear(accountIds,
                Utils.getTargetYear(Feature.SPECIFIC_FUNCTION), WorkflowStatus.New, WorkflowStatus.ApprovalRequested);
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
            AccountTreeNode childNode = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.
                    create(node, account, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            childNode.expand();  // auto-expand all edit nodes by default
        }
    }

    private void obtainViewNodeChildren(RootNode node, Collection<TreeNode> children) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.SPECIFIC_FUNCTION, canReadSealed());
        Set<Integer> years = _specificFunctionFacade.getAgreementCalcYears(accountIds);
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        int targetYear = Utils.getTargetYear(Feature.SPECIFIC_FUNCTION);
        children.clear();
        for (Integer year : years) {
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == year).findFirst();
            YearTreeNode childNode = existing.isPresent() ? (YearTreeNode) existing.get() : YearTreeNode.
                    create(node, year, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (year == targetYear) {
                childNode.expand();
            }
        }
    }

    private void obtainYearNodeChildren(YearTreeNode node, Collection<TreeNode> children) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.SPECIFIC_FUNCTION, canReadSealed());
        List<Account> accounts = _specificFunctionFacade.loadAgreementAccountsForYear(accountIds, node.getId(),
                WorkflowStatus.Provided, WorkflowStatus.Retired);
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
            AccountTreeNode childNode = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.
                    create(node, account, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (account == currentUser) {
                // auto expand user's own data
                childNode.expand();
            }
        }
    }

    private void obtainAccountNodeChildren(AccountTreeNode accountTreeNode, Collection<TreeNode> children) {
        int partnerId = accountTreeNode.getId();
        List<SpecificFunctionAgreement> infos;
        if (accountTreeNode.getParent() instanceof YearTreeNode) {
            int year = accountTreeNode.getParent().getId();
            infos = obtainAgreementsForRead(partnerId, year);
        } else {
            infos = obtainAgreementsForEdit(partnerId);
        }
        accountTreeNode.getChildren().clear();
        for (SpecificFunctionAgreement info : infos) {
            accountTreeNode.getChildren().add(SpecificFunctionAgreementTreeNode.create(accountTreeNode, info, this));
        }
    }

    private List<SpecificFunctionAgreement> obtainAgreementsForRead(int partnerId, int year) {
        WorkflowStatus statusLow = WorkflowStatus.Provided;
        WorkflowStatus statusHigh = WorkflowStatus.Retired;
        if (partnerId != _sessionController.getAccountId()) {
            boolean canReadSealed = _accessManager.canReadSealed(Feature.SPECIFIC_FUNCTION, partnerId);
            if (!canReadSealed) {
                statusLow = WorkflowStatus.Unknown;
                statusHigh = WorkflowStatus.Unknown;
            }
        }
        return _specificFunctionFacade.obtainSpecificFunctionAgreements(partnerId, year, statusLow, statusHigh);
    }

    private List<SpecificFunctionAgreement> obtainAgreementsForEdit(int partnerId) {
        WorkflowStatus statusLow;
        WorkflowStatus statusHigh;
        if (partnerId == _sessionController.getAccountId()) {
            statusLow = WorkflowStatus.New;
            statusHigh = WorkflowStatus.ApprovalRequested;
        } else {
            boolean canReadAlways = _accessManager.canReadAlways(Feature.SPECIFIC_FUNCTION, partnerId);
            boolean canReadCompleted = _accessManager.canReadCompleted(Feature.SPECIFIC_FUNCTION, partnerId);
            statusLow = canReadAlways ? WorkflowStatus.New
                    : canReadCompleted ? WorkflowStatus.ApprovalRequested : WorkflowStatus.Unknown;
            statusHigh = canReadAlways
                    || canReadCompleted ? WorkflowStatus.ApprovalRequested : WorkflowStatus.Unknown;
        }
        return _specificFunctionFacade.obtainSpecificFunctionAgreements(
                partnerId,
                Utils.getTargetYear(Feature.SPECIFIC_FUNCTION),
                statusLow,
                statusHigh);
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        if (treeNode instanceof AccountTreeNode) {
            return sortAccountNodeChildren((AccountTreeNode) treeNode);
        }
        return treeNode.getChildren();
    }

    public Collection<TreeNode> sortAccountNodeChildren(AccountTreeNode treeNode) {
        Stream<SpecificFunctionAgreementTreeNode> stream = treeNode.getChildren().stream().
                map(n -> (SpecificFunctionAgreementTreeNode) n);
        Stream<SpecificFunctionAgreementTreeNode> sorted;
        int direction = treeNode.isDescending() ? -1 : 1;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "id":
                sorted = stream.sorted((n1, n2)
                        -> direction * Integer.compare(n1.getSpecificFunctionAgreement().getId(),
                                n2.getSpecificFunctionAgreement().getId()));
                break;
            case "hospital":
                sorted = stream.sorted((n1, n2)
                        -> direction * _appTools.retrieveHospitalInfo(n1.getSpecificFunctionAgreement().getIk())
                                .compareTo(_appTools.retrieveHospitalInfo(n2.getSpecificFunctionAgreement().getIk())));
                break;
            case "date":
                sorted = stream.sorted((n1, n2)
                        -> direction * n1.getSpecificFunctionAgreement().getLastChanged()
                                .compareTo(n2.getSpecificFunctionAgreement().getLastChanged()));
                break;
            case "status":
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }

    public void selectAll() {
        _rootNode.selectAll(SpecificFunctionRequestTreeNode.class, true);
    }

    public String deselectAll() {
        _rootNode.selectAll(SpecificFunctionRequestTreeNode.class, false);
        return "";
    }

    public String printSelected() {
        // todo
        Map<String, List<KeyValueLevel>> documents = new TreeMap<>();
        List<String> keys = null;
        Utils.getFlash().put("headLine", Utils.getMessage("nameSPECIFIC_FUNCTION"));
        Utils.getFlash().put("targetPage", Pages.SpecificFunctionSummary.URL());
        Utils.getFlash().put("printContentKeys", keys);
        Utils.getFlash().put("printContent", documents);
        return Pages.PrintMultipleView.URL();
    }

}