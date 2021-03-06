package org.inek.dataportal.psy.nub.entities;


import org.inek.dataportal.common.helper.nub.NubValueFormatter;
import org.inek.dataportal.common.utils.Documentation;
import org.inek.dataportal.psy.nub.helper.PsyNubRequestValueChecker;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "NubRequestData", schema = "psy")
public class PsyNubRequestData implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "npdId")
    private int _id;

    @OneToOne
    @JoinColumn(name = "npdNubRequestId")
    private PsyNubRequest _psyNubRequest;

    @Documentation(name = "Stellvertreter IK's")
    @Column(name = "npdProxyIKs")
    private String _proxyIks = "";

    @Documentation(name = "Ausfüllhilfe")
    @Column(name = "npdFormFillHelper")
    private String _formFillHelper = "";

    @Documentation(name = "Kommentar")
    @Column(name = "npdUserComment")
    private String _userComment = "";

    @Documentation(name = "Beschreibung der NUB")
    @Column(name = "npdDescription")
    private String _description = "";

    @Documentation(name = "OPS-Codes")
    @Column(name = "npdProcs")
    private String _procs = "";

    @Documentation(name = "Keine Prozeduren verfügbar")
    @Column(name = "npdHasNoProcs")
    private Boolean _hasNoProcs = false;

    @Documentation(name = "Anmerkung zu den Prozeduren")
    @Column(name = "npdProcsComment")
    private String _procsComment = "";

    @Documentation(name = "Indikation")
    @Column(name = "npdIndication")
    private String _indication = "";

    @Documentation(name = "Ablösung oder Ergänzung")
    @Column(name = "npdReplacement")
    private String _replacement = "";

    @Documentation(name = "Warum handelt es sich um eine neue Methode")
    @Column(name = "npdWhatsNew")
    private String _whatsNew = "";

    @Documentation(name = "Auswirkung auf die Verweildauer im Krankenhaus")
    @Column(name = "npdLos")
    private String _los = "";

    @Documentation(name = "Pepps")
    @Column(name = "npdPepps")
    private String _pepps = "";

    @Documentation(name = "Warum nicht im PEPP-System abgebildet")
    @Column(name = "npdWhyNotRepresented")
    private String _whyNotRepresented = "";

    @Column(name = "npdFormerRequest")
    private Boolean _formerRequest = false;

    @Column(name = "npdFormerExternalId")
    private String _formerExternalId = "";

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public PsyNubRequest getPsyNubRequest() {
        return _psyNubRequest;
    }

    public void setPsyNubRequest(PsyNubRequest psyNubRequest) {
        this._psyNubRequest = psyNubRequest;
    }

    public String getProxyIks() {
        return _proxyIks;
    }

    public void setProxyIks(String proxyIks) {
        this._proxyIks = NubValueFormatter.formatValuesForDatabase(proxyIks);
    }

    public String getFormFillHelper() {
        return _formFillHelper;
    }

    public void setFormFillHelper(String formFillHelper) {
        this._formFillHelper = formFillHelper;
    }

    public String getUserComment() {
        return _userComment;
    }

    public void setUserComment(String userComment) {
        this._userComment = userComment;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        this._description = description;
    }

    public String getProcs() {
        return _procs;
    }

    public void setProcs(String procs) {
        this._procs = NubValueFormatter.formatValuesForDatabase(procs);
    }

    public Boolean getHasNoProcs() {
        return _hasNoProcs;
    }

    public void setHasNoProcs(Boolean hasNoProcs) {
        this._hasNoProcs = hasNoProcs;
    }

    public String getProcsComment() {
        return _procsComment;
    }

    public void setProcsComment(String procsComment) {
        this._procsComment = procsComment;
    }

    public String getIndication() {
        return _indication;
    }

    public void setIndication(String indication) {
        this._indication = indication;
    }

    public String getReplacement() {
        return _replacement;
    }

    public void setReplacement(String replacement) {
        this._replacement = replacement;
    }

    public String getWhatsNew() {
        return _whatsNew;
    }

    public void setWhatsNew(String whatsNew) {
        this._whatsNew = whatsNew;
    }

    public String getLos() {
        return _los;
    }

    public void setLos(String los) {
        this._los = los;
    }

    public String getPepps() {
        return _pepps;
    }

    public void setPepps(String pepps) {
        this._pepps = NubValueFormatter.formatValuesForDatabase(pepps);
    }

    public String getWhyNotRepresented() {
        return _whyNotRepresented;
    }

    public void setWhyNotRepresented(String whyNotRepresented) {
        this._whyNotRepresented = whyNotRepresented;
    }

    public Boolean getFormerRequest() {
        return _formerRequest;
    }

    public void setFormerRequest(Boolean formerRequest) {
        this._formerRequest = formerRequest;
    }

    public String getFormerExternalId() {
        return _formerExternalId;
    }

    public void setFormerExternalId(String formerExternalId) {
        this._formerExternalId = formerExternalId;
    }

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsyNubRequestData that = (PsyNubRequestData) o;
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
        return Objects.hash(_id, _psyNubRequest, _proxyIks, _formFillHelper, _userComment, _description,
                _procs, _hasNoProcs, _procsComment, _indication, _replacement, _whatsNew, _los, _pepps,
                _whyNotRepresented, _formerRequest, _formerExternalId);
    }
}
