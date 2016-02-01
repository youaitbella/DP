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
    public static byte[] stream2blob(InputStream is) throws IOException {
        byte[] buffer = new byte[8192];
        byte[] tempBlob = new byte[8192];
        int n;
        int offset = 0;
        while ((n = is.read(buffer)) >= 0) {
            if (offset + n > tempBlob.length) {
                byte[] newTemp = new byte[tempBlob.length * 2];
                System.arraycopy(tempBlob, 0, newTemp, 0, offset);
                tempBlob = newTemp;
            }
            System.arraycopy(buffer, 0, tempBlob, offset, n);
            offset += n;
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
