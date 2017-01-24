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
@Table(name = "KGPListRadiologyLaboratory", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPListRadiologyLaboratory.findAll", query = "SELECT k FROM KGPListRadiologyLaboratory k")})
public class KGPListRadiologyLaboratory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlID")
    private Integer rlID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlCostCenterID")
    private int rlCostCenterID;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlCostCenterNumber")
    private int rlCostCenterNumber;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "rlCostCenterText")
    private String rlCostCenterText;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocHome")
    private boolean rlServiceDocHome;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocDKG")
    private boolean rlServiceDocDKG;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocEBM")
    private boolean rlServiceDocEBM;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocGOA")
    private boolean rlServiceDocGOA;
    @Basic(optional = false)
    @NotNull
    @Column(name = "rlServiceDocDif")
    private boolean rlServiceDocDif;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2147483647)
    @Column(name = "rlDescription")
    private String rlDescription;
    @JoinColumn(name = "rlBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private PeppCalcBasics rlBaseInformationID;

    public KGPListRadiologyLaboratory() {
    }

    public KGPListRadiologyLaboratory(Integer rlID) {
        this.rlID = rlID;
    }

    public KGPListRadiologyLaboratory(Integer rlID, int rlCostCenterID, int rlCostCenterNumber, String rlCostCenterText, boolean rlServiceDocHome, boolean rlServiceDocDKG, boolean rlServiceDocEBM, boolean rlServiceDocGOA, boolean rlServiceDocDif, String rlDescription) {
        this.rlID = rlID;
        this.rlCostCenterID = rlCostCenterID;
        this.rlCostCenterNumber = rlCostCenterNumber;
        this.rlCostCenterText = rlCostCenterText;
        this.rlServiceDocHome = rlServiceDocHome;
        this.rlServiceDocDKG = rlServiceDocDKG;
        this.rlServiceDocEBM = rlServiceDocEBM;
        this.rlServiceDocGOA = rlServiceDocGOA;
        this.rlServiceDocDif = rlServiceDocDif;
        this.rlDescription = rlDescription;
    }

    public Integer getRlID() {
        return rlID;
    }

    public void setRlID(Integer rlID) {
        this.rlID = rlID;
    }

    public int getRlCostCenterID() {
        return rlCostCenterID;
    }

    public void setRlCostCenterID(int rlCostCenterID) {
        this.rlCostCenterID = rlCostCenterID;
    }

    public int getRlCostCenterNumber() {
        return rlCostCenterNumber;
    }

    public void setRlCostCenterNumber(int rlCostCenterNumber) {
        this.rlCostCenterNumber = rlCostCenterNumber;
    }

    public String getRlCostCenterText() {
        return rlCostCenterText;
    }

    public void setRlCostCenterText(String rlCostCenterText) {
        this.rlCostCenterText = rlCostCenterText;
    }

    public boolean getRlServiceDocHome() {
        return rlServiceDocHome;
    }

    public void setRlServiceDocHome(boolean rlServiceDocHome) {
        this.rlServiceDocHome = rlServiceDocHome;
    }

    public boolean getRlServiceDocDKG() {
        return rlServiceDocDKG;
    }

    public void setRlServiceDocDKG(boolean rlServiceDocDKG) {
        this.rlServiceDocDKG = rlServiceDocDKG;
    }

    public boolean getRlServiceDocEBM() {
        return rlServiceDocEBM;
    }

    public void setRlServiceDocEBM(boolean rlServiceDocEBM) {
        this.rlServiceDocEBM = rlServiceDocEBM;
    }

    public boolean getRlServiceDocGOA() {
        return rlServiceDocGOA;
    }

    public void setRlServiceDocGOA(boolean rlServiceDocGOA) {
        this.rlServiceDocGOA = rlServiceDocGOA;
    }

    public boolean getRlServiceDocDif() {
        return rlServiceDocDif;
    }

    public void setRlServiceDocDif(boolean rlServiceDocDif) {
        this.rlServiceDocDif = rlServiceDocDif;
    }

    public String getRlDescription() {
        return rlDescription;
    }

    public void setRlDescription(String rlDescription) {
        this.rlDescription = rlDescription;
    }

    public PeppCalcBasics getRlBaseInformationID() {
        return rlBaseInformationID;
    }

    public void setRlBaseInformationID(PeppCalcBasics rlBaseInformationID) {
        this.rlBaseInformationID = rlBaseInformationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rlID != null ? rlID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGPListRadiologyLaboratory)) {
            return false;
        }
        KGPListRadiologyLaboratory other = (KGPListRadiologyLaboratory) object;
        if ((this.rlID == null && other.rlID != null) || (this.rlID != null && !this.rlID.equals(other.rlID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListRadiologyLaboratory[ rlID=" + rlID + " ]";
    }
    
}
