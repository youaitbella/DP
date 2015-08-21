/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.tree;

/**
 *
 * @author muellermi
 */
public class YearTreeNode extends TreeNode{


    private YearTreeNode(int year) {
        setId(year);
    }

    public static YearTreeNode createTreeNode(int year, TreeNodeObserver observer) {
        YearTreeNode node = new YearTreeNode(year);
        node.registerObserver(observer);
        return node;
    }

    
}
