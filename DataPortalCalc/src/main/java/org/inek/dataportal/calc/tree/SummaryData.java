/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.tree;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import org.inek.dataportal.calc.entities.CalcHospitalInfo;
import org.inek.dataportal.calc.enums.CalcInfoType;
import org.inek.dataportal.calc.facades.CalcFacade;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.scope.FeatureScoped;

/**
 *
 * @author lautenti
 */
@Named
@FeatureScoped
public final class SummaryData {

    @Inject
    private CalcFacade calcFacade;
    @Inject
    private SessionController sessionController;

    private int selectedtDataYear = 2018;
    private List<CalcHospitalInfo> allKg;
    private List<CalcHospitalInfo> allTe;
    private List<CalcHospitalInfo> allKvm;
    private boolean all = false;

    /**
     * @return the selectedtDataYear
     */
    public int getSelectedtDataYear() {
        return selectedtDataYear;
    }

    /**
     * @param selectedtDataYear the selectedtDataYear to set
     */
    public void setSelectedtDataYear(int selectedtDataYear) {
        this.selectedtDataYear = selectedtDataYear;
    }

    /**
     * @return the allKg
     */
    public List<CalcHospitalInfo> getAllKg() {
        return allKg;
    }

    /**
     * @param allKg the allKg to set
     */
    public void setAllKg(List<CalcHospitalInfo> allKg) {
        this.allKg = allKg;
    }

    /**
     * @return the allTe
     */
    public List<CalcHospitalInfo> getAllTe() {
        return allTe;
    }

    /**
     * @param allTe the allTe to set
     */
    public void setAllTe(List<CalcHospitalInfo> allTe) {
        this.allTe = allTe;
    }

    /**
     * @return the allKvm
     */
    public List<CalcHospitalInfo> getAllKvm() {
        return allKvm;
    }

    /**
     * @param allKvm the allKvm to set
     */
    public void setAllKvm(List<CalcHospitalInfo> allKvm) {
        this.allKvm = allKvm;
    }

    /**
     * @return the all
     */
    public boolean isAll() {
        return all;
    }

    /**
     * @param all the all to set
     */
    public void setAll(boolean all) {
        this.all = all;
    }

    @PostConstruct
    private void init() {
        setAll(!calcFacade.agentHasHospitals(sessionController.getAccount().getEmail(), selectedtDataYear));
        reloadData();
    }

    public String getHospitalInfoLink(CalcInfoType type) {
        switch (type) {
            case SOP:
                return Pages.StatementOfParticipanceEditAddress.URL();
            case CBD:
                return Pages.CalcDrgEdit.URL();
            case CBP:
                return Pages.CalcPeppEdit.URL();
            case CBA:
                return Pages.CalcObdEdit.URL();
            case CDM:
                return Pages.CalcDistributionModel.URL();
            default:
                throw new IllegalArgumentException("Unknown calcInfoType: " + type);
        }
    }

    public String getFilterString(Account acc) {
        return acc.getLastName() + ", " + acc.getFirstName();
    }

    public void reloadData() {
        if (isAll()) {
            setAllKg(calcFacade.getAllCalcBasics(selectedtDataYear));
            setAllTe(calcFacade.getAllSop(selectedtDataYear));
            setAllKvm(calcFacade.getAllDistributionModels(selectedtDataYear));
        } else {
            setAllKg(calcFacade.getCalcBasicsByEmail(sessionController.getAccount().getEmail(), selectedtDataYear, ""));
            setAllTe(calcFacade.getSopByEmail(sessionController.getAccount().getEmail(), selectedtDataYear));
            setAllKvm(calcFacade.getDistributionModelsByEmail(sessionController.getAccount().getEmail(), selectedtDataYear));
        }
    }

    public void reloadPersonalData() {

    }

    public List<String> getAgentList() {
        List<String> agents = getAllKg().stream()
                .map(c -> c.getAgentName())
                .distinct()
                .collect(Collectors.toList());

        Collections.sort(agents);

        return agents;
    }
}
