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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(catalog = "dataportaldev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGLNeonatData.findAll", query = "SELECT k FROM KGLNeonatData k")
    , @NamedQuery(name = "KGLNeonatData.findByNdID", query = "SELECT k FROM KGLNeonatData k WHERE k.ndID = :ndID")
    , @NamedQuery(name = "KGLNeonatData.findByNdData", query = "SELECT k FROM KGLNeonatData k WHERE k.ndData = :ndData")})
public class KGLNeonatData implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    private Integer ndID;
    @Basic(optional = false)
    @NotNull
    private int ndData;
    @JoinColumn(name = "ndBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private DrgCalcBasics ndBaseInformationID;
    @JoinColumn(name = "ndContentTextID", referencedColumnName = "ctID")
    @ManyToOne(optional = false)
    private DrgContentText ndContentTextID;

    public KGLNeonatData() {
    }

    public KGLNeonatData(Integer ndID) {
        this.ndID = ndID;
    }

    public KGLNeonatData(Integer ndID, int ndData) {
        this.ndID = ndID;
        this.ndData = ndData;
    }

    public Integer getNdID() {
        return ndID;
    }

    public void setNdID(Integer ndID) {
        this.ndID = ndID;
    }

    public int getNdData() {
        return ndData;
    }

    public void setNdData(int ndData) {
        this.ndData = ndData;
    }

    public DrgCalcBasics getNdBaseInformationID() {
        return ndBaseInformationID;
    }

    public void setNdBaseInformationID(DrgCalcBasics ndBaseInformationID) {
        this.ndBaseInformationID = ndBaseInformationID;
    }

    public DrgContentText getNdContentTextID() {
        return ndContentTextID;
    }

    public void setNdContentTextID(DrgContentText ndContentTextID) {
        this.ndContentTextID = ndContentTextID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (ndID != null ? ndID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLNeonatData)) {
            return false;
        }
        KGLNeonatData other = (KGLNeonatData) object;
        if ((this.ndID == null && other.ndID != null) || (this.ndID != null && !this.ndID.equals(other.ndID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLNeonatData[ ndID=" + ndID + " ]";
    }
    
}
