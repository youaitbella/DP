package org.inek.dataportal.feature.specificfunction.backingbean.tree.insurance;

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
import javax.enterprise.inject.Instance;
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
import org.inek.dataportal.feature.specificfunction.backingbean.tree.hospital.AccountTreeNodeObserver;
import org.inek.dataportal.feature.specificfunction.facade.SpecificFunctionFacade;
import org.inek.dataportal.feature.specificfunction.entity.SpecificFunctionAgreement;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.tree.entityTree.AccountTreeNode;
import org.inek.dataportal.helper.tree.SpecificFunctionAgreementTreeNode;
import org.inek.dataportal.helper.tree.SpecificFunctionRequestTreeNode;
import org.inek.dataportal.helper.tree.RootNode;
import org.inek.dataportal.helper.tree.TreeNode;
import org.inek.dataportal.helper.tree.TreeNodeObserver;
import org.inek.dataportal.helper.tree.YearTreeNode;
import org.inek.dataportal.utils.KeyValueLevel;

/**
 *
 * @author muellermi
 */
@Named @SessionScoped
public class SpecificFunctionAgreementTreeHandler implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("SpecificFunctionAgreementTreeHandler");
    private static final long serialVersionUID = 1L;

    @Inject private SessionController _sessionController;
    @Inject private ApplicationTools _appTools;
    
    @Inject private Instance<AccountTreeNodeObserver> _accountTreeNodeObserverProvider;

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
