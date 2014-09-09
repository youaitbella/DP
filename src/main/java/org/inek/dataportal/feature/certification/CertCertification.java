package org.inek.dataportal.feature.certification;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.certification.Grouper;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.certification.GrouperFacade;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.helper.StreamHelper;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class CertCertification {

    private static final Logger _logger = Logger.getLogger("CertCertification");

    @Inject private SessionController _sessionController;
    @Inject private SystemFacade _systemFacade;
    @Inject private GrouperFacade _grouperFacade;

    @PostConstruct
    private void init() {
        _logger.log(Level.WARNING, "Init CertCertification");
    }

    @PreDestroy
    private void destroy() {
        _logger.log(Level.WARNING, "Destroy CertCertification");
    }

    public List<SelectItem> getSystems4Account() {

        List<SelectItem> list = new ArrayList<>();
        for (RemunerationSystem system : _sessionController.getAccount().getRemuneratiosSystems()) {
            if (system.isApproved()) {
                list.add(new SelectItem(system.getId(), system.getDisplayName()));
            }
        }
        SelectItem emptyItem = new SelectItem(-1, Utils.getMessage("itemChoose"));
        emptyItem.setNoSelectionOption(true);
        list.add(emptyItem);
        return list;
    }

    private Grouper _grouper = new Grouper();

    public Grouper getGrouper() {
        return _grouper;
    }

    public void setGrouper(Grouper grouper) {
        _grouper = grouper;
    }

    public int getSystemId() {
        return _grouper.getSystemId();
    }

    public void setSystemId(int systemId) {
        if (systemId != _grouper.getId()) {
            if (systemId <= 0) {
                _grouper = new Grouper();
            } else {
                _grouper = _grouperFacade.findByAccountAndSystemId(_sessionController.getAccountId(), systemId);
            }
            setGrouperChanged(false);
        }
    }

    private boolean _grouperChanged = false;

    public boolean isGrouperChanged() {
        return _grouperChanged;
    }

    public void setGrouperChanged(boolean isChanged) {
        _grouperChanged = isChanged;
    }

    public void grouperChangeListener(AjaxBehaviorEvent event) {
        setGrouperChanged(true);
    }

    private Part _file;

    public Part getFile() {
        return _file;
    }

    public void setFile(Part file) {
        _file = file;
    }

    public String download(int systemId, String folder, String fileNameBase, String extension) {
        if (systemId <= 0) {
            return "";
        }
        EditCert editCert = FeatureScopedContextHolder.Instance.getBean(EditCert.class);
        File file = editCert.getCertFile(systemId, folder, fileNameBase, extension);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        try {
            try (BufferedInputStream is = new BufferedInputStream(new FileInputStream(file), StreamHelper.BufLen)) {
                externalContext.setResponseHeader("Content-Type", "text/plain");
                externalContext.setResponseHeader("Content-Length", "");
                externalContext.setResponseHeader("Content-Disposition", "attachment;filename=\"" + file.getName() + "\"");
                new StreamHelper().copyStream(is, externalContext.getResponseOutputStream());
            }
        } catch (IOException ex) {
            _logger.log(Level.SEVERE, null, ex);
            return Pages.Error.URL();
        }
        facesContext.responseComplete();
        return "";
    }

    public String getNextUpload() {
        if (_grouper.getId() < 0) {
            return "";
        }
        if (_grouper.getTestUpload1() == null) {
            return "TestDaten v1";
        }
        if (_grouper.getTestUpload2() == null && _grouper.getTestError1() > 0) {
            return "TestDaten v2";
        }
        if (_grouper.getTestUpload3() == null && _grouper.getTestError2() > 0) {
            return "TestDaten v3";
        }
        if (_grouper.getTestUpload3() != null && _grouper.getTestError3() > 0) {
            return "";
        }
        if (_grouper.getCertUpload1() == null && (_grouper.getTestError1() == 0 || _grouper.getTestError2() == 0 || _grouper.getTestError2() == 0)) {
            return "ZertDaten v1";
        }
        if (_grouper.getCertUpload2() == null && _grouper.getCertError1() > 0) {
            return "ZertDaten v2";
        }
        return "";
    }

    public void uploadTestResult() {
        RemunerationSystem system = _systemFacade.find(_grouper.getSystemId());
        if (system == null) {
            _logger.log(Level.WARNING, "upload, missing system with id {0}", _grouper.getSystemId());
            return;
        }
        EditCert editCert = FeatureScopedContextHolder.Instance.getBean(EditCert.class);
        Optional<File> uploadFolderBase = editCert.getUploadFolder(system, "Daten");  // todo: folder depending on account
        if (!uploadFolderBase.isPresent()) {
            return;
        }
        String prefix = getNextUpload();
        File uploadFolder = new File(uploadFolderBase.get(), "" + _sessionController.getAccountId());
        String outFile = prefix + ".zip";
        editCert.uploadFile(_file, new File(uploadFolder, outFile));
    }

    public String saveGrouper() {
        _grouper = _grouperFacade.merge(_grouper);
        RemunerationSystem system = _systemFacade.find(_grouper.getSystemId());
        persistFiles(new File(system.getSystemRoot(), "Spec"));
        persistFiles(new File(system.getSystemRoot(), "Daten"));
        setGrouperChanged(false);
        return Pages.CertCertification.RedirectURL();
    }

    public String cancelSystem() {
        cleanupUploadFiles();
        setSystemId(_grouper.getSystemId());
        return Pages.CertCertification.RedirectURL();
    }

    private void cleanupUploadFiles() {
        RemunerationSystem system = _systemFacade.find(_grouper.getSystemId());
        deleteFiles(new File(system.getSystemRoot(), "Spec"), ".*\\.upload");
        deleteFiles(new File(system.getSystemRoot(), "Daten"), ".*\\.upload");
    }

    public void deleteFiles(File dir, final String fileNamePattern) {
        if (!dir.exists()) {
            return;
        }
        for (File file : dir.listFiles((File file) -> file.isFile() && file.getName().matches(fileNamePattern))) {
            file.delete();
        }
    }

    private void persistFiles(File dir) {
        if (!dir.exists()) {
            return;
        }
        for (File file : dir.listFiles((File file) -> file.isFile() && file.getName().endsWith(".upload"))) {
            File target = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().length() - 7));
            if (target.exists()) {
                target.delete();
            }
            file.renameTo(target);
        }
    }

}
