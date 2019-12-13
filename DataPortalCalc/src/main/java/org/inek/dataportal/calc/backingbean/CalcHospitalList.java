/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.backingbean;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.calc.entities.CalcHospitalInfo;
import org.inek.dataportal.calc.entities.drg.DrgCalcBasics;
import org.inek.dataportal.calc.entities.psy.PeppCalcBasics;
import org.inek.dataportal.calc.enums.CalcHospitalFunction;
import org.inek.dataportal.calc.enums.CalcInfoType;
import org.inek.dataportal.calc.facades.*;
import org.inek.dataportal.common.controller.ReportController;
import org.inek.dataportal.common.controller.SessionController;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.adm.ReportTemplate;
import org.inek.dataportal.common.data.iface.StatusEntity;
import org.inek.dataportal.common.enums.ConfigKey;
import org.inek.dataportal.common.enums.Pages;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.helper.StreamHelper;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.overall.ApplicationTools;
import org.inek.dataportal.common.utils.DocumentationUtil;
import org.inek.dataportal.common.utils.KeyValueLevel;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.zip.Adler32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author muellermi
 */
@Named
@RequestScoped
public class CalcHospitalList {

    // <editor-fold defaultstate="collapsed" desc="fields">
    private static final Logger LOGGER = Logger.getLogger(CalcHospitalList.class.getName());

    @Inject
    private SessionController _sessionController;
    @Inject
    private CalcFacade _calcFacade;
    @Inject
    private CalcAutopsyFacade _calcAutopsyFacade;
    @Inject
    private CalcDrgFacade _calcDrgFacade;
    @Inject
    private CalcPsyFacade _calcPsyFacade;
    @Inject
    private CalcSopFacade _calcSopPsyFacade;
    @Inject
    private DistributionModelFacade _distModelFacade;
    @Inject
    private ApplicationTools _appTools;
    private final Map<CalcHospitalFunction, Boolean> _allowedButtons = new HashMap<>();
    // </editor-fold>

    public boolean isNewStatementOfParticipanceAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsStatemenOfParticipanceCreateEnabled)) {
            return false;
        }
        if (!_allowedButtons.containsKey(CalcHospitalFunction.StatementOfParticipance)) {
            boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
            Set<Integer> iks = _calcSopPsyFacade.obtainIks4NewStatementOfParticipance(
                    _sessionController.getAccountId(),
                    Utils.getTargetYear(Feature.CALCULATION_HOSPITAL),
                    _appTools.isEnabled(ConfigKey.TestMode));
            _allowedButtons.put(CalcHospitalFunction.StatementOfParticipance, iks.size() > 0);
        }
        return _allowedButtons.get(CalcHospitalFunction.StatementOfParticipance);
    }

    public String newStatementOfParticipance() {
        return Pages.StatementOfParticipanceEditAddress.URL();
    }

    public boolean isNewDistributionModelDrgAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsDistributionModelDrgCreateEnabled)) {
            return false;
        }
        return determineDistModelButtonAllowed(CalcHospitalFunction.ClinicalDistributionModelDrg);
    }

    public boolean isNewDistributionModelPeppAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsDistributionModelPeppCreateEnabled)) {
            return false;
        }
        return determineDistModelButtonAllowed(CalcHospitalFunction.ClinicalDistributionModelPepp);
    }

    private boolean determineDistModelButtonAllowed(CalcHospitalFunction calcFunct) {
        if (!_allowedButtons.containsKey(calcFunct)) {
            boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
            int year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
            Set<Integer> possibleIks = _distModelFacade.obtainIks4NewDistributionModel(calcFunct, _sessionController.
                    getAccountId(), year, testMode);
            Account account = _sessionController.getAccount();
            boolean isAllowed = account.getFullIkSet().stream().anyMatch(ik -> possibleIks.contains(ik));
            _allowedButtons.put(calcFunct, isAllowed);
        }
        return _allowedButtons.get(calcFunct);
    }

    public boolean isNewCalculationBasicsDrgAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsDrgCreateEnabled)) {
            return false;
        }
        return determineButtonAllowed(CalcHospitalFunction.CalculationBasicsDrg);
    }

    public String newCalculationBasicsDrg() {
        return Pages.CalcDrgEdit.RedirectURL();
    }

    public boolean isNewCalculationBasicsPeppAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsPsyCreateEnabled)) {
            return false;
        }
        return determineButtonAllowed(CalcHospitalFunction.CalculationBasicsPepp);
    }

    public String newCalculationBasicsPepp() {
        return Pages.CalcPeppEdit.RedirectURL();
    }

    public boolean isNewCalculationBasicsObdAllowed() {
        if (!_appTools.isEnabled(ConfigKey.IsCalculationBasicsObdCreateEnabled)) {
            return false;
        }
        return determineButtonAllowed(CalcHospitalFunction.CalculationBasicsAutopsy);
    }

    public String newCalculationBasicsObd() {
        return Pages.CalcObdEdit.RedirectURL();
    }

    private boolean determineButtonAllowed(CalcHospitalFunction calcFunct) {
        if (!_allowedButtons.containsKey(calcFunct)) {
            boolean testMode = _appTools.isEnabled(ConfigKey.TestMode);
            Set<Integer> possibleIks;
            int year = Utils.getTargetYear(Feature.CALCULATION_HOSPITAL);
            int accountId = _sessionController.getAccountId();
            switch (calcFunct) {
                case CalculationBasicsDrg:
                    possibleIks = _calcDrgFacade.obtainIks4NewBasicsDrg(accountId, year, testMode);
                    break;
                case CalculationBasicsPepp:
                    possibleIks = _calcPsyFacade.obtainIks4NewBasicsPepp(accountId, year, testMode);
                    break;
                case CalculationBasicsAutopsy:
                    possibleIks = _calcAutopsyFacade.obtainIks4NewBasicsAutopsy(accountId, year, testMode);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown calc function: " + calcFunct);
            }
            Account account = _sessionController.getAccount();
            boolean isAllowed = account.getFullIkSet().stream().anyMatch(ik -> possibleIks.contains(ik));
            _allowedButtons.put(calcFunct, isAllowed);
        }
        return _allowedButtons.get(calcFunct);
    }

    public String printHospitalInfo(CalcHospitalInfo hospitalInfo) {
        switch (hospitalInfo.getType()) {
            case SOP:
                return printData(_calcSopPsyFacade::findStatementOfParticipance, hospitalInfo);
            case CBD:
                DrgCalcBasics drgCalcBasics = _calcDrgFacade.findCalcBasicsDrg(hospitalInfo.getId());
                exportCalcBasics(drgCalcBasics.getDataYear(), drgCalcBasics.getIk(), drgCalcBasics.getId(), "KGL");
                return "";
            case CBP:
                PeppCalcBasics peppCalcBasics = _calcPsyFacade.findCalcBasicsPepp(hospitalInfo.getId());
                exportCalcBasics(peppCalcBasics.getDataYear(), peppCalcBasics.getIk(), peppCalcBasics.getId(), "KGP");
                return "";
            case CBA:
                return printData(_calcAutopsyFacade::findCalcBasicsAutopsy, hospitalInfo);
            case CDM:
                return printData(_distModelFacade::findDistributionModel, hospitalInfo);
            default:
                throw new IllegalArgumentException("Unknown calcInfoType: " + hospitalInfo.getType());
        }
    }

    private <T> String printData(Function<Integer, T> findData, CalcHospitalInfo hospitalInfo) {
        T data = findData.apply(hospitalInfo.getId());
        List<KeyValueLevel> documentation = DocumentationUtil.getDocumentation(data);
        Utils.getFlash().put("headLine", Utils.getMessage("nameCALCULATION_HOSPITAL"));
        Utils.getFlash().put("targetPage", Pages.CalculationHospitalSummary.URL());
        Utils.getFlash().put("printContent", documentation);
        return Pages.PrintView.URL();
    }

    @Inject
    private ReportController _reportController;

    private void exportCalcBasics(int dataYear, int ik, int id, String type) {
        List<ReportTemplate> reports = retrieveReportTemplates(type, dataYear);

        File zipFile = new File("Export_" + ik + ".zip");

        try (FileOutputStream fileOut = new FileOutputStream(zipFile);
             CheckedOutputStream checkedOut = new CheckedOutputStream(fileOut, new Adler32());
             ZipOutputStream compressedOut = new ZipOutputStream(new BufferedOutputStream(checkedOut))) {
            for (ReportTemplate rt : reports) {
                String path = rt.getAddress()
                        .replace("{hostName}", _appTools.readConfig(ConfigKey.ReportHostName))
                        .replace("{hostNameInek}", _appTools.readConfig(ConfigKey.InekReportHostName))
                        .replace("{hostNameCombit}", _appTools.readConfig(ConfigKey.CombitReportHostName))
                        .replace("{0}", id + "")
                        .replace("{1}", URLEncoder.
                                encode(_appTools.retrieveHospitalInfo(ik), "UTF-8"))
                        .replace("{2}", dataYear + "");
                compressedOut.putNextEntry(new ZipEntry(rt.getName()));
                ByteArrayInputStream ips = new ByteArrayInputStream(_reportController.getSingleDocument(path));
                StreamHelper.copyStream(ips, compressedOut);
                compressedOut.closeEntry();
                compressedOut.flush();
            }
        } catch (IOException ex) {
        }
        try {
            InputStream is = new FileInputStream(zipFile);
            Utils.downLoadDocument(is, "Export_" + ik + ".zip", 0);
        } catch (IOException ex) {
        }
    }

    private List<ReportTemplate> retrieveReportTemplates(String name, int dataYear) {
        List<ReportTemplate> reports = _reportController.getReportTemplates(name + dataYear);
        if (reports.isEmpty()) {
            reports = _reportController.getReportTemplates(name);
        }
        return reports;
    }

    public String deleteHospitalInfo(CalcHospitalInfo hospitalInfo) {
        switch (hospitalInfo.getType()) {
            case SOP:
                deleteData(_calcSopPsyFacade::findStatementOfParticipance,
                        _calcSopPsyFacade::saveStatementOfParticipance,
                        _calcSopPsyFacade::delete,
                        hospitalInfo);
                break;
            case CBD:
                deleteData(_calcDrgFacade::findCalcBasicsDrg,
                        _calcDrgFacade::saveCalcBasicsDrg,
                        _calcDrgFacade::delete,
                        hospitalInfo);
                break;
            case CBP:
                deleteData(_calcPsyFacade::findCalcBasicsPepp,
                        _calcPsyFacade::saveCalcBasicsPepp,
                        _calcPsyFacade::delete,
                        hospitalInfo);
                break;
            case CBA:
                deleteData(_calcAutopsyFacade::findCalcBasicsAutopsy,
                        _calcAutopsyFacade::saveCalcBasicsAutopsy,
                        _calcAutopsyFacade::delete,
                        hospitalInfo);
                break;
            case CDM:
                deleteData(_distModelFacade::findDistributionModel,
                        _distModelFacade::saveDistributionModel,
                        _distModelFacade::delete,
                        hospitalInfo);
                break;
            default:
                throw new IllegalArgumentException("Unknown calcInfoType: " + hospitalInfo.getType());
        }
        return "";
    }

    private <T> void deleteData(
            Function<Integer, T> findData,
            Function<T, T> saveData,
            Consumer<T> deleteData,
            CalcHospitalInfo hospitalInfo) {
        T data = findData.apply(hospitalInfo.getId());
        if (data == null) {
            // might be deleted by somebody else
            return;
        }
        StatusEntity entity = (StatusEntity) data;
        if (entity.getStatus().getId() >= WorkflowStatus.Provided.getId()) {
            entity.setStatus(WorkflowStatus.Retired);
            saveData.apply(data);
        } else {
            deleteData.accept(data);
        }
    }

    public String editHospitalInfo(CalcInfoType type) {
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

}
