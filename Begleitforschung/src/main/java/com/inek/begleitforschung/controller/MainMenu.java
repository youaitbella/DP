/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.controller;

import com.inek.begleitforschung.entities.ApplicationData;
import com.inek.begleitforschung.model.MenuModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.inek.portallib.tree.MenuTreeNode;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class MainMenu {
    
    private final static String DATA_YEAR_DIR = ApplicationData.BASE_PATH;

    public String callMenu(String url) {
        return url;
    }


    public String toggle(MenuTreeNode node) {
        node.toggle();
        return "";
    }
    
    public String buildMenu() {
        String menuHtml = "";
        for(String dataYear : getDataYearDirs()) {
            MenuTreeNode root = new MenuModel(Integer.parseInt(dataYear)).getMenuRoot();
            menuHtml += "<ul class=\"nav \"><li><label label-default=\"\" class=\"tree-toggler nav-header bf-menu-item\">"+root.getText()+"&nbsp;<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span></label>";
            menuHtml += "<ul class=\"nav tree active-trial\">";
            for(MenuTreeNode node1 : root.getChildren()) {
                if(node1.getChildren().isEmpty()) {
                    menuHtml += "<li><a class=\"bf-menu-item bf-menu-item-2\" href=\""+node1.getUrl()+"\">"+node1.getText()+"</a>";
                } else {
                    menuHtml += "<li><label label-default=\"\" class=\"tree-toggler nav-header bf-menu-item bf-menu-item-2\">"+node1.getText()+"&nbsp;<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span></label>";
                    menuHtml += "<ul class=\"nav tree active-trial\">";
                    for(MenuTreeNode node2 : node1.getChildren()) {
                        if(node2.getChildren().isEmpty()) {
                            menuHtml += "<li><a class=\"bf-menu-item bf-menu-item-3\" href=\""+node2.getUrl()+"\">"+node2.getText()+"</a>";
                        } else {
                            menuHtml += "<li><label label-default=\"\" class=\"tree-toggler nav-header bf-menu-item bf-menu-item-3\">"+node2.getText()+"&nbsp;<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span></label>";
                            menuHtml += "<ul class=\"nav tree active-trial\">";
                            for(MenuTreeNode node3 : node2.getChildren()) {
                                if(node3.getChildren().isEmpty()) {
                                    menuHtml += "<li><a class=\"bf-menu-item bf-menu-item-4\" href=\""+node3.getUrl()+"\">"+node3.getText()+"</a>";
                                } else {
                                    menuHtml += "<li><label label-default=\"\" class=\"tree-toggler nav-header bf-menu-item bf-menu-item-4\">"+node3.getText()+"&nbsp;<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span></label>";
                                    menuHtml += "<ul class=\"nav tree active-trial\">";
                                    for(MenuTreeNode node4 : node3.getChildren()) {
                                        if(node4.getChildren().isEmpty()) {
                                            menuHtml += "<li><a class=\"bf-menu-item bf-menu-item-5\" href=\""+node4.getUrl()+"\">"+node4.getText()+"</a>";
                                        } else {
                                            menuHtml += "<li><label label-default=\"\" class=\"tree-toggler nav-header bf-menu-item bf-menu-item-5\">"+node4.getText()+"&nbsp;<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span></label>";
                                            menuHtml += "<ul class=\"nav tree active-trial\">";
                                            for(MenuTreeNode node5 : node4.getChildren()) {
                                                if(node5.getChildren().isEmpty()) {
                                                    menuHtml += "<li><a class=\"bf-menu-item bf-menu-item-6\" href=\""+node5.getUrl()+"\">"+node5.getText()+"</a>";
                                                } else{
                                                    menuHtml += "<li><label label-default=\"\" class=\"tree-toggler nav-header bf-menu-item bf-menu-item-6\">"+node5.getText()+"&nbsp;<span class=\"glyphicon glyphicon-menu-down\" aria-hidden=\"true\"></span></label>";
                                                    menuHtml += "<ul class=\"nav tree active-trial\">";
                                                    menuHtml += "</ul>";
                                                    menuHtml += "</li>";
                                                }
                                            }
                                            menuHtml += "</ul>";
                                            menuHtml += "</li>";
                                        }
                                    }
                                    menuHtml += "</ul>";
                                    menuHtml += "</li>";
                                }
                            }
                            menuHtml += "</ul>";
                            menuHtml += "</li>";
                        }
                    }
                    menuHtml += "</ul>";
                    menuHtml += "</li>";
                }
            }
            menuHtml += "</ul>";
            menuHtml += "</li>";
            menuHtml += "</ul>";
        }
        return menuHtml;
    }
    
    private List<String> getDataYearDirs() {
        String[] dirs = new File(DATA_YEAR_DIR).list();
        List<String> dataYears = new ArrayList<>();
        for(String dir : dirs) {
            try {
                Integer.parseInt(dir);
                dataYears.add(dir);
            } catch(Exception ex) {
                continue;
            }
        }
        Collections.reverse(dataYears);
        return dataYears;
    }
    
    public String getInfoByHospitalTitle(int type, int dataYear) {
        if(type == 1) {
            return "C-1-2-1: Vollstationäre Versorgung in Hauptabteilungen nach Größenklassen (Bettenzahl)/Bundesland, Datenjahr " + dataYear;
        } else {
            return "C-2-2-1: Vollstationäre Versorgung in belegärztl. Versorgung nach Größenklassen (Bettenzahl)/Bundesland, Datenjahr " + dataYear;
        }
    }
}
