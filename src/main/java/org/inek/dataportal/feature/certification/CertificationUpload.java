package org.inek.dataportal.feature.certification;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    public void uploadSpec() { // todo: implement upload
        _logger.log(Level.INFO, "<Spec> uploading file {0}", _file.getName());
        try {
            if (_file != null) {
                Scanner scanner = new Scanner(_file.getInputStream(),
                        "UTF-8");
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                }
            }
        } catch (IOException | NoSuchElementException e) {
        }
    }

    public void uploadTrainingData() { // todo: implement upload
        _logger.log(Level.INFO, "<TrainingData> uploading file {0}", _file.getName());
    }

    public void uploadTestData() { // todo: implement upload
        _logger.log(Level.INFO, "<TestData> uploading file {0}", _file.getName());
    }

    public void uploadCertificationData() { // todo: implement upload
        _logger.log(Level.INFO, "<CertificationData> uploading file {0}", _file.getName());
    }

}
