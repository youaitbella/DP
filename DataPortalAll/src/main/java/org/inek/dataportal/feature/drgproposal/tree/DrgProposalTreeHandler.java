package org.inek.dataportal.feature.drgproposal.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.drg.DrgProposal;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.DrgProposalFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.ProposalInfoTreeNode;
import org.inek.dataportal.helper.tree.RootNode;
import org.inek.dataportal.helper.tree.TreeNode;
import org.inek.dataportal.helper.tree.TreeNodeObserver;
import org.inek.dataportal.utils.DocumentationUtil;
import org.inek.dataportal.utils.KeyValueLevel;

/**
 *
 * @author muellermi
 */
@Named @SessionScoped
public class DrgProposalTreeHandler implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("DrpProposalTreeHandler");
    private static final long serialVersionUID = 1L;

    @Inject private DrgProposalFacade _drgProposalFacade;
    @Inject private SessionController _sessionController;
    @Inject private Instance<AccountTreeNodeObserver> _accountTreeNodeObserverProvider;
    @Inject private Instance<EditRootTreeNodeObserver> _editRootTreeNodeObserverProvider;
    @Inject private Instance<ViewRootTreeNodeObserver> _viewRootTreeNodeObserverProvider;

    private RootNode _rootNode;
    private AccountTreeNode _accountNode;

    @PostConstruct
    private void init() {
        _rootNode = RootNode.create(0, null);
        _rootNode.setExpanded(true);
    }

    public RootNode getEditNode() {
        return getRootNode(1, _editRootTreeNodeObserverProvider);
    }

    public RootNode getViewNode() {
        return getRootNode(2, _viewRootTreeNodeObserverProvider);
    }

    private RootNode getRootNode(int id,
            Instance<? extends TreeNodeObserver> treeNodeObserverProvider) {
        Optional<TreeNode> optionalRoot = _rootNode.getChildren().stream().filter(n -> n.getId() == id).findAny();
        if (optionalRoot.isPresent()) {
            return (RootNode) optionalRoot.get();
        }
        RootNode node = RootNode.create(id, treeNodeObserverProvider.get());
        node.expand();
        _rootNode.addChild(node);
        return node;
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
