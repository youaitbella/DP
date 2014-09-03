package org.inek.dataportal.feature.certification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.helper.StreamHelper;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;

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
    @Inject SessionController _sessionController;

    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public void uploadSpec(int systemId) {
        uploadCertFile(systemId, "Spec", "Grouper-Spezifikation", "exe");
    }

    public void uploadTrainingData(int systemId) {
        uploadCertFile(systemId, "Daten", "Uebungsdaten", "zip");
    }

    public void uploadTestData(int systemId) {
        uploadCertFile(systemId, "Daten", "Testdaten", "zip");
    }

    public void uploadCertificationData(int systemId) {
        uploadCertFile(systemId, "Daten", "Zertdaten", "zip");
    }

    public void uploadCertFile(int systemId, String folder, String fileNameBase, String extension) {
        RemunerationSystem system = getSystem(systemId);
        if (system == null) {
            return;
        }

        Optional<File> uploadFolder = getUploadFolder(system, folder);
        if (!uploadFolder.isPresent()) {
            return;
        }
        String fileNamePattern = fileNameBase + "_" + system.getFileName() + "_.*\\.upload";
        deleteFiles(uploadFolder.get(), fileNamePattern);
        String outFile = fileNameBase + "_" + system.getFileName() + "_(" + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ")." + extension + ".upload";
        uploadFile(new File(uploadFolder.get(), outFile));
        EditCert editCert = (EditCert) FeatureScopedContextHolder.Instance.getBean(EditCert.class).getInstance();
        editCert.setSystemChanged(true);
    }

    private RemunerationSystem getSystem(int systemId) {
        if (systemId < 0) {
            return null;
        }
        RemunerationSystem system = _systemFacade.find(systemId);
        if (system == null) {
            _logger.log(Level.WARNING, "upload, missing system with id {0}", systemId);
        }
        return system;
    }

    public File getCertFile(int systemId, String folder, String fileNameBase, String extension) {
        RemunerationSystem system = getSystem(systemId);
        if (system == null) {
            return null;
        }

        Optional<File> uploadFolder = getUploadFolder(system, folder);
        if (!uploadFolder.isPresent()) {
            return null;
        }

        String fileNamePattern = fileNameBase + "_" + system.getFileName() + "_\\(\\d{4}-\\d{2}-\\d{2}\\)." + extension + "(\\.upload)?";
        return getLastFile(uploadFolder.get(), fileNamePattern);
    }

    public String getCertFileName(int systemId, String folder, String fileNameBase, String extension) {
        if (systemId <= 0) {
            return "";
        }
        String fileName = getCertFile(systemId, folder, fileNameBase, extension).getName();
        return fileName.replace(".upload", " [ungespeichert]");
    }

    public void uploadTestResult(int systemId, int AccountId) {
        RemunerationSystem system = _systemFacade.find(systemId);
        if (system == null) {
            _logger.log(Level.WARNING, "upload, missing system with id {0}", systemId);
            return;
        }
        Optional<File> uploadFolder = getUploadFolder(system, "Daten");  // todo: folder depending on account
        if (!uploadFolder.isPresent()) {
            return;
        }
        String prefix = "ErgebnisUebungsdaten_";
        File lastFile = getLastFile(uploadFolder.get(), prefix + "\\d\\.txt");
        String lastFileName = lastFile.getName();
        int version = 1;
        if (lastFileName.startsWith(prefix)) {
            version = 1 + Integer.parseInt(lastFileName.substring(prefix.length(), prefix.length() + 1));
        }
        String outFile = prefix + version + ".txt";
        uploadFile(new File(uploadFolder.get(), outFile));
    }

    /**
     * Get the upload folder, depending on the system (determines folder parent)
     * and ensures the existence of this folder
     *
     * @param systemId
     * @param folderName
     * @return folder if ok, null otherwise
     */
    private Optional getUploadFolder(RemunerationSystem system, String folderName) {
        File folder = new File(system.getSystemRoot(), folderName);
        try {
            folder.mkdirs();
        } catch (Exception ex) {
            _logger.log(Level.WARNING, "upload, error during creating folder {0}", folder.getAbsolutePath());
            return Optional.empty();
        }
        return Optional.of(folder);
    }

    private void uploadFile(File file) {
        if (_file == null) {
            return;
        }
        _logger.log(Level.INFO, "uploading file {0}", _file.getSubmittedFileName());
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
    private File getLastFile(File dir, final String fileNamePattern) {
        File lastFile = new File("");
        for (File file : dir.listFiles((File file) -> file.isFile() && file.getName().matches(fileNamePattern))) {
            if (file.getName().compareTo(lastFile.getName()) > 0) {
                lastFile = file;
            }
        }
        return lastFile;
    }

    private void deleteFiles(File dir, final String fileNamePattern) {
        for (File file : dir.listFiles((File file) -> file.isFile() && file.getName().matches(fileNamePattern))) {
            file.delete();
        }
    }

}
