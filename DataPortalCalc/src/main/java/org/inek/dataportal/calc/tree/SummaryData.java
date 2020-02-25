/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.tree;

import org.inek.dataportal.calc.entities.CalcHospitalInfo;
import org.inek.dataportal.calc.enums.CalcInfoType;
import org.inek.dataportal.calc.facades.CalcFacade;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.scope.FeatureScoped;
import org.inek.dataportal.common.utils.DateUtils;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author lautenti
 */
@Named
@FeatureScoped
public final class SummaryData {

    @Inject
    private CalcFacade calcFacade;
    @Inject
    private SessionController sessionController;

    private int selectedtDataYear = DateUtils.currentMonth() == 1 ? DateUtils.currentYear() - 2 : DateUtils.currentYear() - 1;
    private List<CalcHospitalInfo> allKg;
    private List<CalcHospitalInfo> allTe;
    private List<CalcHospitalInfo> allKvm;
    private boolean all = false;

    public int getSelectedtDataYear() {
        return selectedtDataYear;
    }

    public void setSelectedtDataYear(int selectedtDataYear) {
        this.selectedtDataYear = selectedtDataYear;
    }

    public List<CalcHospitalInfo> getAllKg() {
        return allKg;
    }

    public void setAllKg(List<CalcHospitalInfo> allKg) {
        this.allKg = allKg;
    }

    public List<CalcHospitalInfo> getAllTe() {
        return allTe;
    }

    public void setAllTe(List<CalcHospitalInfo> allTe) {
        this.allTe = allTe;
    }

    public List<CalcHospitalInfo> getAllKvm() {
        return allKvm;
    }

    public void setAllKvm(List<CalcHospitalInfo> allKvm) {
        this.allKvm = allKvm;
    }

    public boolean isAll() {
        return all;
    }

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

    public List<SelectItem> getDataYears() {
        List<SelectItem> items = new ArrayList<>();
        IntStream.range(DateUtils.currentYear()-2, DateUtils.currentYear()).forEach(y -> items.add(new SelectItem(y, "Datenjahr " + y)));
        return items;
    }

    public int getCurrentDataYear(){
        return DateUtils.currentYear();
    }

}
