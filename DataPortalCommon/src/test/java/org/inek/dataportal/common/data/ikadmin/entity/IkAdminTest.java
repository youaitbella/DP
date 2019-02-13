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

        List<Feature> newFeatures = new ArrayList<>();

        newFeatures.add(Feature.NUB);
        newFeatures.add(Feature.DOCUMENTS);

        ikAdmin.updateIkAdminFeatures(newFeatures);

        Assertions.assertThat(ikAdmin.getIkAdminFeatures()).size().isEqualTo(2);
        Assertions.assertThat(ikAdmin.getIkAdminFeatures().stream().anyMatch(c -> c.getFeature() == Feature.NUB)).isTrue();
        Assertions.assertThat(ikAdmin.getIkAdminFeatures().stream().anyMatch(c -> c.getFeature() == Feature.DOCUMENTS)).isTrue();
    }

    @Test
    public void concateIkAdminFeaturesTest () {
        IkAdmin ikAdmin = new IkAdmin();

        Assertions.assertThat(ikAdmin.getConcateFeatures()).isEqualTo("Keine");

        ikAdmin.addIkAdminFeature(Feature.NUB);

        Assertions.assertThat(ikAdmin.getConcateFeatures()).isEqualTo(Feature.NUB.getDescription());

        ikAdmin.addIkAdminFeature(Feature.DROPBOX);

        Assertions.assertThat(ikAdmin.getConcateFeatures()).isEqualTo(Feature.NUB.getDescription() + ", " + Feature.DROPBOX.getDescription());
    }

}