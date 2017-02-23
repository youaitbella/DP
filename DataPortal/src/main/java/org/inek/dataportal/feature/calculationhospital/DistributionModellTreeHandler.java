package org.inek.dataportal.feature.calculationhospital;

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
import org.inek.dataportal.common.CooperationTools;
import static org.inek.dataportal.common.CooperationTools.canReadCompleted;
import static org.inek.dataportal.common.CooperationTools.canReadSealed;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.calc.CalcHospitalInfo;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.calc.CalcFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.calc.DistributionModelFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.tree.AccountTreeNode;
import org.inek.dataportal.helper.tree.CalcHospitalTreeNode;
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
public class DistributionModellTreeHandler implements Serializable, TreeNodeObserver {

    private static final Logger _logger = Logger.getLogger("DistributionModellTreeHandler");
    private static final long serialVersionUID = 1L;

    @Inject private DistributionModelFacade _distributionModelFacade;
    @Inject private SessionController _sessionController;
    @Inject private CooperationTools _cooperationTools;
    @Inject private AccountFacade _accountFacade;

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

    public RootNode getDrgNode() {
        return getRootNode(1);
    }

    public RootNode getPsyNode() {
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
            obtainDrgNodeChildren((RootNode) treeNode, children);
        }
        if (treeNode instanceof RootNode && treeNode.getId() == 2) {
            obtainPsyNodeChildren((RootNode) treeNode, children);
        }
        if (treeNode instanceof AccountTreeNode) {
            obtainAccountNodeChildren((AccountTreeNode) treeNode, children);
        }
    }

    // todo: retrieve data with status 10 for approval
    private void obtainDrgNodeChildren(RootNode node, Collection<TreeNode> children) {
        List<Account> accounts = _distributionModelFacade.getInekAgentsForDrg();
//        Set<Integer> accountIds = _cooperationTools.determineAccountIds(Feature.CALCULATION_HOSPITAL, canReadCompleted());
//        accountIds = _distributionModelFacade.checkAccountsForYear(accountIds, Utils.getTargetYear(Feature.CALCULATION_HOSPITAL), WorkflowStatus.New, WorkflowStatus.ApprovalRequested);
//        List<Account> accounts = _accountFacade.getAccountsForIds(accountIds);
//        Account currentUser = _sessionController.getAccount();
//        if (accounts.contains(currentUser)) {
//            // ensure current user is first, if in list
//            accounts.remove(currentUser);
//            accounts.add(0, currentUser);
//        }
//        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
//        children.clear();
//        for (Account account : accounts) {
//            Integer id = account.getId();
//            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == id).findFirst();
//            AccountTreeNode childNode = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.create(node, account, this);
//            children.add((TreeNode) childNode);
//            oldChildren.remove(childNode);
//            childNode.expand();  // auto-expand all edit nodes by default
//        }
    }

    private void obtainPsyNodeChildren(RootNode node, Collection<TreeNode> children) {
//        Set<Integer> accountIds = _cooperationTools.determineAccountIds(Feature.CALCULATION_HOSPITAL, canReadSealed());
//        Set<Integer> years = _distributionModelFacade.getCalcYears(accountIds);
//        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
//        int targetYear = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
//        children.clear();
//        for (Integer year : years) {
//            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == year).findFirst();
//            YearTreeNode childNode = existing.isPresent() ? (YearTreeNode) existing.get() : YearTreeNode.create(node, year, this);
//            children.add((TreeNode) childNode);
//            oldChildren.remove(childNode);
//            if (year == targetYear) {
//                childNode.expand();
//            }
//        }
    }

    private void obtainAccountNodeChildren(AccountTreeNode accountTreeNode, Collection<TreeNode> children) {
        int partnerId = accountTreeNode.getId();
        List<CalcHospitalInfo> infos;
        if (accountTreeNode.getParent() instanceof YearTreeNode) {
            int year = accountTreeNode.getParent().getId();
            infos = obtainCalculationHospitalInfosForRead(partnerId, year);
        } else {
            infos = obtainCalculationHospitalInfosForEdit(partnerId);
        }
        accountTreeNode.getChildren().clear();
        for (CalcHospitalInfo info : infos) {
            accountTreeNode.getChildren().add(CalcHospitalTreeNode.create(accountTreeNode, info, this));
        }
    }

    private List<CalcHospitalInfo> obtainCalculationHospitalInfosForRead(int partnerId, int year) {
//        WorkflowStatus statusLow = WorkflowStatus.Provided;
//        WorkflowStatus statusHigh = WorkflowStatus.Retired;
//        if (partnerId != _sessionController.getAccountId()) {
//            CooperativeRight achievedRight = _cooperationTools.getAchievedRight(Feature.CALCULATION_HOSPITAL, partnerId);
//            if (!achievedRight.canReadSealed()){
//                statusLow = WorkflowStatus.Unknown;
//                statusHigh = WorkflowStatus.Unknown;
//            }
//        }
//        return _distributionModelFacade.getListCalcInfo(partnerId, year, statusLow, statusHigh);
        return null;
    }

    private List<CalcHospitalInfo> obtainCalculationHospitalInfosForEdit(int partnerId) {
//        WorkflowStatus statusLow;
//        WorkflowStatus statusHigh;
//        if (partnerId == _sessionController.getAccountId()) {
//            statusLow = WorkflowStatus.New;
//            statusHigh = WorkflowStatus.ApprovalRequested;
//        } else {
//            CooperativeRight achievedRight = _cooperationTools.getAchievedRight(Feature.CALCULATION_HOSPITAL, partnerId);
//            statusLow = achievedRight.canReadAlways() ? WorkflowStatus.New
//                    : achievedRight.canReadCompleted() ? WorkflowStatus.ApprovalRequested :  WorkflowStatus.Unknown;
//            statusHigh = achievedRight.canReadAlways() || achievedRight.canReadCompleted() ? WorkflowStatus.ApprovalRequested : WorkflowStatus.Unknown;
//        }
//        return _distributionModelFacade.getListCalcInfo(partnerId, Utils.getTargetYear(Feature.CALCULATION_HOSPITAL), statusLow, statusHigh);
        return null;
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
            case "id":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n2.getCalcHospitalInfo().getId(), n1.getCalcHospitalInfo().getId()));
                } else {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n1.getCalcHospitalInfo().getId(), n2.getCalcHospitalInfo().getId()));
                }
                break;
            case "name":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> n2.getCalcHospitalInfo().getName().compareTo(n1.getCalcHospitalInfo().getName()));
                } else {
                    sorted = stream.sorted((n1, n2) -> n1.getCalcHospitalInfo().getName().compareTo(n2.getCalcHospitalInfo().getName()));
                }
                break;
            case "date":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> n2.getCalcHospitalInfo().getLastChanged().compareTo(n1.getCalcHospitalInfo().getLastChanged()));
                } else {
                    sorted = stream.sorted((n1, n2) -> n1.getCalcHospitalInfo().getLastChanged().compareTo(n2.getCalcHospitalInfo().getLastChanged()));
                }
                break;
            case "status":
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }

    public void selectAll() {
        _rootNode.selectAll(CalcHospitalTreeNode.class, true);
    }

    public String deselectAll() {
        _rootNode.selectAll(CalcHospitalTreeNode.class, false);
        return "";
    }

    public String printSelected() {
        // todo
        Map<String, List<KeyValueLevel>> documents = new TreeMap<>();
        List<String> keys = null;
        Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL"));
        Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
        Utils.getFlash().put("printContentKeys", keys);
        Utils.getFlash().put("printContent", documents);
        return Pages.PrintMultipleView.URL();
    }

}
