/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.feature.calculationhospital.entities.autopsy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.inek.dataportal.common.utils.Documentation;

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
    private int id = -1;

    public AutopsyItem() {
    }

    AutopsyItem(AutopsyServiceText serviceText) {
        setAutopsyServiceText(serviceText);
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
    
    public int getAutopsyServiceTextId(){
        return _autopsyServiceTextId;
    }

    @ManyToOne
    @PrimaryKeyJoinColumn(name = "cbaiAutopsyServiceTextId")
    private AutopsyServiceText _autopsyServiceText;

    public AutopsyServiceText getAutopsyServiceText() {
        return _autopsyServiceText;
    }

    public final void setAutopsyServiceText(AutopsyServiceText autopsyServiceText) {
        _autopsyServiceText = autopsyServiceText;
        _autopsyServiceTextId = autopsyServiceText.getId();
    }
    
    @JsonIgnore
    @Documentation(name = "Bereich", rank = 10)
    public String getAutopsyServiceTextText() {
        return _autopsyServiceText.getText();
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CountCases">
    @Column(name = "cbaiCountCases")
    @Documentation(name = "Anzahl Fälle")
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
    @Documentation(name = "hierfür abgegrenztes Kostenvolumen Ärztlicher Dienst der Pathologie")
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
    @Documentation(name = "hierfür abgegrenztes Kostenvolumen übrige Personalkosten der Pathologie")
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
    @Documentation(name = "Kurzbeschreibung des durchgeführten Abgrenzungsverfahrens")
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
