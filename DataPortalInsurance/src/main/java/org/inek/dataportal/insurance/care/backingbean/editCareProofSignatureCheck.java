package org.inek.dataportal.insurance.care.backingbean;

import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.insurance.care.facade.CareSignatureCheckerFacade;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@FeatureScoped
public class editCareProofSignatureCheck {

    private static final String VALID_SIGNATURE = "Die Signatur ist gültig";
    private static final String NOT_VALID_SIGNATURE = "Die Signatur ist nicht gültig";
    private CareSignatureCheckerFacade _signatureCheckerFacade;
    private String _signature;
    private SignatureEntry _entry;
    private String _checkMessage;

    public editCareProofSignatureCheck() {
    }

    @Inject
    public editCareProofSignatureCheck(CareSignatureCheckerFacade signatureCheckerFacade) {
        _signatureCheckerFacade = signatureCheckerFacade;
    }

    public String getSignature() {
        return _signature;
    }

    public void setSignature(String signature) {
        this._signature = signature;
    }

    public SignatureEntry getEntry() {
        return _entry;
    }

    public String getCheckMessage() {
        return _checkMessage;
    }

    @PostConstruct
    public void init() {
        _entry = new SignatureEntry();
    }

    public void checkSignature() {
        try {
            _entry = _signatureCheckerFacade.retrieveInformationForSignature(_signature);
            _checkMessage = VALID_SIGNATURE;
        } catch (Exception ex) {
            _entry = new SignatureEntry();
            _checkMessage = NOT_VALID_SIGNATURE;
        }
    }

}
