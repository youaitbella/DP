/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.cert.Helper;

import java.io.File;
import org.inek.dataportal.cert.enums.CertStatus;

import org.inek.dataportal.cert.entities.Grouper;
import org.inek.dataportal.cert.entities.RemunerationSystem;
import org.inek.dataportal.common.enums.RemunSystem;

/**
 *
 * @author lautenti
 */
public final class CertFileHelper {

    private static final String BASE = "\\\\vFileserver01\\company$\\EDV\\Projekte\\Zertifizierung\\Pruefung\\System ";
    private static final String DRG = "\\G-DRG-System ";
    private static final String PEPP = "\\PEPP-Entgeltsystem ";
    private static final String TEST = "\\Referenzdaten\\Ausgabe_Testdaten.txt";
    private static final String CERT = "\\Referenzdaten\\Ausgabe_Zertdaten.txt";

    public static String getReferenceFile(Grouper grouper) {
        RemunerationSystem system = grouper.getSystem();
        return getSysDir(system) + (isCertPhase(grouper) ? CERT : TEST);
    }

    public static String getResultFile(Grouper grouper) {
        RemunerationSystem system = grouper.getSystem();
        return getSysDir(system) + "\\Ergebnis\\" + (grouper.getAccountId() > 0 ? "" + grouper.getAccountId() : "XXXX")
                + (isCertPhase(grouper) ? "\\ZertDaten v{0}.zip" : "\\TestDaten v{0}.zip").replace("{0}", "" + getRun(grouper));
    }

    public static String getSysDir(RemunerationSystem system) {
        return BASE + system.getYearSystem() + (system.getRemunerationSystem() == RemunSystem.DRG ? DRG : PEPP)
                + (system.getYearData() == system.getYearSystem() ? "" : system.getYearData() + "_") + system.getYearSystem();
    }

    public static Boolean isCertPhase(Grouper grouper) {
        return grouper.getCertStatus().getStatus() > CertStatus.TestSucceed.getStatus();
    }

    private static int getRun(Grouper grouper) {
        switch (grouper.getCertStatus()) {
            case TestUpload1:
                return 1;
            case TestUpload2:
                return 2;
            case TestUpload3:
                return 3;
            case CertUpload1:
                return 1;
            case CertUpload2:
                return 2;
            default:
                return 999;
        }
    }

    public static File getSystemRoot(RemunerationSystem system) {
        File root = new File(BASE + system.getYearSystem());
        File systemRoot = new File(root, system.getFileName());
        return systemRoot;
    }

    public static String getExtension(String name) {
        if (name.contains(".") && !name.endsWith(".")) {
            String[] split = name.split("\\.");
            return split[split.length - 1];
        }
        return "";
    }

    public static File getCertFile(RemunerationSystem system, String folder, String fileNameBase, String extension) {
        File uploadFolder = getUploadFolder(system, folder);

        String fileNamePattern = fileNameBase + "_" + system.getFileName()
                + "_\\(\\d{4}-\\d{2}-\\d{2}\\)." + extension;

        return getLastFile(uploadFolder, fileNamePattern);
    }

    private static File getLastFile(File dir, final String fileNamePattern) {
        File lastFile = new File("");
        for (File file : dir.listFiles((File file) -> file.isFile() && file.getName().matches(fileNamePattern))) {
            if (file.getName().compareTo(lastFile.getName()) > 0) {
                lastFile = file;
            }
        }
        return lastFile;
    }

    private static File getUploadFolder(RemunerationSystem system, String folderName) {
        File folder = new File(getSystemRoot(system), folderName);
        try {
            if (!folder.exists()) {
                folder.mkdirs();
            }
        } catch (Exception ex) {

        }
        return folder;
    }

}
