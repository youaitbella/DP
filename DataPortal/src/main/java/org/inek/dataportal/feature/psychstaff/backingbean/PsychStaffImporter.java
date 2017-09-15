/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.backingbean;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import org.inek.dataportal.feature.psychstaff.entity.StaffProof;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.feature.psychstaff.enums.PsychType;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.StringUtil;

/**
 *
 * @author muellermi
 */
public class PsychStaffImporter {

    public static String importAgreed(Part file, StaffProof staffProof, PsychType type) {
        String msg = "";
        int count = 0;
        try {
            Scanner scanner = new Scanner(file.getInputStream());
            if (!scanner.hasNextLine()) {
                return "Nichts zu importieren";
            }
            List<StaffProofAgreed> staffProofsAgreed = staffProof.getStaffProofsAgreed(type);
            while (scanner.hasNextLine()) {
                count++;
                String line = Utils.convertFromUtf8(scanner.nextLine());
                if (!line.contains("Bereich;Nummer;StellenVollstaendig;StellenBudget;Kosten")) {
                    try {
                        importAgreedLine(staffProofsAgreed, type, line);
                    } catch (Exception ex) {
                        msg += "Fehler in Zeile " + count + ": " + ex.getMessage() + "\r\n";
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PsychStaffImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        msg += "Ingesamt wurden " + count + " Zeilen gelesen";
        return msg;
    }

    private static void importAgreedLine(List<StaffProofAgreed> staffProofsAgreed, PsychType type, String line) {
        String[] data = StringUtil.splitAtUnquotedSemicolon(line);
        if (data.length != 5) {
            throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
        }
        if (PsychType.getTypeFromShortName(data[0]) != type) {
            throw new IllegalArgumentException("Unzulässige Bereichskennung. Erwartet: " + type.getShortName());
        }
        int number = Integer.parseInt(data[1]);
        if (number < 1 || number > 7) {
            throw new IllegalArgumentException("Die laufende Nummer der Berufsgruppe muss zwischen 1 und 7 liegen");
        }
        if (number == 6 && type == PsychType.Adults){
            throw new IllegalArgumentException("Die laufende Nummer 6 (Logopädie) ist nur bei KJP zulässig");
        }
        double staffingComplete = StringUtil.parseLocalizedDouble(data[2]);
        double staffingBudget = StringUtil.parseLocalizedDouble(data[3]);
        double avgCosts = StringUtil.parseLocalizedDouble(data[4]);
        StaffProofAgreed staffProofAgreed = staffProofsAgreed.stream().filter(a -> a.getOccupationalCategoryId() == number).findFirst().get();
        staffProofAgreed.setStaffingComplete(staffingComplete);
        staffProofAgreed.setStaffingBudget(staffingBudget);
        staffProofAgreed.setAvgCost(avgCosts);
    }

    public static String importEffective(Part file, StaffProof staffProof, PsychType type) {
        String msg = "";
        int count = 0;
        try {
            Scanner scanner = new Scanner(file.getInputStream());
            if (!scanner.hasNextLine()) {
                return "Nichts zu importieren";
            }
            List<StaffProofEffective> staffProofsEffective = staffProof.getStaffProofsEffective(type);
            while (scanner.hasNextLine()) {
                count++;
                String line = Utils.convertFromUtf8(scanner.nextLine());
                if (!line.contains("Bereich;Nummer;Absatz;Berufsgruppe;VK;Erlaeuterung")) {
                    try {
                        importEffectiveLine(staffProofsEffective, type, line);
                    } catch (Exception ex) {
                        msg += "Fehler in Zeile " + count + ": " + ex.getMessage() + "\r\n";
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PsychStaffImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        msg += "Ingesamt wurden " + count + " Zeilen gelesen";
        return msg;
    }

    private static void importEffectiveLine(List<StaffProofEffective> staffProofsEffective, PsychType type, String line) {
        String[] data = StringUtil.splitAtUnquotedSemicolon(line);
        if (data.length != 6) {
            throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
        }
        if (PsychType.getTypeFromShortName(data[0]) != type) {
            throw new IllegalArgumentException("Unzulässige Bereichskennung. Erwartet: " + type.getShortName());
        }
        int number = Integer.parseInt(data[1]);
        if (number < 1 || number > 7) {
            throw new IllegalArgumentException("Die laufende Nummer der Berufsgruppe muss zwischen 1 und 7 liegen");
        }
        if (number == 6 && type == PsychType.Adults){
            throw new IllegalArgumentException("Die laufende Nummer 6 (Logopädie) ist nur bei KJP zulässig");
        }
        double staffingComplete = StringUtil.parseLocalizedDouble(data[2]);
        double deductionPsych = StringUtil.parseLocalizedDouble(data[3]);
        double deductionNonPsych = number == 1 ? 0d : StringUtil.parseLocalizedDouble(data[4]);
        double deductionOther = StringUtil.parseLocalizedDouble(data[5]);
        StaffProofEffective staffProofEffective = staffProofsEffective
                .stream()
                .filter(a -> a.getOccupationalCategoryId() == number)
                .findFirst()
                .get();
        staffProofEffective.setStaffingComplete(staffingComplete);
        staffProofEffective.setStaffingDeductionPsych(deductionPsych);
        staffProofEffective.setStaffingDeductionNonPsych(deductionNonPsych);
        staffProofEffective.setStaffingDeductionOther(deductionOther);
    }

}