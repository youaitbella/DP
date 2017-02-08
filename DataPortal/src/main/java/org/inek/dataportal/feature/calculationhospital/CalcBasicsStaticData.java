/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import org.inek.dataportal.helper.Utils;

/**
 * This class is instantiated at application level.
 * Thus, it acts as a kind of Singleton.
 * It provides the form with data which is independend from a concrete session,
 * e.g. list items
 * 
 * @author muellermi
 */
@Named
@ApplicationScoped
public class CalcBasicsStaticData {
       // todo (low priority): get texts from property file or database

    public List<SelectItem> getNeonatFulfillmentItems(){
       List<SelectItem> items = new ArrayList<>();
       items.add(new SelectItem(0, "nicht erfüllt"));
       items.add(new SelectItem(13, "ganzjährig"));
       items.add(new SelectItem(1, "ab Januar"));
       items.add(new SelectItem(2, "ab Februar"));
       items.add(new SelectItem(3, "ab März"));
       items.add(new SelectItem(4, "ab April"));
       items.add(new SelectItem(5, "ab Mai"));
       items.add(new SelectItem(6, "ab Juni"));
       items.add(new SelectItem(7, "ab Juli"));
       items.add(new SelectItem(8, "ab August"));
       items.add(new SelectItem(9, "ab September"));
       items.add(new SelectItem(10, "ab Oktober"));
       items.add(new SelectItem(11, "ab November"));
       items.add(new SelectItem(12, "ab Dezember"));
       return items;
   } 

    public List<SelectItem> getNeonatLevelItems(){
       List<SelectItem> items = new ArrayList<>();
       items.add(new SelectItem(0, Utils.getMessage("lblNotAvailable")));
       items.add(new SelectItem(1, "Level 1: Versorgungsstufe I: Perinatalzentrum"));
       items.add(new SelectItem(2, "Level 2: Versorgungsstufe II: Perinatalzentrum"));
       items.add(new SelectItem(3, "Level 3: Versorgungsstufe III: Perinataler Schwerpunkt"));
       items.add(new SelectItem(4, "Level 4: Versorgungsstufe IV: Geburtsklinik"));
       return items;
   } 

    public List<SelectItem> getMviFulfillmentItems(){
       List<SelectItem> items = new ArrayList<>();
       items.add(new SelectItem(1, "Das Krankenhaus erfüllt in diesem Datenjahr uneingeschränkt die Anforderungen der Richtlinie für die Durchführung von minimalinvasiven Herzklappeninterventionen"));
       items.add(new SelectItem(2, "Das Krankenhaus erfüllt in diesem Datenjahr die Anforderungen gemäß der Übergangsregelung nach §9 der Richtlinie sowie nach dem Stichtag (30.Juni 2016) uneingeschränkt"));
       items.add(new SelectItem(3, "Das Krankenhaus erfüllt in diesem Datenjahr lediglich die Anforderungen gemäß der Übergangsregelung nach §9 der Richtlinie"));
       items.add(new SelectItem(0, "Das Krankenhaus erfüllt in diesem Datenjahr die Anforderungen der Richtlinie für die Durchführung von minimalinvasiven Herzklappeninterventionen nicht"));
       return items;
   } 

    public List<SelectItem> getInternalCostAllocationItems(){
       List<SelectItem> items = new ArrayList<>();
       items.add(new SelectItem(1, "Gleichungsverfahren"));
       items.add(new SelectItem(2, "Stufenleiterverfahren"));
       items.add(new SelectItem(3, "Anbauverfahren"));
       items.add(new SelectItem(4, "sonstige Vorgehensweise"));
       return items;
   } 

    public List<SelectItem> getExternalServiceProvisionItems(){
       List<SelectItem> items = new ArrayList<>();
       items.add(new SelectItem(1, "wird nicht erbracht"));
       items.add(new SelectItem(2, "Keine Fremdvergabe"));
       items.add(new SelectItem(3, "Vollständige Fremdvergabe"));
       items.add(new SelectItem(4, "Teilweise Fremdvergabe"));
       return items;
   } 
			
     public List<SelectItem> getTimeRecordingTypeItemsSNZ(){
       List<SelectItem> items = new ArrayList<>();
       items.add(new SelectItem(0, "nicht dokumentiert"));
       items.add(new SelectItem(1, "mit fallindividuellem Gleichzeitigkeitsfaktor"));
       items.add(new SelectItem(2, "mit standardisiertem Gleichzeitigkeitsfaktor je OP-Art"));
       items.add(new SelectItem(4, "Alternative (bitte beschreiben)"));
       return items;
   } 

    public List<SelectItem> getTimeRecordingTypeItemsRZ(){
       List<SelectItem> items = new ArrayList<>();
       items.add(new SelectItem(0, "nicht dokumentiert"));
       items.add(new SelectItem(1, "als fallindividuell erfasster Wert je Mitarbeiter(in)"));
       items.add(new SelectItem(2, "als abgestufter Standardwert je OP-Art"));
       items.add(new SelectItem(3, "als Einheitswert"));
       items.add(new SelectItem(4, "Alternative (bitte beschreiben)"));
       return items;
   } 
    
    public List<SelectItem> getCalculationTypeItems(){
       List<SelectItem> items = new ArrayList<>();
       items.add(new SelectItem(1, "vereinfachte Kalkulation"));
       items.add(new SelectItem(2, "Probekalkulation"));
       return items;
   } 

}
