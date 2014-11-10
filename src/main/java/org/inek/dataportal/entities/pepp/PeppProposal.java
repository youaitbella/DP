package org.inek.dataportal.entities.pepp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import org.inek.dataportal.entities.common.ProcedureInfo;
import org.inek.dataportal.enums.PeppProposalCategory;
import org.inek.dataportal.utils.Documentation;

/**
 *
 * @author muellermi
 */
@Entity
@Table(name = "PeppProposal")
public class PeppProposal implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ppId")
    private Integer _peppProposalId;

    @Column(name = "ppAccountId")
    private Integer _accountId;

    @Column(name = "ppStatus")
    private int _status;

    @Documentation(key = "lblAppellation")
    @Column(name = "ppName")
    private String _name = "";

    @Documentation(key = "lblCategory")
    @Column(name = "ppCategory")
    @Enumerated(EnumType.STRING)
    private PeppProposalCategory _category = PeppProposalCategory.UNKNOWN;

    @Column(name = "ppCreated")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _creationDate = null;

    @Column(name = "ppDateSealed")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _dateSealed = null;

    @Column(name = "ppLastModified")
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date _lastModified = null;

    @Documentation(key = "lblProposalRequestor")
    @Column(name = "ppInstitute")
    private String _institute = "";

    @Column(name = "ppGender")
    private int _gender;

    @Documentation(key = "lblTitle")
    @Column(name = "ppTitle")
    private String _title = "";

    @Documentation(key = "lblFirstName")
    @Column(name = "ppFirstName")
    private String _firstName = "";

    @Documentation(key = "lblLastName")
    @Column(name = "ppLastName")
    private String _lastName = "";

    @Documentation(key = "lblDivision")
    @Column(name = "ppDivision")
    private String _division = "";

    @Documentation(key = "lblContactRole")
    @Column(name = "ppRoleId")
    private Integer _roleId = -1;

    @Documentation(key = "lblStreet")
    @Column(name = "ppStreet")
    private String _street = "";

    @Documentation(key = "lblPostalCode")
    @Column(name = "ppPostalCode")
    private String _postalCode = "";

    @Documentation(key = "lblTown")
    @Column(name = "ppTown")
    private String _town = "";

    @Documentation(key = "lblPhone")
    @Column(name = "ppPhone")
    private String _phone = "";

    @Documentation(key = "lblFax")
    @Column(name = "ppFax")
    private String _fax = "";

    @Documentation(key = "lblEMail")
    @Column(name = "ppEmail")
    private String _email = "";

    @Documentation(key = "lblProblem")
    @Column(name = "ppProblem")
    private String _problem = "";

    @Documentation(key = "lblSolution")
    @Column(name = "ppSolution")
    private String _solution = "";

    @Documentation(name = "PEPP")
    @Column(name = "ppPepp")
    private String _pepp = "";

    @Documentation(name = "Diagnos(en), Kode(s)")
    @Column(name = "ppDiagCodes")
    private String _diagCodes = "";

    @Documentation(name = "Diagnos(en), Text")
    @Column(name = "ppDiags")
    private String _diags = "";

    @Documentation(name = "Prozedur(en), Kode(s)")
    @Column(name = "ppProcCodes")
    private String _procCodes = "";

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "mapPeppProposalProcedure",
            joinColumns = @JoinColumn(name = "pppPeppProposalId"),
            inverseJoinColumns = @JoinColumn(name = "pppOpsId"))
    private List<ProcedureInfo> _procedures = new ArrayList<>();

    @Documentation(name = "Prozedur(en), Text")
    @Column(name = "ppProcs")
    private String _procs = "";

    @Column(name = "ppCodingPolicy")
    private String _codingPolicy = "";

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "ppdPeppProposalId", referencedColumnName = "ppId")
    private List<PeppProposalDocument> _documents = new ArrayList<>();

    @Documentation(name = "Dokumente, Post")
    @Column(name = "ppDocumentsOffline")
    private String _documentsOffline = "";

    @Column(name = "ppAnonymousData")
    private Boolean _anonymousData = false;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    @JoinColumn(name = "ppcPeppProposalId", referencedColumnName = "ppId")
    private List<PeppProposalComment> _comments = new ArrayList<>();

    // <editor-fold defaultstate="collapsed" desc="getter / setter Definition">
    public Integer getPeppProposalId() {
        return _peppProposalId;
    }

    public void setPeppProposalId(Integer id) {
        _peppProposalId = id;
    }

    @Documentation(name = "Verfahrensnummer", rank = 1)
    public String getExternalId() {
        if (_peppProposalId == null || _peppProposalId < 0 || _status < 1) {
            return "";
        }
        return "P" + _peppProposalId;
    }

    /**
     * Dummy setter, cause JSF needs on
     *
     *
     * @param dummy
     */
    public void setExternalId(String dummy) {
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

    public PeppProposalCategory getCategory() {
        return _category;
    }

    public void setCategory(PeppProposalCategory category) {
        _category = category;
    }

    public int getStatus() {
        return _status;
    }

    public void setStatus(int status) {
        _status = status;
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

    public Integer getRoleId() {
        return _roleId;
    }

    public void setRoleId(Integer roleId) {
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

    public String getProblem() {
        return _problem.replace((char) 7, '*').replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", "");  // to replace false characters stored by former char conversion
    }

    public void setProblem(String problem) {
        _problem = problem;
    }

    public String getSolution() {
        return _solution.replace((char) 7, '*').replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", "");
    }

    public void setSolution(String solution) {
        _solution = solution;
    }

    public String getPepp() {
        return _pepp;
    }

    public void setPepp(String pepp) {
        _pepp = pepp;
    }

    public String getDiags() {
        return _diags.replace((char) 7, '*').replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", "");
    }

    public void setDiags(String diags) {
        _diags = diags;
    }

    public String getDiagCodes() {
        return _diagCodes;
    }

    public void setDiagCodes(String diagCodes) {
        _diagCodes = diagCodes;
    }

    public String getProcs() {
        return _procs.replace((char) 7, '*').replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", "");
    }

    public void setProcs(String procs) {
        _procs = procs;
    }

    public String getProcCodes() {
        return _procCodes;
//        String procs = "";
//        for (ProcedureInfo procInfo : getProcedures()) {
//            procs += " " + procInfo.getCode();
//        }
//        return procs;
    }

    public void setProcCodes(String procCodes) {
        _procCodes = procCodes;
    }

    public List<ProcedureInfo> getProcedures() {
        return _procedures;
    }

    public void setProcedures(List<ProcedureInfo> procedures) {
        _procedures = procedures;
    }

    public String getCodingPolicy() {
        return _codingPolicy.replace((char) 7, '*').replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", "");
    }

    public void setCodingPolicy(String codingPolicy) {
        _codingPolicy = codingPolicy;
    }

    public List<PeppProposalDocument> getDocuments() {
        return _documents;
    }

    public void setDocuments(List<PeppProposalDocument> documents) {
        _documents = documents;
    }

    public String getDocumentsOffline() {
        return _documentsOffline.replace((char) 7, '*').replaceAll("[\\x00-\\x08\\x0b\\x0c\\x0e-\\x1f]", "");
    }

    public void setDocumentsOffline(String documentsOffline) {
        _documentsOffline = documentsOffline;
    }

    public Boolean isAnonymousData() {
        return _anonymousData;
    }

    public void setAnonymousData(Boolean anonymousData) {
        _anonymousData = anonymousData;
    }

    public List<PeppProposalComment> getComments() {
        return _comments;
    }

    public void setComments(List<PeppProposalComment> comments) {
        _comments = comments;
    }

    public Date getCreationDate() {
        return _creationDate;
    }

    public void setCreationDate(Date date) {
        _creationDate = date;
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

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="hashCode / equals / toString">
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (_peppProposalId != null ? _peppProposalId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PeppProposal)) {
            return false;
        }
        PeppProposal other = (PeppProposal) object;
        if ((_peppProposalId == null && other.getPeppProposalId() != null) || (_peppProposalId != null && !_peppProposalId.equals(other.getPeppProposalId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.inek.entities.PeppProposal[id=" + _peppProposalId + "]";
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
        if (getDateSealed() == null && getStatus() > 0) {
            _dateSealed = Calendar.getInstance().getTime();
        }
    }

}
