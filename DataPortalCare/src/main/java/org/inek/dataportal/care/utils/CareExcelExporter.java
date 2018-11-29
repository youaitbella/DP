/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Comparator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptStation;
import org.inek.dataportal.care.entities.DeptStationsAfterTargetYear;

/**
 *
 * @author lautenti
 */
public class CareExcelExporter {

    private static final String SHEET_NAME_1 = "Umsetzung PpUGV";
    private static final String SHEET_NAME_2 = "§ 5 Abs. 4 PpUGV";
    private static final String IK = "IK-Nummer:";
    private static final String KH_NAME = "Name des Krankenhauses:";
    private static final String KH_TOWN = "Ort:";
    private static final String SEND_AT = "Übermittlung an das InEK:";

    private static final String TABLE_HEADER_1 = "Bezug PpUGV";
    private static final String TABLE_HEADER_2 = "Fachabteilungsschlüssel (§ 21-Daten, 2017)";
    private static final String TABLE_HEADER_3 = "Fachabteilungsname (§ 21-Daten, 2017)";
    private static final String TABLE_HEADER_3_0 = "Fachabteilungsnr. (Eingabe KH, 2017)";
    private static final String TABLE_HEADER_3_1 = "Fachabteilungsname (Eingabe KH, 2017)";
    private static final String TABLE_HEADER_4 = "pflegesensitiver Bereich";
    private static final String TABLE_HEADER_5 = "Stationsname (2017)";
    private static final String TABLE_HEADER_6 = "Standort (2017)";
    private static final String TABLE_HEADER_7 = "Fachabteilungsschlüssel (nach 2017)";
    private static final String TABLE_HEADER_8 = "Fachabteilungsname (nach 2017)";
    private static final String TABLE_HEADER_9 = "Stationsname (nach 2017)";
    private static final String TABLE_HEADER_10 = "Standort (nach 2017)";
    private static final String TABLE_HEADER_11 = "Anmerkung";

    private static final String BEZUG_1 = "ausgewiesene Fachabteilung";
    private static final String BEZUG_2 = "Indikatoren-DRGs";
    private static final String BEZUG_3 = "OPS-Liste Intensivmedizin";

    public InputStream createExcelExportFile(DeptBaseInformation baseInfo, String hospitalName, String hospitalTown) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet1 = workbook.createSheet(SHEET_NAME_1);

            setBaseInformations(sheet1, baseInfo, hospitalName, hospitalTown);
            setTableHeader(sheet1);
            setTable(sheet1, baseInfo);
            if (baseInfo.getDepts().stream().anyMatch(c -> c.getDeptsAftertargetYear().size() > 0)) {
                Sheet sheet2 = workbook.createSheet(SHEET_NAME_2);
                setBaseInformations(sheet2, baseInfo, hospitalName, hospitalTown);
                setTableAfterTargetYearHeader(sheet2, baseInfo);
                setTableAfterTargetYear(sheet2, baseInfo);
                autoSizeCols(sheet2);
            }
            autoSizeCols(sheet1);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            byte[] barray = bos.toByteArray();
            InputStream is = new ByteArrayInputStream(barray);
            return is;
        } catch (Exception e) {

        }
        return null;

    }

    private void setBaseInformations(Sheet sheet, DeptBaseInformation baseInfo, String hospitalName, String hospitalTown) {

        //ik Cell style
        CellStyle styleIk = sheet.getWorkbook().createCellStyle();
        styleIk.setAlignment(HorizontalAlignment.LEFT);

        sheet.createRow(0).createCell(0).setCellValue(IK);
        sheet.getRow(0).createCell(1).setCellValue(new Double(baseInfo.getIk()));
        sheet.getRow(0).getCell(1).setCellStyle(styleIk);

        sheet.createRow(1).createCell(0).setCellValue(KH_NAME);
        sheet.getRow(1).createCell(1).setCellValue(hospitalName);

        sheet.createRow(2).createCell(0).setCellValue(KH_TOWN);
        sheet.getRow(2).createCell(1).setCellValue(hospitalTown);

        // create dateformat Cellstyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        CreationHelper createHelper = sheet.getWorkbook().getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));
        cellStyle.setAlignment(HorizontalAlignment.LEFT);

        sheet.createRow(4).createCell(0).setCellValue(SEND_AT);
        sheet.getRow(4).createCell(1).setCellValue(baseInfo.getSend());
        sheet.getRow(4).getCell(1).setCellStyle(cellStyle);

        //style bold
        // Create bold font style
        CellStyle style = sheet.getWorkbook().createCellStyle();

        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.LEFT);

        sheet.getRow(0).getCell(0).setCellStyle(style);
        sheet.getRow(1).getCell(0).setCellStyle(style);
        sheet.getRow(2).getCell(0).setCellStyle(style);
        sheet.getRow(4).getCell(0).setCellStyle(style);
    }

    private void setTableHeader(Sheet sheet) {
        CellStyle style = getCellStyleBorder(sheet);

        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row row = sheet.createRow(6);
        row.createCell(0).setCellValue(TABLE_HEADER_1);
        row.createCell(1).setCellValue(TABLE_HEADER_2);
        row.createCell(2).setCellValue(TABLE_HEADER_3);
        row.createCell(3).setCellValue(TABLE_HEADER_3_0);
        row.createCell(4).setCellValue(TABLE_HEADER_3_1);
        row.createCell(5).setCellValue(TABLE_HEADER_4);
        row.createCell(6).setCellValue(TABLE_HEADER_5);
        row.createCell(7).setCellValue(TABLE_HEADER_6);
        row.getCell(0).setCellStyle(style);
        row.getCell(1).setCellStyle(style);
        row.getCell(2).setCellStyle(style);
        row.getCell(3).setCellStyle(style);
        row.getCell(4).setCellStyle(style);
        row.getCell(5).setCellStyle(style);
        row.getCell(6).setCellStyle(style);
        row.getCell(7).setCellStyle(style);

    }

    private void setTable(Sheet sheet, DeptBaseInformation baseInfo) {
        int startRow = 7;
        int startCol = 0;

        CellStyle styleBorderd = getCellStyleBorder(sheet);

        baseInfo.getDepts().sort(Comparator.comparing(Dept::getDeptArea, Comparator.nullsLast(Comparator.naturalOrder())));

        for (Dept dept : baseInfo.getDepts()) {
            for (DeptStation deptStation : dept.getDeptStations()) {
                Row row = sheet.createRow(startRow);
                row.createCell(startCol).setCellValue(getAreaText(dept.getDeptArea()));
                row.createCell(startCol + 1).setCellValue(dept.getDeptNumber());
                row.createCell(startCol + 2).setCellValue(dept.getDeptName());
                row.createCell(startCol + 3).setCellValue(deptStation.getDeptNumber());
                row.createCell(startCol + 4).setCellValue(deptStation.getDeptName());
                row.createCell(startCol + 5).setCellValue(dept.getSensitiveArea());
                row.createCell(startCol + 6).setCellValue(deptStation.getStationName());
                row.createCell(startCol + 7).setCellValue(deptStation.getLocationCode());

                for (int i = 0; i < 8; i++) {
                    row.getCell(i).setCellStyle(styleBorderd);
                }

                startRow++;
            }
        }
    }

    private void setTableAfterTargetYear(Sheet sheet, DeptBaseInformation baseInfo) {
        int startRow = 7;
        int startCol = 0;

        CellStyle styleBorderd = getCellStyleBorder(sheet);

        for (Dept dept : baseInfo.getDepts()) {
            for (DeptStationsAfterTargetYear deptStationAfter : dept.getDeptsAftertargetYear()) {

                Row row = sheet.createRow(startRow);
                row.createCell(startCol).setCellValue(getAreaText(dept.getDeptArea()));
                row.createCell(startCol + 1).setCellValue(dept.getDeptNumber());
                row.createCell(startCol + 2).setCellValue(dept.getDeptName());
                row.createCell(startCol + 3).setCellValue(deptStationAfter.getSensitiveArea());
                row.createCell(startCol + 4).setCellValue(deptStationAfter.getStationNameTargetYear());
                row.createCell(startCol + 5).setCellValue(deptStationAfter.getLocationCodeTargetYear());
                row.createCell(startCol + 6).setCellValue(deptStationAfter.getDeptNumber());
                row.createCell(startCol + 7).setCellValue(deptStationAfter.getDeptName());
                row.createCell(startCol + 8).setCellValue(deptStationAfter.getStationName());
                row.createCell(startCol + 9).setCellValue(deptStationAfter.getLocationCode());
                row.createCell(startCol + 10).setCellValue(deptStationAfter.getComment());
                for (int i = 0; i < 11; i++) {
                    row.getCell(i).setCellStyle(styleBorderd);
                }

                startRow++;
            }
        }

    }

    private void setTableAfterTargetYearHeader(Sheet sheet, DeptBaseInformation baseInfo) {

        int startRow = 6;

        CellStyle style = getCellStyleBorder(sheet);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        style.setFont(font);

        Row row = sheet.createRow(startRow);
        row.createCell(0).setCellValue(TABLE_HEADER_1);
        row.createCell(1).setCellValue(TABLE_HEADER_2);
        row.createCell(2).setCellValue(TABLE_HEADER_3);
        row.createCell(3).setCellValue(TABLE_HEADER_4);
        row.createCell(4).setCellValue(TABLE_HEADER_5);
        row.createCell(5).setCellValue(TABLE_HEADER_6);
        row.createCell(6).setCellValue(TABLE_HEADER_7);
        row.createCell(7).setCellValue(TABLE_HEADER_8);
        row.createCell(8).setCellValue(TABLE_HEADER_9);
        row.createCell(9).setCellValue(TABLE_HEADER_10);
        row.createCell(10).setCellValue(TABLE_HEADER_11);
        row.getCell(0).setCellStyle(style);
        row.getCell(1).setCellStyle(style);
        row.getCell(2).setCellStyle(style);
        row.getCell(3).setCellStyle(style);
        row.getCell(4).setCellStyle(style);
        row.getCell(5).setCellStyle(style);
        row.getCell(6).setCellStyle(style);
        row.getCell(7).setCellStyle(style);
        row.getCell(8).setCellStyle(style);
        row.getCell(9).setCellStyle(style);
        row.getCell(10).setCellStyle(style);
    }

    private String getAreaText(int area) {
        switch (area) {
            case 1:
                return BEZUG_1;
            case 2:
                return BEZUG_2;
            case 3:
                return BEZUG_3;
            default:
                return "";
        }
    }

    private CellStyle getCellStyleBorder(Sheet sheet) {
        CellStyle style = sheet.getWorkbook().createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void autoSizeCols(Sheet sheet) {
        for (int i = 0; i < 11; i++) {
            sheet.autoSizeColumn(i);
        }
    }

}
