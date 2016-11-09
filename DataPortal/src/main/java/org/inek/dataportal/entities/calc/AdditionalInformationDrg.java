package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.inek.dataportal.enums.CalcAdditionalInformation;

/**
 *
 * @author vohldo
 */
@Entity
@Table(name = "AddtionalInformationDrg", schema = "calc")
public class AdditionalInformationDrg implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // <editor-fold defaultstate="collapsed" desc="Id">
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "aiId")
    private Integer _id = -1;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer _id) {
        this._id = _id;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="BasicId">
    
    @Column(name = "aiBasicId")
    private Integer _basicId = -1;
    
    public Integer getBasicId() {
        return _basicId;
    }

    public void setBasicId(Integer _basicId) {
        this._basicId = _basicId;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Type">
    
    @Column(name = "aiType")
    private Integer _aiType = -1;

    public CalcAdditionalInformation getAiType() {
        for(CalcAdditionalInformation ai : CalcAdditionalInformation.values()) {
            if(ai.ordinal() == _aiType)
                return ai;
        }
        return null;
    }

    public void setAiType(CalcAdditionalInformation _aiType) {
        this._aiType = _aiType.ordinal();
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="CostCenterNumber">
    
    @Column(name = "aiCostCenterNumber")
    private Integer _costCenterNumber = -1;

    public Integer getCostCenterNumber() {
        return _costCenterNumber;
    }

    public void setCostCenterNumber(Integer _costCenterNumber) {
        this._costCenterNumber = _costCenterNumber;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="CostCenterName">
    
    @Column(name = "aiCostCenterName")
    private String _costCenterName = "";
    
    public String getCostCenterName() {
        return _costCenterName;
    }

    public void setCostCenterName(String _costCenterName) {
        this._costCenterName = _costCenterName;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="CostVolume">
    
    @Column(name = "aiCostVolume")
    private BigDecimal _costVolume = new BigDecimal(-1);
    
    public BigDecimal getCostVolume() {
        return _costVolume;
    }

    public void setCostVolume(BigDecimal _costVolume) {
        this._costVolume = _costVolume;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="MedicalServiceNumber">
    
    @Column(name = "aiMedicalServiceNumber")
    private float _medicalServiceNumber;
    
    public float getMedicalServiceNumber() {
        return _medicalServiceNumber;
    }

    public void setMedicalServiceNumber(float _medicalServiceNumber) {
        this._medicalServiceNumber = _medicalServiceNumber;
    }
    
    // </editor-fold>
}
