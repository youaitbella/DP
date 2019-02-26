/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.psychstaff.backingbean;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.Part;
import org.inek.dataportal.psy.psychstaff.entity.StaffProof;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.psy.psychstaff.entity.StaffProofExplanation;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.StringUtil;
import org.inek.dataportal.common.data.KhComparison.entities.OccupationalCategory;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author muellermi
 */
public class PsychStaffImporter {

    private static final String NOTHING_TO_IMPORT = "Nichts zu importieren";
    private static final String LIMIT_SEQ_OCCUPASIONAL_GROUP = "Die laufende Nummer der Berufsgruppe muss zwischen 1 und 7 liegen";
    private static final String NUMBER_6_VALID_FOR_KIDS_ONLY = "Die laufende Nummer 6 (Logopädie) ist nur bei KJP zulässig";
    private static final String ERROR_IN_LINE = "Fehler in Zeile ";

    public static String importAgreed(UploadedFile file, StaffProof staffProof, PsychType type) {
        String msg = "";
        int count = 0;
        try {
            Scanner scanner = new Scanner(file.getInputstream());
            if (!scanner.hasNextLine()) {
                return NOTHING_TO_IMPORT;
            }
            List<StaffProofAgreed> staffProofsAgreed = staffProof.getStaffProofsAgreed(type);
            while (scanner.hasNextLine()) {
                count++;
                String line = Utils.convertFromUtf8(scanner.nextLine());
                if (!line.contains("Bereich;Nummer;StellenVollstaendig;StellenBudget;Kosten")) {
                    try {
                        importAgreedLine(staffProofsAgreed, type, line);
                    } catch (Exception ex) {
                        msg += ERROR_IN_LINE + count + ": " + ex.getMessage() + "\r\n";
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
        int occupationalCategoryId = Integer.parseInt(data[1]);
        if (occupationalCategoryId < 1 || occupationalCategoryId > 7) {
            throw new IllegalArgumentException(LIMIT_SEQ_OCCUPASIONAL_GROUP);
        }
        if (occupationalCategoryId == 6 && type == PsychType.Adults) {
            throw new IllegalArgumentException(NUMBER_6_VALID_FOR_KIDS_ONLY);
        }
        double staffingComplete = StringUtil.parseLocalizedDouble(data[2]);
        double staffingBudget = StringUtil.parseLocalizedDouble(data[3]);
        double avgCosts = StringUtil.parseLocalizedDouble(data[4]);
        StaffProofAgreed staffProofAgreed = staffProofsAgreed
                .stream()
                .filter(a -> a.getOccupationalCategoryId() == occupationalCategoryId)
                .findFirst()
                .get();
        staffProofAgreed.setStaffingComplete(staffingComplete);
        staffProofAgreed.setStaffingBudget(staffingBudget);
        staffProofAgreed.setAvgCost(avgCosts);
    }

    public static String importEffective(UploadedFile file, StaffProof staffProof, PsychType type) {
        String msg = "";
        int count = 0;
        try {
            Scanner scanner = new Scanner(file.getInputstream());
            if (!scanner.hasNextLine()) {
                return NOTHING_TO_IMPORT;
            }
            List<StaffProofEffective> staffProofsEffective = staffProof.getStaffProofsEffective(type);
            while (scanner.hasNextLine()) {
                count++;
                String line = Utils.convertFromUtf8(scanner.nextLine());
                if (!line.contains("Bereich;Nummer;StellenVollstaendig;AnrechPsy;AnrechNonPsy;AnrechSonstig")) {
                    try {
                        importEffectiveLine(staffProofsEffective, type, line);
                    } catch (Exception ex) {
                        msg += ERROR_IN_LINE + count + ": " + ex.getMessage() + "\r\n";
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
        int occupationalCategoryId = Integer.parseInt(data[1]);
        if (occupationalCategoryId < 1 || occupationalCategoryId > 7) {
            throw new IllegalArgumentException(LIMIT_SEQ_OCCUPASIONAL_GROUP);
        }
        if (occupationalCategoryId == 6 && type == PsychType.Adults) {
            throw new IllegalArgumentException(NUMBER_6_VALID_FOR_KIDS_ONLY);
        }
        double staffingComplete = StringUtil.parseLocalizedDouble(data[2]);
        double deductionPsych = StringUtil.parseLocalizedDouble(data[3]);
        double deductionNonPsych = occupationalCategoryId == 1 ? 0d : StringUtil.parseLocalizedDouble(data[4]);
        double deductionOther = StringUtil.parseLocalizedDouble(data[5]);
        StaffProofEffective staffProofEffective = staffProofsEffective
                .stream()
                .filter(a -> a.getOccupationalCategoryId() == occupationalCategoryId)
                .findFirst()
                .get();
        staffProofEffective.setStaffingComplete(staffingComplete);
        staffProofEffective.setStaffingDeductionPsych(deductionPsych);
        staffProofEffective.setStaffingDeductionNonPsych(deductionNonPsych);
        staffProofEffective.setStaffingDeductionOther(deductionOther);
    }

    public static String importExplanation(UploadedFile file, StaffProof staffProof, PsychType type) {
        String msg = "";
        int count = 0;
        try {
            Scanner scanner = new Scanner(file.getInputstream());
            if (!scanner.hasNextLine()) {
                return NOTHING_TO_IMPORT;
            }
            while (scanner.hasNextLine()) {
                count++;
                String line = Utils.convertFromUtf8(scanner.nextLine());
                if (!line.contains("Bereich;Nummer;Absatz;Berufsgruppe;VK;Erlaeuterung")) {
                    try {
                        importExplanationLine(staffProof, type, line);
                    } catch (Exception ex) {
                        msg += ERROR_IN_LINE + count + ": " + ex.getMessage() + "\r\n";
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PsychStaffImporter.class.getName()).log(Level.SEVERE, null, ex);
        }
        msg += "Ingesamt wurden " + count + " Zeilen gelesen";
        return msg;
    }

    private static void importExplanationLine(StaffProof staffProof, PsychType type, String line) {
        String[] data = StringUtil.splitAtUnquotedSemicolon(line);
        if (data.length != 6) {
            throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
        }
        if (PsychType.getTypeFromShortName(data[0]) != type) {
            throw new IllegalArgumentException("Unzulässige Bereichskennung. Erwartet: " + type.getShortName());
        }
        int occupationalCategoryId = Integer.parseInt(data[1]);
        if (occupationalCategoryId < 1 || occupationalCategoryId > 7) {
            throw new IllegalArgumentException(LIMIT_SEQ_OCCUPASIONAL_GROUP);
        }
        if (occupationalCategoryId == 6 && type == PsychType.Adults) {
            throw new IllegalArgumentException(NUMBER_6_VALID_FOR_KIDS_ONLY);
        }
        OccupationalCategory occupationalCategory = obtainOccupationalCategory(staffProof, type, occupationalCategoryId);
        int paragraph = Integer.parseInt(data[2]);
        if (paragraph < 4 || paragraph > 6) {
            throw new IllegalArgumentException("Der Absatz muss zwischen 4 und 6 liegen");
        }
        double staffing = StringUtil.parseLocalizedDouble(data[4]);
        StaffProofExplanation explanation = new StaffProofExplanation();
        explanation.setStaffProofMasterId(staffProof.getId());
        explanation.setPsychType(type);
        explanation.setOccupationalCategory(occupationalCategory);
        explanation.setDeductedSpecialistId(paragraph);
        explanation.setEffectiveOccupationalCategory(data[3]);
        explanation.setDeductedFullVigor(staffing);
        explanation.setExplanation(data[5]);
        staffProof.addStaffProofExplanation(explanation);
    }

    private static OccupationalCategory obtainOccupationalCategory(StaffProof staffProof, PsychType type, int occupationalCategoryId) {
        // get the appropriate OccupationalCategory from the data
        // this eliminates the need for accessing the database here
        OccupationalCategory occupationalCategory = staffProof
                .getStaffProofsEffective(type)
                .stream().filter(e -> e.getOccupationalCategory().getId() == occupationalCategoryId)
                .findAny()
                .get()
                .getOccupationalCategory();
        return occupationalCategory;
    }

}
