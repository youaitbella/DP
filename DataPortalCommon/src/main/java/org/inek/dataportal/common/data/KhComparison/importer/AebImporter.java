/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.KhComparison.importer;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.inek.dataportal.common.data.KhComparison.entities.*;
import org.inek.dataportal.common.exceptions.FormulaInCellException;
import org.inek.dataportal.common.exceptions.IntegerInDoubleCellException;
import org.inek.dataportal.common.exceptions.StringInNumericCellException;
import org.inek.dataportal.common.helper.excelimport.CellImportHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author lautenti
 */
public class AebImporter {

    public static final Logger LOGGER = Logger.getLogger(AebImporter.class.getName());

    private static final String PAGE_E1_1 = "E1.1 V";
    private static final String PAGE_E1_2 = "E1.2 V";
    private static final String PAGE_E2 = "E2 V";
    private static final String PAGE_E3_1 = "E3.1 V";
    private static final String PAGE_E3_2 = "E3.2 V";
    private static final String PAGE_E3_3 = "E3.3 V";
    private static final String PAGE_B1 = "B1";

    private static final String ERROR_TEXT = "Import fehlgeschlagen: ";

    private int _counter = 0;

    private String _errorMessages;

    public String getErrorMessages() {
        return _errorMessages;
    }

    public int getCounter() {
        return _counter;
    }

    private void setCounter(int counter) {
        this._counter = counter;
    }

    private void increaseCounter() {
        setCounter(getCounter() + 1);
    }

    public Boolean startImport(AEBBaseInformation info, InputStream file) {
        LOGGER.log(Level.INFO, "Start AEB import: " + info.getIk() + " " + info.getYear());
        info.clearAebPages();
        try (Workbook workbook = WorkbookFactory.create(file)) {
            if (!workbookIsInCorrectFormat(workbook)) {
                LOGGER.log(Level.INFO, "Excel has wrong format");
                return false;
            }
            for (Sheet s : workbook) {
                if (s.getSheetName().contains(PAGE_E1_1)) {
                    LOGGER.log(Level.INFO, "Start Sheet " + PAGE_E1_1);
                    importPageE1_1(s, info);
                } else if (s.getSheetName().contains(PAGE_E1_2)) {
                    LOGGER.log(Level.INFO, "Start Sheet " + PAGE_E1_2);
                    importPageE1_2(s, info);
                } else if (s.getSheetName().contains(PAGE_E2)) {
                    LOGGER.log(Level.INFO, "Start Sheet " + PAGE_E2);
                    importPageE2(s, info);
                } else if (s.getSheetName().contains(PAGE_E3_1)) {
                    LOGGER.log(Level.INFO, "Start Sheet " + PAGE_E3_1);
                    importPageE3_1(s, info);
                } else if (s.getSheetName().contains(PAGE_E3_2)) {
                    LOGGER.log(Level.INFO, "Start Sheet " + PAGE_E3_2);
                    importPageE3_2(s, info);
                } else if (s.getSheetName().contains(PAGE_E3_3)) {
                    LOGGER.log(Level.INFO, "Start Sheet " + PAGE_E3_3);
                    importPageE3_3(s, info);
                } else if (s.getSheetName().contains(PAGE_B1)) {
                    LOGGER.log(Level.INFO, "Start Sheet " + PAGE_B1);
                    importPageB1(s, info);
                }
            }
            LOGGER.log(Level.INFO, "End AEB import: " + info.getIk() + " " + info.getYear());
            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Fehler beim AEB Import", ex);
            return false;
        }
    }

    private boolean workbookIsInCorrectFormat(Workbook workbook) {
        LOGGER.log(Level.INFO, "Check excel format");
        boolean correctFormat = true;
        correctFormat = checkAllSheets(workbook);
        return correctFormat;
    }

    @SuppressWarnings({"CyclomaticComplexity", "JavaNCSS"})
    private boolean checkAllSheets(Workbook workbook) {
        List<String> availaibleSheetNames = new ArrayList<>();

        List<String> neededSheetsNames = new ArrayList<>();
        neededSheetsNames.add(PAGE_E1_1);
        neededSheetsNames.add(PAGE_E1_2);
        neededSheetsNames.add(PAGE_E2);
        neededSheetsNames.add(PAGE_E3_1);
        neededSheetsNames.add(PAGE_E3_2);
        neededSheetsNames.add(PAGE_E3_3);
        neededSheetsNames.add(PAGE_B1);

        for (Sheet s : workbook) {
            if (s.getSheetName().contains(PAGE_E1_1)) {
                availaibleSheetNames.add(PAGE_E1_1);
                if (!isPageInCorrectFormat(s, 6)) {
                    createPageNotInCorrectFormMessag(PAGE_E1_1);
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E1_2)) {
                availaibleSheetNames.add(PAGE_E1_2);
                if (!isPageInCorrectFormat(s, 4)) {
                    createPageNotInCorrectFormMessag(PAGE_E1_2);
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E2)) {
                availaibleSheetNames.add(PAGE_E2);
                if (!isPageInCorrectFormat(s, 4)) {
                    createPageNotInCorrectFormMessag(PAGE_E2);
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E3_1)) {
                availaibleSheetNames.add(PAGE_E3_1);
                if (!isPageInCorrectFormat(s, 14)) {
                    createPageNotInCorrectFormMessag(PAGE_E3_1);
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E3_2)) {
                availaibleSheetNames.add(PAGE_E3_2);
                if (!isPageInCorrectFormat(s, 6)) {
                    createPageNotInCorrectFormMessag(PAGE_E3_2);
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E3_3)) {
                availaibleSheetNames.add(PAGE_E3_3);
                if (!isPageInCorrectFormat(s, 6)) {
                    createPageNotInCorrectFormMessag(PAGE_E3_3);
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_B1)) {
                availaibleSheetNames.add(PAGE_B1);
                if (!isPageB1InCorrectFormat(s, 19)) {
                    createPageNotInCorrectFormMessag(PAGE_B1);
                    return false;
                }
            }
        }

        if (!availaibleSheetNames.containsAll(neededSheetsNames)) {
            addErrorMessage(ERROR_TEXT + "Nicht alle Blätter erkannt. Bitte benutzen Sie die Vorlage.");
        }

        return availaibleSheetNames.containsAll(neededSheetsNames);
    }

    private void createPageNotInCorrectFormMessag(String page) {
        LOGGER.log(Level.INFO, "Page: " + page + " not correct");
        addErrorMessage(ERROR_TEXT + "Blatt " + page + " ist nicht im richtigen Format. Bitte benutzen Sie die Vorlage.");
    }

    private boolean isPageB1InCorrectFormat(Sheet sheet, int neededRows) {
        int startRow = 1;
        try {
            if (CellImportHelper.getIntegerFromCell(sheet.getRow(startRow).getCell(1)) == 1 &&
                    CellImportHelper.getIntegerFromCell(sheet.getRow(startRow).getCell(2)) == 2) {
                return true;
            }
            return false;
        }
        catch(Exception ex) {
            return false;
        }

    }

    private boolean isPageInCorrectFormat(Sheet sheet, int neededColumns) {
        int startRow = 1;
        for (int i = 0; i < neededColumns; i++) {
            try {
                int cellValue = CellImportHelper.getIntegerFromCell(sheet.getRow(startRow).getCell(i));
                if (cellValue != i + 1) {
                    return false;
                }
            }
            catch (Exception ex) {

            }
        }
        return true;
    }

    private void importPageE1_1(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            try {
                AEBPageE1_1 page = new AEBPageE1_1();
                page.setPepp(CellImportHelper.getStringFromCell(row.getCell(0)));
                page.setCompensationClass(CellImportHelper.getIntegerFromCell(row.getCell(1)));
                page.setCaseCount(CellImportHelper.getIntegerFromCell(row.getCell(2)));
                page.setCalculationDays(CellImportHelper.getIntegerFromCell(row.getCell(3)));
                page.setValuationRadioDay(CellImportHelper.getDoubleFromCell(row.getCell(4)));
                page.setImportetFrom(getImportetFromString(sheet, i));
                info.addAebPageE1_1(page);
                increaseCounter();
            } catch (Exception ex) {
                handleImporterException(ex);
            }

        }
    }

    private void importPageE1_2(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            try {
                AEBPageE1_2 page = new AEBPageE1_2();
                page.setEt(CellImportHelper.getStringFromCell(row.getCell(0)));
                page.setCalculationDays(CellImportHelper.getIntegerFromCell(row.getCell(1)));
                page.setValuationRadioDay(CellImportHelper.getDoubleFromCell(row.getCell(2)));
                page.setImportetFrom(getImportetFromString(sheet, i));
                info.addAebPageE1_2(page);
                increaseCounter();
            } catch (Exception ex) {
                handleImporterException(ex);
            }
        }
    }

    private void importPageE2(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            try {
                AEBPageE2 page = new AEBPageE2();
                page.setZe(CellImportHelper.getStringFromCell(row.getCell(0)));
                page.setZeCount(CellImportHelper.getIntegerFromCell(row.getCell(1)));
                page.setValuationRadioDay(CellImportHelper.getDoubleFromCell(row.getCell(2)));
                page.setImportetFrom(getImportetFromString(sheet, i));
                info.addAebPageE2(page);
                increaseCounter();
            } catch (Exception ex) {
                handleImporterException(ex);
            }
        }
    }

    private void importPageE3_1(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            try {
                AEBPageE3_1 page = new AEBPageE3_1();
                page.setRenumeration(CellImportHelper.getStringFromCell(row.getCell(0)));
                page.setRenumerationKey(CellImportHelper.getStringFromCell(row.getCell(1)));
                page.setCaseCount(CellImportHelper.getIntegerFromCell(row.getCell(2)));
                page.setRenumerationValue(CellImportHelper.getDoubleFromCell(row.getCell(3)));
                page.setCaseCountDeductions(CellImportHelper.getIntegerFromCell(row.getCell(5)));
                page.setDayCountDeductions(CellImportHelper.getIntegerFromCell(row.getCell(6)));
                page.setDeductionPerDay(CellImportHelper.getDoubleFromCell(row.getCell(7)));
                page.setCaseCountSurcharges(CellImportHelper.getIntegerFromCell(row.getCell(9)));
                page.setDayCountSurcharges(CellImportHelper.getIntegerFromCell(row.getCell(10)));
                page.setSurchargesPerDay(CellImportHelper.getDoubleFromCell(row.getCell(11)));
                page.setImportetFrom(getImportetFromString(sheet, i));
                info.addAebPageE3_1(page);
                increaseCounter();
            } catch (Exception ex) {
                handleImporterException(ex);
            }
        }
    }

    private void importPageE3_2(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            try {
                AEBPageE3_2 page = new AEBPageE3_2();
                page.setZe(CellImportHelper.getStringFromCell(row.getCell(0)));
                page.setRenumerationKey(CellImportHelper.getStringFromCell(row.getCell(1)));
                page.setOps(CellImportHelper.getStringFromCell(row.getCell(2)));
                page.setCount(CellImportHelper.getIntegerFromCell(row.getCell(3)));
                page.setRenumerationValue(CellImportHelper.getDoubleFromCell(row.getCell(4)));
                page.setImportetFrom(getImportetFromString(sheet, i));
                info.addAebPageE3_2(page);
                increaseCounter();
            } catch (Exception ex) {
                handleImporterException(ex);
            }
        }
    }

    private void importPageE3_3(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            try {
                AEBPageE3_3 page = new AEBPageE3_3();
                page.setRenumeration(CellImportHelper.getStringFromCell(row.getCell(0)));
                page.setRenumerationKey(CellImportHelper.getStringFromCell(row.getCell(1)));
                page.setCaseCount(CellImportHelper.getIntegerFromCell(row.getCell(2)));
                page.setDays(CellImportHelper.getIntegerFromCell(row.getCell(3)));
                page.setRenumerationValue(CellImportHelper.getDoubleFromCell(row.getCell(4)));
                page.setImportetFrom(getImportetFromString(sheet, i));
                info.addAebPageE3_3(page);
                increaseCounter();
            } catch (Exception ex) {
                handleImporterException(ex);
            }
        }
    }

    private void importPageB1(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        try {
            info.getAebPageB1().setTotalAgreementPeriod(getValueForForB1(sheet, rowStart, rowEnd, 1));
            info.getAebPageB1().setChangedTotal(getValueForForB1(sheet, rowStart, rowEnd, 10));
            info.getAebPageB1().setChangedProceedsBudget(getValueForForB1(sheet, rowStart, rowEnd, 13));
            info.getAebPageB1().setSumValuationRadioRenumeration(getValueForForB1(sheet, rowStart, rowEnd, 16));
            info.getAebPageB1().setSumEffectivValuationRadio(getValueForForB1(sheet, rowStart, rowEnd, 17));
            info.getAebPageB1().setBasisRenumerationValueCompensation(getValueForForB1(sheet, rowStart, rowEnd, 18));
            info.getAebPageB1().setBasisRenumerationValueNoCompensation(getValueForForB1(sheet, rowStart, rowEnd, 19));
        } catch (Exception ex) {
            handleImporterException(ex);
        }

        increaseCounter();
    }

    private String getImportetFromString(Sheet sheet, int rowNo) {
        return "Blatt " + sheet.getSheetName() + " Zeile " + rowNo + 1;
    }

    private Double getValueForForB1(Sheet sheet, int rowStart, int rowEnd, int runningNumber) {
        for (int i = rowStart; i <= rowEnd; i++) {
            try {
                if ((int) sheet.getRow(i).getCell(0).getNumericCellValue() == runningNumber) {
                    return CellImportHelper.getDoubleFromCell(sheet.getRow(i).getCell(2));
                }
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Sheet B1 row: " + i, ex);
                return 0.0;
            }
        }
        LOGGER.log(Level.WARNING, "Sheet B1 runningNumber: " + runningNumber + " not found");
        return 0.0;
    }

    private int getEndRow(Sheet sheet) {
        LOGGER.log(Level.INFO, sheet.getSheetName() + " EndRows: " + sheet.getLastRowNum());
        return sheet.getLastRowNum();
    }

    private void addErrorMessage(String value) {
        _errorMessages += value + "\n \n";
    }

    private void handleImporterException(Exception ex) {
        if (ex.getClass().isInstance(FormulaInCellException.class)) {
            addErrorMessage("Blatt " + ((FormulaInCellException)ex).getCell().getSheet().getSheetName() + " Zelle: "
                    + ((FormulaInCellException)ex).getCell().getAddress() + "Formeln sind nicht erlaubt.");
        }
        else if (ex.getClass().isInstance(StringInNumericCellException.class)) {
            addErrorMessage("Blatt " + ((StringInNumericCellException)ex).getCell().getSheet().getSheetName() + " Zelle: "
                    + ((StringInNumericCellException)ex).getCell().getAddress() + "Text in Zahlenspalte gefunden.");
        }
    }
}
