/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataprtal.common.utils;

import org.inek.dataportal.common.utils.SecurePassword;
import java.util.HashMap;
import java.util.Map;
import org.inek.dataportal.common.enums.Quality;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vohldo
 */
public class TestSecurePassword {

    public TestSecurePassword() {

    }

    @Test
    public void checkPoorPassword() {
        System.out.println("checkSecurePasswordTest");
        Map<String, Quality> passwordList = new HashMap<>();
        
        passwordList.put("'#", Quality.Poor);
        passwordList.put("?", Quality.Poor);
        passwordList.put("", Quality.Poor);
        passwordList.put("pppppppp", Quality.Poor);
        passwordList.put("ppppppppppp", Quality.Poor);
        passwordList.put("hallo1234", Quality.Poor);
        passwordList.put("hallowelt", Quality.Poor);
        
        processList(passwordList);
    }

    @Test
    public void checkMediumPassword() {
        System.out.println("checkSecurePasswordTest");
        Map<String, Quality> passwordList = new HashMap<>();
        
        passwordList.put("hallo1234x", Quality.Medium);
        passwordList.put("123abc$&", Quality.Medium);
        passwordList.put("pppppppppppppppp", Quality.Medium);
        passwordList.put("ppppppppppppppppp", Quality.Medium);
        
        processList(passwordList);
    }

    @Test
    public void checkGoodPassword() {
        System.out.println("checkSecurePasswordTest");
        Map<String, Quality> passwordList = new HashMap<>();
        
        passwordList.put("pppppppppppppppppp", Quality.Good);
        passwordList.put("L&=MNUu3", Quality.Good);
        passwordList.put("LzM8NUu3", Quality.Good);
        
        processList(passwordList);
    }

    @Test
    public void checkStrongPassword() {
        System.out.println("checkSecurePasswordTest");
        Map<String, Quality> passwordList = new HashMap<>();
        
        passwordList.put("L&§MNUu3", Quality.Strong);
        passwordList.put("L&§n1Uu3", Quality.Strong);
        passwordList.put("__SECUREpAs3sword!", Quality.Strong);
        passwordList.put("OoUnndze23113x", Quality.Strong);
        passwordList.put("?!383_dWzzztmdm\\", Quality.Strong);
        passwordList.put("hallowelt☻P2", Quality.Strong);
        passwordList.put("hallo!2Ewelthallowelthallowelthallowelt", Quality.Strong);
        
        processList(passwordList);
    }

    private void processList(Map<String, Quality> passwordList) {
        SecurePassword sp = new SecurePassword();
        for (String pw : passwordList.keySet()){
            Quality expected = passwordList.get(pw);
            Quality result = sp.determinePasswordQuality(pw);
            assertEquals(expected, result);
        }
    }
}
