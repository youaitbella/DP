/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.inek.dataportal.psy.entities.modelintention;

import org.inek.dataportal.psy.modelintention.entities.AgreedPatients;
import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author vohldo
 */
public class AgreedPatientsTest {
    
    public AgreedPatientsTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }


    /**
     * Test of hashCode method, of class AgreedPatients.
     */
    @Test
    public void testAgreedPatientsHashCodeWithNullIdButDate() {
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
        AgreedPatients instance = new AgreedPatients();
        instance.setId(4711);
        AgreedPatients other = new AgreedPatients();
        other.setId(4711);
        assertEquals(instance.hashCode(), other.hashCode());
    }
    
    @Test
    public void testAgreedPatientsEqualsWithNullId() {
        AgreedPatients instance = new AgreedPatients();
        instance.setId(null);
        Date d = new Date();
        instance.setPatientsTo(d);
        instance.setPatientsFrom(d);
        AgreedPatients other = new AgreedPatients();
        other.setId(null);
        other.setPatientsFrom(d);
        other.setPatientsTo(d);
        assertEquals(instance, other);
    }
    
    @Test
    public void testAgreedPatientsEqualsWithId() {
        AgreedPatients instance = new AgreedPatients();
        instance.setId(null);
        Date d = new Date();
        instance.setPatientsFrom(d);
        instance.setPatientsTo(d);
        AgreedPatients other = new AgreedPatients();
        other.setId(null);
        other.setPatientsFrom(d);
        other.setPatientsTo(d);
        assertEquals(instance, other);
    }

}
