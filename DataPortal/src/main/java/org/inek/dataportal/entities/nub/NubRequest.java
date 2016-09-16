/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//../Licenses/license-default.txt
package org.inek.dataportal.entities.nub;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Version;
import javax.validation.constraints.Size;
import org.inek.dataportal.enums.WorkflowStatus;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "NubProposal")
public class NubRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nubId")
    private int _id = -1;

    // <editor-fold defaultstate="collapsed" desc="Property Version">
    @Column(name = "nubVersion")
    @Version
    private int _version;
    // </editor-fold>

    @Column(name = "nubTargetYear")
    private int _targetYear = 1 + Calendar.getInstance().get(Calendar.YEAR);

    @Column(name = "nubAccountId")
    private Integer _accountId;

    @Column(name = "nubStatus")
    private int _status = -1;

    @Column(name = "nubCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate = null;

    @Column(name = "nubDateSealed")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _dateSealed = null;

    @Column(name = "nubCorrectionRequested")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _dateCorrectionRequested = null;

    public Date getDateCorrectionRequested() {
        return _dateCorrectionRequested;
    }

    public void setDateCorrectionRequested(Date dateCorrectionRequested) {
        _dateCorrectionRequested = dateCorrectionRequested;
    }

    @Documentation(name = "letzte Änderung")
    @Column(name = "nubLastModified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastModified = null;

    @Documentation(key = "lblIK")
    @Column(name = "nubIk")
    private Integer _ik = -1;

    @Column(name = "nubIkName")
    private String _ikName = "";

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

    // <editor-fold defaultstate="collapsed" desc="Property FormFillHelper">
    @Documentation(key = "lblFillHelper")
    @Column(name = "nubFormFillHelper")
    private String _formFillHelper = "";

    @Size(max=1000)
    public String getFormFillHelper() {
        return _formFillHelper;
    }

    public void setFormFillHelper(String formFillHelper) {
        _formFillHelper = formFillHelper;
    }
    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="Property Name">
    @Documentation(key = "lblAppellation")
    @Column(name = "nubName")
    private String _name = "";

    @Size(max=200)
    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }
    // </editor-fold>

    @Documentation(key = "lblAltAppellation")
    @Column(name = "nubAltName")
    private String _altName = "";
    
    // <editor-fold defaultstate="collapsed" desc="Property FormerRequest">
    @Documentation(key = "lblFormerRequestShort", translateValue = "255=lblMissingData; 0=lblNo; 1=lblYes")
    @Column(name = "nubFormerRequest")
    private byte _formerRequest = (byte) 255;

    public byte getFormerRequest() {
        return _formerRequest;
    }

    public void setFormerRequest(byte formerRequest) {
        _formerRequest = formerRequest;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property FormerExternalId">
    @Documentation(key = "lblFormerExternalId")
    @Column(name = "nubFormerExternalId")
    private String _formerExternalId = "";

    public String getFormerExternalId() {
        return _formerExternalId;
    }

    public void setFormerExternalId(String formerExternalId) {
        _formerExternalId = formerExternalId;
    }
    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="Property MedicalDevice">
    @Documentation(key = "lblMedicalDeviceShort", translateValue = "255=lblMissingData; 0=lblNo; 1=lblYes")
    @Column(name = "nubMedicalDevice")
    private byte _medicalDevice = (byte) 255;

    public byte getMedicalDevice() {
        return _medicalDevice;
    }

    public void setMedicalDevice(byte medicalDevice) {
        _medicalDevice = medicalDevice;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property RiscClass">
    @Documentation(key = "lblRiscClass", translateValue = "255=lblMissingData; 0=lblNo; 1=lblYes; 2=lblOther")
    @Column(name = "nubRiscClass")
    private byte _riscClass = (byte) 255;

    public byte getRiscClass() {
        return _riscClass;
    }

    public void setRiscClass(byte riscClass) {
        _riscClass = riscClass;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property RiscClassComment">
    @Documentation(key = "lblComment")
    @Column(name = "nubRiscClassComment")
    private String _riscClassComment = "";

    @Size(max = 200)
    public String getRiscClassComment() {
        return _riscClassComment;
    }

    public void setRiscClassComment(String riscClassComment) {
        _riscClassComment = riscClassComment;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property TradeName">
    @Documentation(key = "lblTradeNameShort")
    @Column(name = "nubTradeName")
    private String _tradeName = "";

    @Size(max = 200)
    public String getTradeName() {
        return _tradeName;
    }

    public void setTradeName(String tradeName) {
        _tradeName = tradeName;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property CeMark">
    @Documentation(key = "lblCeMarkShort")
    @Column(name = "nubCeMark")
    private String _ceMark = "";

    @Size(max = 200)
    public String getCeMark() {
        return _ceMark;
    }

    public void setCeMark(String ceMark) {
        _ceMark = ceMark;
    }
    // </editor-fold>


    
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
    private String _hospitalCount = "";

    // <editor-fold defaultstate="collapsed" desc="Property PatientsLastYear">
    @Documentation(key = "lblPatientsLastYear")
    @Column(name = "nubPatientsLastYear")
    private String _patientsLastYear = "";

    @Size(max=1000)
    public String getPatientsLastYear() {
        return _patientsLastYear;
    }

    public void setPatientsLastYear(String patientsLastYear) {
        _patientsLastYear = patientsLastYear;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PatientsThisYear">
    @Documentation(key = "lblPatientsThisYear")
    @Column(name = "nubPatientsThisYear")
    private String _patientsThisYear = "";

    @Size(max=1000)
    public String getPatientsThisYear() {
        return _patientsThisYear;
    }

    public void setPatientsThisYear(String patientsThisYear) {
        _patientsThisYear = patientsThisYear;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property PatientsFuture">
    @Documentation(key = "lblPatientsFuture")
    @Column(name = "nubPatientsFuture")
    private String _patientsFuture = "";

    @Size(max=1000)
    public String getPatientsFuture() {
        return _patientsFuture;
    }

    public void setPatientsFuture(String patientsFuture) {
        _patientsFuture = patientsFuture;
    }
    // </editor-fold>

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

    @Documentation(key = "lblNoProcs")
    @Column(name = "nubHasNoProcs")
    private boolean _hasNoProcs;

    public boolean isHasNoProcs() {
        return _hasNoProcs;
    }

    public void setHasNoProcs(boolean hasNoProcs) {
        _hasNoProcs = hasNoProcs;
    }

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

    @Documentation(key = "lblDisplayName", omitAlways = true)
    @Column(name = "nubDisplayName")
    private String _displayName = "";

    @Documentation(key = "lblUserComment", omitAlways = true)
    @Column(name = "nubUserComment")
    private String _userComment = "";

    @Documentation(key = "lblNubStatus", rank = 20)
    @Column(name = "nubExternalStatus")
    private String _externalState = "";

    // <editor-fold defaultstate="collapsed" desc="Property Documents">
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "npdNubProposalId", referencedColumnName = "nubId")
    private List<NubRequestDocument> _documents = new ArrayList<>();

    public List<NubRequestDocument> getDocuments() {
        return _documents;
    }

    public void setDocuments(List<NubRequestDocument> documents) {
        _documents = documents;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    @Documentation(name = "Verfahrens-Nr.", omitOnEmpty = true, rank = 0)
    public String getExternalId() {
        return _status == WorkflowStatus.CorrectionRequested.getValue() || _status >= WorkflowStatus.Provided.getValue() ? "N" + _id : "";
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

    @Documentation(name = "Bearbeitungsstatus", rank = 10)
    public WorkflowStatus getStatus() {
        return WorkflowStatus.fromValue(_status);
    }

    public void setStatus(int value) {
        _status = value;
    }

    public void setStatus(WorkflowStatus status) {
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
        if (!(object instanceof NubRequest)) {
            return false;
        }
        NubRequest other = (NubRequest) object;
        return _id == other._id;
    }

    @Override
    public String toString() {
        return "org.inek.entities.NubRequest[id=" + _id + "]";
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
        if (getDateSealed() == null && getStatus() == WorkflowStatus.Provided) {
            _dateSealed = Calendar.getInstance().getTime();
        }
    }

    public boolean isSealed() {
        return getStatus().getValue() >= WorkflowStatus.Provided.getValue();
    }

}
