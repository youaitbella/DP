package org.inek.dataportal.helper.tree;

import java.util.Collection;

/**
 *
 * @author muellermi
 */
public interface TreeNodeObserver {
    public void obtainChildren (TreeNode treeNode, Collection<TreeNode> children);
    public Collection<TreeNode> obtainSortedChildren (TreeNode treeNode, Collection<TreeNode> children);
}
