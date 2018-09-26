package org.inek.dataportal.common.data.ikadmin.facade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.ikadmin.entity.IkAdmin;
import org.junit.jupiter.api.Test;

public class IkAdminFacadeTest extends IkAdminFacade{
    private static final int IK1 = 111111111;
    private static final int IK2 = 222222222;
    private static final int IK3 = 333333333;
    
    public IkAdminFacadeTest() {
    }

    @Test
    public void loadAllManagedIkWithFeaturesReturnExpectedList() {
        Map<Integer, Set<Feature>> managedIkWithFeatures = loadAllManagedIkWithFeatures();
        assertThat(managedIkWithFeatures).isNotNull().isNotEmpty().containsOnlyKeys(IK1, IK2, IK3);

        Set<Feature> features1 = managedIkWithFeatures.get(IK1);
        assertThat(features1).isNotNull().isNotEmpty().containsOnly(Feature.CARE, Feature.NUB, Feature.HC_HOSPITAL);

        Set<Feature> features2 = managedIkWithFeatures.get(IK2);
        assertThat(features2).isNotNull().isNotEmpty().containsOnly(Feature.ADDITIONAL_COST, Feature.NUB);

        Set<Feature> features3 = managedIkWithFeatures.get(IK3);
        assertThat(features3).isNotNull().isEmpty();
    }
    
    @Override
    protected <T> List<T> findAllFresh(Class<T> entityClass) {
        List<IkAdmin> ikAdmins = new ArrayList<>();

        IkAdmin admin = new IkAdmin(1, IK1, "");
        admin.addIkAdminFeature(Feature.CARE);
        admin.addIkAdminFeature(Feature.NUB);
        ikAdmins.add(admin);

        admin = new IkAdmin(1, IK2, "");
        admin.addIkAdminFeature(Feature.ADDITIONAL_COST);
        admin.addIkAdminFeature(Feature.NUB);
        ikAdmins.add(admin);

        admin = new IkAdmin(2, IK1, "");
        admin.addIkAdminFeature(Feature.HC_HOSPITAL);
        admin.addIkAdminFeature(Feature.NUB);
        ikAdmins.add(admin);

        admin = new IkAdmin(2, IK3, "");
        ikAdmins.add(admin);

        admin = new IkAdmin(3, IK1, "");
        ikAdmins.add(admin);

        return (List<T>) ikAdmins;
    }
}
