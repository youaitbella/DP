package org.inek.dataportal.care.proof;

import org.inek.dataportal.care.entities.Extension;
import org.inek.dataportal.care.proof.entity.ProofDocument;
import org.inek.dataportal.care.proof.entity.ProofRegulationBaseInformation;
import org.inek.dataportal.care.proof.entity.ProofWard;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.DateUtils;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
        List<IkYearQuarter> ikYearQuarters = new ArrayList<>();
        if (allowedIks.size() == 0) {
            return ikYearQuarters;
        }

        int year = DateUtils.currentYear() - (DateUtils.currentMonth() == 1 ? 1 : 0);
        int quarter = DateUtils.currentMonth() == 1 ? 4 : (DateUtils.currentMonth() + 1) / 3;

        for (Integer ik : allowedIks) {
            ikYearQuarters.add(new IkYearQuarter(ik, year, quarter));
        }
        ikYearQuarters.removeAll(retrieveExistingInfo(allowedIks));
        return ikYearQuarters;
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
            ProofWard proofWard = new ProofWard(ik, locationNumber, name);
            persist(proofWard);
            return proofWard;
        }
    }
}
