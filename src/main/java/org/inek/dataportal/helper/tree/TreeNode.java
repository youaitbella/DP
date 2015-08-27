/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TreeNode and its descendents are used to encapsulate the tree status as well
 * as to wrap the user object inside.
 *
 * @author muellermi
 */
public abstract class TreeNode {

    // <editor-fold defaultstate="collapsed" desc="Property Children">    
    private final Collection<TreeNode> _children = new ArrayList<>();
    private final TreeNode _parent;

    protected TreeNode(TreeNode parent) {
        _parent = parent;
    }

    public TreeNode getParent() {
        return _parent;
    }

    public Collection<TreeNode> getChildren() {
        return _children;
    }
    // </editor-fold>    

    // <editor-fold defaultstate="collapsed" desc="Property Expanded">    
    private boolean _isExpanded;

    public boolean isExpanded() {
        return _isExpanded;
    }

    public void setExpanded(boolean expand) {
        if (expand) {
            expand();
        } else {
            collapse();
        }
    }

    // </editor-fold>   
    // <editor-fold defaultstate="collapsed" desc="Property Checked">    
    private boolean _isChecked;

    public boolean isChecked() {
        return _isChecked;
    }

    public void setChecked(boolean isChecked) {
        _isChecked = isChecked;
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

    public void toggle() {
        if (_isExpanded) {
            collapse();
        } else {
            expand();
        }
    }

    public void expand() {
        expandNode();
        _isExpanded = true;
    }

    protected void expandNode() {
        if (_observer != null) {
            _observer.obtainChildren(this, getChildren());
        }
    }

    public void collapse() {
        collapseNode();
        _isExpanded = false;
    }

    protected void collapseNode() {
        for (TreeNode child : _children) {
            child.collapse();
        }
        _children.clear();
    }

    public void refresh() {
        if (!_isExpanded) {
            return;
        }
        if (_observer != null) {
            _observer.obtainChildren(this, getChildren());
        }
        for (TreeNode child : _children) {
            child.refresh();
        }
    }

    protected TreeNodeObserver _observer;

    public void registerObserver(TreeNodeObserver observer) {
        _observer = observer;
    }

    public void removeObserver() {
        _observer = null;
    }

    public List<TreeNode> getSelectedNodes() {
        List<TreeNode> selectedNodes = new ArrayList<>();
        if (!_isExpanded) {
            return selectedNodes;
        }
        if (_isChecked) {
            selectedNodes.add(this);
        }
        for (TreeNode child : _children) {
            selectedNodes.addAll(child.getSelectedNodes());
        }
        return selectedNodes;
    }

    public List<Integer> getSelectedIds(Class<? extends TreeNode> clazz) {
        List<Integer> selectedIds = new ArrayList<>();
        if (!_isExpanded) {
            return selectedIds;
        }
        if (_isChecked && getClass() == clazz) {
            selectedIds.add(_id);
        }
        for (TreeNode child : _children) {
            selectedIds.addAll(child.getSelectedIds(clazz));
        }
        return selectedIds;
    }

    public void selectAll(Class<? extends TreeNode> clazz, boolean value) {
        if (!_isExpanded) {
            return ;
        }
        if (getClass() == clazz) {
            _isChecked = value;
        }
        for (TreeNode child : _children) {
            child.selectAll(clazz, value);
        }
    }
}
