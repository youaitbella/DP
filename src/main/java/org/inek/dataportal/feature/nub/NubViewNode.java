/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.nub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author muellermi
 */
public class NubViewNode extends TreeNode {

    private List<TreeNode> _children = new ArrayList<>();

    @Override
    protected void expandNode(){
        for (TreeNodeObserver observer : _observers){
            observer.obtainChildren(this, _children);
        }
        
    }

    public void updateChildrenIfIsExpanded() {
        if (isExpanded()){}
    }

    @Override
    public Collection<TreeNode> getChildren() {
        return _children;
    }


}
