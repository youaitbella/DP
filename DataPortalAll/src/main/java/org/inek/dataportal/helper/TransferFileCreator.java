/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper;

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
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.AccountDocument;
import org.inek.dataportal.entities.calc.autopsy.CalcBasicsAutopsy;
import org.inek.dataportal.entities.calc.drg.DrgCalcBasics;
import org.inek.dataportal.entities.calc.psy.PeppCalcBasics;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.data.access.ConfigFacade;

/**
 *
 * @author muellermi
 */
public class TransferFileCreator {

    protected static final Logger LOGGER = Logger.getLogger("TransferFileCreator");

    public static void createCalcBasicsTransferFile(SessionController sessionController, Object calcBasics) {
        String type;
        int ik;
        if (calcBasics instanceof DrgCalcBasics) {
            ik = ((DrgCalcBasics) calcBasics).getIk();
            type = "KGL";
        } else if (calcBasics instanceof PeppCalcBasics){
            ik = ((PeppCalcBasics) calcBasics).getIk();
            type = "KGP";
        } else if (calcBasics instanceof CalcBasicsAutopsy){
            ik = ((CalcBasicsAutopsy) calcBasics).getIk();
            type = "KGS";
        }else{
            throw new IllegalArgumentException("unknown object type: " + calcBasics);
        }
        File workingDir = new File(sessionController.getApplicationTools().readConfig(ConfigKey.FolderRoot), 
                sessionController.getApplicationTools().readConfig(ConfigKey.FolderUpload));
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

            String dataFileName = type + ik + "_" + new SimpleDateFormat("ddMMyyyyHHmmss").format(ts) + ".json";
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
        return obtainInfoText(sessionController.getAccount().getEmail(), type + "_" + ik);
    }
    
    private static String obtainInfoText(String email, String subject) {
        Date ts = Calendar.getInstance().getTime();
        String content = "";
        content += "Accept=Dataportal\r\n";
        content += "From=" + email + "\r\n";
        content += "Received=" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(ts) + "\r\n";
        content += "Subject=" + subject + "\r\n";
        return content;
    }

    public static void createEmailTransferFile(ConfigFacade configFacade, String email) {
        try {
            File workingDir = new File(configFacade.readConfig(ConfigKey.FolderRoot), configFacade.readConfig(ConfigKey.FolderUpload));
            if (!workingDir.exists()) {
                workingDir.mkdirs();
            }
            File targetDir = new File(configFacade.readConfig(ConfigKey.FolderRoot), "added");
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            
            File zipFile = new File(workingDir, UUID.randomUUID() + ".zip");

            Date ts = Calendar.getInstance().getTime();
            String emailInfo = "EMailInfo" + new SimpleDateFormat("ddMMyyyyHHmmss").format(ts) + ".txt";

            try (FileOutputStream fileOut = new FileOutputStream(zipFile);
                    CheckedOutputStream checkedOut = new CheckedOutputStream(fileOut, new Adler32());
                    ZipOutputStream compressedOut = new ZipOutputStream(new BufferedOutputStream(checkedOut))) {

                compressedOut.putNextEntry(new ZipEntry(emailInfo));

                String content = "Accept=Dataportal\r\n"
                        + "From=" + email + "\r\n"
                        + "Received=" + new SimpleDateFormat("dd.MM.yyyy HH:mm").format(ts) + "\r\n"
                        + "Subject=EmailActivation\r\n";

                ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
                StreamHelper.copyStream(is, compressedOut);

                
                compressedOut.flush();
            }
            File file;
            do {
                ts = Calendar.getInstance().getTime();
                file = new File(targetDir, "Box" + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(ts) + ".zip");
            } while (file.exists());
            zipFile.renameTo(file);
        } catch (IOException ex) {
            // any problem whilst creating the transfer file shall not hamper the user
            // we just log for further investigation and ignore the execption
            LOGGER.log(Level.WARNING, ex.getMessage());
        }

    }
    
    public static void createInekDocumentFiles(ConfigFacade configFacade, List<AccountDocument> documents, String email, String subject) {
        File workingDir = new File(configFacade.readConfig(ConfigKey.FolderRoot), 
                configFacade.readConfig(ConfigKey.FolderUpload));
        File targetDir = new File(configFacade.readConfig(ConfigKey.FolderRoot), "added");
        File zipFile = new File(workingDir, UUID.randomUUID() + ".zip");

        Date ts = Calendar.getInstance().getTime();
        String emailInfo = "EMailInfo" + new SimpleDateFormat("ddMMyyyyHHmmss").format(ts) + ".txt";

        try (FileOutputStream fileOut = new FileOutputStream(zipFile);
                CheckedOutputStream checkedOut = new CheckedOutputStream(fileOut, new Adler32());
                ZipOutputStream compressedOut = new ZipOutputStream(new BufferedOutputStream(checkedOut))) {

            compressedOut.putNextEntry(new ZipEntry(emailInfo));
            String content = obtainInfoText(email, subject);
            ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
            StreamHelper.copyStream(is, compressedOut);
            for(AccountDocument document : documents) {
                String dataFileName = document.getName();
                compressedOut.putNextEntry(new ZipEntry(dataFileName));
                ByteArrayInputStream data = new ByteArrayInputStream(document.getContent());
                StreamHelper.copyStream(data, compressedOut);
                compressedOut.flush();
            }
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
}
