/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Basic;
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
@Table(catalog = "dataportaldev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLNormalStationServiceDocumentation.findAll", query = "SELECT k FROM KGLNormalStationServiceDocumentation k")
    , @NamedQuery(name = "KGLNormalStationServiceDocumentation.findByNssID", query = "SELECT k FROM KGLNormalStationServiceDocumentation k WHERE k.nssID = :nssID")
    , @NamedQuery(name = "KGLNormalStationServiceDocumentation.findByNssUsed", query = "SELECT k FROM KGLNormalStationServiceDocumentation k WHERE k.nssUsed = :nssUsed")
    , @NamedQuery(name = "KGLNormalStationServiceDocumentation.findByNssDepartment", query = "SELECT k FROM KGLNormalStationServiceDocumentation k WHERE k.nssDepartment = :nssDepartment")
    , @NamedQuery(name = "KGLNormalStationServiceDocumentation.findByNssDepartmentKey", query = "SELECT k FROM KGLNormalStationServiceDocumentation k WHERE k.nssDepartmentKey = :nssDepartmentKey")
    , @NamedQuery(name = "KGLNormalStationServiceDocumentation.findByNssAlternative", query = "SELECT k FROM KGLNormalStationServiceDocumentation k WHERE k.nssAlternative = :nssAlternative")})
public class KGLNormalStationServiceDocumentation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer nssID;
    @Basic(optional = false)
    @NotNull
    private boolean nssUsed;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    private String nssDepartment;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    private String nssDepartmentKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    private String nssAlternative;
    @JoinColumn(name = "nssBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics nssBaseInformationID;
    @JoinColumn(name = "nssContentTextID", referencedColumnName = "ctID")
    @ManyToOne(optional = false)
    private DrgContentText nssContentTextID;

    public KGLNormalStationServiceDocumentation() {
    }

    public KGLNormalStationServiceDocumentation(Integer nssID) {
        this.nssID = nssID;
    }

    public KGLNormalStationServiceDocumentation(Integer nssID, boolean nssUsed, String nssDepartment, String nssDepartmentKey, String nssAlternative) {
        this.nssID = nssID;
        this.nssUsed = nssUsed;
        this.nssDepartment = nssDepartment;
        this.nssDepartmentKey = nssDepartmentKey;
        this.nssAlternative = nssAlternative;
    }

    public Integer getNssID() {
        return nssID;
    }

    public void setNssID(Integer nssID) {
        this.nssID = nssID;
    }

    public boolean getNssUsed() {
        return nssUsed;
    }

    public void setNssUsed(boolean nssUsed) {
        this.nssUsed = nssUsed;
    }

    public String getNssDepartment() {
        return nssDepartment;
    }

    public void setNssDepartment(String nssDepartment) {
        this.nssDepartment = nssDepartment;
    }

    public String getNssDepartmentKey() {
        return nssDepartmentKey;
    }

    public void setNssDepartmentKey(String nssDepartmentKey) {
        this.nssDepartmentKey = nssDepartmentKey;
    }

    public String getNssAlternative() {
        return nssAlternative;
    }

    public void setNssAlternative(String nssAlternative) {
        this.nssAlternative = nssAlternative;
    }

    public DrgCalcBasics getNssBaseInformationID() {
        return nssBaseInformationID;
    }

    public void setNssBaseInformationID(DrgCalcBasics nssBaseInformationID) {
        this.nssBaseInformationID = nssBaseInformationID;
    }

    public DrgContentText getNssContentTextID() {
        return nssContentTextID;
    }

    public void setNssContentTextID(DrgContentText nssContentTextID) {
        this.nssContentTextID = nssContentTextID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nssID != null ? nssID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLNormalStationServiceDocumentation)) {
            return false;
        }
        KGLNormalStationServiceDocumentation other = (KGLNormalStationServiceDocumentation) object;
        if ((this.nssID == null && other.nssID != null) || (this.nssID != null && !this.nssID.equals(other.nssID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLNormalStationServiceDocumentation[ nssID=" + nssID + " ]";
    }
    
}
