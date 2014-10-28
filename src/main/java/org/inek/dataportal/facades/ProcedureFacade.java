package org.inek.dataportal.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.inek.dataportal.entities.ProcedureInfo;

/**
 *
 * @author muellermi
 */
@Stateless
public class ProcedureFacade extends AbstractFacade<ProcedureInfo> {

    public ProcedureFacade() {
        super(ProcedureInfo.class);
    }

    public List<ProcedureInfo> findAll(String pattern) {
        return findAll(pattern, 0, 0);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<ProcedureInfo> findAll(String pattern, int firstYear, int lastYear) {
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
        if (firstYear > 2000) {
            where.append(" and p._lastYear >= ").append(firstYear);
        }
        if (lastYear > 2000) {
            where.append(" and p._firstYear <= ").append(lastYear);
        }
        // "where" is dynamically created. Since every whitespace and punctuation splits the search criteria, no SqlInjection can be introduced
        String query = "SELECT p FROM ProcedureInfo p WHERE " + where + " order by p._code, p._firstYear desc";
        return getEntityManager().createQuery(query, ProcedureInfo.class).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String findProcedure(String code, int firstYear, int lastYear) {
        StringBuilder where = new StringBuilder();
        where.append("(p._code = :code or p._codeShort = :code)");
        if (firstYear > 2000) {
            where.append(" and p._lastYear >= ").append(firstYear);
        }
        if (lastYear > 2000) {
            where.append(" and p._firstYear <= ").append(lastYear);
        }
        String query = "SELECT p FROM ProcedureInfo p WHERE " + where + " order by p._firstYear desc";
        List<ProcedureInfo> results = getEntityManager().createQuery(query, ProcedureInfo.class).setParameter("code", code).getResultList();
        return results.size() > 0 ? results.get(0).getCode() : "";
//        CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
//        CriteriaQuery<ProcedureInfo> cq = cb.createQuery(ProcedureInfo.class);
//        Root<ProcedureInfo> proc = cq.from(ProcedureInfo.class);
//        cq.select(proc).where(cb.equal(proc.get("code"), search));
//        TypedQuery<ProcedureInfo> q = em.createQuery(cq);
//        List<ProcedureInfo> results = q.getResultList();
//        return results.size() > 0 ? results.get(0).getCode() : "";
    }

}
