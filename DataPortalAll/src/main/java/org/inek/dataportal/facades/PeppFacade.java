/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.inek.dataportal.entities.pepp.PeppInfo;

/**
 *
 * @author muellermi
 */
@Stateless
public class PeppFacade extends AbstractFacade<PeppInfo> {

    public PeppFacade() {
        super(PeppInfo.class);
    }

    public List<PeppInfo> findAll(String pattern) {
        return findAll(pattern, 0, 0);
    }

    
    public List<PeppInfo> findAll(String pattern, int firstYear, int lastYear) {
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
        String query = "SELECT p FROM PeppInfo p WHERE " + where + " order by p._code, p._year desc";
        return getEntityManager().createQuery(query, PeppInfo.class).getResultList();
    }

    
    public String findPepp(String code, int firstYear, int lastYear) {
        StringBuilder where = new StringBuilder();
        where.append("(p._code = :code)");
        if (firstYear > 2000) {
            where.append(" and p._year >= ").append(firstYear);
        }
        if (lastYear > 2000) {
            where.append(" and p._year <= ").append(lastYear);
        }
        String query = "SELECT p FROM PeppInfo p WHERE " + where + " order by p._year desc";
        List<PeppInfo> results = getEntityManager().createQuery(query, PeppInfo.class).setParameter("code", code).getResultList();
        return results.size() > 0 ? results.get(0).getCode() : "";

    }

}
