package org.inek.dataportal.drg.specificfunction.backingbean;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.drg.specificfunction.facade.SpecificFunctionFacade;

@Named
@ViewScoped
public class SpecificFunctionSummary implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private SpecificFunctionFacade _specificFunctionFacade;

    private List<SpecificFunctionListItem> _inekSpecificFunctions;

    @PostConstruct
    public void init() {
        _inekSpecificFunctions = _specificFunctionFacade.getSpecificFunctionItemsForInek();
    }

    public List<SpecificFunctionListItem> getInekSpecificFunctions() {
        return _inekSpecificFunctions;
    }

}
