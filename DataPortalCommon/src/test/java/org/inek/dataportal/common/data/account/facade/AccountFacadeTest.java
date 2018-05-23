/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.account.facade;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.data.account.entities.Account;
import org.junit.Test;

/**
 *
 * @author lautenti
 */
public class AccountFacadeTest {

    @Test
    public void setNewIkTest() {
        AccountFacade accountFacade = new AccountFacade();
        Account acc = new Account();
        acc.setIK(0);
        accountFacade.setNewIk(acc);
        Assertions.assertThat(acc.getIK() == -1).isTrue().as("IK should be set to -1");
        Assertions.assertThat(acc.getAdditionalIKs().isEmpty()).isTrue().as("IK List should be empty");

        acc.setIK(2222222);

        Assertions.assertThat(acc.getIK() == 2222222).isTrue().as("IK should be 2222222");

        accountFacade.setNewIk(acc);
        Assertions.assertThat(acc.getIK() == -1).isTrue().as("IK should be set to -1");
        Assertions.assertThat(acc.getAdditionalIKs().isEmpty()).isTrue().as("IK List should be empty");

        acc.setIK(222222222);
        Assertions.assertThat(acc.getIK() == 222222222).isTrue().as("IK should be 222222222");
        accountFacade.setNewIk(acc);
        Assertions.assertThat(acc.getIK() == -1).isTrue().as("IK should be set to -1");
        Assertions.assertThat(acc.getAdditionalIKs().size() == 1).isTrue().as("IK List must have 1 element");
        Assertions.assertThat(acc.getAdditionalIKs().get(0).getIK() == 222222222).isTrue().as("IK List first element ik must be 222222222");

        Account acc2 = new Account();
        accountFacade.setNewIk(acc2);
        Assertions.assertThat(acc2.getIK() == -1).isTrue().as("IK should be set to -1");
    }

}
