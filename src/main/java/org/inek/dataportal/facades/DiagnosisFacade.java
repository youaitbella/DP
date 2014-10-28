/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.inek.dataportal.entities.DiagnosisInfo;

/**
 *
 * @author muellermi
 */
@Stateless
public class DiagnosisFacade extends AbstractFacade<DiagnosisInfo> {

    public DiagnosisFacade() {
        super(DiagnosisInfo.class);
    }

    public List<DiagnosisInfo> findAll(String pattern) {
        return findAll(pattern, 0, 0);
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public List<DiagnosisInfo> findAll(String pattern, int firstYear, int lastYear) {
        String[] parts = pattern.toLowerCase().split("(\\s|,})");
        StringBuilder where = new StringBuilder();
        for (String part : parts) {
            if (part.trim().isEmpty()) {
                continue;
            }
            if (where.length() > 0) {
                where.append(" and");
            }
            where.append(" d._searchWords like '%").append(part).append("%'");
        }
        if (firstYear > 2000) {
            where.append(" and d._lastYear >= ").append(firstYear);
        }
        if (lastYear > 2000) {
            where.append(" and d._firstYear <= ").append(lastYear);
        }
        // "where" is dynamically created. Since every whitespace and punctuation splits the search criteria, no SqlInjection can be introduced
        String query = "SELECT d FROM DiagnosisInfo d WHERE " + where + " order by d._code, d._firstYear desc";
        return getEntityManager().createQuery(query, DiagnosisInfo.class).getResultList();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public String findDiagnosis(String code, int firstYear, int lastYear) {
        StringBuilder where = new StringBuilder();
        where.append("(d._code = :code or d._codeShort = :code)");
        if (firstYear > 2000) {
            where.append(" and d._lastYear >= ").append(firstYear);
        }
        if (lastYear > 2000) {
            where.append(" and d._firstYear <= ").append(lastYear);
        }
        String query = "SELECT d FROM DiagnosisInfo d WHERE " + where + " order by d._firstYear desc";
        List<DiagnosisInfo> results = getEntityManager().createQuery(query, DiagnosisInfo.class).setParameter("code", code).getResultList();
        return results.size() > 0 ? results.get(0).getCode() : "";
    }

}
