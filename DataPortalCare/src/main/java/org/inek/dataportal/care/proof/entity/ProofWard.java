package org.inek.dataportal.care.proof.entity;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "ProofWard", schema = "care")
public class ProofWard {

    // <editor-fold defaultstate="collapsed" desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pwId")
    private Integer id = -1;

    public int getId() {
        return id;
    }

    public void setId(int val) {
        id = val;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property Ik">
    @Column(name = "pwIk")
    private int ik;

    public int getIk() {
        return ik;
    }

    public void setIk(int val) {
        ik = val;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Property LocationNumber">
    @Column(name = "pwLocationNumber")
    private int locationNumber;

    public int getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(int val) {
        locationNumber = val;
    }
    // </editor-fold>

    //<editor-fold desc="Property LocationText">
    @Column(name = "pwLocationText")
    private String locationText;

    public String getLocationText() {
        return locationText;
    }

    public void setLocationText(String val) {
        locationText = val;
    }
    //</editor-fold>

    //<editor-fold desc="Property Name">
    @Column(name = "pwName")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String val) {
        name = val;
    }
    //</editor-fold>

    @OneToMany(mappedBy = "proofWard", cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "pwdProofWardId")
    private List<ProofWardDept> proofWardDepts = new ArrayList<>();

    public void addProofWardDept(ProofWardDept proofWardDept) {
        proofWardDepts.add(proofWardDept);
    }

    public List<ProofWardDept> getProofWardDepts(){
        return Collections.unmodifiableList(proofWardDepts);
    }

    public ProofWardDept validProofWardDept(Date date){

        return proofWardDepts.stream()
                .sorted((p1, p2) -> p2.getValidFrom().compareTo(p1.getValidFrom()))
                .filter(p -> p.getValidFrom().compareTo(date) <= 0)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("No proof ward found"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProofWard proofWard = (ProofWard) o;
        return ik == proofWard.ik &&
                locationNumber == proofWard.locationNumber &&
                id.equals(proofWard.id) &&
                locationText.equals(proofWard.locationText) &&
                name.equals(proofWard.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ik, locationNumber, locationText, name);
    }
}
