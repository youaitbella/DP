/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.psychstaff.facade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.inek.dataportal.common.data.AbstractDataAccessWithActionLog;
import org.inek.dataportal.common.data.KhComparison.entities.OccupationalCategory;
import org.inek.dataportal.common.data.KhComparison.entities.PersonnelGroup;
import org.inek.dataportal.common.data.access.ConfigFacade;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.DataSet;
import org.inek.dataportal.psy.psychstaff.entity.*;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityGraph;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author muellermi
 */
//@RequestScoped
//@Transactional
// can't use CDI, need EJB to enable inject into entity
@Stateless
public class PsychStaffFacade extends AbstractDataAccessWithActionLog {

    private static final Logger LOGGER = Logger.getLogger("PsychStaffFacade");
    @Inject
    private ConfigFacade _configFacade;

    public StaffProof findStaffProof(int id) {
        EntityGraph graph = getEntityManager().createEntityGraph(StaffProof.class);
        graph.addSubgraph("_staffProofAgreed");
        graph.addSubgraph("_staffProofEffective");
        graph.addSubgraph("_staffProofExplanation");

        Map<String, Object> props = new HashMap<>();
        props.put("javax.persistence.loadgraph", graph);

        return getEntityManager().find(StaffProof.class, id, props);
        //  return find(StaffProof.class, id);
    }


    @SuppressWarnings("unchecked")
    public List<StaffProof> getStaffProofs(int accountId, DataSet dataSet, Set<Integer> allowedIkSet, Set<Integer> deniedIkSet) {
        String allowedIks = allowedIkSet.stream().map(i -> "" + i).collect(Collectors.joining(","));
        String deniedIks = deniedIkSet.stream().map(i -> "" + i).collect(Collectors.joining(","));

        String sql = "SELECT m.* \n"
                + "FROM psy.StaffProofMaster m \n";
        sql += "WHERE (spmAccountId = " + accountId;
        if (!deniedIks.isEmpty()) {
            sql += " and spmIk not in (" + deniedIks + ")";
        }
        if (!allowedIks.isEmpty()) {
            sql += " or spmIk in (" + allowedIks + ")";
        }
        sql += ")\r\n";
        if (dataSet == DataSet.AllSealed) {
            sql += " and spmStatusApx1 = 10 \n"
                    + " and (spmExclusionFactId1 > 0 \n"
                    + "     or exists (select 1 from psy.StaffProofDocument where spdSignature = spmSignatureAgreement))\n"
                    + " and spmStatusApx2 = 10 \n"
                    + " and (spmExclusionFactId2 > 0 \n"
                    + "     or exists (select 1 from psy.StaffProofDocument where spdSignature = spmSignatureEffective))\n";
        } else {
            sql += " and (\n"
                    + "     spmStatusApx1 < 10 \n"
                    + "  or spmExclusionFactId1 = 0 and not exists "
                    + "        (select 1 from psy.StaffProofDocument where spdSignature = spmSignatureAgreement)\n"
                    + "  or spmStatusApx2 < 10 \n"
                    + "  or spmExclusionFactId2 = 0 and not exists "
                    + "        (select 1 from psy.StaffProofDocument where spdSignature = spmSignatureEffective)\n"
                    + "  )";
        }
        sql += "ORDER BY spmYear, spmIk";
        Query query = getEntityManager().createNativeQuery(sql, StaffProof.class);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<StaffProof> getStaffProofs(String filter) {
//        String sql = "SELECT n FROM StaffProof n "
//                + "ORDER BY n._year, n._ik";
//        TypedQuery<StaffProof> query = getEntityManager().createQuery(sql, StaffProof.class);
        String sql = "SELECT * FROM psy.StaffProofMaster\r\n"
                + "join CallCenterDb.dbo.ccCustomer on spmIk = cuIk\r\n"
                + "where cast(spmIk as varchar) like '%" + filter + "%'\r\n"
                + (filter.length() > 0 ? "or cuName like '%" + filter + "%'\r\n" : "")
                + (filter.length() > 0 ? "or cuCity like '%" + filter + "%'\r\n" : "")
                + "ORDER BY spmYear, spmIk";
        Query query = getEntityManager().createNativeQuery(sql, StaffProof.class);
        return query.getResultList();
    }

    public int getSumSamePersonalGroup(int personalGroupId) {
        List<OccupationalCategory> categories = findAll(OccupationalCategory.class);
        int result = 0;
        categories.stream().filter((category)
                -> (category.getPersonnelGroup().getId() == personalGroupId)).map((_item) -> 1).reduce(result, Integer::sum);
        return result;
    }

    public List<PersonnelGroup> getPersonnelGroups() {
        return findAll(PersonnelGroup.class);
    }

    public List<OccupationalCategory> getOccupationalCategories() {
        String sql = "SELECT oc FROM  OccupationalCategory oc WHERE oc._isPsyStaff = true";
        TypedQuery<OccupationalCategory> query = getEntityManager().createQuery(sql, OccupationalCategory.class);
        return query.getResultList();
    }

    public StaffProof saveStaffProof(StaffProof staffProof) {
        if (staffProof.getId() < 0) {
            persist(staffProof);
            return staffProof;
        }

        for (PsychType type : PsychType.values()) {
            for (StaffProofAgreed item : staffProof.getStaffProofsAgreed(type)) {
                item.setStaffProofMasterId(staffProof.getId());
                merge(item);
            }
            for (StaffProofEffective item : staffProof.getStaffProofsEffective(type)) {
                item.setStaffProofMasterId(staffProof.getId());
                merge(item);
            }
            for (StaffProofExplanation item : staffProof.getStaffProofExplanations(type)) {
                item.setStaffProofMasterId(staffProof.getId());
                merge(item);
            }
        }
        StaffProof mergedStaffProof = merge(staffProof);
        if (_configFacade.readConfigBool(ConfigKey.IsPsychStaffParanoiacheckEnabled)) {
            if (!hasDifferentData(staffProof, mergedStaffProof)) {
                StaffProof findFresh = findFresh(StaffProof.class, mergedStaffProof.getId());
                hasDifferentData(findFresh, mergedStaffProof);
            }
        }
        return merge(mergedStaffProof);
    }

    private boolean hasDifferentData(StaffProof staffProof, StaffProof mergedStaffProof) {
        boolean foundDiff = false;
        for (PsychType type : PsychType.values()) {
            List<StaffProofAgreed> staffProofsAgreed = mergedStaffProof.getStaffProofsAgreed(type);
            for (StaffProofAgreed agreedItem : staffProof.getStaffProofsAgreed(type)) {
                Optional<StaffProofAgreed> mergedEntry = staffProofsAgreed
                        .stream()
                        .filter(a -> a.getOccupationalCategory().getId() == agreedItem.getOccupationalCategory().getId())
                        .findFirst();
                if (!mergedEntry.isPresent()) {
                    LOGGER.log(Level.SEVERE, "Entry missing in saved StaffProofAgreed: {0}", getJson(agreedItem));
                    foundDiff = true;
                    continue;
                }
                StaffProofAgreed savedAgreed = mergedEntry.get();
                if (agreedItem.getStaffingComplete() != savedAgreed.getStaffingComplete()) {
                    LOGGER.log(Level.SEVERE, "Entry differs in saved StaffProofAgreed: {0}", getJson(agreedItem));
                    foundDiff = true;
                }
            }

            List<StaffProofEffective> staffProofsEffective = mergedStaffProof.getStaffProofsEffective(type);
            for (StaffProofEffective effectiveItem : staffProof.getStaffProofsEffective(type)) {
                Optional<StaffProofEffective> mergedEntry = staffProofsEffective
                        .stream()
                        .filter(a -> a.getOccupationalCategory().getId() == effectiveItem.getOccupationalCategory().getId())
                        .findFirst();
                if (!mergedEntry.isPresent()) {
                    LOGGER.log(Level.SEVERE, "Entry missing in saved StaffProofEffective: {0}", getJson(effectiveItem));
                    foundDiff = true;
                    continue;
                }
                StaffProofEffective savedEffective = mergedEntry.get();
                if (effectiveItem.getStaffingComplete() != savedEffective.getStaffingComplete()) {
                    LOGGER.log(Level.SEVERE, "Entry differs in saved StaffProofEffective: {0}", getJson(effectiveItem));
                    foundDiff = true;
                }
            }
        }
        return foundDiff;
    }

    private String getJson(Object proof) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            String json = mapper.writeValueAsString(proof);
            return new String(json.getBytes("UTF-8"));
        } catch (JsonProcessingException | UnsupportedEncodingException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
            return "Error during json conversion. For details see log.";
        }
    }

    public void delete(StaffProof staffProof) {
        remove(staffProof);
    }

    public List<Integer> getExistingYears(int ik) {
        String sql = "SELECT sp._year FROM StaffProof sp WHERE sp._ik = :ik";
        TypedQuery<Integer> query = getEntityManager().createQuery(sql, Integer.class);
        query.setParameter("ik", ik);
        return query.getResultList();
    }

    public List<ExclusionFact> findAllExclusionFacts() {
        return findAll(ExclusionFact.class);
    }

}
