package org.inek.dataportal.care.testcommon;

import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.entities.version.MapVersion;
import org.inek.dataportal.common.utils.DateUtils;

import java.time.Month;
import java.util.Date;

import static org.inek.dataportal.common.utils.DateUtils.createDate;

public class WardBuilder {

    private Date validFrom = DateUtils.MIN_DATE;
    private Date validTo = DateUtils.MAX_DATE;
    private String wardName = "";
    private String deptName = "";
    private int locationP21;
    private int locationNumber;
    private String dept = "";
    private String sensitiveArea = "";

    public WardBuilder(String wardName) {
        this.wardName = wardName;
    }

    public WardBuilder validFrom(Date validFrom) {
        this.validFrom = validFrom;
        return this;
    }

    public WardBuilder validTo(Date validTo) {
        this.validTo = validTo;
        return this;
    }

    public WardBuilder deptName(String deptName) {
        this.deptName = deptName;
        return this;
    }

    public WardBuilder locationP21(int locationP21) {
        this.locationP21 = locationP21;
        return this;
    }

    public WardBuilder locationNumber(int locationNumber) {
        this.locationNumber = locationNumber;
        return this;
    }

    public WardBuilder dept(String dept) {
        this.dept = dept;
        return this;
    }

    public WardBuilder sensitiveArea(String sensitiveArea) {
        this.sensitiveArea = sensitiveArea;
        return this;
    }

    public DeptWard create() {
        return createDeptWard(validFrom, validTo, wardName, deptName, locationP21, locationNumber, dept, sensitiveArea);
    }

    public static DeptWard createDeptWard(String name, String deptName, int p21, int vz, String fab) {
        return WardBuilder.createDeptWard(createDate(2019, Month.JANUARY, 1), createDate(2019, Month.DECEMBER, 31), name, deptName, p21, vz, fab);
    }

    public static DeptWard createDeptWard(Date validFrom, Date validTo, String name, String deptName, int p21, int vz, String fab) {
        return createDeptWard(validFrom, validTo, name, deptName, p21, vz, fab, "Intensiv");
    }

    public static DeptWard createDeptWard(Date validFrom, Date validTo, String wardName, String deptName, int p21, int vz, String fab, String sensitiveArea) {
        Dept dept = new Dept();
        dept.setSensitiveArea(sensitiveArea);
        DeptWard ward = new DeptWard(new MapVersion());
        ward.setDept(dept);
        ward.setValidFrom(validFrom);
        ward.setValidTo(validTo);
        ward.setWardName(wardName);
        ward.setDeptName(deptName);
        ward.setLocationCodeP21(p21);
        ward.setLocationCodeVz(vz);
        ward.setFab(fab);
        return ward;
    }

}
