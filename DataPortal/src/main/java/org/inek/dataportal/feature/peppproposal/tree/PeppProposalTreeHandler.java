package org.inek.dataportal.feature.peppproposal.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.AccessManager;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.pepp.PeppProposal;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.PeppProposalFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.ProposalInfoTreeNode;
import org.inek.dataportal.helper.tree.RootNode;
import org.inek.dataportal.helper.tree.TreeNode;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.utils.KeyValueLevel;

/**
 *
 * @author muellermi
 */
@Named
@SessionScoped
public class PeppProposalTreeHandler implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("PeppProposalTreeHandler");
    private static final long serialVersionUID = 1L;

    @Inject
    private PeppProposalFacade _peppProposalFacade;
    @Inject
    private SessionController _sessionController;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private AccountFacade _accountFacade;

    @Inject
    private Instance<AccountTreeNodeObserver> _accountTreeNodeObserverProvider;

    private final RootNode _rootNode = RootNode.create(0, null);
    private AccountTreeNode _accountNode;

    private RootNode getRootNode(int id) {
        Optional<TreeNode> optionalRoot = _rootNode.getChildren().stream().filter(n -> n.getId() == id).findAny();
        if (optionalRoot.isPresent()) {
            return (RootNode) optionalRoot.get();
        }
        RootNode node = RootNode.create(id, null);
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
            _accountNode = AccountTreeNode.create(null, _sessionController.getAccount(), _accountTreeNodeObserverProvider.get());
            _accountNode.expand();
        }
        return _accountNode;
    }

//    @Override
//    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
//        return new ArrayList<>();
//    }
//    @Override
//    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
//        if (treeNode instanceof AccountTreeNode) {
//            return sortAccountNodeChildren((AccountTreeNode) treeNode);
//        }
//        return treeNode.getChildren();
//    }
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
