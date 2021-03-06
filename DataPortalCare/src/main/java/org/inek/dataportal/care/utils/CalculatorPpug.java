package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.proof.entity.Proof;
import org.inek.dataportal.common.helper.MathHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculatorPpug {
    // todo: move calculation into Proof and perform automatically on property change?

    public static void calculateAll(Proof proof, double partNurse) {
        calculatePatientPerNurse(proof, partNurse);
        calculateCountHelpeNurseChargeable(proof, partNurse);
    }

    public static void calculatePatientPerNurse(Proof proof, double partNurse) {

        BigDecimal decNurse = new BigDecimal(String.valueOf(proof.getNurse())).setScale(2, RoundingMode.HALF_UP);
        BigDecimal decHelpNurse = new BigDecimal(String.valueOf(proof.getHelpNurse())).setScale(2, RoundingMode.HALF_UP);
        BigDecimal decPart = new BigDecimal(String.valueOf(partNurse)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal decPatientOccupancy = new BigDecimal(String.valueOf(proof.getPatientOccupancy())).setScale(2, RoundingMode.HALF_UP);

        if (decNurse.add(decHelpNurse).doubleValue() == 0 || decNurse.doubleValue() == 0) {
            proof.setPatientPerNurse(0);
            return;
        }

        BigDecimal maxAllowed = decNurse.divide(decPart, RoundingMode.HALF_UP);

        BigDecimal totalStaff = maxAllowed.min(decHelpNurse.add(decNurse));

        double result = decPatientOccupancy.divide(totalStaff, RoundingMode.HALF_UP).doubleValue();
        proof.setPatientPerNurse(MathHelper.round(result, 2));
    }

    public static void calculateCountHelpeNurseChargeable(Proof proof, double partNurse) {

        BigDecimal decNurse = new BigDecimal(String.valueOf(proof.getNurse())).setScale(2, RoundingMode.HALF_UP);
        BigDecimal decHelpNurse = new BigDecimal(String.valueOf(proof.getHelpNurse())).setScale(2, RoundingMode.HALF_UP);
        BigDecimal decPart = new BigDecimal(String.valueOf(partNurse)).setScale(2, RoundingMode.HALF_UP);


        if (decNurse.add(decHelpNurse).doubleValue() == 0) {
            proof.setCountHelpeNurseChargeable(0);
            return;
        }

        double result = decNurse.divide(decPart, RoundingMode.HALF_UP).subtract(decNurse).doubleValue();

        proof.setCountHelpeNurseChargeable(MathHelper.round(result, 2));
    }
}
