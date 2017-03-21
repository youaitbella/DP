/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.entities.nub;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import org.inek.dataportal.enums.WorkflowStatus;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "NubRequestHistory")
public class NubRequestHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nrhId")
    private int _id = -1;

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Property NubId">
    @Column(name = "nrhNubId")
    private int _nubId;

    public int getNubId() {
        return _nubId;
    }

    public void setNubId(int nubId) {
        _nubId = nubId;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Version">
    @Column(name = "nrhVersion")
    @Version
    private int _version;

    public int getVersion() {
        return _version;
    }

    public void setVersion(int version) {
        _version = version;
    }
    // </editor-fold>

    @Column(name = "nrhTargetYear")
    private int _targetYear = 1 + Calendar.getInstance().get(Calendar.YEAR);

    @Column(name = "nrhAccountId")
    private Integer _accountId;

    @Column(name = "nrhStatus")
    private int _status = -1;

    @Column(name = "nrhCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate = null;

    @Column(name = "nrhDateSealed")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _dateSealed = null;

    @Column(name = "nrhCorrectionRequested")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _dateCorrectionRequested = null;

    public Date getDateCorrectionRequested() {
        return _dateCorrectionRequested;
    }

    public void setDateCorrectionRequested(Date dateCorrectionRequested) {
        _dateCorrectionRequested = dateCorrectionRequested;
    }

    @Column(name = "nrhLastModified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastModified = null;

    @Column(name = "nrhIk")
    private Integer _ik = -1;

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

    @Column(name = "nrhFormFillHelper")
    private String _formFillHelper = "";

    @Column(name = "nrhName")
    private String _name = "";

    @Column(name = "nrhAltName")
    private String _altName = "";

    @Column(name = "nrhDescription")
    private String _description = "";

    @Column(name = "nrhIndication")
    private String _indication = "";

    @Column(name = "nrhReplacement")
    private String _replacement = "";

    @Column(name = "nrhWhatsNew")
    private String _whatsNew = "";

    @Column(name = "nrhLos")
    private String _los = "";

    @Column(name = "nrhInGermanySince")
    private String _inGermanySince = "";

    @Column(name = "nrhInGermanyYear")
    private int _inGermanyYear;

    @Column(name = "nrhInGermanyMonth")
    private int _inGermanyMonth;

    @Column(name = "nrhMedApproved")
    private String _medApproved = "";

    @Column(name = "nrhMedApprovedYear")
    private int _medApprovedYear;

    @Column(name = "nrhMedApprovedMonth")
    private int _medApprovedMonth;

    @Column(name = "nrhMedCountryCode")
    private int _medCountryCode;

    @Column(name = "nrhMedCountryName")
    private String _medCountryName = "";

    @Column(name = "nrhInHouseSince")
    private String _inHouseSince = "";

    @Column(name = "nrhInHouseYear")
    private int _inHouseYear;

    @Column(name = "nrhInHouseMonth")
    private int _inHouseMonth;

    @Column(name = "nrhHospitalCount")
    private String _hospitalCount = "";

    @Column(name = "nrhPatientsLastYear")
    private String _patientsLastYear = "";

    @Column(name = "nrhPatientsThisYear")
    private String _patientsThisYear = "";

    @Column(name = "nrhPatientsFuture")
    private String _patientsFuture = "";

    @Column(name = "nrhAddCosts")
    private String _addCosts = "";

    @Column(name = "nrhWhyNotRepresented")
    private String _whyNotRepresented = "";

    @Column(name = "nrhRequestedEarlierSelf")
    private boolean _isRequestedEarlierSelf;

    @Column(name = "nrhRequestedEarlierOther")
    private boolean _isRequestedEarlierOther;

    @Column(name = "nrhOps")
    private String _procedures = "";

    @Column(name = "nrhHasNoProcs")
    private boolean _hasNoProcs;

    public boolean isHasNoProcs() {
        return _hasNoProcs;
    }

    public void setHasNoProcs(boolean hasNoProcs) {
        _hasNoProcs = hasNoProcs;
    }

    @Column(name = "nrhDrgs")
    private String _drgs = "";

    @Column(name = "nrhHelperId")
    private int _helperId;

    @Column(name = "nrhProxyIKs")
    private String _proxyIKs = "";

    @Column(name = "nrhDateOfReview")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _dateOfReview;

    @Column(name = "nrhProcs")
    private String _procs = "";

    @Column(name = "nrhErrorText")
    private String _errorText = "";

    @Column(name = "nrhByEmail")
    private boolean _byEmail;

    @Column(name = "nrhCreatedBy")
    private int _createdBy;

    @Column(name = "nrhLastChangedBy")
    private int _lastChangedBy;

    @Column(name = "nrhSealedBy")
    private int _sealedBy;

    @Column(name = "nrhDisplayName")
    private String _displayName = "";

    @Column(name = "nrhUserComment")
    private String _userComment = "";

    @Column(name = "nrhExternalStatus")
    private String _externalState = "";

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public String getExternalId() {
        return _status == WorkflowStatus.CorrectionRequested.getId() || _status >= WorkflowStatus.Provided.getId() ? "N" + _id : "";
    }

    public int getTargetYear() {
        return _targetYear;
    }

    public void setTargetYear(int targetYear) {
        _targetYear = targetYear;
    }

    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer accountId) {
        _accountId = accountId;
    }

    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_status);
    }

    public void setStatus(int value) {
        _status = value;
    }

    public void setStatus(WorkflowStatus status) {
        _status = status.getId();
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
        _ikName = hospitalName;
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
        _procs = procs;
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

    public void setHelperId(int helperId) {
        _helperId = helperId;
    }

    public String getProxyIKs() {
        return _proxyIKs;
    }

    public void setProxyIKs(String proxyIKs) {
        _proxyIKs = proxyIKs;
    }

    public Date getDateOfReview() {
        return _dateOfReview;
    }

    public void setDateOfReview(Date dateOfReview) {
        _dateOfReview = dateOfReview;
    }

    public String getErrorText() {
        return _errorText;
    }

    public void setErrorText(String errorText) {
        _errorText = errorText;
    }

    public boolean isByEmail() {
        return _byEmail;
    }

    public void setByEmail(boolean byMail) {
        _byEmail = byMail;
    }

    public int getCreatedBy() {
        return _createdBy;
    }

    public void setCreatedBy(int createdBy) {
        _createdBy = createdBy;
    }

    public int getLastChangedBy() {
        return _lastChangedBy;
    }

    public void setLastChangedBy(int lastChangedBy) {
        _lastChangedBy = lastChangedBy;
    }

    public int getSealedBy() {
        return _sealedBy;
    }

    public void setSealedBy(int sealedBy) {
        _sealedBy = sealedBy;
    }

    public String getDisplayName() {
        return _displayName;
    }

    public void setDisplayName(String displayName) {
        _displayName = displayName;
    }

    public String getUserComment() {
        return _userComment;
    }

    public void setUserComment(String userComment) {
        _userComment = userComment;
    }

    public String getExternalState() {
        return _externalState == null ? "" : _externalState;
    }

    public void setExternalState(String externalState) {
        _externalState = externalState;
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        return _id;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NubRequestHistory)) {
            return false;
        }
        NubRequestHistory other = (NubRequestHistory) object;
        return _id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.entities.NubRequest[id=" + _id + "]";
    }

    // </editor-fold>
}
