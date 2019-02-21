/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.drg;

import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;
import org.inek.dataportal.common.utils.IgnoreOnCompare;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListCostCenterOpAn", schema = "calc")
public class KGLListCostCenterOpAn implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    public KGLListCostCenterOpAn() {
    }

    // <editor-fold defaultstate="collapsed" desc="id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "oaId", updatable = false, nullable = false)
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BaseInformationId">
    @Column(name = "oaBaseInformationId")
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="oaRunningNumber">
    @Column(name = "oaRunningNumber")
    private int _runningNumber;

    public int getRunningNumber() {
        return _runningNumber;
    }

    public void setRunningNumber(int runningNumber) {
        this._runningNumber = runningNumber;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="oaCostCenterNumber">
    @Column(name = "oaCostCenterNumber")
    private String _costCenterNumber = "";

    public String getCostCenterNumber() {
        return _costCenterNumber;
    }

    public void setCostCenterNumber(String costCenterNumber) {
        this._costCenterNumber = costCenterNumber;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CostCenterText">
    @Column(name = "oaCostCenterText")
    private String _costCenterText = "";

    public String getCostCenterText() {
        return _costCenterText;
    }

    public void setCostCenterText(String costCenterText) {
        this._costCenterText = costCenterText;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Location">
    @Column(name = "oaLocation")
    private String _location = "";

    public String getLocation() {
        return _costCenterText;
    }

    public void setLocation(String location) {
        this._costCenterText = location;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="CountMedStaff">
    @Column(name = "oaCountMedStaff")
    private double _countMedStaff;

    public double getCountMedStaff() {
        return _countMedStaff;
    }

    public void setCountMedStaff(double countMedStaff) {
        this._countMedStaff = countMedStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="oaCostVolumeMedStaff">
    @Column(name = "oaCostVolumeMedStaff")
    private double _costVolumeMedStaff;

    public double getCostVolumeMedStaff() {
        return _costVolumeMedStaff;
    }

    public void setCostVolumeMedStaff(double costVolumeMedStaff) {
        this._costVolumeMedStaff = costVolumeMedStaff;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="oaCountFunctionalService">
    @Column(name = "oaCountFunctionalService")
    private double _countFunctionalService;

    public double getCountFunctionalService() {
        return _countFunctionalService;
    }

    public void setCountFunctionalService(double countFunctionalService) {
        this._countFunctionalService = countFunctionalService;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="oaCostVolumeFunctionalService">
    @Column(name = "oaCostVolumeFunctionalService")
    private double _costVolumeFunctionalService;

    public double getCostVolumeFunctionalService() {
        return _costVolumeFunctionalService;
    }

    public void setCostVolumeFunctionalService(double costVolumeFunctionalService) {
        this._costVolumeFunctionalService = costVolumeFunctionalService;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="oaServiceTime">
    @Column(name = "oaServiceTime")
    private double _serviceTime;

    public double getServiceTime() {
        return _serviceTime;
    }

    public void setServiceTime(double serviceTime) {
        this._serviceTime = serviceTime;
    }
    // </editor-fold>


    //<editor-fold defaultstate="collapsed" desc="hash && equals && toString">

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KGLListCostCenterOpAn that = (KGLListCostCenterOpAn) o;
        return _id == that._id &&
                _baseInformationId == that._baseInformationId &&
                _runningNumber == that._runningNumber &&
                Double.compare(that._countMedStaff, _countMedStaff) == 0 &&
                Double.compare(that._costVolumeMedStaff, _costVolumeMedStaff) == 0 &&
                Double.compare(that._countFunctionalService, _countFunctionalService) == 0 &&
                Double.compare(that._costVolumeFunctionalService, _costVolumeFunctionalService) == 0 &&
                Double.compare(that._serviceTime, _serviceTime) == 0 &&
                Objects.equals(_costCenterNumber, that._costCenterNumber) &&
                Objects.equals(_costCenterText, that._costCenterText) &&
                Objects.equals(_location, that._location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _baseInformationId, _runningNumber, _costCenterNumber, _costCenterText, _location,
                _countMedStaff, _costVolumeMedStaff, _countFunctionalService, _costVolumeFunctionalService, _serviceTime);
    }

    //</editor-fold>

}
