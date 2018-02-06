/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.tree;

/**
 *
 * @author muellermi
 */
public final class YearTreeNode extends TreeNode{

    private final int _year;

    public int getYear() {
        return _year;
    }

    private YearTreeNode(TreeNode parent, int year) {
        super(parent);
        _year = year;
        setId(year);
    }

    public static YearTreeNode create(TreeNode parent, int year, TreeNodeObserver observer) {
        YearTreeNode node = new YearTreeNode(parent, year);
        node.registerObserver(observer);
        return node;
    }

    
}
