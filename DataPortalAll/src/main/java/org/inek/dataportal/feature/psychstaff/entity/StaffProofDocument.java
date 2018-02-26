package org.inek.dataportal.feature.psychstaff.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.inek.dataportal.common.feature.account.iface.Document;
import org.inek.dataportal.feature.psychstaff.enums.PsychType;

@Entity
@Table(name = "StaffProofDocument", schema = "psy")
public class StaffProofDocument implements Serializable, Document {
    
    private static final long serialVersionUID = 1L;
    
    public StaffProofDocument() {}

    public StaffProofDocument(String name) {
        _name = name;
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spdId")
    private int _id;
    
    public Integer getId() {
        return _id;
    }

    public void setId(Integer id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property StaffProofMasterId">
    @Column(name = "spdStaffProofMasterId")
    private int _staffProofMasterId;

    public int getStaffProofMasterId() {
        return _staffProofMasterId;
    }

    public void setStaffProofMasterId(int staffProofMasterId) {
        _staffProofMasterId = staffProofMasterId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Appendix">
    @Column(name = "spdAppendix")
    private int _appendix;

    public int getAppendix() {
        return _appendix;
    }

    public void setAppendix(int appendix) {
        _appendix = appendix;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Signature">
    @Column(name = "spdSignature")
    private String _signature;

    public String getSignature() {
        return _signature;
    }

    public void setSignature(String signature) {
        _signature = signature;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Created">
    @Column(name = "spdCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _created = Calendar.getInstance().getTime();;
    
    public Date getCreated() {
        return _created;
    }

    public void setCreated(Date created) {
        _created = created;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "spdName")
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
    @Column(name = "spdContent")
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
        hash = 31 * hash + this._appendix;
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
        final StaffProofDocument other = (StaffProofDocument) obj;
        if (this._id != other._id) {
            return false;
        }
        if (this._appendix != other._appendix) {
            return false;
        }
        return true;
    }
    // </editor-fold>
    
}
