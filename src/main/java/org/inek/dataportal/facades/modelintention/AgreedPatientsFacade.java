package org.inek.dataportal.facades.modelintention;

import java.util.List;
import javax.ejb.Stateless;
import org.inek.dataportal.entities.modelintention.AgreedPatients;
import org.inek.dataportal.facades.AbstractFacade;

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
