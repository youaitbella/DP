/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades.common;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.inek.dataportal.entities.common.DiagnosisInfo;
import org.inek.dataportal.common.data.AbstractFacade;

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

    
    public String findDiagnosis(String code, int firstYear, int lastYear) {
        String jql = "SELECT d FROM DiagnosisInfo d "
                + "WHERE (d._code = :code or d._codeShort = :code) and d._lastYear >= :firstYear and d._firstYear <= :lastYear "
                + "order by d._firstYear desc";
        TypedQuery<DiagnosisInfo> query = getEntityManager().createQuery(jql, DiagnosisInfo.class);
        query.setParameter("code", code);
        query.setParameter("firstYear", firstYear);
        query.setParameter("lastYear", lastYear);
        List<DiagnosisInfo> results = query.getResultList();
        return results.size() > 0 ? results.get(0).getCode() : "";
    }

    public String checkDiagnoses(String value, int firstYear, int lastYear) {
        return checkDiagnoses(value, firstYear, lastYear, "\\s");
    }
    
    public String checkDiagnoses(String value, int firstYear, int lastYear, String splitRegex) {
        String[] codes = value.split(splitRegex);
        StringBuilder invalidCodes = new StringBuilder();
        for (String code : codes) {
            if (code.isEmpty()){continue;}
            if (findDiagnosis(code, firstYear, lastYear).equals("")) {
                invalidCodes.append(invalidCodes.length() > 0 ? ", " : "").append(code);
            }
        }
        if (invalidCodes.length() > 0) {
            if (invalidCodes.indexOf(",") > 0) {
                invalidCodes.insert(0, "Unbekannte Codes: ");
            } else {
                invalidCodes.insert(0, "Unbekannter Code: ");
            }
        }
        return invalidCodes.toString();
    }
    
}
