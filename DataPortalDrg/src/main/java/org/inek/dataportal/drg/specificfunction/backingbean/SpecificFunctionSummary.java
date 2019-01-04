/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.drg.specificfunction.backingbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.drg.specificfunction.facade.SpecificFunctionFacade;

@Named
@ViewScoped
public class SpecificFunctionSummary implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private SpecificFunctionFacade _specificFunctionFacade;
    @Inject
    private ApplicationTools _applicationTools;

    private List<SpecificFunctionListItem> _inekSpecificFunctions = new ArrayList<>();

    public List<SpecificFunctionListItem> getInekSpecificFunctions() {
        return _inekSpecificFunctions;
    }

    @PostConstruct
    public void init() {
        _inekSpecificFunctions = _specificFunctionFacade.getSpecificFunctionItemsForInek();

        for (SpecificFunctionListItem item : _inekSpecificFunctions) {
            item.setHospitalName(_applicationTools.retrieveHospitalName(item.getIk()));
            item.setTown(_applicationTools.retrieveHospitalTown(item.getIk()));
        }
    }

}
