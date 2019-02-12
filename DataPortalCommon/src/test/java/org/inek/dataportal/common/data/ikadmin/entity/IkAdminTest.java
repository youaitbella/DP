package org.inek.dataportal.common.data.ikadmin.entity;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.api.enums.Feature;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IkAdminTest {

    @Test
    public void removeIkAdminFeaturesIfNotInListTest () {
        IkAdmin ikAdmin = new IkAdmin();

        List<Feature> features = new ArrayList<>();

        features.add(Feature.NUB);
        features.add(Feature.CERT);
        features.add(Feature.DOCUMENTS);

        ikAdmin.updateIkAdminFeatures(features);

        Assertions.assertThat(ikAdmin.getIkAdminFeatures()).size().isEqualTo(3);

    }

}