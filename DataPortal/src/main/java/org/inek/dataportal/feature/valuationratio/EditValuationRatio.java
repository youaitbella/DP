/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.valuationratio;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJBException;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.account.AccountAdditionalIK;
import org.inek.dataportal.entities.valuationratio.ValuationRatio;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.ValuationRatioFacade;
import org.inek.dataportal.feature.AbstractEditController;
import org.inek.dataportal.helper.Utils;
import org.inek.dataportal.helper.scope.FeatureScoped;

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

    @Inject private ValuationRatioFacade _valuationRatioFacade;
    @Inject private SessionController _sessionController;

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
        }
    }
    
    private ValuationRatio newValuationRatio() {
        ValuationRatio vr = new ValuationRatio();
        vr.setIk(getIks().get(0));
        return vr;
    }

    public void ikChanged() {
        
    }

    private ValuationRatio _valuationRatio;

    public ValuationRatio getValuationRatio() {
        return _valuationRatio;
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
        iks.add(_sessionController.getAccount().getIK());
        for(AccountAdditionalIK ik : _sessionController.getAccount().getAdditionalIKs()) {
            iks.add(ik.getIK());
        }
        return iks;
    }

    public String save() {
        try {
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
