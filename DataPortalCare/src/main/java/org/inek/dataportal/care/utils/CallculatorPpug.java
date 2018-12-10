package org.inek.dataportal.care.utils;

import org.inek.dataportal.care.entities.Proof;

public class CallculatorPpug {

    public static void calculateAll(Proof proof) {
        calculatePatientPerNurse(proof, proof.getPart());
        calculateCountHelpeNurseChargeable(proof, proof.getPart());
    }

    public static void calculatePatientPerNurse(Proof proof, double part) {
        double nurse = proof.getNurse();
        double helpNurse = proof.getHelpNurse();
        double patientOccupancy = proof.getPatientOccupancy();

        if (nurse + helpNurse == 0) {
            proof.setPatientPerNurse(0);
            return;
        }
        part = 1 - part;

        double minValue = Math.min(nurse + helpNurse, nurse / part);
        double roundedValue = Math.round(minValue * 100.0) / 100.0;

        double result = patientOccupancy / roundedValue;
        proof.setPatientPerNurse(Math.round(result * 100.0) / 100.0);
    }

    public static void calculateCountHelpeNurseChargeable(Proof proof, double part) {
        double nurse = proof.getNurse();
        double helpNurse = proof.getHelpNurse();

        if (nurse + helpNurse == 0) {
            proof.setCountHelpeNurseChargeable(0);
            return;
        }
        part = 1 - part;

        double result = (nurse / part) - nurse;
        proof.setCountHelpeNurseChargeable(Math.round(result * 100.0) / 100.0);
    }
}
