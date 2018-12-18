package org.inek.dataportal.drg.specificfunction.backingbean.tree;

import org.inek.dataportal.common.tree.TreeNode;
import org.inek.dataportal.common.tree.TreeNodeObserver;
import org.inek.dataportal.drg.specificfunction.entity.SpecificFunctionRequest;

public final class SpecificFunctionRequestTreeNode extends TreeNode{
    
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
