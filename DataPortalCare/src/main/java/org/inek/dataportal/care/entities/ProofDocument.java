package org.inek.dataportal.care.entities;


import org.inek.dataportal.common.data.account.iface.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "ProofDocument", schema = "care")
@NamedQueries({
        @NamedQuery(name = "ProofDocument.NameByIkAndYear", query = "select d._name from ProofDocument d where d._ik = :ik and d._year = :year"),
        @NamedQuery(name = "ProofDocument.DocumentByIkAndYear", query = "select d from ProofDocument d where d._ik = :ik and d._year = :year")
})
public class ProofDocument implements Serializable, Document {
    private static final long serialVersionUID = 1L;

    public ProofDocument() {}

    public ProofDocument(String name) {
        _name = name;
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pdId")
    private int _id;

    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    //<editor-fold desc="Property Ik">
    @Column(name = "pdIk")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int _ik) {
        this._ik = _ik;
    }
    //</editor-fold>

    //<editor-fold desc="Property Year">
    @Column(name = "pdYear")
    private int _year;

    public int getYear() {
        return _year;
    }

    public void setYear(int _year) {
        this._year = _year;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Created">
    @Column(name = "pdCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _created = Calendar.getInstance().getTime();

    public Date getCreated() {
        return _created;
    }

    public void setCreated(Date created) {
        _created = created;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "pdName")
    private String _name;

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Content">
    @Lob
    @Column(name = "pdContent")
    private byte[] _content;

    @Override
    public byte[] getContent() {
        return _content;
    }

    @Override
    public void setContent(byte[] content) {
        _content = content;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals">
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + this._id;
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
        final ProofDocument other = (ProofDocument) obj;
        if (this._id != other._id) {
            return false;
        }
        return true;
    }
    // </editor-fold>

}
