/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.inek.dataportal.utils;

import org.inek.dataportal.utils.Helper;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author muellermi
 */
public class HelperTest {

    public HelperTest() {
    }

    @Test
    public void testCollectException() {
        try {
            throw new Exception("Test");
        } catch (Exception ex) {
            String msg = Helper.collectException(ex, 0);
            String expected = "Level: 0\r\n\r\nTest";
            assertTrue(msg.startsWith(expected));
        }
    }

    @Test
    public void testCollectException2() {
        Exception cause = new Exception("TestCause");
        Exception ex = new Exception("TestException", cause);
        String msg = Helper.collectException(ex, 0);
        String expected = "Level: 0\r\n\r\nTestException";
        assertTrue(msg.startsWith(expected));
        assertTrue(msg.contains("Level: 1"));
        assertTrue(msg.contains("TestCause"));
    }
}
