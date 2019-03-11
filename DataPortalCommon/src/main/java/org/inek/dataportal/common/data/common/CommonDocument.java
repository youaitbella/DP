package org.inek.dataportal.common.data.common;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.account.entities.DocumentDomain;
import org.inek.dataportal.common.data.account.iface.Document;
import org.inek.dataportal.common.data.converter.FeatureConverter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

@Entity
@Table(name = "Document")
public class CommonDocument implements Document {

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docId")
    private Integer _id = -1;

    public int getId() {
        return _id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Created">
    @Column(name = "docCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _created = Calendar.getInstance().getTime();
    ;

    public Date getCreated() {
        return _created;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property AccountId">
    @Column(name = "docAccountId")
    private int _accountId;

    public int getAccountId() {
        return _accountId;
    }

    public void setAccountId(int accountId) {
        _accountId = accountId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Feature">
    @Column(name = "docFeatureId")
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

    // <editor-fold defaultstate="collapsed" desc="Property IK">
    @Column(name = "docIk")
    private int _ik;

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        _ik = ik;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Domain">
    @ManyToOne()
    @JoinColumn(name = "docDomainId")
    private DocumentDomain _domain;

    public DocumentDomain getDomain() {
        return _domain;
    }

    public void setDomain(DocumentDomain domain) {
        _domain = domain;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Column(name = "docName")
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
    @Column(name = "docContent")
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

    //<editor-fold desc="equal & hash">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CommonDocument document = (CommonDocument) o;

        if (_accountId != document._accountId) return false;
        if (_ik != document._ik) return false;
        if (!_id.equals(document._id)) return false;
        if (!_created.equals(document._created)) return false;
        if (_feature != document._feature) return false;
        if (!_domain.equals(document._domain)) return false;
        return _name.equals(document._name);
    }

    @Override
    public int hashCode() {
        return _id.hashCode();
    }
    //</editor-fold>
}
