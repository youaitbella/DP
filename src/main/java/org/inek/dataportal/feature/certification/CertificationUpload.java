package org.inek.dataportal.feature.certification;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipInputStream;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.Part;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class CertificationUpload {

    private static final Logger _logger = Logger.getLogger("CertificationUpload");

    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public void uploadSpec(int systemId) { // todo: implement upload
        _logger.log(Level.INFO, "<Spec> uploading file {0}", _file.getName());
        try {
            if (_file != null) {
                InputStreamReader reader;
                if (_file.getName().toLowerCase().endsWith(".zip")) {
                    ZipInputStream zipreader = new ZipInputStream(_file.getInputStream());
                } else {
                    reader = new InputStreamReader(_file.getInputStream(),
                            "UTF-8");
                }

            }
        } catch (IOException | NoSuchElementException e) {
        }
    }

    public void uploadTrainingData(int systemId) { // todo: implement upload
        _logger.log(Level.INFO, "<TrainingData> uploading file {0}", _file.getName());
    }

    public void uploadTestData() { // todo: implement upload
        _logger.log(Level.INFO, "<TestData> uploading file {0}", _file.getName());
    }

    public void uploadCertificationData() { // todo: implement upload
        _logger.log(Level.INFO, "<CertificationData> uploading file {0}", _file.getName());
    }

}
