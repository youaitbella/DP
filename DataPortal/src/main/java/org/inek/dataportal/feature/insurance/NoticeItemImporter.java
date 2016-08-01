/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.insurance;

import java.math.BigDecimal;
import java.util.Optional;
import javax.inject.Inject;
import org.inek.dataportal.entities.common.RemunerationType;
import org.inek.dataportal.entities.insurance.DosageForm;
import org.inek.dataportal.entities.insurance.InsuranceNubNotice;
import org.inek.dataportal.entities.insurance.InsuranceNubNoticeItem;
import org.inek.dataportal.entities.insurance.Unit;
import org.inek.dataportal.facades.InsuranceFacade;
import org.inek.dataportal.helper.Utils;

/**
 *
 * @author muellermi
 */
public class NoticeItemImporter {

    @Inject private InsuranceFacade _insuranceFacade;

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
            String[] data = line.split(";");
            if (data.length != 9) {
                throw new IllegalArgumentException(Utils.getMessage("msgWrongElementCount"));
            }
            InsuranceNubNoticeItem item = new InsuranceNubNoticeItem();
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
                throw new IllegalArgumentException("Verfahrensnummer " + dataString + " existiert nicht für IK " + _notice.getHospitalIk() + " in Jahr " + _notice.getYear());
            }
            if (!isRequestId) {
                number = _insuranceFacade.retrieveRequestId(number, _notice.getHospitalIk(), _notice.getYear());
                if (number < 0) {
                    throw new IllegalArgumentException("Zur lfd. Nummmer " + dataString + " existiert keine Anfrage für IK " + _notice.getHospitalIk() + " in Jahr " + _notice.getYear());
                }
            }
            item.setNubRequestId(number);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(Utils.getMessage("msgMissingIdOrSequence"));
        }
    }

    private void tryImportDosageForm(InsuranceNubNoticeItem item, String dataString) {
        Optional<DosageForm> dosageFormOpt = _insuranceFacade.getDosageForm(dataString);
        if (dosageFormOpt.isPresent()) {
            item.setDosageFormId(dosageFormOpt.get().getId());
        } else {
            throw new IllegalArgumentException(Utils.getMessage("msgUnknowDosageForm") + ": " + dataString);
        }
    }

    private void tryImportAmount(InsuranceNubNoticeItem item, String dataString) {
        try {
            BigDecimal amount = new BigDecimal(dataString);
            item.setAmount(amount);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(Utils.getMessage("msgNotANumber") + ": " + dataString);
        }
    }

    private void tryImportUnit(InsuranceNubNoticeItem item, String dataString) {
        Optional<Unit> unitOpt = _insuranceFacade.getUnit(dataString);
        if (unitOpt.isPresent()) {
            item.setUnitId(unitOpt.get().getId());
        } else {
            throw new IllegalArgumentException(Utils.getMessage("msgUnknownUnit") + ": " + dataString);
        }
    }

    private void tryImportQuantity(InsuranceNubNoticeItem item, String dataString) {
        try {
            int quantity = Integer.parseInt(dataString);
            item.setQuantity(quantity);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(Utils.getMessage("msgNotANumber") + ": " + dataString);
        }
    }

    private void tryImportPrice(InsuranceNubNoticeItem item, String dataString) {
        try {
            BigDecimal price = new BigDecimal(dataString);
            item.setPrice(price);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(Utils.getMessage("msgNotANumber") + ": " + dataString);
        }
    }

    private void tryImportRemunerationKey(InsuranceNubNoticeItem item, String dataString) {
        Optional<RemunerationType> remunTypeOpt = _insuranceFacade.getRemunerationType(dataString);
        if (remunTypeOpt.isPresent()) {
            //item.setRemunerationTypeId(remunTypeOpt.get().getId());
        } else {
            throw new IllegalArgumentException(Utils.getMessage("msgUnknownUnit") + ": " + dataString);
        }
    }

    private void tryImportProcedures(InsuranceNubNoticeItem item, String dataString) {
        // todo: check existance of opCodes
        item.setProcedures(dataString);
    }

}
