package org.inek.dataportal.care.utils;

import org.apache.poi.ss.usermodel.*;
import org.inek.dataportal.care.entities.Proof;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;
import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.SensitiveArea;
import org.inek.dataportal.care.enums.Shift;

import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProofImporter {

    private static final Logger LOGGER = Logger.getLogger(ProofImporter.class.getName());
    private String _message = "";

    public ProofImporter() {

    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        this._message = message;
    }

    public Boolean handleProofUpload(ProofRegulationBaseInformation info, InputStream input) {
        return startImport(info, input);
    }

    private Boolean startImport(ProofRegulationBaseInformation info, InputStream file) {
        LOGGER.log(Level.INFO, "Start Proof import: " + info.getIk() + " " + info.getYear());

        try (Workbook workbook = WorkbookFactory.create(file)) {
            if (!workbookIsInCorrectFormat(workbook)) {
                LOGGER.log(Level.INFO, "Excel has wrong format");
                return false;
            }
            Sheet sheet = workbook.getSheetAt(0);
            importSheet(info, sheet);

            if ("".equals(_message)) {
                addMessage("Alle Zeilen erfolgreich eingelesen.");
            }

            return true;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Fehler beim Proof Import", ex);
            return false;
        }
    }

    private void importSheet(ProofRegulationBaseInformation info, Sheet sheet) {
        LOGGER.log(Level.INFO, "Start Sheet " + sheet.getSheetName());
        int rowStart = 1;
        int rowEnd = getEndRow(sheet);

        for (int i = rowStart; i <= rowEnd; i++) {
            Optional<Proof> proofFromRow = getProofFromRow(info, sheet.getRow(i));

            if (proofFromRow.isPresent()) {
                proofFromRow.get().setCountShift(getIntFromCell(sheet.getRow(i).getCell(7)));
                proofFromRow.get().setNurse(getDoubleFromCell(sheet.getRow(i).getCell(8), true));
                proofFromRow.get().setHelpNurse(getDoubleFromCell(sheet.getRow(i).getCell(9), true));
                proofFromRow.get().setPatientOccupancy(getDoubleFromCell(sheet.getRow(i).getCell(10), true));
                proofFromRow.get().setCountShiftNotRespected(getIntFromCell(sheet.getRow(i).getCell(11)));
            } else {
                LOGGER.log(Level.WARNING, "Row " + i + " no matching proof found");
                addMessage("Keinen passenden Eintrag gefunden. Zeile: " + i);
            }
        }
        LOGGER.log(Level.INFO, "End Proof import: " + info.getIk() + " " + info.getYear());
    }

    private Optional<Proof> getProofFromRow(ProofRegulationBaseInformation info, Row row) {
        Optional<Proof> first = info.getProofs().stream()
                .filter(c -> c.getProofRegulationStation().getSensitiveArea() == SensitiveArea.getByName(getStringFromCell(row.getCell(0))))
                .filter(c -> c.getProofRegulationStation().getFabNumber().equals(getFabNumberFromCell(row.getCell(1))))
                .filter(c -> c.getProofRegulationStation().getFabName().equals(getStringFromCell(row.getCell(2))))
                .filter(c -> c.getProofRegulationStation().getStationName().equals(getStringFromCell(row.getCell(3))))
                .filter(c -> c.getProofRegulationStation().getLocationCode().equals(getLocationFromCell(row.getCell(4))))
                .filter(c -> c.getMonth() == Months.getByName(getStringFromCell(row.getCell(5))))
                .filter(c -> c.getShift() == Shift.getByName(getStringFromCell(row.getCell(6))))
                .findFirst();
        return first;
    }

    private String getLocationFromCell(Cell cell) {
        try {
            int valueInt = (int) cell.getNumericCellValue();
            return String.valueOf(valueInt);
        } catch (Exception ex) {
            return cell.getStringCellValue();
        }
    }

    private String getFabNumberFromCell(Cell cell) {
        try {
            int valueInt = (int) cell.getNumericCellValue();
            return String.valueOf(valueInt);
        } catch (Exception ex) {
            return cell.getStringCellValue();
        }
    }

    private boolean workbookIsInCorrectFormat(Workbook workbook) {
        LOGGER.log(Level.INFO, "Check excel format");
        return workbook.getNumberOfSheets() == 1;
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
            addMessage("Wert konnte nicht eingelesen werden. Zelle: " + cell.getAddress());
            LOGGER.log(Level.WARNING, "Error gettin String from : " + cell.getAddress());
            return "";
        }
    }

    private int getIntFromCell(Cell cell) {
        try {
            double value = cell.getNumericCellValue();
            return (int) value;
        } catch (Exception ex) {
            addMessage("Wert konnte nicht als Zahl eingelesen werden. Zelle: " + cell.getAddress());
            LOGGER.log(Level.WARNING, "Error gettin Int from : " + cell.getAddress());
            return 0;
        }
    }

    private double getDoubleFromCell(Cell cell, Boolean round) {
        try {
            double numericCellValue = cell.getNumericCellValue();
            if (round) {
                return Math.round(numericCellValue * 100d) / 100d;
            }
            return cell.getNumericCellValue();
        } catch (Exception ex) {
            addMessage("Wert konnte nicht als Dezimalwert eingelesen werden. Zelle: " + cell.getAddress());
            LOGGER.log(Level.WARNING, "Error gettin Double from : " + cell.getAddress());
            return 0;
        }
    }

    private void addMessage(String message) {
        setMessage(getMessage() + message + "\n");
    }
}
