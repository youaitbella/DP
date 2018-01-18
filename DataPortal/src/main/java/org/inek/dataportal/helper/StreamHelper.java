/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.helper;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author muellermi
 */
public class StreamHelper {

    public static final int BUFFER_LENGHT = 8192;

    public void compressFiles(File[] files, File target) throws ProcessingException {

        try {

            try (FileOutputStream fileOut = new FileOutputStream(target);
                    CheckedOutputStream checkedOut = new CheckedOutputStream(fileOut, new Adler32());
                    ZipOutputStream compressedOut = new ZipOutputStream(new BufferedOutputStream(checkedOut))) {
                for (File file : files) {
                    try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(file), BUFFER_LENGHT)) {
                        compressedOut.putNextEntry(new ZipEntry(file.getName()));
                        copyStream(is, compressedOut);
                    }
                }
            }
        } catch (IOException ex) {
            throw new ProcessingException(ex);
        }
    }

    public void unzipArchive(File archive, File dir) throws ProcessingException {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.isDirectory()) {
            throw new ProcessingException(dir.getAbsolutePath() + " is not a valid directory");
        }

        try {
            try (
                    FileInputStream fis = new FileInputStream(archive);
                    CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
                    ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum))) {
                ZipEntry entry;
                while ((entry = zis.getNextEntry()) != null) {
                    File file = new File(dir, entry.getName());
                    try (BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(file), BUFFER_LENGHT)) {
                        copyStream(zis, dest);
                        dest.flush();
                    }
                }
            }
        } catch (IOException ex) {
            throw new ProcessingException(ex);
        }
    }

    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buff = new byte[BUFFER_LENGHT];
        int count;
        while ((count = is.read(buff)) != -1) {
            os.write(buff, 0, count);
        }
    }

    public static byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        copyStream(is, buffer);
        buffer.flush();
        return buffer.toByteArray();
    }

    public static String toString(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        copyStream(is, buffer);
        buffer.flush();
        return buffer.toString("UTF-8");
    }

}
