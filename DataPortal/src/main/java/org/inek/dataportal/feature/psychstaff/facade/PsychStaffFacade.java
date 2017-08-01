/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.facade;

import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import org.inek.dataportal.facades.AbstractDataAccess;
import org.inek.dataportal.feature.psychstaff.entity.OccupationalCatagory;
import org.inek.dataportal.feature.psychstaff.entity.PersonnelGroup;
import org.inek.dataportal.feature.psychstaff.entity.StaffProof;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class PsychStaffFacade extends AbstractDataAccess {

    public StaffProof findStaffProof(int id) {
        return find(StaffProof.class, id);
    }
    
    public int getSumSamePersonalGroup(int personalGroupId) {
        List<OccupationalCatagory> categories = findAll(OccupationalCatagory.class);
        int result = 0;
        categories.stream().filter((category) -> 
                (category.getPersonnelGroup().getId() == personalGroupId)).map((_item) -> 1).reduce(result, Integer::sum);
        return result;
    }
    
    public List<PersonnelGroup> getPersonnelGroups() {
        return findAll(PersonnelGroup.class);
    }
    
    public List<OccupationalCatagory> getOccupationalCategories() {
        return findAll(OccupationalCatagory.class);
    }

    public StaffProof saveStaffProof(StaffProof staffProof) {
        if (staffProof.getId() < 0){
            persist(staffProof);
            return staffProof;
        }
        
        // todo: persist included lists
        return merge(staffProof);
    }
    
}
