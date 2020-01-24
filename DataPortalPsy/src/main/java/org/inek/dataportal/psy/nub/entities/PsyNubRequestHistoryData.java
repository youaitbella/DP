package org.inek.dataportal.psy.nub.entities;


import org.inek.dataportal.common.helper.nub.NubValueFormatter;
import org.inek.dataportal.common.utils.Documentation;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "NubRequestHistoryData", schema = "psy")
public class PsyNubRequestHistoryData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "npdId")
    private int _id;

    @OneToOne
    @JoinColumn(name = "npdNubRequestHistoryId")
    private PsyNubRequestHistory _psyNubRequest;

    @Column(name = "npdProxyIKs")
    private String _proxyIks = "";

    @Column(name = "npdFormFillHelper")
    private String _formFillHelper = "";

    @Column(name = "npdUserComment")
    private String _userComment = "";

    @Column(name = "npdDescription")
    private String _description = "";

    @Column(name = "npdProcs")
    private String _procs = "";

    @Column(name = "npdHasNoProcs")
    private Boolean _hasNoProcs = false;

    @Column(name = "npdProcsComment")
    private String _procsComment = "";

    @Column(name = "npdIndication")
    private String _indication = "";

    @Column(name = "npdReplacement")
    private String _replacement = "";

    @Column(name = "npdWhatsNew")
    private String _whatsNew = "";

    @Column(name = "npdLos")
    private String _los = "";

    @Column(name = "npdPepps")
    private String _pepps = "";

    @Column(name = "npdWhyNotRepresented")
    private String _whyNotRepresented = "";

    @Column(name = "npdFormerRequest")
    private Boolean _formerRequest = false;

    @Column(name = "npdFormerExternalId")
    private String _formerExternalId = "";

    public PsyNubRequestHistory getPsyNubRequest() {
        return _psyNubRequest;
    }

    public String getProxyIks() {
        return _proxyIks;
    }

    public String getFormFillHelper() {
        return _formFillHelper;
    }

    public String getUserComment() {
        return _userComment;
    }

    public String getDescription() {
        return _description;
    }

    public String getProcs() {
        return _procs;
    }

    public Boolean getHasNoProcs() {
        return _hasNoProcs;
    }

    public String getProcsComment() {
        return _procsComment;
    }

    public String getIndication() {
        return _indication;
    }

    public String getReplacement() {
        return _replacement;
    }

    public String getWhatsNew() {
        return _whatsNew;
    }

    public String getLos() {
        return _los;
    }

    public String getPepps() {
        return _pepps;
    }

    public String getWhyNotRepresented() {
        return _whyNotRepresented;
    }

    public Boolean getFormerRequest() {
        return _formerRequest;
    }

    public String getFormerExternalId() {
        return _formerExternalId;
    }

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsyNubRequestHistoryData that = (PsyNubRequestHistoryData) o;
        return _id == that._id &&
                Objects.equals(_psyNubRequest, that._psyNubRequest) &&
                Objects.equals(_proxyIks, that._proxyIks) &&
                Objects.equals(_formFillHelper, that._formFillHelper) &&
                Objects.equals(_userComment, that._userComment) &&
                Objects.equals(_description, that._description) &&
                Objects.equals(_procs, that._procs) &&
                Objects.equals(_hasNoProcs, that._hasNoProcs) &&
                Objects.equals(_procsComment, that._procsComment) &&
                Objects.equals(_indication, that._indication) &&
                Objects.equals(_replacement, that._replacement) &&
                Objects.equals(_whatsNew, that._whatsNew) &&
                Objects.equals(_los, that._los) &&
                Objects.equals(_pepps, that._pepps) &&
                Objects.equals(_whyNotRepresented, that._whyNotRepresented) &&
                Objects.equals(_formerRequest, that._formerRequest) &&
                Objects.equals(_formerExternalId, that._formerExternalId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _psyNubRequest, _proxyIks, _formFillHelper, _userComment, _description, _procs,
                _hasNoProcs, _procsComment, _indication, _replacement, _whatsNew, _los, _pepps, _whyNotRepresented,
                _formerRequest, _formerExternalId);
    }
}
