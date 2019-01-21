/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.insurance.care.backingbean;

import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.insurance.care.facade.CareSignatureCheckerFacade;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author lautenti
 */
@Named
@FeatureScoped
public class editCareProofSignatureCheck {

    @Inject
    private CareSignatureCheckerFacade _signatureCheckerFacade;

    private final static String VALID_SIGNATURE = "Die Signatur ist gültig";
    private final static String NOT_VALID_SIGNATURE = "Die Signatur ist nicht gültig";

    private String _signature;
    private SignatureEntry _entry;
    private String _checkMessage;

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
        List<Object[]> objects = _signatureCheckerFacade.retrieveInformationForSignature(_signature);

        if (objects.size() == 1) {
            setEntryValues(_entry, objects.get(0));
            _checkMessage = VALID_SIGNATURE;
        }
        else {
            _entry = new SignatureEntry();
            _checkMessage = NOT_VALID_SIGNATURE;
        }
    }

    private void setEntryValues(SignatureEntry entry, Object[] object) {
        entry.setSignature((String) object[0]);
        entry.setIk((int) object[1]);
        entry.setHospitalName((String) object[2]);
        entry.setYear((int) object[3]);
        entry.setQuarter((int) object[4]);
    }


    public class SignatureEntry {
        private String _signature = "";
        private int _ik = 0;
        private String _hospitalName = "";
        private int _year = 0;
        private int _quarter = 0;

        public String getSignature() {
            return _signature;
        }

        public void setSignature(String signature) {
            this._signature = signature;
        }

        public int getIk() {
            return _ik;
        }

        public void setIk(int ik) {
            this._ik = ik;
        }

        public String getHospitalName() {
            return _hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this._hospitalName = hospitalName;
        }

        public int getYear() {
            return _year;
        }

        public void setYear(int year) {
            this._year = year;
        }

        public int getQuarter() {
            return _quarter;
        }

        public void setQuarter(int quarter) {
            this._quarter = quarter;
        }
    }

}
