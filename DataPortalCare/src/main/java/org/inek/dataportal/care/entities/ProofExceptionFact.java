package org.inek.dataportal.care.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author lautenti
 */
@Entity
@Table(name = "ProofExceptionFact", schema = "care")
public class ProofExceptionFact implements Serializable {

    private static final long serialVersionUID = 1L;

    public ProofExceptionFact() {
    }


    public ProofExceptionFact(ProofExceptionFact proofExceptionFact) {
        this._exceptionFactId = proofExceptionFact.getExceptionFactId();
        this._statement = proofExceptionFact.getStatement();
    }


    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pefId")
    private Integer _id;

    public int getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Proof">
    @OneToOne
    @JoinColumn(name = "pefProofId")
    private Proof _proof;

    public Proof getProof() {
        return _proof;
    }

    public void setProof(Proof proof) {
        this._proof = proof;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property pefExceptionFactId">
    @Column(name = "pefExceptionFactId")
    private int _exceptionFactId;

    public int getExceptionFactId() {
        return _exceptionFactId;
    }

    public void setExceptionFactId(int exceptionFactId) {
        this._exceptionFactId = exceptionFactId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Statement">
    @Column(name = "pefStatement")
    private String _statement = "";

    public String getStatement() {
        return _statement;
    }

    public void setStatement(String statement) {
        this._statement = statement;
    }
    //</editor-fold>


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProofExceptionFact that = (ProofExceptionFact) o;
        return _exceptionFactId == that._exceptionFactId &&
                Objects.equals(_proof, that._proof) &&
                Objects.equals(_statement, that._statement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_proof, _exceptionFactId, _statement);
    }
}
