package org.inek.dataportal.feature.certification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.StreamHelper;
import org.inek.dataportal.helper.scope.FeatureScoped;

/**
 *
 * @author vohldo, muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class EditCert extends AbstractEditController {

    private static final Logger _logger = Logger.getLogger("EditCert");

    @Inject private SessionController _sessionController;
    @Inject private SystemFacade _systemFacade;

//    @PostConstruct
//    private void init() {
//        _logger.log(Level.WARNING, "Init EditCert");
//    }
//
//    @PreDestroy
//    private void destroy() {
//        _logger.log(Level.WARNING, "Destroy EditCert");
//    }
    @Override
    protected void addTopics() {
        if (_sessionController.isInekUser(Feature.CERT)) {
            addTopic(CertTabs.tabCertSystemManagement.name(), Pages.CertSystemManagement.URL());
            addTopic(CertTabs.tabCertMailTemplate.name(), Pages.CertMailTemplate.URL());
            addTopic(CertTabs.tabCertMail.name(), Pages.CertMail.URL());
            addTopic(CertTabs.tabCertification.name(), Pages.CertCertification.URL());
        }
        addTopic(CertTabs.tabCertification.name(), Pages.CertCertification.URL());
    }

    private enum CertTabs {

        tabCertSystemManagement,
        tabCertMailTemplate,
        tabCertMail,
        tabCertification;
    }

    public File getCertFile(int systemId, String folder, String fileNameBase, String extension) {
        return getCertFile(systemId, folder, fileNameBase, extension, false);
    }

    public File getCertFile(int systemId, String folder, String fileNameBase, String extension, boolean includeUpload) {
        RemunerationSystem system = getSystem(systemId);
        if (system == null) {
            return null;
        }

        Optional<File> uploadFolder = getUploadFolder(system, folder);
        if (!uploadFolder.isPresent()) {
            return null;
        }

        String fileNamePattern = fileNameBase + "_" + system.getFileName()
                + "_\\(\\d{4}-\\d{2}-\\d{2}\\)." + extension
                + (includeUpload ? "(\\.upload)?" : "");
        return getLastFile(uploadFolder.get(), fileNamePattern);
    }

    public String getCertFileName(int systemId, String folder, String fileNameBase, String extension) {
        return getCertFileName(systemId, folder, fileNameBase, extension, false);
    }

    public String getCertFileName(int systemId, String folder, String fileNameBase, String extension, boolean includeUpload) {
        if (systemId <= 0) {
            return "";
        }
        String fileName = getCertFile(systemId, folder, fileNameBase, extension, includeUpload).getName();
        return fileName.replace(".upload", " [ungespeichert]");
    }

    public RemunerationSystem getSystem(int systemId) {
        if (systemId < 0) {
            return null;
        }
        RemunerationSystem system = _systemFacade.find(systemId);
        if (system == null) {
            _logger.log(Level.WARNING, "Certification, missing system with id {0}", systemId);
        }
        return system;
    }

    /**
     * Get the upload folder, depending on the system (determines folder parent)
     * and ensures the existence of this folder
     *
     * @param system
     * @param folderName
     * @return folder if ok, null otherwise
     */
    public Optional getUploadFolder(RemunerationSystem system, String folderName) {
        File folder = new File(system.getSystemRoot(), folderName);
        try {
            folder.mkdirs();
        } catch (Exception ex) {
            _logger.log(Level.WARNING, "upload, error during creating folder {0}", folder.getAbsolutePath());
            return Optional.empty();
        }
        return Optional.of(folder);
    }

    /**
     * gets the last file (in alphabetical order) matching the file name pattern
     *
     * @param dir
     * @param fileNamePattern
     * @return
     */
    public File getLastFile(File dir, final String fileNamePattern) {
        File lastFile = new File("");
        for (File file : dir.listFiles((File file) -> file.isFile() && file.getName().matches(fileNamePattern))) {
            if (file.getName().compareTo(lastFile.getName()) > 0) {
                lastFile = file;
            }
        }
        return lastFile;
    }

    public void uploadFile(Part uploadFile, File target) {
        if (uploadFile == null) {
            return;
        }
        _logger.log(Level.INFO, "uploading file {0}", uploadFile.getSubmittedFileName());
        try (InputStream inStream = uploadFile.getInputStream();
                FileOutputStream fos = new FileOutputStream(target)) {
            new StreamHelper().copyStream(inStream, fos);
        } catch (IOException e) {
            _logger.log(Level.WARNING, "upload exception: {0}", e.getMessage());
        }
    }

}
