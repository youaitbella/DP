/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades;

import javax.ejb.Stateless;
import org.inek.dataportal.entities.insurance.InsuranceNubNotice;

/**
 *
 * @author muellermi
 */
@Stateless
public class InsuranceFacade extends AbstractDataAccess {

    public InsuranceNubNotice findNubNotice (int id){
        return super.find(InsuranceNubNotice.class, id);
    }
    
    public InsuranceNubNotice findFreshNubNotice (int id){
        return super.findFresh(InsuranceNubNotice.class, id);
    }
   
    public InsuranceNubNotice saveNubNotice(InsuranceNubNotice notice) {
        if (notice.getId() == -1) {
            persist(notice);
            return notice;
        }
        return merge(notice);
    }
    
}
