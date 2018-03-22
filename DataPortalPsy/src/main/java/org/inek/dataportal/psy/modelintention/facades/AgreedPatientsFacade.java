package org.inek.dataportal.psy.modelintention.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.inek.dataportal.psy.modelintention.entities.AgreedPatients;
import org.inek.dataportal.common.data.AbstractFacade;

@Stateless
public class AgreedPatientsFacade extends AbstractFacade<AgreedPatients> {

    public AgreedPatientsFacade() {
        super(AgreedPatients.class);
    }

    
    public AgreedPatients findAgreedPatientsByModelIntentionId(int miId) {
        String query = "SELECT a FROM AgreedPatients a WHERE a._modelIntentionId = :miId";
        List<AgreedPatients> list = getEntityManager().createQuery(query, AgreedPatients.class).setParameter("miId", miId).getResultList();
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

}
