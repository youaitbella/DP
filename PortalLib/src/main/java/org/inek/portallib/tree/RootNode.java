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
public final class RootNode extends TreeNode {

    private RootNode(TreeNode parent, int id) {
        super(parent);
        setId(id);
    }

    public static RootNode create(int id, TreeNodeObserver observer) {
        RootNode node = new RootNode(null, id);
        node.registerObserver(observer);
        return node;
    }

}
