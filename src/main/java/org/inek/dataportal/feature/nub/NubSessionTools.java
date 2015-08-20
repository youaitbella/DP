package org.inek.dataportal.feature.nub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.CooperationTools;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.cooperation.CooperationRight;
import org.inek.dataportal.entities.NubRequest;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.enums.CooperativeRight;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.cooperation.CooperationRightFacade;

@Named @SessionScoped
public class NubSessionTools implements Serializable, TreeNodeObserver {

    private static final Logger _logger = Logger.getLogger("NubSessionTools");
    private static final long serialVersionUID = 1L;

    @Inject private CooperationRightFacade _cooperationRightFacade;
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

    private NubEditNode _editNode;

    @Inject
    private void createEditNode(CooperationTools cooperationTools) {
        _editNode = new NubEditNode(cooperationTools);
    }

    public NubEditNode getEditNode() {
        _editNode.updateChildrenIfIsExpanded();
        return _editNode;
    }

    private NubViewNode _viewNode = new NubViewNode();
    @Inject private CooperationTools _cooperationTools;

    @PostConstruct
    private void init() {
        _viewNode.addObserver(this);
    }

    public NubViewNode getViewNode() {
        if (!_viewNode.isExpanded()) {
            _viewNode.expandNode();
        }
        return _viewNode;
    }

    public Collection<TreeNode> getChildren(){
        if (!_viewNode.isExpanded()) {
            _viewNode.expandNode();
        }
        return _viewNode.getChildren();
    }
    
    @Override
    public void obtainChildren(TreeNode treeNode, List<TreeNode> children) {
        if (treeNode instanceof NubViewNode) {
            obtainNubViewNodeChildren(children);
        }
    }

    private void obtainNubViewNodeChildren(List<TreeNode> children) {
        List<Account> accounts = _cooperationTools.getPartnersForDisplay(Feature.NUB);
        accounts.add(0, _sessionController.getAccount());
        List<? extends TreeNode> oldChildren = new ArrayList<>(children);
        children.clear();
        for (Account account : accounts) {
            Integer id = account.getId();
            Optional<? extends TreeNode> existing = oldChildren.stream().filter(n -> n.getId() == id).findFirst();
            AccountTreeNode node = existing.isPresent() ? (AccountTreeNode) existing.get() : AccountTreeNode.createTreeNode(account);
            children.add((TreeNode) node);
        }
    }
}
