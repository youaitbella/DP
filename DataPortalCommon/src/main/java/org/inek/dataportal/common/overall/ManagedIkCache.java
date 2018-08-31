package org.inek.dataportal.common.overall;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.common.data.ikadmin.facade.IkAdminFacade;

@Named
@ApplicationScoped
public class ManagedIkCache {

    private IkAdminFacade _ikAdminFacade;

    private List<Integer> _managedIks = new ArrayList<>();

    public ManagedIkCache(){}
    
    @Inject
    public ManagedIkCache(IkAdminFacade ikAdminFacade) {
        _ikAdminFacade = ikAdminFacade;
    }

    @PostConstruct
    private void load(){
        _managedIks = _ikAdminFacade.loadAllManagegIks();
    }
    
    public List<Integer> retrieveManagedIks() {
        return _managedIks;
    }

}
