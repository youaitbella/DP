/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.utils;

import java.util.HashMap;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author vohldo
 */
public class TestSecurePassword {

    public TestSecurePassword() {

    }

    @Test
    public void checkSecurePassword() {
        System.out.println("checkSecurePasswordTest");
        Map<String, Boolean> passwordList = new HashMap<>();
        
        passwordList.put("hallowelt", Boolean.FALSE);
        passwordList.put("__SECUREpAs3sword!", Boolean.TRUE);
        passwordList.put("OoUnndze23113x", Boolean.FALSE);
        passwordList.put("?!383_dWzzztmdm\\", Boolean.TRUE);
        passwordList.put("123abc$&", Boolean.FALSE);
        passwordList.put("'#", Boolean.FALSE);
        passwordList.put("?", Boolean.FALSE);
        passwordList.put("", Boolean.FALSE);
        passwordList.put("L&§MNUu", Boolean.FALSE);
        passwordList.put("L&§MNUu3", Boolean.TRUE);
        passwordList.put("pppppppp", Boolean.FALSE);
        passwordList.put("hallowelt☻P2", Boolean.FALSE);
        passwordList.put("hallo!2Ewelthallowelthallowelthallowelt", Boolean.FALSE);
        
        for(int i = 0; i < passwordList.size(); i++) {
            String pw = (String) passwordList.keySet().toArray()[i];
            boolean expected = passwordList.get(pw);
            boolean result = SecurePassword.checkSecurePassword(pw);
            assertEquals("Password: "+pw, result, expected);
        }
    }
}
