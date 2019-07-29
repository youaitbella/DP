package org.inek.psyEvaluationService.backingBean;

import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import java.io.Serializable;

@ManagedBean(name = "evaluationServiceBean")
@ApplicationScoped
public class EvaluationServiceBean implements Serializable {

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

}
