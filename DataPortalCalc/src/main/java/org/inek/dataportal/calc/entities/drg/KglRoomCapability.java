package org.inek.dataportal.calc.entities.drg;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "KglRoomCapability", schema = "calc")
public class KglRoomCapability {

    public KglRoomCapability() {
    }

    public KglRoomCapability(DrgCalcBasics baseInformation, int costCenterId) {
        this.baseInformation = baseInformation;
        this.costCenterId = costCenterId;
    }

    //<editor-fold desc="Property Id">
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rcId")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //</editor-fold>

    //<editor-fold desc="Property BaseInformation">
    @ManyToOne
    @JoinColumn(name = "rcBaseInformationId")
    @JsonIgnore
    private DrgCalcBasics baseInformation;

    @JsonIgnore
    public DrgCalcBasics getBaseInformation() {
        return baseInformation;
    }

    @JsonIgnore
    public void setBaseInformation(DrgCalcBasics baseInformation) {
        this.baseInformation = baseInformation;
    }

    public int getBaseInformationId() {
        return this.baseInformation.getId();
    }

    public void setBaseInformationId(int baseInformationId) {
        this.baseInformation.setId(baseInformationId);
    }
    //</editor-fold>

    //<editor-fold desc="Property CostCenterId">
    @Column(name = "rcCostCenterId")
    private int costCenterId;

    public int getCostCenterId() {
        return costCenterId;
    }

    public void setCostCenterId(int costCenterId) {
        this.costCenterId = costCenterId;
    }
    //</editor-fold>

    //<editor-fold desc="Property RoomName">
    @Column(name = "rcRoomName")
    @Size(max = 250)
    private String roomName = "";

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    //</editor-fold>

    //<editor-fold desc="Property MainServiceId">
    @Column(name = "rcMainServiceId")
    private int mainServiceId;

    public int getMainServiceId() {
        return mainServiceId;
    }

    public void setMainServiceId(int mainServiceId) {
        this.mainServiceId = mainServiceId;
    }
    //</editor-fold>

    //<editor-fold desc="Property Explanation">
    @Column(name = "rcExplanation")
    @Size(max = 250)
    private String explanation = "";

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    //</editor-fold>

    //<editor-fold desc="Property CaseCount">
    @Column(name = "rcCaseCount")
    private int caseCount;

    public int getCaseCount() {
        return caseCount;
    }

    public void setCaseCount(int caseCount) {
        this.caseCount = caseCount;
    }
    //</editor-fold>

    //<editor-fold desc="Property MedFullVigorCount">
    @Column(name = "rcMedFullVigorCount")
    private double medFullVigorCount;

    public double getMedFullVigorCount() {
        return medFullVigorCount;
    }

    public void setMedFullVigorCount(double medFullVigorCount) {
        this.medFullVigorCount = medFullVigorCount;
    }
    //</editor-fold>

    //<editor-fold desc="Property MedCostAmount">
    @Column(name = "rcMedCostAmount")
    private double medCostAmount;

    public double getMedCostAmount() {
        return medCostAmount;
    }

    public void setMedCostAmount(double medCostAmount) {
        this.medCostAmount = medCostAmount;
    }
    //</editor-fold>

    //<editor-fold desc="Property FunctFullVigorCount">
    @Column(name = "rcFunctFullVigorCount")
    private double functFullVigorCount;

    public double getFunctFullVigorCount() {
        return functFullVigorCount;
    }

    public void setFunctFullVigorCount(double functFullVigorCount) {
        this.functFullVigorCount = functFullVigorCount;
    }
    //</editor-fold>

    //<editor-fold desc="Property FunctCostAmount">
    @Column(name = "rcFunctCostAmount")
    private double functCostAmount;

    public double getFunctCostAmount() {
        return functCostAmount;
    }

    public void setFunctCostAmount(double functCostAmount) {
        this.functCostAmount = functCostAmount;
    }
    //</editor-fold>

    //<editor-fold desc="equals, hashcode">
    @Override
    @SuppressWarnings("CyclomaticComplexity")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KglRoomCapability that = (KglRoomCapability) o;

        if (id != that.id) return false;
        if (id > 0) return id == that.id;
        if (costCenterId != that.costCenterId) return false;
        if (mainServiceId != that.mainServiceId) return false;
        if (caseCount != that.caseCount) return false;
        if (Double.compare(that.medFullVigorCount, medFullVigorCount) != 0) return false;
        if (Double.compare(that.medCostAmount, medCostAmount) != 0) return false;
        if (Double.compare(that.functFullVigorCount, functFullVigorCount) != 0) return false;
        if (Double.compare(that.functCostAmount, functCostAmount) != 0) return false;
        if (!baseInformation.equals(that.baseInformation)) return false;
        return roomName.equals(that.roomName);
    }

    @Override
    public int hashCode() {
        return 79273424;
    }

    @JsonIgnore
    public boolean isEmpty() {
        return mainServiceId == 0
                & roomName.length() == 0
                & caseCount == 0
                & medFullVigorCount == 0
                & medCostAmount == 0
                & functFullVigorCount == 0
                & functCostAmount == 0;
    }
    //</editor-fold>
}
