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
    @NamedQuery(name = "KGLPKMSAlternative.findAll", query = "SELECT k FROM KGLPKMSAlternative k")
    , @NamedQuery(name = "KGLPKMSAlternative.findByPaID", query = "SELECT k FROM KGLPKMSAlternative k WHERE k.paID = :paID")
    , @NamedQuery(name = "KGLPKMSAlternative.findByPaDepartment", query = "SELECT k FROM KGLPKMSAlternative k WHERE k.paDepartment = :paDepartment")
    , @NamedQuery(name = "KGLPKMSAlternative.findByPaDepartmentKey", query = "SELECT k FROM KGLPKMSAlternative k WHERE k.paDepartmentKey = :paDepartmentKey")
    , @NamedQuery(name = "KGLPKMSAlternative.findByPaAlternative", query = "SELECT k FROM KGLPKMSAlternative k WHERE k.paAlternative = :paAlternative")})
public class KGLPKMSAlternative implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer paID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    private String paDepartment;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    private String paDepartmentKey;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    private String paAlternative;
    @JoinColumn(name = "paBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics paBaseInformationID;

    public KGLPKMSAlternative() {
    }

    public KGLPKMSAlternative(Integer paID) {
        this.paID = paID;
    }

    public KGLPKMSAlternative(Integer paID, String paDepartment, String paDepartmentKey, String paAlternative) {
        this.paID = paID;
        this.paDepartment = paDepartment;
        this.paDepartmentKey = paDepartmentKey;
        this.paAlternative = paAlternative;
    }

    public Integer getPaID() {
        return paID;
    }

    public void setPaID(Integer paID) {
        this.paID = paID;
    }

    public String getPaDepartment() {
        return paDepartment;
    }

    public void setPaDepartment(String paDepartment) {
        this.paDepartment = paDepartment;
    }

    public String getPaDepartmentKey() {
        return paDepartmentKey;
    }

    public void setPaDepartmentKey(String paDepartmentKey) {
        this.paDepartmentKey = paDepartmentKey;
    }

    public String getPaAlternative() {
        return paAlternative;
    }

    public void setPaAlternative(String paAlternative) {
        this.paAlternative = paAlternative;
    }

    public DrgCalcBasics getPaBaseInformationID() {
        return paBaseInformationID;
    }

    public void setPaBaseInformationID(DrgCalcBasics paBaseInformationID) {
        this.paBaseInformationID = paBaseInformationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (paID != null ? paID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLPKMSAlternative)) {
            return false;
        }
        KGLPKMSAlternative other = (KGLPKMSAlternative) object;
        if ((this.paID == null && other.paID != null) || (this.paID != null && !this.paID.equals(other.paID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLPKMSAlternative[ paID=" + paID + " ]";
    }
    
}
