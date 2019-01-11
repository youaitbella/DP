package org.inek.dataportal.common.helper;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.ManagedBy;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountFeature;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.data.ikadmin.entity.IkAdmin;
import org.inek.dataportal.common.enums.Right;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AccessRightHelper {

    public static void ensureRightsForAccountFeature(Account account, List<IkAdmin> ikAdminsForIk,
                                                     Set<Integer> ikAdminsForMailing,
                                                     int ik) {
        for (AccountFeature accountFeature : account.getFeatures()) {
            if (hasIkAdmin(ikAdminsForIk, accountFeature.getFeature())) {
                if (!rightsExists(account, accountFeature.getFeature(), ik)) {
                    AccessRight accessRight = new AccessRight(account.getId(), ik, accountFeature.getFeature(), Right.Deny);
                    account.addAccessRigth(accessRight);
                    ikAdminsForMailing.addAll(getIkAdminAccountsForFeature(ikAdminsForIk, accountFeature.getFeature()));
                }
            }
        }
    }

    public static void ensureRightsForNonAccountFeature(Account account, List<IkAdmin> ikAdminsForIk,
                                                     Set<Integer> ikAdminsForMailing,
                                                     int ik) {
        for (Feature feature : Feature.values()) {
            if (feature.getManagedBy() == ManagedBy.IkAdminOnly && account.getFeatures().stream().noneMatch(c -> c.getFeature() == feature)) {
                if (hasIkAdmin(ikAdminsForIk, feature)) {
                    account.addFeature(feature, true);
                    AccessRight accessRight = new AccessRight(account.getId(), ik, feature, Right.Deny);
                    account.addAccessRigth(accessRight);
                    ikAdminsForMailing.addAll(getIkAdminAccountsForFeature(ikAdminsForIk, feature));
                }
            }
        }
    }


    private static List<Integer> getIkAdminAccountsForFeature(List<IkAdmin> ikAdmins, Feature feature) {
        return ikAdmins.stream()
                .filter(a -> a.getIkAdminFeatures().stream().anyMatch(af -> af.getFeature() == feature))
                .map(a -> a.getAccountId())
                .collect(Collectors.toList());
    }

    private static Boolean hasIkAdmin(List<IkAdmin> ikAdmins, Feature feature) {
        return ikAdmins.stream()
                .anyMatch(a -> a.getIkAdminFeatures().stream().anyMatch(af -> af.getFeature() == feature));
    }

    private static Boolean rightsExists(Account acc, Feature feature, int ik) {
        return acc.getAccessRights().stream()
                .anyMatch(ar -> ar.getIk() == ik
                        && ar.getFeature() == feature);
    }

}
