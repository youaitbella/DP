package org.inek.dataportal.psy.nub.entities;

import org.inek.dataportal.common.data.converter.WorkflowStatusConverter;
import org.inek.dataportal.common.enums.WorkflowStatus;
import org.inek.dataportal.common.utils.Documentation;
import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;
import org.inek.dataportal.psy.nub.enums.PsyNubMoneyFields;
import org.inek.dataportal.psy.nub.enums.PsyNubNumberFields;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.*;

@Entity
@Table(name = "NubRequest", schema = "psy")
public class PsyNubRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nubId")
    private int _id;

    @Version
    @Column(name = "nubVersion")
    private int _version;

    @Column(name = "nubExternalStatus")
    private String _externalStatus = "";

    @Column(name = "nubErrorText")
    private String _errorText = "";

    @Column(name = "nubTargetYear")
    private int _targetYear;

    @Column(name = "nubCreatedByAccountId")
    private int _createdByAccountId;

    @Column(name = "nubLastChangedByAccountId")
    private int _lastChangedByAccountId;

    @Column(name = "nubHelperId")
    private int _helperId;

    @Column(name = "nubStatusId")
    @Convert(converter = WorkflowStatusConverter.class)
    private WorkflowStatus _status;

    @Column(name = "nubCreatedAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _createdAt;

    @Column(name = "nubSealedAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _sealedAt = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    @Column(name = "nubLastModifiedAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _lastModifiedAt;

    @Column(name = "nubDateOfReview")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _dateOfReview = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    @Column(name = "nubDateCorrectionRequested")
    @Temporal(TemporalType.TIMESTAMP)
    private Date _dateCorrectionRequested = Date.from(LocalDate.of(2000, Month.JANUARY, 1).atStartOfDay().toInstant(ZoneOffset.UTC));

    @Documentation(name = "Anzeigename")
    @Column(name = "nubDisplayName")
    private String _displayName = "";

    @Documentation(name = "NUB-Name")
    @Column(name = "nubName")
    private String _name = "";

    @Documentation(name = "Alternativer Name")
    @Column(name = "nubAltName")
    private String _altName = "";

    @Documentation(name = "IK")
    @Column(name = "nubIk")
    private int _ik;

    @Documentation(name = "Krankenhausname")
    @Column(name = "nubIkName")
    private String _ikName = "";

    @Documentation(name = "Geschlecht")
    @Column(name = "nubGender")
    private int _gender;

    @Documentation(name = "Titel")
    @Column(name = "nubTitle")
    private String _title = "";

    @Documentation(name = "Vorname")
    @Column(name = "nubFirstName")
    private String _firstName = "";

    @Documentation(name = "Nachname")
    @Column(name = "nubLastName")
    private String _lastName = "";

    @Documentation(name = "Abteilung")
    @Column(name = "nubDivision")
    private String _division = "";

    @Documentation(name = "Funktion")
    @Column(name = "nubRoleId")
    private int _roleId;

    @Documentation(name = "Straße")
    @Column(name = "nubStreet")
    private String _street = "";

    @Documentation(name = "PLZ")
    @Column(name = "nubPostalCode")
    private String _postalCode = "";

    @Documentation(name = "Stadt")
    @Column(name = "nubTown")
    private String _town = "";

    @Documentation(name = "Telefon")
    @Column(name = "nubPhone")
    private String _phone = "";

    @Documentation(name = "FAX")
    @Column(name = "nubFax")
    private String _fax = "";

    @Documentation(name = "Email")
    @Column(name = "nubEmail")
    private String _email = "";

    @OneToOne(mappedBy = "_psyNubRequest", cascade = CascadeType.ALL)
    private PsyNubRequestData _requestData;

    @OneToMany(mappedBy = "_psyNubRequest",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "dvNubRequestId")
    private List<PsyNubRequestDateValue> _requestDateValues = new ArrayList<>();

    @OneToMany(mappedBy = "_psyNubRequest",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "nvNubRequestId")
    private List<PsyNubRequestNumberValue> _requestNumberValues = new ArrayList<>();

    @OneToMany(mappedBy = "_psyNubRequest",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "mvNubRequestId")
    private List<PsyNubRequestMoneyValue> _requestMoneyValues = new ArrayList<>();

    @OneToMany(mappedBy = "_psyNubRequest",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "npdNubRequestId")
    private List<PsyNubRequestDocument> _requestDocuments = new ArrayList<>();

    @Transient
    private Boolean _selected = false;

    public int getVersion() {
        return _version;
    }

    public void setVersion(int version) {
        this._version = version;
    }

    public Boolean getSelected() {
        return _selected;
    }

    public void setSelected(Boolean selected) {
        this._selected = selected;
    }

    public String getNubIdExtern() {
        return "N" + _id;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getExternalStatus() {
        return _externalStatus;
    }

    public void setExternalStatus(String externalStatus) {
        this._externalStatus = externalStatus;
    }

    public String getErrorText() {
        return _errorText;
    }

    public void setErrorText(String errorText) {
        this._errorText = errorText;
    }

    public int getTargetYear() {
        return _targetYear;
    }

    public void setTargetYear(int targetYear) {
        this._targetYear = targetYear;
    }

    public int getCreatedByAccountId() {
        return _createdByAccountId;
    }

    public void setCreatedByAccountId(int createdByAccountId) {
        this._createdByAccountId = createdByAccountId;
    }

    public int getLastChangedByAccountId() {
        return _lastChangedByAccountId;
    }

    public void setLastChangedByAccountId(int lastChangedByAccountId) {
        this._lastChangedByAccountId = lastChangedByAccountId;
    }

    public int getHelperId() {
        return _helperId;
    }

    public void setHelperId(int helperId) {
        this._helperId = helperId;
    }

    public WorkflowStatus getStatus() {
        return _status;
    }

    public void setStatus(WorkflowStatus status) {
        this._status = status;
    }

    public Date getCreatedAt() {
        return _createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this._createdAt = createdAt;
    }

    public Date getSealedAt() {
        return _sealedAt;
    }

    public void setSealedAt(Date sealedAt) {
        this._sealedAt = sealedAt;
    }

    public Date getLastModifiedAt() {
        return _lastModifiedAt;
    }

    public void setLastModifiedAt(Date lastModifiedAt) {
        this._lastModifiedAt = lastModifiedAt;
    }

    public Date getDateOfReview() {
        return _dateOfReview;
    }

    public void setDateOfReview(Date dateOfReview) {
        this._dateOfReview = dateOfReview;
    }

    public Date getDateCorrectionRequested() {
        return _dateCorrectionRequested;
    }

    public void setDateCorrectionRequested(Date dateCorrectionRequested) {
        this._dateCorrectionRequested = dateCorrectionRequested;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public void setDisplayName(String displayName) {
        this._displayName = displayName;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        this._name = name;
    }

    public String getAltName() {
        return _altName;
    }

    public void setAltName(String altName) {
        this._altName = altName;
    }

    public int getIk() {
        return _ik;
    }

    public void setIk(int ik) {
        this._ik = ik;
    }

    public String getIkName() {
        return _ikName;
    }

    public void setIkName(String ikName) {
        this._ikName = ikName;
    }

    public int getGender() {
        return _gender;
    }

    public void setGender(int gender) {
        this._gender = gender;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        this._title = title;
    }

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        this._firstName = firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        this._lastName = lastName;
    }

    public String getDivision() {
        return _division;
    }

    public void setDivision(String division) {
        this._division = division;
    }

    public int getRoleId() {
        return _roleId;
    }

    public void setRoleId(int roleId) {
        this._roleId = roleId;
    }

    public String getStreet() {
        return _street;
    }

    public void setStreet(String street) {
        this._street = street;
    }

    public String getPostalCode() {
        return _postalCode;
    }

    public void setPostalCode(String postalCode) {
        this._postalCode = postalCode;
    }

    public String getTown() {
        return _town;
    }

    public void setTown(String town) {
        this._town = town;
    }

    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        this._phone = phone;
    }

    public String getFax() {
        return _fax;
    }

    public void setFax(String fax) {
        this._fax = fax;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        this._email = email;
    }

    public PsyNubRequestData getProposalData() {
        return _requestData;
    }

    public void setRequestData(PsyNubRequestData requestData) {
        requestData.setPsyNubRequest(this);
        this._requestData = requestData;
    }

    public PsyNubRequestDateValue getDateValue(PsyNubDateFields field) {
        Optional<PsyNubRequestDateValue> first = _requestDateValues.stream().filter(c -> c.getField().equals(field))
                .findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            throw new IllegalArgumentException("Unknown PsyNubDateFields type: " + field.name());
        }
    }

    public PsyNubRequestNumberValue getNumberValue(PsyNubNumberFields field) {
        Optional<PsyNubRequestNumberValue> first = _requestNumberValues.stream().filter(c -> c.getField().equals(field))
                .findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            throw new IllegalArgumentException("Unknown PsyNubNumberFields type: " + field.name());
        }
    }

    public PsyNubRequestMoneyValue getMoneyValue(PsyNubMoneyFields field) {
        Optional<PsyNubRequestMoneyValue> first = _requestMoneyValues.stream().filter(c -> c.getField().equals(field))
                .findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            throw new IllegalArgumentException("Unknown PsyNubMoneyFields type: " + field.name());
        }
    }

    public void addNewPsyNubProposalDateValue(PsyNubRequestDateValue value) {
        value.setPsyNubRequest(this);
        _requestDateValues.add(value);
    }

    public void addNewPsyNubProposalMoneyValue(PsyNubRequestMoneyValue value) {
        value.setPsyNubRequest(this);
        _requestMoneyValues.add(value);
    }

    public void addNewPsyNubProposalNumberValue(PsyNubRequestNumberValue value) {
        value.setPsyNubRequest(this);
        _requestNumberValues.add(value);
    }

    public void addDocument(PsyNubRequestDocument doc) {
        doc.setPsyNubRequest(this);
        _requestDocuments.add(doc);
    }

    public void removeDocument(PsyNubRequestDocument doc) {
        _requestDocuments.remove(doc);
    }

    public List<PsyNubRequestDocument> getProposalDocuments() {
        return _requestDocuments;
    }

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsyNubRequest that = (PsyNubRequest) o;
        return _id == that._id &&
                _version == that._version &&
                _targetYear == that._targetYear &&
                _createdByAccountId == that._createdByAccountId &&
                _lastChangedByAccountId == that._lastChangedByAccountId &&
                _helperId == that._helperId &&
                _ik == that._ik &&
                _gender == that._gender &&
                _roleId == that._roleId &&
                Objects.equals(_externalStatus, that._externalStatus) &&
                Objects.equals(_errorText, that._errorText) &&
                _status == that._status &&
                Objects.equals(_createdAt, that._createdAt) &&
                Objects.equals(_sealedAt, that._sealedAt) &&
                Objects.equals(_lastModifiedAt, that._lastModifiedAt) &&
                Objects.equals(_dateOfReview, that._dateOfReview) &&
                Objects.equals(_dateCorrectionRequested, that._dateCorrectionRequested) &&
                Objects.equals(_displayName, that._displayName) &&
                Objects.equals(_name, that._name) &&
                Objects.equals(_altName, that._altName) &&
                Objects.equals(_ikName, that._ikName) &&
                Objects.equals(_title, that._title) &&
                Objects.equals(_firstName, that._firstName) &&
                Objects.equals(_lastName, that._lastName) &&
                Objects.equals(_division, that._division) &&
                Objects.equals(_street, that._street) &&
                Objects.equals(_postalCode, that._postalCode) &&
                Objects.equals(_town, that._town) &&
                Objects.equals(_phone, that._phone) &&
                Objects.equals(_fax, that._fax) &&
                Objects.equals(_email, that._email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _version, _externalStatus, _errorText, _targetYear, _createdByAccountId, _lastChangedByAccountId, _helperId,
                _status, _createdAt, _sealedAt, _lastModifiedAt, _dateOfReview, _dateCorrectionRequested, _displayName, _name, _altName,
                _ik, _ikName, _gender, _title, _firstName, _lastName, _division, _roleId, _street, _postalCode, _town, _phone, _fax, _email);
    }
}
