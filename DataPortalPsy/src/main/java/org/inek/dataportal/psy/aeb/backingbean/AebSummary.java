/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.aeb.backingbean;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.psy.aeb.entity.AEBBaseInformation;
import org.inek.dataportal.psy.aeb.facade.AEBFacade;

/**
 *
 * @author lautenti
 */
@Named
@FeatureScoped
public class AebSummary {

    @Inject
    private AEBFacade _aebfacade;

    private List<AEBBaseInformation> _listAEBComplete;
    private List<AEBBaseInformation> _listAEBWorking;

    public List<AEBBaseInformation> getListAEBComplete() {
        return _listAEBComplete;
    }

    public void setListAEBComplete(List<AEBBaseInformation> listAEBComplete) {
        this._listAEBComplete = listAEBComplete;
    }

    public List<AEBBaseInformation> getListAEBWorking() {
        return _listAEBWorking;
    }

    public void setListAEBWorking(List<AEBBaseInformation> listAEBWorking) {
        this._listAEBWorking = listAEBWorking;
    }

    @PostConstruct
    public void init() {
        _listAEBComplete = new ArrayList<>();
        _listAEBWorking = new ArrayList<>();
        _listAEBComplete.addAll(getAebsByStatus(WorkflowStatus.Provided));
        _listAEBWorking.addAll(getAebsByStatus(WorkflowStatus.New));
    }

    private List<AEBBaseInformation> getAebsByStatus(WorkflowStatus status) {
        return _aebfacade.getAllByStatus(status);
    }

    public String newAeb() {
        return Pages.AebEdit.URL();
    }
}
