/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital;

import org.inek.dataportal.entities.calc.drg.DrgCalcBasics;
import org.inek.dataportal.entities.calc.drg.KGLOpAn;

/**
 *
 * @author muellermi
 */
public class CalcBasicsDrgValueCleaner {

    /**
     * clears or defaults unused fields e.g. if there is no laboratory, clear
     * any
     *
     * @param calcBasics
     */
    public static void clearUnusedFields(DrgCalcBasics calcBasics) {
        cleanBasics(calcBasics);
        cleanBasicExplanation(calcBasics);
        cleanExternalServiceProvision(calcBasics);
        cleanOpAn(calcBasics);
        cleanMaternityRoom(calcBasics);
        cleanCardiology(calcBasics);
        cleanEndosoppy(calcBasics);
        cleanRadiology(calcBasics);
        cleanLaboratory(calcBasics);
        cleanDiagnosticScope(calcBasics);
        cleanNormalWard(calcBasics);
        cleanIntensiveCare(calcBasics);
        cleanStrokeUnit(calcBasics);
        cleanMedicalInfrastructure(calcBasics);
        cleanNonMedicalInfrastructure(calcBasics);
        cleanStaffCost(calcBasics);
        cleanValvularIntervention(calcBasics);
        cleanNeonat(calcBasics);
    }

    //<editor-fold defaultstate="collapsed" desc="cleanBasics">
    private static void cleanBasics(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanBasicExplanation">
    private static void cleanBasicExplanation(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanExternalServiceProvision">
    private static void cleanExternalServiceProvision(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanOpAn">
    private static void cleanOpAn(DrgCalcBasics calcBasics) {
        KGLOpAn opAn = calcBasics.getOpAn();
        if (!opAn.isCentralOP()) {
            // if there is no OperationRoom, remove any entry
            KGLOpAn emptyOpAn = new KGLOpAn();
            emptyOpAn.setBaseInformationId(calcBasics.getId());
            calcBasics.setOpAn(emptyOpAn);
            return;
        }
        if (opAn.getMedicalServiceSnzOP() != 4 && opAn.getFunctionalServiceSnzOP() != 4) {
            opAn.setDescriptionSnzOP("");
        }
        if (opAn.getMedicalServiceSnzAN() != 4 && opAn.getFunctionalServiceSnzAN() != 4) {
            opAn.setDescriptionSnzAN("");
        }
        if (opAn.getMedicalServiceRzOP() != 4 && opAn.getFunctionalServiceRzOP() != 4) {
            opAn.setDescriptionRzOP("");
        }
        if (opAn.getMedicalServiceRzAN() != 4 && opAn.getFunctionalServiceRzAN() != 4) {
            opAn.setDescriptionRzAN("");
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanMaternityRoom">
    private static void cleanMaternityRoom(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanCardiology">
    private static void cleanCardiology(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanEndosoppy">
    private static void cleanEndosoppy(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanRadiology">
    private static void cleanRadiology(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanLaboratory">
    private static void cleanLaboratory(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanDiagnosticScope">
    private static void cleanDiagnosticScope(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanNormalWard">
    private static void cleanNormalWard(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanIntensiveCare">
    private static void cleanIntensiveCare(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanStrokeUnit">
    private static void cleanStrokeUnit(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanMedicalInfrastructure">
    private static void cleanMedicalInfrastructure(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanNonMedicalInfrastructure">
    private static void cleanNonMedicalInfrastructure(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanStaffCost">
    private static void cleanStaffCost(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanValvularIntervention">
    private static void cleanValvularIntervention(DrgCalcBasics calcBasics) {
        if (calcBasics.isMinimalValvularIntervention()){
            return;
        }
        calcBasics.setMviFulfilled(-1);
        calcBasics.setMviGuidelineAspired(false);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="cleanNeonat">
    private static void cleanNeonat(DrgCalcBasics calcBasics) {
    }
    //</editor-fold>

}
