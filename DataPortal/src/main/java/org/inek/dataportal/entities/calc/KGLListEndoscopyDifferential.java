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
@Table(name = "KGLListEndoscopyDifferential", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLListEndoscopyDifferential.findAll", query = "SELECT k FROM KGLListEndoscopyDifferential k")
    , @NamedQuery(name = "KGLListEndoscopyDifferential.findByEdID", query = "SELECT k FROM KGLListEndoscopyDifferential k WHERE k.edID = :edID")
    , @NamedQuery(name = "KGLListEndoscopyDifferential.findByEdDivision", query = "SELECT k FROM KGLListEndoscopyDifferential k WHERE k.edDivision = :edDivision")
    , @NamedQuery(name = "KGLListEndoscopyDifferential.findByEdActivityKey", query = "SELECT k FROM KGLListEndoscopyDifferential k WHERE k.edActivityKey = :edActivityKey")})
public class KGLListEndoscopyDifferential implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "edID")
    private Integer edID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "edDivision")
    private String edDivision;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "edActivityKey")
    private String edActivityKey;
    @JoinColumn(name = "edBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics edBaseInformationID;

    public KGLListEndoscopyDifferential() {
    }

    public KGLListEndoscopyDifferential(Integer edID) {
        this.edID = edID;
    }

    public KGLListEndoscopyDifferential(Integer edID, String edDivision, String edActivityKey) {
        this.edID = edID;
        this.edDivision = edDivision;
        this.edActivityKey = edActivityKey;
    }

    public Integer getEdID() {
        return edID;
    }

    public void setEdID(Integer edID) {
        this.edID = edID;
    }

    public String getEdDivision() {
        return edDivision;
    }

    public void setEdDivision(String edDivision) {
        this.edDivision = edDivision;
    }

    public String getEdActivityKey() {
        return edActivityKey;
    }

    public void setEdActivityKey(String edActivityKey) {
        this.edActivityKey = edActivityKey;
    }

    public DrgCalcBasics getEdBaseInformationID() {
        return edBaseInformationID;
    }

    public void setEdBaseInformationID(DrgCalcBasics edBaseInformationID) {
        this.edBaseInformationID = edBaseInformationID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (edID != null ? edID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListEndoscopyDifferential)) {
            return false;
        }
        KGLListEndoscopyDifferential other = (KGLListEndoscopyDifferential) object;
        if ((this.edID == null && other.edID != null) || (this.edID != null && !this.edID.equals(other.edID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListEndoscopyDifferential[ edID=" + edID + " ]";
    }
    
}
