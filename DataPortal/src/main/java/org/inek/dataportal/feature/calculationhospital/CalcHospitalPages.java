/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.util.LinkedHashMap;
import java.util.Map;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import org.inek.dataportal.enums.Pages;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author vohldo
 */
@Named
@RequestScoped
public class CalcHospitalPages {
    
    public Map<String, String> getCalcDrgPages() {
        LinkedHashMap<String, String> menu = new LinkedHashMap<>();
        menu.put(Utils.getMessage("tabUMMaster"), Pages.CalcDrgBasics.URL());
        menu.put(Utils.getMessage("lblBasicExplanation"), Pages.CalcDrgBasicExplanation.URL());
        menu.put(Utils.getMessage("lblCalcNeonatology"), Pages.CalcDrgNeonatology.URL());
        return menu;
    }
}
