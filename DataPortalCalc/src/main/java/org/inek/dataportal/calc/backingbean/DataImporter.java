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
 * @param <T> Type to import will create elements of this type
 * @param <S> DrgCaclBasics or PeppCalcBasic dataholder to which the items generated belongs. StatusEntity gives the
 *            possibility to get the id to store it in T items which can set the BaseInformationId of itself. So these two types
 *            are bound to each other in a loosly way, up to now only the two Types mentioned.
 * @author kunkelan
 */
public final class DataImporter<T extends BaseIdValue, S extends StatusEntity> implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DataImporter.class.getName());
    private final FileHolder fileHolder;
    private final ErrorCounter errorCounter;
    private final String headLine;
    private final List<DataImportCheck<T, ?>> checkers;
    private final BiConsumer<S, T> dataSink;
    private final Class<T> clazz;
    private final Consumer<S> clearList;
    private boolean showJournal = false;

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

    @SuppressWarnings({"MethodLength", "JavaNCSS", "MultipleStringLiterals"})
    static DataImporter obtainDataImporter(String importer) {
        switch (importer.toLowerCase()) {
            case "peppradiology":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Radiology">
                return new DataImporter<KGPListRadiologyLaboratory, PeppCalcBasics>(
                        "KostenstelleNummer;KostenstelleName;Leistungsdokumentation;Beschreibung",
                        new FileHolder("Radiology.csv"),
                        new ErrorCounter(),
                        Arrays.asList(
                                new DataImportCheck<KGPListRadiologyLaboratory, String>(
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostCenterId(9);
                                            i.setCostCenterNumber(s);
                                        },
                                        "Nummer der Kostenstelle ungültig"),


                                new DataImportCheck<KGPListRadiologyLaboratory, String>(
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostCenterId(9);
                                            i.setCostCenterText(s);
                                        },
                                        "Name der Kostenstelle ungültig"),


                                new DataImportCheck<KGPListRadiologyLaboratory, String>(
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostCenterId(9);
                                            i.setServiceDocTypeFromString(s);
                                        },
                                        "Leistungsdokumentation"),

                                new DataImportCheck<KGPListRadiologyLaboratory, String>(
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostCenterId(9);
                                            i.setDescription(s);
                                        },
                                        "Beschreibung")
                        ),
                        //s -> s.getKgpMedInfraList().stream().filter(t -> 170 == t.getCostTypeId()).collect(Collectors.toList()),
                        //(s, t) -> s.getKgpMedInfraList().add(t),
                        PeppCalcBasics::addRadiologyLaboratory,
                        s -> s.clearRadiologyLaboratory(9),
                        KGPListRadiologyLaboratory.class
                );
            //</editor-fold>
            case "pepplaboratory":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Laboratory">
                return new DataImporter<KGPListRadiologyLaboratory, PeppCalcBasics>(
                        "Nummer der Kostenstelle;Name der Kostenstelle;Leistungsdokumentation;Beschreibung",
                        new FileHolder("Laboratory.csv"),
                        new ErrorCounter(),
                        Arrays.asList(
                                new DataImportCheck<KGPListRadiologyLaboratory, String>(
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostCenterId(10);
                                            i.setCostCenterNumber(s);
                                        },
                                        "Nummer der Kostenstelle ungültig"),
                                new DataImportCheck<KGPListRadiologyLaboratory, String>(
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostCenterId(10);
                                            i.setCostCenterText(s);
                                        },
                                        "Name der Kostenstelle ungültig"),
                                new DataImportCheck<KGPListRadiologyLaboratory, Integer>(
                                        DataImportCheck::tryImportServiceDocType,
                                        (i, s) -> {
                                            i.setCostCenterId(10);
                                            i.setServiceDocType(s);
                                        },
                                        "Verwendeter Schlüssel ungültig"),
                                new DataImportCheck<KGPListRadiologyLaboratory, String>(
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostCenterId(10);
                                            i.setDescription(s);
                                        },
                                        "Beschreibung ungültig")
                        ),
                        //s -> s.getKgpMedInfraList().stream().filter(t -> 170 == t.getCostTypeId()).collect(Collectors.toList()),
                        //(s, t) -> s.getKgpMedInfraList().add(t),
                        PeppCalcBasics::addRadiologyLaboratory,
                        s -> s.clearRadiologyLaboratory(10),
                        KGPListRadiologyLaboratory.class
                );
            //</editor-fold>
            case "peppmedinfra":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter medInfra">
                return new DataImporter<KgpListMedInfra, PeppCalcBasics>(
                        "Nummer der Kostenstelle;Name der Kostenstelle;Verwendeter Schlüssel;Kostenvolumen;AnteilKostenvolumenNachAbgrenzung",
                        new FileHolder("Med_Infra.csv"),
                        new ErrorCounter(),
                        Arrays.asList(new DataImportCheck<KgpListMedInfra, String>(
                                DataImportCheck::tryImportString,
                                (i, s) -> {
                                    i.setCostTypeId(170);
                                    i.setCostCenterNumber(s);
                                },
                                        "Nummer der Kostenstelle ungültig"),
                                new DataImportCheck<KgpListMedInfra, String>(
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(170);
                                            i.setCostCenterText(s);
                                        },
                                        "Name der Kostenstelle ungültig"),
                                new DataImportCheck<KgpListMedInfra, String>(
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(170);
                                            i.setKeyUsed(s);
                                        },
                                        "Verwendeter Schlüssel ungültig"),
                                new DataImportCheck<KgpListMedInfra, Integer>(
                                        DataImportCheck::tryImportRoundedInteger,
                                        (i, s) -> {
                                            i.setCostTypeId(170);
                                            i.setAmount(s);
                                        },
                                        "Kostenvolumen nach Abgrenzung ungültig"),
                                new DataImportCheck<KgpListMedInfra, Double>(
                                        DataImportCheck::tryImportDoubleAndInteger,
                                        (i, s) -> {
                                            i.setCostTypeId(170);
                                            i.setPartCostVolumeMedStaffAfter(s);
                                        },
                                        "Kostenvolumen nach Abgrenzung ungültig")
                        ),
                        //s -> s.getKgpMedInfraList().stream().filter(t -> 170 == t.getCostTypeId()).collect(Collectors.toList()),
                        //(s, t) -> s.getKgpMedInfraList().add(t),
                        PeppCalcBasics::addMedInfraItem,
                        s -> s.deleteKgpMedInfraList(170),
                        KgpListMedInfra.class
                );
            //</editor-fold>
            case "peppnonmedinfra":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter nonMedInfra">
                return new DataImporter<KgpListMedInfra, PeppCalcBasics>(
                        "Nummer der Kostenstelle;Name der Kostenstelle;Verwendeter Schlüssel;Kostenvolumen;AnteilKostenvolumenNachAbgrenzung",
                        new FileHolder("NON_Med_Infra.csv"),
                        new ErrorCounter(),
                        Arrays.asList(
                                new DataImportCheck<KgpListMedInfra, String>(
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(180);
                                            i.setCostCenterNumber(s);
                                        },
                                        "Nummer der Kostenstelle ungültig"),
                                new DataImportCheck<KgpListMedInfra, String>(
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(180);
                                            i.setCostCenterText(s);
                                        },
                                        "Name der Kostenstelle ungültig"),
                                new DataImportCheck<KgpListMedInfra, String>(
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(180);
                                            i.setKeyUsed(s);
                                        },
                                        "Verwendeter Schlüssel ungültig"),
                                new DataImportCheck<KgpListMedInfra, Integer>(
                                        DataImportCheck::tryImportRoundedInteger,
                                        (i, s) -> {
                                            i.setCostTypeId(180);
                                            i.setAmount(s);
                                        },
                                        "Kostenvolumen nach Abgrenzung ungültig"),
                                new DataImportCheck<KgpListMedInfra, Double>(
                                        DataImportCheck::tryImportDoubleAndInteger,
                                        (i, s) -> {
                                            i.setCostTypeId(180);
                                            i.setPartCostVolumeMedStaffAfter(s);
                                        },
                                        "Kostenvolumen nach Abgrenzung ungültig")
                        ),
                        PeppCalcBasics::addMedInfraItem,
                        s -> s.deleteKgpMedInfraList(180),
                        KgpListMedInfra.class
                );
            //</editor-fold>
            case "peppcostcenter":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGPListCostCenter, PeppCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;Kostenvolumen;VollkräfteÄD;"
                        + "Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_11_12_13.csv"),
                        new ErrorCounter(),
                        Arrays.asList(
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportCostCenterId,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(11, 12, 13)"),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setCostCenterNumber,
                                        "ungültige Kostenstellennummer"),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setCostCenterText,
                                        "ungültiger Kostenstellentext"),
                                new DataImportCheck<KGPListCostCenter, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGPListCostCenter::setAmount,
                                        "Kostenvolumen ungültig"),
                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setFullVigorCnt,
                                        "[Anzahl VK ÄD] ungültig"),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setServiceKey,
                                        "ungültiger service"),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setServiceKeyDescription,
                                        "ungültige service Beschreibung"),
                                new DataImportCheck<KGPListCostCenter, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListCostCenter::setServiceSum,
                                        "Summer der Leistungseinheiten ungültig")),
                        PeppCalcBasics::addCostCenter,
                        PeppCalcBasics::deleteCostCenters,
                        KGPListCostCenter.class
                );
            //</editor-fold>
            case "peppcostcenter11":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGPListCostCenter, PeppCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;" +
                                "AnzahlVKÄDVor;AnzahlVKÄDNach;" +
                                "KostenvolumenÄDVor;KostenvolumenÄDNach;" +
                                "AnzahlVKPDVor;AnzahlVKPDNach;" +
                                "KostenvolumenPDVor;KostenvolumenPDNach;" +
                                "AnzahlVKFDVor;AnzahlVKFDNach;" +
                                "KostenvolumenFDVor;KostenvolumenFDNach;" +
                                "Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_11.csv"),
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("PEPP_COST_CENTER_11"),
                        Arrays.asList(
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportCostCenterId11,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(11)"),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setCostCenterNumber,
                                        "ungültige Kostenstellennummer"),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setCostCenterText,
                                        "ungültiger Kostenstellentext"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountMedStaffPre,
                                        "[Anzahl VK ÄD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountMedStaffAfter,
                                        "[Anzahl VK ÄD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeMedStaffPre,
                                        "[Anzahl VK FD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeMedStaffAfter,
                                        "[Anzahl VK FD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountCareServicePre,
                                        "[Anzahl VK PD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountCareServiceAfter,
                                        "[Anzahl VK PD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeCareServicePre,
                                        "[Kostenvolumen PD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeCareServiceAfter,
                                        "[Kostenvolumen PD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountFunctionalServicePre,
                                        "[Anzahl VK FD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountFunctionalServiceAfter,
                                        "[Anzahl VK FD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeFunctionalServicePre,
                                        "[Kostenvolumen FD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeFunctionalServiceAfter,
                                        "[Kostenvolumen FD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setServiceKey,
                                        "ungültiger service"),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setServiceKeyDescription,
                                        "ungültige service Beschreibung"),
                                new DataImportCheck<KGPListCostCenter, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListCostCenter::setServiceSum,
                                        "Summer der Leistungseinheiten ungültig")),
                        PeppCalcBasics::addCostCenter,
                        (s) -> s.deleteCostCenters(11),
                        KGPListCostCenter.class
                );
            //</editor-fold>
            case "peppcostcenter12":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGPListCostCenter, PeppCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;" +
                                "AnzahlVKÄDVor;AnzahlVKÄDNach;" +
                                "KostenvolumenÄDVor;KostenvolumenÄDNach;" +
                                "AnzahlVKPDVor;AnzahlVKPDNach;" +
                                "KostenvolumenPDVor;KostenvolumenPDNach;" +
                                "AnzahlVKFDVor;AnzahlVKFDNach;" +
                                "KostenvolumenFDVor;KostenvolumenFDNach;" +
                                "Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_12.csv"),
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("PEPP_COST_CENTER_12"),
                        Arrays.asList(
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportCostCenterId12,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(12)"),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setCostCenterNumber,
                                        "ungültige Kostenstellennummer"),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setCostCenterText,
                                        "ungültiger Kostenstellentext"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountMedStaffPre,
                                        "[Anzahl VK ÄD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountMedStaffAfter,
                                        "[Anzahl VK ÄD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeMedStaffPre,
                                        "[Anzahl VK FD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeMedStaffAfter,
                                        "[Anzahl VK FD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountCareServicePre,
                                        "[Anzahl VK PD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountCareServiceAfter,
                                        "[Anzahl VK PD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeCareServicePre,
                                        "[Kostenvolumen PD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeCareServiceAfter,
                                        "[Kostenvolumen PD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountFunctionalServicePre,
                                        "[Anzahl VK FD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountFunctionalServiceAfter,
                                        "[Anzahl VK FD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeFunctionalServicePre,
                                        "[Kostenvolumen FD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeFunctionalServiceAfter,
                                        "[Kostenvolumen FD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setServiceKey,
                                        "ungültiger service"),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setServiceKeyDescription,
                                        "ungültige service Beschreibung"),
                                new DataImportCheck<KGPListCostCenter, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListCostCenter::setServiceSum,
                                        "Summer der Leistungseinheiten ungültig")),
                        PeppCalcBasics::addCostCenter,
                        (s) -> s.deleteCostCenters(12),
                        KGPListCostCenter.class
                );
            //</editor-fold>
            case "peppcostcenter13":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGPListCostCenter, PeppCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;" +
                                "AnzahlVKÄDVor;AnzahlVKÄDNach;" +
                                "KostenvolumenÄDVor;KostenvolumenÄDNach;" +
                                "AnzahlVKPDVor;AnzahlVKPDNach;" +
                                "KostenvolumenPDVor;KostenvolumenPDNach;" +
                                "AnzahlVKFDVor;AnzahlVKFDNach;" +
                                "KostenvolumenFDVor;KostenvolumenFDNach;" +
                                "Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_13.csv"),
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("PEPP_COST_CENTER_13"),
                        Arrays.asList(
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportCostCenterId13,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(13)"),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setCostCenterNumber,
                                        "ungültige Kostenstellennummer"),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setCostCenterText,
                                        "ungültiger Kostenstellentext"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountMedStaffPre,
                                        "[Anzahl VK ÄD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountMedStaffAfter,
                                        "[Anzahl VK ÄD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeMedStaffPre,
                                        "[Anzahl VK FD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeMedStaffAfter,
                                        "[Anzahl VK FD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountCareServicePre,
                                        "[Anzahl VK PD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountCareServiceAfter,
                                        "[Anzahl VK PD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeCareServicePre,
                                        "[Kostenvolumen PD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeCareServiceAfter,
                                        "[Kostenvolumen PD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountFunctionalServicePre,
                                        "[Anzahl VK FD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCountFunctionalServiceAfter,
                                        "[Anzahl VK FD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeFunctionalServicePre,
                                        "[Kostenvolumen FD vor Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListCostCenter::setCostVolumeFunctionalServiceAfter,
                                        "[Kostenvolumen FD nach Abgrenzung] ungültig"),

                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setServiceKey,
                                        "ungültiger service"),
                                new DataImportCheck<KGPListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListCostCenter::setServiceKeyDescription,
                                        "ungültige service Beschreibung"),
                                new DataImportCheck<KGPListCostCenter, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListCostCenter::setServiceSum,
                                        "Summer der Leistungseinheiten ungültig")),
                        PeppCalcBasics::addCostCenter,
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
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("DRG_COST_CENTER"),
                        Arrays.asList(
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportCostCenterId,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(11, 12, 13)"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setCostCenterNumber,
                                        "ungültige Kostenstellennummer"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setCostCenterText,
                                        "ungültiger Kostenstellentext"),
                                new DataImportCheck<KGLListCostCenter, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListCostCenter::setAmount,
                                        "Kostenvolumen ungültig"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setFullVigorCnt,
                                        "[Anzahl VK ÄD] ungültig"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setServiceKey,
                                        "ungültiger service"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setServiceKeyDescription,
                                        "ungültige service Beschreibung"),
                                new DataImportCheck<KGLListCostCenter, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListCostCenter::setServiceSum,
                                        "Summer der Leistungseinheiten ungültig")),
                        DrgCalcBasics::addCostCenter,
                        DrgCalcBasics::deleteCostCenters_11_12_13,
                        KGLListCostCenter.class
                );
            //</editor-fold>
            case "drgcostcenter11":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGLListCostCenter, DrgCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;" +
                                "AnzahlVKÄDVor;AnzahlVKÄDNach;" +
                                "KostenvolumenÄDVor;KostenvolumenÄDNach;" +
                                "AnzahlVKPDVor;AnzahlVKPDNach;" +
                                "KostenvolumenPDVor;KostenvolumenPDNach;" +
                                "AnzahlVKFDVor;AnzahlVKFDNach;" +
                                "KostenvolumenFDVor;KostenvolumenFDNach;" +
                                "Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_11.csv"),
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("DRG_COST_CENTER_11"),
                        Arrays.asList(
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportCostCenterId11,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(11)"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setCostCenterNumber,
                                        "ungültige Kostenstellennummer"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setCostCenterText,
                                        "ungültiger Kostenstellentext"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountMedStaffPre,
                                        "Anzahl VK ÄD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountMedStaffAfter,
                                        "Anzahl VK ÄD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeMedStaffPre,
                                        "Kostenvolumen ÄD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeMedStaffAfter,
                                        "Kostenvolumen ÄD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountCareServicePre,
                                        "Anzahl VK PD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountCareServiceAfter,
                                        "Anzahl VK PD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeCareServicePre,
                                        "Kostenvolumen PD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeCareServiceAfter,
                                        "Kostenvolumen PD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountFunctionalServicePre,
                                        "Anzahl VK FD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountFunctionalServiceAfter,
                                        "Anzahl VK FD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeFunctionalServicePre,
                                        "Kostenvolumen FD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeFunctionalServiceAfter,
                                        "Kostenvolumen FD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setServiceKey,
                                        "ungültiger service"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setServiceKeyDescription,
                                        "ungültige service Beschreibung"),
                                new DataImportCheck<KGLListCostCenter, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListCostCenter::setServiceSum,
                                        "Summer der Leistungseinheiten ungültig")),
                        DrgCalcBasics::addCostCenter,
                        s -> s.deleteCostCenters(11),
                        KGLListCostCenter.class
                );
            //</editor-fold>
            case "drgcostcenter12":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGLListCostCenter, DrgCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;" +
                                "AnzahlVKÄDVor;AnzahlVKÄDNach;" +
                                "KostenvolumenÄDVor;KostenvolumenÄDNach;" +
                                "AnzahlVKPDVor;AnzahlVKPDNach;" +
                                "KostenvolumenPDVor;KostenvolumenPDNach;" +
                                "AnzahlVKFDVor;AnzahlVKFDNach;" +
                                "KostenvolumenFDVor;KostenvolumenFDNach;" +
                                "Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_12.csv"),
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("DRG_COST_CENTER_12"),
                        Arrays.asList(
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportCostCenterId12,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(12)"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setCostCenterNumber,
                                        "ungültige Kostenstellennummer"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setCostCenterText,
                                        "ungültiger Kostenstellentext"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountMedStaffPre,
                                        "Anzahl VK ÄD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountMedStaffAfter,
                                        "Anzahl VK ÄD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeMedStaffPre,
                                        "Kostenvolumen ÄD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeMedStaffAfter,
                                        "Kostenvolumen ÄD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountCareServicePre,
                                        "Anzahl VK PD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountCareServiceAfter,
                                        "Anzahl VK PD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeCareServicePre,
                                        "Kostenvolumen PD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeCareServiceAfter,
                                        "Kostenvolumen PD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountFunctionalServicePre,
                                        "Anzahl VK FD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountFunctionalServiceAfter,
                                        "Anzahl VK FD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeFunctionalServicePre,
                                        "Kostenvolumen FD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeFunctionalServiceAfter,
                                        "Kostenvolumen FD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setServiceKey,
                                        "ungültiger service"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setServiceKeyDescription,
                                        "ungültige service Beschreibung"),
                                new DataImportCheck<KGLListCostCenter, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListCostCenter::setServiceSum,
                                        "Summer der Leistungseinheiten ungültig")),
                        DrgCalcBasics::addCostCenter,
                        s -> s.deleteCostCenters(12),
                        KGLListCostCenter.class
                );
            //</editor-fold>
            case "drgcostcenter13":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenter">
                return new DataImporter<KGLListCostCenter, DrgCalcBasics>(
                        "Kostenstellengruppe;Kostenstellennummer;Kostenstellenname;" +
                                "AnzahlVKÄDVor;AnzahlVKÄDNach;" +
                                "KostenvolumenÄDVor;KostenvolumenÄDNach;" +
                                "AnzahlVKPDVor;AnzahlVKPDNach;" +
                                "KostenvolumenPDVor;KostenvolumenPDNach;" +
                                "AnzahlVKFDVor;AnzahlVKFDNach;" +
                                "KostenvolumenFDVor;KostenvolumenFDNach;" +
                                "Leistungsschlüssel;Beschreibung;SummeLeistungseinheiten",
                        new FileHolder("Kostenstellengruppe_13.csv"),
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("DRG_COST_CENTER_13"),
                        Arrays.asList(
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportCostCenterId13,
                                        (i, s) -> i.setCostCenterId(Integer.parseInt(s)),
                                        "Keine zulässige Kostenstellengruppe(13)"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setCostCenterNumber,
                                        "ungültige Kostenstellennummer"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setCostCenterText,
                                        "ungültiger Kostenstellentext"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountMedStaffPre,
                                        "Anzahl VK ÄD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountMedStaffAfter,
                                        "Anzahl VK ÄD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeMedStaffPre,
                                        "Kostenvolumen ÄD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeMedStaffAfter,
                                        "Kostenvolumen ÄD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountCareServicePre,
                                        "Anzahl VK PD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountCareServiceAfter,
                                        "Anzahl VK PD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeCareServiceAfter,
                                        "Kostenvolumen PD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeCareServiceAfter,
                                        "Kostenvolumen PD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountFunctionalServicePre,
                                        "Anzahl VK FD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCountFunctionalServiceAfter,
                                        "Anzahl VK FD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeFunctionalServicePre,
                                        "Kostenvolumen FD vor Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenter::setCostVolumeFunctionalServiceAfter,
                                        "Kostenvolumen FD nach Abgrenzung"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setServiceKey,
                                        "ungültiger service"),
                                new DataImportCheck<KGLListCostCenter, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenter::setServiceKeyDescription,
                                        "ungültige service Beschreibung"),
                                new DataImportCheck<KGLListCostCenter, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListCostCenter::setServiceSum,
                                        "Summer der Leistungseinheiten ungültig")),
                        DrgCalcBasics::addCostCenter,
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
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("PEPP_STATION_SERVICE_COST"),
                        Arrays.asList(
                                new DataImportCheck<KGPListStationServiceCost, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListStationServiceCost::setCostCenterNumber,
                                        "Nummer der Kostenstelle ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListStationServiceCost::setStation,
                                        "Name der Station ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListStationServiceCost::setPsyPvMapping,
                                        "Eindeutige Zuordnung nach Psych-PV* (A, S, G, KJP, P) ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListStationServiceCost::setBedCnt,
                                        "Summer der Leistungseinheiten ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportOccupancyType,
                                        KGPListStationServiceCost::setOccupancy,
                                        "Belegung (voll- und/oder teilstationär) ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListStationServiceCost::setRegularCareDays,
                                        "Summe Pflegetage Regelbehandlung ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListStationServiceCost::setRegularWeight,
                                        "Summe Gewichtungspunkte** Regelbehandlung ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListStationServiceCost::setIntensiveCareDays,
                                        "Summe Pflegetage Intensivbehandlung ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListStationServiceCost::setIntensiveWeight,
                                        "Summe Gewichtungspunkte** Intensivbehandlung ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListStationServiceCost::setMedicalServiceCnt,
                                        "VK Ärztlicher Dienst ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListStationServiceCost::setNursingServiceCnt,
                                        "VK Pflegedienst/Erziehungsdienst ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListStationServiceCost::setPsychologistCnt,
                                        "VK Psychologen ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListStationServiceCost::setSocialWorkerCnt,
                                        "VK Sozialarbeiter/Sozial-/Heil-pädagogen ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListStationServiceCost::setSpecialTherapistCnt,
                                        "VK Spezialtherapeuten ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGPListStationServiceCost::setFunctionalServiceCnt,
                                        "VK med.-techn. Dienst/Funktionsdienst ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListStationServiceCost::setMedicalServiceAmount,
                                        "Kosten Ärztlicher Dienst ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListStationServiceCost::setNursingServiceAmount,
                                        "Kosten Pflegedienst/Erziehungsdienst ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListStationServiceCost::setPsychologistAmount,
                                        "Kosten Psychologen ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListStationServiceCost::setSocialWorkerAmount,
                                        "Kosten Sozialarbeiter/Sozial-/Heil-pädagogen ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListStationServiceCost::setSpecialTherapistAmount,
                                        "Kosten Spezialtherapeuten ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListStationServiceCost::setFunctionalServiceAmount,
                                        "Kosten med.-techn. Dienst/Funktionsdienst ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListStationServiceCost::setMedicalInfrastructureAmount,
                                        "Kosten med. Infrastruktur ungültig"),
                                new DataImportCheck<KGPListStationServiceCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListStationServiceCost::setNonMedicalInfrastructureAmount,
                                        "Kosten nicht med. Infrastruktur ungültig")),
                        PeppCalcBasics::addStationServiceCost,
                        PeppCalcBasics::clearStationServiceCosts,
                        KGPListStationServiceCost.class
                );
            //</editor-fold>
            case "pepptherapy":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter therapy">
                return new DataImporter<KGPListTherapy, PeppCalcBasics>(
                        "KST-Gruppe;Leistungsinhalt der Kostenstelle;Fremdvergabe;"
                        + "Leistungsschlüssel;KoArtG 1 Summe Leistungseinheiten;KoArtG 1 Personalkosten;"
                        + "KoArtG 3a Summe Leistungseinheiten;KoArtG 3a Personalkosten;"
                        + "KoArtG 2 Summe Leistungseinheiten;KoArtG 2 Personalkosten;"
                        + "KoArtG 3b Summe Leistungseinheiten;KoArtG 3b Personalkosten;"
                        + "KoArtG 3c Summe Leistungseinheiten;KoArtG 3c Personalkosten;"
                        + "KoArtG 3 Summe Leistungseinheiten;KoArtG 3 Personalkosten",
                        new FileHolder("Therapeutischer_Bereich_.csv"),
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("PEPP_THERAPY"),
                        Arrays.asList(
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListTherapy::setCostCenterId,
                                        "Keine zulässige KST-Gruppe (23-26)"),
                                new DataImportCheck<KGPListTherapy, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListTherapy::setCostCenterText,
                                        "Ungültige Zeichenkette"),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportFremdvergabe,
                                        KGPListTherapy::setExternalService,
                                        "Keine zulässige Leistungserbringung 'Keine, Teilweise, Vollständige Fremdvergabe'"),
                                new DataImportCheck<KGPListTherapy, String>(
                                        DataImportCheck::tryImportString,
                                        KGPListTherapy::setKeyUsed,
                                        "Kein gültiger Leistungsschlüssel"),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListTherapy::setServiceUnitsCt1,
                                        "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 1"),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGPListTherapy::setPersonalCostCt1,
                                        "Ungültiger Wert für Personalkosten KoArtGr 1"),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListTherapy::setServiceUnitsCt3a,
                                        "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 3a"),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGPListTherapy::setPersonalCostCt3a,
                                        "Ungültiger Wert für Personalkosten KoArtGr 3a"),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListTherapy::setServiceUnitsCt2,
                                        "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 2"),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGPListTherapy::setPersonalCostCt2,
                                        "Ungültiger Wert für Personalkosten KoArtGr 2"),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListTherapy::setServiceUnitsCt3b,
                                        "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 3b"),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGPListTherapy::setPersonalCostCt3b,
                                        "Ungültiger Wert für Personalkosten KoArtGr 3b"),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListTherapy::setServiceUnitsCt3c,
                                        "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 3c"),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGPListTherapy::setPersonalCostCt3c,
                                        "Ungültiger Wert für Personalkosten KoArtGr 3c"),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGPListTherapy::setServiceUnitsCt3,
                                        "Ungültiger Wert für Summe Leistungseinheiten KoArtGr 3"),
                                new DataImportCheck<KGPListTherapy, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGPListTherapy::setPersonalCostCt3,
                                        "Ungültiger Wert für Personalkosten KoArtGr 3")),
                        PeppCalcBasics::addTherapy,
                        PeppCalcBasics::clearTheapies,
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
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("DRG_NORMAL_WARD"),
                        Arrays.asList(
                                new DataImportCheck<KGLListCostCenterCost, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenterCost::setCostCenterNumber,
                                        "Nummer der Kostenstelle ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenterCost::setCostCenterText,
                                        "Name der Kostenstelle ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenterCost::setDepartmentKey,
                                        "FABSchlüssel ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenterCost::setDepartmentAssignment,
                                        "BelegungFAB ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListCostCenterCost::setBedCnt,
                                        "Bettenzahl ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListCostCenterCost::setCareDays,
                                        "Pflegetage ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListCostCenterCost::setPprMinutes,
                                        "PPRMinuten ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListCostCenterCost::setPprWeight,
                                        "Zusaetliche Gewichtung ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenterCost::setMedicalServiceCnt,
                                        "AerztlicherDienstVK ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenterCost::setNursingServiceCnt,
                                        "PflegedienstVK ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenterCost::setFunctionalServiceCnt,
                                        "FunktionsdienstVK ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListCostCenterCost::setMedicalServiceAmount,
                                        "AerztlicherDienstKostenstelle ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListCostCenterCost::setNursingServiceAmount,
                                        "PflegedienstKostenstelle ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListCostCenterCost::setFunctionalServiceAmount,
                                        "FunktionsdienstKostenstelle ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListCostCenterCost::setOverheadsMedicine,
                                        "ArzneimittelKostenstelle ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListCostCenterCost::setOverheadsMedicalGoods,
                                        "medSachbedarfKostenstelle ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListCostCenterCost::setMedicalInfrastructureCost,
                                        "medInfraKostenstelle ungültig"),
                                new DataImportCheck<KGLListCostCenterCost, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListCostCenterCost::setNonMedicalInfrastructureCost,
                                        "nichtMedInfraKostenstelle ungültig")
                        ),
                        DrgCalcBasics::addCostCenterCost,
                        DrgCalcBasics::clearCostCenterCosts,
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
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("DRG_INTENSIVE"),
                        Arrays.asList(
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListIntensivStroke::setCostCenterText,
                                        "Kein zulässiger Intensivstationsname:  "),
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListIntensivStroke::setDepartmentAssignment,
                                        "Kein zulässiger Abteilungsname"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListIntensivStroke::setBedCnt,
                                        "Bettenzahl ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListIntensivStroke::setCaseCnt,
                                        "Fallzahl ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Boolean>(
                                        DataImportCheck::tryImportBoolean,
                                        KGLListIntensivStroke::setOps8980,
                                        "Mindestmerkmale OPS 8-980 erfüllt"),
                                new DataImportCheck<KGLListIntensivStroke, Boolean>(
                                        DataImportCheck::tryImportBoolean,
                                        KGLListIntensivStroke::setOps898f,
                                        "Mindestmerkmale OPS 8-98f erfüllt"),
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListIntensivStroke::setMinimumCriteriaPeriod,
                                        "Mindestmerkmale nur erfüllt im Zeitabschnitt"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListIntensivStroke::setIntensivHoursWeighted,
                                        "Summe gewichtete Intensivstunden"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListIntensivStroke::setIntensivHoursNotweighted,
                                        "Summe ungewichtete Intensivstunden"),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListIntensivStroke::setWeightMinimum,
                                        "Minimum"),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListIntensivStroke::setWeightMaximum,
                                        "Maximum"),
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListIntensivStroke::setWeightDescription,
                                        "Mindestmerkmale nur erfüllt im Zeitabschnitt"),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListIntensivStroke::setMedicalServiceCnt,
                                        "VK Ärztlicher Dienst ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListIntensivStroke::setNursingServiceCnt,
                                        "VK Pflegedienst ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListIntensivStroke::setFunctionalServiceCnt,
                                        "VK Funktionsdienst ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setMedicalServiceCost,
                                        "Kosten Ärztlicher Dienst ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setNursingServiceCost,
                                        "Kosten Funktionsdienst ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setFunctionalServiceCost,
                                        "Kosten Funktionsdienst ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setOverheadsMedicine,
                                        "Kosten GK Arzneimittel ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setOverheadMedicalGoods,
                                        "Kosten GK med Sachbedarf ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setMedicalInfrastructureCost,
                                        "Kosten med. Infrastruktur ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setNonMedicalInfrastructureCost,
                                        "Kosten nicht med. Infrastuktur ungültig")
                        ),
                        DrgCalcBasics::addIntensive,
                        DrgCalcBasics::clearIntensive,
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
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("DRG_STROKE_UNIT"),
                        Arrays.asList(
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListIntensivStroke::setCostCenterText,
                                        "Kein zulässiger Intensivstationsname:  "),
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListIntensivStroke::setDepartmentAssignment,
                                        "Kein zulässiger Abteilungsname"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListIntensivStroke::setBedCnt,
                                        "Bettenzahl ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListIntensivStroke::setCaseCnt,
                                        "Fallzahl ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Boolean>(
                                        DataImportCheck::tryImportBoolean,
                                        KGLListIntensivStroke::setOps8981,
                                        "Mindestmerkmale OPS 8-981 erfüllt"),
                                new DataImportCheck<KGLListIntensivStroke, Boolean>(
                                        DataImportCheck::tryImportBoolean,
                                        KGLListIntensivStroke::setOps898b,
                                        "Mindestmerkmale OPS 8-98b erfüllt"),
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListIntensivStroke::setMinimumCriteriaPeriod,
                                        "Mindestmerkmale nur erfüllt im Zeitabschnitt"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListIntensivStroke::setIntensivHoursWeighted,
                                        "Summe gewichtete Intensivstunden"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListIntensivStroke::setIntensivHoursNotweighted,
                                        "Summe ungewichtete Intensivstunden"),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListIntensivStroke::setWeightMinimum,
                                        "Minimum"),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListIntensivStroke::setWeightMaximum,
                                        "Maximum"),
                                new DataImportCheck<KGLListIntensivStroke, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListIntensivStroke::setWeightDescription,
                                        "Mindestmerkmale nur erfüllt im Zeitabschnitt"),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListIntensivStroke::setMedicalServiceCnt,
                                        "VK Ärztlicher Dienst ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListIntensivStroke::setNursingServiceCnt,
                                        "VK Pflegedienst ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListIntensivStroke::setFunctionalServiceCnt,
                                        "VK Funktionsdienst ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setMedicalServiceCost,
                                        "Kosten Ärztlicher Dienst ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setNursingServiceCost,
                                        "Kosten Funktionsdienst ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setFunctionalServiceCost,
                                        "Kosten Funktionsdienst ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setOverheadsMedicine,
                                        "Kosten GK Arzneimittel ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setOverheadMedicalGoods,
                                        "Kosten GK med Sachbedarf ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setMedicalInfrastructureCost,
                                        "Kosten med. Infrastruktur ungültig"),
                                new DataImportCheck<KGLListIntensivStroke, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListIntensivStroke::setNonMedicalInfrastructureCost,
                                        "Kosten nicht med. Infrastuktur ungültig")
                        ),
                        DrgCalcBasics::addStroke,
                        DrgCalcBasics::clearStroke,
                        KGLListIntensivStroke.class
                );
            //</editor-fold>
            case "drgmedinfra":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Med Infra">
                return new DataImporter<KGLListMedInfra, DrgCalcBasics>(
                        "KostenstelleNummer;KostenstelleText;Schlüssel;Kostenvolumen;AnteilKostenvolumenNachAbgrenzung",
                        new FileHolder("MedInfra.csv"),
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("DRG_MED_INFRA"),
                        Arrays.asList(
                                new DataImportCheck<KGLListMedInfra, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListMedInfra::setCostCenterNumber,
                                        "Kostenstellennummer:  "),
                                new DataImportCheck<KGLListMedInfra, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListMedInfra::setCostCenterText,
                                        "Kostenstellenname"),
                                new DataImportCheck<KGLListMedInfra, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListMedInfra::setKeyUsed,
                                        "Schlüssel"),
                                new DataImportCheck<KGLListMedInfra, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListMedInfra::setAmount,
                                        "Kostenvolumen"),
                                new DataImportCheck<KGLListMedInfra, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListMedInfra::setAmountAfter,
                                        "Kostenvolumen nach Abgrenzung")
                        ),
                        DrgCalcBasics::addMedInfra,
                        DrgCalcBasics::clearMedInfra,
                        KGLListMedInfra.class
                );
            //</editor-fold>
            case "drgnonmedinfra":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Non-Med Infra">
                return new DataImporter<KGLListMedInfra, DrgCalcBasics>(
                        "KostenstelleNummer;KostenstelleText;Schlüssel;Kostenvolumen;AnteilKostenvolumenNachAbgrenzung",
                        new FileHolder("NonMedInfra.csv"),
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("DRG_NON_MED_INFRA"),
                        Arrays.asList(
                                new DataImportCheck<KGLListMedInfra, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListMedInfra::setCostCenterNumber,
                                        "Kostenstellennummer:  "),
                                new DataImportCheck<KGLListMedInfra, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListMedInfra::setCostCenterText,
                                        "Kostenstellenname"),
                                new DataImportCheck<KGLListMedInfra, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListMedInfra::setKeyUsed,
                                        "Schlüssel"),
                                new DataImportCheck<KGLListMedInfra, Integer>(
                                        DataImportCheck::tryImportDoubleAsInt,
                                        KGLListMedInfra::setAmount,
                                        "Kostenvolumen"),
                                new DataImportCheck<KGLListMedInfra, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListMedInfra::setAmountAfter,
                                        "Kostenvolumen nach Abgrenzung")
                        ),
                        DrgCalcBasics::addNonMedInfra,
                        DrgCalcBasics::clearNonMedInfra,
                        KGLListMedInfra.class
                );
            //</editor-fold>
            case "drglaboratory":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter Laboratory">
                return new DataImporter<KGLListRadiologyLaboratory, DrgCalcBasics>(
                        "KostenstelleNummer;KostenstelleName;Leistungsbereich;Leistungsdokumentation;"
                                + "Beschreibung;LeistungsvolumenVor;KostenvolumenVor;KostenvolumenNach",
                        new FileHolder("Laboratory.csv"),
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("DRG_LABORATORY"),
                        Arrays.asList(
                                new DataImportCheck<KGLListRadiologyLaboratory, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListRadiologyLaboratory::setCostCenterNumber,
                                        "KostenstelleNummer"),
                                new DataImportCheck<KGLListRadiologyLaboratory, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListRadiologyLaboratory::setCostCenterText,
                                        "KostenstelleName"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        DataImportCheck::tryImportLabServiceArea,
                                        KGLListRadiologyLaboratory::setServiceArea,
                                        "Leistungsbereich"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        DataImportCheck::tryImportServiceDocType,
                                        KGLListRadiologyLaboratory::setService,
                                        "Leistungsdokumentation"),
                                new DataImportCheck<KGLListRadiologyLaboratory, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListRadiologyLaboratory::setDescription,
                                        "Beschreibung"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListRadiologyLaboratory::setServiceVolumePre,
                                        "LeistungsvolumenVor"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListRadiologyLaboratory::setAmountPre,
                                        "KostenvolumenVor"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListRadiologyLaboratory::setAmountPost,
                                        "KostenvolumenNach")
                        ),
                        (s, t) -> s.addRadiologyLaboratories(t, 10),
                        DrgCalcBasics::clearLaboratories,
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
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("DRG_LABORATORY"),
                        Arrays.asList(
                                new DataImportCheck<KGLListRadiologyLaboratory, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListRadiologyLaboratory::setCostCenterNumber,
                                        "KostenstelleNummer"),
                                new DataImportCheck<KGLListRadiologyLaboratory, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListRadiologyLaboratory::setCostCenterText,
                                        "KostenstelleName"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        DataImportCheck::tryImportServiceDocType,
                                        KGLListRadiologyLaboratory::setService,
                                        "Leistungsdokumentation"),
                                new DataImportCheck<KGLListRadiologyLaboratory, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListRadiologyLaboratory::setDescription,
                                        "Beschreibung"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListRadiologyLaboratory::setServiceVolumePre,
                                        "LeistungsvolumenVor"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListRadiologyLaboratory::setServiceVolumePost,
                                        "LeistungsvolumenNach"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListRadiologyLaboratory::setAmountPre,
                                        "KostenvolumenVor"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListRadiologyLaboratory::setAmountPost,
                                        "KostenvolumenNach"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListRadiologyLaboratory::setCountMedStaffPre,
                                        "Anzahl VK ÄD vor Abgrenzung"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListRadiologyLaboratory::setCountMedStaffAfter,
                                        "Anzahl VK ÄD nach Abgrenzung"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListRadiologyLaboratory::setCostVolumeMedStaffPre,
                                        "Kostenvolumen ÄD vor Abgrenzung"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListRadiologyLaboratory::setCostVolumeMedStaffAfter,
                                        "Kostenvolumen ÄD nach Abgrenzung"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListRadiologyLaboratory::setCountFunctionalServicePre,
                                        "Anzahl VK FD vor Abgrenzung"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListRadiologyLaboratory::setCountFunctionalServiceAfter,
                                        "Anzahl VK FD nach Abgrenzung"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListRadiologyLaboratory::setCostVolumeFunctionalServicePre,
                                        "Kostenvolumen FD vor Abgrenzung"),
                                new DataImportCheck<KGLListRadiologyLaboratory, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListRadiologyLaboratory::setCostVolumeFunctionalServiceAfter,
                                        "Kostenvolumen FD nach Abgrenzung")

                        ),
                        (s, t) -> s.addRadiologyLaboratories(t, 9),
                        DrgCalcBasics::clearRadiologies,
                        KGLListRadiologyLaboratory.class
                );
            //</editor-fold>
            case "costcenteropan":
                //<editor-fold defaultstate="collapsed" desc="new DataImporter costCenterOpAn">
                return new DataImporter<KGLListCostCenterOpAn, DrgCalcBasics>(
                        "LaufendeNr;KostenstellenNr;KostenstellenName;Standort;Anzahl_VK_ÄD;Kostenvolumen_ÄD;" +
                                "Anzahl_VK_FD;Kostenvolumen_FD;Gesamtzeit",
                        new FileHolder("Operation.csv"),
                        new ErrorCounter(), //errorCounter.obtainErrorCounter("DRG_costCenterOpAn"),
                        Arrays.asList(
                                new DataImportCheck<KGLListCostCenterOpAn, Integer>(
                                        DataImportCheck::tryImportInteger,
                                        KGLListCostCenterOpAn::setRunningNumber,
                                        "LaufendeNr"),
                                new DataImportCheck<KGLListCostCenterOpAn, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenterOpAn::setCostCenterNumber,
                                        "KostenstellenNr"),
                                new DataImportCheck<KGLListCostCenterOpAn, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenterOpAn::setCostCenterText,
                                        "KostenstellenName"),
                                new DataImportCheck<KGLListCostCenterOpAn, String>(
                                        DataImportCheck::tryImportString,
                                        KGLListCostCenterOpAn::setLocation,
                                        "Standort"),
                                new DataImportCheck<KGLListCostCenterOpAn, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenterOpAn::setCountMedStaff,
                                        "Anzahl_VK_ÄD"),
                                new DataImportCheck<KGLListCostCenterOpAn, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenterOpAn::setCostVolumeMedStaff,
                                        "Kostenvolumen_ÄD"),
                                new DataImportCheck<KGLListCostCenterOpAn, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenterOpAn::setCountFunctionalService,
                                        "Anzahl_VK_FD"),
                                new DataImportCheck<KGLListCostCenterOpAn, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenterOpAn::setCostVolumeFunctionalService,
                                        "Kostenvolumen_FD"),
                                new DataImportCheck<KGLListCostCenterOpAn, Double>(
                                        DataImportCheck::tryImportDouble,
                                        KGLListCostCenterOpAn::setServiceTime,
                                        "Gesamtzeit")

                        ),
                        DrgCalcBasics::addCostCenterOpAn,
                        DrgCalcBasics::clearCostCenterOpAn,
                        KGLListCostCenterOpAn.class
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
//    private DataImporter(String headLine, FileHolder fileHolder, ErrorCounter errorCounter,
//            List<DataImportCheck<T, ?>> checker, BiConsumer<S, T> dataSink, Consumer<S> clearList, Class<T> clazz) {
//        this.headLine = headLine;
//        this.fileHolder = fileHolder;
//        this.errorCounter = errorCounter;
//        this.checkers = checker;
//        this.dataSink = dataSink;
//        this.clazz = clazz;
//        this.clearList = clearList;
//    }

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
            checker.tryImport(item, data[i++], errorCounter);
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
        errorCounter.reset();
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

//    private final FileHolder fileHolder;
//    private final ErrorCounter errorCounter;
//    private final String headLine;
//    private final List<DataImportCheck<T, ?>> checkers;
//    private final BiConsumer<S, T> dataSink;
//    private boolean showJournal = false;
//    private final Class<T> clazz;
//    private final Consumer<S> clearList;
}
