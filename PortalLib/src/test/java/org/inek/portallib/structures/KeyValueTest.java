/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.portallib.structures;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author muellermi
 */
public class KeyValueTest {
    
    public KeyValueTest() {
    }

    @Test
    public void testGetKey() {
        KeyValue<Integer, String> keyValue = new KeyValue<>(1, "Test");
        assertThat (keyValue.getKey(), is(1));
    }

    @Test
    public void testGetValue() {
        KeyValue<Integer, String> keyValue = new KeyValue<>(1, "Test");
        assertThat (keyValue.getValue(), is("Test"));
    }

    @Test
    public void differentObjectsWithSameKeyAreEqual() {
        KeyValue<Integer, String> keyValue1 = new KeyValue<>(1, "Test1");
        KeyValue<Integer, String> keyValue2 = new KeyValue<>(1, "Test2");
        assertTrue (keyValue1.equals(keyValue2));
    }

    @Test
    public void differentObjectsWithSimilarKeyAreNotEqual() {
        KeyValue<Integer, String> keyValue1 = new KeyValue<>(1, "Test1");
        KeyValue<Long, String> keyValue2 = new KeyValue<>(1L, "Test2");
        assertFalse (keyValue1.equals(keyValue2));
    }

    
}
