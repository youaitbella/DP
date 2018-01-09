package org.inek.portallib.tree;

import java.util.Collection;

/**
 *
 * @author muellermi
 */
public interface TreeNodeObserver {

    void obtainChildren(TreeNode treeNode, Collection<TreeNode> children);

    default Collection<TreeNode> obtainSortedChildren(TreeNode treeNode){
        return treeNode.getChildren();
    }
   
}
