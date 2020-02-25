package org.inek.dataportal.care.proof.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ProofWard", schema = "care")
public class ProofWard {
    public ProofWard() {
    }

    public ProofWard(int ik, int locationNumber, String locationP21, String name) {
        this.ik = ik;
        this.locationNumber = locationNumber;
        this.locationP21 = locationP21;
        this.name = name;
    }

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

    //<editor-fold desc="Property LocationP21">
    @Column(name = "pwLocationP21")
    private String locationP21 = "";

    public String getLocationP21() {
        return locationP21;
    }

    public void setLocationP21(String val) {
        locationP21 = val;
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

    //<editor-fold desc="equals / hashCode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProofWard proofWard = (ProofWard) o;
        return ik == proofWard.ik &&
                locationNumber == proofWard.locationNumber &&
                id.equals(proofWard.id) &&
                locationP21.equals(proofWard.locationP21) &&
                name.equals(proofWard.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ik, locationNumber, locationP21, name);
    }
    //</editor-fold>
}
