/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListTherapy", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPListTherapy.findAll", query = "SELECT k FROM KGPListTherapy k")})
public class KGPListTherapy implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "thID")
    private Integer thID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thCostCenterID")
    private int thCostCenterID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "thCostCenterText")
    private String thCostCenterText;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thExternalService")
    private int thExternalService;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "thKeyUsed")
    private String thKeyUsed;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thServiceUnitsCt1")
    private int thServiceUnitsCt1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thPersonalCostCt1")
    private int thPersonalCostCt1;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thServiceUnitsCt2")
    private int thServiceUnitsCt2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thPersonalCostCt2")
    private int thPersonalCostCt2;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thServiceUnitsCt3")
    private int thServiceUnitsCt3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thPersonalCostCt3")
    private int thPersonalCostCt3;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thServiceUnitsCt3a")
    private int thServiceUnitsCt3a;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thPersonalCostCt3a")
    private int thPersonalCostCt3a;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thServiceUnitsCt3b")
    private int thServiceUnitsCt3b;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thPersonalCostCt3b")
    private int thPersonalCostCt3b;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thServiceUnitsCt3c")
    private int thServiceUnitsCt3c;
    @Basic(optional = false)
    @NotNull
    @Column(name = "thPersonalCostCt3c")
    private int thPersonalCostCt3c;
    @JoinColumn(name = "thBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private PeppCalcBasics thBaseInformationID;

    public KGPListTherapy() {
    }

    public KGPListTherapy(Integer thID) {
        this.thID = thID;
    }

    public KGPListTherapy(Integer thID, int thCostCenterID, String thCostCenterText, int thExternalService, String thKeyUsed, int thServiceUnitsCt1, int thPersonalCostCt1, int thServiceUnitsCt2, int thPersonalCostCt2, int thServiceUnitsCt3, int thPersonalCostCt3, int thServiceUnitsCt3a, int thPersonalCostCt3a, int thServiceUnitsCt3b, int thPersonalCostCt3b, int thServiceUnitsCt3c, int thPersonalCostCt3c) {
        this.thID = thID;
        this.thCostCenterID = thCostCenterID;
        this.thCostCenterText = thCostCenterText;
        this.thExternalService = thExternalService;
        this.thKeyUsed = thKeyUsed;
        this.thServiceUnitsCt1 = thServiceUnitsCt1;
        this.thPersonalCostCt1 = thPersonalCostCt1;
        this.thServiceUnitsCt2 = thServiceUnitsCt2;
        this.thPersonalCostCt2 = thPersonalCostCt2;
        this.thServiceUnitsCt3 = thServiceUnitsCt3;
        this.thPersonalCostCt3 = thPersonalCostCt3;
        this.thServiceUnitsCt3a = thServiceUnitsCt3a;
        this.thPersonalCostCt3a = thPersonalCostCt3a;
        this.thServiceUnitsCt3b = thServiceUnitsCt3b;
        this.thPersonalCostCt3b = thPersonalCostCt3b;
        this.thServiceUnitsCt3c = thServiceUnitsCt3c;
        this.thPersonalCostCt3c = thPersonalCostCt3c;
    }

    public Integer getThID() {
        return thID;
    }

    public void setThID(Integer thID) {
        this.thID = thID;
    }

    public int getThCostCenterID() {
        return thCostCenterID;
    }

    public void setThCostCenterID(int thCostCenterID) {
        this.thCostCenterID = thCostCenterID;
    }

    public String getThCostCenterText() {
        return thCostCenterText;
    }

    public void setThCostCenterText(String thCostCenterText) {
        this.thCostCenterText = thCostCenterText;
    }

    public int getThExternalService() {
        return thExternalService;
    }

    public void setThExternalService(int thExternalService) {
        this.thExternalService = thExternalService;
    }

    public String getThKeyUsed() {
        return thKeyUsed;
    }

    public void setThKeyUsed(String thKeyUsed) {
        this.thKeyUsed = thKeyUsed;
    }

    public int getThServiceUnitsCt1() {
        return thServiceUnitsCt1;
    }

    public void setThServiceUnitsCt1(int thServiceUnitsCt1) {
        this.thServiceUnitsCt1 = thServiceUnitsCt1;
    }

    public int getThPersonalCostCt1() {
        return thPersonalCostCt1;
    }

    public void setThPersonalCostCt1(int thPersonalCostCt1) {
        this.thPersonalCostCt1 = thPersonalCostCt1;
    }

    public int getThServiceUnitsCt2() {
        return thServiceUnitsCt2;
    }

    public void setThServiceUnitsCt2(int thServiceUnitsCt2) {
        this.thServiceUnitsCt2 = thServiceUnitsCt2;
    }

    public int getThPersonalCostCt2() {
        return thPersonalCostCt2;
    }

    public void setThPersonalCostCt2(int thPersonalCostCt2) {
        this.thPersonalCostCt2 = thPersonalCostCt2;
    }

    public int getThServiceUnitsCt3() {
        return thServiceUnitsCt3;
    }

    public void setThServiceUnitsCt3(int thServiceUnitsCt3) {
        this.thServiceUnitsCt3 = thServiceUnitsCt3;
    }

    public int getThPersonalCostCt3() {
        return thPersonalCostCt3;
    }

    public void setThPersonalCostCt3(int thPersonalCostCt3) {
        this.thPersonalCostCt3 = thPersonalCostCt3;
    }

    public int getThServiceUnitsCt3a() {
        return thServiceUnitsCt3a;
    }

    public void setThServiceUnitsCt3a(int thServiceUnitsCt3a) {
        this.thServiceUnitsCt3a = thServiceUnitsCt3a;
    }

    public int getThPersonalCostCt3a() {
        return thPersonalCostCt3a;
    }

    public void setThPersonalCostCt3a(int thPersonalCostCt3a) {
        this.thPersonalCostCt3a = thPersonalCostCt3a;
    }

    public int getThServiceUnitsCt3b() {
        return thServiceUnitsCt3b;
    }

    public void setThServiceUnitsCt3b(int thServiceUnitsCt3b) {
        this.thServiceUnitsCt3b = thServiceUnitsCt3b;
    }

    public int getThPersonalCostCt3b() {
        return thPersonalCostCt3b;
    }

    public void setThPersonalCostCt3b(int thPersonalCostCt3b) {
        this.thPersonalCostCt3b = thPersonalCostCt3b;
    }

    public int getThServiceUnitsCt3c() {
        return thServiceUnitsCt3c;
    }

    public void setThServiceUnitsCt3c(int thServiceUnitsCt3c) {
        this.thServiceUnitsCt3c = thServiceUnitsCt3c;
    }

    public int getThPersonalCostCt3c() {
        return thPersonalCostCt3c;
    }

    public void setThPersonalCostCt3c(int thPersonalCostCt3c) {
        this.thPersonalCostCt3c = thPersonalCostCt3c;
    }

    public PeppCalcBasics getThBaseInformationID() {
        return thBaseInformationID;
    }

    public void setThBaseInformationID(PeppCalcBasics thBaseInformationID) {
        this.thBaseInformationID = thBaseInformationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (thID != null ? thID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGPListTherapy)) {
            return false;
        }
        KGPListTherapy other = (KGPListTherapy) object;
        if ((this.thID == null && other.thID != null) || (this.thID != null && !this.thID.equals(other.thID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListTherapy[ thID=" + thID + " ]";
    }
    
}
