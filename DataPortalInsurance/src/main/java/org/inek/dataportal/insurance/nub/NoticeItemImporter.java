/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.insurance.nub;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;
import org.inek.dataportal.common.data.access.ProcedureFacade;
import org.inek.dataportal.common.data.common.RemunerationType;
import org.inek.dataportal.insurance.nub.entities.InsuranceNubNotice;
import org.inek.dataportal.insurance.nub.entities.InsuranceNubNoticeItem;
import org.inek.dataportal.insurance.nub.entities.Unit;
import org.inek.dataportal.insurance.facade.InsuranceFacade;
import org.inek.dataportal.common.helper.Utils;
import org.inek.dataportal.common.utils.StringUtil;

/**
 *
 * @author muellermi
 */
public class NoticeItemImporter {

    @Inject private InsuranceFacade _insuranceFacade;
    @Inject private ProcedureFacade _procedureFacade;

    private String _errorMsg = "";
    private int _totalCount = 0;

    public int getTotalCount() {
        return _totalCount;
    }

    private int _errorCount = 0;

    public int getErrorCount() {
        return _errorCount;
    }

    private InsuranceNubNotice _notice;

    void setNotice(InsuranceNubNotice notice) {
        _notice = notice;
    }

    public boolean containsError() {
        return !_errorMsg.isEmpty();
    }

    public String getMessage() {
        return (_totalCount - _errorCount) + " von " + _totalCount + " Zeilen gelesen\r\n\r\n" + _errorMsg;
    }

    public void tryImportLine(String line) {
        _totalCount++;
        try {
            if (line.endsWith(";")) {
                line = line + " ";
            }
            String[] data = StringUtil.splitAtUnquotedSemicolon(line);
            if (data.length != 9) {
                throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
            }
            InsuranceNubNoticeItem item = new InsuranceNubNoticeItem();
            item.setInsuranceNubNoticeId(_notice.getId());
            tryImportRequestId(item, data[0]);
            tryImportDosageForm(item, data[1]);
            tryImportAmount(item, data[2]);
            tryImportUnit(item, data[3]);
            tryImportQuantity(item, data[4]);
            tryImportPrice(item, data[5]);
            tryImportRemunerationKey(item, data[6]);
            tryImportProcedures(item, data[7]);
            item.setNote(data[8]);
//            if (_notice.getId() > 0) {
//                item.setInsuranceNubNoticeId(_notice.getId());
//            }
            _notice.getItems().add(item);
        } catch (IllegalArgumentException ex) {
            _errorMsg += "\r\nFehler in Zeile " + _totalCount + ": " + ex.getMessage();
            _errorCount++;
        }
    }

    private void tryImportRequestId(InsuranceNubNoticeItem item, String dataString) {
        boolean isRequestId = dataString.startsWith("N");
        if (isRequestId) {
            dataString = dataString.substring(1);
        }
        try {
            int number = Integer.parseInt(dataString);
            if (isRequestId && !_insuranceFacade.existsNubRequest(number, _notice.getHospitalIk(), _notice.getYear())) {
                throw new IllegalArgumentException("Verfahrensnummer " + dataString + " existiert nicht für IK " 
                        + _notice.getHospitalIk() + " in Jahr " + _notice.getYear());
            }
            if (!isRequestId) {
                number = _insuranceFacade.retrieveRequestId(number, _notice.getHospitalIk(), _notice.getYear());
                if (number < 0) {
                    throw new IllegalArgumentException("Zur lfd. Nummmer " + dataString + " existiert keine Anfrage für IK " 
                            + _notice.getHospitalIk() + " in Jahr " + _notice.getYear());
                }
            }
            item.setNubRequestId(number);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(Utils.getMessage("msgMissingIdOrSequence"));
        }
    }

    private Map<String, Integer> dosageCache = new HashMap<>();
    private void tryImportDosageForm(InsuranceNubNoticeItem item, String dataString) {
        if (dataString.trim().isEmpty()){
            item.setDosageFormId(0);
            return;
        }
        if (dosageCache.containsKey(dataString)){
            item.setDosageFormId(dosageCache.get(dataString));
        }
        Optional<Integer> dosageFormOpt = _insuranceFacade.getDosageFormId(dataString);
        if (dosageFormOpt.isPresent()) {
            dosageCache.put(dataString, dosageFormOpt.get());
            item.setDosageFormId(dosageFormOpt.get());
        } else {
            throw new IllegalArgumentException(Utils.getMessage("msgUnknowDosageForm") + ": " + dataString);
        }
    }

    private void tryImportAmount(InsuranceNubNoticeItem item, String dataString) {
        if (dataString.trim().isEmpty()){
            // this field is optional. if empty default to 1
            dataString = "1";
        }
        try {
            BigDecimal amount = parseDecimal(dataString);
            item.setAmount(amount);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("[" +Utils.getMessage("lblAmount") + "] " + Utils.getMessage("msgNotANumber") + ": " + dataString);
        }
    }

    private BigDecimal parseDecimal(String value) throws ParseException {
        DecimalFormat df = (DecimalFormat) NumberFormat.getInstance(Locale.GERMAN);
        df.setParseBigDecimal(true);
        return (BigDecimal) df.parseObject(value);
    }

    private void tryImportUnit(InsuranceNubNoticeItem item, String dataString) {
        if (dataString.trim().isEmpty()){
            item.setUnitId(0);
            return;
        }
        Optional<Unit> unitOpt = _insuranceFacade.getUnit(dataString);
        if (unitOpt.isPresent()) {
            item.setUnitId(unitOpt.get().getId());
        } else {
            throw new IllegalArgumentException(Utils.getMessage("msgUnknownUnit") + ": " + dataString);
        }
    }

    private void tryImportQuantity(InsuranceNubNoticeItem item, String dataString) {
        if (dataString.trim().isEmpty()){
            // if empty default to 1
            dataString = "1";
        }
        try {
            NumberFormat nf = NumberFormat.getInstance(Locale.GERMAN);
            nf.setParseIntegerOnly(true);
            int quantity = (int)(long)nf.parse(dataString);
            item.setQuantity(quantity);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("[" + Utils.getMessage("lblCount") + "] " + Utils.getMessage("msgNotANumber") + ": " + dataString);
        }
    }

    private void tryImportPrice(InsuranceNubNoticeItem item, String dataString) {
        try {
            BigDecimal price = parseDecimal(dataString);
            item.setPrice(price);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("[" +Utils.getMessage("lblPayment") + "] " + Utils.getMessage("msgNotANumber") + ": " + dataString);
        }
    }

    private void tryImportRemunerationKey(InsuranceNubNoticeItem item, String dataString) {
        if (dataString.length() != 8) {
            throw new IllegalArgumentException(Utils.getMessage("msgInvalidRemunerationKey") + ": " + dataString);
        }
        Optional<RemunerationType> remunTypeOpt = _insuranceFacade.getRemunerationType(dataString);
        if (!remunTypeOpt.isPresent()) {
            _errorMsg += "\r\nHinweis in Zeile " + _totalCount + ": Handelt es sich bei " + dataString + " um einen gültigen Entgeltschlüssel?";
        }
        item.setRemunerationTypeCharId(dataString);
    }

    private void tryImportProcedures(InsuranceNubNoticeItem item, String dataString) {
        int targetYear = _notice.getYear();
        String invalidCodes = _procedureFacade.checkProcedures(dataString, targetYear, targetYear, "\\s|,|\\+");
        if (invalidCodes.length() > 0) {
            throw new IllegalArgumentException(invalidCodes);
        }
        item.setProcedures(dataString);
    }
}
