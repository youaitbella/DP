/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.model;

import java.io.Serializable;
import java.util.Collection;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.inek.portallib.tree.MenuTreeNode;
import org.inek.portallib.tree.TreeNode;
import org.inek.portallib.tree.TreeNodeObserver;

/**
 *
 * @author muellermi
 */
@SessionScoped
@Named
public class MenuModel implements Serializable, TreeNodeObserver{
    private MenuTreeNode _menuRoot;

    public MenuTreeNode getMenuRoot() {
        return _menuRoot;
    }
    
    @PostConstruct
    private void Init(){
        _menuRoot = MenuTreeNode.createRoot(null);
        _menuRoot.expand();
        MenuTreeNode node = MenuTreeNode.create(_menuRoot, "Datenbasis", "database.xhtml");
        MenuTreeNode.create(node, "Beteiligung an der Datenübermittlung", "d1.xhtml");
        MenuTreeNode.create(node, "Datenqualität", "d1.xhtml");
        MenuTreeNode.create(node, "Unspezifische Kodierung", "d1.xhtml");
        node = MenuTreeNode.create(_menuRoot, "KH-Strukturdaten", "kh.xhtml");
        MenuTreeNode.create(node, "Größenklassen (Bettenzahl) / Bundesland", "kh1.xhtml");
        node = MenuTreeNode.create(_menuRoot, "G-DRG-System", "kh.xhtml");
        MenuTreeNode subNode = MenuTreeNode.create(node, "Hauptabteilungen", "kh1.xhtml");
        MenuTreeNode.create(subNode, "wenig komplexe Leistungen", "kh1.xhtml");
    }

    @Override
    public void obtainChildren(TreeNode treeNode, Collection<TreeNode> children) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<TreeNode> obtainSortedChildren(TreeNode treeNode, Collection<TreeNode> children) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
