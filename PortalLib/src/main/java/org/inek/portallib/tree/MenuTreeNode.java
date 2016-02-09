/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.portallib.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 *
 * @author muellermi
 */
public class MenuTreeNode {

    // <editor-fold defaultstate="collapsed" desc="Property Level">    
    private final int _level;

    public int getLevel() {
        return _level;
    }
    // </editor-fold>   

    // <editor-fold defaultstate="collapsed" desc="Property Text">    
    private final String _text;

    public String getText() {
        return _text;
    }
    // </editor-fold>   

    // <editor-fold defaultstate="collapsed" desc="Property Url">    
    private final String _url;

    public String getUrl() {
        return _url;
    }
    // </editor-fold>   

    // <editor-fold defaultstate="collapsed" desc="Property Children">    
    private final Collection<MenuTreeNode> _children = new ArrayList<>();

    public Collection<MenuTreeNode> getChildren() {
        return _children;
    }
    // </editor-fold>   

    // <editor-fold defaultstate="collapsed" desc="Property Expanded">    
    private boolean _isExpanded;

    public boolean isExpanded() {
        return _isExpanded;
    }
    // </editor-fold>   

    private MenuTreeNode(int level, String text, String url) {
        _isExpanded = level == 0;
        _level = level;
        _text = text;
        _url = url;
    }

    public static MenuTreeNode createRoot(String text, String url) {
        MenuTreeNode node = new MenuTreeNode(0, text, url);
        return node;
    }

//    public static MenuTreeNode createCopy(MenuTreeNode node) {
//        MenuTreeNode copy = new MenuTreeNode(node._level, node._text, node._url);
//        copy._isExpanded = node._isExpanded;
//        for (MenuTreeNode child : node._children) {
//            copy._children.add(child);
//        }
//        
//        return copy;
//    }

    public MenuTreeNode addChild(String text, String url) {
        MenuTreeNode node = new MenuTreeNode(_level + 1, text, url);
        _children.add(node);
        return node;
    }

    public void toggle() {
        if (_isExpanded) {
            collapse();
        } else {
            expand();
        }
    }

    public void expand() {
        _isExpanded = true;
    }

    public void collapse() {
        for (MenuTreeNode node : _children) {
            node.collapse();
        }
        _isExpanded = false;
    }

    public boolean hasChildrenToShow() {
        return _isExpanded && _children.size() > 0;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + this._level;
        hash = 71 * hash + Objects.hashCode(this._url);
        return hash;
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
        final MenuTreeNode other = (MenuTreeNode) obj;
        if (this._level != other._level) {
            return false;
        }
        if (!Objects.equals(this._url, other._url)) {
            return false;
        }
        return true;
    }

}
