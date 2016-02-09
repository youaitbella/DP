/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.model;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.inek.portallib.tree.MenuTreeNode;

/**
 *
 * @author muellermi
 */
@SessionScoped
@Named
public class MenuModel implements Serializable{
    private MenuTreeNode _menuRoot;

    public MenuTreeNode getMenuRoot() {
        return _menuRoot;
    }
    
    @PostConstruct
    private void Init(){
        _menuRoot = MenuTreeNode.createRoot("", "");
        MenuTreeNode node1 = _menuRoot.addChild("Datenbasis", "/views/Participation.xhtml");
        node1.addChild("Beteiligung an der Datenübermittlung", "/views/Participation1.xhtml");
        node1.addChild("Datenqualität", "/views/Participation2.xhtml");
        node1.addChild("Unspezifische Kodierung", "/views/Participation3.xhtml");
        
        node1 = _menuRoot.addChild("KH-Strukturdaten", "/views/Participation4.xhtml");
        node1.addChild("Größenklassen (Bettenzahl) / Bundesland", "/views/Participation5.xhtml");
        
        node1 = _menuRoot.addChild("G-DRG-System", "/views/Participation6.xhtml");
        MenuTreeNode node2 = node1.addChild("Hauptabteilungen", "/views/Participation7.xhtml");
        node2.addChild("wenig komplexe Leistungen", "/views/Participation8.xhtml");
        node2.addChild("komplexe Leistungen", "/views/Participation9.xhtml");
        node2.addChild("häufige Leistungen", "/views/Participationa.xhtml");

        node1 = _menuRoot.addChild("Vollstationäre Falldaten", "/views/Participationb.xhtml");
        node2 = node1.addChild("Versorgung in den Hauptabteilungen", "/views/Participationc.xhtml");
        MenuTreeNode node3 = node2.addChild("Demographische und medizinische Merkmale", "/views/Participationd.xhtml");
        MenuTreeNode node4 = node3.addChild("nach Hauptdiagnosen", "/views/Participatione.xhtml");
        node4.addChild("HD Kapitel", "/views/Participationf.xhtml");
        node4.addChild("HD Gruppe", "/views/Participationg.xhtml");
    }
   
}
