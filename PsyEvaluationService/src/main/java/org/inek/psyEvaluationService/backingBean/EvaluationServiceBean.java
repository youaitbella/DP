package org.inek.psyEvaluationService.backingBean;

import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;

import javax.annotation.PostConstruct;
import javax.ejb.Asynchronous;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

@ManagedBean(name = "evaluationServiceBean")
@ApplicationScoped
public class EvaluationServiceBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EvaluationServiceBean.class.toString());

    @Inject
    private AEBFacade _aebFacade;
    @Inject
    private ConfigFacade _config;

    private boolean _scanEnable = false;

    public boolean isScanEnable() {
        return _scanEnable;
    }

    @PostConstruct
    public void init() {

    }

    public void switchScannerEnable() {
        _scanEnable = !_scanEnable;
    }

    public void start() {
        startWorking();
    }

    @Asynchronous
    private void startWorking() {
        while (true) {
            if (_scanEnable) {
                LOGGER.log(Level.INFO, "TestLog");
            }
        }
    }

}
