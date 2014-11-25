package org.inek.dataportal.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.inek.dataportal.entities.drg.DrgInfo;


/**
 *
 * @author muellermi
 */
@Stateless
public class DrgFacade extends AbstractFacade<DrgInfo> {

    public DrgFacade() {
        super(DrgInfo.class);
    }

    public List<DrgInfo> findAll(String pattern) {
        return findAll(pattern, 0, 0);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<DrgInfo> findAll(String pattern, int firstYear, int lastYear) {
        String[] parts = pattern.toLowerCase().split("(\\s|,})");
        StringBuilder where = new StringBuilder();
        for (String part : parts) {
            if (part.trim().isEmpty()) {
                continue;
            }
            if (where.length() > 0) {
                where.append(" and");
            }
            where.append(" p._searchWords like '%").append(part).append("%'");
        }
        if (firstYear > 2000 && lastYear > 2000) {
            where.append(" and p._year >= ").append(firstYear).append(" and p._year <= ").append(lastYear);
        }
        // "where" is dynamically created. Since every whitespace and punctuation splits the search criteria, no SqlInjection can be introduced
        String query = "SELECT p FROM DrgInfo p WHERE " + where + " order by p._code, p._year desc";
        return getEntityManager().createQuery(query, DrgInfo.class).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String findDrg(String code, int firstYear, int lastYear) {
        StringBuilder where = new StringBuilder();
        where.append("(p._code = :code)");
        if (firstYear > 2000) {
            where.append(" and p._year >= ").append(firstYear);
        }
        if (lastYear > 2000) {
            where.append(" and p._year <= ").append(lastYear);
        }
        String query = "SELECT p FROM DrgInfo p WHERE " + where + " order by p._year desc";
        List<DrgInfo> results = getEntityManager().createQuery(query, DrgInfo.class).setParameter("code", code).getResultList();
        return results.size() > 0 ? results.get(0).getCode() : "";

    }

}
