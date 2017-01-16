/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.portallib.tree;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * TreeNode and its descendents are used to encapsulate the tree status as well
 * as to wrap the user object inside.
 *
 * @author muellermi
 */
public abstract class TreeNode implements Serializable{

    private final TreeNode _parent;

    protected TreeNode(TreeNode parent) {
        _parent = parent;
    }

    public TreeNode getParent() {
        return _parent;
    }

    // <editor-fold defaultstate="collapsed" desc="Property Children">    
    private final Collection<TreeNode> _children = new ArrayList<>();

    public Collection<TreeNode> getChildren() {
        return _children;
    }
    
    public Collection<TreeNode> copyChildren() {
        return new CopyOnWriteArrayList<>(getChildren());
    }
    
    public Collection<TreeNode> getSortedChildren() {
        if (_observer != null) {
            return _observer.obtainSortedChildren(this, getChildren());
        }
        return _children;
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
    private boolean _isChecked = false;

    public boolean isChecked() {
        return _isChecked;
    }

    public void setChecked(boolean isChecked) {
        _isChecked = isChecked;
    }

    // </editor-fold>  
    
    // <editor-fold defaultstate="collapsed" desc="Property SortCriteria + state">    
    private String _sortCriteria = "";
    private boolean _isDescending = false;

    public boolean isDescending() {
        return _isDescending;
    }

    public void setDescending(boolean isDescending) {
        _isDescending = isDescending;
    }

    public void setSortCriteria(String sortCriteria) {
        if (_sortCriteria.equals(sortCriteria)) {
            _isDescending = !_isDescending;
        } else {
            _isDescending = false;
        }
        _sortCriteria = sortCriteria == null ? "" : sortCriteria;
    }
    public String getSortCriteria(){
        return _sortCriteria;
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
        for (TreeNode child : copyChildren()) {
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
            return;
        }
        if (getClass() == clazz) {
            _isChecked = value;
        }
        for (TreeNode child : _children) {
            child.selectAll(clazz, value);
        }
    }

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TreeNode other = (TreeNode) obj;
        return _id == other._id;
    }
    
}
