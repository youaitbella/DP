package org.inek.dataportal.feature.calculationhospital;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class DataImporter<T extends BaseIdValue> {
    private static final Logger LOGGER = Logger.getLogger(DataImporter.class.getName());

    public static DataImporter obtainDataImporter(String importer) {
        switch (importer.toLowerCase()) {
            case "peppmedinfra":
                return new DataImporter<KGPListMedInfra>(
                        "Nummer der Kostenstelle;Name der Kostenstelle;Verwendeter Schlüssel;Kostenvolumen",
                        new FileHolder(),
                        Arrays.asList(
                                new DataImportCheck<KGPListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterNumber(s),
                                        "Nummer der Kostenstelle ungültig: "),
                                new DataImportCheck<KGPListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setCostCenterText(s),
                                        "Name der Kostenstelle ungültig: "),
                                new DataImportCheck<KGPListMedInfra, String>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportString,
                                        (i, s) -> i.setKeyUsed(s),
                                        "Verwendeter Schlüssel ungültig: "),
                                new DataImportCheck<KGPListMedInfra, Integer>(
                                        ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"),
                                        DataImportCheck::tryImportRoundedInteger,
                                        (i, s) -> i.setAmount(s),
                                        "Kostenvolumen ungültig: ")
                        ),
                        s -> s.getKgpMedInfraList(),
                        KGPListMedInfra.class
                );
            default: throw new IllegalArgumentException("unknown importer " + importer);
        }
    }
//    ),
//    
//    PEPP_THERAPY(
//            
//            )

    DataImporter(String headLine, FileHolder fileHolder, List<DataImportCheck<T, ?>> checker, Function<PeppCalcBasics, List<T>> listToFill, Class<T> clazz) {
        this.headLine = headLine;
        this.fileHolder = fileHolder;
        this.checkers = checker;
        this.listToFill = listToFill;
        this.clazz = clazz;
    }

    public void uploadNoticesPepp(PeppCalcBasics calcBasics) {
        try {
            resetCounter();
            int cntColumns = headLine.split(";").length;
            
            Scanner scanner = new Scanner( fileHolder.getFile().getInputStream());
            if (!scanner.hasNextLine()) {
                // skip header and eventually quit when nothing to import
                return;
            }
            
            List<T> items = listToFill.apply(calcBasics);
            
            while (scanner.hasNextLine()) {
                String line = Utils.convertFromUtf8(scanner.nextLine());
                T item = readLine(line, cntColumns, calcBasics);
                items.add(item);
            }
            
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }
    }

    private T readLine(String line, int cntColumns, PeppCalcBasics calcBasics ) {
        
        T item = null;
        try {
            if (line.endsWith(";")) {
                line = line + " ";
            }
            String[] data = StringUtil.splitAtUnquotedSemicolon(line);
            if (data.length != cntColumns) {
                throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
            }
            item = clazz.newInstance();
            item.setBaseInformationId(calcBasics.getId());
            
            int i = 0;
            for (DataImportCheck<T, ?> checker : checkers) {
                checker.tryImport(item, data[i++]);
            }
                        
            String validateText = BeanValidator.validateData(item);
            if (!validateText.isEmpty()) {
                throw new IllegalArgumentException(validateText);
            }
            
        } catch (InstantiationException | IllegalAccessException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException("can not instantiate type " + clazz.getSimpleName());
        }
        return item;
    }

    private void resetCounter() {
        checkers.get(0).resetCounter();
    }

    public String getHeadLine() {
        return headLine;
    }

    private FileHolder fileHolder;
    private String headLine;
    private List<DataImportCheck<T, ?>> checkers;
    private Function<PeppCalcBasics, List<T>> listToFill;
    private Class<T> clazz;

}
