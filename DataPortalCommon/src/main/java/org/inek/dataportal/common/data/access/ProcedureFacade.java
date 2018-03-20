package org.inek.dataportal.common.data.access;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.inek.dataportal.common.data.common.ProcedureInfo;
import org.inek.dataportal.common.data.AbstractFacade;

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

    
    public String findProcedure(String code, int firstYear, int lastYear) {
        String jql = "SELECT p FROM ProcedureInfo p "
                + "WHERE (p._code = :code or p._codeShort = :code) and p._lastYear >= :firstYear and p._firstYear <= :lastYear "
                + "order by p._firstYear desc";
        TypedQuery<ProcedureInfo> query = getEntityManager().createQuery(jql, ProcedureInfo.class);
        query.setParameter("code", code);
        query.setParameter("firstYear", firstYear);
        query.setParameter("lastYear", lastYear);
        List<ProcedureInfo> results = query.getResultList();
        return results.size() > 0 ? results.get(0).getCode() : "";
    }

    public String checkProcedures(String value, int firstYear, int lastYear) {
        return checkProcedures(value, firstYear, lastYear, "\\s");
    }
    
    public String checkProcedures(String value, int firstYear, int lastYear, String splitRegex) {
        String[] codes = value.split(splitRegex);
        StringBuilder invalidCodes = new StringBuilder();
        for (String code : codes) {
            String searchCode = code.replace("*", "");
            if (searchCode.endsWith(".")){
                searchCode = searchCode.substring(0, searchCode.length()-1);
            }
            if (searchCode.isEmpty()){continue;}
            if (findProcedure(searchCode, firstYear, lastYear).equals("")) {
                invalidCodes.append(invalidCodes.length() > 0 ? ", " : "").append(searchCode);
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
