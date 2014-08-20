package org.inek.dataportal.feature.certification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.helper.StreamHelper;
import org.inek.dataportal.utils.PropertyKey;
import org.inek.dataportal.utils.PropertyManager;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class CertificationUpload {

    private static final Logger _logger = Logger.getLogger("CertificationUpload");
    @Inject SystemFacade _systemFacade;

    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public void uploadSpec(int systemId) { // todo: implement upload
        if (_file == null) {
            return;
        }
        _logger.log(Level.INFO, "<Spec> uploading file {0}", _file.getSubmittedFileName());
        RemunerationSystem system = _systemFacade.find(systemId);
        if (system == null) {
            _logger.log(Level.WARNING, "<Spec> upload, missing system with id {0}", systemId);
            return;
        }
        File root = new File(PropertyManager.INSTANCE.getProperty(PropertyKey.CertiFolderRoot), "System " + system.getYearSystem());
        File dir = new File(root, "Spec");
        try {
            dir.mkdirs();
            InputStream inStream = _file.getInputStream();
            try (FileOutputStream fos = new FileOutputStream(new File(dir, _file.getSubmittedFileName()))) {
                new StreamHelper().copyStream(inStream, fos);
            }
        } catch (IOException e) {
            _logger.log(Level.WARNING, "<Spec> upload, exception: {0}", e.getMessage());
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
