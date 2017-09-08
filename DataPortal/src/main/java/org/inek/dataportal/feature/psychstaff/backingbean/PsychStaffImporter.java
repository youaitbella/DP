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
import org.inek.dataportal.feature.insurance.NoticeItemImporter;
import org.inek.dataportal.feature.psychstaff.entity.StaffProof;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.feature.psychstaff.enums.PsychType;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.utils.StringUtil;

/**
 *
 * @author muellermi
 */
public class PsychStaffImporter {

    public static void importAgreed(Part file, StaffProof staffProof, PsychType type) {
        try {
            Scanner scanner = new Scanner(file.getInputStream());
            if (!scanner.hasNextLine()) {
                return;
            }
            List<StaffProofAgreed> staffProofsAgreed = staffProof.getStaffProofsAgreed(type);
            while (scanner.hasNextLine()) {
                String line = Utils.convertFromUtf8(scanner.nextLine());
                if (!line.contains(";Form;Menge;Einheit;Anzahl;Preis;Entgelt")) {
                    tryImportLine(staffProofsAgreed, line);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(PsychStaffImporter.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private static void tryImportLine(List<StaffProofAgreed> staffProofsAgreed, String line) {
        String[] data = StringUtil.splitAtUnquotedSemicolon(line);
        if (data.length != 5) {
            throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
        }
        //stringif (data[0].trim() == type.)
    }

    public static void importEffective(Part file, StaffProof staffProof, PsychType type) {

    }

}
