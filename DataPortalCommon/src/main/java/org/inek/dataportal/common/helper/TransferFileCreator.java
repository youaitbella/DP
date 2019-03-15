/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author muellermi
 */
public class TransferFileCreator {

    protected static final Logger LOGGER = Logger.getLogger("TransferFileCreator");


    public static String obtainInfoText(String email, String subject) {
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
    
    public static void createObjectTransferFile(SessionController sessionController, Object object, int ik, String type) {
        File workingDir = new File(sessionController.getApplicationTools().readConfig(ConfigKey.FolderRoot), sessionController.getApplicationTools().
                readConfig(ConfigKey.FolderUpload));
        File targetDir = new File(sessionController.getApplicationTools().readConfig(ConfigKey.FolderRoot), "added");
        File zipFile = new File(workingDir, UUID.randomUUID() + ".zip");
        Date ts = Calendar.getInstance().getTime();
        String emailInfo = "EMailInfo" + new SimpleDateFormat("ddMMyyyyHHmmss").format(ts) + ".txt";
        try (final FileOutputStream fileOut = new FileOutputStream(zipFile);
             final CheckedOutputStream checkedOut = new CheckedOutputStream(fileOut, new Adler32());
             final ZipOutputStream compressedOut = new ZipOutputStream(new BufferedOutputStream(checkedOut))) {
            compressedOut.putNextEntry(new ZipEntry(emailInfo));
            String content = obtainInfoText(sessionController.getAccount().getEmail(), type + "_" + ik);
            ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
            StreamHelper.copyStream(is, compressedOut);
            String dataFileName = type + ik + "_" + new SimpleDateFormat("ddMMyyyyHHmmss").format(ts) + ".json";
            compressedOut.putNextEntry(new ZipEntry(dataFileName));
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String json = mapper.writeValueAsString(object);
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
}
