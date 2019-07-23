/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.khcomparison.backingbean;

import org.apache.commons.lang3.RandomStringUtils;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.KhComparison.entities.HosptalComparisonEvaluations;
import org.inek.dataportal.common.data.KhComparison.entities.HosptalComparisonInfo;
import org.inek.dataportal.common.data.KhComparison.enums.PsyEvaluationType;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.icmt.entities.Customer;
import org.inek.dataportal.common.data.icmt.enums.State;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.omg.DynamicAny.DynArray;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author lautenti
 */
@Named
@FeatureScoped
public class Evaluation {

    @Inject
    private AEBFacade _aebFacade;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private SessionController _sessionController;
    @Inject
    private CustomerFacade _customerFacade;

    private int _selectedIk = 0;
    private int _selectedAgreementYear = 0;

    private List<HosptalComparisonInfo> _listEvaluations = new ArrayList<>();
    private List<Integer> _validYears = new ArrayList<>();


    public int getSelectedIk() {
        return _selectedIk;
    }

    public void setSelectedIk(int selectedIk) {
        this._selectedIk = selectedIk;
    }

    public int getSelectedAgreementYear() {
        return _selectedAgreementYear;
    }

    public void setSelectedAgreementYear(int selectedAgreementYear) {
        this._selectedAgreementYear = selectedAgreementYear;
    }

    public List<HosptalComparisonInfo> getListEvaluations() {
        return _listEvaluations;
    }

    public void setListEvaluations(List<HosptalComparisonInfo> listEvaluations) {
        this._listEvaluations = listEvaluations;
    }

    public List<Integer> getValidYears() {
        return _validYears;
    }

    public void setValidYears(List<Integer> validYears) {
        this._validYears = validYears;
    }

    @PostConstruct
    public void init() {
        setEvaluationsList();
    }

    private void setEvaluationsList() {
        Set<Integer> iks = _accessManager.retrieveAllowedManagedIks(Feature.HC_HOSPITAL);
        _listEvaluations = _aebFacade.getHosptalComparisonInfoByIks(iks);
    }

    public Set<Integer> getAllowedIks() {
        return _accessManager.retrieveAllowedManagedIks(Feature.HC_HOSPITAL);
    }

    public boolean isCreateNewEvaluationAllowed() {
        //todo machen
        return true;
    }

    public void ikChanged() {
        //_validYears = _aebFacade.getUsedDataYears(_selectedIk);
        _validYears.clear();
        _validYears.add(2019);
    }

    public void startInfoEvaluation() {
        if (isReadeForEvaluation()) {
            setEvaluationsList();
            DialogController.showInfoDialog("Noch nicht", "Wird demnächts irgendwann funktionieren");
        } else {
            DialogController.showErrorDialog("Daten unvollständig", "Bitte wählen Sie eine gültige IK und Vereinbarungsjahr.");
        }
    }

    public void startEvaluation() {
        if (isReadeForEvaluation()) {
            Customer customerByIK = _customerFacade.getCustomerByIK(_selectedIk);
            createHosptalComparisonInfo(customerByIK);
            setEvaluationsList();
            DialogController.showInfoDialog("Noch nicht", "Wird demnächts irgendwann funktionieren");
        } else {
            DialogController.showErrorDialog("Daten unvollständig", "Bitte wählen Sie eine gültige IK und Vereinbarungsjahr.");
        }
    }

    private boolean isReadeForEvaluation() {
        return _selectedIk > 0 && _selectedAgreementYear > 0;
    }

    private void createHosptalComparisonInfo(Customer cus) {
        HosptalComparisonInfo newInfo = new HosptalComparisonInfo();
        newInfo.setAccountId(_sessionController.getAccountId());
        newInfo.setAccountFirstName(_sessionController.getAccount().getFirstName());
        newInfo.setAccountLastName(_sessionController.getAccount().getLastName());
        newInfo.setAgreementYear(_selectedAgreementYear);
        newInfo.setHospitalIk(_selectedIk);
        newInfo.setCreatedAt(new Date());
        newInfo.setHospitalComparisonId(createNewHcId());
        newInfo.setHospitalStateId(cus.getPsyState().equals(State.Unknown) ? cus.getState().getId() : cus.getState().getId() );
        newInfo.setHospitalTypeId(cus.getPsyHospitalType().getId());
        ensureHosptalComparisonEvaluations(newInfo);
        _aebFacade.save(newInfo);
    }

    private void ensureHosptalComparisonEvaluations(HosptalComparisonInfo info) {
        for (PsyEvaluationType psyEvaluationType : PsyEvaluationType.values()) {
            HosptalComparisonEvaluations evaluation = new HosptalComparisonEvaluations();
            evaluation.setEvaluationTypeId(psyEvaluationType.getId());
            info.addHosptalComparisonEvaluations(evaluation);
        }
    }

    private String createNewHcId() {
        return RandomStringUtils.randomAlphanumeric(15);
    }
}
