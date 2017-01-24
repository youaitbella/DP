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
import javax.persistence.Lob;
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
@Table(name = "KGPDocuments", catalog = "DataPortalDev", schema = "calc")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KGPDocuments.findAll", query = "SELECT k FROM KGPDocuments k")})
public class KGPDocuments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "doID")
    private Integer doID;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "doName")
    private String doName;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "doContent")
    private byte[] doContent;
    @JoinColumn(name = "doBaseInformationID", referencedColumnName = "biID")
    @ManyToOne(optional = false)
    private PeppCalcBasics doBaseInformationID;
    @JoinColumn(name = "doSheetID", referencedColumnName = "sID")
    @ManyToOne(optional = false)
    private KGPListSheet doSheetID;

    public KGPDocuments() {
    }

    public KGPDocuments(Integer doID) {
        this.doID = doID;
    }

    public KGPDocuments(Integer doID, String doName, byte[] doContent) {
        this.doID = doID;
        this.doName = doName;
        this.doContent = doContent;
    }

    public Integer getDoID() {
        return doID;
    }

    public void setDoID(Integer doID) {
        this.doID = doID;
    }

    public String getDoName() {
        return doName;
    }

    public void setDoName(String doName) {
        this.doName = doName;
    }

    public byte[] getDoContent() {
        return doContent;
    }

    public void setDoContent(byte[] doContent) {
        this.doContent = doContent;
    }

    public PeppCalcBasics getDoBaseInformationID() {
        return doBaseInformationID;
    }

    public void setDoBaseInformationID(PeppCalcBasics doBaseInformationID) {
        this.doBaseInformationID = doBaseInformationID;
    }

    public KGPListSheet getDoSheetID() {
        return doSheetID;
    }

    public void setDoSheetID(KGPListSheet doSheetID) {
        this.doSheetID = doSheetID;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (doID != null ? doID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGPDocuments)) {
            return false;
        }
        KGPDocuments other = (KGPDocuments) object;
        if ((this.doID == null && other.doID != null) || (this.doID != null && !this.doID.equals(other.doID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPDocuments[ doID=" + doID + " ]";
    }
    
}
