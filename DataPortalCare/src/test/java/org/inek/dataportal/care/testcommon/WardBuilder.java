package org.inek.dataportal.care.testcommon;

import org.inek.dataportal.care.entities.Dept;
import org.inek.dataportal.care.entities.DeptWard;
import org.inek.dataportal.care.entities.version.MapVersion;

import java.time.Month;
import java.util.Date;

import static org.inek.dataportal.common.utils.DateUtils.createDate;

public class WardBuilder {

    public static DeptWard createDeptWard(String name, String deptName, int p21, int vz, String fab) {
        return WardBuilder.createDeptWard(createDate(2019, Month.JANUARY, 1), createDate(2019, Month.DECEMBER, 31), name, deptName, p21, vz, fab);
    }

    public static DeptWard createDeptWard(Date validFrom, Date validTo, String name, String deptName, int p21, int vz, String fab) {
        return createDeptWard(validFrom, validTo, name, deptName, p21, vz, fab, "Intensiv");
    }

    public static DeptWard createDeptWard(Date validFrom, Date validTo, String name, String deptName, int p21, int vz, String fab, String sensitiveArea) {
        Dept dept = new Dept();
        dept.setSensitiveArea(sensitiveArea);
        DeptWard ward = new DeptWard(new MapVersion());
        ward.setDept(dept);
        ward.setValidFrom(validFrom);
        ward.setValidTo(validTo);
        ward.setWardName(name);
        ward.setDeptName(deptName);
        ward.setLocationCodeP21(p21);
        ward.setLocationCodeVz(vz);
        ward.setFab(fab);
        return ward;
    }

}
