package org.inek.dataportal.feature.certification;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.CheckedInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.scope.FeatureScoped;
import static org.inek.dataportal.common.helper.StreamHelper.BUFFER_LENGHT;

/**
 *
 * @author vohldo, muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class EditCert extends AbstractEditController {

    private static final Logger LOGGER = Logger.getLogger("EditCert");

    @Inject private ConfigFacade _config;
    @Inject private SessionController _sessionController;
    @Inject private SystemFacade _systemFacade;
    
//    @PostConstruct
//    private void init() {
//        LOGGER.log(Level.WARNING, "Init EditCert");
//    }
//
//    @PreDestroy
//    private void destroy() {
//        LOGGER.log(Level.WARNING, "Destroy EditCert");
//    }
    @Override
    protected void addTopics() {
        if (_sessionController.isInekUser(Feature.CERT)) {
            addTopic(CertTabs.tabCertSystemManagement.name(), Pages.CertSystemManagement.URL());
            addTopic(CertTabs.tabCertGrouperCC.name(), Pages.CertGrouperCc.URL());
            addTopic(CertTabs.tabCertMailTemplate.name(), Pages.CertMailTemplate.URL());
            addTopic(CertTabs.tabCertMail.name(), Pages.CertMail.URL());
            addTopic(CertTabs.tabCertification.name(), Pages.CertCertification.URL());
        }
        addTopic(CertTabs.tabCertification.name(), Pages.CertCertification.URL());
    }

    private enum CertTabs {

        tabCertSystemManagement,
        tabCertGrouperCC,
        tabCertMailTemplate,
        tabCertMail,
        tabCertification;
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

    public String getSpecFileName(int systemId, boolean includeUpload) {
        return getCertFileName(systemId, "Spec", "Grouper-Spezifikation", "exe", includeUpload);
    }

    public String getTestFileName(int systemId, boolean includeUpload) {
        return getCertFileName(systemId, "Daten", "Testdaten", "zip", includeUpload);
    }

    public String getCertFileName(int systemId, boolean includeUpload) {
        return getCertFileName(systemId, "Daten", "Zertdaten", "zip", includeUpload);
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
        RemunerationSystem system = _systemFacade.findFresh(systemId);
        if (system == null) {
            LOGGER.log(Level.WARNING, "Certification, missing system with id {0}", systemId);
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
    public Optional<File> getUploadFolder(RemunerationSystem system, String folderName) {
        File folder = new File(getSystemRoot(system), folderName);
        try {
            folder.mkdirs();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "upload, error during creating folder {0}", folder.getAbsolutePath());
            return Optional.empty();
        }
        return Optional.of(folder);
    }
   // <editor-fold defaultstate="collapsed" desc="SystemRoot">
    public File getSystemRoot(RemunerationSystem system) {
        File root = new File(_config.readConfig(ConfigKey.CertiFolderRoot), "System " + system.getYearSystem());
        File systemRoot = new File(root, system.getFileName());
        return systemRoot;
    }
    // </editor-fold>
    

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
        LOGGER.log(Level.INFO, "uploading file {0}", uploadFile.getSubmittedFileName());
        try (InputStream inStream = uploadFile.getInputStream();
                BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(target), BUFFER_LENGHT)) {
            new StreamHelper().copyStream(inStream, dest);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "upload exception: {0}", e.getMessage());
        }
    }

    public void uploadAndUnzipFile(Part uploadFile, File target) {
        if (uploadFile == null) {
            return;
        }
        LOGGER.log(Level.INFO, "uploading file {0}", uploadFile.getSubmittedFileName());
        try (InputStream inStream = uploadFile.getInputStream();
                CheckedInputStream checksum = new CheckedInputStream(inStream, new Adler32());
                ZipInputStream zis = new ZipInputStream(new BufferedInputStream(checksum))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                try (BufferedOutputStream dest = new BufferedOutputStream(new FileOutputStream(target), BUFFER_LENGHT)) {
                    new StreamHelper().copyStream(zis, dest);
                    dest.flush();
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "upload exception: {0}", e.getMessage());
        }
    }

}
