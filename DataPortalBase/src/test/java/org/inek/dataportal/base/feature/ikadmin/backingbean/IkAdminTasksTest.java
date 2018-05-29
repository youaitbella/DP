package org.inek.dataportal.base.feature.ikadmin.backingbean;


import java.util.ArrayList;
import java.util.List;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.enums.Feature;
import org.inek.dataportal.common.enums.Right;
import org.assertj.core.api.Assertions;

import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class IkAdminTasksTest {

    @Test
    public void saveAccessRightsAllowedTest() {

        IkAdminTasks adminTask = new IkAdminTasks();

        List<AccessRight> rights = new ArrayList<>();

        rights.add(new AccessRight(10, 222222222, Feature.CERT, Right.Read));
        rights.add(new AccessRight(11, 222222222, Feature.DRG_PROPOSAL, Right.Deny));

        Assertions.assertThat(adminTask.saveAccessRightsAllowed(rights)).isFalse().as("Should be False");

        rights.add(new AccessRight(11, 222222222, Feature.DRG_PROPOSAL, Right.Write));

        Assertions.assertThat(adminTask.saveAccessRightsAllowed(rights)).isTrue().as("Should be True");

        rights.add(new AccessRight(11, 222222223, Feature.CALCULATION_HOSPITAL, Right.Deny));
        rights.add(new AccessRight(12, 222222223, Feature.CALCULATION_HOSPITAL, Right.Deny));
        rights.add(new AccessRight(13, 222222223, Feature.CALCULATION_HOSPITAL, Right.Deny));

        Assertions.assertThat(adminTask.saveAccessRightsAllowed(rights)).isFalse().as("Should be False");

        rights.add(new AccessRight(14, 222222224, Feature.CALCULATION_HOSPITAL, Right.All));

        Assertions.assertThat(adminTask.saveAccessRightsAllowed(rights)).isFalse().as("Should be False");

        rights.add(new AccessRight(15, 222222223, Feature.CALCULATION_HOSPITAL, Right.Create));

        Assertions.assertThat(adminTask.saveAccessRightsAllowed(rights)).isTrue().as("Should be True");

        List<AccessRight> rights2 = new ArrayList<>();
        rights2.add(new AccessRight(10, 222222222, Feature.CERT, Right.Read));
        rights2.add(new AccessRight(10, 222222223, Feature.CERT, Right.Deny));

        Assertions.assertThat(adminTask.saveAccessRightsAllowed(rights2)).isFalse().as("Should be False");
    }
}
