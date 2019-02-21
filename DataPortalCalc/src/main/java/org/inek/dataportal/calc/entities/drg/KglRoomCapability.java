package org.inek.dataportal.calc.entities.drg;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "KglRoomCapability", schema = "calc")
public class KglRoomCapability {

    public KglRoomCapability() {
    }

    public KglRoomCapability(DrgCalcBasics baseInformation) {
        this.baseInformation = baseInformation;
    }

    //<editor-fold desc="Property Id">
    @Id
    @Column(name = "rcId")
    int id;

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
    DrgCalcBasics baseInformation;

    public DrgCalcBasics getBaseInformation() {
        return baseInformation;
    }

    public void setBaseInformation(DrgCalcBasics baseInformation) {
        this.baseInformation = baseInformation;
    }
    //</editor-fold>

    //<editor-fold desc="Property CostCenterId">
    @Column(name = "rcCostCenterId")
    int costCenterId;

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
    String roomName = "";

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
    //</editor-fold>

    //<editor-fold desc="Property MainServiceId">
    @Column(name = "rcMainServiceId")
    int mainServiceId;

    public int getMainServiceId() {
        return mainServiceId;
    }

    public void setMainServiceId(int mainServiceId) {
        this.mainServiceId = mainServiceId;
    }
    //</editor-fold>

    //<editor-fold desc="Property CaseCount">
    @Column(name = "rcCaseCount")
    int caseCount;

    public int getCaseCount() {
        return caseCount;
    }

    public void setCaseCount(int caseCount) {
        this.caseCount = caseCount;
    }
    //</editor-fold>

    //<editor-fold desc="Property MedFullVigorCount">
    @Column(name = "rcMedFullVigorCount")
    double mMedFullVigorCount;

    public double getmMedFullVigorCount() {
        return mMedFullVigorCount;
    }

    public void setmMedFullVigorCount(double mMedFullVigorCount) {
        this.mMedFullVigorCount = mMedFullVigorCount;
    }
    //</editor-fold>

    //<editor-fold desc="Property MedCostAmount">
    @Column(name = "rcMedCostAmount")
    double medCostAmount;

    public double getMedCostAmount() {
        return medCostAmount;
    }

    public void setMedCostAmount(double medCostAmount) {
        this.medCostAmount = medCostAmount;
    }
    //</editor-fold>

    //<editor-fold desc="Property FunctFullVigorCount">
    @Column(name = "rcFunctFullVigorCount")
    double functFullVigorCount;

    public double getFunctFullVigorCount() {
        return functFullVigorCount;
    }

    public void setFunctFullVigorCount(double functFullVigorCount) {
        this.functFullVigorCount = functFullVigorCount;
    }
    //</editor-fold>

    //<editor-fold desc="Property FunctCostAmount">
    @Column(name = "rcFunctCostAmount")
    double functCostAmount;

    public double getFunctCostAmount() {
        return functCostAmount;
    }

    public void setFunctCostAmount(double functCostAmount) {
        this.functCostAmount = functCostAmount;
    }
    //</editor-fold>

    //<editor-fold desc="equals, hashcode">
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KglRoomCapability that = (KglRoomCapability) o;

        if (id != that.id) return false;
        if (id > 0) return id == that.id;
        if (costCenterId != that.costCenterId) return false;
        if (mainServiceId != that.mainServiceId) return false;
        if (caseCount != that.caseCount) return false;
        if (Double.compare(that.mMedFullVigorCount, mMedFullVigorCount) != 0) return false;
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
    //</editor-fold>
}
