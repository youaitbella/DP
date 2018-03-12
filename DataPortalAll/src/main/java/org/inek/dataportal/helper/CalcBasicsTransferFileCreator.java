package org.inek.dataportal.helper;

import org.inek.dataportal.common.helper.TransferFileCreator;
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
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.entities.calc.autopsy.CalcBasicsAutopsy;
import org.inek.dataportal.entities.calc.drg.DrgCalcBasics;
import org.inek.dataportal.entities.calc.psy.PeppCalcBasics;

/**
 *
 * @author muellermi
 */
public class CalcBasicsTransferFileCreator {

    public static void createCalcBasicsTransferFile(SessionController sessionController, Object calcBasics) {
        String type;
        int ik;
        if (calcBasics instanceof DrgCalcBasics) {
            ik = ((DrgCalcBasics) calcBasics).getIk();
            type = "KGL";
        } else if (calcBasics instanceof PeppCalcBasics) {
            ik = ((PeppCalcBasics) calcBasics).getIk();
            type = "KGP";
        } else if (calcBasics instanceof CalcBasicsAutopsy) {
            ik = ((CalcBasicsAutopsy) calcBasics).getIk();
            type = "KGS";
        } else {
            throw new IllegalArgumentException("unknown object type: " + calcBasics);
        }
        File workingDir = new File(sessionController.getApplicationTools().readConfig(ConfigKey.FolderRoot), sessionController.getApplicationTools().
                readConfig(ConfigKey.FolderUpload));
        File targetDir = new File(sessionController.getApplicationTools().readConfig(ConfigKey.FolderRoot), "added");
        File zipFile = new File(workingDir, UUID.randomUUID() + ".zip");
        Date ts = Calendar.getInstance().getTime();
        String emailInfo = "EMailInfo" + new SimpleDateFormat("ddMMyyyyHHmmss").format(ts) + ".txt";
        try (final FileOutputStream fileOut = new FileOutputStream(zipFile);final CheckedOutputStream checkedOut = new CheckedOutputStream(fileOut, new Adler32());final ZipOutputStream compressedOut = new ZipOutputStream(new BufferedOutputStream(checkedOut))) {
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
        return TransferFileCreator.obtainInfoText(sessionController.getAccount().getEmail(), type + "_" + ik);
    }
    
    
}
