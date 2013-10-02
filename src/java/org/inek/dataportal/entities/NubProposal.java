/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.inek.dataportal.enums.NubStatus;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "NubProposal")
public class NubProposal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nubId")
    private Integer _nubId;

    @Column(name = "nubTargetYear")
    private int _targetYear = 1 + Calendar.getInstance().get(Calendar.YEAR);

    @Column(name = "nubAccountId")
    private Integer _accountId;

    @Column(name = "nubStatus")
    private int _status;

    @Column(name = "nubCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate = null;

    @Column(name = "nubDateSealed")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _dateSealed = null;

    @Documentation(name = "letzte Änderung")
    @Column(name = "nubLastModified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastModified = null;

    @Documentation(key = "lblIK")
    @Column(name = "nubIk")
    private Integer _ik = -1;
    
    @Column(name = "nubIkName")
    private String _ikName="";

    //@Documentation(name="Anrede")
    @Column(name = "nubGender")
    private int _gender;

    @Documentation(key = "lblTitle")
    @Column(name = "nubTitle")
    private String _title = "";

    @Documentation(key = "lblFirstName")
    @Column(name = "nubFirstName")
    private String _firstName = "";

    @Documentation(key = "lblLastName")
    @Column(name = "nubLastName")
    private String _lastName = "";

    @Documentation(key = "lblDivision")
    @Column(name = "nubDivision")
    private String _division = "";

    //@Documentation(key = "lblContactRole")
    @Column(name = "nubRoleId")
    private int _roleId;

    @Documentation(key = "lblStreet")
    @Column(name = "nubStreet")
    private String _street = "";

    @Documentation(key = "lblPostalCode")
    @Column(name = "nubPostalCode")
    private String _postalCode = "";

    @Documentation(key = "lblTown")
    @Column(name = "nubTown")
    private String _town = "";

    @Documentation(key = "lblPhone")
    @Column(name = "nubPhone")
    private String _phone = "";

    @Documentation(key = "lblFax")
    @Column(name = "nubFax")
    private String _fax = "";

    @Documentation(key = "lblEMail")
    @Column(name = "nubEmail")
    private String _email = "";
    
    @Documentation(key = "lblFillHelper")
    @Column(name = "nubFormFillHelper")
    private String _formFillHelper = "";
    
    @Documentation(key = "lblAppellation")
    @Column(name = "nubName")
    private String _name = "";
    
    @Documentation(key = "lblAltAppellation")
    @Column(name = "nubAltName")
    private String _altName = "";
    
    @Documentation(key = "lblDescription")
    @Column(name = "nubDescription")
    private String _description = "";
    
    @Documentation(key = "lblIndication")
    @Column(name = "nubIndication")
    private String _indication = "";
    
    @Documentation(key = "lblNubReplacementPrint")
    @Column(name = "nubReplacement")
    private String _replacement = "";
    
    @Documentation(key = "lblWhatsNew")
    @Column(name = "nubWhatsNew")
    private String _whatsNew = "";
    
    @Documentation(key = "lblLos")
    @Column(name = "nubLos")
    private String _los = "";
    
    @Documentation(key = "lblInGermanySince")
    @Column(name = "nubInGermanySince")
    private String _inGermanySince = "";

    @Column(name = "nubInGermanyYear")
    private int _inGermanyYear;

    @Column(name = "nubInGermanyMonth")
    private int _inGermanyMonth;
    
    @Documentation(key = "lblMedApproved")
    @Column(name = "nubMedApproved")
    private String _medApproved = "";

    @Column(name = "nubMedApprovedYear")
    private int _medApprovedYear;

    @Column(name = "nubMedApprovedMonth")
    private int _medApprovedMonth;

    @Column(name = "nubMedCountryCode")
    private int _medCountryCode;

    @Column(name = "nubMedCountryName")
    private String _medCountryName = "";
    
    @Documentation(key = "lblMethodInHouse")
    @Column(name = "nubInHouseSince")
    private String _inHouseSince = "";

    @Column(name = "nubInHouseYear")
    private int _inHouseYear;

    @Column(name = "nubInHouseMonth")
    private int _inHouseMonth;
    
    @Documentation(key = "lblMethodInHouses")
    @Column(name = "nubHospitalCount")
    private String _hospitalCount="";
    
    @Documentation(key = "lblPatientsLastYear")
    @Column(name = "nubPatientsLastYear")
    private String _patientsLastYear = "";
    
    @Documentation(key = "lblPatientsThisYear")
    @Column(name = "nubPatientsThisYear")
    private String _patientsThisYear = "";
    
    @Documentation(key = "lblPatientsFuture")
    @Column(name = "nubPatientsFuture")
    private String _patientsFuture = "";
    
    @Documentation(key = "lblAddCosts")
    @Column(name = "nubAddCosts")
    private String _addCosts = "";
    
    @Documentation(key = "lblWhyNotRepresented")
    @Column(name = "nubWhyNotRepresented")
    private String _whyNotRepresented = "";
    
    @Documentation(key = "lblRequestedEarlierSelf")
    @Column(name = "nubRequestedEarlierSelf")
    private boolean _isRequestedEarlierSelf;
    
    @Documentation(key = "lblRequestedEarlierOther")
    @Column(name = "nubRequestedEarlierOther")
    private boolean _isRequestedEarlierOther;
    
    @Documentation(key = "lblNubProceduresPrint")
    @Column(name = "nubOps")
    private String _procedures = "";
    
    @Documentation(key = "lblDrgs")
    @Column(name = "nubDrgs")
    private String _drgs = "";

    @Column(name = "nubHelperId")
    private int _helperId;
    
    @Documentation(key = "lblProxyIkPrint")
    @Column(name = "nubProxyIKs")
    private String _proxyIKs = "";
    
    @Column(name = "nubDateOfReview")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _dateOfReview;
    
    @Documentation(key = "lblProcedures")
    @Column(name = "nubProcs")
    private String _procs = "";
    
    @Documentation(key = "lblNubErrorText", omitOnEmpty = true)
    @Column(name = "nubErrorText")
    private String _errorText = "";
    
    @Column(name = "nubByEmail")
    private boolean _byEmail;
    
    @Column(name = "nubCreatedBy")
    private int _createdBy;
    
    @Column(name = "nubLastChangedBy")
    private int _lastChangedBy;
    
    @Column(name = "nubSealedBy")
    private int _sealedBy;
    
    @Column(name = "nubDisplayName")
    private String _displayName = "";
    
    @Column(name = "nubUserComment")
    private String _userComment = "";

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Integer getNubId() {
        return _nubId;
    }

    public void setNubId(Integer nubId) {
        _nubId = nubId;
    }

    public int getTargetYear() {
        return _targetYear;
    }

    public void setTargetYear(int _targetYear) {
        this._targetYear = _targetYear;
    }

    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer accountId) {
        _accountId = accountId;
    }

    @Documentation(name="Bearbeitungsstatus", rank = 10)
    public NubStatus getStatus() {
        return NubStatus.fromValue(_status);
    }

    public void setStatus(int value) {
        _status = value;
    }

    public void setStatus(NubStatus status) {
        _status = status.getValue();
    }

    public Date getCreationDate() {
        return _creationDate;
    }

    public void setCreationDate(Date created) {
        _creationDate = created;
    }

    public Date getDateSealed() {
        return _dateSealed;
    }

    public void setDateSealed(Date dateSealed) {
        _dateSealed = dateSealed;
    }

    public Date getLastModified() {
        return _lastModified;
    }

    public void setLastModified(Date lastModified) {
        _lastModified = lastModified;
    }

    public Integer getIk() {
        return _ik == null || _ik < 0 ? null : _ik;
    }

    public void setIk(Integer ik) {
        _ik = ik == null ? -1 : ik;
    }

    public String getIkName() {
        return _ikName;
    }

    public void setIkName(String hospitalName) {
        this._ikName = hospitalName;
    }

    public int getGender() {
        return _gender;
    }

    public void setGender(int gender) {
        _gender = gender;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String title) {
        _title = title;
    }

    public String getFirstName() {
        return _firstName;
    }

    public void setFirstName(String firstName) {
        _firstName = firstName;
    }

    public String getLastName() {
        return _lastName;
    }

    public void setLastName(String lastName) {
        _lastName = lastName;
    }

    public String getDivision() {
        return _division;
    }

    public void setDivision(String division) {
        _division = division;
    }

    public int getRoleId() {
        return _roleId;
    }

    public void setRoleId(int roleId) {
        _roleId = roleId;
    }

    public String getStreet() {
        return _street;
    }

    public void setStreet(String street) {
        _street = street;
    }

    public String getPostalCode() {
        return _postalCode;
    }

    public void setPostalCode(String postalCode) {
        _postalCode = postalCode;
    }

    public String getTown() {
        return _town;
    }

    public void setTown(String town) {
        _town = town;
    }

    public String getPhone() {
        return _phone;
    }

    public void setPhone(String phone) {
        _phone = phone;
    }

    public String getFax() {
        return _fax;
    }

    public void setFax(String fax) {
        _fax = fax;
    }

    public String getEmail() {
        return _email;
    }

    public void setEmail(String email) {
        _email = email;
    }

    public String getFormFillHelper() {
        return _formFillHelper;
    }

    public void setFormFillHelper(String formFillHelper) {
        _formFillHelper = formFillHelper;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getAltName() {
        return _altName;
    }

    public void setAltName(String altName) {
        _altName = altName;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getIndication() {
        return _indication;
    }

    public void setIndication(String indication) {
        _indication = indication;
    }

    public String getReplacement() {
        return _replacement;
    }

    public void setReplacement(String replacement) {
        _replacement = replacement;
    }

    public String getWhatsNew() {
        return _whatsNew;
    }

    public void setWhatsNew(String whatsNew) {
        _whatsNew = whatsNew;
    }

    public String getLos() {
        return _los;
    }

    public void setLos(String los) {
        _los = los;
    }

    public String getInGermanySince() {
        return _inGermanySince;
    }

    public void setInGermanySince(String inGermanySince) {
        _inGermanySince = inGermanySince;
    }

    public int getInGermanyYear() {
        return _inGermanyYear;
    }

    public void setInGermanyYear(int inGermanyYear) {
        _inGermanyYear = inGermanyYear;
    }

    public int getInGermanyMonth() {
        return _inGermanyMonth;
    }

    public void setInGermanyMonth(int inGermanyMonth) {
        _inGermanyMonth = inGermanyMonth;
    }

    public String getMedApproved() {
        return _medApproved;
    }

    public void setMedApproved(String medApproved) {
        _medApproved = medApproved;
    }

    public int getMedApprovedYear() {
        return _medApprovedYear;
    }

    public void setMedApprovedYear(int medApprovedYear) {
        _medApprovedYear = medApprovedYear;
    }

    public int getMedApprovedMonth() {
        return _medApprovedMonth;
    }

    public void setMedApprovedMonth(int medApprovedMonth) {
        _medApprovedMonth = medApprovedMonth;
    }

    public int getMedCountryCode() {
        return _medCountryCode;
    }

    public void setMedCountryCode(int medCountryCode) {
        _medCountryCode = medCountryCode;
    }

    public String getMedCountryName() {
        return _medCountryName;
    }

    public void setMedCountryName(String medCountryName) {
        _medCountryName = medCountryName;
    }

    public String getInHouseSince() {
        return _inHouseSince;
    }

    public void setInHouseSince(String inHouseSince) {
        _inHouseSince = inHouseSince;
    }

    public int getInHouseYear() {
        return _inHouseYear;
    }

    public void setInHouseYear(int inHouseYear) {
        _inHouseYear = inHouseYear;
    }

    public int getInHouseMonth() {
        return _inHouseMonth;
    }

    public void setInHouseMonth(int inHouseMonth) {
        _inHouseMonth = inHouseMonth;
    }

    public String getHospitalCount() {
        return _hospitalCount;
    }

    public void setHospitalCount(String hospitalCount) {
        _hospitalCount = hospitalCount;
    }

    public String getPatientsFuture() {
        return _patientsFuture;
    }

    public void setPatientsFuture(String patientsFuture) {
        _patientsFuture = patientsFuture;
    }

    public String getPatientsThisYear() {
        return _patientsThisYear;
    }

    public void setPatientsThisYear(String patientsThisYear) {
        _patientsThisYear = patientsThisYear;
    }

    public String getPatientsLastYear() {
        return _patientsLastYear;
    }

    public void setPatientsLastYear(String patientsLastYear) {
        _patientsLastYear = patientsLastYear;
    }

    public String getAddCosts() {
        return _addCosts;
    }

    public void setAddCosts(String higherCosts) {
        _addCosts = higherCosts;
    }

    public String getWhyNotRepresented() {
        return _whyNotRepresented;
    }

    public void setWhyNotRepresented(String whyNotRepresented) {
        _whyNotRepresented = whyNotRepresented;
    }

    public boolean getIsRequestedEarlierSelf() {
        return _isRequestedEarlierSelf;
    }

    public void setIsRequestedEarlierSelf(boolean isRequestedEarlierSelf) {
        _isRequestedEarlierSelf = isRequestedEarlierSelf;
    }

    public boolean getRequestedEarlierOther() {
        return _isRequestedEarlierOther;
    }

    public void setRequestedEarlierOther(boolean isRequestedEarlierOther) {
        _isRequestedEarlierOther = isRequestedEarlierOther;
    }

    public String getProcedures() {
        return _procedures;
    }

    public void setProcedures(String procedures) {
        _procedures = procedures;
    }

    public String getProcs() {
        return _procs;
    }

    public void setProcs(String procs) {
        this._procs = procs;
    }

    public String getDrgs() {
        return _drgs;
    }

    public void setDrgs(String drgs) {
        _drgs = drgs;
    }

    public int getHelperId() {
        return _helperId;
    }

    public void setHelperId(int _helperId) {
        this._helperId = _helperId;
    }

    public String getProxyIKs() {
        return _proxyIKs;
    }

    public void setProxyIKs(String proxyIKs) {
        this._proxyIKs = proxyIKs;
    }

    public Date getDateOfReview() {
        return _dateOfReview;
    }

    public void setDateOfReview(Date dateOfReview) {
        this._dateOfReview = dateOfReview;
    }

    public String getErrorText() {
        return _errorText;
    }

    public void setErrorText(String errorText) {
        this._errorText = errorText;
    }

    public boolean isByEmail() {
        return _byEmail;
    }

    public void setByEmail(boolean byMail) {
        this._byEmail = byMail;
    }

    public int getCreatedBy() {
        return _createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this._createdBy = createdBy;
    }

    public int getLastChangedBy() {
        return _lastChangedBy;
    }

    public void setLastChangedBy(int lastChangedBy) {
        this._lastChangedBy = lastChangedBy;
    }

    public int getSealedBy() {
        return _sealedBy;
    }

    public void setSealedBy(int sealedBy) {
        this._sealedBy = sealedBy;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public void setDisplayName(String displayName) {
        this._displayName = displayName;
    }

    public String getUserComment() {
        return _userComment;
    }

    public void setUserComment(String userComment) {
        this._userComment = userComment;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_nubId != null ? _nubId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NubProposal)) {
            return false;
        }
        NubProposal other = (NubProposal) object;
        if ((_nubId == null && other.getNubId() != null) || (_nubId != null && !_nubId.equals(other.getNubId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.NubProposal[id=" + _nubId + "]";
    }

    // </editor-fold>
    @PrePersist
    private void prepareCreate() {
        _creationDate = Calendar.getInstance().getTime();
        prepareUpdate();
    }

    @PreUpdate
    private void prepareUpdate() {
        _lastModified = Calendar.getInstance().getTime();
        if (getDateSealed() == null && _status > 0) {
            _dateSealed = Calendar.getInstance().getTime();
        }
    }

}
