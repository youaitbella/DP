package org.inek.dataportal.feature.certification;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.helper.StreamHelper;

/**
 * This class is used to upload files during the certification process. All
 * uploads will be stored using a ".uploade" extenstion until the user hits the
 * save button. Then, the ".upload" extension will be removed
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class CertificationUpload {

    private static final Logger _logger = Logger.getLogger("CertificationUpload");
    @Inject SystemFacade _systemFacade;

    @ManagedProperty("#{param.systemId}")
    private int _systemId;

    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public void uploadSpecManual(int systemId) {
        uploadSpec(systemId, "SpecHandbuch", "Spec-Handbuch", "pdf");
    }

    public void uploadTrainingData(int systemId) {
        uploadSpec(systemId, "Daten", "Uebungsdaten", "zip");
    }

    public void uploadTestData(int systemId) {
        uploadSpec(systemId, "Daten", "Testdaten", "zip");
    }

//    public void uploadCertificationData(AjaxBehaviorEvent event) {
//        uploadSpec(_systemId, "Daten", "Zertdaten", "zip");
//    }
//
    public void uploadCertificationData(int systemId) {
        uploadSpec(systemId, "Daten", "Zertdaten", "zip");
    }

    private void uploadSpec(int systemId, String folder, String prefix, String extension) {
//        Optional<File> uploadFolder = getUploadFolder(systemId, folder);
//        if (!uploadFolder.isPresent()) {
//            return;
//        }
        File uploadFolder = getUploadFolder(systemId, folder);
        if (uploadFolder == null) {
            return;
        }
        String outFile = prefix + "_" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + "." + extension + ".upload";
//        uploadFile(new File(uploadFolder.get(), outFile));
        uploadFile(new File(uploadFolder, outFile));
    }

    public void uploadTestResult(int systemId, int AccountId) {
//        Optional<File> uploadFolder = getUploadFolder(systemId, "Daten");  // todo: folder depending on account
//        if (!uploadFolder.isPresent()) {
//            return;
//        }
        File uploadFolder = getUploadFolder(systemId, "Daten");  // todo: folder depending on account
        if (uploadFolder == null) {
            return;
        }
        String prefix = "ErgebnisUebungsdaten_";
//        String lastFile = getLastFile(uploadFolder.get(), prefix + "\\d\\.txt");
        String lastFile = getLastFile(uploadFolder, prefix + "\\d\\.txt");
        int version = 1;
        if (lastFile.startsWith(prefix)) {
            version = 1 + Integer.parseInt(lastFile.substring(prefix.length(), prefix.length() + 1));
        }
        String outFile = prefix + version + ".txt";
//        uploadFile(new File(uploadFolder.get(), outFile));
        uploadFile(new File(uploadFolder, outFile));
    }

    /**
     * Get the upload folder, depending on the system (determines folder parent)
     * and ensures the existence of this folder
     *
     * @param systemId
     * @param folderName
     * @return folder if ok, null otherwise
     */
//    private Optional getUploadFolder(int systemId, String folderName) {
//        if (_file == null) {
//            return Optional.empty();
//        }
//        _logger.log(Level.INFO, "uploading file {0}", _file.getSubmittedFileName());
//        RemunerationSystem system = _systemFacade.find(systemId);
//        if (system == null) {
//            _logger.log(Level.WARNING, "upload, missing system with id {0}", systemId);
//            return Optional.empty();
//        }
//        File folder = new File(system.getSystemRoot(), folderName);
//        try {
//            folder.mkdirs();
//        } catch (Exception ex) {
//            _logger.log(Level.WARNING, "upload, error during creating folder {0}", folder.getAbsolutePath());
//            return Optional.empty();
//        }
//        return Optional.of(folder);
//    }
    private File getUploadFolder(int systemId, String folderName) {
        if (_file == null) {
            return null;
        }
        _logger.log(Level.INFO, "uploading file {0}", _file.getSubmittedFileName());
        RemunerationSystem system = _systemFacade.find(systemId);
        if (system == null) {
            _logger.log(Level.WARNING, "upload, missing system with id {0}", systemId);
            return null;
        }
        File folder = new File(system.getSystemRoot(), folderName);
        try {
            folder.mkdirs();
        } catch (Exception ex) {
            _logger.log(Level.WARNING, "upload, error during creating folder {0}", folder.getAbsolutePath());
            return null;
        }
        return folder;
    }

    private void uploadFile(File file) {
        try (InputStream inStream = _file.getInputStream();
                FileOutputStream fos = new FileOutputStream(file)) {
            new StreamHelper().copyStream(inStream, fos);
        } catch (IOException e) {
            _logger.log(Level.WARNING, "upload exception: {0}", e.getMessage());
        }
    }

    /**
     * gets the last file (in alphabetical order) matching the file name pattern
     *
     * @param dir
     * @param fileNamePattern
     */
    private String getLastFile(File dir, final String fileNamePattern) {
        String lastFile = "";
        for (File file : dir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File file) {
                return file.isFile() && file.getName().matches(fileNamePattern);
            }

        })) {
            if (file.getName().compareTo(lastFile) > 0) {
                lastFile = file.getName();
            }
        }
        return lastFile;
    }

}
