package org.inek.dataportal.drg.specificfunction.backingbean.tree.hospital;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.drg.specificfunction.backingbean.tree.SpecificFunctionRequestTreeNode;

public class Sorter {

    public static Collection<TreeNode> obtainSortedChildren(TreeNode treeNode, ApplicationTools appTools) {
        Stream<SpecificFunctionRequestTreeNode> stream = treeNode.getChildren().stream().
                map(n -> (SpecificFunctionRequestTreeNode) n);
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
                sorted = stream.sorted((n1, n2) -> direction * appTools.retrieveHospitalInfo(n1.
                        getSpecificFunctionRequest().getIk())
                        .compareTo(appTools.retrieveHospitalInfo(n2.getSpecificFunctionRequest().getIk())));
                break;
            case "code":
                sorted = stream.sorted((n1, n2) -> direction * n1.getSpecificFunctionRequest().getCode()
                        .compareTo(n2.getSpecificFunctionRequest().getCode()));
                break;
            default:
                sorted = stream;
        }
        return sorted.collect(Collectors.toList());
    }

}
