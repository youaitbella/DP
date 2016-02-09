/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.controller;

import com.inek.begleitforschung.model.MenuModel;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.portallib.tree.MenuTreeNode;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class MainMenu {

    @Inject private MenuModel _menuModel;

    public MenuTreeNode getMenuRoot() {
        return _menuModel.getMenuRoot();
    }

    public String callMenu(String url) {
        return url;
    }


    public String toggle(MenuTreeNode node) {
        node.toggle();
        return "";
    }

}
