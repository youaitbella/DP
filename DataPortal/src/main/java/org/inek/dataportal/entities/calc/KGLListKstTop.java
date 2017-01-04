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
    private String text = "";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Column(name = "ktCaseCnt")
    private int caseCount;

    public int getCaseCount() {
        return caseCount;
    }

    public void setCaseCount(int caseCount) {
        this.caseCount = caseCount;
    }

    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ktAmount")
    private double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Column(name = "ktDelimitationAmount")
    private double delimitationAmount;

    public double getDelimitationAmount() {
        return delimitationAmount;
    }

    public void setDelimitationAmount(double delimitationAmount) {
        this.delimitationAmount = delimitationAmount;
    }

    @Column(name = "ktRank")
    private int rank;

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Column(name = "ktBaseInformationID")
    private int baseInformationID;

    public int getBaseInformationID() {
        return baseInformationID;
    }

    public void setBaseInformationID(int baseInformationID) {
        this.baseInformationID = baseInformationID;
    }

    public KGLListKstTop() {
    }

    public KGLListKstTop(Integer ktID) {
        this.id = ktID;
    }

    public KGLListKstTop(Integer ktID, int ktCostCenterID, String ktText, int ktCaseCnt, double ktAmount, double ktDelimitationAmount, int ktRank) {
        this.id = ktID;
        this.ktCostCenterID = ktCostCenterID;
        this.text = ktText;
        this.caseCount = ktCaseCnt;
        this.amount = ktAmount;
        this.delimitationAmount = ktDelimitationAmount;
        this.rank = ktRank;
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
