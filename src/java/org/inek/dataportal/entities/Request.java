package org.inek.dataportal.entities;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import org.inek.dataportal.enums.RequestCategory;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "Request")
public class Request implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reId")
    private Integer _requestId;
    
    @Column(name = "reAccountId")
    private Integer _accountId;
    
    @Column(name = "reIsComplete")
    private boolean _isComplete;
    
    @Documentation(name = "Benennung")
    @Column(name = "reName")
    private String _name = "";

    @Documentation(name = "Institut")
    @Column(name = "reInstitute")
    private String _institute = "";
    
    @Column(name = "reGender")
    private int _gender;
    
    @Documentation(name = "Titel")
    @Column(name = "reTitle")
    private String _title = "";
    
    @Documentation(name = "Vorname")
    @Column(name = "reFirstName")
    private String _firstName = "";
    
    @Documentation(name = "Name")
    @Column(name = "reLastName")
    private String _lastName = "";
    
    @Documentation(name = "Abteilung")
    @Column(name = "reDivision")
    private String _division = "";
    
    @Documentation(name = "Straße")
    @Column(name = "reStreet")
    private String _street = "";
    
    @Documentation(name = "Postleitzahl")
    @Column(name = "rePostalCode")
    private String _postalCode = "";
    
    @Documentation(name = "Ort")
    @Column(name = "reTown")
    private String _town = "";
    
    @Documentation(name = "Telefon")
    @Column(name = "rePhone")
    private String _phone = "";
    
    @Documentation(name = "Telefax")
    @Column(name = "reFax")
    private String _fax = "";
    
    @Documentation(name = "E-Mail")
    @Column(name = "reEmail")
    private String _email = "";
    @Column(name = "reDescription")
    private String _description = "";
    @Column(name = "reSolution")
    private String _solution = "";
    @Column(name = "reAlternativeSolution")
    private String _alternativeSolution = "";
    @Column(name = "reCategory")
    @Enumerated(EnumType.STRING)
    private RequestCategory _category = RequestCategory.UNKNOWN;
    @Column(name = "reCategoryOther")
    private String _categoryOther = "";
    @Column(name = "reReasonPointOfIssue")
    private Boolean _isReasonPointOfIssue = false;
    @Column(name = "reReasonCurrentSuit")
    private Boolean _isReasonCurrentSuit = false;
    @Column(name = "reReasonHeavyEncodingCase")
    private Boolean _isReasonHeavyEncodingCase = false;
    @Column(name = "reReasonHeavyEncodingIssue")
    private Boolean _isReasonHeavyEncodingIssue = false;
    @Column(name = "reReasonOther")
    private String _reasonOther = "";
    @Column(name = "reReasonNoActiveSuit")
    private Boolean _isReasonNoActiveSuit = false;
    @Column(name = "reRelevanceCurrent")
    private Integer _relevanceCurrent = -1;
    @Column(name = "reRelevancePast")
    private Integer _relevancePast = -1;
    @Column(name = "reRelevanceHospitals")
    private Integer _relevanceHospitals = -1;
    @Column(name = "reRelevanceReason")
    private String _relevanceReason = "";
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "rdRequestId", referencedColumnName="reId")
    private List<RequestDocument> _documents;
    @Column(name = "reDocumentsOffline")
    private String _documentsOffline = "";
    @Column(name = "reAnonymousData")
    private Boolean _isAnonymousData = false;
    @Column(name = "reCreationDate")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate = null;
    @Column(name = "reLastModified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastModified = null;
    
    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">

    public Integer getRequestId() {
        return _requestId;
    }

    public void setRequestId(Integer id) {
        _requestId = id;
    }
    public Integer getAccountId() {
        return _accountId;
    }

    public void setAccountId(Integer id) {
        _accountId = id;
    }

    public String getName() {
        return _name;
    }

    public void setName(String name) {
        _name = name;
    }

    public boolean isComplete() {
        return _isComplete;
    }

    public void setComplete(boolean isComplete) {
        _isComplete = isComplete;
    }

    public String getInstitute() {
        return _institute;
    }

    public void setInstitute(String institute) {
        _institute = institute;
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

    public String getDescription() {
        return _description;
    }

    public void setDescription(String description) {
        _description = description;
    }

    public String getSolution() {
        return _solution;
    }

    public void setSolution(String solution) {
        _solution = solution;
    }

    public String getAlternativeSolution() {
        return _alternativeSolution;
    }

    public void setAlternativeSolution(String alternativeSolution) {
        _alternativeSolution = alternativeSolution;
    }

    public RequestCategory getCategory() {
        return _category;
    }

    public void setCategory(RequestCategory category) {
        _category = category;
    }

    public String getCategoryOther() {
        return _categoryOther;
    }

    public void setCategoryOther(String categoryOther) {
        _categoryOther = categoryOther;
    }

    public Boolean isReasonPointOfIssue() {
        return _isReasonPointOfIssue;
    }

    public void setReasonPointOfIssue(Boolean reasonPointOfIssue) {
        _isReasonPointOfIssue = reasonPointOfIssue;
    }

    public Boolean isReasonCurrentSuit() {
        return _isReasonCurrentSuit;
    }

    public void setReasonCurrentSuit(Boolean reasonCurrentSuit) {
        _isReasonCurrentSuit = reasonCurrentSuit;
    }

    public Boolean isReasonHeavyEncodingCase() {
        return _isReasonHeavyEncodingCase;
    }

    public void setReasonHeavyEncodingCase(Boolean reasonHeavyEncodingCase) {
        _isReasonHeavyEncodingCase = reasonHeavyEncodingCase;
    }

    public Boolean isReasonHeavyEncodingIssue() {
        return _isReasonHeavyEncodingIssue;
    }

    public void setReasonHeavyEncodingIssue(Boolean reasonHeavyEncodingIssue) {
        _isReasonHeavyEncodingIssue = reasonHeavyEncodingIssue;
    }

    public String getReasonOther() {
        return _reasonOther;
    }

    public void setReasonOther(String reasonOther) {
        _reasonOther = reasonOther;
    }

    public Boolean isReasonNoActiveSuit() {
        return _isReasonNoActiveSuit;
    }

    public void setReasonNoActiveSuit(Boolean reasonNoActiveSuit) {
        _isReasonNoActiveSuit = reasonNoActiveSuit;
    }

    public Integer getRelevanceCurrent() {
        return _relevanceCurrent;
    }

    public void setRelevanceCurrent(Integer relevanceCurrent) {
        _relevanceCurrent = relevanceCurrent;
    }

    public Integer getRelevancePast() {
        return _relevancePast;
    }

    public void setRelevancePast(Integer relevancePast) {
        _relevancePast = relevancePast;
    }

    public Integer getRelevanceHospitals() {
        return _relevanceHospitals;
    }

    public void setRelevanceHospitals(Integer relevanceHospitals) {
        _relevanceHospitals = relevanceHospitals;
    }

    public String getRelevanceReason() {
        return _relevanceReason;
    }

    public void setRelevanceReason(String relevanceReason) {
        _relevanceReason = relevanceReason;
    }

    public List<RequestDocument> getDocuments() {
        return _documents;
    }

    public void setDocuments(List<RequestDocument> documents) {
        _documents = documents;
    }

    public String getDocumentsOffline() {
        return _documentsOffline;
    }

    public void setDocumentsOffline(String documentsOffline) {
        _documentsOffline = documentsOffline;
    }

    public Boolean isAnonymousData() {
        return _isAnonymousData;
    }

    public void setAnonymousData(Boolean anonymousData) {
        _isAnonymousData = anonymousData;
    }

    public Date getCreationDate() {
        return _creationDate;
    }
    
    public void setCreationDate(Date date){
        _creationDate = date;
    }

    public Date getLastModified() {
        return _lastModified;
    }


    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_requestId != null ? _requestId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Request)) {
            return false;
        }
        Request other = (Request) object;
        if ((_requestId == null && other.getRequestId() != null) || (_requestId != null && !_requestId.equals(other.getRequestId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.Request[id=" + _requestId + "]";
    }

    // </editor-fold>

    @PostLoad
    @PostPersist
    @PostUpdate
    public void gotData() {
        convert2null();
    }
    
    @PrePersist
    private void prepareCreate(){
        _creationDate = Calendar.getInstance().getTime();
        prepareUpdate();
    }
    @PreUpdate
    private void prepareUpdate() {
        _lastModified = Calendar.getInstance().getTime();
        convertNull();
    }

    private void convert2null(){
        if (_relevanceCurrent != null && _relevanceCurrent < 0){
            _relevanceCurrent = null;
        }
        if (_relevancePast != null && _relevancePast < 0){
            _relevancePast = null;
        }
        if (_relevanceHospitals != null && _relevanceHospitals < 0){
            _relevanceHospitals = null;
        }
    }

    private void convertNull(){
        if (_relevanceCurrent == null){
            _relevanceCurrent = -1;
        }
        if (_relevancePast == null){
            _relevancePast = -1;
        }
        if (_relevanceHospitals == null){
            _relevanceHospitals = -1;
        }
    }
    
}
