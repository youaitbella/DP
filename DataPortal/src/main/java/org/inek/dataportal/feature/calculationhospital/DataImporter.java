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
import org.inek.dataportal.entities.calc.psy.KGPListMedInfra;
import org.inek.dataportal.entities.calc.psy.PeppCalcBasics;
import org.inek.dataportal.entities.iface.BaseIdValue;
import org.inek.dataportal.helper.BeanValidator;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.StringUtil;

/**
 *
 * @author kunkelan
 */
public class DataImporter<T extends BaseIdValue> implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(DataImporter.class.getName());

    public static DataImporter obtainDataImporter(String importer) {
        switch (importer.toLowerCase()) {
            case "peppmedinfra":
                return new DataImporter<KGPListMedInfra>(
                        "Nummer der Kostenstelle;Name der Kostenstelle;Verwendeter Schlüssel;Kostenvolumen",
                        new FileHolder("Med_Infra.csv"),
                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                        Arrays.asList(
                                new DataImportCheck<KGPListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(170);
                                            i.setCostCenterNumber(s);
                                        },
                                        "Nummer der Kostenstelle ungültig: "),
                                new DataImportCheck<KGPListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(170);
                                            i.setCostCenterText(s);
                                        },
                                        "Name der Kostenstelle ungültig: "),
                                new DataImportCheck<KGPListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(170);
                                            i.setKeyUsed(s);
                                        },
                                        "Verwendeter Schlüssel ungültig: "),
                                new DataImportCheck<KGPListMedInfra, Integer>(
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
                        KGPListMedInfra.class
                );
            case "peppnonmedinfra":
                return new DataImporter<KGPListMedInfra>(
                        "Nummer der Kostenstelle;Name der Kostenstelle;Verwendeter Schlüssel;Kostenvolumen",
                        new FileHolder("NON_Med_Infra.csv"),
                        ErrorCounter.obtainErrorCounter("PEPP_NON_MED_INFRA"),
                        Arrays.asList(
                                new DataImportCheck<KGPListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_NON_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(180);
                                            i.setCostCenterNumber(s);
                                        },
                                        "Nummer der Kostenstelle ungültig: "),
                                new DataImportCheck<KGPListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_NON_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(180);
                                            i.setCostCenterText(s);
                                        },
                                        "Name der Kostenstelle ungültig: "),
                                new DataImportCheck<KGPListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_NON_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> {
                                            i.setCostTypeId(180);
                                            i.setKeyUsed(s);
                                        },
                                        "Verwendeter Schlüssel ungültig: "),
                                new DataImportCheck<KGPListMedInfra, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_NON_MED_INFRA"),
                                        DataImportCheck::tryImportRoundedInteger,
                                        (i, s) -> {
                                            i.setCostTypeId(180);
                                            i.setAmount(s);
                                        },
                                        "Kostenvolumen ungültig: ")
                        ),
                        (s, t) -> s.addMedInfraItem(t),
                        KGPListMedInfra.class
                );

            default:
                throw new IllegalArgumentException("unknown importer " + importer);
        }
    }
//    ),
//
//    PEPP_THERAPY(
//
//            )

    DataImporter(String headLine, FileHolder fileHolder, ErrorCounter errorCounter,
            List<DataImportCheck<T, ?>> checker, BiConsumer<PeppCalcBasics, T> dataSink, Class<T> clazz) {
        this.headLine = headLine;
        this.fileHolder = fileHolder;
        this.errorCounter = errorCounter;
        this.checkers = checker;
        //this.listToFill = listToFill;
        this.dataSink = dataSink;
        this.clazz = clazz;
    }

    public void uploadNoticesPepp(PeppCalcBasics calcBasics) {
        try {
            resetCounter();

            int cntColumns = headLine.split(";").length;

            Scanner scanner = new Scanner(fileHolder.getFile().getInputStream());

            if (!scanner.hasNextLine()) {
                // empty file will be skipped silently
                return;
            }

            String header = scanner.nextLine();
            if (!headLine.equals(header)) {
                throw new IllegalArgumentException("Datei hat falsches Format, erwartete Kopfzeile " + headLine
                        + " aber geliefert " + header);
            }

            while (scanner.hasNextLine()) {
                String line = Utils.convertFromUtf8(scanner.nextLine());
                T item = readLine(line, cntColumns, calcBasics);
                dataSink.accept(calcBasics, item);
            }

        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private T readLine(String line, int cntColumns, PeppCalcBasics calcBasics) {

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
        }
        return item;
    }

    private T createNewItem(T item, PeppCalcBasics calcBasics) throws IllegalAccessException, InstantiationException {
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
        Utils.downloadText(headLine + "\n", fileHolder.getFileName());
    }

    public String getMessage() {
        return errorCounter.getMessage();
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

    private FileHolder fileHolder;
    private ErrorCounter errorCounter;
    private String headLine;
    private List<DataImportCheck<T, ?>> checkers;
    private final BiConsumer<PeppCalcBasics, T> dataSink;
//    private Function<PeppCalcBasics, List<T>> listToFill;
    private Class<T> clazz;

}
