/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.tree;

import org.inek.dataportal.entities.specificfunction.SpecificFunctionRequest;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;

/**
 *
 * @author muellermi
 */
public class SpecificFunctionRequestTreeNode extends TreeNode{
    
    private final SpecificFunctionRequest _request;
    
    public SpecificFunctionRequest getSpecificFunctionRequest (){
        return _request;
    }
    
    private SpecificFunctionRequestTreeNode(TreeNode parent, SpecificFunctionRequest info) {
        super(parent);
        _request = info;
        setId(info.getId());
        setExpanded(true);
    }
    
    public static SpecificFunctionRequestTreeNode create (TreeNode parent, SpecificFunctionRequest info, TreeNodeObserver observer) {
        SpecificFunctionRequestTreeNode node = new SpecificFunctionRequestTreeNode(parent, info);
        node.registerObserver(observer);
        return node;
    }
}
