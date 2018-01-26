package org.inek.dataportal.helper.tree;

import java.util.Collection;

/**
 *
 * @author muellermi
 */
public interface TreeNodeObserver {

    Collection<TreeNode> obtainChildren(TreeNode treeNode);

    default Collection<TreeNode> obtainSortedChildren(TreeNode treeNode){
        return treeNode.getChildren();
    }
   
}
