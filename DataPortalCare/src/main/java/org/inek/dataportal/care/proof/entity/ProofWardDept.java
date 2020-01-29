package org.inek.dataportal.care.proof.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ProofWardDept", schema = "care")
public class ProofWardDept {

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pwdId")
    private Integer id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property ProofWardId">
    @Column(name = "pwdProofWardId")
    private int pwid;

    public int getPwid() {
        return pwid;
    }

    public void setPwid(int pwid) {
        this.pwid = pwid;
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProofWardDept proofWardDept = (ProofWardDept) o;
        return pwid == proofWardDept.pwid &&
                id.equals(proofWardDept.id) &&
                validFrom.equals(proofWardDept.validFrom) &&
                deptNumbers.equals(proofWardDept.deptNumbers) &&
                deptNames.equals(proofWardDept.deptNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pwid, validFrom, deptNumbers, deptNames);
    }
}
