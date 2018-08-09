package org.inek.dataportal.cert;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.cert.entities.RemunerationSystem;
import org.inek.dataportal.cert.facade.SystemFacade;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.cert.Helper.CertFileHelper;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.scope.FeatureScoped;

/**
 *
 * @author vohldo, muellermi
 */
@Named
@FeatureScoped(name = "Certification")
public class EditCert extends AbstractEditController {

    private static final Logger LOGGER = Logger.getLogger("EditCert");

    @Inject
    private SessionController _sessionController;
    @Inject
    private SystemFacade _systemFacade;

    @Override
    protected void addTopics() {
        if (_sessionController.isInekUser(Feature.CERT)) {
            addTopic(CertTabs.tabCertSystemManagement.name(), Pages.CertSystemManagement.URL());
            addTopic(CertTabs.tabCertMailTemplate.name(), Pages.CertMailTemplate.URL());
            addTopic(CertTabs.tabCertMail.name(), Pages.CertMail.URL());
            addTopic(CertTabs.tabCertification.name(), Pages.CertCertification.URL());
        }
        addTopic(CertTabs.tabCertification.name(), Pages.CertCertification.URL());
        addTopic(CertTabs.tabCertGrouperCC.name(), Pages.CertGrouperCc.URL());
    }

    private enum CertTabs {
        tabCertSystemManagement,
        tabCertGrouperCC,
        tabCertMailTemplate,
        tabCertMail,
        tabCertification;
    }

    public String getSpecFileName(int systemId) {
        return getCertFileName(systemId, "Spec", "Grouper-Spezifikation", "exe");
    }

    public String getTestFileName(int systemId) {
        return getCertFileName(systemId, "Daten", "Testdaten", "zip");
    }

    public String getCertFileName(int systemId) {
        return getCertFileName(systemId, "Daten", "Zertdaten", "zip");
    }

    public String getCertFileName(int systemId, String folder, String fileNameBase, String extension) {
        if (systemId <= 0) {
            return "";
        }
        String fileName = CertFileHelper.getCertFile(_systemFacade.findFresh(systemId), folder, fileNameBase, extension).getName();
        return fileName;
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
}
