/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.nub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class TreeNode {

    // <editor-fold defaultstate="collapsed" desc="Property Expanded">    
    private boolean _isExpanded;

    public boolean isExpanded() {
        return _isExpanded;
    }

    public void setExpanded(boolean expand) {
        if (expand){
            expand();
        }else{
            collapse();
        }
    }
    
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Property Id">    
    private int _id;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>    

    public void expand() {
        expandNode();
        _isExpanded = true;
    }
    
    public void collapse() {
        collapseNode();
        _isExpanded = false;
    }

    protected final List<TreeNodeObserver> _observers = new ArrayList<>();
    public void addObserver (TreeNodeObserver observer){
        if (!_observers.contains(observer)){
            _observers.add(observer);
        }
    }
    public void removeObserver (TreeNodeObserver observer){
        if (_observers.contains(observer)){
            _observers.remove(observer);
        }
    }

    protected void expandNode(){}
    protected void collapseNode(){}

    public abstract Collection<TreeNode> getChildren();


}
