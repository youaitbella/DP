package org.inek.dataportal.care.proof;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ProofWard {
    //<editor-fold desc="Property Date From">
    private Date from;

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }
    //</editor-fold>

    //<editor-fold desc="Property Date To">
    private Date to;

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }
    //</editor-fold>

    //<editor-fold desc="Property LocationNumber">
    private int locationNumber;

    public int getLocationNumber() {
        return locationNumber;
    }

    public void setLocationNumber(int locationNumber) {
        this.locationNumber = locationNumber;
    }
    //</editor-fold>

    //<editor-fold desc="Property wardName">
    private String wardName;

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }
    //</editor-fold>

    //<editor-fold desc="Property sensitiveArea">
    private Set<Integer> sensitiveAreas = new HashSet<>();

    public Set<Integer> getSensitiveAreas() {
        return sensitiveAreas;
    }

    public void setSensitiveAreas(Set<Integer> sensitiveAreas) {
        this.sensitiveAreas = sensitiveAreas;
    }
    //</editor-fold>

    //<editor-fold desc="Property depts">
    private Set<String> depts = new HashSet<>();

    public Set<String> getDepts() {
        return depts;
    }

    public void setDepts(Set<String> depts) {
        this.depts = depts;
    }
    //</editor-fold>

    //<editor-fold desc="Property deptNames">
    private Set<String> deptNames = new HashSet<>();

    public Set<String> getDeptNames() {
        return deptNames;
    }

    public void setDeptNames(Set<String> deptNames) {
        this.deptNames = deptNames;
    }
    //</editor-fold>

    //<editor-fold desc="Property beds">
    private int beds;

    public int getBeds() {
        return beds;
    }

    public void setBeds(int beds) {
        this.beds = beds;
    }
    //</editor-fold>

}
