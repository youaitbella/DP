/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.facade;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.TypedQuery;
import org.inek.dataportal.enums.DataSet;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.AbstractDataAccess;
import org.inek.dataportal.feature.psychstaff.entity.OccupationalCatagory;
import org.inek.dataportal.feature.psychstaff.entity.PersonnelGroup;
import org.inek.dataportal.feature.psychstaff.entity.StaffProof;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofAgreed;
import org.inek.dataportal.feature.psychstaff.entity.StaffProofEffective;
import org.inek.dataportal.feature.psychstaff.enums.PsychType;

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
    
    public List<StaffProof> getPersonals(int accountId, DataSet dataSet) {
        String sql = "SELECT n FROM StaffProof n "
                + "WHERE n._accountId = :accountId and n._statusId BETWEEN :minStatus AND :maxStatus ORDER BY n._year, n._id";
        TypedQuery<StaffProof> query = getEntityManager().createQuery(sql, StaffProof.class);
        int minStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.New.getId() : WorkflowStatus.Provided.getId();
        int maxStatus = dataSet == DataSet.AllOpen ? WorkflowStatus.Provided.getId()-1 : WorkflowStatus.Retired.getId();
        query.setParameter("accountId", accountId);
        query.setParameter("minStatus", minStatus);
        query.setParameter("maxStatus", maxStatus);
        return query.getResultList();
    }

    public int getSumSamePersonalGroup(int personalGroupId) {
        List<OccupationalCatagory> categories = findAll(OccupationalCatagory.class);
        int result = 0;
        categories.stream().filter((category)
                -> (category.getPersonnelGroup().getId() == personalGroupId)).map((_item) -> 1).reduce(result, Integer::sum);
        return result;
    }

    public List<PersonnelGroup> getPersonnelGroups() {
        return findAll(PersonnelGroup.class);
    }

    public List<OccupationalCatagory> getOccupationalCategories() {
        return findAll(OccupationalCatagory.class);
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
