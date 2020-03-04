package org.inek.dataportal.common.data.KhComparison.facade;

import javafx.util.Pair;
import org.inek.dataportal.common.data.AbstractDataAccess;
import org.inek.dataportal.common.data.KhComparison.entities.*;
import org.inek.dataportal.common.data.KhComparison.enums.PsyGroup;
import org.inek.dataportal.common.data.KhComparison.enums.PsyHosptalComparisonStatus;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.CustomerTyp;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.XmlReaderPsyEvaluation;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.inek.dataportal.api.helper.PortalConstants.*;

/**
 * @author lautenti
 */
@Stateless
public class AEBFacade extends AbstractDataAccess {

    public AEBFacade() {
    }

    public AEBFacade(EntityManager em) {
        super(em);
    }

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
        query.setParameter(IK, ik);
        query.setParameter(YEAR, year);
        query.setParameter("typ", typ);
        query.setParameter(STATUS, status);
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
        query.setParameter(STATUS, status);
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
        query.setParameter(STATUS, status);
        query.setParameter(IK, ik);
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
        query.setParameter(STATUS, status);
        query.setParameter("iks", iks);
        query.setParameter("typ", typ.id());
        return query.getResultList();
    }

    public boolean structureBaseInformaionAvailable(int ik) {
        String sql = "SELECT si FROM StructureBaseInformation si WHERE si._ik = :ik";
        TypedQuery<StructureBaseInformation> query = getEntityManager().createQuery(sql, StructureBaseInformation.class);
        query.setParameter(IK, ik);
        return !query.getResultList().isEmpty();
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
        query.setParameter(IK, ik);
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
                + "and biStatusId in (10)";
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

    @Transactional
    public InekComparisonJob saveInekComparison(InekComparisonJob info) {
        if (info.getId() == 0) {
            persist(info);
            return info;
        }
        return merge(info);
    }

    @Transactional
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public HospitalComparisonJob save(HospitalComparisonJob job) {
        return merge(job);
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

    public Optional<HospitalComparisonInfo> getHospitalComparisonInfoByHcId(String hcId) {
        String jpql = "select hc from HospitalComparisonInfo hc where hc._hospitalComparisonId = :hcId";
        TypedQuery<HospitalComparisonInfo> query = getEntityManager().createQuery(jpql, HospitalComparisonInfo.class);
        query.setParameter("hcId", hcId);
        try {
            return Optional.of(query.getSingleResult());
        } catch (Exception ex) {
            return Optional.empty();
        }
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
        return retrieveAebIdsForGroup(stateIds, year, psyGroup, sql);
    }

    public List<Integer> getAebIdsForEvaluationGroup1_7(String stateIds, int year, PsyGroup psyGroup) {
        String sql = XmlReaderPsyEvaluation.getStatementById("Gruppe_1_7");
        return retrieveAebIdsForGroup(stateIds, year, psyGroup, sql);
    }

    private List<Integer> retrieveAebIdsForGroup(String stateIds, int year, PsyGroup psyGroup, String sql) {
        sql = sql.replace(VAR_YEAR, String.valueOf(year));
        sql = sql.replace(VAR_PSY_GROUP_ID, String.valueOf(psyGroup.getId()));
        sql = sql.replace(VAR_STATE_IDS, stateIds);

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> results = query.getResultList();

        return results;
    }

    public List<Integer> getAebIdsForEvaluationGroup5_6(String stateIds, int year) {
        String sql = XmlReaderPsyEvaluation.getStatementById("Gruppe_5_6");
        sql = sql.replace(VAR_YEAR, String.valueOf(year));
        sql = sql.replace(VAR_STATE_IDS, stateIds);

        Query query = getEntityManager().createNativeQuery(sql);
        @SuppressWarnings("unchecked")
        List<Integer> results = query.getResultList();

        return results;
    }

    public List<Integer> getAebIdsForEvaluationGroup4(String stateIds, int year) {
        String sql = XmlReaderPsyEvaluation.getStatementById("Gruppe_4");
        sql = sql.replace(VAR_YEAR, String.valueOf(year));
        sql = sql.replace(VAR_STATE_IDS, stateIds);

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
        sql = sql.replace(VAR_YEAR, String.valueOf(year));
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

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Optional<HospitalComparisonJob> getOldestNewJob() {
        String sql = "SELECT jo FROM HospitalComparisonJob jo WHERE jo._status = :status order by jo._createdDate";
        TypedQuery<HospitalComparisonJob> query = getEntityManager().createQuery(sql, HospitalComparisonJob.class);
        query.setParameter(STATUS, PsyHosptalComparisonStatus.NEW.name());

        List<HospitalComparisonJob> resultList = query.getResultList();

        if (resultList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(resultList.get(0));
        }
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public Optional<InekComparisonJob> getOldestNewInekJob() {
        String sql = "SELECT jo FROM InekComparisonJob jo WHERE jo.status = :status order by jo.createdDate";
        TypedQuery<InekComparisonJob> query = getEntityManager().createQuery(sql, InekComparisonJob.class);
        query.setParameter(STATUS, PsyHosptalComparisonStatus.NEW.name());

        List<InekComparisonJob> resultList = query.getResultList();

        if (resultList.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(resultList.get(0));
        }
    }

    public List<HospitalComparisonInfo> getHosptalComparisonInfoByIks(Set<Integer> iks) {
        if (iks.isEmpty()) {
            return new ArrayList<>();
        }
        String jpql = "select hc from HospitalComparisonInfo hc where hc._hospitalIk in :ik";
        TypedQuery<HospitalComparisonInfo> query = getEntityManager().createQuery(jpql, HospitalComparisonInfo.class);
        query.setParameter(IK, iks);
        return query.getResultList();
    }

    public boolean emailIsLkaForStateId(String emailDomain, int stateId) {
        String sql = "select *\n" +
                "from psy.lkaStateDomainMap\n" +
                "where lstStateId = " + stateId + "\n" +
                "and lstEmailDomain = '" + emailDomain + "'";

        List resultList = getEntityManager().createNativeQuery(sql).getResultList();
        return !resultList.isEmpty();
    }

    public List excludedIks() {
        String sql = "select hcexIk from psy.HospitalComparisonExcludeIK";
        return getEntityManager().createNativeQuery(sql).getResultList();
    }

    public boolean ikHasBedsOrPlacesForYear(int ik, int year) {
        String sql = "select *\n" +
                "from psy.StructureBaseInformation sbi\n" +
                "join psy.StructureInformation si on si.siStructureBaseInformationId = sbi.sbiId\n" +
                "where sbi.sbiIk = " + ik + "\n" +
                "and year(si.siValidFrom) <= " + year + "\n" +
                "and siStructureCategorie in ('BedCount', 'TherapyPartCount')";

        List resultList = getEntityManager().createNativeQuery(sql).getResultList();
        return !resultList.isEmpty();
    }

    public boolean aebIdIsInAnyEvaluation(int id) {
        String sql = "select *\n" +
                "from psy.HospitalComparisonHospitals hc\n" +
                "where hc.hchAebBaseInformationId = " + id + "\n";

        List resultList = getEntityManager().createNativeQuery(sql).getResultList();
        return !resultList.isEmpty();
    }

    public void storeCollision(int aebId1, int aebId2) {
        String sql = "insert into psy.mapHospitalComparisonConflict (hccAebBaseInformationId1, hccAebBaseInformationId2)\n" +
                "select {id1}, {id2}\n" +
                "where not exists (\n" +
                "    select 1 from psy.mapHospitalComparisonConflict \n" +
                "\twhere hccAebBaseInformationId1 = {id1} and hccAebBaseInformationId2 = {id2})";

        String id1 = "" + (aebId1 < aebId1 ? aebId1 : aebId2);
        String id2 = "" + (aebId1 < aebId1 ? aebId2 : aebId1);
        sql = sql.replace("{id1}", id1)
                .replace("{id2}", id2);
        Query query = getEntityManager().createNativeQuery(sql);
        query.executeUpdate();
    }

    public InekComparisonJob newInekComparisonJob(Account account, int inekDataYear, String inekAebSendDateUpToConsider) {
        InekComparisonJob.checkDataYear(inekDataYear);
        InekComparisonJob.checkAebToConsider(inekAebSendDateUpToConsider);
        InekComparisonJob inekComparisonJob = new InekComparisonJob(account, inekDataYear, inekAebSendDateUpToConsider);
        getEntityManager().persist(inekComparisonJob);
        return inekComparisonJob;
    }

    public void generateInekComparisonHospitals(InekComparisonJob inekComparisonJob) {
        String nativeSql = "insert into psy.InekComparisonHospital(ichInekComparisonJobId, ichStateId, ichAebBaseInformationId)\n" +
                "select icjid, bl, biid\n" +
                "from (\n" +
                "\tselect icjid, bl, biid, max(nInBl) over (partition by bl order by nInBl desc) maxNinBl\n" +
                "\tfrom (\n" +
                "\t\tselect icjid, bl, biid, row_number() over (partition by bl order by biid) as nInBl \n" +
                "\t\tfrom (values \n" +
                "\t\t\t(1), (2), (3), (4), (5), (6), (7), (8), (9), (10), (11), (12), (13), (14), (15), (16), (0)\n" +
                "\t\t) bundesland(bl)\n" +
                "\t\tjoin psy.InekComparisonJob icj on 1=1\n" +
                "\t\tjoin (\n" +
                "\t\t\tselect biId, biIk, biDataYear, biTyp, biSend, row_number() " +
                " over (partition by biik, biDatayear order by biik, biDatayear desc, bityp) nr\n" +
                "\t\t\tfrom psy.AEBBaseInformation bi\n" +
                "\t\t\twhere biStatusId = 10\n" +
                "\t\t) bi on bi.biDataYear = icj.icjDataYear and datediff(day, biSend, icjAebUpTo) >= 0 and nr = 1\n" +
                "\t\tjoin CallCenterDB.[dbo].[ccCustomer] on cuik=biIk and cuIsActive=1 and cuIsTest=0\n" +
                "\t\tleft join (\n" +
                "\t\t\tselect hccAebBaseInformationId1, hccAebBaseInformationId2 \n" +
                "\t\t\tfrom [psy].[mapHospitalComparisonConflict]\n" +
                "\t\t\tjoin psy.AEBBaseInformation aeb1 on aeb1.biid=hccAebBaseInformationId1 and aeb1.biStatusId=10\n" +
                "\t\t\tjoin psy.AEBBaseInformation aeb2 on aeb2.biid=hccAebBaseInformationId2 and aeb2.biStatusId=10\n" +
                "\t\t) a on a.hccAebBaseInformationId1=bi.biId or a.hccAebBaseInformationId2=bi.biId\n" +
                "\t\tleft join psy.HospitalComparisonExcludeIK hce on hcexIk=biIk\n" +
                "\t\twhere icjId = ${icjId} and a.hccAebBaseInformationId1 is null and hce.hcexIk is null\n" +
                "\t\t\tand (\n" +
                "\t\t\t\tcase when cuPsyStateId not in (0, 255) then cuPsyStateId else cuStateId end = bl\n" +
                "\t\t\t\tor bl = 0\n" +
                "\t\t\t)\n" +
                "\t) proBl\n" +
                ") bl\n" +
                "where maxNinBl > 3\n";

        String hospitalsToCreate = nativeSql.replace("${icjId}", String.valueOf(inekComparisonJob.getId()));
        Query query = getEntityManager().createNativeQuery(hospitalsToCreate);
        query.executeUpdate();
    }
}
