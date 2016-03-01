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
    private final MenuTreeNode _menuRoot;
    private final int _dataYear;
    
    public MenuModel(int year) {
        _dataYear = year;
        _menuRoot = MenuTreeNode.createRoot(_dataYear+"", "");
        String baseUrl = FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath();
        MenuTreeNode node1 = _menuRoot.addChild("Datenbasis", "");
        node1.addChild("Beteiligung an der Datenübermittlung", baseUrl+"/views/Participation.xhtml?dataYear=" + _dataYear);
        node1.addChild("Datenqualität", baseUrl+"/views/DataQuality.xhtml?dataYear="+_dataYear);
        node1.addChild("Unspezifische Kodierung", baseUrl+"/views/UnspecificCoding.xhtml?dataYear="+_dataYear);
        
        node1 = _menuRoot.addChild("KH-Strukturdaten", "");
        node1.addChild("Größenklassen (Bettenzahl) / Bundesland", baseUrl+"/views/structural/SizeByState.xhtml?dataYear=" + _dataYear);
        node1.addChild("Größenklasse (Fälle) / Trägerschaft", baseUrl+"/views/structural/SizeByResponsible.xhtml?dataYear=" + _dataYear);
        node1.addChild("CMI-Klassen / Größen (Betten)", baseUrl+"/views/structural/CmiByBedClass.xhtml?dataYear=" + _dataYear);
        
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
        node4.addChild("Kapitel", baseUrl+"/views/PrimaryDiagsProcsInpatientPdChapter.xhtml?dataYear="+_dataYear);
        node4.addChild("Gruppe", baseUrl+"/views/PrimaryDiagsProcsInpatientPdGroup.xhtml?dataYear="+_dataYear);
        node4.addChild("Kategorie", baseUrl+"/views/PrimaryDiagsProcsInpatientPdCat.xhtml?dataYear="+_dataYear);
        node4 = node3.addChild("nach Prozeduren (Alterskl.)", "");
        node4.addChild("Kapitel", baseUrl+"/views/PrimaryDiagsProcsInpatientProcChapter.xhtml?dataYear="+_dataYear);
        node4.addChild("Bereich", baseUrl+"/views/PrimaryDiagsProcsInpatientProcArea.xhtml?dataYear="+_dataYear);
        node4.addChild("4-Steller", baseUrl+"/views/PrimaryDiagsProcsInpatientProcCode.xhtml?dataYear="+_dataYear);
        node3 = node2.addChild("Versorgung im Krankenhaus", "");
        node3.addChild("Fallzahl, VWD, CMI nach Größe (Betten) / Bundesland", baseUrl+"/views/InfoByHospitalSize.xhtml?dataYear="+_dataYear + "&type=1");
        node3.addChild("Aufnahmeanlass und Entlassungs-/ Verlegungsgrund", baseUrl+"/views/CasesPrimaryDepartment.xhtml?dataYear="+_dataYear);
        node3.addChild("Häufigkeit von Operationen", baseUrl+"/views/NumOperationsPrimary.xhtml?dataYear="+_dataYear);
        
        node2 = node1.addChild("Belegärztliche Versorgung", "");
        node3 = node2.addChild("Demographische und medizinische Merkmale", "");
        node3.addChild("nach MDC und Geschlecht", baseUrl+"/views/SlipMcCharacteristicsMdcGender.xhtml?dataYear="+_dataYear);
        node3.addChild("nach Altersklassen und Geschlecht", baseUrl+"/views/SlipMcAgeGender.xhtml?dataYear="+_dataYear);
        node3.addChild("nach DRG", baseUrl+"/views/SlipMcDrg.xhtml?dataYear="+_dataYear);
        
        node4 = node3.addChild("nach Hauptdiagnosen (Alterskl.)", "");
        node4.addChild("Kapitel", baseUrl+"/views/PrimaryDiagsProcsSlipMcPdChapter.xhtml?dataYear="+_dataYear);
        node4.addChild("Gruppe", baseUrl+"/views/PrimaryDiagsProcsSlipMcPdGroup.xhtml?dataYear="+_dataYear);
        node4.addChild("Kategorie", baseUrl+"/views/PrimaryDiagsProcsSlipMcPdCat.xhtml?dataYear="+_dataYear);
        
        node4 = node3.addChild("nach Prozeduren (Alterskl.)", "");
        node4.addChild("Kapitel", baseUrl+"/views/PrimaryDiagsProcsSlipMcProcChapter.xhtml?dataYear="+_dataYear);
        node4.addChild("Bereich", baseUrl+"/views/PrimaryDiagsProcsSlipMcProcArea.xhtml?dataYear="+_dataYear);
        node4.addChild("4-Steller", baseUrl+"/views/PrimaryDiagsProcsSlipMcProcCode.xhtml?dataYear="+_dataYear);
        
        node3 = node2.addChild("Versorgung im Krankenhaus", "");
        node3.addChild("Fallzahl, VWD, CMI nach Größe (Betten) / Bundesland", baseUrl+"/views/InfoByHospitalSize.xhtml?dataYear="+_dataYear + "&type=2");
        node3.addChild("Aufnahmeanlass und Entlassungs-/ Verlegungsgrund", baseUrl+"/views/CasesSlipMc.xhtml?dataYear="+_dataYear);
        node3.addChild("Häufigkeit von Operationen", baseUrl+"/views/NumOperationsSlipMc.xhtml?dataYear="+_dataYear);
        
        node1 = _menuRoot.addChild("Teilstationäre Fälle", "");
        node2 = node1.addChild("Hauptdiagnosen", "");
        node2.addChild("Kapitel", baseUrl+"/views/PartialInpatientPdChapter.xhtml?dataYear="+_dataYear);
        node2.addChild("Gruppe", baseUrl+"/views/PartialInpatientPdGroup.xhtml?dataYear="+_dataYear);
        node2.addChild("Kategorie", baseUrl+"/views/PartialInpatientPdCat.xhtml?dataYear="+_dataYear);
        
        node2 = node1.addChild("Prozeduren", "");
        node2.addChild("Kapitel", baseUrl+"/views/PartialInpatientProcChapter.xhtml?dataYear="+_dataYear);
        node2.addChild("Bereich", baseUrl+"/views/PartialInpatientProcArea.xhtml?dataYear="+_dataYear);
    }
    
    public MenuTreeNode getMenuRoot() {
        return _menuRoot;
    }

    public int getDataYear() {
        return _dataYear;
    }
}
