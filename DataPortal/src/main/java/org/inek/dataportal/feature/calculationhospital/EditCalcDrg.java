/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Set;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.controller.SessionController;
import org.inek.dataportal.entities.calc.CalcBasicsDrg;
import org.inek.dataportal.enums.CalcHospitalFunction;
import org.inek.dataportal.facades.CalcFacade;
import org.jboss.weld.util.collections.ArraySet;

/**
 *
 * @author vohldo
 */
@SessionScoped
@Named
public class EditCalcDrg implements Serializable {
    
    @Inject private CalcFacade _calcFacade;
    @Inject private SessionController _sessionController;
    
    private CalcBasicsDrg _basicsDrg;
    private int _basicIk = 0;
    
    public int getBasicIk() {
        initCalcBasics();
        return _basicIk;
    }

    public void setBasicIk(int _basicIk) {
        this._basicIk = _basicIk;
    }
    
    public Set<Integer> getCalcIks() {
        Set<Integer> iks = new ArraySet<>();
        iks.add(_sessionController.getAccountId());
        return _calcFacade.obtainIks4NewBasiscs(CalcHospitalFunction.CalculationBasicsDrg, iks, Calendar.getInstance().get(Calendar.YEAR));
    }
    
    public String save() {
        
        return "";
    }
    
    private void initCalcBasics() {
        if(this._basicIk != 0) {
            _basicsDrg = _calcFacade.getCalcBasicDrgByIkAndAccountId(_basicIk, _sessionController.getAccountId());
        }
    }
}
