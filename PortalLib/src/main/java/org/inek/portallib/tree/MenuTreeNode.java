/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.portallib.tree;

/**
 *
 * @author muellermi
 */
public class MenuTreeNode extends TreeNode {

    private static int _autoId = 0;
    private final int _level;
    private final String _text;

    public String getText() {
        return _text;
    }

    public String getUrl() {
        return _url;
    }
    private final String _url;

    public int getLevel() {
        return _level;
    }

    private MenuTreeNode() {
        this(null, "", "");
    }

    private MenuTreeNode(MenuTreeNode parent, String text, String url) {
        super(parent);
        setId(_autoId++);
        _text = text;
        _url = url;
        if (parent == null) {
            _level = 0;
        } else {
            _observer = parent._observer;
            _level = parent.getLevel() + 1;
            parent.getChildren().add(this);
        }
    }

    public static MenuTreeNode createRoot(TreeNodeObserver observer) {
        MenuTreeNode node = new MenuTreeNode();
        node.expand();
        node.registerObserver(observer);
        return node;
    }

    public static MenuTreeNode create(MenuTreeNode parent, String text, String url) {
        MenuTreeNode node = new MenuTreeNode(parent, text, url);
        return node;
    }

}
