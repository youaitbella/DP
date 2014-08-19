/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author muellermi
 */
public class StreamHelper {

    private final int _BufLen = 8192;

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

    public void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buff = new byte[_BufLen];
        int count;
        while ((count = is.read(buff)) != -1) {
            os.write(buff, 0, count);
        }
    }

}
