/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.tree.entityTree;

import org.inek.dataportal.common.tree.TreeNode;

/**
 *
 * @author muellermi
 */
public abstract class EntityTreeNode extends TreeNode{

    public abstract String getDisplayName();
    
    public abstract String getCompany();

    public abstract String getTown();

    protected EntityTreeNode(TreeNode parent) {
        super(parent);
    }

    
}
