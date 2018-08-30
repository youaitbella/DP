package org.inek.dataportal.common.overall;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.data.ikadmin.facade.IkAdminFacade;

@ApplicationScoped
@Named
public class ManagedIkCache {

    private final IkAdminFacade _ikAdminFacade;

    private List<Integer> _managedIks = new ArrayList<>();

    @Inject
    public ManagedIkCache(IkAdminFacade ikAdminFacade) {
        _ikAdminFacade = ikAdminFacade;
    }

    @PostConstruct
    private void init(){
        _managedIks = _ikAdminFacade.loadAllManagegIks();
    }
    public List<Integer> retrieveManagedIks() {
        return _managedIks;
    }

}
