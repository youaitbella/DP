/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.KhComparison.importer;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.inek.dataportal.common.data.KhComparison.entities.*;

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

    public static final String PAGE_E1_1 = "E1.1 V";
    public static final String PAGE_E1_2 = "E1.2 V";
    public static final String PAGE_E2 = "E2 V";
    public static final String PAGE_E3_1 = "E3.1 V";
    public static final String PAGE_E3_2 = "E3.2 V";
    public static final String PAGE_E3_3 = "E3.3 V";
    public static final String PAGE_B1 = "B1";

    public static final String ERROR_TEXT = "Import fehlgeschlagen: ";

    private int _counter = 0;

    private String _errorMessage;

    public String getErrorMessage() {
        return _errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this._errorMessage = errorMessage;
    }

    public int getCounter() {
        return _counter;
    }

    public void setCounter(int counter) {
        this._counter = counter;
    }

    public void increaseCounter() {
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
                    LOGGER.log(Level.INFO, "Page: " + PAGE_E1_1 + " not correct");
                    setErrorMessage(ERROR_TEXT + "Blatt " + PAGE_E1_1 + " ist nicht im richtigen Format. Bitte benutzen Sie die Vorlage.");
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E1_2)) {
                availaibleSheetNames.add(PAGE_E1_2);
                if (!isPageInCorrectFormat(s, 4)) {
                    LOGGER.log(Level.INFO, "Page: " + PAGE_E1_2 + " not correct");
                    setErrorMessage(ERROR_TEXT + "Blatt " + PAGE_E1_2 + " ist nicht im richtigen Format. Bitte benutzen Sie die Vorlage.");
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E2)) {
                availaibleSheetNames.add(PAGE_E2);
                if (!isPageInCorrectFormat(s, 4)) {
                    LOGGER.log(Level.INFO, "Page: " + PAGE_E2 + " not correct");
                    setErrorMessage(ERROR_TEXT + "Blatt " + PAGE_E2 + " ist nicht im richtigen Format. Bitte benutzen Sie die Vorlage.");
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E3_1)) {
                availaibleSheetNames.add(PAGE_E3_1);
                if (!isPageInCorrectFormat(s, 14)) {
                    LOGGER.log(Level.INFO, "Page: " + PAGE_E3_1 + " not correct");
                    setErrorMessage(ERROR_TEXT + "Blatt " + PAGE_E3_1 + " ist nicht im richtigen Format. Bitte benutzen Sie die Vorlage.");
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E3_2)) {
                availaibleSheetNames.add(PAGE_E3_2);
                if (!isPageInCorrectFormat(s, 6)) {
                    LOGGER.log(Level.INFO, "Page: " + PAGE_E3_2 + " not correct");
                    setErrorMessage(ERROR_TEXT + "Blatt " + PAGE_E3_2 + " ist nicht im richtigen Format. Bitte benutzen Sie die Vorlage.");
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_E3_3)) {
                availaibleSheetNames.add(PAGE_E3_3);
                if (!isPageInCorrectFormat(s, 6)) {
                    LOGGER.log(Level.INFO, "Page: " + PAGE_E3_3 + " not correct");
                    setErrorMessage(ERROR_TEXT + "Blatt " + PAGE_E3_3 + " ist nicht im richtigen Format. Bitte benutzen Sie die Vorlage.");
                    return false;
                }
            } else if (s.getSheetName().contains(PAGE_B1)) {
                availaibleSheetNames.add(PAGE_B1);
                if (!isPageB1InCorrectFormat(s, 19)) {
                    LOGGER.log(Level.INFO, "Page: " + PAGE_B1 + " not correct");
                    setErrorMessage(ERROR_TEXT + "Blatt " + PAGE_B1 + " ist nicht im richtigen Format. Bitte benutzen Sie die Vorlage.");
                    return false;
                }
            }
        }

        if (!availaibleSheetNames.containsAll(neededSheetsNames)) {
            setErrorMessage(ERROR_TEXT + "Nicht alle Blätter erkannt. Bitte benutzen Sie die Vorlage.");
        }

        return availaibleSheetNames.containsAll(neededSheetsNames);
    }

    private boolean isPageB1InCorrectFormat(Sheet sheet, int neededRows) {
        int startRow = 1;
        if (getIntFromCell(sheet.getRow(startRow).getCell(1)) == 1 &&
                getIntFromCell(sheet.getRow(startRow).getCell(2)) == 2) {
            return true;
        }
        return false;
    }

    private boolean isPageInCorrectFormat(Sheet sheet, int neededColumns) {
        int startRow = 1;
        for (int i = 0; i < neededColumns; i++) {
            int cellValue = getIntFromCell(sheet.getRow(startRow).getCell(i));
            if (cellValue != i + 1) {
                return false;
            }
        }
        return true;
    }

    private void importPageE1_1(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            AEBPageE1_1 page = new AEBPageE1_1();
            page.setPepp(getStringFromCell(sheet.getRow(i).getCell(0)));
            page.setCompensationClass(getIntFromCell(sheet.getRow(i).getCell(1)));
            page.setCaseCount(getIntFromCell(sheet.getRow(i).getCell(2)));
            page.setCalculationDays(getIntFromCell(sheet.getRow(i).getCell(3)));
            page.setValuationRadioDay(getDoubleFromCell(sheet.getRow(i).getCell(4)));
            info.addAebPageE1_1(page);
            increaseCounter();
        }
    }

    private void importPageE1_2(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            AEBPageE1_2 page = new AEBPageE1_2();
            page.setEt(getStringFromCell(sheet.getRow(i).getCell(0)));
            page.setCalculationDays(getIntFromCell(sheet.getRow(i).getCell(1)));
            page.setValuationRadioDay(getDoubleFromCell(sheet.getRow(i).getCell(2)));
            info.addAebPageE1_2(page);
            increaseCounter();
        }
    }

    private void importPageE2(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            AEBPageE2 page = new AEBPageE2();
            page.setZe(getStringFromCell(sheet.getRow(i).getCell(0)));
            page.setZeCount(getIntFromCell(sheet.getRow(i).getCell(1)));
            page.setValuationRadioDay(getDoubleFromCell(sheet.getRow(i).getCell(2)));
            info.addAebPageE2(page);
            increaseCounter();
        }
    }

    private void importPageE3_1(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            AEBPageE3_1 page = new AEBPageE3_1();
            page.setRenumeration(getStringFromCell(sheet.getRow(i).getCell(0)));
            page.setRenumerationKey(getStringFromCell(sheet.getRow(i).getCell(1)));
            page.setCaseCount(getIntFromCell(sheet.getRow(i).getCell(2)));
            page.setRenumerationValue(getDoubleFromCell(sheet.getRow(i).getCell(3)));
            page.setCaseCountDeductions(getIntFromCell(sheet.getRow(i).getCell(5)));
            page.setDayCountDeductions(getIntFromCell(sheet.getRow(i).getCell(6)));
            page.setDeductionPerDay(getDoubleFromCell(sheet.getRow(i).getCell(7)));
            page.setCaseCountSurcharges(getIntFromCell(sheet.getRow(i).getCell(9)));
            page.setDayCountSurcharges(getIntFromCell(sheet.getRow(i).getCell(10)));
            page.setSurchargesPerDay(getDoubleFromCell(sheet.getRow(i).getCell(11)));
            info.addAebPageE3_1(page);
            increaseCounter();
        }
    }

    private void importPageE3_2(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            AEBPageE3_2 page = new AEBPageE3_2();
            page.setZe(getStringFromCell(sheet.getRow(i).getCell(0)));
            page.setRenumerationKey(getStringFromCell(sheet.getRow(i).getCell(1)));
            page.setOps(getStringFromCell(sheet.getRow(i).getCell(2)));
            page.setCount(getIntFromCell(sheet.getRow(i).getCell(3)));
            page.setRenumerationValue(getDoubleFromCell(sheet.getRow(i).getCell(4)));
            info.addAebPageE3_2(page);
            increaseCounter();
        }
    }

    private void importPageE3_3(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            AEBPageE3_3 page = new AEBPageE3_3();
            page.setRenumeration(getStringFromCell(sheet.getRow(i).getCell(0)));
            page.setRenumerationKey(getStringFromCell(sheet.getRow(i).getCell(1)));
            page.setCaseCount(getIntFromCell(sheet.getRow(i).getCell(2)));
            page.setDays(getIntFromCell(sheet.getRow(i).getCell(3)));
            page.setRenumerationValue(getDoubleFromCell(sheet.getRow(i).getCell(4)));
            info.addAebPageE3_3(page);
            increaseCounter();
        }
    }

    private void importPageB1(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet);

        info.getAebPageB1().setTotalAgreementPeriod(getValueForForB1(sheet, rowStart, rowEnd, 1));
        info.getAebPageB1().setChangedTotal(getValueForForB1(sheet, rowStart, rowEnd, 10));
        info.getAebPageB1().setChangedProceedsBudget(getValueForForB1(sheet, rowStart, rowEnd, 13));
        info.getAebPageB1().setSumValuationRadioRenumeration(getValueForForB1(sheet, rowStart, rowEnd, 16));
        info.getAebPageB1().setSumEffectivValuationRadio(getValueForForB1(sheet, rowStart, rowEnd, 17));
        info.getAebPageB1().setBasisRenumerationValueCompensation(getValueForForB1(sheet, rowStart, rowEnd, 18));
        info.getAebPageB1().setBasisRenumerationValueNoCompensation(getValueForForB1(sheet, rowStart, rowEnd, 19));

        increaseCounter();
    }

    private Double getValueForForB1(Sheet sheet, int rowStart, int rowEnd, int runningNumber) {
        for (int i = rowStart; i <= rowEnd; i++) {
            try {
                if ((int) sheet.getRow(i).getCell(0).getNumericCellValue() == runningNumber) {
                    return getDoubleFromCell(sheet.getRow(i).getCell(2));
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

    private String getStringFromCell(Cell cell) {
        try {
            String value = cell.getStringCellValue();
            if (value == null) {
                return "";
            }
            return value;
        } catch (Exception ex) {
            return "";
        }
    }

    private int getIntFromCell(Cell cell) {
        try {
            double value = cell.getNumericCellValue();
            return (int) value;
        } catch (Exception ex) {
            return 0;
        }
    }

    private double getDoubleFromCell(Cell cell) {
        try {
            return cell.getNumericCellValue();
        } catch (Exception ex) {
            return 0;
        }
    }
}
