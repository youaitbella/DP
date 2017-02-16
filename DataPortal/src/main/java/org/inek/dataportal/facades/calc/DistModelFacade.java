/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.facades.calc;

import javax.enterprise.context.RequestScoped;
import javax.transaction.Transactional;
import org.inek.dataportal.entities.calc.DistributionModel;
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

    public DistributionModel saveDistributionModel(DistributionModel _model) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
