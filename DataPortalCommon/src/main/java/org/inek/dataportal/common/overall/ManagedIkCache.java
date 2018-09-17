package org.inek.dataportal.common.overall;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
    private Map<Integer, Set<Integer>> _correlatedIks = new ConcurrentHashMap<>();

    public ManagedIkCache(){}
    
    @Inject
    public ManagedIkCache(IkAdminFacade ikAdminFacade) {
        _ikAdminFacade = ikAdminFacade;
    }

    @PostConstruct
    private void load(){
        _managedIks = _ikAdminFacade.loadAllManagegIks();
        _correlatedIks = _ikAdminFacade.loadAllCorrelatedIks();
    }
    
    public List<Integer> retrieveManagedIks() {
        return _managedIks;
    }

    public boolean contains(Integer ik) {
        return _managedIks.contains(ik);
    }

    Set<Integer> retriveResponsibleForIks(Set<Integer> iks) {
        Set<Integer> correlatedIs = new HashSet<>();
        for (Integer ik : iks) {
            correlatedIs.addAll(_correlatedIks.get(ik));
        }
        return correlatedIs;
    }

}
