package org.inek.dataportal.controller;

import org.inek.dataportal.common.controller.SessionController;
import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.SearchController;
import org.inek.dataportal.facades.DrgFacade;
import org.inek.dataportal.facades.PeppFacade;
import org.inek.dataportal.facades.common.DiagnosisFacade;
import org.inek.dataportal.facades.common.ProcedureFacade;

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
    @Inject private DrgFacade _drgFacade;
    @Inject private SessionController _sessionController;
    private SearchController _searchController;

    public SearchController getSearchController() {
        if (_searchController == null) {
            _searchController = new SearchController(_sessionController, _procedureFacade, _diagnosisFacade, _peppFacade, _drgFacade);
        }
        return _searchController;
    }

    
}
