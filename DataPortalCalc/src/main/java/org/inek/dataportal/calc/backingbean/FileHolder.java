package org.inek.dataportal.calc.backingbean;

import java.io.Serializable;
import javax.servlet.http.Part;

/**
 *
 * @author kunkelan
 */
public class FileHolder implements Serializable {

    private Part file;
    private final String fileName;

    public FileHolder(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }
}
