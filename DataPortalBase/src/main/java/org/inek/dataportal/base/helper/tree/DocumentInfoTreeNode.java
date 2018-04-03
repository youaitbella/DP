/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.base.helper.tree;

import org.inek.dataportal.common.helper.structures.DocInfo;
import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;

/**
 *
 * @author muellermi
 */
public final class DocumentInfoTreeNode extends TreeNode{
    
    private final DocInfo _info;
    
    public DocInfo getDocInfo (){
        return _info;
    }
    
    private DocumentInfoTreeNode(TreeNode parent, DocInfo info) {
        super(parent);
        _info = info;
        setId(info.getId());
        setExpanded(true);
    }
    
    public static DocumentInfoTreeNode create (TreeNode parent, DocInfo info, TreeNodeObserver observer) {
        DocumentInfoTreeNode node = new DocumentInfoTreeNode(parent, info);
        node.registerObserver(observer);
        return node;
    }
}
