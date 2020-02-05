package org.inek.dataportal.care.proof.entity;

import org.inek.dataportal.care.entities.SensitiveDomain;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "ProofWardDept", schema = "care")
public class ProofWardDept {

    public ProofWardDept() {
    }

    public ProofWardDept(ProofWard proofWard) {
        this.proofWard = proofWard;
    }

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pwdId")
    private Integer id = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ProofWardId">
    @ManyToOne
    @JoinColumn(name = "pwdProofWardId")
    private ProofWard proofWard;

    public ProofWard getProofWard() {
        return proofWard;
    }

    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ValidFrom">
    @Column(name = "pwdValidFrom")
    private Date validFrom;

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }
    // </editor-fold>

    //<editor-fold desc="Property DeptNumbers">
    @Column(name = "pwdDeptNumbers")
    private String deptNumbers;

    public String getDeptNumbers() {
        return deptNumbers;
    }

    public void setDeptNumbers(String deptNumbers) {
        this.deptNumbers = deptNumbers;
    }
    //</editor-fold>

    //<editor-fold desc="Property DeptNames">
    @Column(name = "pwdDeptNames")
    private String deptNames;

    public String getDeptNames() {
        return deptNames;
    }

    public void setDeptNames(String deptNames) {
        this.deptNames = deptNames;
    }
    //</editor-fold>

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="mapProofWardDetailSensitiveDomain",
            schema = "care",
            joinColumns=@JoinColumn(name="proofWardDeptId", referencedColumnName="pwdId"),
            inverseJoinColumns=@JoinColumn(name="sensitiveDomainId", referencedColumnName="sdId"))
    private List<SensitiveDomain> sensitiveDomains = new ArrayList<>();

    private void addSensitiveDomain(SensitiveDomain sensitiveDomain) {
        sensitiveDomains.add(sensitiveDomain);
    }

    public List<SensitiveDomain> getSensitiveDomains() {
        return Collections.unmodifiableList(sensitiveDomains);
    }

    public void setSensitiveDomains(List<SensitiveDomain> sensitiveDomains) {
        this.sensitiveDomains = sensitiveDomains;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProofWardDept proofWardDept = (ProofWardDept) o;
        return proofWard == proofWardDept.proofWard &&
                id.equals(proofWardDept.id) &&
                validFrom.equals(proofWardDept.validFrom) &&
                deptNumbers.equals(proofWardDept.deptNumbers) &&
                deptNames.equals(proofWardDept.deptNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, proofWard, validFrom, deptNumbers, deptNames);
    }
}
