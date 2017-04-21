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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cbaiId", updatable = false, nullable = false)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property DescriptionAccrual">
    @Column(name = "cbaiDescriptionAccrual")
    private String _descriptionAccrual;
    
    public String getDescriptionAccrual() {
        return _descriptionAccrual;
    }
    
    public void setDescriptionAccrual(String descriptionAccrual) {
        this._descriptionAccrual = descriptionAccrual;
    }
    //</editor-fold>
    
    
    /*
	int	Unchecked
cbaiCalcBasicsAutopsyId	int	Unchecked
cbaiAutopsyServiceTextId	int	Unchecked
cbaiCountCases	int	Unchecked
cbaiCostVolumeMedical	int	Unchecked
cbaiCostVolumeOther	int	Unchecked
	nvarchar(MAX)	Unchecked    
    */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AutopsyItem)) {
            return false;
        }
        AutopsyItem other = (AutopsyItem) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.autopsy.AutopsyItem[ id=" + id + " ]";
    }
    
}
