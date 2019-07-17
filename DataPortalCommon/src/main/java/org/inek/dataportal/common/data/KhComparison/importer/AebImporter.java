/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.KhComparison.importer;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.inek.dataportal.common.data.KhComparison.checker.RenumerationChecker;
import org.inek.dataportal.common.data.KhComparison.entities.*;
import org.inek.dataportal.common.exceptions.FormulaInCellException;
import org.inek.dataportal.common.exceptions.StringInNumericCellException;
import org.inek.dataportal.common.exceptions.ValueToLongCellException;
import org.inek.dataportal.common.helper.excelimport.CellImportHelper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static final String HEADER_TEXT_PAGE_E1_1 = "PEPP;Vergütung;Fallzahl;Berechnung;Bewertung;Summe";
    private static final String HEADER_TEXT_PAGE_E1_2 = "ET-Nr;Anzahl;Bewertungsrelation;Summe";
    private static final String HEADER_TEXT_PAGE_E2 = "ZE-Nr.;Anzahl der ZE;Entgelthöhe lt. ZE-Katalog;Erlössumme";

    private static final String HEADER_TEXT_PAGE_E3_1 = "§ 6 BPflV;Entgeltschlüssel;Fallzahl;Entgelthöhe;Bruttoerlössumme;Fälle;Tage;Abschlag" +
            ";Abschläge;Fälle;Tage;Tag;Zuschläge;Nettoerlössumme";
    private static final String HEADER_TEXT_PAGE_E3_2 = "§ 6 BPflV;§ 301 SGB V;OPS-Kode;Anzahl;Entgelt;Erlös";
    private static final String HEADER_TEXT_PAGE_E3_3 = "§ 6 BPflV;§ 301 SGB V;Fallzahl;Tage;Entgelt;Erlös";


    private static final int MAX_SCAN_COLS = 10;
    private static final int MAX_SCAN_ROWS = 15;
    private static final int MAX_SCAN_ROWS_B1 = 60;

    private static final String ERROR_TEXT = "Import fehlgeschlagen: ";

    private int _counter = 0;

    private String _errorMessages = "";

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
                if (!isPageInCorrectFormat(s, HEADER_TEXT_PAGE_E1_1)) {
                    createPageNotInCorrectFormMessag(PAGE_E1_1);
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E1_2)) {
                availaibleSheetNames.add(PAGE_E1_2);
                if (!isPageInCorrectFormat(s, HEADER_TEXT_PAGE_E1_2)) {
                    createPageNotInCorrectFormMessag(PAGE_E1_2);
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E2)) {
                availaibleSheetNames.add(PAGE_E2);
                if (!isPageInCorrectFormat(s, HEADER_TEXT_PAGE_E2)) {
                    createPageNotInCorrectFormMessag(PAGE_E2);
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E3_1)) {
                availaibleSheetNames.add(PAGE_E3_1);
                if (!isPageInCorrectFormat(s, HEADER_TEXT_PAGE_E3_1)) {
                    createPageNotInCorrectFormMessag(PAGE_E3_1);
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E3_2)) {
                availaibleSheetNames.add(PAGE_E3_2);
                if (!isPageInCorrectFormat(s, HEADER_TEXT_PAGE_E3_2)) {
                    createPageNotInCorrectFormMessag(PAGE_E3_2);
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E3_3)) {
                availaibleSheetNames.add(PAGE_E3_3);
                if (!isPageInCorrectFormat(s, HEADER_TEXT_PAGE_E3_3)) {
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

        boolean hasNoFormatErrors = _errorMessages.isEmpty();

        if (!availaibleSheetNames.containsAll(neededSheetsNames)) {
            neededSheetsNames.removeAll(availaibleSheetNames);
            for (String sheetName : neededSheetsNames) {
                addErrorMessage("Blatt [" + sheetName + "] konnte nicht gefunden werden.");
            }
        }

        return hasNoFormatErrors;
    }

    private void createPageNotInCorrectFormMessag(String page) {
        LOGGER.log(Level.INFO, "Page: " + page + " not correct");
        addErrorMessage(ERROR_TEXT + "Blatt [" + page + "] ist nicht im richtigen Format. Bitte benutzen Sie die Vorlage.");
    }

    private boolean isPageB1InCorrectFormat(Sheet sheet, int neededRows) {
        CellAddress adressWithValue = getAdressWithValue(sheet, "lfd", 0, 0);

        Map<Integer, Boolean> mapRunningNumbersFound = new HashMap<>();

        for (int i = 1; i <= neededRows; i++) {
            mapRunningNumbersFound.put(i, false);
        }

        for (int i = adressWithValue.getRow() + 1; i < MAX_SCAN_ROWS_B1; i++) {
            try {
                Integer integerFromCell = CellImportHelper.getIntegerFromCell(sheet.getRow(i).getCell(adressWithValue.getColumn()));
                if (!mapRunningNumbersFound.containsValue(false)) {
                    return true;
                }
                if (mapRunningNumbersFound.get(integerFromCell) != null) {
                    mapRunningNumbersFound.put(integerFromCell, true);
                }
            } catch (Exception ex) {

            }
        }

        return !mapRunningNumbersFound.containsValue(false);
    }

    private boolean isPageInCorrectFormat(Sheet sheet, String neededHeader) {
        CellAddress startAdress = getRowStartWithPageHeader(sheet, neededHeader);
        return !"0".equals(startAdress.formatAsString());
    }

    private CellAddress getAdressWithValue(Sheet sheet, String value, int startRow, int startCol) {
        for (int i = startRow; i < MAX_SCAN_ROWS; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            for (int j = startCol; j < MAX_SCAN_COLS; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                try {
                    String stringFromCell = CellImportHelper.getStringFromCell(cell, true).toUpperCase();
                    if (stringFromCell.contains(value.toUpperCase())) {
                        return new CellAddress(i, j);
                    }
                } catch (Exception ex) {

                }
            }
        }
        return new CellAddress(-1, -1);
    }

    private CellAddress getRowStartWithPageHeader(Sheet sheet, String header) {
        LOGGER.log(Level.INFO, "Sheet " + sheet.getSheetName() + " start find start cell ");

        String[] headerCols = header.split(";");

        int foundRow = -1;
        int foundCol = -1;

        CellAddress firstHeaderElementAdress = findAdressForHeader(sheet, headerCols);

        if ("0".equals(firstHeaderElementAdress.toString())) {
            LOGGER.log(Level.INFO, "Sheet + " + sheet.getSheetName() + " no start cell found");
            return firstHeaderElementAdress;
        }
        else {
            foundRow = firstHeaderElementAdress.getRow();
            foundCol = firstHeaderElementAdress.getColumn();
        }

        try {
            Integer integerFromCell = CellImportHelper.getIntegerFromCell(sheet.getRow(foundRow + 1).getCell(foundCol));
            if (integerFromCell == 1) {
                foundRow += 2;
            } else {
                foundRow++;
            }
        } catch (Exception e) {
            foundRow++;
        }

        CellAddress adress = new CellAddress(foundRow, foundCol);

        LOGGER.log(Level.INFO, "Sheet + " + sheet.getSheetName() + " end find start cell " + adress.formatAsString());

        return adress;
    }

    private CellAddress findAdressForHeader(Sheet sheet, String[] headerCols) {
        for (int i = 0; i < MAX_SCAN_ROWS; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            int cellIndex = haederStartCellIndex(row, headerCols);

            if (cellIndex != -1) {
                return new CellAddress(i, cellIndex);
            }
        }
        return new CellAddress(-1, -1);
    }

    private int haederStartCellIndex(Row row, String[] headerCols) {
        for (int j = 0; j < MAX_SCAN_COLS; j++) {
            Cell cell = row.getCell(j);
            if (cell == null) {
                continue;
            }

            if (cellRangeMatchHeader(cell, headerCols)) {
                return j;
            }
        }
        return -1;
    }

    private boolean cellRangeMatchHeader(Cell cell, String[] headerCols) {
        for (int k = 0; k < headerCols.length; k++) {
            try {
                String stringFromCell = CellImportHelper.getStringFromCell(cell, true).toUpperCase();
                stringFromCell = stringFromCell.replace("\n", "").replace("\r", "");

                if (stringFromCell.contains(headerCols[k].toUpperCase())) {
                    cell = cell.getRow().getCell(cell.getColumnIndex() + 1);
                    if (k + 1 >= headerCols.length) {
                        return true;
                    }
                    if (cell == null) {
                        return false;
                    }
                } else {
                    return false;
                }
            } catch (Exception ex) {

            }
        }
        return false;
    }

    private void importPageE1_1(Sheet sheet, AEBBaseInformation info) {
        CellAddress startAdress = getRowStartWithPageHeader(sheet, HEADER_TEXT_PAGE_E1_1);

        int rowStart = startAdress.getRow();
        int colStart = startAdress.getColumn();
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }

            try {

                String pepp = CellImportHelper.getStringFromCell(row.getCell(colStart));
                if (!RenumerationChecker.isFormalValidPepp(pepp)) {
                    addErrorMessage("Blatt [" + sheet.getSheetName() + "] Zelle: " + (i + 1) + " keine gültige PEPP.");
                    continue;
                }
                AEBPageE1_1 page = new AEBPageE1_1();
                page.setPepp(CellImportHelper.getStringFromCell(row.getCell(colStart)));
                page.setCompensationClass(CellImportHelper.getIntegerFromCell(row.getCell(colStart + 1)));
                page.setCaseCount(CellImportHelper.getIntegerFromCell(row.getCell(colStart + 2), false, true));
                page.setCalculationDays(CellImportHelper.getIntegerFromCell(row.getCell(colStart + 3)));
                page.setValuationRadioDay(CellImportHelper.getDoubleFromCell(row.getCell(colStart + 4)));
                page.setImportetFrom(getImportetFromString(sheet, i));
                info.addAebPageE1_1(page);
                increaseCounter();
            } catch (Exception ex) {
                handleImporterException(ex);
            }

        }
    }


    private void importPageE1_2(Sheet sheet, AEBBaseInformation info) {
        CellAddress startAdress = getRowStartWithPageHeader(sheet, HEADER_TEXT_PAGE_E1_2);

        int rowStart = startAdress.getRow();
        int colStart = startAdress.getColumn();

        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            try {
                String et = CellImportHelper.getStringFromCell(row.getCell(colStart));
                if (!RenumerationChecker.isFormalValidEt(et)) {
                    addErrorMessage("Blatt [" + sheet.getSheetName() + "] Zelle: " + (i + 1) + " kein gültiges ET.");
                    continue;
                }

                AEBPageE1_2 page = new AEBPageE1_2();
                page.setEt(CellImportHelper.getStringFromCell(row.getCell(colStart)));
                page.setCalculationDays(CellImportHelper.getIntegerFromCell(row.getCell(colStart + 1)));
                page.setValuationRadioDay(CellImportHelper.getDoubleFromCell(row.getCell(colStart + 2)));
                page.setImportetFrom(getImportetFromString(sheet, i));
                info.addAebPageE1_2(page);
                increaseCounter();
            } catch (Exception ex) {
                handleImporterException(ex);
            }
        }
    }

    private void importPageE2(Sheet sheet, AEBBaseInformation info) {
        CellAddress startAdress = getRowStartWithPageHeader(sheet, HEADER_TEXT_PAGE_E2);

        int rowStart = startAdress.getRow();
        int colStart = startAdress.getColumn();

        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            try {
                String ze = CellImportHelper.getStringFromCell(row.getCell(colStart));
                if (!RenumerationChecker.isFormalValidZe(ze)) {
                    addErrorMessage("Blatt [" + sheet.getSheetName() + "] Zelle: " + (i + 1) + " kein gültiges ZE.");
                    continue;
                }

                AEBPageE2 page = new AEBPageE2();
                page.setZe(CellImportHelper.getStringFromCell(row.getCell(colStart)));
                page.setZeCount(CellImportHelper.getIntegerFromCell(row.getCell(colStart + 1)));
                page.setValuationRadioDay(CellImportHelper.getDoubleFromCell(row.getCell(colStart + 2)));
                page.setImportetFrom(getImportetFromString(sheet, i));
                info.addAebPageE2(page);
                increaseCounter();
            } catch (Exception ex) {
                handleImporterException(ex);
            }
        }
    }

    private void importPageE3_1(Sheet sheet, AEBBaseInformation info) {
        CellAddress startAdress = getRowStartWithPageHeader(sheet, HEADER_TEXT_PAGE_E3_1);

        int rowStart = startAdress.getRow();
        int colStart = startAdress.getColumn();

        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            try {
                AEBPageE3_1 page = new AEBPageE3_1();
                page.setRenumeration(CellImportHelper.getStringFromCell(row.getCell(colStart)));
                page.setRenumerationKey(CellImportHelper.getStringFromCell(row.getCell(colStart + 1)));
                page.setCaseCount(CellImportHelper.getIntegerFromCell(row.getCell(colStart + 2)));
                page.setRenumerationValue(CellImportHelper.getDoubleFromCell(row.getCell(colStart + 3)));
                page.setCaseCountDeductions(CellImportHelper.getIntegerFromCell(row.getCell(colStart + 5)));
                page.setDayCountDeductions(CellImportHelper.getIntegerFromCell(row.getCell(colStart + 6)));
                page.setDeductionPerDay(CellImportHelper.getDoubleFromCell(row.getCell(colStart + 7)));
                page.setCaseCountSurcharges(CellImportHelper.getIntegerFromCell(row.getCell(colStart + 9)));
                page.setDayCountSurcharges(CellImportHelper.getIntegerFromCell(row.getCell(colStart + 10)));
                page.setSurchargesPerDay(CellImportHelper.getDoubleFromCell(row.getCell(colStart + 11)));
                page.setImportetFrom(getImportetFromString(sheet, i));
                info.addAebPageE3_1(page);
                increaseCounter();
            } catch (Exception ex) {
                handleImporterException(ex);
            }
        }
    }

    private void importPageE3_2(Sheet sheet, AEBBaseInformation info) {
        CellAddress startAdress = getRowStartWithPageHeader(sheet, HEADER_TEXT_PAGE_E3_2);

        int rowStart = startAdress.getRow();
        int colStart = startAdress.getColumn();

        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            try {
                String ze = CellImportHelper.getStringFromCell(row.getCell(colStart));
                if (!RenumerationChecker.isFormalValidZe(ze)) {
                    addErrorMessage("Blatt [" + sheet.getSheetName() + "] Zelle: " + (i + 1) + " kein gültiges ZE.");
                    continue;
                }

                AEBPageE3_2 page = new AEBPageE3_2();
                page.setZe(CellImportHelper.getStringFromCell(row.getCell(colStart)));

                String renumerationKeyFromCell = CellImportHelper.getStringFromCell(row.getCell(colStart + 1));
                if (renumerationKeyFromCell.length() > 10) {
                    throw new ValueToLongCellException(row.getCell(colStart + 1), 10);
                }

                page.setRenumerationKey(renumerationKeyFromCell);

                String opsFromCell = CellImportHelper.getStringFromCell(row.getCell(colStart + 2));
                if (opsFromCell.length() > 20) {
                    throw new ValueToLongCellException(row.getCell(colStart + 2), 20);
                }

                page.setOps(opsFromCell);
                page.setCount(CellImportHelper.getIntegerFromCell(row.getCell(colStart + 3)));
                page.setRenumerationValue(CellImportHelper.getDoubleFromCell(row.getCell(colStart + 4)));
                page.setImportetFrom(getImportetFromString(sheet, i));
                info.addAebPageE3_2(page);
                increaseCounter();
            } catch (Exception ex) {
                handleImporterException(ex);
            }
        }
    }

    private void importPageE3_3(Sheet sheet, AEBBaseInformation info) {
        CellAddress startAdress = getRowStartWithPageHeader(sheet, HEADER_TEXT_PAGE_E3_3);

        int rowStart = startAdress.getRow();
        int colStart = startAdress.getColumn();

        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            try {
                String pepp = CellImportHelper.getStringFromCell(row.getCell(colStart));
                if (!RenumerationChecker.isFormalValidPepp(pepp)) {
                    addErrorMessage("Blatt [" + sheet.getSheetName() + "] Zelle: " + (i + 1) + " keine gültige PEPP.");
                    continue;
                }

                AEBPageE3_3 page = new AEBPageE3_3();
                page.setRenumeration(CellImportHelper.getStringFromCell(row.getCell(colStart)));
                page.setRenumerationKey(CellImportHelper.getStringFromCell(row.getCell(colStart + 1)));
                page.setCaseCount(CellImportHelper.getIntegerFromCell(row.getCell(colStart + 2), false, true));
                page.setDays(CellImportHelper.getIntegerFromCell(row.getCell(colStart + 3)));
                page.setRenumerationValue(CellImportHelper.getDoubleFromCell(row.getCell(colStart + 4)));
                page.setImportetFrom(getImportetFromString(sheet, i));
                info.addAebPageE3_3(page);
                increaseCounter();
            } catch (Exception ex) {
                handleImporterException(ex);
            }
        }
    }

    private void importPageB1(Sheet sheet, AEBBaseInformation info) {
        CellAddress adressWithValue = getAdressWithValue(sheet, "lfd", 0, 0);

        int rowStart = adressWithValue.getRow();
        int col = adressWithValue.getColumn();

        try {
            info.getAebPageB1().setTotalAgreementPeriod(getValueForForB1(sheet, rowStart, col, 1));
            info.getAebPageB1().setChangedTotal(getValueForForB1(sheet, rowStart, col, 10));
            info.getAebPageB1().setChangedProceedsBudget(getValueForForB1(sheet, rowStart, col, 13));
            info.getAebPageB1().setSumValuationRadioRenumeration(getValueForForB1(sheet, rowStart, col, 16));
            info.getAebPageB1().setSumEffectivValuationRadio(getValueForForB1(sheet, rowStart, col, 17));
            info.getAebPageB1().setBasisRenumerationValueCompensation(getValueForForB1(sheet, rowStart, col, 18));
            info.getAebPageB1().setBasisRenumerationValueNoCompensation(getValueForForB1(sheet, rowStart, col, 19));
        } catch (Exception ex) {
            handleImporterException(ex);
        }

        increaseCounter();
    }

    private String getImportetFromString(Sheet sheet, int rowNo) {
        return "Blatt " + sheet.getSheetName() + " Zeile " + (rowNo + 1);
    }

    private Double getValueForForB1(Sheet sheet, int rowStart, int col, int runningNumber) {
        for (int i = rowStart; i <= MAX_SCAN_ROWS_B1; i++) {
            try {
                Integer integerFromCell = CellImportHelper.getIntegerFromCell(sheet.getRow(i).getCell(col));
                if (runningNumber == integerFromCell) {
                    try {
                        return CellImportHelper.getDoubleFromCell(sheet.getRow(i).getCell(col + 2), true, true);
                    } catch (Exception ex) {
                        LOGGER.log(Level.WARNING, "Sheet B1 row: " + i, ex);
                        return 0.0;
                    }
                }
            } catch (Exception ex) {

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
        _errorMessages += value + "\n";
    }

    private void handleImporterException(Exception ex) {
        if (ex instanceof FormulaInCellException) {
            addErrorMessage("Blatt [" + ((FormulaInCellException) ex).getCell().getSheet().getSheetName() + "] Zelle: ["
                    + ((FormulaInCellException) ex).getCell().getAddress() + "] Formeln sind nicht erlaubt.");
        } else if (ex instanceof StringInNumericCellException) {
            addErrorMessage("Blatt [" + ((StringInNumericCellException) ex).getCell().getSheet().getSheetName() + "] Zelle: ["
                    + ((StringInNumericCellException) ex).getCell().getAddress() + "] Text in Zahlenspalte gefunden.");
        }
        else if (ex instanceof ValueToLongCellException) {
            addErrorMessage("Blatt [" + ((ValueToLongCellException) ex).getCell().getSheet().getSheetName() + "] Zelle: ["
                    + ((ValueToLongCellException) ex).getCell().getAddress() + "] Text zu lang. Erlaubte Länge: "
                    + ((ValueToLongCellException) ex).getMaxAllowedLength());
        }
    }
}
