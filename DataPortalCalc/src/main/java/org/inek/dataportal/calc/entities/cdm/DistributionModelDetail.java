package org.inek.dataportal.calc.entities.cdm;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.persistence.*;
import javax.validation.constraints.Size;
import org.inek.dataportal.common.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "DistributionModelDetail", schema = "calc")
public class DistributionModelDetail implements Serializable {

    private static final long serialVersionUID = 1L;
    //@Inject @Transient private ValueLists _valueLists;  delivers a null object :(

    public DistributionModelDetail() {
    }

    public DistributionModelDetail(int masterId) {
        _masterId = masterId;
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dmdId", updatable = false, nullable = false)
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Property MasterId">
    @Column(name = "dmdMasterId")
    private int _masterId = -1;

    public int getMasterId() {
        return _masterId;
    }

    public void setMasterId(int masterId) {
        this._masterId = masterId;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Article">
    @Column(name = "dmdArticle")
    @Documentation(name = "Artikel", rank = 100)
    private String _article = "";

    @Size(max = 250, message = "Für Artikelbezsichnung sind max. {max} Zeichen zulässig.")
    public String getArticle() {
        return _article;
    }

    public void setArticle(String article) {
        _article = article;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CostCenterId">
    @Column(name = "dmdCostCenterId")
    private int _costCenterId = -1;

    public int getCostCenterId() {
        return _costCenterId;
    }

    public void setCostCenterId(int costCenterId) {
        this._costCenterId = costCenterId;
    }

    @Documentation(name = "KstStGr", rank = 105)
    public String getCostCenterName() {
//        CostCenter cost = _valueLists.getCostCenter(_costCenterId);
//        return cost.getCharId() + " " + cost.getText();
        return  _costCenterId == 90 ? "OV" : "" + _costCenterId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CostTypeId">
    @Column(name = "dmdCostTypeId")
    private int _costTypeId = -1;

    public int getCostTypeId() {
        return _costTypeId;
    }

    public void setCostTypeId(int costTypeId) {
        this._costTypeId = costTypeId;
    }

    @Documentation(name = "KstArtGr", rank = 110)
    @SuppressWarnings("MagicNumber")
    public String getCostTypeName() {
        Map<Integer, String> costTypes = new HashMap<>();
        costTypes.put(142, "4b");
        costTypes.put(150, "5");
        costTypes.put(162, "6b");
        costTypes.put(163, "6c");
        costTypes.put(100, "10");
        return costTypes.get(_costTypeId);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property CountCases">
    @Column(name = "dmdCountCases")
    @Documentation(name = "Fallzahl", rank = 120)
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
    @Documentation(name = "Pflegetage", omitOnEmpty = true, rank = 115)
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
    @Documentation(name = "Kostenvolumen", rank = 125)
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
    @Documentation(name = "Verteilung über OPS", rank = 130)
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
    @Documentation(name = "Verteilung über ICD", rank = 140)
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
    @Documentation(name = "Verteilung über DRG/PEPP", rank = 150)
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
    @Documentation(name = "Verteilung über anderen Schlüssel", rank = 160)
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
    @Documentation(name = "Genemigung erteilt", rank = 170)
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
    @Documentation(name = "Anmerkung OPS", rank = 135, omitOnEmpty = true)
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
    @Documentation(name = "Anmerkung ICD", rank = 145, omitOnEmpty = true)
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
    @Documentation(name = "Anmerkung Grupierung", rank = 155, omitOnEmpty = true)
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
    @Documentation(name = "Anmerkung sonstiger Schlüssel", rank = 165, omitOnEmpty = true)
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
    @Documentation(name = "Bemerkung InEK", rank = 175, omitOnEmpty = true)
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

    @SuppressWarnings("CyclomaticComplexity")
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
