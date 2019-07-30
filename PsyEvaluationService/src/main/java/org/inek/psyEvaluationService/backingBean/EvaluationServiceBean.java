package org.inek.psyEvaluationService.backingBean;

import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;

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

    public void switchScannerEnable() {
        _scanEnable = !_scanEnable;
    }

    private void startWorking() {
        while (true) {
            LOGGER.log(Level.INFO, "TestLog");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
