package org.inek.dataportal.feature.specificfunction;

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
import org.inek.dataportal.entities.specificfunction.SpecificFunctionRequest;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.facades.SpecificFunctionFacade;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.tree.SpecificFunctionRequestTreeNode;
import org.inek.portallib.tree.RootNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;

/**
 *
 * @author muellermi
 */
@Named @SessionScoped
public class InekSpfTreeHandler implements Serializable, TreeNodeObserver {

    private static final Logger LOGGER = Logger.getLogger("InekSpfTreeHandler");
    private static final long serialVersionUID = 1L;

    @Inject private SpecificFunctionFacade _specificFunctionFacade;
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
        List<SpecificFunctionRequest> infos = _specificFunctionFacade.getCalcBasicsForInek(Utils.getTargetYear(Feature.SPECIFIC_FUNCTION));
        node.getChildren().clear();
        for (SpecificFunctionRequest info : infos) {
            node.getChildren().add(SpecificFunctionRequestTreeNode.create(node, info, this));
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
        Stream<SpecificFunctionRequestTreeNode> stream = children.stream().map(n -> (SpecificFunctionRequestTreeNode) n);
        Stream<SpecificFunctionRequestTreeNode> sorted;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "ik":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n2.getSpecificFunctionRequest().getIk(), n1.getSpecificFunctionRequest().getIk()));
                } else {
                    sorted = stream.sorted((n1, n2) -> Integer.compare(n1.getSpecificFunctionRequest().getIk(), n2.getSpecificFunctionRequest().getIk()));
                }
                break;
            case "hospital":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> _appTools.retrieveHospitalInfo(n2.getSpecificFunctionRequest().getIk())
                            .compareTo(_appTools.retrieveHospitalInfo(n1.getSpecificFunctionRequest().getIk())));
                } else {
                    sorted = stream.sorted((n1, n2) -> _appTools.retrieveHospitalInfo(n1.getSpecificFunctionRequest().getIk())
                            .compareTo(_appTools.retrieveHospitalInfo(n2.getSpecificFunctionRequest().getIk())));
                }
                break;
            case "code":
                if (treeNode.isDescending()) {
                    sorted = stream.sorted((n1, n2) -> n2.getSpecificFunctionRequest().getCode().compareTo(n1.getSpecificFunctionRequest().getCode()));
                } else {
                    sorted = stream.sorted((n1, n2) -> n1.getSpecificFunctionRequest().getCode().compareTo(n2.getSpecificFunctionRequest().getCode()));
                }
                break;
            case "status":
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }
}
