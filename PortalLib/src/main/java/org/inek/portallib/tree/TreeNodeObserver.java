package org.inek.portallib.tree;

import java.util.Collection;

/**
 *
 * @author muellermi
 */
public interface TreeNodeObserver {

    void obtainChildren(TreeNode treeNode);

    default Collection<TreeNode> obtainSortedChildren(TreeNode treeNode){
        return treeNode.getChildren();
    }
   
}
