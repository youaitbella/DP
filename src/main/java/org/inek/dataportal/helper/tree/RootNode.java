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
public class RootNode extends TreeNode {

    private RootNode(int id) {
        setId(id);
    }

    public static RootNode createTreeNode(int id, TreeNodeObserver observer) {
        RootNode node = new RootNode(id);
        node.registerObserver(observer);
        return node;
    }

}
