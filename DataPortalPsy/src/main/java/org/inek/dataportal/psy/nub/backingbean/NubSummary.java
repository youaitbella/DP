/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.nub.backingbean;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.ReportController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.psy.nub.bo.UploadedTemplate;
import org.inek.dataportal.psy.nub.entities.PsyNubRequest;
import org.inek.dataportal.psy.nub.facade.PsyNubFacade;
import org.inek.dataportal.psy.nub.helper.NewPsyNubRequestHelper;
import org.inek.dataportal.psy.nub.helper.PsyNubRequestChecker;
import org.inek.dataportal.psy.nub.helper.PsyNubRequestHelper;
import org.inek.dataportal.psy.nub.helper.PsyNubRequestTemplateHelper;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    @Inject
    private ReportController _reportController;
    @Inject
    private PsyNubRequestHelper _psyNubRequestHelper;

    private List<PsyNubRequest> _listComplete = new ArrayList<>();
    private List<PsyNubRequest> _listWorking = new ArrayList<>();
    private List<UploadedTemplate> _requestFromTemplateUploads = new ArrayList<>();

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

    public List<UploadedTemplate> getRequestsFromTemplateUploads() {
        return _requestFromTemplateUploads;
    }

    public List<PsyNubRequest> getListComplete() {
        return _listComplete;
    }

    public void setListComplete(List<PsyNubRequest> listComplete) {
        this._listComplete = listComplete;
    }

    public List<PsyNubRequest> getListWorking() {
        return _listWorking;
    }

    public void setListWorking(List<PsyNubRequest> listWorking) {
        this._listWorking = listWorking;
    }

    @PostConstruct
    public void init() {
        setWorkingList();
        setCompleteList();
    }

    private void setWorkingList() {
        List<WorkflowStatus> statusForWorkingList = new ArrayList<>();
        statusForWorkingList.add(WorkflowStatus.New);
        statusForWorkingList.add(WorkflowStatus.CorrectionRequested);
        setListWithStatus(statusForWorkingList, _listWorking);
    }

    private void setListWithStatus(List<WorkflowStatus> statusForWorkingList, List<PsyNubRequest> listWorking) {
        listWorking.clear();
        listWorking.addAll(_psyNubFacade.findAllByAccountIdAndNoIk(_sessionController.getAccountId(), statusForWorkingList));
        for (Integer ik : _sessionController.getAccount().getFullIkSet()) {
            if (_accessManager.ikIsManaged(ik, Feature.NUB_PSY)) {
                if (_accessManager.userHasReadAccess(Feature.NUB_PSY, ik)) {
                    listWorking.addAll(_psyNubFacade.findAllByIkAndStatus(ik, statusForWorkingList));
                }
            } else {
                listWorking.addAll(_psyNubFacade.findAllByAccountIdAndIkAndStatus(_sessionController.getAccountId(), ik, statusForWorkingList));
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
        setListWithStatus(statusForCompleteList, _listComplete);
    }

    public String psyNubEditOpen() {
        return Pages.NubPsyEdit.URL();
    }

    public boolean isCreateEntryAllowed() {
        return _configFacade.readConfigBool(ConfigKey.IsPsyNubCreateEnabled);
    }

    public void deletePsyNubRequest(PsyNubRequest request) {
        if (deleteAllowed(request)) {
            _psyNubFacade.delete(request);
            setWorkingList();
        } else {
            DialogController.showAccessDeniedDialog();
        }
    }

    public void retirePsyNubRequest(PsyNubRequest request) {
        if (deleteAllowed(request)) {
            request.setStatus(WorkflowStatus.Retired);
            request.setLastModifiedAt(new Date());
            request.setLastChangedByAccountId(_sessionController.getAccountId());
            _psyNubFacade.save(request);
            _psyNubRequestHelper.sendPsyNubConformationMail(request, _sessionController.getAccount());
            setCompleteList();
        } else {
            DialogController.showAccessDeniedDialog();
        }
    }

    private Boolean deleteAllowed(PsyNubRequest request) {
        // TODO prüfen ob Benutzer nub löschen darf
        return true;
    }

    public void handleTemplateUpload(FileUploadEvent file) {
        UploadedTemplate template;
        try {
            String content = new String(file.getFile().getContents(), "UTF-8");
            Optional<PsyNubRequest> request = PsyNubRequestTemplateHelper.createNewRequestFromTemplate(content,
                    _sessionController.getAccount());
            template = new UploadedTemplate(file.getFile().getFileName(), request, "");
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error during template import: " + ex.getMessage());
            template = new UploadedTemplate(file.getFile().getFileName(), Optional.empty(), ex.getMessage());
        }
        _requestFromTemplateUploads.add(template);
    }

    public void createNubsFromTemplates() {
        if (!_requestFromTemplateUploads.isEmpty()) {
            _requestFromTemplateUploads
                    .stream()
                    .filter(c -> !c.getHasError())
                    .map(UploadedTemplate::getRequest)
                    .forEach(request -> {
                        _psyNubFacade.save(request);
                    });
            _requestFromTemplateUploads.clear();
            setWorkingList();
        } else {
            DialogController.showInfoDialog("Keine Vorlagen ausgewählt", "Bitte laden Sie mindestens eine Vorlage hoch, " +
                    "um daraus eine neue NUB zu erzeugen. Ein Hochladen von mehreren Vorlagen gleichzeitig ist auch möglich.");
        }
    }

    public void executeBatchWorkingList() {
        if (_listWorking.stream().noneMatch(PsyNubRequest::getSelected)) {
            DialogController.showInfoDialog("Bitte eine NUB auswählen", "Bitte wählen Sie mindestens eine NUB aus");
            return;
        }

        switch (_selectedWorkingListCommand) {
            case "send":
                sendAllSelectedRequests(_listWorking);
                break;
            case "print":
                printAllSelectedRequests(_listWorking);
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
        if (_listComplete.stream().noneMatch(PsyNubRequest::getSelected)) {
            DialogController.showInfoDialog("Bitte eine NUB auswählen", "Bitte wählen Sie mindestens eine NUB aus");
            return;
        }

        DialogController.openDialogByName("dialogProcess");
        switch (_selectedCompleteListCommand) {
            case "createNew":
                createNewPsyRequestFromSelectedRequests(_listComplete);
                break;
            case "print":
                printAllSelectedRequests(_listComplete);
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

    private void createNewPsyRequestFromSelectedRequests(List<PsyNubRequest> requestList) {
        int counter = 0;
        for (PsyNubRequest request : requestList.stream().filter(PsyNubRequest::getSelected).collect(Collectors.toList())) {
            PsyNubRequest newRequest = NewPsyNubRequestHelper.createNewPsyNubRequestFromPsyNubRequest(request, _sessionController.getAccount());
            _psyNubFacade.save(newRequest);
            counter++;
        }
        DialogController.showInfoDialog("Verarbeitung beendet", "Es wurden erfolgreich " + counter + " NUB's übernommen");
    }

    private void printAllSelectedRequests(List<PsyNubRequest> requestList) {
        //TODO Alle ausgewählte drucken
        /*
        File zipFile = new File("NUB's.zip");

        try (FileOutputStream fileOut = new FileOutputStream(zipFile);
             CheckedOutputStream checkedOut = new CheckedOutputStream(fileOut, new Adler32());
             ZipOutputStream compressedOut = new ZipOutputStream(new BufferedOutputStream(checkedOut))) {
            for (PsyNubRequest psyNubRequest : requestList) {
                String fileName = "NUB_" + psyNubRequest.getNubIdExtern() + ".pdf";
                compressedOut.putNextEntry(new ZipEntry(fileName));
                ByteArrayInputStream ips = new ByteArrayInputStream(
                        _reportController.getSingleDocument("NUB_PSY.pdf", psyNubRequest.getId(), fileName));
                StreamHelper.copyStream(ips, compressedOut);
                compressedOut.closeEntry();
                compressedOut.flush();
            }
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage() + " " + ex.getStackTrace().toString());
        }
        try {
            InputStream is = new FileInputStream(zipFile);
            Utils.downLoadDocument(is, zipFile.getName(), 0);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage() + " " + ex.getStackTrace().toString());
        }
        */
    }

    private void sendAllSelectedRequests(List<PsyNubRequest> requestList) {
        int counter = 0;
        _errorMessages = "";
        for (PsyNubRequest request : requestList.stream().filter(PsyNubRequest::getSelected).collect(Collectors.toList())) {
            if (requestIsReadyForSend(request)) {
                request.setStatus(WorkflowStatus.Accepted);
                request.setLastModifiedAt(new Date());
                request.setLastChangedByAccountId(_sessionController.getAccountId());
                request.setSealedAt(new Date());
                _psyNubFacade.save(request);
                _psyNubRequestHelper.sendPsyNubConformationMail(request, _sessionController.getAccount());
                counter++;
            }
        }

        if (!_errorMessages.isEmpty()) {
            DialogController.showInfoDialog("Verarbeitung beendet", "Es konnten nicht alle Anträge gesendet werden. " +
                    "Bitte überprüfen Sie ob alle ausgewählten Anträge vollständig sind. Es wurden " + counter + " Anträge übernommen");
        } else {
            DialogController.showInfoDialog("Verarbeitung beendet", "Es wurden " + counter + " NUB's übernommen");
        }
    }

    private boolean requestIsReadyForSend(PsyNubRequest request) {
        List<String> errors = PsyNubRequestChecker.checkPsyRequestForSend(request);
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


    public StreamedContent printNubRequest(PsyNubRequest request) {
        String fileName = "NUB_" + request.getNubIdExtern() + ".pdf";
        ByteArrayInputStream stream = new ByteArrayInputStream(
                _reportController.getSingleDocument("NUB_PSY.pdf", request.getId(), fileName));
        return new DefaultStreamedContent(stream, "applikation/pdf", fileName);
    }
}




