package org.inek.dataportal.calc.backingbean;

import org.inek.dataportal.calc.BeanValidator;
import org.inek.dataportal.calc.entities.drg.*;
import org.inek.dataportal.calc.entities.psy.*;
import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.data.iface.StatusEntity;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.StringUtil;

import javax.servlet.http.Part;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DataImporter.class.getName());

    @SuppressWarnings({"MethodLength", "JavaNCSS", "MultipleStringLiterals"})
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
                        s -> s.clearRadiologyLaboratory(9),
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
                        s -> s.clearRadiologyLaboratory(10),
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
                        s -> s.deleteKgpMedInfraList(170),
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
                        s -> s.deleteKgpMedInfraList(170),
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
                        (s) -> s.deleteCostCenters(),
                        KGPListCostCenter.class
                );
            //</editor-fold>
            case "peppcostcenter11":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGPListCostCenter, PeppCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;" +
                                "AnzahlVKÄD_vor;AnzahlVKÄD_nach;AnzahlVKFD_vor;AnzahlVKFD_nach;" +
                                "AnzahlVKFD_vor;AnzahlVKFD_nach;KostenvolumenFD_vor;KostenvolumenFD_nach;"
                        + "Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_11.csv"),
                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                        Arrays.asList(
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportCostCenterId11,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(11): "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "ungültige Kostenstellennummer : "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "ungültiger Kostenstellentext : "),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountMedStaffPre(s),
                                        "[Anzahl VK ÄD vor Abgrenzung] ungültig : "),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountMedStaffAfter(s),
                                        "[Anzahl VK ÄD nach Abgrenzung] ungültig : "),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeMedStaffPre(s),
                                        "[Anzahl VK FD vor Abgrenzung] ungültig : "),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeMedStaffAfter(s),
                                        "[Anzahl VK FD nach Abgrenzung] ungültig : "),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountFunctionalServicePre(s),
                                        "[Anzahl VK FD vor Abgrenzung] ungültig : "),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountFunctionalServiceAfter(s),
                                        "[Anzahl VK FD nach Abgrenzung] ungültig : "),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeFunctionalServicePre(s),
                                        "[Kostenvolumen FD vor Abgrenzung] ungültig : "),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeFunctionalServiceAfter(s),
                                        "[Kostenvolumen FD nach Abgrenzung] ungültig : "),



                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKey(s),
                                        "ungültiger service : "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKeyDescription(s),
                                        "ungültige service Beschreibung : "),
                                new DataImportCheck<KGPListCostCenter, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceSum(s),
                                        "Summer der Leistungseinheiten ungültig : ")),
                        (s, t) -> s.addCostCenter(t),
                        (s) -> s.deleteCostCenters(11),
                        KGPListCostCenter.class
                );
            //</editor-fold>
            case "peppcostcenter12":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGPListCostCenter, PeppCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;Kostenvolumen;VollkräfteÄD;"
                        + "Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_12.csv"),
                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_12"),
                        Arrays.asList(
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_12"),
                                        DataImportCheck::tryImportCostCenterId12,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(12): "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_12"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "ungültige Kostenstellennummer : "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_12"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "ungültiger Kostenstellentext : "),
                                new DataImportCheck<KGPListCostCenter, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_12"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setAmount(s),
                                        "Kostenvolumen ungültig : "),
                                new DataImportCheck<KGPListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_12"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setFullVigorCnt(s),
                                        "[Anzahl VK ÄD] ungültig : "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_12"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKey(s),
                                        "ungültiger service : "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_12"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKeyDescription(s),
                                        "ungültige service Beschreibung : "),
                                new DataImportCheck<KGPListCostCenter, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_12"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceSum(s),
                                        "Summer der Leistungseinheiten ungültig : ")),
                        (s, t) -> s.addCostCenter(t),
                        (s) -> s.deleteCostCenters(12),
                        KGPListCostCenter.class
                );
            //</editor-fold>
            case "peppcostcenter13":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGPListCostCenter, PeppCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;Kostenvolumen;VollkräfteÄD;"
                        + "Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_13.csv"),
                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_13"),
                        Arrays.asList(
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_13"),
                                        DataImportCheck::tryImportCostCenterId13,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(13): "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_13"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "ungültige Kostenstellennummer : "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_13"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "ungültiger Kostenstellentext : "),
                                new DataImportCheck<KGPListCostCenter, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_13"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setAmount(s),
                                        "Kostenvolumen ungültig : "),
                                new DataImportCheck<KGPListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_13"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setFullVigorCnt(s),
                                        "[Anzahl VK ÄD] ungültig : "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_13"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKey(s),
                                        "ungültiger service : "),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_13"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKeyDescription(s),
                                        "ungültige service Beschreibung : "),
                                new DataImportCheck<KGPListCostCenter, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_COST_CENTER_13"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceSum(s),
                                        "Summer der Leistungseinheiten ungültig : ")),
                        (s, t) -> s.addCostCenter(t),
                        (s) -> s.deleteCostCenters(13),
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
                        s -> s.deleteCostCenters_11_12_13(),
                        KGLListCostCenter.class
                );
            //</editor-fold>
            case "drgcostcenter11":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGLListCostCenter, DrgCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;AnzahlVKÄDVor;AnzahlVKÄDNach;KostenvolumenÄDVor;" +
                                "KostenvolumenÄDNach;AnzahlVKFDVor;AnzahlVKFDNach;KostenvolumenFDVor;KostenvolumenFDNach;Leistungsschlüssel;" +
                                "Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_11.csv"),
                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                        Arrays.asList(
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportCostCenterId11,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(11): "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "ungültige Kostenstellennummer : "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "ungültiger Kostenstellentext : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountMedStaffPre(s),
                                        "Anzahl VK ÄD vor Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountMedStaffAfter(s),
                                        "Anzahl VK ÄD nach Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeMedStaffPre(s),
                                        "Kostenvolumen ÄD vor Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeMedStaffAfter(s),
                                        "Kostenvolumen ÄD nach Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountFunctionalServicePre(s),
                                        "Anzahl VK FD vor Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountFunctionalServiceAfter(s),
                                        "Anzahl VK FD nach Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeFunctionalServicePre(s),
                                        "Kostenvolumen FD vor Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeFunctionalServiceAfter(s),
                                        "Kostenvolumen FD nach Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKey(s),
                                        "ungültiger service : "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKeyDescription(s),
                                        "ungültige service Beschreibung : "),
                                new DataImportCheck<KGLListCostCenter, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceSum(s),
                                        "Summer der Leistungseinheiten ungültig : ")),
                        (s, t) -> s.addCostCenter(t),
                        s -> s.deleteCostCenters(11),
                        KGLListCostCenter.class
                );
            //</editor-fold>
            case "drgcostcenter12":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGLListCostCenter, DrgCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;AnzahlVKÄDVor;AnzahlVKÄDNach;KostenvolumenÄDVor;" +
                                "KostenvolumenÄDNach;AnzahlVKFDVor;AnzahlVKFDNach;KostenvolumenFDVor;KostenvolumenFDNach;Leistungsschlüssel;" +
                                "Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_12.csv"),
                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                        Arrays.asList(
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportCostCenterId12,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(12): "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "ungültige Kostenstellennummer : "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "ungültiger Kostenstellentext : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountMedStaffPre(s),
                                        "Anzahl VK ÄD vor Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountMedStaffAfter(s),
                                        "Anzahl VK ÄD nach Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeMedStaffPre(s),
                                        "Kostenvolumen ÄD vor Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeMedStaffAfter(s),
                                        "Kostenvolumen ÄD nach Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountFunctionalServicePre(s),
                                        "Anzahl VK FD vor Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountFunctionalServiceAfter(s),
                                        "Anzahl VK FD nach Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeFunctionalServicePre(s),
                                        "Kostenvolumen FD vor Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeFunctionalServiceAfter(s),
                                        "Kostenvolumen FD nach Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKey(s),
                                        "ungültiger service : "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKeyDescription(s),
                                        "ungültige service Beschreibung : "),
                                new DataImportCheck<KGLListCostCenter, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceSum(s),
                                        "Summer der Leistungseinheiten ungültig : ")),
                        (s, t) -> s.addCostCenter(t),
                        s -> s.deleteCostCenters(12),
                        KGLListCostCenter.class
                );
            //</editor-fold>
            case "drgcostcenter13":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGLListCostCenter, DrgCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;AnzahlVKÄDVor;AnzahlVKÄDNach;KostenvolumenÄDVor;" +
                                "KostenvolumenÄDNach;AnzahlVKFDVor;AnzahlVKFDNach;KostenvolumenFDVor;KostenvolumenFDNach;Leistungsschlüssel;" +
                                "Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_13.csv"),
                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                        Arrays.asList(
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportCostCenterId13,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(13): "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "ungültige Kostenstellennummer : "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "ungültiger Kostenstellentext : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountMedStaffPre(s),
                                        "Anzahl VK ÄD vor Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountMedStaffAfter(s),
                                        "Anzahl VK ÄD nach Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeMedStaffPre(s),
                                        "Kostenvolumen ÄD vor Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeMedStaffAfter(s),
                                        "Kostenvolumen ÄD nach Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountFunctionalServicePre(s),
                                        "Anzahl VK FD vor Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountFunctionalServiceAfter(s),
                                        "Anzahl VK FD nach Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeFunctionalServicePre(s),
                                        "Kostenvolumen FD vor Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeFunctionalServiceAfter(s),
                                        "Kostenvolumen FD nach Abgrenzung : "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKey(s),
                                        "ungültiger service : "),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setServiceKeyDescription(s),
                                        "ungültige service Beschreibung : "),
                                new DataImportCheck<KGLListCostCenter, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceSum(s),
                                        "Summer der Leistungseinheiten ungültig : ")),
                        (s, t) -> s.addCostCenter(t),
                        s -> s.deleteCostCenters(13),
                        KGLListCostCenter.class
                );
            //</editor-fold>
            case "peppstationservicecost":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter stationServiceCost">
                return new DataImporter<KGPListStationServiceCost, PeppCalcBasics>(
                        "Nummer der Kostenstelle;Station;Eindeutige Zuordnung nach Psych-PV* (A, S, G, KJP, P);"
                        + "Anzahl Betten;Belegung;"
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
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                                        DataImportCheck::tryImportOccupancyType,
                                        (i, s) -> i.setOccupancy(s),
                                        "Belegung (voll- und/oder teilstationär) ungültig: "),
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
                        s -> s.clearStationServiceCosts(),
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
                        s -> s.clearTheapies(),
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
                        s -> s.clearCostCenterCosts(),
                        KGLListCostCenterCost.class
                );
            //</editor-fold>
            case "drgintensive":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Intensive">
                return new DataImporter<KGLListIntensivStroke, DrgCalcBasics>(
                        "Intensivstation;FAB;Anzahl_Betten;Anzahl_Fälle;Mindestmerkmale_OPS_8-980_erfüllt;"
                        + "Mindestmerkmale_OPS_8-98f_erfüllt;Mindestmerkmale_nur_erfüllt_im_Zeitabschnitt;"
                        + "Summe_gewichtete_Intensivstunden;Summe_ungewichtete_Intensivstunden;"
                        + "Minimum;Maximum;Erläuterung;Vollkraft_ÄD;Vollkraft_PD;Vollkraft_FD;"
                        + "Kosten_ÄD;Kosten_PD;Kosten_FD;Kosten_GK_Arzneimittel;Kosten_GK_med_Sachbedarf;"
                        + "Kosten_med_Infra;Kosten_nicht_med_Infra",
                        new FileHolder("Intensiv.csv"),
                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                        Arrays.asList(
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "Kein zulässiger Intensivstationsname:  "),
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setDepartmentAssignment(s),
                                        "Kein zulässiger Abteilungsname: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setBedCnt(s),
                                        "Bettenzahl ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setCaseCnt(s),
                                        "Fallzahl ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Boolean>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportBoolean,
                                        (i, s) -> i.setOps8980(s),
                                        "Mindestmerkmale OPS 8-980 erfüllt: "),
                                new DataImportCheck<KGLListIntensivStroke, Boolean>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportBoolean,
                                        (i, s) -> i.setOps898f(s),
                                        "Mindestmerkmale OPS 8-98f erfüllt: "),
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setMinimumCriteriaPeriod(s),
                                        "Mindestmerkmale nur erfüllt im Zeitabschnitt: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setIntensivHoursWeighted(s),
                                        "Summe gewichtete Intensivstunden: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setIntensivHoursNotweighted(s),
                                        "Summe ungewichtete Intensivstunden: "),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setWeightMinimum(s),
                                        "Minimum: "),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setWeightMaximum(s),
                                        "Maximum: "),
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setWeightDescription(s),
                                        "Mindestmerkmale nur erfüllt im Zeitabschnitt: "),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setMedicalServiceCnt(s),
                                        "VK Ärztlicher Dienst ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setNursingServiceCnt(s),
                                        "VK Pflegedienst ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setFunctionalServiceCnt(s),
                                        "VK Funktionsdienst ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setMedicalServiceCost(s),
                                        "Kosten Ärztlicher Dienst ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setNursingServiceCost(s),
                                        "Kosten Funktionsdienst ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setFunctionalServiceCost(s),
                                        "Kosten Funktionsdienst ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setOverheadsMedicine(s),
                                        "Kosten GK Arzneimittel ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setOverheadMedicalGoods(s),
                                        "Kosten GK med Sachbedarf ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setMedicalInfrastructureCost(s),
                                        "Kosten med. Infrastruktur ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setNonMedicalInfrastructureCost(s),
                                        "Kosten nicht med. Infrastuktur ungültig: ")
                        ),
                        (s, t) -> s.addIntensive(t),
                        s -> s.clearIntensive(),
                        KGLListIntensivStroke.class
                );
            //</editor-fold>
            case "drgstrokeunit":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Stroke Unit">
                return new DataImporter<KGLListIntensivStroke, DrgCalcBasics>(
                        "Intensivstation;FAB;Anzahl_Betten;Anzahl_Fälle;Mindestmerkmale_OPS_8-981_erfüllt;"
                        + "Mindestmerkmale_OPS_8-98b_erfüllt;Mindestmerkmale_nur_erfüllt_im_Zeitabschnitt;"
                        + "Summe_gewichtete_Intensivstunden;Summe_ungewichtete_Intensivstunden;"
                        + "Minimum;Maximum;Erläuterung;Vollkraft_ÄD;Vollkraft_PD;Vollkraft_FD;"
                        + "Kosten_ÄD;Kosten_PD;Kosten_FD;Kosten_GK_Arzneimittel;Kosten_GK_med_Sachbedarf;"
                        + "Kosten_med_Infra;Kosten_nicht_med_Infra",
                        new FileHolder("StrokeUnit.csv"),
                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                        Arrays.asList(
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "Kein zulässiger Intensivstationsname:  "),
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setDepartmentAssignment(s),
                                        "Kein zulässiger Abteilungsname: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setBedCnt(s),
                                        "Bettenzahl ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setCaseCnt(s),
                                        "Fallzahl ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Boolean>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportBoolean,
                                        (i, s) -> i.setOps8981(s),
                                        "Mindestmerkmale OPS 8-981 erfüllt: "),
                                new DataImportCheck<KGLListIntensivStroke, Boolean>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportBoolean,
                                        (i, s) -> i.setOps898b(s),
                                        "Mindestmerkmale OPS 8-98b erfüllt: "),
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setMinimumCriteriaPeriod(s),
                                        "Mindestmerkmale nur erfüllt im Zeitabschnitt: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setIntensivHoursWeighted(s),
                                        "Summe gewichtete Intensivstunden: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setIntensivHoursNotweighted(s),
                                        "Summe ungewichtete Intensivstunden: "),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setWeightMinimum(s),
                                        "Minimum: "),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setWeightMaximum(s),
                                        "Maximum: "),
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setWeightDescription(s),
                                        "Mindestmerkmale nur erfüllt im Zeitabschnitt: "),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setMedicalServiceCnt(s),
                                        "VK Ärztlicher Dienst ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setNursingServiceCnt(s),
                                        "VK Pflegedienst ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setFunctionalServiceCnt(s),
                                        "VK Funktionsdienst ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setMedicalServiceCost(s),
                                        "Kosten Ärztlicher Dienst ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setNursingServiceCost(s),
                                        "Kosten Funktionsdienst ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setFunctionalServiceCost(s),
                                        "Kosten Funktionsdienst ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setOverheadsMedicine(s),
                                        "Kosten GK Arzneimittel ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setOverheadMedicalGoods(s),
                                        "Kosten GK med Sachbedarf ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setMedicalInfrastructureCost(s),
                                        "Kosten med. Infrastruktur ungültig: "),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setNonMedicalInfrastructureCost(s),
                                        "Kosten nicht med. Infrastuktur ungültig: ")
                        ),
                        (s, t) -> s.addStroke(t),
                        s -> s.clearStroke(),
                        KGLListIntensivStroke.class
                );
            //</editor-fold>
            case "drgmedinfra":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Med Infra">
                return new DataImporter<KGLListMedInfra, DrgCalcBasics>(
                        "KostenstelleNummer;KostenstelleText;Schlüssel;Kostenvolumen;KostenvolumenNachAbgrenzung",
                        new FileHolder("MedInfra.csv"),
                        ErrorCounter.obtainErrorCounter("DRG_MED_INFRA"),
                        Arrays.asList(
                                new DataImportCheck<KGLListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "Kostenstellennummer:  "),
                                new DataImportCheck<KGLListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "Kostenstellenname: "),
                                new DataImportCheck<KGLListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setKeyUsed(s),
                                        "Schlüssel: "),
                                new DataImportCheck<KGLListMedInfra, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_MED_INFRA"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setAmount(s),
                                        "Kostenvolumen: "),
                                new DataImportCheck<KGLListMedInfra, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_MED_INFRA"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setAmountAfter(s),
                                        "Kostenvolumen nach Abgrenzung: ")
                        ),
                        (s, t) -> s.addMedInfra(t),
                        s -> s.clearMedInfra(),
                        KGLListMedInfra.class
                );
            //</editor-fold>
            case "drgnonmedinfra":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Non-Med Infra">
                return new DataImporter<KGLListMedInfra, DrgCalcBasics>(
                        "KostenstelleNummer;KostenstelleText;Schlüssel;Kostenvolumen;KostenvolumenNachAbgrenzung",
                        new FileHolder("NonMedInfra.csv"),
                        ErrorCounter.obtainErrorCounter("DRG_NON_MED_INFRA"),
                        Arrays.asList(
                                new DataImportCheck<KGLListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_NON_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "Kostenstellennummer:  "),
                                new DataImportCheck<KGLListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_NON_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "Kostenstellenname: "),
                                new DataImportCheck<KGLListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_NON_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setKeyUsed(s),
                                        "Schlüssel: "),
                                new DataImportCheck<KGLListMedInfra, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_NON_MED_INFRA"),
                                        DataImportCheck::tryImportDoubleAsInt,
                                        (i, s) -> i.setAmount(s),
                                        "Kostenvolumen: "),
                                new DataImportCheck<KGLListMedInfra, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_NON_MED_INFRA"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setAmountAfter(s),
                                        "Kostenvolumen nach Abgrenzung: ")
                        ),
                        (s, t) -> s.addNonMedInfra(t),
                        s -> s.clearNonMedInfra(),
                        KGLListMedInfra.class
                );
            //</editor-fold>
            case "drglaboratory":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Laboratory">
                return new DataImporter<KGLListRadiologyLaboratory, DrgCalcBasics>(
                        "KostenstelleNummer;KostenstelleName;Leistungsbereich;Leistungsdokumentation;"
                                + "Beschreibung;LeistungsvolumenVor;KostenvolumenVor;KostenvolumenNach",
                        new FileHolder("Laboratory.csv"),
                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                        Arrays.asList(
                                new DataImportCheck<KGLListRadiologyLaboratory, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "KostenstelleNummer: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "KostenstelleName: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportLabServiceArea,
                                        (i, s) -> i.setServiceArea(s),
                                        "Leistungsbereich: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportServiceDocType,
                                        (i, s) -> i.setService(s),
                                        "Leistungsdokumentation: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setDescription(s),
                                        "Beschreibung: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceVolumePre(s),
                                        "LeistungsvolumenVor: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setAmountPre(s),
                                        "KostenvolumenVor: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setAmountPost(s),
                                        "KostenvolumenNach: ")
                        ),
                        (s, t) -> s.addRadiologyLaboratories(t, 10),
                        s -> s.clearLaboratories(),
                        KGLListRadiologyLaboratory.class
                );
            //</editor-fold>
            case "drgradiology":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Radiology">
                return new DataImporter<KGLListRadiologyLaboratory, DrgCalcBasics>(
                        "KostenstelleNummer;KostenstelleName;Leistungsdokumentation;Beschreibung;LeistungsvolumenVor;" +
                                "LeistungsvolumenNach;KostenvolumenVor;KostenvolumenNach;AnzahlVKÄDVor;AnzahlVKÄDNach;KostenvolumenÄDVor;" +
                                "KostenvolumenÄDNach;AnzahlVKFDVor;AnzahlVKFDNach;KostenvolumenFDVor;KostenvolumenFDNach",
                        new FileHolder("Radiology.csv"),
                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                        Arrays.asList(
                                new DataImportCheck<KGLListRadiologyLaboratory, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "KostenstelleNummer: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "KostenstelleName: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportServiceDocType,
                                        (i, s) -> i.setService(s),
                                        "Leistungsdokumentation: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, String>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setDescription(s),
                                        "Beschreibung: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceVolumePre(s),
                                        "LeistungsvolumenVor: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setServiceVolumePost(s),
                                        "LeistungsvolumenNach: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setAmountPre(s),
                                        "KostenvolumenVor: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportInteger,
                                        (i, s) -> i.setAmountPost(s),
                                        "KostenvolumenNach: "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountMedStaffPre(s),
                                        "Anzahl VK ÄD vor Abgrenzung : "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountMedStaffAfter(s),
                                        "Anzahl VK ÄD nach Abgrenzung : "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeMedStaffPre(s),
                                        "Kostenvolumen ÄD vor Abgrenzung : "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeMedStaffAfter(s),
                                        "Kostenvolumen ÄD nach Abgrenzung : "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountFunctionalServicePre(s),
                                        "Anzahl VK FD vor Abgrenzung : "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCountFunctionalServiceAfter(s),
                                        "Anzahl VK FD nach Abgrenzung : "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeFunctionalServicePre(s),
                                        "Kostenvolumen FD vor Abgrenzung : "),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        ErrorCounter.obtainErrorCounter("DRG_LABORATORY"),
                                        DataImportCheck::tryImportDouble,
                                        (i, s) -> i.setCostVolumeFunctionalServiceAfter(s),
                                        "Kostenvolumen FD nach Abgrenzung : ")

                        ),
                        (s, t) -> s.addRadiologyLaboratories(t, 9),
                        (s) -> s.clearRadiologies(),
                        KGLListRadiologyLaboratory.class
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
            List<DataImportCheck<T, ?>> checker, BiConsumer<S, T> dataSink, Consumer<S> clearList, Class<T> clazz) {
        this.headLine = headLine;
        this.fileHolder = fileHolder;
        this.errorCounter = errorCounter;
        this.checkers = checker;
        this.dataSink = dataSink;
        this.clazz = clazz;
        this.clearList = clearList;
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

            List<T> items = new ArrayList<>();
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
                    items.add(item);
                }
            }

            if (!errorCounter.containsError() && !items.isEmpty()) {
                clearList.accept(calcBasics);
                for (T item : items) {
                    dataSink.accept(calcBasics, item);
                }
            }

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private T readLine(String line, int cntColumns, S calcBasics) {

        T item = null;
        String[] data = splitLineInColumns(line, cntColumns);
        errorCounter.incRowCounter();
        if (data == null) {
            errorCounter.addRowErrorMsg(Utils.getMessage("msgWrongElementCount"));
            return null;
        }
        try {
            item = createNewItem(item, calcBasics);
            applyImport(item, data);
            String msg = BeanValidator.validateData(item);
            if (msg != null && !msg.isEmpty()) {
                errorCounter.addRowErrorMsg(msg);
                return null;
            }
        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException("can not instantiate type " + clazz.getSimpleName());
        }
        return item;
    }

    private T createNewItem(T item, S calcBasics) throws IllegalAccessException, InstantiationException {
        item = clazz.newInstance();
        item.setBaseInformationId(calcBasics.getId());
        return item;
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
            // the following exception floods the log
            //throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
            // instead, we return null
            return null;
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
    private final Consumer<S> clearList;
}
