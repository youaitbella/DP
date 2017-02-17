/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades.calc;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import org.inek.dataportal.entities.calc.DistributionModel;
import org.inek.dataportal.entities.calc.DistributionModelDetail;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.facades.AbstractDataAccess;

/**
 *
 * @author muellermi
 */
@RequestScoped
@Transactional
public class DistModelFacade extends AbstractDataAccess{
    
    public DistributionModel findDistributionModel(int id) {
        return findFresh(DistributionModel.class, id);
    }

    public DistributionModel saveDistributionModel(DistributionModel model) {
        if (model.getStatus() == WorkflowStatus.Unknown) {
            model.setStatus(WorkflowStatus.New);
        }

        if (model.getId() == -1) {
            persist(model);
            return model;
        }
        
        for (DistributionModelDetail detail : model.getDetails()) {
            if (detail.getId() < 0){
                persist (detail);
            }else{
                merge (detail);
            }
        }
        
        return merge(model);
    }
    
}
