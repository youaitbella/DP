/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.KhComparison.facade;

import javafx.util.Pair;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.KhComparison.entities.*;
import org.inek.dataportal.common.data.KhComparison.enums.PsyGroup;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.CustomerTyp;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.XmlReaderPsyEvaluation;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author lautenti
 */
@Stateless
public class AEBFacade extends AbstractDataAccess {

    public AEBBaseInformation findAEBBaseInformation(int id) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._id = :id";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public AEBBaseInformation findAEBBaseInformation(int ik, int year, int typ, WorkflowStatus status) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._year = :year "
                + "and bi._ik = :ik "
                + "and bi._typ = :typ "
                + "and bi._status = :status";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("ik", ik);
        query.setParameter("year", year);
        query.setParameter("typ", typ);
        query.setParameter("status", status);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public StructureBaseInformation findStructureBaseInformation(int id) {
        String sql = "SELECT si FROM StructureBaseInformation si WHERE si._id = :id";
        TypedQuery<StructureBaseInformation> query = getEntityManager().createQuery(sql, StructureBaseInformation.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    public List<AEBBaseInformation> getAllByStatus(WorkflowStatus status) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._status = :status";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    public List<StructureBaseInformation> getAllStructureBaseInformations() {
        String sql = "SELECT bi FROM StructureBaseInformation bi ";
        TypedQuery<StructureBaseInformation> query = getEntityManager().createQuery(sql, StructureBaseInformation.class);
        return query.getResultList();
    }

    public List<AEBBaseInformation> getAllByStatusAndIk(WorkflowStatus status, int ik, CustomerTyp typ) {
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._status = :status "
                + "and bi._ik = :ik "
                + "and bi._typ = :typ";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("status", status);
        query.setParameter("ik", ik);
        query.setParameter("typ", typ.id());
        return query.getResultList();
    }

    public List<AEBBaseInformation> getAllByStatusAndIk(List<WorkflowStatus> status, Set<Integer> iks, CustomerTyp typ) {
        if (iks.size() == 0 || status.size() == 0) {
            return new ArrayList<>();
        }
        String sql = "SELECT bi FROM AEBBaseInformation bi WHERE bi._status in :status "
                + "and bi._ik in :iks "
                + "and bi._typ = :typ";
        TypedQuery<AEBBaseInformation> query = getEntityManager().createQuery(sql, AEBBaseInformation.class);
        query.setParameter("status", status);
        query.setParameter("iks", iks);
        query.setParameter("typ", typ.id());
        return query.getResultList();
    }

    public boolean structureBaseInformaionAvailable(int ik) {
        String sql = "SELECT si FROM StructureBaseInformation si WHERE si._ik = :ik";
        TypedQuery<StructureBaseInformation> query = getEntityManager().createQuery(sql, StructureBaseInformation.class);
        query.setParameter("ik", ik);
        return !query.getResultList().isEmpty();
    }

    public Optional<AEBBaseInformation> getBaseInformationForComparing(int id) {
        String sql = "select top 1 *\n" +
                "from psy.AEBBaseInformation a\n" +
                "where a.biDataYear = (\n" +
                "\tselect biDataYear\n" +
                "\tfrom psy.AEBBaseInformation\n" +
                "\twhere biid = " + id + "\n" +
                ")\n" +
                "and a.biIk = (\n" +
                "\tselect biIk\n" +
                "\tfrom psy.AEBBaseInformation\n" +
                "\twhere biid = " + id + "\n" +
                ")\n" +
                "and a.biTyp != (\n" +
                "\tselect biTyp\n" +
                "\tfrom psy.AEBBaseInformation\n" +
                "\twhere biid = " + id + "\n" +
                ") \n" +
                "and a.biStatusId in (10, 200)\n" +
                "order by a.biStatusId, a.biSend desc";
        Query query = getEntityManager().createNativeQuery(sql, AEBBaseInformation.class);

        AEBBaseInformation result = (AEBBaseInformation) query.getSingleResult();
        return Optional.ofNullable(result);
    }

    public boolean ikHasModelIntention(int ik) {
        String sql = "select ehComment\n" +
                "--select *\n" +
                "from psy.ExpectedHospital\n" +
                "where ehIk = " + ik + "\n" +
                "and (ehComment like '%MV%'\n" +
                "or ehComment like '%Modellvorhaben%')";
        Query query = getEntityManager().createNativeQuery(sql);
        return query.getResultList().size() > 0;
    }

    public StructureBaseInformation getStructureBaseInformationByIk(int ik) {
        String sql = "SELECT si FROM StructureBaseInformation si WHERE si._ik = :ik";
        TypedQuery<StructureBaseInformation> query = getEntityManager().createQuery(sql, StructureBaseInformation.class);
        query.setParameter("ik", ik);
        return query.getSingleResult();
    }

    private Set<Pair<Integer, Integer>> retrieveIkYearPairs(Collection<Integer> iks, CustomerTyp typ) {
        if (iks.isEmpty()) {
            return new HashSet<>();
        }
        String ikList = iks.stream().map(ik -> "" + ik).collect(Collectors.joining(", "));
        String sql = "select distinct biIk, biDataYear \n"
                + "from psy.AEBBaseInformation \n"
                + "where biIk in (" + ikList + ") \n"
                + "    and biTyp = " + typ.id();
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Object[]> objects = query.getResultList();

        return objects.stream().map(obj -> new Pair<>((int) obj[0], (int) obj[1])).collect(Collectors.toSet());
    }

    public Set<Integer> retrievePossibleIks(Set<Integer> allowedIks, CustomerTyp typ) {
        Set<Pair<Integer, Integer>> existingIkYearPairs = retrieveIkYearPairs(allowedIks, typ);
        List<Integer> possibleYears = getPossibleDataYears();

        Set<Integer> possibleIks = allowedIks
                .stream()
                .filter(ik -> possibleYears
                        .stream()
                        .anyMatch(year -> !existingIkYearPairs.contains(new Pair<>(ik, year))))
                .collect(Collectors.toSet());
        return possibleIks;
    }

    public List<Integer> getPossibleDataYears() {
        List<Integer> years = new ArrayList<>();
        IntStream.rangeClosed(2018, Year.now().getValue() + 1).
                forEach((int y) -> years.add(y));
        return years;
    }

    public List<Integer> getUsedDataYears(int ik, CustomerTyp typ) {
        String sql = "select distinct biDataYear from psy.AEBBaseInformation\n"
                + "where biIk = " + ik + "\n"
                + "and biTyp = " + typ.id();
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> result = query.getResultList();
        return result;
    }

    public List<Integer> getUsedDataYears(int ik) {
        String sql = "select distinct biDataYear from psy.AEBBaseInformation\n"
                + "where biIk = " + ik + "\n"
                + "and biStatusId in (10, 3, 200)";
        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> result = query.getResultList();
        return result;
    }

    @Transactional
    public AEBBaseInformation save(AEBBaseInformation aebBaseInformation) {
        if (aebBaseInformation.getId() == -1) {
            persist(aebBaseInformation);
            return aebBaseInformation;
        }
        return merge(aebBaseInformation);
    }

    @Transactional
    public StructureBaseInformation save(StructureBaseInformation info) {
        if (info.getId() == 0) {
            persist(info);
            return info;
        }
        return merge(info);
    }

    @Transactional
    public HospitalComparisonInfo save(HospitalComparisonInfo info) {
        if (info.getId() == 0) {
            persist(info);
            return info;
        }
        return merge(info);
    }

    public void deleteBaseInformation(AEBBaseInformation info) {
        remove(info);
    }

    public List<OccupationalCategory> getOccupationalCategories() {
        String sql = "SELECT oc FROM  OccupationalCategory oc WHERE oc._isAeb = true";
        TypedQuery<OccupationalCategory> query = getEntityManager().createQuery(sql, OccupationalCategory.class);
        return query.getResultList();
    }

    public void insertOrUpdatePsyGroup(int ik, int year, PsyGroup psyGroup) {
        String sql = "update psy.ExpectedHospital \n" +
                "set ehpsygroup = " + psyGroup.getId() + " \n" +
                "where ehIk = " + ik + " \n" +
                "and ehYear = " + year + "";
        Query query = getEntityManager().createNativeQuery(sql);
        query.executeUpdate();
    }

    public List<HospitalComparisonInfo> getHosptalComparisonInfoByAccount(Account acc) {
        String jpql = "select hc from HospitalComparisonInfo hc where hc._accountId = :id";
        TypedQuery<HospitalComparisonInfo> query = getEntityManager().createQuery(jpql, HospitalComparisonInfo.class);
        query.setParameter("id", acc.getId());
        return query.getResultList();
    }

    public PsyGroup getPsyGroupByIkAndYear(int ik, int year) {
        String sql = "select ehPsyGroup\n" +
                "from psy.ExpectedHospital\n" +
                "where ehIk = " + ik + "\n" +
                "and ehYear = " + year + "";

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> result = query.getResultList();
        if (result.size() == 1) {
            return PsyGroup.getById(result.get(0));
        } else {
            throw new IllegalArgumentException("PsyGroup not found for IK: " + ik + " and Year: " + year);
        }
    }


    public List<Integer> getAebIdsForEvaluationGroup2_3_8_9(String stateIds, int year, PsyGroup psyGroup) {
        String sql = XmlReaderPsyEvaluation.getStatementById("Gruppe_2_3_8_9");
        sql = sql.replace("{year}", String.valueOf(year));
        sql = sql.replace("{psyGroupId}", String.valueOf(psyGroup.getId()));
        sql = sql.replace("{stateIds}", stateIds);

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> results = query.getResultList();

        return results;
    }

    public List<Integer> getAebIdsForEvaluationGroup1_7(String stateIds, int year, PsyGroup psyGroup) {
        String sql = XmlReaderPsyEvaluation.getStatementById("Gruppe_1_7");
        sql = sql.replace("{year}", String.valueOf(year));
        sql = sql.replace("{psyGroupId}", String.valueOf(psyGroup.getId()));
        sql = sql.replace("{stateIds}", stateIds);

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> results = query.getResultList();

        return results;
    }

    public List<Integer> getAebIdsForEvaluationGroup5_6(String stateIds, int year) {
        String sql = XmlReaderPsyEvaluation.getStatementById("Gruppe_5_6");
        sql = sql.replace("{year}", String.valueOf(year));
        sql = sql.replace("{stateIds}", stateIds);

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> results = query.getResultList();

        return results;
    }

    public List<Integer> getAebIdsForEvaluationGroup4(String stateIds, int year) {
        String sql = XmlReaderPsyEvaluation.getStatementById("Gruppe_4");
        sql = sql.replace("{year}", String.valueOf(year));
        sql = sql.replace("{stateIds}", stateIds);

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> results = query.getResultList();

        return results;
    }

    public int getAebIdForEvaluationHospital2_3_5_6_8_9(int ik, int year) {
        return getAebIdForEvaluationHospital(ik, year, "KH_2_3_5_6_8_9");
    }

    public int getAebIdForEvaluationHospital1_4_7(int ik, int year) {
        return getAebIdForEvaluationHospital(ik, year, "KH_1_4_7");
    }

    private int getAebIdForEvaluationHospital(int ik, int year, String statementName) {
        String sql = XmlReaderPsyEvaluation.getStatementById(statementName);
        sql = sql.replace("{year}", String.valueOf(year));
        sql = sql.replace("{ik}", String.valueOf(ik));

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> result = query.getResultList();

        if (result.size() == 1) {
            return result.get(0);
        } else {
            return 0;
        }
    }

    public void insertNewCompatingConflict(AEBBaseInformation aebBaseInformation1, HospitalComparisonHospitals hospital) {
        String sqlTemplate = "insert into psy.HospitalComparisonConflicts" +
                "(hccHospitalComparisonEvaluationId, hccAebBaseInformationId1, hccAebBaseInformationId2) values(%s, %s, %s)";

        String sql = String.format(sqlTemplate, hospital.getId(), hospital.getAebBaseInformationId(), aebBaseInformation1);

        getEntityManager().createNativeQuery(sql).executeUpdate();
    }

    public Optional<HospitalComparisonJob> getOldestNewJob() {
        // TODO: Herraussuchen
        return Optional.empty();
    }
}
