/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.entities.modelintention;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vohldo
 */
public class AgreedPatientsTest {
    
    public AgreedPatientsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getPatientsFrom method, of class AgreedPatients.
     */
    @Test
    public void testGetPatientsFrom() {
        System.out.println("getPatientsFrom");
        AgreedPatients instance = new AgreedPatients();
        Date expResult = null;
        Date result = instance.getPatientsFrom();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPatientsFrom method, of class AgreedPatients.
     */
    @Test
    public void testSetPatientsFrom() {
        System.out.println("setPatientsFrom");
        Date from = null;
        AgreedPatients instance = new AgreedPatients();
        instance.setPatientsFrom(from);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPatientsTo method, of class AgreedPatients.
     */
    @Test
    public void testGetPatientsTo() {
        System.out.println("getPatientsTo");
        AgreedPatients instance = new AgreedPatients();
        Date expResult = null;
        Date result = instance.getPatientsTo();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPatientsTo method, of class AgreedPatients.
     */
    @Test
    public void testSetPatientsTo() {
        System.out.println("setPatientsTo");
        Date to = null;
        AgreedPatients instance = new AgreedPatients();
        instance.setPatientsTo(to);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPatientsCount method, of class AgreedPatients.
     */
    @Test
    public void testGetPatientsCount() {
        System.out.println("getPatientsCount");
        AgreedPatients instance = new AgreedPatients();
        Integer expResult = null;
        Integer result = instance.getPatientsCount();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setPatientsCount method, of class AgreedPatients.
     */
    @Test
    public void testSetPatientsCount() {
        System.out.println("setPatientsCount");
        Integer patientsCount = null;
        AgreedPatients instance = new AgreedPatients();
        instance.setPatientsCount(patientsCount);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getId method, of class AgreedPatients.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        AgreedPatients instance = new AgreedPatients();
        Integer expResult = null;
        Integer result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setId method, of class AgreedPatients.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        Integer id = null;
        AgreedPatients instance = new AgreedPatients();
        instance.setId(id);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getModelIntentionId method, of class AgreedPatients.
     */
    @Test
    public void testGetModelIntentionId() {
        System.out.println("getModelIntentionId");
        AgreedPatients instance = new AgreedPatients();
        int expResult = 0;
        int result = instance.getModelIntentionId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setModelIntentionId method, of class AgreedPatients.
     */
    @Test
    public void testSetModelIntentionId() {
        System.out.println("setModelIntentionId");
        int miId = 0;
        AgreedPatients instance = new AgreedPatients();
        instance.setModelIntentionId(miId);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class AgreedPatients.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object object = null;
        AgreedPatients instance = new AgreedPatients();
        boolean expResult = false;
        boolean result = instance.equals(object);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class AgreedPatients.
     */
    @Test
    public void testAgreedPatientsHashCodeWithNullIdButDate() {
        System.out.println("testAgreedPatientsHashCodeWithNullIdButDate");
        AgreedPatients instance = new AgreedPatients();
        instance.setId(null);
        Date d = new Date();
        instance.setPatientsFrom(d);
        instance.setPatientsTo(d);
        AgreedPatients other = new AgreedPatients();
        other.setId(null);
        other.setPatientsFrom(d);
        other.setPatientsTo(d);
        assertEquals(instance.hashCode(), other.hashCode());
    }
    
    @Test
    public void testAgreedPatientsHashCodeWithId() {
        System.out.println("testAgreedPatientsHashCodeWithId");
        AgreedPatients instance = new AgreedPatients();
        instance.setId(4711);
        AgreedPatients other = new AgreedPatients();
        other.setId(4711);
        assertEquals(instance.hashCode(), other.hashCode());
    }
    
    @Test
    public void testAgreedPatientsEqualsWithNullId() {
        System.out.println("testAgreedPatientsEqualsWithNullId");
        AgreedPatients instance = new AgreedPatients();
        instance.setId(null);
        Date d = new Date();
        instance.setPatientsTo(d);
        instance.setPatientsFrom(d);
        AgreedPatients other = new AgreedPatients();
        other.setId(null);
        other.setPatientsFrom(d);
        other.setPatientsTo(d);
        assertTrue(instance.equals(other));
    }
    
    @Test
    public void testAgreedPatientsEqualsWithId() {
        System.out.println("testAgreedPatientsHashCodeWithId");
        AgreedPatients instance = new AgreedPatients();
        instance.setId(null);
        Date d = new Date();
        instance.setPatientsFrom(d);
        instance.setPatientsTo(d);
        AgreedPatients other = new AgreedPatients();
        other.setId(null);
        other.setPatientsFrom(d);
        other.setPatientsTo(d);
        assertTrue(instance.equals(other));
    }

    /**
     * Test of toString method, of class AgreedPatients.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        AgreedPatients instance = new AgreedPatients();
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
