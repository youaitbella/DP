/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inek.begleitforschung;

import com.inek.begleitforschung.entities.BegleitEntity;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

/**
 *
 * @author vohldo
 */
@Named
@RequestScoped
public class MenuController {
    
    public List<MenuItem> getDataYears() {
        List<MenuItem> dataYears = new ArrayList<>();
        String path = BegleitEntity.BASE_FILE_PATH;
        String[] directories;
        directories = new File(path).list((File current, String name) -> new File(current, name).isDirectory()); 
        int i = 0;
        for(String dir : directories) {
            try {
                Integer.parseInt(dir);
                MenuItem item = new MenuItem();
                item.setVisible(true);
                item.setName(dir);
                if(i == directories.length-1)
                    item.setActive(true);
                else
                    item.setActive(false);
                createSubMenus(item);
                dataYears.add(item);
            } catch(NumberFormatException ex) {
            }
            i++;
        }
        return dataYears;
    }
    
    private void createSubMenus(MenuItem masterYearItem) {
        MenuItem database = new MenuItem("Datenbasis", "");
        MenuItem databaseTransfer = new MenuItem("Beteiligung Datenübermittlung", Pages.Participation.URL());
        MenuItem databaseQuality = new MenuItem("Datenqualität", "");
        MenuItem databaseUnspecificCoding = new MenuItem("Unspezifische Kodierung", "");
        List<MenuItem> subItemsDatabase = new ArrayList<>();
        subItemsDatabase.add(databaseTransfer);
        subItemsDatabase.add(databaseQuality);
        subItemsDatabase.add(databaseUnspecificCoding);
        database.setSubItems(subItemsDatabase);
        
        MenuItem structureData = new MenuItem("KH - Strukturdaten", "");
        MenuItem structureDataState = new MenuItem("Größenklasse (Betten) / Bundesland", "");
        MenuItem structureDataOwnership = new MenuItem("Größenklasse (Betten) / Trägerschaft", "");
        MenuItem structureDataCMI = new MenuItem("CMI-Klassen / Größe (Betten)", "");
        List<MenuItem> subItemsStructure = new ArrayList<>();
        subItemsStructure.add(structureDataState);
        subItemsStructure.add(structureDataOwnership);
        subItemsStructure.add(structureDataCMI);
        structureData.setSubItems(subItemsStructure);
        
        MenuItem system = new MenuItem("G-DRG-System", "");
        MenuItem systemPrimar = new MenuItem("Hauptabteilungen", "");
        MenuItem systemSupport = new MenuItem("belegärztliche Versorgung", "");
        
        List<MenuItem> yearSubs = new ArrayList<>();
        yearSubs.add(database);
        yearSubs.add(structureData);
        masterYearItem.setSubItems(yearSubs);
    }
    
    public void clickedMenuItem(MenuItem item) {
        for(MenuItem it : getDataYears())
            collapseMenuItems(it);
        activateMenuItem(item);
    }
    
    private void collapseMenuItems(MenuItem item) {
        for(MenuItem it : item.getSubItems()) {
            it.setActive(true);
            collapseMenuItems(it);
        }
    }
    
    private void activateMenuItem(MenuItem item) {
//        item.setActive(true);
//        for(MenuItem it : item.getSubItems()) {
//            it.setVisible(true);
//        }
    }
}
