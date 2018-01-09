package org.inek.dataportal.feature.specificfunction.backingbean.tree.hospital;

import org.inek.dataportal.feature.specificfunction.backingbean.tree.hospital.ViewRootTreeNodeObserver;
import org.inek.dataportal.feature.specificfunction.backingbean.tree.hospital.AccountTreeNodeObserver;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.feature.specificfunction.backingbean.tree.hospital.EditRootTreeNodeObserver;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.SpecificFunctionRequestTreeNode;
import org.inek.portallib.tree.RootNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.dataportal.utils.KeyValueLevel;
import org.inek.portallib.tree.TreeNodeObserver;

/**
 *
 * @author muellermi
 */
@Named @SessionScoped
public class SpecificFunctionRequestTreeHandler implements Serializable {

    private static final long serialVersionUID = 1L;

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
        _rootNode.getChildren().add(node);
        return node;
    }

    public void refreshNodes() {
        _rootNode.refresh();
    }

    public AccountTreeNode getAccountNode() {
        if (_accountNode == null) {
            _accountNode = AccountTreeNode.
                    create(null, _sessionController.getAccount(), _accountTreeNodeObserverProvider.get());
            _accountNode.expand();
        }
        return _accountNode;
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
