package org.inek.dataportal.insurance.psychstaff;

import java.io.Serializable;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.insurance.facade.InsuranceFacade;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class SignatureChecker implements Serializable {

    private static final long serialVersionUID = 1L;
    private String _signature;
    private String _checkResult;
    @Inject private InsuranceFacade _data;

    public String getCheckResult() {
        return _checkResult;
    }

    public void setCheckResult(String checkResult) {
        _checkResult = checkResult;
    }

    public String getSignature() {
        return _signature;
    }

    public void setSignature(String signature) {
        _signature = signature;
    }

    public void check() {
        _checkResult = _data.checkSignature(_signature);
    }
}
