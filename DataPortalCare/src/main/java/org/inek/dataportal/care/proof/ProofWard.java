package org.inek.dataportal.care.proof;

import java.util.*;

public final class ProofWard {
    public static Builder builder() {
        return new Builder();
    }

    private ProofWard(Builder builder) {
        setFrom(builder.from);
        setTo(builder.to);
        setLocationNumber(builder.locationNumber);
        setWardName(builder.wardName);
        sensitiveAreas = builder.sensitiveAreas;
        depts = builder.depts;
        deptNames = builder.deptNames;
        setBeds(builder.beds);
    }

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
    private Set<String> sensitiveAreas = new HashSet<>();

    public Set<String> getSensitiveAreas() {
        return Collections.unmodifiableSet(sensitiveAreas);
    }

    public void addSensitiveArea(String sensitiveArea) {
        sensitiveAreas.add(sensitiveArea);
    }
    //</editor-fold>

    //<editor-fold desc="Property depts">
    private Set<String> depts = new HashSet<>();

    public Set<String> getDepts() {
        return Collections.unmodifiableSet(depts);
    }

    public void addDept(String dept) {
        depts.add(dept);
    }
    //</editor-fold>

    //<editor-fold desc="Property deptNames">
    private Set<String> deptNames = new HashSet<>();

    public Set<String> getDeptNames() {
        return Collections.unmodifiableSet(deptNames);
    }

    public void addDeptName(String deptName) {
        deptNames.add(deptName);
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProofWard proofWard = (ProofWard) o;
        return locationNumber == proofWard.locationNumber &&
                beds == proofWard.beds &&
                from.equals(proofWard.from) &&
                to.equals(proofWard.to) &&
                wardName.equals(proofWard.wardName) &&
                sensitiveAreas.equals(proofWard.sensitiveAreas) &&
                depts.equals(proofWard.depts) &&
                deptNames.equals(proofWard.deptNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, locationNumber, wardName, sensitiveAreas, depts, deptNames, beds);
    }

    //<editor-fold desc="Builder">
    public static final class Builder {
        private Date from;
        private Date to;
        private int locationNumber;
        private String wardName;
        private Set<String> sensitiveAreas = new HashSet<>();
        private Set<String> depts = new HashSet<>();
        private Set<String> deptNames = new HashSet<>();
        private int beds;

        private Builder() {
        }

        public Builder from(Date val) {
            from = val;
            return this;
        }

        public Builder to(Date val) {
            to = val;
            return this;
        }

        public Builder locationNumber(int val) {
            locationNumber = val;
            return this;
        }

        public Builder wardName(String val) {
            wardName = val;
            return this;
        }

        public Builder addSensitiveArea(String val) {
            sensitiveAreas.add(val);
            return this;
        }

        public Builder addDept(String val) {
            depts.add(val);
            return this;
        }

        public Builder addDeptName(String val) {
            deptNames.add(val);
            return this;
        }

        public Builder beds(int val) {
            beds = val;
            return this;
        }

        public ProofWard build() {
            return new ProofWard(this);
        }
    }
    //</editor-fold>

}
