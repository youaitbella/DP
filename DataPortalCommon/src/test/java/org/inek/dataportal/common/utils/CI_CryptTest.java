/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.common.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author muellermi
 */
public class CI_CryptTest {

    @Test
    public void testGetHash() {
        String hashPassword = Crypt.hashPassword("TopSecret", "15cb7d501719475fbe1c3bb4d9a828ec9684078c09664657a7ecec481c1b6f32");
        System.out.println(hashPassword);
        assertEquals("0156ed845bab2c33eef66b6c06e2ae6e41cb4f985145c697f9af19d847c7ce663047fa17968ee26b66d57b58c329fa39"
                + "d9bce01b759c2bb5562c3192392f1864c5efc9a489e156569fc8d00d1c72baab8818e8f6f81ed58fd64cdf99550e0955d51"
                + "c43da655651cca639f72c7748949e61d228f15e2f3cf6796d75ae917273d5", hashPassword);
    }
    
}
