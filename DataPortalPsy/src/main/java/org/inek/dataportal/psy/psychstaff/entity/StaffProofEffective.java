/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.psy.psychstaff.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.inek.dataportal.psy.psychstaff.enums.PsychType;
import org.inek.dataportal.common.data.KhComparison.entities.OccupationalCategory;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "StaffProofEffective", schema = "psy")
public class StaffProofEffective implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "speId")
    private int _id;

    public StaffProofEffective() {
        this._psychType = PsychType.Unknown;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StaffProofMasterId">
    @Column(name = "speStaffProofMasterId")
    private int _staffProofMasterId;

    public int getStaffProofMasterId() {
        return _staffProofMasterId;
    }

    public void setStaffProofMasterId(int staffProofMasterId) {
        _staffProofMasterId = staffProofMasterId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property OccupationalCategoryId">
    @Column(name = "speOccupationalCategoryId")
    private int _occupationalCategoryId;

    public int getOccupationalCategoryId() {
        return _occupationalCategoryId;
    }

    public void setOccupationalCategoryId(int occupationalCategoryId) {
        _occupationalCategoryId = occupationalCategoryId;
    }

    @OneToOne()
    @PrimaryKeyJoinColumn(name = "speOccupationalCategoryId")
    private OccupationalCategory _occupationalCategory;

    public OccupationalCategory getOccupationalCategory() {
        return _occupationalCategory;
    }

    public void setOccupationalCategory(OccupationalCategory occupationalCategory) {
        _occupationalCategory = occupationalCategory;
        _occupationalCategoryId = occupationalCategory.getId();
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PsychType">
    @Column(name = "spePsychType")
    private PsychType _psychType;

    public PsychType getPsychType() {
        return _psychType;
    }

    public void setPsychType(PsychType psychType) {
        _psychType = psychType;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StaffingComplete">
    @Column(name = "speStaffingComplete")
    private double _staffingComplete;

    public double getStaffingComplete() {
        return _staffingComplete;
    }

    public void setStaffingComplete(double staffingComplete) {
        _staffingComplete = staffingComplete;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StaffingDeductionPsych">
    @Column(name = "speStaffingDeductionPsych")
    private double _staffingDeductionPsych;

    public double getStaffingDeductionPsych() {
        return _staffingDeductionPsych;
    }

    public void setStaffingDeductionPsych(double staffingDeductionPsych) {
        _staffingDeductionPsych = staffingDeductionPsych;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StaffingDeductionNonPsych">
    @Column(name = "speStaffingDeductionNonPsych")
    private double _staffingDeductionNonPsych;

    public double getStaffingDeductionNonPsych() {
        return _staffingDeductionNonPsych;
    }

    public void setStaffingDeductionNonPsych(double staffingDeductionNonPsych) {
        _staffingDeductionNonPsych = staffingDeductionNonPsych;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StaffingDeductionOther">
    @Column(name = "speStaffingDeductionOther")
    private double _staffingDeductionOther;

    public double getStaffingDeductionOther() {
        return _staffingDeductionOther;
    }

    public void setStaffingDeductionOther(double staffingDeductionOther) {
        _staffingDeductionOther = staffingDeductionOther;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="hashCode, equals, toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof StaffProofEffective)) {
            return false;
        }
        StaffProofEffective other = (StaffProofEffective) obj;

        return _id == other._id;
    }

    @Override
    public String toString() {
        return StaffProofEffective.class.getName() + " ID [" + _id + "]";
    }
    //</editor-fold>

}
