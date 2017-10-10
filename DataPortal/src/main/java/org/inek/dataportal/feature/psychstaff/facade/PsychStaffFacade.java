/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import org.inek.dataportal.facades.AbstractDataAccess;
import org.inek.dataportal.feature.psychstaff.entity.OccupationalCategory;
import org.inek.dataportal.feature.psychstaff.entity.PersonnelGroup;
import org.inek.dataportal.feature.psychstaff.entity.StaffProof;

/**
 *
 * @author muellermi
 */
//@RequestScoped
//@Transactional
// can't use CDI, need EJB to enable inject into entity
@Stateless
public class PsychStaffFacade extends AbstractDataAccess {

    public StaffProof findStaffProof(int id) {
        return find(StaffProof.class, id);
    }
    
    public List<StaffProof> getStaffProofs(int accountId) {
        String sql = "SELECT n FROM StaffProof n "
                + "WHERE n._accountId = :accountId ORDER BY n._year, n._id";
        TypedQuery<StaffProof> query = getEntityManager().createQuery(sql, StaffProof.class);
        query.setParameter("accountId", accountId);
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
        return findAll(OccupationalCategory.class);
    }

    public StaffProof saveStaffProof(StaffProof staffProof) {
        if (staffProof.getId() < 0) {
            persist(staffProof);
            return staffProof;
        }

        /*
        usually we need to store lists separate - but here, everything is ok with a merge only
        merging separate will double the entries!
        
        for (PsychType type : PsychType.values()) {
            for (StaffProofAgreed proof : staffProof.getStaffProofsAgreed(type)) {
                proof.setStaffProofMasterId(staffProof.getId());
                merge(proof);
            }
            for (StaffProofEffective proof : staffProof.getStaffProofsEffective(type)) {
                proof.setStaffProofMasterId(staffProof.getId());
                merge(proof);
            }
        }
        */
        return merge(staffProof);
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
}
