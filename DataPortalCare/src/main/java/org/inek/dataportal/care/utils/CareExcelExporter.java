/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptBaseInformation;
import org.inek.dataportal.care.entities.DeptStation;

/**
 *
 * @author lautenti
 */
public class CareExcelExporter {

    private static final String SHEET_NAME = "Umsetzung PpUGV";
    private static final String IK = "IK-Nummer:";
    private static final String KH_NAME = "Name des Krankenhauses:";
    private static final String KH_TOWN = "Ort:";
    private static final String SEND_AT = "Übermittlung an das InEK:";

    private static final String TABLE_HEADER_1 = "Bezug PpUGV";
    private static final String TABLE_HEADER_2 = "Fachabteilungsschlüssel (2017)";
    private static final String TABLE_HEADER_3 = "Fachabteilungsname (2017)";
    private static final String TABLE_HEADER_4 = "pflegesensitiver Bereich";
    private static final String TABLE_HEADER_5 = "Stationsname (2017)";
    private static final String TABLE_HEADER_6 = "Standort (2017)";

    private static final String BEZUG_1 = "ausgewiesene Fachabteilung";
    private static final String BEZUG_2 = "Indikatoren-DRGs";
    private static final String BEZUG_3 = "OPS-Liste Intensivmedizin";

    public InputStream createExcelExportFile(DeptBaseInformation baseInfo, String hospitalName, String hospitalTown) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet(SHEET_NAME);

            setBaseInformations(sheet, baseInfo, hospitalName, hospitalTown);
            setTableHeader(sheet);
            setTable(sheet, baseInfo);
            autoSizeCols(sheet);
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

        sheet.createRow(0).createCell(0).setCellValue(IK);
        sheet.getRow(0).createCell(1).setCellValue(new Double(baseInfo.getIk()));

        sheet.createRow(1).createCell(0).setCellValue(KH_NAME);
        sheet.getRow(1).createCell(1).setCellValue(hospitalName);

        sheet.createRow(2).createCell(0).setCellValue(KH_TOWN);
        sheet.getRow(2).createCell(1).setCellValue(hospitalTown);

        // create dateformat Cellstyle
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        CreationHelper createHelper = sheet.getWorkbook().getCreationHelper();
        cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy"));

        sheet.createRow(4).createCell(0).setCellValue(SEND_AT);
        sheet.getRow(4).createCell(1).setCellValue(baseInfo.getSend());
        sheet.getRow(4).getCell(1).setCellStyle(cellStyle);

        //style bold
        // Create bold font style
        CellStyle style = sheet.getWorkbook().createCellStyle();

        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        style.setFont(font);
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

        Row row = sheet.createRow(6);
        row.createCell(0).setCellValue(TABLE_HEADER_1);
        row.createCell(1).setCellValue(TABLE_HEADER_2);
        row.createCell(2).setCellValue(TABLE_HEADER_3);
        row.createCell(3).setCellValue(TABLE_HEADER_4);
        row.createCell(4).setCellValue(TABLE_HEADER_5);
        row.createCell(5).setCellValue(TABLE_HEADER_6);
        row.getCell(0).setCellStyle(style);
        row.getCell(1).setCellStyle(style);
        row.getCell(2).setCellStyle(style);
        row.getCell(3).setCellStyle(style);
        row.getCell(4).setCellStyle(style);
        row.getCell(5).setCellStyle(style);
    }

    private void setTable(Sheet sheet, DeptBaseInformation baseInfo) {

        int startRow = 7;
        int startCol = 0;

        for (Dept dept : baseInfo.getDepts()) {
            for (DeptStation deptStation : dept.getDeptStations()) {
                Row row = sheet.createRow(startRow);
                row.createCell(startCol).setCellValue(getAreaText(dept.getDeptArea()));
                row.getCell(startCol).setCellStyle(getCellStyleBorder(sheet));
                row.createCell(startCol + 1).setCellValue(dept.getDeptNumber());
                row.getCell(startCol + 1).setCellStyle(getCellStyleBorder(sheet));
                row.createCell(startCol + 2).setCellValue(dept.getDeptName());
                row.getCell(startCol + 2).setCellStyle(getCellStyleBorder(sheet));
                row.createCell(startCol + 3).setCellValue(dept.getSensitiveArea());
                row.getCell(startCol + 3).setCellStyle(getCellStyleBorder(sheet));
                row.createCell(startCol + 4).setCellValue(deptStation.getStationName());
                row.getCell(startCol + 4).setCellStyle(getCellStyleBorder(sheet));
                row.createCell(startCol + 5).setCellValue(deptStation.getLocationCode());
                row.getCell(startCol + 5).setCellStyle(getCellStyleBorder(sheet));
                startRow++;
            }
        }

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
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setBorderRight(BorderStyle.MEDIUM);
        return style;
    }

    private void autoSizeCols(Sheet sheet) {
        for (int i = 0; i < 11; i++) {
            sheet.autoSizeColumn(i);
        }
    }

}
