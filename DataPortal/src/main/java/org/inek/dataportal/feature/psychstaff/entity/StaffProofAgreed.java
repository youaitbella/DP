/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.psychstaff.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.inek.dataportal.feature.psychstaff.enums.PsychType;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "StaffProofAgreed", schema = "psy")
public class StaffProofAgreed implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spaId")
    private int _id;

    public StaffProofAgreed() {
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
    @Column(name = "spaStaffProofMasterId")
    private int _staffProofMasterId;
    
    public int getStaffProofMasterId() {
        return _staffProofMasterId;
    }
    
    public void setStaffProofMasterId(int staffProofMasterId) {
        _staffProofMasterId = staffProofMasterId;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property OccupationalCatagoryId">
    @OneToOne()
    @PrimaryKeyJoinColumn(name = "spaOccupationalCatagory")
    private OccupationalCatagory _occupationalCatagory;
    
    public OccupationalCatagory getOccupationalCatagory() {
        return _occupationalCatagory;
    }
    
    public void setOccupationalCatagory(OccupationalCatagory occupationalCatagory) {
        _occupationalCatagory = occupationalCatagory;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PsychType">
    @Column(name = "spaPsychType")
    private PsychType _psychType;

    public PsychType getPsychType() {
        return _psychType;
    }

    public void setPsychType(PsychType psychType) {
        _psychType = psychType;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property StaffingComplete">
    @Column(name = "spaStaffingComplete")
    private double _staffingComplete;
    
    public double getStaffingComplete() {
        return _staffingComplete;
    }
    
    public void setStaffingComplete(double staffingComplete) {
        _staffingComplete = staffingComplete;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property StaffingBudget">
    @Column(name = "spaStaffingBudget")
    private double _staffingBudget;
    
    public double getStaffingBudget() {
        return _staffingBudget;
    }
    
    public void setStaffingBudget(double staffingBudget) {
        _staffingBudget = staffingBudget;
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Property AvgCost">
    @Column(name = "spaAvgCost")
    private double _avgCost;
    
    public double getAvgCost() {
        return _avgCost;
    }
    
    public void setAvgCost(double avgCost) {
        _avgCost = avgCost;
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
        if (!(obj instanceof StaffProofAgreed)) {
            return false;
        }
        StaffProofAgreed other = (StaffProofAgreed) obj;

        return _id == other._id;
    }
    
    @Override
    public String toString() {
        return StaffProofAgreed.class.getName() + " ID [" + _id + "]";
    }
    //</editor-fold>
    
}
