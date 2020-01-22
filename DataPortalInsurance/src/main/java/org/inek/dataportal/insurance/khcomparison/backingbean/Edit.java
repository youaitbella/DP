package org.inek.dataportal.insurance.khcomparison.backingbean;

import org.eclipse.persistence.exceptions.OptimisticLockException;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.DialogController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.KhComparison.checker.AebChecker;
import org.inek.dataportal.common.data.KhComparison.checker.AebComparer;
import org.inek.dataportal.common.data.KhComparison.checker.RenumerationChecker;
import org.inek.dataportal.common.data.KhComparison.entities.*;
import org.inek.dataportal.common.data.KhComparison.facade.AEBFacade;
import org.inek.dataportal.common.data.KhComparison.facade.AEBListItemFacade;
import org.inek.dataportal.common.data.KhComparison.helper.AebCheckerHelper;
import org.inek.dataportal.common.data.KhComparison.helper.AebCleanerHelper;
import org.inek.dataportal.common.data.KhComparison.helper.AebUploadHelper;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.enums.CustomerTyp;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.MailTemplateHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.AccessManager;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Named
@FeatureScoped
public class Edit {

    @Inject
    private SessionController _sessionController;
    @Inject
    private AEBFacade _aebFacade;
    @Inject
    private AEBListItemFacade _aebListItemFacade;
    @Inject
    private AccountFacade _accountFacade;
    @Inject
    private Mailer _mailer;
    @Inject
    private AccessManager _accessManager;

    private AEBBaseInformation _aebBaseInformation;
    private List<Integer> _validDatayears = new ArrayList<>();
    private String _errorMessage = "";

    @PostConstruct
    public void init() {
        String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
            return;
        } else if ("new".equals(id)) {
            _aebBaseInformation = createNewAebBaseInformation();
            _aebBaseInformation.setCreatedFrom(_sessionController.getAccountId());
        } else {
            _aebBaseInformation = _aebFacade.findAEBBaseInformation(Integer.parseInt(id));
        }
        ikChanged();
    }

    public String getErrorMessage() {
        return _errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this._errorMessage = errorMessage;
    }

    public boolean isWriteable() {
        return _accessManager.isWritable(Feature.HC_INSURANCE, _aebBaseInformation.getStatus(), 0, _aebBaseInformation.getIk());
    }

    public boolean isReadOnly() {
        return !isWriteable();
    }

    public boolean isChangeable() {
        return _aebBaseInformation.getStatus() == WorkflowStatus.Provided
                && _accessManager.userHasWriteAccess(Feature.HC_INSURANCE, _aebBaseInformation.getIk());
    }

    public boolean isSendEnabled() {
        return _accessManager.isSealedEnabled(Feature.HC_INSURANCE, _aebBaseInformation.getStatus(), 0, _aebBaseInformation.getIk());
    }

    public List<Integer> getValidDatayears() {
        return _validDatayears;
    }

    public void setValidDatayears(List<Integer> validDatayears) {
        this._validDatayears = validDatayears;
    }

    public AEBBaseInformation getAebBaseInformation() {
        return _aebBaseInformation;
    }

    public void setAebBaseInformation(AEBBaseInformation aebBaseInformation) {
        this._aebBaseInformation = aebBaseInformation;
    }

    public List<SelectItem> getStructureCategories() {
        return _aebListItemFacade.getStructureCategorie();
    }

    private AEBBaseInformation createNewAebBaseInformation() {
        AEBBaseInformation info = new AEBBaseInformation();
        info.setStatus(WorkflowStatus.New);
        info.setTyp(CustomerTyp.Insurance.id());
        for (OccupationalCategory cat : _aebFacade.getOccupationalCategories()) {
            PersonalAgreed agreed = new PersonalAgreed();
            agreed.setOccupationalCategory(cat);
            info.addPersonalAgreed(agreed);
        }

        info.setAebPageB1(new AEBPageB1());
        info.getAebPageB1().setBaseInformation(info);
        if (getAllowedIks().size() == 1) {
            int ik = getAllowedIks().stream().findFirst().get();
            info.setIk(ik);
        }
        return info;
    }

    public void saveAeb() {
        save(false);
    }

    private boolean save(boolean sendCheck) {
        _errorMessage = "";
        if (!baseInfoHasIkAndYear(_aebBaseInformation)) {
            DialogController.showWarningDialog("Fehler beim Speichern",
                    "Bitte geben Sie mindestens eine gültige IK und Datenjahr an");
            return false;
        }

        if (baseInfoIsCorrect(_aebBaseInformation, sendCheck)) {
            removeEmptyEntries(_aebBaseInformation);
            AebCheckerHelper.ensureValuationRadios(_aebBaseInformation, _aebListItemFacade);
            _aebBaseInformation.setLastChangeFrom(_sessionController.getAccountId());
            _aebBaseInformation.setLastChanged(new Date());
            try {
                _aebBaseInformation = _aebFacade.save(_aebBaseInformation);
                if (_aebBaseInformation.getStatus() == WorkflowStatus.Provided) {
                    DialogController.showSendDialog();
                    sendSendMail(_aebBaseInformation);
                } else {
                    DialogController.showSaveDialog();
                }
            } catch (OptimisticLockException ex) {
                DialogController.showErrorDialog("Daten können nicht gespeichert werden.", "Die Daten wurden bereits von einem " +
                        "anderen Benutzer geändert. Bitte laden Sie die Seite neu.");
                return false;
            } catch (Exception ex) {
                DialogController.showErrorDialog("Fehler beim Speichern", "Vorgang abgebrochen");
                _mailer.sendError("AEB Fehler beim speichern", ex);
                return false;
            }
            return true;
        } else {
            DialogController.showWarningDialog("Fehler beim Speichern",
                    "Bitte überprüfen Sie das Fehlerprotokoll");
            return false;
        }
    }

    public String send() {
        WorkflowStatus oldState = _aebBaseInformation.getStatus();
        Date oldSend = _aebBaseInformation.getSend();
        _aebBaseInformation.setStatus(WorkflowStatus.Provided);
        _aebBaseInformation.setSend(new Date());
        if (save(true)) {
            if (aebContainsDifferences()) {
                DialogController.showWarningDialog("Unterschiede in der AEB festgestellt",
                        "Es wurden Unterschiede in bereits abgegeben Information für das IK "
                                + _aebBaseInformation.getIk() + " festgestellt");
            }
            return Pages.InsuranceKhComparisonSummary.URL();
        } else {
            _aebBaseInformation.setStatus(oldState);
            _aebBaseInformation.setSend(oldSend);
            return "";
        }
    }

    private boolean baseInfoIsCorrect(AEBBaseInformation info, boolean sendCheck) {
        AebChecker checker = new AebChecker(_aebListItemFacade, false, sendCheck);
        if (!checker.checkAeb(info)) {
            _errorMessage = checker.getMessage();
            return false;
        }
        return true;
    }

    private boolean aebContainsDifferences() {
        AebComparer comparer = new AebComparer();
        AEBBaseInformation info = _aebFacade.findAEBBaseInformation(_aebBaseInformation.getIk(),
                _aebBaseInformation.getYear(), 0, WorkflowStatus.Provided);
        if (info != null) {
            if (!comparer.compareEuqality(_aebBaseInformation, info)) {
                _aebFacade.storeCollision(_aebBaseInformation.getId(), info.getId());
                setErrorMessage(comparer.getResult());
                sendContainsDifferencesMail(_aebBaseInformation, info, comparer.getResult());
                return true;
            }
        }
        return false;
    }

    public void addNewPageE1_1() {
        _aebBaseInformation.addAebPageE1_1();
    }

    public void removePageE1_1(AEBPageE1_1 page) {
        _aebBaseInformation.removeAebPageE1_1(page);
    }

    public void addNewPageE1_2() {
        _aebBaseInformation.addAebPageE1_2();
    }

    public void removePageE1_2(AEBPageE1_2 page) {
        _aebBaseInformation.removeAebPageE1_2(page);
    }

    public void addNewPageE2() {
        _aebBaseInformation.addAebPageE2();
    }

    public void removePageE2(AEBPageE2 page) {
        _aebBaseInformation.removeAebPageE2(page);
    }

    public void addNewPageE3_1() {
        _aebBaseInformation.addAebPageE3_1();
    }

    public void removePageE3_1(AEBPageE3_1 page) {
        _aebBaseInformation.removeAebPageE3_1(page);
    }

    public void addNewPageE3_2() {
        _aebBaseInformation.addAebPageE3_2();
    }

    public void removePageE3_2(AEBPageE3_2 page) {
        _aebBaseInformation.removeAebPageE3_2(page);
    }

    public void addNewPageE3_3() {
        _aebBaseInformation.addAebPageE3_3();
    }

    public void removePageE3_3(AEBPageE3_3 page) {
        _aebBaseInformation.removeAebPageE3_3(page);
    }

    public void removeRegionStructurParticularities(RegionStructurParticularities region) {
        _aebBaseInformation.removeRegionStructurParticularities(region);
    }

    public void addNewRegionStructurParticularities() {
        _aebBaseInformation.addNewRegionStructurParticularities();
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            setErrorMessage(AebUploadHelper.handleAebUpload(_aebBaseInformation, event.getFile().getInputstream(), _aebListItemFacade));
        } catch (Exception ex) {
            DialogController.showWarningDialog("Upload fehlgeschlagen", "Fehler beim Upload. Bitte versuchen Sie es erneut");
        }
    }

    private void removeEmptyEntries(AEBBaseInformation baseInformation) {
        AebCleanerHelper.removeEmptyEntries(baseInformation);
    }

    public void peppChanged(AEBPageE1_1 page) {
        if (page.getPepp().length() == 5) {
            page.setValuationRadioDay(_aebListItemFacade.getValuationRadioDaysByPepp(page.getPepp(),
                    page.getCompensationClass(), _aebBaseInformation.getYear()));
            page.setIsOverlyer(false);
        } else if (isPseudoPepp(page.getPepp())) {
            page.setCompensationClass(1);
            page.setCaseCount(1);
            page.setCalculationDays(1);
            page.setIsOverlyer("PUEL".equals(page.getPepp()));
        } else {
            page.setValuationRadioDay(0.0);
        }
    }

    public void zeChanged(AEBPageE2 page) {
        if (page.getZe().length() == 7) {
            page.setValuationRadioDay(_aebListItemFacade.getValuationRadioDaysByZe(page.getZe(),
                    _aebBaseInformation.getYear()));
        } else {
            page.setValuationRadioDay(0.0);
        }
    }

    public void etChanged(AEBPageE1_2 page) {
        if (page.getEt().length() == 7) {
            page.setValuationRadioDay(_aebListItemFacade.getValuationRadioDaysByEt(page.getEt(),
                    _aebBaseInformation.getYear()));
        } else {
            page.setValuationRadioDay(0.0);
        }
    }

    public void handleDocumentUpload(FileUploadEvent file) {
        PsyDocument doc = new PsyDocument();
        doc.setName(file.getFile().getFileName());
        doc.setContent(file.getFile().getContents());
        _aebBaseInformation.addPsyDocument(doc);
    }

    public void removeDocument(PsyDocument doc) {
        _aebBaseInformation.removePsyDocument(doc);
    }

    public StreamedContent downloadDocument(PsyDocument doc) {
        ByteArrayInputStream stream = new ByteArrayInputStream(doc.getContent());
        return new DefaultStreamedContent(stream, "application/" + doc.getContentTyp(), doc.getName());
    }

    public void change() {
        archiveAndCopyAEB(_aebBaseInformation);
        _aebBaseInformation.setStatus(WorkflowStatus.CorrectionRequested);
        _aebBaseInformation = _aebFacade.save(_aebBaseInformation);
    }

    private AEBBaseInformation archiveAndCopyAEB(AEBBaseInformation info) {
        info.setStatus(WorkflowStatus.Retired);
        info.setLastChanged(new Date());
        info.setLastChangeFrom(_sessionController.getAccountId());
        _aebFacade.save(info);
        return new AEBBaseInformation(info);
    }

    public void ikChanged() {
        List<Integer> usedYears = _aebFacade.getUsedDataYears(_aebBaseInformation.getIk(), CustomerTyp.Insurance);
        List<Integer> possibleYears = _aebFacade.getPossibleDataYears();
        possibleYears.removeAll(usedYears);
        setValidDatayears(possibleYears);
    }

    public Set<Integer> getAllowedIks() {
        Set<Integer> allowedIks = _accessManager.obtainIksForCreation(Feature.HC_INSURANCE);
        Set<Integer> iks = _aebFacade.retrievePossibleIks(allowedIks, CustomerTyp.Insurance);
        return iks;

    }

    private boolean baseInfoHasIkAndYear(AEBBaseInformation info) {
        return info.getIk() != 0 && info.getYear() != 0;
    }

    private void sendSendMail(AEBBaseInformation info) {
        MailTemplate template = _mailer.getMailTemplate("KH-Vergleich Senden bestätigung");

        MailTemplateHelper.setPlaceholderInTemplate(template, "{ik}", String.valueOf(info.getIk()));
        MailTemplateHelper.setPlaceholderInTemplate(template, "{year}", String.valueOf(info.getYear()));

        String date = new SimpleDateFormat("dd.MM.yyyy").format(info.getLastChanged());
        String time = new SimpleDateFormat("HH:mm").format(info.getLastChanged());

        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{date}", date);
        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{time}", time);

        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{salutation}",
                _mailer.getFormalSalutation(_sessionController.getAccount()));

        _mailer.sendMailTemplate(template, _sessionController.getAccount().getEmail());
    }

    private void sendContainsDifferencesMail(AEBBaseInformation info1, AEBBaseInformation info2, String result) {
        MailTemplate template = _mailer.getMailTemplate("KH-Vergleich Unterschiede");

        MailTemplateHelper.setPlaceholderInTemplate(template, "{ik}", String.valueOf(info1.getIk()));
        MailTemplateHelper.setPlaceholderInTemplate(template, "{year}", String.valueOf(info1.getYear()));

        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{results}", result);

        Account ac1 = _accountFacade.findAccount(info1.getLastChangeFrom());
        Account ac2 = _accountFacade.findAccount(info2.getLastChangeFrom());

        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{salutation1}", _mailer.getSalutation(ac1));
        MailTemplateHelper.setPlaceholderInTemplateBody(template, "{salutation2}", _mailer.getSalutation(ac2));

        _mailer.sendMailTemplate(template, ac1.getEmail() + ";" + ac2.getEmail());
    }

    public boolean isPseudoPepp(String pepp) {
        return RenumerationChecker.isPseudoPepp(pepp);
    }

}
