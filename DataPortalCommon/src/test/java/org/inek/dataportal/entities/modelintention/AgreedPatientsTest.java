/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.entities.modelintention;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

}
