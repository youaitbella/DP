package org.inek.dataportal.feature.calculationhospital.tree.user;

import java.io.Serializable;
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
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.CalcHospitalTreeNode;
import org.inek.dataportal.common.tree.RootNode;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.common.utils.KeyValueLevel;

/**
 *
 * @author muellermi
 */
@Named @SessionScoped
public class CalcHospitalTreeHandler implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("CalcHospitalTreeHandler");
    private static final long serialVersionUID = 1L;

    @Inject private SessionController _sessionController;
    @Inject private Instance<EditRootTreeNodeObserver> _editRootTreeNodeObserverProvider;
    @Inject private Instance<ViewRootTreeNodeObserver> _viewRootTreeNodeObserverProvider;
    @Inject private Instance<AccountTreeNodeObserver> _accountTreeNodeObserverProvider;

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
            _accountNode = AccountTreeNode.
                    create(null, _sessionController.getAccount(), _accountTreeNodeObserverProvider.get());
            _accountNode.expand();
        }
        return _accountNode;
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
