/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.cert.comparer;

import de.inek.cmdargument.CmdArguments;
import de.inek.diffxgout.DiffXgOut;
import de.inek.diffxgout.DiffXgOut.RemunerationSystem;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipInputStream;

/**
 *
 * @author lautenti
 */
public class CertComparer {

    protected static final Logger LOGGER = Logger.getLogger("ComparingService");

    public int compare(String resultFileName, String referenceFileName, boolean isDrg, boolean isCertPhase) {
        try {
            int pos = resultFileName.lastIndexOf(".");
            String nameOfOutputFile = resultFileName.substring(0, pos) + "_Diff.xls";
            if (new File(nameOfOutputFile).exists()) {
                new File(nameOfOutputFile).delete();
            }
            if (resultFileName.endsWith(".zip")) {
                unzipArchive(resultFileName);
                resultFileName = resultFileName.substring(0, pos) + ".txt";
            }

            CmdArguments params;
            if (isDrg) {
                params = DiffXgOut.getArgumentDef(RemunerationSystem.DRG);
            } else {
                params = DiffXgOut.getArgumentDef(RemunerationSystem.PEPP);
            }
            List<String> paramVal = new ArrayList<>();
            paramVal.add("/ref:" + new File(referenceFileName));
            paramVal.add("/diff:" + nameOfOutputFile);
            if (isCertPhase) {
                paramVal.add("/zert");
            }
            paramVal.add("/data:" + resultFileName);
            params.assignValues(paramVal);
            return DiffXgOut.diff(params);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error during compare", ex);
        }
        return -1;
    }

    private static final int BUFLEN = 8192;

    private boolean unzipArchive(String archiveName) {
        File archive = new File(archiveName);
        int pos = archiveName.lastIndexOf(".");
        File targetFile = new File(archiveName.substring(0, pos) + ".txt");
        try (
                FileInputStream fis = new FileInputStream(archive);
                CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum))) {
            if (zis.getNextEntry() == null) {
                return false;
            }
            try (BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(targetFile), BUFLEN)) {
                copyStream(zis, dest);
                dest.flush();
                dest.close();
            }
            if (zis.getNextEntry() != null) {
                throw (new IOException("Archive " + archiveName + " contains more than one file!"));
            }
        } catch (IOException ex) {
            Logger.getLogger(CertComparer.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    private void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buff = new byte[BUFLEN];
        int count;
        while ((count = is.read(buff)) != -1) {
            os.write(buff, 0, count);
        }
    }
}
