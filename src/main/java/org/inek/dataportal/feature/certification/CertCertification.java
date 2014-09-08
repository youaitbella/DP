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
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.certification.RemunerationSystem;
import org.inek.dataportal.enums.Pages;
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

    @PostConstruct
    private void init() {
        _logger.log(Level.WARNING, "Init CertCertification");
    }

    @PreDestroy
    private void destroy() {
        _logger.log(Level.WARNING, "Destroy CertCertification");
    }

    private RemunerationSystem _system = new RemunerationSystem();

    public RemunerationSystem getSystem() {
        return _system;
    }

    public void setSystem(RemunerationSystem system) {
        _system = system;
    }

    public int getSystemId() {
        return _system.getId();
    }

    public void setSystemId(int templateId) {
        if (templateId != _system.getId()) {
            if (templateId == -1) {
                _system = new RemunerationSystem();
            } else {
                _system = _systemFacade.find(templateId);
            }
            setSystemChanged(false);
        }
    }

    private boolean _systemChanged = false;

    public boolean isSystemChanged() {
        return _systemChanged;
    }

    public void setSystemChanged(boolean isChanged) {
        _systemChanged = isChanged;
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

    public void uploadTestResult(int systemId, int AccountId) {
        RemunerationSystem system = _systemFacade.find(systemId);
        if (system == null) {
            _logger.log(Level.WARNING, "upload, missing system with id {0}", systemId);
            return;
        }
        EditCert editCert = FeatureScopedContextHolder.Instance.getBean(EditCert.class);
        Optional<File> uploadFolder = editCert.getUploadFolder(system, "Daten");  // todo: folder depending on account
        if (!uploadFolder.isPresent()) {
            return;
        }
        String prefix = "ErgebnisUebungsdaten_";
        File lastFile = editCert.getLastFile(uploadFolder.get(), prefix + "\\d\\.txt");
        String lastFileName = lastFile.getName();
        int version = 1;
        if (lastFileName.startsWith(prefix)) {
            version = 1 + Integer.parseInt(lastFileName.substring(prefix.length(), prefix.length() + 1));
        }
        String outFile = prefix + version + ".txt";
        editCert.uploadFile(_file, new File(uploadFolder.get(), outFile));
    }

}
