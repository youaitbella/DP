package org.inek.dataportal.entities.calc;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.Size;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "DistributionModelDetail", schema = "calc")
public class DistributionModelDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    public DistributionModelDetail() {
    }

    public DistributionModelDetail(int masterId) {
        _masterId = masterId;
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dmdId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Property MasterId">
    @Column(name = "dmdMasterId")
    private int _masterId;

    public int getMasterId() {
        return _masterId;
    }

    public void setMasterId(int masterId) {
        this._masterId = masterId;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Article">
    @Column(name = "dmdArticle")
    private String _article = "";

    @Size(max = 250)
    public String getArticle() {
        return _article;
    }

    public void setArticle(String article) {
        _article = article;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CostCenterId">
    @Column(name = "dmdCostCenterId")
    private int _costCenterId;

    public int getCostCenterId() {
        return _costCenterId;
    }

    public void setCostCenterId(int costCenterId) {
        this._costCenterId = costCenterId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CostTypeId">
    @Column(name = "dmdCostTypeId")
    private int _costTypeId;

    public int getCostTypeId() {
        return _costTypeId;
    }

    public void setCostTypeId(int costTypeId) {
        this._costTypeId = costTypeId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CountCases">
    @Column(name = "dmdCountCases")
    private int _countCases;

    public int getCountCases() {
        return _countCases;
    }

    public void setCountCases(int countCases) {
        this._countCases = countCases;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CountCareDays">
    @Column(name = "dmdCountCareDays")
    private int _countCaredays;

    public int getCountCaredays() {
        return _countCaredays;
    }

    public void setCountCaredays(int countCaredays) {
        this._countCaredays = countCaredays;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CostVolume">
    @Column(name = "dmdCostVolume")
    private int _costVolume;

    public int getCostVolume() {
        return _costVolume;
    }

    public void setCostVolume(int costVolume) {
        this._costVolume = costVolume;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property UseProcCode">
    @Column(name = "dmdUseProcCode")
    private boolean _useProcCode;

    public boolean isUseProcCode() {
        return _useProcCode;
    }

    public void setUseProcCode(boolean useProcCode) {
        this._useProcCode = useProcCode;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property UseDiagCode">
    @Column(name = "dmdUseDiagCode")
    private boolean _useDiagCode;

    public boolean isUseDiagCode() {
        return _useDiagCode;
    }

    public void setUseDiagCode(boolean useDiagCode) {
        this._useDiagCode = useDiagCode;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property UseGroupResult">
    @Column(name = "dmdUseGroupResult")
    private boolean _useGroupResult;

    public boolean isUseGroupResult() {
        return _useGroupResult;
    }

    public void setUseGroupResult(boolean useGroupResult) {
        this._useGroupResult = useGroupResult;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property UseOtherCode">
    @Column(name = "dmdUseOtherCode")
    private boolean _useOtherCode;

    public boolean isUseOtherCode() {
        return _useOtherCode;
    }

    public void setUseOtherCode(boolean useOtherCode) {
        this._useOtherCode = useOtherCode;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Approved">
    @Column(name = "dmdApproved")
    private boolean _approved;

    public boolean isApproved() {
        return _approved;
    }

    public void setApproved(boolean approved) {
        this._approved = approved;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property NoteProcCode">
    @Column(name = "dmdNoteProcCode")
    private String _noteProcCode = "";

    @Size(max = 500)
    public String getNoteProcCode() {
        return _noteProcCode;
    }

    public void setNoteProcCode(String noteProcCode) {
        _noteProcCode = noteProcCode;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property NoteDiagCode">
    @Column(name = "dmdNoteDiagCode")
    private String _noteDiagCode = "";

    @Size(max = 500)
    public String getNoteDiagCode() {
        return _noteDiagCode;
    }

    public void setNoteDiagCode(String noteDiagCode) {
        _noteDiagCode = noteDiagCode;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property NoteGroupResult">
    @Column(name = "dmdNoteGroupResult")
    private String _noteGroupResult = "";

    @Size(max = 500)
    public String getNoteGroupResult() {
        return _noteGroupResult;
    }

    public void setNoteGroupResult(String noteGroupResult) {
        _noteGroupResult = noteGroupResult;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property NoteOtherCode">
    @Column(name = "dmdNoteOtherCode")
    private String _noteOtherCode = "";

    @Size(max = 500)
    public String getNoteOtherCode() {
        return _noteOtherCode;
    }

    public void setNoteOtherCode(String noteOtherCode) {
        _noteOtherCode = noteOtherCode;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ApprovalNote">
    @Column(name = "dmdApprovalNote")
    private String _approvalNote = "";

    @Size(max = 500)
    public String getApprovalNote() {
        return _approvalNote;
    }

    public void setApprovalNote(String approvalNote) {
        _approvalNote = approvalNote;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        if (_id > 0) {
            return _id;
        }
        int hash = 3;
        hash = 89 * hash + this._id;
        hash = 89 * hash + this._masterId;
        hash = 89 * hash + Objects.hashCode(this._article);
        hash = 89 * hash + this._costCenterId;
        hash = 89 * hash + this._costTypeId;
        return hash;
    }

    @Override
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
        final DistributionModelDetail other = (DistributionModelDetail) obj;
        if (_id > 0) {
            return _id == other._id;
        }
        if (this._id != other._id) {
            return false;
        }
        if (this._masterId != other._masterId) {
            return false;
        }
        if (this._costCenterId != other._costCenterId) {
            return false;
        }
        if (this._costTypeId != other._costTypeId) {
            return false;
        }
        if (!Objects.equals(this._article, other._article)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "DistributionModelDetail[id=" + _id + "]";
    }
    // </editor-fold>

    public boolean isEmpty() {
        return _id <= 0
                && _article.isEmpty()
                && _costCenterId <= 0
                && _costTypeId <= 0
                && _countCases <= 0
                && _countCaredays <= 0
                && _costVolume <= 0
                && !_useProcCode
                && !_useDiagCode
                && !_useGroupResult
                && !_useOtherCode
                && !_approved
                && _noteProcCode.isEmpty()
                && _noteDiagCode.isEmpty()
                && _noteGroupResult.isEmpty()
                && _noteOtherCode.isEmpty()
                && _approvalNote.isEmpty();
    }

}