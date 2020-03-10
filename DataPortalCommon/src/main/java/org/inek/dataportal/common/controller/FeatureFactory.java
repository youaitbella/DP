package org.inek.dataportal.common.controller;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.common.data.account.entities.AccountFeature;

import java.util.ArrayList;
import java.util.List;

public interface FeatureFactory {

    IFeatureController createController(Feature feature, SessionController sessionController);

    /**
     * this hook allows each portal to check and add its own missing features
     *
     * @param accountId
     * @param features
     * @return
     */
    default List<Feature> obtainMissingRequiredFeatures(int accountId, List<AccountFeature> features) {
        return new ArrayList<>();
    }
}
