package org.inek.dataportal.feature.calculationhospital;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import org.inek.dataportal.entities.calc.drg.DrgCalcBasics;
import org.inek.dataportal.entities.calc.drg.KGLListCostCenter;
import org.inek.dataportal.entities.calc.drg.KGLListCostCenterCost;
import org.inek.dataportal.entities.calc.psy.KGPListCostCenter;
import org.inek.dataportal.entities.calc.psy.KGPListRadiologyLaboratory;
import org.inek.dataportal.entities.calc.psy.KGPListStationServiceCost;
import org.inek.dataportal.entities.calc.psy.KGPListTherapy;
import org.inek.dataportal.entities.calc.psy.KgpListMedInfra;
import org.inek.dataportal.entities.calc.psy.PeppCalcBasics;
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.entities.iface.StatusEntity;
import org.inek.dataportal.helper.BeanValidator;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.StringUtil;

/**
 * A utility class to read csv data and create elements for each row. These Elements will be bound to an owner via
 * BaseIdValue.setBaseInformationId where the needed id is obtained by StatusEntity.getId. So the object of the second
 * type will be the owner of the objects of the first type.
 *
 * @author kunkelan
 * @param <T> Type to import will create elements of this type
 * @param <S> DrgCaclBasics or PeppCalcBasic dataholder to which the items generated belongs. StatusEntity gives the
 * possibility to get the id to store it in T items which can set the BaseInformationId of itself. So these two types
 * are bound to each other in a loosly way, up to now only the two Types mentioned.
 *
 */
public final class DataImporter<T extends BaseIdValue, S extends StatusEntity> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DataImporter.class.getName());

    public static DataImporter obtainDataImporter(String importer) {
        switch (importer.toLowerCase()) {
            case "peppradiology":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Radiology">
                return new DataImporter<KGPListRadiologyLaboratory, PeppCalcBasics>(
                        "Nummer der Kostenstelle;Name der Kostenstelle;Leistungsdokumentation;Beschreibung",
                        new FileHolder("Radiology.csv"),
                        ErrorCounter.obtainErrorCounter("PEPP_RADIOLOGY"),
                        Arrays.asList(
                                new DataImportCheck<KGPListRadiologyLaboratory, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostCenterId(9);
                                            i.setCostCenterNumber(s);
                                        },
                                        "Nummer der Kostenstelle ungültig: "),
                                new DataImportCheck<KGPListRadiologyLaboratory, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostCenterId(9);
                                            i.setCostCenterText(s);
                                        },
                                        "Name der Kostenstelle ungültig: "),
                                new DataImportCheck<KGPListRadiologyLaboratory, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportServiceDocType,
                                        (i, s) -> {
                                            i.setCostCenterId(9);
                                            i.setServiceDocType(s);
                                        },
                                        "Verwendeter Schlüssel ungültig: "),
                                new DataImportCheck<KGPListRadiologyLaboratory, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostCenterId(9);
                                            i.setDescription(s);
                                        },
                                        "Beschreibung ungültig: ")
                        ),
                        //s -> s.getKgpMedInfraList().stream().filter(t -> 170 == t.getCostTypeId()).collect(Collectors.toList()),
                        //(s, t) -> s.getKgpMedInfraList().add(t),
                        (s, t) -> s.addRadiologyLaboratory(t),
                        KGPListRadiologyLaboratory.class
                );
            //</editor-fold>
            case "pepplaboratory":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Laboratory">
                return new DataImporter<KGPListRadiologyLaboratory, PeppCalcBasics>(
                        "Nummer der Kostenstelle;Name der Kostenstelle;Leistungsdokumentation;Beschreibung",
                        new FileHolder("Laboratory.csv"),
                        ErrorCounter.obtainErrorCounter("PEPP_LABORATORY"),
                        Arrays.asList(
                                new DataImportCheck<KGPListRadiologyLaboratory, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_LABORATORY"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostCenterId(10);
                                            i.setCostCenterNumber(s);
                                        },
                                        "Nummer der Kostenstelle ungültig: "),
                                new DataImportCheck<KGPListRadiologyLaboratory, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_LABORATORY"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostCenterId(10);
                                            i.setCostCenterText(s);
                                        },
                                        "Name der Kostenstelle ungültig: "),
                                new DataImportCheck<KGPListRadiologyLaboratory, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_LABORATORY"),
                                        DataImportCheck::tryImportServiceDocType,
                                        (i, s) -> {
                                            i.setCostCenterId(10);
                                            i.setServiceDocType(s);
                                        },
                                        "Verwendeter Schlüssel ungültig: "),
                                new DataImportCheck<KGPListRadiologyLaboratory, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_LABORATORY"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostCenterId(10);
                                            i.setDescription(s);
                                        },
                                        "Beschreibung ungültig: ")
                        ),
                        //s -> s.getKgpMedInfraList().stream().filter(t -> 170 == t.getCostTypeId()).collect(Collectors.toList()),
                        //(s, t) -> s.getKgpMedInfraList().add(t),
                        (s, t) -> s.addRadiologyLaboratory(t),
                        KGPListRadiologyLaboratory.class
                );
            //</editor-fold>
            case "peppmedinfra":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter medInfra">
                return new DataImporter<KgpListMedInfra, PeppCalcBasics>(
                        "Nummer der Kostenstelle;Name der Kostenstelle;Verwendeter Schlüssel;Kostenvolumen",
                        new FileHolder("Med_Infra.csv"),
                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                        Arrays.asList(new DataImportCheck<KgpListMedInfra, String>(
                                ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                DataImportCheck::tryImportString,
                                (i, s) -> {
                                    i.setCostTypeId(170);
                                    i.setCostCenterNumber(s);
                                },
                                "Nummer der Kostenstelle ungültig: "),
                                new DataImportCheck<KgpListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(170);
                                            i.setCostCenterText(s);
                                        },
                                        "Name der Kostenstelle ungültig: "),
                                new DataImportCheck<KgpListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(170);
                                            i.setKeyUsed(s);
                                        },
                                        "Verwendeter Schlüssel ungültig: "),
                                new DataImportCheck<KgpListMedInfra, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportRoundedInteger,
                                        (i, s) -> {
                                            i.setCostTypeId(170);
                                            i.setAmount(s);
                                        },
                                        "Kostenvolumen ungültig: ")
                        ),
                        //s -> s.getKgpMedInfraList().stream().filter(t -> 170 == t.getCostTypeId()).collect(Collectors.toList()),
                        //(s, t) -> s.getKgpMedInfraList().add(t),
                        (s, t) -> s.addMedInfraItem(t),
                        KgpListMedInfra.class
                );
            //</editor-fold>
            case "peppnonmedinfra":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter nonMedInfra">
                return new DataImporter<KgpListMedInfra, PeppCalcBasics>(
                        "Nummer der Kostenstelle;Name der Kostenstelle;Verwendeter Schlüssel;Kostenvolumen",
                        new FileHolder("NON_Med_Infra.csv"),
                        ErrorCounter.obtainErrorCounter("PEPP_NON_MED_INFRA"),
                        Arrays.asList(new DataImportCheck<KgpListMedInfra, String>(
                                ErrorCounter.obtainErrorCounter("PEPP_NON_MED_INFRA"),
                                DataImportCheck::tryImportString,
                                (i, s) -> {
                                    i.setCostTypeId(180);
                                    i.setCostCenterNumber(s);
                                },
                                "Nummer der Kostenstelle ungültig: "),
                                new DataImportCheck<KgpListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_NON_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(180);
                                            i.setCostCenterText(s);
                                        },
                                        "Name der Kostenstelle ungültig: "),
                                new DataImportCheck<KgpListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_NON_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(180);
                                            i.setKeyUsed(s);
                                        },
                                        "Verwendeter Schlüssel ungültig: "),
                                new DataImportCheck<KgpListMedInfra, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_NON_MED_INFRA"),
                                        DataImportCheck::tryImportRoundedInteger,
                                        (i, s) -> {
                                            i.setCostTypeId(180);
                                            i.setAmount(s);
                                        },
                                        "Kostenvolumen ungültig: ")
                        ),
                        (s, t) -> s.addMedInfraItem(t),
                        KgpListMedInfra.class
                );
            //</editor-fold>
            case "peppcostcenter":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGPListCostCenter, PeppCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;Kostenvolumen;VollkräfteÄD;"
                        + "Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_11_12_13.csv"),
                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER"),
                        Arrays.asList(
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER"),
                                        DataImportCheck::tryImportCostCenterId,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(11, 12, 13): "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "ungültige Kostenstellennummer : "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "ungültiger Kostenstellentext : "),
                                new DataImportCheck<KGPListCostCenter, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setAmount(s),
                                        "Kostenvolumen ungültig : "),
                                new DataImportCheck<KGPListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setFullVigorCnt(s),
                                        "[Anzahl VK ÄD] ungültig : "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKey(s),
                                        "ungültiger service : "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKeyDescription(s),
                                        "ungültige service Beschreibung : "),
                                new DataImportCheck<KGPListCostCenter, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceSum(s),
                                        "Summer der Leistungseinheiten ungültig : ")),
                        (s, t) -> s.addCostCenter(t),
                        KGPListCostCenter.class
                );
            //</editor-fold>
            case "drgcostcenter":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGLListCostCenter, DrgCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;Kostenvolumen;VollkräfteÄD;"
                        + "Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_11_12_13.csv"),
                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER"),
                        Arrays.asList(
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER"),
                                        DataImportCheck::tryImportCostCenterId,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(11, 12, 13): "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "ungültige Kostenstellennummer : "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "ungültiger Kostenstellentext : "),
                                new DataImportCheck<KGLListCostCenter, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setAmount(s),
                                        "Kostenvolumen ungültig : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setFullVigorCnt(s),
                                        "[Anzahl VK ÄD] ungültig : "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKey(s),
                                        "ungültiger service : "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKeyDescription(s),
                                        "ungültige service Beschreibung : "),
                                new DataImportCheck<KGLListCostCenter, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceSum(s),
                                        "Summer der Leistungseinheiten ungültig : ")),
                        (s, t) -> s.addCostCenter(t),
                        KGLListCostCenter.class
                );
            //</editor-fold>
            case "peppstationservicecost":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter stationServiceCost">
                return new DataImporter<KGPListStationServiceCost, PeppCalcBasics>(
                        "Nummer der Kostenstelle;Station;Eindeutige Zuordnung nach Psych-PV* (A, S, G, KJP, P);"
                        + "Anzahl Betten;bettenführende Aufnahmestation (bitte ankreuzen);"
                        + "Summe Pflegetage Regelbehandlung;Summe Gewichtungspunkte** Regelbehandlung;"
                        + "Summe Pflegetage Intensivbehandlung;Summe Gewichtungspunkte** Intensivbehandlung;"
                        + "VK Ärztlicher Dienst;VK Pflegedienst/Erziehungsdienst;VK Psychologen;"
                        + "VK Sozialarbeiter/Sozial-/Heil-pädagogen;VK Spezialtherapeuten;"
                        + "VK med.-techn. Dienst/Funktionsdienst;Kosten Ärztlicher Dienst;"
                        + "Kosten Pflegedienst/Erziehungsdienst;Kosten Psychologen;"
                        + "Kosten Sozialarbeiter/Sozial-/Heil-pädagogen;Kosten Spezialtherapeuten;"
                        + "Kosten med.-techn. Dienst/Funktionsdienst;Kosten med. Infrastruktur;"
                        + "Kosten nicht med. Infrastruktur",
                        new FileHolder("Station_kstg_21_22.csv"),
                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                        Arrays.asList(
                                new DataImportCheck<KGPListStationServiceCost, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "Nummer der Kostenstelle ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setStation(s),
                                        "Name der Station ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setPsyPvMapping(s),
                                        "Eindeutige Zuordnung nach Psych-PV* (A, S, G, KJP, P) ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setBedCnt(s),
                                        "Summer der Leistungseinheiten ungültig : "),
                                new DataImportCheck<KGPListStationServiceCost, Boolean>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportBoolean,
                                        (i, s) -> i.setReceivingStation(s),
                                        "bettenführende Aufnahmestation (bitte ankreuzen) ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setRegularCareDays(s),
                                        "Summe Pflegetage Regelbehandlung ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setRegularWeight(s),
                                        "Summe Gewichtungspunkte** Regelbehandlung ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setIntensiveCareDays(s),
                                        "Summe Pflegetage Intensivbehandlung ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setIntensiveWeight(s),
                                        "Summe Gewichtungspunkte** Intensivbehandlung ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setMedicalServiceCnt(s),
                                        "VK Ärztlicher Dienst ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setNursingServiceCnt(s),
                                        "VK Pflegedienst/Erziehungsdienst ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setPsychologistCnt(s),
                                        "VK Psychologen ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setSocialWorkerCnt(s),
                                        "VK Sozialarbeiter/Sozial-/Heil-pädagogen ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setSpecialTherapistCnt(s),
                                        "VK Spezialtherapeuten ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setFunctionalServiceCnt(s),
                                        "VK med.-techn. Dienst/Funktionsdienst ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setMedicalServiceAmount(s),
                                        "Kosten Ärztlicher Dienst ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setNursingServiceAmount(s),
                                        "Kosten Pflegedienst/Erziehungsdienst ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setPsychologistAmount(s),
                                        "Kosten Psychologen ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setSocialWorkerAmount(s),
                                        "Kosten Sozialarbeiter/Sozial-/Heil-pädagogen ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setSpecialTherapistAmount(s),
                                        "Kosten Spezialtherapeuten ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setFunctionalServiceAmount(s),
                                        "Kosten med.-techn. Dienst/Funktionsdienst ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setMedicalInfrastructureAmount(s),
                                        "Kosten med. Infrastruktur ungültig: "),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setNonMedicalInfrastructureAmount(s),
                                        "Kosten nicht med. Infrastruktur ungültig: ")),
                        (s, t) -> s.addStationServiceCost(t),
                        KGPListStationServiceCost.class
                );
            //</editor-fold>
            case "pepptherapy":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter therapy">
                return new DataImporter<KGPListTherapy, PeppCalcBasics>(
                        "KST-Gruppe;Leistungsinhalt der Kostenstelle;Fremdvergabe (keine, teilweise, vollständig);"
                        + "Leistungsschlüssel;KoArtG 1 Summe Leistungseinheiten;KoArtG 1 Personalkosten;"
                        + "KoArtG 3a Summe Leistungseinheiten;KoArtG 3a Personalkosten;"
                        + "KoArtG 2 Summe Leistungseinheiten;KoArtG 2 Personalkosten;"
                        + "KoArtG 3b Summe Leistungseinheiten;KoArtG 3b Personalkosten;"
                        + "KoArtG 3c Summe Leistungseinheiten;KoArtG 3c Personalkosten;"
                        + "KoArtG 3 Summe Leistungseinheiten;KoArtG 3 Personalkosten",
                        new FileHolder("Therapeutischer_Bereich_.csv"),
                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                        Arrays.asList(
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setCostCenterId(s),
                                        "Keine zulässige KST-Gruppe (23-26) : "),
                                new DataImportCheck<KGPListTherapy, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "Ungültige Zeichenkette: "),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportFremdvergabe,
                                        (i, s) -> i.setExternalService(s),
                                        "Keine zulässige Leistungserbringung 'Keine, Teilweise, Vollständige Fremdvergabe' : "),
                                new DataImportCheck<KGPListTherapy, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setKeyUsed(s),
                                        "Kein gültiger Leistungsschlüssel: "),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceUnitsCt1(s),
                                        "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 1: "),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setPersonalCostCt1(s),
                                        "Ungültiger Wert für Personalkosten KoArtGr 1: "),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceUnitsCt3a(s),
                                        "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 3a: "),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setPersonalCostCt3a(s),
                                        "Ungültiger Wert für Personalkosten KoArtGr 3a: "),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceUnitsCt2(s),
                                        "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 2: "),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setPersonalCostCt2(s),
                                        "Ungültiger Wert für Personalkosten KoArtGr 2: "),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceUnitsCt3b(s),
                                        "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 3b: "),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setPersonalCostCt3b(s),
                                        "Ungültiger Wert für Personalkosten KoArtGr 3b: "),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceUnitsCt3c(s),
                                        "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 3c: "),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setPersonalCostCt3c(s),
                                        "Ungültiger Wert für Personalkosten KoArtGr 3c: "),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceUnitsCt3(s),
                                        "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 3: "),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_THERAPY"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setPersonalCostCt3(s),
                                        "Ungültiger Wert für Personalkosten KoArtGr 3: ")),
                        (s, t) -> s.addTherapy(t),
                        KGPListTherapy.class
                );
            //</editor-fold>
            case "drgnormalward":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Normal Ward">
                return new DataImporter<KGLListCostCenterCost, DrgCalcBasics>(
                        "NummerKostenstelle;NameKostenstelle;FABSchluessel;"
                        + "BelegungFAB;Bettenzahl;Pflegetage;PPRMinuten;zusaetlicheGewichtung;"
                        + "AerztlicherDienstVK;PflegedienstVK;FunktionsdienstVK;"
                        + "AerztlicherDienstKostenstelle;PflegedienstKostenstelle;"
                        + "FunktionsdienstKostenstelle;ArzneimittelKostenstelle;"
                        + "medSachbedarfKostenstelle;medInfraKostenstelle;nichtMedInfraKostenstelle",
                        new FileHolder("Normalstation.csv"),
                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                        Arrays.asList(
                                new DataImportCheck<KGLListCostCenterCost, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "Nummer der Kostenstelle ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "Name der Kostenstelle ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setDepartmentKey(s),
                                        "FABSchlüssel ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setDepartmentAssignment(s),
                                        "BelegungFAB ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setBedCnt(s),
                                        "Bettenzahl ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setCareDays(s),
                                        "Pflegetage ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setPprMinutes(s),
                                        "PPRMinuten ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setPprWeight(s),
                                        "Zusaetliche Gewichtung ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setMedicalServiceCnt(s),
                                        "AerztlicherDienstVK ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setNursingServiceCnt(s),
                                        "PflegedienstVK ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setFunctionalServiceCnt(s),
                                        "FunktionsdienstVK ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setMedicalServiceAmount(s),
                                        "AerztlicherDienstKostenstelle ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setNursingServiceAmount(s),
                                        "PflegedienstKostenstelle ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setFunctionalServiceAmount(s),
                                        "FunktionsdienstKostenstelle ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setOverheadsMedicine(s),
                                        "ArzneimittelKostenstelle ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setOverheadsMedicalGoods(s),
                                        "medSachbedarfKostenstelle ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setMedicalInfrastructureCost(s),
                                        "medInfraKostenstelle ungültig: "),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setNonMedicalInfrastructureCost(s),
                                        "nichtMedInfraKostenstelle ungültig: ")
                        ),
                        (s, t) -> s.addCostCenterCost(t),
                        KGLListCostCenterCost.class
                );
            //</editor-fold>

            default:
                throw new IllegalArgumentException("unknown importer " + importer);
        }
    }

//    ),
//
//    PEPP_THERAPY(
//
//            )
    private DataImporter(String headLine, FileHolder fileHolder, ErrorCounter errorCounter,
            List<DataImportCheck<T, ?>> checker, BiConsumer<S, T> dataSink, Class<T> clazz) {
        this.headLine = headLine;
        this.fileHolder = fileHolder;
        this.errorCounter = errorCounter;
        this.checkers = checker;
        //this.listToFill = listToFill;
        this.dataSink = dataSink;
        this.clazz = clazz;
    }

    public void uploadNotices(S calcBasics) {
        try {
            resetCounter();

            int cntColumns = headLine.split(";").length;

            Scanner scanner = new Scanner(fileHolder.getFile().getInputStream());

            if (!scanner.hasNextLine()) {
                errorCounter.addRowErrorMsg("Datei " + fileHolder.getFile().getName() + " enthält keine Daten");
                return;
            }

            boolean isFirstLine = true;
            while (scanner.hasNextLine()) {
                String line = Utils.convertFromUtf8(scanner.nextLine());
                if (isFirstLine && headLine.equals(line)) {
                    // ignore optional header
                    continue;
                }
                isFirstLine = false;
                T item = readLine(line, cntColumns, calcBasics);
                if (item != null) {
                    dataSink.accept(calcBasics, item);
                }
            }

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private T readLine(String line, int cntColumns, S calcBasics) {

        T item = null;
        try {
            String[] data = splitLineInColumns(line, cntColumns);
            errorCounter.incRowCounter();

            item = createNewItem(item, calcBasics);

            applyImport(item, data);

            validateImport(item);

        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException("can not instantiate type " + clazz.getSimpleName());
        } catch (IllegalArgumentException ex) {
            LOGGER.log(Level.WARNING, null, ex);
            errorCounter.addRowErrorMsg(ex.getMessage());
            return null;
        }
        return item;
    }

    private T createNewItem(T item, S calcBasics) throws IllegalAccessException, InstantiationException {
        item = clazz.newInstance();
        item.setBaseInformationId(calcBasics.getId());
        return item;
    }

    private void validateImport(T item) throws IllegalArgumentException {
        String validateText = BeanValidator.validateData(item);
        if (!validateText.isEmpty()) {
            throw new IllegalArgumentException(validateText);
        }
    }

    private void applyImport(T item, String[] data) {
        int i = 0;
        for (DataImportCheck<T, ?> checker : checkers) {
            checker.tryImport(item, data[i++]);
        }
    }

    private String[] splitLineInColumns(String line, int cntColumns) throws IllegalArgumentException {
        if (line.endsWith(";")) {
            line = line + " ";
        }
        String[] data = StringUtil.splitAtUnquotedSemicolon(line);
        if (data.length != cntColumns) {
            throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
        }
        return data;
    }

    private void resetCounter() {
        checkers.get(0).resetCounter();
    }

    public void downloadTemplate() {
        Utils.downloadText(headLine + "\r\n", fileHolder.getFileName());
    }

    public String getMessage() {
        return errorCounter.getMessage();
    }

    public String getHeader() {
        return headLine;
    }

    public boolean containsError() {
        return errorCounter.containsError();
    }

    public Part getFile() {
        return fileHolder.getFile();
    }

    public void setFile(Part file) {
        fileHolder.setFile(file);
    }

    public void toggleJournal() {
        showJournal = !showJournal;
    }

    public boolean isShowJournal() {
        return showJournal;
    }

    public void setShowJournal(boolean showJournal) {
        this.showJournal = showJournal;
    }

    private final FileHolder fileHolder;
    private final ErrorCounter errorCounter;
    private final String headLine;
    private final List<DataImportCheck<T, ?>> checkers;
    private final BiConsumer<S, T> dataSink;
    private boolean showJournal = false;
    private final Class<T> clazz;

}
