/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListKstTop", schema = "calc")
@XmlRootElement
public class KGLListKstTop implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ktID")
    private int id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "ktCostCenterID")
    private int ktCostCenterID;

    public int getKtCostCenterID() {
        return ktCostCenterID;
    }

    public void setKtCostCenterID(int ktCostCenterID) {
        this.ktCostCenterID = ktCostCenterID;
    }

    @Column(name = "ktText")
    private String ktText = "";

    public String getKtText() {
        return ktText;
    }

    public void setKtText(String ktText) {
        this.ktText = ktText;
    }

    @Column(name = "ktCaseCnt")
    private int ktCaseCnt;

    public int getKtCaseCnt() {
        return ktCaseCnt;
    }

    public void setKtCaseCnt(int ktCaseCnt) {
        this.ktCaseCnt = ktCaseCnt;
    }

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ktAmount")
    private double ktAmount;

    public double getKtAmount() {
        return ktAmount;
    }

    public void setKtAmount(double ktAmount) {
        this.ktAmount = ktAmount;
    }

    @Column(name = "ktDelimitationAmount")
    private double ktDelimitationAmount;

    public double getKtDelimitationAmount() {
        return ktDelimitationAmount;
    }

    public void setKtDelimitationAmount(double ktDelimitationAmount) {
        this.ktDelimitationAmount = ktDelimitationAmount;
    }

    @Column(name = "ktRank")
    private int ktRank;

    public int getKtRank() {
        return ktRank;
    }

    public void setKtRank(int ktRank) {
        this.ktRank = ktRank;
    }

    @Column(name = "ktBaseInformationID")
    private int ktBaseInformationID;

    public int getKtBaseInformationID() {
        return ktBaseInformationID;
    }

    public void setKtBaseInformationID(int ktBaseInformationID) {
        this.ktBaseInformationID = ktBaseInformationID;
    }

    public KGLListKstTop() {
    }

    public KGLListKstTop(Integer ktID) {
        this.id = ktID;
    }

    public KGLListKstTop(Integer ktID, int ktCostCenterID, String ktText, int ktCaseCnt, double ktAmount, double ktDelimitationAmount, int ktRank) {
        this.id = ktID;
        this.ktCostCenterID = ktCostCenterID;
        this.ktText = ktText;
        this.ktCaseCnt = ktCaseCnt;
        this.ktAmount = ktAmount;
        this.ktDelimitationAmount = ktDelimitationAmount;
        this.ktRank = ktRank;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KGLListKstTop)) {
            return false;
        }
        KGLListKstTop other = (KGLListKstTop) object;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListKstTop[ ktID=" + id + " ]";
    }

}
