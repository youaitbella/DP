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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author lautenti
 */
public class AebImporter {

    public static final Logger LOGGER = Logger.getLogger(AebImporter.class.getName());

    public static final int MAX_ROWS = 100;
    public static final String PAGE_E1_1 = "E1.1 V";
    public static final String PAGE_E1_2 = "E1.2 V";
    public static final String PAGE_E2 = "E2 V";
    public static final String PAGE_E3_1 = "E3.1 V";
    public static final String PAGE_E3_2 = "E3.2 V";
    public static final String PAGE_E3_3 = "E3.3 V";
    public static final String PAGE_B1 = "B1";
    public static final String LASTROW_VALUE = "";

    private int _counter = 0;

    public int getCounter() {
        return _counter;
    }

    public void setCounter(int counter) {
        this._counter = counter;
    }

    public Boolean startImport(AEBBaseInformation info, InputStream file) {
        info.clearAebPages();
        try (Workbook workbook = WorkbookFactory.create(file)) {
            for (Sheet s : workbook) {
                if (s.getSheetName().contains(PAGE_E1_1)) {
                    importPageE1_1(s, info);
                } else if (s.getSheetName().contains(PAGE_E1_2)) {
                    importPageE1_2(s, info);
                } else if (s.getSheetName().contains(PAGE_E2)) {
                    importPageE2(s, info);
                } else if (s.getSheetName().contains(PAGE_E3_1)) {
                    importPageE3_1(s, info);
                } else if (s.getSheetName().contains(PAGE_E3_2)) {
                    importPageE3_2(s, info);
                } else if (s.getSheetName().contains(PAGE_E3_3)) {
                    importPageE3_3(s, info);
                } else if (s.getSheetName().contains(PAGE_B1)) {
                    importPageB1(s, info);
                }
            }
            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Fehler beim AEB Import", ex);
            return false;
        }
    }

    private void importPageE1_1(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet, rowStart, MAX_ROWS);

        for (int i = rowStart; i < rowEnd; i++) {
            AEBPageE1_1 page = new AEBPageE1_1();
            page.setPepp(getStringFromCell(sheet.getRow(i).getCell(0)));
            page.setCompensationClass(getIntFromCell(sheet.getRow(i).getCell(1)));
            page.setCaseCount(getIntFromCell(sheet.getRow(i).getCell(2)));
            page.setCalculationDays(getIntFromCell(sheet.getRow(i).getCell(3)));
            page.setValuationRadioDay(getDoubleFromCell(sheet.getRow(i).getCell(4)));
            info.addAebPageE1_1(page);
            _counter++;
        }
    }

    private void importPageE1_2(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet, rowStart, MAX_ROWS);

        for (int i = rowStart; i < rowEnd; i++) {
            AEBPageE1_2 page = new AEBPageE1_2();
            page.setEt(getStringFromCell(sheet.getRow(i).getCell(0)));
            page.setCalculationDays(getIntFromCell(sheet.getRow(i).getCell(1)));
            page.setValuationRadioDay(getDoubleFromCell(sheet.getRow(i).getCell(2)));
            info.addAebPageE1_2(page);
            _counter++;
        }
    }

    private void importPageE2(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet, rowStart, MAX_ROWS);

        for (int i = rowStart; i < rowEnd; i++) {
            AEBPageE2 page = new AEBPageE2();
            page.setZe(getStringFromCell(sheet.getRow(i).getCell(0)));
            page.setZeCount(getIntFromCell(sheet.getRow(i).getCell(1)));
            page.setValuationRadioDay(getDoubleFromCell(sheet.getRow(i).getCell(2)));
            info.addAebPageE2(page);
            _counter++;
        }
    }

    private void importPageE3_1(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet, rowStart, MAX_ROWS);

        for (int i = rowStart; i < rowEnd; i++) {
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
            _counter++;
        }
    }

    private void importPageE3_2(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet, rowStart, MAX_ROWS);

        for (int i = rowStart; i < rowEnd; i++) {
            AEBPageE3_2 page = new AEBPageE3_2();
            page.setZe(getStringFromCell(sheet.getRow(i).getCell(0)));
            page.setRenumerationKey(getStringFromCell(sheet.getRow(i).getCell(1)));
            page.setOps(getStringFromCell(sheet.getRow(i).getCell(2)));
            page.setCount(getIntFromCell(sheet.getRow(i).getCell(3)));
            page.setRenumerationValue(getDoubleFromCell(sheet.getRow(i).getCell(4)));
            info.addAebPageE3_2(page);
            _counter++;
        }
    }

    private void importPageE3_3(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRow(sheet, rowStart, MAX_ROWS);

        for (int i = rowStart; i < rowEnd; i++) {
            AEBPageE3_3 page = new AEBPageE3_3();
            page.setRenumeration(getStringFromCell(sheet.getRow(i).getCell(0)));
            page.setRenumerationKey(getStringFromCell(sheet.getRow(i).getCell(1)));
            page.setCaseCount(getIntFromCell(sheet.getRow(i).getCell(2)));
            page.setDays(getIntFromCell(sheet.getRow(i).getCell(3)));
            page.setRenumerationValue(getDoubleFromCell(sheet.getRow(i).getCell(4)));
            info.addAebPageE3_3(page);
            _counter++;
        }
    }

    private void importPageB1(Sheet sheet, AEBBaseInformation info) {
        int rowStart = 2;
        int rowEnd = getEndRowForB1(sheet, rowStart, MAX_ROWS);

        info.getAebPageB1().setTotalAgreementPeriod(getValueForForB1(sheet, rowStart, rowEnd, 1));
        info.getAebPageB1().setChangedTotal(getValueForForB1(sheet, rowStart, rowEnd, 10));
        info.getAebPageB1().setChangedProceedsBudget(getValueForForB1(sheet, rowStart, rowEnd, 13));
        info.getAebPageB1().setSumValuationRadioRenumeration(getValueForForB1(sheet, rowStart, rowEnd, 16));
        info.getAebPageB1().setSumEffectivValuationRadio(getValueForForB1(sheet, rowStart, rowEnd, 17));
        info.getAebPageB1().setBasisRenumerationValueCompensation(getValueForForB1(sheet, rowStart, rowEnd, 18));
        info.getAebPageB1().setBasisRenumerationValueNoCompensation(getValueForForB1(sheet, rowStart, rowEnd, 19));

        _counter++;
    }

    private Double getValueForForB1(Sheet sheet, int rowStart, int rowEnd, int runningNumber) {
        for (int i = rowStart ; i < rowEnd; i++) {
            try {
                if ((int)sheet.getRow(i).getCell(0).getNumericCellValue() == runningNumber) {
                    return getDoubleFromCell(sheet.getRow(i).getCell(2));
                }
            }
            catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Sheet B1 row: " + i, ex);
                return 0.0;
            }
        }
        LOGGER.log(Level.WARNING, "Sheet B1 runningNumber: " + runningNumber + " not found");
        return 0.0;
    }

    private int getEndRowForB1(Sheet sheet, int startRow, int maxRow) {
        int rowEnd = 0;
        for (int i = startRow; i <= maxRow; i++) {
            if (sheet.getRow(i) == null || sheet.getRow(i).getCell(0) == null) {
                rowEnd = i;
                break;
            }

            int value = getIntFromCell(sheet.getRow(i).getCell(0));
            if (value == 0 || LASTROW_VALUE.equals(value) || "".equals(value)) {
                rowEnd = i;
                break;
            }
        }
        return rowEnd;
    }

    private int getEndRow(Sheet sheet, int startRow, int maxRow) {
        int rowEnd = 0;
        for (int i = startRow; i <= maxRow; i++) {
            if (sheet.getRow(i) == null || sheet.getRow(i).getCell(0) == null) {
                rowEnd = i;
                break;
            }
            String value = getStringFromCell(sheet.getRow(i).getCell(0));
            if (value == null || LASTROW_VALUE.equals(value) || "".equals(value)) {
                rowEnd = i;
                break;
            }
        }
        return rowEnd;
    }

    private String getStringFromCell(Cell cell) {
        try {
            String value = cell.getStringCellValue();
            if (value == null) {
                return "";
            }
            return value;
        }
        catch (Exception ex) {
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
