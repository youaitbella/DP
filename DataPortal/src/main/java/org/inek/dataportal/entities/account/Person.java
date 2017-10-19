package org.inek.dataportal.entities.account;

/**
 *
 * @author muellermi
 */
public interface Person {

    int getId();

    int getGender();

    void setGender(int gender);

    String getTitle();

    void setTitle(String title);

    String getFirstName();

    void setFirstName(String firstName);

    String getLastName();

    void setLastName(String lastName);

    String getEmail();

    void setEmail(String mail);

    String getCompany();

    void setCompany(String company);

    default String getDisplayName() {
        String dispName = getTitle() + " " + getFirstName() + " " + getLastName();
        return dispName.replace("  ", " ").trim();
    }
}
