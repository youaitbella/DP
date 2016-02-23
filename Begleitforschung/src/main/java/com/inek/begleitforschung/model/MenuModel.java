/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung.model;

import java.io.Serializable;
import javax.faces.context.FacesContext;
import org.inek.portallib.tree.MenuTreeNode;

/**
 *
 * @author muellermi
 */
public class MenuModel implements Serializable{
    private MenuTreeNode _menuRoot;
    private int _dataYear;
    
    public MenuModel(int year) {
        _dataYear = year;
        _menuRoot = MenuTreeNode.createRoot(_dataYear+"", "");
        String baseUrl = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        MenuTreeNode node1 = _menuRoot.addChild("Datenbasis", "");
        node1.addChild("Beteiligung an der Datenübermittlung", baseUrl+"/views/Participation.xhtml?dataYear=" + _dataYear);
        node1.addChild("Datenqualität", "link");
        node1.addChild("Unspezifische Kodierung", "link");
        
        node1 = _menuRoot.addChild("KH-Strukturdaten", "");
        node1.addChild("Größenklassen (Bettenzahl) / Bundesland", "link");
        node1.addChild("Größenklasse (Fälle) / Trägerschaft", "link");
        node1.addChild("CMI-Klassen / Größen (Betten)", "/views/Participation5.xhtml");
        
        node1 = _menuRoot.addChild("G-DRG-System", "");
        MenuTreeNode node2 = node1.addChild("Hauptabteilungen", "");
        node2.addChild("20 niedrigst bewertete Fallgruppen", "/views/Participation8.xhtml");
        node2.addChild("20 höchst bewertete Fallgruppen", "/views/Participation9.xhtml");
        node2.addChild("20 häufigste Fallgruppen", "/views/Participationa.xhtml");
        MenuTreeNode node3 = node1.addChild("belegärztliche Versorgung", "");
        node3.addChild("20 niedrigst bewertete Fallgruppen", "/views/Participation8.xhtml");
        node3.addChild("20 höchst bewertete Fallgruppen", "/views/Participation9.xhtml");
        node3.addChild("20 häufigste Fallgruppen", "/views/Participationa.xhtml");

        node1 = _menuRoot.addChild("Vollstationäre Falldaten", "");
        node2 = node1.addChild("Versorgung in den Hauptabteilungen", "");
        node3 = node2.addChild("Demographische und medizinische Merkmale", "");
        node3.addChild("nach MDC und Geschlecht", baseUrl+"/views/CharacteristicsMdcGender.xhtml?dataYear="+_dataYear);
        node3.addChild("nach Altersklassen und Geschlecht", baseUrl+"/views/AgeGender.xhtml?dataYear="+_dataYear);
        node3.addChild("nach DRG", baseUrl+"/views/Drg.xhtml?dataYear="+_dataYear);
        MenuTreeNode node4 = node3.addChild("nach Hauptdiagnosen (Alterskl.)", "");
        node4.addChild("Kapitel", "/views/Participationf.xhtml");
        node4.addChild("Gruppe", "/views/Participationg.xhtml");
        node4.addChild("Kategorie", "link");
        node4 = node3.addChild("nach Prozeduren (Alterskl.)", "link");
        node4.addChild("Kapitel", "link");
        node4.addChild("Bereich", "link");
        node4.addChild("4-Steller", "link");
        node3 = node2.addChild("Versorgung im Krankenhaus", "");
        node3.addChild("Fallzahl, VWD, CMI nach Größe (Betten) / Bundesland", baseUrl+"/views/MagnitudeBedState.xhtml?dataYear="+_dataYear);
        node3.addChild("Aufnahmeanlass und Entlassungs-/ Verlegungsgrund", "link");
        node3.addChild("Häufigkeit von Operationen", "link");
        
        node2 = node1.addChild("Belegärztliche Versorgung", "");
        node3 = node2.addChild("Demographische und medizinische Merkmale", "");
        node3.addChild("nach MDC und Geschlecht", baseUrl+"/views/SlipMcCharacteristicsMdcGender.xhtml?dataYear="+_dataYear);
        node3.addChild("nach Altersklassen und Geschlecht", baseUrl+"/views/SlipMcAgeGender.xhtml?dataYear="+_dataYear);
        node3.addChild("nach DRG", baseUrl+"/views/SlipMcDrg.xhtml?dataYear="+_dataYear);
        
        node4 = node3.addChild("nach Hauptdiagnosen (Alterskl.)", "");
        node4.addChild("Kapitel", "link");
        node4.addChild("Gruppe", "link");
        node4.addChild("Kategorie", "link");
        
        node4 = node3.addChild("nach Prozeduren (Alterskl.)", "");
        node4.addChild("Kapitel", "link");
        node4.addChild("Bereich", "link");
        node4.addChild("4-Steller", "link");
        
        node3 = node2.addChild("Versorgung im Krankenhaus", "");
        node3.addChild("Fallzahl, VWD, CMI nach Größe (Betten) / Bundesland", "link");
        node3.addChild("Aufnahmeanlass und Entlassungs-/ Verlegungsgrund", "link");
        node3.addChild("Häufigkeit von Operationen", "link");
        
        node1 = _menuRoot.addChild("Teilstationäre Fälle", "");
        node2 = node1.addChild("Hauptdiagnosen", "");
        node2.addChild("Kapitel", "link");
        node2.addChild("Gruppe", "link");
        node2.addChild("Kategorie", "link");
        
        node2 = node1.addChild("Prozeduren", "");
        node2.addChild("Kapitel", "link");
        node2.addChild("Bereich", "link");
    }
    
    public MenuTreeNode getMenuRoot() {
        return _menuRoot;
    }

    public int getDataYear() {
        return _dataYear;
    }
}
