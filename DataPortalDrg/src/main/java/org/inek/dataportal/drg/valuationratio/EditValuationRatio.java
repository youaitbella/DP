/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.drg.valuationratio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.ValidationException;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.drg.valuationratio.entities.ValuationRatio;
import org.inek.dataportal.drg.valuationratio.entities.ValuationRatioDrgCount;
import org.inek.dataportal.drg.valuationratio.entities.ValuationRatioMedian;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.drg.valuationratio.facades.ValuationRatioFacade;
import org.inek.dataportal.common.controller.AbstractEditController;
import org.inek.dataportal.common.data.adm.MailTemplate;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.mail.Mailer;
import org.inek.dataportal.common.overall.AccessManager;

/**
 *
 * @author muellermi
 */
@Named
@ViewScoped
public class EditValuationRatio extends AbstractEditController {

    private static final Logger LOGGER = Logger.getLogger("EditValuationRatio");
    private static final String MSG_CHECK_INPUT = "Bitte korrigieren Sie erst Ihre Eingabe.";

    // <editor-fold defaultstate="collapsed" desc="override AbstractEditController">    
    @Override
    protected void addTopics() {
    }
    // </editor-fold>

    @Inject
    private ValuationRatioFacade _valuationRatioFacade;
    @Inject
    private SessionController _sessionController;
    @Inject private ApplicationTools _appTools;
    @Inject private AccessManager _accessManager;

    @PostConstruct
    private void init() {
        Object id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
        } else if (id.toString().equals("new")) {
            _valuationRatio = newValuationRatio();
            ikChanged();
        } else {
            int idInt = Integer.parseInt(id.toString());
            _valuationRatio = _valuationRatioFacade.findFreshValuationRatio(idInt);
        }
    }

    private ValuationRatio newValuationRatio() {
        Account acc = _sessionController.getAccount();
        ValuationRatio vr = new ValuationRatio();
        vr.setAccountId(acc.getId());
        List<SelectItem> iks = getIks();
        if (iks.size() == 1) {
            vr.setIk((int) iks.get(0).getValue());
        }
        vr.setValidFrom(null);
        vr.setContactGender(acc.getGender());
        vr.setContactFirstName(acc.getFirstName());
        vr.setContactLastName(acc.getLastName());
        vr.setContactPhone(acc.getPhone());
        vr.setContactEmail(acc.getEmail());
        return vr;
    }

    public void ikChanged() {
        int ik = _valuationRatio.getIk();
        int year = _valuationRatio.getDataYear();
        ValuationRatioDrgCount count = _valuationRatioFacade.findValuationRatioDrgCount(ik, year, "I68D");
        if (count != null) {
            _valuationRatio.setI68d(count.getCount());
        }
        count = _valuationRatioFacade.findValuationRatioDrgCount(ik, year, "I68E");
        if (count != null) {
            _valuationRatio.setI68e(count.getCount());
        }
    }

    private boolean _validationErrorI68d;

    public void validateI68D(FacesContext context, UIComponent component, Object value) {
        try {
            validateRange("I68D", value);
            _validationErrorI68d = false;
        } catch (ValidatorException ex) {
            _validationErrorI68d = true;
            throw ex;
        }
    }

    private boolean _validationErrorI68e;

    public void validateI68E(FacesContext context, UIComponent component, Object value) {
        try {
            validateRange("I68E", value);
            _validationErrorI68e = false;
        } catch (ValidatorException ex) {
            _validationErrorI68e = true;
            throw ex;
        }
    }

    private void validateRange(String drg, Object value) {
        if (value == null) {
            return;
        }
        int val = (int) value;
        if (!checkRange(drg, val)) {
            throw new ValidatorException(
                    new FacesMessage(
                            drg + ": Die Fallzahl weicht zu stark von den gemäß §21 KHEntG übermittelten Daten ab. "
                            + "Eine Korrektur ist lediglich im Rahmen +/- 5% möglich. "
                            + "Bei stärkeren Abweichungen ist eine Klärung mit dem InEK erforderlich (E-Mail). "
                            + "Bitte beachten Sie: "
                            + "Gemäß Vereinbarung ist die Fallzahl der Datenlieferung gemäß §21 KHEntG entscheidend, "
                            + "auch wenn die Zahl der abgerechneten Fälle davon abweichen kann."));
        }
    }

    private boolean checkRange(String drg, int val) {
        ValuationRatioDrgCount caseCount = _valuationRatioFacade.
                findValuationRatioDrgCount(_valuationRatio.getIk(), _valuationRatio.getDataYear(),
                        drg);
        int minCount = (int) (caseCount.getCount() * 0.95);
        int maxCount = (int) (caseCount.getCount() * 1.05);
        return val <= maxCount && val >= minCount;
    }

    private ValuationRatio _valuationRatio;

    public ValuationRatio getValuationRatio() {
        return _valuationRatio;
    }

    public ValuationRatioMedian obtainMedian(String drg) {
        return _valuationRatioFacade.findMedianByDrgAndDataYear(drg, _valuationRatio.getDataYear());
    }

    public boolean isDrgBelowMedian(String drg, Object value) {
        if (value == null) {
            return false;
        }
        ValuationRatioMedian median = obtainMedian(drg);
        int maxCases = (int) (median.getFactor() * median.getMedian());
        int drgValue = (int) value;
        return drgValue <= maxCases;
    }

    public int getI68d() {
        return _valuationRatio.getI68d();
    }

    public void setI68d(int value) {
        _valuationRatio.setI68d(value);
        if (!isDrgBelowMedian("I68D", value)) {
            _valuationRatio.setI68dList(false);
        }
    }

    public int getI68e() {
        return _valuationRatio.getI68e();
    }

    public void setI68e(int value) {
        _valuationRatio.setI68e(value);
        if (!isDrgBelowMedian("I68E", value)) {
            _valuationRatio.setI68eList(false);
        }
    }

    public boolean getProvideEnabled() {
        return true;
    }

    public boolean getReadOnly() {
        if (_valuationRatio.getStatusId() >= WorkflowStatus.Provided.getId()) {
            return true;
        }
        return false;
    }

    public boolean isSealEnabled() {
        return _appTools.isEnabled(ConfigKey.IsValuationRatioSendEnabled);
    }

    public List<SelectItem> getIks() {
        int dataYear = _valuationRatio == null ? Calendar.getInstance().get(Calendar.YEAR) - 1 : _valuationRatio.
                getDataYear();

        List<SelectItem> items = new ArrayList<>();
        for (int ik : _accessManager.ObtainIksForCreation(Feature.VALUATION_RATIO)) {
            if (!_valuationRatioFacade.existsValuationRatio(ik, dataYear)) {
                items.add(new SelectItem(ik));
            }
        }
        return items;
    }

    public boolean getCheckIfEdit() {
        return _valuationRatio.getId() == -1;
    }

    public String save() {
        if (hasError()) {
            _sessionController.alertClient(MSG_CHECK_INPUT);
            return "";
        }
        try {
            _valuationRatio = _valuationRatioFacade.saveValuationRatio(_valuationRatio);
            _sessionController.alertClient(Utils.getMessage("msgSaveAndMentionSend"));
        } catch (EJBException e) {
            _sessionController.alertClient(Utils.getMessage("msgSaveError"));
        }
        return "";
    }

    public boolean hasError() {
        return _validationErrorI68d 
                || _validationErrorI68e 
                || !checkRange("I68D", _valuationRatio.getI68d()) 
                || !checkRange("I68E", _valuationRatio.getI68e());
    }

    /**
     * provide the message to InEK
     *
     * @return
     */
    public String provide() {
        if (hasError()) {
            _sessionController.alertClient(MSG_CHECK_INPUT);
            return "";
        }
        _valuationRatio.setStatus(WorkflowStatus.Provided);
        try {
            _valuationRatioFacade.merge(_valuationRatio);
            _valuationRatioFacade.clearCache();
            Mailer mailer = _sessionController.getMailer();
            MailTemplate template = mailer.getMailTemplate("Teilnahme - Gezielte Absenkung");
            String body = template.getBody()
                    .replace("{formalSalutation}", getFormalSalutation())
                    .replace("{year}", "" + _valuationRatio.getDataYear())
                    .replace("{ik}", "" + _valuationRatio.getIk())
                    .replace("{drgs}", buildDrgString());
            template.setBody(body);
            String subject = template.getSubject()
                    .replace("{year}", "" + _valuationRatio.getDataYear())
                    .replace("{ik}", "" + _valuationRatio.getIk())
                    .replace("{drgs}", buildDrgString());
            template.setSubject(subject);
            mailer.sendMailTemplate(template, _sessionController.getAccount().getEmail());
            _sessionController.alertClient("Gezielte Absenkung wurde erfolgreich eingereicht.");
            return Pages.ValuationRatioSummary.RedirectURL();
        } catch (EJBException ex) {
            _sessionController.alertClient(Utils.getMessage("msgSaveError"));
            return "";
        }
    }

    public String getFormalSalutation() {
        String salutation = _valuationRatio.getContactGender() == 1
                ? Utils.getMessage("formalSalutationFemale")
                : Utils.getMessage("formalSalutationMale");
        salutation = salutation
                .replace("{title}", _valuationRatio.getContactTitle())
                .replace("{lastname}", _valuationRatio.getContactLastName())
                .replace("  ", " ");
        return salutation;
    }

    private String buildDrgString() {
        String drgs = "";
        if (_valuationRatio.getI68dList()) {
            drgs += "I68D";
        }
        if (_valuationRatio.getI68eList()) {
            if (_valuationRatio.getI68dList()) {
                drgs += ", I68E";
            } else {
                drgs += "I68E";
            }
        }
        if (drgs.length() == 0) {
            drgs = "<keine>";
        }
        return drgs;
    }

    public enum DrgRangeState {
        Valid,
        Below,
        Above
    }
}
