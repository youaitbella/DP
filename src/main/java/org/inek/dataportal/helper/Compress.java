/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper;

import java.io.*;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author muellermi
 */
public class Compress {

    private final int _BufLen = 2048;

    public void compressFiles(File[] files, File target) throws ProcessingException {

        try {

            try (FileOutputStream fileOut = new FileOutputStream(target);
                    CheckedOutputStream checkedOut = new CheckedOutputStream(fileOut, new Adler32());
                    ZipOutputStream compressedOut = new ZipOutputStream(new BufferedOutputStream(checkedOut))) {
                for (File file : files) {
                    try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(file), _BufLen)) {
                        compressedOut.putNextEntry(new ZipEntry(file.getName()));
                        copyStream(is, compressedOut);
                    }
                }
            }
        } catch (IOException ex) {
            throw new ProcessingException(ex);
        }
    }

    private void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buff = new byte[_BufLen];
        int count;
        while ((count = is.read(buff)) != -1) {
            os.write(buff, 0, count);
        }
    }
}
