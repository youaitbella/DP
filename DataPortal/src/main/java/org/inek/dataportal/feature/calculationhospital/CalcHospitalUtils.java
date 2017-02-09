/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.calc.DrgCalcBasics;
import org.inek.dataportal.entities.calc.PeppCalcBasics;
import org.inek.dataportal.enums.ConfigKey;
import org.inek.dataportal.helper.StreamHelper;

/**
 *
 * @author muellermi
 */
public class CalcHospitalUtils {
    
    public static void createTransferFile(SessionController sessionController, Object calcBasics) {
        String type;
        int ik;
        if (calcBasics instanceof DrgCalcBasics) {
            ik = ((DrgCalcBasics) calcBasics).getIk();
            type = "KGL";
        } else {
            ik = ((PeppCalcBasics) calcBasics).getIk();
            type = "KGP";
        }
        File workingDir = new File(sessionController.getApplicationTools().readConfig(ConfigKey.FolderRoot), sessionController.getApplicationTools().readConfig(ConfigKey.FolderUpload));
        File targetDir = new File(sessionController.getApplicationTools().readConfig(ConfigKey.FolderRoot), "added");
        File zipFile = new File(workingDir, UUID.randomUUID() + ".zip");

        Date ts = Calendar.getInstance().getTime();
        String emailInfo = "EMailInfo" + new SimpleDateFormat("ddMMyyyyHHmmss").format(ts) + ".txt";

        try (FileOutputStream fileOut = new FileOutputStream(zipFile);
                CheckedOutputStream checkedOut = new CheckedOutputStream(fileOut, new Adler32());
                ZipOutputStream compressedOut = new ZipOutputStream(new BufferedOutputStream(checkedOut))) {
            
            compressedOut.putNextEntry(new ZipEntry(emailInfo));
            String content = obtainInfoText(sessionController, type, ik);
            ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
            StreamHelper.copyStream(is, compressedOut);

            String dataFileName = type + ik + "_" + new SimpleDateFormat("ddMMyyyyHHmmss").format(ts) + ".txt";
            compressedOut.putNextEntry(new ZipEntry(dataFileName));
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String json = mapper.writeValueAsString(calcBasics);
            ByteArrayInputStream data = new ByteArrayInputStream(json.getBytes("UTF-8"));
            StreamHelper.copyStream(data, compressedOut);
            
            compressedOut.flush();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        File file;
        do {
            ts = Calendar.getInstance().getTime();
            file = new File(targetDir, "Box" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(ts) + ".zip");
        } while (file.exists());
        zipFile.renameTo(file);
    }

    private static String obtainInfoText(SessionController sessionController, String type, int ik) {
        Date ts = Calendar.getInstance().getTime();
        String content = "";
        if (sessionController.getAccount().isReportViaPortal()) {
            content += "Account.Mail=" + sessionController.getAccount().getEmail() + "\r\n";
        }
        content += "Accept=Dataportal\r\n";
        content += "From=" + sessionController.getAccount().getEmail() + "\r\n";
        content += "Received=" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(ts) + "\r\n";
        content += "Subject=" + type + "_" + ik + "\r\n";
        return content;
    }
    
}
