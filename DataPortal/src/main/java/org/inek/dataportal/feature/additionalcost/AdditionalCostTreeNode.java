/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.additionalcost;

import org.inek.dataportal.entities.additionalcost.AdditionalCost;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;

/**
 *
 * @author aitbellayo
 */
class AdditionalCostTreeNode extends TreeNode{
    
    private final AdditionalCost _additionalCost;
    
    public AdditionalCost getAdditionalCost (){
        return _additionalCost;
    }
    
    private AdditionalCostTreeNode(TreeNode parent, AdditionalCost info) {
        super(parent);
        _additionalCost = info;
        setId(info.getId());
        setExpanded(true);
    }
    
    public static AdditionalCostTreeNode create (TreeNode parent, AdditionalCost info, TreeNodeObserver observer) {
        AdditionalCostTreeNode node = new AdditionalCostTreeNode(parent, info);
        node.registerObserver(observer);
        return node;
    }
}
