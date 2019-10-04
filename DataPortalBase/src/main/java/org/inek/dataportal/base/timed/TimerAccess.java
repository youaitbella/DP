package org.inek.dataportal.base.timed;

import org.inek.dataportal.common.data.AbstractDataAccess;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

public class TimerAccess extends AbstractDataAccess {

    public List<IdIkText> retrieveIdTexts(String sql) {
        Query query = getEntityManager().createNativeQuery(sql);
        List<IdIkText> result = new ArrayList<>();
        for (Object x : query.getResultList()) {
            Object[] info = (Object[]) x;
            int id = (int) info[0];
            String text = (String) info[1];
            result.add(new IdIkText(id, text));
        }
        return result;
    }
}
