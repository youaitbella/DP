/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLOpAn", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLOpAn.findAll", query = "SELECT k FROM KGLOpAn k")})
public class KGLOpAn implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="baseInformationId">
    @Id
    @Column(name = "oaBaseInformationID")
    private int _baseInformationID;

    public int getBaseInformationID() {
        return _baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this._baseInformationID = baseInformationID;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="centralOPCnt">    
    @Column(name = "oaCentralOPCnt")
    private int _centralOPCnt;

    public int getCentralOPCnt() {
        return _centralOPCnt;
    }

    public void setCentralOPCnt(int centralOPCnt) {
        this._centralOPCnt = centralOPCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="staffBindingMsOP">    
    @Column(name = "oaStaffBindingMsOP")
    private boolean _staffBindingMsOP;

    public boolean getStaffBindingMsOP() {
        return _staffBindingMsOP;
    }

    public void setStaffBindingMsOP(boolean staffBindingMsOP) {
        this._staffBindingMsOP = staffBindingMsOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="staffBindingFsOP">    
    @Column(name = "oaStaffBindingFsOP")
    private boolean _staffBindingFsOP;

    public boolean getStaffBindingFsOP() {
        return _staffBindingFsOP;
    }

    public void setStaffBindingFsOP(boolean staffBindingFsOP) {
        this._staffBindingFsOP = staffBindingFsOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="medicalServiceSnzOP">    
    @Column(name = "oaMedicalServiceSnzOP")
    private int _medicalServiceSnzOP;

    public int getMedicalServiceSnzOP() {
        return _medicalServiceSnzOP;
    }

    public void setMedicalServiceSnzOP(int medicalServiceSnzOP) {
        this._medicalServiceSnzOP = medicalServiceSnzOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="functionalServiceSnzOP">    
    @Column(name = "oaFunctionalServiceSnzOP")
    private int _functionalServiceSnzOP;

    public int getFunctionalServiceSnzOP() {
        return _functionalServiceSnzOP;
    }

    public void setFunctionalServiceSnzOP(int functionalServiceSnzOP) {
        this._functionalServiceSnzOP = functionalServiceSnzOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="descriptionSnzOP">    
    @Size(max = 2147483647)
    @Column(name = "oaDescriptionSnzOP")
    private String _descriptionSnzOP = "";

    public String getDescriptionSnzOP() {
        return _descriptionSnzOP;
    }

    public void setDescriptionSnzOP(String descriptionSnzOP) {
        this._descriptionSnzOP = descriptionSnzOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="medicalServiceRzOP">    
    @Column(name = "oaMedicalServiceRzOP")
    private int _medicalServiceRzOP;

    public int getMedicalServiceRzOP() {
        return _medicalServiceRzOP;
    }

    public void setMedicalServiceRzOP(int medicalServiceRzOP) {
        this._medicalServiceRzOP = medicalServiceRzOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="functionalServiceRzOP">    
    @Column(name = "oaFunctionalServiceRzOP")
    private int _functionalServiceRzOP;

    public int getFunctionalServiceRzOP() {
        return _functionalServiceRzOP;
    }

    public void setFunctionalServiceRzOP(int functionalServiceRzOP) {
        this._functionalServiceRzOP = functionalServiceRzOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="descriptionRzOP">    
    @Size(max = 2147483647)
    @Column(name = "oaDescriptionRzOP")
    private String _descriptionRzOP = "";

    public String getDescriptionRzOP() {
        return _descriptionRzOP;
    }

    public void setDescriptionRzOP(String descriptionRzOP) {
        this._descriptionRzOP = descriptionRzOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="medicalServiceAmountOP">    
    @Column(name = "oaMedicalServiceAmountOP")
    private double _medicalServiceAmountOP;

    public double getMedicalServiceAmountOP() {
        return _medicalServiceAmountOP;
    }

    public void setMedicalServiceAmountOP(double medicalServiceAmountOP) {
        this._medicalServiceAmountOP = medicalServiceAmountOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="functionalServiceAmountOP">    
    @Column(name = "oaFunctionalServiceAmountOP")
    private double _functionalServiceAmountOP;

    public double getFunctionalServiceAmountOP() {
        return _functionalServiceAmountOP;
    }

    public void setFunctionalServiceAmountOP(double functionalServiceAmountOP) {
        this._functionalServiceAmountOP = functionalServiceAmountOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="staffBindingMsAN">    
    @Column(name = "oaStaffBindingMsAN")
    private boolean _staffBindingMsAN;

    public boolean getStaffBindingMsAN() {
        return _staffBindingMsAN;
    }

    public void setStaffBindingMsAN(boolean staffBindingMsAN) {
        this._staffBindingMsAN = staffBindingMsAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="staffBindingFsAN">    
    @Column(name = "oaStaffBindingFsAN")
    private boolean _staffBindingFsAN;

    public boolean getStaffBindingFsAN() {
        return _staffBindingFsAN;
    }

    public void setStaffBindingFsAN(boolean staffBindingFsAN) {
        this._staffBindingFsAN = staffBindingFsAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="medicalServiceSnzAN">    
    @Column(name = "oaMedicalServiceSnzAN")
    private int _medicalServiceSnzAN;

    public int getMedicalServiceSnzAN() {
        return _medicalServiceSnzAN;
    }

    public void setMedicalServiceSnzAN(int medicalServiceSnzAN) {
        this._medicalServiceSnzAN = medicalServiceSnzAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="functionalServiceSnzAN">    
    @Column(name = "oaFunctionalServiceSnzAN")
    private int _functionalServiceSnzAN;

    public int getFunctionalServiceSnzAN() {
        return _functionalServiceSnzAN;
    }

    public void setFunctionalServiceSnzAN(int functionalServiceSnzAN) {
        this._functionalServiceSnzAN = functionalServiceSnzAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="descriptionSnzAN">    
    @Size(max = 2147483647)
    @Column(name = "oaDescriptionSnzAN")
    private String _descriptionSnzAN = "";

    public String getDescriptionSnzAN() {
        return _descriptionSnzAN;
    }

    public void setDescriptionSnzAN(String descriptionSnzAN) {
        this._descriptionSnzAN = descriptionSnzAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="medicalServiceRzAN">    
    @Column(name = "oaMedicalServiceRzAN")
    private int _medicalServiceRzAN;

    public int getMedicalServiceRzAN() {
        return _medicalServiceRzAN;
    }

    public void setMedicalServiceRzAN(int medicalServiceRzAN) {
        this._medicalServiceRzAN = medicalServiceRzAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="functionalServiceRzAN">    
    @Column(name = "oaFunctionalServiceRzAN")
    private int _functionalServiceRzAN;

    public int getFunctionalServiceRzAN() {
        return _functionalServiceRzAN;
    }

    public void setFunctionalServiceRzAN(int functionalServiceRzAN) {
        this._functionalServiceRzAN = functionalServiceRzAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="descriptionRzAN">    
    @Size(max = 2147483647)
    @Column(name = "oaDescriptionRzAN")
    private String _descriptionRzAN = "";

    public String getDescriptionRzAN() {
        return _descriptionRzAN;
    }

    public void setDescriptionRzAN(String descriptionRzAN) {
        this._descriptionRzAN = descriptionRzAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="medicalServiceAmountAN">    
    @Column(name = "oaMedicalServiceAmountAN")
    private double _medicalServiceAmountAN;

    public double getMedicalServiceAmountAN() {
        return _medicalServiceAmountAN;
    }

    public void setMedicalServiceAmountAN(double medicalServiceAmountAN) {
        this._medicalServiceAmountAN = medicalServiceAmountAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="functionalServiceAmountAN">    
    @Column(name = "oaFunctionalServiceAmountAN")
    private double _functionalServiceAmountAN;

    public double getFunctionalServiceAmountAN() {
        return _functionalServiceAmountAN;
    }

    public void setFunctionalServiceAmountAN(double functionalServiceAmountAN) {
        this._functionalServiceAmountAN = functionalServiceAmountAN;
    }
    //</editor-fold>

    public KGLOpAn() {
    }

    public KGLOpAn(Integer oaBaseInformationID) {
        this._baseInformationID = oaBaseInformationID;
    }

    public KGLOpAn(Integer oaBaseInformationID, int oaCentralOPCnt, boolean oaStaffBindingMsOP, boolean oaStaffBindingFsOP, int oaMedicalServiceSnzOP, int oaFunctionalServiceSnzOP, String oaDescriptionSnzOP, int oaMedicalServiceRzOP, int oaFunctionalServiceRzOP, String oaDescriptionRzOP, double oaMedicalServiceAmountOP, double oaFunctionalServiceAmountOP, boolean oaStaffBindingMsAN, boolean oaStaffBindingFsAN, int oaMedicalServiceSnzAN, int oaFunctionalServiceSnzAN, String oaDescriptionSnzAN, int oaMedicalServiceRzAN, int oaFunctionalServiceRzAN, String oaDescriptionRzAN, double oaMedicalServiceAmountAN, double oaFunctionalServiceAmountAN) {
        this._baseInformationID = oaBaseInformationID;
        this._centralOPCnt = oaCentralOPCnt;
        this._staffBindingMsOP = oaStaffBindingMsOP;
        this._staffBindingFsOP = oaStaffBindingFsOP;
        this._medicalServiceSnzOP = oaMedicalServiceSnzOP;
        this._functionalServiceSnzOP = oaFunctionalServiceSnzOP;
        this._descriptionSnzOP = oaDescriptionSnzOP;
        this._medicalServiceRzOP = oaMedicalServiceRzOP;
        this._functionalServiceRzOP = oaFunctionalServiceRzOP;
        this._descriptionRzOP = oaDescriptionRzOP;
        this._medicalServiceAmountOP = oaMedicalServiceAmountOP;
        this._functionalServiceAmountOP = oaFunctionalServiceAmountOP;
        this._staffBindingMsAN = oaStaffBindingMsAN;
        this._staffBindingFsAN = oaStaffBindingFsAN;
        this._medicalServiceSnzAN = oaMedicalServiceSnzAN;
        this._functionalServiceSnzAN = oaFunctionalServiceSnzAN;
        this._descriptionSnzAN = oaDescriptionSnzAN;
        this._medicalServiceRzAN = oaMedicalServiceRzAN;
        this._functionalServiceRzAN = oaFunctionalServiceRzAN;
        this._descriptionRzAN = oaDescriptionRzAN;
        this._medicalServiceAmountAN = oaMedicalServiceAmountAN;
        this._functionalServiceAmountAN = oaFunctionalServiceAmountAN;
    }

    @Override
    public int hashCode() {
        return _baseInformationID;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLOpAn)) {
            return false;
        }
        KGLOpAn other = (KGLOpAn) object;
        return this._baseInformationID == other._baseInformationID;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLOpAn[ oaBaseInformationID=" + _baseInformationID + " ]";
    }

}
