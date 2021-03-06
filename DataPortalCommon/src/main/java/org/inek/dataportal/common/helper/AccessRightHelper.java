package org.inek.dataportal.common.helper;

import org.inek.dataportal.api.enums.Feature;
import org.inek.dataportal.api.enums.IkUsage;
import org.inek.dataportal.api.enums.ManagedBy;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.common.data.account.entities.AccountFeature;
import org.inek.dataportal.common.data.common.User;
import org.inek.dataportal.common.data.ikadmin.entity.AccessRight;
import org.inek.dataportal.common.data.ikadmin.entity.AccountResponsibility;
import org.inek.dataportal.common.data.ikadmin.entity.IkAdmin;
import org.inek.dataportal.common.enums.Right;

import java.util.*;
import java.util.stream.Collectors;

public class AccessRightHelper {

    public static boolean userCanGetAllRight(List<AccessRight> accessRights, Feature feature, int ik) {
        List<AccessRight> tmpAccessRights = accessRights.stream().filter(c -> c.getFeature().equals(feature) && c.getIk() == ik)
                .collect(Collectors.toList());
        StringBuilder tmpStringBuilder = new StringBuilder();
        if (!accessWriteHasNotToMuchUsers(accessRights, tmpStringBuilder)) {
            return false;
        } else {
            AccessRight accessRight = new AccessRight(new User(), ik, feature, Right.All);
            tmpAccessRights.add(accessRight);
            return accessWriteHasNotToMuchUsers(tmpAccessRights, tmpStringBuilder);
        }
    }

    public static boolean accessWriteHasNotToMuchUsers(List<AccessRight> accessRights, StringBuilder stringBuilder) {
        for (AccessRight ar : accessRights.stream()
                .filter(c -> !c.getFeature().getIkUsage().equals(IkUsage.ByResponsibilityAndCorrelation)).collect(Collectors.toList())) {
            int maxUsersWithAccess = ar.getFeature().getMaxUsersWithAccess();

            long usersWithRight = accessRights.stream().filter(c -> c.getIk() == ar.getIk()
                    && c.getFeature() == ar.getFeature()
                    && c.getRight() != Right.Deny)
                    .count();
            if (usersWithRight > maxUsersWithAccess) {
                stringBuilder.append("Für die Funktion [");
                stringBuilder.append(ar.getFeature().getDescription());
                stringBuilder.append("] wurden zu vielen Benutzern Rechte zugewiesen. \\r\\nMaximal erlaubt: ");
                stringBuilder.append(maxUsersWithAccess);
                stringBuilder.append("\\r\\nBenutzer Rechte vergeben: ");
                stringBuilder.append(usersWithRight);
                return false;
            }
        }
        return true;
    }

    public static boolean accessWriteHasMinOneWithAccesRigth(List<AccessRight> accessRights, StringBuilder stringBuilder) {
        for (AccessRight ar : accessRights) {
            if (accessRights.stream()
                    .noneMatch(c -> c.getIk() == ar.getIk()
                            && c.getFeature() == ar.getFeature()
                            && c.getRight() != Right.Deny)) {
                stringBuilder.append("Für die Funktion [");
                stringBuilder.append(ar.getFeature().getDescription());
                stringBuilder.append("] besteht kein Zugriff durch einen Benutzer. Bitte passen Sie die Berechtigungen so an, ");
                stringBuilder.append("dass mindesten ein Benutzer die Daten einsehen kann.");
                return false;
            }
        }
        return true;
    }

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

    public static void ensureFeatureAndRightsForIkAdminOnly(Account account, List<IkAdmin> ikAdminsForIk,
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

    public static boolean responsibilitiesHasNotToMuchUsers(Map<String, List<AccountResponsibility>> responsibleForIks, StringBuilder errorMessages) {
        List<AccountResponsibility> accountResponsibilities = new ArrayList<>();

        for (String key : responsibleForIks.keySet()) {
            accountResponsibilities.addAll(responsibleForIks.get(key));
        }

        List<Feature> distinctFeatures = accountResponsibilities.stream().map(AccountResponsibility::getFeature)
                .distinct()
                .collect(Collectors.toList());
        for (Feature fe : distinctFeatures) {
            int maxUsersWithAccess = fe.getMaxUsersWithAccess();
            for (AccountResponsibility accountResponsibility : accountResponsibilities.stream()
                    .filter(c -> c.getFeature().equals(fe)).collect(Collectors.toList())) {
                long usersWithAccess = accountResponsibilities.stream()
                        .filter(c -> c.getDataIk() == accountResponsibility.getDataIk())
                        .count();
                if (usersWithAccess > maxUsersWithAccess) {
                    errorMessages.append("Für das IK [");
                    errorMessages.append(accountResponsibility.getDataIk());
                    errorMessages.append("] wurden zu vielen Benutzern Rechte zugewiesen. \\r\\nMaximal erlaubt: ");
                    errorMessages.append(maxUsersWithAccess);
                    errorMessages.append(", \\r\\nBenutzer Rechte vergeben: ");
                    errorMessages.append(usersWithAccess);
                    return false;
                }
            }
        }

        return true;
    }

    public static void ensureRightsForAccounts(List<Account> accountsForIk, List<IkAdmin> ikAdminsForIk, int Ik) {
        for (Account acc : accountsForIk) {
            List<AccessRight> accesRightsForIk = acc.getAccessRights().stream().filter(ar -> ar.getIk() == Ik).collect(Collectors.toList());

            for (AccountFeature accf : acc.getFeatures()) {
                if (accesRightsForIk.stream().noneMatch(ar -> ar.getFeature().equals(accf.getFeature()))
                        && accf.getFeature().getManagedBy().equals(ManagedBy.IkAdminOnly)) {
                    AccessRight ar1 = new AccessRight(acc.getId(), Ik, accf.getFeature(), Right.Deny);
                    acc.addAccessRigth(ar1);
                } else {
                    if (featureHasNoIkAdmin(ikAdminsForIk, accf.getFeature()) && accf.getFeature().getManagedBy().equals(ManagedBy.IkAdminOnly)) {
                        setAccesRightsForFeatureToRight(accesRightsForIk, accf.getFeature(), Right.Deny);
                    }
                }
            }

            List<Feature> featurelist = getFeaturesFromAccesRights(accesRightsForIk);
            for (Feature feature : featurelist) {
                if (acc.getFeatures().stream().noneMatch(af -> af.getFeature().equals(feature))
                        && feature.getManagedBy().equals(ManagedBy.IkAdminOnly)) {
                    setAccesRightsForFeatureToRight(accesRightsForIk, feature, Right.Deny);
                }
            }
        }
    }

    private static void setAccesRightsForFeatureToRight(List<AccessRight> accessRightsList, Feature feature, Right right) {
        Optional<AccessRight> first = accessRightsList.stream().filter(ar -> ar.getFeature().equals(feature)).findFirst();
        if (first.isPresent()) {
            AccessRight ar = first.get();
            ar.setRight(right);
        }
    }

    private static List<Feature> getFeaturesFromAccesRights(List<AccessRight> AccesRights) {
        List<Feature> FeatureList = new ArrayList<>();

        for (AccessRight ar : AccesRights) {
            if (!FeatureList.contains(ar.getFeature())) {
                FeatureList.add(ar.getFeature());
            }
        }
        return FeatureList;
    }

    private static boolean featureHasNoIkAdmin(List<IkAdmin> ikAdminsForIk, Feature feature) {
        return ikAdminsForIk.stream().noneMatch(ikA -> ikA.getIkAdminFeatures().stream().anyMatch(af -> af.getFeature().equals(feature)));
    }
}

