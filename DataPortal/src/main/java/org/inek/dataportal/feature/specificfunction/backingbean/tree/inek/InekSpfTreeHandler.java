package org.inek.dataportal.feature.specificfunction.backingbean.tree.inek;

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
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.feature.specificfunction.entity.SpecificFunctionRequest;
import org.inek.dataportal.feature.specificfunction.facade.SpecificFunctionFacade;
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
        if (!_rootNode.isExpanded()) {
            _rootNode.expand();
        }
        return _rootNode;
    }

    public void refreshNodes() {
        _rootNode.refresh();
    }

    @Override
    public void obtainChildren(TreeNode treeNode, Collection<TreeNode> children) {
        obtainRootNodeChildren((RootNode) treeNode, children);
    }

    private void obtainRootNodeChildren(RootNode node, Collection<TreeNode> children) {
        List<SpecificFunctionRequest> infos = _specificFunctionFacade.
                getSpecificFunctionsForInek(getYear(), getFilter());
        node.getChildren().clear();
        for (SpecificFunctionRequest info : infos) {
            node.getChildren().add(SpecificFunctionRequestTreeNode.create(node, info, null));
        }
    }

    private String _filter = "";

    public String getFilter() {
        return _filter;
    }

    public void setFilter(String filter) {
        _filter = filter == null ? "" : filter;
        refreshNodes();
    }

    private int _year = Utils.getTargetYear(Feature.SPECIFIC_FUNCTION);

    public int getYear() {
        return _year;
    }

    public void setYear(int year) {
        _year = year;
        refreshNodes();
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode, Collection<TreeNode> children) {
        Stream<SpecificFunctionRequestTreeNode> stream = children.stream().map(n -> (SpecificFunctionRequestTreeNode) n);
        Stream<SpecificFunctionRequestTreeNode> sorted;
        int direction = treeNode.isDescending() ? -1 : 1;
        switch (treeNode.getSortCriteria().toLowerCase()) {
            case "ik":
                sorted = stream.sorted((n1, n2) -> direction * Integer.compare(n1.getSpecificFunctionRequest().getIk(),
                        n2.getSpecificFunctionRequest().getIk()));
                break;
            case "year":
                sorted = stream.sorted((n1, n2) -> direction * Integer.compare(n1.getSpecificFunctionRequest().
                        getDataYear(),
                        n2.getSpecificFunctionRequest().getDataYear()));
                break;
            case "hospital":
                sorted = stream.sorted((n1, n2) -> direction * _appTools.retrieveHospitalInfo(
                        n1.getSpecificFunctionRequest().getIk())
                        .compareTo(_appTools.retrieveHospitalInfo(n2.getSpecificFunctionRequest().getIk())));
                break;
            case "code":
                sorted = stream.sorted((n1, n2) -> direction * n1.getSpecificFunctionRequest().getCode()
                        .compareTo(n2.getSpecificFunctionRequest().getCode()));
                break;
            case "status":
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }

}
