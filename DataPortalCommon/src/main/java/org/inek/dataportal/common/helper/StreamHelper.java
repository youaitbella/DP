/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.helper;

import org.inek.dataportal.api.helper.PortalConstants;

import java.io.*;
import java.util.zip.*;

/**
 *
 * @author muellermi
 */
public class StreamHelper {


    public void compressFiles(File[] files, File target) throws IOException {

        try (FileOutputStream fileOut = new FileOutputStream(target);
                CheckedOutputStream checkedOut = new CheckedOutputStream(fileOut, new Adler32());
                ZipOutputStream compressedOut = new ZipOutputStream(new BufferedOutputStream(checkedOut))) {
            for (File file : files) {
                try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(file), PortalConstants.BUFFER_SIZE)) {
                    compressedOut.putNextEntry(new ZipEntry(file.getName()));
                    copyStream(is, compressedOut);
                }
            }
        }
    }

    public void unzipArchive(File archive, File dir) throws IOException {
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.isDirectory()) {
            throw new IOException(dir.getAbsolutePath() + " is not a valid directory");
        }

        try (
                FileInputStream fis = new FileInputStream(archive);
                CheckedInputStream checksum = new CheckedInputStream(fis, new Adler32());
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                File file = new File(dir, entry.getName());
                try (BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(file), PortalConstants.BUFFER_SIZE)) {
                    copyStream(zis, dest);
                    dest.flush();
                }
            }
        }
    }

    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buff = new byte[PortalConstants.BUFFER_SIZE];
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
