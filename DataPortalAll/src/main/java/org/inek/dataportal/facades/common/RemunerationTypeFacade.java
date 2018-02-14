package org.inek.dataportal.facades.common;

import java.util.Optional;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.common.RemunerationType;
import org.inek.dataportal.common.data.AbstractDataAccess;

@Stateless
public class RemunerationTypeFacade extends AbstractDataAccess {

    public Optional<RemunerationType> findByCode(String charId){
        String jql = "select r from RemunerationType r where r._charId = :charId";
        TypedQuery<RemunerationType> query = getEntityManager().createQuery(jql, RemunerationType.class);
        query.setParameter("charId", charId);
        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception ex) {
            return Optional.empty();
        }
       
    }
    
}
