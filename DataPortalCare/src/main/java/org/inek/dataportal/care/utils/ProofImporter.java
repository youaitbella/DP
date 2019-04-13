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

    // Cell numbers
    private static final int CELL_SENSITIVEAREA = 0;
    private static final int CELL_FABNUMBER = 1;
    private static final int CELL_FABNAME = 2;
    private static final int CELL_STATIONNAME = 3;
    private static final int CELL_LOCATION_CODE = 4;
    private static final int CELL_MONTH = 5;
    private static final int CELL_SHIFT = 6;
    private static final int CELL_COUNT_SHIFT = 7;
    private static final int CELL_NURSE = 8;
    private static final int CELL_HELPNURSE = 9;
    private static final int CELL_PATIENT_OCCUPANCY = 10;
    private static final int CELL_COUNT_SHIFT_NOT_RESPECTED = 11;
    private static final int CELL_COMMENT = 12;


    private static final Logger LOGGER = Logger.getLogger(ProofImporter.class.getName());
    private String _message = "";
    private int _rowCounter = 0;
    private Boolean _isBwHospital = false;

    public ProofImporter(boolean isBwHospital) {
        _isBwHospital = isBwHospital;
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
                addMessage("Keinen passenden Eintrag gefunden. Zeile: " + (i + 1));
            }
        }
        LOGGER.log(Level.INFO, "End Proof import: " + info.getIk() + " " + info.getYear());
    }

    private void fillRowToProof(Row row, Proof proofFromRow) {
        try {
            Proof tmpProof = new Proof();

            tmpProof.setCountShift(getIntFromCell(row.getCell(CELL_COUNT_SHIFT)));
            tmpProof.setNurse(getDoubleFromCell(row.getCell(CELL_NURSE), true));
            tmpProof.setHelpNurse(getDoubleFromCell(row.getCell(CELL_HELPNURSE), true));
            tmpProof.setPatientOccupancy(getDoubleFromCell(row.getCell(CELL_PATIENT_OCCUPANCY), true));
            tmpProof.setCountShiftNotRespected(getIntFromCell(row.getCell(CELL_COUNT_SHIFT_NOT_RESPECTED)));
            tmpProof.setComment(getCommentFromCell(row.getCell(CELL_COMMENT)));

            checkProofIsValid(tmpProof, row);

            proofFromRow.setCountShift(tmpProof.getCountShift());
            proofFromRow.setNurse(tmpProof.getNurse());
            proofFromRow.setHelpNurse(tmpProof.getHelpNurse());
            proofFromRow.setPatientOccupancy(tmpProof.getPatientOccupancy());
            proofFromRow.setCountShiftNotRespected(tmpProof.getCountShiftNotRespected());
            proofFromRow.setComment(tmpProof.getComment());
            _rowCounter++;
        } catch (InvalidValueException ex) {
            addMessage(ex.getMessage());
        }
    }


    private void checkProofIsValid(Proof proof, Row row) throws InvalidValueException {
        checkCountShift(proof.getCountShift(), row.getCell(CELL_COUNT_SHIFT));
        checkNurse(proof.getNurse(), row.getCell(CELL_NURSE));
        checkHelpNurse(proof.getHelpNurse(), row.getCell(CELL_HELPNURSE));
        checkPatientOccupancy(proof.getPatientOccupancy(), row.getCell(CELL_PATIENT_OCCUPANCY));
        checkCountShiftNotRespected(proof.getCountShiftNotRespected(), row.getCell(CELL_COUNT_SHIFT_NOT_RESPECTED));
    }

    private void checkCountShiftNotRespected(double value, Cell cell) throws InvalidValueException {
        if (value < 0 || value > 31) {
            throw new InvalidValueException("Die Anzahl Schichten, in denen die PPUG im Monat nicht eingehalten wurde in Zelle "
                    + cell.getAddress() + " ist unplausibel");
        }
    }

    private void checkPatientOccupancy(double value, Cell cell) throws InvalidValueException {
        if (value < 0 || value > 999) {
            throw new InvalidValueException("Die Anzahl der durchschnitlichen durchschnittliche Patientenbelegung in Zelle "
                    + cell.getAddress() + " ist unplausibel");
        }
    }

    private void checkHelpNurse(double value, Cell cell) throws InvalidValueException {
        if (value < 0 || value > 999) {
            throw new InvalidValueException("Die durchschnittliche Pflegepersonalausstattung Pflegehilfskräfte in Zelle "
                    + cell.getAddress() + " ist unplausibel");
        }
    }

    private void checkNurse(double value, Cell cell) throws InvalidValueException {
        if (value < 0 || value > 999) {
            throw new InvalidValueException("Die durchschnittliche Pflegepersonalausstattung Pflegefachkräfte in Zelle "
                    + cell.getAddress() + " ist unplausibel");
        }
    }

    private void checkCountShift(int value, Cell cell) throws InvalidValueException {
        if (value < 0 || value > 31) {
            throw new InvalidValueException("Die Anzahl der Schichten in Zelle " + cell.getAddress() + " ist unplausibel");
        }
    }

    private Optional<Proof>  getProofFromRow(ProofRegulationBaseInformation info, Row row) {
        if (row.getCell(CELL_SENSITIVEAREA) == null) {
            return Optional.empty();
        }
        Optional<Proof> first = info.getProofs().stream()
                .filter(c -> c.getProofRegulationStation().getSensitiveArea() ==
                        SensitiveArea.getByName(getStringFromCell(row.getCell(CELL_SENSITIVEAREA))))
                .filter(c -> c.getProofRegulationStation().getFabNumber().equals(getFabNumberFromCell(row.getCell(CELL_FABNUMBER))))
                .filter(c -> c.getProofRegulationStation().getFabName().equals(getStringFromCell(row.getCell(CELL_FABNAME))))
                .filter(c -> c.getProofRegulationStation().getStationName().equals(getStringFromCell(row.getCell(CELL_STATIONNAME))))
                .filter(c -> c.getProofRegulationStation().getLocationCode().equals(getLocationFromCell(row.getCell(CELL_LOCATION_CODE))))
                .filter(c -> c.getMonth() == Months.getByName(getStringFromCell(row.getCell(CELL_MONTH))))
                .filter(c -> c.getShift() == Shift.getByName(getStringFromCell(row.getCell(CELL_SHIFT))))
                .findFirst();
        return first;
    }

    private String getLocationFromCell(Cell cell) {
        if (cell == null) {
            return "";
        }
        try {
            int valueInt = (int) cell.getNumericCellValue();
            if (valueInt == 0) {
                return "";
            }
            return String.valueOf(valueInt);
        } catch (Exception ex) {
            return cell.getStringCellValue();
        }
    }

    private String getFabNumberFromCell(Cell cell) {
        if (cell == null) {
            return "";
        }
        try {
            int valueInt = (int) cell.getNumericCellValue();
            if (valueInt == 0) {
                return "";
            }
            return String.valueOf(valueInt);
        } catch (Exception ex) {
            return cell.getStringCellValue();
        }
    }

    private String getCommentFromCell(Cell cell) {
        if (cell == null) {
            return "";
        }
        else {
            String stringFromCell = getStringFromCell(cell);
            if (!"".equals(stringFromCell) && !_isBwHospital) {
                LOGGER.log(Level.INFO, "Using comment is not allowed for ik. Adress:" + cell.getAddress());
                addMessage("Kommentarspalte ist nur für Bundeswehkrankenhäuser. Wert an Position " + cell.getAddress() + " wird ignoriert");
                return "";
            }
            else {
                return stringFromCell;
            }
        }
    }

    private boolean workbookIsInCorrectFormat(Workbook workbook) {
        LOGGER.log(Level.INFO, "Check excel format");
        if (workbook.getNumberOfSheets() > 1) {
            addMessage("Die Exceldatei hat mehr als 1 Blatt");
            return false;
        }

        int maxRow = workbook.getSheetAt(0).getLastRowNum();

        for (int i = 0 ; i < maxRow ; i++) {
            if (workbook.getSheetAt(0).getRow(i).getLastCellNum() < 12) {
                addMessage("Nicht genug Spalten in Zeile " + (i +1));
            }
        }

        return _message.isEmpty();
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
            try {
                String cellValue = cell.getStringCellValue();
                cellValue = cellValue.replace(',', '.');

                double numberValue = Double.parseDouble(cellValue);

                return (int)numberValue;
            }
            catch (Exception ex2){
                LOGGER.log(Level.WARNING, "Error getting Int from : " + cell.getAddress());
                throw new InvalidValueException("Wert in Zelle " + cell.getAddress() + " konnte nicht als gültige Ganzzahl erkannt werden");
            }
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
            try {
                String cellValue = cell.getStringCellValue();
                cellValue = cellValue.replace(',', '.');

                double numberValue = Double.parseDouble(cellValue);
                if (round) {
                    return Math.round(numberValue * 100d) / 100d;
                }
                return numberValue;
            }
            catch (Exception ex2){
                LOGGER.log(Level.WARNING, "Error getting Double from : " + cell.getAddress());
                throw new InvalidValueException("Wert in Zelle " + cell.getAddress() + " konnte nicht als gültige Dezimalzahl erkannt werden");
            }
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
