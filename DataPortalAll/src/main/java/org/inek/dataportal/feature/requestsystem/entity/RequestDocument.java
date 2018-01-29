package org.inek.dataportal.feature.requestsystem.entity;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author muellermi
 */
@Entity
public class RequestDocument implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rdId")
    private int _requestDocumentId;

    //<editor-fold defaultstate="collapsed" desc="Property RequestId">
    @Column(name = "rdRequestId")
    private int _requestId = -1;
    
    public int getRequestId() {
        return _requestId;
    }
    
    public void setRequestId(int requestId) {
        this._requestId = requestId;
    }
    //</editor-fold>
    
    
    @Column(name = "rdName")
    private String _name = "";
    @Lob
    @Column(name = "rdContent")
    private byte[] _content;

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public int getRequestDocumentId() {
        return _requestDocumentId;
    }

    public void setRequestDocumentId(int id) {
        _requestDocumentId = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    
    public byte[] getContent() {
        return _content;
    }

    public void setContent(byte[] content) {
        _content = content;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _requestDocumentId;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RequestDocument)) {
            return false;
        }
        RequestDocument other = (RequestDocument) object;
        return _requestDocumentId == other._requestDocumentId;
    }

    @Override
    public String toString() {
        return "org.inek.entities.RequestDocument[id=" + _requestDocumentId + "]";
    }
    // </editor-fold>

}
