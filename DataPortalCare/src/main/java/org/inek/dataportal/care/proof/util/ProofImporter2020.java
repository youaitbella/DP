package org.inek.dataportal.care.proof.util;

import org.apache.poi.ss.usermodel.Row;
import org.inek.dataportal.care.enums.Months;
import org.inek.dataportal.care.enums.Shift;
import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;

import java.util.Optional;

public class ProofImporter2020 extends ProofImporter {

    // Cell numbers
    private static final int CELL_SENSITIVEAREA = 0;
    private static final int CELL_DEPTNUMBERS = 1;
    private static final int CELL_DEPTNAMES = 2;
    private static final int CELL_WARDNAME = 3;
    private static final int CELL_LOCATION_P21 = 4;
    private static final int CELL_LOCATION_NUMBER = 5;
    private static final int CELL_MONTH = 6;
    private static final int CELL_SHIFT = 7;
    private static final int CELL_COUNT_BEDS = 8;
    private static final int CELL_COUNT_SHIFT = 9;
    private static final int CELL_OCCUPANCY_DAYS = 10;
    private static final int CELL_NURSE = 11;
    private static final int CELL_HELPNURSE = 12;
    private static final int CELL_PATIENT_OCCUPANCY = 13;
    private static final int CELL_COUNT_SHIFT_NOT_RESPECTED = 14;
    private static final int CELL_COMMENT = 15;

    public ProofImporter2020(boolean isCommentAllowed) {
        super(isCommentAllowed);
    }

    @Override
    protected void fillRowToProof(Row row, Proof proofFromRow) {
        try {
            Proof tmpProof = new Proof();

            tmpProof.setBeds(getIntFromCell(row.getCell(CELL_COUNT_BEDS)));
            tmpProof.setCountShift(getIntFromCell(row.getCell(CELL_COUNT_SHIFT)));
            tmpProof.setOccupancyDays(getIntFromCell(row.getCell(CELL_OCCUPANCY_DAYS)));
            tmpProof.setNurse(getDoubleFromCell(row.getCell(CELL_NURSE), true));
            tmpProof.setHelpNurse(getDoubleFromCell(row.getCell(CELL_HELPNURSE), true));
            tmpProof.setPatientOccupancy(getDoubleFromCell(row.getCell(CELL_PATIENT_OCCUPANCY), true));
            tmpProof.setCountShiftNotRespected(getIntFromCell(row.getCell(CELL_COUNT_SHIFT_NOT_RESPECTED)));
            tmpProof.setComment(getCommentFromCell(row.getCell(CELL_COMMENT)));

            checkProofIsValid(tmpProof, row);

            proofFromRow.setBeds(tmpProof.getBeds());
            proofFromRow.setCountShift(tmpProof.getCountShift());
            proofFromRow.setOccupancyDays(tmpProof.getOccupancyDays());
            proofFromRow.setNurse(tmpProof.getNurse());
            proofFromRow.setHelpNurse(tmpProof.getHelpNurse());
            proofFromRow.setPatientOccupancy(tmpProof.getPatientOccupancy());
            proofFromRow.setCountShiftNotRespected(tmpProof.getCountShiftNotRespected());
            proofFromRow.setComment(tmpProof.getComment());
            incrementRowCount();
        } catch (InvalidValueException ex) {
            addMessage(ex.getMessage());
        }
    }


    @Override
    protected void checkProofIsValid(Proof proof, Row row) throws InvalidValueException {
        checkCountBeds(proof.getBeds(), row.getCell(CELL_COUNT_BEDS), proof);
        checkCountShift(proof.getCountShift(), row.getCell(CELL_COUNT_SHIFT), proof);
        checkOccupancyDays(proof.getOccupancyDays(), row.getCell(CELL_OCCUPANCY_DAYS), proof);
        checkNurse(proof.getNurse(), row.getCell(CELL_NURSE));
        checkHelpNurse(proof.getHelpNurse(), row.getCell(CELL_HELPNURSE));
        checkPatientOccupancy(proof.getPatientOccupancy(), row.getCell(CELL_PATIENT_OCCUPANCY));
        checkCountShiftNotRespected(proof.getCountShiftNotRespected(), row.getCell(CELL_COUNT_SHIFT_NOT_RESPECTED), proof);
    }

    @Override
    protected Optional<Proof> getProofFromRow(ProofRegulationBaseInformation info, Row row) {
        if (row == null || row.getCell(CELL_SENSITIVEAREA) == null) {
            return Optional.empty();
        }
        Optional<Proof> first = info.getProofs().stream()
                .filter(p -> p.getSignificantSensitiveDomain().getName().equalsIgnoreCase(getStringFromCell(row.getCell(CELL_SENSITIVEAREA))))
                .filter(p -> p.getDeptNumbers().equals(getStringFromCell(row.getCell(CELL_DEPTNUMBERS))))
                .filter(p -> p.getDeptNames().equals(getStringFromCell(row.getCell(CELL_DEPTNAMES))))
                .filter(p -> p.getProofWard().getName().equals(getStringFromCell(row.getCell(CELL_WARDNAME))))
                .filter(p -> p.getProofWard().getLocationP21().equals(getLocationFromCell(row.getCell(CELL_LOCATION_P21))))
                .filter(p -> {
                    try {
                        return p.getProofWard().getLocationNumber() == getIntFromCell(row.getCell(CELL_LOCATION_NUMBER));
                    } catch (InvalidValueException e) {
                        return false;
                    }
                })
                .filter(p -> p.getMonth() == Months.getByName(getStringFromCell(row.getCell(CELL_MONTH))))
                .filter(p -> p.getShift() == Shift.getByName(getStringFromCell(row.getCell(CELL_SHIFT))))
                .findFirst();
        return first;
    }

}
