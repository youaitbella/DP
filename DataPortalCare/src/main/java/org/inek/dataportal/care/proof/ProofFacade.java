package org.inek.dataportal.care.proof;

import org.inek.dataportal.care.proof.entity.ProofDocument;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.care.proof.entity.ProofWard;
import org.inek.dataportal.care.proof.util.ProofHelper;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.DateUtils;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import static org.inek.dataportal.api.helper.PortalConstants.*;

@Stateless
public class ProofFacade extends AbstractDataAccessWithActionLog {
    private static final Logger LOGGER = Logger.getLogger("ProofFacade");


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
        List<IkYearQuarter> ikYearQuarters = new ArrayList<>();
        if (allowedIks.size() == 0) {
            return ikYearQuarters;
        }

        IkYearQuarter editableQuarter = ProofHelper.determineEditableYearQuarter();

        for (Integer ik : allowedIks) {
            ikYearQuarters.add(new IkYearQuarter(ik, editableQuarter.getYear(), editableQuarter.getQuarter()));
        }
        ikYearQuarters.removeAll(retrieveExistingInfo(allowedIks));
        return ikYearQuarters;
    }

    public List<Integer> retrievePossibleIkForAnnualReport(Set<Integer> allowedIks) {
        int year = DateUtils.currentYear() - 1;
        String jpql = "SELECT bi._ik FROM ProofRegulationBaseInformation bi " +
                "\r\n WHERE bi._ik in :ik and bi._year = :year" +
                "\r\n and (bi._quarter <= 4 and bi._statusId = 10) or (bi._quarter = 5 and bi._statusId <= 10) " +
                "\r\n group by bi._ik" +
                "\r\n having count(bi._quarter) = 4 and max(bi._quarter) = 4";
        TypedQuery<Integer> query = getEntityManager().createQuery(jpql, Integer.class);
        query.setParameter(IK, allowedIks);
        query.setParameter(YEAR, year);
        return query.getResultList();
    }

    public List<IkYearQuarter> retrieveExistingInfo(Set<Integer> allowedIks) {
        String jpql = "select new org.inek.dataportal.care.proof.IkYearQuarter(bi._ik, bi._year, bi._quarter) " +
                "from ProofRegulationBaseInformation bi " +
                "where bi._statusId <= 10 and bi._ik in :iks";
        TypedQuery<IkYearQuarter> query = getEntityManager().createQuery(jpql, IkYearQuarter.class);
        query.setParameter(IKS, allowedIks);
        return query.getResultList();
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

    public ProofWard retrieveOrCreateProofWard(int ik, int locationNumber, String locationP21, String name) {
        String jpql = "select p from ProofWard p where p.ik = :ik and p.locationNumber = :locationNumber " +
                "and p.locationP21 = :locationP21 and p.name = :name";
        TypedQuery<ProofWard> query = getEntityManager().createQuery(jpql, ProofWard.class);
        query.setParameter("ik", ik);
        query.setParameter("locationNumber", locationNumber);
        query.setParameter("locationP21", locationP21);
        query.setParameter("name", name);
        try {
            return query.getSingleResult();
        } catch (Exception ex) {
            ProofWard proofWard = new ProofWard(ik, locationNumber, locationP21, name);
            persist(proofWard);
            return proofWard;
        }
    }

    public Optional<ProofRegulationBaseInformation> retrieveCurrent(int ik) {
        IkYearQuarter editableQuarter = ProofHelper.determineEditableYearQuarter();

        String jpql = "select p from ProofRegulationBaseInformation p " +
                "where p._ik = :ik and p._year = :year and p._quarter = :quarter and p._statusId < 10";
        TypedQuery<ProofRegulationBaseInformation> query = getEntityManager().createQuery(jpql, ProofRegulationBaseInformation.class);
        query.setParameter("ik", ik);
        query.setParameter("year", editableQuarter.getYear());
        query.setParameter("quarter", editableQuarter.getQuarter());
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception e) {
            LOGGER.severe("Error fetching ProofBase for ik " + ik + "\r\n" + e.getMessage());
            return Optional.empty();
        }
    }

    public List<ProofRegulationBaseInformation> retrievePreviousYearInfos(int ik) {
        String jpql = "select p from ProofRegulationBaseInformation p " +
                "where p._ik = :ik and p._year = :year and p._statusId = 10";
        TypedQuery<ProofRegulationBaseInformation> query = getEntityManager().createQuery(jpql, ProofRegulationBaseInformation.class);
        query.setParameter("ik", ik);
        query.setParameter("year", DateUtils.currentYear() - 1);
        return query.getResultList();
    }
}
