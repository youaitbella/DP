
package org.inek.dataportal.entities.admin;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import org.inek.dataportal.enums.Feature;

@Entity
@Table(name = "MailTemplate", schema = "adm")
public class MailTemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @Column (name = "mtId")
    private Integer _id;
    
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Feature">
    @Column (name = "mtFeature")
    @Enumerated(EnumType.STRING)
    private Feature _feature;
    
    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Subject">
    @Column (name = "mtSubject")
    private String _subject;
    
    public String getSubject() {
        return _subject;
    }

    public void setSubject(String subject) {
        _subject = subject;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">

    // <editor-fold defaultstate="collapsed" desc="Property Body">
    @Column (name = "mtBody")
    private String _body;
    
    public String getBody() {
        return _body;
    }

    public void setBody(String body) {
        _body = body;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_id != null ? _id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the _id fields are not set
        if (!(object instanceof MailTemplate)) {
            return false;
        }
        MailTemplate other = (MailTemplate) object;
        if ((this._id == null && other._id != null) || (this._id != null && !this._id.equals(other._id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _id + " ]";
    }
    // </editor-fold>

}
