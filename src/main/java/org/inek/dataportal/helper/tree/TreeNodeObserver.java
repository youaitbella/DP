/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
