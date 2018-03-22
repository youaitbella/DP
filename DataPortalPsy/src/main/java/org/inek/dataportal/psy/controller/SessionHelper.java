package org.inek.dataportal.psy.controller;

import org.inek.dataportal.common.controller.SessionController;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.psy.peppproposal.facades.PeppFacade;
import org.inek.dataportal.common.data.access.DiagnosisFacade;
import org.inek.dataportal.common.data.access.ProcedureFacade;

/**
 *
 * @author muellermi
 */
@Named
@SessionScoped
public class SessionHelper implements Serializable{
    
    @Inject private ProcedureFacade _procedureFacade;
    @Inject private DiagnosisFacade _diagnosisFacade;
    @Inject private PeppFacade _peppFacade;
    @Inject private SessionController _sessionController;
    private SearchController _searchController;

    public SearchController getSearchController() {
        if (_searchController == null) {
            _searchController = new SearchController(_sessionController, _procedureFacade, _diagnosisFacade, _peppFacade);
        }
        return _searchController;
    }

    
}
