package org.inek.dataportal.feature.certification;

import java.util.logging.Logger;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.enums.Feature;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.facades.admin.MailTemplateFacade;
import org.inek.dataportal.facades.certification.EmailReceiverFacade;
import org.inek.dataportal.facades.certification.EmailReceiverLabelFacade;
import org.inek.dataportal.facades.certification.SystemFacade;
import org.inek.dataportal.feature.AbstractEditController;
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
    @Inject private AccountFacade _accFacade;
    @Inject private MailTemplateFacade _mtFacade;
    @Inject private EmailReceiverFacade _erFacade;
    @Inject private EmailReceiverLabelFacade _erlFacade;

    private CertMail _certMail;
    public CertMail getCertMail() {
        if (_certMail == null) {
            _certMail = new CertMail(_mtFacade, _systemFacade, _accFacade, _erFacade, _erlFacade);
        }
        return _certMail;
    }

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
            addTopic(CertTabs.tabCertMail.name(), Pages.CertMail.URL());
            addTopic(CertTabs.tabCertification.name(), Pages.CertCertification.URL());
        }
        addTopic(CertTabs.tabCertification.name(), Pages.CertCertification.URL());
    }

    private enum CertTabs {

        tabCertSystemManagement,
        tabCertMail,
        tabCertification;
    }

}
