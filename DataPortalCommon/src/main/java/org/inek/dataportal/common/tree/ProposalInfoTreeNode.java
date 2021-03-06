/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.tree;

import org.inek.dataportal.common.helper.structures.ProposalInfo;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;

/**
 *
 * @author muellermi
 */
public final class ProposalInfoTreeNode extends TreeNode{
    
    private final ProposalInfo _info;
    
    public ProposalInfo getProposalInfo (){
        return _info;
    }
    
    private ProposalInfoTreeNode(TreeNode parent, ProposalInfo info) {
        super(parent);
        _info = info;
        setId(info.getId());
        setExpanded(true);
    }
    
    public static ProposalInfoTreeNode create (TreeNode parent, ProposalInfo info, TreeNodeObserver observer) {
        ProposalInfoTreeNode node = new ProposalInfoTreeNode(parent, info);
        node.registerObserver(observer);
        return node;
    }
}
