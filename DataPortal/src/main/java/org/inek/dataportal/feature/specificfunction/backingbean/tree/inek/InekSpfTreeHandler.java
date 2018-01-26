package org.inek.dataportal.feature.specificfunction.backingbean.tree.inek;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.inek.dataportal.helper.tree.RootNode;
import org.inek.dataportal.helper.tree.TreeNode;
import org.inek.dataportal.helper.tree.TreeNodeObserver;

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
    public Collection<TreeNode> obtainChildren(TreeNode treeNode) {
        List<SpecificFunctionRequest> infos = _specificFunctionFacade.
                getSpecificFunctionsForInek(getYear(), getFilter());
        Collection<TreeNode> children = new ArrayList<>();
        for (SpecificFunctionRequest info : infos) {
            children.add(SpecificFunctionRequestTreeNode.create(treeNode, info, null));
        }
        return children;
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
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode) {
        Stream<SpecificFunctionRequestTreeNode> stream = treeNode.getChildren().stream().map(n -> (SpecificFunctionRequestTreeNode) n);
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
