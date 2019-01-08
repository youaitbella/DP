/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.care.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.inek.dataportal.care.entities.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author lautenti
 */
public class ProofExcelExporter {

    private static final String SHEET_NAME_1 = "Vorlage";

    private static final String TABLE_HEADER_1 = "pflegesensitiver Bereich im Krankenhaus gem. § 3 PpUGV";
    private static final String TABLE_HEADER_2 = "Fachabteilungsschlüssel in §-21-Daten";
    private static final String TABLE_HEADER_3 = "Fachabteilung";
    private static final String TABLE_HEADER_4 = "Station";
    private static final String TABLE_HEADER_5 = "Standort";
    private static final String TABLE_HEADER_6 = "Monat";
    private static final String TABLE_HEADER_7 = "Schicht";
    private static final String TABLE_HEADER_8 = "Anzahl Schichten (Summe)";
    private static final String TABLE_HEADER_9 = "durchschnittliche Pflegepersonalausstattung Pflegefachkräfte";
    private static final String TABLE_HEADER_10 = "durchschnittliche Pflegepersonalausstattung Pflegehilfskräfte";
    private static final String TABLE_HEADER_11 = "durchschnittliche Patientenbelegung";
    private static final String TABLE_HEADER_12 = "Anzahl Schichten, in denen die PPUG im Monat nicht eingehalten wurde";

    public InputStream createExcelExportFile(ProofRegulationBaseInformation baseInfo) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet1 = workbook.createSheet(SHEET_NAME_1);

            setTableHeader(sheet1);
            setTable(sheet1, baseInfo);

            autoSizeCols(sheet1);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            byte[] barray = bos.toByteArray();
            return new ByteArrayInputStream(barray);
        } catch (Exception e) {

        }
        return null;

    }

    private void setTableHeader(Sheet sheet) {
        CellStyle style = getCellStyleBorder(sheet);

        Font font = sheet.getWorkbook().createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Row row = sheet.createRow(0);
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
        row.createCell(11).setCellValue(TABLE_HEADER_12);

        for (int i = 0; i < 12; i++) {
            row.getCell(i).setCellStyle(style);
        }

    }

    private void setTable(Sheet sheet, ProofRegulationBaseInformation baseInfo) {
        int startRow = 1;
        int startCol = 0;

        CellStyle styleBorderd = getCellStyleBorder(sheet);

        for (Proof proof : baseInfo.getProofs()) {
            Row row = sheet.createRow(startRow);
            row.createCell(startCol).setCellValue(proof.getProofRegulationStation().getSensitiveArea().getName());
            row.createCell(startCol + 1).setCellValue(proof.getProofRegulationStation().getFabNumber());
            row.createCell(startCol + 2).setCellValue(proof.getProofRegulationStation().getFabName());
            row.createCell(startCol + 3).setCellValue(proof.getProofRegulationStation().getStationName());
            row.createCell(startCol + 4).setCellValue(proof.getProofRegulationStation().getLocationCode());
            row.createCell(startCol + 5).setCellValue(proof.getMonth().getName());
            row.createCell(startCol + 6).setCellValue(proof.getShift().getName());
            row.createCell(startCol + 7).setCellValue(proof.getCountShift());

            row.createCell(startCol + 8).setCellValue(0.00);
            row.createCell(startCol + 9).setCellValue(0.00);
            row.createCell(startCol + 10).setCellValue(0.00);
            row.createCell(startCol + 11).setCellValue(0.00);

            for (int i = 0; i < 12; i++) {
                row.getCell(i).setCellStyle(styleBorderd);
            }

            startRow++;
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
        for (int i = 0; i < 12; i++) {
            sheet.autoSizeColumn(i);
        }
    }

}
