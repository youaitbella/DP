/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.nub.backingbean;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.psy.nub.entities.PsyNubProposal;
import org.inek.dataportal.psy.nub.facade.PsyNubFacade;
import org.inek.dataportal.psy.nub.helper.NewPsyNubProposalHelper;
import org.inek.dataportal.psy.nub.helper.PsyNubProposalChecker;
import org.inek.dataportal.psy.nub.helper.PsyNubProposalTemplateHelper;
import org.primefaces.event.FileUploadEvent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * @author lautenti
 */
@Named
@FeatureScoped
public class NubSummary implements Serializable {

    private static final Logger LOGGER = Logger.getLogger("PsyNubSummary");
    @Inject
    private PsyNubFacade _psyNubFacade;
    @Inject
    private AccessManager _accessManager;
    @Inject
    private SessionController _sessionController;
    @Inject
    private ConfigFacade _configFacade;
    private List<PsyNubProposal> _listComplete = new ArrayList<>();
    private List<PsyNubProposal> _listWorking = new ArrayList<>();
    private List<PsyNubProposal> _proposalsFromTemplateUploads = new ArrayList<>();

    private String _selectedWorkingListCommand = "";
    private String _selectedCompleteListCommand = "";

    private String _errorMessages = "";

    public String getErrorMessages() {
        return _errorMessages;
    }

    public String getSelectedWorkingListCommand() {
        return _selectedWorkingListCommand;
    }

    public void setSelectedWorkingListCommand(String selectedWorkingListCommand) {
        this._selectedWorkingListCommand = selectedWorkingListCommand;
    }

    public String getSelectedCompleteListCommand() {
        return _selectedCompleteListCommand;
    }

    public void setSelectedCompleteListCommand(String selectedCompleteListCommand) {
        this._selectedCompleteListCommand = selectedCompleteListCommand;
    }

    public List<PsyNubProposal> getProposalsFromTemplateUploads() {
        return _proposalsFromTemplateUploads;
    }

    public List<PsyNubProposal> getListComplete() {
        return _listComplete;
    }

    public void setListComplete(List<PsyNubProposal> listComplete) {
        this._listComplete = listComplete;
    }

    public List<PsyNubProposal> getListWorking() {
        return _listWorking;
    }

    public void setListWorking(List<PsyNubProposal> listWorking) {
        this._listWorking = listWorking;
    }

    @PostConstruct
    public void init() {
        setWorkingList();
        setCompleteList();
    }

    private void setWorkingList() {
        _listWorking.clear();
        _listWorking.addAll(_psyNubFacade.findAllByAccountIdAndNoIk(_sessionController.getAccountId(), WorkflowStatus.New));
        for (Integer ik : _sessionController.getAccount().getFullIkSet()) {
            if (_accessManager.ikIsManaged(ik, Feature.NUB_PSY)) {
                if (_accessManager.userHasReadAccess(Feature.NUB_PSY, ik)) {
                    _listWorking.addAll(_psyNubFacade.findAllByIkAndStatus(ik, WorkflowStatus.New));
                }
            } else {
                _listWorking.addAll(_psyNubFacade.findAllByAccountIdAndIkAndStatus(_sessionController.getAccountId(), ik, WorkflowStatus.New));
            }
        }
    }

    private void setCompleteList() {
        List<WorkflowStatus> statusForCompleteList = new ArrayList<>();
        statusForCompleteList.add(WorkflowStatus.Accepted);
        statusForCompleteList.add(WorkflowStatus.Updated);
        statusForCompleteList.add(WorkflowStatus.Retired);
        statusForCompleteList.add(WorkflowStatus.Taken);
        statusForCompleteList.add(WorkflowStatus.TakenUpdated);
        _listComplete.clear();

        _listComplete.addAll(_psyNubFacade.findAllByAccountIdAndNoIk(_sessionController.getAccountId(), statusForCompleteList));
        for (Integer ik : _sessionController.getAccount().getFullIkSet()) {
            if (_accessManager.ikIsManaged(ik, Feature.NUB_PSY)) {
                if (_accessManager.userHasReadAccess(Feature.NUB_PSY, ik)) {
                    _listComplete.addAll(_psyNubFacade.findAllByIkAndStatus(ik, statusForCompleteList));
                }
            } else {
                _listComplete.addAll(_psyNubFacade.findAllByAccountIdAndIkAndStatus(_sessionController.getAccountId(), ik, statusForCompleteList));
            }
        }
    }

    public String psyNubEditOpen() {
        return Pages.NubPsyEdit.URL();
    }

    public boolean isCreateEntryAllowed() {
        return _configFacade.readConfigBool(ConfigKey.IsPsyNubCreateEnabled);
    }

    public void deletePsyNubProposal(PsyNubProposal proposal) {
        if (deleteAllowed(proposal)) {
            _psyNubFacade.delete(proposal);
            setWorkingList();
        } else {
            DialogController.showAccessDeniedDialog();
        }
    }

    public void retirePsyNubProposal(PsyNubProposal proposal) {
        if (deleteAllowed(proposal)) {
            proposal.setStatus(WorkflowStatus.Retired);
            proposal.setLastModifiedAt(new Date());
            proposal.setLastChangedByAccountId(_sessionController.getAccountId());
            _psyNubFacade.save(proposal);
            setCompleteList();
        } else {
            DialogController.showAccessDeniedDialog();
        }
    }

    private Boolean deleteAllowed(PsyNubProposal proposal) {
        // TODO prüfen ob Benutzer nub löschen darf
        return true;
    }

    public void handleTemplateUpload(FileUploadEvent file) {
        try {
            String content = new String(file.getFile().getContents(), "UTF-8");
            Optional<PsyNubProposal> newProposal = PsyNubProposalTemplateHelper.createNewProposalFromTemplate(content,
                    _sessionController.getAccount());
            _proposalsFromTemplateUploads.add(newProposal.get());
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error during template import: " + ex.getMessage());
            //TODO Fehlerhaftes einlesen abfangen
        }
    }

    public void createNubsFromTemplates() {
        if (!_proposalsFromTemplateUploads.isEmpty()) {
            for (PsyNubProposal proposal : _proposalsFromTemplateUploads) {
                _psyNubFacade.save(proposal);
            }
            _proposalsFromTemplateUploads.clear();
            setWorkingList();
        } else {
            DialogController.showInfoDialog("Keine Vorlagen ausgewählt", "Bitte ladnen Sie mindesten eine Vorlage hoch, " +
                    "um daraus eine neue NUB zu erzeugen. Ein hochladen von mehreren Vorlagen gleichzeitig ist auch möglich.");
        }
    }

    public void executeBatchWorkingList() {
        if (_listWorking.stream().noneMatch(PsyNubProposal::getSelected)) {
            DialogController.showInfoDialog("Bitte eine NUB auswählen", "Bitte wählen Sie mindestens eine NUB aus");
            return;
        }

        switch (_selectedWorkingListCommand) {
            case "send":
                sendAllSelectedProposals(_listWorking);
                break;
            case "print":
                printAllSelectedProposals(_listWorking);
                break;
            case "":
                DialogController.showInfoDialog("Bitte eine Aktion auswählen", "Bitte wählen Sie eine Aktion aus");
                break;
            default:
                throw new IllegalArgumentException("Unknown batch command:" + _selectedWorkingListCommand);
        }
        setWorkingList();
        setCompleteList();
    }

    public void executeBatchSendList() {
        if (_listComplete.stream().noneMatch(PsyNubProposal::getSelected)) {
            DialogController.showInfoDialog("Bitte eine NUB auswählen", "Bitte wählen Sie mindestens eine NUB aus");
            return;
        }

        switch (_selectedCompleteListCommand) {
            case "createNew":
                createNewPsyProposalsFromSelectedProposals(_listComplete);
                break;
            case "print":
                printAllSelectedProposals(_listComplete);
                break;
            case "":
                DialogController.showInfoDialog("Bitte eine Aktion auswählen", "Bitte wählen Sie eine Aktion aus");
                break;
            default:
                throw new IllegalArgumentException("Unknown batch command:" + _selectedCompleteListCommand);
        }
        setWorkingList();
        setCompleteList();
    }

    private void createNewPsyProposalsFromSelectedProposals(List<PsyNubProposal> listComplete) {
        int counter = 0;
        for (PsyNubProposal proposal : listComplete.stream().filter(PsyNubProposal::getSelected).collect(Collectors.toList())) {
            PsyNubProposal newProposal = NewPsyNubProposalHelper.createNewPsyNubProposalFromPsyNubProposal(proposal, _sessionController.getAccount());
            _psyNubFacade.save(newProposal);
            counter++;
        }
        DialogController.showInfoDialog("Verarbeitung beendet", "Es wurden erfolgreich " + counter + " NUB's übernommen");
    }

    private void printAllSelectedProposals(List<PsyNubProposal> listWorking) {
        // TODO Print selected proposals
    }

    private void sendAllSelectedProposals(List<PsyNubProposal> listProposals) {
        int counter = 0;
        _errorMessages = "";
        for (PsyNubProposal proposal : listProposals.stream().filter(PsyNubProposal::getSelected).collect(Collectors.toList())) {
            if (proposalIsReadyForSend(proposal)) {
                proposal.setStatus(WorkflowStatus.Accepted);
                proposal.setLastModifiedAt(new Date());
                proposal.setLastChangedByAccountId(_sessionController.getAccountId());
                proposal.setSealedAt(new Date());
                _psyNubFacade.save(proposal);
                counter++;
            }
        }

        _errorMessages += "\n\nEs wurden " + counter + " NUB's übernommen";

        DialogController.openDialogByName("errorMessageDialog");
    }

    private boolean proposalIsReadyForSend(PsyNubProposal proposal) {
        List<String> errors = PsyNubProposalChecker.checkPsyProposalForSend(proposal);
        if (errors.isEmpty()) {
            return true;
        } else {
            createErrorMessageString(errors);
            return false;
        }
    }

    private void createErrorMessageString(List<String> errors) {
        _errorMessages += String.join("\n", errors);
    }
}
