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
    private int _rowCounter = 0;

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

            addMessage("Zeilen erfolgreich eingelesen: " + _rowCounter);

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
                fillRowToProof(sheet.getRow(i), proofFromRow.get());
            } else {
                LOGGER.log(Level.WARNING, "Row " + i + " no matching proof found");
                addMessage("Keinen passenden Eintrag gefunden. Zeile: " + i + 1);
            }
        }
        LOGGER.log(Level.INFO, "End Proof import: " + info.getIk() + " " + info.getYear());
    }

    private void fillRowToProof(Row row, Proof proofFromRow) {
        try {
            Proof tmpProof = new Proof();

            tmpProof.setCountShift(getIntFromCell(row.getCell(7)));
            tmpProof.setNurse(getDoubleFromCell(row.getCell(8), true));
            tmpProof.setHelpNurse(getDoubleFromCell(row.getCell(9), true));
            tmpProof.setPatientOccupancy(getDoubleFromCell(row.getCell(10), true));
            tmpProof.setCountShiftNotRespected(getIntFromCell(row.getCell(11)));

            proofFromRow.setCountShift(tmpProof.getCountShift());
            proofFromRow.setNurse(tmpProof.getNurse());
            proofFromRow.setHelpNurse(tmpProof.getHelpNurse());
            proofFromRow.setPatientOccupancy(tmpProof.getPatientOccupancy());
            proofFromRow.setCountShiftNotRespected(tmpProof.getCountShiftNotRespected());
            _rowCounter++;
        } catch (InvalidValueException ex) {
            addMessage(ex.getMessage());
        }
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
        if (workbook.getNumberOfSheets() > 1) {
            addMessage("Die Exceldatei hat mehr als 1 Blatt");
        }
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
            LOGGER.log(Level.WARNING, "Error getting String from : " + cell.getAddress());
            return "";
        }
    }

    private int getIntFromCell(Cell cell) throws InvalidValueException {
        try {
            double value = cell.getNumericCellValue();
            return (int) value;
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error getting Int from : " + cell.getAddress());
            throw new InvalidValueException("Wert in Zelle " + cell.getAddress() + " konnte nicht als gültige Ganzzahl erkannt werden");
        }
    }

    private double getDoubleFromCell(Cell cell, Boolean round) throws InvalidValueException {
        try {
            double numericCellValue = cell.getNumericCellValue();
            if (round) {
                return Math.round(numericCellValue * 100d) / 100d;
            }
            return cell.getNumericCellValue();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error getting Double from : " + cell.getAddress());
            throw new InvalidValueException("Wert in Zelle " + cell.getAddress() + " konnte nicht als gültige Dezimalzahl erkannt werden");
        }
    }

    private void addMessage(String message) {
        setMessage(getMessage() + message + "\n");
    }

    class InvalidValueException extends Exception {

        InvalidValueException() {

        }

        InvalidValueException(String message) {
            super(message);
        }

        InvalidValueException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
