/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.tree;

import org.inek.dataportal.entities.specificfunction.SpecificFunctionAgreement;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;

/**
 *
 * @author muellermi
 */
public class SpecificFunctionAgreementTreeNode extends TreeNode{
    
    private final SpecificFunctionAgreement _agreement;
    
    public SpecificFunctionAgreement getSpecificFunctionAgreement (){
        return _agreement;
    }
    
    private SpecificFunctionAgreementTreeNode(TreeNode parent, SpecificFunctionAgreement info) {
        super(parent);
        _agreement = info;
        setId(info.getId());
        setExpanded(true);
    }
    
    public static SpecificFunctionAgreementTreeNode create (TreeNode parent, SpecificFunctionAgreement info, TreeNodeObserver observer) {
        SpecificFunctionAgreementTreeNode node = new SpecificFunctionAgreementTreeNode(parent, info);
        node.registerObserver(observer);
        return node;
    }
}
