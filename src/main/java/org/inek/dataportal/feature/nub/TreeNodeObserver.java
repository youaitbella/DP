/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.nub;

import java.util.List;

/**
 *
 * @author muellermi
 */
public interface TreeNodeObserver {
    public void obtainChildren (TreeNode treeNode, List<TreeNode> children);
}
