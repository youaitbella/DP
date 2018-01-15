package org.inek.dataportal.feature.drgproposal;

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
import org.inek.dataportal.common.AccessManager;
import static org.inek.dataportal.common.AccessManager.canReadCompleted;
import static org.inek.dataportal.common.AccessManager.canReadSealed;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.drg.DrgProposal;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.DrgProposalFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.ProposalInfo;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.ProposalInfoTreeNode;
import org.inek.portallib.tree.RootNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;
import org.inek.portallib.tree.YearTreeNode;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.utils.KeyValueLevel;

/**
 *
 * @author muellermi
 */
@Named @SessionScoped
public class DrgProposalTreeHandler implements Serializable, TreeNodeObserver {

    private static final Logger LOGGER = Logger.getLogger("DrpProposalTreeHandler");
    private static final long serialVersionUID = 1L;

    @Inject private CooperationRightFacade _cooperationRightFacade;
    @Inject private DrgProposalFacade _drgProposalFacade;
    @Inject private SessionController _sessionController;
    @Inject private AccessManager _accessManager;
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
    public void obtainChildren(TreeNode treeNode) {
        if (treeNode instanceof RootNode && treeNode.getId() == 1) {
            obtainEditNodeChildren((RootNode) treeNode);
        }
        if (treeNode instanceof RootNode && treeNode.getId() == 2) {
            obtainViewNodeChildren((RootNode) treeNode);
        }
        if (treeNode instanceof YearTreeNode) {
            obtainYearNodeChildren((YearTreeNode) treeNode);
        }
        if (treeNode instanceof AccountTreeNode) {
            obtainAccountNodeChildren((AccountTreeNode) treeNode);
        }
    }

    private void obtainEditNodeChildren(RootNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.DRG_PROPOSAL, canReadCompleted());
        accountIds = _drgProposalFacade.
                checkAccountsForProposalOfYear(accountIds, -1, WorkflowStatus.New, WorkflowStatus.ApprovalRequested);
        List<Account> accounts = _accountFacade.getAccountsForIds(accountIds);
        Account currentUser = _sessionController.getAccount();
        if (accounts.contains(currentUser)) {
            // ensure current user is first, if in list
            accounts.remove(currentUser);
            accounts.add(0, currentUser);
        }
        Collection<TreeNode> children = treeNode.getChildren();
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        children.clear();
        for (Account account : accounts) {
            Integer id = account.getId();
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == id).findFirst();
            AccountTreeNode childNode = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.
                    create(treeNode, account, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            childNode.expand();  // auto-expand all edit nodes by default
        }
    }

    private void obtainViewNodeChildren(RootNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.DRG_PROPOSAL, canReadSealed());
        List<Integer> years = _drgProposalFacade.getProposalYears(accountIds);
        Collection<TreeNode> children = treeNode.getChildren();
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        int targetYear = Utils.getTargetYear(Feature.DRG_PROPOSAL);
        children.clear();
        for (Integer year : years) {
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == year).findFirst();
            YearTreeNode childNode = existing.isPresent() ? (YearTreeNode) existing.get() : YearTreeNode.
                    create(treeNode, year, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (year == targetYear) {
                childNode.expand();
            }
        }
    }

    private void obtainYearNodeChildren(YearTreeNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.DRG_PROPOSAL, canReadSealed());
        accountIds = _drgProposalFacade.
                checkAccountsForProposalOfYear(accountIds, treeNode.getId(), WorkflowStatus.Provided, WorkflowStatus.Retired);
        List<Account> accounts = _accountFacade.getAccountsForIds(accountIds);
        Account currentUser = _sessionController.getAccount();
        if (accounts.contains(currentUser)) {
            // ensure current user is first, if in list
            accounts.remove(currentUser);
            accounts.add(0, currentUser);
        }
        Collection<TreeNode> children = treeNode.getChildren();
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        children.clear();
        for (Account account : accounts) {
            Integer id = account.getId();
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == id).findFirst();
            AccountTreeNode childNode = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.
                    create(treeNode, account, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (account == currentUser) {
                // auto expand user's own data
                childNode.expand();
            }
        }
    }

    private void obtainAccountNodeChildren(AccountTreeNode treeNode) {
        int partnerId = treeNode.getId();
        List<ProposalInfo> infos;
        if (treeNode.getParent() instanceof YearTreeNode) {
            int year = treeNode.getParent().getId();
            infos = obtainProposalInfosForRead(partnerId, year);
        } else {
            infos = obtainProposalInfosForEdit(partnerId);
        }
        treeNode.getChildren().clear();
        for (ProposalInfo info : infos) {
            treeNode.getChildren().add(ProposalInfoTreeNode.create(treeNode, info, this));
        }
    }

    private List<ProposalInfo> obtainProposalInfosForRead(int partnerId, int year) {
        DataSet dataSet;
        if (partnerId == _sessionController.getAccountId()) {
            dataSet = DataSet.AllSealed;
        } else {
            boolean canReadSealed = _accessManager.canReadSealed(Feature.DRG_PROPOSAL, partnerId);
            dataSet = canReadSealed ? DataSet.AllSealed : DataSet.None;
        }
        return _drgProposalFacade.getDrgProposalInfos(partnerId, year, dataSet);
    }

    private List<ProposalInfo> obtainProposalInfosForEdit(int partnerId) {
        DataSet dataSet;
        if (partnerId == _sessionController.getAccountId()) {
            dataSet = DataSet.AllOpen;
        } else {
            boolean canReadAlways = _accessManager.canReadAlways(Feature.DRG_PROPOSAL, partnerId);
            boolean canReadCompleted = _accessManager.canReadCompleted(Feature.DRG_PROPOSAL, partnerId);
            dataSet = canReadAlways ? DataSet.AllOpen
                    : canReadCompleted ? DataSet.ApprovalRequested : DataSet.None;
        }
        return _drgProposalFacade.getDrgProposalInfos(partnerId, -1, dataSet);
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        if (treeNode instanceof AccountTreeNode) {
            return sortAccountNodeChildren((AccountTreeNode) treeNode);
        }
        return treeNode.getChildren();
    }

    public Collection<TreeNode> sortAccountNodeChildren(AccountTreeNode treeNode) {
        Stream<ProposalInfoTreeNode> stream = treeNode.getChildren().stream().map(n -> (ProposalInfoTreeNode) n);
        Stream<ProposalInfoTreeNode> sorted;
        int direction = treeNode.isDescending() ? -1 : 1;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "id":
                sorted = stream.sorted((n1, n2) -> direction * Integer.compare(n1.getProposalInfo().getId(), n2.
                        getProposalInfo().getId()));
                break;
            case "name":
                sorted = stream.sorted((n1, n2) -> direction * n1.getProposalInfo().getName().compareTo(n2.
                        getProposalInfo().getName()));
                break;
            case "status":
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }

    public void selectAll() {
        _rootNode.selectAll(ProposalInfoTreeNode.class, true);
    }

    public String deselectAll() {
        _rootNode.selectAll(ProposalInfoTreeNode.class, false);
        return "";
    }

    public String printSelected() {
        List<Integer> selectedProposals = _rootNode.getSelectedIds(ProposalInfoTreeNode.class);
        if (selectedProposals.isEmpty()) {
            return "";
        }
        List<DrgProposal> proposals = _drgProposalFacade.find(selectedProposals);
        Map<String, List<KeyValueLevel>> documents = new TreeMap<>();
        int count = 1;
        for (DrgProposal proposal : proposals) {
            String key = proposal.getExternalId();
            if (key.isEmpty()) {
                key = "<nicht gesendet> " + count++;
            }
            documents.put(key, DocumentationUtil.getDocumentation(proposal));
        }
        List<String> keys = new ArrayList<>(documents.keySet());
        Utils.getFlash().put("headLine", Utils.getMessage("nameDRG_PROPOSAL"));
        Utils.getFlash().put("targetPage", Pages.DrgProposalSummary.URL());
        Utils.getFlash().put("printContentKeys", keys);
        Utils.getFlash().put("printContent", documents);
        return Pages.PrintMultipleView.URL();
    }

}
