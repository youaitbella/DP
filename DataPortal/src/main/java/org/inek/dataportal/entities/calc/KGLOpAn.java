/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.inek.dataportal.helper.groupinterface.TopicCalcOpAn;
import org.inek.dataportal.helper.groupinterface.Seal;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author kunkelan
 */
@Entity
@Table(name = "KGLOpAn", schema = "calc")
public class KGLOpAn implements Serializable {

    private static final long serialVersionUID = 1L;

    //<editor-fold defaultstate="collapsed" desc="baseInformationId">
    @Id
    @Column(name = "oaBaseInformationId", nullable = false)
    private int _baseInformationId;

    public int getBaseInformationId() {
        return _baseInformationId;
    }

    public void setBaseInformationId(int baseInformationId) {
        this._baseInformationId = baseInformationId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="centralOP">    
    @Documentation(name = "Zentral OPs vorhanden", rank = 3000, headline = "Kostenstellengruppen 4 und 5 (OP-Bereich und Anästhesie)")
    public boolean isCentralOP() {
        return _centralOPCnt > 0;
    }

    public void setCentralOP(boolean centralOP) {
        if (centralOP && _centralOPCnt < 1) {
            _centralOPCnt = 1;
        }
        if (!centralOP) {
            _centralOPCnt = 0;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="centralOPCnt">    
    @Documentation(name = "Anzahl Zentral-OPs", omitOnValues = "0", rank = 3001)
    @Column(name = "oaCentralOPCnt")
    private int _centralOPCnt;

    @Min(value = 0, groups = {Seal.class}, message = "Die Anzahl der OPs darf nicht negativ sein", payload = TopicCalcOpAn.class)
    @Max(value = 99, groups = {Seal.class}, message = "Die Anzahl der OPs ist unplausibel hoch", payload = TopicCalcOpAn.class)
    public int getCentralOPCnt() {
        return _centralOPCnt;
    }

    public void setCentralOPCnt(int centralOPCnt) {
        this._centralOPCnt = centralOPCnt;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="staffBindingMsOP">    
    @Column(name = "oaStaffBindingMsOP")
    @Documentation(name = "Personalbindungszeit OP ÄD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0")
    private boolean _staffBindingMsOP;

    public boolean getStaffBindingMsOP() {
        return _staffBindingMsOP;
    }

    public void setStaffBindingMsOP(boolean staffBindingMsOP) {
        this._staffBindingMsOP = staffBindingMsOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="staffBindingFsOP">    
    @Column(name = "oaStaffBindingFsOP")
    @Documentation(name = "Personalbindungszeit OP FD/MTD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0")
    private boolean _staffBindingFsOP;

    public boolean getStaffBindingFsOP() {
        return _staffBindingFsOP;
    }

    public void setStaffBindingFsOP(boolean staffBindingFsOP) {
        this._staffBindingFsOP = staffBindingFsOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="medicalServiceSnzOP">    
    @Column(name = "oaMedicalServiceSnzOP")
    @Documentation(name = "Schnitt-Naht-Zeit OP ÄD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0", translateValue = "1=mit fallindividuellem Gleichzeitigkeitsfaktor;2=mit standardisiertem Gleichzeitigkeitsfaktor je OP-Art;4=Alternative (bitte beschreiben)")
    private int _medicalServiceSnzOP;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte Schnitt-Naht-Zeit OP ÄD wählen", payload = TopicCalcOpAn.class)})
    @Max(value = 4, groups = {Seal.class}, message = "Bitte Schnitt-Naht-Zeit OP ÄD wählen", payload = TopicCalcOpAn.class)
    public int getMedicalServiceSnzOP() {
        return _medicalServiceSnzOP;
    }

    public void setMedicalServiceSnzOP(int medicalServiceSnzOP) {
        this._medicalServiceSnzOP = medicalServiceSnzOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="functionalServiceSnzOP">    
    @Column(name = "oaFunctionalServiceSnzOP")
    
    @Documentation(name = "Schnitt-Naht-Zeit OP FD/MTD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0", translateValue = "1=mit fallindividuellem Gleichzeitigkeitsfaktor;2=mit standardisiertem Gleichzeitigkeitsfaktor je OP-Art;4=Alternative (bitte beschreiben)")
    private int _functionalServiceSnzOP;

    public int getFunctionalServiceSnzOP() {
        return _functionalServiceSnzOP;
    }

    public void setFunctionalServiceSnzOP(int functionalServiceSnzOP) {
        this._functionalServiceSnzOP = functionalServiceSnzOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="descriptionSnzOP">    
    @Column(name = "oaDescriptionSnzOP")
    @Documentation(name = "SNZ Alternative OP", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0")
    private String _descriptionSnzOP = "";

    public String getDescriptionSnzOP() {
        return _descriptionSnzOP;
    }

    public void setDescriptionSnzOP(String descriptionSnzOP) {
        this._descriptionSnzOP = descriptionSnzOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="medicalServiceRzOP">    
    @Column(name = "oaMedicalServiceRzOP")
    @Documentation(name = "Rüstzeit OP ÄD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0", translateValue = "1=als fallindividuell erfasster Wert je Mitarbeiter(in);2=als abgestufter Standardwert je OP-Art;3=als Einheitswert;4=Alternative (bitte beschreiben)")
    private int _medicalServiceRzOP;

    public int getMedicalServiceRzOP() {
        return _medicalServiceRzOP;
    }

    public void setMedicalServiceRzOP(int medicalServiceRzOP) {
        this._medicalServiceRzOP = medicalServiceRzOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="functionalServiceRzOP">    
    @Column(name = "oaFunctionalServiceRzOP")
    @Documentation(name = "Rüstzeit OP FD/MTD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0", translateValue = "1=als fallindividuell erfasster Wert je Mitarbeiter(in);2=als abgestufter Standardwert je OP-Art;3=als Einheitswert;4=Alternative (bitte beschreiben)")
    private int _functionalServiceRzOP;

    public int getFunctionalServiceRzOP() {
        return _functionalServiceRzOP;
    }

    public void setFunctionalServiceRzOP(int functionalServiceRzOP) {
        this._functionalServiceRzOP = functionalServiceRzOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="descriptionRzOP">    
    @Column(name = "oaDescriptionRzOP")
    @Documentation(name = "Rüstzeit Alternative OP", omitOnOtherValues = "KGLOpAn._centralOPCnt=0", rank = 3010)
    private String _descriptionRzOP = "";

    public String getDescriptionRzOP() {
        return _descriptionRzOP;
    }

    public void setDescriptionRzOP(String descriptionRzOP) {
        this._descriptionRzOP = descriptionRzOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="medicalServiceAmountOP">    
    @Column(name = "oaMedicalServiceAmountOP")
    @Documentation(name = "Leistungsminuten OP ÄD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0")
    private int _medicalServiceAmountOP;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte Leistungsminuten OP ÄD angeben", payload = TopicCalcOpAn.class)})
    public int getMedicalServiceAmountOP() {
        return _medicalServiceAmountOP;
    }

    public void setMedicalServiceAmountOP(int medicalServiceAmountOP) {
        this._medicalServiceAmountOP = medicalServiceAmountOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="functionalServiceAmountOP">    
    @Column(name = "oaFunctionalServiceAmountOP")
    @Documentation(name = "Leistungsminuten OP FD/MTD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0")
    private int _functionalServiceAmountOP;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte Leistungsminuten OP FD/MTD angeben", payload = TopicCalcOpAn.class)})
    public int getFunctionalServiceAmountOP() {
        return _functionalServiceAmountOP;
    }

    public void setFunctionalServiceAmountOP(int functionalServiceAmountOP) {
        this._functionalServiceAmountOP = functionalServiceAmountOP;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="staffBindingMsAN">    
    @Column(name = "oaStaffBindingMsAN")
    @Documentation(name = "Personalbindungszeit AN ÄD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0")
    private boolean _staffBindingMsAN;

    public boolean getStaffBindingMsAN() {
        return _staffBindingMsAN;
    }

    public void setStaffBindingMsAN(boolean staffBindingMsAN) {
        this._staffBindingMsAN = staffBindingMsAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="staffBindingFsAN">    
    @Column(name = "oaStaffBindingFsAN")
    @Documentation(name = "Personalbindungszeit AN FD/MTD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0")
    private boolean _staffBindingFsAN;

    public boolean getStaffBindingFsAN() {
        return _staffBindingFsAN;
    }

    public void setStaffBindingFsAN(boolean staffBindingFsAN) {
        this._staffBindingFsAN = staffBindingFsAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="medicalServiceSnzAN">    
    @Column(name = "oaMedicalServiceSnzAN")
    
    @Documentation(name = "Schnitt-Naht-Zeit OP ÄD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0", translateValue = "1=mit fallindividuellem Gleichzeitigkeitsfaktor;2=mit standardisiertem Gleichzeitigkeitsfaktor je OP-Art;4=Alternative (bitte beschreiben)")
    private int _medicalServiceSnzAN;

    public int getMedicalServiceSnzAN() {
        return _medicalServiceSnzAN;
    }

    public void setMedicalServiceSnzAN(int medicalServiceSnzAN) {
        this._medicalServiceSnzAN = medicalServiceSnzAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="functionalServiceSnzAN">    
    @Column(name = "oaFunctionalServiceSnzAN")
    
    @Documentation(name = "Schnitt-Naht-Zeit OP FD/MTD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0", translateValue = "1=mit fallindividuellem Gleichzeitigkeitsfaktor;2=mit standardisiertem Gleichzeitigkeitsfaktor je OP-Art;4=Alternative (bitte beschreiben)")
    private int _functionalServiceSnzAN;

    public int getFunctionalServiceSnzAN() {
        return _functionalServiceSnzAN;
    }

    public void setFunctionalServiceSnzAN(int functionalServiceSnzAN) {
        this._functionalServiceSnzAN = functionalServiceSnzAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="descriptionSnzAN">    
    @Column(name = "oaDescriptionSnzAN")
    @Documentation(name = "SNZ Alternative AN", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0")
    private String _descriptionSnzAN = "";

    public String getDescriptionSnzAN() {
        return _descriptionSnzAN;
    }

    public void setDescriptionSnzAN(String descriptionSnzAN) {
        this._descriptionSnzAN = descriptionSnzAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="medicalServiceRzAN">    
    @Column(name = "oaMedicalServiceRzAN")
    @Documentation(name = "Rüstzeit AN ÄD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0", translateValue = "1=als fallindividuell erfasster Wert je Mitarbeiter(in);2=als abgestufter Standardwert je OP-Art;3=als Einheitswert;4=Alternative (bitte beschreiben)")
    private int _medicalServiceRzAN;

    public int getMedicalServiceRzAN() {
        return _medicalServiceRzAN;
    }

    public void setMedicalServiceRzAN(int medicalServiceRzAN) {
        this._medicalServiceRzAN = medicalServiceRzAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="functionalServiceRzAN">    
    @Column(name = "oaFunctionalServiceRzAN")
    @Documentation(name = "Rüstzeit AN FD/MTD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0", translateValue = "1=als fallindividuell erfasster Wert je Mitarbeiter(in);2=als abgestufter Standardwert je OP-Art;3=als Einheitswert;4=Alternative (bitte beschreiben)")
    private int _functionalServiceRzAN;

    public int getFunctionalServiceRzAN() {
        return _functionalServiceRzAN;
    }

    public void setFunctionalServiceRzAN(int functionalServiceRzAN) {
        this._functionalServiceRzAN = functionalServiceRzAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="descriptionRzAN">    
    @Column(name = "oaDescriptionRzAN")
    @Documentation(name = "Rüstzeit Alternative AN", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0")
    private String _descriptionRzAN = "";

    public String getDescriptionRzAN() {
        return _descriptionRzAN;
    }

    public void setDescriptionRzAN(String descriptionRzAN) {
        this._descriptionRzAN = descriptionRzAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="medicalServiceAmountAN">    
    @Column(name = "oaMedicalServiceAmountAN")
    @Documentation(name = "Leistungsminuten AN ÄD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0")
    private int _medicalServiceAmountAN;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte Leistungsminuten AN ÄD angeben", payload = TopicCalcOpAn.class)})
    public int getMedicalServiceAmountAN() {
        return _medicalServiceAmountAN;
    }

    public void setMedicalServiceAmountAN(int medicalServiceAmountAN) {
        this._medicalServiceAmountAN = medicalServiceAmountAN;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="functionalServiceAmountAN">    
    @Column(name = "oaFunctionalServiceAmountAN")
    @Documentation(name = "Leistungsminuten AN FD/MTD", rank = 3010, omitOnOtherValues = "KGLOpAn._centralOPCnt=0")
    private int _functionalServiceAmountAN;

    @Min.List({@Min(value = 0),
        @Min(value = 1, groups = {Seal.class}, message = "Bitte Leistungsminuten AN FD/MTD angeben", payload = TopicCalcOpAn.class)})
    public int getFunctionalServiceAmountAN() {
        return _functionalServiceAmountAN;
    }

    public void setFunctionalServiceAmountAN(int functionalServiceAmountAN) {
        this._functionalServiceAmountAN = functionalServiceAmountAN;
    }
    //</editor-fold>

    public KGLOpAn() {
    }

    public KGLOpAn(Integer oaBaseInformationId) {
        this._baseInformationId = oaBaseInformationId;
    }

    public KGLOpAn(int baseInformationId, KGLOpAn template) {
        this._baseInformationId = baseInformationId;
        this._centralOPCnt = template._centralOPCnt;
        this._staffBindingMsOP = template._staffBindingMsOP;
        this._staffBindingFsOP = template._staffBindingFsOP;
        this._medicalServiceSnzOP = template._medicalServiceSnzOP;
        this._functionalServiceSnzOP = template._functionalServiceSnzOP;
        this._descriptionSnzOP = template._descriptionSnzOP;
        this._medicalServiceRzOP = template._medicalServiceRzOP;
        this._functionalServiceRzOP = template._functionalServiceRzOP;
        this._descriptionRzOP = template._descriptionRzOP;
        this._staffBindingMsAN = template._staffBindingMsAN;
        this._staffBindingFsAN = template._staffBindingFsAN;
        this._medicalServiceSnzAN = template._medicalServiceSnzAN;
        this._functionalServiceSnzAN = template._functionalServiceSnzAN;
        this._descriptionSnzAN = template._descriptionSnzAN;
        this._medicalServiceRzAN = template._medicalServiceRzAN;
        this._functionalServiceRzAN = template._functionalServiceRzAN;
        this._descriptionRzAN = template._descriptionRzAN;
        // these parts wont be copied!
        //this._medicalServiceAmountAN = template._medicalServiceAmountAN;
        //this._functionalServiceAmountAN = template._functionalServiceAmountAN;
        //this._medicalServiceAmountOP = template._medicalServiceAmountOP;
        //this._functionalServiceAmountOP = template._functionalServiceAmountOP;
    }

    @Override
    public int hashCode() {
        return _baseInformationId;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof KGLOpAn)) {
            return false;
        }
        KGLOpAn other = (KGLOpAn) object;
        return this._baseInformationId == other._baseInformationId;
    }

    @Override
    public String toString() {
        return "org.inek.dataportal.entities.calc.KGLOpAn[ oaBaseInformationId=" + _baseInformationId + " ]";
    }

}
