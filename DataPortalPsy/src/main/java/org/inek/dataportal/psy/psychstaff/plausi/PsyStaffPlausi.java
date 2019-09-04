package org.inek.dataportal.psy.psychstaff.plausi;

import org.inek.dataportal.psy.psychstaff.entity.StaffProof;

public interface PsyStaffPlausi {
    String getPId();
    String getErrorMessage();
    boolean isPlausiCheckOk(StaffProof staffProof);
}
