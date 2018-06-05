
package org.inek.dataportal.common.data.adm;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.converter.FeatureConverter;

@Entity
@Table(name = "MailTemplate", schema = "adm")
public class MailTemplate implements Serializable {
    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "mtId")
    private int _id = -1;
    
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column (name = "mtName")
    private String _name;
    
    @Size(min = 1, max = 50)
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Feature">
    @Column (name = "mtFeatureId")
    @Convert(converter = FeatureConverter.class)
    private Feature _feature;
    
    @NotNull
    public Feature getFeature() {
        return _feature;
    }

    public void setFeature(Feature feature) {
        _feature = feature;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property BCC">
    @Column (name = "mtBcc")
    private String _bcc = "";
    
    @Size(max = 100)
    public String getBcc() {
        return _bcc;
    }

    public void setBcc(String value) {
        _bcc = value;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Sender">
    @Column (name = "mtFrom")
    private String _from = "";
    
    @Size(max = 100)
    public String getFrom() {
        return _from;
    }

    public void setFrom(String value) {
        _from = value;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Subject">
    @Column (name = "mtSubject")
    private String _subject;
    
    @Size(min = 1, max = 200)
    public String getSubject() {
        return _subject;
    }

    public void setSubject(String subject) {
        _subject = subject;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Body">
    @Column (name = "mtBody")
    private String _body;
    
    @Size(min = 1)
    public String getBody() {
        return _body;
    }

    public void setBody(String body) {
        _body = body;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="Property Type">
    @Column(name = "mtMailTypeId")
    private int _type = 0;

    public int getType() {
        return _type;
    }

    public void setType(int type) {
        this._type = type;
    }
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">

    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof MailTemplate)) {
            return false;
        }
        MailTemplate other = (MailTemplate) object;
        return this._id == other._id;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "[ id=" + _id + " ]";
    }
    // </editor-fold>

}
