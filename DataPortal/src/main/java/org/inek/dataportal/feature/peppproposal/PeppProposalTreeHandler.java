package org.inek.dataportal.feature.peppproposal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
import org.inek.dataportal.entities.pepp.PeppProposal;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.PeppProposalFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.structures.ProposalInfo;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.ProposalInfoTreeNode;
import org.inek.dataportal.helper.tree.RootNode;
import org.inek.dataportal.helper.tree.TreeNode;
import org.inek.dataportal.helper.tree.TreeNodeObserver;
import org.inek.dataportal.helper.tree.YearTreeNode;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.utils.KeyValueLevel;

/**
 *
 * @author muellermi
 */
@Named @SessionScoped
public class PeppProposalTreeHandler implements Serializable, TreeNodeObserver {

    private static final Logger LOGGER = Logger.getLogger("PeppProposalTreeHandler");
    private static final long serialVersionUID = 1L;

    @Inject private PeppProposalFacade _peppProposalFacade;
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
        _rootNode.addChild(node);
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
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        if (treeNode instanceof RootNode && treeNode.getId() == 1) {
            return obtainEditNodeChildren((RootNode) treeNode);
        }
        if (treeNode instanceof RootNode && treeNode.getId() == 2) {
            return obtainViewNodeChildren((RootNode) treeNode);
        }
        if (treeNode instanceof YearTreeNode) {
            return obtainYearNodeChildren((YearTreeNode) treeNode);
        }
        if (treeNode instanceof AccountTreeNode) {
            return obtainAccountNodeChildren((AccountTreeNode) treeNode);
        }
        return new ArrayList<>();
    }

    private Collection<TreeNode> obtainEditNodeChildren(RootNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.PEPP_PROPOSAL, canReadCompleted());
        accountIds = _peppProposalFacade.
                checkAccountsForProposalOfYear(accountIds, -1, WorkflowStatus.New, WorkflowStatus.ApprovalRequested);
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
            AccountTreeNode childNode = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.
                    create(treeNode, account, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            childNode.expand();  // auto-expand all edit nodes by default
        }
        return children;
    }

    private Collection<TreeNode> obtainViewNodeChildren(RootNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.PEPP_PROPOSAL, canReadSealed());
        List<Integer> years = _peppProposalFacade.getProposalYears(accountIds);
        int targetYear = Utils.getTargetYear(Feature.PEPP_PROPOSAL);
        List<? extends TreeNode> oldChildren = new ArrayList<>(treeNode.getChildren());
        Collection<TreeNode> children = new ArrayList<>();
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
        return children;
    }

    private Collection<TreeNode> obtainYearNodeChildren(YearTreeNode treeNode) {
        Set<Integer> accountIds = _accessManager.determineAccountIds(Feature.PEPP_PROPOSAL, canReadSealed());
        accountIds = _peppProposalFacade.
                checkAccountsForProposalOfYear(accountIds, treeNode.getId(), WorkflowStatus.Provided, WorkflowStatus.Retired);
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
            AccountTreeNode childNode = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.
                    create(treeNode, account, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (account == currentUser) {
                // auto expand user's own data
                childNode.expand();
            }
        }
        return children;
    }

    private Collection<TreeNode> obtainAccountNodeChildren(AccountTreeNode treeNode) {
        int partnerId = treeNode.getId();
        List<ProposalInfo> infos;
        if (treeNode.getParent() instanceof YearTreeNode) {
            int year = treeNode.getParent().getId();
            infos = obtainProposalInfosForRead(partnerId, year);
        } else {
            infos = obtainProposalInfosForEdit(partnerId);
        }
        Collection<TreeNode> children = new ArrayList<>();
        for (ProposalInfo info : infos) {
            children.add(ProposalInfoTreeNode.create(treeNode, info, this));
        }
        return children;
    }

    private List<ProposalInfo> obtainProposalInfosForRead(int partnerId, int year) {
        DataSet dataSet;
        if (partnerId == _sessionController.getAccountId()) {
            dataSet = DataSet.AllSealed;
        } else {
            boolean canReadSealed = _accessManager.canReadSealed(Feature.PEPP_PROPOSAL, partnerId);
            dataSet = canReadSealed ? DataSet.AllSealed : DataSet.None;
        }
        return _peppProposalFacade.getPeppProposalInfos(partnerId, year, dataSet);
    }

    private List<ProposalInfo> obtainProposalInfosForEdit(int partnerId) {
        DataSet dataSet;
        if (partnerId == _sessionController.getAccountId()) {
            dataSet = DataSet.AllOpen;
        } else {
            boolean canReadAlways = _accessManager.canReadAlways(Feature.PEPP_PROPOSAL, partnerId);
            boolean canReadCompleted = _accessManager.canReadCompleted(Feature.PEPP_PROPOSAL, partnerId);
            dataSet = canReadAlways ? DataSet.AllOpen
                    : canReadCompleted ? DataSet.ApprovalRequested : DataSet.None;
        }
        return _peppProposalFacade.getPeppProposalInfos(partnerId, -1, dataSet);
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
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "id":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n2.getProposalInfo().getId(), n1.
                            getProposalInfo().getId()));
                } else {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n1.getProposalInfo().getId(), n2.
                            getProposalInfo().getId()));
                }
                break;
            case "name":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> n2.getProposalInfo().getName().compareTo(n1.getProposalInfo().
                            getName()));
                } else {
                    sorted = stream.sorted((n1, n2) -> n1.getProposalInfo().getName().compareTo(n2.getProposalInfo().
                            getName()));
                }
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
        List<PeppProposal> proposals = _peppProposalFacade.find(selectedProposals);
        Map<String, List<KeyValueLevel>> documents = new TreeMap<>();
        int count = 1;
        for (PeppProposal proposal : proposals) {
            String key = proposal.getExternalId();
            if (key.isEmpty()) {
                key = "<nicht gesendet> " + count++;
            }
            documents.put(key, DocumentationUtil.getDocumentation(proposal));
        }
        List<String> keys = new ArrayList<>(documents.keySet());
        Utils.getFlash().put("headLine", Utils.getMessage("namePEPP_PROPOSAL"));
        Utils.getFlash().put("targetPage", Pages.PeppProposalSummary.URL());
        Utils.getFlash().put("printContentKeys", keys);
        Utils.getFlash().put("printContent", documents);
        return Pages.PrintMultipleView.URL();
    }

}
