package org.inek.dataportal.care.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author lautenti
 */
@Entity
@Table(name = "ProofObjection", schema = "care")
public class ProofObjection implements Serializable {

    private static final long serialVersionUID = 1L;

    public ProofObjection() {
    }


    public ProofObjection(ProofObjection proofObjection) {
        this._objectionId = proofObjection.getObjectionId();
        this._statement = proofObjection.getStatement();
    }


    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "poId")
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
    @JoinColumn(name = "poProofId")
    private Proof _proof;

    public Proof getProof() {
        return _proof;
    }

    public void setProof(Proof proof) {
        this._proof = proof;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property ObjectionId">
    @Column(name = "prObjectionId")
    private int _objectionId;

    public int getObjectionId() {
        return _objectionId;
    }

    public void setObjectionId(int objectionId) {
        this._objectionId = objectionId;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Property Statement">
    @Column(name = "poStatement")
    private String _statement;

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
        ProofObjection that = (ProofObjection) o;
        return _objectionId == that._objectionId &&
                Objects.equals(_proof, that._proof) &&
                Objects.equals(_statement, that._statement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_proof, _objectionId, _statement);
    }
}
