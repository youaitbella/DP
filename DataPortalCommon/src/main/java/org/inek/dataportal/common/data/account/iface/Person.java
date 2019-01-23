package org.inek.dataportal.common.data.account.iface;

/**
 *
 * @author muellermi
 */
public interface Person {

    int getId();

    int getGender();

    String getTitle();

    String getFirstName();

    String getLastName();

    String getEmail();

    String getCompany();

    default String getDisplayName() {
        String dispName = getTitle() + " " + getFirstName() + " " + getLastName();
        return dispName.replace("  ", " ").trim();
    }
}
