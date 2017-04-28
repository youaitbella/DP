/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc.autopsy;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "CalcBasicsAutopsyItem", schema = "calc")
public class AutopsyItem implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cbaiId", updatable = false, nullable = false)
    private int id;

    public AutopsyItem() {
    }

    AutopsyItem(AutopsyServiceText serviceText) {
        _autopsyServiceText = serviceText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CalcBasicsAutopsyId">
    @Column(name = "cbaiCalcBasicsAutopsyId")
    private int _calcBasicsAutopsyId = -1;

    public int getCalcBasicsAutopsyId() {
        return _calcBasicsAutopsyId;
    }

    public void setCalcBasicsAutopsyId(int calcBasicsAutopsyId) {
        _calcBasicsAutopsyId = calcBasicsAutopsyId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AutopsyServiceTextId">
    @Column(name = "cbaiAutopsyServiceTextId")
    private int _autopsyServiceTextId;  // needed for storing only 

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "cbaiAutopsyServiceTextId")
    private AutopsyServiceText _autopsyServiceText;

    public AutopsyServiceText getAutopsyServiceText() {
        return _autopsyServiceText;
    }

    public void setAutopsyServiceText(AutopsyServiceText autopsyServiceText) {
        _autopsyServiceText = autopsyServiceText;
        _autopsyServiceTextId = autopsyServiceText.getId();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CountCases">
    @Column(name = "cbaiCountCases")
    private int _countCases;

    public int getCountCases() {
        return _countCases;
    }

    public void setCountCases(int countCases) {
        _countCases = countCases;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeMedical">
    @Column(name = "cbaiCostVolumeMedical")
    private int _costVolumeMedical;

    public int getCostVolumeMedical() {
        return _costVolumeMedical;
    }

    public void setCostVolumeMedical(int costVolumeMedical) {
        _costVolumeMedical = costVolumeMedical;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CostVolumeOther">
    @Column(name = "cbaiCostVolumeOther")
    private int _costVolumeOther;

    public int getCostVolumeOther() {
        return _costVolumeOther;
    }

    public void setCostVolumeOther(int costVolumeOther) {
        _costVolumeOther = costVolumeOther;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DescriptionAccrual">
    @Column(name = "cbaiDescriptionAccrual")
    private String _descriptionAccrual = "";

    public String getDescriptionAccrual() {
        return _descriptionAccrual;
    }

    public void setDescriptionAccrual(String descriptionAccrual) {
        this._descriptionAccrual = descriptionAccrual;
    }
    //</editor-fold>

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AutopsyItem)) {
            return false;
        }
        AutopsyItem other = (AutopsyItem) object;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.autopsy.AutopsyItem[ id=" + id + " ]";
    }

}
