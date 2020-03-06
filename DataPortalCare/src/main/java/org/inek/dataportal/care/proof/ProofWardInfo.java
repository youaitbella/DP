package org.inek.dataportal.care.proof;

import org.inek.dataportal.common.utils.DateUtils;

import java.util.*;
import java.util.stream.Collectors;

public final class ProofWardInfo {
    public static Builder builder() {
        return new Builder();
    }

    private ProofWardInfo(Builder builder) {
        setFrom(builder.from);
        setTo(builder.to);
        setLocationNumber(builder.locationNumber);
        setLocationP21(builder.locationP21);
        setWardName(builder.wardName);
        sensitiveDomains = builder.sensitiveAreas;
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

    //<editor-fold desc="Property locationP21">
    private String locationP21;

    public String getLocationP21() {
        return locationP21;
    }

    public void setLocationP21(String locationP21) {
        this.locationP21 = locationP21;
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
    private Set<String> sensitiveDomains = new HashSet<>();

    public String sensitiveDomains() {
        return sensitiveDomains.stream()
                .distinct()
                .sorted(String::compareTo)
                .collect(Collectors.joining(", "));
    }

    public void addSensitiveDomain(String sensitiveDomain) {
        sensitiveDomains.add(sensitiveDomain);
    }

    public List<String> sensitiveDomainSet() {
        return Collections.unmodifiableList(new ArrayList<>(sensitiveDomains));
    }
    //</editor-fold>

    //<editor-fold desc="Property depts">
    private Set<String> depts = new HashSet<>();

    public String depts() {
        return depts.stream()
                .distinct()
                .sorted(String::compareTo)
                .collect(Collectors.joining(", "));
    }

    public void addDept(String dept) {
        depts.add(dept);
    }
    //</editor-fold>

    //<editor-fold desc="Property deptNames">
    private Set<String> deptNames = new HashSet<>();

    public String deptNames() {
        return deptNames.stream()
                .distinct()
                .sorted(String::compareTo)
                .collect(Collectors.joining(", "));
    }

    public void addDeptName(String deptName) {
        deptNames.add(deptName);
    }
    //</editor-fold>

    //<editor-fold desc="Property beds">
    private int daybeds;

    public double getBeds() {
        return ((double) daybeds) / DateUtils.duration(from, to);
    }

    public void setBeds(int beds) {
        daybeds = beds * DateUtils.duration(from, to);
    }
    //</editor-fold>

    public void merge(ProofWardInfo other) {
        if (!DateUtils.addDays(to, 1).equals(other.from)) {
            throw new IllegalArgumentException("Merge ranges need to be continuous");
        }
        if (!sensitiveDomains.equals(other.sensitiveDomains)) {
            throw new IllegalArgumentException("Sensitive areas need to be equal");
        }
        daybeds += other.daybeds;
        depts.addAll(other.depts);
        deptNames.addAll(other.deptNames);
        to = other.to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProofWardInfo proofWardInfo = (ProofWardInfo) o;
        return locationNumber == proofWardInfo.locationNumber &&
                daybeds == proofWardInfo.daybeds &&
                from.equals(proofWardInfo.from) &&
                to.equals(proofWardInfo.to) &&
                wardName.equals(proofWardInfo.wardName) &&
                sensitiveDomains.equals(proofWardInfo.sensitiveDomains) &&
                depts.equals(proofWardInfo.depts) &&
                deptNames.equals(proofWardInfo.deptNames);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, locationNumber, wardName, sensitiveDomains, depts, deptNames, daybeds);
    }

    //<editor-fold desc="Builder">
    public static final class Builder {
        private Date from;
        private Date to;
        private int locationNumber;
        private String locationP21;
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

        public Builder locationP21(String val) {
            locationP21 = val;
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

        public ProofWardInfo build() {
            return new ProofWardInfo(this);
        }
    }
    //</editor-fold>

}
