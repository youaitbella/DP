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
import org.inek.dataportal.common.data.KhComparison.entities.HosptalComparisonHospitals;
import org.inek.dataportal.common.data.KhComparison.entities.HosptalComparisonInfo;
import org.inek.dataportal.common.data.KhComparison.enums.PsyEvaluationType;
import org.inek.dataportal.common.data.KhComparison.enums.PsyHosptalComparisonHospitalsType;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.icmt.entities.Customer;
import org.inek.dataportal.common.data.icmt.enums.State;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.*;

/**
 * @author lautenti
 */
@Named
@FeatureScoped
public class Evaluation {

    private static final String ALL_STATE_IDS = "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16";

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
        newInfo.setHospitalStateId(cus.getPsyState().equals(State.Unknown) ? cus.getState().getId() : cus.getState().getId());
        newInfo.setHospitalPsyGroup(_aebFacade.getPsyGroupByIkAndYear(_selectedIk, _selectedAgreementYear - 1));
        ensureHosptalComparisonEvaluations(newInfo);
        _aebFacade.save(newInfo);
    }

    private void ensureHosptalComparisonEvaluations(HosptalComparisonInfo info) {
        for (PsyEvaluationType psyEvaluationType : PsyEvaluationType.values()) {
            Optional<HosptalComparisonEvaluations> evaluation = getHosptalComparisonEvaluations(psyEvaluationType, info);
            evaluation.ifPresent(info::addHosptalComparisonEvaluations);
        }
    }

    private Optional<HosptalComparisonEvaluations> getHosptalComparisonEvaluations(PsyEvaluationType psyEvaluationType, HosptalComparisonInfo info) {
        HosptalComparisonEvaluations evaluation = new HosptalComparisonEvaluations();
        evaluation.setEvaluationTypeId(psyEvaluationType.getId());
        int aebId = 0;
        List<Integer> aebIdsForGroup = new ArrayList<>();

        switch (psyEvaluationType) {
            case Type_1:
                aebId = _aebFacade.getAebIdForEvaluationHospital1_4_7(info.getHospitalIk(),
                        info.getAgreementYear() - 1);

                aebIdsForGroup = _aebFacade.getAebIdsForEvaluationGroup1_7(String.valueOf(info.getHospitalStateId()),
                        info.getAgreementYear() - 1, info.getHospitalPsyGroup());
                break;
            case Type_2:
                aebId = _aebFacade.getAebIdForEvaluationHospital2_3_5_6_8_9(info.getHospitalIk(),
                        info.getAgreementYear() - 1);

                aebIdsForGroup = _aebFacade.getAebIdsForEvaluationGroup2_3_8_9(String.valueOf(info.getHospitalStateId()),
                        info.getAgreementYear() - 1, info.getHospitalPsyGroup());
                break;
            case Type_3:
                aebId = _aebFacade.getAebIdForEvaluationHospital2_3_5_6_8_9(info.getHospitalIk(),
                        info.getAgreementYear() - 2);

                aebIdsForGroup = _aebFacade.getAebIdsForEvaluationGroup2_3_8_9(String.valueOf(info.getHospitalStateId()),
                        info.getAgreementYear() - 2, info.getHospitalPsyGroup());
                break;
            case Type_4:
                aebId = _aebFacade.getAebIdForEvaluationHospital1_4_7(info.getHospitalIk(),
                        info.getAgreementYear() - 1);

                aebIdsForGroup = _aebFacade.getAebIdsForEvaluationGroup4(String.valueOf(info.getHospitalStateId()),
                        info.getAgreementYear() - 1);
                break;
            case Type_5:
                aebId = _aebFacade.getAebIdForEvaluationHospital2_3_5_6_8_9(info.getHospitalIk(),
                        info.getAgreementYear() - 1);

                aebIdsForGroup = _aebFacade.getAebIdsForEvaluationGroup5_6(String.valueOf(info.getHospitalStateId()),
                        info.getAgreementYear() - 1);

                break;
            case Type_6:
                aebId = _aebFacade.getAebIdForEvaluationHospital2_3_5_6_8_9(info.getHospitalIk(),
                        info.getAgreementYear() - 2);

                aebIdsForGroup = _aebFacade.getAebIdsForEvaluationGroup5_6(String.valueOf(info.getHospitalStateId()),
                        info.getAgreementYear() - 2);
                break;
            case Type_7:
                aebId = _aebFacade.getAebIdForEvaluationHospital1_4_7(info.getHospitalIk(),
                        info.getAgreementYear() - 1);
                aebIdsForGroup = _aebFacade.getAebIdsForEvaluationGroup1_7(ALL_STATE_IDS,
                        info.getAgreementYear() - 1, info.getHospitalPsyGroup());
                break;
            case Type_8:
                aebId = _aebFacade.getAebIdForEvaluationHospital2_3_5_6_8_9(info.getHospitalIk(),
                        info.getAgreementYear() - 1);

                aebIdsForGroup = _aebFacade.getAebIdsForEvaluationGroup2_3_8_9(ALL_STATE_IDS,
                        info.getAgreementYear() - 1, info.getHospitalPsyGroup());
                break;
            case Type_9:
                aebId = _aebFacade.getAebIdForEvaluationHospital2_3_5_6_8_9(info.getHospitalIk(),
                        info.getAgreementYear() - 2);

                aebIdsForGroup = _aebFacade.getAebIdsForEvaluationGroup2_3_8_9(ALL_STATE_IDS,
                        info.getAgreementYear() - 2, info.getHospitalPsyGroup());
                break;
            default:
                throw new IllegalArgumentException("invalid psyEvaluationType: " + psyEvaluationType.toString());
        }

        if (aebId == 0 || aebIdsForGroup.size() < 4) {
            return Optional.empty();
        }

        // Add aebids to evaluationType
        evaluation.addHosptalComparisonHospitals(createHosptalComparisonHospitalsForIdsAndType(new ArrayList<>(aebId),
                PsyHosptalComparisonHospitalsType.Hospital));

        evaluation.addHosptalComparisonHospitals(createHosptalComparisonHospitalsForIdsAndType(aebIdsForGroup,
                PsyHosptalComparisonHospitalsType.Group));

        return Optional.of(evaluation);
    }

    private String createNewHcId() {
        return RandomStringUtils.randomAlphanumeric(15);
    }

    private List<HosptalComparisonHospitals> createHosptalComparisonHospitalsForIdsAndType(List<Integer> ids,
                                                                                           PsyHosptalComparisonHospitalsType type) {
        List<HosptalComparisonHospitals> hosptalComparisonHospitals = new ArrayList<>();

        for (Integer id : ids) {
            HosptalComparisonHospitals ho = new HosptalComparisonHospitals();
            ho.setAebBaseInformationId(id);
            ho.setType(type);
        }

        return hosptalComparisonHospitals;
    }
}
