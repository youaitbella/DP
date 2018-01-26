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
 * This class is instantiated at application level. Thus, it acts as a kind of
 * Singleton. It provides the form with data which is independend from a
 * concrete session, e.g. list items
 *
 * Because we cant't inject this class into an entity, all methods shall be
 * static. Since EL can not access static methods, we use instance methods
 * accessing their static companions.
 *
 * @author muellermi
 */
@Named
@ApplicationScoped
public class CalcBasicsStaticData {
    // todo (low priority): get texts from property file or database

    public List<SelectItem> getNeonatFulfillmentItems() {
        return staticGetNeonatFulfillmentItems();
    }

    public static List<SelectItem> staticGetNeonatFulfillmentItems() {
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

    public List<SelectItem> getNeonatLevelItems() {
        return staticGetNeonatLevelItems();
    }

    public static List<SelectItem> staticGetNeonatLevelItems() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(0, Utils.getMessage("lblNotAvailable")));
        items.add(new SelectItem(1, "Level 1: Versorgungsstufe I: Perinatalzentrum"));
        items.add(new SelectItem(2, "Level 2: Versorgungsstufe II: Perinatalzentrum"));
        items.add(new SelectItem(3, "Level 3: Versorgungsstufe III: Perinataler Schwerpunkt"));
        items.add(new SelectItem(4, "Level 4: Versorgungsstufe IV: Geburtsklinik"));
        return items;
    }

    public List<SelectItem> getMviFulfillmentItems() {
        return staticGetMviFulfillmentItems();
    }

    public static List<SelectItem> staticGetMviFulfillmentItems() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(1, "Das Krankenhaus erfüllt in diesem Datenjahr uneingeschränkt die Anforderungen "
                + "der Richtlinie für die Durchführung von minimalinvasiven Herzklappeninterventionen"));
        items.add(new SelectItem(2, "Das Krankenhaus erfüllt in diesem Datenjahr die Anforderungen gemäß der "
                + "Übergangsregelung nach §9 der Richtlinie sowie nach dem Stichtag (30.Juni 2016) uneingeschränkt"));
        items.add(new SelectItem(3, "Das Krankenhaus erfüllt in diesem Datenjahr lediglich die Anforderungen gemäß "
                + "der Übergangsregelung nach §9 der Richtlinie"));
        items.add(new SelectItem(0, "Das Krankenhaus erfüllt in diesem Datenjahr die Anforderungen der Richtlinie für "
                + "die Durchführung von minimalinvasiven Herzklappeninterventionen nicht"));
        return items;
    }
    
    public List<SelectItem> getProvidedTypeText() {
        return staticGetProvidedTypeText();
    }
    
    public static List<SelectItem> staticGetProvidedTypeText() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(1, "keine Fremdvergabe"));
        items.add(new SelectItem(2, "vollständige Fremdvergabe"));
        items.add(new SelectItem(3, "teilweise Fremdvergabe"));
        items.add(new SelectItem(0, "wird nicht erbracht"));
        return items;
    }
    

    public List<SelectItem> getInternalCostAllocationItems() {
        return staticGetInternalCostAllocationItems();
    }

    public List<SelectItem> staticGetInternalCostAllocationItems() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(1, "Gleichungsverfahren"));
        items.add(new SelectItem(2, "Stufenleiterverfahren"));
        items.add(new SelectItem(3, "Anbauverfahren"));
        items.add(new SelectItem(4, "sonstige Vorgehensweise"));
        return items;
    }

    public List<SelectItem> getExternalServiceProvisionItems() {
        return staticGetExternalServiceProvisionItems();
    }

    public static List<SelectItem> staticGetExternalServiceProvisionItems() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(1, "wird nicht erbracht"));
        items.add(new SelectItem(2, "Keine Fremdvergabe"));
        items.add(new SelectItem(3, "Vollständige Fremdvergabe"));
        items.add(new SelectItem(4, "Teilweise Fremdvergabe"));
        return items;
    }
    
    
    public List<SelectItem> getServiceKeyItems() {
        return staticGetServiceKeyItems();
    }

    public static List<SelectItem> staticGetServiceKeyItems() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(0, "DGVS"));
        items.add(new SelectItem(1, "Leistungszeit"));
        items.add(new SelectItem(2, "Punktesystem"));
        items.add(new SelectItem(3, "Sonstige"));
        return items;
    }
    

    public List<SelectItem> getTimeRecordingTypeItemsSNZ() {
        return staticGetTimeRecordingTypeItemsSNZ();
    }

    public static List<SelectItem> staticGetTimeRecordingTypeItemsSNZ() {
        List<SelectItem> items = new ArrayList<>();
        //items.add(new SelectItem(0, "nicht dokumentiert"));
        items.add(new SelectItem(1, "mit fallindividuellem Gleichzeitigkeitsfaktor"));
        items.add(new SelectItem(2, "mit standardisiertem Gleichzeitigkeitsfaktor je OP-Art"));
        items.add(new SelectItem(4, "Alternative (bitte beschreiben)"));
        return items;
    }

    public List<SelectItem> getTimeRecordingTypeItemsRZ() {
        return staticGetTimeRecordingTypeItemsRZ();
    }

    public static List<SelectItem> staticGetTimeRecordingTypeItemsRZ() {
        List<SelectItem> items = new ArrayList<>();
        //items.add(new SelectItem(0, "nicht dokumentiert"));
        items.add(new SelectItem(1, "als fallindividuell erfasster Wert je Mitarbeiter(in)"));
        items.add(new SelectItem(2, "als abgestufter Standardwert je OP-Art"));
        items.add(new SelectItem(3, "als Einheitswert"));
        items.add(new SelectItem(4, "Alternative (bitte beschreiben)"));
        return items;
    }

    public List<SelectItem> getCalculationTypeItems() {
        return staticGetCalculationTypeItems();
    }

    public static List<SelectItem> staticGetCalculationTypeItems() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(1, "vereinfachte Kalkulation"));
        items.add(new SelectItem(2, "Probekalkulation"));
        return items;
    }

    public List<SelectItem> getIbvlTypeItem() {
        return staticGetIbvlTypeItem();
    }

    public static List<SelectItem> staticGetIbvlTypeItem() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(1, "Gleichungsverfahren"));
        items.add(new SelectItem(2, "Stufenleiterverfahren"));
        items.add(new SelectItem(3, "Anbauverfahren*"));
        items.add(new SelectItem(0, "sonstige Vorgehensweise"));
        return items;
    }
    
    public List<SelectItem> getServiceProvivionItem() {
        return staticGetServiceProvivionItem();
    }

    public static List<SelectItem> staticGetServiceProvivionItem() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(1, "Elektrokrampftherapie (EKT) [OPS 8-630*]"));
        items.add(new SelectItem(2, "Dialyse (Kostenstellengruppe 3)"));
        items.add(new SelectItem(3, "OP (Kostenstellengruppe 4)"));
        items.add(new SelectItem(4, "Anästhesie (Kostenstellengruppe 5 u.a. auch für EKT-Leistungen)"));
        items.add(new SelectItem(5, "Geburtshilfe/Gynäkologie (Kostenstellengruppe 6)"));
        items.add(new SelectItem(6, "Kardiologie (Kostenstellengruppe 7)"));
        items.add(new SelectItem(7, "Endoskopie (Kostenstellengruppe 8)"));
        items.add(new SelectItem(8, "Radiologie (Kostenstellengruppe 9)"));
        items.add(new SelectItem(9, "Laboratorien (Kostenstellengruppe 10)"));
        items.add(new SelectItem(10, "Diagnostische Bereiche (Kostenstellengruppe 11)"));
        items.add(new SelectItem(11, "Therapeutische Verfahren (Kostenstellengruppe 12)"));
        items.add(new SelectItem(12, "Apotheke"));
        items.add(new SelectItem(13, "Sterilisation"));
        items.add(new SelectItem(14, "Externe Konsile"));
        items.add(new SelectItem(15, "Sonstige"));
        return items;
    }
    
    public List<SelectItem> getEndoscopyAmbulantTypes() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(-1, "Bitte wählen..."));
        //items.add(new SelectItem(1, "DGVS"));
        items.add(new SelectItem(2, "Leistungszeit"));
        items.add(new SelectItem(3, "Punktesystem"));
        items.add(new SelectItem(4, "Sonstige"));
        return items;
    }
    
    public List<String> getEndoscopyKeys() {
        List<String> items = new ArrayList<>();
        items.add("Eingriffszeit");
        items.add("Sonstiges");
        return items;
    }

    public List<String> getDelimitationFactsSubTitles() {
        List<String> tmp = new ArrayList<>();
        tmp.add("Personalkosten");
        tmp.add("Sachkosten");
        tmp.add("Infrastrukturkosten");
        return tmp;
    }

    public List<SelectItem> getRadLabServices() {
        List<SelectItem> items = new ArrayList<>();
        items.add(new SelectItem(0, ""));
        items.add(new SelectItem(1, "Hauskatalog"));
        items.add(new SelectItem(2, "DKG-NT"));
        items.add(new SelectItem(3, "EBM"));
        items.add(new SelectItem(4, "GOÄ"));
        items.add(new SelectItem(5, "Sonstige"));
        return items;
    }
    
}
