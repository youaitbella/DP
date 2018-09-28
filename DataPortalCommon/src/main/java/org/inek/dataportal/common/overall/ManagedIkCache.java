package org.inek.dataportal.common.overall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import javax.ejb.Asynchronous;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.ikadmin.entity.IkCorrelation;
import org.inek.dataportal.common.data.ikadmin.facade.IkAdminFacade;

@Singleton
@Startup
public class ManagedIkCache {

    private IkAdminFacade _ikAdminFacade;

    private Map<Integer, Set<Feature>> _managedIks = new HashMap<>();
    private List<IkCorrelation> _ikCorrelations = new ArrayList<>();
    private final ReentrantLock _writeLock = new ReentrantLock();

    public ManagedIkCache() {
    }

    @Inject
    public ManagedIkCache(IkAdminFacade ikAdminFacade) {
        _ikAdminFacade = ikAdminFacade;
    }

    @Schedule(hour = "*", minute = "*/30", info = "every 30 minutes")
    private void timedReset() {
        reset();
    }

    @Asynchronous
    public void reset() {
        if (_writeLock.isLocked()) {
            return;
        }
        _writeLock.lock();
        try {
            loadManagedIks();
            loadIkCorrelations();
        } finally {
            _writeLock.unlock();
        }
    }

    private void loadIkCorrelations() {
        _writeLock.lock();
        try {
            if (!_ikCorrelations.isEmpty()) {
                return;
            }
            _ikCorrelations = _ikAdminFacade.loadAllCorrelations();
        } finally {
            _writeLock.unlock();
        }
    }

    private void loadManagedIks() {
        _writeLock.lock();
        try {
            if (!_managedIks.isEmpty()) {
                return;
            }
            _managedIks = _ikAdminFacade.loadAllManagedIkWithFeatures();
        } finally {
            _writeLock.unlock();
        }
    }

    /**
     * Indicates that an ik feature combination has an admin. Due to performance reasons, this is NOT locked. Instead we
     * check for the writeLock after the read, whether the lock is set then and retry. In very rare (almost unexpected)
     * cases this might return a tolerated false negative.
     *
     * @param ik
     * @param feature
     *
     * @return
     */
    public boolean isManaged(Integer ik, Feature feature) {
        if (_managedIks.isEmpty()) {
            loadManagedIks();
        }
        boolean isManaged;
        do {
            isManaged = _managedIks.containsKey(ik) && _managedIks.get(ik).contains(feature);
        } while (_writeLock.isLocked());
        return isManaged;
    }

    Set<Integer> retriveCorrelatedIks(Feature feature, Set<Integer> userIks, Set<Integer> requestedIks) {
        if (_ikCorrelations.isEmpty()) {
            loadIkCorrelations();
        }
        Set<Integer> correlatedIks;
        do {
            correlatedIks = _ikCorrelations
                    .stream()
                    .filter(c -> c.getFeature() == feature)
                    .filter(c -> userIks.contains(c.getUserIk()) && requestedIks.contains(c.getDataIk()))
                    .map(c -> c.getDataIk())
                    .collect(Collectors.toSet());
        } while (_writeLock.isLocked());
        return correlatedIks;
    }

    /**
     * This method retrieves the iks where a user is responsible for, limited by feature and allowed iks Even though
     * this class is called xxxCache, this method always performs db access.
     *
     * @param feature
     * @param account
     * @param iks
     *
     * @return
     */
    Set<Integer> retriveResponsibleForIks(Feature feature, Account account, Set<Integer> iks) {
        return _ikAdminFacade
                .loadAccountResponsibilities(feature, account, iks)
                .stream()
                .map(r -> r.getDataIk())
                .collect(Collectors.toSet());
    }

}
