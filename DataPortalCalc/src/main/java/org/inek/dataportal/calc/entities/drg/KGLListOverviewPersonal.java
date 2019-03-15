/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.calc.entities.drg;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.common.data.iface.BaseIdValue;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLListOverviewPersonal", schema = "calc")
@XmlRootElement
public class KGLListOverviewPersonal implements Serializable, BaseIdValue {

    private static final long serialVersionUID = 1L;

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
    @Column(name = "opBaseInformationId")
    private int _baseInformationId;

    @Override
    public int getBaseInformationId() {
        return _baseInformationId;
    }

    @Override
    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property OverviewPersonalType">
    @ManyToOne
    @PrimaryKeyJoinColumn(name = "opOverviewPersonalTypeId")
    private KGLListOverviewPersonalType _overviewPersonalType = new KGLListOverviewPersonalType();

    @JsonIgnore
    public KGLListOverviewPersonalType getOverviewPersonalType() {
        return _overviewPersonalType;
    }

    public void setOverviewPersonalType(KGLListOverviewPersonalType overviewPersonalType) {
        _overviewPersonalType = overviewPersonalType;
    }

    public int getOverviewPersonalTypeId() { // for JSON transferfile
        return _overviewPersonalType == null ? -1 : _overviewPersonalType.getId();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="opAmountVKPre">
    @Column(name = "opAmountVKPre")
    private double _amountVKPre;

    public double getAmountVKPre() {
        return _amountVKPre;
    }

    public void setAmountVKPre(double amountVKPre) {
        this._amountVKPre = amountVKPre;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="opAmountVKPost">
    @Column(name = "opAmountVKPost")
    private double _amountVKPost;

    public double getAmountVKPost() {
        return _amountVKPost;
    }

    public void setAmountVKPost(double amountVKPost) {
        this._amountVKPost = amountVKPost;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="opCostAmountPre">
    @Column(name = "opCostAmountPre")
    private double _costAmountPre;

    public double getCostAmountPre() {
        return _costAmountPre;
    }

    public void setCostAmountPre(double costAmountPre) {
        this._costAmountPre = costAmountPre;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="opCostAmountPost">
    @Column(name = "opCostAmountPost")
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

    public KGLListOverviewPersonal() {
    }

    public KGLListOverviewPersonal(int baseInformationId) {
        _baseInformationId = baseInformationId;
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
        hash = 37 * hash + Objects.hashCode(this._baseInformationId);
        hash = 37 * hash + Objects.hashCode(this._costAmountPre);
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
        final KGLListOverviewPersonal other = (KGLListOverviewPersonal) obj;
        if (this._id != -1 && this._id == other._id) {
            return true;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._amountVKPost != other._amountVKPost) {
            return false;
        }
        if (this._baseInformationId != other._baseInformationId) {
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
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLListOverviewPersonal[ opId=" + _id + " ]";
    }
}
