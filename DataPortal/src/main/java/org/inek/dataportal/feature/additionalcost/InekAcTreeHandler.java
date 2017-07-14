package org.inek.dataportal.feature.additionalcost;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.ApplicationTools;
import org.inek.dataportal.entities.additionalcost.AdditionalCost;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.AdditionalCostFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.portallib.tree.RootNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;

/**
 *
 * @author muellermi
 */
@Named @SessionScoped
public class InekAcTreeHandler implements Serializable, TreeNodeObserver {

    private static final Logger LOGGER = Logger.getLogger("InekAcTreeHandler");
    private static final long serialVersionUID = 1L;

    @Inject private AdditionalCostFacade _additionalCostFacade;
    @Inject private ApplicationTools _appTools;

    private final RootNode _rootNode = RootNode.create(0, this);

    public RootNode getRootNode() {
        if (!_rootNode.isExpanded()){
            _rootNode.expand();
        }
        return _rootNode;
    }

    public void refreshNodes() {
        _rootNode.refresh();
    }

    @Override
    public void obtainChildren(TreeNode treeNode, Collection<TreeNode> children) {
        if (treeNode instanceof RootNode) {
            obtainRootNodeChildren((RootNode) treeNode, children);
        }
    }

    private void obtainRootNodeChildren(RootNode node, Collection<TreeNode> children) {
        List<AdditionalCost> infos = _additionalCostFacade.getAdditionalCostsForInek(Utils.getTargetYear(Feature.ADDITIONAL_COST));
        node.getChildren().clear();
        for (AdditionalCost info : infos) {
            node.getChildren().add(AdditionalCostTreeNode.create(node, info, this));
        }
    }


    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode, Collection<TreeNode> children) {
        if (treeNode instanceof RootNode) {
            return sortChildren((RootNode) treeNode, children);
        }
        return children;
    }

    public Collection<TreeNode> sortChildren(RootNode treeNode, Collection<TreeNode> children) {
        Stream<AdditionalCostTreeNode> stream = children.stream().map(n -> (AdditionalCostTreeNode) n);
        Stream<AdditionalCostTreeNode> sorted;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "ik":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n2.getAdditionalCost().getIk(), 
                            n1.getAdditionalCost().getIk()));
                } else {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n1.getAdditionalCost().getIk(), 
                            n2.getAdditionalCost().getIk()));
                }
                break;
            case "hospital":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> _appTools.retrieveHospitalInfo(n2.getAdditionalCost().getIk())
                            .compareTo(_appTools.retrieveHospitalInfo(n1.getAdditionalCost().getIk())));
                } else {
                    sorted = stream.sorted((n1, n2) -> _appTools.retrieveHospitalInfo(n1.getAdditionalCost().getIk())
                            .compareTo(_appTools.retrieveHospitalInfo(n2.getAdditionalCost().getIk())));
                }
                break;
//            case "code":
//                if (treeNode.isDescending()) {
//                    sorted = stream.sorted((n1, n2) -> n2.getAdditionalCost().getCode()
//                            .compareTo(n1.getAdditionalCost().getCode()));
//                } else {
//                    sorted = stream.sorted((n1, n2) -> n1.getAdditionalCost().getCode()
//                            .compareTo(n2.getAdditionalCost().getCode()));
//                }
//                break;
            case "status":
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }
}
