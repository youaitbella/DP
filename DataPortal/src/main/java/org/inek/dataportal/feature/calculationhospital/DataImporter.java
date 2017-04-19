/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import java.util.Arrays;
import java.util.List;
import org.inek.dataportal.entities.calc.KGPListMedInfra;
import org.inek.dataportal.entities.calc.PeppCalcBasics;

/**
 *
 * @author kunkelan
 */
public enum DataImporter {
    PEPP_MED_INFRA("Nummer der Kostenstelle;Name der Kostenstelle;Verwendeter Schlüssel;Kostenvolumen",
            new FileHolder(),
            Arrays.asList(
                    new DataImportCheck<KGPListMedInfra, String>(
                            ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"), 
                            DataImportCheck::tryImportString, 
                            (i, s) -> i.setCostCenterNumber(s), 
                            "Nummer der Kostenstelle ungültig: "),
                    new DataImportCheck<KGPListMedInfra, String>(
                            ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"), 
                            DataImportCheck::tryImportString, 
                            (i, s) -> i.setCostCenterText(s), 
                            "Name der Kostenstelle ungültig: "),
                    new DataImportCheck<KGPListMedInfra, String>(
                            ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"), 
                            DataImportCheck::tryImportString, 
                            (i, s) -> i.setKeyUsed(s), 
                            "Verwendeter Schlüssel ungültig: "),
                    new DataImportCheck<KGPListMedInfra, Integer>(
                            ErrorCounter.obtainErrorCounter("PEPP_MED_INFRA"), 
                            DataImportCheck::tryImportRoundedInteger, 
                            (i, s) -> i.setAmount(s), 
                            "Kostenvolumen ungültig: ")
            )
    );
    
    DataImporter(String headLine, FileHolder fileHolder, List<DataImportCheck<?, ?>> checker) {
        this.headLine = headLine;
        this.fileHolder = fileHolder;
        this.checker = checker;
    }
    
    public void uploadNoticesPepp(PeppCalcBasics calcBasics) {
    
    }
    
    public FileHolder fileHolder;
    private String headLine;
    private List<DataImportCheck<?, ?>> checker;
    
    
}
