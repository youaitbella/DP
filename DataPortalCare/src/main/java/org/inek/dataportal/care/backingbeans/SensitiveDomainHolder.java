package org.inek.dataportal.care.backingbeans;

import org.inek.dataportal.care.entities.SensitiveDomain;
import org.inek.dataportal.common.data.AbstractDataAccess;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.persistence.TypedQuery;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class SensitiveDomainHolder extends AbstractDataAccess {

    private Map<Integer, SensitiveDomain> sensitiveDomains = new ConcurrentHashMap<>();

    private void init(@Observes @Initialized(ApplicationScoped.class) Object dummy) {
        String jpql = "select d from SensitiveDomain d where d.id between 1 and 100"; // todo: use flag once the structure is updated
        TypedQuery<SensitiveDomain> query = getEntityManager().createQuery(jpql, SensitiveDomain.class);
        for (SensitiveDomain domain : query.getResultList()) {
            sensitiveDomains.put(domain.getId(), domain);
        }
    }


    public String fromId(int id) {
        return sensitiveDomains.get(id).getName();
    }

    public int toId(String text) {
        return sensitiveDomains.values()
                .stream().filter(d -> d.getName().equals(text))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("")).getId();
    }
}
