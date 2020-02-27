package org.inek.dataportal.care.proof.util;

import org.apache.poi.ss.usermodel.Row;
import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;

import java.util.Optional;
import java.util.logging.Logger;

public class ProofImporter2020 extends ProofImporter {

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


    private static final Logger LOGGER = Logger.getLogger(ProofImporter2020.class.getName());
    public static final String IMPLAUSIBLE = " ist unplausibel";
    private String _message = "";
    private int _rowCounter = 0;
    private boolean _isCommentAllowed = false;

    public ProofImporter2020(boolean isCommentAllowed) {
        super(isCommentAllowed);
    }

    @Override
    protected void fillRowToProof(Row row, Proof proofFromRow) {
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


    @Override
    protected void checkProofIsValid(Proof proof, Row row) throws InvalidValueException {
        checkCountShift(proof.getCountShift(), row.getCell(CELL_COUNT_SHIFT));
        checkNurse(proof.getNurse(), row.getCell(CELL_NURSE));
        checkHelpNurse(proof.getHelpNurse(), row.getCell(CELL_HELPNURSE));
        checkPatientOccupancy(proof.getPatientOccupancy(), row.getCell(CELL_PATIENT_OCCUPANCY));
        checkCountShiftNotRespected(proof.getCountShiftNotRespected(), row.getCell(CELL_COUNT_SHIFT_NOT_RESPECTED));
    }

    @Override
    protected Optional<Proof> getProofFromRow(ProofRegulationBaseInformation info, Row row) {
        if (row == null || row.getCell(CELL_SENSITIVEAREA) == null) {
            return Optional.empty();
        }
        Optional<Proof> first = info.getProofs().stream()
                .filter(p -> p.getSignificantSensitiveDomain().getName().equalsIgnoreCase(getStringFromCell(row.getCell(CELL_SENSITIVEAREA))))
                .filter(p -> p.getDeptNumbers().equals(getFabNumberFromCell(row.getCell(CELL_FABNUMBER))))
                .filter(p -> p.getDeptNames().equals(getStringFromCell(row.getCell(CELL_FABNAME))))
                .filter(p -> p.getProofWard().getName().equals(getStringFromCell(row.getCell(CELL_STATIONNAME))))
                .filter(p -> p.getProofWard().getLocationP21().equals(getLocationFromCell(row.getCell(CELL_LOCATION_CODE))))
                .filter(p -> p.getMonth() == Months.getByName(getStringFromCell(row.getCell(CELL_MONTH))))
                .filter(p -> p.getShift() == Shift.getByName(getStringFromCell(row.getCell(CELL_SHIFT))))
                .findFirst();
        return first;
    }

}
