/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.utils;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author muellermi
 */
public class StreamUtils {

    /**
     * copies an input stream into an array of bytes
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] stream2blob(InputStream is) throws IOException, IllegalArgumentException {
        return stream2blob(is, 8192);
    }

    public static byte[] stream2blob(InputStream is, int initialSize) throws IOException, IllegalArgumentException {
        int remaining = 1000;
        byte[] buffer = new byte[8192];
        byte[] tempBlob = new byte[initialSize];
        int n;
        int offset = 0;
        try {
            while ((n = is.read(buffer)) >= 0) {
                if (offset + n > Integer.MAX_VALUE - 10000){
                    System.out.println("Offset: " + offset + ", n: " + n + ", Total: " + (offset + n));
                }
                if (offset + n > Integer.MAX_VALUE - remaining) {
                    throw new IllegalArgumentException("Data too long for byte array");
                }
                if (offset + n > tempBlob.length) {
                    int newLen = tempBlob.length * 2;
                    if (newLen <= tempBlob.length) {
                        // might be negative due to overflow
                        newLen = Integer.MAX_VALUE - remaining;
                    }
                    byte[] newTemp = new byte[newLen];
                    System.arraycopy(tempBlob, 0, newTemp, 0, offset);
                    tempBlob = newTemp;
                }
                System.arraycopy(buffer, 0, tempBlob, offset, n);
                offset += n;
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException(ex);
        }
        byte[] blob;
        if (offset == tempBlob.length) {
            blob = tempBlob;
        } else {
            blob = new byte[offset];
            System.arraycopy(tempBlob, 0, blob, 0, offset);
        }
        return blob;
    }

}
