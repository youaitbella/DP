/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.scope.FeatureScopedContextHolder;

/**
 *
 * @author muellermi
 */
@Named
@RequestScoped
public class CalculationHospitalList {
    
    public String newStatementOfParticipance() {
        // if the user hit the browser's back-button, a request might be still active. 
        // To prevent invoking the wrong, we destroy all Feature scoped beans first
        FeatureScopedContextHolder.Instance.destroyBeansOfScope("CalculationHospital");
        return Pages.NubEditAddress.URL();
    }
    
}
