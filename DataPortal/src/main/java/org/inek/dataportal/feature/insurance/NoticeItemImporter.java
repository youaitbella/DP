/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.insurance;

import java.math.BigDecimal;
import java.util.Optional;
import javax.inject.Inject;
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

    private InsuranceNubNotice _notice;

    void setNotice(InsuranceNubNotice notice) {
        _notice = notice;
    }

    public boolean containsError() {
        return !_errorMsg.isEmpty();
    }

    public String getMessage() {
        return _totalCount + " Zeilen gelesen\r\n\r\n" + _errorMsg;
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
        }
    }

    private void tryImportRequestId(InsuranceNubNoticeItem item, String dataString) {
        boolean isRequestId = dataString.startsWith("N");
        if (isRequestId) {
            dataString = dataString.substring(1);
        }
        try {
            int number = Integer.parseInt(dataString);
            // todo: check whether requstId exist for given IK or retrieve request id for given seqNo
            item.setNubRequestId(number);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(Utils.getMessage("msgMissingIdOrSequence"));
        }
    }

    private void tryImportDosageForm(InsuranceNubNoticeItem item, String string) {
        Optional<DosageForm> dosageFormOpt = _insuranceFacade.getDosageForm(string);
        if (dosageFormOpt.isPresent()) {
            item.setDosageFormId(dosageFormOpt.get().getId());
        } else {
            throw new IllegalArgumentException(Utils.getMessage("msgUnknowDosageForm") + ": " + string);
        }
    }

    private void tryImportAmount(InsuranceNubNoticeItem item, String string) {
        try {
            BigDecimal amount = new BigDecimal(string);
            item.setAmount(amount);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(Utils.getMessage("msgNotANumber") + ": " + string);
        }
    }

    private void tryImportUnit(InsuranceNubNoticeItem item, String string) {
        Optional<Unit> unitOpt = _insuranceFacade.getUnit(string);
        if (unitOpt.isPresent()) {
            item.setUnitId(unitOpt.get().getId());
        } else {
            throw new IllegalArgumentException(Utils.getMessage("msgUnknownUnit") + ": " + string);
        }
    }

    private void tryImportQuantity(InsuranceNubNoticeItem item, String string) {
        try {
            int quantity = Integer.parseInt(string);
            item.setQuantity(quantity);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(Utils.getMessage("msgNotANumber") + ": " + string);
        }
    }

    private void tryImportPrice(InsuranceNubNoticeItem item, String string) {
        try {
            BigDecimal price = new BigDecimal(string);
            item.setPrice(price);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(Utils.getMessage("msgNotANumber") + ": " + string);
        }
    }

    private void tryImportRemunerationKey(InsuranceNubNoticeItem item, String string) {
        // todo: check and get id
        item.setRemunerationTypeId(44109024);
    }

    private void tryImportProcedures(InsuranceNubNoticeItem item, String string) {
        // todo: check existance of opCodes
        item.setProcedures(string);
    }

}
