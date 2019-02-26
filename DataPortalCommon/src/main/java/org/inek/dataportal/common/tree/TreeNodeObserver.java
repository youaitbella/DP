package org.inek.dataportal.common.tree;

import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author muellermi
 */
public interface TreeNodeObserver extends Serializable {

    Collection<TreeNode> obtainChildren(TreeNode treeNode);

    default Collection<TreeNode> obtainSortedChildren(TreeNode treeNode){
        return treeNode.getChildren();
    }
   
}
