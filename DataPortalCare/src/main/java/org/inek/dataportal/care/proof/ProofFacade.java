package org.inek.dataportal.care.proof;

import org.inek.dataportal.care.entities.Extension;
import org.inek.dataportal.care.entities.ProofDocument;
import org.inek.dataportal.care.entities.ProofRegulationBaseInformation;
import org.inek.dataportal.care.entities.ProofRegulationStation;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.DateUtils;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.inek.dataportal.api.helper.PortalConstants.*;

@Stateless
public class ProofFacade extends AbstractDataAccessWithActionLog {


    public ProofRegulationBaseInformation findBaseInformation(int id) {
        String jpql = "SELECT bi FROM ProofRegulationBaseInformation bi WHERE bi._id = :id";
        TypedQuery<ProofRegulationBaseInformation> query = getEntityManager().createQuery(jpql, ProofRegulationBaseInformation.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<ProofRegulationBaseInformation> getAllByStatusAndIk(WorkflowStatus status, int ik) {
        String jpql = "SELECT bi FROM ProofRegulationBaseInformation bi WHERE bi._statusId = :status "
                + "and bi._ik = :ik";
        TypedQuery<ProofRegulationBaseInformation> query = getEntityManager().createQuery(jpql, ProofRegulationBaseInformation.class);
        query.setParameter(STATUS, status.getId());
        query.setParameter(IK, ik);
        return query.getResultList();
    }

    public List<IkYearQuarter> retrievePossibleIkYearQuarters(Set<Integer> allowedIks) {
        List<IkYearQuarter> all = new ArrayList<>();
        for (Integer ik : allowedIks) {
            for (int year : getPossibleYears()) {
                int maxQuarter = year == DateUtils.currentYear() ? (DateUtils.currentMonth() + 2) / 3 : 4;
                IntStream.rangeClosed(1, maxQuarter).forEach(q -> {
                    all.add(new IkYearQuarter(ik, year, q));
                });
            }
        }
        all.removeAll(retrieveExistingInfo(allowedIks));
        return all;
    }

    public List<IkYearQuarter> retrieveExistingInfo(Set<Integer> allowedIks) {
        String jpql = "select new org.inek.dataportal.care.proof.IkYearQuarter(bi._ik, bi._year, bi._quarter) " +
                "from ProofRegulationBaseInformation bi " +
                "where bi._statusId <= 10 and bi._ik in :iks";
        TypedQuery<IkYearQuarter> query = getEntityManager().createQuery(jpql, IkYearQuarter.class);
        query.setParameter(IKS, allowedIks);
        return query.getResultList();
    }

    public Set<Integer> retrievePossibleIks(Set<Integer> allowedIks) {
        Set<Integer> possibleIks = new HashSet<>();
        Set<Integer> possibleYears = getPossibleYears();

        for (int ik : allowedIks) {
            if (newEntryPossibleForIk(ik, possibleYears)) {
                possibleIks.add(ik);
            }
        }

        return possibleIks;
    }

    public Set<Integer> retrievePossibleYears(int ik) {
        Set<Integer> possibleYearsForCreation = new HashSet<>();
        Set<Integer> possibleYears = getPossibleYears();

        for (int year : possibleYears) {
            if (newEntryPossibleForYear(ik, year)) {
                possibleYearsForCreation.add(year);
            }
        }
        return possibleYearsForCreation;
    }

    public Set<Integer> retrievePossibleQuarter(int ik, int year) {
        // either select all 4 quarters
        Set<Integer> possibleQuarters = IntStream.rangeClosed(1, 4).collect(HashSet::new, HashSet::add, HashSet::addAll);
        // or determine the one and only
//        Set<Integer> possibleQuarters = new HashSet<>();
//        int quarter = (DateUtils.currentMonth() + 1) / 3;
//        possibleQuarters.add(quarter == 0 ? 4 : quarter);

        Set<Integer> existingQuarters = getQuartersForValidEntrys(ik, year);
        possibleQuarters.removeAll(existingQuarters);

        return possibleQuarters;
    }

    private boolean newEntryPossibleForYear(int ik, int year) {
        Set<Integer> quartersForValidEntrys = getQuartersForValidEntrys(ik, year);
        return quartersForValidEntrys.size() < 4;
    }


    private boolean newEntryPossibleForIk(int ik, Set<Integer> possibleYears) {
        for (int year : possibleYears) {
            Set<Integer> quartersForValidEntrys = getQuartersForValidEntrys(ik, year);

            if (quartersForValidEntrys.size() < 4) {
                return true;
            }
        }
        return false;
    }

    private Set<Integer> getQuartersForValidEntrys(int ik, int year) {
        String sql = "select prbiQuarter \n" +
                "from care.ProofRegulationBaseInformation \n" +
                "where prbiIk = " + ik + " \n" +
                "and prbiYear = " + year + " \n" +
                "and prbiQuarter in (1,2,3,4) \n" +
                "and prbiStatusId in (0, 3, 10)";

        Query query = getEntityManager().createNativeQuery(sql);

        @SuppressWarnings("unchecked")
        List<Object[]> objects = query.getResultList();

        Set<Integer> quarters = new HashSet<>();

        for (Object quarter : objects) {
            quarters.add((int) quarter);
        }

        return quarters;
    }

    public boolean hasAllQuartersSend(int ik, int year) {
        String sql = "select 1 \n" +
                "from care.ProofRegulationBaseInformation \n" +
                "where prbiIk = " + ik + " \n" +
                "and prbiYear = " + year + " \n" +
                "and prbiStatusId = 10";
        Query query = getEntityManager().createNativeQuery(sql);
        return query.getResultList().size() == 4;
    }

    private Set<Integer> getPossibleYears() {
        int year = DateUtils.currentYear();
        return IntStream.rangeClosed(year - 1, year).collect(HashSet::new, HashSet::add, HashSet::addAll);
    }

    public ProofRegulationBaseInformation save(ProofRegulationBaseInformation deptBaseInformation) {
        if (deptBaseInformation.getId() == -1) {
            persist(deptBaseInformation);
            return deptBaseInformation;
        }
        return merge(deptBaseInformation);
    }

    public void deleteBaseInformation(ProofRegulationBaseInformation info) {
        remove(info);
    }

    public List<ProofRegulationBaseInformation> getAllByStatus(WorkflowStatus status) {
        String jpql = "SELECT bi FROM ProofRegulationBaseInformation bi WHERE bi._statusId = :status ";
        TypedQuery<ProofRegulationBaseInformation> query = getEntityManager().createQuery(jpql, ProofRegulationBaseInformation.class);
        query.setParameter(STATUS, status.getId());
        return query.getResultList();
    }

    public List<ProofRegulationStation> getStationsForProof(int ik, int year) {
        String jpql = "SELECT ds " +
                "FROM ProofRegulationStation ds " +
                "WHERE ds._ik = :ik " +
                "AND ds._year = :year";
        TypedQuery<ProofRegulationStation> query = getEntityManager().createQuery(jpql, ProofRegulationStation.class);
        query.setParameter(IK, ik);
        query.setParameter(YEAR, year);
        return query.getResultList();
    }

    public List<SelectItem> getExceptionsFactsForYear(int year) {
        String sql = "select lefId, lefTitle\n" +
                "from care.listExceptionFact\n" +
                "where lefValidFrom <= " + year + "\n" +
                "and lefValidTo >= " + year;

        Query query = getEntityManager().createNativeQuery(sql);

        @SuppressWarnings("unchecked")
        List<Object[]> objects = query.getResultList();

        List<SelectItem> items = new ArrayList<>();

        for (Object[] obj : objects) {
            SelectItem item = new SelectItem();
            item.setValue((int) obj[0]);
            item.setLabel((String) obj[1]);
            items.add(item);
        }

        return items;
    }

    public boolean hasExtension(int ik, int year, int quarter) {
        String name = "Extension.findByCoordinates";
        TypedQuery<Extension> query = getEntityManager().createNamedQuery(name, Extension.class);
        query.setParameter(IK, ik);
        query.setParameter(YEAR, year);
        query.setParameter("quarter", quarter);
        try {
            query.getSingleResult();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public void saveExtension(Extension extension) {
        persist(extension);
    }

    public void saveProofDocument(ProofDocument document) {
        if (document.getId() <= 0) {
            persist(document);
        } else {
            merge(document);
        }
    }

    public String findProofDocumentNameByIkAndYear(int ik, int year) {
        String name = "ProofDocument.NameByIkAndYear";
        TypedQuery<String> query = getEntityManager().createNamedQuery(name, String.class);
        query.setParameter(IK, ik);
        query.setParameter(YEAR, year);
        try {
            return query.getSingleResult();
        } catch (Exception ex) {
            return "";
        }
    }

    public ProofDocument findProofDocumentByIkAndYear(int ik, int year) {
        String name = "ProofDocument.DocumentByIkAndYear";
        TypedQuery<ProofDocument> query = getEntityManager().createNamedQuery(name, ProofDocument.class);
        query.setParameter(IK, ik);
        query.setParameter(YEAR, year);
        return query.getSingleResult();
    }
}
