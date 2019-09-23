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
@Table(name = "NubRequestHistory", schema = "psy")
public class PsyNubRequestHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nrhId")
    private int _id;

    @Column(name = "nrhNubId")
    private int _psyNubRequestId;

    @Column(name = "nrhLastChangedByAccountId")
    private int _lastChangedByAccountId;

    @Column(name = "nrhHelperId")
    private int _helperId;

    @Column(name = "nrhLastModifiedAt")
    private Date _lastModifiedAt;

    @Column(name = "nrhDisplayName")
    private String _displayName = "";

    @Column(name = "nrhName")
    private String _name = "";

    @Column(name = "nrhAltName")
    private String _altName = "";

    @Column(name = "nrhIk")
    private int _ik;

    @Column(name = "nrhIkName")
    private String _ikName = "";

    @Column(name = "nrhGender")
    private int _gender;

    @Column(name = "nrhTitle")
    private String _title = "";

    @Column(name = "nrhFirstName")
    private String _firstName = "";

    @Column(name = "nrhLastName")
    private String _lastName = "";

    @Column(name = "nrhDivision")
    private String _division = "";

    @Column(name = "nrhRoleId")
    private int _roleId;

    @Column(name = "nrhStreet")
    private String _street = "";

    @Column(name = "nrhPostalCode")
    private String _postalCode = "";

    @Column(name = "nrhTown")
    private String _town = "";

    @Column(name = "nrhPhone")
    private String _phone = "";

    @Column(name = "nrhFax")
    private String _fax = "";

    @Column(name = "nrhEmail")
    private String _email = "";

    @OneToOne(mappedBy = "_psyNubRequest", cascade = CascadeType.ALL)
    private PsyNubRequestHistoryData _requestData;

    @OneToMany(mappedBy = "_psyNubRequest",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "dvNubRequestHistoryId")
    private List<PsyNubRequestHistoryDateValue> _requestDateValues = new ArrayList<>();

    @OneToMany(mappedBy = "_psyNubRequest",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "nvNubRequestHistoryId")
    private List<PsyNubRequestHistoryNumberValue> _requestNumberValues = new ArrayList<>();

    @OneToMany(mappedBy = "_psyNubRequest",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "mvNubRequestHistoryId")
    private List<PsyNubRequestHistoryMoneyValue> _requestMoneyValues = new ArrayList<>();

    @OneToMany(mappedBy = "_psyNubRequest",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JoinColumn(name = "npdNubRequesHistorytId")
    private List<PsyNubRequestHistoryDocument> _requestDocuments = new ArrayList<>();

    public PsyNubRequestHistoryDateValue getDateValue(PsyNubDateFields field) {
        Optional<PsyNubRequestHistoryDateValue> first = _requestDateValues.stream().filter(c -> c.getField().equals(field))
                .findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            throw new IllegalArgumentException("Unknown PsyNubDateFields type: " + field.name());
        }
    }

    public PsyNubRequestHistoryNumberValue getNumberValue(PsyNubNumberFields field) {
        Optional<PsyNubRequestHistoryNumberValue> first = _requestNumberValues.stream().filter(c -> c.getField().equals(field))
                .findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            throw new IllegalArgumentException("Unknown PsyNubNumberFields type: " + field.name());
        }
    }

    public PsyNubRequestHistoryMoneyValue getMoneyValue(PsyNubMoneyFields field) {
        Optional<PsyNubRequestHistoryMoneyValue> first = _requestMoneyValues.stream().filter(c -> c.getField().equals(field))
                .findFirst();
        if (first.isPresent()) {
            return first.get();
        } else {
            throw new IllegalArgumentException("Unknown PsyNubMoneyFields type: " + field.name());
        }
    }

    public List<PsyNubRequestHistoryDocument> getProposalDocuments() {
        return _requestDocuments;
    }

    public int getPsyNubRequestId() {
        return _psyNubRequestId;
    }

    public int getLastChangedByAccountId() {
        return _lastChangedByAccountId;
    }

    public int getHelperId() {
        return _helperId;
    }

    public Date getLastModifiedAt() {
        return _lastModifiedAt;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public String getName() {
        return _name;
    }

    public String getAltName() {
        return _altName;
    }

    public int getIk() {
        return _ik;
    }

    public String getIkName() {
        return _ikName;
    }

    public int getGender() {
        return _gender;
    }

    public String getTitle() {
        return _title;
    }

    public String getFirstName() {
        return _firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public String getDivision() {
        return _division;
    }

    public int getRoleId() {
        return _roleId;
    }

    public String getStreet() {
        return _street;
    }

    public String getPostalCode() {
        return _postalCode;
    }

    public String getTown() {
        return _town;
    }

    public String getPhone() {
        return _phone;
    }

    public String getFax() {
        return _fax;
    }

    public String getEmail() {
        return _email;
    }

    public PsyNubRequestHistoryData getRequestData() {
        return _requestData;
    }

    public List<PsyNubRequestHistoryDateValue> getRequestDateValues() {
        return _requestDateValues;
    }

    public List<PsyNubRequestHistoryNumberValue> getRequestNumberValues() {
        return _requestNumberValues;
    }

    public List<PsyNubRequestHistoryMoneyValue> getRequestMoneyValues() {
        return _requestMoneyValues;
    }

    public List<PsyNubRequestHistoryDocument> getRequestDocuments() {
        return _requestDocuments;
    }

    @SuppressWarnings("checkstyle:CyclomaticComplexity")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PsyNubRequestHistory that = (PsyNubRequestHistory) o;
        return _id == that._id &&
                _psyNubRequestId == that._psyNubRequestId &&
                _lastChangedByAccountId == that._lastChangedByAccountId &&
                _helperId == that._helperId &&
                _ik == that._ik &&
                _gender == that._gender &&
                _roleId == that._roleId &&
                Objects.equals(_lastModifiedAt, that._lastModifiedAt) &&
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
                Objects.equals(_email, that._email) &&
                Objects.equals(_requestData, that._requestData) &&
                Objects.equals(_requestDateValues, that._requestDateValues) &&
                Objects.equals(_requestNumberValues, that._requestNumberValues) &&
                Objects.equals(_requestMoneyValues, that._requestMoneyValues) &&
                Objects.equals(_requestDocuments, that._requestDocuments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, _psyNubRequestId, _lastChangedByAccountId, _helperId, _lastModifiedAt, _displayName,
                _name, _altName, _ik, _ikName, _gender, _title, _firstName, _lastName, _division, _roleId, _street,
                _postalCode, _town, _phone, _fax, _email, _requestData, _requestDateValues, _requestNumberValues,
                _requestMoneyValues, _requestDocuments);
    }
}
