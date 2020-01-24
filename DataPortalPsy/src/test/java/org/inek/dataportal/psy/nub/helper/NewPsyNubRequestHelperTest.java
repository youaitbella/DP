package org.inek.dataportal.psy.nub.helper;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.data.account.entities.Account;
import org.inek.dataportal.psy.nub.entities.PsyNubRequest;
import org.inek.dataportal.psy.nub.enums.PsyNubDateFields;
import org.inek.dataportal.psy.nub.enums.PsyNubMoneyFields;
import org.inek.dataportal.psy.nub.enums.PsyNubNumberFields;
import org.junit.jupiter.api.Test;

class NewPsyNubRequestHelperTest {

    @Test
    void createNewPsyNubRequestAllDateValuesTest() {
        Account acc = new Account();
        acc.setId(10);
        PsyNubRequest newPsyNubRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(acc);

        for (PsyNubDateFields value : PsyNubDateFields.values()) {
            Assertions.assertThat(newPsyNubRequest.getDateValue(value).getField()).as(value.name()).isEqualTo(value);
        }
    }

    @Test
    void createNewPsyNubRequestAllNumberValuesTest() {
        Account acc = new Account();
        acc.setId(10);
        PsyNubRequest newPsyNubRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(acc);

        for (PsyNubNumberFields value : PsyNubNumberFields.values()) {
            Assertions.assertThat(newPsyNubRequest.getNumberValue(value).getField()).as(value.name()).isEqualTo(value);
        }
    }

    @Test
    void createNewPsyNubRequestAllMoneyValuesTest() {
        Account acc = new Account();
        acc.setId(10);
        PsyNubRequest newPsyNubRequest = NewPsyNubRequestHelper.createNewPsyNubRequest(acc);

        for (PsyNubMoneyFields value : PsyNubMoneyFields.values()) {
            Assertions.assertThat(newPsyNubRequest.getMoneyValue(value).getField()).as(value.name()).isEqualTo(value);
        }
    }

    @Test
    void fillAccountToPsyNubTest() {
        Account acc = new Account();
        acc.setFirstName("Max");
        acc.setLastName("Mustermann");
        acc.setPhone("+ 49 (0) 126587");
        acc.setGender(1);
        acc.setTitle("Dr.");
        acc.setRoleId(3);
        acc.setStreet("Musterstraße 1");
        acc.setPostalCode("01358");
        acc.setCustomerFax("+46 (5) 989312");
        acc.setEmail("Muster@Max.Test");
        acc.setTown("Siegburg");

        PsyNubRequest request = new PsyNubRequest();

        NewPsyNubRequestHelper.fillAccountToPsyNub(request, acc);

        Assertions.assertThat(request.getFirstName()).as("Firstname").isEqualTo(acc.getFirstName());
        Assertions.assertThat(request.getLastName()).as("Lastname").isEqualTo(acc.getLastName());
        Assertions.assertThat(request.getPhone()).as("Phone").isEqualTo(acc.getPhone());
        Assertions.assertThat(request.getGender()).as("Gender").isEqualTo(acc.getGender());
        Assertions.assertThat(request.getTitle()).as("Title").isEqualTo(acc.getTitle());
        Assertions.assertThat(request.getRoleId()).as("RoleId").isEqualTo(acc.getRoleId());
        Assertions.assertThat(request.getStreet()).as("Street").isEqualTo(acc.getStreet());
        Assertions.assertThat(request.getPostalCode()).as("Zip").isEqualTo(acc.getPostalCode());
        Assertions.assertThat(request.getFax()).as("Fax").isEqualTo(acc.getCustomerFax());
        Assertions.assertThat(request.getEmail()).as("Email").isEqualTo(acc.getEmail());
        Assertions.assertThat(request.getTown()).as("Town").isEqualTo(acc.getTown());
    }
}