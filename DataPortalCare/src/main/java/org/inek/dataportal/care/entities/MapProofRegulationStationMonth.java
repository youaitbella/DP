package org.inek.dataportal.care.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.inek.dataportal.care.enums.Months;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author lautenti
 */
@Entity
@Table(name = "MapProofRegulationStationMonth", schema = "care")
@Cacheable(true)
public class MapProofRegulationStationMonth implements Serializable {

    private static final long serialVersionUID = 1L;

    public MapProofRegulationStationMonth() {
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prsmId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property prsSensitiveAreaIds">
    @ManyToOne
    @JoinColumn(name = "prsmProofRegulationStationId")
    @JsonIgnore
    private ProofRegulationStation _proofRegulationStation;

    @JsonIgnore
    public ProofRegulationStation getProofRegulationStation() {
        return _proofRegulationStation;
    }

    @JsonIgnore
    public void setProofRegulationStation(ProofRegulationStation proofRegulationStation) {
        this._proofRegulationStation = proofRegulationStation;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Month">
    @Column(name = "prsmMonth")
    private int _month;

    public Months getMonth() {
        return Months.getById(_month);
    }

    public void setMonth(Months month) {
        this._month = month.getId();
    }

    //</editor-fold>
}
