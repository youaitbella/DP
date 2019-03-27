/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.psy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.common.data.iface.BaseIdValue;
import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGPListOverviewPersonal", schema = "calc")
@XmlRootElement
public class KGPListOverviewPersonal implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

    public KGPListOverviewPersonal() { }

    public KGPListOverviewPersonal(PeppCalcBasics baseInformation, KGPListOverviewPersonalType overviewPersonalType) {
        _baseInformation = baseInformation;
        _overviewPersonalType = overviewPersonalType;
    }

    // <editor-fold defaultstate="collapsed" desc="Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opId", updatable = false, nullable = false)
    private int _id = -1;

    @Override
    public int getId() {
        return _id;
    }

    @Override
    public void setId(int id) {
        this._id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="BaseInformationId">
    @ManyToOne
    @JoinColumn(name = "opBaseInformationId")
    private PeppCalcBasics _baseInformation;

    @JsonIgnore
    public PeppCalcBasics getBaseInformation() {
        return _baseInformation;
    }

    @JsonIgnore
    public void setBaseInformation(PeppCalcBasics baseInformation) {
        this._baseInformation = baseInformation;
    }

    @Override
    public int getBaseInformationId() {
        return _baseInformation.getId();
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformation.setId(baseInformationId);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property OverviewPersonalType">
    @ManyToOne
    @JoinColumn(name = "opOverviewPersonalTypeId")
    private KGPListOverviewPersonalType _overviewPersonalType = new KGPListOverviewPersonalType();

    @JsonIgnore
    public KGPListOverviewPersonalType getOverviewPersonalType() {
        return _overviewPersonalType;
    }

    public void setOverviewPersonalType(KGPListOverviewPersonalType overviewPersonalType) {
        _overviewPersonalType = overviewPersonalType;
    }

    public int getOverviewPersonalTypeId() { // for JSON transferfile
        return _overviewPersonalType == null ? -1 : _overviewPersonalType.getId();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Anzahl VK vor Abgrenzung">
    @Column(name = "opAmountVKPre")
    @Documentation(name = "Anzahl VK vor Abgrenzung")
    private double _amountVKPre;

    public double getAmountVKPre() {
        return _amountVKPre;
    }

    public void setAmountVKPre(double amountVKPre) {
        this._amountVKPre = amountVKPre;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Anzahl VK nach Abgrenzung">
    @Column(name = "opAmountVKPost")
    @Documentation(name = "Anzahl VK nach Abgrenzung")
    private double _amountVKPost;

    public double getAmountVKPost() {
        return _amountVKPost;
    }

    public void setAmountVKPost(double amountVKPost) {
        this._amountVKPost = amountVKPost;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Kostenvolumen vor Abgrenzung">
    @Column(name = "opCostAmountPre")
    @Documentation(name = "Kostenvolumen vor Abgrenzung")
    private double _costAmountPre;

    public double getCostAmountPre() {
        return _costAmountPre;
    }

    public void setCostAmountPre(double costAmountPre) {
        this._costAmountPre = costAmountPre;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Kostenvolumen nach Abgrenzung">
    @Column(name = "opCostAmountPost")
    @Documentation(name = "Kostenvolumen nach Abgrenzung")
    private double _costAmountPost;

    public double getCostAmountPost() {
        return _costAmountPost;
    }

    public void setCostAmountPost(double costAmountPost) {
        this._costAmountPost = costAmountPost;
    }
    // </editor-fold>

    @JsonIgnore
    public boolean isEmpty(){
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this._id;
        if (this._id != -1) {
            return hash;
        }
        hash = 37 * hash + Objects.hashCode(this._amountVKPost);
        hash = 37 * hash + Objects.hashCode(this._amountVKPre);
        hash = 37 * hash + Objects.hashCode(this._costAmountPost);
        hash = 37 * hash + Objects.hashCode(this._baseInformation);
        hash = 37 * hash + Objects.hashCode(this._costAmountPre);
        hash = 37 * hash + Objects.hashCode(this._overviewPersonalType);
        return hash;
    }

    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final KGPListOverviewPersonal other = (KGPListOverviewPersonal) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._amountVKPost != other._amountVKPost) {
            return false;
        }
        if (this._baseInformation != other._baseInformation) {
            return false;
        }
        if (this._amountVKPre != other._amountVKPre) {
            return false;
        }
        if (this._costAmountPost != other._costAmountPost) {
            return false;
        }
        if (!Objects.equals(this._costAmountPre, other._costAmountPre)) {
            return false;
        }
        if (!Objects.equals(this._overviewPersonalType, other._overviewPersonalType)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGPListOverviewPersonal[ opId=" + _id + " ]";
    }
}
