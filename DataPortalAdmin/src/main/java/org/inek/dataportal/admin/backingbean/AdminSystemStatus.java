/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.admin.backingbean;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.admin.system.SessionCounter;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.facade.AccountFacade;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.scope.FeatureScoped;

/**
 *
 * @author lanzrama
 */
@Named
@FeatureScoped
public class AdminSystemStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private AccountFacade _accountFacade;
    @Inject
    private SessionController _sessionController;

    public int getSessionCount() {
        return SessionCounter.getCount();
    }

    public void clearCache() {
        _accountFacade.clearCache();
        Utils.showMessageInBrowser("Cache cleared.");
    }

    public String getProcessorInfo() {
        Runtime runtime = Runtime.getRuntime();
        return "Processor cores " + runtime.availableProcessors();
    }

    public String getMemoryInfo() {
        int MB = 1024 * 1024;
        Runtime runtime = Runtime.getRuntime();
        return "Speicher: max " + runtime.maxMemory() / MB + ", total " + runtime.totalMemory() / MB + "; free " + runtime.
                freeMemory() / MB;
    }

}
