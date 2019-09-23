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
import org.inek.dataportal.common.data.KhComparison.checker.AebComparer;
import org.inek.dataportal.common.data.KhComparison.entities.*;
import org.inek.dataportal.common.data.KhComparison.enums.PsyEvaluationType;
import org.inek.dataportal.common.data.KhComparison.enums.PsyHosptalComparisonHospitalsType;
import org.inek.dataportal.common.data.KhComparison.enums.PsyHosptalComparisonStatus;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.data.icmt.entities.Customer;
import org.inek.dataportal.common.data.icmt.enums.State;
import org.inek.dataportal.common.data.icmt.facade.CustomerFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author lautenti
 */
@Named
@FeatureScoped
public class Evaluation {

    private static final String ALL_STATE_IDS = "1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16";
    private static final Logger LOGGER = Logger.getLogger("PSY_Evaluation");

    @Inject
    private AEBFacade _aebFacade;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private SessionController _sessionController;
    @Inject
    private CustomerFacade _customerFacade;
    @Inject
    private ConfigFacade _config;

    private int _selectedIk = 0;
    private int _selectedAgreementYear = 0;

    private List<HospitalComparisonInfo> _listEvaluations = new ArrayList<>();
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

    public List<HospitalComparisonInfo> getListEvaluations() {
        return _listEvaluations;
    }

    public void setListEvaluations(List<HospitalComparisonInfo> listEvaluations) {
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
        //_listEvaluations = _aebFacade.getHosptalComparisonInfoByAccount(_sessionController.getAccount());
    }

    public Set<Integer> getAllowedIks() {
        return _accessManager.retrieveAllowedManagedIks(Feature.HC_HOSPITAL);
    }

    public boolean isCreateNewEvaluationAllowed() {
        //todo klären, ob es eine einschränkung gib
        return true;
    }

    public void ikChanged() {
        _validYears.clear();
        List<Integer> usedDataYears = _aebFacade.getUsedDataYears(_selectedIk);
        for (Integer value : usedDataYears) {
            _validYears.add(value + 1);
        }
    }

    public void startInfoEvaluation() {
        if (isReadeForEvaluation()) {
            setEvaluationsList();
            DialogController.showInfoDialog("Auswertung gestartet", "Wir erstellen nun die Auswertung nach Ihren Kriterien. " +
                    "Sobald der Vorgang abgeschlossen ist, wird diese Ihnen als Dokument im InEK Datenportal zur Verfügung gestellt. " +
                    "Sie werden per E-Mail über die Bereitstellung informaiert.");
        } else {
            DialogController.showErrorDialog("Daten unvollständig", "Bitte wählen Sie eine gültige IK und Vereinbarungsjahr.");
        }
    }

    public void startEvaluation() {
        if (isReadeForEvaluation()) {
            Customer customerByIK = _customerFacade.getCustomerByIK(_selectedIk);
            if (createHospitalComparisonInfo(customerByIK)) {
                DialogController.showInfoDialog("Auswertung gestartet", "Wir erstellen nun die Auswertung nach Ihren Kriterien. " +
                        "Sobald der Vorgang abgeschlossen ist, wird diese Ihnen als Dokument im InEK Datenportal zur Verfügung gestellt. " +
                        "Sie werden per E-Mail über die Bereitstellung informaiert.");
                setEvaluationsList();

            } else {
                DialogController.showInfoDialog("Keine Auswertung möglich", "Es konnte keine Vergleichsgruppe gebildet " +
                        "werden. Bitte versuchen Sie es später nocheinmal.");
            }
        } else {
            DialogController.showErrorDialog("Daten unvollständig", "Bitte wählen Sie eine gültige IK und Vereinbarungsjahr.");
        }
    }

    private boolean isReadeForEvaluation() {
        return _selectedIk > 0 && _selectedAgreementYear > 0;
    }

    private boolean createHospitalComparisonInfo(Customer cus) {
        HospitalComparisonInfo newInfo = new HospitalComparisonInfo();
        newInfo.setAccountId(_sessionController.getAccountId());
        newInfo.setAccountFirstName(_sessionController.getAccount().getFirstName());
        newInfo.setAccountLastName(_sessionController.getAccount().getLastName());
        newInfo.setAgreementYear(_selectedAgreementYear);
        newInfo.setHospitalIk(_selectedIk);
        newInfo.setCreatedAt(new Date());
        newInfo.setHospitalComparisonId(createNewHcId());
        newInfo.setHospitalStateId(cus.getPsyState().equals(State.Unknown) ? cus.getState().getId() : cus.getState().getId());
        newInfo.setHospitalPsyGroup(_aebFacade.getPsyGroupByIkAndYear(_selectedIk, _selectedAgreementYear - 1));
        ensureHospitalComparisonEvaluations(newInfo);
        ensureHospitalComparisonJob(newInfo);
        ensureAebConflicts(newInfo);
        if (newInfo.getHospitalComparisonEvaluation().isEmpty()) {
            return false;
        }
        _aebFacade.save(newInfo);
        return true;
    }

    private void ensureAebConflicts(HospitalComparisonInfo newInfo) {
        for (HospitalComparisonEvaluation evaluation : newInfo.getHospitalComparisonEvaluation()) {
            for (HospitalComparisonHospitals hospital : evaluation.getHospitalComparisonHospitalsGroup()) {
                Optional<AEBBaseInformation> baseInfo = _aebFacade.getBaseInformationForComparing(hospital.getAebBaseInformationId());
                if (baseInfo.isPresent()) {
                    AEBBaseInformation aebBaseInformation1 = baseInfo.get();
                    AEBBaseInformation aebBaseInformation2 = _aebFacade.findAEBBaseInformation(hospital.getId());
                    AebComparer comparer = new AebComparer();
                    if (!comparer.compare(aebBaseInformation1, aebBaseInformation2)) {
                        _aebFacade.insertNewCompatingConflict(aebBaseInformation1, hospital);
                    }
                }
            }
        }

    }

    private void ensureHospitalComparisonJob(HospitalComparisonInfo newInfo) {
        HospitalComparisonJob newJob = new HospitalComparisonJob();
        newJob.setStatus(PsyHosptalComparisonStatus.NEW);
        newInfo.setHospitalComparisonJob(newJob);
    }

    private void ensureHospitalComparisonEvaluations(HospitalComparisonInfo info) {
        for (PsyEvaluationType psyEvaluationType : PsyEvaluationType.values()) {
            Optional<HospitalComparisonEvaluation> evaluation = getHosptalComparisonEvaluations(psyEvaluationType, info);
            evaluation.ifPresent(info::addHospitalComparisonEvaluation);
        }
    }

    private Optional<HospitalComparisonEvaluation> getHosptalComparisonEvaluations(PsyEvaluationType psyEvaluationType, HospitalComparisonInfo info) {
        HospitalComparisonEvaluation evaluation = new HospitalComparisonEvaluation();
        evaluation.setEvaluationType(psyEvaluationType);
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
        evaluation.addHospitalComparisonHospitals(createHosptalComparisonHospitalsForIdsAndType(aebId, PsyHosptalComparisonHospitalsType.Hospital));


        evaluation.addHospitalComparisonHospitals(createHosptalComparisonHospitalsForIdsAndType(aebIdsForGroup,
                PsyHosptalComparisonHospitalsType.Group));

        return Optional.of(evaluation);
    }


    private List<HospitalComparisonHospitals> createHosptalComparisonHospitalsForIdsAndType(int id, PsyHosptalComparisonHospitalsType type) {
        List<Integer> idList = new ArrayList<>();
        idList.add(id);
        return createHosptalComparisonHospitalsForIdsAndType(idList, type);
    }

    private String createNewHcId() {
        return RandomStringUtils.randomAlphanumeric(15);
    }

    private List<HospitalComparisonHospitals> createHosptalComparisonHospitalsForIdsAndType(List<Integer> ids,
                                                                                            PsyHosptalComparisonHospitalsType type) {
        List<HospitalComparisonHospitals> hospitalComparisonHospitals = new ArrayList<>();

        for (Integer id : ids) {
            HospitalComparisonHospitals ho = new HospitalComparisonHospitals();
            ho.setAebBaseInformationId(id);
            ho.setType(type);
            hospitalComparisonHospitals.add(ho);
        }

        return hospitalComparisonHospitals;
    }

    public StreamedContent downloadEvaluation(HospitalComparisonInfo evaluation) {
        String jobFolder = evaluation.getHospitalComparisonJob().getEvaluationFilePath(_config.readConfig(ConfigKey.KhComparisonJobSavePath));
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(jobFolder));
            ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            return new DefaultStreamedContent(stream, "applikation/pdf", evaluation.getHospitalComparisonJob().getEvaluationFileName());
        } catch (Exception ex) {
            DialogController.showErrorDialog("Fehler beim herunterladen", "Die Datei konnte nicht heruntergeldaden werden. " +
                    "Bitte versuchen Sie es später nocheinmal, oder kontaktieren Sie die Datenstelle.");
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex.getStackTrace());
            return null;
        }
    }

    public boolean evaluationIsReadyForDownload(HospitalComparisonInfo evaluation) {
        return evaluation.getHospitalComparisonJob().getStatus().equals(PsyHosptalComparisonStatus.DONE);
    }
}
