package org.inek.dataportal.feature.nub;

import org.inek.dataportal.helper.tree.TreeNodeObserver;
import org.inek.dataportal.helper.tree.RootNode;
import org.inek.dataportal.helper.tree.AccountTreeNode;
import org.inek.dataportal.helper.tree.TreeNode;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.CooperationTools;
import static org.inek.dataportal.common.CooperationTools.canReadSealed;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.cooperation.CooperationRight;
import org.inek.dataportal.entities.NubRequest;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.NubRequestFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;
import org.inek.dataportal.helper.structures.ProposalInfo;
import org.inek.dataportal.helper.tree.ProposalInfoTreeNode;
import org.inek.dataportal.helper.tree.YearTreeNode;

@Named @SessionScoped
public class NubSessionTools implements Serializable, TreeNodeObserver {

    private static final Logger _logger = Logger.getLogger("NubSessionTools");
    private static final long serialVersionUID = 1L;

    @Inject private CooperationRightFacade _cooperationRightFacade;
    @Inject private NubRequestFacade _nubRequestFacade;
    @Inject private SessionController _sessionController;

    private String _nubFilter = "";

    public String getNubFilter() {
        return _nubFilter;
    }

    public void setNubFilter(String nubFilter) {
        _nubFilter = nubFilter;
    }

    // Seal own NUB is a marker, whether a NUB may be sealed by the owner (true)
    // or by a supervisor only (false)
    // It is used in coopearative environment
    private Map<Integer, Boolean> _sealOwnNub;

    public Map<Integer, Boolean> getSealOwnNub() {
        ensureSealOwnNub();
        return _sealOwnNub;
    }

    /**
     * clears cache of sealOwnNub e.g. to ensure update after changing rights.
     */
    public void clearCache() {
        _sealOwnNub = null;
    }

    private void ensureSealOwnNub() {
        if (_sealOwnNub != null) {
            return;
        }
        _sealOwnNub = new HashMap<>();
        Account account = _sessionController.getAccount();
        for (int ik : account.getFullIkList()) {
            // allowed for own NUB if supervisor herself or no supervisor exists
            _sealOwnNub.put(ik, _cooperationRightFacade.isIkSupervisor(Feature.NUB, ik, account.getId()) || !_cooperationRightFacade.hasSupervisor(Feature.NUB, ik));
        }
        List<CooperationRight> rights = _cooperationRightFacade
                .getGrantedCooperationRights(account.getId(), Feature.NUB);
        for (CooperationRight right : rights) {
            if (right.getCooperativeRight().equals(CooperativeRight.ReadCompletedSealSupervisor)
                    || right.getCooperativeRight().equals(CooperativeRight.ReadWriteCompletedSealSupervisor)
                    || right.getCooperativeRight().equals(CooperativeRight.ReadWriteSealSupervisor)) {
                _sealOwnNub.put(right.getIk(), Boolean.FALSE);
            }
        }
    }

    public CooperativeRight getCooperativeRight(NubRequest nubRequest) {
        return _cooperationRightFacade.getCooperativeRight(
                nubRequest.getAccountId(),
                _sessionController.getAccountId(),
                Feature.NUB,
                nubRequest.getIk());
    }

    public CooperativeRight getSupervisorRight(NubRequest nub) {
        return _cooperationRightFacade.getIkSupervisorRight(Feature.NUB, nub.getIk(), _sessionController.getAccountId());
    }

    private RootNode _viewNode = RootNode.create(1, this);
    private RootNode _editNode = RootNode.create(0, this);
    @Inject private CooperationTools _cooperationTools;

    public RootNode getEditNode() {
        if (!_editNode.isExpanded()) {
            _editNode.expand();
        }
        return _editNode;
    }

    public RootNode getViewNode() {
        if (!_viewNode.isExpanded()) {
            _viewNode.expand();
        }
        return _viewNode;
    }

    @Override
    public void obtainChildren(TreeNode treeNode, Collection<TreeNode> children) {
        if (treeNode instanceof RootNode && treeNode == _editNode) {
            obtainNubEditNodeChildren((RootNode) treeNode, children);
        }
        if (treeNode instanceof RootNode && treeNode == _viewNode) {
            obtainNubViewNodeChildren((RootNode) treeNode, children);
        }
        if (treeNode instanceof YearTreeNode) {
            obtainYearNodeChildren((YearTreeNode) treeNode, children);
        }
        if (treeNode instanceof AccountTreeNode) {
            obtainAccountNodeChildren((AccountTreeNode) treeNode, children);
        }
    }

    private void obtainNubEditNodeChildren(RootNode treeNode, Collection<TreeNode> children) {
        List<Account> accounts = _cooperationTools.getPartnersForEdit(Feature.NUB);
        accounts.add(0, _sessionController.getAccount());
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        children.clear();
        for (Account account : accounts) {
            Integer id = account.getId();
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == id).findFirst();
            AccountTreeNode node = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.create(treeNode, account, this);
            children.add((TreeNode) node);
            oldChildren.remove(node);
        }
    }

    private void obtainNubViewNodeChildren(RootNode treeNode, Collection<TreeNode> children) {
        Set<Integer> accountIds = _cooperationTools.determineAccountIds(Feature.NUB, canReadSealed());
        List<Integer> years = _nubRequestFacade.getNubYears(accountIds);
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        int targetYear = LocalDateTime.now().getYear() + (LocalDateTime.now().getMonthValue() >= 9 ? 1 : 0);
        children.clear();
        for (Integer year : years) {
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == year).findFirst();
            YearTreeNode childNode = existing.isPresent() ? (YearTreeNode) existing.get() : YearTreeNode.create(treeNode, year, this);
            children.add((TreeNode) childNode);
            oldChildren.remove(childNode);
            if (year == targetYear) {
                childNode.expand();
            }
        }
    }

    @Inject private AccountFacade _accountFacade;

    private void obtainYearNodeChildren(YearTreeNode node, Collection<TreeNode> children) {
        Set<Integer> accountIds = _cooperationTools.determineAccountIds(Feature.NUB, canReadSealed());
        accountIds = _nubRequestFacade.checkAccountsForNubOfYear(accountIds, node.getId(), WorkflowStatus.Provided, WorkflowStatus.Retired);
        List<Account> accounts = _accountFacade.getAccountsForIds(accountIds);
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
            if (account == currentUser) {
                // auto expand user's own data
                childNode.expand();
            }
        }
    }

    private void obtainAccountNodeChildren(AccountTreeNode accountTreeNode, Collection<TreeNode> children) {
        int partnerId = accountTreeNode.getId();
        int year = accountTreeNode.getParent().getId();
        List<ProposalInfo> infos = new ArrayList<>();
        if (partnerId == _sessionController.getAccountId()) {
            infos = _nubRequestFacade.getNubRequestInfos(_sessionController.getAccountId(), -1, year, DataSet.AllSealed, getFilter());
        } else {
            Set<Integer> iks = _cooperationTools.getPartnerIks(Feature.NUB, partnerId);
            for (int ik : iks) {
                CooperativeRight achievedRight = _cooperationTools.getAchievedRight(Feature.NUB, partnerId, ik);
                DataSet dataSet = achievedRight.canReadSealed() ? DataSet.AllSealed : DataSet.None;
                List<ProposalInfo> infosForIk = _nubRequestFacade.getNubRequestInfos(partnerId, ik, year, dataSet, getFilter());
                infos.addAll(infosForIk);
            }
        }
        accountTreeNode.getChildren().clear();
        for (ProposalInfo info : infos) {
            accountTreeNode.getChildren().add(ProposalInfoTreeNode.create(accountTreeNode, info, this));
        }
    }
public List<ProposalInfo> getNubRequests(AccountTreeNode accountNode){
    return accountNode.getChildren().stream().map(a -> ((ProposalInfoTreeNode)a).getProposalInfo()).collect(Collectors.toList());
}
    private String getFilter() {
        String filter = getNubFilter();
        if (!filter.isEmpty() && !filter.contains("%")) {
            filter = "%" + filter + "%";
        }
        return filter;
    }

    /*    
    Map<Integer, List<ProposalInfo>> _partnerNubsForDisplay = new HashMap<>();

    public List<ProposalInfo> getNubRequestsForDisplayFromPartner(int partnerId, int year) {
        if (!_partnerNubsForDisplay.containsKey(partnerId)) {
            if (partnerId == _sessionController.getAccountId()) {
                List<ProposalInfo> infos = _nubRequestFacade.getNubRequestInfos(_sessionController.getAccountId(), -1, year, DataSet.AllSealed, getFilter());
                _partnerNubsForDisplay.put(partnerId, infos);
            } else {
                List<ProposalInfo> infos = new ArrayList<>();
                Set<Integer> iks = _cooperationTools.getPartnerIks(Feature.NUB, partnerId);
                for (int ik : iks) {
                    CooperativeRight achievedRight = _cooperationTools.getAchievedRight(Feature.NUB, partnerId, ik);
                    DataSet dataSet = achievedRight.canReadSealed() ? DataSet.AllSealed : DataSet.None;
                    List<ProposalInfo> infosForIk = _nubRequestFacade.getNubRequestInfos(partnerId, ik, year, dataSet, getFilter());
                    infos.addAll(infosForIk);
                }
                _partnerNubsForDisplay.put(partnerId, infos);
            }
        }
        return _partnerNubsForDisplay.get(partnerId);
    }
     */
}
