/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.data.account.facade;

import org.assertj.core.api.Assertions;
import org.inek.dataportal.common.data.account.entities.Account;
import org.junit.jupiter.api.Test;

/**
 *
 * @author lautenti
 */
public class AccountFacadeTest {

    @Test
    public void setNewIkTest() {
        AccountFacade accountFacade = new AccountFacade();
        Account acc = new Account();
        accountFacade.setNewIk(acc, 0);
        Assertions.assertThat(acc.getAccountIks().isEmpty()).isTrue().as("IK List should be empty");

        accountFacade.setNewIk(acc, 22222);
        Assertions.assertThat(acc.getAccountIks().isEmpty()).isTrue().as("IK List should be empty");

        accountFacade.setNewIk(acc, 222222222);
        Assertions.assertThat(acc.getAccountIks().size() == 1).isTrue().as("IK List must have 1 element");
        Assertions.assertThat(acc.getAccountIks().get(0).getIK() == 222222222).isTrue().as("IK List first element ik must be 222222222");
    }
}
