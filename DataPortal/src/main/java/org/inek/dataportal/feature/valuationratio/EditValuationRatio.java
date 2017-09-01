/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.valuationratio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.Account;
import org.inek.dataportal.entities.account.AccountAdditionalIK;
import org.inek.dataportal.entities.icmt.Customer;
import org.inek.dataportal.entities.valuationratio.ValuationRatio;
import org.inek.dataportal.entities.valuationratio.ValuationRatioMedian;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.CustomerFacade;
import org.inek.dataportal.facades.ValuationRatioFacade;
import org.inek.dataportal.facades.account.AccountFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;
import org.inek.dataportal.utils.DateUtils;

/**
 *
 * @author muellermi
 */
@Named
@FeatureScoped(name = "ValuationRatio")
public class EditValuationRatio extends AbstractEditController {

    private static final Logger LOGGER = Logger.getLogger("EditValuationRatio");

    // <editor-fold defaultstate="collapsed" desc="override AbstractEditController">    
    @Override
    protected void addTopics() {
    }
    // </editor-fold>

    @Inject
    private ValuationRatioFacade _valuationRatioFacade;
    @Inject
    private SessionController _sessionController;
    @Inject
    private AccountFacade _accFacade;
    @Inject
    private CustomerFacade _customerFacade;

    @PostConstruct
    private void init() {
        Object id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
        if (id == null) {
            Utils.navigate(Pages.NotAllowed.RedirectURL());
        } else if (id.toString().equals("new")) {
            _valuationRatio = newValuationRatio();
        } else {
            int idInt = Integer.parseInt(id.toString());
            _valuationRatio = _valuationRatioFacade.findFreshValuationRatio(idInt);
            Account acc = _accFacade.find(_valuationRatio.getAccountId());
            initCustomerFields(_valuationRatio);
            if (DateUtils.isNullAlias(_valuationRatio.getValidFrom())) {
                _valuationRatio.setValidFrom(null);
            }
        }
    }

    private ValuationRatio newValuationRatio() {
        Account acc = _sessionController.getAccount();
        ValuationRatio vr = new ValuationRatio();
        vr.setAccountId(acc.getId());
        vr.setIk(getIks().get(0));
        vr.setValidFrom(null);
        vr.setContactGender(acc.getGender());
        vr.setContactFirstName(acc.getFirstName());
        vr.setContactLastName(acc.getLastName());
        vr.setContactPhone(acc.getPhone());
        vr.setContactEmail(acc.getEmail());
        initCustomerFields(vr);
        return vr;
    }

    public void ikChanged() {
        initCustomerFields(_valuationRatio);
    }
    
    private void initCustomerFields(ValuationRatio vr) {
        Customer cu = _customerFacade.getCustomerByIK(vr.getIk());
        vr.setHospital(cu.getName());
        vr.setCity(cu.getTown());
        vr.setStreet(cu.getStreet());
        vr.setZip(cu.getPostCode());
    }

    private ValuationRatio _valuationRatio;

    public ValuationRatio getValuationRatio() {
        return _valuationRatio;
    }

    public boolean isI68dBelowMedian() {
        ValuationRatioMedian vrm = _valuationRatioFacade.findMedianByDrgAndDataYear("I68D", _valuationRatio.getDataYear());
        if (vrm == null) {
            return false;
        }
        return _valuationRatio.getI68d() <= (vrm.getMedian() * vrm.getFactor());
    }

    public boolean isI68eBelowMedian() {
        ValuationRatioMedian vrm = _valuationRatioFacade.findMedianByDrgAndDataYear("I68E", _valuationRatio.getDataYear());
        if (vrm == null) {
            return false;
        }
        return _valuationRatio.getI68e() <= (vrm.getMedian() * vrm.getFactor());
    }

    public int getI68d() {
        return _valuationRatio.getI68d();
    }

    public void setI68d(int value) {
        if (!isI68dBelowMedian()) {
            _valuationRatio.setI68dList(false);
        }
        _valuationRatio.setI68d(value);
    }

    public int getI68e() {
        return _valuationRatio.getI68e();
    }

    public void setI68e(int value) {
        if (!isI68eBelowMedian()) {
            _valuationRatio.setI68eList(false);
        }
        _valuationRatio.setI68e(value);
    }

    public boolean getProvideEnabled() {
        return true;
    }

    public boolean getReadOnly() {
        if (_valuationRatio.getStatus() >= WorkflowStatus.Provided.getId()) {
            return true;
        }
        return false;
    }

    public List<Integer> getIks() {
        List<Integer> iks = new ArrayList<>();
        if (!_valuationRatioFacade.existsValuationRatio(
                _sessionController.getAccount().getIK())) {
            iks.add(_sessionController.getAccount().getIK());
        }

        for (AccountAdditionalIK ik : _sessionController
                .getAccount().getAdditionalIKs()) {
            if (!_valuationRatioFacade
                    .existsValuationRatio(ik.getIK())) {
                iks.add(ik.getIK());
            }

        }
        return iks;
    }
    
    public boolean getcheckIfEdit() {
        if(_valuationRatio.getId() == -1)
            return true;
        return false;
    }

    public String save() {
        try {
            if (_valuationRatio.getValidFrom() == null) {
                Date test = DateUtils.getNullAlias();
                _valuationRatio.setValidFrom(test);
            }

            _valuationRatio = _valuationRatioFacade.saveValuationRatio(_valuationRatio);
            _sessionController.alertClient(Utils.getMessage("msgSave"));
        } catch (EJBException e) {
            _sessionController.alertClient(Utils.getMessage("msgSaveError"));
        }
        return "";
    }

    /**
     * provide the message to InEK
     *
     * @return
     */
    public String provide() {
        _valuationRatio.setStatus(WorkflowStatus.Provided.getId());
        try {
            _valuationRatioFacade.merge(_valuationRatio);
            _valuationRatioFacade.clearCache();
            _sessionController.alertClient("Bewertungsrelation wurde erfolgreich eingereicht.");
            return Pages.InsuranceSummary.RedirectURL();
        } catch (EJBException ex) {
            _sessionController.alertClient(Utils.getMessage("msgSaveError"));
            return "";
        }
    }
}
