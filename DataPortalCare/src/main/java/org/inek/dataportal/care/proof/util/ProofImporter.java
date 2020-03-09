package org.inek.dataportal.care.proof.util;

import org.apache.poi.ss.usermodel.*;
import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;

import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ProofImporter {

    protected static final Logger LOGGER = Logger.getLogger(ProofImporter.class.getName());
    public static final String IMPLAUSIBLE = " ist unplausibel";
    private String _message = "";
    private int _rowCounter = 0;
    private boolean _isCommentAllowed = false;

    public ProofImporter(boolean isCommentAllowed) {
        _isCommentAllowed = isCommentAllowed;
    }

    public String getMessage() {
        return _message;
    }

    public void setMessage(String message) {
        this._message = message;
    }

    public boolean handleProofUpload(ProofRegulationBaseInformation info, InputStream input) {
        return startImport(info, input);
    }

    protected void incrementRowCount() {
        _rowCounter++;
    }

    protected boolean startImport(ProofRegulationBaseInformation info, InputStream file) {
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

    protected void importSheet(ProofRegulationBaseInformation info, Sheet sheet) {
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

    protected abstract void fillRowToProof(Row row, Proof proofFromRow);


    protected abstract void checkProofIsValid(Proof proof, Row row) throws InvalidValueException;

    protected void checkCountBeds(double value, Cell cell, Proof proof) throws InvalidValueException {
    }

    protected void checkCountShift(int value, Cell cell, Proof proof) throws InvalidValueException {
        if (value < 0 || value > 31) {
            throw new InvalidValueException("Die Anzahl der Schichten in Zelle " + cell.getAddress() + IMPLAUSIBLE);
        }
    }

    protected void checkOccupancyDays(int value, Cell cell, Proof proof) throws InvalidValueException {
    }

    protected void checkNurse(double value, Cell cell) throws InvalidValueException {
        if (value < 0 || value > 999) {
            throw new InvalidValueException("Die durchschnittliche Pflegepersonalausstattung Pflegefachkräfte in Zelle "
                    + cell.getAddress() + IMPLAUSIBLE);
        }
    }

    protected void checkHelpNurse(double value, Cell cell) throws InvalidValueException {
        if (value < 0 || value > 999) {
            throw new InvalidValueException("Die durchschnittliche Pflegepersonalausstattung Pflegehilfskräfte in Zelle "
                    + cell.getAddress() + IMPLAUSIBLE);
        }
    }

    protected void checkPatientOccupancy(double value, Cell cell) throws InvalidValueException {
        if (value < 0 || value > 999) {
            throw new InvalidValueException("Die Anzahl der durchschnitlichen durchschnittliche Patientenbelegung in Zelle "
                    + cell.getAddress() + IMPLAUSIBLE);
        }
    }

    protected void checkCountShiftNotRespected(double value, Cell cell, Proof proof) throws InvalidValueException {
        if (value < 0 || value > 31) {
            throw new InvalidValueException("Die Anzahl Schichten, in denen die PPUG im Monat nicht eingehalten wurde in Zelle "
                    + cell.getAddress() + IMPLAUSIBLE);
        }
    }

    protected abstract Optional<Proof> getProofFromRow(ProofRegulationBaseInformation info, Row row);

    protected String getLocationFromCell(Cell cell) {
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

    protected String getCommentFromCell(Cell cell) {
        if (cell == null) {
            return "";
        } else {
            String stringFromCell = getStringFromCell(cell);
            if (!"".equals(stringFromCell) && !_isCommentAllowed) {
                LOGGER.log(Level.INFO, "Using comment is not allowed for ik. Adress:" + cell.getAddress());
                addMessage("Kommentarspalte ist nur für Bundeswehkrankenhäuser. Wert an Position " + cell.getAddress() + " wird ignoriert");
                return "";
            } else {
                return stringFromCell;
            }
        }
    }

    protected boolean workbookIsInCorrectFormat(Workbook workbook) {
        LOGGER.log(Level.INFO, "Check excel format");
        if (workbook.getNumberOfSheets() > 1) {
            addMessage("Die Exceldatei hat mehr als 1 Blatt");
            return false;
        }

        int maxRow = workbook.getSheetAt(0).getLastRowNum();

        for (int i = 0; i < maxRow; i++) {
            if (workbook.getSheetAt(0).getRow(i) == null) {
                continue;
            }
            if (workbook.getSheetAt(0).getRow(i).getLastCellNum() < 12) {
                addMessage("Nicht genug Spalten in Zeile " + (i + 1));
            }
        }

        return _message.isEmpty();
    }

    protected int getEndRow(Sheet sheet) {
        LOGGER.log(Level.INFO, sheet.getSheetName() + " EndRows: " + sheet.getLastRowNum());
        return sheet.getLastRowNum();
    }

    protected String getStringFromCell(Cell cell) {
        try {
            String value = cell.getStringCellValue();
            if (value == null) {
                return "";
            }
            return value;
        } catch (Exception ex) {
            try {
                return "" + cell.getNumericCellValue();
            } catch (Exception ex2) {
                addMessage("Wert konnte nicht eingelesen werden. Zelle: " + cell.getAddress());
                LOGGER.log(Level.WARNING, "Error getting String from : " + cell.getAddress());
                return "";
            }
        }
    }

    protected int getIntFromCell(Cell cell) throws InvalidValueException {
        try {
            double value = cell.getNumericCellValue();
            return (int) value;
        } catch (Exception ex) {
            try {
                String cellValue = cell.getStringCellValue();
                cellValue = cellValue.replace(',', '.');

                double numberValue = Double.parseDouble(cellValue);

                return (int) numberValue;
            } catch (Exception ex2) {
                LOGGER.log(Level.WARNING, "Error getting Int from : " + cell.getAddress());
                throw new InvalidValueException("Wert in Zelle " + cell.getAddress() + " konnte nicht als gültige Ganzzahl erkannt werden");
            }
        }
    }

    protected double getDoubleFromCell(Cell cell, boolean round) throws InvalidValueException {
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
            } catch (Exception ex2) {
                LOGGER.log(Level.WARNING, "Error getting Double from : " + cell.getAddress());
                throw new InvalidValueException("Wert in Zelle " + cell.getAddress() + " konnte nicht als gültige Dezimalzahl erkannt werden");
            }
        }
    }

    protected void addMessage(String message) {
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
