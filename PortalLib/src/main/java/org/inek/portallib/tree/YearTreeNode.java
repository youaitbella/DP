/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.portallib.tree;

/**
 *
 * @author muellermi
 */
public final class YearTreeNode extends TreeNode{


    private YearTreeNode(TreeNode parent, int year) {
        super(parent);
        setId(year);
    }

    public static YearTreeNode create(TreeNode parent, int year, TreeNodeObserver observer) {
        YearTreeNode node = new YearTreeNode(parent, year);
        node.registerObserver(observer);
        return node;
    }

    
}
