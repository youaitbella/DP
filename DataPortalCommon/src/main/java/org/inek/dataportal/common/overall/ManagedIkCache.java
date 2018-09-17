package org.inek.dataportal.common.overall;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.ikadmin.entity.IkCorrelation;
import org.inek.dataportal.common.data.ikadmin.facade.IkAdminFacade;

@Named
@ApplicationScoped
public class ManagedIkCache {

    private IkAdminFacade _ikAdminFacade;
    private List<Integer> _managedIks = new ArrayList<>();
    private List<IkCorrelation> _ikCorrelations = new ArrayList<>();

    public ManagedIkCache() {
    }

    @Inject
    public ManagedIkCache(IkAdminFacade ikAdminFacade) {
        _ikAdminFacade = ikAdminFacade;
    }

    @PostConstruct
    private void load() {
        _managedIks = _ikAdminFacade.loadAllManagegIks();
        _ikCorrelations = _ikAdminFacade.loadAllCorrelations();
    }

    public List<Integer> retrieveManagedIks() {
        return _managedIks;
    }

    public boolean contains(Integer ik) {
        return _managedIks.contains(ik);
    }

    /**
     * This method retrieves the iks where a user is responsible for, limited by feature and allowed iks
     * Even though this class is called xxxCache, this method always performs db access.
     * @param feature
     * @param account
     * @param iks
     * @return 
     */
    Set<Integer> retriveResponsibleForIks(Feature feature, Account account, Set<Integer> iks) {
        return _ikAdminFacade
                .loadAccountResponsibilities(feature, account, iks)
                .stream()
                .map(r -> r.getDataIk())
                .collect(Collectors.toSet());
    }

    Set<Integer> retriveCorrelatedIks(Feature feature, Set<Integer> userIks, Set<Integer> requestedIks) {
        Set<Integer> correlatedIks = _ikCorrelations
                .stream()
                .filter(c -> c.getFeature() == feature)
                .filter(c -> userIks.contains(c.getUserIk()) && requestedIks.contains(c.getDataIk()))
                .map(c -> c.getDataIk())
                .collect(Collectors.toSet());
        return correlatedIks;
    }

}
